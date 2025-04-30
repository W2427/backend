-- -----------------------------------------------------------------------------
-- 数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.entity_weld, ose_tasks.welder, ose_auth.users,
-- 输出目标　：ose_report.statistics
-- 创建时间　：2019-01-17
-- 创建者　　：梅洛谊
-- -----------------------------------------------------------------------------

USE `ose_report`;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_welder_daily`;
DROP PROCEDURE IF EXISTS `archive_welder`;
DROP PROCEDURE IF EXISTS `archive_welder_of_project`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计对象以外的SQL，没有RUNNING在项目中

-- 焊工合格率统计。
-- 统计对象：焊口，焊工等信息。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_welder`(IN v_archive_time TIMESTAMP)
  BEGIN

    DECLARE v_project_id      BIGINT(20);
    DECLARE v_cursor_finished INTEGER DEFAULT 0;

    DECLARE v_project_cursor  CURSOR FOR (
      SELECT
        distinct `id`
      FROM
        `ose_tasks`.`projects`
    );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET v_cursor_finished = 0;

    OPEN v_project_cursor;
    REPEAT

      FETCH v_project_cursor INTO v_project_id;

      IF !v_cursor_finished THEN
        CALL `archive_welder_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 焊工合格率统计数据归档。
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_welder_of_project`(
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
    DECLARE v_welder_id             VARCHAR(255); -- 焊工ID
    DECLARE v_welder_name             VARCHAR(255); -- 焊工名
    DECLARE v_nps                 VARCHAR(255); -- 寸径
    DECLARE v_material               VARCHAR(255); -- 材质
    DECLARE v_nps_qualified       DOUBLE; -- 合格寸径
    DECLARE v_qualified_count     DOUBLE; -- 合格个数
    DECLARE v_nps_failed        DOUBLE; -- 不合格寸径
    DECLARE v_failed_count        DOUBLE; -- 不合格个数
    DECLARE v_cursor_finished       INTEGER DEFAULT 0;

    DECLARE v_welder_cursor  CURSOR FOR (
      -- 以当前日期为基准，统计焊工合格率
      -- TODO
      SELECT
            `ew`.`welder_id`,
            `u`.`name` AS `name`,
            `ew`.`nps` AS `nps`,
            `ew`.`material_code1` AS `entity_material`,
            YEAR(`ew`.`last_modified_at`) AS group_year,
            MONTH(`ew`.`last_modified_at`) AS group_month,
            DAY(`ew`.`last_modified_at`) AS group_day,
            WEEK_OF_YEAR(`ew`.`last_modified_at`) AS group_week,
            SUM(IF(`ew`.`ndt_result` = 'OK', nps, 0)) AS nps_qualified,
            SUM(IF(`ew`.`ndt_result` = 'OK', 1, 0)) AS qualified_count,
            SUM(IF(`ew`.`ndt_result` = 'NG', nps, 0)) AS nps_failed,
            SUM(IF(`ew`.`ndt_result` = 'NG', 1, 0)) AS failed_count
      FROM
       `ose_tasks`.`entity_weld` AS `ew`
       JOIN `ose_tasks`.`welders` AS `w` ON `ew`.`welder_id` = `w`.`id` AND `ew`.`project_id` = `w`.`project_id`
       JOIN `ose_auth`.`users` AS `u` ON `w`.`user_id` = `u`.`id`

      WHERE `ew`.`project_id` = v_project_id AND `ew`.`deleted` = FALSE

       group by `ew`.`project_id`, `ew`.`welder_id`, `u`.`name`, `nps`, `entity_material`, `group_year`, `group_month`, `group_day`, `group_week`
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
      `code` = 'ARCHIVE_WBS_WELDER_RATE'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_WELDER_RATE', '焊工合格率', 'RUNNING');

    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;

    OPEN v_welder_cursor;
    REPEAT

      FETCH
        v_welder_cursor
      INTO
        v_welder_id,
        v_welder_name,
        v_nps,
        v_material,
        v_group_year,
        v_group_month,
        v_group_day,
        v_group_week,
        v_nps_qualified,
        v_qualified_count,
        v_nps_failed,
        v_failed_count
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
          `welder_id`,
          `welder_name`,
          `entity_nps`,
          `entity_material`,
          `value_01`,
          `value_02`,
          `value_03`,
          `value_04`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_WELDER_RATE\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            IFNULL(v_welder_id, '')
          ))),
          v_project_id,
          'WBS_WELDER_RATE',
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
          v_welder_id,
          v_welder_name,
          v_nps,
          v_material,
          v_nps_qualified,
          v_qualified_count,
          v_nps_failed,
          v_failed_count
        )
        ON DUPLICATE KEY UPDATE
          `value_01`         = v_nps_qualified,
          `value_02`         = v_qualified_count,
          `value_03`         = v_nps_failed,
          `value_04`         = v_failed_count,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_welder_cursor;

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
-- 焊工合格率数据归档定时任务。
-- 执行频率：每日凌晨零点45分
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_welder_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2200-11-01 00:45:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前6小时
    CALL `archive_welder`(ADDTIME(NOW(), -60000));
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
