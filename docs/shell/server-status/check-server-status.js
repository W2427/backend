#!/usr/bin/env node
'use strict';

const spawn = require('child_process').spawnSync;
const serverConfig = require('./servers.json');
const thresholds = serverConfig.thresholds;
const receivers = serverConfig.receivers;
const servers = serverConfig.servers;
const fs = require('fs');
const path = require('path');
const os = require('os');
const crypto = require('crypto');
const USERNAME = serverConfig.user;
const SEND_MAIL = path.join(__dirname, 'send-mail.sh');
const HASH_FILE = path.join(__dirname, 'mail-content-hash');
const MAIL_CONTENT = path.join(__dirname, `mail-content-${os.hostname()}`);
const DFS_PATH = '/mnt/mfs/scripts';
const KB = 1024;
const MB = KB * KB;
const DEBUG_MODE = (process.argv.indexOf('--debug') >= 0);

let sendMail = false;
let digestKeywords = [(new Date()).toLocaleDateString()];
let mailContent = `检查时间：${(new Date()).toLocaleString()}\r\n执行服务器：${os.hostname()}\r\n`;

if (!DEBUG_MODE) {
  console.log('提示：通过 --debug 参数可以查看调试信息');
}

const debug = text => {
  DEBUG_MODE && console.log(`[${(new Date()).toLocaleString()}] ${text}`);
};

/**
 * 解析标准输出流中的 JSON 数据。
 * @param {Buffer} buffer
 * @returns {Object}
 */
const parseJSON = buffer => {
  if (!buffer.length) {
    return null;
  }
  try {
    return JSON.parse(buffer.toString('utf8').replace(/^[^{\[]+({.*}|\[.*])[^}\]]+$/g, '$1'));
  } catch (e) {
    return null;
  }
};

/**
 * 格式化数字。
 * @param {Number} number
 * @returns {String}
 */
const formatNumber = number => {
  let string = number.toFixed(2);
  if (string.length < 10) {
    string = ('      ' + string).slice(-10);
  }
  return string;
};

/**
 * 执行 Shell 命令。
 * @param {String} command
 * @param {[String|Number]} parameters
 * @param {Function} callback
 */
const execCommand = (command, parameters, callback) => {
  const childProcess = spawn(command, parameters);
  if (childProcess.stderr.length) {
    throw `命令（${command}${(parameters || []).length === 0 ? '' : ` ${parameters.join(' ')}`}）执行错误：${childProcess.stderr.toString('utf8').replace(/[\r\n]/g, ' ')}`;
  }
  callback(childProcess.stdout.toString('utf8'));
};

