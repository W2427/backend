/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const path = require('path');
const fs = require('fs');
const logger = require('../utils/logger');
const is = require('../utils/is');

let counter = 0;

/**
 * 递归加载指定路径下的 SQL 文件的内容。
 * @param {string} [dir]
 * @param {string} [namespace]
 */
const loadSQL = (dir = __dirname, namespace = '') => {

  let files = fs.readdirSync(dir);

  let filepath, fragments, sql;

  for (let filename of files) {

    filepath = path.join(dir, filename);

    if (path.extname(filename) === '.sql') {

      filename = filename.slice(0, -4);

      fragments = filename.match(/^(.+)\.(insert-into|values)$/i);

      sql = fs
        .readFileSync(filepath, 'utf8')
        .replace(/\/\*(.|[\r\n])*\*\//gm, '')
        .replace(/--[^\r\n]+/g, '')
        .replace(/[\r\n\s\t]+/g, ' ');

      if (!fragments) {
        exports[`${namespace}${filename}`] = sql;
      } else {
        filename = fragments[1];
        exports[`${namespace}${filename}`] = exports[`${namespace}${filename}`] || {};
        exports[`${namespace}${filename}`][fragments[2].toLowerCase()] = sql;
      }


      counter++;

    } else if (fs.statSync(filepath).isDirectory()) {
      loadSQL(filepath, `${namespace}${filename}.`);
    }

  }

};

logger.begin('SQL 语句加载开始');

// 递归加载当前路径下的 SQL 文件的内容。
loadSQL();

logger.debug(`已加载 ${counter} 条 SQL 语句`);

logger.end('SQL 语句加载结束');

/**
 * 批量插入数据。
 * @param {object} connection 数据库连接实例
 * @param {object} sqlFragments SQL 片段
 * @param {number} totalCount 插入总件数
 * @param {function(index: number)} generateValues 值数组生成函数
 * @param {function(count: number)|number} [insertCallback] 插入完成回调函数
 * @param {number} [bufferSize] 缓冲大小
 */
exports.insert = async (connection, sqlFragments, totalCount, generateValues, insertCallback, bufferSize) => {

  if (is.number(insertCallback)) {
    bufferSize = insertCallback;
  }

  if (!is.function(insertCallback)) {
    insertCallback = () => {};
  }

  if (!is.number(bufferSize)) {
    bufferSize = Math.floor(totalCount / 100) || 1;
  }

  bufferSize = Math.min(bufferSize, 1000);

  let values = [];
  let statementCount = 0;

  for (let i = 0; i < totalCount; i++) {

    values = values.concat(generateValues(i));

    statementCount++;

    if (statementCount === bufferSize || (i + 1) >= totalCount) {

      try {
        await connection.query(`${sqlFragments['insert-into']} ${Array.apply(null, Array(statementCount)).map(() => sqlFragments['values']).join(',')};`, values);
      } catch (e) {
        // nothing to do
        console.log(e);
      }

      insertCallback(statementCount);

      values = [];

      statementCount = 0;
    }

  }

};
