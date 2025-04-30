-- saint_whale_tasks.wp01_hierarchy_info source

CREATE OR REPLACE
ALGORITHM = UNDEFINED VIEW `saint_whale_tasks`.`wp01_hierarchy_info` AS
select
  `hn_on_structure`.`hierarchy_type` AS `hierarchy_type`,
  `hn_on_structure`.`id` AS `hierarchy_id`,
  `hn_on_structure`.`path` AS `path_on_structure`,
  `pn`.`id` AS `project_node_id`,
  '' AS `node_type`,
  `pn`.`entity_type` AS `entity_type`,
  `pn`.`entity_sub_type` AS `entity_sub_type`,
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
  `w1`.`discipline` AS `discipline`,
  `w1`.`display_name` AS `display_name`,
  `w1`.`no` AS `no`,
  `w1`.`org_id` AS `org_id`,
  `w1`.`project_id` AS `project_id`,
  `w1`.`remarks` AS `remarks`,
  `w1`.`remarks2` AS `remarks2`,
  `w1`.`business_type` AS `business_type`,
  `w1`.`business_type_id` AS `business_type_id`,
  `w1`.`height` AS `height`,
  `w1`.`height_text` AS `height_text`,
  `w1`.`height_unit` AS `height_unit`,
  `w1`.`is_new` AS `is_new`,
  `w1`.`length` AS `length`,
  `w1`.`length_text` AS `length_text`,
  `w1`.`length_unit` AS `length_unit`,
  `w1`.`qr_code` AS `qr_code`,
  `w1`.`revision` AS `revision`,
  `w1`.`shipment_no` AS `shipment_no`,
  `w1`.`weight` AS `weight`,
  `w1`.`weight_text` AS `weight_text`,
  `w1`.`weight_unit` AS `weight_unit`,
  `w1`.`width` AS `width`,
  `w1`.`width_text` AS `width_text`,
  `w1`.`width_unit` AS `width_unit`,
  `w1`.`work_package_no` AS `work_package_no`
from
  ((`saint_whale_tasks`.`entity_wp01` `w1`
    left join `saint_whale_tasks`.`project_node` `pn` on
    (((`w1`.`project_id` = `pn`.`project_id`)
      and (`w1`.`id` = `pn`.`entity_id`)
      and (`pn`.`deleted` = 0)
      and (`pn`.`entity_type` = 'WP01'))))
    left join `saint_whale_tasks`.`hierarchy_node` `hn_on_structure` on
    (((`hn_on_structure`.`project_id` = `pn`.`project_id`)
      and (`hn_on_structure`.`node_id` = `pn`.`id`)
      and (`hn_on_structure`.`deleted` = 0)
      and (`hn_on_structure`.`hierarchy_type` = 'STRUCTURE'))))
where
  (`w1`.`deleted` = 0);
