USE `saint_whale_tasks`;
-- -----------------------------------------------------------------------------
-- 实体-工序视图
-- entity_processes
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `entity_processes` AS
  SELECT
    CASE
      WHEN ISNULL(`p`.`name_en`) THEN `s`.`name_en`
      ELSE CONCAT(`s`.`name_en`,'/',`p`.`name_en`)
    END               AS `id`,
    `s`.`org_id`      AS `org_id`,
    `s`.`project_id`  AS `project_id`,
    `s`.`id`          AS `stage_id`,
    `s`.`name_en`     AS `stage_name`,
    `p`.`id`          AS `process_id`,
    `p`.`name_en`     AS `process_name`
  FROM
    `bpm_process_stage`      AS `s`
    LEFT JOIN `bpm_process` AS `p`
      ON  `p`.`process_stage_id` = `s`.`id`
      AND `p`.`status` = 'ACTIVE'
  WHERE
    `s`.`status` = 'ACTIVE'
  ORDER BY
  `s`.`id` ASC,
  `p`.`id` ASC
;
