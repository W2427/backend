-- saint_whale_tasks.wp04_hierarchy_info source

CREATE OR REPLACE
ALGORITHM = UNDEFINED VIEW `saint_whale_tasks`.`wp04_hierarchy_info` AS
select
  `wp04_hn`.`hierarchy_type` AS `hierarchy_type`,
  `wp04_hn`.`id` AS `hierarchy_id`,
  `wp04_hn`.`path` AS `path_on_structure`,
  `wp03_hn`.`id` AS `parent_hierarchy_id`,
  `wp03_pn`.`no` AS `module_parent_no`,
  `wp03_pn`.`id` AS `module_parent_id`,
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
  `wp04`.`discipline` AS `discipline`,
  `wp04`.`no` AS `no`,
  `wp04`.`org_id` AS `org_id`,
  `wp04`.`project_id` AS `project_id`,
  `wp04`.`remarks` AS `remarks`,
  `wp04`.`remarks2` AS `remarks2`,
  `wp04`.`business_type` AS `business_type`,
  `wp04`.`business_type_id` AS `business_type_id`,
  `wp04`.`height` AS `height`,
  `wp04`.`height_text` AS `height_text`,
  `wp04`.`height_unit` AS `height_unit`,
  `wp04`.`is_new` AS `is_new`,
  `wp04`.`length` AS `length`,
  `wp04`.`length_text` AS `length_text`,
  `wp04`.`length_unit` AS `length_unit`,
  `wp04`.`member_size` AS `member_size`,
  `wp04`.`paint_code` AS `paint_code`,
  `wp04`.`qr_code` AS `qr_code`,
  `wp04`.`revision` AS `revision`,
  `wp04`.`weight` AS `weight`,
  `wp04`.`weight_text` AS `weight_text`,
  `wp04`.`weight_unit` AS `weight_unit`,
  `wp04`.`width` AS `width`,
  `wp04`.`width_text` AS `width_text`,
  `wp04`.`width_unit` AS `width_unit`,
  `wp04`.`work_class` AS `work_class`,
  `wp04`.`work_package_no` AS `work_package_no`,
  `wp04`.`tag_description` AS `tag_description`
from
  ((((`saint_whale_tasks`.`entity_wp04` `wp04`
    left join `saint_whale_tasks`.`project_node` `pn` on
    (((`wp04`.`id` = `pn`.`entity_id`)
      and (`pn`.`project_id` = `wp04`.`project_id`)
      and (`pn`.`entity_type` = 'WP04')
      and (`pn`.`deleted` = 0))))
    left join `saint_whale_tasks`.`hierarchy_node` `wp04_hn` on
    (((`pn`.`project_id` = `wp04_hn`.`project_id`)
      and (`pn`.`id` = `wp04_hn`.`node_id`)
      and (`wp04_hn`.`deleted` = 0)
      and (`wp04_hn`.`hierarchy_type` = 'STRUCTURE'))))
    left join `saint_whale_tasks`.`hierarchy_node` `wp03_hn` on
    (((`wp03_hn`.`id` = `wp04_hn`.`parent_id`)
      and (`wp03_hn`.`project_id` = `wp04_hn`.`project_id`)
      and (`wp03_hn`.`deleted` = 0)
      and (`wp03_hn`.`hierarchy_type` = 'STRUCTURE'))))
    left join `saint_whale_tasks`.`project_node` `wp03_pn` on
    (((`wp03_hn`.`node_id` = `wp03_pn`.`id`)
      and (`wp03_hn`.`project_id` = `pn`.`project_id`)
      and (`wp03_pn`.`deleted` = 0)
      and (`wp03_pn`.`entity_type` in ('WP01', 'WP02', 'WP03', 'WP04', 'WP05')))))
where
  (`wp04`.`deleted` = 0);
