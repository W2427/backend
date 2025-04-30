/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

/**
 * 复制对象。
 * @param object
 * @returns {object}
 */
exports.clone = object => JSON.parse(JSON.stringify(object));

/**
 * 合并对象。
 * @param destination
 * @param sources
 * @returns {*}
 */
exports.merge = (destination, ... sources) => {

  !!sources && sources.forEach(source => {
    Object.keys(source).forEach(key => {
      destination[key] = source[key];
    });
  });

  return destination;
};
