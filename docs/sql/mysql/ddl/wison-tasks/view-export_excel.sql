USE `saint_whale_tasks`;

-- ----------------------------
-- 导出下料报告
-- View structure for export_xls_cutting_report
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_cutting_report` AS
  SELECT `epp`.`no` AS `no`,
  `bc`.`no` AS `cutting_no`,
  `bc`.`date` AS `cuttingDate`,
  (CASE `bce`.`cuttingflag` WHEN 1 then 'OK' WHEN 0 then 'DOING' else 'notStart' end) AS `cutResult`,
  `bce`.`task_package_name` AS `packageName`,
  (CASE `bde`.`execute_ng_flag` WHEN 0 then 'OK' WHEN 1 then 'NG' else 'notStart' end) AS `deliveryResult`,
  `bd`.`no` AS `deliveryNo`,
  `bd`.`date` AS `DeliveryDate`,
  `epp`.`nps` AS `nps`,
  `bce`.`mat_issue_code` AS `issueCode`,
  `bce`.`mat_prepare_code` AS `prepareCode`,
  `bce`.`memo` AS `Remark`,
  json_extract(`bce`.`nesting_file`,'$.reportName') AS `nestingFile`,
  `bce`.`material_code` AS `material_code`,
  `usr`.`name` AS `cuttingUser`,
  `uusr`.`name` AS `deliveryUser`,
  `epp`.`project_id` AS `project_id`,
  `epp`.`org_id` AS `org_id`
  FROM
    `entity_pipe_piece` `epp`
    LEFT JOIN `bpm_cutting_entity` `bce` ON
      `epp`.`id` = `bce`.`pipe_piece_entity_id`
      AND (bce.cuttingflag= (SELECT max(cuttingflag) FROM bpm_cutting_entity bbce WHERE bbce.pipe_piece_entity_no =bce.pipe_piece_entity_no))
    LEFT JOIN  `bpm_cutting` `bc` ON `bc`.`id` = `bce`.`cutting_id`
    LEFT JOIN  `bpm_activity_instance_base` `bai` ON `bai`.`entity_id` = `bc`.`id`
    LEFT JOIN  `bpm_hi_taskinst` `bht` ON `bht`.`act_inst_id` = `bai`.`act_inst_id` AND `bht`.`task_def_key_` = 'usertask-PIPE-PIECE-CUTTING-EXECUTE'
    LEFT JOIN  `saint_whale_auth`.`users` `usr` ON `usr`.`id` = `bht`.`operator`
    LEFT JOIN  `bpm_delivery_entity` `bde` ON `epp`.`id` = `bde`.`entity_id`
    LEFT JOIN  `bpm_delivery` `bd` ON `bd`.`id` = `bde`.`delivery_id`
    LEFT JOIN  `bpm_activity_instance_base` `bbai` ON `bbai`.`entity_id` = `bd`.`id`
    LEFT JOIN  `bpm_hi_taskinst` `bbht` ON `bbht`.`act_inst_id` = `bbai`.`act_inst_id` AND `bbht`.`task_def_key_` = 'usertask-RECEIVE-CHECK-EXECUTE'
    LEFT JOIN  `saint_whale_auth`.`users` `uusr` ON `uusr`.`id` = `bbht`.`operator`
;

-- ----------------------------
-- 导出材料跟踪
-- View structure for export_xls_material_trace
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_material_trace` AS
  SELECT
    group_concat(distinct `temp`.`pieceNo` separator ',') AS `pieceNo`,
    group_concat(distinct `temp`.`spoolNo` separator ',') AS `spoolNo`,
    group_concat(distinct `temp`.`isoNo` separator ',') AS `isoNo`,
    group_concat(distinct `temp`.`material` separator ',') AS `material`,
    group_concat(distinct `temp`.`spec` separator ',') AS `spec`,
    group_concat(distinct `temp`.`qty` separator ',') AS `qty`,
    group_concat(distinct `temp`.`heatNo` separator ',') AS `heatNo`,
    group_concat(distinct `temp`.`batchNo` separator ',') AS `batchNo`,
    group_concat(distinct `temp`.`inspReportNo` separator ',') AS `inspReportNo`,
    max(`temp`.`project_id`) AS `project_id`,
    max(`temp`.`org_id`) AS `org_id`,
    `temp`.`qr_code` AS `qr_code`
  FROM
    (
      SELECT
        `eqc`.`entity_no` AS `pieceNo`,
        `epp`.`spool_no` AS `spoolNo`,
        `epp`.`iso_no` AS `isoNo`,
        `baim`.`short_desc1` AS `material`,
        `mfmsi`.`spec` AS `spec`,
        `eqc`.`qty` AS `qty`,
        `mhn`.`heat_no_code` AS `heatNo`,
        `mhn`.`batch_no_code` AS `batchNo`,
        `mfmobir`.`fmobir_code` AS `inspReportNo`,
        `epp`.`project_id` AS `project_id`,
        `epp`.`org_id` AS `org_id`,
        `eqc`.`qr_code` AS `qr_code`
      FROM
        `bpm_act_inst_material` `baim`
        JOIN  `entity_qr_code` `eqc`
          ON `baim`.`entity_qr_code1` = `eqc`.`qr_code`

        JOIN  `entity_pipe_piece` `epp`
          ON `epp`.`id` = `eqc`.`entity_id`

        JOIN  `mat_f_item_detail` `mfid`
          ON `mfid`.`rn_qr_code` = `eqc`.`material_qr_code`

        JOIN  `mat_f_material_stocktake_item` `mfmsi`
          ON `mfmsi`.`qr_code` = `mfid`.`rn_qr_code`

        JOIN  `mat_f_material_open_box_inspection_report` `mfmobir`
          ON `mfmobir`.`fmst_id` = `mfmsi`.`fmst_id`

        JOIN  `mat_heat_no` `mhn`
          ON `mhn`.`id` = `mfmsi`.`heat_no_id`
      WHERE `epp`.`deleted` = 0

      GROUP BY
        `eqc`.`qr_code` ASC,
        `epp`.`project_id`

      UNION ALL

      SELECT
        `eqc`.`entity_no` AS `pieceNo`,
        `epp`.`spool_no` AS `spoolNo`,
        `epp`.`iso_no` AS `isoNo`,
        `baim`.`short_desc2` AS `material`,
        `mfmsi`.`spec` AS `spec`,
        `eqc`.`qty` AS `qty`,
        `mhn`.`heat_no_code` AS `heatNo`,
        `mhn`.`batch_no_code` AS `batchNo`,
        `mfmobir`.`fmobir_code` AS `inspReportNo`,
        `epp`.`project_id` AS `project_id`,
        `epp`.`org_id` AS `org_id`,
        `eqc`.`qr_code` AS `qr_code`
      FROM
        `bpm_act_inst_material` `baim`
        JOIN  `entity_qr_code` `eqc`
          ON  `baim`.`entity_qr_code2` = `eqc`.`qr_code`

        JOIN  `entity_pipe_piece` `epp`
          ON `epp`.`id` = `eqc`.`entity_id`

        JOIN `mat_f_item_detail` `mfid`
          ON `mfid`.`rn_qr_code` = `eqc`.`material_qr_code`

        JOIN  `mat_f_material_stocktake_item` `mfmsi`
          ON `mfmsi`.`qr_code` = `mfid`.`rn_qr_code`

        JOIN  `mat_f_material_open_box_inspection_report` `mfmobir`
          ON `mfmobir`.`fmst_id` = `mfmsi`.`fmst_id`

        JOIN  `mat_heat_no` `mhn`
          ON `mhn`.`id` = `mfmsi`.`heat_no_id`
      WHERE
        `epp`.`deleted` = 0

      GROUP BY
        `eqc`.`qr_code` ASC,
        `epp`.`project_id` ASC
        ) `temp`

    GROUP BY `temp`.`qr_code`

    ORDER BY `isoNo`
