-- saint_whale_tasks.wp02_hierarchy_info source

CREATE OR REPLACE
ALGORITHM = UNDEFINED VIEW `saint_whale_tasks`.`wp02_hierarchy_info` AS
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
  `wp02`.`discipline` AS `discipline`,
  `wp02`.`no` AS `no`,
  `wp02`.`org_id` AS `org_id`,
  `wp02`.`project_id` AS `project_id`,
  `wp02`.`remarks` AS `remarks`,
  `wp02`.`remarks2` AS `remarks2`,
  `wp02`.`business_type` AS `business_type`,
  `wp02`.`business_type_id` AS `business_type_id`,
  `wp02`.`height` AS `height`,
  `wp02`.`height_text` AS `height_text`,
  `wp02`.`height_unit` AS `height_unit`,
  `wp02`.`is_new` AS `is_new`,
  `wp02`.`length` AS `length`,
  `wp02`.`length_text` AS `length_text`,
  `wp02`.`length_unit` AS `length_unit`,
  `wp02`.`qr_code` AS `qr_code`,
  `wp02`.`revision` AS `revision`,
  `wp02`.`weight` AS `weight`,
  `wp02`.`weight_text` AS `weight_text`,
  `wp02`.`weight_unit` AS `weight_unit`,
  `wp02`.`width` AS `width`,
  `wp02`.`width_text` AS `width_text`,
  `wp02`.`width_unit` AS `width_unit`,
  `wp02`.`work_class` AS `work_class`,
  `wp02`.`work_package_no` AS `work_package_no`,
  `wp02`.`tag_description` AS `tag_description`
from
  ((((`saint_whale_tasks`.`entity_wp02` `wp02`
    left join `saint_whale_tasks`.`project_node` `pn` on
    (((`wp02`.`id` = `pn`.`entity_id`)
      and (`pn`.`project_id` = `wp02`.`project_id`)
      and (`pn`.`entity_type` = 'WP02')
      and (`pn`.`deleted` = 0))))
    left join `saint_whale_tasks`.`hierarchy_node` `hn_on_structure` on
    (((`pn`.`project_id` = `hn_on_structure`.`project_id`)
      and (`pn`.`id` = `hn_on_structure`.`node_id`)
      and (`hn_on_structure`.`deleted` = 0)
      and (`hn_on_structure`.`hierarchy_type` = 'STRUCTURE'))))
    left join `saint_whale_tasks`.`hierarchy_node` `parent_hn_on_structure` on
    (((`parent_hn_on_structure`.`id` = `hn_on_structure`.`parent_id`)
      and (`parent_hn_on_structure`.`project_id` = `hn_on_structure`.`project_id`)
      and (`parent_hn_on_structure`.`deleted` = 0)
      and (`parent_hn_on_structure`.`hierarchy_type` = 'STRUCTURE'))))
    left join `saint_whale_tasks`.`project_node` `parent_pn` on
    (((`parent_hn_on_structure`.`node_id` = `parent_pn`.`id`)
      and (`parent_hn_on_structure`.`project_id` = `parent_pn`.`project_id`)
      and (`parent_pn`.`deleted` = 0)
      and (`parent_pn`.`entity_type` = 'WP01'))))
where
  (`wp02`.`deleted` = 0);
