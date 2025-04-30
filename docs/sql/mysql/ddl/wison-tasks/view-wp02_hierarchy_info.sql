USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wp02_hierarchy_info` AS
SELECT
	`hn_on_structure`.`hierarchy_type` AS `hierarchy_type`,
	`hn_on_structure`.`id` AS `hierarchy_id`,
	`hn_on_structure`.`path` AS `path_on_structure`,
	`parent_hn_on_structure`.`id` AS `parent_hierarchy_id`,
	`parent_pn`.`no` AS `module_parent_no`,
	`parent_pn`.`id` AS `module_parent_id`,
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
	`wp02`.*
FROM
	`entity_wp02` `wp02`
	LEFT JOIN `project_node` `pn` ON `wp02`.`id` = `pn`.`entity_id`
	AND `pn`.`project_id` = `wp02`.`project_id`
	AND `pn`.`entity_type` = 'WP02'
	AND `pn`.`deleted` = 0
	LEFT JOIN `hierarchy_node` `hn_on_structure` ON `pn`.`project_id` = `hn_on_structure`.`project_id`
	AND `pn`.`id` = `hn_on_structure`.`node_id`
	AND `hn_on_structure`.`deleted` = 0
	AND `hn_on_structure`.`hierarchy_type` = 'STRUCTURE'
	LEFT JOIN `hierarchy_node` `parent_hn_on_structure` ON `parent_hn_on_structure`.`id` = `hn_on_structure`.`parent_id`
	AND `parent_hn_on_structure`.`project_id` = `hn_on_structure`.`project_id`
	AND `parent_hn_on_structure`.`deleted` = 0
	AND `parent_hn_on_structure`.`hierarchy_type` = 'STRUCTURE'
	LEFT JOIN `project_node` `parent_pn` ON `parent_hn_on_structure`.`node_id` = `parent_pn`.`id`
	AND `parent_hn_on_structure`.`project_id` = `parent_pn`.`project_id`
	AND `parent_pn`.`deleted` = 0
	AND `parent_pn`.`entity_type` = 'WP01'
WHERE `wp02`.deleted = 0;
