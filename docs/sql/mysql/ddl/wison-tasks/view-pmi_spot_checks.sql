USE `saint_whale_tasks`;
-- ----------------------------
-- View structure for pmi_spot_check
-- PMI抽检
-- ----------------------------
CREATE OR REPLACE VIEW `pmi_spot_check` AS
  SELECT
    max(`w`.`id`) AS `id`,
    `e`.`project_id` AS `project_id`,
    `w`.`iso_no` AS `iso_no`,
    `e`.`stage` AS `stage`,
    `w`.`pmi_ratio` AS `pmi_ratio`,
    count(0) AS `total_quantity`,
    sum(if((`w`.`pmi_execute_flag` = 'EXECUTE'),1,0)) AS `finished_quantity`,
    `e`.`process` AS `process`,
    sum(if(isnull(`w`.`pmi_execute_flag`),1,0)) AS `undecided_pmi_quantity`
  FROM
    `wbs_entry` `e`
    JOIN `entity_weld` `w`
      ON `w`.`id` = `e`.`entity_id`
      AND `w`.`project_id` = `e`.`project_id`
    WHERE
      `e`.`type` = 'ENTITY'
      AND `e`.`entity_type` = 'WELD_JOINT'
      AND `e`.`process` = 'PMI'
      AND `w`.`deleted` = FALSE
    GROUP BY
      `e`.`project_id`,
      `w`.`iso_no`,
      `e`.`stage`,
      `e`.`process`,
      `w`.`pmi_ratio`
;
