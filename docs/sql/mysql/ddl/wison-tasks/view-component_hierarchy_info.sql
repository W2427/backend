USE `saint_whale_tasks`;
-- ------------------------
--  component_hierarchy_info
--  管件实体层级信息
-- ------------------------

CREATE OR REPLACE VIEW `component_hierarchy_info` AS

  SELECT
    `parent_hn_on_piping`.`id`              AS `parent_hierarchy_id_on_piping`,
    `parent_pn_on_piping`.`entity_id`       AS `parent_entity_id`,
    `parent_pn_on_piping`.`no`              AS `parent_no`,
    `module_pn`.`no`                        AS `module_parent_no`,
    `hn_on_piping`.`id`                     AS `hierarchy_id_on_piping`,
    `parent_pn_on_piping`.`entity_type`     AS `parent_entity_type`,
    `hn_on_piping`.`path`                   AS `path_on_piping`,
    `hn_on_ptp`.`path`                      AS `path_on_ptp`,
    `hn_on_cp`.`path`                       AS `path_on_cp`,
    `hn_on_ss`.`path`                       AS `path_on_ss`,
    `component`.`module_id`                 AS `module_id`,
    `component`.`module_no`                 AS `module_no`,
     `component`.`layer_package_no`             AS `layer_package_no`,
    `component`.`pressure_test_package_no`      AS `pressure_test_package_no`,
    `component`.`clean_package_no`              AS `clean_package_no`,
    `component`.`sub_system_no`                 AS `sub_system_no`,
    `component`.`id`                        AS `id`,
    `component`.`short_code`                AS `short_code`,
    `component`.`no`                        AS `no`,
    `component`.`component_entity_type`     AS `component_entity_type`,
    `component`.`entity_business_type`      AS `entity_business_type`,
    `component`.`material`                  AS `material`,
    `component`.`material_code`             AS `material_code`,
    `component`.`status`                    AS `status`,
    `component`.`qty`                       AS `qty`,
    `component`.`qty_unit`                  AS `qty_unit`,
    `component`.`nps`                       AS `nps`,
    `component`.`nps_text`                  AS `nps_text`,
    `component`.`nps_unit`                  AS `nps_unit`,
    `component`.`thickness`                 AS `thickness`,
    `component`.`flange_management`         AS `flange_management`,
    `component`.`revision`                  AS `revision`,
    `component`.`created_at`                AS `created_at`,
    `component`.`created_by`                AS `created_by`,
    `component`.`version`                   AS `version`,
    `component`.`last_modified_at`          AS `last_modified_at`,
    `component`.`last_modified_by`          AS `last_modified_by`,
    `component`.`company_id`                AS `company_id`,
    `component`.`org_id`                    AS `org_id`,
    `component`.`project_id`                AS `project_id`,
    `component`.`pipe_class`                AS `pipe_class`,
    `component`.`insulation_code`           AS `insulation_code`,
    `component`.`sheet_no`                  AS `sheet_no`,
    `component`.`sheet_total`               AS `sheet_total`,
    `component`.`iso_drawing`               AS `iso_drawing`,
    `component`.`remarks`                   AS `remarks`,
    `component`.`deleted`                   AS `deleted`,
    `component`.`deleted_at`                AS `deleted_at`,
    `component`.`deleted_by`                AS `deleted_by`,
    `component`.`display_name`              AS `display_name`,
    `component`.`coordinatex`               AS `coordinatex`,
    `component`.`coordinatey`               AS `coordinatey`,
    `component`.`coordinatez`               AS `coordinatez`,
    `component`.`fabricated`                AS `fabricated`,
    `component`.`painting_code`             AS `painting_code`,
    `component`.`surface_treatment`         AS `surface_treatment`,
    `component`.`iso_entity_id`             AS `iso_entity_id`,
    `component`.`spool_entity_id`           AS `spool_entity_id`,
    `component`.`iso_no`                    AS `iso_no`,
    `component`.`spool_no`                  AS `spool_no`,
    `component`.`cancelled`                 AS `cancelled`,
    `component`.`remarks2`                  AS `remarks2`,
    `component`.`material_with_positional_mark`  AS `material_with_positional_mark`,
    `component`.`work_package_no` AS `work_package_no`

  FROM

    `entity_components` `component`
      JOIN `project_node` `pn`
        ON `pn`.`project_id` = `component`.`project_id`
          AND `pn`.`entity_id` = `component`.`id`
          AND `pn`.`entity_type` = 'COMPONENT'
          AND `pn`.`discipline`                       =   "PIPING"
          AND `pn`.`deleted` = `component`.`deleted`

      LEFT JOIN `hierarchy_node` `hn_on_piping`
        ON `hn_on_piping`.`project_id` = `pn`.`project_id`
          AND `pn`.`id` = `hn_on_piping`.`node_id`
          AND `hn_on_piping`.`hierarchy_type` = "PIPING"
          AND `hn_on_piping`.`deleted` = FALSE

      LEFT JOIN `hierarchy_node` `parent_hn_on_piping`
        ON `hn_on_piping`.`parent_id` = `parent_hn_on_piping`.`id`
          AND `parent_hn_on_piping`.`project_id` = `hn_on_piping`.`project_id`
          AND `parent_hn_on_piping`.`hierarchy_type` = "PIPING"
          AND `parent_hn_on_piping`.`deleted` = FALSE

      LEFT JOIN `project_node` `parent_pn_on_piping`
        ON `parent_pn_on_piping`.`id` = `parent_hn_on_piping`.`node_id`
          AND `parent_pn_on_piping`.`project_id` = `parent_hn_on_piping`.`project_id`
          AND `parent_pn_on_piping`.`deleted` = FALSE

      LEFT JOIN `hierarchy_node` `hn_on_ptp`
        ON `hn_on_ptp`.`project_id` = `pn`.`project_id`
          AND `pn`.`id` = `hn_on_ptp`.`node_id`
          AND `hn_on_ptp`.`hierarchy_type` = 'PRESSURE_TEST_PACKAGE'
          AND `hn_on_ptp`.`deleted` = FALSE

      LEFT JOIN `hierarchy_node` `hn_on_cp`
        ON `hn_on_cp`.`project_id` = `pn`.`project_id`
          AND `pn`.`id` = `hn_on_cp`.`node_id`
          AND `hn_on_cp`.`hierarchy_type` = 'CLEAN_PACKAGE'
          AND `hn_on_cp`.`deleted` = FALSE

      LEFT JOIN `hierarchy_node` `hn_on_ss`
        ON `hn_on_ss`.`project_id` = `pn`.`project_id`
          AND `pn`.`id` = `hn_on_ss`.`node_id`
          AND `hn_on_ss`.`hierarchy_type` = 'SUB_SYSTEM'
          AND `hn_on_ss`.`deleted` = FALSE

      LEFT JOIN `hierarchy_node` `module_hn_on_piping`
        ON `module_hn_on_piping`.`path` LIKE CONCAT(`hn_on_piping`.`path`,`hn_on_piping`.`id`,'%')
          AND `module_hn_on_piping`.`project_id` = `hn_on_piping`.`project_id`
          AND `module_hn_on_piping`.`hierarchy_type` = "PIPING"
          AND `module_hn_on_piping`.`deleted` = FALSE

      LEFT JOIN `project_node` `module_pn`
        ON `module_pn`.`id` = `module_hn_on_piping`.`node_id`
          AND `module_pn`.`project_id` = `module_hn_on_piping`.`project_id`
          AND `module_pn`.`node_type` = 'MODULE'
          AND `module_pn`.`deleted` = FALSE

;

