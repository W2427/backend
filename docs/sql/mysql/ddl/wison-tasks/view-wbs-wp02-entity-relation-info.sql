USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wbs_wp02_entity_relation_info` AS
SELECT
  `parent_pn`.`no` AS `module_parent_no`,
  `pn`.`entity_type` AS `entity_type`,
  `wp02`.`id` AS `id`,
  `wp02`.`status` AS `status`,
  `wp02`.`no` AS `no`,
  `wp02`.`org_id` AS `org_id`,
  `wp02`.`project_id` AS `project_id`,
  `wp02`.`tag_description` AS `tag_description`,
  `wbs_entry`.`process` AS `process`,
  `wbs_entry_state`.`running_status` AS `running_status`,
  `wp02`.`work_class` AS `work_class`,
  `parent_pn`.`entity_type` AS `parent_entity_type`,
  `wp02`.`weight` AS `weight`
FROM
  ((((((
    `entity_wp02` `wp02`
      LEFT JOIN `project_node` `pn` ON (((
                                              `wp02`.`id` = `pn`.`entity_id`
                                            )
      AND ( `pn`.`project_id` = `wp02`.`project_id` )
      AND ( `pn`.`entity_type` = 'WP02' )
      AND ( `pn`.`deleted` = 0 ))))
    LEFT JOIN `hierarchy_node` `hn_on_structure` ON (((
                                                           `pn`.`project_id` = `hn_on_structure`.`project_id`
                                                         )
      AND ( `pn`.`id` = `hn_on_structure`.`node_id` )
      AND ( `hn_on_structure`.`deleted` = 0 )
      AND ( `hn_on_structure`.`hierarchy_type` = 'STRUCTURE' ))))
    LEFT JOIN `hierarchy_node` `parent_hn_on_structure` ON (((
                                                                  `parent_hn_on_structure`.`id` = `hn_on_structure`.`parent_id`
                                                                )
      AND ( `parent_hn_on_structure`.`project_id` = `hn_on_structure`.`project_id` )
      AND ( `parent_hn_on_structure`.`deleted` = 0 )
      AND ( `parent_hn_on_structure`.`hierarchy_type` = 'STRUCTURE' ))))
    LEFT JOIN `project_node` `parent_pn` ON (((
                                                   `parent_hn_on_structure`.`node_id` = `parent_pn`.`id`
                                                 )
      AND ( `parent_hn_on_structure`.`project_id` = `parent_pn`.`project_id` )
      AND ( `parent_pn`.`deleted` = 0 )
      AND ( `parent_pn`.`entity_type` = 'WP01' ))))
    JOIN `wbs_entry` ON (((
                              `wp02`.`id` = `wbs_entry`.`entity_id`
                            )
      AND ( `wbs_entry`.`entity_type` = 'WP02' ))))
    JOIN `wbs_entry_state` ON ((
      `wbs_entry`.`id` = `wbs_entry_state`.`wbs_entry_id`
    )))
WHERE
  (
      `wp02`.`deleted` = 0)
