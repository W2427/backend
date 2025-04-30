package com.ose.tasks.domain.model.repository.trident;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.trident.EntityTypeProcessItrTemplateRelationCriteriaDTO;
import com.ose.tasks.entity.trident.EntityTypeProcessItrTemplateRelation;
import com.ose.tasks.vo.trident.CheckSheetStage;
import com.ose.tasks.vo.trident.CheckSheetType;
import com.ose.util.BeanUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityTypeProcessItrTemplateRelationRepositoryImpl extends BaseRepository implements EntityTypeProcessItrTemplateRelationRepositoryCustom {

    /**
     * 获取列表。
     *
     * @param projectId         项目ID
     * @param entityTypeProcessItrTemplateRelationCriteriaDTO 查询条件
     * @return 焊工列表
     */
    public Page<EntityTypeProcessItrTemplateRelation> search(Long projectId,
                                                             EntityTypeProcessItrTemplateRelationCriteriaDTO entityTypeProcessItrTemplateRelationCriteriaDTO) {

        EntityManager entityManager = getEntityManager();

        // 查询焊工数据SQL
        StringBuilder sql = new StringBuilder();
        // 焊工和焊工证关联
        sql.append("SELECT id, " +
            "created_at createAt, " +
            "last_modified_at lastModifiedAt, " +
            "status, " +
            "seq, " +
            "asset_type_id assetTypeId, " +
            "check_sheet_stage checkSheetStage, " +
            "check_sheet_type checkSheetType, " +
            "entity_sub_type_id entitySubTypeId, " +
            "entity_category_process_relation_id entityCategoryProcessRelationId, " +
            "entity_sub_type entitySubType, " +
            "entity_type entityType, " +
            "file_id fileId, " +
            "itr_template_desc itrTemplateDesc, " +
            "itr_template_no itrTemplateNo, " +
            "process, " +
            "process_id processId, " +
            "process_stage processStage, " +
            "project_id projectId, " +
            "itr_template_id itrTemplateId, " +
            "clazz_for_list clazzForList" +
            " FROM `entity_type_process_itr_template_relation` WHERE project_id = :projectId ");

        // 关键字查询条件
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getKeyword() != null) {
            sql.append("AND (`itr_template_no` LIKE :keyword OR `itr_template_desc` LIKE :keyword) ");
        }
        // 工序阶段查询条件
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getProcessStage() != null) {
            sql.append("AND `process_stage` = :processStage ");
        }
        // 工序查询条件
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getProcess() != null) {
            sql.append("AND `process` = :processName ");

        }
        // 实体类型查询条件
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getEntityType() != null) {
            sql.append("AND `entity_type` = :entityType ");

        }
        // 实体子类型查询条件
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getEntitySubType() != null) {
            sql.append("AND `entity_sub_type` = :entitySubType ");

        }

        // 检查阶段查询条件
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getCheckSheetStage() != null) {
            sql.append("AND `check_sheet_stage` = :checkSheetStage ");

        }

        // 检查类型查询条件
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getCheckSheetType() != null) {
            sql.append("AND `check_sheet_type` = :checkSheetType ");

        }


        sql.append("AND `status` = 'ACTIVE' ");
        sql.append(" LIMIT :start , :offset ");

        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `w`";

        Query query = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + entityTypeProcessItrTemplateRelationCriteriaDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + entityTypeProcessItrTemplateRelationCriteriaDTO.getKeyword() + "%");
        }
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getProcessStage() != null) {
            query.setParameter("processStage", entityTypeProcessItrTemplateRelationCriteriaDTO.getProcessStage());
            countQuery.setParameter("processStage", entityTypeProcessItrTemplateRelationCriteriaDTO.getProcessStage());
        }
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getProcess() != null) {
            query.setParameter("processName", entityTypeProcessItrTemplateRelationCriteriaDTO.getProcess());
            countQuery.setParameter("processName", entityTypeProcessItrTemplateRelationCriteriaDTO.getProcess());
        }
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getEntityType() != null) {
            query.setParameter("entityType", entityTypeProcessItrTemplateRelationCriteriaDTO.getEntityType());
            countQuery.setParameter("entityType", entityTypeProcessItrTemplateRelationCriteriaDTO.getEntityType());
        }
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getEntitySubType() != null) {
            query.setParameter("entitySubType", entityTypeProcessItrTemplateRelationCriteriaDTO.getEntitySubType());
            countQuery.setParameter("entitySubType", entityTypeProcessItrTemplateRelationCriteriaDTO.getEntitySubType());
        }
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getCheckSheetStage() != null) {
            query.setParameter("checkSheetStage", entityTypeProcessItrTemplateRelationCriteriaDTO.getCheckSheetStage().name());
            countQuery.setParameter("checkSheetStage", entityTypeProcessItrTemplateRelationCriteriaDTO.getCheckSheetStage().name());
        }
        if (entityTypeProcessItrTemplateRelationCriteriaDTO.getCheckSheetType() != null) {
            query.setParameter("checkSheetType", entityTypeProcessItrTemplateRelationCriteriaDTO.getCheckSheetType().name());
            countQuery.setParameter("checkSheetType", entityTypeProcessItrTemplateRelationCriteriaDTO.getCheckSheetType().name());
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        int pageNo = entityTypeProcessItrTemplateRelationCriteriaDTO.getPage().getNo();
        int pageSize = entityTypeProcessItrTemplateRelationCriteriaDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到焊工对象中
        List<EntityTypeProcessItrTemplateRelation> etpitrList = new ArrayList<>();
        EntityTypeProcessItrTemplateRelation etpitr;
        for (Map<String, Object> etpitrMap : queryResultList) {
            etpitr = new EntityTypeProcessItrTemplateRelation();
            String checkSheetType =  (String) etpitrMap.get("checkSheetType");
            String checkSheetStage = (String) etpitrMap.get("checkSheetStage");
            String entityType = (String) etpitrMap.get("entityType");
            BeanUtils.copyProperties(etpitrMap, etpitr, "entityType","checkSheetStage","checkSheetType","status");
            etpitr.setStatus(EntityStatus.ACTIVE);
            if(!StringUtils.isEmpty(entityType)){
                etpitr.setEntityType(entityType);
            }
            if(!StringUtils.isEmpty(checkSheetStage)) {
                etpitr.setCheckSheetStage(CheckSheetStage.valueOf(checkSheetStage));
            }
            if(!StringUtils.isEmpty(checkSheetType)) {
                etpitr.setCheckSheetType(CheckSheetType.valueOf(checkSheetType));
            }
            etpitrList.add(etpitr);
        }

        return new PageImpl<>(etpitrList, entityTypeProcessItrTemplateRelationCriteriaDTO.toPageable(), count.longValue());
    }

}
