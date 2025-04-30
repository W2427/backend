USE `saint_whale_tasks`;
CREATE OR REPLACE VIEW `fa_pipe_piece` AS
 SELECT
    `epp`.`spool_no` AS `Four_Node`,
    `epp`.`material_code` AS `tag_code`,
	`epp`.`material` AS `Tag_Desc`,
    `epp`.`nps` AS `Geom_Size`,
    `epp`.`nps_unit` AS `UOM`,
    sum(`epp`.`length`) AS `Total_Qty`,
    `mmp`.`id` AS `id`
  FROM
    `mat_f_material_prepare` `mmp`
  JOIN `mat_f_material_prepare_item` `mmpn`
    ON `mmpn`.`mp_id` = `mmp`.`id`
		AND `mmpn`.`status` = 'ACTIVE'
  JOIN `entity_pipe_piece` `epp`
    ON `epp`.`id` = `mmpn`.`pipe_piece_id`
    AND `epp`.`status` = 'ACTIVE'
  group by
    `epp`.`spool_no`,
    `epp`.`material_code`,
    `epp`.`material`,
    `epp`.`nps`,
    `epp`.`nps_unit`,
    `mmp`.`id`
;
