USE `ose_tasks`;

CREATE OR REPLACE VIEW `structure_material_traceablility` AS
SELECT
				seqr.project_id `project_id`,
				seqr.org_id `org_id`,
				DATE_FORMAT(bai.end_date,"%Y/%m/%d")	`Cutting date`,
				LEFT(seqr.entity_no,LENGTH(seqr.entity_no) - LENGTH(substring_index(seqr.entity_no,'-',-1))-1)	`Tag No.`,
				''	`DWG No.`,
				seqr.cutting_no `Cutting No.`,
				seqr.program_no `Nesting drawing No.`,
				seqr.entity_no `Member`,
				seqr.material `Material`,
				seqr.height `Thickness(mm)`,
				1 `QTY`,
				seqr.material_qr_code `QR CODE`,
				mhn.cert_code `Certification No`,
				mhn.heat_no_code `Heat No`,
				mhn.batch_no_code	`Plate No`

FROM structure_entity_qr_code seqr
		LEFT JOIN bpm_activity_instance_state bai ON bai.project_id = seqr.project_id AND bai.entity_id = seqr.cutting_id
		LEFT JOIN mat_heat_no mhn ON mhn.id = seqr.heat_no_id


;

