USE `ose_tasks`;
DELETE FROM
  `wbs_entry_relation`
WHERE
  `id` IN (
    SELECT
      `erps`.`id`
    FROM
      (
        SELECT
          `er`.`id`,
          `er`.`project_id`,
          `pe`.`id` AS `predecessor_id`,
          `pe`.`guid` AS `predecessor_guid`,
          `se`.`id` AS `successor_id`,
          `se`.`guid` AS `successor_guid`
        FROM
          `wbs_entry_relation` AS `er`
          LEFT OUTER JOIN `wbs_entry` AS `pe`
            ON `pe`.`project_id` = `er`.`project_id`
            AND`pe`.`guid` = `er`.`predecessor_id`
          LEFT OUTER JOIN `wbs_entry` AS `se`
            ON `se`.`project_id` = `er`.`project_id`
            AND `se`.`guid` = `er`.`successor_id`
      ) AS `erps`
    WHERE
      `predecessor_id` IS NULL
      OR `successor_id` IS NULL
  )
;
