USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wp04_task_package` AS
SELECT
	`wp04_hn`.`hierarchy_type` AS `hierarchy_type`,
	`wp04_hn`.`id` AS `hierarchy_id`,
	`wp04_hn`.`path` AS `path_on_structure`,
	`wp03_hn`.`id` AS `parent_hierarchy_id`,
	`pn`.`wp01_id` AS `module_parent_id`,
	`pn`.`wp01_no` AS `module_parent_no`,
	`pn`.`wp01_id` AS `module_id`,
	`pn`.`wp01_no` AS `module_no`,
	`pn`.`id` AS `project_node_id`,
	`pn`.`node_type` AS `node_type`,
	`pn`.`entity_type` AS `entity_type`,
	`pn`.`entity_sub_type` AS `subEntity_type`,
	`wp04`.`id` AS `id`,
	`wp04`.`created_at` AS `created_at`,
	`wp04`.`last_modified_at` AS `last_modified_at`,
	`wp04`.`status` AS `status`,
	`wp04`.`created_by` AS `created_by`,
	`wp04`.`deleted` AS `deleted`,
	`wp04`.`deleted_at` AS `deleted_at`,
	`wp04`.`deleted_by` AS `deleted_by`,
	`wp04`.`last_modified_by` AS `last_modified_by`,
	`wp04`.`version` AS `version`,
	`wp04`.`cancelled` AS `cancelled`,
	`wp04`.`company_id` AS `company_id`,
	`wp04`.`display_name` AS `display_name`,
	`wp04`.`no` AS `no`,
	`wp04`.`org_id` AS `org_id`,
	`wp04`.`project_id` AS `project_id`,
	`wp04`.`remarks` AS `remarks`,
	`wp04`.`business_type` AS `business_type`,
	`wp04`.`business_type_id` AS `business_type_id`,
-- 	`wp04`.`description` AS `description`,
-- 	`wp04`.`dwg_no` AS `dwg_no`,
	`wp04`.`height` AS `height`,
	`wp04`.`height_text` AS `height_text`,
	`wp04`.`height_unit` AS `height_unit`,
	`wp04`.`is_new` AS `is_new`,
	`wp04`.`length` AS `length`,
	`wp04`.`length_text` AS `length_text`,
	`wp04`.`length_unit` AS `length_unit`,
-- 	`wp04`.`material` AS `material`,
	`wp04`.`member_size` AS `member_size`,
-- 	`wp04`.`nde` AS `nde`,
-- 	`wp04`.`nde_ratio` AS `nde_ratio`,
-- 	`wp04`.`qty` AS `qty`,
	`wp04`.`revision` AS `revision`,
-- 	`wp04`.`sheet_no` AS `sheet_no`,
-- 	`wp04`.`sheet_total` AS `sheet_total`,
-- 	`wp04`.`short_code` AS `short_code`,
-- 	`wp04`.`stick_built` AS `stick_built`,
	`wp04`.`entity_sub_type` AS `entity_sub_type`,
-- 	`wp04`.`unit_area` AS `unit_area`,
-- 	`wp04`.`unit_area_text` AS `unit_area_text`,
-- 	`wp04`.`unit_area_unit` AS `unit_area_unit`,
-- 	`wp04`.`unit_weight` AS `unit_weight`,
-- 	`wp04`.`unit_weight_text` AS `unit_weight_text`,
-- 	`wp04`.`unit_weight_unit` AS `unit_weight_unit`,
	`wp04`.`width` AS `width`,
	`wp04`.`width_text` AS `width_text`,
	`wp04`.`width_unit` AS `width_unit`,
	`pn`.`wp01_id` AS `wp01_id`,
	`pn`.`wp01_no` AS `wp01_no`,
	`pn`.`wp02_id` AS `wp02_id`,
	`pn`.`wp02_no` AS `wp02_no`,
	`pn`.`wp03_id` AS `wp03_id`,
	`pn`.`wp03_no` AS `wp03_no`,
	`pn`.`wp04_id` AS `wp04_id`,
	`pn`.`wp04_no` AS `wp04_no`,
-- 	`wp04`.`area` AS `area`,
-- 	`wp04`.`paint_code` AS `paint_code`,
-- 	`wp04`.`paint_flag` AS `paint_flag`,
-- 	`wp04`.`painting_area` AS `painting_area`,
-- 	`wp04`.`painting_area_unit` AS `painting_area_unit`,
-- 	`wp04`.`painting_area_text` AS `painting_area_text`,
-- 	`wp04`.`pfp_area` AS `pfp_area`,
-- 	`wp04`.`pfp_area_text` AS `pfp_area_text`,
-- 	`wp04`.`pfp_area_unit` AS `pfp_area_unit`,
-- 	`wp04`.`total_area` AS `total_area`,
-- 	`wp04`.`total_area_text` AS `total_area_text`,
-- 	`wp04`.`total_area_unit` AS `total_area_unit`,
	null           AS `remarks2`,
	null           AS `weight`,
	null           AS `weight_text`,
	null           AS `clean_package_no`,
	null           AS `pressure_test_package_no`,
	null           AS `sub_system_no`,
	null           AS `layer_package_no`,
	null           AS `weight_unit`,
	null           AS `work_class`,
		CASE isnull(`tper`.`task_package_id`) WHEN 1 THEN 0 ELSE 1  END AS `assigned_task_package`
FROM
	`entity_wp04` `wp04`
	LEFT JOIN `project_node` `pn` ON
		`wp04`.`id` = `pn`.`entity_id`
		AND  `pn`.`project_id` = `wp04`.`project_id`
		AND  `pn`.`entity_type` = 'WP04'
		AND  `pn`.`deleted` = 0

	LEFT JOIN `hierarchy_node` `wp04_hn` ON
		`pn`.`project_id` = `wp04_hn`.`project_id`
		AND  `pn`.`id` = `wp04_hn`.`node_id`
		AND  `wp04_hn`.`deleted` = 0
		AND  `wp04_hn`.`hierarchy_type` = 'STRUCTURE'

	LEFT JOIN `hierarchy_node` `wp03_hn` ON
		`wp03_hn`.`id` = `wp04_hn`.`parent_id`
		AND  `wp03_hn`.`project_id` = `wp04_hn`.`project_id`
		AND  `wp03_hn`.`deleted` = 0
		AND  `wp03_hn`.`hierarchy_type` = 'STRUCTURE'

	LEFT JOIN `project_node` `wp03_pn` ON
		`wp03_hn`.`node_id` = `wp03_pn`.`id`
		AND  `wp03_hn`.`project_id` = `pn`.`project_id`
		AND  `wp03_pn`.`deleted` = 0
		AND  `wp03_pn`.`entity_type` = 'WP03'

		LEFT JOIN `task_package_entity_relation` `tper` ON
      `tper`.`project_id` = `wp04`.`project_id`
      AND  `tper`.`org_id` = `wp04`.`org_id`
      AND  `wp04`.`id` = `tper`.`entity_id`
      AND  `wp04`.`status` = 'ACTIVE'
;
