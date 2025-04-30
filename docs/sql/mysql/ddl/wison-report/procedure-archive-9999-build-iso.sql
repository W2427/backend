-- -----------------------------------------------------------------------------
-- 数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.wbs_entry, ose_tasks.entity_iso
-- 输出目标　：ose_report.statistics
-- 创建时间　：2019-01-16
-- 创建者　　：梅洛谊
-- -----------------------------------------------------------------------------

USE `ose_report`;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_build_iso_daily`;
DROP PROCEDURE IF EXISTS `archive_build_iso`;
DROP PROCEDURE IF EXISTS `archive_build_iso_ptp_of_project`;
DROP PROCEDURE IF EXISTS `archive_build_iso_clean_of_project`;
DROP PROCEDURE IF EXISTS `archive_build_iso_insulation_of_project`;
DROP PROCEDURE IF EXISTS `archive_build_iso_tightness_of_project`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计对象以外的SQL，没有RUNNING在项目中

-- 管线试压完成量统计。
-- 统计对象：所有尚未结束的项目的四级计划。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_build_iso`(IN v_archive_time TIMESTAMP)
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
            `w`.`project_id`,
            COUNT(`id`) AS `count`
          FROM
            `ose_tasks`.`wbs_entry` AS `w`
          WHERE
            `entity_type` = 'ISO'
            AND `deleted` = 0
            AND `active` = 1
            AND `finished` = 0
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
        CALL `archive_build_iso_ptp_of_project`(v_archive_time, v_project_id);
        CALL `archive_build_iso_clean_of_project`(v_archive_time, v_project_id);
        CALL `archive_build_iso_insulation_of_project`(v_archive_time, v_project_id);
        CALL `archive_build_iso_tightness_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 项目管线建造完成量统计数据归档。
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_build_iso_ptp_of_project`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
  BEGIN_LABEL:BEGIN

    DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
    DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;
    DECLARE v_group_year            INTEGER;
    DECLARE v_group_month           INTEGER;
    DECLARE v_group_day             INTEGER;
    DECLARE v_group_week            INTEGER;
    DECLARE v_module                VARCHAR(255); -- 模块
    DECLARE v_ptp_no                VARCHAR(255); -- 试压包号
    DECLARE v_nps_unfinished     DOUBLE; -- 未完成数量
    DECLARE v_nps_in_process     DOUBLE; -- 进行中数量
    DECLARE v_nps_finished       DOUBLE; -- 已完成数量
    DECLARE v_cursor_finished       INTEGER DEFAULT 0;

    DECLARE v_iso_cursor  CURSOR FOR (
      -- 以最后完成日期为单位，计算所有四级计划单管施工完成量，未完成数量
      SELECT
            `pn`.`no`        AS `module_no`,
            `pnptp2`.`no`    AS `ptp_no`,
            YEAR(we.finished_at) AS group_year,
            MONTH(we.finished_at) AS group_month,
            DAY(we.finished_at) AS group_day,
            WEEK_OF_YEAR(we.finished_at) AS group_week,
            SUM(IF(`we`.`finished` = 0 AND we.started_at is null, iso.nps, 0)) AS nps_unfinished,
            SUM(IF(`we`.`finished` = 0 AND we.started_at < v_archive_time, iso.nps, 0)) AS nps_in_process,
            SUM(IF(`we`.`finished` = 1, iso.nps, 0)) AS nps_finished
      FROM
       ose_tasks.wbs_entry we
       INNER JOIN ose_tasks.entity_iso iso ON we.entity_id = iso.id
       INNER JOIN ose_tasks.hierarchy_node hn ON we.module_hierarchy_node_id = hn.id AND we.project_id = hn.project_id
       INNER JOIN ose_tasks.project_node pn ON hn.node_id = pn.id AND pn.project_id = hn.project_id

       LEFT JOIN ose_tasks.project_node pnptp ON we.entity_id = pnptp.entity_id AND we.project_id = pnptp.project_id
       LEFT JOIN ose_tasks.hierarchy_node hnptp ON hnptp.node_id = pnptp.id AND pnptp.project_id = hnptp.project_id AND hnptp.hierarchy_type = 'PRESSURE_TEST_PACKAGE'
       LEFT JOIN ose_tasks.hierarchy_node hnptp2 ON hnptp2.id = hnptp.parent_id
       LEFT JOIN ose_tasks.project_node pnptp2 ON hnptp2.node_id = pnptp2.id AND pnptp2.project_id = hnptp2.project_id

      WHERE we.project_id = v_project_id AND we.stage = "PRESSURETEST"

       group by we.project_id, module_no, ptp_no, group_year, group_month, group_day, group_week
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
      `code` = 'ARCHIVE_WBS_PRESSURE_TEST_PROGRESS'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_PRESSURE_TEST_PROGRESS', '试压进度数据归档', 'RUNNING');

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;

    OPEN v_iso_cursor;
    REPEAT

      FETCH
        v_iso_cursor
      INTO
        v_module,
        v_ptp_no,
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_nps_unfinished,
        v_nps_in_process,
        v_nps_finished
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
          `pressure_test_package`,
          `value_01`,
          `value_02`,
          `value_03`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_PRESSURE_TEST_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            v_module
          ))),
          v_project_id,
          'WBS_PRESSURE_TEST_PROGRESS',
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
          v_ptp_no,
          v_nps_unfinished,
          v_nps_in_process,
          v_nps_finished
        )
        ON DUPLICATE KEY UPDATE
          `value_01`         = v_nps_unfinished,
          `value_02`         = v_nps_in_process,
          `value_03`         = v_nps_finished,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_iso_cursor;

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
  -- 项目管线清洁完成量统计数据归档。
  -- 为项目创建定时任务执行记录。
  -- -----------------------------------------------------------------------------
  CREATE PROCEDURE `archive_build_iso_clean_of_project`(
    IN v_archive_time TIMESTAMP,  -- 归档时间
    IN v_project_id   BIGINT(20) -- 项目 ID
  )
    BEGIN_LABEL:BEGIN

      DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
      DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;
      DECLARE v_group_year            INTEGER;
      DECLARE v_group_month           INTEGER;
      DECLARE v_group_day             INTEGER;
      DECLARE v_group_week            INTEGER;
      DECLARE v_module                VARCHAR(255); -- 模块
      DECLARE v_nps_unfinished     DOUBLE; -- 未完成数量
      DECLARE v_nps_in_process     DOUBLE; -- 进行中数量
      DECLARE v_nps_finished       DOUBLE; -- 已完成数量
      DECLARE v_cursor_finished       INTEGER DEFAULT 0;

      DECLARE v_iso_cursor  CURSOR FOR (
        -- 以最后完成日期为单位，计算所有四级计划管线清洁完成量，未完成数量
        SELECT
              `pn`.`no`        AS `module_no`,
              YEAR(we.finished_at) AS group_year,
              MONTH(we.finished_at) AS group_month,
              DAY(we.finished_at) AS group_day,
              WEEK_OF_YEAR(we.finished_at) AS group_week,
              SUM(IF(`we`.`finished` = 0 AND we.started_at is null, iso.nps, 0)) AS nps_unfinished,
              SUM(IF(`we`.`finished` = 0 AND we.started_at < v_archive_time, iso.nps, 0)) AS nps_in_process,
              SUM(IF(`we`.`finished` = 1, iso.nps, 0)) AS nps_finished
        FROM
         ose_tasks.wbs_entry we
         INNER JOIN ose_tasks.entity_iso iso ON we.entity_id = iso.id
         INNER JOIN ose_tasks.hierarchy_node hn ON we.module_hierarchy_node_id = hn.id AND we.project_id = hn.project_id
         INNER JOIN ose_tasks.project_node pn ON hn.node_id = pn.id AND pn.project_id = hn.project_id

        WHERE we.project_id = v_project_id AND we.stage = "CLEAN"

         group by we.project_id, module_no, group_year, group_month, group_day, group_week
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
        `code` = 'ARCHIVE_WBS_CLEAN_PROGRESS'
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
      VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_CLEAN_PROGRESS', '清洁进度数据归档', 'RUNNING');

      SET @archive_year     = YEAR(v_archive_time);
      SET @archive_month    = MONTH(v_archive_time);
      SET @archive_day      = DAY(v_archive_time);
      SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
      SET v_cursor_finished = 0;

      OPEN v_iso_cursor;
      REPEAT

        FETCH
          v_iso_cursor
        INTO
          v_module,
          v_group_year,
          v_group_month,
          v_group_day,
          v_group_week,
          v_nps_unfinished,
          v_nps_in_process,
          v_nps_finished
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
            `value_01`,
            `value_02`,
            `value_03`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\nWBS_CLEAN_PROGRESS\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              v_module
            ))),
            v_project_id,
            'WBS_CLEAN_PROGRESS',
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
            v_nps_unfinished,
            v_nps_in_process,
            v_nps_finished
          )
          ON DUPLICATE KEY UPDATE
            `value_01`         = v_nps_unfinished,
            `value_02`         = v_nps_in_process,
            `value_03`         = v_nps_finished,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
      CLOSE v_iso_cursor;

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
-- 项目管线保温保冷完成量统计数据归档。
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_build_iso_insulation_of_project`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
  BEGIN_LABEL:BEGIN

    DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
    DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;
    DECLARE v_group_year            INTEGER;
    DECLARE v_group_month           INTEGER;
    DECLARE v_group_day             INTEGER;
    DECLARE v_group_week            INTEGER;
    DECLARE v_module                VARCHAR(255); -- 模块
    DECLARE v_stage                 VARCHAR(255); -- 工序阶段
    DECLARE v_process               VARCHAR(255); -- 工序
    DECLARE v_nps_unfinished     DOUBLE; -- 未完成数量
    DECLARE v_nps_in_process     DOUBLE; -- 进行中数量
    DECLARE v_nps_finished       DOUBLE; -- 已完成数量
    DECLARE v_cursor_finished       INTEGER DEFAULT 0;

    DECLARE v_iso_cursor  CURSOR FOR (
      -- 以最后完成日期为单位，计算所有四级计划管线保温保冷完成量，未完成数量
      SELECT
            `pn`.`no`        AS `module_no`,
            `we`.`stage`     AS `stage`,
            `we`.`process`   AS `process`,
            YEAR(we.finished_at) AS group_year,
            MONTH(we.finished_at) AS group_month,
            DAY(we.finished_at) AS group_day,
            WEEK_OF_YEAR(we.finished_at) AS group_week,
            SUM(IF(`we`.`finished` = 0 AND we.started_at is null, iso.nps, 0)) AS nps_unfinished,
            SUM(IF(`we`.`finished` = 0 AND we.started_at < v_archive_time, iso.nps, 0)) AS nps_in_process,
            SUM(IF(`we`.`finished` = 1, iso.nps, 0)) AS nps_finished
      FROM
       ose_tasks.wbs_entry we
       INNER JOIN ose_tasks.entity_iso iso ON we.entity_id = iso.id
       INNER JOIN ose_tasks.hierarchy_node hn ON we.module_hierarchy_node_id = hn.id AND we.project_id = hn.project_id
       INNER JOIN ose_tasks.project_node pn ON hn.node_id = pn.id AND pn.project_id = hn.project_id

      WHERE we.project_id = v_project_id AND we.stage = "INSULATION"

       group by we.project_id, module_no, stage, process, group_year, group_month, group_day, group_week
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
      `code` = 'ARCHIVE_WBS_INSULATION_PROGRESS'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_INSULATION_PROGRESS', '保温保冷进度数据归档', 'RUNNING');

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;

    OPEN v_iso_cursor;
    REPEAT

      FETCH
        v_iso_cursor
      INTO
        v_module,
        v_stage,
        v_process,
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_nps_unfinished,
        v_nps_in_process,
        v_nps_finished
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
          `value_02`,
          `value_03`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_INSULATION_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            v_module
          ))),
          v_project_id,
          'WBS_INSULATION_PROGRESS',
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
          v_nps_unfinished,
          v_nps_in_process,
          v_nps_finished
        )
        ON DUPLICATE KEY UPDATE
          `value_01`         = v_nps_unfinished,
          `value_02`         = v_nps_in_process,
          `value_03`         = v_nps_finished,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_iso_cursor;

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
  -- 项目系统密性进度完成量统计数据归档。
  -- 为项目创建定时任务执行记录。
  -- -----------------------------------------------------------------------------
  CREATE PROCEDURE `archive_build_iso_tightness_of_project`(
    IN v_archive_time TIMESTAMP,  -- 归档时间
    IN v_project_id   BIGINT(20) -- 项目 ID
  )
    BEGIN_LABEL:BEGIN

      DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
      DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;
      DECLARE v_group_year            INTEGER;
      DECLARE v_group_month           INTEGER;
      DECLARE v_group_day             INTEGER;
      DECLARE v_group_week            INTEGER;
      DECLARE v_sub_system_no         VARCHAR(255); -- 子系统
      DECLARE v_nps_unfinished     DOUBLE; -- 未完成数量
      DECLARE v_nps_in_process     DOUBLE; -- 进行中数量
      DECLARE v_nps_finished       DOUBLE; -- 已完成数量
      DECLARE v_cursor_finished       INTEGER DEFAULT 0;

      DECLARE v_iso_cursor  CURSOR FOR (
        -- 以最后完成日期为单位，计算所有四级计划系统密性进度完成量，未完成数量
        SELECT
              `pnss`.`no`        AS `sub_system_no`,
              YEAR(we.finished_at) AS group_year,
              MONTH(we.finished_at) AS group_month,
              DAY(we.finished_at) AS group_day,
              WEEK_OF_YEAR(we.finished_at) AS group_week,
              SUM(IF(`we`.`finished` = 0 AND we.started_at is null, iso.nps, 0)) AS nps_unfinished,
              SUM(IF(`we`.`finished` = 0 AND we.started_at < v_archive_time, iso.nps, 0)) AS nps_in_process,
              SUM(IF(`we`.`finished` = 1, iso.nps, 0)) AS nps_finished
        FROM
         ose_tasks.wbs_entry we
         INNER JOIN ose_tasks.entity_iso iso ON we.entity_id = iso.id
         INNER JOIN ose_tasks.hierarchy_node hn ON we.entity_id = pn.entity_id AND we.project_id = hn.project_id
         LEFT JOIN ose_tasks.project_node pn ON hn.node_id = pn.id AND pn.project_id = hn.project_id AND hn.hierarchy_type = 'SUB_SYSTEM'
         -- 子系统信息
         LEFT JOIN ose_tasks.hierarchy_node hnss ON hnss.id = hn.parent_id AND pn.project_id = hn.project_id AND hn.hierarchy_type = 'SUB_SYSTEM'
         LEFT JOIN ose_tasks.project_node pnss ON pnss.id = hnss.node_id AND pnss.project_id = hnss.project_id

        WHERE we.project_id = v_project_id AND we.stage = "TIGHTNESS"

         group by we.project_id, sub_system_no, group_year, group_month, group_day, group_week
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
        `code` = 'ARCHIVE_WBS_TIGHTNESS_PROGRESS'
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
      VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_TIGHTNESS_PROGRESS', '系统密性进度数据归档', 'RUNNING');

      SET @archive_year     = YEAR(v_archive_time);
      SET @archive_month    = MONTH(v_archive_time);
      SET @archive_day      = DAY(v_archive_time);
      SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
      SET v_cursor_finished = 0;

      OPEN v_iso_cursor;
      REPEAT

        FETCH
          v_iso_cursor
        INTO
          v_sub_system_no,
          v_group_year,
          v_group_month,
          v_group_day,
          v_group_week,
          v_nps_unfinished,
          v_nps_in_process,
          v_nps_finished
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
            `sub_system`,
            `value_01`,
            `value_02`,
            `value_03`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\nWBS_TIGHTNESS_PROGRESS\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              v_module
            ))),
            v_project_id,
            'WBS_TIGHTNESS_PROGRESS',
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
            v_sub_system_no,
            v_nps_unfinished,
            v_nps_in_process,
            v_nps_finished
          )
          ON DUPLICATE KEY UPDATE
            `value_01`         = v_nps_unfinished,
            `value_02`         = v_nps_in_process,
            `value_03`         = v_nps_finished,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
      CLOSE v_iso_cursor;

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
-- 管线建造完成量数据归档定时任务。
-- 执行频率：每日凌晨零点30分
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_build_iso_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2200-11-01 00:30:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前6小时
    CALL `archive_build_iso`(ADDTIME(NOW(), -60000));
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
