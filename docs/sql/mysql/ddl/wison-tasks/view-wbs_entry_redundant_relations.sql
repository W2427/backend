USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 四级计划冗余关系视图
-- wbs_entry_redundant_relation
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `wbs_entry_redundant_relation` AS
  SELECT
    `rr`.`id`,
    `ppr`.`project_id`,
    `ppr`.`predecessor_id` AS `predecessor_id`,
    `pr`.`predecessor_id` AS `connector_id`,
    `pr`.`successor_id`   AS `successor_id`
  FROM
    -- 冗余的前置任务关系
    `wbs_entry_relation` AS `rr`
    -- 前置任务的前置任务关系
    INNER JOIN `wbs_entry_relation` AS `ppr`
      ON `ppr`.`project_id` = `rr`.`project_id`
      AND `ppr`.`predecessor_id` = `rr`.`predecessor_id`
      AND `ppr`.`optional` = 0
    -- 前置任务关系
    INNER JOIN `wbs_entry_relation` AS `pr`
      ON `pr`.`project_id` = `rr`.`project_id`
      AND `pr`.`successor_id` = `rr`.`successor_id`
      AND `pr`.`predecessor_id` = `ppr`.`successor_id`
;
