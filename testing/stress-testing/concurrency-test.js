/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

(async () => {

  await require('./stress-test/user-sign-in')();

  process.exit();

})();
