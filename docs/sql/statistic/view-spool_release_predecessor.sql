USE `ose_tasks`;

CREATE OR REPLACE VIEW `spool_release_predecessor` AS
SELECT
    se.project_id     			AS project_id,
    se.entity_id    AS spool_entity_id,
    MAX(se.finished_at) AS release_date,
    SUM(CASE WHEN (pe.running_status = 'APPROVED' or r.optional) THEN 1 ELSE 0 END)
             AS release_predecessor_finished,
    COUNT(0) AS release_predecessor_total
  FROM
    wbs_entry AS se
		JOIN wbs_entry_relation AS r
      ON r.project_id = se.project_id
      AND r.successor_id = se.guid
    JOIN wbs_entry AS pe
      ON pe.project_id = se.project_id
      AND pe.guid = r.predecessor_id
      AND pe.deleted = 0
      AND pe.active = 1
  WHERE
    se.deleted = 0
    AND se.active = 1
    AND se.process = 'SPOOL_RELEASE'
  GROUP BY
    se.entity_id,se.project_id
;
