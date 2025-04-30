USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `fa_component` AS
  SELECT
    `pn`.`spool_no` AS `Four_Node`,
    `ecom`.`material_code` AS `Material Code`,
    `ecom`.`material` AS `Tag_Desc`,
    `ecom`.`nps_text` AS `NPS`,
    `ecom`.`nps_unit` AS `NPS unit`,
	`ecom`.`qty` AS `Total_Qty`,
	'EA' AS `UOM`,
    `mmp`.`id` AS `id`
  FROM
  `mat_f_material_prepare` `mmp`
  JOIN `mat_f_material_prepare_item` `mmpn`
    ON `mmpn`.`mp_id` = `mmp`.`id`
  JOIN `project_node` `pn`
    ON `pn`.`entity_id` = `mmpn`.`mpn_id`
  JOIN `entity_components` `ecom`
    ON `pn`.`entity_id` = `ecom`.`spool_entity_id`
    AND `ecom`.`status` = 'ACTIVE'
  group by
	`pn`.`spool_no`,
    `ecom`.`material_code`,
    `ecom`.`material`,
    `ecom`.`nps_text`,
	`ecom`.`nps_unit`,
    `mmp`.`id`
;
