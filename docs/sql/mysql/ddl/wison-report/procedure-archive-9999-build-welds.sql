-- -----------------------------------------------------------------------------
-- 数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.wbs_entry, ose_tasks.entity_weld
-- 输出目标　：ose_report.statistics
-- 创建时间　：2018-11-30
-- 创建者　　：金海岩
-- -----------------------------------------------------------------------------

USE `ose_report`;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_build_welds_daily`;
DROP PROCEDURE IF EXISTS `archive_build_welds`;
DROP PROCEDURE IF EXISTS `archive_build_welds_of_project`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计对象以外的SQL，没有RUNNING在项目中

-- 焊口建造完成量统计。
-- 统计对象：所有尚未结束的项目的四级计划。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_build_welds`(IN v_archive_time TIMESTAMP)
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
            `ose_tasks`.`wbs_entry` `w`
          WHERE
            `entity_type` = 'WELD_JOINT'
            AND `deleted` = 0
            AND `active` = 1
            AND `finished` = 1
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
        CALL `archive_build_welds_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 项目焊口建造完成量统计数据归档。
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_build_welds_of_project`(
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
    DECLARE v_module                VARCHAR(255); -- 代表模块
    DECLARE v_weld_type             VARCHAR(255);
    DECLARE v_material              VARCHAR(255);
    DECLARE v_subconstractor        VARCHAR(255);
    DECLARE v_count                 DOUBLE;
    DECLARE v_nps                   DOUBLE;
    DECLARE v_cursor_finished       INTEGER DEFAULT 0;

    DECLARE v_weld_cursor  CURSOR FOR (
      -- 以最后完成日期为单位，计算所有四级计划均已完成的焊口的个数及寸径总和
      SELECT
        `w`.`year`,
        `w`.`month`,
        `w`.`day`,
        `w`.`week`,
        `w`.`module_no`,
        `w`.`weld_type`,
        `w`.`material`,
        `w`.`subcontractor`,
        COUNT(`w`.`id`) AS `count`,
        SUM(`w`.`nps`)  AS `nps`
      FROM
       (
          -- 筛选所有四级计划均已完成的焊口
          SELECT
            `w`.`id`,
            `w`.`module_no`,
            `w`.`weld_type`,
            `w`.`material`,
            `w`.`subcontractor`,
            `w`.`nps`,
            YEAR(`w`.`finished_at`)         AS `year`,
            MONTH(`w`.`finished_at`)        AS `month`,
            DAY(`w`.`finished_at`)          AS `day`,
            WEEK_OF_YEAR(`w`.`finished_at`) AS `week`
          FROM
            (
              -- 计算未每一个焊口的未完成的四级计划的件数，取得最后完成时间
              SELECT
                `w`.`id`,
                `w`.`module_no`,
                `w`.`weld_type`,
                `w`.`material`,
                `w`.`subcontractor`,
                `w`.`nps`,
                SUM(IF(`w`.`finished` = 0, 1, 0)) AS `not_finished_count`,
                MAX(`w`.`finished_at`)            AS `finished_at`
              FROM
                (
                  -- 取得所有焊口的四级计划
                  SELECT
                    `w`.`id`,
                    `mpn`.`no`           AS `module_no`,
                    `w`.`weld_type`,
                    `w`.`material_code1` AS `material`,
                    `sc`.`id`            AS `subcontractor`,
                    `w`.`nps`,
                    `e`.`finished`,
                    `e`.`finished_at`
                  FROM
                    `ose_tasks`.`wbs_entry` AS `e`
                    INNER JOIN `ose_tasks`.`entity_weld` AS `w`
                      ON `w`.`id` = `e`.`entity_id`
                      AND `w`.`deleted` = 0
                    INNER JOIN `ose_tasks`.`project_node` AS `p`
                      ON `p`.`entity_id` = `w`.`id`
                      AND `p`.`deleted` = 0
                    INNER JOIN `ose_tasks`.`hierarchy_node` AS `hn`
                      ON `hn`.`node_id` = `p`.`id`
                      AND `hn`.`deleted` = 0
                    INNER JOIN `ose_tasks`.`hierarchy_node` AS `mhn`
                      ON `mhn`.`project_id` = `e`.`project_id`
                      AND `mhn`.`deleted` = 0
                      AND `hn`.`path` LIKE CONCAT(`mhn`.`path`, `mhn`.`id`, '%')
                    INNER JOIN `ose_tasks`.`project_node` AS `mpn`
                      ON `mpn`.`id` = `mhn`.`node_id`
                      AND `mpn`.`node_type` = 'MODULE'
                      AND `mpn`.`deleted` = 0
                    LEFT OUTER JOIN `ose_tasks`.`welder` AS `wr`
                      ON `wr`.`id` = `w`.`welder_id`
                    LEFT OUTER JOIN `ose_tasks`.`subcon` AS `sc`
                      ON `sc`.`id` = `wr`.`sub_con`
                  WHERE
                    `e`.`project_id` = v_project_id
                    AND `e`.`deleted` = 0
                    AND `e`.`active` = 1
                ) AS `w`
              GROUP BY
                `w`.`id`,
                `w`.`module_no`,
                `w`.`weld_type`,
                `w`.`material`,
                `w`.`subcontractor`
            ) AS `w`
          WHERE
          `w`.`not_finished_count` = 0
        ) AS `w`
      GROUP BY
        `w`.`year`,
        `w`.`month`,
        `w`.`day`,
        `w`.`week`,
        `w`.`module_no`,
        `w`.`weld_type`,
        `w`.`material`,
        `w`.`subcontractor`
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
      `code` = 'ARCHIVE_WBS_WELD_PROGRESS'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_WELD_PROGRESS', '焊口建造完成量数据归档', 'RUNNING');

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;

    OPEN v_weld_cursor;
    REPEAT

      FETCH
        v_weld_cursor
      INTO
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_module,
        v_weld_type,
        v_material,
        v_subconstractor,
        v_count,
        v_nps
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
          `weld_type`,
          `material`,
          `subconstractor`,
          `value_01`,
          `value_02`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_WELD_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            CONCAT(v_module, '\n', v_weld_type, '\n', v_material, '\n', v_subconstractor)
          ))),
          v_project_id,
          'WBS_WELD_PROGRESS',
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
          v_weld_type,
          v_material,
          v_subconstractor,
          v_count,
          v_nps
        )
        ON DUPLICATE KEY UPDATE
          `value_01`         = v_total_score,
          `value_02`         = v_duration,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_weld_cursor;

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
-- 焊口建造完成量数据归档定时任务。
-- 执行频率：每日凌晨零点
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_build_welds_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2200-11-01 00:15:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前6小时
    CALL `archive_build_welds`(ADDTIME(NOW(), -60000));
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
