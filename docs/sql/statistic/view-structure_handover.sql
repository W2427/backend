USE `ose_tasks`;

CREATE OR REPLACE VIEW `structure_handover` AS
SELECT
		MAX(bd.`no`)		`交接单号`,
		MAX(bde.entity_module_name)		`模块`,
		MAX(bde.entity_no)							`工作包号`,
		MAX(wwc.work_class)						`工作项代码`,
		MAX(swl.subContractor) 				`分包商`,
		MAX(swl.ReleaseSite)				`发送场地`,
		MAX(swl.HandOverSite)				`交接场地`,
		MAX(swl.ReleasePerson)					`配送人`,
		MAX(swl.ReleaseDate)							`配送日期`,
		MAX(swl.HandOverSupervisor)					`接收主管`,
		MAX(swl.HandOverDate)							`接收日期`,
		bde.project_id,
    bde.org_id


FROM bpm_delivery_entity bde

		 JOIN bpm_delivery bd ON bd.id = bde.delivery_id

		 JOIN structure_wp_log swl ON swl.project_id = bde.project_id AND swl.entity_id = bde.entity_id

		 JOIN wp_work_class wwc ON wwc.id = bde.entity_id

		WHERE	bde.status = 'ACTIVE'


		GROUP BY bde.id, bde.project_id
;

