USE `ose_tasks`;

CREATE OR REPLACE VIEW `pipe_welder_subcontractor` AS
SELECT
		wwr.weld_id,
		GROUP_CONCAT(DISTINCT sc.name SEPARATOR '/') subContractors,
		wwr.project_id,
		GROUP_CONCAT(DISTINCT wwr.welder_no SEPARATOR '/') welder
-- 		w.user_id,
-- 		w.name,
-- 		wwr.weld_no,
-- 		wwr.welder_no-- ,
-- 		org.type
-- 		GROUP_CONCAT(DISTINCT org.name SEPARATOR '/')

FROM weld_welder_relation wwr
		LEFT JOIN welder_simplified w ON w.id = wwr.welder_id
		LEFT JOIN subcon sc ON sc.id = w.sub_con_id AND sc.status = 'ACTIVE'
-- 		LEFT JOIN ose_auth.organizations org ON org.id = uor.organization_id AND org.`status` = 'ACTIVE' AND org.type = 'PROJECT'

GROUP BY wwr.weld_id, wwr.project_id
;

