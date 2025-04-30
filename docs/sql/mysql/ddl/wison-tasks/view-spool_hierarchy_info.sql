USE `saint_whale_tasks`;
-- -----------------------------------------------------------------------------
-- Spool-hierarchy-info
-- 单管层级视图
-- View structure for process_entity_types
-- -----------------------------------------------------------------------------

CREATE OR REPLACE VIEW `spool_hierarchy_info` AS
  SELECT
    `parent_hn_on_piping`.`id`    AS `parent_hierarchy_id_on_piping`,
     IFNULL(`module_pn`.`no` ,`module_pn_layer`.`no`)             AS `module_parent_no`,
    `hn_on_piping`.`id`           AS `hierarchy_id_on_piping`,
    `hn_on_piping`.`path`         AS `path_on_piping`,
    `hn_on_ptp`.`path`            AS `path_on_ptp`,
    `hn_on_cp`.`path`             AS `path_on_cp`,
    `hn_on_ss`.`path`             AS `path_on_ss`,
    `spool`.*,
    `pn`.`id` AS `project_node_id`,
    `pn`.`node_type` AS `node_type`,
    `pn`.`entity_type` AS `entity_type`,
    `pn`.`entity_sub_type` AS `entity_sub_type`

-- 	 spool 主表
  FROM
    `entity_spool` `spool`
		-- 	 spool 节点表
    JOIN `project_node` `pn`
      ON `pn`.`project_id` = `spool`.`project_id`
        AND `pn`.`entity_id` = `spool`.`id`
        AND `pn`.`entity_type` = 'SPOOL'
        AND `pn`.`deleted` = FALSE
		-- 	 spool 管系 层级表
    LEFT JOIN `hierarchy_node` `hn_on_piping`
        ON `hn_on_piping`.`project_id` = `pn`.`project_id`
          AND `pn`.`id` = `hn_on_piping`.`node_id`
          AND `hn_on_piping`.`hierarchy_type` = "PIPING"
          AND `hn_on_piping`.`deleted` = FALSE
		-- 	 spool 管系 父级 层级表
    LEFT JOIN `hierarchy_node` `parent_hn_on_piping`
      ON `hn_on_piping`.`parent_id` = `parent_hn_on_piping`.`id`
        AND `parent_hn_on_piping`.`project_id` = `hn_on_piping`.`project_id`
        AND `parent_hn_on_piping`.`hierarchy_type` = "PIPING"
        AND `parent_hn_on_piping`.`deleted` = FALSE
		-- 	 spool 管系 父级 节点表
    LEFT JOIN `project_node` `parent_pn_on_piping`
      ON `parent_pn_on_piping`.`id` = `parent_hn_on_piping`.`node_id`
        AND `parent_pn_on_piping`.`project_id` = `parent_hn_on_piping`.`project_id`
        AND `parent_pn_on_piping`.`deleted` = FALSE
		-- 	 iso 管系 父级 层级表
    LEFT JOIN `hierarchy_node` `iso_parent_hn_on_piping`
      ON `parent_hn_on_piping`.`parent_id` = `iso_parent_hn_on_piping`.`id`
      AND `parent_hn_on_piping`.`project_id` = `iso_parent_hn_on_piping`.`project_id`
      AND `iso_parent_hn_on_piping`.`hierarchy_type` = "PIPING"
      AND `iso_parent_hn_on_piping`.`deleted` = FALSE
		-- 	 spool 试压包 层级表
    LEFT JOIN `hierarchy_node` `hn_on_ptp`
      ON `hn_on_ptp`.`project_id` = `pn`.`project_id`
        AND `pn`.`id` = `hn_on_ptp`.`node_id`
        AND `hn_on_ptp`.`hierarchy_type` = 'PRESSURE_TEST_PACKAGE'
        AND `hn_on_ptp`.`deleted` = FALSE
		-- 	 spool 清洁包 层级表
    LEFT JOIN `hierarchy_node` `hn_on_cp`
      ON `hn_on_cp`.`project_id` = `pn`.`project_id`
        AND `pn`.`id` = `hn_on_cp`.`node_id`
        AND `hn_on_cp`.`hierarchy_type` = 'CLEAN_PACKAGE'
        AND `hn_on_cp`.`deleted` = FALSE
		-- 	 spool 子系统 层级表
    LEFT JOIN `hierarchy_node` `hn_on_ss`
      ON `hn_on_ss`.`project_id` = `pn`.`project_id`
        AND `pn`.`id` = `hn_on_ss`.`node_id`
        AND `hn_on_ss`.`hierarchy_type` = 'SUB_SYSTEM'
        AND `hn_on_ss`.`deleted` = FALSE
		-- 	 spool 管系 模块层级表（原逻辑）
--     LEFT JOIN `hierarchy_node` `module_hn_on_piping`
--       ON `module_hn_on_piping`.`path` LIKE CONCAT(`hn_on_piping`.`path`,`hn_on_piping`.`id`,'%')
--         AND `module_hn_on_piping`.`project_id` = `hn_on_piping`.`project_id`
--         AND `module_hn_on_piping`.`hierarchy_type` = "PIPING"
--         AND `module_hn_on_piping`.`deleted` = FALSE
		-- 	 spool 管系 模块层级表	(直接)
		LEFT JOIN `hierarchy_node` `module_hn_on_piping`
      ON `iso_parent_hn_on_piping`.`id`=module_hn_on_piping.id
        AND `module_hn_on_piping`.`project_id` = `hn_on_piping`.`project_id`
        AND `module_hn_on_piping`.`hierarchy_type` = "PIPING"
        AND `module_hn_on_piping`.`deleted` = FALSE
		-- 	 spool 管系 模块节点表
		LEFT JOIN `project_node` `module_pn`
      ON `module_pn`.`id` = `module_hn_on_piping`.`node_id`
        AND `module_pn`.`project_id` = `module_hn_on_piping`.`project_id`
        AND `module_pn`.`node_type` = 'MODULE'
        AND `module_pn`.`deleted` = FALSE
		-- 	 spool 管系 模块层级表	(layer)
		LEFT JOIN `hierarchy_node` `module_hn_on_piping_layer`
      ON `iso_parent_hn_on_piping`.path = CONCAT(`module_hn_on_piping_layer`.path,`module_hn_on_piping_layer`.`id`,'/')
        AND `module_hn_on_piping_layer`.`project_id` = `hn_on_piping`.`project_id`
        AND `module_hn_on_piping_layer`.`hierarchy_type` = "PIPING"
        AND `module_hn_on_piping_layer`.`deleted` = FALSE
		-- 	 spool 管系 模块节点表（layer）
    LEFT JOIN `project_node` `module_pn_layer`
      ON `module_pn_layer`.`id` = `module_hn_on_piping_layer`.`node_id`
        AND `module_pn_layer`.`project_id` = `module_hn_on_piping_layer`.`project_id`
        AND `module_pn_layer`.`node_type` = 'MODULE'
        AND `module_pn_layer`.`deleted` = FALSE
;

