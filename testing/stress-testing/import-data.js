/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const logger = require('./utils/logger');
const userModel = require('./model/user-model');

(async () => {

  logger.begin('压力测试数据导入处理开始');

  // 取得上下文信息
  const context = {
    systemUser: await userModel.getSystemUserInfo()
  };

  // 清理测试数据
  await require('./data/truncate')();

  // 添加测试数据
  await require('./data/0010-add-users')(context);
  await require('./data/0020-add-projects')(context);
  await require('./data/0030-add-entities')(context);

  logger.end('压力测试数据导入处理结束');

  // 结束进程
  process.exit();

})();
