SELECT
  concat( `s`.`id`, ':', `p`.`id`, ':', `hn`.`id` ) AS `id`,
  `s`.`org_id` AS `org_id`,
  `s`.`project_id` AS `project_id`,
  `s`.`id` AS `stage_id`,
  `s`.`name_en` AS `stage`,
  `p`.`id` AS `process_id`,
  `p`.`name_en` AS `process`,
  `ect`.`id` AS `entity_type_id`,
  `ect`.`name_en` AS `entity_type`,
  `ec`.`id` AS `entity_sub_type_id`,
  `ec`.`name_en` AS `entity_sub_type`,
  `pn`.`entity_id` AS `entity_id`,
  `hn`.`id` AS `hierarchy_node_id`,
  `hn`.`path` AS `hierarchy_path`,
  `hn`.`depth` AS `hierarchy_depth`,
  `hn`.`parent_id` AS `parent_hierarchy_node_id`,
  `hn`.`hierarchy_type` AS `hierarchy_type`,
  `ahn`.`module_hierarchy_node_id` AS `module_hierarchy_node_id`,
  `ahn`.`hierarchy_ancestor_id` AS `hierarchy_ancestor_id`,
  `pn`.`iso_no` AS `iso_project_node_no`,
  `pn`.`id` AS `project_node_id`,
  `pn`.`no` AS `project_node_no`,
  `pn`.`dwg_sht_no` AS `dwg_sht_no`,
  `pn`.`work_load` AS `work_load`,
  `pn`.`wp01_id` AS `wp01_id`,
  `pn`.`wp01_no` AS `wp01_no`,
  `pn`.`wp02_id` AS `wp02_id`,
  `pn`.`wp02_no` AS `wp02_no`,
  `pn`.`wp03_id` AS `wp03_id`,
  `pn`.`wp03_no` AS `wp03_no`,
  `pn`.`wp04_id` AS `wp04_id`,
  `pn`.`wp04_no` AS `wp04_no`,
  `pn`.`discipline` AS `discipline`
FROM
  (((((((
    `bpm_process_stage` `s`
      JOIN `bpm_process` `p` ON (((
                                        `p`.`process_stage_id` = `s`.`id`
                                      )
      AND ( `p`.`status` = 'ACTIVE' ))))
    JOIN `bpm_entity_type_process_relation` `ecp` ON (((
                                                                `ecp`.`process_id` = `p`.`id`
                                                              )
    AND ( `ecp`.`status` = 'ACTIVE' ))))
    JOIN `bpm_entity_sub_type` `ec` ON (((
                                               `ec`.`id` = `ecp`.`entity_sub_type_id`
                                             )
    AND ( `ec`.`status` = 'ACTIVE' ))))
    JOIN `bpm_entity_type` `ect` ON (((
                                                    `ect`.`id` = `ec`.`entity_category_type_id`
                                                  )
    AND ( `ect`.`type` = 'READONLY' )
    AND ( `ect`.`status` = 'ACTIVE' ))))
    JOIN `project_node` `pn` ON (((
                                       `pn`.`project_id` = `s`.`project_id`
                                     )
    AND ( `pn`.`entity_sub_type` = `ec`.`name_en` )
    AND ( `pn`.`deleted` = 0 ))))
    JOIN `hierarchy_node` `hn` ON (((
                                         `hn`.`node_id` = `pn`.`id`
                                       )
    AND (
                                         `hn`.`hierarchy_type` IN ( "PIPING", 'PRESSURE_TEST_PACKAGE', 'CLEAN_PACKAGE', 'SUB_SYSTEM' ))
    AND ( `hn`.`deleted` = 0 ))))
    JOIN `hierarchy_node_relation` `ahn` ON ((
      `ahn`.`hierarchy_id` = `hn`.`id`
    )))
WHERE
  (
      `s`.`status` = 'ACTIVE')
