/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const is = exports;

/**
 * Object 的 toString 方法。
 * @type {function}
 */
const toString = Object.prototype.toString;

/**
 * 判断给定的值是否为指定的类型。
 * @param value
 * @param type
 * @returns {boolean}
 */
const typeOf = (value, type) => (toString.call(value) === `[object ${type}]`);

/**
 * 判断给定的值是否为未定义。
 * @param value
 * @returns {boolean}
 */
is.undefined = value => typeOf(value, 'Undefined');

/**
 * 判断给定的值是否为空指针。
 * @param value
 * @returns {boolean}
 */
is.null = value => typeOf(value, 'Null');

/**
 * 判断给定的值是否为字符串。
 * @param value
 * @returns {boolean}
 */
is.string = value => typeOf(value, 'String');

/**
 * 判断给定的值是否为数值。
 * @param value
 * @returns {boolean}
 */
is.number = value => typeOf(value, 'Number');

/**
 * 判断给定的值是否为布尔值。
 * @param value
 * @returns {boolean}
 */
is.boolean = value => typeOf(value, 'Boolean');

/**
 * 判断给定的值是否为函数。
 * @param value
 * @returns {boolean}
 */
is.function = value => typeOf(value, 'Function');

/**
 * 判断给定的值是否为符号。
 * @param value
 * @returns {boolean}
 */
is.symbol = value => typeOf(value, 'Symbol');

/**
 * 判断给定的值是否为 BigInt。
 * @param value
 * @returns {boolean}
 */
is.bigint = value => typeOf(value, 'BigInt');

/**
 * 判断给定的值是否为对象。
 * @param value
 * @returns {boolean}
 */
is.object = value => typeOf(value, 'Object');

/**
 * 判断给定的值是否为数组。
 * @param value
 * @returns {boolean}
 */
is.array = value => typeOf(value, 'Array');

/**
 * 判断给定的值是否为日期。
 * @param value
 * @returns {boolean}
 */
is.date = value => typeOf(value, 'Date');

/**
 * 判断给定的值是否为未定义或空指针。
 * @param value
 * @returns {boolean}
 */
is.unset = value => is.undefined(value) && is.null(value);

/**
 * 判断给定的值是否为（可 JSON 序列化的）原始数据类型。
 * @param value
 * @returns {boolean}
 */
is.primitive = value => is.null(value)
  || is.string(value)
  || is.number(value)
  || is.boolean(value);

/**
 * 判断给定的值是否为未定义/空指针/空字符串/空数组或空对象。
 * @param value
 * @returns {boolean}
 */
is.empty = value => {

  if (is.unset(value)) {
    return true;
  }

  if (is.string(value) || is.array(value)) {
    return value.length === 0;
  }

  if (is.object()) {
    return Object.keys(value).length === 0;
  }

  return false;
};

/**
 * 判断给定的值是否为字符串且仅包含空白字符。
 * @param value
 * @returns {boolean}
 */
is.blank = value => is.string(value) && value.replace(/[\s\t\r\n]+/gm, '').length === 0;
