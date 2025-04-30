-- -----------------------------------------------------------------------------
-- 层级节点进度信息更新存储过程及定时任务定义。
--
-- 一个实体的进度为该实体的所有（指定阶段的）工序计划（即四级计划）的进度的聚合。
-- 进度信息包含已完成工序计划权重之和、工序计划权重总和、预计工时、实际工时，
-- 并将其分为五个维度（视图），即区域（建造）、层/包、试压、清洁、子系统（竣工），共计二十个字段。
-- 一个类型为实体的层级节点的进度：
--  • 若该节点位于区域视图（AREA/ISO）中，那么聚合该节点对应实体的所有阶段的工序计划
--  • 若该节点位于层/包视图（LAYER_PACKAGE）中，那么聚合该节点对应实体的建造阶段的工序计划（INSTALLATION）
--  • 若该节点位于试压包视图（PRESSURE_TEST_PACKAGE）中，那么聚合该节点对应实体的试压和绝热阶段的工序（PRESSURE_TEST）
--  • 若该节点位于清洁包视图（CLEAN_PACKAGE）中，那么聚合该节点对应实体的清洁阶段的工序（CLEANNESS）
--  • 若该节点位于子系统视图（SUB_SYSTEM）中，那么聚合该节点对应实体的竣工阶段的工序（MECHANICAL_COMPLETION）
--  • 若该节点拥有子节点，那么还将统计所有子节点的进度
-- 一个类型不为实体的层级节点的进度为所有直接子节点进度的聚合。
--
-- 数据库类型：MySQL
-- 数据库　　：saint_whale_tasks
-- 创建时间　：2018-11-06
-- 创建者　　：金海岩
-- -----------------------------------------------------------------------------

USE `saint_whale_tasks`;

-- 停用定时任务
SET GLOBAL event_scheduler = 0;

-- 删除已创建的数据库对象以更新
DROP VIEW IF EXISTS `wbs_entity_progress`;
DROP PROCEDURE IF EXISTS `set_progress_on_hierarchy_node`;
DROP PROCEDURE IF EXISTS `set_progress_on_hierarchy_nodes_of_project`;
DROP PROCEDURE IF EXISTS `set_progress_on_hierarchy_nodes`;
DROP PROCEDURE IF EXISTS `start_setting_progress_on_hierarchy_nodes`;
DROP EVENT IF EXISTS `set_progress_on_hierarchy_nodes_hourly`;

