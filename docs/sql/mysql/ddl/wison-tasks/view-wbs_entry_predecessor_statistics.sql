USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 四级计划前置任务统计信息视图
-- wbs_entry_predecessor_statistics
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `wbs_entry_predecessor_statistics` AS
  SELECT
    `s`.`id`,
    `s`.`project_id`,
    `s`.`entity_type`,
    GROUP_CONCAT(DISTINCT `p`.`entity_type`) AS `predecessor_entity_types`,
    COUNT(0) `total`,
    SUM(IF(`wes`.`finished` = 0, 1, 0)) `not_finished`,
    SUM(IF(`wes`.`running_status` IS NULL OR `wes`.`running_status` <> 'APPROVED', 1, 0)) `not_approved`
  FROM
    `wbs_entry` AS `s`
    INNER JOIN `wbs_entry_relation` AS `r`
      ON `r`.`project_id` = `s`.`project_id`
      AND `r`.`successor_id` = `s`.`guid`
      AND `r`.`deleted` = 0
      AND (`r`.`optional` IS NULL OR `r`.`optional` = 0)
    INNER JOIN `wbs_entry` AS `p`
      ON `p`.`project_id` = `r`.`project_id`
      AND `p`.`guid` = `r`.`predecessor_id`
      AND `p`.`deleted` = 0
      AND `p`.`active` = 1
			INNER JOIN `wbs_entry_state` AS wes
			ON p.id= wes.wbs_entry_id
  WHERE
    `s`.`deleted` = 0
  GROUP BY
    `s`.`id`
;

