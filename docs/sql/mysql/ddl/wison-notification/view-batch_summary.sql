USE `ose_notifications`;

CREATE OR REPLACE VIEW `batch_summary` AS
  SELECT
	`b`.`id` AS `id`,
	`b`.`org_id` AS `org_id`,
	`b`.`project_id` AS `project_id`,
	`b`.`type` AS `type`,
	`b`.`announcement` AS `announcement`,
	`b`.`created_at` AS `created_at`,
	`b`.`created_by` AS `created_by`,
	`b`.`creator_name` AS `creator_name`,
	`b`.`total_count` AS `total_count`,
	count(IF(( `l`.`email_send_status` = 'PENDING' ),TRUE,NULL)) AS `email_pending_count`,
	count(IF(( `l`.`email_send_status` = 'SENDING' ),TRUE,NULL)) AS `email_sending_count`,
	count(IF(( `l`.`email_send_status` = 'SUCCESSFUL' ),TRUE,NULL)) AS `email_sent_count`,
	count(IF(( `l`.`email_send_status` = 'FAILED' ),TRUE,NULL)) AS `email_failed_count`,
	count(IF(( `l`.`sms_send_status` = 'PENDING' ),TRUE,NULL)) AS `sms_pending_count`,
	count(IF(( `l`.`sms_send_status` = 'SENDING' ),TRUE,NULL)) AS `sms_sending_count`,
	count(IF(( `l`.`sms_send_status` = 'SUCCESSFUL' ),TRUE,NULL)) AS `sms_sent_count`,
	count(IF(( `l`.`sms_send_status` = 'FAILED' ),TRUE,NULL)) AS `sms_failed_count`
FROM
	(`batches` `b` LEFT JOIN `logs` `l` ON ( ( `l`.`batch_id` = `b`.`id` ) ))
GROUP BY
	`b`.`id`
;

