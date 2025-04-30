/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const K = 1024;
const M = K * K;
const G = M * K;

/**
 * 单位转换。
 * @param {number} size
 * @param {string} unit
 * @returns {string}
 */
const convert = (size, unit) => {

  size = Math.round(size);

  if (size < K) {
    return `${size} ${unit}`;
  }

  if (size < M) {
    return `${(size / K).toFixed(2)} K${unit}`;
  }

  if (size < G) {
    return `${(size / M).toFixed(2)} M${unit}`;
  }

  return `${(size / G).toFixed(2)} G${unit}`;
};

/**
 * 将字节数格式为数据的字节大小。
 * @param bytes 字节数
 * @returns {string}
 */
exports.bytes = bytes => convert(bytes, 'B');

/**
 * 将字节数格式为数据的比特大小。
 * @param bytes 字节数
 * @returns {string}
 */
exports.bits = bytes => convert(bytes * 8, 'b');
