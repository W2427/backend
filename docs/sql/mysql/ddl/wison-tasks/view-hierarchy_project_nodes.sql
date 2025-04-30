USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 项目节点层级视图
-- hierarchy_project_nodes
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `hierarchy_project_nodes` AS
  SELECT
    `hn`.`id`               AS `id`,
    `hn`.`org_id`           AS `org_id`,
    `hn`.`project_id`       AS `project_id`,
    `hn`.`path`             AS `path`,
    `hn`.`depth`            AS `depth`,
    `hn`.`parent_id`        AS `parent_id`,
    `hn`.`hierarchy_type`   AS `hierarchy_type`,
    `hn`.`node_id`          AS `node_id`,
    `pn`.`node_type`        AS `node_type`,
    `pn`.`entity_id`        AS `entity_id`,
    `pn`.`entity_type`      AS `entity_type`,
    `pn`.`entity_sub_type`  AS `entity_sub_type`,
    `pn`.`no`               AS `no`
  FROM
    `hierarchy_node` AS `hn`
    INNER JOIN `project_node` AS `pn`
      ON `pn`.`id` = `hn`.`node_id`
      AND `pn`.`deleted` = 0
  WHERE
    `hn`.`deleted` = 0
;
