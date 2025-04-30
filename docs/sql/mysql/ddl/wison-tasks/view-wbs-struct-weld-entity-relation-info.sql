USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wbs_struct_weld_entity_relation_info` AS
SELECT
  `parent_pn`.`no` AS `module_parent_no`,
  `pn`.`entity_type` AS `entity_type`,
  `parent_pn`.`entity_type` AS `parent_entity_type`,
  `entity`.`id` AS `id`,
  `entity`.`no` AS `no`,
  `entity`.`org_id` AS `org_id`,
  `entity`.`project_id` AS `project_id`,
  `wbs_entry`.`process` AS `process`,
  `wbs_entry_state`.`running_status` AS `running_status`,
  `entity`.`status` AS `status`,
  `entity`.`work_class` AS `work_class`
FROM
  ((
     `wbs_entry`
       JOIN `wbs_entry_state` ON ((
         `wbs_entry`.`id` = `wbs_entry_state`.`wbs_entry_id`
       )))
    JOIN ((((
    `entity_structure_weld` `entity`
      LEFT JOIN `project_node` `pn` ON (((
                                              `entity`.`id` = `pn`.`entity_id`
                                            )
      AND ( `entity`.`project_id` = `pn`.`project_id` )
      AND ( `pn`.`deleted` = 0 )
      AND ( `pn`.`entity_type` = 'STRUCT_WELD_JOINT' ))))
    LEFT JOIN `hierarchy_node` `hn_on_structure` ON (((
                                                           `pn`.`project_id` = `hn_on_structure`.`project_id`
                                                         )
      AND ( `pn`.`id` = `hn_on_structure`.`node_id` )
      AND ( `hn_on_structure`.`deleted` = 0 )
      AND ( `hn_on_structure`.`hierarchy_type` = 'STRUCTURE' ))))
    LEFT JOIN `hierarchy_node` `parent_hn_on_structure` ON (((
                                                                  `hn_on_structure`.`parent_id` = `parent_hn_on_structure`.`id`
                                                                )
      AND ( `hn_on_structure`.`project_id` = `parent_hn_on_structure`.`project_id` )
      AND ( `parent_hn_on_structure`.`deleted` = `pn`.`deleted` )
      AND ( `parent_hn_on_structure`.`hierarchy_type` = 'STRUCTURE' ))))
    LEFT JOIN `project_node` `parent_pn` ON (((
                                                   `parent_hn_on_structure`.`node_id` = `parent_pn`.`id`
                                                 )
      AND ( `parent_pn`.`project_id` = `pn`.`project_id` )
      AND ( `parent_pn`.`deleted` = 0 )
      AND (
                                                   `parent_pn`.`entity_type` IN ( 'WP01', 'WP02', 'WP03', 'WP04', 'WP05' ))))) ON ((
      `entity`.`id` = `wbs_entry`.`entity_id`
    )))
WHERE
  (
      `entity`.`deleted` = 0)
