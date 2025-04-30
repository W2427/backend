-- -----------------------------------------------------------------------------
-- 报验合格率数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.bpm_activity_instance,
--           ose_tasks.bpm_hi_taskinst
-- 输出目标　：ose_report.statistics
-- 创建时间　：2019-03-14
-- 创建者　　：梅洛谊
-- -----------------------------------------------------------------------------

USE `ose_report`;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_pass_rate_daily`;
DROP PROCEDURE IF EXISTS `archive_pass_rate`;
DROP PROCEDURE IF EXISTS `archive_pass_rate_of_project`;
DROP PROCEDURE IF EXISTS `archive_inspection_pass_rate_of_project`;
DROP PROCEDURE IF EXISTS `archive_qualified_pass_rate_of_project`;
DROP PROCEDURE IF EXISTS `archive_fpy_pass_rate_of_project`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计项目：质量控制 / 报验合格率
-- 统计对象：报验合格率
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_pass_rate`(IN v_archive_time TIMESTAMP)
  BEGIN_LABEL:BEGIN

    DECLARE v_project_id      BIGINT(20);
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
    DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;

    DECLARE v_project_cursor  CURSOR FOR (
      SELECT
        distinct `act`.`project_id`
      FROM
        `ose_tasks`.`bpm_activity_instance` AS `act`
    );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET v_cursor_finished = 0;

    OPEN v_project_cursor;
    REPEAT

      FETCH v_project_cursor INTO v_project_id;

      IF !v_cursor_finished THEN
        CALL `archive_pass_rate_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 任务数据归档。
-- 统计已创建任务到创建时间上。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_pass_rate_of_project`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
  BEGIN_LABEL:BEGIN
    DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
    DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;

    -- 取得最后一条定时任务的执行记录
    SELECT
      `started_at`,
      `finished_at`
    INTO
      v_last_task_started_at,
      v_last_task_finished_at
    FROM
      `ose_tasks`.`schedule_log`
    WHERE
      `code` = 'ARCHIVE_WBS_PASS_RATE_PROGRESS'
      AND `project_id` = v_project_id
    ORDER BY
      `id` DESC
    LIMIT
      0, 1
    ;

    -- 若存在尚未完成的任务且执行时长小于两个小时则结束
    IF (v_last_task_finished_at IS NULL AND CURRENT_TIMESTAMP() - v_last_task_started_at < 20000) THEN
      LEAVE BEGIN_LABEL;
    END IF;

    -- 生成执行记录的实体 ID
    SET @schedule_id = `ose_tasks`.`generate_entity_id`();

    -- 创建执行记录
    INSERT INTO `ose_tasks`.`schedule_log` (`id`, `project_id`, `type`, `code`, `name`, `status`)
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_PASS_RATE_PROGRESS', '数据归档：质量控制 / 报验合格率', 'RUNNING');

    -- 为防止当天有业务数据减少的情况，归档数据改为先删除，后插入
    DELETE FROM `ose_report`.`statistics`
    WHERE `project_id` = v_project_id
      AND `archive_year`  = YEAR(v_archive_time)
      AND `archive_month` = MONTH(v_archive_time)
      AND `archive_day`   = DAY(v_archive_time)
      AND `archive_type`  = 'WBS_PASS_RATE_PROGRESS';

    -- 内检数量，外检数量统计，归档数据方式是先删除，后插入
    CALL `archive_inspection_pass_rate_of_project`(v_archive_time, v_project_id);

    -- 内检合格数量，外检合格数量统计，归档数据方式是插入更新
    CALL `archive_qualified_pass_rate_of_project`(v_archive_time, v_project_id);

    -- 内检一次合格数量，外检一次合格数量，内检一次不合格数量，外检一次不合格数量统计，归档数据方式是插入更新
    CALL `archive_fpy_pass_rate_of_project`(v_archive_time, v_project_id);

    -- 更新执行记录
    UPDATE
      `ose_tasks`.`schedule_log`
    SET
      `finished_at` = CURRENT_TIMESTAMP(),
      `status` = 'FINISHED'
    WHERE
      `id` = @schedule_id
    ;
  END $$

