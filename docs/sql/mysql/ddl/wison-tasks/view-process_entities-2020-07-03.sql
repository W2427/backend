USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 工序-实体视图
-- View structure for process_entity
-- ----------------------------
CREATE OR REPLACE VIEW `process_entity` AS
  SELECT
    concat(`s`.`id`,':',`p`.`id`,':',`hn`.`id`) AS `id`,
    `s`.`org_id`          AS `org_id`,
    `s`.`project_id`      AS `project_id`,
    `s`.`id`              AS `stage_id`,
    `s`.`name_en`         AS `stage`,
    `p`.`id`              AS `process_id`,
    `p`.`name_en`         AS `process`,
    `ect`.`id`            AS `entity_type_id`,
    `ect`.`name_en`       AS `entity_type`,
    `ec`.`id`             AS `entity_sub_type_id`,
    `ec`.`name_en`        AS `entity_sub_type`,
    `pn`.`entity_id`      AS `entity_id`,
    `hn`.`id`             AS `hierarchy_node_id`,
    `hn`.`path`           AS `hierarchy_path`,
    `hn`.`depth`          AS `hierarchy_depth`,
    `hn`.`parent_id`      AS `parent_hierarchy_node_id`,
    `hn`.`hierarchy_type` AS `hierarchy_type`,
    `mhn`.`id`            AS `module_hierarchy_node_id`,
    `pn`.`iso_no`         AS `iso_project_node_no`,
    `pn`.`id`             AS `project_node_id`,
    `pn`.`no`             AS `project_node_no`,
    `pn`.`dwg_sht_no`     AS `dwg_sht_no`,
    `pn`.`work_load`      AS `work_load`,
    `pn`.`wp01_id`        AS `wp01_id`,
    `pn`.`wp01_no`        AS `wp01_no`,
    `pn`.`wp02_id`        AS `wp02_id`,
    `pn`.`wp02_no`        AS `wp02_no`,
    `pn`.`wp03_id`        AS `wp03_id`,
    `pn`.`wp03_no`        AS `wp03_no`,
    `pn`.`wp04_id`        AS `wp04_id`,
    `pn`.`wp04_no`        AS `wp04_no`,
    `pn`.`discipline`     AS `discipline`
  FROM
    `bpm_process_stage` AS `s`
    JOIN `bpm_process` AS `p`
      ON `p`.`process_stage_id` = `s`.`id`
      AND `p`.`status` = 'ACTIVE'

    JOIN `bpm_entity_type_process_relation` AS `ecp`
      ON `ecp`.`process_id` = `p`.`id`
      AND `ecp`.`status` = 'ACTIVE'

    JOIN `bpm_entity_sub_type` AS `ec`
      ON `ec`.`id` = `ecp`.`entity_sub_type_id`
      AND `ec`.`status` = 'ACTIVE'

    JOIN `bpm_entity_type` AS `ect`
      ON `ect`.`id` = `ec`.`entity_category_type_id`
      AND `ect`.`type` = 'READONLY'
      AND `ect`.`status` = 'ACTIVE'

    JOIN `project_node` `pn`
      ON `pn`.`project_id` = `s`.`project_id`
      AND `pn`.`entity_sub_type` = `ec`.`name_en`
      AND `pn`.`deleted` = 0

    JOIN `hierarchy_node` AS `hn`
      ON `hn`.`node_id` = `pn`.`id`
      AND `hn`.`hierarchy_type` IN ("PIPING",'PRESSURE_TEST_PACKAGE','CLEAN_PACKAGE','SUB_SYSTEM')
      AND `hn`.`deleted` = 0

    JOIN `hierarchy_node` AS `phn`
      ON (`phn`.`id` = `hn`.`id` OR `hn`.`path` LIKE concat(`phn`.`path`,`phn`.`id`,'/%'))
      AND `phn`.`hierarchy_type` = `hn`.`hierarchy_type`
      AND `phn`.`deleted` = 0


    LEFT JOIN `hierarchy_project_nodes` `mhn`
      ON `phn`.`path` LIKE concat(`mhn`.`path`,`mhn`.`id`,'/%')
      AND `mhn`.`project_id` = `s`.`project_id`
      AND `mhn`.`hierarchy_type` IN ("PIPING",'PRESSURE_TEST_PACKAGE','CLEAN_PACKAGE','SUB_SYSTEM')

    JOIN `project_node` `mpn` ON `mpn`.`id` = `mhn`.`node_id`
      AND `mpn`.`project_id` = `s`.`project_id`
      AND `mpn`.`node_type` IN ('MODULE','SYSTEM')
      AND `mpn`.`deleted` = 0
    WHERE
      `s`.`status` = 'ACTIVE'
;
