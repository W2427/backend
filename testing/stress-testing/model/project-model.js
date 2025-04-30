/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const tasks = require('../data/dbs').tasks;
const sql = require('../sql');
const uid36 = require('../utils/uid36');

/**
 * 取得测试项目信息。
 * @returns {Promise.<{id, company_id, org_id}[]>}
 */
exports.findByIdLike = () => tasks.query(
  sql['ose-tasks.projects.find-by-id-like'],
  `${uid36.PREFIX}%`
);
