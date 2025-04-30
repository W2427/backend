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
const userModel = require('../model/user-model');

/**
 * 工序阶段列表。
 * @type {{
 *   name: string,
 *   processes: [
 *     {
 *       stage: string,
 *       stageIndex: number,
 *       name: string,
 *       entityType: string,
 *       entitySubType: string
 *     }
 *   ]
 * }[]}
 */
const PROCESS_STAGES = [
  {
    name: 'FABRICATION',
    processes: [
      {name: '下料', entityType: 'PIPE_PIECE'},
      {name: '弯管', entityType: 'PIPE_PIECE'},
      {name: '坡口打磨', entityType: 'PIPE_PIECE'},
      {name: '组对', entityType: 'WELD', entitySubType: 'SBW'},
      {name: '焊接', entityType: 'WELD', entitySubType: 'SBW'},
      {name: 'PMI', entityType: 'WELD', entitySubType: 'SBW'},
      {name: 'PWHT', entityType: 'WELD', entitySubType: 'SBW'},
      {name: 'HD', entityType: 'WELD', entitySubType: 'SBW'},
      {name: 'NDT', entityType: 'WELD', entitySubType: 'SBW'},
      {name: '配送', entityType: 'SPOOL'}
    ]
  },
  {
    name: 'INSTALLATION',
    processes: [
      {name: '表面处理', entityType: 'SPOOL'},
      {name: '涂装', entityType: 'SPOOL'},
      {name: '安装', entityType: 'SPOOL'},
      {name: '坡口打磨', entityType: 'SPOOL'},
      {name: '组对', entityType: 'WELD', entitySubType: 'FBW'},
      {name: '焊接', entityType: 'WELD', entitySubType: 'FBW'},
      {name: 'PMI', entityType: 'WELD', entitySubType: 'FBW'},
      {name: 'PWHT', entityType: 'WELD', entitySubType: 'FBW'},
      {name: 'HD', entityType: 'WELD', entitySubType: 'FBW'},
      {name: 'NDT', entityType: 'WELD', entitySubType: 'FBW'},
      {name: '补漆', entityType: 'SPOOL'}
    ]
  },
  {
    name: 'PRESSURE_TEST',
    processes: [
      {name: '管线检查', entityType: 'ISO'},
      {name: '盲板预制', entityType: 'ISO'},
      {name: '盲板安装', entityType: 'ISO'},
      {name: '试压', entityType: 'ISO'},
      {name: '盲板拆除', entityType: 'ISO'}
    ]
  },
  {
    name: 'INSULATION',
    processes: [
      {name: '外皮预制', entityType: 'ISO'},
      {name: '保温安装', entityType: 'ISO'},
      {name: '外皮安装', entityType: 'ISO'},
      {name: '试压', entityType: 'ISO'},
      {name: '盲板拆除', entityType: 'ISO'}
    ]
  },
  {
    name: 'CLEANNESS',
    processes: [
      {name: '吹扫', entityType: 'ISO'},
      {name: '串油串水', entityType: 'ISO'},
      {name: '手动清理', entityType: 'ISO'},
      {name: '精度测量', entityType: 'ISO'}
    ]
  },
  {
    name: 'MECHANICAL_COMPLETION',
    processes: [
      {name: '完工检查', entityType: 'ISO'}
    ]
  }
];

const STAGE_COUNT = PROCESS_STAGES.length;

/**
 * 工序列表。
 * @type {{
 *   stageIndex: number,
 *   stage: string,
 *   name: string,
 *   entityType: string,
 *   entitySubType: string
 * }[]}
 */
const PROCESSES = [[]].concat(PROCESS_STAGES).reduce((list, next, index) => {
  return list.concat(next.processes.map(p => {
    p.stageIndex = index - 1;
    p.stage = next.name;
    return p;
  }));
});

const PROCESS_COUNT = PROCESSES.length;

/**
 * 实体类型列表。
 * @type {[string]}
 */
const ENTITY_TYPES = [[]].concat(PROCESSES).reduce((list, next) => {

  if (list.indexOf(next.entityType) < 0) {
    list.push(next.entityType);
  }

  return list;
});

const ENTITY_TYPE_COUNT = ENTITY_TYPES.length;

const ENTITY_SUB_TYPE_PATHS = [];

