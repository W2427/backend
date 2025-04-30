/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

/**
 * 测试数据 ID 前缀。
 * @type {string}
 */
exports.PREFIX = 'ZST';

let serialNo = 0;

/**
 * 取得下一个序列号。
 * @returns {number}
 */
const nextNo = () => {
  serialNo = serialNo % 10000;
  return serialNo++;
};

/**
 * 生成十六位三十六进制的 Unique ID。
 * @returns {string}
 */
exports.generate = () =>
  exports.PREFIX
  + (
    '0'.repeat(13)
    + (
      BigInt(Date.now()) * 100000000n
      + BigInt(Math.round(Math.random() * 9999)) * 10000n
      + BigInt(nextNo())
    )
      .toString(36)
      .toUpperCase()
  ).slice(-13);
