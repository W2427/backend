/* 用户的通知视图 */
DROP VIEW IF EXISTS `ose_notifications`.`user_notifications`;
CREATE VIEW `ose_notifications`.`user_notifications` AS
  SELECT
    `n`.`id`,
    `l`.`org_id`,
    `l`.`project_id`,
    `b`.`type`,
    `b`.`announcement`,
    `n`.`sent_at`,
    `n`.`title`,
    `b`.`created_by`,
    `b`.`creator_name`,
    `l`.`user_id`,
    `l`.`email_sent_at`,
    `l`.`sms_sent_at`,
    `l`.`read_by_user`
  FROM
    `ose_notifications`.`logs` AS `l`
    INNER JOIN `ose_notifications`.`notifications` AS `n`
      ON `n`.`id` = `l`.`notification_id`
    INNER JOIN `ose_notifications`.`batches` AS `b`
      ON `b`.`id` = `l`.`batch_id`
;

/* 通知批次统计视图 */
DROP VIEW IF EXISTS `ose_notifications`.`batch_summary`;
CREATE VIEW `ose_notifications`.`batch_summary` AS
  SELECT
    `b`.`id`,
    `b`.`org_id`,
    `b`.`project_id`,
    `b`.`type`,
    `b`.`announcement`,
    `b`.`created_at`,
    `b`.`created_by`,
    `b`.`creator_name`,
    `b`.`total_count`,
    COUNT(IF(`l`.`email_send_status` = 'PENDING', TRUE, NULL)) AS `email_pending_count`,
    COUNT(IF(`l`.`email_send_status` = 'SENDING', TRUE, NULL)) AS `email_sending_count`,
    COUNT(IF(`l`.`email_send_status` = 'SUCCESSFUL', TRUE, NULL)) AS `email_sent_count`,
    COUNT(IF(`l`.`email_send_status` = 'FAILED', TRUE, NULL)) AS `email_failed_count`,
    COUNT(IF(`l`.`sms_send_status` = 'PENDING', TRUE, NULL)) AS `sms_pending_count`,
    COUNT(IF(`l`.`sms_send_status` = 'SENDING', TRUE, NULL)) AS `sms_sending_count`,
    COUNT(IF(`l`.`sms_send_status` = 'SUCCESSFUL', TRUE, NULL)) AS `sms_sent_count`,
    COUNT(IF(`l`.`sms_send_status` = 'FAILED', TRUE, NULL)) AS `sms_failed_count`
  FROM
    `ose_notifications`.`batches` AS `b`
    LEFT OUTER JOIN `ose_notifications`.`logs` AS `l`
      ON `l`.`batch_id` = `b`.`id`
  GROUP BY
    `b`.`id`
;
