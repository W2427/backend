USE saint_whale_tasks;
DROP PROCEDURE IF EXISTS `set_progress_on_hierarchy_nodes_of_project`;
delimiter ;;
CREATE DEFINER=`backend`@`%` PROCEDURE `set_progress_on_hierarchy_nodes_of_project`(IN v_project_id BIGINT(20))
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

  END

;;
delimiter ;
