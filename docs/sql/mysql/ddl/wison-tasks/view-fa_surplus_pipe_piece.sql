USE `saint_whale_tasks`;
CREATE OR REPLACE VIEW `fa_surplus_pipe_piece` AS
  SELECT
    `pn`.`no` AS `Four_Node`,
    `epp`.`material_code` AS `tag_code`,
	`epp`.`material` AS `Tag_Desc`,
    `epp`.`nps` AS `Geom_Size`,
    `epp`.`nps_unit` AS `UOM`,
    sum(`epp`.`length`) AS `Total_Qty`,
    `mmp`.`id` AS `id`
  FROM
    `mat_f_material_surplus_receipt` `mmp`
  JOIN `mat_f_material_surplus_receipt_node_item` `mmpn`
    ON `mmpn`.`f_material_surplus_receipt_id` = `mmp`.`id`
  JOIN `project_node` `pn`
    ON `pn`.`entity_id` = `mmpn`.`entity_id`
  JOIN `entity_pipe_piece` `epp`
    ON `pn`.`entity_id` = `epp`.`id`
    AND `epp`.`status` = 'ACTIVE'
  group by
    `pn`.`no`,
    `epp`.`material_code`,
    `epp`.`material`,
    `epp`.`nps`,
    `epp`.`nps_unit`,
    `mmp`.`id`
;

