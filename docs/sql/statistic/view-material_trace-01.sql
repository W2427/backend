USE `ose_tasks`;

-- ----------------------------
-- View structure for material_trace
-- 材料跟踪
-- ----------------------------
CREATE OR REPLACE VIEW `material_trace-01` AS
SELECT
				bai.end_date	`Cutting date`,
				bsce.tag_number	`Tag No.`,
				''	`DWG No.`,
				bsct.no `Cutting No.`,
				bscp.structure_nest_no `Nesting drawing No.`,
				bsce.no `Member`,
				bsce.material `Material`,
				bsce.height `Thickness(mm)`,
				1 `QTY`,
				bsce.qr_code `QR CODE`,
				mrnd.cert_code `Certification No`,
				substring_index(bsce.heat_no,'/',1) `Heat No`,
				substring_index(bsce.heat_no,'/',-1)	`Plate No`

FROM bpm_structure_cutting_task_program_entity bsce
		LEFT JOIN bpm_structure_cutting_task bsct on bsce.cutting_id = bsct.id
		LEFT JOIN bpm_activity_instance_base bai ON bai.act_inst_id = bsct.act_inst_id
		LEFT JOIN bpm_structure_cutting_task_program bscp ON bsce.program_id = bscp.id
		LEFT JOIN mat_release_note_item_detail mrnd ON bsce.heat_no_id = mrnd.heat_no_id
;


