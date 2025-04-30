USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 节点所在模块类型视图
-- View structure for project_node_module_type
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `project_node_module_type` AS
  SELECT
    `hn`.`id`           AS `id`,
    `ppn`.`org_id`      AS `org_id`,
    `ppn`.`project_id`  AS `project_id`,
    `phn`.`id`          AS `module_hierarchy_node_id`,
    `ppn`.`id`          AS `module_project_node_id`,
    CASE
      WHEN `ppn`.`module_type` IS NOT NULL THEN `ppn`.`module_type`
      ELSE 'SYSTEM' END
                        AS `module_type`,
    `ppn`.`no`          AS `module_no`,
    `pn`.`id`           AS `project_node_id`,
    `pn`.`no`           AS `no`,
    `pn`.`entity_type`  AS `entity_type`
  FROM
    `project_node` `ppn`
    JOIN `hierarchy_node` `phn`
      ON `phn`.`deleted` = 0
      AND `phn`.`node_id` = `ppn`.`id`

    LEFT JOIN `hierarchy_node` `hn`
      ON `hn`.`deleted` = 0
      AND (`hn`.`id` = `phn`.`id` OR `hn`.`path` LIKE concat(`phn`.`path`,`phn`.`id`,'/%'))

    LEFT JOIN `project_node` `pn`
      ON `pn`.`deleted` = 0
      AND `pn`.`id` = `hn`.`node_id`

    WHERE
      `ppn`.`entity_type` IN ('MODULE','SYSTEM')
      AND `ppn`.`deleted` = 0
      AND `ppn`.`module_type` IS NOT NULL
--       AND (`ppn`.`module_type` <> '' OR `ppn`.`node_type` = 'SYSTEM')
;

