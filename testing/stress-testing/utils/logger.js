/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

require('colors');
const moment = require('moment');
const ProgressBar = require('progress');
const TimeUtil = require('../utils/time');
const StringUtil = require('../utils/string');
const DataSizeUtil = require('../utils/data-size');
const is = require('../utils/is');

let previousNewline = false;

/**
 * 输出日志。
 * @param log 日志内容
 * @param [newlineFlag] 是否插入空行
 */
const exportLog = (log, newlineFlag) => {

  let beginNewline = '';
  let endNewline = '';

  if (!previousNewline && (newlineFlag & 0b10) === 0b10) {
    beginNewline = '\r\n';
  }

  if ((newlineFlag & 0b01) === 0b01) {
    endNewline = '\r\n';
    previousNewline = true;
  } else {
    previousNewline = false;
  }

  console.log(`${beginNewline}${`${moment().format('YYYY-MM-DD HH:mm:ss.SSS')}`.green} ${log}${endNewline}`);
};

/**
 * 输出标签开始日志。
 * @param label 标签内容
 */
exports.begin = label => exportLog(label.black.bold.bgYellow, 0b10);

/**
 * 输出标签结束日志。
 * @param label 标签内容
 */
exports.end = label => exportLog(label.black.bold.bgYellow, 0b01);

/**
 * 输出调试日志。
 * @param log 日志内容
 * @param [startAt] 开始时间
 * @returns {number} 当前时间
 */
exports.debug = (log, startAt = null) => {
  exportLog(log.yellow + (startAt ? ` 耗时：${TimeUtil.elapsed(Date.now() - startAt)}`.red : ''));
  return Date.now();
};

/**
 * 输出错误日志。
 * @param log 日志内容
 * @param throwError 是否抛出错误
 */
exports.error = (log, throwError = false) => {

  exportLog(log.red);

  if (throwError) {
    throw new Error(log);
  }

};

const PROGRESS_WIDTH = 50;
const PROGRESS_START_AT = Symbol.for('progress_start_at');
const PROGRESS_CURRENT = Symbol.for('progress_current');
const PROGRESS_TOTAL = Symbol.for('progress_total');
const PROGRESS_BAR = Symbol.for('progress_bar');
const PROGRESS_SENT = Symbol.for('progress_sent');
const PROGRESS_CALLBACK = Symbol.for('progress_callback');
const PROGRESS_RESULTS = Symbol.for('progress_results');
const DURATION_COLORS = ['cyan', 'green', 'yellow', 'magenta', 'red'];
const DURATION_LABELS = ['≤ 0.5xd', '≤ 1.0×d', '≤ 1.5×d', '≤ 2.0×d', '> 2.0×d'];
const PERCENTAGE_BLOCKS = 50;
const REQ_START_AT = Symbol.for('startAt');

/**
 * 输出统计信息。
 * @param {{
 *   startAt: number,
 *   finishAt: number,
 *   duration: number,
 *   durations: number[],
 *   bytesWritten: number,
 *   bytesRead: number,
 *   ok: number,
 *   clientError: number,
 *   serverError: number,
 *   firstError: string
 * }} results
 */
