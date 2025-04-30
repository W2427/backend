-- -----------------------------------------------------------------------------
-- 数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.detail_design_drawing, bpm_entity_sub_type, bpm_entity_type
-- 输出目标　：ose_report.statistics
-- 创建时间　：2019-05-06
-- 创建者　　：卢杨
-- -----------------------------------------------------------------------------
USE `ose_report` ;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_detail_design_daily`;
DROP PROCEDURE IF EXISTS `archive_detail_design`;
DROP PROCEDURE IF EXISTS `archive_detail_design_of_project`;
DROP PROCEDURE IF EXISTS `archive_detail_design_finished_daily`;
DROP PROCEDURE IF EXISTS `archive_detail_design_created_daily`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计项目：生产设计 / 详细设计状态
-- 统计对象：detail_design_drawing
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_detail_design`(IN v_archive_time TIMESTAMP)
BEGIN

    DECLARE v_project_id      BIGINT(20);
    DECLARE v_cursor_finished INTEGER DEFAULT 0;

    DECLARE v_project_cursor  CURSOR FOR (
      SELECT
        DISTINCT `ddl`.`project_id`
      FROM
        `ose_tasks`.`detail_design_drawing` AS `ddl`
    );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET v_cursor_finished = 0;

    OPEN v_project_cursor;
    REPEAT

      FETCH v_project_cursor INTO v_project_id;

      IF !v_cursor_finished THEN
        CALL `archive_detail_design_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END$$

-- -----------------------------------------------------------------------------
-- 生产设计详细设计状态统计
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_detail_design_of_project`(
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
      `code` = 'ARCHIVE_DETAIL_DESIGN'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_DETAIL_DESIGN', '数据归档：生产设计 / 详细设计状态', 'RUNNING');

    -- 为防止当天有业务数据减少的情况，归档数据改为先删除，后插入
    DELETE FROM `ose_report`.`statistics`
    WHERE `project_id` = v_project_id
      AND `archive_year`  = YEAR(v_archive_time)
      AND `archive_month` = MONTH(v_archive_time)
      AND `archive_day`   = DAY(v_archive_time)
      AND `archive_type`  = 'DETAIL_DESIGN';

    -- 归档创建数据
    CALL `archive_detail_design_created_daily`(v_archive_time, v_project_id);

    -- 归档已完成数据
    CALL `archive_detail_design_finished_daily`(v_archive_time, v_project_id);

    -- 更新执行记录
    UPDATE
      `ose_tasks`.`schedule_log`
    SET
      `finished_at` = CURRENT_TIMESTAMP(),
      `status` = 'FINISHED'
    WHERE
      `id` = @schedule_id
    ;

  END$$

-- -----------------------------------------------------------------------------
-- 统计创建数据
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_detail_design_created_daily`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
BEGIN

    DECLARE v_group_year      INTEGER;
    DECLARE v_group_month     INTEGER;
    DECLARE v_group_day       INTEGER;
    DECLARE v_group_week      INTEGER;
    DECLARE v_discipline_code VARCHAR(255);
    DECLARE v_document_type   VARCHAR(255);
    DECLARE v_name_cn         VARCHAR(255);
    DECLARE v_created_count	  INTEGER;
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_archive_timestamp TIMESTAMP;

    DECLARE v_wbs_cursor  CURSOR FOR (
      SELECT
        `e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`discipline_code`,
        `e`.`document_type`,
        `e`.`name_cn`,
        COUNT(`e`.`id`) AS `created_count`
      FROM
        (
          SELECT
            YEAR(`ddl`.`created_at`) AS `year`,
            MONTH(`ddl`.`created_at`) AS `month`,
            DAY(`ddl`.`created_at`) AS `day`,
            WEEK_OF_YEAR(`ddl`.`created_at`) AS `week`,
            `ddl`.`discipline_code`,
            `ddl`.`document_type`,
            `bec`.`name_cn`,
            `ddl`.`id`
          FROM
            `ose_tasks`.`detail_design_drawing` `ddl`
            INNER JOIN `ose_tasks`.`bpm_entity_sub_type` `bec`
               ON `bec`.`id` = `ddl`.`enginering_category`
              AND `bec`.`project_id` = `ddl`.`project_id`
              AND `bec`.`status` = 'ACTIVE'
            INNER JOIN `ose_tasks`.`bpm_entity_type` `bect`
               ON `bect`.`id` = `bec`.`entity_category_type_id`
              AND `bect`.`project_id` = `bec`.`project_id`
              AND `bect`.`status` = 'ACTIVE'
              AND `bect`.`type` = 'READONLY'
              AND `bect`.`name_en` = 'DETAIL_DRAWING'
          WHERE `ddl`.`project_id` = v_project_id
            AND `ddl`.`status` = 'ACTIVE'
        ) e
      GROUP BY `e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`discipline_code`,
        `e`.`document_type`,
        `e`.`name_cn`
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
          v_discipline_code,
          v_document_type,
          v_name_cn,
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
            `discipline_code`,
            `document_type`,
            `engineering_category`,
            `value_01`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id,
              '\DETAIL_DESIGN\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT(v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              CONCAT(v_discipline_code, '\n', v_document_type, '\n', v_name_cn)
            ))),
            v_project_id,
            'DETAIL_DESIGN',
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
            v_discipline_code,
            v_document_type,
            v_name_cn,
            v_created_count
          )
          ON DUPLICATE KEY UPDATE
            `value_01`         = v_created_count,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_wbs_cursor;

  END$$

-- -----------------------------------------------------------------------------
-- 统计已完成数据
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_detail_design_finished_daily`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
BEGIN

    DECLARE v_group_year      INTEGER;
    DECLARE v_group_month     INTEGER;
    DECLARE v_group_day       INTEGER;
    DECLARE v_group_week      INTEGER;
    DECLARE v_discipline_code VARCHAR(255);
    DECLARE v_document_type   VARCHAR(255);
    DECLARE v_name_cn         VARCHAR(255);
    DECLARE v_finished_coun   INTEGER;
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_archive_timestamp TIMESTAMP;

    DECLARE v_wbs_cursor  CURSOR FOR (
      SELECT
        `e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`discipline_code`,
        `e`.`document_type`,
        `e`.`name_cn`,
        COUNT(`e`.`id`) AS `finished_coun`
      FROM
        (
          SELECT
            YEAR(`ddl`.`last_modified_at`) AS `year`,
            MONTH(`ddl`.`last_modified_at`) AS `month`,
            DAY(`ddl`.`last_modified_at`) AS `day`,
            WEEK_OF_YEAR(`ddl`.`last_modified_at`) AS `week`,
            `ddl`.`discipline_code`,
            `ddl`.`document_type`,
            `bec`.`name_cn`,
            `ddl`.`id`
          FROM
            `ose_tasks`.`detail_design_drawing` `ddl`
            INNER JOIN `ose_tasks`.`bpm_entity_sub_type` `bec`
               ON `bec`.`id` = `ddl`.`enginering_category`
              AND `bec`.`project_id` = `ddl`.`project_id`
              AND `bec`.`status` = 'ACTIVE'
            INNER JOIN `ose_tasks`.`bpm_entity_type` `bect`
               ON `bect`.`id` = `bec`.`entity_category_type_id`
              AND `bect`.`project_id` = `bec`.`project_id`
              AND `bect`.`status` = 'ACTIVE'
              AND `bect`.`type` = 'READONLY'
              AND `bect`.`name_en` = 'DETAIL_DRAWING'
          WHERE `ddl`.`project_id` = v_project_id
            AND `ddl`.`status` = 'ACTIVE'
        ) e
      GROUP BY `e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`discipline_code`,
        `e`.`document_type`,
        `e`.`name_cn`
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
          v_discipline_code,
          v_document_type,
          v_name_cn,
          v_finished_coun
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
            `discipline_code`,
            `document_type`,
            `engineering_category`,
            `value_02`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id,
              '\DETAIL_DESIGN\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT(v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              CONCAT(v_discipline_code, '\n', v_document_type, '\n', v_name_cn)
            ))),
            v_project_id,
            'DETAIL_DESIGN',
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
            v_discipline_code,
            v_document_type,
            v_name_cn,
            v_finished_coun
          )
          ON DUPLICATE KEY UPDATE
            `value_02`         = v_finished_coun,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_wbs_cursor;

  END$$

-- -----------------------------------------------------------------------------
-- 生产设计详细设计状态数据归档定时任务。
-- 执行频率：每日凌晨03:30
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_detail_design_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2018-11-01 03:30:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前6小时
    CALL `archive_detail_design`(ADDTIME(NOW(), -60000));
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
