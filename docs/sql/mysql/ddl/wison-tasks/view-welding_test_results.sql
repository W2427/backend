USE `saint_whale_tasks`;

-- -----------------------------------------------------------------------------
-- 焊接历史日志
-- View structure for welding_test_result
-- -----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `welding_test_result` AS
  SELECT
    `ew`.`id` AS `id`,
    `ew`.`org_id` AS `org_id`,
    `ew`.`project_id` AS `project_id`,
    `ew`.`no` AS `iso_no`,
    `ew`.`shop_field` AS `shop_field`,
    `ew`.`material1` AS `base_metal1`,
    `ew`.`material2` AS `base_metal2`,
    `wl`.`wps_no` AS `wps_no`,
    `ew`.`weld_type` AS `weld_type`,
    `wl`.`welder_id` AS `welder_id`,
    `pwht`.`test_result` AS `pwht_result`,
    `pwht`.`comment` AS `pwht_comment`,
    `pwht`.`id` AS `pwht_id`,
    `hd`.`test_result` AS `hd_result`,
    `hd`.`comment` AS `hd_comment`,
    `hd`.`id` AS `hd_id`,
    `rt`.`test_result` AS `rt_result`,
    `rt`.`comment` AS `rt_comment`,
    `rt`.`id` AS `rt_id`,
    `ut`.`test_result` AS `ut_result`,
    `ut`.`comment` AS `ut_comment`,
    `ut`.`id` AS `ut_id`,
    `mt`.`test_result` AS `mt_result`,
    `mt`.`comment` AS `mt_comment`,
    `mt`.`id` AS `mt_id`,
    `pt`.`test_result` AS `pt_result`,
    `pt`.`comment` AS `pt_comment`,
    `pt`.`id` AS `pt_id`,
    `pmi`.`test_result` AS `pmi_result`,
    `pmi`.`comment` AS `pmi_comment`,
    `pmi`.`id` AS `pmi_id`,
    `bevel`.`test_result` AS `groove_grinding_result`,
    `bevel`.`comment` AS `groove_grinding_comment`,
    `bevel`.`id` AS `groove_grinding_id`,
    `fitup`.`test_result` AS `pair_result`,
    `fitup`.`comment` AS `pair_comment`,
    `fitup`.`id` AS `pair_id`,
    `vt`.`test_result` AS `welding_result`,
    `vt`.`id` AS `welding_id`,
    `vt`.`comment` AS `welding_comment`,
    `tu`.`id` AS `touch_up_id`,
    `tu`.`test_result` AS `paint_repair_result`,
    `tu`.`comment` AS `paint_repair_comment`
  FROM
    `entity_weld` AS `ew`
    LEFT JOIN `weld_log` AS `wl`
      ON `ew`.`id` = `wl`.`entity_id`

    LEFT JOIN `welding_hd_logs` AS `hd`
      ON `wl`.`hd_id` = `hd`.`id`

    LEFT JOIN `welding_ndt_logs` AS `rt`
      ON `wl`.`rt_id` = `ndt`.`id` AND `rt`.`ndt_type` = 'RT'
    LEFT JOIN `welding_ndt_logs` AS `ut`
      ON `wl`.`ut_id` = `ndt`.`id` AND `ut`.`ndt_type` = 'UT'
    LEFT JOIN `welding_ndt_logs` AS `mt`
      ON `wl`.`mt_id` = `ndt`.`id` AND `mt`.`ndt_type` = 'MT'
    LEFT JOIN `welding_ndt_logs` AS `pt`
      ON `wl`.`pt_id` = `ndt`.`id` AND `pt`.`ndt_type` = 'PT'
    LEFT JOIN `welding_ndt_logs` AS `ndt`

    LEFT JOIN `welding_pmi_logs` AS `pmi`
      ON `wl`.`pmi_id` = `pmi`.`id`

    LEFT JOIN `welding_pwht_logs` AS `pwht`
      ON `wl`.`pwht_id` = `pwht`.`id`

    LEFT JOIN `welding_vt_logs` AS `welding`
      ON `welding`.`id` = `wl`.`vt_id`

    LEFT JOIN `welding_bevel_logs` AS `bevel`
      ON `bevel`.`id` = `wl`.`bevel_id`

    LEFT JOIN `welding_fitup_logs` AS `fitup`
      ON `fitup`.`id` = `wl`.`fitup_id`

    LEFT JOIN `welding_touch_up_logs` AS `tu`
      ON `tu`.`id` = `wl`.`touch_up_id`

    LEFT JOIN `weld_history` AS `weldhis`
      ON `ew`.`id` = `weldhis`.`entity_id`

    where `ew`.`deleted` = FALSE
;