/**
 * 实体子类型列表。
 * @type {{
 *   entityTypeIndex: number,
 *   name: string
 * }[]}
 */
const ENTITY_SUB_TYPES = [[]].concat(PROCESSES).reduce((list, next) => {

  const entitySubTypePath = `${next.entityType}/${next.entitySubType || next.entityType}`;

  if (ENTITY_SUB_TYPE_PATHS.indexOf(entitySubTypePath) < 0) {

    ENTITY_SUB_TYPE_PATHS.push(entitySubTypePath);

    list.push({
      entityTypeIndex: ENTITY_TYPES.indexOf(next.entityType),
      name: next.entitySubType || next.entityType
    });

  }

  return list;
});

const ENTITY_SUB_TYPE_COUNT = ENTITY_SUB_TYPES.length;

/**
 * 实体类型-工序关系。
 * @type {{
 *     entitySubTypeIndex: number,
 *     processIndex: number
 * }[]}
 */
const ENTITY_PROCESS_RELATIONS = [[]].concat(PROCESSES).reduce((list, next, index) => {

  list.push({
    entitySubTypeIndex: ENTITY_SUB_TYPE_PATHS.indexOf(`${next.entityType}/${next.entitySubType || next.entityType}`),
    processIndex: index
  });

  return list;
});

const ENTITY_PROCESS_RELATION_COUNT = ENTITY_PROCESS_RELATIONS.length;

/**
 * 生成组织测试数据。
 * @param context
 * @returns {Promise<void>}
 */
