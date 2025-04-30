USE `saint_whale_tasks`;
-- ------------------------------
-- iso_hierarchy_info
-- iso层级视图
-- ------------------------------
CREATE OR REPLACE VIEW `iso_hierarchy_info` AS
  SELECT
    `hn_on_piping`.`path`               AS `path_on_piping`,
    `hn_on_piping`.`id`               AS `hierarchy_id_area`,
    `parent_hn_on_piping`.`id`          AS `parent_hierarchy_id_on_piping`,
    `parent_pn_on_piping`.`no`          AS `parent_no_on_piping`,
    `hn_on_ptp`.`path`                  AS `path_on_ptp`,
    `parent_hn_on_ptp`.`id`             AS `parent_hierarchy_id_on_ptp`,
    `parent_pn_on_ptp`.`no`             AS `parent_no_on_ptp`,
    `hn_on_cp`.`path`                   AS `path_on_cp`,
    `parent_hn_on_cp`.`id`              AS `parent_hierarchy_id_on_cp`,
    `parent_pn_on_cp`.`no`              AS `parent_no_on_cp`,
    `hn_on_ss`.`path`                   AS `path_on_ss`,
    `parent_hn_on_ss`.`id`              AS `parent_hierarchy_id_on_ss`,
    `parent_pn_on_ss`.`no`              AS `parent_no_on_ss`,
		 IFNULL(`module_pn`.`no` ,`module_pn_layer`.`no`)             AS `module_parent_no`,
    `iso`.`id`                          AS `id`,
    `iso`.`module_id`                          AS `module_id`,
    `iso`.`module_no`                          AS `module_no`,
    `iso`.`layer_package_no`                          AS `layer_package_no`,
    `iso`.`pressure_test_package_no`                          AS `pressure_test_package_no`,
    `iso`.`clean_package_no`                          AS `clean_package_no`,
    `iso`.`sub_system_no`                          AS `sub_system_no`,
    `iso`.`created_at`                  AS `created_at`,
    `iso`.`last_modified_at`            AS `last_modified_at`,
    `iso`.`status`                      AS `status`,
    `iso`.`created_by`                  AS `created_by`,
    `iso`.`deleted`                     AS `deleted`,
    `iso`.`deleted_at`                  AS `deleted_at`,
    `iso`.`deleted_by`                  AS `deleted_by`,
    `iso`.`last_modified_by`            AS `last_modified_by`,
    `iso`.`version`                     AS `version`,
    `iso`.`company_id`                  AS `company_id`,
    `iso`.`no`                          AS `no`,
    `iso`.`org_id`                      AS `org_id`,
    `iso`.`project_id`                  AS `project_id`,
    `iso`.`asme_category`               AS `asme_category`,
    `iso`.`design_pressure`             AS `design_pressure`,
    `iso`.`design_pressure_extra_info`  AS `design_pressure_extra_info`,
    `iso`.`design_pressure_text`        AS `design_pressure_text`,
    `iso`.`design_pressure_unit`        AS `design_pressure_unit`,
    `iso`.`design_temperature`          AS `design_temperature`,
    `iso`.`design_temperature_text`     AS `design_temperature_text`,
    `iso`.`design_temperature_unit`     AS `design_temperature_unit`,
    `iso`.`fluid`                       AS `fluid`,
    `iso`.`heat_tracing_code`           AS `heat_tracing_code`,
    `iso`.`insulation_code`             AS `insulation_code`,
    `iso`.`insulation_thickness`        AS `insulation_thickness`,
    `iso`.`insulation_thickness_text`   AS `insulation_thickness_text`,
    `iso`.`insulation_thickness_unit`   AS `insulation_thickness_unit`,
    `iso`.`jacket_pipe`                 AS `jacket_pipe`,
    `iso`.`nde`                         AS `nde`,
    `iso`.`nde_ratio`                   AS `nde_ratio`,
    `iso`.`nps`                         AS `nps`,
    `iso`.`nps_text`                    AS `nps_text`,
    `iso`.`nps_unit`                    AS `nps_unit`,
    `iso`.`operate_pressure`            AS `operate_pressure`,
    `iso`.`operate_pressure_text`       AS `operate_pressure_text`,
    `iso`.`operate_pressure_unit`       AS `operate_pressure_unit`,
    `iso`.`operate_temperature`         AS `operate_temperature`,
    `iso`.`operate_temperature_text`    AS `operate_temperature_text`,
    `iso`.`operate_temperature_unit`    AS `operate_temperature_unit`,
    `iso`.`painting_code`               AS `painting_code`,
    `iso`.`pid_drawing`                 AS `pid_drawing`,
    `iso`.`pipe_class`                  AS `pipe_class`,
    `iso`.`pipe_grade`                  AS `pipe_grade`,
    `iso`.`pmi_ratio`                   AS `pmi_ratio`,
    `iso`.`pressure_test_medium`        AS `pressure_test_medium`,
    `iso`.`pwht`                        AS `pwht`,
    `iso`.`revision`                    AS `revision`,
    `iso`.`test_pressure`               AS `test_pressure`,
    `iso`.`test_pressure_text`          AS `test_pressure_text`,
    `iso`.`test_pressure_unit`          AS `test_pressure_unit`,
    `iso`.`display_name`                AS `display_name`,
    `iso`.`blind_fabricate_id`          AS `blind_fabricate_id`,
    `iso`.`blind_install_id`            AS `blind_install_id`,
    `iso`.`blind_remove_id`             AS `blind_remove_id`,
    `iso`.`blow_id`                     AS `blow_id`,
    `iso`.`filling_install_id`          AS `filling_install_id`,
    `iso`.`line_check_id`               AS `line_check_id`,
    `iso`.`oil_flush_id`                AS `oil_flush_id`,
    `iso`.`pressure_test_id`            AS `pressure_test_id`,
    `iso`.`shield_fabricate_id`         AS `shield_fabricate_id`,
    `iso`.`shield_install_id`           AS `shield_install_id`,
    `iso`.`water_flush_id`              AS `water_flush_id`,
    `iso`.`bom_ln_code`                 AS `bom_ln_code`,
    `iso`.`bom_ln_id`                   AS `bom_ln_id`,
    `iso`.`match_date`                  AS `match_date`,
    `iso`.`match_percent`               AS `match_percent`,
    `iso`.`cancelled`                   AS `cancelled`,
    `iso`.`process_line_no`             AS `process_line_no`,
    `iso`.`process_system_no`           AS `process_system_no`,
    `iso`.`is_used_in_field_component`  AS `is_used_in_field_component`,
    `iso`.`work_package_no`             AS `work_package_no`,
    `iso`.`remarks2`                    AS `remarks2`,
    `iso`.`remarks`                     AS `remarks`,
    `iso`.`internal_mechanical_cleaning`  AS `internal_mechanical_cleaning`,
    `pn`.`id`                           AS `project_node_id`,
    `pn`.`node_type`                    AS `node_type`,
    `pn`.`entity_type`                  AS `entity_type`,
    `pn`.`entity_sub_type`              AS `entity_sub_type`

