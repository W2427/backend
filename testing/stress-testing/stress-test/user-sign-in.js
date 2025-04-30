/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const config = require('../config');
const parameters = config['parameters'];
const logger = require('../utils/logger');
const userModel = require('../model/user-model');
const HttpClient = require('../utils/http');

/**
 * 并发执行用户登录。
 * @param {{username, email, mobile}[]} users
 * @returns {Promise<any>}
 */
const test = users => {

  const requests = [];

  users.forEach(user => {
    requests.push(
      (new HttpClient(config['hosts']['auth']))
        .post('/authorizations')
        .body({
          username: [ user.username, user.email, user.mobile ][Math.floor(Math.random() * 3)],
          password: parameters.password
        })
    );
  });

  return new Promise(resolve => {

    const progress = logger.progress(
      `测试用户账号数：${users.length}`,
      users.length,
      () => resolve()
    );

    requests.forEach(req => {
      progress.send(1, req);
      req.send().then(res => progress.tick(1, res));
    });

  });

};

/**
 * 并发执行用户登录。
 * @returns {Promise<void>}
 */
module.exports = async () => {

  logger.begin('用户并发登录测试开始');

  logger.debug('取得用户账号信息');

  const userCounts = parameters['concurrency']['userSignIn'];

  let users = await userModel.findByIdLike(Math.max(... userCounts));

  for (let userCount of userCounts) {
    await test(users.slice(0, userCount));
  }

  logger.end('用户并发登录测试结束');
};
