USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 实体类型视图
-- entity_types
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `entity_types` AS
  SELECT
    CASE
      WHEN ISNULL(`st`.`name_en`) OR `st`.`name_en` = `t`.`name_en` THEN `t`.`name_en`
      ELSE CONCAT(`t`.`name_en`,'/',`st`.`name_en`)
    END                 AS `id`,
    `t`.`org_id`        AS `org_id`,
    `t`.`project_id`    AS `project_id`,
    `t`.`id`            AS `entity_type_id`,
    `t`.`name_en`       AS `entity_type`,
    `st`.`id`           AS `entity_sub_type_id`,
    `st`.`name_en`      AS `entity_sub_type`
  FROM
    `bpm_entity_type`       AS `t`
    LEFT JOIN `bpm_entity_sub_type` AS`st`
      ON `st`.`entity_type_id` = `t`.`id`
      AND `t`.`type` = 'READONLY'
      AND `st`.`status` = 'ACTIVE'
  WHERE
    `t`.`status` = 'ACTIVE'
  ORDER BY
    `t`.`id` ASC,
    `st`.`id` ASC
;
