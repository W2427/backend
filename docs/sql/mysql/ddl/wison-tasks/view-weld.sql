USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `weld` AS
SELECT
	`ew`.`id`                             AS `id`,
	`ew`.`org_id`                         AS `org_id`,
	`ew`.`project_id`                     AS `project_id`,
	`ew`.`no`                             AS `iso_no`,
	`ew`.`shop_field`                     AS `shop_field`,
	`ew`.`material1`                      AS `base_metal1`,
	`ew`.`material2`                      AS `base_metal2`,
	`ew`.`wps_no`                         AS `wps_no`,
	`ew`.`weld_type`                      AS `weld_type`,
	ifnull(`weldhis`.`welder_id`, '')     AS `welder_id`,
	`pwht`.`test_result`                  AS `pwht_result`,
	`pwht`.`comment`                      AS `pwht_comment`,
	`pwht`.`id`                           AS `pwht_id`,
	`hd`.`test_result`                    AS `hd_result`,
	`hd`.`comment`                        AS `hd_comment`,
	`hd`.`id`                             AS `hd_id`,
	`ndt`.`test_result`                   AS `ndt_result`,
	`ndt`.`comment`                       AS `ndt_comment`,
	`ndt`.`id`                            AS `ndt_id`,
	`pmi`.`test_result`                   AS `pmi_result`,
	`pmi`.`comment`                       AS `pmi_comment`,
	`pmi`.`id`                            AS `pmi_id`,
	`gg`.`test_result`                    AS `groove_grinding_result`,
	`gg`.`comment`                        AS `groove_grinding_comment`,
	`gg`.`id`                             AS `groove_grinding_id`,
	`pair`.`test_result`                  AS `pair_result`,
	`pair`.`comment`                      AS `pair_comment`,
	`pair`.`id`                           AS `pair_id`,
	`welding`.`test_result`               AS `welding_result`,
	`welding`.`id`                        AS `welding_id`,
	`welding`.`comment`                   AS `welding_comment`,
	`pr`.`id`                             AS `paint_repair_id`,
	`pr`.`test_result`                    AS `paint_repair_result`,
	`pr`.`comment`                        AS `paint_repair_comment`
FROM
	`entity_weld` `ew`

	LEFT JOIN `welding_hardness_testing_logs` `hd`  ON   `ew`.`hd_id` = `hd`.`id`

	LEFT JOIN `welding_ndt_logs` `ndt`              ON   `ew`.`ndt_id` = `ndt`.`id`

	LEFT JOIN `welding_pmi_logs` `pmi`              ON   `ew`.`pmi_id` = `pmi`.`id`

	LEFT JOIN `welding_pwht_logs` `pwht`            ON   `ew`.`pwht_id` = `pwht`.`id`

	LEFT JOIN `welding_logs` `welding`              ON   `welding`.`id` = `ew`.`welding_id`

	LEFT JOIN `welding_groove_grinding_logs` `gg`   ON   `gg`.`id` = `ew`.`groove_grinding_id`

	LEFT JOIN `welding_pair_logs` `pair`            ON   `pair`.`id` = `ew`.`pair_id`

	LEFT JOIN `welding_paint_repair_logs` `pr`      ON   `pr`.`id` = `ew`.`paint_repair_id`

	LEFT JOIN `weld_history` `weldhis`              ON   `ew`.`id` = `weldhis`.`entity_id`

WHERE
	 `ew`.`deleted` = FALSE
;

