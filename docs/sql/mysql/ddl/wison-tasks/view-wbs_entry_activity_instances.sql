USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 四级计划-工作流活动实例视图
-- wbs_entry_activity_instance
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `wbs_entry_activity_instance` AS
  SELECT
    `e`.`id`,
    `a`.`project_id`,
    `g`.`path` AS `wbs_path`,
    `e`.`module_type`,
    `a`.`process_stage_id` AS `stage_id`,
    `e`.`stage` AS `stage_name`,
    `a`.`process_id`,
    `e`.`process` AS `process_name`,
    `t`.`name_` AS `task_name`,
    `e`.`entity_type`,
    `e`.`entity_sub_type`,
    `e`.`entity_id`,
    `e`.`no` AS `entity_no`,
    CONCAT(`f`.`team_path`, `f`.`team_id`, '/') AS `team_path`,
    `f`.`team_id`,
    `f`.`work_site_id`,
    `f`.`work_site_name`,
    CONCAT(',', `t`.`assignee_`, ',') AS `assignee_id`,
    CONCAT(`t`.`category_`, '.') AS `privileges`,
    `e`.`start_at` AS `plan_start_at`,
    `e`.`finish_at` AS `plan_finish_at`,
    `f`.`started_at` AS `actual_started_at`,
    `f`.`process_instance_id`,
    `f`.`finished`,
    `g`.`remarks`
  FROM
    `bpm_activity_instance_base` AS `a`
    INNER JOIN `bpm_ru_task` AS `t`
      ON `t`.`act_inst_id` = `a`.`act_inst_id`
      AND `t`.`tenant_id_` = `a`.`project_id`
    INNER JOIN `wbs_entry` AS `e`
      ON `e`.`entity_id` = `a`.`entity_id`
      AND `e`.`process_id` = `a`.`process_id`
      AND `e`.`deleted` = 0
      AND `e`.`active` = 1
    INNER JOIN `wbs_entry_state` AS `f`
      ON `e`.id = `f`.wbs_entry_id
    INNER JOIN `wbs_entry_blob` AS `g`
      ON `e`.id = `g`.wbs_entry_id
;
