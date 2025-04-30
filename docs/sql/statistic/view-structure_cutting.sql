USE `ose_tasks`;

CREATE OR REPLACE VIEW `structure_cutting` AS
SELECT
	swl.cuttingDate  `下料日期`,
	swl.distributionDate	`配送日期`,
	wp05.no			`零件号`,
	pn.wp04_no						`父级工作包`,
	bsct.`no`							`Cutting No`,
	wp05.revision					`版本`,
	swl.cuttingSite				`场地`,
	wp05.work_class				`工作项代码`,
	wp05.member_size			`部件型号`,
	wp05.material					`材质`,
	bsce.heat_no						`炉批号`,
	wp05.weight_text			`重量`,
	swl.cuttingResult			`下料状态`,
	swl.distributionResult	`配送状态`,
	wp05.project_id,
	wp05.org_id


FROM
		entity_wp05 wp05
		LEFT JOIN bpm_structure_cutting_task_program_entity bsce ON wp05.id = bsce.entity_id AND wp05.project_id = bsce.project_id

		LEFT JOIN bpm_structure_cutting_task bsct ON bsct.id = bsce.cutting_id

		LEFT JOIN project_node pn ON pn.entity_id = wp05.id

		LEFT JOIN structure_wp05_log swl ON swl.entity_id = wp05.id

		WHERE NOT ISNULL(bsce.id)

		ORDER BY wp05.no
;

