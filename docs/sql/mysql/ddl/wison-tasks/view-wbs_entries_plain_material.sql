USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `wbs_entries_plain_dwg` AS
SELECT
	`we`.`id`                       AS `id`,
	`we`.`created_at`               AS `created_at`,
	`we`.`last_modified_at`         AS `last_modified_at`,
	`we`.`status`                   AS `status`,
	`we`.`created_by`               AS `created_by`,
	`we`.`deleted`                  AS `deleted`,
	`we`.`deleted_at`               AS `deleted_at`,
	`we`.`deleted_by`               AS `deleted_by`,
	`we`.`last_modified_by`         AS `last_modified_by`,
	`we`.`version`                  AS `version`,
	`we`.`active`                   AS `active`,
	`we`.`company_id`               AS `company_id`,
	`we`.`depth`                    AS `depth`,
	`we`.`duration`                 AS `duration`,
	`we`.`duration_unit`            AS `duration_unit`,
	`we`.`finish_at`                AS `finish_at`,
	`we`.`guid`                     AS `guid`,
	`we`.`name`                     AS `name`,
	`we`.`no`                       AS `no`,
	`we`.`org_id`                   AS `org_id`,
	`we`.`parent_id`                AS `parent_id`,
-- 	`we`.`path`                     AS `path`,
	`we`.`progress`                 AS `progress`,
	`we`.`project_id`               AS `project_id`,
	`we`.`sort`                     AS `sort`,
	`we`.`start_at`                 AS `start_at`,
	`we`.`type`                     AS `type`,
	`we`.`wbs`                      AS `wbs`,
	`we`.`entity_id`                AS `entity_id`,
	`we`.`process`                  AS `process`,
	`we`.`sector`                   AS `sector`,
	`we`.`stage`                    AS `stage`,
	`we`.`entity_sub_type`          AS `entity_sub_type`,
	`we`.`entity_type`              AS `entity_type`,
	`we`.`module_type`              AS `module_type`,
	`we`.`project_node_id`          AS `project_node_id`,
	`we`.`proportion`               AS `proportion`,
	`we`.`team_id`                  AS `team_id`,
	`we`.`hierarchy_node_id`        AS `hierarchy_node_id`,
	`we`.`parent_hierarchy_node_id` AS `parent_hierarchy_node_id`,
	`we`.`random_no`                AS `random_no`,
	`we`.`team_path`                AS `team_path`,
	`we`.`finished`                 AS `finished`,
	`we`.`finished_score`           AS `finished_score`,
	`we`.`total_score`              AS `total_score`,
	`we`.`started_at`               AS `started_at`,
	`we`.`started_by`               AS `started_by`,
	`we`.`module_hierarchy_node_id` AS `module_hierarchy_node_id`,
	`we`.`process_id`               AS `process_id`,
	`we`.`process_instance_id`      AS `process_instance_id`,
	`we`.`remarks`                  AS `remarks`,
	`we`.`work_site_id`             AS `work_site_id`,
	`we`.`work_site_name`           AS `work_site_name`,
	`we`.`iso_no`                   AS `iso_no`,
	`we`.`finished_at`              AS `finished_at`,
	`we`.`running_status`           AS `running_status`,
	`we`.`start_before_hours`       AS `start_before_hours`,
	`we`.`actual_man_hours`         AS `actual_man_hours`,
	`we`.`estimated_man_hours`      AS `estimated_man_hours`,
	`we`.`actual_duration`          AS `actual_duration`,
	`we`.`start_unix_time`          AS `start_unix_time`,
	`we`.`task_package_id`          AS `task_package_id`,
	`we`.`bundle_id`                AS `bundle_id`,
	`we`.`iso_entity_id`            AS `iso_entity_id`,
	`we`.`spool_entity_id`          AS `spool_entity_id`,
	`we`.`spool_no`                 AS `spool_no`,
	`we`.`layer_package`            AS `layer_package`,
	`we`.`dwg_sht_no`               AS `dwg_sht_no`,
	`we`.`work_load`                AS `work_load`,
	`we`.`wp01_id`                  AS `wp01_id`,
	`we`.`wp01_no`                  AS `wp01_no`,
	`we`.`wp02_id`                  AS `wp02_id`,
	`we`.`wp02_no`                  AS `wp02_no`,
	`we`.`wp03_id`                  AS `wp03_id`,
	`we`.`wp03_no`                  AS `wp03_no`,
	`we`.`wp04_id`                  AS `wp04_id`,
	`we`.`wp04_no`                  AS `wp04_no`,
	`we`.`discipline`               AS `discipline`,
	`ei`.`match_percent`            AS `bom_match_percent`,
	`ei`.`bom_ln_id`                AS `spm_ln_id`,
	`dsp`.`is_issued`               AS `issue_status`,
	concat( `hn`.`path`, `hn`.`id`) AS `hn_path`,
	`slp`.`ln_id`                   AS `ln_id`,
	`slp`.`tag_number`              AS `tag_number`,
	`slp`.`resv_qty`                AS `resv_qty`,
	`slp`.`quantity`                AS `quantity`,
	`slp`.`issue_qty`               AS `issue_qty`,
	`slp`.`short_desc`              AS `short_desc`,
	`slp`.`ident`                   AS `ident`,
	( `slp`.`quantity` - `slp`.`resv_qty`  - `slp`.`issue_qty` ) AS `lack_qty`
FROM
	`wbs_entry` `we`

		LEFT JOIN `entity_iso` `ei` ON  `we`.`iso_entity_id` = `ei`.`id`

		LEFT JOIN `sub_drawing` `dsp`
			ON REPLACE(REPLACE(`we`.`iso_no`, '"', '_' ),'/','_') = `dsp`.`sub_drawing_no`
			AND `dsp`.`page_no` = `we`.`dwg_sht_no`

		LEFT JOIN `hierarchy_node` `hn` ON `we`.`hierarchy_node_id` = `hn`.`id`

		LEFT JOIN `spm_list_pos` `slp` ON `slp`.`ln_id` = `ei`.`bom_ln_id`

WHERE
	`we`.`deleted` = 0
	AND `we`.`active` = 1
	AND ( `dsp`.`status` = 'ACTIVE' OR isnull( `dsp`.`status` ) )

;

