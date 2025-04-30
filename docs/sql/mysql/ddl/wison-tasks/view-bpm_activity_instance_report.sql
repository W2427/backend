USE `saint_whale_tasks`;
-- ------------------------------
-- bpm_activity_instance_report
-- bpm_activity_instance 报告视图
-- ------------------------------
CREATE OR REPLACE VIEW `bpm_activity_instance_report` AS

  SELECT
    `base`.`id`         AS  `id`,
    `base`.`project_id` AS `project_id`,
    `base`.`org_id` AS `org_id`,
    `base`.`status` AS `status`,
    `base`.`act_inst_id` AS `act_inst_id`,
    `base`.`version` AS `version`,
    `base`.`entity_no` AS `entity_no`,
    `base`.`drawing_title` AS `drawing_title`,
    `base`.`entity_type` AS `entity_type`,
    `base`.`entity_id` AS `entity_id`,
--     `base`.`task_def_key` AS `task_def_key`,
    `base`.`entity_module_name` AS `entity_module_name`,
    `base`.`entity_module_project_node_id` AS `entity_module_project_node_id`,
    `base`.`entity_category_type_name_cn` AS `entity_category_type`,
    `base`.`entity_category_type_id` AS `entity_category_type_id`,
    `base`.`entity_sub_type` AS `entity_sub_type`,
    `base`.`entity_project_node_id` AS  `entity_project_node_id`,
    `base`.`entity_sub_type_id` AS `entity_sub_type_id`,
    `base`.`process_stage_name_cn` AS `process_stage`,
    `base`.`process_stage_id` AS `process_stage_id`,
    `base`.`a.process_name` AS `process`,
    `base`.`process_id` AS `process_id`,
    `base`.`act_category` AS `act_category`,
    `base`.`process_category_id` AS `process_category_id`,
    `base`.`pipe_class` AS `pipe_class`,
    `base`.`nde_type` AS `nde_type`,
    `base`.`material_group_code` AS `material_group_code`,
    `blob`.`report_sub_type_info`       AS `report_sub_type_info`,
    `state`.`current_executor`          AS `current_executor`,
    `state`.`weld_repair_count`         AS `weld_repair_count`,
    `state`.`work_site_name`            AS `work_site_name`,
    `state`.`wps_no`                    AS `wps_no`,
    `state`.`suspension_state`          AS `suspension_state`,
    `state`.`finish_state`              AS `finish_state`

  FROM
    `bpm_activity_instance_base`        `base`
    LEFT JOIN `bpm_activity_instance_state`  `state`
      ON `base`.`id` = `state`.`bai_id`

    LEFT JOIN `bpm_activity_instance_blob` `blob`
      ON `base`.`id` = `blob`.`bai_id`
;
