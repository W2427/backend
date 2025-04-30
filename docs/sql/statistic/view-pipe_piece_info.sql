USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `pipe_piece_info` AS
SELECT
		ei.module_no `模块号`,
		epp.no				`下料段号`,
		epp.nps				`NPS`,
		epp.revision 	`下料段版本号`,
		epp.material_code 	`材料编码`,
		epp.material				`材料描述`,
		epp.length					`长度`,
		eqc.heat_no					`炉批号`,
		eqc.operator_name		`切割人员`,
		eqc.update_date			`切割日期`,
		eqc.qr_code					`二维码`,
		eqc.bpm_cutting_no	`切割单号`,
		eqc.material_qr_code `原材料二维码`,
		mfi.ident 					 `IDENT`,
		bce.task_package_name `下料任务包(任务包名）`,
		epp.project_id,
		epp.org_id

-- 序号	模块号	下料段号	NPS	下料段件版本号	材料编码	材料描述	长度	炉批号	切割人员	切割日期	二维码	切割单号	原材料二维码	IDENT	下料任务包(任务包名）

FROM entity_pipe_piece epp
		LEFT JOIN entity_iso ei ON epp.iso_entity_id = ei.id AND epp.project_id = ei.project_id
		LEFT JOIN entity_qr_code eqc ON eqc.entity_id = epp.id AND eqc.project_id = epp.project_id
		LEFT JOIN bpm_cutting_entity bce ON bce.pipe_piece_entity_id = epp.id AND epp.project_id = bce.project_id
		LEFT JOIN mat_f_item mfi ON mfi.id = eqc.f_item_id
;