;

-- ----------------------------
-- 导出管段 周报
-- View structure for export_xls_pipepiece_wkreport
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_pipepiece_wkreport` AS
  SELECT
    sum(`ep`.`nps`) AS `sumNps`,
    `we`.`process` AS `process`,
    `we`.`stage` AS `stage`,
    curdate() AS `curdate( )`,
    `we`.`sector` AS `module`,
    `ep`.`project_id` AS `project_id`,
    `ep`.`org_id` AS `org_id`
  FROM
    `wbs_entry` `we`
		INNER JOIN `wbs_entry_state` `wes`
		ON `we`.id = `wes`.wbs_entry_id
    LEFT JOIN `entity_pipe_piece` `ep`
      ON `we`.`entity_id` = `ep`.`id` and `ep`.`deleted` = 0

  WHERE
    (curdate() - interval 7 day) <= cast(`we`.`finish_at` as date)
    AND `wes`.`finished` = 1
    AND `wes`.`running_status` = 'APPROVED'
    AND `ep`.`id` is not null
  GROUP BY
    `we`.`process` ASC,
    `we`.`stage` ASC,
    `we`.`sector` ASC

  ORDER BY
   `we`.`process`,
   `we`.`sector`
;

-- ----------------------------
-- 导出试压包焊口
-- View structure for export_xls_ptp_welds
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_ptp_welds` AS
  SELECT
    `ew`.`iso_no` AS `isoNO`,
    `ppn`.`no` AS `PTPNO`,
    `ew`.`revision` AS `weldRev`,
    concat(`ew`.`nde`,`ew`.`nde_ratio`) AS `NDT`,
    `ew`.`display_name` AS `weldNo`,
    `ew`.`nps` AS `NPS`,
    `ew`.`weld_type` AS `weldType`,
    `wd`.`no` AS `welderNo`,
    `ew`.`wps_no` AS `wpsNo`,
    max(CASE `bai`.`a.process_name` WHEN 'WELD' then `bais`.`end_date` end) AS `vtDate`,
    max(CASE `bai`.`a.process_name` WHEN 'WELD' then `qr`.`report_no` end) AS `vtReport`,
    max(CASE WHEN `ew`.`nde` = 'RT' and `bai`.`a.process_name` = 'NDT' then `qr`.`created_at` end) AS `rtDate`,
    max(CASE WHEN `ew`.`nde` = 'RT' and `bai`.`a.process_name` = 'NDT' then `qr`.`report_no` end) AS `rtReport`,
    max(CASE WHEN `ew`.`nde` = 'PT' and `bai`.`a.process_name` = 'NDT' then `qr`.`created_at` end) AS `ptDate`,
    max(CASE WHEN `ew`.`nde` = 'PT' and `bai`.`a.process_name` = 'NDT' then `qr`.`report_no` end) AS `ptReport`,
    NULL AS `pmiReport`,
    NULL AS `pwhtDate`,
    NULL AS `pwhtReport`,
    `nde_done_percent`.`finished_percent` AS `finishedNdtRatio`,
    `ew`.`project_id` AS `project_id`,
    `ew`.`org_id` AS `org_id`
  FROM
    `entity_weld` `ew`
    LEFT JOIN  `project_node` `pn`
      ON `pn`.`entity_id` = `ew`.`iso_entity_id`

    LEFT JOIN  `hierarchy_node` `hn`
      ON `hn`.`node_id` = `pn`.`id`

    LEFT JOIN  `hierarchy_node` `hhn`
      ON `hhn`.`id` = `hn`.`parent_id`

    LEFT JOIN  `project_node` `ppn`
      ON `ppn`.`id` = `hhn`.`node_id`

    LEFT JOIN  `welder` `wd`
      ON `wd`.`id` = `ew`.`welder_id`

    LEFT JOIN  `bpm_activity_instance_base` `bai`
      ON `bai`.`entity_id` = `ew`.`id`
			JOIN `bpm_activity_instance_state` `bais`
			ON `bais`.bai_id =  `bai`.id
    LEFT JOIN  `bpm_entity_docs_material` `bedm`
      ON `bedm`.`entity_id` = `bai`.`entity_id`
      AND `bedm`.`process_id` = `bai`.`process_id`

    LEFT JOIN  `qc_report` `qr`
      ON convert(`qr`.`qrcode` using utf8mb4) = json_extract(`bedm`.`docs`,'$[0].reportId')

    LEFT JOIN
      (SELECT
                `entity_weld`.`iso_entity_id` AS `iso_entity_id`,
                sum(if( `entity_weld`.`ndt_execute_flag` = 1,1,0)) / count(0) AS `finished_percent`
                FROM
                  `entity_weld`
                where
                  `entity_weld`.`deleted` = 0
                group by  `entity_weld`.`iso_entity_id`
                ) `nde_done_percent`
      ON `nde_done_percent`.`iso_entity_id` = `ew`.`iso_entity_id`
    WHERE
      `hn`.`hierarchy_type` = 'PRESSURE_TEST_PACKAGE'
       and `ew`.`deleted` = 0
       and (`bedm`.`type` in ('EXTERNAL_INSPECTION','CHECK_LIST') or isnull(`bedm`.`type`))
       and (`bai`.`a.process_name` in ('FITUP','WELD','NDT') or isnull(`bai`.`a.process_name`))
       and (`qr`.`process` in ('FITUP','WELD','NDT') or isnull(`qr`.`process`))
    group by `ew`.`id`
