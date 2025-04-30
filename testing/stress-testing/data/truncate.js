/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const UID36 = require('../utils/uid36');
const logger = require('../utils/logger');
const dbs = require('./dbs');
const sql = require('../sql');

/**
 * 删除测试数据。
 * @returns {Promise<void>}
 */
module.exports = async () => {

  logger.begin('测试数据清理开始');

  let result;

  /* 清理层级结构数据 */

  logger.debug('清理【层级节点】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.hierarchy-nodes.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【层级节点】信息`);

  logger.debug('清理【项目节点】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.project-nodes.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【项目节点】信息`);

  /* 清理实体数据 */

  logger.debug('清理【焊口实体】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.entity-welds.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【焊口实体】信息`);

  logger.debug('清理【管段实体】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.entity-pipe-pieces.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【管段实体】信息`);

  logger.debug('清理【单管实体】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.entity-spools.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【单管实体】信息`);

  logger.debug('清理【管线实体】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.entity-isos.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【管线实体】信息`);

  /* 清理项目数据 */

  logger.debug('清理【实体类型-工序关系】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.entity-process-relations.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【实体类型-工序关系】信息`);

  logger.debug('清理【实体子类型】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.entity-sub-types.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【实体子类型】信息`);

  logger.debug('清理【实体类型】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.entity-types.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【实体类型】信息`);

  logger.debug('清理【工序】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.processes.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【工序】信息`);

  logger.debug('清理【工序分类】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.process-categories.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【工序分类】信息`);

  logger.debug('清理【工序阶段】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.process-stages.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【工序阶段】信息`);

  logger.debug('清理【项目】信息 ...');
  result = await dbs.tasks.query(sql['ose-tasks.projects.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【项目】信息`);

  /* 清理组织数据 */

  logger.debug('清理【用户角色关系】信息 ...');
  result = await dbs.auth.query(sql['ose-auth.user-role-relations.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【用户角色关系】信息`);

  logger.debug('清理【角色】信息 ...');
  result = await dbs.auth.query(sql['ose-auth.roles.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【角色】信息`);

  logger.debug('清理【成员关系】信息 ...');
  result = await dbs.auth.query(sql['ose-auth.user-org-relations.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件组织【成员关系】信息`);

  logger.debug('清理【组织及公司】信息 ...');
  result = await dbs.auth.query(sql['ose-auth.organizations.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件【组织及公司】信息`);

  /* 清理用户数据 */

  logger.debug('清理用户账号信息 ...');
  result = await dbs.auth.query(sql['ose-auth.users.delete-by-id-like'], `${UID36.PREFIX}%`);
  logger.debug(`已清理 ${result['affectedRows']} 件用户账号信息`);

  logger.end('测试数据清理结束');

};