--  ISO 主表
  FROM
    `entity_iso` `iso`
--  ISO 节点表
    LEFT JOIN `project_node` `pn`
      ON `pn`.`project_id` = `iso`.`project_id`
      AND `pn`.`entity_id` = `iso`.`id`
      AND `pn`.`entity_type` = 'ISO'
      AND `pn`.`deleted` = FALSE
--  ISO 管线维度 层级表
    LEFT JOIN `hierarchy_node` `hn_on_piping`
      ON `hn_on_piping`.`project_id` = `pn`.`project_id`
      AND `pn`.`id` = `hn_on_piping`.`node_id`
      AND `hn_on_piping`.`hierarchy_type` = "PIPING"
      AND `hn_on_piping`.`deleted` = FALSE
--  ISO 管线维度 父级 层级表
    LEFT JOIN `hierarchy_node` `parent_hn_on_piping`
      ON `hn_on_piping`.`parent_id` = `parent_hn_on_piping`.`id`
      AND `parent_hn_on_piping`.`project_id` = `hn_on_piping`.`project_id`
      AND `parent_hn_on_piping`.`hierarchy_type` = "PIPING"
      AND `parent_hn_on_piping`.`deleted` = FALSE
--  ISO 管线维度 父级 节点表
    LEFT JOIN `project_node` `parent_pn_on_piping`
      ON `parent_pn_on_piping`.`id` = `parent_hn_on_piping`.`node_id`
      AND `pn`.`project_id` = `parent_pn_on_piping`.`project_id`
      AND `parent_pn_on_piping`.`deleted` = FALSE
--  ISO 试压包维度 层级表
    LEFT JOIN `hierarchy_node` `hn_on_ptp`
      ON `hn_on_ptp`.`project_id` = `pn`.`project_id`
      AND `pn`.`id` = `hn_on_ptp`.`node_id`
      AND `hn_on_ptp`.`hierarchy_type` = 'PRESSURE_TEST_PACKAGE'
      AND `hn_on_ptp`.`deleted` = FALSE
--  ISO 试压包维度 父级 层级表
    LEFT JOIN `hierarchy_node` `parent_hn_on_ptp`
      ON `parent_hn_on_ptp`.`project_id` = `pn`.`project_id`
      AND `parent_hn_on_ptp`.`id` = `hn_on_ptp`.`parent_id`
      AND `parent_hn_on_ptp`.`hierarchy_type` = 'PRESSURE_TEST_PACKAGE'
      AND `parent_hn_on_ptp`.`deleted` = FALSE
--  ISO 试压包维度 父级 节点表
    LEFT JOIN `project_node` `parent_pn_on_ptp`
      ON `parent_pn_on_ptp`.`id` = `parent_hn_on_ptp`.`node_id`
      AND `parent_pn_on_ptp`.`project_id` = `parent_hn_on_ptp`.`project_id`
      AND `parent_pn_on_ptp`.`deleted` = FALSE