-- -----------------------------------------------------------------------------
-- 报验合格率统计数据归档。
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_inspection_pass_rate_of_project`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
  BEGIN_LABEL:BEGIN

    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_group_year            INTEGER;
    DECLARE v_group_month           INTEGER;
    DECLARE v_group_day             INTEGER;
    DECLARE v_group_week            INTEGER;
    DECLARE v_module                VARCHAR(255); -- 模块
    DECLARE v_stage                 VARCHAR(255); -- 工序阶段名称
    DECLARE v_process               VARCHAR(255); -- 工序名称
    DECLARE v_team_name             VARCHAR(255); -- 班组
    DECLARE v_internal_inspection                INTEGER; -- 内检数量
    DECLARE v_external_inspection                INTEGER; -- 外检数量

    DECLARE v_inspection_pass_rate_cursor  CURSOR FOR (
         SELECT
             `t1`.`entity_module_name` AS `module_name`, -- 任务所属模块名
             `t1`.`process_stage_name_cn` AS `stage`, -- 工序阶段名称
             `t1`.`a.process_name` AS `process`,  -- 工序名称
             `t1`.`team_name` AS `team_name`, -- 班组
              YEAR(`t2`.`start_time_`) AS group_year,
              MONTH(`t2`.`start_time_`) AS group_month,
              DAY(`t2`.`start_time_`) AS group_day,
             WEEK_OF_YEAR(`t2`.`start_time_`) AS group_week,
              SUM(IF(`t2`.`id` is not null AND `t2`.`task_def_key_` = 'usertask-QC-ACCEPT-INSPECTION', 1, 0)) AS internal_inspection,  -- 内检数量
              SUM(IF(`t2`.`id` is not null AND `t2`.`task_def_key_` = 'usertask-EX-INSP-FINISH-CONFIRM', 1, 0)) AS external_inspection  -- 外检数量

          FROM

          `ose_tasks`.`bpm_activity_instance` AS `t1`
          JOIN `ose_tasks`.`bpm_hi_taskinst` AS `t2` ON `t1`.`act_inst_id` = `t2`.`act_inst_id` AND `t2`.`tenant_id_` = `t1`.`project_id`

          WHERE `t1`.`project_id` = v_project_id

          GROUP BY `t1`.`project_id`, `t1`.`entity_module_name`, `stage`, `process`, `t1`.`team_name`, `group_year`, `group_month`, `group_day`, `group_week`
      );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;

    OPEN v_inspection_pass_rate_cursor;
    REPEAT

      FETCH
        v_inspection_pass_rate_cursor
      INTO
        v_module,
        v_stage,
        v_process,
        v_team_name,
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_internal_inspection,
        v_external_inspection
      ;

      IF !v_cursor_finished AND v_group_year IS NOT NULL THEN
        INSERT INTO `statistics` (
          `id`,
          `project_id`,
          `archive_type`,
          `schedule_type`,
          `archive_year`,
          `archive_month`,
          `archive_day`,
          `archive_week`,
          `group_year`,
          `group_month`,
          `group_day`,
          `group_week`,
          `group_date`,
          `module`,
          `stage`,
          `process`,
          `team_name`,
          `value_01`,
          `value_03`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_PASS_RATE_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            IFNULL(v_module, ''),
            IFNULL(v_stage, ''),
            IFNULL(v_process, ''),
            IFNULL(v_team_name, '')
          ))),
          v_project_id,
          'WBS_PASS_RATE_PROGRESS',
          'DAILY',
          @archive_year,
          @archive_month,
          @archive_day,
          @archive_week,
          v_group_year,
          v_group_month,
          v_group_day,
          v_group_week,
          v_group_year * 10000 + v_group_month * 100 + v_group_day,
          v_module,
          v_stage,
          v_process,
          v_team_name, -- 班组
          v_internal_inspection,
          v_external_inspection
        )
        ON DUPLICATE KEY UPDATE
          `value_01`         = v_internal_inspection,
          `value_03`         = v_external_inspection,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_inspection_pass_rate_cursor;

  END $$

  -- 内检合格数量，外检合格数量统计，归档数据方式是插入更新
  CREATE PROCEDURE `archive_qualified_pass_rate_of_project`(
    IN v_archive_time TIMESTAMP,  -- 归档时间
    IN v_project_id   BIGINT(20) -- 项目 ID
  )
    BEGIN_LABEL:BEGIN

      DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
      DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;
      DECLARE v_cursor_finished INTEGER DEFAULT 0;
      DECLARE v_group_year            INTEGER;
      DECLARE v_group_month           INTEGER;
      DECLARE v_group_day             INTEGER;
      DECLARE v_group_week            INTEGER;
      DECLARE v_module                VARCHAR(255); -- 模块
      DECLARE v_stage                 VARCHAR(255); -- 工序阶段名称
      DECLARE v_process               VARCHAR(255); -- 工序名称
      DECLARE v_team_name             VARCHAR(255); -- 班组
      DECLARE v_qualified_internal_inspection      INTEGER; -- 合格内检数量
      DECLARE v_qualified_external_inspection      INTEGER; -- 合格外检数量

      DECLARE v_qualified_pass_rate_cursor  CURSOR FOR (
           SELECT
               `t1`.`entity_module_name` AS `module_name`, -- 任务所属模块名
               `t1`.`process_stage_name_cn` AS `stage`, -- 工序阶段名称
               `t1`.`a.process_name` AS `process`,  -- 工序名称
               `t1`.`team_name` AS `team_name`, -- 班组
                YEAR(`r2`.`last_modified_at`) AS group_year,
                MONTH(`r2`.`last_modified_at`) AS group_month,
                DAY(`r2`.`last_modified_at`) AS group_day,
               WEEK_OF_YEAR(`r2`.`last_modified_at`) AS group_week,
                SUM(IF(`t2`.`task_def_key_` = 'usertask-INSPECTION-RESULT-CONFIRM' AND `r2`.`comment` like '%:ACCEPT%' , 1, 0)) AS qualified_internal_inspection, -- 合格内检数量
                SUM(IF(
                        (`t2`.`task_def_key_` = 'usertask-EX-INSP-FINISH-CONFIRM' AND `r2`.`comment` like '%:NO_COMMENT')
                        OR (`t2`.`task_def_key_` = 'usertask-ENTER-EX-COMMENT' AND `r2`.`comment` like '%:ACCEPT')
                      , 1, 0)
                   ) AS qualified_external_inspection -- 合格外检数量
            FROM

            `ose_tasks`.`bpm_activity_instance` AS `t1`
            JOIN `ose_tasks`.`bpm_hi_taskinst` AS `t2` ON `t1`.`act_inst_id` = `t2`.`act_inst_id` AND `t2`.`tenant_id_` = `t1`.`project_id`
            JOIN `ose_tasks`.`bpm_act_task` AS `r2` ON `t2`.`task_id_` = `r2`.`task_id`
            WHERE `t1`.`project_id` = v_project_id

            GROUP BY `t1`.`project_id`, `t1`.`entity_module_name`, `stage`, `process`, `t1`.`team_name`, `group_year`, `group_month`, `group_day`, `group_week`
        );

      DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

      -- 取得最后一条定时任务的执行记录
      SELECT
        `started_at`,
        `finished_at`
      INTO
        v_last_task_started_at,
        v_last_task_finished_at
      FROM
        `ose_tasks`.`schedule_log`
      WHERE
        `code` = 'ARCHIVE_WBS_PASS_RATE_PROGRESS'
        AND `project_id` = v_project_id
      ORDER BY
        `id` DESC
      LIMIT
        0, 1
      ;

      SET @archive_year     = YEAR(v_archive_time);
      SET @archive_month    = MONTH(v_archive_time);
      SET @archive_day      = DAY(v_archive_time);
      SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
      SET v_cursor_finished = 0;

      OPEN v_qualified_pass_rate_cursor;
      REPEAT

        FETCH
          v_qualified_pass_rate_cursor
        INTO
          v_module,
          v_stage,
          v_process,
          v_team_name,
          v_group_year,
          v_group_month,
          v_group_day,
          v_group_week,
          v_qualified_internal_inspection,
          v_qualified_external_inspection
        ;

        IF !v_cursor_finished AND v_group_year IS NOT NULL THEN
          INSERT INTO `statistics` (
            `id`,
            `project_id`,
            `archive_type`,
            `schedule_type`,
            `archive_year`,
            `archive_month`,
            `archive_day`,
            `archive_week`,
            `group_year`,
            `group_month`,
            `group_day`,
            `group_week`,
            `group_date`,
            `module`,
            `stage`,
            `process`,
            `team_name`,
            `value_02`,
            `value_04`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\nWBS_PASS_RATE_PROGRESS\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              IFNULL(v_module, ''),
              IFNULL(v_stage, ''),
              IFNULL(v_process, ''),
              IFNULL(v_team_name, '')
            ))),
            v_project_id,
            'WBS_PASS_RATE_PROGRESS',
            'DAILY',
            @archive_year,
            @archive_month,
            @archive_day,
            @archive_week,
            v_group_year,
            v_group_month,
            v_group_day,
            v_group_week,
            v_group_year * 10000 + v_group_month * 100 + v_group_day,
            v_module,
            v_stage,
            v_process,
            v_team_name, -- 班组
            v_qualified_internal_inspection,
            v_qualified_external_inspection
          )
          ON DUPLICATE KEY UPDATE
            `value_02`         = v_qualified_internal_inspection,
            `value_04`         = v_qualified_external_inspection,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
      CLOSE v_qualified_pass_rate_cursor;

    END $$

-- 内检一次合格数量，外检一次合格数量统计，归档数据方式是插入更新
CREATE PROCEDURE `archive_fpy_pass_rate_of_project`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
  BEGIN_LABEL:BEGIN

    DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
    DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_group_year            INTEGER;
    DECLARE v_group_month           INTEGER;
    DECLARE v_group_day             INTEGER;
    DECLARE v_group_week            INTEGER;
    DECLARE v_module                VARCHAR(255); -- 模块
    DECLARE v_stage                 VARCHAR(255); -- 工序阶段名称
    DECLARE v_process               VARCHAR(255); -- 工序名称
    DECLARE v_team_name             VARCHAR(255); -- 班组
    DECLARE v_fpy1_internal_inspection      INTEGER; -- 内检一次合格数量
    DECLARE v_fpy1_external_inspection      INTEGER; -- 外检一次合格数量
    DECLARE v_fpy2_internal_inspection      INTEGER; -- 内检一次不合格数量
    DECLARE v_fpy2_external_inspection      INTEGER; -- 外检一次不合格数量

    DECLARE v_fpy_pass_rate_cursor  CURSOR FOR (
         SELECT
             `t1`.`entity_module_name` AS `module_name`, -- 任务所属模块名
             `t1`.`process_stage_name_cn` AS `stage`, -- 工序阶段名称
             `t1`.`a.process_name` AS `process`,  -- 工序名称
             `t1`.`team_name` AS `team_name`, -- 班组
              YEAR(`t1`.`end_date`) AS group_year,
              MONTH(`t1`.`end_date`) AS group_month,
              DAY(`t1`.`end_date`) AS group_day,
             WEEK_OF_YEAR(`t1`.`end_date`) AS group_week,
              SUM(IF(`t1`.`internal_inspection_fpy` = 1, 1, 0)) AS fpy1_internal_inspection, -- 内检一次合格数量
              SUM(IF(`t1`.`external_inspection_fpy` = 1, 1, 0)) AS fpy1_external_inspection, -- 外检一次合格数量
              SUM(IF(`t1`.`internal_inspection_fpy` = 2, 1, 0)) AS fpy2_internal_inspection, -- 内检一次不合格数量
              SUM(IF(`t1`.`external_inspection_fpy` = 2, 1, 0)) AS fpy2_external_inspection -- 外检一次不合格数量

          FROM

          `ose_tasks`.`bpm_activity_instance` AS `t1`
          WHERE `t1`.`project_id` = v_project_id

          GROUP BY `t1`.`project_id`, `t1`.`entity_module_name`, `stage`, `process`, `t1`.`team_name`, `group_year`, `group_month`, `group_day`, `group_week`
      );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    -- 取得最后一条定时任务的执行记录
    SELECT
      `started_at`,
      `finished_at`
    INTO
      v_last_task_started_at,
      v_last_task_finished_at
    FROM
      `ose_tasks`.`schedule_log`
    WHERE
      `code` = 'ARCHIVE_WBS_PASS_RATE_PROGRESS'
      AND `project_id` = v_project_id
    ORDER BY
      `id` DESC
    LIMIT
      0, 1
    ;

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;

    OPEN v_fpy_pass_rate_cursor;
    REPEAT

      FETCH
        v_fpy_pass_rate_cursor
      INTO
        v_module,
        v_stage,
        v_process,
        v_team_name,
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_fpy1_internal_inspection,
        v_fpy1_external_inspection,
        v_fpy2_internal_inspection,
        v_fpy2_external_inspection
      ;

      IF !v_cursor_finished AND v_group_year IS NOT NULL THEN
        INSERT INTO `statistics` (
          `id`,
          `project_id`,
          `archive_type`,
          `schedule_type`,
          `archive_year`,
          `archive_month`,
          `archive_day`,
          `archive_week`,
          `group_year`,
          `group_month`,
          `group_day`,
          `group_week`,
          `group_date`,
          `module`,
          `stage`,
          `process`,
          `team_name`,
          `value_05`,
          `value_06`,
          `value_07`,
          `value_08`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_PASS_RATE_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            IFNULL(v_module, ''),
            IFNULL(v_stage, ''),
            IFNULL(v_process, ''),
            IFNULL(v_team_name, '')
          ))),
          v_project_id,
          'WBS_PASS_RATE_PROGRESS',
          'DAILY',
          @archive_year,
          @archive_month,
          @archive_day,
          @archive_week,
          v_group_year,
          v_group_month,
          v_group_day,
          v_group_week,
          v_group_year * 10000 + v_group_month * 100 + v_group_day,
          v_module,
          v_stage,
          v_process,
          v_team_name, -- 班组
          v_fpy1_internal_inspection,
          v_fpy1_external_inspection,
          v_fpy2_internal_inspection,
          v_fpy2_external_inspection
        )
        ON DUPLICATE KEY UPDATE
          `value_05`         = v_fpy1_internal_inspection,
          `value_06`         = v_fpy1_external_inspection,
          `value_07`         = v_fpy2_internal_inspection,
          `value_08`         = v_fpy2_external_inspection,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;
    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_fpy_pass_rate_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 报验合格率归档定时任务。
-- 执行频率：每日凌晨4点
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_pass_rate_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2018-11-01 04:00:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前6小时
    CALL `archive_pass_rate`(ADDTIME(NOW(), -60000));

    -- EVENT[archive_plan_daily_report_daily]中，会定义其他的启动时间，为计划日报归档数据
    -- 如有启动时间方面的修改，请结合日报的时间，一起考虑。
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