const statistics = results => {

  const durations = results.durations;

  durations.sort();

  const total = durations.length;

  let totalDuration = 0;

  // 计算总耗时
  durations.forEach(duration => {
    totalDuration += duration;
  });

  const averageDuration = totalDuration / total;
  const step = averageDuration * 0.5;
  const maxDuration = step * 5;
  const stages = [];

  let stage = {to: 0};

  while (stage.to < maxDuration) {

    stage = {
      from: stage.to,
      to: stage.to + step,
      count: 0
    };

    stages.push(stage);
  }

  stage.to = durations[total - 1] > stage.from ? durations[total - 1] : stage.to;

  // 设置各阶段请求的数量
  for (let i = 0; i < total; i++) {
    stages[Math.min(4, Math.floor(durations[i] / step))].count++;
  }

  let maxPercent = 0;

  // 设置各阶段请求数的百分比
  stages.forEach(stage => {
    stage.percent = stage.count / total;
    maxPercent = Math.max(stage.percent, maxPercent);
  });

  const length = Math.ceil(Math.log10(total));

  const averageDurationText = TimeUtil.elapsed(averageDuration);

  console.log(
    '　　总耗时：'.cyan
    + `${StringUtil.align(TimeUtil.elapsed(results.duration), 11, 22)}`.blue
    + '　发送数据：'.cyan
    + `${StringUtil.align(DataSizeUtil.bytes(results.bytesWritten), 14, 22)}`.blue
    + '　成功件数：'.cyan
    + `${StringUtil.align(results.ok, 8, 22)}\n`.blue
    + '　累计耗时：'.cyan
    + `${StringUtil.align(TimeUtil.elapsed(totalDuration), 11, 22)}`.blue
    + '　接收数据：'.cyan
    + `${StringUtil.align(DataSizeUtil.bytes(results.bytesRead), 14, 22)}`.blue
    + '客户端错误：'.cyan
    + `${StringUtil.align(results.clientError, 8, 22)}\n`.blue
    + '　平均耗时：'.cyan
    + `${StringUtil.align(averageDurationText + ' (d)', averageDurationText.match(/\.\d{3}$/) ? 16 : 15, 22).bold}`.green
    + '　平均速率：'.cyan
    + `${StringUtil.align(DataSizeUtil.bits((results.bytesWritten + results.bytesRead) / (results.duration / 1000)) + '/s', 16, 22)}`.blue
    + '服务器错误：'.cyan
    + `${StringUtil.align(results.serverError, 8, 22)}\n`.blue
    + (results.firstError ? `　错误消息：${results.firstError}\n`.red : '')
    + '　分布统计：'.cyan
  );

  stages.forEach((stage, index) => {

    //const rangePercent = Math.min(stage.to / maxDuration, 1);
    //const rangeBlocks = Math.round(PERCENTAGE_BLOCKS * rangePercent);
    const rangeColor = DURATION_COLORS[index];
    const percentageBlocks = Math.min(PERCENTAGE_BLOCKS, Math.round(PERCENTAGE_BLOCKS * (stage.percent / maxPercent)));
    const percentageColor = stage.count === 0 ? 'dim' : 'cyan';

    // 打印统计信息
    console.log(
      `　　[${DURATION_LABELS[index]}] `[rangeColor].bold
      // 耗时阶段的范围
      + `${(' '.repeat(4) + TimeUtil.elapsed(stage.from)).slice(-8)} ~ ${(' '.repeat(4) + TimeUtil.elapsed(stage.to)).slice(-8)} `[rangeColor]
      // 耗时阶段的范围与最大耗时时长的比例
      //+ `${'░'.repeat(PERCENTAGE_BLOCKS - rangeBlocks)}${'█'.repeat(rangeBlocks)} `[DURATION_COLORS[index]]
      // 耗时阶段请求数与最大请求数的比例
      + `${'█'.repeat(percentageBlocks)}${'░'.repeat(PERCENTAGE_BLOCKS - percentageBlocks)} `[percentageColor]
      // 耗时阶段请求数占比
      + `${('  ' + (Math.round(stage.percent * 1000) / 10).toFixed(1)).slice(-5)}% (${(' '.repeat(length) + stage.count).slice(-1 * length)})`[percentageColor]
    );

  });

};

/**
 * 进度条包装类。
 */
class ProgressBarWrapper {

