USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 工序-实体类型视图
-- View structure for process_entity_types
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `process_entity_types` AS
  SELECT
    CASE
      WHEN ISNULL(`st`.`name_en`) or `st`.`name_en` = `t`.`name_en` THEN concat(`s`.`name_en`,'/',`p`.`name_en`,'/',`t`.`name_en`)
      ELSE concat(`s`.`name_en`,'/',`p`.`name_en`,'/',`t`.`name_en`,'/',`st`.`name_en`)
    END AS `id`,
    `r`.`org_id`      AS `org_id`,
    `r`.`project_id`  AS `project_id`,
    `s`.`id`          AS `stage_id`,
    `s`.`name_en`     AS `stage_name`,
    `p`.`id`          AS `process_id`,
    `p`.`name_en`     AS `process_name`,
    `t`.`id`          AS `entity_type_id`,
    `t`.`name_en`     AS `entity_type`,
    `st`.`id`         AS `entity_sub_type_id`,
    `st`.`name_en`    AS `entity_sub_type`
  FROM
    `bpm_entity_type_process_relation` AS `r`
    JOIN `bpm_process` `p`
      ON `p`.`id` = `r`.`process_id`
      AND `p`.`status` = 'ACTIVE'

    JOIN `bpm_process_stage` AS `s`
      ON `s`.`id` = `p`.`process_stage_id`
      AND `s`.`status` = 'ACTIVE'

    JOIN `bpm_entity_sub_type` AS `st`
      ON `st`.`id` = `r`.`entity_sub_type_id`
      AND `st`.`status` = 'ACTIVE'

    JOIN `bpm_entity_type` AS `t`
      ON `t`.`id` = `st`.`entity_type_id`
      AND `t`.`type` = 'READONLY'
      AND `t`.`status` = 'ACTIVE'

    WHERE
      `r`.`status` = 'ACTIVE'
    ORDER BY
      `s`.`id`  ASC,
      `p`.`id`  ASC,
      `t`.`id`  ASC,
      `st`.`id` ASC
;
