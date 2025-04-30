USE `ose_tasks`;

CREATE OR REPLACE VIEW `structure_release` AS
SELECT
		MAX(bd.`no`)		`放行单号`,
		max(bde.entity_module_name)		`模块`,
		max(bde.entity_no)						`工作包号`,
		max(wwc.WP)										`层级标识`,
		max(wwc.work_class)						`工作项代码`,
		group_concat(distinct swl.ReleaseTeam separator '/')						`分包商`,
		group_concat(distinct swl.ReleaseSite separator '/')				`放行场地`,
		max(swl.ReleasePerson)				`申请放行主管`,
		max(swl.ReleaseDate)				 `申请放行日期`,
		max(swl.ReleaseQc)					`QC`,
		max(swl.inspResult)	`QC确认结果`,
		max(swl.releaseDate)				 `QC确认日期`,
		bd.project_id,
		bd.org_id

FROM bpm_delivery_entity bde

		 JOIN bpm_delivery bd ON bd.id = bde.delivery_id

		 JOIN structure_wp_log swl ON swl.entity_id = bde.entity_id

		 JOIN wp_work_class wwc ON wwc.id = bde.entity_id


		WHERE	bde.status = 'ACTIVE' AND bde.delivery_id IS NOT NULL


		GROUP BY bde.id, bde.project_id

		ORDER BY `放行单号`
;

