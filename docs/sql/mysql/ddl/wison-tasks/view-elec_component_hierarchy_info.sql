USE `saint_whale_tasks`;
-- ------------------------
--  elec_component_hierarchy_info
--  电气部件实体层级信息
-- ------------------------

CREATE OR REPLACE VIEW `elec_component_hierarchy_info` AS

  SELECT
    `parent_hn_on_electrical`.`id`              AS `parent_hierarchy_id_on_electrical`,
    `parent_pn_on_electrical`.`entity_id`       AS `parent_entity_id`,
    `parent_pn_on_electrical`.`no`              AS `parent_no`,
    `module_pn`.`no`                            AS `module_parent_no`,
    `hn_on_electrical`.`id`                     AS `hierarchy_id_on_electrical`,
    `parent_pn_on_electrical`.`entity_type`     AS `parent_entity_type`,
    `hn_on_electrical`.`path`                   AS `path_on_electrical`,
    `hn_on_cpp`.`path`                          AS `path_on_cpp`,
    `hn_on_ss`.`path`                           AS `path_on_ss`,
    `elec_component`.*

  FROM

    `entity_elec_component` `elec_component`
      JOIN `project_node` `pn`
        ON `pn`.`project_id` = `elec_component`.`project_id`
          AND `pn`.`entity_id` = `elec_component`.`id`
          AND `pn`.`entity_type` = 'ELEC_COMPONENT'
          AND `pn`.`deleted` = `elec_component`.`deleted`

      LEFT JOIN `hierarchy_node` `hn_on_electrical`
        ON `hn_on_electrical`.`project_id` = `pn`.`project_id`
          AND `pn`.`id` = `hn_on_electrical`.`node_id`
          AND `hn_on_electrical`.`hierarchy_type` = 'ELECTRICAL'
          AND `hn_on_electrical`.`deleted` = FALSE

      LEFT JOIN `hierarchy_node` `parent_hn_on_electrical`
        ON `hn_on_electrical`.`parent_id` = `parent_hn_on_electrical`.`id`
          AND `parent_hn_on_electrical`.`project_id` = `hn_on_electrical`.`project_id`
          AND `parent_hn_on_electrical`.`hierarchy_type` = 'ELECTRICAL'
          AND `parent_hn_on_electrical`.`deleted` = FALSE

      LEFT JOIN `project_node` `parent_pn_on_electrical`
        ON `parent_pn_on_electrical`.`id` = `parent_hn_on_electrical`.`node_id`
          AND `parent_pn_on_electrical`.`project_id` = `parent_hn_on_electrical`.`project_id`
          AND `parent_pn_on_electrical`.`deleted` = FALSE

      LEFT JOIN `hierarchy_node` `hn_on_cpp`
        ON `hn_on_cpp`.`project_id` = `pn`.`project_id`
          AND `pn`.`id` = `hn_on_cpp`.`node_id`
          AND `hn_on_cpp`.`hierarchy_type` = 'CABLE_PULLING_PACKAGE'
          AND `hn_on_cpp`.`deleted` = FALSE

      LEFT JOIN `hierarchy_node` `hn_on_ss`
        ON `hn_on_ss`.`project_id` = `pn`.`project_id`
          AND `pn`.`id` = `hn_on_ss`.`node_id`
          AND `hn_on_ss`.`hierarchy_type` = 'SUB_SYSTEM'
          AND `hn_on_ss`.`deleted` = FALSE

      LEFT JOIN `hierarchy_node` `module_hn_on_electrical`
        ON `module_hn_on_electrical`.`path` LIKE CONCAT(`hn_on_electrical`.`path`,`hn_on_electrical`.`id`,'%')
          AND `module_hn_on_electrical`.`project_id` = `hn_on_electrical`.`project_id`
          AND `module_hn_on_electrical`.`hierarchy_type` = 'ELECTRICAL'
          AND `module_hn_on_electrical`.`deleted` = FALSE

      LEFT JOIN `project_node` `module_pn`
        ON `module_pn`.`id` = `module_hn_on_electrical`.`node_id`
          AND `module_pn`.`project_id` = `module_hn_on_electrical`.`project_id`
          AND `module_pn`.`node_type` = 'MODULE'
          AND `module_pn`.`deleted` = FALSE

;

