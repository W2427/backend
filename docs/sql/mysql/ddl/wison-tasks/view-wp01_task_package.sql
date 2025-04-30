USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wp01_task_package` AS
 SELECT
	`hn_on_structure`.`hierarchy_type`   AS `hierarchy_type`,
	`hn_on_structure`.`id`    AS `hierarchy_id`,
	`hn_on_structure`.`path`  AS `path_on_structure`,
	`pn`.`node_type`          AS `node_type`,
	`pn`.`id`                 AS `project_node_id`,
	`pn`.`entity_type`        AS `entity_type`,
	`pn`.`entity_sub_type`    AS `subEntity_type`,
	`w1`.`id` AS `id`,
	`w1`.`created_at` AS `created_at`,
	`w1`.`last_modified_at` AS `last_modified_at`,
	`w1`.`status` AS `status`,
	`w1`.`created_by` AS `created_by`,
	`w1`.`deleted` AS `deleted`,
	`w1`.`deleted_at` AS `deleted_at`,
	`w1`.`deleted_by` AS `deleted_by`,
	`w1`.`last_modified_by` AS `last_modified_by`,
	`w1`.`version` AS `version`,
	`w1`.`cancelled` AS `cancelled`,
	`w1`.`company_id` AS `company_id`,
	`w1`.`display_name` AS `display_name`,
	`w1`.`no` AS `no`,
	`w1`.`org_id` AS `org_id`,
	`w1`.`project_id` AS `project_id`,
	`w1`.`remarks` AS `remarks`,
	`w1`.`business_type` AS `business_type`,
	`w1`.`business_type_id` AS `business_type_id`,
-- 	`w1`.`description` AS `description`,
-- 	`w1`.`dwg_no` AS `dwg_no`,
	`w1`.`height` AS `height`,
	`w1`.`height_text` AS `height_text`,
	`w1`.`height_unit` AS `height_unit`,
	`w1`.`is_new` AS `is_new`,
	`w1`.`length` AS `length`,
	`w1`.`length_text` AS `length_text`,
	`w1`.`length_unit` AS `length_unit`,
-- 	`w1`.`total_area` AS `total_area`,
-- 	`w1`.`total_area_text` AS `total_area_text`,
-- 	`w1`.`total_area_unit` AS `total_area_unit`,
-- 	`w1`.`painting_area` AS `painting_area`,
-- 	`w1`.`painting_area_unit` AS `painting_area_unit`,
-- 	`w1`.`painting_area_text` AS `painting_area_text`,
-- 	`w1`.`pfp_area` AS `pfp_area`,
-- 	`w1`.`pfp_area_text` AS `pfp_area_text`,
-- 	`w1`.`pfp_area_unit` AS `pfp_area_unit`,
-- 	`w1`.`paint_flag` AS `paint_flag`,
-- 	`w1`.`paint_code` AS `paint_code`,
	`w1`.`qr_code` AS `qr_code`,
	`w1`.`revision` AS `revision`,
-- 	`w1`.`sheet_no` AS `sheet_no`,
-- 	`w1`.`sheet_total` AS `sheet_total`,
-- 	`w1`.`short_code` AS `short_code`,
	`w1`.`entity_sub_type` AS `entity_sub_type`,
	`w1`.`weight` AS `weight`,
	`w1`.`weight_text` AS `weight_text`,
	`w1`.`weight_unit` AS `weight_unit`,
	`w1`.`width` AS `width`,
	`w1`.`width_text` AS `width_text`,
	`w1`.`width_unit` AS `width_unit`,
	`pn`.`wp01_id` AS `wp01_id`,
	`pn`.`wp01_no` AS `wp01_no`,
	`pn`.`wp02_id` AS `wp02_id`,
	`pn`.`wp02_no` AS `wp02_no`,
	`pn`.`wp03_id` AS `wp03_id`,
	`pn`.`wp03_no` AS `wp03_no`,
	`pn`.`wp04_id` AS `wp04_id`,
	`pn`.`wp04_no` AS `wp04_no`,
	`pn`.`wp01_id` AS `module_id`,
	`pn`.`wp01_no` AS `module_no`,
	`pn`.`no` AS `module_parent_no`,
  `pn`.`id` AS `module_parent_id`,
	null           AS `clean_package_no`,
	null           AS `pressure_test_package_no`,
	null           AS `sub_system_no`,
	null           AS `layer_package_no`,
	null           AS `remarks2`,
	null           AS `work_class`,
	`hn_on_structure`.`parent_id`           AS `parent_hierarchy_id`,
	CASE ISNULL(`tper`.`task_package_id`) WHEN 1 then 0 ELSE 1 END AS assigned_task_package
FROM
	`entity_wp01` `w1`
	  LEFT JOIN `project_node` `pn` ON
		`w1`.`project_id` = `pn`.`project_id`
		AND  `w1`.`id` = `pn`.`entity_id`
		AND  `pn`.`deleted` = 0
		AND  `pn`.`entity_type` = 'WP01'

	LEFT JOIN `hierarchy_node` `hn_on_structure` ON
		`hn_on_structure`.`project_id` = `pn`.`project_id`
		AND  `hn_on_structure`.`node_id` = `pn`.`id`
		AND  `hn_on_structure`.`deleted` = 0
		AND  `hn_on_structure`.`hierarchy_type` = 'STRUCTURE'

		LEFT JOIN `task_package_entity_relation` `tper` ON
			`tper`.`project_id` = `w1`.`project_id`
			AND `tper`.`org_id` = `w1`.`org_id`
			AND `w1`.`id` = `tper`.`entity_id`
			AND `w1`.`status` = 'ACTIVE'
;

