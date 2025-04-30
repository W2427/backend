USE `saint_whale_tasks`;
-- ------------------------------
-- cable_hierarchy_info
-- cable层级视图
-- ------------------------------
CREATE OR REPLACE VIEW `cable_hierarchy_info` AS
  SELECT
    `hn_on_electrical`.`path`           AS `path_on_electrical`,
    `parent_hn_on_electrical`.`id`      AS `parent_hierarchy_id_on_electrical`,
    `parent_pn_on_electrical`.`no`      AS `parent_no_on_electrical`,
    `hn_on_cpp`.`path`                  AS `path_on_cpp`,
    `parent_hn_on_cpp`.`id`             AS `parent_hierarchy_id_on_cpp`,
    `parent_pn_on_cpp`.`no`             AS `parent_no_on_cpp`,
    `hn_on_ss`.`path`                   AS `path_on_ss`,
    `parent_hn_on_ss`.`id`              AS `parent_hierarchy_id_on_ss`,
    `parent_pn_on_ss`.`no`              AS `parent_no_on_ss`,
    `cable`.*,
    `pn`.`id`                           AS `project_node_id`,
    `pn`.`node_type`                    AS `node_type`,
    `pn`.`entity_type`                  AS `entity_type`,
    `pn`.`entity_sub_type`              AS `entity_sub_type`

  FROM
    `entity_cable` `cable`
    JOIN `project_node` `pn`
      ON `pn`.`project_id` = `cable`.`project_id`
      AND `pn`.`entity_id` = `cable`.`id`
      AND `pn`.`entity_type` = 'CABLE'
      AND `pn`.`deleted` = `cable`.`deleted`

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
      AND `pn`.`project_id` = `parent_pn_on_electrical`.`project_id`
      AND `parent_pn_on_electrical`.`deleted` = FALSE

    LEFT JOIN `hierarchy_node` `hn_on_cpp`
      ON `hn_on_cpp`.`project_id` = `pn`.`project_id`
      AND `pn`.`id` = `hn_on_cpp`.`node_id`
      AND `hn_on_cpp`.`hierarchy_type` = 'CABLE_PULLING_PACKAGE'
      AND `hn_on_cpp`.`deleted` = FALSE

    LEFT JOIN `hierarchy_node` `parent_hn_on_cpp`
      ON `parent_hn_on_cpp`.`project_id` = `pn`.`project_id`
      AND `parent_hn_on_cpp`.`id` = `hn_on_cpp`.`parent_id`
      AND `parent_hn_on_cpp`.`hierarchy_type` = 'CABLE_PULLING_PACKAGE'
      AND `parent_hn_on_cpp`.`deleted` = FALSE

    LEFT JOIN `project_node` `parent_pn_on_cpp`
      ON `parent_pn_on_cpp`.`id` = `parent_hn_on_cpp`.`node_id`
      AND `parent_pn_on_cpp`.`project_id` = `parent_hn_on_cpp`.`project_id`
      AND `parent_pn_on_cpp`.`deleted` = FALSE

    LEFT JOIN `hierarchy_node` `hn_on_ss`
      ON `hn_on_ss`.`project_id` = `pn`.`project_id`
      AND `pn`.`id` = `hn_on_ss`.`node_id`
      AND `hn_on_ss`.`hierarchy_type` = 'SUB_SYSTEM'
      AND `hn_on_ss`.`deleted` = FALSE

    LEFT JOIN `hierarchy_node` `parent_hn_on_ss`
      ON `parent_hn_on_ss`.`project_id` = `pn`.`project_id`
      AND `parent_hn_on_ss`.`id` = `hn_on_ss`.`parent_id`
      AND `parent_hn_on_ss`.`hierarchy_type` = 'SUB_SYSTEM'
      AND `parent_hn_on_ss`.`deleted` = FALSE

    LEFT JOIN `project_node` `parent_pn_on_ss`
      ON `parent_pn_on_ss`.`id` = `parent_hn_on_ss`.`node_id`
      AND `parent_pn_on_ss`.`project_id` = `parent_hn_on_ss`.`project_id`
      AND `parent_pn_on_ss`.`deleted` = FALSE


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
