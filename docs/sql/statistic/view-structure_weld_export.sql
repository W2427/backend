USE `ose_tasks`;

CREATE OR REPLACE VIEW `structure_weld_export` AS
SELECT
		pn.wp01_no	`模块`,
		LEFT(ew.no,LENGTH(ew.no) - LENGTH(substring_index(ew.no,'-',-1))-1)	`父级工作包`,
		ew.no	`结构焊口编号`,
		ew.hierachy_parent	`父级工作包类型`,
		ew.work_class	`工作项代码`,
		ew.welding_map_no	`焊点图纸号`,
		concat(ew.wp04_no_1,'-',ew.wp05_no_1)	`零件号1`,
		ew.thickness_1_text	`厚度1`,
		ew5.weight	`重量1(kg)`,
		concat(ew.wp04_no_2,'-',ew.wp05_no_2)	`零件号2`,
		ew.thickness_2_text	`厚度2`,
		ew52.weight	`重量2(kg)`,
		swl.all_welders	`焊工号`,
		swl.`fitup team`	`组对分包商`,
		swl.`fitup site`	`组对场地`,
		swl.`Fit-up Date`	`组对日期`,
		swl.`Fit-up End Date`	`组对完成日期`,
		swl.`weld team`	`焊接分包商`,
		swl.`weld site`	`焊接场地`,
		swl.`Welding date`	`焊接日期`,
		swl.`Welding end date`	`焊接完成日期`,
		swl.`RT Result`	`RT状态`,
		swl.`Date of RT testing`	`RT日期`,
		swl.`UT Result`	`UT状态`,
		swl.`Date of UT testing`	`UT日期`,
		swl.`MT Result`	`MT状态`,
		swl.`Date of MT testing`	`MT日期`,
		swl.`PT Result`	`PT状态`,
		swl.`Date of PT testing`	`PT日期`,
		swl.`Date of NDT testing`	`NDT日期`,
		ew.project_id,
		ew.org_id


FROM
	entity_structure_weld ew
	JOIN project_node pn ON ew.id = pn.entity_id
	LEFT JOIN structure_weld_log swl ON swl.entity_id = ew.id
	LEFT JOIN entity_wp05 ew5 ON ew5.no = concat(ew.wp04_no_1,'-',ew.wp05_no_1)
	LEFT JOIN entity_wp05 ew52 ON ew52.no = concat(ew.wp04_no_2,'-',ew.wp05_no_2)
;

