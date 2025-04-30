-- saint_whale_tasks.wp05_hierarchy_info source

CREATE OR REPLACE
ALGORITHM = UNDEFINED VIEW `saint_whale_tasks`.`wp05_hierarchy_info` AS
select
  `wp05_hn`.`hierarchy_type` AS `hierarchy_type`,
  `wp05_hn`.`id` AS `hierarchy_id`,
  `wp05_hn`.`path` AS `path_on_structure`,
  `wp04_hn`.`id` AS `parent_hierarchy_id`,
  `wp04_pn`.`no` AS `module_parent_no`,
  `wp04_pn`.`id` AS `module_parent_id`,
  `pn`.`id` AS `project_node_id`,
  '' AS `node_type`,
  `pn`.`entity_type` AS `entity_type`,
  `pn`.`entity_sub_type` AS `entity_sub_type`,
  `pn`.`wp01_id` AS `wp01_id`,
  `pn`.`wp01_no` AS `wp01_no`,
  `pn`.`wp02_id` AS `wp02_id`,
  `pn`.`wp02_no` AS `wp02_no`,
  `pn`.`wp03_id` AS `wp03_id`,
  `pn`.`wp03_no` AS `wp03_no`,
  `pn`.`wp04_id` AS `wp04_id`,
  `pn`.`wp04_no` AS `wp04_no`,
  `wp05`.`id` AS `id`,
  `wp05`.`created_at` AS `created_at`,
  `wp05`.`last_modified_at` AS `last_modified_at`,
  `wp05`.`status` AS `status`,
  `wp05`.`created_by` AS `created_by`,
  `wp05`.`deleted` AS `deleted`,
  `wp05`.`deleted_at` AS `deleted_at`,
  `wp05`.`deleted_by` AS `deleted_by`,
  `wp05`.`last_modified_by` AS `last_modified_by`,
  `wp05`.`version` AS `version`,
  `wp05`.`cancelled` AS `cancelled`,
  `wp05`.`company_id` AS `company_id`,
  `wp05`.`display_name` AS `display_name`,
  `wp05`.`discipline` AS `discipline`,
  `wp05`.`no` AS `no`,
  `wp05`.`org_id` AS `org_id`,
  `wp05`.`project_id` AS `project_id`,
  `wp05`.`remarks` AS `remarks`,
  `wp05`.`remarks2` AS `remarks2`,
  `wp05`.`business_type` AS `business_type`,
  `wp05`.`business_type_id` AS `business_type_id`,
  `wp05`.`cost_code` AS `cost_code`,
  `wp05`.`height` AS `height`,
  `wp05`.`height_text` AS `height_text`,
  `wp05`.`height_unit` AS `height_unit`,
  `wp05`.`is_new` AS `is_new`,
  `wp05`.`length` AS `length`,
  `wp05`.`length_text` AS `length_text`,
  `wp05`.`length_unit` AS `length_unit`,
  `wp05`.`material` AS `material`,
  `wp05`.`member_size` AS `member_size`,
  `wp05`.`nested_flag` AS `nested_flag`,
  `wp05`.`paint_code` AS `paint_code`,
  `wp05`.`qr_code` AS `qr_code`,
  `wp05`.`revision` AS `revision`,
  `wp05`.`weight` AS `weight`,
  `wp05`.`weight_text` AS `weight_text`,
  `wp05`.`weight_unit` AS `weight_unit`,
  `wp05`.`width` AS `width`,
  `wp05`.`width_text` AS `width_text`,
  `wp05`.`width_unit` AS `width_unit`,
  `wp05`.`work_class` AS `work_class`,
  `wp05`.`work_package_no` AS `work_package_no`,
  `wp05`.`material_cutting` AS `material_cutting`
from
  ((((`saint_whale_tasks`.`entity_wp05` `wp05`
    left join `saint_whale_tasks`.`project_node` `pn` on
    (((`wp05`.`id` = `pn`.`entity_id`)
      and (`pn`.`project_id` = `wp05`.`project_id`)
      and (`pn`.`entity_type` = 'WP05')
      and (`pn`.`deleted` = 0))))
    left join `saint_whale_tasks`.`hierarchy_node` `wp05_hn` on
    (((`pn`.`project_id` = `wp05_hn`.`project_id`)
      and (`wp05_hn`.`node_id` = `pn`.`id`)
      and (`wp05_hn`.`deleted` = 0)
      and (`wp05_hn`.`hierarchy_type` = 'STRUCTURE'))))
    left join `saint_whale_tasks`.`hierarchy_node` `wp04_hn` on
    (((`wp04_hn`.`id` = `wp05_hn`.`parent_id`)
      and (`wp04_hn`.`project_id` = `pn`.`project_id`)
      and (`wp04_hn`.`deleted` = 0)
      and (`wp04_hn`.`hierarchy_type` = 'STRUCTURE'))))
    left join `saint_whale_tasks`.`project_node` `wp04_pn` on
    (((`wp04_hn`.`node_id` = `wp04_pn`.`id`)
      and (`wp04_pn`.`project_id` = `pn`.`project_id`)
      and (`wp04_pn`.`deleted` = 0)
      and (`wp04_pn`.`entity_type` in ('WP01', 'WP02', 'WP03', 'WP04', 'WP05')))))
where
  (`wp05`.`deleted` = 0);
