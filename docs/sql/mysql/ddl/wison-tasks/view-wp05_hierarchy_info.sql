USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wp05_hierarchy_info` AS
SELECT
	`wp05_hn`.`hierarchy_type` AS `hierarchy_type`,
	`wp05_hn`.`id` AS `hierarchy_id`,
	`wp05_hn`.`path` AS `path_on_structure`,
	`wp04_hn`.`id` AS `parent_hierarchy_id`,
	`wp04_pn`.`no` AS `module_parent_no`,
	`wp04_pn`.`id` AS `module_parent_id`,
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
	`wp05`.*
FROM
	`entity_wp05` `wp05`
	LEFT JOIN `project_node` `pn` ON `wp05`.`id` = `pn`.`entity_id`
	AND `pn`.`project_id` = `wp05`.`project_id`
	AND `pn`.`entity_type` = 'WP05'
	AND `pn`.`deleted` = 0
	LEFT JOIN `hierarchy_node` `wp05_hn` ON `pn`.`project_id` = `wp05_hn`.`project_id`
	AND `wp05_hn`.`node_id` = `pn`.`id`
	AND `wp05_hn`.`deleted` = 0
	AND `wp05_hn`.`hierarchy_type` = 'STRUCTURE'
	LEFT JOIN `hierarchy_node` `wp04_hn` ON `wp04_hn`.`id` = `wp05_hn`.`parent_id`
	AND `wp04_hn`.`project_id` = `pn`.`project_id`
	AND `wp04_hn`.`deleted` = 0
	AND `wp04_hn`.`hierarchy_type` = 'STRUCTURE'
	LEFT JOIN `project_node` `wp04_pn` ON `wp04_hn`.`node_id` = `wp04_pn`.`id`
	AND `wp04_pn`.`project_id` = `pn`.`project_id`
	AND `wp04_pn`.`deleted` = 0
	AND `wp04_pn`.`entity_type` IN ( 'WP01', 'WP02', 'WP03', 'WP04', 'WP05' )
WHERE `wp05`.deleted = 0;
