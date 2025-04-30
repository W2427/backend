USE `ose_tasks`;

CREATE OR REPLACE VIEW `spool_group` AS
SELECT
		project_id,
		spool_entity_id,
		SUM(CASE WHEN process = 'CUTTING' THEN 1 ELSE 0 END) cutting_total_count,
		SUM(CASE WHEN process = 'CUTTING' AND running_status = 'APPROVED' THEN 1 ELSE 0 END) cutting_finished_count,
		MAX(CASE WHEN process = 'CUTTING' THEN finished_at END) cutting_date,

		SUM(CASE WHEN process = 'FITUP' THEN 1 ELSE 0 END) fitup_total_count,
		SUM(CASE WHEN process = 'FITUP' AND running_status = 'APPROVED' THEN 1 ELSE 0 END) fitup_finished_count,
		MAX(CASE WHEN process = 'FITUP' THEN finished_at END) fitup_date,

		SUM(CASE WHEN process = 'WELD' THEN 1 ELSE 0 END) weld_total_count,
		SUM(CASE WHEN process = 'WELD' AND running_status = 'APPROVED' THEN 1 ELSE 0 END) weld_finished_count,
		MAX(CASE WHEN process = 'WELD' THEN finished_at END) weld_date,

		SUM(CASE WHEN process = 'PMI' THEN 1 ELSE 0 END) pmi_total_count,
		SUM(CASE WHEN process = 'PMI' AND running_status = 'APPROVED' THEN 1 ELSE 0 END) pmi_finished_count,
		MAX(CASE WHEN process = 'PMI' THEN finished_at END) pmi_date,

		SUM(CASE WHEN process = 'PWHT' THEN 1 ELSE 0 END) pwht_total_count,
		SUM(CASE WHEN process = 'PWHT' AND running_status = 'APPROVED' THEN 1 ELSE 0 END) pwht_finished_count,
		MAX(CASE WHEN process = 'PWHT' THEN finished_at END) pwht_date,


		SUM(CASE WHEN process = 'HD' THEN 1 ELSE 0 END) hd_total_count,
		SUM(CASE WHEN process = 'HD' AND running_status = 'APPROVED' THEN 1 ELSE 0 END) hd_finished_count,
		MAX(CASE WHEN process = 'HD' THEN finished_at END) hd_date,

		SUM(CASE WHEN process = 'NDT' THEN 1 ELSE 0 END) ndt_total_count,
		SUM(CASE WHEN process = 'NDT' AND running_status = 'APPROVED' THEN 1 ELSE 0 END) ndt_finished_count,
		MAX(CASE WHEN process = 'NDT' THEN finished_at END) ndt_date,

		SUM(CASE WHEN process = 'POST_PAINTING_HANDOVER' THEN 1 ELSE 0 END) paint_total_count,
		SUM(CASE WHEN process = 'POST_PAINTING_HANDOVER' AND running_status = 'APPROVED' THEN 1 ELSE 0 END) paint_finished_count,
		MAX(CASE WHEN process = 'POST_PAINTING_HANDOVER' THEN finished_at END) paint_date

FROM wbs_entry

WHERE deleted = 0 AND active = 1 AND NOT ISNULL(spool_entity_id)

GROUP BY spool_entity_id, project_id
;
