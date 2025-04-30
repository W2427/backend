USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wp02_task_package` AS
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
  `pn`.`wp01_id` AS `module_id`,
	`pn`.`wp01_no` AS `module_no`,
	`wp02`.`id` AS `id`,
	`wp02`.`created_at` AS `created_at`,
	`wp02`.`last_modified_at` AS `last_modified_at`,
	`wp02`.`status` AS `status`,
	`wp02`.`created_by` AS `created_by`,
	`wp02`.`deleted` AS `deleted`,
	`wp02`.`deleted_at` AS `deleted_at`,
	`wp02`.`deleted_by` AS `deleted_by`,
	`wp02`.`last_modified_by` AS `last_modified_by`,
	`wp02`.`version` AS `version`,
	`wp02`.`cancelled` AS `cancelled`,
	`wp02`.`company_id` AS `company_id`,
	`wp02`.`display_name` AS `display_name`,
	`wp02`.`no` AS `no`,
	`wp02`.`org_id` AS `org_id`,
	`wp02`.`project_id` AS `project_id`,
	`wp02`.`remarks` AS `remarks`,
	`wp02`.`business_type` AS `business_type`,
	`wp02`.`business_type_id` AS `business_type_id`,
-- 	`wp02`.`description` AS `description`,
-- 	`wp02`.`dwg_no` AS `dwg_no`,
	`wp02`.`height` AS `height`,
	`wp02`.`height_text` AS `height_text`,
	`wp02`.`height_unit` AS `height_unit`,
	`wp02`.`is_new` AS `is_new`,
	`wp02`.`length` AS `length`,
	`wp02`.`length_text` AS `length_text`,
	`wp02`.`length_unit` AS `length_unit`,
	`wp02`.`revision` AS `revision`,
-- 	`wp02`.`sheet_no` AS `sheet_no`,
-- 	`wp02`.`sheet_total` AS `sheet_total`,
-- 	`wp02`.`short_code` AS `short_code`,
	`wp02`.`entity_sub_type` AS `entity_sub_type`,
	`wp02`.`weight` AS `weight`,
	`wp02`.`weight_text` AS `weight_text`,
	`wp02`.`weight_unit` AS `weight_unit`,
	`wp02`.`width` AS `width`,
	`wp02`.`width_text` AS `width_text`,
	`wp02`.`width_unit` AS `width_unit`,
	`pn`.`wp01_id` AS `wp01_id`,
	`pn`.`wp01_no` AS `wp01_no`,
	`pn`.`wp02_id` AS `wp02_id`,
	`pn`.`wp02_no` AS `wp02_no`,
	`pn`.`wp03_id` AS `wp03_id`,
	`pn`.`wp03_no` AS `wp03_no`,
	`pn`.`wp04_id` AS `wp04_id`,
	`pn`.`wp04_no` AS `wp04_no`,
-- 	`wp02`.`paint_code` AS `paint_code`,
-- 	`wp02`.`painting_area_unit` AS `painting_area_unit`,
-- 	`wp02`.`paint_flag` AS `paint_flag`,
-- 	`wp02`.`painting_area` AS `painting_area`,
-- 	`wp02`.`painting_area_text` AS `painting_area_text`,
-- 	`wp02`.`pfp_area` AS `pfp_area`,
-- 	`wp02`.`pfp_area_text` AS `pfp_area_text`,
-- 	`wp02`.`pfp_area_unit` AS `pfp_area_unit`,
-- 	`wp02`.`total_area` AS `total_area`,
-- 	`wp02`.`total_area_text` AS `total_area_text`,
-- 	`wp02`.`total_area_unit` AS `total_area_unit`,
	null           AS `clean_package_no`,
	null           AS `pressure_test_package_no`,
	null           AS `sub_system_no`,
	null           AS `layer_package_no`,
	null           AS `remarks2`,
	null           AS `work_class`,
	CASE isnull(`tper`.`task_package_id`) WHEN 1 THEN 0 ELSE 1  END AS `assigned_task_package`
	FROM
    `entity_wp02` `wp02`
    LEFT JOIN `project_node` `pn` ON
      `wp02`.`id` = `pn`.`entity_id`
      AND  `pn`.`project_id` = `wp02`.`project_id`
      AND  `pn`.`entity_type` = 'WP02'
      AND  `pn`.`deleted` = 0

    LEFT JOIN `hierarchy_node` `hn_on_structure` ON
      `pn`.`project_id` = `hn_on_structure`.`project_id`
      AND  `pn`.`id` = `hn_on_structure`.`node_id`
      AND  `hn_on_structure`.`deleted` = 0
      AND  `hn_on_structure`.`hierarchy_type` = 'STRUCTURE'

    LEFT JOIN `hierarchy_node` `parent_hn_on_structure` ON
      `parent_hn_on_structure`.`id` = `hn_on_structure`.`parent_id`
      AND  `parent_hn_on_structure`.`project_id` = `hn_on_structure`.`project_id`
      AND  `parent_hn_on_structure`.`deleted` = 0
      AND  `parent_hn_on_structure`.`hierarchy_type` = 'STRUCTURE'

    LEFT JOIN `project_node` `parent_pn` ON
      `parent_hn_on_structure`.`node_id` = `parent_pn`.`id`
      AND  `parent_hn_on_structure`.`project_id` = `parent_pn`.`project_id`
      AND  `parent_pn`.`deleted` = 0
      AND  `parent_pn`.`entity_type` = 'WP01'

		LEFT JOIN `task_package_entity_relation` `tper` ON
      `tper`.`project_id` = `wp02`.`project_id`
      AND  `tper`.`org_id` = `wp02`.`org_id`
      AND  `wp02`.`id` = `tper`.`entity_id`
      AND  `wp02`.`status` = 'ACTIVE'
;

