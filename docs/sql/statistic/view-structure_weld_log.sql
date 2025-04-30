USE `ose_tasks`;

CREATE OR REPLACE VIEW `structure_weld_log` AS
SELECT
		scl.entity_id entity_id,
		MAX(CASE process WHEN 'FITUP' THEN mhn1.cert_code END) `No1 Element Certificate`,
		MAX(CASE process WHEN 'FITUP' THEN concat(mhn1.heat_no_code,'/',mhn1.batch_no_code) END) `Heat Number Element No1`,
		MAX(CASE process WHEN 'FITUP' THEN mhn2.cert_code END) `No2 Element Certificate`,
		MAX(CASE process WHEN 'FITUP' THEN concat(mhn2.heat_no_code,'/',mhn2.batch_no_code) END) `Heat Number Element No2`,
		MAX(CASE process WHEN 'FITUP' THEN performed_at END) `Fit-up Date`,
		MAX(CASE process WHEN 'FITUP' THEN finished_at END) `Fit-up End Date`,
		MAX(CASE process WHEN 'FITUP' THEN report_nos END) `Fit-up Report Number`,
		MAX(CASE process WHEN 'FITUP' THEN ex_insp_status END) `Result ACC/REJ`, -- AZ
		MAX(CASE process WHEN 'FITUP' THEN work_team END) `fitup team`, -- AZ
		MAX(CASE process WHEN 'FITUP' THEN work_site END) `fitup site`, -- AZ


		MAX(CASE process WHEN 'WELD' THEN performed_at END) `Welding date`,
		MAX(CASE process WHEN 'WELD' THEN finished_at END) `Welding end date`,
		MAX(CASE process WHEN 'WELD' THEN wps_nos END) `WPS No`,
		MAX(CASE process WHEN 'WELD' THEN json_extract(executors,'$.ALL') END) `all_welders`,
		MAX(CASE process WHEN 'WELD' THEN json_extract(executors,'$.A') END) `Welder's stamp Root Pass`,
		MAX(CASE process WHEN 'WELD' THEN json_extract(executors,'$.B') END) `Welder's stamp Filling pass (incl. HOT Pass)`, -- AZ
		MAX(CASE process WHEN 'WELD' THEN json_extract(executors,'$.C') END) `Welder's Stamp Facing pass`, -- AZ
		MAX(CASE process WHEN 'WELD' THEN work_team END) `weld team`, -- AZ
		MAX(CASE process WHEN 'WELD' THEN work_site END) `weld site`, -- AZ

		MAX(CASE process WHEN 'PWHT' THEN performed_at END) `Date of PWHT`,
		MAX(CASE process WHEN 'PWHT' THEN report_nos END) `PWHT Report`,
		MAX(CASE process WHEN 'WELD' THEN performed_at END) `Date of visual testing`,
		MAX(CASE process WHEN 'WELD' THEN report_nos END) `No of visual testing report`,
		MAX(CASE process WHEN 'WELD' THEN ex_insp_status END) `VT Result`, -- AZ
		MAX(CASE process WHEN 'MT' THEN performed_at END) `Date of MT testing`,
		MAX(CASE process WHEN 'MT' THEN report_nos END) `No of MT testing report`,
		MAX(CASE process WHEN 'MT' THEN ex_insp_status END) `MT Result`,
		MAX(CASE process WHEN 'PT' THEN performed_at END) `Date of PT testing`,
		MAX(CASE process WHEN 'PT' THEN report_nos END) `No of PT testing report`,
		MAX(CASE process WHEN 'PT' THEN ex_insp_status END) `PT Result`,
		MAX(CASE process WHEN 'UT' THEN performed_at END) `Date of UT testing`,
		MAX(CASE process WHEN 'UT' THEN report_nos END) `No of UT testing report`,
		MAX(CASE process WHEN 'UT' THEN ex_insp_status END) `UT Result`,

		MAX(CASE process WHEN 'RT' THEN performed_at END) `Date of RT testing`,
		MAX(CASE process WHEN 'RT' THEN report_nos END) `No of RT testing report`,
		MAX(CASE process WHEN 'RT' THEN ex_insp_status END) `RT Result`,

		MAX(CASE process WHEN 'PMI' THEN performed_at END) `Date of PMI testing`,
		MAX(CASE process WHEN 'PMI' THEN report_nos END) `No of PMI testing report`,
		MAX(CASE process WHEN 'PMI' THEN ex_insp_status END) `PMI Result`,

		MAX(CASE process WHEN 'NDT' THEN finished_at END) `Date of NDT testing`,
		MAX(CASE process WHEN 'NDT' THEN report_nos END) `No of NDT testing report`,
		MAX(CASE process WHEN 'NDT' THEN ex_insp_status END) `NDT Result`,

		MAX(CASE process WHEN 'WELD' THEN json_extract(executors,'$.FAILED') END) `Rejected weld Welder stamp`


FROM structure_construction_log scl
		LEFT JOIN mat_heat_no mhn1 ON scl.heat_no_id1 = mhn1.id
		LEFT JOIN mat_heat_no mhn2 ON scl.heat_no_id2 = mhn2.id

WHERE entity_type = 'STRUCT_WELD_JOINT'
GROUP BY entity_id
;

