USE `ose_notifications`;

CREATE OR REPLACE VIEW `user_notification` AS
SELECT
	`n`.`id` AS `id`,
	`l`.`org_id` AS `org_id`,
	`l`.`project_id` AS `project_id`,
	`b`.`type` AS `type`,
	`b`.`announcement` AS `announcement`,
	`n`.`sent_at` AS `sent_at`,
	`n`.`title` AS `title`,
	`b`.`created_by` AS `created_by`,
	`b`.`creator_name` AS `creator_name`,
	`l`.`user_id` AS `user_id`,
	`l`.`email_sent_at` AS `email_sent_at`,
	`l`.`sms_sent_at` AS `sms_sent_at`,
	`l`.`read_by_user` AS `read_by_user`
FROM
	(
	(
	`logs` `l`
	JOIN `notifications` `n` ON ( ( `n`.`id` = `l`.`notification_id` ) )
	)
	JOIN `batches` `b` ON ( ( `b`.`id` = `l`.`batch_id` ) )
	)
;

