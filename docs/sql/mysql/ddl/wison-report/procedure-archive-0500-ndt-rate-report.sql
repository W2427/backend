-- -----------------------------------------------------------------------------
-- NDT合格率统计定时任务。
--
-- 数据库类型：MySQL
-- 数据源　　：ose_tasks.entity_weld,
--           ose_tasks.wbs_entry,
--           ose_auth.organizations,
--           ose_tasks.bpm_activity_instance
--           ose_tasks.bpm_hi_taskinst
--           ose_tasks.welder
-- 输出目标　：ose_report.statistics
-- 创建时间　：2019-04-11
-- 创建者　　：梅洛谊
-- -----------------------------------------------------------------------------

USE `ose_report`;

SET GLOBAL event_scheduler = 0;

DROP EVENT IF EXISTS `archive_ndt_rate_daily`;
DROP PROCEDURE IF EXISTS `archive_ndt_rate`;
DROP PROCEDURE IF EXISTS `archive_ndt_rate_of_project`;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 统计项目：质量控制 / NDT合格率
-- 统计对象：完成NDT的焊口
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_ndt_rate`(IN v_archive_time TIMESTAMP)
  BEGIN_LABEL:BEGIN

    DECLARE v_project_id      BIGINT(20);
    DECLARE v_cursor_finished INTEGER DEFAULT 0;

    DECLARE v_project_cursor  CURSOR FOR (
      SELECT
        DISTINCT `w`.`project_id`
      FROM
        `ose_tasks`.`entity_weld` AS `w`
    );

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET v_cursor_finished = 0;

    OPEN v_project_cursor;
    REPEAT

      FETCH v_project_cursor INTO v_project_id;

      IF !v_cursor_finished THEN
        -- 按项目归档数据
        CALL `archive_ndt_rate_of_project`(v_archive_time, v_project_id);
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- NDT合格率归档。
-- 统计NDT的合格个数和寸径以及不合格个数和寸径到工作流第一次结束的时间上。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `archive_ndt_rate_of_project`(
  IN v_archive_time TIMESTAMP,  -- 归档时间
  IN v_project_id   BIGINT(20) -- 项目 ID
)
  BEGIN_LABEL:BEGIN
    DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
    DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;
    DECLARE v_cursor_finished       INTEGER DEFAULT 0;
    DECLARE v_group_date            TIMESTAMP DEFAULT NULL;
    DECLARE v_group_year            INTEGER;
    DECLARE v_group_month           INTEGER;
    DECLARE v_group_day             INTEGER;
    DECLARE v_group_week            INTEGER;
    DECLARE v_module                VARCHAR(255); -- 模块
    DECLARE v_stage                 VARCHAR(255); -- 工序阶段
    DECLARE v_entity_material       VARCHAR(255); -- 材质
    DECLARE v_team_name             VARCHAR(255); -- 班组名
    DECLARE v_ndt_type              VARCHAR(255); -- NDT分类
    DECLARE v_welder_no             VARCHAR(255); -- 焊工号
    DECLARE v_welder_name           VARCHAR(255); -- 焊工名
    DECLARE v_qualified_nps         DECIMAL(19,3); -- NDT合格寸径
    DECLARE v_failed_nps            DECIMAL(19,3); -- NDT不合格寸径
    DECLARE v_qualified_count       INTEGER; -- NDT合格数量
    DECLARE v_failed_count          INTEGER; -- NDT不合格数量

    DECLARE v_ndt_rate_cursor  CURSOR FOR (
		SELECT
		  tt.`module`,
		  tt.`stage`,
		  tt.`entity_material`,
		  tt.`team_name`,
		  tt.`ndt_type`,
		  tt.`welder_no`,
		  tt.`welder_name`,
		  SUM(tt.qualified_nps) AS qualified_nps,
		  SUM(tt.failed_nps) AS failed_nps,
		  SUM(tt.qualified_count) AS qualified_count,
		  SUM(tt.failed_count) AS failed_count,
		  tt.group_year,
		  tt.group_month,
		  tt.group_day,
		  tt.group_week
		FROM
		  (SELECT
			`w`.`project_id` AS project_id,
			`wbs`.`sector` AS `module`,
			-- 模块
			`wbs`.`stage` AS `stage`,
			-- 工序阶段
			`w`.`material_group_code` AS `entity_material`,
			-- 材质
			`org`.`name` AS `team_name`,
			-- 班组
			`w`.`nde` AS `ndt_type`,
			-- NDT分类
			`welder`.`no` AS `welder_no`,
			-- 焊工号
			`welder`.`name` AS `welder_name`,
			-- 焊工名
			(
			  IF(
				`hi`.`task_def_key_` = 'usertask-QC-REPORT-UPLOAD',
				`w`.`nps`,
				0
			  )
			) AS `qualified_nps`,
			(
			  IF(
				`hi`.`task_def_key_` = 'usertask-QC-REPORT-NG-UPLOAD',
				`w`.`nps`,
				0
			  )
			) AS `failed_nps`,
			(
			  IF(
				`hi`.`task_def_key_` = 'usertask-QC-REPORT-UPLOAD',
				1,
				0
			  )
			) AS `qualified_count`,
			(
			  IF(
				`hi`.`task_def_key_` = 'usertask-QC-REPORT-NG-UPLOAD',
				1,
				0
			  )
			) AS `failed_count`,
			YEAR(`act`.`end_date`) AS group_year,
			MONTH(`act`.`end_date`) AS group_month,
			DAY(`act`.`end_date`) AS group_day,
			WEEK_OF_YEAR (`act`.`end_date`) AS group_week
		  FROM
			`ose_tasks`.`entity_weld` AS `w`
			JOIN `ose_tasks`.`wbs_entry` `wbs`
			  ON `w`.`project_id` = `wbs`.`project_id`
			  AND `w`.`id` = `wbs`.`entity_id`
			  AND `wbs`.`process` = 'NDT'
			JOIN `ose_auth`.`organizations` AS `org`
			  ON `wbs`.`team_id` = `org`.`id`
			JOIN `ose_tasks`.`welders` AS `welder`
			  ON `w`.`project_id` = `welder`.`project_id`
			  AND `w`.`welder_id` = `welder`.`id`
			JOIN `ose_tasks`.`bpm_activity_instance` AS `act`
			  ON `w`.`id` = `act`.`entity_id`
			  AND `act`.`a.process_name` = 'NDT'
			  AND `act`.`end_date` IS NOT NULL
			JOIN `ose_tasks`.`bpm_hi_taskinst` AS `hi`
			  ON `act`.`act_inst_id` = `hi`.`act_inst_id`
		  WHERE `w`.`project_id` = v_project_id
			AND (
			  hi.`task_def_key_` = 'usertask-QC-REPORT-UPLOAD'
			  OR hi.`task_def_key_` = 'usertask-QC-REPORT-NG-UPLOAD'
			)) tt
		GROUP BY `tt`.`project_id`,
		  tt.`module`,
		  tt.`stage`,
		  tt.`entity_material`,
		  tt.`team_name`,
		  tt.`ndt_type`,
		  tt.`welder_no`,
		  tt.`welder_name`,
		  tt.group_year,
		  tt.group_month,
		  tt.group_day,
		  tt.group_week
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
      `code` = 'ARCHIVE_WBS_NDT_RATE_PROGRESS'
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
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'ARCHIVE_WBS_NDT_RATE_PROGRESS', '数据归档：质量控制 / NDT合格率', 'RUNNING');

    -- 统计焊接个数和寸径到工作流第一次结束的时间上
    SET @archive_year     = YEAR(v_archive_time);
    SET @archive_month    = MONTH(v_archive_time);
    SET @archive_day      = DAY(v_archive_time);
    SET @archive_week     = WEEK_OF_YEAR(v_archive_time);
    SET v_cursor_finished = 0;

    -- 为防止当天有业务数据减少的情况，归档数据改为先删除，后插入
    DELETE FROM `ose_report`.`statistics`
    WHERE `project_id` = v_project_id
        AND `archive_year`  = @archive_year
        AND `archive_month` = @archive_month
        AND `archive_day`   = @archive_day
        AND `archive_type`  = 'WBS_NDT_RATE_PROGRESS';

    OPEN v_ndt_rate_cursor;
    REPEAT

      FETCH
        v_ndt_rate_cursor
      INTO
        v_module,
        v_stage,
        v_entity_material,
        v_team_name,
        v_ndt_type,
        v_welder_no,
        v_welder_name,
        v_qualified_nps,
        v_failed_nps,
        v_qualified_count,
        v_failed_count,
        v_group_year,
		v_group_month,
		v_group_day,
		v_group_week
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
          `ndt_type`,
          `welder_no`,
          `welder_name`,
          `value_01`,
          `value_02`,
          `value_03`,
          `value_04`
        ) VALUES (
          UPPER(MD5(CONCAT(
            v_project_id, '\nWBS_NDT_RATE_PROGRESS\nDAILY\n',
            CONCAT(@archive_year, '\n', @archive_month, '\n', @archive_day, '\n'),
            CONCAT( v_group_year, '\n', v_group_month, '\n', v_group_day, '\n'),
            IFNULL(v_module, ''),
            IFNULL(v_stage, ''),
            IFNULL(v_entity_material, ''),
            IFNULL(v_team_name, ''),
            IFNULL(v_ndt_type, ''),
            IFNULL(v_welder_no, ''),
            IFNULL(v_welder_name, '')
          ))),
          v_project_id,
          'WBS_NDT_RATE_PROGRESS',
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
          v_entity_material,
          v_team_name,
          v_ndt_type,
          v_welder_no,
          v_welder_name,
          v_qualified_nps,
          v_qualified_count,
          v_failed_nps,
          v_failed_count
        )
        ON DUPLICATE KEY UPDATE
          `value_01`         = v_qualified_nps,
          `value_02`         = v_qualified_count,
          `value_03`         = v_failed_nps,
          `value_04`         = v_failed_count,
          `last_modified_at` = CURRENT_TIMESTAMP()
        ;
      END IF;

    UNTIL v_cursor_finished END REPEAT;
    CLOSE v_ndt_rate_cursor;

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
-- NDT合格率归档定时任务。
-- 执行频率：每日凌晨5点00分
-- -----------------------------------------------------------------------------
CREATE EVENT `archive_ndt_rate_daily`
  ON SCHEDULE EVERY 24 HOUR STARTS TIMESTAMP '2018-11-01 05:00:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    -- 执行基准时间是当前时间之前6小时
    CALL `archive_ndt_rate`(ADDTIME(NOW(), -60000));
  END $$

DELIMITER ;

SET GLOBAL event_scheduler = 1;
