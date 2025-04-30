USE `saint_whale_tasks`;
-- ----------------------------
-- View structure for pipe_piece_hierarchy_info
-- 管道实体层级
-- ----------------------------
CREATE OR REPLACE VIEW `pipe_piece_hierarchy_info` AS
  SELECT
    `pipe_piece`.*,
    `pn`.`id`                     AS `project_node_id`,
    `pn`.`node_type`              AS `node_type`,
    `pn`.`entity_type`            AS `entity_type`,
    `pn`.`entity_sub_type`        AS `entity_sub_type`,
	`module_pn`.`no`              AS `module_parent_no`,
    `hn_on_piping`.`parent_id`    AS `parent_hierarchy_id_on_piping`
  FROM
    `entity_pipe_piece` `pipe_piece`
    JOIN `project_node` `pn`
      ON `pn`.`project_id` = `pipe_piece`.`project_id`
      AND `pn`.`entity_id` = `pipe_piece`.`id`
      AND `pn`.`entity_type` = 'PIPE_PIECE'
      AND `pn`.`deleted` = `pipe_piece`.`deleted`
      AND `pn`.`discipline` =   "PIPING"

    LEFT JOIN `hierarchy_node` `hn_on_piping`
      ON `hn_on_piping`.`project_id` = `pn`.`project_id`
      AND `pn`.`id` = `hn_on_piping`.`node_id`
      AND `hn_on_piping`.`hierarchy_type` = "PIPING"
      AND `hn_on_piping`.`deleted` = FALSE

    LEFT JOIN `hierarchy_node_relation` `module_hnr`
      ON `module_hnr`.`hierarchy_id` = `hn_on_piping`.`id`
	  AND `module_hnr`.`depth` = 1

    LEFT JOIN `project_node` `module_pn`
      ON `module_pn`.`project_id` = `pn`.`project_id`
      AND `module_pn`.`node_type` = 'MODULE'
      AND `module_pn`.`deleted` = FALSE
	  AND `module_pn`.`id` = `module_hnr`.`node_ancestor_id`
     WHERE `pipe_piece`.`deleted`  = FALSE

;

