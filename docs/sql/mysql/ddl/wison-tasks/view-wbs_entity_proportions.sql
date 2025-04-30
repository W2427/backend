USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 实体工序实施结果抽检比例统计视图
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `wbs_entity_proportion` AS
  SELECT
    CONCAT(`project_id`, '/', `stage`, '/', `sector`, '/', `process`, '/', `entity_type`, ':', `proportion`) AS `id`,
    `project_id`,
    `sector`,
    `stage`,
    `process`,
    `entity_type`,
    `proportion`,
    COUNT(0) AS `total_count`,
    SUM(CASE WHEN `active` = 1 THEN 1 ELSE 0 END) AS `active_count`
  FROM
    `wbs_entry`
  WHERE
    `proportion` > 0
    AND `type` = 'ENTITY'
    AND `deleted` = 0
  GROUP BY
    `project_id`,
    `sector`,
    `stage`,
    `process`,
    `entity_type`,
    `proportion`
;

