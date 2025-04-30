#!/usr/bin/env node
'use strict';

const fs = require('fs');
const path = require('path');
const spawn = require('child_process').spawn;
const DFS_MOUNT_PATH = '/mnt/mfs/ose-jars';
const BASE_DIR = '/var/ose';
const LOCK_FILE_PATH = path.join(BASE_DIR, '.lock-starting');
const LOG_FILE_PATH = path.join(BASE_DIR, 'ose-daemon.log');
const OSE_FILENAME_PATTERN = /^(ose-(\w+))(-(\d\.\d\.\d-SNAPSHOT))?\.(jar|pid)$/;

/**
 * 执行 Shell 命令。
 * @param command 命令名
 * @param args    命令参数
 * @returns {Promise<*>}
 */
const execCommand = async (command, args) => {
  return new Promise((resolve, reject) => {
    let child = spawn(command, args);
    let result = new Buffer([]);
    child.stdout.on('data', buffer => (result = Buffer.concat([result, buffer])));
    child.stdout.on('end', () => resolve(result));
    let error = new Buffer([]);
    child.stderr.on('data', buffer => (error = Buffer.concat([error, buffer])));
    child.stderr.on('end', () => (error.length > 0 && reject(error)));
  });
};

/**
 * 根据进程 ID 判断进程是否存在。
 * @param pid 进程 ID
 * @returns {Promise<void>}
 */
const existsByPID = async pid => {
  return (await execCommand('ps', ['-p', pid, '-o', 'pid']))
    .toString()
    .match(/PID\r?\n\s*\d+/);
};

/**
 * 启动服务。
 * @param serviceCodes 服务代码
 * @returns {Promise<void>}
 */
const startServices = async serviceCodes => {
  return await execCommand(path.join(BASE_DIR, 'ose.sh'), ['start', ...serviceCodes]);
};

/**
 * 写入日志文件。
 * @param code 日志代码
 * @param text 日志内容
 */
const writeLog = (code, text) => {
  code = (code + ' '.repeat(8)).slice(0, 8);
  fs.writeFileSync(
    LOG_FILE_PATH,
    `[${(new Date()).toISOString()}] ${code} ${text}\r\n`,
    {flag: 'a'}
  );
};

(async () => {

  // 若尚未挂载分布式文件系统则结束
  try {
    if (!fs.statSync(DFS_MOUNT_PATH).isDirectory()) {
      writeLog('ERROR', `${DFS_MOUNT_PATH} is not a directory`);
      process.exit();
      return;
    }
  } catch (e) {
    if (e.code === 'ENOENT') {
      writeLog('ERROR', 'DFS not mounted');
      process.exit();
      return;
    }
  }

  // 若之前的定时任务尚未执行完成则结束
  try {
    if (fs.statSync(LOCK_FILE_PATH).ctimeMs > Date.now() - 600000) {
      process.exit();
      return;
    }
  } catch (e) {
    if (e.code !== 'ENOENT') {
      writeLog('ERROR', e.message);
      process.exit();
      return;
    }
  }

  // 创建排他锁文件
  fs.writeFileSync(LOCK_FILE_PATH, Date.now(), {flag: 'w'});

  let filenames = fs.readdirSync(BASE_DIR);
  let matched;
  let serviceName;
  let serviceNames = [];
  let services = {};
  let service;

  // 取得所有服务的 JAR 和 PID 文件名
  for (let filename of filenames) {

    matched = filename.match(OSE_FILENAME_PATTERN);

    if (!matched) {
      continue;
    }

    serviceName = matched[1];
    serviceNames.push(serviceName);
    service = services[serviceName] = services[serviceName] || {};
    service.code = matched[2];

    switch (matched[5]) {
      case 'jar':
        service.jar = filename;
        service.updatedAt = fs.statSync(path.join(BASE_DIR, filename)).mtimeMs;
        break;
      case 'pid':
        service.pid = filename;
        service.startedAt = fs.statSync(path.join(BASE_DIR, filename)).mtimeMs;
        break;
    }
  }

  let pidFileName;
  let jarFileName;
  let serviceCodes = [];

  // 遍历每一个服务
  for (serviceName of serviceNames) {

    service = services[serviceName];
    pidFileName = service.pid;
    jarFileName = service.jar;

    // 若 JAR 文件不存在，或者存在 PID 文件且服务已启动，则继续下一个服务的判断
    if (!service.updatedAt
        || (!!service.startedAt
            && existsByPID(fs.readFileSync(path.join(BASE_DIR, pidFileName))))) {
      continue;
    }

    // 将服务添加到待启动列表
    serviceCodes.push(service.code);
  }

  // 启动服务
  if (serviceCodes.length > 0) {
    writeLog('START', serviceCodes.join(', '));
    await startServices(serviceCodes);
    writeLog('STARTED', serviceCodes.join(', '));
  }

  // 删除排他锁文件
  fs.unlinkSync(LOCK_FILE_PATH);

  process.exit();
})();
