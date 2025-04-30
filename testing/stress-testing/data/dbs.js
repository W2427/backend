/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const mysql = require('promise-mysql');
const config = require('../config')['mysql'];
const misc = require('../utils/misc');

/**
 * 用户认证子系统数据库连接池。
 * @type {pool}
 */
exports.auth = mysql.createPool(misc.merge({}, config, {database: 'ose_auth'}));

/**
 * 任务管理子系统数据库连接池。
 * @type {pool}
 */
exports.tasks = mysql.createPool(misc.merge({}, config, {database: 'ose_tasks'}));