module.exports = async (context) => {

  logger.begin('测试组织/项目数据生成开始');

  logger.debug('测试用例：');
  logger.debug(` • 共 ${params['projects']} 个项目组，每个项目组 ${params['members']} 名成员`);
  logger.debug(` • 每个项目包含 ${STAGE_COUNT} 个工序阶段，每个项目共 ${PROCESS_COUNT} 个工序`);
  logger.debug(` • 每个项目包含 ${ENTITY_TYPE_COUNT} 种实体，每个项目共 ${ENTITY_SUB_TYPE_COUNT} 个子类型`);

  logger.debug('取得用户信息 ...');

  const users = await userModel.findByIdLike(params['members']);

  /*----------------------------------------------------------------------------
   * 创建公司
   *--------------------------------------------------------------------------*/

  logger.debug('创建测试【公司】信息 ...');

  const companyId = uid36.generate();

  await sql.insert(
    dbs.auth,
    sql['ose-auth.organizations.create'],
    1,
    () => {
      return [
        companyId, // 组织 ID
        'COMPANY', // 组织类型
        '压力测试公司', // 名称
        null, //上级 ID
        '/', // 组织路径
        0, // 层级深度
        0, // 排序
        context.systemUser.id, // 创建者
        context.systemUser.id, // 更新者
        Date.now() // 更新版本号
      ];
    }
  );

  const projectCount = params['projects'];

  let progress;

  /*----------------------------------------------------------------------------
   * 创建组织
   *--------------------------------------------------------------------------*/

  const orgIDs = [];

  progress = logger.progress('创建测试【组织】信息 ...', projectCount);

  await sql.insert(
    dbs.auth,
    sql['ose-auth.organizations.create'],
    projectCount,
    index => {

      const orgId = uid36.generate();

      orgIDs.push(orgId);

      return [
        orgId, // 组织 ID
        'PROJECT', // 组织类型
        `压力测试项目组[${('000' + (index + 1)).slice(-4)}]`, // 名称
        companyId, //上级 ID
        `/${companyId}/`, // 组织路径
        1, // 层级深度
        index % 100, // 排序
        context.systemUser.id, // 创建者
        context.systemUser.id, // 更新者
        Date.now() // 更新版本号
      ];

    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 创建组织成员关系
   *--------------------------------------------------------------------------*/

  const organizationCount = 1 + projectCount;

  progress = logger.progress('创建组织【成员关系】信息 ...', organizationCount * users.length);

  await sql.insert(
    dbs.auth,
    sql['ose-auth.user-org-relations.create'],
    organizationCount * users.length,
    index => {

      const orgIndex = index % organizationCount;
      const userIndex = Math.floor(index / organizationCount);

      return [
        uid36.generate(), // 关系 ID
        orgIndex === 0 ? companyId : orgIDs[orgIndex - 1], // 组织 ID
        orgIndex === 0 ? 'COMPANY' : 'PROJECT', // 组织类型
        users[userIndex].id, // 用户 ID
        userIndex % organizationCount === 0 ? 1 : 0, // 是否默认
        context.systemUser.id, // 创建者
        context.systemUser.id, // 更新者
        Date.now() // 更新版本号
      ];

    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 创建管理员角色
   *--------------------------------------------------------------------------*/

  const roleIDs = [];

  progress = logger.progress('创建组织管理员【角色】信息 ...', projectCount);

  await sql.insert(
    dbs.auth,
    sql['ose-auth.roles.create'],
    projectCount,
    index => {

      const roleId = uid36.generate();

      roleIDs.push(roleId);

      return [
        roleId, // 角色 ID
        orgIDs[index], // 组织 ID
        `/${companyId}/${orgIDs[index]}/`, // 组织路径
        '管理员', // 名称
        'administrator', // 编号
        'administrator', // 代码
        1, // 是否可编辑
        0, // 是否为模版
        ',ALL,', // 权限列表
        0, // 排序
        context.systemUser.id, // 创建者
        context.systemUser.id, // 更新者
        Date.now() // 更新版本号
      ];
    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 将用户加入到管理员角色
   *--------------------------------------------------------------------------*/

  progress = logger.progress('创建组织管理员【用户角色关系】信息 ...', projectCount * users.length);

  await sql.insert(
    dbs.auth,
    sql['ose-auth.user-role-relations.create'],
    projectCount * users.length,
    index => {

      const orgIndex = index % projectCount;
      const userIndex = Math.floor(index / projectCount);

      return [
        uid36.generate(), // 关系 ID
        orgIDs[orgIndex], // 组织 ID
        roleIDs[orgIndex], // 角色 ID
        users[userIndex].id, // 用户 ID
        context.systemUser.id, // 创建者
        context.systemUser.id, // 更新者
        Date.now() // 更新版本号
      ];
    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 创建项目
   *--------------------------------------------------------------------------*/

  progress = logger.progress('创建测试【项目】信息 ...', projectCount);

  const projectIDs = [];

  await sql.insert(
    dbs.tasks,
    sql['ose-tasks.projects.create'],
    projectCount,
    index => {

      const projectId = uid36.generate();

      projectIDs.push(projectId);

      return [
        projectId, // 组织 ID
        `压力测试项目[${('000' + (index + 1)).slice(-4)}]`, // 名称
        companyId, // 公司 ID
        orgIDs[index], // 组织 ID
        '压力测试项目', // 备注
        context.systemUser.id, // 创建者
        context.systemUser.id, // 更新者
        Date.now() // 更新版本号
      ];

    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 创建工序阶段
   *--------------------------------------------------------------------------*/

  const stageCount = projectCount * STAGE_COUNT;
  const processStageIDs = [];

  progress = logger.progress('创建测试项目的【工序阶段】信息 ...', stageCount);

  await sql.insert(
    dbs.tasks,
    sql['ose-tasks.process-stages.create'],
    stageCount,
    index => {

      const orgIndex = index % projectCount;
      const stageId = uid36.generate();
      const stageName = PROCESS_STAGES[Math.floor(index / projectCount)].name;

      processStageIDs.push(stageId);

      return [
        stageId, // 工序阶段 ID
        orgIDs[orgIndex], // 组织 ID
        projectIDs[orgIndex], // 项目 ID
        index % 100, // 排序
        stageName, // 名称（英文）
        stageName, // 名称（中文）
        stageName // 备注
      ];

    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 创建工序分类
   *--------------------------------------------------------------------------*/

  const processCategoryIDs = [];

  progress = logger.progress('创建测试项目的【工序分类】信息 ...', projectCount);

  await sql.insert(
    dbs.tasks,
    sql['ose-tasks.process-categories.create'],
    projectCount,
    index => {

      const orgIndex = index % projectCount;
      const categoryId = uid36.generate();

      processCategoryIDs.push(categoryId);

      return [
        categoryId, // 工序阶段 ID
        orgIDs[orgIndex], // 组织 ID
        projectIDs[orgIndex], // 项目 ID
        '建造', // 名称（英文）
        '建造', // 名称（中文）
        '建造' // 备注
      ];

    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 创建工序
   *--------------------------------------------------------------------------*/

  const processCount = projectCount * PROCESS_COUNT;
  const processIDs = [];

  progress = logger.progress('创建测试项目的【工序】信息 ...', processCount);

  await sql.insert(
    dbs.tasks,
    sql['ose-tasks.processes.create'],
    processCount,
    index => {

      const orgIndex = index % projectCount;
      const entityProcessId = uid36.generate();
      const entityProcess = PROCESSES[Math.floor(index / projectCount)];
      const entityProcessName = entityProcess.name;

      processIDs.push(entityProcessId);

      return [
        entityProcessId, // 工序阶段 ID
        orgIDs[orgIndex], // 组织 ID
        projectIDs[orgIndex], // 项目 ID
        processStageIDs[entityProcess.stageIndex * projectCount + orgIndex], // 工序阶段 ID
        processCategoryIDs[orgIndex], // 工序分类 ID
        index % 100, // 排序
        entityProcessName, // 名称（英文）
        entityProcessName, // 名称（中文）
        entityProcessName, // 备注
      ];

    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 创建实体类型
   *--------------------------------------------------------------------------*/

  const entityTypeCount = projectCount * ENTITY_TYPE_COUNT;
  const entityTypeIDs = [];

  progress = logger.progress('创建测试项目的【实体类型】信息 ...', entityTypeCount);

  await sql.insert(
    dbs.tasks,
    sql['ose-tasks.entity-types.create'],
    entityTypeCount,
    index => {

      const orgIndex = index % projectCount;
      const entityTypeId = uid36.generate();
      const entityType = ENTITY_TYPES[Math.floor(index / projectCount)];

      entityTypeIDs.push(entityTypeId);

      return [
        entityTypeId, // 实体类型 ID
        orgIDs[orgIndex], // 组织 ID
        projectIDs[orgIndex], // 项目 ID
        entityType, // 名称（英文）
        entityType // 名称（中文）
      ];

    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 创建实体子类型
   *--------------------------------------------------------------------------*/

  const entitySubTypeCount = projectCount * ENTITY_SUB_TYPE_COUNT;
  const entitySubTypeIDs = [];

  progress = logger.progress('创建测试项目的【实体子类型】信息 ...', entitySubTypeCount);

  await sql.insert(
    dbs.tasks,
    sql['ose-tasks.entity-sub-types.create'],
    entitySubTypeCount,
    index => {

      const orgIndex = index % projectCount;
      const entitySubType = ENTITY_SUB_TYPES[Math.floor(index / projectCount)];
      const entitySubTypeId = uid36.generate();

      entitySubTypeIDs.push(entitySubTypeId);

      return [
        entitySubTypeId, // 实体子类型 ID
        orgIDs[orgIndex], // 组织 ID
        projectIDs[orgIndex], // 项目 ID
        entityTypeIDs[entitySubType.entityTypeIndex * projectCount + orgIndex], // 实体类型 ID
        entitySubType.name, // 名称（英文）
        entitySubType.name // 名称（中文）
      ];

    },
    inserted => progress.tick(inserted)
  );

  /*----------------------------------------------------------------------------
   * 创建实体工序关系
   *--------------------------------------------------------------------------*/

  const entityProcessRelationCount = projectCount * ENTITY_PROCESS_RELATION_COUNT;

  progress = logger.progress('创建测试项目的【实体类型-工序关系】信息 ...', entityProcessRelationCount);

  await sql.insert(
    dbs.tasks,
    sql['ose-tasks.entity-process-relations.create'],
    entityProcessRelationCount,
    index => {

      const orgIndex = index % projectCount;
      const entityProcessRelation = ENTITY_PROCESS_RELATIONS[Math.floor(index / projectCount)];

      return [
        uid36.generate(), // 实体子类型 ID
        orgIDs[orgIndex], // 组织 ID
        projectIDs[orgIndex], // 项目 ID
        entitySubTypeIDs[entityProcessRelation.entitySubTypeIndex * projectCount + orgIndex], // 实体子类型 ID
        processIDs[entityProcessRelation.processIndex * projectCount + orgIndex] // 工序 ID
      ];

    },
    inserted => progress.tick(inserted)
  );


  logger.end('测试组织/项目数据生成结束');

};
