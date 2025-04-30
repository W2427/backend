USE `saint_whale_tasks`;


-- ----------------------------
-- View structure for structure_process_entity
-- 结构专业的 工序和实体关系视图
-- ----------------------------
CREATE OR REPLACE VIEW `structure_process_entity` AS
  SELECT
    concat(`s`.`id`,':',`p`.`id`,':',`hn`.`id`) 			AS `id`,
    `s`.`org_id` 																			AS `org_id`,
    `s`.`project_id` 																	AS `project_id`,
    `s`.`id` 																					AS `stage_id`,
    `s`.`name_en` 																		AS `stage`,
    `p`.`id` 																					AS `process_id`,
    `p`.`name_en` 																		AS `process`,
    `ect`.`id` 																				AS `entity_type_id`,
    `ect`.`name_en` 																	AS `entity_type`,
    `ec`.`id` 																				AS `entity_sub_type_id`,
    `ec`.`name_en` 																		AS `entity_sub_type`,
    `pn`.`entity_id` 																	AS `entity_id`,
    `hn`.`id` 																				AS `hierarchy_node_id`,
    `hn`.`path` 																			AS `hierarchy_path`,
    `hn`.`depth` 																			AS `hierarchy_depth`,
    `hn`.`parent_id` 																	AS `parent_hierarchy_node_id`,
    `hn`.`hierarchy_type` 														AS `hierarchy_type`,
    `ppn`.`id` 																				AS `module_hierarchy_node_id`,
    `pn`.`wp01_id` 																		AS `wp01_id`,
    `pn`.`wp01_no` 																		AS `wp01_no`,
    `pn`.`wp02_id` 																		AS `wp02_id`,
    `pn`.`wp02_no` 																		AS `wp02_no`,
    `pn`.`wp03_id` 																		AS `wp03_id`,
    `pn`.`wp03_no` 																		AS `wp03_no`,
    `pn`.`wp04_id` 																		AS `wp04_id`,
    `pn`.`wp04_no` 																		AS `wp04_no`,
    `pn`.`id` 																				AS `project_node_id`,
    `pn`.`no` 																				AS `project_node_no`,
    `pn`.`dwg_sht_no` 																AS `dwg_sht_no`,
    `pn`.`work_load` 																	AS `work_load`,
    `pn`.`discipline`																	AS `discipline`,
    ''                                                AS `iso_project_node_no`


  FROM

    -- 取得 工序阶段 和 工序
    `bpm_process_stage` `s`
        JOIN `bpm_process` `p` ON
          `p`.`process_stage_id` = `s`.`id`
          AND  `p`.`status` = 'ACTIVE'

        -- 取得 工序阶段+工序 和 实体类型 关系
        JOIN `bpm_entity_type_process_relation` `ecp` ON
          `ecp`.`process_id` = `p`.`id`
          AND  `ecp`.`status` = 'ACTIVE'

        -- 取得 实体类型
        JOIN `bpm_entity_sub_type` `ec` ON
          `ec`.`id` = `ecp`.`entity_sub_type_id`
          AND  `ec`.`status` = 'ACTIVE'

        -- 取得 实体类型分类
        JOIN `bpm_entity_type` `ect` ON
          `ect`.`id` = `ec`.`entity_category_type_id`
          AND  `ect`.`type` = 'READONLY'
          AND  `ect`.`status` = 'ACTIVE'

        -- 取得 项目节点 关联 实体类型+工序阶段+工序
        JOIN `project_node` `pn` ON
          `pn`.`project_id` = `s`.`project_id`
          AND  `pn`.`entity_sub_type` = `ec`.`name_en`
          AND  `pn`.`deleted` = 0

        -- 取得 层级节点 根据 项目节点 （ 在 STRUCTURE维度 的层级节点）
        JOIN `hierarchy_node` `hn` ON
          `hn`.`node_id` = `pn`.`id`
          AND `hn`.`hierarchy_type` = 'STRUCTURE'
          AND  `hn`.`deleted` = 0

         -- 层级父级 包括自己 和 祖先父级（多父级），维度和hn相同
        JOIN `hierarchy_node` `phn` ON
          (`phn`.`id` = `hn`.`id` OR `hn`.`path` LIKE concat(`phn`.`path`, `phn`.`id`, '/%'))
          AND  `phn`.`hierarchy_type` = `hn`.`hierarchy_type`
          AND  `phn`.`deleted` = 0

        -- 项目节点父级（找ISO节点），ISO维度，实体类型为 ISO，或者LP维度，实体类型为ISO
        LEFT JOIN `project_node` `ppn` ON
          `ppn`.`id` = `phn`.`node_id`
          AND  `ppn`.`project_id` = `s`.`project_id`
          AND `ppn`.`node_type` = 'MODULE'
          AND  `ppn`.`deleted` = 0

  WHERE
     `s`.`status` = 'ACTIVE'
;
