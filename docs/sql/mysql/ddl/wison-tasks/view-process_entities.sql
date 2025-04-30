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
  `ecp`.func_part				AS `func_part`,
  `ect`.`id`            AS `entity_type_id`,
  `ect`.`name_en`       AS `entity_type`,
  `ec`.`id`             AS `entity_sub_type_id`,
  `ec`.`name_en`        AS `entity_sub_type`,
  `pn`.`entity_id`      AS `entity_id`,
  `hn`.`id`             AS `hierarchy_node_id`,
  `hn`.`depth`          AS `hierarchy_depth`,
  `hn`.`parent_id`      AS `parent_hierarchy_node_id`,
  `hn`.`hierarchy_type` AS `hierarchy_type`,
  `ahn`.`hierarchy_ancestor_id`       AS `hierarchy_ancestor_id`,
  `pn`.`id`             AS `project_node_id`,
  `pn`.`no`             AS `project_node_no`,
  `pn`.`dwg_sht_no`     AS `dwg_sht_no`,
  `pn`.`work_load`      AS `work_load`,
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
         ON `ect`.`id` = `ec`.`entity_type_id`
           AND `ect`.`type` = 'READONLY'
           AND `ect`.`status` = 'ACTIVE'

    JOIN `project_node` `pn`
         ON `pn`.`project_id` = `s`.`project_id`
           AND `pn`.`entity_sub_type` = `ec`.`name_en`
           AND `pn`.`deleted` = 0
           AND `pn`.`is_cancelled` = 0

    JOIN `hierarchy_node` AS `hn`
         ON `hn`.`node_id` = `pn`.`id`
           AND `hn`.`deleted` = 0

    JOIN `hierarchy_node_relation` AS `ahn`
         ON (`ahn`.`hierarchy_id` = `hn`.`id`)

WHERE
    `s`.`status` = 'ACTIVE'
;
