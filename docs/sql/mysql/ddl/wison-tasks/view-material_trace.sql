USE `saint_whale_tasks`;

-- ----------------------------
-- View structure for material_trace
-- 材料跟踪
-- ----------------------------
CREATE OR REPLACE VIEW `material_trace` AS
  SELECT
    `temp`.`pieceNo` AS `pieceNo`,
    `temp`.`spoolNo` AS `spoolNo`,
    `temp`.`isoNo` AS `isoNo`,
    `temp`.`material` AS `material`,
    `temp`.`spec` AS `spec`,
    `temp`.`qty` AS `qty`,
    `temp`.`heatNo` AS `heatNo`,
    `temp`.`batchNo` AS `batchNo`,
    `temp`.`inspReportNo` AS `inspReportNo`,
    `temp`.`project_id` AS `project_id`,
    `temp`.`org_id` AS `org_id`
  FROM
    (
      SELECT
        group_concat(distinct `eqc`.`entity_no` separator ',') AS `pieceNo`,
        group_concat(distinct `ew`.`spool_no` separator ',') AS `spoolNo`,
        group_concat(distinct `ew`.`iso_no` separator ',') AS `isoNo`,
        group_concat(distinct `baim`.`short_desc1` separator ',') AS `material`,
        group_concat(distinct `mfmsi`.`spec` separator ',') AS `spec`,
        group_concat(distinct `eqc`.`qty` separator ',') AS `qty`,
        group_concat(distinct `mhn`.`heat_no_code` separator ',') AS `heatNo`,
        group_concat(distinct `mhn`.`batch_no_code` separator ',') AS `batchNo`,
        group_concat(distinct `mfmobir`.`fmobir_code` separator ',') AS `inspReportNo`,
        max(`ew`.`project_id`) AS `project_id`,
        max(`ew`.`org_id`) AS `org_id`

      FROM
        `bpm_act_inst_material` `baim`
        JOIN `entity_qr_code` `eqc`
          ON `baim`.`entity_qr_code1` = `eqc`.`qr_code`

        JOIN `entity_weld` `ew`
          ON `ew`.`id` = `baim`.`entity_id`

        JOIN `mat_f_item_detail` `mfid`
          ON `mfid`.`rn_qr_code` = `eqc`.`material_qr_code`

        JOIN `mat_f_material_stocktake_item` `mfmsi`
          ON `mfmsi`.`qr_code` = `mfid`.`rn_qr_code`

        JOIN `mat_f_material_open_box_inspection_report` `mfmobir`
          ON `mfmobir`.`fmst_id` = `mfmsi`.`fmst_id`

        JOIN `mat_heat_no` `mhn`
          ON `mhn`.`id` = `mfmsi`.`heat_no_id`

        GROUP BY
          `eqc`.`qr_code`,
          `ew`.`project_id`

      UNION ALL

      SELECT
        group_concat(distinct `eqc`.`entity_no` separator ',') AS `pieceNo`,
        group_concat(distinct `ew`.`spool_no` separator ',') AS `spoolNo`,
        group_concat(distinct `ew`.`iso_no` separator ',') AS `isoNo`,
        group_concat(distinct `baim`.`short_desc2` separator ',') AS `material`,
        group_concat(distinct `mfmsi`.`spec` separator ',') AS `spec`,
        group_concat(distinct `eqc`.`qty` separator ',') AS `qty`,
        group_concat(distinct `mhn`.`heat_no_code` separator ',') AS `heatNo`,
        group_concat(distinct `mhn`.`batch_no_code` separator ',') AS `batchNo`,
        group_concat(distinct `mfmobir`.`fmobir_code` separator ',') AS `inspReportNo`,
        max(`ew`.`project_id`) AS `project_id`,
        max(`ew`.`org_id`) AS `org_id`

      FROM
        `bpm_act_inst_material` `baim`
        JOIN `entity_qr_code` `eqc`
          ON `baim`.`entity_qr_code2` = `eqc`.`qr_code`

        JOIN `entity_weld` `ew`
          ON `ew`.`id` = `baim`.`entity_id`

        JOIN `mat_f_item_detail` `mfid`
          ON `mfid`.`rn_qr_code` = `eqc`.`material_qr_code`

        JOIN `mat_f_material_stocktake_item` `mfmsi`
          ON `mfmsi`.`qr_code` = `mfid`.`rn_qr_code`

        JOIN `mat_f_material_open_box_inspection_report` `mfmobir`
          ON `mfmobir`.`fmst_id` = `mfmsi`.`fmst_id`

        JOIN `mat_heat_no` `mhn`
          ON `mhn`.`id` = `mfmsi`.`heat_no_id`

        GROUP BY
          `eqc`.`qr_code`,
          `ew`.`project_id`

      UNION ALL

      SELECT
        '' AS `pieceNo`,
        group_concat(distinct `ew`.`spool_no` separator ',') AS `spoolNo`,
        group_concat(distinct `ew`.`iso_no` separator ',') AS `isoNo`,
        group_concat(distinct `baim`.`short_desc1` separator ',') AS `material`,
        group_concat(distinct `mfmsi`.`spec` separator ',') AS `spec`,
        1 AS `qty`,
        group_concat(distinct `mhn`.`heat_no_code` separator ',') AS `heatNo`,
        group_concat(distinct `mhn`.`batch_no_code` separator ',') AS `batchNo`,
        group_concat(distinct `mfmobir`.`fmobir_code` separator ',') AS `inspReportNo`,
        max(`ew`.`project_id`) AS `project_id`,
        max(`ew`.`org_id`) AS `org_id`

      FROM
        `bpm_act_inst_material` `baim`
        JOIN `entity_weld` `ew`
          ON `ew`.`id` = `baim`.`entity_id`

        JOIN `mat_f_item_detail` `mfid`
          ON `mfid`.`rn_qr_code` = `baim`.`entity_qr_code1`

        JOIN `mat_f_material_stocktake_item` `mfmsi`
          ON `mfmsi`.`qr_code` = `mfid`.`rn_qr_code`

        JOIN `mat_f_material_open_box_inspection_report` `mfmobir`
          ON `mfmobir`.`fmst_id` = `mfmsi`.`fmst_id`

        JOIN `mat_heat_no` `mhn`
          ON `mhn`.`id` = `mfmsi`.`heat_no_id`

        GROUP BY
          `baim`.`entity_qr_code1`,
          `ew`.`project_id`

      UNION ALL

      SELECT
        '' AS `pieceNo`,
        group_concat(distinct `ew`.`spool_no` separator ',') AS `spoolNo`,
        group_concat(distinct `ew`.`iso_no` separator ',') AS `isoNo`,
        group_concat(distinct `baim`.`short_desc2` separator ',') AS `material`,
        group_concat(distinct `mfmsi`.`spec` separator ',') AS `spec`,
        1 AS `qty`,
        group_concat(distinct `mhn`.`heat_no_code` separator ',') AS `heatNo`,
        group_concat(distinct `mhn`.`batch_no_code` separator ',') AS `batchNo`,
        group_concat(distinct `mfmobir`.`fmobir_code` separator ',') AS `inspReportNo`,
        max(`ew`.`project_id`) AS `project_id`,
        max(`ew`.`org_id`) AS `org_id`

      FROM
        `bpm_act_inst_material` `baim`
        JOIN `entity_weld` `ew`
          ON `ew`.`id` = `baim`.`entity_id`

        JOIN `mat_f_item_detail` `mfid`
          ON `mfid`.`rn_qr_code` = `baim`.`entity_qr_code2`

        JOIN `mat_f_material_stocktake_item` `mfmsi`
          ON `mfmsi`.`qr_code` = `mfid`.`rn_qr_code`

        JOIN `mat_f_material_open_box_inspection_report` `mfmobir`
          ON `mfmobir`.`fmst_id` = `mfmsi`.`fmst_id`

        JOIN `mat_heat_no` `mhn`
          ON `mhn`.`id` = `mfmsi`.`heat_no_id`

        GROUP BY
          `baim`.`entity_qr_code2`,
          `ew`.`project_id`

      ) `temp`

      order by `temp`.`isoNo`
;