;

-- ----------------------------
-- 导出spool结果
-- View structure for export_xls_spool_result
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_spool_result` AS
  SELECT
				mid(es.iso_no, LENGTH(es.iso_no) - LOCATE('-', REVERSE(es.iso_no))+2,8) as moduelNo,
				es.id as spoolId,
				es.no as spoolNo,
				ppn.no as Layer,
				es.sheet_no as SheetNo,
				es.sheet_total as sheetTotal,
				es.revision as Rev,
				es.nps as nps,
				es.material as material,
				es.weight as weight,
				es.painting_code as paintCode,
				es.painting_area as paintArea,
				es.painting_area_unit as AreaUnit,
				group_concat(distinct bbai.entity_no separator ',') as SPOOL_RELEASE_NO,
				group_concat(distinct bbai.end_date separator ',') as ReleaseDate,
				group_concat(distinct bai.entity_no separator ',') as PRE_HANDOVER,
				group_concat(distinct bai.end_date  separator ',') as PRE_HANDOVER_DATE,
				group_concat(distinct b2ai.entity_no separator ',') as POST_HANDOVER,
				group_concat(distinct b2ai.end_date  separator ',') as POST_HANDOVER_DATE,
				group_concat(distinct b3ai.entity_no separator ',') as SP_TRANSPORT,
				group_concat(distinct b3ai.end_date  separator ',') as SP_TRANSPORT_DATE

		FROM
		 entity_spool es
			left join bpm_delivery_entity bde on bde.entity_id = es.id
			left join (
			  select bbaib.*,bbais.end_date
				from bpm_activity_instance_base bbaib
				inner join bpm_activity_instance_state bbais
				on bbaib.id = bbais.bai_id
				where bbaib.entity_sub_type = 'SPOOL_DELIVERY_LIST'
				and bbaib.process_name = 'SPOOL_RELEASE'
				) bbai on bbai.entity_id = bde.delivery_id
			left join (
			  select baib.*,bais.end_date
				from bpm_activity_instance_base baib
				inner join bpm_activity_instance_state bais
				on baib.id = bais.bai_id
				where baib.entity_sub_type = 'SPOOL_DELIVERY_LIST'
				and baib.process_name = 'PRE_PAINTING_HANDOVER'
				) bai on bai.entity_id = bde.delivery_id
			left join (
			  select b2aib.*,b2ais.end_date
				from bpm_activity_instance_base b2aib
				inner join bpm_activity_instance_state b2ais
				on b2aib.id = b2ais.bai_id
				where b2aib.entity_sub_type = 'SPOOL_DELIVERY_LIST'
				and b2aib.process_name = 'POST_PAINTING_HANDOVER'
				) b2ai on b2ai.entity_id = bde.delivery_id
			left join (
			  select b3aib.*,b3ais.end_date
				from bpm_activity_instance_base b3aib
				inner join bpm_activity_instance_state b3ais
				on b3aib.id = b3ais.bai_id
				where b3aib.entity_sub_type = 'SPOOL_DELIVERY_LIST'
				and b3aib.process_name = 'SPOOL_TRANSPORT'
				) b3ai on b3ai.entity_id = bde.delivery_id
			left join project_node pn on pn.entity_id = es.iso_entity_id
			left join hierarchy_node hn on hn.node_id = pn.id
			left join hierarchy_node hhn on hhn.id = hn.parent_id
			left join project_node ppn on ppn.id = hhn.node_id

		WHERE
		  pn.node_type = 'ENTITY'
		  AND hn.hierarchy_type = "PIPING"

		GROUP BY es.id
;

-- ----------------------------
-- 导出任务完成4级计划未更新清单
-- View structure for export_xls_taskdone_4thschedule_notdone
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_taskdone_4thschedule_notdone` AS
  SELECT
    `bai`.`id` AS `id`,
    `bai`.`entity_no` AS `entity_no`,
    `bais`.`finish_state` AS `finish_state`,
    `bais`.`end_date` AS `end_date`,
    `bai`.`a.process_name` AS `a.process_name`,
    `wes`.`running_status` AS `running_status`,
    `we`.`project_id` AS `project_id`,
    `we`.`org_id` AS `org_id`
  FROM
    `bpm_activity_instance_base` `bai`
		INNER JOIN `bpm_activity_instance_state` `bais`
		ON  `bai`.id = `bais`.bai_id
		  LEFT JOIN  `wbs_entry_state` `wes`
			 ON  `bai`.`act_inst_id` = `wes`.`process_instance_id`
     JOIN `wbs_entry` `we`
       ON  `we`.id =  `wes`.wbs_entry_id
  WHERE
    `bais`.`finish_state` = 2
    AND `wes`.`running_status` <> 'APPROVED'
    AND `wes`.`running_status` <> 'REJECTED'
