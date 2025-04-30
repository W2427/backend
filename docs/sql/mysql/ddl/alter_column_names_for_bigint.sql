ALTER TABLE `ose_report`.`report_checklists`
CHANGE COLUMN `header_template` `header_template_id` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `id`,
CHANGE COLUMN `signature_template` `signature_template_id` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `serial`;

ALTER TABLE `ose_tasks`.`bpm_external_inspection_confirm`
CHANGE COLUMN `already_upload` `is_uploaded` bit(1) NOT NULL AFTER `status`,
CHANGE COLUMN `reports` `report` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL AFTER `report_no`,
CHANGE COLUMN `second_upload` `is_second_upload` bit(1) NOT NULL AFTER `report`;

ALTER TABLE `ose_tasks`.`bpm_external_inspection_mail_application`
CHANGE COLUMN `coordinate_catgory` `coordinate_category` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `comment`,
CHANGE COLUMN `report_party` `inspect_party` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `attachments`;


ALTER TABLE `ose_tasks`.`bpm_external_inspection_mail_application_detail`
CHANGE COLUMN `report_party` `inspect_party` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `series_nos`;

ALTER TABLE `ose_tasks`.`bpm_external_inspection_schedule`
CHANGE COLUMN `coordinate_catgory` `coordinate_category` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `act_task_ids`;

ALTER TABLE `ose_tasks`.`bpm_external_inspection_schedule_detail`
CHANGE COLUMN `report_party` `inspect_party` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `operator`;

ALTER TABLE `ose_tasks`.`bpm_external_inspection_upload_history`
CHANGE COLUMN `operator_id` `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `file_path`,
CHANGE COLUMN `report_name` `file_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;

ALTER TABLE `ose_tasks`.`bpm_plan_execution_history`
CHANGE COLUMN `operator_id` `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `execution_state`;

ALTER TABLE `ose_tasks`.`bpm_process`
CHANGE COLUMN `one_off_report` `is_one_off_report` bit(1) NOT NULL DEFAULT b'0' AFTER `insp_application_class`;

ALTER TABLE `ose_tasks`.`drawing`
CHANGE COLUMN `operater` `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `drawing_category_id`;

ALTER TABLE `ose_tasks`.`drawing_history`
CHANGE COLUMN `operater` `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `qr_code`;

ALTER TABLE `ose_tasks`.`drawing_upload_zip_file_history`
CHANGE COLUMN `operater` `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `success_count`;

ALTER TABLE `ose_tasks`.`iso_process_log`
CHANGE COLUMN `report_id` `report_qr_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;

ALTER TABLE `ose_tasks`.`qc_report`
CHANGE COLUMN `inspection_party` `inspect_parties` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `excel_report_file_id`,
CHANGE COLUMN `opeator` `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `nde_type`,
CHANGE COLUMN `opeator_name` `operator_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `upload_report_page_count`;

ALTER TABLE `ose_tasks`.`sub_drawing`
CHANGE COLUMN `operater` `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `sub_no`,
CHANGE COLUMN `issue_flag` `is_issued` bit(1) NOT NULL AFTER `mark_deleted`;

ALTER TABLE `ose_tasks`.`sub_drawing_history`
CHANGE COLUMN `operater` `operator` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `qr_code`;

ALTER TABLE `ose_tasks`.`welding_groove_grinding_logs`
CHANGE COLUMN `report_id` `report_qr_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;

ALTER TABLE `ose_tasks`.`welding_hardness_testing_logs`
CHANGE COLUMN `report_id` `report_qr_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;

ALTER TABLE `ose_tasks`.`welding_logs`
CHANGE COLUMN `report_id` `report_qr_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;

ALTER TABLE `ose_tasks`.`welding_ndt_logs`
CHANGE COLUMN `report_id` `report_qr_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;

ALTER TABLE `ose_tasks`.`welding_paint_repair_logs`
CHANGE COLUMN `report_id` `report_qr_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;

ALTER TABLE `ose_tasks`.`welding_pair_logs`
CHANGE COLUMN `report_id` `report_qr_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;

ALTER TABLE `ose_tasks`.`welding_pmi_logs`
CHANGE COLUMN `report_id` `report_qr_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;

ALTER TABLE `ose_tasks`.`welding_pwht_logs`
CHANGE COLUMN `report_id` `report_qr_code` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL AFTER `project_id`;
