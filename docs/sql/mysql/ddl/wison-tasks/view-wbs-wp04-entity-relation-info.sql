USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wbs_wp04_entity_relation_info` AS
SELECT
  `wp03_pn`.`no` AS `module_parent_no`,
  `wp04`.`id` AS `id`,
  `wp04`.`status` AS `status`,
  `wp04`.`no` AS `no`,
  `wp04`.`org_id` AS `org_id`,
  `wp04`.`project_id` AS `project_id`,
  `wp04`.`work_class` AS `work_class`,
  `wp04`.`tag_description` AS `tag_description`,
  `wbs_entry`.`process` AS `process`,
  `wbs_entry_state`.`running_status` AS `running_status`,
  `pn`.`entity_type` AS `entity_type`,
  `wp03_pn`.`entity_type` AS `parent_entity_type`,
  `wp04`.`weight` AS `weight`
FROM
  ((((((
    `entity_wp04` `wp04`
      LEFT JOIN `project_node` `pn` ON (((
                                              `wp04`.`id` = `pn`.`entity_id`
                                            )
      AND ( `pn`.`project_id` = `wp04`.`project_id` )
      AND ( `pn`.`entity_type` = 'WP04' )
      AND ( `pn`.`deleted` = 0 ))))
    LEFT JOIN `hierarchy_node` `wp04_hn` ON (((
                                                   `pn`.`project_id` = `wp04_hn`.`project_id`
                                                 )
      AND ( `pn`.`id` = `wp04_hn`.`node_id` )
      AND ( `wp04_hn`.`deleted` = 0 )
      AND ( `wp04_hn`.`hierarchy_type` = 'STRUCTURE' ))))
    LEFT JOIN `hierarchy_node` `wp03_hn` ON (((
                                                   `wp03_hn`.`id` = `wp04_hn`.`parent_id`
                                                 )
      AND ( `wp03_hn`.`project_id` = `wp04_hn`.`project_id` )
      AND ( `wp03_hn`.`deleted` = 0 )
      AND ( `wp03_hn`.`hierarchy_type` = 'STRUCTURE' ))))
    LEFT JOIN `project_node` `wp03_pn` ON (((
                                                 `wp03_hn`.`node_id` = `wp03_pn`.`id`
                                               )
      AND ( `wp03_hn`.`project_id` = `pn`.`project_id` )
      AND ( `wp03_pn`.`deleted` = 0 )
      AND (
                                                 `wp03_pn`.`entity_type` IN ( 'WP01', 'WP02', 'WP03', 'WP04', 'WP05' )))))
    JOIN `wbs_entry` ON (((
                              `wp04`.`id` = `wbs_entry`.`entity_id`
                            )
      AND ( `wbs_entry`.`entity_type` = 'WP04' ))))
    JOIN `wbs_entry_state` ON ((
      `wbs_entry`.`id` = `wbs_entry_state`.`wbs_entry_id`
    )))
WHERE
  (
      `wp04`.`deleted` = 0)
