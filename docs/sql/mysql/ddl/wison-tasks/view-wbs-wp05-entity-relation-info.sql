USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wbs_wp05_entity_relation_info` AS
SELECT
  `wp04_pn`.`no` AS `module_parent_no`,
  `wp05`.`id` AS `id`,
  `wp05`.`status` AS `status`,
  `wp05`.`no` AS `no`,
  `wp05`.`org_id` AS `org_id`,
  `wp05`.`project_id` AS `project_id`,
  `wp05`.`weight` AS `weight`,
  `wp05`.`work_class` AS `work_class`,
  `wbs_entry_state`.`running_status` AS `running_status`,
  `wbs_entry`.`process` AS `process`,
  `pn`.`entity_type` AS `entity_type`,
  `wp04_pn`.`entity_type` AS `parent_entity_type`,
  `wp04_pn`.`entity_id` AS `parent_id`
FROM
  ((((((
    `entity_wp05` `wp05`
      LEFT JOIN `project_node` `pn` ON (((
                                              `wp05`.`id` = `pn`.`entity_id`
                                            )
      AND ( `pn`.`project_id` = `wp05`.`project_id` )
      AND ( `pn`.`entity_type` = 'WP05' )
      AND ( `pn`.`deleted` = 0 ))))
    LEFT JOIN `hierarchy_node` `wp05_hn` ON (((
                                                   `pn`.`project_id` = `wp05_hn`.`project_id`
                                                 )
      AND ( `wp05_hn`.`node_id` = `pn`.`id` )
      AND ( `wp05_hn`.`deleted` = 0 )
      AND ( `wp05_hn`.`hierarchy_type` = 'STRUCTURE' ))))
    LEFT JOIN `hierarchy_node` `wp04_hn` ON (((
                                                   `wp04_hn`.`id` = `wp05_hn`.`parent_id`
                                                 )
      AND ( `wp04_hn`.`project_id` = `pn`.`project_id` )
      AND ( `wp04_hn`.`deleted` = 0 )
      AND ( `wp04_hn`.`hierarchy_type` = 'STRUCTURE' ))))
    LEFT JOIN `project_node` `wp04_pn` ON (((
                                                 `wp04_hn`.`node_id` = `wp04_pn`.`id`
                                               )
      AND ( `wp04_pn`.`project_id` = `pn`.`project_id` )
      AND ( `wp04_pn`.`deleted` = 0 )
      AND (
                                                 `wp04_pn`.`entity_type` IN ( 'WP01', 'WP02', 'WP03', 'WP04', 'WP05' )))))
    JOIN `wbs_entry` ON (((
                              `wp05`.`id` = `wbs_entry`.`entity_id`
                            )
      AND ( `wbs_entry`.`entity_type` = 'WP05' ))))
    JOIN `wbs_entry_state` ON ((
      `wbs_entry`.`id` = `wbs_entry_state`.`wbs_entry_id`
    )))
WHERE
  (
      `wp05`.`deleted` = 0)
