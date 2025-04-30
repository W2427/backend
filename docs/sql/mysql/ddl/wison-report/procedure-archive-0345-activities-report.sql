-- -----------------------------------------------------------------------------
-- 任务数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.bpm_activity_instance,ose_tasks.bpm_act_task
-- 输出目标　：ose_report.statistics
-- 创建时间　：2019-03-14
-- 创建者　　：梅洛谊
-- -----------------------------------------------------------------------------

USE `ose_report`;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_activities_daily`;
DROP PROCEDURE IF EXISTS `archive_activities`;
DROP PROCEDURE IF EXISTS `archive_activities_of_project`;
DROP PROCEDURE IF EXISTS `archive_created_activities_of_project`;
DROP PROCEDURE IF EXISTS `archive_stopped_activities_of_project`;
DROP PROCEDURE IF EXISTS `archive_finished_activities_of_project`;
DROP PROCEDURE IF EXISTS `archive_activities_working_hours_of_project`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计项目：任务管理 / 任务统计
-- 统计对象：任务。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_activities`(IN v_archive_time TIMESTAMP)
  BEGIN_LABEL:BEGIN

    DECLARE v_project_id      BIGINT(20);
    DECLARE v_cursor_finished INTEGER DEFAULT 0;

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
        -- 按项目归档数据
        CALL `archive_activities_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 任务数据归档。
-- 统计已创建任务到创建时间上。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_activities_of_project`(
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
      `code` = 'ARCHIVE_WBS_ACTIVITIES_PROGRESS'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_ACTIVITIES_PROGRESS', '数据归档：任务管理 / 任务统计', 'RUNNING');

    -- 为防止当天有业务数据减少的情况，归档数据改为先删除，后插入
    DELETE FROM `ose_report`.`statistics`
    WHERE `project_id` = v_project_id
      AND `archive_year`  = YEAR(v_archive_time)
      AND `archive_month` = MONTH(v_archive_time)
      AND `archive_day`   = DAY(v_archive_time)
      AND `archive_type`  = 'WBS_ACTIVITIES_PROGRESS';

    -- 统计已创建任务到创建时间上
    CALL `archive_created_activities_of_project`(v_archive_time, v_project_id);

    -- 统计已停止任务到最终更新时间上
    CALL `archive_stopped_activities_of_project`(v_archive_time, v_project_id);

    -- 统计已完成任务到bpm_activity_instances.end_date上
    CALL `archive_finished_activities_of_project`(v_archive_time, v_project_id);

    -- 统计任务工时到bpm_act_tasks.created_at上
    CALL `archive_activities_working_hours_of_project`(v_archive_time, v_project_id);

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
-- 任务数据归档。
-- 统计已创建任务到创建时间上。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_created_activities_of_project`(
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
    DECLARE v_stage                 VARCHAR(255); -- 工序阶段
    DECLARE v_process               VARCHAR(255); -- 工序
    DECLARE v_team_name             VARCHAR(255); -- 班组名
    DECLARE v_task_package_name     VARCHAR(255); -- 任务包名
    DECLARE v_created_count         INTEGER; -- 创建任务数

    DECLARE v_created_activities_cursor  CURSOR FOR (
         SELECT
            YEAR(`t1`.`created_at`) AS `group_year`,
            MONTH(`t1`.`created_at`) AS `group_month`,
            DAY(`t1`.`created_at`) AS `group_day`,
            WEEK_OF_YEAR(`t1`.`created_at`) AS `group_week`,
            `t1`.`entity_module_name` AS `module`,
            `t1`.`process_stage_name_cn` AS `stage`,
            `t1`.`process_name` AS `process`,
            `t1`.`team_name` AS `team_name`,
            `t1`.`task_package_name` AS `task_package_name`,
            COUNT(`t1`.`id`) AS `created_count`

         FROM

         `ose_tasks`.`bpm_activity_instance` AS `t1`
         WHERE `t1`.`project_id` = v_project_id

         GROUP BY `group_year`, `group_month`, `group_day`, `group_week`,
                  `t1`.`entity_module_name`, `t1`.`process_stage_name_cn`, `t1`.`process_name`, `t1`.`team_name`, `t1`.`task_package_name`
      );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;

    OPEN v_created_activities_cursor;
    REPEAT

      FETCH
        v_created_activities_cursor
      INTO
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_module,
        v_stage,
        v_process,
        v_team_name,
        v_task_package_name,
        v_created_count
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
          `task_package_name`,
          `value_01`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_ACTIVITIES_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            IFNULL(v_module, ''),
            IFNULL(v_stage, ''),
            IFNULL(v_process, ''),
            IFNULL(v_team_name, ''),
            IFNULL(v_task_package_name, '')
          ))),
          v_project_id,
          'WBS_ACTIVITIES_PROGRESS',
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
          v_team_name,
          v_task_package_name,
          v_created_count
        )
        ON DUPLICATE KEY UPDATE
          `value_01`         = v_created_count,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_created_activities_cursor;

  END $$

  -- -----------------------------------------------------------------------------
  -- 停止任务数据归档。
  -- 统计已停止任务到最终更新时间上。
  -- -----------------------------------------------------------------------------
  CREATE PROCEDURE `archive_stopped_activities_of_project`(
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
      DECLARE v_stage                 VARCHAR(255); -- 工序阶段
      DECLARE v_process               VARCHAR(255); -- 工序
      DECLARE v_team_name             VARCHAR(255); -- 班组名
      DECLARE v_task_package_name     VARCHAR(255); -- 任务包名
      DECLARE v_stopped_count         INTEGER; -- 已停止任务数

      DECLARE v_stopped_activities_cursor  CURSOR FOR (
           SELECT
              YEAR(`t1`.`last_modified_at`) AS `group_year`,
              MONTH(`t1`.`last_modified_at`) AS `group_month`,
              DAY(`t1`.`last_modified_at`) AS `group_day`,
              WEEK_OF_YEAR(`t1`.`last_modified_at`) AS `group_week`,
              `t1`.`entity_module_name` AS `module`,
              `t1`.`process_stage_name_cn` AS `stage`,
              `t1`.`process_name` AS `process`,
              `t1`.`team_name` AS `team_name`,
              `t1`.`task_package_name` AS `task_package_name`,
              COUNT(`t1`.`id`) AS `stopped_count`

           FROM

           `ose_tasks`.`bpm_activity_instance` AS `t1`
           WHERE `t1`.`project_id` = v_project_id AND `t1`.`suspension_state` = 'SUSPEND' -- 2:已停止； 1：运行中

           GROUP BY `group_year`, `group_month`, `group_day`, `group_week`,
                    `t1`.`entity_module_name`, `t1`.`process_stage_name_cn`, `t1`.`process_name`, `t1`.`team_name`, `t1`.`task_package_name`
        );

      DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

      SET @archive_year     = YEAR(v_archive_time);
      SET @archive_month    = MONTH(v_archive_time);
      SET @archive_day      = DAY(v_archive_time);
      SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
      SET v_cursor_finished = 0;



      OPEN v_stopped_activities_cursor;
      REPEAT

        FETCH
          v_stopped_activities_cursor
        INTO
          v_group_year,
          v_group_month,
          v_group_day,
          v_group_week,
          v_module,
          v_stage,
          v_process,
          v_team_name,
          v_task_package_name,
          v_stopped_count
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
            `task_package_name`,
            `value_02`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\nWBS_ACTIVITIES_PROGRESS\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              IFNULL(v_module, ''),
              IFNULL(v_stage, ''),
              IFNULL(v_process, ''),
              IFNULL(v_team_name, ''),
              IFNULL(v_task_package_name, '')
            ))),
            v_project_id,
            'WBS_ACTIVITIES_PROGRESS',
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
            v_team_name,
            v_task_package_name,
            v_stopped_count
          )
          ON DUPLICATE KEY UPDATE
            `value_02`         = v_stopped_count,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
      CLOSE v_stopped_activities_cursor;

    END $$

  -- -----------------------------------------------------------------------------
  -- 完成任务数据归档。
  -- 统计已完成任务到end_date上。
  -- -----------------------------------------------------------------------------
  CREATE PROCEDURE `archive_finished_activities_of_project`(
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
    DECLARE v_stage                 VARCHAR(255); -- 工序阶段
    DECLARE v_process               VARCHAR(255); -- 工序
    DECLARE v_team_name             VARCHAR(255); -- 班组名
    DECLARE v_task_package_name     VARCHAR(255); -- 任务包名
    DECLARE v_finished_count         INTEGER; -- 已完成任务数

    DECLARE v_finished_activities_cursor  CURSOR FOR (
         SELECT
            YEAR(`t1`.`end_date`) AS `group_year`,
            MONTH(`t1`.`end_date`) AS `group_month`,
            DAY(`t1`.`end_date`) AS `group_day`,
            WEEK_OF_YEAR(`t1`.`end_date`) AS `group_week`,
            `t1`.`entity_module_name` AS `module`,
            `t1`.`process_stage_name_cn` AS `stage`,
            `t1`.`process_name` AS `process`,
            `t1`.`team_name` AS `team_name`,
            `t1`.`task_package_name` AS `task_package_name`,
            COUNT(`t1`.`id`) AS `finished_count`

         FROM

         `ose_tasks`.`bpm_activity_instance` AS `t1`
         WHERE `t1`.`project_id` = v_project_id AND `t1`.`finish_state` = 2 -- 2:已完成； 1：未完成

         GROUP BY `group_year`, `group_month`, `group_day`, `group_week`,
                  `t1`.`entity_module_name`, `t1`.`process_stage_name_cn`, `t1`.`process_name`, `t1`.`team_name`, `t1`.`task_package_name`
      );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;



    OPEN v_finished_activities_cursor;
    REPEAT

      FETCH
        v_finished_activities_cursor
      INTO
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_module,
        v_stage,
        v_process,
        v_team_name,
        v_task_package_name,
        v_finished_count
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
          `task_package_name`,
          `value_03`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_ACTIVITIES_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            IFNULL(v_module, ''),
            IFNULL(v_stage, ''),
            IFNULL(v_process, ''),
            IFNULL(v_team_name, ''),
            IFNULL(v_task_package_name, '')
          ))),
          v_project_id,
          'WBS_ACTIVITIES_PROGRESS',
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
          v_team_name,
          v_task_package_name,
          v_finished_count
        )
        ON DUPLICATE KEY UPDATE
          `value_03`         = v_finished_count,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_finished_activities_cursor;

  END $$

  -- -----------------------------------------------------------------------------
  -- 任务工时数据归档。
  -- 统计任务工时到bpm_act_tasks表的created_at字段上。
  -- -----------------------------------------------------------------------------
  CREATE PROCEDURE `archive_activities_working_hours_of_project`(
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
    DECLARE v_stage                 VARCHAR(255); -- 工序阶段
    DECLARE v_process               VARCHAR(255); -- 工序
    DECLARE v_team_name             VARCHAR(255); -- 班组名
    DECLARE v_task_package_name     VARCHAR(255); -- 任务包名
    DECLARE v_working_hours         INTEGER; -- 任务工时

    DECLARE v_activities_working_hours_cursor  CURSOR FOR (
         SELECT
            YEAR(`t2`.`created_at`) AS `group_year`,
            MONTH(`t2`.`created_at`) AS `group_month`,
            DAY(`t2`.`created_at`) AS `group_day`,
            WEEK_OF_YEAR(`t2`.`created_at`) AS `group_week`,
            `t1`.`entity_module_name` AS `module`,
            `t1`.`process_stage_name_cn` AS `stage`,
            `t1`.`a.process_name` AS `process`,
            `t1`.`team_name` AS `team_name`,
            `t1`.`task_package_name` AS `task_package_name`,
            SUM(`t2`.`cost_hour`) AS `working_hours`

         FROM

         `ose_tasks`.`bpm_activity_instance` AS `t1`
         JOIN `ose_tasks`.`bpm_act_task` AS `t2` ON `t2`.`act_inst_id` = `t1`.`act_inst_id`
         WHERE `t1`.`project_id` = v_project_id

         GROUP BY `group_year`, `group_month`, `group_day`, `group_week`,
                  `t1`.`entity_module_name`, `t1`.`process_stage_name_cn`, `t1`.`a.process_name`, `t1`.`team_name`, `t1`.`task_package_name`
      );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;



    OPEN v_activities_working_hours_cursor;
    REPEAT

      FETCH
        v_activities_working_hours_cursor
      INTO
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_module,
        v_stage,
        v_process,
        v_team_name,
        v_task_package_name,
        v_working_hours
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
          `task_package_name`,
          `value_04`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_ACTIVITIES_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            IFNULL(v_module, ''),
            IFNULL(v_stage, ''),
            IFNULL(v_process, ''),
            IFNULL(v_team_name, ''),
            IFNULL(v_task_package_name, '')
          ))),
          v_project_id,
          'WBS_ACTIVITIES_PROGRESS',
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
          v_team_name,
          v_task_package_name,
          v_working_hours
        )
        ON DUPLICATE KEY UPDATE
          `value_04`         = v_working_hours,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_activities_working_hours_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 任务处理状况归档定时任务。
-- 执行频率：每日凌晨03:45
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_activities_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2018-11-01 03:45:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前10分钟
    CALL `archive_activities`(ADDTIME(NOW(), -60000));
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
