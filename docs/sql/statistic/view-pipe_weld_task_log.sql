USE `ose_tasks`;

CREATE OR REPLACE VIEW `pipe_weld_task_log` AS
SELECT
		pcl.entity_id entity_id,
		MAX(CASE process WHEN 'FITUP' THEN baim.heat_no_code1 END) `HeatNo1`,
		MAX(CASE process WHEN 'FITUP' THEN baim.heat_no_code2 END) `HeatNo2`,
		MAX(CASE process WHEN 'FITUP' THEN baim.tag_number1 END) `material1`,
		MAX(CASE process WHEN 'FITUP' THEN baim.tag_number2 END) `material2`,
		MAX(CASE process WHEN 'FITUP' THEN performed_at END) `FitupDate`,
		MAX(CASE process WHEN 'FITUP' THEN report_nos END) `FitupReportNo`,
		MAX(CASE
						WHEN process = 'FITUP' AND test_result = 'FINISHED' THEN 'FINISHED'
						WHEN process = 'FITUP' AND test_result = 'UNFINISHED' THEN brt.name_
						ELSE 'NOT_START'
				END) `FitupStatus`,
		MAX(CASE
						WHEN process = 'FITUP' AND test_result = 'FINISHED' THEN pcl.finished_at
						WHEN process = 'FITUP' AND test_result = 'UNFINISHED' THEN brt.create_time_
						ELSE '--'
				END) `FitupCurrentDate`,
		MAX(CASE process WHEN 'WELD' THEN performed_at END) `WeldDate`,
		GROUP_CONCAT(DISTINCT CASE process WHEN 'WELD' THEN performed_at END SEPARATOR ',') `wpsNos`,
		MAX(CASE process WHEN 'WELD' THEN report_nos END) `WeldReportNo`,
		MAX(CASE
						WHEN process = 'WELD' AND test_result = 'FINISHED' THEN 'FINISHED'
						WHEN process = 'WELD' AND test_result = 'UNFINISHED' THEN brt.name_
						ELSE 'NOT_START'
				END) `WeldStatus`,
		MAX(CASE
						WHEN process = 'WELD' AND test_result = 'FINISHED' THEN pcl.finished_at
						WHEN process = 'WELD' AND test_result = 'UNFINISHED' THEN brt.create_time_
						ELSE '--'
				END) `WeldCurrentDate`,
		MAX(CASE process WHEN 'RT' THEN performed_at END) `RtDate`,
		MAX(CASE process WHEN 'RT' THEN report_nos END) `RtReportNo`,
		MAX(CASE
						WHEN process = 'RT' AND test_result = 'FINISHED' THEN 'FINISHED'
						WHEN process = 'RT' AND test_result = 'UNFINISHED' THEN brt.name_
						ELSE 'NOT_START'
				END) `RtStatus`,
		MAX(CASE process WHEN 'UT' THEN performed_at END) `UtDate`,
		MAX(CASE process WHEN 'UT' THEN report_nos END) `UtReportNo`,
		MAX(CASE
						WHEN process = 'UT' AND test_result = 'FINISHED' THEN 'FINISHED'
						WHEN process = 'UT' AND test_result = 'UNFINISHED' THEN brt.name_
						ELSE 'NOT_START'
				END) `UtStatus`,
		MAX(CASE process WHEN 'MT' THEN performed_at END) `MtDate`,
		MAX(CASE process WHEN 'MT' THEN report_nos END) `MtReportNo`,
		MAX(CASE
						WHEN process = 'MT' AND test_result = 'FINISHED' THEN 'FINISHED'
						WHEN process = 'MT' AND test_result = 'UNFINISHED' THEN brt.name_
						ELSE 'NOT_START'
				END) `MtStatus`,
		MAX(CASE process WHEN 'PT' THEN performed_at END) `PtDate`,
		MAX(CASE process WHEN 'PT' THEN report_nos END) `PtReportNo`,
		MAX(CASE
						WHEN process = 'PT' AND test_result = 'FINISHED' THEN 'FINISHED'
						WHEN process = 'PT' AND test_result = 'UNFINISHED' THEN brt.name_
						ELSE 'NOT_START'
				END) `PtStatus`,

		MAX(CASE
						WHEN process = 'PWHT' AND test_result = 'FINISHED' THEN 'FINISHED'
						WHEN process = 'PWHT' AND test_result = 'UNFINISHED' THEN brt.name_
						ELSE 'NOT_START'
				END) `PwhtStatus`,
		MAX(CASE process WHEN 'PWHT' THEN performed_at END) `PwhtDate`,
		MAX(CASE process WHEN 'PWHT' THEN report_nos END) `PwhtReportNo`,

		MAX(CASE
						WHEN process = 'HD' AND test_result = 'FINISHED' THEN 'FINISHED'
						WHEN process = 'HD' AND test_result = 'UNFINISHED' THEN brt.name_
						ELSE 'NOT_START'
				END) `HdStatus`,
		MAX(CASE process WHEN 'HD' THEN performed_at END) `HdDate`,

		MAX(CASE process WHEN 'PMI' THEN performed_at END) `PmiDate`,
		MAX(CASE process WHEN 'PMI' THEN report_nos END) `PmiReport`,
		MAX(CASE
						WHEN process = 'PMI' AND test_result = 'FINISHED' THEN 'FINISHED'
						WHEN process = 'PMI' AND test_result = 'UNFINISHED' THEN brt.name_
						ELSE 'NOT_START'
				END) `PmiStatus`,
		pcl.project_id,
		pcl.org_id


FROM piping_construction_log pcl
		LEFT JOIN bpm_ru_task brt ON brt.act_inst_id = pcl.act_inst_id
		LEFT JOIN bpm_act_inst_material baim ON baim.heat_no_id1 = pcl.heat_no_id1 AND baim.heat_no_id2 = pcl.heat_no_id2

WHERE entity_type = 'WELD_JOINT'
GROUP BY entity_id, project_id, org_id
;

