package com.ose.tasks.domain.model.repository.trident;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.completion.ItrStatisticDTO;
import com.ose.tasks.dto.trident.ItrCriteriaDTO;
import com.ose.tasks.entity.trident.Itr;
import com.ose.tasks.vo.trident.CheckSheetStage;
import com.ose.tasks.vo.trident.CheckSheetType;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItrRepositoryImpl extends BaseRepository implements ItrRepositoryCustom {

    /**
     * 获取列表。
     *
     * @param projectId         项目ID
     * @param itrCriteriaDTO 查询条件
     * @return 列表
     */
    public Page<Itr> search(Long projectId, ItrCriteriaDTO itrCriteriaDTO) {

        EntityManager entityManager = getEntityManager();

        // 查询焊工数据SQL
        StringBuilder sql = new StringBuilder();
        // 焊工和焊工证关联
        sql.append("SELECT " +
            "it.id," +
            "it.created_at createdAt," +
            "it.last_modified_at lastModifiedAt," +
            "it.status," +
            "it.check_sheet_stage checkSheetStage," +
            "it.check_sheet_trident_tbl checkSheetTridentTbl," +
            "it.check_sheet_type checkSheetType," +
            "it.entity_id entityId," +
            "it.entity_no entityNo," +
            "it.file_id fileId," +
            "it.in_charge_person inChargePerson," +
            "it.is_in_trident isInTrident," +
            "it.itr_check_template itrCheckTemplate," +
            "it.project_id projectId," +
            "it.trident_check_sheet_id tridentCheckSheetId," +
            "it.trident_completed_date tridentCompletedDate," +
            "it.trident_file_ref tridentFileRef," +
            "it.trident_itr_id tridentItrId," +
            "it.trident_proj_phase_id tridentProjPhaseId," +
            "it.trident_tag_id tridentTagId," +
            "it.running_status runningStatus," +
            "it.entity_sub_type_id entitySubTypeId," +
            "it.process_id processId" +
            " FROM `itr` it" +
//            " ON hnr.project_id = it.project_id AND hnr.entity_id = it.entity_id " +
            " JOIN `project_node` pn ON pn.project_id = it.project_id AND pn.entity_id = it.entity_id ");
            // 层级父级查询条件
        if (itrCriteriaDTO.getAncestorHierarchyId() != null) {
            sql.append(" JOIN `hierarchy_node_relation` hnr ON hnr.project_id = it.project_id AND hnr.entity_id = it.entity_id ");
        }
            //`hierarchy_node_relation` hnr JOIN
            sql.append(" JOIN `bpm_process` bp ON bp.id = it.process_id " +
            " JOIN `bpm_process_stage` bps ON bps.id = bp.process_stage_id " +
            " JOIN `bpm_entity_sub_type` bec ON bec.id = it.entity_sub_type_id" +

            " WHERE it.project_id = :projectId ");

        // 关键字查询条件
        if (itrCriteriaDTO.getKeyword() != null) {
            sql.append("AND it.`entity_no` LIKE :keyword ");
        }
        // 工序阶段查询条件
        if (itrCriteriaDTO.getEntityNo() != null) {
            sql.append("AND it.`entity_no` = :entityNo ");
        }
        // 层级父级查询条件
        if (!CollectionUtils.isEmpty(itrCriteriaDTO.getAncestorHierarchyId())) {
            sql.append("AND hnr.`hierarchy_ancestor_id` IN :ancestorHierarchyId ");
        }
        // 专业查询条件
        if (itrCriteriaDTO.getDiscipline() != null) {
            sql.append("AND pn.`discipline` = :discipline ");

        }

        // 检查阶段查询条件
        if (itrCriteriaDTO.getCheckSheetStage() != null) {
            sql.append("AND it.`check_sheet_stage` = :checkSheetStage ");

        }


        // 检查类型查询条件
        if (itrCriteriaDTO.getCheckSheetType() != null) {
            sql.append("AND it.`check_sheet_type` = :checkSheetType ");

        }

        // Trident是否存在查询条件
        if (itrCriteriaDTO.getInTrident() != null) {
            sql.append("AND it.`is_in_trident` = :inTrident ");

        }


        // 工序查询条件
        if (itrCriteriaDTO.getProcessId() != null) {
            sql.append("AND it.`process_id` = :processId ");

        }
        // 工序阶段查询条件
        if (itrCriteriaDTO.getProcessStageId() != null) {
            sql.append("AND bp.`process_stage_id` = :processStageId ");

        }
        // 实体子类查询条件
        if (itrCriteriaDTO.getEntitySubTypeId() != null) {
            sql.append("AND it.`entity_sub_type_id` = :entitySubTypeId ");

        }


//        sql.append(" AND hnr.ancestor_entity_type = 'SUB_SYSTEM' AND it.`status` = 'ACTIVE' ");
        sql.append(" AND it.`status` = 'ACTIVE' ");
        sql.append(" LIMIT :start , :offset ");

        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `w`";

        Query query = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (itrCriteriaDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + itrCriteriaDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + itrCriteriaDTO.getKeyword() + "%");
        }
        if (itrCriteriaDTO.getEntityNo() != null) {
            query.setParameter("entityNo", itrCriteriaDTO.getEntityNo());
            countQuery.setParameter("entityNo", itrCriteriaDTO.getEntityNo());
        }
        if (!CollectionUtils.isEmpty(itrCriteriaDTO.getAncestorHierarchyId())) {
            query.setParameter("ancestorHierarchyId", itrCriteriaDTO.getAncestorHierarchyId());
            countQuery.setParameter("ancestorHierarchyId", itrCriteriaDTO.getAncestorHierarchyId());
        }
        if (itrCriteriaDTO.getDiscipline() != null) {
            query.setParameter("discipline", itrCriteriaDTO.getDiscipline());
            countQuery.setParameter("discipline", itrCriteriaDTO.getDiscipline());
        }
        if (itrCriteriaDTO.getCheckSheetStage() != null) {
            query.setParameter("checkSheetStage", itrCriteriaDTO.getCheckSheetStage());
            countQuery.setParameter("checkSheetStage", itrCriteriaDTO.getCheckSheetStage());
        }
        if (itrCriteriaDTO.getCheckSheetType() != null) {
            query.setParameter("checkSheetType", itrCriteriaDTO.getCheckSheetType().name());
            countQuery.setParameter("checkSheetType", itrCriteriaDTO.getCheckSheetType().name());
        }
        if (itrCriteriaDTO.getInTrident() != null) {
            query.setParameter("inTrident", itrCriteriaDTO.getInTrident());
            countQuery.setParameter("inTrident", itrCriteriaDTO.getInTrident());
        }

        if (itrCriteriaDTO.getProcessId() != null) {
            query.setParameter("processId", itrCriteriaDTO.getProcessId());
            countQuery.setParameter("processId", itrCriteriaDTO.getProcessId());
        }

        if (itrCriteriaDTO.getProcessStageId() != null) {
            query.setParameter("processStageId", itrCriteriaDTO.getProcessStageId());
            countQuery.setParameter("processStageId", itrCriteriaDTO.getProcessStageId());
        }

        if (itrCriteriaDTO.getEntitySubTypeId() != null) {
            query.setParameter("entitySubTypeId", itrCriteriaDTO.getEntitySubTypeId());
            countQuery.setParameter("entitySubTypeId", itrCriteriaDTO.getEntitySubTypeId());
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        int pageNo = itrCriteriaDTO.getPage().getNo();
        int pageSize = itrCriteriaDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到焊工对象中
        List<Itr> itrList = new ArrayList<>();
        Itr itr;
        for (Map<String, Object> itrMap : queryResultList) {
            itr = new Itr();
            String checkSheetStageStr = (String) itrMap.get("checkSheetStage");
            String checkSheetTypeStr = (String) itrMap.get("checkSheetType");
            String runningStatusStr = (String) itrMap.get("runningStatus");

            BeanUtils.copyProperties(itrMap, itr, "status","checkSheetStage","checkSheetType","runningStatus","checkSheetTridentTbl");
            itr.setStatus(EntityStatus.ACTIVE);
            if(!StringUtils.isEmpty(checkSheetStageStr)) {
                itr.setCheckSheetStage(CheckSheetStage.valueOf(checkSheetStageStr));
            }
            if(!StringUtils.isEmpty(checkSheetTypeStr)) {
                itr.setCheckSheetType(CheckSheetType.valueOf(checkSheetTypeStr));
            }
            if(!StringUtils.isEmpty(runningStatusStr)) {
                itr.setRunningStatus(EntityStatus.valueOf(runningStatusStr));
            }

            itrList.add(itr);
        }

        return new PageImpl<>(itrList, itrCriteriaDTO.toPageable(), count.longValue());
    }

    @Override
    public List<ItrStatisticDTO> getItrStatistic(Long projectId){
        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" bec.discipline, ");
        sql.append(" count(0) subTotal,  ");
        sql.append(" SUM(case pn.no when 'HULL' THEN 1 ELSE 0 END) AS hullCnt, ");
        sql.append(" SUM(case pn.no when 'TOPSIDE' THEN 1 ELSE 0 END) AS topsideCnt, ");
        sql.append(" SUM(case i.running_status WHEN 'INIT' THEN 1 ELSE 0 END) AS initCnt, ");
        sql.append(" SUM(case i.running_status WHEN 'INIT' THEN 1 ELSE 0 END)/count(0) AS initPer, ");
        sql.append(" SUM(case i.running_status WHEN 'PRINTED' THEN 1 ELSE 0 END) AS printedCnt, ");
        sql.append(" SUM(case i.running_status WHEN 'PRINTED' THEN 1 ELSE 0 END)/count(0) AS printedPer, ");
        sql.append(" SUM(case i.running_status WHEN 'SIGNED' THEN 1 ELSE 0 END) AS signedCnt, ");
        sql.append(" SUM(case i.running_status WHEN 'SIGNED' THEN 1 ELSE 0 END)/count(0) AS signedPer, ");
        sql.append(" SUM(case i.running_status WHEN 'CLOSED' THEN 1 ELSE 0 END) AS closedCnt , ");
        sql.append(" SUM(case i.running_status WHEN 'CLOSED' THEN 1 ELSE 0 END)/count(0) AS closedPer  ");
        sql.append(" from itr i join bpm_entity_sub_type bec on i.entity_sub_type_id = bec.id  ");
        sql.append(" join bpm_process bp on bp.id = i.process_id  ");
        sql.append(" join bpm_entity_type bect on bect.id = bec.entity_category_type_id  ");
        sql.append(" join bpm_process_stage bps on bps.id = bp.process_stage_id  ");
        sql.append(" join hierarchy_node_relation hnr on hnr.entity_id = i.entity_id and hnr.ancestor_entity_type = 'SECTOR'  ");
        sql.append(" join project_node pn on pn.id = hnr.node_ancestor_id  ");
        sql.append("  WHERE i.project_id = :projectId ");
        sql.append(" group by  ");
        sql.append("     bec.discipline  ");

        sql.append(" order by bec.discipline  ");


        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();
        List<ItrStatisticDTO> itrStatisticDTOS = new ArrayList<>();
        ItrStatisticDTO subTotal = new ItrStatisticDTO();
        subTotal.setClosedCnt(BigDecimal.ZERO);
        subTotal.setClosedPer(BigDecimal.ZERO);
        subTotal.setDiscipline("");
        subTotal.setHullCnt(BigDecimal.ZERO);
        subTotal.setTopsideCnt(BigDecimal.ZERO);
        subTotal.setInitCnt(BigDecimal.ZERO);
        subTotal.setInitPer(BigDecimal.ZERO);
        subTotal.setPrintedCnt(BigDecimal.ZERO);
        subTotal.setPrintedPer(BigDecimal.ZERO);
        subTotal.setSignedCnt(BigDecimal.ZERO);
        subTotal.setSignedPer(BigDecimal.ZERO);
        subTotal.setSubTotal(BigInteger.ZERO);

        for (Map<String, Object> resultMap : queryResultList) {

            ItrStatisticDTO itrStatisticDTO = new ItrStatisticDTO();
            String disciplineStr = (String) resultMap.get("discipline");
            BeanUtils.copyProperties(resultMap, itrStatisticDTO);
            itrStatisticDTO.setDiscipline(disciplineStr);
            itrStatisticDTO.setSubTotal(itrStatisticDTO.getHullCnt().add(itrStatisticDTO.getTopsideCnt()).toBigInteger());
            itrStatisticDTOS.add(itrStatisticDTO);
            subTotal.setSignedPer(subTotal.getSignedPer().add( subTotal.getSignedPer()));
            subTotal.setSignedCnt(subTotal.getSignedCnt().add(itrStatisticDTO.getSignedCnt()));
            subTotal.setPrintedPer(subTotal.getPrintedPer().add(itrStatisticDTO.getPrintedPer()));
            subTotal.setPrintedCnt(subTotal.getPrintedCnt().add(itrStatisticDTO.getPrintedCnt()));
            subTotal.setInitCnt(subTotal.getInitCnt().add(itrStatisticDTO.getInitCnt()));
            subTotal.setInitPer(subTotal.getInitPer().add(itrStatisticDTO.getInitPer()));
            subTotal.setClosedPer(subTotal.getClosedPer().add(itrStatisticDTO.getClosedPer()));
            subTotal.setClosedCnt(subTotal.getClosedCnt().add(itrStatisticDTO.getClosedCnt()));
            subTotal.setHullCnt(subTotal.getHullCnt().add(itrStatisticDTO.getHullCnt()));
            subTotal.setTopsideCnt(subTotal.getTopsideCnt().add(itrStatisticDTO.getTopsideCnt()));
            subTotal.setSubTotal(subTotal.getSubTotal().add(itrStatisticDTO.getSubTotal()));

        }
        subTotal.setDiscipline("SUB_TOTAL");
        itrStatisticDTOS.add(subTotal);
        return itrStatisticDTOS;

    }


}