;

-- ----------------------------
-- 导出任务清单
-- View structure for export_xls_task_list
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_task_list` AS
  SELECT
    `bai`.`entity_no` AS `entityNo`,
    `bais`.`memo` AS `displayName`,
    `bai`.`entity_sub_type` AS `entityType`,
    `bai`.`a.process_name` AS `processName`,
    `bai`.`plan_start_date` AS `planStartTime`,
    `bai`.`plan_end_date` AS `planEndTime`,
    `bais`.`start_date` AS `actualStartTime`,
    `bais`.`end_date` AS `actualEndTime`,
    `brt`.`name_` AS `currentTaskNode`,
    `bai`.`created_at` AS `createdAt`,
    `bais`.`task_package_name` AS `taskPackageName`,
    `bais`.`team_name` AS `teamName`,
    `bais`.`work_site_name` AS `workSiteName`,
    `bais`.`cost_hour` AS `workHours`,
    `bai`.`owner_name` AS `ownerName`,
    `bais`.`current_executor` AS `currentExecutor`,
    `bai`.`version` AS `version`,
    `bais`.`memo` AS `memo`,
    `bais`.`weld_welder_count` AS `weldCount`,
    `bais`.`suspension_state` AS `pendingState`,
    `bais`.`finish_state` AS `finishedState`,
    `bai`.`project_id` AS `project_id`,
    `bai`.`org_id` AS `org_id`
  FROM
    `bpm_activity_instance_base` `bai`
		INNER JOIN `bpm_activity_instance_state` `bais`
		ON `bai`.id = `bais`.bai_id
    LEFT JOIN `bpm_ru_task` `brt`
      ON `bai`.`act_inst_id` = `brt`.`act_inst_id`
;

-- ----------------------------
-- 导出完整的焊接表清单
-- View structure for export_xls_welds
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_welds` AS
  SELECT
    `w`.`spool_no` AS `spool_no`,
    `w`.`iso_no` AS `iso_no`,
    `w`.`display_name` AS `wel_no`,
    `w`.`shop_field` AS `shop_field`,
    `w`.`weld_type` AS `weld_type`,
    `w`.`sheet_no` AS `sheet_no`,
    `w`.`sheet_total` AS `sheet_total`,
    `w`.`revision` AS `revision`,
    `w`.`wps_no` AS `wps_no`,
    concat(`w`.`nde`,`w`.`nde_ratio`) AS `NDE`,
    `w`.`pwht` AS `pwht`,
    (CASE `w`.`hardness_test` WHEN 1 then 'Y' else 'N' end) AS `Hardness_test`,
    `w`.`pmi_ratio` AS `pmi_ratio`,
    `w`.`pipe_class` AS `pipe_class`,
    `w`.`nps_text` AS `nps`,
    `w`.`thickness` AS `thickness`,
    `w`.`tag_no1` AS `tag_no1`,
    `w`.`material_code1` AS `material_code1`,
    `w`.`material1` AS `material1`,
    `w`.`remarks1` AS `remark1`,
    `w`.`tag_no2` AS `tag_no2`,
    `w`.`material_code2` AS `material_code2`,
    `w`.`material2` AS `material2`,
    `w`.`remarks2` AS `remark2`,
    `w`.`painting_code` AS `painting_code`,
    `w`.`remarks` AS `remarks`,
    `w`.`project_id` AS `project_id`,
    `w`.`org_id` AS `org_id`
  FROM
    `entity_weld` `w`
  where
    `w`.`deleted` = 0;

