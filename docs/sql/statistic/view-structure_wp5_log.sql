USE `ose_tasks`;

CREATE OR REPLACE VIEW `structure_wp05_log` AS
SELECT
		entity_id entity_id,
		project_id,
		MAX(CASE process WHEN 'CUTTING' THEN performed_at END) `cuttingDate`,
		MAX(CASE process WHEN 'CUTTING' THEN work_site END) `cuttingSite`,
		MAX(CASE process WHEN 'CUTTING' THEN test_result END) `cuttingResult`,
		MAX(CASE WHEN process IN ('DISTRIBUTION_TO_FABRICATION', 'DISTRIBUTION_TO_ASSEMBLY') THEN performed_at END) `distributionDate`,
		MAX(CASE WHEN process IN ('DISTRIBUTION_TO_FABRICATION', 'DISTRIBUTION_TO_ASSEMBLY') THEN test_result END) `distributionResult`


FROM structure_construction_log

WHERE entity_type = 'WP05' AND process IN ('CUTTING','DISTRIBUTION_TO_FABRICATION', 'DISTRIBUTION_TO_ASSEMBLY')
GROUP BY entity_id, project_id
;

