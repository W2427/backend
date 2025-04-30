USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wbs_wp03_entity_relation_info` AS
SELECT
  `parent_pn`.`no` AS `module_parent_no`,
  `pn`.`entity_type` AS `entity_type`,
  `wp3`.`id` AS `id`,
  `wp3`.`status` AS `status`,
  `wp3`.`no` AS `no`,
  `wp3`.`org_id` AS `org_id`,
  `wp3`.`project_id` AS `project_id`,
  `wp3`.`work_class` AS `work_class`,
  `wp3`.`tag_description` AS `tag_description`,
  `wbs_entry`.`process` AS `process`,
  `wbs_entry_state`.`running_status` AS `running_status`,
  `parent_pn`.`entity_type` AS `parent_entity_type`,
  `wp3`.`weight` AS `weight`
FROM
  ((((((
    `entity_wp03` `wp3`
      LEFT JOIN `project_node` `pn` ON (((
                                              `pn`.`entity_id` = `wp3`.`id`
                                            )
      AND ( `pn`.`project_id` = `wp3`.`project_id` )
      AND ( `pn`.`deleted` = `wp3`.`deleted` )
      AND ( `pn`.`entity_type` = 'WP03' ))))
    LEFT JOIN `hierarchy_node` `hn_on_structure` ON (((
                                                           `pn`.`project_id` = `hn_on_structure`.`project_id`
                                                         )
      AND ( `pn`.`id` = `hn_on_structure`.`node_id` )
      AND ( `hn_on_structure`.`hierarchy_type` = 'STRUCTURE' )
      AND ( `hn_on_structure`.`deleted` = 0 ))))
    LEFT JOIN `hierarchy_node` `parent_hn_on_structure` ON (((
                                                                  `parent_hn_on_structure`.`project_id` = `hn_on_structure`.`project_id`
                                                                )
      AND ( `hn_on_structure`.`parent_id` = `parent_hn_on_structure`.`id` )
      AND ( `parent_hn_on_structure`.`hierarchy_type` = 'STRUCTURE' )
      AND ( `parent_hn_on_structure`.`deleted` = 0 ))))
    LEFT JOIN `project_node` `parent_pn` ON (((
                                                   `parent_hn_on_structure`.`project_id` = `parent_pn`.`project_id`
                                                 )
      AND ( `parent_pn`.`id` = `parent_hn_on_structure`.`node_id` )
      AND ( `parent_pn`.`entity_type` = 'WP02' )
      AND ( `parent_pn`.`deleted` = 0 ))))
    JOIN `wbs_entry` ON ((
        `wp3`.`id` = `wbs_entry`.`entity_id`
      )))
    JOIN `wbs_entry_state` ON ((
      `wbs_entry`.`id` = `wbs_entry_state`.`wbs_entry_id`
    )))
WHERE
  (
      `wp3`.`deleted` = 0)