-- ----------------------------
-- 导出焊接简单清单
-- View structure for export_xls_weld_brief
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_weld_brief` AS
  SELECT
    `ew`.`no` AS `weld_no`,
    `ew`.`weld_type` AS `weld_type`,
    `ew`.`shop_field` AS `shop_field`,
    `ew`.`thickness` AS `thickness`,
    `ew`.`wps_no` AS `wps_no`,
    concat(`ew`.`nde`,`ew`.`nde_ratio`) AS `concat(ew.nde,ew.nde_ratio)`,
    `ew`.`material1` AS `material1`,
    `ew`.`material_code1` AS `material_code1`,
    `ew`.`material2` AS `material2`,
    `ew`.`material_code2` AS `material_code2`,
    `ew`.`pmi_ratio` AS `pmi_ratio`,
    (CASE `bais`.`finish_state` WHEN 2 then 'DONE' WHEN 1 then 'DOING' else 'notStart' end) AS `FITUP_Result`,
    `ew`.`nps` AS `nps`,
    group_concat(distinct `usr`.`name` separator ' , ') AS `fitup_Person`,
    group_concat(distinct `bais`.`end_date` separator ' , ') AS `fitup_date`,
    (CASE max(`bbais`.`finish_state`) WHEN 2 then 'DONE' WHEN 1 then 'DOING' else 'notStart' end) AS `WELD_Result`,
    group_concat(distinct `bbais`.`end_date` separator ' , ') AS `weld_date`,
    group_concat(distinct `wer`.`name` separator ' , ') AS `welder`,
    group_concat(distinct `bbbais`.`end_date` separator ' , ') AS `ndt_date`,
    (CASE
        WHEN (max(`bbbais`.`finish_state`) = 2 and `ew`.`ndt_result` = 'OK') then 'DONE'
        WHEN (max(`bbbais`.`finish_state`) = 2 and `ew`.`ndt_result` = 'NG') then 'REDO'
        WHEN (max(`bbbais`.`finish_state`) = 1 and `ew`.`ndt_result` = 'OK') then 'DOING'
        WHEN (max(`bbbais`.`finish_state`) = 1 and `ew`.`ndt_result` = 'NG') then 'REDOING'
        ELSE 'notStart' END) AS `ndt_result`,
    group_concat(distinct `uusr`.`name` separator ' , ') AS `ndt_operator`,
    `ew`.`project_id` AS `project_id`,`ew`.`org_id` AS `org_id`
  FROM
    `entity_weld` `ew`
    LEFT JOIN  `bpm_activity_instance_base` `bai`
    ON `bai`.`entity_id` = `ew`.`id`
    AND `bai`.`a.process_name` = 'FITUP'
		JOIN `bpm_activity_instance_state` `bais`
		ON `bai`.`id` = `bais`.`bai_id`
    LEFT JOIN  `bpm_hi_taskinst` `bht`
      ON `bht`.`act_inst_id` = `bai`.`act_inst_id`
      AND `bht`.`task_def_key_` = 'usertask-FITUP_EXECUTE'

    LEFT JOIN `saint_whale_auth`.`users` `usr`
      ON `usr`.`id` = `bht`.`operator`

    LEFT JOIN  `bpm_activity_instance_base` `bbai`
      ON `bbai`.`entity_id` = `ew`.`id`
      AND `bbai`.`a.process_name` = 'WELD'

		JOIN `bpm_activity_instance_state` `bbais`
		ON `bbai`.`id` = `bbais`.`bai_id`

    LEFT JOIN  `welders` `wer`
      ON `wer`.`id` = `ew`.`welder_id`

    LEFT JOIN  `bpm_activity_instance_base` `bbbai`
      ON `bbbai`.`entity_id` = `ew`.`id`
      AND `bbbai`.`a.process_name` = 'NDT'

		JOIN `bpm_activity_instance_state` `bbbais`
		ON `bbbai`.`id` = `bbbais`.`bai_id`

    LEFT JOIN  `bpm_hi_taskinst` `bbht`
      ON `bbht`.`act_inst_id` = `bbbai`.`act_inst_id`
      AND `bbht`.`task_def_key_` = 'usertask-NDT-ENGINEER-EXECUTE'

    LEFT JOIN `saint_whale_auth`.`users` `uusr`
      ON  `uusr`.`id` = `bbht`.`operator`

  WHERE `ew`.`deleted` = 0

  GROUP BY `ew`.`no`
;

-- ----------------------------
-- 导出焊接详情
-- View structure for export_xls_weld_detail
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_weld_detail` AS
  SELECT
    `ew`.`id` AS `id`,
    `ew`.`no` AS `weld_no`,
    `ew`.`weld_type` AS `weld_type`,
    `ew`.`shop_field` AS `shop_field`,
    `ew`.`thickness` AS `thickness`,
    `ew`.`wps_no` AS `wps_no`,
    concat(`ew`.`nde`,`ew`.`nde_ratio`) AS `concat(ew.nde,ew.nde_ratio)`,
    (CASE WHEN isnull(`baim`.`short_desc1`) then `ew`.`material1` else `baim`.`short_desc1` end) AS `material1`,
    `baim`.`heat_no_code1` AS `heat_no1`,
    (CASE WHEN isnull(`baim`.`short_desc2`) then `ew`.`material2` else `baim`.`short_desc2` end) AS `material2`,
    `baim`.`heat_no_code2` AS `heat_no2`,
    `ew`.`pmi_ratio` AS `pmi_ratio`,
    (CASE `bais`.`finish_state` WHEN 2 then 'DONE' WHEN 1 then 'DOING' else 'notStart' end) AS `FITUP_Result`,
    `ew`.`nps` AS `nps`,
    group_concat(distinct json_extract(`bedmm`.`docs`,'$[0].reportName') separator ' , ') AS `fitup_report`,
    group_concat(distinct `usr`.`name` separator ' , ') AS `fitup_Person`,
    group_concat(distinct `bais`.`end_date` separator ' , ') AS `fitup_date`,
    (CASE max(`bbais`.`finish_state`) WHEN 2 then 'DONE' WHEN 1 then 'DOING' else 'notStart' end) AS `WELD_Result`,
    group_concat(distinct `bbais`.`end_date` separator ' , ') AS `weld_date`,
    group_concat(distinct `wer`.`name` separator ' , ') AS `welder`,
    group_concat(distinct json_extract(`bedm`.`docs`,'$[0].reportName') separator ' , ') AS `weld_report`,
    group_concat(distinct `bbbais`.`end_date` separator ' , ') AS `ndt_date`,
    (CASE WHEN ((max(`bbbais`.`finish_state`) = 2) and (`ew`.`ndt_result` = 'OK')) then 'DONE' WHEN ((max(`bbbais`.`finish_state`) = 2) and (`ew`.`ndt_result` = 'NG')) then 'REDO' WHEN ((max(`bbbais`.`finish_state`) = 1) and (`ew`.`ndt_result` = 'OK')) then 'DOING' WHEN ((max(`bbbais`.`finish_state`) = 1) and (`ew`.`ndt_result` = 'NG')) then 'REDOING' else 'notStart' end) AS `ndt_result`,
    group_concat(distinct `uusr`.`name` separator ' , ') AS `ndt_operator`,
    group_concat(distinct json_extract(`bbedm`.`docs`,'$[0].reportName') separator ' , ') AS `ndt_report`,
    `ew`.`project_id` AS `project_id`,`ew`.`org_id` AS `org_id`
  FROM
    `entity_weld` `ew`
    LEFT JOIN  `bpm_activity_instance_base` `bai`
      ON `bai`.`entity_id` = `ew`.`id`
      AND `bai`.`a.process_name` = 'FITUP'

    JOIN  `bpm_activity_instance_state` `bais`
		ON  `bai`.id = `bais`.bai_id

    LEFT JOIN  `bpm_hi_taskinst` `bht`
      ON `bht`.`act_inst_id` = `bai`.`act_inst_id`
      AND `bht`.`task_def_key_` = 'usertask-FITUP_EXECUTE'

    LEFT JOIN `saint_whale_auth`.`users` `usr`
      ON `usr`.`id` = `bht`.`operator`

    LEFT JOIN  `bpm_entity_docs_material` `bedmm`
      ON `ew`.`id` = `bedmm`.`entity_id`
      AND `bedmm`.`process_id` in (SELECT `bp`.`id` FROM  `bpm_process` `bp` where `bp`.`name_en` = 'FITUP')
      AND `bedmm`.`type` = 'EXTERNAL_INSPECTION'

    LEFT JOIN  `bpm_activity_instance_base` `bbai`
      ON `bbai`.`entity_id` = `ew`.`id`
      AND `bbai`.`a.process_name` = 'WELD'

   JOIN  `bpm_activity_instance_state` `bbais`
		ON  `bbai`.id = `bbais`.bai_id

    LEFT JOIN  `welders` `wer`
      ON `wer`.`id` = `ew`.`welder_id`

    LEFT JOIN  `bpm_entity_docs_material` `bedm`
      ON `ew`.`id` = `bedm`.`entity_id`
      AND `bedm`.`process_id` in (SELECT `bp`.`id` FROM  `bpm_process` `bp` where `bp`.`name_en` = 'WELD')
      AND `bedm`.`type` = 'EXTERNAL_INSPECTION'

    LEFT JOIN  `bpm_activity_instance_base` `bbbai`
      ON `bbbai`.`entity_id` = `ew`.`id`
      AND `bbbai`.`a.process_name` = 'NDT'

   JOIN  `bpm_activity_instance_state` `bbbais`
		ON  `bbbai`.id = `bbbais`.bai_id

    LEFT JOIN  `bpm_hi_taskinst` `bbht`
      ON `bbht`.`act_inst_id` = `bbbai`.`act_inst_id`
      AND `bbht`.`task_def_key_` = 'usertask-NDT-ENGINEER-EXECUTE'

    LEFT JOIN `saint_whale_auth`.`users` `uusr`
      ON `uusr`.`id` = `bbht`.`operator`

    LEFT JOIN  `bpm_entity_docs_material` `bbedm`
      ON `ew`.`id` = `bbedm`.`entity_id`
      AND `bbedm`.`process_id` in (SELECT `bp`.`id` FROM  `bpm_process` `bp` where `bp`.`name_en` = 'NDT')
      AND `bbedm`.`type` = 'EXTERNAL_INSPECTION'

    LEFT JOIN  `bpm_act_inst_material` `baim`
      ON `baim`.`act_inst_id` = `bht`.`act_inst_id`
      AND `baim`.`act_task_id` = `bht`.`task_id_`

    WHERE `ew`.`deleted` = 0

    group by `ew`.`id`
