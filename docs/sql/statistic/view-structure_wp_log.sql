USE `ose_tasks`;

CREATE OR REPLACE VIEW `structure_wp_log` AS
SELECT
		entity_id entity_id,
		project_id,
		MAX(CASE process WHEN 'HANDOVER_WORKSTATION' THEN performed_at END) `HandOverDate`,
		MAX(CASE process WHEN 'HANDOVER_WORKSTATION' THEN work_site END) `HandOverSite`,
		MAX(CASE process WHEN 'HANDOVER_WORKSTATION' THEN executors END) `HandOverSupervisor`,
		MAX(CASE process WHEN 'HANDOVER_WORKSTATION' THEN work_team END) `subContractor`,
		MAX(CASE process WHEN 'RELEASE' THEN work_site END) `ReleaseSite`,
		MAX(CASE process WHEN 'RELEASE' THEN executors END) `ReleasePerson`,
		MAX(CASE process WHEN 'RELEASE' THEN qc END) `ReleaseQc`,
		MAX(CASE process WHEN 'RELEASE' THEN work_team END) `ReleaseTeam`,
		MAX(CASE process WHEN 'RELEASE' THEN ex_insp_status END) `inspResult`,
		MAX(CASE process WHEN 'RELEASE' THEN performed_at END) `ReleaseDate`


FROM structure_construction_log

WHERE entity_type IN ('WP02','WP03','WP04') AND process IN ('HANDOVER_WORKSTATION','RELEASE')
GROUP BY entity_id, project_id
;

