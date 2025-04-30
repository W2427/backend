/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const logger = require('../utils/logger');
const auth = require('../data/dbs').auth;
const sql = require('../sql');
const uid36 = require('../utils/uid36');
const is = require('../utils/is');

/**
 * 取得系统用户信息。
 * @returns {Promise<RowDataPacket>}
 */
exports.getSystemUserInfo = async () => {

  logger.debug('取得系统用户信息 ...');

  let users = await auth.query(sql['ose-auth.users.find-system-user']);

  if (users === null || users.length === 0) {
    logger.error('系统用户信息不存在，请确认系统是否已完成初始化', true);
  }

  return users[0];
};

/**
 * 取得测试用户信息。
 * @param [skip] 跳过件数
 * @param limit 最大件数
 * @returns {Promise.<{username, email, mobile}[]>}
 */
exports.findByIdLike = (skip, limit) => auth.query(
  sql['ose-auth.users.find-by-id-like'],
  [
    `${uid36.PREFIX}%`,
    is.undefined(limit) ? 0 : skip,
    is.undefined(limit) ? skip : limit
  ]
);
