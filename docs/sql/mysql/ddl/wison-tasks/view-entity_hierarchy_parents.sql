USE `saint_whale_tasks`;

-- ----------------------------
-- View structure for entity_hierarchy_parent
-- 实体在层级结构中的上级视图
-- ----------------------------
CREATE OR REPLACE VIEW `entity_hierarchy_parent` AS
  SELECT
    `hn`.`id`               AS `id`,
    `pn`.`org_id`           AS `org_id`,
    `pn`.`project_id`       AS `project_id`,
    `phn`.`id`              AS `parent_hierarchy_node_id`,
    `phn`.`hierarchy_type`  AS `parent_hierarchy_type`,
    `phn`.`path`            AS `parent_path`,
    `ppn`.`id`              AS `parent_project_node_id`,
    `ppn`.`entity_id`       AS `parent_entity_id`,
    `ppn`.`entity_type`     AS `parent_entity_type`,
    `ppn`.`entity_sub_type` AS `parent_entity_sub_type`,
    `ppn`.`no`              AS `parent_no`,
    `hn`.`hierarchy_type`   AS `hierarchy_type`,
    `hn`.`path`             AS `path`,
    `pn`.`id`               AS `project_node_id`,
    `pn`.`entity_id`        AS `entity_id`,
    `pn`.`entity_type`      AS `entity_type`,
    `pn`.`entity_sub_type`  AS `entity_sub_type`,
    `pn`.`no`               AS `no`
  FROM
        `project_node`                     AS `pn`
        INNER JOIN `hierarchy_node`        AS `hn`
          ON `hn`.`node_id` = `pn`.`id`
          AND `hn`.`deleted` = 0

        INNER JOIN `hierarchy_node`        AS `phn`
          ON `phn`.`id` = `hn`.`parent_id`
          AND `phn`.`deleted` = 0

        INNER JOIN `project_node`          AS `ppn`
          ON `ppn`.`id` = `phn`.`node_id`
          AND `ppn`.`deleted` = 0

  WHERE
    `pn`.`node_type` IN ('ENTITY','WP01','WP02','WP03','WP04','WP05','WP_MISC')
        AND `pn`.`deleted` = 0
;
