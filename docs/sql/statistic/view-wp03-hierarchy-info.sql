-- saint_whale_tasks.wp03_hierarchy_info source

CREATE OR REPLACE
ALGORITHM = UNDEFINED VIEW `saint_whale_tasks`.`wp03_hierarchy_info` AS
select
  `hn_on_structure`.`hierarchy_type` AS `hierarchy_type`,
  `hn_on_structure`.`id` AS `hierarchy_id`,
  `hn_on_structure`.`path` AS `path_on_structure`,
  `parent_hn_on_structure`.`id` AS `parent_hierarchy_id`,
  `parent_pn`.`no` AS `module_parent_no`,
  `parent_pn`.`id` AS `module_parent_id`,
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
  `wp3`.`id` AS `id`,
  `wp3`.`created_at` AS `created_at`,
  `wp3`.`last_modified_at` AS `last_modified_at`,
  `wp3`.`status` AS `status`,
  `wp3`.`created_by` AS `created_by`,
  `wp3`.`deleted` AS `deleted`,
  `wp3`.`deleted_at` AS `deleted_at`,
  `wp3`.`deleted_by` AS `deleted_by`,
  `wp3`.`last_modified_by` AS `last_modified_by`,
  `wp3`.`version` AS `version`,
  `wp3`.`cancelled` AS `cancelled`,
  `wp3`.`company_id` AS `company_id`,
  `wp3`.`display_name` AS `display_name`,
  `wp3`.`discipline` AS `discipline`,
  `wp3`.`no` AS `no`,
  `wp3`.`org_id` AS `org_id`,
  `wp3`.`project_id` AS `project_id`,
  `wp3`.`remarks` AS `remarks`,
  `wp3`.`remarks2` AS `remarks2`,
  `wp3`.`business_type` AS `business_type`,
  `wp3`.`business_type_id` AS `business_type_id`,
  `wp3`.`height` AS `height`,
  `wp3`.`height_text` AS `height_text`,
  `wp3`.`height_unit` AS `height_unit`,
  `wp3`.`is_new` AS `is_new`,
  `wp3`.`length` AS `length`,
  `wp3`.`length_text` AS `length_text`,
  `wp3`.`length_unit` AS `length_unit`,
  `wp3`.`paint_code` AS `paint_code`,
  `wp3`.`qr_code` AS `qr_code`,
  `wp3`.`revision` AS `revision`,
  `wp3`.`weight` AS `weight`,
  `wp3`.`weight_text` AS `weight_text`,
  `wp3`.`weight_unit` AS `weight_unit`,
  `wp3`.`width` AS `width`,
  `wp3`.`width_text` AS `width_text`,
  `wp3`.`width_unit` AS `width_unit`,
  `wp3`.`work_class` AS `work_class`,
  `wp3`.`work_package_no` AS `work_package_no`,
  `wp3`.`tag_description` AS `tag_description`
from
  ((((`saint_whale_tasks`.`entity_wp03` `wp3`
    left join `saint_whale_tasks`.`project_node` `pn` on
    (((`pn`.`entity_id` = `wp3`.`id`)
      and (`pn`.`project_id` = `wp3`.`project_id`)
      and (`pn`.`deleted` = `wp3`.`deleted`)
      and (`pn`.`entity_type` = 'WP03'))))
    left join `saint_whale_tasks`.`hierarchy_node` `hn_on_structure` on
    (((`pn`.`project_id` = `hn_on_structure`.`project_id`)
      and (`pn`.`id` = `hn_on_structure`.`node_id`)
      and (`hn_on_structure`.`hierarchy_type` = 'STRUCTURE')
      and (`hn_on_structure`.`deleted` = 0))))
    left join `saint_whale_tasks`.`hierarchy_node` `parent_hn_on_structure` on
    (((`parent_hn_on_structure`.`project_id` = `hn_on_structure`.`project_id`)
      and (`hn_on_structure`.`parent_id` = `parent_hn_on_structure`.`id`)
      and (`parent_hn_on_structure`.`hierarchy_type` = 'STRUCTURE')
      and (`parent_hn_on_structure`.`deleted` = 0))))
    left join `saint_whale_tasks`.`project_node` `parent_pn` on
    (((`parent_hn_on_structure`.`project_id` = `parent_pn`.`project_id`)
      and (`parent_pn`.`id` = `parent_hn_on_structure`.`node_id`)
      and (`parent_pn`.`entity_type` = 'WP02')
      and (`parent_pn`.`deleted` = 0))))
where
  (`wp3`.`deleted` = 0);
