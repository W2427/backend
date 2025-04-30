USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `fa_component` AS
  SELECT
    `ecom`.`material_code` AS `tag_code`,
    `ecom`.`nps_text` AS `Geom_Size`,
    `ecom`.`material` AS `Tag_Desc`,
    `ecom`.`material` AS `Tag_Ldesc`,
    'EA' AS `UOM`,
    sum(`ecom`.`qty`) AS `Design_Qty`,
    sum(`ecom`.`qty`) AS `Total_Qty`,
    left(substring_index(`pn`.`iso_no`,'-',-(1)),9) AS `Third_node`,
    `pn`.`iso_no` AS `Four_Node`
  FROM
    `mat_f_material_prepare` `mmp`
    JOIN `mat_f_material_prepare_node` `mmpn`
      ON `mmpn`.`mp_id` = `mmp`.`id`

    JOIN `project_node` `pn`
      ON `pn`.`entity_id` = `mmpn`.`project_node_id`

    JOIN `entity_components` `ecom`
      ON `pn`.`entity_id` = `ecom`.`iso_entity_id`
      and isnull(`ecom`.`spool_entity_id`)
      and `ecom`.`status` = 'ACTIVE'

    group by
      `ecom`.`material_code`,
      `pn`.`iso_no`,
      `ecom`.`nps_text`,
      `ecom`.`material`
;


