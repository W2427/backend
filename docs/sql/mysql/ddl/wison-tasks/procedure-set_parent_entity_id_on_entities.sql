USE saint_whale_tasks;
DROP PROCEDURE IF EXISTS `set_parent_entity_id_on_entities`;
delimiter ;;
CREATE DEFINER=`backend`@`%` PROCEDURE `set_parent_entity_id_on_entities`(IN v_project_id BIGINT(20))
BEGIN

    -- 设置管线的项目节点的管线实体 ID 字段为自身实体 ID
    UPDATE
      `entity_iso` AS `e`
      INNER JOIN `project_node` AS `pn`
        ON `pn`.`entity_id` = `e`.`id`
    SET
      `pn`.`iso_entity_id` = `e`.`id`,
      `pn`.`iso_no` = `e`.`no`,
      `pn`.`spool_entity_id` = NULL,
      `pn`.`spool_no` = NULL
    WHERE
      `e`.`project_id` = v_project_id
    ;

    -- 设置单管的所属管线实体 ID，并将单管实体 ID 设置为自身实体 ID
    UPDATE
      `entity_spool` AS `e`
      INNER JOIN `project_node` AS `pn`
        ON `pn`.`entity_id` = `e`.`id`
        AND `pn`.`deleted` = 0
      INNER JOIN `hierarchy_node` AS `hn`
        ON `hn`.`node_id` = `pn`.`id`
        AND `hn`.`hierarchy_type` = "PIPING"
        AND `hn`.`deleted` = 0
      INNER JOIN `hierarchy_node` AS `phn`
        ON `phn`.`id` = `hn`.`parent_id`
        AND `phn`.`deleted` = 0
      INNER JOIN `project_node` AS `ppn`
        ON `ppn`.`id` = `phn`.`node_id`
        AND `ppn`.`deleted` = 0
      INNER JOIN `entity_iso` AS `p`
        ON `p`.`id` = `ppn`.`entity_id`
    SET
      `e`.`iso_entity_id` = `p`.`id`,
      `e`.`iso_no` = `p`.`no`,
      `pn`.`iso_entity_id` = `p`.`id`,
      `pn`.`iso_no` = `p`.`no`,
      `pn`.`spool_entity_id` = `e`.`id`,
      `pn`.`spool_no` = `e`.`no`
    WHERE
      `e`.`project_id` = v_project_id
    ;

    -- 设置管段的所属管线实体 ID 和所属单管 ID
    -- 管段必属于一个单管
    UPDATE
      `entity_pipe_piece` AS `e`
      INNER JOIN `project_node` AS `pn`
        ON `pn`.`entity_id` = `e`.`id`
        AND `pn`.`deleted` = 0
      INNER JOIN `hierarchy_node` AS `hn`
        ON `hn`.`node_id` = `pn`.`id`
        AND `hn`.`hierarchy_type` = "PIPING"
        AND `hn`.`deleted` = 0
      INNER JOIN `hierarchy_node` AS `phn`
        ON `phn`.`id` = `hn`.`parent_id`
        AND `phn`.`deleted` = 0
      INNER JOIN `project_node` AS `ppn`
        ON `ppn`.`id` = `phn`.`node_id`
        AND `ppn`.`deleted` = 0
      INNER JOIN `entity_spool` AS `p`
        ON `p`.`id` = `ppn`.`entity_id`
    SET
      `e`.`iso_entity_id` = `p`.`iso_entity_id`,
      `e`.`iso_no` = `p`.`iso_no`,
      `e`.`spool_entity_id` = `p`.`id`,
      `e`.`spool_no` = `p`.`no`,
      `pn`.`iso_entity_id` = `p`.`iso_entity_id`,
      `pn`.`iso_no` = `p`.`iso_no`,
      `pn`.`spool_entity_id` = `p`.`id`,
      `pn`.`spool_no` = `p`.`no`
    WHERE
      `e`.`project_id` = v_project_id
    ;

    -- 设置焊口的所属管线实体 ID 和所属单管实体 ID
    -- 焊口可直接隶属于管线而无所属单管
    UPDATE
      `entity_weld` AS `e`
      INNER JOIN `project_node` AS `pn`
        ON `pn`.`entity_id` = `e`.`id`
        AND `pn`.`deleted` = 0
      INNER JOIN `hierarchy_node` AS `hn`
        ON `hn`.`node_id` = `pn`.`id`
        AND `hn`.`hierarchy_type` = "PIPING"
        AND `hn`.`deleted` = 0
      INNER JOIN `hierarchy_node` AS `phn`
        ON `phn`.`id` = `hn`.`parent_id`
        AND `phn`.`deleted` = 0
      INNER JOIN `project_node` AS `ppn`
        ON `ppn`.`id` = `phn`.`node_id`
        AND `ppn`.`deleted` = 0
      LEFT OUTER JOIN `entity_iso` AS `ip`
        ON `ip`.`id` = `ppn`.`entity_id`
      LEFT OUTER JOIN `entity_spool` AS `sp`
        ON `sp`.`id` = `ppn`.`entity_id`
    SET
      `e`.`iso_entity_id` = IFNULL(`sp`.`iso_entity_id`, `ip`.`id`),
      `e`.`iso_no` = IFNULL(`sp`.`iso_no`, `ip`.`no`),
      `e`.`spool_entity_id` = `sp`.`id`,
      `e`.`spool_no` = `sp`.`no`,
      `pn`.`iso_entity_id` = IFNULL(`sp`.`iso_entity_id`, `ip`.`id`),
      `pn`.`iso_no` = IFNULL(`sp`.`iso_no`, `ip`.`no`),
      `pn`.`spool_entity_id` = `sp`.`id`,
      `pn`.`spool_no` = `sp`.`no`
    WHERE
      `e`.`project_id` = v_project_id
    ;

    -- 设置组件的所属管线实体 ID 和所属单管实体 ID
    -- 组件可直接隶属于管线而无所属单管
    UPDATE
      `entity_components` AS `e`
      INNER JOIN `project_node` AS `pn`
        ON `pn`.`entity_id` = `e`.`id`
        AND `pn`.`deleted` = 0
      INNER JOIN `hierarchy_node` AS `hn`
        ON `hn`.`node_id` = `pn`.`id`
        AND `hn`.`hierarchy_type` = "PIPING"
        AND `hn`.`deleted` = 0
      INNER JOIN `hierarchy_node` AS `phn`
        ON `phn`.`id` = `hn`.`parent_id`
        AND `phn`.`deleted` = 0
      INNER JOIN `project_node` AS `ppn`
        ON `ppn`.`id` = `phn`.`node_id`
        AND `ppn`.`deleted` = 0
      LEFT OUTER JOIN `entity_iso` AS `ip`
        ON `ip`.`id` = `ppn`.`entity_id`
      LEFT OUTER JOIN `entity_spool` AS `sp`
        ON `sp`.`id` = `ppn`.`entity_id`
    SET
      `e`.`iso_entity_id` = IFNULL(`sp`.`iso_entity_id`, `ip`.`id`),
      `e`.`iso_no` = IFNULL(`sp`.`iso_no`, `ip`.`no`),
      `e`.`spool_entity_id` = `sp`.`id`,
      `e`.`spool_no` = `sp`.`no`,
      `pn`.`iso_entity_id` = IFNULL(`sp`.`iso_entity_id`, `ip`.`id`),
      `pn`.`iso_no` = IFNULL(`sp`.`iso_no`, `ip`.`no`),
      `pn`.`spool_entity_id` = `sp`.`id`,
      `pn`.`spool_no` = `sp`.`no`
    WHERE
      `e`.`project_id` = v_project_id
    ;

    CALL `set_parent_entity_id_on_wbs_entries`(v_project_id);

  END

;;
delimiter ;
