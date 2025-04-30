USE `saint_whale_tasks`;
-- -----------------------------------------------------------------------------
-- 外检申请视图
-- View structure for view_external_inspection_apply
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `view_external_inspection_apply` AS
SELECT
	`bai`.`org_id` AS `orgId`,
	`bai`.`project_id` AS `projectId`,
	`bai`.`act_inst_id` AS `actInstId`,
	`bai`.`entity_no` AS `entityNo`,
	`brt`.`create_time_` AS `createTime`,
	`brt`.`assignee_` AS `assignee`,
	`bai`.`process_stage_name_cn` AS `process_stage_name_en`,
	`bai`.`a.process_name` AS `process_name_en`,
	`bai`.`entity_module_name` AS `entityModuleName`,
	CONCAT(`bai`.`process_stage_name_cn`, '-', `bai`.`a.process_name`) AS `processStageName`,
	`bai`.`entity_sub_type` AS `entityCategoryNameEn`,
	`bai`.`entity_sub_type_id` AS `entitySubTypeId`,
	`brt`.`task_id` AS `task_id`,
	`bai`.`inspect_parties` AS `inspect_parties`

 FROM `bpm_activity_instance_base` `bai`,
	bpm_ru_task brt

WHERE  `brt`.`act_inst_id` = `bai`.`act_inst_id`

	AND `bai`.`status` = 'ACTIVE'
;
