USE `saint_whale_tasks`;
-- -----------------------------------------------------------------------------
-- NDT 抽检
-- View structure for ndt_spot_check
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `ndt_spot_check` AS
  SELECT
    max(`w`.`id`) AS `id`,
    `e`.`project_id` AS `project_id`,
    `w`.`iso_no` AS `iso_no`,
    `e`.`stage` AS `stage`,
    `w`.`nde_ratio` AS `nde_ratio`,
    `w`.`nde` AS `nde`,
    count(0) AS `total_quantity`,
    sum(if((`w`.`ndt_execute_flag` = 1),1,0)) AS `finished_quantity`,
    `e`.`process` AS `process`,
    sum(if(isnull(`w`.`ndt_execute_flag`),1,0)) AS `undecided_ndt_quantity`
  FROM
    `wbs_entry` `e`
    JOIN `entity_weld` `w`
      ON `w`.`id` = `e`.`entity_id`
      AND `w`.`project_id` = `e`.`project_id`
  WHERE
    `e`.`type` = 'ENTITY'
    AND `e`.`entity_type` = 'WELD_JOINT'
    AND `e`.`process` = 'NDT'
    AND `w`.`deleted` = FALSE
  GROUP BY
    `e`.`project_id`,
    `w`.`iso_no`,
    `e`.`stage`,
    `e`.`process`,
    `w`.`nde`,
    `w`.`nde_ratio`
;

