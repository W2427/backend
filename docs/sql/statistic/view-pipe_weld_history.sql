USE `ose_tasks`;

CREATE OR REPLACE VIEW `pipe_weld_history` AS
SELECT
	ew.project_id `project_id`,
	ew.org_id			`org_id`,
	1 `No`,
	'OSE'	`Sub Contractor`,
	''	`Unit`, -- NO
	ei.module_no	`Module`, -- Module No
	ei.layer_package_no	`Deck`, -- Deck No
	ei.process_line_no	`Line Number`, -- ISO No
	ew.sheet_no	`Sheet`,
	ei.revision	`Iso Rev`,
	ew.iso_no	`ISO`, -- ISO DWG = iso no
	ei.pid_drawing	`P&ID`, -- PID DWG
	ew.spool_no	`Spool Number`,
	ei.pipe_class	`Piping Class`,
	ew.no	`Weld No`,
	''	`Category piping as per GOST 32569`, -- 不填
	''	`Piping category TR CU 032`, -- 不填
	''	`Fluid group TR CU 032`, -- 不填
	ei.operate_temperature_text	`Operation Temperature`,
	ei.operate_pressure_text	`Operation Pressure`,
	''	`Material`, -- NO
	ei.fluid	`Fluid code`,-- NO
	ew.material_code1	`Item Code No1`,
	ew.material1	`Name Item No1`,
	ew.material_code2	`Item Code No2`,
	ew.material2	`Name Item No2`,
	ew.nps 	`D-inches Item No1`,
	ew.nps_text	`Item No1 diameter`, -- 26
	''	`Thickness item No1 Identification Schedule`, -- NO
	ew.thickness	`Thickness item No1 mm`,
	ew.nps 	`D-inches Item No2`,
	ew.nps_text	`Item No2 diameter`,
	''	`Thickness item No2 Identification Schedule`, -- NO
	ew.thickness	`Thickness item No2 mm`,
	ew.material1	`Material grade No1`,
	pwl.`No1 Element Certificate` `No1 Element Certificate`,
	pwl.`Heat Number Element No1` `Heat Number Element No1`,
	ew.material2	`Material grade No2`,
	pwl.`No2 Element Certificate`,
	pwl.`Heat Number Element No2`,
	''	`Welding electrodes Type`, -- NO
	''	`Welding electrodes Mark`, -- NO
	''	`Certificate No`, -- NO
	''	`Filler rod Type`, -- NO
	''	`Filler rod Mark`, -- NO
	''	`Certificate No1`, -- NO AR
	''	`Shielding gas`, -- NO
	''	`Certificate No2`, -- NO
	''	`Flux`, -- NO
	''	`Certificate No3`, -- NO
	''	`Dinch`, -- NO
	pwl.`Fit-up Date`,
	pwl.`Fit-up Report Number`,
	pwl.`Result ACC/REJ`, -- AZ
	pwl.`Welding date`,
	pwl.`WPS No`,
	ew.weld_type `Type of weld joint`,
	ew.shop_field `Type of weld cat`,
	''	`Location of welding`, -- BE NO
	ew.display_name `Weld joint number`,
	ew.repair_count	`Repair`,
	''	`Welding method Root pass`, -- NO
	''	`Welding method Filling pass (Incl. HOT Pass)`, -- NO
	''	`Welding method Facing pass`, -- NO
	pwl.`Welder's stamp Root Pass`, -- BK
	pwl.`Welder's stamp Filling pass (incl. HOT Pass)`,
	pwl.`Welder's Stamp Facing pass`, -- BM
	pwl.`Date of PWHT`,
	''	`No Flow chart`, -- NO
	''	`PWHT Diagram Number`, -- NO
	pwl.`PWHT report`,
	''	`Hardness test report No`, -- NO BR
	pwl.`Date of visual testing`,
	pwl.`No of visual testing report`,
	pwl.`VT Result`,
	pwl.`Date of MT testing`,
	pwl.`No of MT testing report`,
	pwl.`MT Result`,
	pwl.`Date of PT testing`,
	pwl.`No of PT testing report`, -- BZ
	pwl.`PT Result`,
	pwl.`Date of UT testing`,
	pwl.`No of UT testing report`,
	pwl.`UT Result`,
	pwl.`Date of RT testing`, -- CE
	pwl.`No of RT testing report`,
	pwl.`RT Result`,
	pwl.`Date of PMI testing`,
	pwl.`No of PMI testing report`,
	pwl.`PMI Result`,
	'' `Ferrite measurement date`, -- NO
	''	`Ferrite Number`, -- NO
	''	`Ferrite Result`, -- NO
	pwl.`Rejected weld Welder stamp`, -- CN
	ei.pressure_test_package_no `Test Pack #`,
	''	`Type of pressure testing`, -- NO
	pil.`Date test (Signing TP)`,
	''	`Type of Defect`, -- NO
	''	`Size of Defect (in mm)`, -- NO
	''	`DEFECT WELDER NO`, -- NO
	''	`PENALTY DETAILS IF APPLICABLE`

FROM entity_weld ew
		LEFT JOIN entity_iso ei ON ew.iso_entity_id = ei.id
		LEFT JOIN pipe_weld_log pwl ON ew.id = pwl.entity_id
		LEFT JOIN pipe_iso_log pil ON ei.id = pil.entity_id
;
