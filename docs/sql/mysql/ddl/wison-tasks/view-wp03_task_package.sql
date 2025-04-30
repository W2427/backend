USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wp03_task_package` AS
SELECT
  `hn_on_structure`.`hierarchy_type` AS `hierarchy_type`,
  `hn_on_structure`.`id` AS `hierarchy_id`,
  `hn_on_structure`.`path` AS `path_on_structure`,
  `parent_hn_on_structure`.`id` AS `parent_hierarchy_id`,
  `pn`.`wp01_no` AS `module_parent_no`,
  `pn`.`wp01_id` AS `module_parent_id`,
  `pn`.`id` AS `project_node_id`,
  `pn`.`node_type` AS `node_type`,
  `pn`.`entity_type` AS `entity_type`,
  `pn`.`entity_sub_type` AS `subEntity_type`,	`wp03`.`id` AS `id`,
  `pn`.`wp01_id` AS `module_id`,
	`pn`.`wp01_no` AS `module_no`,
	`wp03`.`created_at` AS `created_at`,
	`wp03`.`last_modified_at` AS `last_modified_at`,
	`wp03`.`status` AS `status`,
	`wp03`.`created_by` AS `created_by`,
	`wp03`.`deleted` AS `deleted`,
	`wp03`.`deleted_at` AS `deleted_at`,
	`wp03`.`deleted_by` AS `deleted_by`,
	`wp03`.`last_modified_by` AS `last_modified_by`,
	`wp03`.`version` AS `version`,
	`wp03`.`cancelled` AS `cancelled`,
	`wp03`.`company_id` AS `company_id`,
	`wp03`.`display_name` AS `display_name`,
	`wp03`.`no` AS `no`,
	`wp03`.`org_id` AS `org_id`,
	`wp03`.`project_id` AS `project_id`,
	`wp03`.`remarks` AS `remarks`,
	`wp03`.`business_type` AS `business_type`,
	`wp03`.`business_type_id` AS `business_type_id`,
-- 	`wp03`.`description` AS `description`,
-- 	`wp03`.`dwg_no` AS `dwg_no`,
	`wp03`.`height` AS `height`,
	`wp03`.`height_text` AS `height_text`,
	`wp03`.`height_unit` AS `height_unit`,
	`wp03`.`is_new` AS `is_new`,
	`wp03`.`length` AS `length`,
	`wp03`.`length_text` AS `length_text`,
	`wp03`.`length_unit` AS `length_unit`,
-- 	`wp03`.`member_size` AS `member_size`,
	`wp03`.`revision` AS `revision`,
-- 	`wp03`.`sheet_no` AS `sheet_no`,
-- 	`wp03`.`sheet_total` AS `sheet_total`,
-- 	`wp03`.`short_code` AS `short_code`,
	`wp03`.`entity_sub_type` AS `entity_sub_type`,
	`wp03`.`weight` AS `weight`,
	`wp03`.`weight_text` AS `weight_text`,
	`wp03`.`weight_unit` AS `weight_unit`,
	`wp03`.`width` AS `width`,
	`wp03`.`width_text` AS `width_text`,
	`wp03`.`width_unit` AS `width_unit`,
	`pn`.`wp01_id` AS `wp01_id`,
	`pn`.`wp01_no` AS `wp01_no`,
	`pn`.`wp02_id` AS `wp02_id`,
	`pn`.`wp02_no` AS `wp02_no`,
	`pn`.`wp03_id` AS `wp03_id`,
	`pn`.`wp03_no` AS `wp03_no`,
	`pn`.`wp04_id` AS `wp04_id`,
	`pn`.`wp04_no` AS `wp04_no`,
	`wp03`.`paint_code` AS `paint_code`,
-- 	`wp03`.`remark` AS `remark`,
-- 	`wp03`.`paint_flag` AS `paint_flag`,
-- 	`wp03`.`painting_area` AS `painting_area`,
-- 	`wp03`.`painting_area_unit` AS `painting_area_unit`,
-- 	`wp03`.`painting_area_text` AS `painting_area_text`,
-- 	`wp03`.`pfp_area` AS `pfp_area`,
-- 	`wp03`.`pfp_area_unit` AS `pfp_area_unit`,
-- 	`wp03`.`pfp_area_text` AS `pfp_area_text`,
-- 	`wp03`.`total_area` AS `total_area`,
-- 	`wp03`.`total_area_text` AS `total_area_text`,
-- 	`wp03`.`total_area_unit` AS `total_area_unit`,
	null           AS `clean_package_no`,
	null           AS `pressure_test_package_no`,
	null           AS `sub_system_no`,
	null           AS `layer_package_no`,
	null           AS `remarks2`,
	null           AS `work_class`,
	CASE isnull(`tper`.`task_package_id`) WHEN 1 THEN 0 ELSE 1  END AS `assigned_task_package`
FROM
    `entity_wp03` `wp03`
    LEFT JOIN `project_node` `pn` ON
      `pn`.`entity_id` = `wp03`.`id`
      AND  `pn`.`project_id` = `wp03`.`project_id`
      AND  `pn`.`deleted` = `wp03`.`deleted`
      AND  `pn`.`entity_type` = 'WP03'

    LEFT JOIN `hierarchy_node` `hn_on_structure` ON
      `pn`.`project_id` = `hn_on_structure`.`project_id`
      AND  `pn`.`id` = `hn_on_structure`.`node_id`
      AND  `hn_on_structure`.`hierarchy_type` = 'STRUCTURE'
      AND  `hn_on_structure`.`deleted` = 0

    LEFT JOIN `hierarchy_node` `parent_hn_on_structure` ON
      `parent_hn_on_structure`.`project_id` = `hn_on_structure`.`project_id`
      AND  `hn_on_structure`.`parent_id` = `parent_hn_on_structure`.`id`
      AND  `parent_hn_on_structure`.`hierarchy_type` = 'STRUCTURE'
      AND  `parent_hn_on_structure`.`deleted` = 0

    LEFT JOIN `project_node` `parent_pn` ON
      `parent_hn_on_structure`.`project_id` = `parent_pn`.`project_id`
      AND  `parent_pn`.`id` = `parent_hn_on_structure`.`node_id`
      AND  `parent_pn`.`entity_type` = 'WP02'
      AND  `parent_pn`.`deleted` = 0


		LEFT JOIN `task_package_entity_relation` `tper` ON
      `tper`.`project_id` = `wp03`.`project_id`
      AND  `tper`.`org_id` = `wp03`.`org_id`
      AND  `wp03`.`id` = `tper`.`entity_id`
      AND  `wp03`.`status` = 'ACTIVE'
;
