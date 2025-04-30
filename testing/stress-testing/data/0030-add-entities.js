/*!
 *  OSE 系统压力测试。
 * @date   2018-11-09
 * @author jinhy
 * @copy   livebridge.com.cn
 */
'use strict';

const params = require('../config')['parameters'];
const uid36 = require('../utils/uid36');
const logger = require('../utils/logger');
const dbs = require('./dbs');
const sql = require('../sql');
const projectModel = require('../model/project-model');

/**
 * 创建层级节点及其相关信息。
 * @param {{systemUser: {id}}} context
 * @param {{id, company_id, org_id}} project
 * @param {{id, module_type, path, depth, hierarchy_type}} parent
 * @param {string} nodeType
 * @param {string} [entityType]
 * @param {string} [entitySubType]
 * @returns {Promise<object>}
 */
const createHierarchyNode = async (context, project, parent, nodeType, entityType, entitySubType) => {

  const node = {};

  node['node_id'] = uid36.generate();
  node['id'] = uid36.generate();

  if (!parent) {

    node['module_type'] = node['module_type'] || null;
    node['parent_id'] = null;
    node['path'] = '/';
    node['depth'] = 0;
    node['hierarchy_type'] = "PIPING";

  } else {

    node['module_type'] = node['module_type'] || parent['module_type'];
    node['parent_id'] = parent['id'];
    node['path'] = `${parent['path']}${parent['id']}/`;
    node['depth'] = parent['depth'] + 1;

    if (entityType && entityType === 'ISO') {

    } else if (
      nodeType === 'LAYER_PACKAGE'
      || nodeType === 'PRESSURE'
    ) {

    }

  }

  await dbs.tasks.query(
    sql['ose-tasks.project-nodes.insert-into'],
    [
      node['node_id'], // ID
      project['company_id'], // Company ID
      project['org_id'], // Organization ID
      project['id'], // Project ID
      node['module_type'], // Module Type
      // Hierarchy Node Type
      // Entity Type
      // Entity Sub Type
      // Entity Business Type
      // Entity ID
      // No.
      // Display Name
      context.systemUser.id, // Created By User ID
      context.systemUser.id, // Last Modified By User ID
      Date.now() // Version
    ]
  );

  await dbs.tasks.query(
    sql['ose-tasks.hierarchy-nodes.insert-into'],
    [
      node['id'], // ID
      project['company_id'], // Company ID
      project['org_id'], // Organization ID
      node['parent_id'], // Parent ID
      project['id'], // Project ID
      node['path'], // Hierarchy Path
      node['depth'], // Depth
      node['hierarchy_type'], // Hierarchy Type
      // Sort
      node['node_id'], // Project Node ID
      context.systemUser.id, // Created By User ID
      context.systemUser.id, // Last Modified By User ID
      Date.now() // Version
    ]
  );

  return node;
};

/**
 * 生成区域层级节点。
 * @param {object} context
 * @param {object} project
 * @returns {Promise<void>}
 */
const generateAreas = async (context, project) => {

  for (let i = 0; i < params['hierarchyNodes']['area']; i++) {


    await generateModules(project);
  }

};

/**
 * 生成模块层级节点。
 * @param {object} project
 * @returns {Promise<void>}
 */
const generateModules = async (project) => {

  for (let i = 0; i < params['hierarchyNodes']['module']; i++) {
    await generateISOs(project);
  }

};

/**
 * 生成管线层级节点。
 * @param {object} project
 * @returns {Promise<void>}
 */
const generateISOs = async (project) => {

  for (let i = 0; i < params['hierarchyNodes']['iso']; i++) {
    await generateSpools(project);
  }

};

/**
 * 生成单管层级节点。
 * @param {object} project
 * @returns {Promise<void>}
 */
const generateSpools = async (project) => {

  for (let i = 0; i < params['hierarchyNodes']['spool']; i++) {
    await generatePipePieces(project);
    await generateSBWs(project);
    await generateFBWs(project);
  }

};

/**
 * 生成管段层级节点。
 * @param {object} project
 * @returns {Promise<void>}
 */
const generatePipePieces = async (project) => {

  for (let i = 0; i < params['hierarchyNodes']['pipePiece']; i++) {
  }

};

/**
 * 生成车间焊口层级节点。
 * @param {object} project
 * @returns {Promise<void>}
 */
const generateSBWs = async (project) => {

  for (let i = 0; i < params['hierarchyNodes']['sbw']; i++) {
  }

};

/**
 * 生成现场焊口层级节点。
 * @param {object} project
 * @returns {Promise<void>}
 */
const generateFBWs = async (project) => {

  for (let i = 0; i < params['hierarchyNodes']['fbw']; i++) {
  }

};

/**
 * 生成组织测试数据。
 * @param context
 * @returns {Promise<void>}
 */
module.exports = async (context) => {

  logger.begin('测试实体/层级数据生成开始');

  logger.debug('取得项目信息 ...');

  const projects = await projectModel.findByIdLike();
  const projectCount = projects.length;

  for (let i = 0; i < projectCount; i++) {

    logger.begin(`目 ${i + 1}/${projectCount} 层级创建项开始`);

    await generateAreas(context, projects[i]);

    logger.begin(`目 ${i + 1}/${projectCount} 层级创建项结束`);
  }

  logger.end('测试实体/层级数据生成结束');

};