servers.forEach(server => {
  debug(`---------------- 服务器 ${server.host} 状态检查 ----------------`);

  const hostname = `${USERNAME}@${server.host}`;
  const errors = [];

  try {
    // 检查目标服务器是否可连接
    execCommand(
      'ping',
      ['-c', 5, server.host],
      stdout => {
        const packetLoss = parseFloat((stdout.replace(/[\r\n]/g, '').match(/(\d+(\.\d+)?)% packet loss/) || [])[1]);
        if (packetLoss === 100 || isNaN(packetLoss)) {
          digestKeywords.push(`${server.host}::PING`);
          throw '无法连接到该服务器';
        } else if (packetLoss > 0) {
          digestKeywords.push(`${server.host}::PING`);
          throw '与该服务器的连接不稳定';
        }
      }
    );

    // 检查目标服务器的磁盘使用状况
    execCommand(
      'ssh',
      [hostname, 'df'],
      stdout => {
        stdout.split(/[\r\n]+/).forEach(line => {
          if (!line.match(/^\//)) {
            return;
          }
          const values = line.split(/\s+/);
          const disk = values[0];
          const used = parseInt(values[2]);
          const available = parseInt(values[3]);
          const diskAvailablePercent = available * 100 / (used + available);
          const availableMB = available / KB;
          const totalMB = (used + available) / KB;
          if (availableMB < thresholds.freeDiskMB && diskAvailablePercent < thresholds.freeDiskPercent) {
            digestKeywords.push(`${server.host}::DISK_USAGE`);
            errors.push(`磁盘 ${disk} 可用空间（${availableMB.toFixed(2)} MB, ${diskAvailablePercent.toFixed(2)}%）低于设定的下限（${thresholds.freeDiskMB} MB，${thresholds.freeDiskPercent}%）`);
          }
          debug(`Disk ${disk} ${formatNumber(availableMB)} / ${formatNumber(totalMB)} MB ${formatNumber(diskAvailablePercent)}%`);
        });
      }
    );

    // 检查目标服务器是否已挂载分布式文件系统
    execCommand(
      'ssh',
      [hostname, `. ~/.bash_profile && node -e "try{console.log(JSON.stringify(fs.statSync('${DFS_PATH}')));}catch(e){console.log(JSON.stringify(e));}"`],
      stdout => {
        const result = parseJSON(stdout);
        if (result && (result.code === 'ENOENT')) {
          digestKeywords.push(`${server.host}::DFS_MOUNT`);
          errors.push('尚未挂载分布式文件系统');
        }
      }
    );

    // 检查目标服务器内存使用状况
    execCommand(
      'ssh',
      [hostname, 'free -b'],
      stdout => {
        const values = (stdout.split(/[\r\n]+/)[1] || '').match(/\d+/g);
        if (!values || values.length !== 6) {
          return;
        }
        const freeMB = parseInt(values[5]) / MB;
        const freePercent = freeMB * 100 / (parseInt(values[0]) / MB);
        if (freeMB < thresholds.freeMemoryMB && freePercent < thresholds.freeMemoryPercent) {
          digestKeywords.push(`${server.host}::FREE_MEM`);
          errors.push(`剩余内存（${freeMB.toFixed(2)} MB, ${freePercent.toFixed(2)}%）低于设定的下限（${thresholds.freeMemoryMB} MB，${thresholds.freeMemoryPercent}%）`);
        }
        debug(`RAM ${formatNumber(freeMB)} MB ${formatNumber(freePercent)}% free`);
      }
    );

    // 检查目标服务器处理器的使用状况
    execCommand(
      'ssh',
      [hostname, '. ~/.bash_profile && node -e "console.log(JSON.stringify(os.cpus()))"'],
      stdout => {
        const result = parseJSON(stdout);
        if (result instanceof Array) {
          let idle = 0;
          let total = 0;
          result.forEach(cpu => {
            idle += (cpu.times.idle || 0);
            Object.keys(cpu.times).forEach(key => {
              total += (cpu.times[key] || 0);
            });
          });
          let idlePercent = idle * 100 / total;
          if (idlePercent < thresholds.cpuIdlePercent) {
            digestKeywords.push(`${server.host}::IDLE_CPU`);
            errors.push(`CPU 空闲率（${idlePercent.toFixed(2)}%）低于设定的下限（${thresholds.cpuIdlePercent}%）`);
          }
          debug(`CPU ${formatNumber(idlePercent)}% idle`);
        }
      }
    );

    // 检查目标服务器上的服务是否正在运行
    if (server.services && server.services.length > 0) {
      execCommand(
        'ssh',
        [hostname, 'lsof -i -P -n | grep LISTEN'],
        stdout => {
          const ports = [];

          (stdout.match(/:\d+ \(LISTEN\)/g) || []).forEach(port => {
            port = port.match(/(\d+)/)[1];
            ports.indexOf(port) < 0 && ports.push(port);
          });

          server.services.forEach(service => {
            if (ports.indexOf('' + service.port) < 0) {
              digestKeywords.push(`${server.host}::SERVICE_NA::${service.port}`);
              errors.push(`服务 ${service.desc}:${service.port} 未启动`);
              debug(`service ${service.desc}:${service.port} not available`);
            } else {
              debug(`service ${service.desc}:${service.port} available`);
            }
          });
        }
      );
    }
  } catch (e) {
    errors.push(e);
  }

  if (errors.length) {
    sendMail = true;
    mailContent += [`\r\n服务器【${server.host}】：`].concat(errors).join('\r\n  • ') + '\r\n';
  }
});

// 若存在资源不足的服务器则发送通知邮件
if (sendMail) {
  debug('---------------- 发送通知邮件 ----------------');
  DEBUG_MODE && console.log(mailContent);

  let currentHash = crypto.createHash('md5').update(digestKeywords.join('\r\n')).digest('hex').toUpperCase();
  let previousHash = null;

  // 读取已发送的邮件的摘要
  try {
    previousHash = fs.readFileSync(HASH_FILE, 'utf8');
  } catch (e) {
  }

  // 若邮件内容摘要不同则发送邮件
  if (currentHash !== previousHash) {
    fs.writeFileSync(HASH_FILE, currentHash, 'utf8');
    fs.writeFileSync(MAIL_CONTENT, mailContent, 'utf8');
    receivers.forEach(receiver => spawn(SEND_MAIL, [MAIL_CONTENT, receiver]));
    debug('---------------- 结束（邮件已发送） ----------------');
  } else {
    debug('---------------- 结束（邮件已忽略） ----------------');
  }
} else {
  debug('---------------- 结束（无异常） ----------------');
}

process.exit();