;

-- ----------------------------
-- 导出焊口详情
-- View structure for export_xls_weld_details
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_weld_details` AS
  SELECT
    `ew`.`no` AS `weld_no`,
    `ew`.`weld_type` AS `weld_type`,
    `ew`.`shop_field` AS `shop_field`,
    `ew`.`thickness` AS `thickness`,
    `ew`.`wps_no` AS `wps_no`,
    concat(`ew`.`nde`,`ew`.`nde_ratio`) AS `concat(ew.nde,ew.nde_ratio)`,
    (CASE WHEN isnull(`baim`.`short_desc1`) then `ew`.`material1` else `baim`.`short_desc1` end) AS `material1`,
    (CASE WHEN isnull(`baim`.`short_desc1`) then '' else `baim`.`heat_no_code1` end) AS `heat_no1`,
    (CASE WHEN isnull(`baim`.`short_desc1`) then `ew`.`material_code1` else `baim`.`tag_number1` end) AS `material_code1`,
    (CASE WHEN isnull(`baim`.`short_desc2`) then `ew`.`material2` else `baim`.`short_desc2` end) AS `material2`,
    (CASE WHEN isnull(`baim`.`short_desc2`) then '' else `baim`.`heat_no_code2` end) AS `heat_no2`,
    (CASE WHEN isnull(`baim`.`short_desc2`) then `ew`.`material_code2` else `baim`.`tag_number2` end) AS `material_code2`,
    `ew`.`pmi_ratio` AS `pmi_ratio`,
    `ew`.`nps` AS `nps`,
    max(CASE WHEN (`bai`.`a.process_name` = 'FITUP' and `bais`.`finish_state` = 2) then 'DONE' WHEN (`bai`.`a.process_name` = 'FITUP' and `bais`.`finish_state` = 1) then 'DOING' WHEN `bai`.`a.process_name` = 'FITUP' then 'notStart' end) AS `FITUP_Result`,
    max(CASE `bai`.`a.process_name` WHEN 'FITUP' then `bais`.`end_date` end) AS `fitupDate`,
    max(CASE `bai`.`a.process_name` WHEN 'FITUP' then `qr`.`report_no` end) AS `fitupReport`,
    group_concat(distinct (CASE `bai`.`a.process_name` WHEN 'FITUP' then `usr`.`name` end) separator ',') AS `fitupPerson`,
    max(CASE WHEN (`bai`.`a.process_name` = 'WELD' and `bais`.`finish_state` = 2) then 'DONE' WHEN (`bai`.`a.process_name` = 'WELD' and `bais`.`finish_state` = 1) then 'DOING' WHEN `bai`.`a.process_name` = 'WELD' then 'notStart' end) AS `WELD_Result`,
    max(CASE `bai`.`a.process_name` WHEN 'WELD' then `bais`.`end_date` end) AS `weldDate`,
    max(CASE `bai`.`a.process_name` WHEN 'WELD' then `qr`.`report_no` end) AS `weldReport`,
    group_concat(distinct (CASE `bai`.`a.process_name` WHEN 'WELD' then `usr`.`name` end) separator ',') AS `weldPerson`,
    `wdr`.`no` AS `welderNo`,
    max(CASE WHEN (`bai`.`a.process_name` = 'NDT' and `bais`.`finish_state` = 2 and `ew`.`ndt_result` = 'OK') then 'DONE' WHEN (`bai`.`a.process_name` = 'NDT' and `bais`.`finish_state` = 2 and `ew`.`ndt_result` = 'NG') then 'REDO' WHEN (`bai`.`a.process_name` = 'NDT' and `bais`.`finish_state` = 1 and `ew`.`ndt_result` = 'OK') then 'DOING' WHEN (`bai`.`a.process_name` = 'NDT' and `bais`.`finish_state` = 1 and `ew`.`ndt_result` = 'NG') then 'REDOING' WHEN `bai`.`a.process_name` = 'NDT' then 'notStart' end) AS `ndtResult`,
    max(CASE WHEN (`ew`.`nde` = 'RT' and `bai`.`a.process_name` = 'NDT') then `qr`.`created_at` end) AS `rtDate`,
    max(CASE WHEN (`ew`.`nde` = 'RT' and `bai`.`a.process_name` = 'NDT') then `qr`.`report_no` end) AS `rtReport`,
    max(CASE WHEN (`ew`.`nde` = 'PT' and `bai`.`a.process_name` = 'NDT') then `qr`.`created_at` end) AS `ptDate`,
    max(CASE WHEN (`ew`.`nde` = 'PT' and `bai`.`a.process_name` = 'NDT') then `qr`.`report_no` end) AS `ptReport`,
    group_concat(distinct (CASE `bai`.`a.process_name` WHEN 'NDT' then `usr`.`name` end) separator ',') AS `ndtPerson`,
    `ew`.`project_id` AS `project_id`,
    `ew`.`org_id` AS `org_id`
  FROM
    `entity_weld` `ew`
    LEFT JOIN  `bpm_activity_instance_base` `bai`
      ON `bai`.`entity_id` = `ew`.`id`

    JOIN  `bpm_activity_instance_state` `bais`
      ON `bai`.`id` = `bais`.`bai_id`

    LEFT JOIN  `bpm_entity_docs_material` `bedm`
      ON `bedm`.`entity_id` = `bai`.`entity_id`
      AND `bedm`.`process_id` = `bai`.`process_id`

    LEFT JOIN  `bpm_hi_taskinst` `bht`
      ON `bht`.`act_inst_id` = `bai`.`act_inst_id`

    LEFT JOIN `saint_whale_auth`.`users` `usr`
      ON `usr`.`id` = `bht`.`operator`

    LEFT JOIN  `bpm_act_inst_material` `baim`
      ON `baim`.`act_inst_id` = `bai`.`act_inst_id`

    LEFT JOIN  `qc_report` `qr`
      ON convert(`qr`.`qrcode` using utf8mb4) = json_extract(`bedm`.`docs`,'$[0].reportId')

    LEFT JOIN  `welders` `wdr`
      ON `wdr`.`id` = `ew`.`welder_id`

  WHERE
    (`bedm`.`type` in ('EXTERNAL_INSPECTION','CHECK_LIST') or isnull(`bedm`.`type`))
    AND (`bai`.`a.process_name` in ('FITUP','WELD','NDT') or isnull(`bai`.`a.process_name`))
    AND (`qr`.`process` in ('FITUP','WELD','NDT') or isnull(`qr`.`process`))
    AND (`bht`.`task_def_key_` in ('usertask-FITUP_EXECUTE','usertask-WELD_EXECUTE','usertask-NDT-ENGINEER-EXECUTE') or isnull(`bht`.`task_def_key_`))

  group by `ew`.`id`
  order by `bais`.`start_date`
;

-- ----------------------------
-- 焊口炉批号
-- View structure for export_xls_weld_heat_no
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_weld_heat_no` AS
  SELECT
    `ew`.`id` AS `id`,
		`ew`.`no` AS `weld_no`,
		`ew`.`weld_type` AS `weld_type`,
		`ew`.`shop_field` AS `shop_field`,
		`ew`.`thickness` AS `thickness`,
		`ew`.`wps_no` AS `wps_no`,
		concat(`ew`.`nde`,`ew`.`nde_ratio`) AS `concat(ew.nde,ew.nde_ratio)`,
		(CASE WHEN isnull(`baim`.`short_desc1`) then `ew`.`material1` else `baim`.`short_desc1` end) AS `material1`,
		`baim`.`heat_no_code1` AS `heat_no1`,
		(CASE WHEN isnull(`baim`.`short_desc2`) then `ew`.`material2` else `baim`.`short_desc2` end) AS `material2`,
		`baim`.`heat_no_code2` AS `heat_no2`,
		`ew`.`pmi_ratio` AS `pmi_ratio`,
		`ew`.`nps` AS `nps`,
		`ew`.`project_id` AS `project_id`,
		`ew`.`org_id` AS `org_id`
		FROM (
		  (
		    (`entity_weld` `ew`
        LEFT JOIN `bpm_activity_instance_base` `bai`
		    ON((`bai`.`entity_id` = `ew`.`id`) and (`bai`.`a.process_name` = 'FITUP'))
			  )
		    LEFT JOIN `bpm_hi_taskinst` `bht`
		    ON((`bht`.`act_inst_id` = `bai`.`act_inst_id`) and (`bht`.`task_def_key_` = 'usertask-FITUP_EXECUTE'))
		  )
		  LEFT JOIN `bpm_act_inst_material` `baim`
		  ON((`baim`.`act_inst_id` = `bht`.`act_inst_id`) and (`baim`.`act_task_id` = `bht`.`task_id_`))
			)
		where (`ew`.`deleted` = 0)
		group by `ew`.`id`;

