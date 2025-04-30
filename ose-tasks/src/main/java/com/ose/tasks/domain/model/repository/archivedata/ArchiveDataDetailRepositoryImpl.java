package com.ose.tasks.domain.model.repository.archivedata;

import java.util.List;
import java.util.Map;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import com.ose.vo.BpmTaskDefKey;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.archivedata.ArchiveDataCriteriaDTO;

public class ArchiveDataDetailRepositoryImpl extends BaseRepository implements ArchiveDataDetailRepositoryCustom {

    public enum InspectionType {
        EX_INSPECTION, IN_INSPECTION;
    }

    public enum QualifiedType {
        QUALIFIED, UN_QUALIFIED;
    }

    @Override
    public List<Map<String, Object>> getWbsPassRateProgressFpyQualified(
        Long projectId,
        ArchiveDataCriteriaDTO criteriaDTO,
        InspectionType inspectionType,
        QualifiedType qualifiedType
    ) {
        EntityManager entityManager = getEntityManager();
        StringBuffer sql = new StringBuffer()
            .append("SELECT ")
            .append("  * ")
            .append("FROM")
            .append("  (SELECT ")
            .append("    `t1`.`entity_module_name` AS `module_name`,")
            .append("    `t1`.`process_stage_name_cn` AS `stage`,")
            .append("    `t1`.`a.process_name` AS `process`,")
            .append("    `t1`.`team_name` AS `team_name`,")
            .append("    YEAR(`t1`.`end_date`) AS group_year,")
            .append("    MONTH(`t1`.`end_date`) AS group_month,")
            .append("    DAY(`t1`.`end_date`) AS group_day,")
            .append("    WEEK_OF_YEAR (`t1`.`end_date`) AS group_week,")
            .append("    t1.`entity_no`")
            .append("  FROM")
            .append("    bpm_activity_instance_base AS `t1`  JOIN bpm_activity_instance_state AS `t2` ON `t1`.`project_id` = `t2`.`project_id` AND `t1`.`id` = `t2`.`bai_id`")
            .append("  WHERE `t1`.`project_id` = :projectId ");

        if (inspectionType == InspectionType.IN_INSPECTION
            && qualifiedType == QualifiedType.QUALIFIED) {
            sql.append("    AND t2.`internal_inspection_fpy` = 1) AS t ");
        } else if (inspectionType == InspectionType.IN_INSPECTION
            && qualifiedType == QualifiedType.UN_QUALIFIED) {
            sql.append("    AND t2.`internal_inspection_fpy` = 2) AS t ");
        } else if (inspectionType == InspectionType.EX_INSPECTION
            && qualifiedType == QualifiedType.QUALIFIED) {
            sql.append("    AND t2.`external_inspection_fpy` = 1) AS t ");
        } else if (inspectionType == InspectionType.EX_INSPECTION
            && qualifiedType == QualifiedType.UN_QUALIFIED) {
            sql.append("    AND t2.`external_inspection_fpy` = 2) AS t ");
        } else {
            sql.append("    ) AS t ");
        }

        sql.append("WHERE 1 = 1 ");

        if (criteriaDTO.getModule() != null
            && !"".equals(criteriaDTO.getModule())) {
            sql.append("  AND t.module_name = :module ");
        }

        if (criteriaDTO.getStage() != null
            && !"".equals(criteriaDTO.getStage())) {
            sql.append("  AND t.stage = :stage ");
        }

        if (criteriaDTO.getProcess() != null
            && !"".equals(criteriaDTO.getProcess())) {
            sql.append("  AND t.process = :process ");
        }

        if (criteriaDTO.getTeamName() != null
            && !"".equals(criteriaDTO.getTeamName())) {
            sql.append("  AND t.team_name = :teamName ");
        }

        if (criteriaDTO.getGroupYear() != null) {
            sql.append("  AND t.group_year = :groupYear ");
        }

        if (criteriaDTO.getGroupMonth() != null) {
            sql.append("  AND t.group_month = :groupMonth ");
        }

        if (criteriaDTO.getGroupDay() != null) {
            sql.append("  AND t.group_day = :groupDay ");
        }

        if (criteriaDTO.getGroupWeek() != null) {
            sql.append("  AND t.group_week = :groupWeek ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);

        if (criteriaDTO.getModule() != null
            && !"".equals(criteriaDTO.getModule())) {
            query.setParameter("module", criteriaDTO.getModule());
        }

        if (criteriaDTO.getStage() != null
            && !"".equals(criteriaDTO.getStage())) {
            query.setParameter("stage", criteriaDTO.getStage());
        }

        if (criteriaDTO.getProcess() != null
            && !"".equals(criteriaDTO.getProcess())) {
            query.setParameter("process", criteriaDTO.getProcess());
        }

        if (criteriaDTO.getTeamName() != null
            && !"".equals(criteriaDTO.getTeamName())) {
            query.setParameter("teamName", criteriaDTO.getTeamName());
        }

        if (criteriaDTO.getGroupYear() != null) {
            query.setParameter("groupYear", criteriaDTO.getGroupYear());
        }

        if (criteriaDTO.getGroupMonth() != null) {
            query.setParameter("groupMonth", criteriaDTO.getGroupMonth());
        }

        if (criteriaDTO.getGroupDay() != null) {
            query.setParameter("groupDay", criteriaDTO.getGroupDay());
        }

        if (criteriaDTO.getGroupWeek() != null) {
            query.setParameter("groupWeek", criteriaDTO.getGroupYear() * 100 + criteriaDTO.getGroupWeek());
        }

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        return list;
    }

    @Override
    public List<Map<String, Object>> getWbsWeldRateProgress(
        Long projectId,
        ArchiveDataCriteriaDTO criteriaDTO,
        QualifiedType qualified
    ) {
        EntityManager entityManager = getEntityManager();
        StringBuffer sql = new StringBuffer()
            .append("SELECT ")
            .append("  * ")
            .append("FROM")
            .append("  (SELECT ")
            .append("    tt.*,")
            .append("    YEAR(`tt`.`group_date`) AS group_year,")
            .append("    MONTH(`tt`.`group_date`) AS group_month,")
            .append("    DAY(`tt`.`group_date`) AS group_day,")
            .append("    WEEK_OF_YEAR (`tt`.`group_date`) AS group_week ")
            .append("  FROM")
            .append("    (SELECT ")
            .append("      `wbs`.`sector` AS `module`,")
            .append("      `wbs`.`stage` AS `stage`,")
            .append("      `w`.`material_group_code` AS `entity_material`,")
            .append("      `org`.`name` AS `team_name`,")
            .append("      `w`.`weld_type` AS `weld_type`,")
            .append("      `welder`.`no` AS `welder_no`,")
            .append("      w.`nps` AS `nps`,")
            .append("      act.`entity_no`,")
            .append("      MIN(`act`.`end_date`) AS `group_date` ")
            .append("    FROM")
            .append("      `entity_weld` AS `w` ")
            .append("      JOIN `wbs_entry` `wbs` ")
            .append("        ON `w`.`project_id` = `wbs`.`project_id` ")
            .append("        AND `w`.`id` = `wbs`.`entity_id` ")
            .append("        AND `wbs`.`process` = 'WELD' ")
            .append("      JOIN `ose_auth`.`organizations` AS `org` ")
            .append("        ON `wbs`.`team_id` = `org`.`id` ")
            .append("      JOIN `welder` AS `welder` ")
            .append("        ON `w`.`project_id` = `welder`.`project_id` ")
            .append("        AND `w`.`welder_id` = `welder`.`id` ")
            .append("      JOIN `bpm_activity_instance_base` AS `act` ")
            .append("        ON `w`.`project_id` = `act`.`project_id` ")
            .append("        AND `w`.`id` = `act`.`entity_id` ")
            .append("        AND `act`.`a.process_name` = 'WELD' ")
            .append("      JOIN `bpm_activity_instance_state` AS `act1s` ")
            .append("        ON `act`.`project_id` = 'act1s`.`project_id` AND `act`.`id` = `act1s`.`bai_id` ");



        if (qualified == QualifiedType.QUALIFIED) {
            sql.append("        AND `act1s`.`external_inspection_fpy` = 1 ");
        } else if (qualified == QualifiedType.UN_QUALIFIED) {
            sql.append("        AND `act1s`.`external_inspection_fpy` = 2 ");
        } else {
            sql.append("        AND `act`.`external_inspection_fpy` <> 0 ");
        }

        sql.append("        AND `act1s`.`end_date` IS NOT NULL ")
            .append("    WHERE `w`.`project_id` = :projectId ")
            .append("      AND EXISTS ")
            .append("      (SELECT ")
            .append("        `id` ")
            .append("      FROM")
            .append("        `bpm_activity_instance_bai` `act2`, `bpm_activity_instance_state` `acts` ")
            .append("      WHERE `act2`.`project_id` = `w`.`project_id` ")
            .append("        AND `act2`.`entity_id` = `w`.`id` ")
            .append("        AND `acts`.`project_id` = `acts`.`project_id` ")
            .append("        AND `acts`.`bai_id` = `act2`.`id` ");

        if (qualified == QualifiedType.QUALIFIED) {
            sql.append("        AND `acts`.`external_inspection_fpy` = 1 ");
        } else if (qualified == QualifiedType.UN_QUALIFIED) {
            sql.append("        AND `acts`.`external_inspection_fpy` = 2 ");
        } else {
            sql.append("        AND `acts`.`external_inspection_fpy` <> 0 ");
        }

        sql.append("        AND `acts`.`end_date` IS NOT NULL) ")
            .append("    GROUP BY `module`,")
            .append("      `stage`,")
            .append("      `entity_material`,")
            .append("      `team_name`,")
            .append("      `weld_type`,")
            .append("      `welder_no`,")
            .append("      `nps`,")
            .append("      entity_no) AS tt) AS t ")
            .append("WHERE 1 = 1 ");

        if (criteriaDTO.getModule() != null
            && !"".equals(criteriaDTO.getModule())) {
            sql.append("  AND t.module_name = :module ");
        }

        if (criteriaDTO.getStage() != null
            && !"".equals(criteriaDTO.getStage())) {
            sql.append("  AND t.stage = :stage ");
        }

        if (criteriaDTO.getEntityMaterial() != null
            && !"".equals(criteriaDTO.getEntityMaterial())) {
            sql.append("  AND t.entity_material = :entityMaterial ");
        }

        if (criteriaDTO.getTeamName() != null
            && !"".equals(criteriaDTO.getTeamName())) {
            sql.append("  AND t.team_name = :teamName ");
        }

        if (criteriaDTO.getWeldType() != null
            && !"".equals(criteriaDTO.getWeldType())) {
            sql.append("  AND t.weld_type = :weldType ");
        }

        if (criteriaDTO.getWelderNo() != null
            && !"".equals(criteriaDTO.getWelderNo())) {
            sql.append("  AND t.welder_no = :welderNo ");
        }

        if (criteriaDTO.getGroupYear() != null) {
            sql.append("  AND t.group_year = :groupYear ");
        }

        if (criteriaDTO.getGroupMonth() != null) {
            sql.append("  AND t.group_month = :groupMonth ");
        }

        if (criteriaDTO.getGroupDay() != null) {
            sql.append("  AND t.group_day = :groupDay ");
        }

        if (criteriaDTO.getGroupWeek() != null) {
            sql.append("  AND t.group_week = :groupWeek ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);

        if (criteriaDTO.getModule() != null
            && !"".equals(criteriaDTO.getModule())) {
            query.setParameter("module", criteriaDTO.getModule());
        }

        if (criteriaDTO.getStage() != null
            && !"".equals(criteriaDTO.getStage())) {
            query.setParameter("stage", criteriaDTO.getStage());
        }

        if (criteriaDTO.getEntityMaterial() != null
            && !"".equals(criteriaDTO.getEntityMaterial())) {
            query.setParameter("entityMaterial", criteriaDTO.getEntityMaterial());
        }

        if (criteriaDTO.getTeamName() != null
            && !"".equals(criteriaDTO.getTeamName())) {
            query.setParameter("teamName", criteriaDTO.getTeamName());
        }

        if (criteriaDTO.getWeldType() != null
            && !"".equals(criteriaDTO.getWeldType())) {
            query.setParameter("weldType", criteriaDTO.getWeldType());
        }

        if (criteriaDTO.getWelderNo() != null
            && !"".equals(criteriaDTO.getWelderNo())) {
            query.setParameter("welderNo", criteriaDTO.getWelderNo());
        }

        if (criteriaDTO.getGroupYear() != null) {
            query.setParameter("groupYear", criteriaDTO.getGroupYear());
        }

        if (criteriaDTO.getGroupMonth() != null) {
            query.setParameter("groupMonth", criteriaDTO.getGroupMonth());
        }

        if (criteriaDTO.getGroupDay() != null) {
            query.setParameter("groupDay", criteriaDTO.getGroupDay());
        }

        if (criteriaDTO.getGroupWeek() != null) {
            query.setParameter("groupWeek", criteriaDTO.getGroupYear() * 100 + criteriaDTO.getGroupWeek());
        }

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        return list;
    }

    @Override
    public List<Map<String, Object>> getWbsNdtRateProgress(
        Long projectId,
        ArchiveDataCriteriaDTO criteriaDTO,
        QualifiedType qualified
    ) {
        EntityManager entityManager = getEntityManager();
        StringBuffer sql = new StringBuffer()
            .append("SELECT ")
            .append("  * ")
            .append("FROM")
            .append("  (SELECT ")
            .append("    tt.*,")
            .append("    YEAR(`tt`.`group_date`) AS group_year,")
            .append("    MONTH(`tt`.`group_date`) AS group_month,")
            .append("    DAY(`tt`.`group_date`) AS group_day,")
            .append("    WEEK_OF_YEAR (`tt`.`group_date`) AS group_week ")
            .append("  FROM")
            .append("    (SELECT ")
            .append("      `wbs`.`sector` AS `module`,")
            .append("      `wbs`.`stage` AS `stage`,")
            .append("      `w`.`material_group_code` AS `entity_material`,")
            .append("      `org`.`name` AS `team_name`,")
            .append("      `w`.`nde` AS `ndt_type`,")
            .append("      `welder`.`no` AS `welder_no`,")
            .append("      `welder`.`name` AS `welder_name`,")
            .append("      w.`nps` AS `nps`,")
            .append("      act.`entity_no`,")
            .append("      `act`.`end_date` AS `group_date` ")
            .append("    FROM")
            .append("      `entity_weld` AS `w` ")
            .append("      JOIN `wbs_entry` `wbs` ")
            .append("        ON `w`.`project_id` = `wbs`.`project_id` ")
            .append("        AND `w`.`id` = `wbs`.`entity_id` ")
            .append("        AND `wbs`.`process` = 'NDT' ")
            .append("      JOIN `ose_auth`.`organizations` AS `org` ")
            .append("        ON `wbs`.`team_id` = `org`.`id` ")
            .append("      JOIN `welder` AS `welder` ")
            .append("        ON `w`.`project_id` = `welder`.`project_id` ")
            .append("        AND `w`.`welder_id` = `welder`.`id` ")
            .append("      JOIN `bpm_activity_instance_base` AS `act` ")
            .append("        ON `w`.`project_id` = `act`.`project_id` ")
            .append("        AND `w`.`id` = `act`.`entity_id` ")
            .append("        AND `act`.`a.process_name` = 'NDT' ")
            .append("      JOIN `bpm_hi_taskinst` AS `hi`")
            .append("        ON `act`.`act_inst_id` = `hi`.`act_inst_id`")
            .append("    WHERE `w`.`project_id` = :projectId ");

        if (qualified == QualifiedType.QUALIFIED) {
            sql.append("      AND hi.`task_def_key_` = '" + BpmTaskDefKey.USERTASK_QC_UPLOAD_REPORT.getType() + "' ");
        } else if (qualified == QualifiedType.UN_QUALIFIED) {
            sql.append("      AND hi.`task_def_key_` = '" + BpmTaskDefKey.USERTASK_QC_UPLOAD_NG_REPORT.getType() + "' ");
        } else {
            sql.append("      AND (hi.`task_def_key_` = '" + BpmTaskDefKey.USERTASK_QC_UPLOAD_REPORT.getType() + "' OR hi.`task_def_key_` = '" + BpmTaskDefKey.USERTASK_QC_UPLOAD_NG_REPORT.getType() + "') ");
        }

        sql.append("    GROUP BY `group_date`,")
            .append("      `module`,")
            .append("      `stage`,")
            .append("      `entity_material`,")
            .append("      `team_name`,")
            .append("      `ndt_type`,")
            .append("      `welder_id`,")
            .append("      `welder_name`,")
            .append("      `nps`,")
            .append("      entity_no) AS tt) AS t ")
            .append("WHERE 1 = 1 ");

        if (criteriaDTO.getModule() != null
            && !"".equals(criteriaDTO.getModule())) {
            sql.append("  AND t.module_name = :module ");
        }

        if (criteriaDTO.getStage() != null
            && !"".equals(criteriaDTO.getStage())) {
            sql.append("  AND t.stage = :stage ");
        }

        if (criteriaDTO.getEntityMaterial() != null
            && !"".equals(criteriaDTO.getEntityMaterial())) {
            sql.append("  AND t.entity_material = :entityMaterial ");
        }

        if (criteriaDTO.getTeamName() != null
            && !"".equals(criteriaDTO.getTeamName())) {
            sql.append("  AND t.team_name = :teamName ");
        }

        if (criteriaDTO.getNdtType() != null
            && !"".equals(criteriaDTO.getNdtType())) {
            sql.append("  AND t.ndt_type = :ndtType ");
        }

        if (criteriaDTO.getWelderNo() != null
            && !"".equals(criteriaDTO.getWelderNo())) {
            sql.append("  AND t.welder_no = :welderNo ");
        }

        if (criteriaDTO.getGroupYear() != null) {
            sql.append("  AND t.group_year = :groupYear ");
        }

        if (criteriaDTO.getGroupMonth() != null) {
            sql.append("  AND t.group_month = :groupMonth ");
        }

        if (criteriaDTO.getGroupDay() != null) {
            sql.append("  AND t.group_day = :groupDay ");
        }

        if (criteriaDTO.getGroupWeek() != null) {
            sql.append("  AND t.group_week = :groupWeek ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);

        if (criteriaDTO.getModule() != null
            && !"".equals(criteriaDTO.getModule())) {
            query.setParameter("module", criteriaDTO.getModule());
        }

        if (criteriaDTO.getStage() != null
            && !"".equals(criteriaDTO.getStage())) {
            query.setParameter("stage", criteriaDTO.getStage());
        }

        if (criteriaDTO.getEntityMaterial() != null
            && !"".equals(criteriaDTO.getEntityMaterial())) {
            query.setParameter("entityMaterial", criteriaDTO.getEntityMaterial());
        }

        if (criteriaDTO.getTeamName() != null
            && !"".equals(criteriaDTO.getTeamName())) {
            query.setParameter("teamName", criteriaDTO.getTeamName());
        }

        if (criteriaDTO.getNdtType() != null
            && !"".equals(criteriaDTO.getNdtType())) {
            query.setParameter("ndtType", criteriaDTO.getNdtType());
        }

        if (criteriaDTO.getWelderNo() != null
            && !"".equals(criteriaDTO.getWelderNo())) {
            query.setParameter("welderNo", criteriaDTO.getWelderNo());
        }

        if (criteriaDTO.getGroupYear() != null) {
            query.setParameter("groupYear", criteriaDTO.getGroupYear());
        }

        if (criteriaDTO.getGroupMonth() != null) {
            query.setParameter("groupMonth", criteriaDTO.getGroupMonth());
        }

        if (criteriaDTO.getGroupDay() != null) {
            query.setParameter("groupDay", criteriaDTO.getGroupDay());
        }

        if (criteriaDTO.getGroupWeek() != null) {
            query.setParameter("groupWeek", criteriaDTO.getGroupYear() * 100 + criteriaDTO.getGroupWeek());
        }

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        return list;
    }

}
