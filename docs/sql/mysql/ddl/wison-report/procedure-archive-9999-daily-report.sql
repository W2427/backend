-- -----------------------------------------------------------------------------
-- 日报任务数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.bpm_activity_instance
-- 输出目标　：ose_report.statistics
-- 创建时间　：2019-03-14
-- 创建者　　：梅洛谊
-- -----------------------------------------------------------------------------

USE `ose_report`;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_activities_daily`;
DROP PROCEDURE IF EXISTS `archive_activities`;
DROP PROCEDURE IF EXISTS `archive_activities_of_project`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计对象以外的SQL，没有RUNNING在项目中

-- 任务数据统计。
-- 统计对象：任务。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_activities`(IN v_archive_time TIMESTAMP)
  BEGIN

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
        CALL `archive_activities_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 项目单管建造完成量统计数据归档。
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_activities_of_project`(
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
    DECLARE v_user_name             VARCHAR(255); -- 责任人名
    DECLARE v_unfinished_activities      INTEGER; -- 未完成任务
    DECLARE v_finished_activities        INTEGER; -- 已完成任务
    DECLARE v_total_activities             INTEGER; -- 全部任务

    DECLARE v_activities_cursor  CURSOR FOR (
         SELECT
--            `t1`.`tenant_id_` AS `project_id`, -- 工程ID
            `t2`.`entity_module_name` AS `module_name`, -- 任务所属模块名
            `u`.`name` AS `user_name`, -- 任务责任人
            YEAR(`t1`.`end_time_`) AS group_year,
            MONTH(`t1`.`end_time_`) AS group_month,
            DAY(`t1`.`end_time_`) AS group_day,
            WEEK_OF_YEAR(`t1`.`end_time_`) AS group_week,
            SUM(IF(`t1`.`end_time_` is null, 1, 0)) AS unfinished_activities,
            SUM(IF(`t1`.`end_time_` is not null AND (`t1`.`end_time_` < v_archive_time), 1, 0)) AS finished_activities,
            COUNT(`t1`.`id`) AS total_activities

         FROM

         `ose_tasks`.`bpm_hi_taskinst` AS `t1`
         JOIN `ose_tasks`.`bpm_activity_instance` AS `t2` ON `t1`.`act_inst_id` = `t2`.`act_inst_id` AND `t1`.`tenant_id_` = `t2`.`project_id`
         JOIN `ose_auth`.`users` AS `u` ON `t1`.`assignee_` = `u`.`id`
         WHERE `t1`.`tenant_id_` = v_project_id

         GROUP BY `t1`.`tenant_id_`, `t2`.`entity_module_name`, `u`.`name`, `group_year`, `group_month`, `group_day`, `group_week`
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_ACTIVITIES_PROGRESS', '日报任务归档', 'RUNNING');

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;

    OPEN v_activities_cursor;
    REPEAT

      FETCH
        v_activities_cursor
      INTO
        v_module,
        v_user_name,
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_unfinished_activities,
        v_finished_activities,
        v_total_activities
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
          `user_name`,
          `value_01`,
          `value_02`,
          `value_03`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_ACTIVITIES_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            IFNULL(v_module, '')
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
          v_user_name,
          v_unfinished_activities,
          v_finished_activities,
          v_total_activities
        )
        ON DUPLICATE KEY UPDATE
          `value_01`         = v_unfinished_activities,
          `value_02`         = v_finished_activities,
          `value_03`         = v_total_activities,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_activities_cursor;

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
-- 任务处理状况归档定时任务。
-- 执行频率：每日凌晨1点
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_activities_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2200-11-01 01:00:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前10分钟
    CALL `archive_activities`(ADDTIME(NOW(), -1000));
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
