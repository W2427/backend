USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wp04_hierarchy_info` AS
SELECT
	`wp04_hn`.`hierarchy_type` AS `hierarchy_type`,
	`wp04_hn`.`id` AS `hierarchy_id`,
	`wp04_hn`.`path` AS `path_on_structure`,
	`wp03_hn`.`id` AS `parent_hierarchy_id`,
	`wp03_pn`.`no` AS `module_parent_no`,
	`wp03_pn`.`id` AS `module_parent_id`,
	`pn`.`id` AS `project_node_id`,
	`pn`.`node_type` AS `node_type`,
	`pn`.`entity_type` AS `entity_type`,
	`pn`.`entity_sub_type` AS `subEntity_type`,
	`pn`.`wp01_id` AS `wp01_id`,
	`pn`.`wp01_no` AS `wp01_no`,
	`pn`.`wp02_id` AS `wp02_id`,
	`pn`.`wp02_no` AS `wp02_no`,
	`pn`.`wp03_id` AS `wp03_id`,
	`pn`.`wp03_no` AS `wp03_no`,
	`pn`.`wp04_id` AS `wp04_id`,
	`pn`.`wp04_no` AS `wp04_no`,
	`wp04`.*
FROM
	`entity_wp04` `wp04`
	LEFT JOIN `project_node` `pn` ON `wp04`.`id` = `pn`.`entity_id`
	AND `pn`.`project_id` = `wp04`.`project_id`
	AND `pn`.`entity_type` = 'WP04'
	AND `pn`.`deleted` = 0
	LEFT JOIN `hierarchy_node` `wp04_hn` ON `pn`.`project_id` = `wp04_hn`.`project_id`
	AND `pn`.`id` = `wp04_hn`.`node_id`
	AND `wp04_hn`.`deleted` = 0
	AND `wp04_hn`.`hierarchy_type` = 'STRUCTURE'
	LEFT JOIN `hierarchy_node` `wp03_hn` ON `wp03_hn`.`id` = `wp04_hn`.`parent_id`
	AND `wp03_hn`.`project_id` = `wp04_hn`.`project_id`
	AND `wp03_hn`.`deleted` = 0
	AND `wp03_hn`.`hierarchy_type` = 'STRUCTURE'
	LEFT JOIN `project_node` `wp03_pn` ON `wp03_hn`.`node_id` = `wp03_pn`.`id`
	AND `wp03_hn`.`project_id` = `pn`.`project_id`
	AND `wp03_pn`.`deleted` = 0
	AND `wp03_pn`.`entity_type` IN ( 'WP01', 'WP02', 'WP03', 'WP04', 'WP05' )
WHERE `wp04`.deleted = 0;
