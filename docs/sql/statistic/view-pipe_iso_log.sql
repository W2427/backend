USE `ose_tasks`;

CREATE OR REPLACE VIEW `pipe_iso_log` AS
SELECT
		pcl.entity_id entity_id,
		MAX(CASE process WHEN 'PRESSURE_TEST' THEN last_modified_at END ) `Date test (Signing TP)`

-- 	''	`Date test (Signing TP)`,
FROM piping_construction_log pcl

WHERE entity_type = 'ISO'
GROUP BY entity_id
;