-- 创建进度统计处理执行记录表
CREATE TABLE IF NOT EXISTS `schedule_log` (
  `id`              BIGINT(20)  NOT NULL,
  `project_id`      BIGINT(20),
  `type`            VARCHAR(45)  NOT NULL,
  `code`            VARCHAR(45)  NOT NULL,
  `name`            VARCHAR(45)  NOT NULL,
  `total_count`     INTEGER      NULL DEFAULT 0,
  `processed_count` INTEGER      NULL DEFAULT 0,
  `passed_count`    INTEGER      NULL DEFAULT 0,
  `error_count`     INTEGER      NULL DEFAULT 0,
  `started_at`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `finished_at`     TIMESTAMP    NULL,
  `stopped_at`      TIMESTAMP    NULL,
  `stopped_by`      BIGINT(20)  NULL,
  `error_log`       VARCHAR(512) NULL,
  `status`          VARCHAR(45)  NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 创建实体工序实施进度统计视图
CREATE VIEW `wbs_entity_progress` AS
  (
    SELECT
      CONCAT(`entity_id`) AS `id`,
      `project_id`,
      `entity_id`,
      `entity_type`,
      NULL AS `stage_name`,
      SUM(IF(`finished_score` IS NULL, 0, `finished_score`))                            AS `finished_score`,
      SUM(IF(`total_score` IS NULL, 0, `total_score`))                                  AS `total_score`,
      SUM(IF(`finished` = 1, IF(`actual_man_hours` IS NULL, 0, `actual_man_hours`), 0)) AS `actual_man_hours`,
      SUM(IF(`estimated_man_hours` IS NULL, 0, `estimated_man_hours`))                  AS `estimated_man_hours`
    FROM
      `wbs_entry`
    WHERE
      `type` = 'ENTITY'
      AND `deleted` = 0
      AND `active` = 1
    GROUP BY
      `project_id`,
      `entity_id`,
      `entity_type`
  ) UNION (
    SELECT
      CONCAT(`entity_id`, ':', `stage`) AS `id`,
      `project_id`,
      `entity_id`,
      `entity_type`,
      `stage` AS `stage_name`,
      SUM(IF(`finished_score` IS NULL, 0, `finished_score`))                          AS `finished_score`,
      SUM(IF(`total_score` IS NULL, 0, `total_score`))                                AS `total_score`,
      SUM(IF(`finished` = 1, IF(`actual_man_hours` IS NULL, 0, `actual_man_hours`), 0)) AS `actual_man_hours`,
      SUM(IF(`estimated_man_hours` IS NULL, 0, `estimated_man_hours`))                        AS `estimated_man_hours`
    FROM
      `wbs_entry`
    WHERE
      `type` = 'ENTITY'
      AND `deleted` = 0
      AND `active` = 1
      AND `stage` IN ('INSTALLATION', 'PRESSURE_TEST', 'CLEANNESS', 'MECHANICAL_COMPLETION')
    GROUP BY
      `project_id`,
      `entity_id`,
      `entity_type`,
      `stage`
  )
;

DELIMITER $$

-- -----------------------------------------------------------------------------
-- 启动层级节点进度信息更新存储过程。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `start_setting_progress_on_hierarchy_nodes`()
  BEGIN
    CALL `set_progress_on_hierarchy_nodes`();
  END $$

-- -----------------------------------------------------------------------------
-- 层级节点进度信息更新存储过程。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `set_progress_on_hierarchy_nodes`()
  BEGIN

    DECLARE v_project_id      BIGINT(20);
    DECLARE v_cursor_finished INTEGER DEFAULT 0;

    -- 取得所有项目信息
    DECLARE v_project_cursor CURSOR FOR
      SELECT
        `id`
      FROM
        `projects`
      WHERE
        `deleted` = 0
    ;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    SET v_cursor_finished = 0;

    -- 遍历每一个项目
    OPEN v_project_cursor;
      REPEAT

        FETCH v_project_cursor INTO v_project_id;

        IF !v_cursor_finished THEN
          CALL `set_progress_on_hierarchy_nodes_of_project`(v_project_id);
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_project_cursor;

  END $$

-- -----------------------------------------------------------------------------
-- 层级节点进度信息更新存储过程（指定项目）。
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `set_progress_on_hierarchy_nodes_of_project`(IN v_project_id BIGINT(20))
  BEGIN_LABEL:BEGIN

    DECLARE v_last_task_started_at  TIMESTAMP DEFAULT NULL;
    DECLARE v_last_task_finished_at TIMESTAMP DEFAULT NULL;
    DECLARE v_hierarchy_node_id     BIGINT(20);
    DECLARE v_cursor_finished       INTEGER DEFAULT 0;
    DECLARE v_project_finished_at   DATETIME;
    DECLARE v_wbs_finished_score    DOUBLE  DEFAULT 0;

    -- 取得所有层级节点信息
    DECLARE v_hierarchy_node_cursor CURSOR FOR
      SELECT
        `id`
      FROM
        `hierarchy_node`
      WHERE
        `deleted` = 0
        AND `project_id` = v_project_id
      ORDER BY
        `project_id` ASC,
        `depth` DESC
    ;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_cursor_finished = 1;

    -- 取得最后一条定时任务的执行记录
    SELECT
      `started_at`,
      `finished_at`
    INTO
      v_last_task_started_at,
      v_last_task_finished_at
    FROM
      `schedule_log`
    WHERE
      `code` = 'SET_HIERARCHY_NODE_PROGRESS'
      AND `project_id` = v_project_id
    ORDER BY
      `id` DESC
    LIMIT 0, 1;

    -- 若存在尚未完成的任务且执行时长小于四个小时则结束
    IF (v_last_task_finished_at IS NULL AND CURRENT_TIMESTAMP() - v_last_task_started_at < 40000) THEN
      LEAVE BEGIN_LABEL;
    END IF;

    -- 取得项目信息
    SELECT
      `finished_at`
    INTO
      v_project_finished_at
    FROM
      `projects`
    WHERE
      `id` = v_project_id
    ;

    -- 若项目计划已结束则不执行统计
    IF  (v_project_finished_at IS NOT NULL) THEN
      LEAVE BEGIN_LABEL;
    END IF;

    -- 计算当前已完成任务的权重之和
    SELECT
      SUM(IF(`finished_score` IS NULL, 0, `finished_score`)) AS `started_count`
    INTO
      v_wbs_finished_score
    FROM
      `wbs_entry`
    WHERE
      `project_id` = v_project_id
      AND `depth` = 0
      AND `deleted` = 0
      AND `active` = 1
    ;

    -- 若项目计划尚未开始则不执行统计
    IF (v_wbs_finished_score = 0) THEN
      LEAVE BEGIN_LABEL;
    END IF;

    -- 生成执行记录的实体 ID
    SET @schedule_id = `generate_entity_id`();

    -- 创建执行记录
    INSERT INTO `schedule_log` (`id`, `project_id`, `type`, `code`, `name`, `status`)
    VALUES (@schedule_id, v_project_id, 'STORED_PROCEDURE', 'SET_HIERARCHY_NODE_PROGRESS', '层级节点实体工序实施进度更新', 'RUNNING');

    SET v_cursor_finished = 0;

    -- 更新每一个层级节点的工序实施进度统计信息
    OPEN v_hierarchy_node_cursor;
      REPEAT

        FETCH v_hierarchy_node_cursor INTO v_hierarchy_node_id;

        IF !v_cursor_finished THEN
          CALL `set_progress_on_hierarchy_node`(v_hierarchy_node_id);
        END IF;

      UNTIL v_cursor_finished END REPEAT;
    CLOSE v_hierarchy_node_cursor;

    -- 记录项目计划实施进度最后更新时间
    UPDATE
      `projects`
    SET
      `progress_refreshed_at` = CURRENT_TIMESTAMP()
    WHERE
      `id` = v_project_id
    ;

    -- 更新执行记录
    UPDATE
      `schedule_log`
    SET
      `finished_at` = CURRENT_TIMESTAMP(),
      `status` = 'FINISHED'
    WHERE
      `id` = @schedule_id
    ;

  END $$

-- -----------------------------------------------------------------------------
-- 层级节点进度信息更新存储过程。
-- 输入参数 v_hierarchy_node_id：层级节点 ID
-- -----------------------------------------------------------------------------
CREATE PROCEDURE `set_progress_on_hierarchy_node`(IN v_hierarchy_node_id BIGINT(20))
  BEGIN

    DECLARE v_stage_name                  VARCHAR(45);
    DECLARE v_finished_score              DOUBLE  DEFAULT NULL;
    DECLARE v_total_score                 DOUBLE  DEFAULT NULL;
    DECLARE v_plan_hours                  DOUBLE  DEFAULT NULL;
    DECLARE v_actual_hours                DOUBLE  DEFAULT NULL;
    DECLARE v_overall_finished_score      DOUBLE  DEFAULT NULL;
    DECLARE v_overall_total_score         DOUBLE  DEFAULT NULL;
    DECLARE v_overall_plan_hours          DOUBLE  DEFAULT NULL;
    DECLARE v_overall_actual_hours        DOUBLE  DEFAULT NULL;
    DECLARE v_installation_finished_score DOUBLE  DEFAULT NULL;
    DECLARE v_installation_total_score    DOUBLE  DEFAULT NULL;
    DECLARE v_installation_plan_hours     DOUBLE  DEFAULT NULL;
    DECLARE v_installation_actual_hours   DOUBLE  DEFAULT NULL;
    DECLARE v_ptp_finished_score          DOUBLE  DEFAULT NULL;
    DECLARE v_ptp_total_score             DOUBLE  DEFAULT NULL;
    DECLARE v_ptp_plan_hours              DOUBLE  DEFAULT NULL;
    DECLARE v_ptp_actual_hours            DOUBLE  DEFAULT NULL;
    DECLARE v_cleanness_finished_score    DOUBLE  DEFAULT NULL;
    DECLARE v_cleanness_total_score       DOUBLE  DEFAULT NULL;
    DECLARE v_cleanness_plan_hours        DOUBLE  DEFAULT NULL;
    DECLARE v_cleanness_actual_hours      DOUBLE  DEFAULT NULL;
    DECLARE v_mc_finished_score           DOUBLE  DEFAULT NULL;
    DECLARE v_mc_total_score              DOUBLE  DEFAULT NULL;
    DECLARE v_mc_plan_hours               DOUBLE  DEFAULT NULL;
    DECLARE v_mc_actual_hours             DOUBLE  DEFAULT NULL;
    DECLARE v_cursor_finished             INTEGER DEFAULT NULL;
    DECLARE v_hierarchy_type              VARCHAR(45); -- 层级类型
    DECLARE v_project_node_id             BIGINT(20); -- 项目节点ID
    DECLARE v_node_type                   VARCHAR(32); -- 节点类型
    DECLARE v_entity_type                 VARCHAR(32); -- 实体类型
    DECLARE v_entity_id                   BIGINT(20); -- 实体ID

    -- 取得节点详细信息
    SELECT hn.hierarchy_type, pn.id, pn.node_type, pn.entity_type, pn.entity_id
    INTO   v_hierarchy_type, v_project_node_id, v_node_type, v_entity_type, v_entity_id
    FROM hierarchy_node hn JOIN project_node pn ON hn.project_id = pn.project_id AND hn.node_id = pn.id AND pn.deleted = FALSE
    WHERE hn.id = v_hierarchy_node_id AND hn.deleted = FALSE
    LIMIT 0,1
    ;
    -- 更新层级节点的工时进度信息
    -- 区域维度，各实体子节点的数据求和，再加上本身的数据
    IF (v_hierarchy_type = "PIPING") THEN
        -- 不会有子节点的节点，只更新自己的进度工时信息
        IF (v_entity_type IN ('PIPE_PIECE', 'WELD_JOINT', 'COMPONENT')) THEN
            SELECT
                SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
                SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
                SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
                SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
            INTO v_overall_finished_score, v_overall_total_score, v_overall_actual_hours, v_overall_plan_hours
            FROM
                `wbs_entity_progress` AS `pg`
            WHERE
              `pg`.`entity_id` = v_entity_id
              AND `pg`.`stage_name` IS NULL
            GROUP BY
              `pg`.`entity_id`,
              `pg`.`stage_name`
            ;

            -- 更新层级节点上的进度统计信息
            UPDATE
              `hierarchy_node`
            SET
              `overall_finished_score`      = v_overall_finished_score,
              `overall_total_score`         = v_overall_total_score,
              `overall_plan_hours`          = v_overall_plan_hours,
              `overall_actual_hours`        = v_overall_actual_hours
            WHERE
              `id` = v_hierarchy_node_id
            ;
        END IF;

        -- 有子节点的节点，各子节点的数据求和，再加上本身的数据
        IF (v_entity_type IN ('ISO', 'SPOOL')) THEN

            SELECT
                SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
                SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
                SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
                SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
            INTO v_overall_finished_score, v_overall_total_score, v_overall_actual_hours, v_overall_plan_hours
            FROM
                `wbs_entity_progress` AS `pg`
            WHERE
              (`pg`.`entity_id` IN (SELECT `pn`.`entity_id`
                                       FROM `hierarchy_node` `hn` JOIN `project_node` `pn`
                                        ON `hn`.`project_id` = `pn`.`project_id` AND `hn`.`node_id` = `pn`.`id` AND `pn`.`deleted` = FALSE
                                       WHERE `hn`.`path` like concat('%/', v_hierarchy_node_id, '/%') AND `hn`.`hierarchy_type` = "PIPING" AND `hn`.`deleted` = FALSE
                                    )
                    OR `pg`.`entity_id` = v_entity_id
                  )
              AND `pg`.`stage_name` IS NULL
            ;

            -- 更新层级节点上的进度统计信息
            UPDATE
              `hierarchy_node`
            SET
              `overall_finished_score`      = v_overall_finished_score,
              `overall_total_score`         = v_overall_total_score,
              `overall_plan_hours`          = v_overall_plan_hours,
              `overall_actual_hours`        = v_overall_actual_hours
            WHERE
              `id` = v_hierarchy_node_id
            ;
        END IF;
    END IF;
    -- 区域维度，MODULE节点，需要求区域维度，试压包，清洁包维度的数据和
    -- 区域维度，MODULE以上节点，求自身和MODULE节点数据的和
    IF (v_hierarchy_type = "PIPING") THEN
            SELECT
                SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
                SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
                SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
                SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
            INTO v_overall_finished_score, v_overall_total_score, v_overall_actual_hours, v_overall_plan_hours
            FROM
                `wbs_entity_progress` AS `pg`
            WHERE
              (`pg`.`entity_id` IN (SELECT `pn`.`entity_id`
                                       FROM `hierarchy_node` `hn` JOIN `project_node` `pn`
                                        ON `hn`.`project_id` = `pn`.`project_id` AND `hn`.`node_id` = `pn`.`id` AND `pn`.`deleted` = FALSE
                                       WHERE `hn`.`path` like concat('%/', v_hierarchy_node_id, '/%') AND `hn`.`hierarchy_type` = "PIPING" AND `hn`.`deleted` = FALSE
                                    )
                    OR `pg`.`entity_id` = v_entity_id
                  )
            ;

            -- 更新层级节点上的进度统计信息
            UPDATE
              `hierarchy_node`
            SET
              `overall_finished_score`      = v_overall_finished_score,
              `overall_total_score`         = v_overall_total_score,
              `overall_plan_hours`          = v_overall_plan_hours,
              `overall_actual_hours`        = v_overall_actual_hours
            WHERE
              `id` = v_hierarchy_node_id
            ;
    END IF;
    -- 模块下的其他维度（层，试压包，清洁包）
    -- 试压包，清洁包，只统计自己维度的数据；
    -- 由于层维度是项目可选项，所以层维度与区域维度一样统计总体进度
    -- 层
    IF (v_hierarchy_type = "PIPING") THEN
        -- 不会有子节点的节点，只更新自己的进度工时信息
        IF (v_entity_type IN ('PIPE_PIECE', 'WELD_JOINT', 'COMPONENT')) THEN
            SELECT
                SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
                SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
                SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
                SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
            INTO v_overall_finished_score, v_overall_total_score, v_overall_actual_hours, v_overall_plan_hours
            FROM
                `wbs_entity_progress` AS `pg`
            WHERE
              `pg`.`entity_id` = v_entity_id
            GROUP BY
              `pg`.`entity_id`
            ;

            -- 更新层级节点上的进度统计信息
            UPDATE
              `hierarchy_node`
            SET
              `overall_finished_score`      = v_overall_finished_score,
              `overall_total_score`         = v_overall_total_score,
              `overall_plan_hours`          = v_overall_plan_hours,
              `overall_actual_hours`        = v_overall_actual_hours
            WHERE
              `id` = v_hierarchy_node_id
            ;
        END IF;

        -- 有子节点的节点，各子节点的数据求和，再加上本身的数据
        IF (v_entity_type IN ('ISO', 'SPOOL')) THEN

            SELECT
                SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
                SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
                SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
                SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
            INTO v_overall_finished_score, v_overall_total_score, v_overall_actual_hours, v_overall_plan_hours
            FROM
                `wbs_entity_progress` AS `pg`
            WHERE
              (`pg`.`entity_id` IN (SELECT `pn`.`entity_id`
                                       FROM `hierarchy_node` `hn` JOIN `project_node` `pn`
                                        ON `hn`.`project_id` = `pn`.`project_id` AND `hn`.`node_id` = `pn`.`id` AND `pn`.`deleted` = FALSE
                                       WHERE `hn`.`path` like concat('%/', v_hierarchy_node_id, '/%') AND `hn`.`hierarchy_type` = "PIPING" AND `hn`.`deleted` = FALSE
                                    )
                    OR `pg`.`entity_id` = v_entity_id
                  )
            ;

            -- 更新层级节点上的进度统计信息
            UPDATE
              `hierarchy_node`
            SET
              `overall_finished_score`      = v_overall_finished_score,
              `overall_total_score`         = v_overall_total_score,
              `overall_plan_hours`          = v_overall_plan_hours,
              `overall_actual_hours`        = v_overall_actual_hours
            WHERE
              `id` = v_hierarchy_node_id
            ;
        END IF;

        -- 对于层节点(node_type是LAYER_PACKAGE的节点)，各子节点的数据求和，再加上本身的数据
        IF (v_node_type = "PIPING") THEN

            SELECT
                SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
                SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
                SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
                SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
            INTO v_overall_finished_score, v_overall_total_score, v_overall_actual_hours, v_overall_plan_hours
            FROM
                `wbs_entity_progress` AS `pg`
            WHERE
              (`pg`.`entity_id` IN (SELECT `pn`.`entity_id`
                                       FROM `hierarchy_node` `hn` JOIN `project_node` `pn`
                                        ON `hn`.`project_id` = `pn`.`project_id` AND `hn`.`node_id` = `pn`.`id` AND `pn`.`deleted` = FALSE
                                       WHERE `hn`.`path` like concat('%/', v_hierarchy_node_id, '/%') AND `hn`.`hierarchy_type` = "PIPING" AND `hn`.`deleted` = FALSE
                                    )
                    OR `pg`.`entity_id` = v_entity_id
                  )
            ;

            -- 更新层级节点上的进度统计信息
            UPDATE
              `hierarchy_node`
            SET
              `overall_finished_score`      = v_overall_finished_score,
              `overall_total_score`         = v_overall_total_score,
              `overall_plan_hours`          = v_overall_plan_hours,
              `overall_actual_hours`        = v_overall_actual_hours
            WHERE
              `id` = v_hierarchy_node_id
            ;
        END IF;
    END IF;
    -- 试压包
    IF (v_hierarchy_type = 'PRESSURE_TEST_PACKAGE') THEN
        SELECT
            SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
            SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
            SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
            SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
        INTO v_ptp_finished_score, v_ptp_total_score, v_ptp_actual_hours, v_ptp_plan_hours
        FROM
            `wbs_entity_progress` AS `pg`
        WHERE
          (`pg`.`entity_id` IN (SELECT `pn`.`entity_id`
                                   FROM `hierarchy_node` `hn` JOIN `project_node` `pn`
                                    ON `hn`.`project_id` = `pn`.`project_id` AND `hn`.`node_id` = `pn`.`id` AND `pn`.`deleted` = FALSE
                                   WHERE `hn`.`path` like concat('%/', v_hierarchy_node_id, '/%') AND `hn`.`hierarchy_type` = 'PRESSURE_TEST_PACKAGE' AND `hn`.`deleted` = FALSE
                                )
                OR `pg`.`entity_id` = v_entity_id
              )
          AND `pg`.`stage_name` = 'PRESSURE_TEST'
        ;

        -- 更新层级节点上的进度统计信息
        UPDATE
          `hierarchy_node`
        SET
          `ptp_finished_score`          = v_ptp_finished_score,
          `ptp_total_score`             = v_ptp_total_score,
          `ptp_plan_hours`              = v_ptp_plan_hours,
          `ptp_actual_hours`            = v_ptp_actual_hours

        WHERE
          `id` = v_hierarchy_node_id
        ;
    END IF;
    -- 清洁包
    IF (v_hierarchy_type = 'CLEAN_PACKAGE') THEN
        SELECT
            SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
            SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
            SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
            SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
        INTO v_cleanness_finished_score, v_cleanness_total_score, v_cleanness_actual_hours, v_cleanness_plan_hours
        FROM
            `wbs_entity_progress` AS `pg`
        WHERE
          (`pg`.`entity_id` IN (SELECT `pn`.`entity_id`
                                   FROM `hierarchy_node` `hn` JOIN `project_node` `pn`
                                    ON `hn`.`project_id` = `pn`.`project_id` AND `hn`.`node_id` = `pn`.`id` AND `pn`.`deleted` = FALSE
                                   WHERE `hn`.`path` like concat('%/', v_hierarchy_node_id, '/%') AND `hn`.`hierarchy_type` = 'CLEAN_PACKAGE' AND `hn`.`deleted` = FALSE
                                )
                OR `pg`.`entity_id` = v_entity_id
              )
          AND `pg`.`stage_name` = 'CLEANNESS'
        ;

        -- 更新层级节点上的进度统计信息
        UPDATE
          `hierarchy_node`
        SET
          `cleanness_finished_score`    = v_cleanness_finished_score,
          `cleanness_total_score`       = v_cleanness_total_score,
          `cleanness_plan_hours`        = v_cleanness_plan_hours,
          `cleanness_actual_hours`      = v_cleanness_actual_hours

        WHERE
          `id` = v_hierarchy_node_id
        ;
    END IF;

    -- -------------------------- 系统维度 ------------------------------------
    -- 系统维度，只统计自己维度的数据
    IF (v_hierarchy_type = 'SUB_SYSTEM') THEN
            SELECT
                SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
                SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
                SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
                SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
            INTO v_mc_finished_score, v_mc_total_score, v_mc_actual_hours, v_mc_plan_hours
            FROM
                `wbs_entity_progress` AS `pg`
            WHERE
              (`pg`.`entity_id` IN (SELECT `pn`.`entity_id`
                                           FROM `hierarchy_node` `hn` JOIN `project_node` `pn`
                                            ON `hn`.`project_id` = `pn`.`project_id` AND `hn`.`node_id` = `pn`.`id` AND `pn`.`deleted` = FALSE
                                           WHERE `hn`.`path` like concat('%/', v_hierarchy_node_id, '/%') AND `hn`.`hierarchy_type` IN ('SYSTEM', 'SUB_SYSTEM') AND `hn`.`deleted` = FALSE
                                    )
                    OR `pg`.`entity_id` = v_entity_id
                  )
              AND `pg`.`stage_name` = 'MECHANICAL_COMPLETION'
            ;

            -- 更新层级节点上的进度统计信息
            UPDATE
              `hierarchy_node`
            SET
              `mc_finished_score`           = v_mc_finished_score,
              `mc_total_score`              = v_mc_total_score,
              `mc_plan_hours`               = v_mc_plan_hours,
              `mc_actual_hours`             = v_mc_actual_hours
            WHERE
              `id` = v_hierarchy_node_id
            ;
    END IF;

    -- 系统维度，子系统节点，只统计自己维度的数据
    IF (v_hierarchy_type = 'SUB_SYSTEM') THEN
            SELECT
                SUM(IF(`pg`.`finished_score` IS NULL, 0, `pg`.`finished_score`))       AS `finished_score`,
                SUM(IF(`pg`.`total_score` IS NULL, 0, `pg`.`total_score`))             AS `total_score`,
                SUM(IF(`pg`.`finished_duration` IS NULL, 0, `pg`.`finished_duration`)) AS `finished_duration`,
                SUM(IF(`pg`.`total_duration` IS NULL, 0, `pg`.`total_duration`))       AS `total_duration`
            INTO v_mc_finished_score, v_mc_total_score, v_mc_actual_hours, v_mc_plan_hours
            FROM
                `wbs_entity_progress` AS `pg`
            WHERE
              (`pg`.`entity_id` IN (SELECT `pn`.`entity_id`
                                           FROM `hierarchy_node` `hn` JOIN `project_node` `pn`
                                            ON `hn`.`project_id` = `pn`.`project_id` AND `hn`.`node_id` = `pn`.`id` AND `pn`.`deleted` = FALSE
                                           WHERE `hn`.`path` like concat('%/', v_hierarchy_node_id, '/%') AND `hn`.`hierarchy_type` = 'SUB_SYSTEM' AND `hn`.`deleted` = FALSE
                                    )
                    OR `pg`.`entity_id` = v_entity_id
                  )
              AND `pg`.`stage_name` = 'MECHANICAL_COMPLETION'
            ;

            -- 更新层级节点上的进度统计信息
            UPDATE
              `hierarchy_node`
            SET
              `mc_finished_score`           = v_mc_finished_score,
              `mc_total_score`              = v_mc_total_score,
              `mc_plan_hours`               = v_mc_plan_hours,
              `mc_actual_hours`             = v_mc_actual_hours
            WHERE
              `id` = v_hierarchy_node_id
            ;
    END IF;

  END $$

-- -----------------------------------------------------------------------------
-- 层级节点进度更新定时任务。
-- 执行频率：1小时
-- -----------------------------------------------------------------------------
CREATE EVENT `set_progress_on_hierarchy_nodes_hourly`
  ON SCHEDULE EVERY 60 MINUTE STARTS TIMESTAMP '2018-11-01 00:00:00'
  ON COMPLETION PRESERVE ENABLE
DO
  BEGIN
    CALL `start_setting_progress_on_hierarchy_nodes`();
  END $$

DELIMITER ;

-- 启用定时任务
SET GLOBAL event_scheduler = 1;