  /**
   * 构造函数。
   * @param {string} title
   * @param {number} total
   * @param {function} callback
   */
  constructor(title, total, callback) {

    exports.debug(title);

    const startAt = Date.now();

    this[PROGRESS_START_AT] = startAt;
    this[PROGRESS_CURRENT] = 0;
    this[PROGRESS_TOTAL] = total;
    this[PROGRESS_SENT] = 0;

    const results = this[PROGRESS_RESULTS] = {
      startAt: startAt,
      finishAt: 0,
      duration: 0,
      durations: [],
      http: false,
      bytesWritten: 0,
      bytesRead: 0,
      ok: 0,
      clientError: 0,
      serverError: 0,
      firstError: null
    };

    this[PROGRESS_CALLBACK] = () => {

      if (results.http) {
        results.duration = results.finishAt - results.startAt;
        statistics(results);
      } else {
        exports.debug('完成', startAt);
      }

      is.function(callback) && callback(results);
    };

    this[PROGRESS_BAR] = new ProgressBar('　:bar :percent :CURRENT:ELAPSED'.cyan, {
      total,
      width: PROGRESS_WIDTH,
      complete: '█',
      incomplete: '░',
      callback: this[PROGRESS_CALLBACK]
    });

  }

  /**
   * 设置错误消息。
   * @param {ServerResponse} res
   */
  setError(res) {

    this[PROGRESS_RESULTS].firstError = ' ';

    let chunk = Buffer.from('');

    res.on('data', buffer => {
      chunk = Buffer.concat([chunk, Buffer.from(buffer)]);
    });

    res.on('end', () => {
      this[PROGRESS_RESULTS].firstError = chunk.toString('utf8');
    });

  }

  /**
   * 标记为请求已发送。
   * @param {number} count
   * @param {ClientRequest} [req]
   */
  send(count, req) {
    this[PROGRESS_SENT] += count;
  }

  /**
   * 更新进度。
   * @param {number} [count]
   * @param {ServerResponse} [res]
   */
  tick(count, res) {

    this[PROGRESS_CURRENT] += count;

    const now = Date.now();
    const elapsed = now - this[PROGRESS_START_AT];
    const average = elapsed / this[PROGRESS_CURRENT];
    const eta = (this[PROGRESS_TOTAL] - this[PROGRESS_CURRENT]) * average;

    // 更新响应结果统计
    if (res) {

      const results = this[PROGRESS_RESULTS];

      results.http = true;
      results.startAt = Math.min(results.startAt, res[REQ_START_AT]);
      results.finishAt = Math.max(results.startAt, now);
      results.durations.push(now - res[REQ_START_AT]);
      results.bytesWritten += res.socket.bytesWritten;
      results.bytesRead += res.socket.bytesRead;

      switch (Math.floor(res.statusCode / 100)) {
        case 2:
          results.ok++;
          break;
        case 4:
          results.clientError++;
          !results.firstError && this.setError(res);
          break;
        case 5:
          results.serverError++;
          !results.firstError && this.setError(res);
          break;
      }

      this[PROGRESS_BAR].tick(count, {
        CURRENT: ' 已完成/已发送/总计 '.black.bold.bgCyan + ` ${this[PROGRESS_CURRENT]}/${this[PROGRESS_SENT]}/${this[PROGRESS_TOTAL]} `.cyan,
        ELAPSED: ' 已过/剩余 '.black.bold.bgCyan + ` ${TimeUtil.elapsed(elapsed)}/${TimeUtil.elapsed(eta)} `.cyan
      });

    // 更新进度信息
    } else {
      this[PROGRESS_BAR].tick(count, {
        CURRENT: ' 已完成/总计 '.black.bold.bgCyan + ` ${this[PROGRESS_CURRENT]}/${this[PROGRESS_TOTAL]} `.cyan,
        ELAPSED: ' 已过/剩余 '.black.bold.bgCyan + ` ${TimeUtil.elapsed(elapsed)}/${TimeUtil.elapsed(eta)} `.cyan
      });
    }

  }

}

/**
 * 初始化进度条。
 * @param title 标题
 * @param total 总数
 * @param [callback] 回调函数
 * @returns {ProgressBarWrapper}
 */
exports.progress = (title, total, callback) => new ProgressBarWrapper(title, total, callback);
