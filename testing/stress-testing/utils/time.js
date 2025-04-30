/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const SECOND = 1000;
const MINUTE = 60 * SECOND;
const HOUR = 60 * MINUTE;

/**
 * 将毫秒数格式化为 HH:mm:ss.SS 格式。
 * @param elapsed 毫秒数
 * @returns {string}
 */
exports.elapsed = elapsed => {

  elapsed = Math.round(elapsed);

  if (elapsed < 1000) {
    return (elapsed / 1000).toFixed(3);
  }

  let hours = Math.floor(elapsed / HOUR);
  let minutes = Math.floor(elapsed / MINUTE) % 60;
  let seconds = Math.floor(elapsed / SECOND) % 60;
  let millisecs = Math.floor((elapsed % SECOND) * 100 / 1000);

  let result = [];

  hours && result.push(hours);
  (hours || minutes) && result.push(('0' + minutes).slice(-2));
  (hours || minutes || seconds) && result.push(('0' + seconds).slice(-2));

  result = result.join(':') || '0';

  return `${result}.${('0' + millisecs).slice(-2)}`;
};
