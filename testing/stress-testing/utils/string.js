/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

/**
 * 左填充。
 * @param {*} text
 * @param {number} length
 * @param {string} [char]
 * @returns {string}
 */
exports.padLeft = (text, length, char = ' ') => (char.repeat(length) + text).slice(-1 * length);

/**
 * 右填充。
 * @param {*} text
 * @param {number} length
 * @param {string} [char]
 * @returns {string}
 */
exports.padRight = (text, length, char = ' ') => (text + char.repeat(length)).slice(0, length);

/**
 * 对齐文本。
 * @param {*} text
 * @param {number} left
 * @param {number} length
 * @returns {string}
 */
exports.align = (text, left, length) => exports.padRight(exports.padLeft(text, left), length);
