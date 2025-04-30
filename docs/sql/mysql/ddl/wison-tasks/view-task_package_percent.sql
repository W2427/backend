USE `saint_whale_tasks`;

-- ----------------------------
-- View structure for task_package_percent
-- 任务包完成%
-- ----------------------------
CREATE OR REPLACE VIEW `task_package_percent` AS
 SELECT
    `tp`.`id`                   AS `id`,
    `tp`.`created_at`           AS `created_at`,
    `tp`.`last_modified_at`     AS `last_modified_at`,
    `tp`.`status`               AS `status`,
    `tp`.`created_by`           AS `created_by`,
    `tp`.`deleted`              AS `deleted`,
    `tp`.`deleted_at`           AS `deleted_at`,
    `tp`.`deleted_by`           AS `deleted_by`,
    `tp`.`last_modified_by`     AS `last_modified_by`,
    `tp`.`version`              AS `version`,
    `tp`.`memo`                 AS `memo`,
    `tp`.`name`                 AS `name`,
    `tp`.`org_id`               AS `org_id`,
    `tp`.`project_id`           AS `project_id`,
    `tp`.`sort_order`           AS `sort_order`,
    `tp`.`category_id`          AS `category_id`,
    `tp`.`finished_work_load`   AS `finished_work_load`,
    `tp`.`total_work_load`       			AS `total_work_load`,
    CASE
      WHEN `tp`.`total_work_load` = 0 THEN 0
      ELSE `tp`.`finished_work_load` / `tp`.`total_work_load` END AS `percent_work_load`,
    `tp`.`finished_count`       AS `finished_count`,
    `tp`.`total_count`          AS `total_count`,
    CASE
      WHEN `tp`.`total_count` = 0 THEN 0
      ELSE `tp`.`finished_count` / `tp`.`total_count`   END AS `percent_count`
  FROM
    `task_package` `tp`

  WHERE `tp`.`deleted` = 0
;