--  ISO 清洁包维度 层级表
    LEFT JOIN `hierarchy_node` `hn_on_cp`
      ON `hn_on_cp`.`project_id` = `pn`.`project_id`
      AND `pn`.`id` = `hn_on_cp`.`node_id`
      AND `hn_on_cp`.`hierarchy_type` = 'CLEAN_PACKAGE'
      AND `hn_on_cp`.`deleted` = FALSE
--  ISO 清洁包维度 父级 层级表
    LEFT JOIN `hierarchy_node` `parent_hn_on_cp`
      ON `parent_hn_on_cp`.`project_id` = `pn`.`project_id`
      AND `parent_hn_on_cp`.`id` = `hn_on_cp`.`parent_id`
      AND `parent_hn_on_cp`.`hierarchy_type` = 'CLEAN_PACKAGE'
      AND `parent_hn_on_cp`.`deleted` = FALSE
--  ISO 清洁包维度 父级 节点表
    LEFT JOIN `project_node` `parent_pn_on_cp`
      ON `parent_pn_on_cp`.`id` = `parent_hn_on_cp`.`node_id`
      AND `parent_pn_on_cp`.`project_id` = `parent_hn_on_cp`.`project_id`
      AND `parent_pn_on_cp`.`deleted` = FALSE
--  ISO 子系统维度 层级表
    LEFT JOIN `hierarchy_node` `hn_on_ss`
      ON `hn_on_ss`.`project_id` = `pn`.`project_id`
      AND `pn`.`id` = `hn_on_ss`.`node_id`
      AND `hn_on_ss`.`hierarchy_type` = 'SUB_SYSTEM'
      AND `hn_on_ss`.`deleted` = FALSE
--  ISO 子系统维度 父级 层级表
    LEFT JOIN `hierarchy_node` `parent_hn_on_ss`
      ON `parent_hn_on_ss`.`project_id` = `pn`.`project_id`
      AND `parent_hn_on_ss`.`id` = `hn_on_ss`.`parent_id`
      AND `parent_hn_on_ss`.`hierarchy_type` = 'SUB_SYSTEM'
      AND `parent_hn_on_ss`.`deleted` = FALSE
--  ISO 子系统维度 父级 节点表
    LEFT JOIN `project_node` `parent_pn_on_ss`
      ON `parent_pn_on_ss`.`id` = `parent_hn_on_ss`.`node_id`
      AND `parent_pn_on_ss`.`project_id` = `parent_hn_on_ss`.`project_id`
      AND `parent_pn_on_ss`.`deleted` = FALSE
-- ISO 管线维度 模块 层级表(原逻辑)
--     LEFT JOIN `hierarchy_node` `module_hn_on_piping`
--       ON `module_hn_on_piping`.`path` LIKE CONCAT(`hn_on_piping`.`path`,`hn_on_piping`.`id`,'%')
--         AND `module_hn_on_piping`.`project_id` = `hn_on_piping`.`project_id`
--         AND `module_hn_on_piping`.`hierarchy_type` = "PIPING"
--         AND `module_hn_on_piping`.`deleted` = FALSE
-- ISO 管线维度 模块 层级表
    LEFT JOIN `hierarchy_node` `module_hn_on_piping`
      ON `parent_hn_on_piping`.`id`=module_hn_on_piping.id
        AND `module_hn_on_piping`.`project_id` = `hn_on_piping`.`project_id`
        AND `module_hn_on_piping`.`hierarchy_type` = "PIPING"
        AND `module_hn_on_piping`.`deleted` = FALSE
-- ISO 管线维度 模块 节点表
    LEFT JOIN `project_node` `module_pn`
      ON `module_pn`.`id` = `module_hn_on_piping`.`node_id`
        AND `module_pn`.`project_id` = `module_hn_on_piping`.`project_id`
        AND `module_pn`.`node_type` = 'MODULE'
        AND `module_pn`.`deleted` = FALSE
-- ISO 管线维度 模块 层级表	(layer)
    LEFT JOIN `hierarchy_node` `module_hn_on_piping_layer`
      ON `parent_hn_on_piping`.path = CONCAT(`module_hn_on_piping_layer`.path,`module_hn_on_piping_layer`.`id`,'/')
        AND `module_hn_on_piping_layer`.`project_id` = `hn_on_piping`.`project_id`
        AND `module_hn_on_piping_layer`.`hierarchy_type` = "PIPING"
        AND `module_hn_on_piping_layer`.`deleted` = FALSE
-- ISO 管线维度 模块 节点表	(layer)
    LEFT JOIN `project_node` `module_pn_layer`
      ON `module_pn_layer`.`id` = `module_hn_on_piping_layer`.`node_id`
        AND `module_pn_layer`.`project_id` = `module_hn_on_piping_layer`.`project_id`
        AND `module_pn_layer`.`node_type` = 'MODULE'
        AND `module_pn_layer`.`deleted` = FALSE
;
