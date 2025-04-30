-- -----------------------------------------------------------------------------
-- 数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.wbs_entry, ose_auth.organizations,
-- 输出目标　：ose_report.statistics
-- 创建时间　：2019-04-17
-- 创建者　　：白东旭
-- -----------------------------------------------------------------------------
USE `ose_report` ;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_wbs_design_hour_daily`;
DROP PROCEDURE IF EXISTS `archive_design_hour`;
DROP PROCEDURE IF EXISTS `archive_design_hour_of_project`;
DROP PROCEDURE IF EXISTS `archive_design_hour_finished_daily`;
DROP PROCEDURE IF EXISTS `archive_design_hour_plan_daily`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计项目：生产设计 / 工时
-- 统计对象：四级计划
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_design_hour`(IN v_archive_time TIMESTAMP)
BEGIN

    DECLARE v_project_id      BIGINT(20);
    DECLARE v_cursor_finished INTEGER DEFAULT 0;

    DECLARE v_project_cursor  CURSOR FOR (
      SELECT
        DISTINCT `we`.`project_id`
      FROM
        `ose_tasks`.`drawing` AS `we`
    );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET v_cursor_finished = 0;

    OPEN v_project_cursor;
    REPEAT

      FETCH v_project_cursor INTO v_project_id;

      IF !v_cursor_finished THEN
        CALL `archive_design_hour_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END$$

-- -----------------------------------------------------------------------------
-- 生产设计工时统计
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_design_hour_of_project`(
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
      `code` = 'ARCHIVE_DESIGN_HOUR'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_DESIGN_HOUR', '数据归档：生产设计 / 工时', 'RUNNING');

    -- 为防止当天有业务数据减少的情况，归档数据改为先删除，后插入
    DELETE FROM `ose_report`.`statistics`
    WHERE `project_id` = v_project_id
      AND `archive_year`  = YEAR(v_archive_time)
      AND `archive_month` = MONTH(v_archive_time)
      AND `archive_day`   = DAY(v_archive_time)
      AND `archive_type`  = 'DESIGN_HOUR';

    -- 归档已完成数据
    CALL `archive_design_hour_finished_daily`(v_archive_time, v_project_id);

    -- 归档计划数据
    CALL `archive_design_hour_plan_daily`(v_archive_time, v_project_id);

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
-- 统计已完成
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_design_hour_finished_daily`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
BEGIN

    DECLARE v_group_year      INTEGER;
    DECLARE v_group_month     INTEGER;
    DECLARE v_group_day       INTEGER;
    DECLARE v_group_week      INTEGER;
    DECLARE v_category        VARCHAR(255);
    DECLARE v_working_hours	  DOUBLE;
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_archive_timestamp TIMESTAMP;

    DECLARE v_wbs_cursor  CURSOR FOR (
		SELECT
  `e`.`year`,
  `e`.`month`,
  `e`.`day`,
  `e`.`week`,
  `e`.`name_cn`,
  SUM(e.cost_hour) AS working_hours
FROM
  (SELECT
    YEAR(a.`end_date`) AS `year`,
    MONTH(a.`end_date`) AS `month`,
    DAY(a.`end_date`) AS `day`,
    WEEK_OF_YEAR(a.`end_date`) AS `week`,
    c.`name_cn`,
    ac.`cost_hour`
  FROM
    ose_tasks.`drawing` p
    LEFT JOIN ose_tasks.`bpm_entity_sub_type` c
      ON p.`drawing_category_id` = c.`id`
    LEFT JOIN ose_tasks.`bpm_activity_instance` a
      ON p.`id` = a.`entity_id`
    LEFT JOIN `ose_tasks`.`bpm_act_task` AS `ac` ON `ac`.`act_inst_id` = `a`.`act_inst_id`
   WHERE p.`project_id` = v_project_id
    AND a.`process_name` = 'ENGINEERING'
    AND a.`end_date` IS NOT NULL
    AND p.`status` = 'ACTIVE') e
GROUP BY `e`.`year`,
  `e`.`month`,
  `e`.`day`,
  `e`.`week`,
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
       v_category,
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
            `entity_category`,
            `value_01`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\DESIGN_HOUR\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT(v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              IFNULL(v_category, '')
            ))),
            v_project_id,
            'DESIGN_HOUR',
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
            v_category,
            v_working_hours
          )
          ON DUPLICATE KEY UPDATE
            `value_01`         = v_working_hours,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_wbs_cursor;

  END$$

-- -----------------------------------------------------------------------------
-- 统计计划
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_design_hour_plan_daily`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
BEGIN

    DECLARE v_group_year      INTEGER;
    DECLARE v_group_month     INTEGER;
    DECLARE v_group_day       INTEGER;
    DECLARE v_group_week      INTEGER;
    DECLARE v_category        VARCHAR(255);
    DECLARE v_working_hours	  DOUBLE;
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_archive_timestamp TIMESTAMP;

    DECLARE v_wbs_cursor  CURSOR FOR (
SELECT
  `e`.`year`,
  `e`.`month`,
  `e`.`day`,
  `e`.`week`,
  `e`.`name_cn`,
  SUM(e.estimated_man_hours) AS working_hours
FROM
  (SELECT
    YEAR(p.`engineering_finish_date`) AS `year`,
    MONTH(p.`engineering_finish_date`) AS `month`,
    DAY(p.`engineering_finish_date`) AS `day`,
    WEEK_OF_YEAR(p.`engineering_finish_date`) AS `week`,
    c.`name_cn`,
    p.estimated_man_hours
  FROM
    ose_tasks.`drawing` p
    LEFT JOIN ose_tasks.`bpm_entity_sub_type` c
      ON p.`drawing_category_id` = c.`id`
  WHERE p.`project_id` = v_project_id
    AND p.`status` = 'ACTIVE'
    AND p.engineering_finish_date IS NOT NULL) e
GROUP BY `e`.`year`,
  `e`.`month`,
  `e`.`day`,
  `e`.`week`,
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
       v_category,
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
            `entity_category`,
            `value_02`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\DESIGN_HOUR\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT(v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              IFNULL(v_category, '')
            ))),
            v_project_id,
            'DESIGN_HOUR',
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
            v_category,
            v_working_hours
          )
          ON DUPLICATE KEY UPDATE
            `value_02`         = v_working_hours,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_wbs_cursor;

  END$$


-- -----------------------------------------------------------------------------
-- 生产设计完成率数据归档定时任务。
-- 执行频率：每日凌晨03:15
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_wbs_design_hour_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2018-11-01 03:15:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前6小时
    CALL `archive_design_hour`(ADDTIME(NOW(), -60000));
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
