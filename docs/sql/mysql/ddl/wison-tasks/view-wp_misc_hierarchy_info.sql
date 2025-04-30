USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wp_misc_hierarchy_info` AS
SELECT
	`hn_on_structure`.`hierarchy_type` AS `hierarchy_type`,
	`hn_on_structure`.`id` AS `hierarchy_id`,
	`hn_on_structure`.`path` AS `path_on_structure`,
	`parent_hn_on_structure`.`id` AS `parent_hierarchy_id`,
	`pn`.`entity_type` AS `entity_type`,
-- 	`entity`.`wp01_id` AS `module_parent_id`,
	`pn`.`node_type` AS `node_type`,
	`pn`.`id` AS `project_node_id`,
-- 	`entity`.`wp01_no` AS `module_parent_no`,
	`entity`.`id` AS `id`,
	`entity`.`created_at` AS `created_at`,
	`entity`.`last_modified_at` AS `last_modified_at`,
	`entity`.`status` AS `status`,
	`entity`.`created_by` AS `created_by`,
	`entity`.`deleted` AS `deleted`,
	`entity`.`deleted_at` AS `deleted_at`,
	`entity`.`deleted_by` AS `deleted_by`,
	`entity`.`last_modified_by` AS `last_modified_by`,
	`entity`.`version` AS `version`,
	`entity`.`cancelled` AS `cancelled`,
	`entity`.`company_id` AS `company_id`,
	`entity`.`display_name` AS `display_name`,
	`entity`.`no` AS `no`,
	`entity`.`org_id` AS `org_id`,
	`entity`.`project_id` AS `project_id`,
	`entity`.`remarks` AS `remarks`,
	`entity`.`area` AS `area`,
	`entity`.`area_text` AS `area_text`,
	`entity`.`area_unit` AS `area_unit`,
	`entity`.`business_type` AS `business_type`,
	`entity`.`business_type_id` AS `business_type_id`,
	`entity`.`description` AS `description`,
	`entity`.`dwg_no` AS `dwg_no`,
	`entity`.`is_new` AS `is_new`,
	`entity`.`material` AS `material`,
	`entity`.`member_size` AS `member_size`,
	`entity`.`qty` AS `qty`,
	`entity`.`revision` AS `revision`,
	`entity`.`sheet_no` AS `sheet_no`,
	`entity`.`sheet_total` AS `sheet_total`,
	`entity`.`short_code` AS `short_code`,
	`entity`.`entity_sub_type` AS `entity_sub_type`,
	`entity`.`weight` AS `weight`,
	`entity`.`weight_text` AS `weight_text`,
	`entity`.`weight_unit` AS `weight_unit`,
-- 	`pn`.`wp01_id` AS `wp01_id`,
	`pn`.`wp01_no` AS `wp01_no`,
	`pn`.`wp02_id` AS `wp02_id`,
	`pn`.`wp02_no` AS `wp02_no`,
	`pn`.`wp03_id` AS `wp03_id`,
	`pn`.`wp03_no` AS `wp03_no`,
	`pn`.`wp04_id` AS `wp04_id`,
	`pn`.`wp04_no` AS `wp04_no`,
	`entity`.`bolt_diameter` AS `bolt_diameter`,
	`entity`.`bolt_diameter_text` AS `bolt_diameter_text`,
	`entity`.`bolt_diameter_unit` AS `bolt_diameter_unit`,
	`entity`.`bolt_length` AS `bolt_length`,
	`entity`.`bolt_length_text` AS `bolt_length_text`,
	`entity`.`bolt_length_unit` AS `bolt_length_unit`,
	`entity`.`hierachy_parent` AS `hierachy_parent`,
	`entity`.`total_weight` AS `total_weight`,
	`entity`.`total_weight_text` AS `total_weight_text`,
	`entity`.`total_weight_unit` AS `total_weight_unit`,
	`entity`.`component_no` AS `component_no`,
	`entity`.`module_no` AS `module_no`,
	`entity`.`panel_no` AS `panel_no`,
	`entity`.`section_no` AS `section_no`
FROM
	`entity_wp_misc` `entity`
	    LEFT JOIN `project_node` `pn` ON
      `entity`.`id` = `pn`.`entity_id`
      AND  `entity`.`project_id` = `pn`.`project_id`
      AND  `pn`.`deleted` = 0
      AND  `pn`.`entity_type` = 'WP_MISC'

    LEFT JOIN `hierarchy_node` `hn_on_structure` ON
      `pn`.`project_id` = `hn_on_structure`.`project_id`
      AND  `pn`.`id` = `hn_on_structure`.`node_id`
      AND  `hn_on_structure`.`deleted` = 0
      AND  `hn_on_structure`.`hierarchy_type` = 'STRUCTURE'

    LEFT JOIN `hierarchy_node` `parent_hn_on_structure` ON
      `hn_on_structure`.`parent_id` = `parent_hn_on_structure`.`id`
      AND  `hn_on_structure`.`project_id` = `parent_hn_on_structure`.`project_id`
      AND  `parent_hn_on_structure`.`deleted` = `pn`.`deleted`
      AND  `parent_hn_on_structure`.`hierarchy_type` = 'STRUCTURE'

    LEFT JOIN `project_node` `parent_pn` ON
      `parent_hn_on_structure`.`node_id` = `parent_pn`.`id`
      AND  `parent_pn`.`project_id` = `pn`.`project_id`
      AND  `parent_pn`.`deleted` = 0
      AND  `parent_pn`.`entity_type` IN  ('WP01', 'WP02', 'WP03', 'WP04', 'WP05')



;