-- ----------------------------
-- 焊口状态
-- View structure for export_xls_weld_status
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_weld_status` AS
  SELECT
    `ew`.`id` AS `id`,
    `ew`.`no` AS `weld_no`,
    `ew`.`weld_type` AS `weld_type`,
    `ew`.`shop_field` AS `shop_field`,
    `ew`.`thickness` AS `thickness`,
    `ew`.`wps_no` AS `wps_no`,
    concat(`ew`.`nde`,`ew`.`nde_ratio`) AS `concat(ew.nde,ew.nde_ratio)`,
    `ew`.`material1` AS `material1`,
    `ew`.`material2` AS `material2`,
    `ew`.`pmi_ratio` AS `pmi_ratio`,
    (CASE `bais`.`finish_state` WHEN 2 then 'DONE' WHEN 1 then 'DOING' else 'notStart' end) AS `FITUP_Result`,
    `ew`.`nps` AS `nps`,
    group_concat(distinct `usr`.`name` separator ' , ') AS `fitup_Person`,
    group_concat(distinct `bais`.`end_date` separator ' , ') AS `fitup_date`,
    (CASE max(`bbais`.`finish_state`) WHEN 2 then 'DONE' WHEN 1 then 'DOING' else 'notStart' end) AS `WELD_Result`,
    group_concat(distinct `bbais`.`end_date` separator ' , ') AS `weld_date`,
    group_concat(distinct `wer`.`name` separator ' , ') AS `welder`,
    group_concat(distinct `bbbais`.`end_date` separator ' , ') AS `ndt_date`,
    (CASE
			WHEN (max(`bbbais`.`finish_state`) = 2 and `ew`.`ndt_result` = 'OK') then 'DONE'
			WHEN (max(`bbbais`.`finish_state`) = 2 and `ew`.`ndt_result` = 'NG') then 'REDO'
			WHEN (max(`bbbais`.`finish_state`) = 1 and `ew`.`ndt_result` = 'OK') then 'DOING'
			WHEN (max(`bbbais`.`finish_state`) = 1 and `ew`.`ndt_result` = 'NG') then 'REDOING'
			else 'notStart' end) AS `ndt_result`,
    group_concat(distinct `uusr`.`name` separator ' , ') AS `ndt_operator`,
    `ew`.`project_id` AS `project_id`,
    `ew`.`org_id` AS `org_id`

  FROM
    `entity_weld` `ew`
    LEFT JOIN  `bpm_activity_instance_base` `bai`
      ON `bai`.`entity_id` = `ew`.`id`
      AND `bai`.`a.process_name` = 'FITUP'
    JOIN  `bpm_activity_instance_state` `bais`
      ON `bai`.`id` = `bais`.`bai_id`
    LEFT JOIN  `bpm_hi_taskinst` `bht`
      ON `bht`.`act_inst_id` = `bai`.`act_inst_id`
      and `bht`.`task_def_key_` = 'usertask-FITUP_EXECUTE'

    LEFT JOIN `saint_whale_auth`.`users` `usr`
      ON `usr`.`id` = `bht`.`operator`

    LEFT JOIN  `bpm_activity_instance_base` `bbai`
      ON `bbai`.`entity_id` = `ew`.`id`
      and `bbai`.`a.process_name` = 'WELD'
    JOIN  `bpm_activity_instance_state` `bbais`
      ON `bbai`.`id` = `bbais`.`bai_id`
    LEFT JOIN  `welders` `wer`
      ON `wer`.`id` = `ew`.`welder_id`

    LEFT JOIN  `bpm_activity_instance_base` `bbbai`
      ON `bbbai`.`entity_id` = `ew`.`id`
      and `bbbai`.`a.process_name` = 'NDT'

   JOIN  `bpm_activity_instance_state` `bbbais`
      ON `bbbai`.`id` = `bbbais`.`bai_id`

    LEFT JOIN  `bpm_hi_taskinst` `bbht`
      ON `bbht`.`act_inst_id` = `bbbai`.`act_inst_id`
      and `bbht`.`task_def_key_` = 'usertask-NDT-ENGINEER-EXECUTE'

    LEFT JOIN `saint_whale_auth`.`users` `uusr`
      ON `uusr`.`id` = `bbht`.`operator`

    where `ew`.`deleted` = 0

    group by `ew`.`id`
;

-- ----------------------------
-- 焊口测试
-- View structure for export_xls_weld_test
-- ----------------------------
CREATE OR REPLACE VIEW `export_xls_weld_test` AS
  SELECT
    `ew`.`no` AS `weld_no`,
    `ew`.`weld_type` AS `weld_type`,
    `ew`.`shop_field` AS `shop_field`,
    `ew`.`thickness` AS `thickness`,
    `ew`.`wps_no` AS `wps_no`,
    concat(`ew`.`nde`,`ew`.`nde_ratio`) AS `concat(ew.nde,ew.nde_ratio)`,
    `ew`.`material1` AS `material1`,
    `ew`.`material2` AS `material2`,
    `ew`.`pmi_ratio` AS `pmi_ratio`,
    `ew`.`nps` AS `nps`,
    `ew`.`project_id` AS `project_id`,
    `ew`.`org_id` AS `org_id`

  FROM
    `entity_weld` `ew`

  where `ew`.`deleted` = 0

  group by `ew`.`id`
;
