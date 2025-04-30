-- -----------------------------------------------------------------------------
-- 数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.wbs_entry
-- 输出目标　：ose_report.statistics
-- 创建时间　：2018-11-30
-- 创建者　　：金海岩
-- -----------------------------------------------------------------------------

USE `ose_report`;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_wbs_progress_daily`;
DROP PROCEDURE IF EXISTS `archive_wbs_progress`;
DROP PROCEDURE IF EXISTS `archive_wbs_progress_of_project`;
DROP PROCEDURE IF EXISTS `archive_wbs_progress_plan_daily`;
DROP PROCEDURE IF EXISTS `archive_wbs_progress_actual_daily`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计项目：建造计划 / 完成率 + 工时
-- 统计对象：所有尚未结束的项目的四级计划。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_wbs_progress`(IN v_archive_time TIMESTAMP)
  BEGIN

    DECLARE v_project_id      BIGINT(20);
    DECLARE v_cursor_finished INTEGER DEFAULT 0;

    DECLARE v_project_cursor  CURSOR FOR (
      SELECT
        `p`.`id`
      FROM
        `ose_tasks`.`projects` AS `p`
        INNER JOIN (
          SELECT
            `project_id`,
            COUNT(0) AS `count`
          FROM
            `ose_tasks`.`wbs_entry`
          WHERE
            `type` = 'ENTITY'
            AND `active` = 1
            AND (`finish_at` IS NOT NULL OR (`finished` = 1 AND `finished_at` IS NOT NULL))
            AND `deleted` = 0
          GROUP BY
            `project_id`
        ) AS `e`
          ON `e`.`project_id` = `p`.`id`
          AND `e`.`count` > 0
      WHERE
        `p`.`finished_at` IS NULL
        AND `p`.`deleted` = 0
    );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET v_cursor_finished = 0;

    OPEN v_project_cursor;
    REPEAT

      FETCH v_project_cursor INTO v_project_id;

      IF !v_cursor_finished THEN
        CALL `archive_wbs_progress_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 项目进度及工时数据归档。
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_wbs_progress_of_project`(
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
      `code` = 'ARCHIVE_WBS_PROGRESS'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_PROGRESS', '数据归档：建造计划 / 完成率，建造计划 / 工时', 'RUNNING');

    -- 为防止当天有业务数据减少的情况，归档数据改为先删除，后插入
    DELETE FROM `ose_report`.`statistics`
    WHERE `project_id` = v_project_id
      AND `archive_year`  = YEAR(v_archive_time)
      AND `archive_month` = MONTH(v_archive_time)
      AND `archive_day`   = DAY(v_archive_time)
      AND `archive_type`  = 'WBS_PROGRESS';

    -- 归档计划数据
    CALL `archive_wbs_progress_plan_daily`(v_archive_time, v_project_id);

    -- 归档实际数据
    CALL `archive_wbs_progress_actual_daily`(v_archive_time, v_project_id);

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
-- 项目计划进度及工时数据归档。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_wbs_progress_plan_daily`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
  BEGIN

    DECLARE v_group_year      INTEGER;
    DECLARE v_group_month     INTEGER;
    DECLARE v_group_day       INTEGER;
    DECLARE v_group_week      INTEGER;
    DECLARE v_sector          VARCHAR(255); -- 代表模块
    DECLARE v_stage           VARCHAR(255);
    DECLARE v_process         VARCHAR(255);
    DECLARE v_total_score     DOUBLE;
    DECLARE v_estimated_man_hours        DOUBLE;
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_archive_timestamp TIMESTAMP;

    DECLARE v_wbs_cursor  CURSOR FOR (
      SELECT
        `e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`sector`,
        `e`.`stage`,
        `e`.`process`,
        SUM(`e`.`total_score`) AS `total_score`,
        SUM(`e`.`estimated_man_hours`)    AS `estimated_man_hours`
      FROM (
        SELECT
          YEAR(`finish_at`)         AS `year`,
          MONTH(`finish_at`)        AS `month`,
          DAY(`finish_at`)          AS `day`,
          WEEK_OF_YEAR(`finish_at`) AS `week`,
          `sector`,
          `stage`,
          `process`,
          `total_score`,
          `estimated_man_hours`
        FROM
          `ose_tasks`.`wbs_entry`
        WHERE
          `project_id` = v_project_id
          AND `type` = 'ENTITY'
          AND `active` = 1
          AND `finish_at` IS NOT NULL
          AND `deleted` = 0
      ) AS `e`
      GROUP BY
        `e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`sector`,
        `e`.`stage`,
        `e`.`process`
    );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET @archive_year  = YEAR(v_archive_time);
    SET @archive_month = MONTH(v_archive_time);
    SET @archive_day   = DAY(v_archive_time);
    SET @archive_week  = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;
    SET v_archive_timestamp = CURRENT_TIMESTAMP();

    OPEN v_wbs_cursor;
      REPEAT

        FETCH
          v_wbs_cursor
        INTO
          v_group_year,
          v_group_month,
          v_group_day,
          v_group_week,
          v_sector,
          v_stage,
          v_process,
          v_total_score,
          v_estimated_man_hours
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
            `value_01`,
            `value_02`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\nWBS_PROGRESS\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              CONCAT(v_sector, '\n', v_stage, '\n', v_process)
            ))),
            v_project_id,
            'WBS_PROGRESS',
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
            v_sector,
            v_stage,
            v_process,
            v_total_score,
            v_estimated_man_hours
          )
          ON DUPLICATE KEY UPDATE
            `value_01`         = v_total_score,
            `value_02`         = v_estimated_man_hours,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_wbs_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 项目实际进度及工时数据归档。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_wbs_progress_actual_daily`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
  BEGIN

    DECLARE v_group_year      INTEGER;
    DECLARE v_group_month     INTEGER;
    DECLARE v_group_day       INTEGER;
    DECLARE v_group_week      INTEGER;
    DECLARE v_sector          VARCHAR(255); -- 代表模块
    DECLARE v_stage           VARCHAR(255);
    DECLARE v_process         VARCHAR(255);
    DECLARE v_finished_score  DOUBLE;
    DECLARE v_actual_man_hours DOUBLE;
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_archive_timestamp TIMESTAMP;

    DECLARE v_wbs_cursor  CURSOR FOR (
      SELECT
        `e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`sector`,
        `e`.`stage`,
        `e`.`process`,
        SUM(`e`.`finished_score`)   AS `finished_score`,
        SUM(`e`.`actual_man_hours`) AS `actual_man_hours`
      FROM (
        SELECT
          YEAR(`finished_at`)         AS `year`,
          MONTH(`finished_at`)        AS `month`,
          DAY(`finished_at`)          AS `day`,
          WEEK_OF_YEAR(`finished_at`) AS `week`,
          `sector`,
          `stage`,
          `process`,
          `finished_score`,
          `actual_man_hours`
        FROM
          `ose_tasks`.`wbs_entry`
        WHERE
          `project_id` = v_project_id
          AND `type` = 'ENTITY'
          AND `active` = 1
          AND `finished` = 1
          AND `finished_at` IS NOT NULL
          AND `deleted` = 0
      ) AS `e`
      GROUP BY
        `e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`sector`,
        `e`.`stage`,
        `e`.`process`
      );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET @archive_year  = YEAR(v_archive_time);
    SET @archive_month = MONTH(v_archive_time);
    SET @archive_day   = DAY(v_archive_time);
    SET @archive_week  = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;
    SET v_archive_timestamp = CURRENT_TIMESTAMP();

    OPEN v_wbs_cursor;
      REPEAT

        FETCH
          v_wbs_cursor
        INTO
          v_group_year,
          v_group_month,
          v_group_day,
          v_group_week,
          v_sector,
          v_stage,
          v_process,
          v_finished_score,
          v_actual_man_hours
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
            `value_03`,
            `value_04`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\nWBS_PROGRESS\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              CONCAT(v_sector, '\n', v_stage, '\n', v_process)
            ))),
            v_project_id,
            'WBS_PROGRESS',
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
            v_sector,
            v_stage,
            v_process,
            v_finished_score,
            v_actual_man_hours
          )
          ON DUPLICATE KEY UPDATE
            `value_03`         = v_finished_score,
            `value_04`         = v_actual_man_hours,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_wbs_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 计划完成率进度数据归档定时任务。
-- 执行频率：每日凌晨零点
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_wbs_progress_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2018-11-01 00:00:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前6小时
    CALL `archive_wbs_progress`(ADDTIME(NOW(), -60000));

    -- EVENT[archive_plan_daily_report_daily]中，会定义其他的启动时间，为计划日报归档数据
    -- 如有启动时间方面的修改，请结合日报的时间，一起考虑。
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
