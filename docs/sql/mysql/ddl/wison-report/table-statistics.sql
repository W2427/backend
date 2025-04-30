-- -----------------------------------------------------------------------------
-- 数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据库　　：ose_tasks
-- 创建时间　：2018-11-30
-- 创建者　　：金海岩
-- -----------------------------------------------------------------------------

USE `ose_report`;

DROP TABLE IF EXISTS `statistics`;

-- -----------------------------------------------------------------------------
-- 统计归档数据表。
-- 统计计划类型有效值：DAILY（日次），WEEKLY（周次），MONTHLY（月次）
-- 归档时间期间的格式：yyyy/ww（周），yyyy-MM（月）或 yyyy-MM-dd（日期）。
-- 聚合值的业务意义由统计数据的类型决定。
-- -----------------------------------------------------------------------------
CREATE TABLE `statistics` (

  -- 项目 ID、归档数据业务类型、统计计划类型、归档时间、年、月、日、周、聚合单位值拼接字符串的 MD5 摘要
  `id`                       VARCHAR(32)  NOT NULL COMMENT '聚合单位拼接字符串 MD5 摘要',

  -- 归档数据分类
  `project_id`               BIGINT(20)  NOT NULL COMMENT '项目 ID',
  `archive_type`             VARCHAR(45)  NOT NULL COMMENT '归档数据业务类型',
  `schedule_type`            VARCHAR(7)   NOT NULL COMMENT '统计计划类型',

  -- 归档期间
  `archive_year`             INTEGER      NOT NULL COMMENT '归档时间期间',
  `archive_month`            INTEGER      NOT NULL COMMENT '归档时间期间',
  `archive_day`              INTEGER      NOT NULL COMMENT '归档时间期间',
  `archive_week`             INTEGER      NOT NULL COMMENT '归档时间期间',

  -- 聚合对象数据期间
  `group_year`               INTEGER      NOT NULL COMMENT '聚合对象期间（年）',
  `group_month`              INTEGER      NOT NULL COMMENT '聚合对象期间（月）',
  `group_day`                INTEGER      NOT NULL COMMENT '聚合对象期间（日）',
  `group_week`               INTEGER      NOT NULL COMMENT '聚合对象期间（周）',
  `group_date`               INTEGER      NOT NULL COMMENT '聚合对象期间（年月日）',

  -- 聚合单位
  `module`                   VARCHAR(255) NULL     COMMENT '聚合单位：模块 No.',
  `pressure_test_package`    VARCHAR(255) NULL     COMMENT '聚合单位：试压包 ID',
  `sub_system`               VARCHAR(255) NULL     COMMENT '聚合单位：子系统 ID',
  `stage`                    VARCHAR(255) NULL     COMMENT '聚合单位：工序阶段名称',
  `process`                  VARCHAR(255) NULL     COMMENT '聚合单位：工序名称',
  `weld_type`                VARCHAR(255) NULL     COMMENT '聚合单位：焊口类型',
  `issue_type`               VARCHAR(255) NULL     COMMENT '聚合单位：遗留问题类型',
  `entity_nps`               VARCHAR(255) NULL     COMMENT '聚合单位：实体寸径',
  `entity_length`            VARCHAR(255) NULL     COMMENT '聚合单位：实体长度',
  `entity_material`          VARCHAR(255) NULL     COMMENT '聚合单位：实体材质',
  `subcontractor_id`         VARCHAR(255) NULL     COMMENT '聚合单位：工程建造分包商 ID',
  `department_id`            VARCHAR(255) NULL     COMMENT '聚合单位：责任部门 ID',
  `welder_id`                VARCHAR(255) NULL     COMMENT '聚合单位：焊工 ID',
  `manager_id`               VARCHAR(255) NULL     COMMENT '聚合单位：管理者用户 ID',

  -- 聚合值
  `value_01`                 DOUBLE       NULL     COMMENT '聚合值#1' DEFAULT 0,
  `value_02`                 DOUBLE       NULL     COMMENT '聚合值#2' DEFAULT 0,
  `value_03`                 DOUBLE       NULL     COMMENT '聚合值#3' DEFAULT 0,
  `value_04`                 DOUBLE       NULL     COMMENT '聚合值#4' DEFAULT 0,
  `value_05`                 DOUBLE       NULL     COMMENT '聚合值#5' DEFAULT 0,
  `value_06`                 DOUBLE       NULL     COMMENT '聚合值#6' DEFAULT 0,
  `value_07`                 DOUBLE       NULL     COMMENT '聚合值#7' DEFAULT 0,
  `value_08`                 DOUBLE       NULL     COMMENT '聚合值#8' DEFAULT 0,
  `value_09`                 DOUBLE       NULL     COMMENT '聚合值#9' DEFAULT 0,

  -- 记录创建/最后更新时间
  `created_at`               TIMESTAMP    NULL     COMMENT '记录创建时间' DEFAULT CURRENT_TIMESTAMP,
  `last_modified_at`         TIMESTAMP    NULL     COMMENT '记录最后更新时间',

  -- 索引
  PRIMARY KEY (`id`),
  INDEX (`project_id`, `archive_type`, `archive_year`, `archive_month`, `archive_day`),
  INDEX (`project_id`, `archive_type`, `archive_year`, `archive_month`, `archive_day`, `group_year`, `group_month`, `group_day`),
  INDEX (`project_id`, `archive_type`, `archive_year`, `archive_week`),
  INDEX (`project_id`, `archive_type`, `archive_year`, `archive_week`, `group_year`, `group_week`),
  INDEX (`project_id`, `archive_type`, `archive_year`, `archive_month`),
  INDEX (`project_id`, `archive_type`, `archive_year`, `archive_month`, `group_year`, `group_month`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
