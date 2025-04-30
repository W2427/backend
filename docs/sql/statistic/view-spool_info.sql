USE `saint_whale_tasks`;

CREATE OR REPLACE VIEW `spool_info` AS
SELECT
		es.`no`		`管段号`,
		es.short_code 	`短代码`,
		es.sheet_no			`子图页码`,
		es.sheet_total  `总页码`,
		es.revision			`版本号`,
		es.nps_text			`NPS`,
		''	`壁厚`,
		es.painting_code	`油漆代码`,

		CASE WHEN sg.cutting_total_count = 0 THEN '--' WHEN sg.cutting_total_count = sg.cutting_finished_count THEN 'Y' ELSE 'N' END AS `下料切割是否完毕`,
		sg.cutting_date	`切割完毕日期`,

		CASE WHEN sg.fitup_total_count = 0 THEN '--' WHEN sg.fitup_total_count = sg.fitup_finished_count THEN 'Y' ELSE 'N' END AS `组对是否完毕`,
		sg.fitup_date	`组对完毕日期`,

		CASE WHEN sg.weld_total_count = 0 THEN '--' WHEN sg.weld_total_count = sg.weld_finished_count THEN 'Y' ELSE 'N' END AS `焊接是否完毕`,
		sg.weld_date	`焊接完毕日期`,

		CASE WHEN sg.pmi_total_count = 0 THEN '--' WHEN sg.pmi_total_count = sg.pmi_finished_count THEN 'Y' ELSE 'N' END AS `PMI是否完毕`,
		sg.pmi_date	`PMI完毕日期`,

		CASE WHEN sg.pwht_total_count = 0 THEN '--' WHEN sg.pwht_total_count = sg.pwht_finished_count THEN 'Y' ELSE 'N' END AS `PWHT是否完毕`,
		sg.pwht_date	`PWHT完毕日期`,

		CASE WHEN sg.hd_total_count = 0 THEN '--' WHEN sg.hd_total_count = sg.hd_finished_count THEN 'Y' ELSE 'N' END AS `HD是否完毕`,
		sg.hd_date	`HD完毕日期`,

		CASE WHEN sg.ndt_total_count = 0 THEN '--' WHEN sg.ndt_total_count = sg.ndt_finished_count THEN 'Y' ELSE 'N' END AS `NDT是否完毕`,
		sg.ndt_date	`NDT完毕日期`,

		CASE WHEN sg.paint_total_count = 0 THEN '--' WHEN sg.paint_total_count = sg.paint_finished_count THEN 'Y' ELSE 'N' END AS `涂装是否完毕`,
		sg.paint_date	`涂装完毕日期`,

		CASE WHEN srp.release_predecessor_total = 0 THEN '--' WHEN srp.release_predecessor_total = srp.release_predecessor_finished THEN 'Y' ELSE 'N' END AS `具备放行条件`,
		srp.release_date	`放行日期`,
		es.project_id,
		es.org_id


FROM entity_spool es
		LEFT JOIN spool_group sg ON es.id = sg.spool_entity_id AND es.project_id = sg.project_id
		LEFT JOIN spool_release_predecessor srp ON srp.spool_entity_id = es.id AND es.project_id = srp.project_id
;
