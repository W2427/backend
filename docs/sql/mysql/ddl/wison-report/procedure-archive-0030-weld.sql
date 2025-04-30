-- -----------------------------------------------------------------------------
-- 数据统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.wbs_entry, ose_tasks.entity_weld, ose_auth.organizations,
-- 输出目标　：ose_report.statistics
-- 创建时间　：2019-04-07
-- 创建者　　：白东旭
-- -----------------------------------------------------------------------------
USE `ose_report` ;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_weld_daily`;
DROP PROCEDURE IF EXISTS `archive_weld`;
DROP PROCEDURE IF EXISTS `archive_weld_of_project`;
DROP PROCEDURE IF EXISTS `archive_weld_finish_daily`;
DROP PROCEDURE IF EXISTS `archive_weld_total_daily`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计项目：建造计划 / 焊接完成量
-- 统计对象：焊口NPS，班组等信息。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_weld`(IN v_archive_time TIMESTAMP)
BEGIN

    DECLARE v_project_id      BIGINT(20);
    DECLARE v_cursor_finished INTEGER DEFAULT 0;

    DECLARE v_project_cursor  CURSOR FOR (
      SELECT
        DISTINCT `we`.`project_id`
      FROM
        `ose_tasks`.`wbs_entry` AS `we`
    );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET v_cursor_finished = 0;

    OPEN v_project_cursor;
    REPEAT

      FETCH v_project_cursor INTO v_project_id;

      IF !v_cursor_finished THEN
        CALL `archive_weld_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END$$

-- -----------------------------------------------------------------------------
-- 焊接完成量统计。
-- 为项目创建定时任务执行记录。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_weld_of_project`(
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
      `code` = 'ARCHIVE_WBS_WELD'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_WELD', '数据归档：建造计划 / 焊接完成量', 'RUNNING');

    -- 为防止当天有业务数据减少的情况，归档数据改为先删除，后插入
    DELETE FROM `ose_report`.`statistics`
    WHERE `project_id` = v_project_id
      AND `archive_year`  = YEAR(v_archive_time)
      AND `archive_month` = MONTH(v_archive_time)
      AND `archive_day`   = DAY(v_archive_time)
      AND `archive_type`  = 'WBS_WELD';

    -- 归档已完成数据
    CALL `archive_weld_finish_daily`(v_archive_time, v_project_id);

    -- 归档总数据
    CALL `archive_weld_total_daily`(v_archive_time, v_project_id);

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
-- 统计已完成焊接量
-- -----------------------------------------------------------------------------
  CREATE PROCEDURE `archive_weld_finish_daily`(
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
    DECLARE v_entity_material VARCHAR(255);
    DECLARE v_team_name       VARCHAR(255);
    DECLARE v_weld_type       VARCHAR(255);
    DECLARE v_nps 	          DOUBLE;
    DECLARE v_countt	        DOUBLE;
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_archive_timestamp TIMESTAMP;

    DECLARE v_weld_cursor  CURSOR FOR (
       SELECT
        `e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`sector`,
        `e`.`stage`,
        `e`.`material_group_code`,
        `e`.`name`,
        `e`.`weld_type`,
        SUM(e.nps) AS nps,
        COUNT(e.nps) AS countt
       FROM (
          SELECT
            YEAR(we.`finished_at`)  AS `year`,
            MONTH(we.`finished_at`) AS `month`,
            DAY(we.`finished_at`)   AS `day`,
            WEEK_OF_YEAR(we.`finished_at`)  AS `week`,
            we.`sector`,
            we.stage,
            ew.`material_group_code`,
            org.`name`,
            ew.`weld_type`,
            ew.`nps`
          FROM ose_tasks.wbs_entry we
          INNER JOIN ose_tasks.entity_weld ew ON we.entity_id = ew.id
          LEFT JOIN ose_auth.organizations org ON org.id = we.team_id
          WHERE we.`project_id` = v_project_id
            AND we.`type` = 'ENTITY'
            AND we.`active` = 1
            AND we.`finished` = 1
            AND we.`finished_at` IS NOT NULL
            AND we.`deleted` = 0
            AND we.process ='WELD') AS e
          GROUP BY
            `e`.`year`,
            `e`.`month`,
            `e`.`day`,
            `e`.`week`,
            `e`.`sector`,
            `e`.stage,
            `e`.`material_group_code`,
            `e`.`name`,
            `e`.`weld_type`
      );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET @archive_year  = YEAR(v_archive_time);
    SET @archive_month = MONTH(v_archive_time);
    SET @archive_day   = DAY(v_archive_time);
    SET @archive_week  = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;
    SET v_archive_timestamp = CURRENT_TIMESTAMP();

    OPEN v_weld_cursor;
      REPEAT

        FETCH
          v_weld_cursor
        INTO
          v_group_year,
          v_group_month,
          v_group_day,
          v_group_week,
          v_sector,
          v_stage,
          v_entity_material,
          v_team_name,
          v_weld_type,
          v_nps,
          v_countt
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
            `entity_material`,
            `team_name`,
            `weld_type`,
            `value_01`,
            `value_02`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\nWBS_WELD\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT(v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              CONCAT(v_sector, '\n', v_stage),
              IFNULL(v_entity_material, ''),
              IFNULL(v_team_name, ''),
              IFNULL(v_weld_type, '')
            ))),
            v_project_id,
            'WBS_WELD',
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
            v_entity_material,
            v_team_name,
            v_weld_type,
            v_nps,
            v_countt
          )
          ON DUPLICATE KEY UPDATE
            `value_01`         = v_nps,
            `value_02`         = v_countt,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_weld_cursor;

  END$$

-- -----------------------------------------------------------------------------
-- 统计焊口总量
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_weld_total_daily`(
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
    DECLARE v_entity_material VARCHAR(255);
    DECLARE v_weld_type       VARCHAR(255);
    DECLARE v_team_name       VARCHAR(255);
    DECLARE v_nps 	       DOUBLE;
    DECLARE v_countt	       DOUBLE;
    DECLARE v_cursor_finished INTEGER DEFAULT 0;
    DECLARE v_archive_timestamp TIMESTAMP;

  DECLARE v_weld_cursor  CURSOR FOR (
       SELECT
		`e`.`year`,
        `e`.`month`,
        `e`.`day`,
        `e`.`week`,
        `e`.`sector`,
        `e`.`stage`,
        `e`.`material_group_code`,
        `e`.`name`,
        `e`.`weld_type`,
        SUM(e.nps) AS nps,
        COUNT(e.nps) AS countt
       FROM (
          SELECT
          YEAR(we.`created_at`)  AS `year`,
          MONTH(we.`created_at`) AS `month`,
          DAY(we.`created_at`)   AS `day`,
          WEEK_OF_YEAR(we.`created_at`)  AS `week`,
          we.`sector`,
          we.stage,
          ew.`material_group_code`,
          org.`name`,
          ew.`weld_type`,
          ew.`nps`
        FROM ose_tasks.wbs_entry we
        INNER JOIN ose_tasks.entity_weld ew ON we.entity_id = ew.id
        LEFT JOIN ose_auth.organizations org ON org.id = we.team_id
        WHERE we.`project_id` = v_project_id
          AND we.`type` = 'ENTITY'
          AND we.`active` = 1
                AND we.`created_at` IS NOT NULL
                AND we.`deleted` = 0
                AND we.process ='WELD') AS e
  GROUP BY
    `e`.`year`,
    `e`.`month`,
    `e`.`day`,
    `e`.`week`,
    `e`.`sector`,
    `e`.stage,
    `e`.`material_group_code`,
    `e`.`name`,
    `e`.`weld_type`

      );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET @archive_year  = YEAR(v_archive_time);
    SET @archive_month = MONTH(v_archive_time);
    SET @archive_day   = DAY(v_archive_time);
    SET @archive_week  = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;
    SET v_archive_timestamp = CURRENT_TIMESTAMP();

    OPEN v_weld_cursor;
      REPEAT

        FETCH
          v_weld_cursor
        INTO
       v_group_year,
       v_group_month,
       v_group_day,
       v_group_week,
       v_sector,
       v_stage,
       v_entity_material,
       v_team_name,
       v_weld_type,
       v_nps,
       v_countt
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
            `entity_material`,
            `team_name`,
            `weld_type`,
            `value_03`,
            `value_04`
          ) VALUES (
            UPPER(MD5(CONCAT(
              v_project_id, '\nWBS_WELD\nDAILY\n',
              CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
              CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
              CONCAT(v_sector, '\n', v_stage),
              IFNULL(v_entity_material, ''),
              IFNULL(v_team_name, ''),
              IFNULL(v_weld_type, '')
            ))),
            v_project_id,
            'WBS_WELD',
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
            v_entity_material,
            v_team_name,
            v_weld_type,
            v_nps,
            v_countt
          )
          ON DUPLICATE KEY UPDATE
            `value_03`         = v_nps,
            `value_04`         = v_countt,
            `last_modified_at` = CURRENT_TIMESTAMP()
          ;
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_weld_cursor;

  END$$
-- -----------------------------------------------------------------------------
-- 焊接完成量数据归档定时任务。
-- 执行频率：每日凌晨00:30
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_weld_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2018-11-01 00:30:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前6小时
    CALL `archive_weld`(ADDTIME(NOW(), -60000));

    -- EVENT[archive_plan_daily_report_daily]中，会定义其他的启动时间，为计划日报归档数据
    -- 如有启动时间方面的修改，请结合日报的时间，一起考虑。
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
