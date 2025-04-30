/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const bcrypt = require('bcrypt');
const params = require('../config')['parameters'];
const uid36 = require('../utils/uid36');
const logger = require('../utils/logger');
const auth = require('./dbs').auth;
const sql = require('../sql');

/**
 * 生成测试用户数据。
 * @param context
 * @returns {Promise<void>}
 */
module.exports = async (context) => {

  logger.begin('生成测试用户数据开始');

  logger.debug('生成测试用户登录密码 ...');

  const password = bcrypt.hashSync(params.password, bcrypt.genSaltSync(12, 'a'));
  const userCount = params['users'];
  const progress = logger.progress('创建测试用户账号信息 ...', userCount);

  await sql.insert(
    auth,
    sql['ose-auth.users.create'],
    userCount,
    index => {

      const userId = uid36.generate();

      return [
        userId,
        'administrator',
        userId,
        userId,
        `${userId.toLowerCase()}@livebridge.com.cn`,
        13000000000 + parseInt(('0'.repeat(8) + index).slice(-9).split('').reverse().join('')),
        password,
        context.systemUser.id,
        context.systemUser.id,
        Date.now()
      ];

    },
    inserted => progress.tick(inserted)
  );

  logger.end('生成测试用户数据结束');

};
