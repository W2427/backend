package com.ose.tasks.domain.model.repository;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.taskpackage.TaskPackageProjectNodeEntitySearchDTO;
import com.ose.tasks.dto.wbs.WBSEntryExecutionHistoryDTO;
import com.ose.tasks.entity.ProjectNode;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 项目节点 CRUD 操作接口。
 */
public class ProjectNodeCustomRepositoryImpl extends BaseRepository implements ProjectNodeCustomRepository {

    /**
     * 设置实体信息及项目节点的所属管线及单管实体的 ID。
     *
     * @param projectId 项目 ID
     */
    @Transactional
    @Modifying
    @Override
    public void setParentISOAndSpoolEntityIDs(final Long projectId) {
        getEntityManager()
            .createStoredProcedureQuery("set_parent_entity_id_on_entities")
            .registerStoredProcedureParameter(1, Long.class, ParameterMode.IN)
            .setParameter(1, projectId)
            .execute();
    }

    /**
     * 设置WP实体信息被删除后更新其他 WP表
     *
     * @param projectId 项目ID
     * @param wpName
     */
    @Transactional
    @Modifying
    @Override
    public void setDeletedParentEntityIdOnStructureEntities(Long projectId, String wpName, Long entityId) {
        EntityManager entityManager = getEntityManager();
        entityManager.createStoredProcedureQuery("set_deleted_parent_entity_id_on_structure_entities")
            .registerStoredProcedureParameter(1, Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, Long.class, ParameterMode.IN)
            .setParameter(1, projectId)
            .setParameter(2, wpName)
            .setParameter(3, entityId)
            .execute();
    }


    /**
     * 设置结构实体的 WP01/02/03/04/05
     *
     * @param projectId 项目ID
     * @param wpName
     * @param entityId
     */
    @Transactional
    @Modifying
    @Override
    public void setParentEntityIdOnStructureEntity(Long projectId, String wpName, Long entityId) {
        EntityManager entityManager = getEntityManager();
        entityManager.createStoredProcedureQuery("set_parent_entity_id_on_structure_entity")
            .registerStoredProcedureParameter(1, Long.class, ParameterMode.IN)
            .registerStoredProcedureParameter(2, String.class, ParameterMode.IN)
            .registerStoredProcedureParameter(3, Long.class, ParameterMode.IN)
            .setParameter(1, projectId)
            .setParameter(2, wpName)
            .setParameter(3, entityId)
            .execute();
    }

    /**
     * 设置结构实体的 WP01/02/03/04/05
     *
     * @param projectId 项目ID
     */
    @Transactional
    @Modifying
    @Override
    public void setParentEntityIdOnStructureEntities(Long projectId) {
        EntityManager entityManager = getEntityManager();
        entityManager.createStoredProcedureQuery("set_parent_entity_id_on_structure_entities")
            .registerStoredProcedureParameter(1, Long.class, ParameterMode.IN)
            .setParameter(1, projectId)
            .execute();
    }



    /**
     * 取得要插入的 新增实体 到 wbs_entry_execution_history
     *
     * @param operatorId
     * @param projectId  项目ID
     * @param entityId
     * @return
     */
    @Transactional
    @Modifying
    @Override
    public List<WBSEntryExecutionHistoryDTO> getWbsEntryExecutionHistory(
        @Param("operatorId") Long operatorId,
        @Param("projectId") Long projectId,
        @Param("entityId") Long entityId) {
        EntityManager entityManager = getEntityManager();


        String sql = "select " +






            "   null                    AS          memo, " +
            "   mpn.module_type         AS          moduleType, " +
            "   mpn.no                  AS          module, " +
            "   mhn.id                  AS          moduleId, " +
            "   lpn.no                  AS          layer, " +
            "   lpn.id                  AS          layerId, " +
            "   pn.entity_id            AS          entityId, " +
            "   pn.entity_type          AS          entityType, " +
            "   pn.entity_sub_type      AS          entitySubType, " +
            "   'UNDO'                  AS          executionState, " +
            "   :operatorId             AS          operatorId, " +
            "   :projectId              AS          projectId " +
            "FROM " +
            "   project_node pn left join " +
            "   hierarchy_node lhn on pn.id = lhn.node_id and lhn.hierarchy_type " +
            "   IN ('PIPING','PRESSURE_TEST_PACKAGE','CLEAN_PACKAGE','SUB_SYSTEM','STRUCTURE')  and lhn.deleted =0" +
            "   left join hierarchy_node llhn on lhn.path like concat(llhn.path,llhn.id,'%')  and llhn.deleted =0" +
            "   left join project_node lpn on llhn.node_id = lpn.id  and lpn.deleted =0" +
            "   left join hierarchy_node mhn on lhn.path like concat(mhn.path, mhn.id,'%')  and mhn.deleted =0" +
            "   left join project_node mpn on mhn.node_id = mpn.id  and mpn.deleted =0 " +

            "WHERE pn.entity_id = :entityId " +
            "   and lpn.node_type = 'LAYER_PACKAGE' " +
            "   and mpn.node_type IN ('MODULE','SYSTEM') " +
            "   and pn.project_id = :projectId";

        Query query = entityManager.createNativeQuery(sql, "WBSEntryExecutionHistorySqlResultMapping");
        query.setParameter("projectId", projectId);
        query.setParameter("entityId", entityId);
        query.setParameter("operatorId", operatorId);
        List<WBSEntryExecutionHistoryDTO> result = query.getResultList();
        return result;
    }


    @Transactional
    @Modifying
    @Override
    public Page<ProjectNode> taskPackageEntities(
        Long orgId,
        Long projectId,
        TaskPackageProjectNodeEntitySearchDTO taskPackageProjectNodeEntitySearchDTO
    ) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT p.* ");
        sql.append(" FROM project_node p ");
        sql.append(" INNER JOIN hierarchy_node h ");
        sql.append(" ON p.id = h.node_id ");

        if (taskPackageProjectNodeEntitySearchDTO.getHierarchyType() != null) {
            sql.append(" AND h.hierarchy_type = :hierarchyType ");
        }
        sql.append(" WHERE 1 = 1 ");
        sql.append(" AND p.org_id = :orgId ");
        sql.append(" AND p.project_id = :projectId ");
        sql.append(" AND p.status =:status ");

        if (taskPackageProjectNodeEntitySearchDTO.getEntityNo() != null) {
            sql.append(" AND p.no like :entityNo ");
        }
        if (taskPackageProjectNodeEntitySearchDTO.getEntityType() != null) {
            sql.append(" AND p.entity_type = :entityType ");
        }

//        sql.append(" ORDER BY p.created_at desc ");

        if (!taskPackageProjectNodeEntitySearchDTO.getFetchAll()) {
            sql.append(" LIMIT :start , :offset ");
        }

        // 查询结果
        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (taskPackageProjectNodeEntitySearchDTO.getHierarchyType() != null) {
            query.setParameter("hierarchyType", taskPackageProjectNodeEntitySearchDTO.getHierarchyType());
            countQuery.setParameter("hierarchyType", taskPackageProjectNodeEntitySearchDTO.getHierarchyType());
        }

        query.setParameter("orgId", orgId);
        countQuery.setParameter("orgId", orgId);

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        query.setParameter("status", EntityStatus.ACTIVE.name());
        countQuery.setParameter("status", EntityStatus.ACTIVE.name());

        if (taskPackageProjectNodeEntitySearchDTO.getEntityNo() != null) {
            query.setParameter("entityNo", "%" + taskPackageProjectNodeEntitySearchDTO.getEntityNo() + "%");
            countQuery.setParameter("entityNo", "%" + taskPackageProjectNodeEntitySearchDTO.getEntityNo() + "%");
        }

        if (taskPackageProjectNodeEntitySearchDTO.getEntityType() != null) {
            query.setParameter("entityType", taskPackageProjectNodeEntitySearchDTO.getEntityType());
            countQuery.setParameter("entityType", taskPackageProjectNodeEntitySearchDTO.getEntityType());
        }

        // 分页参数
        if (!taskPackageProjectNodeEntitySearchDTO.getFetchAll()) {
            int pageNo = taskPackageProjectNodeEntitySearchDTO.getPage().getNo();
            int pageSize = taskPackageProjectNodeEntitySearchDTO.getPage().getSize();
            query.setParameter("start", (pageNo - 1) * pageSize);
            query.setParameter("offset", pageSize);
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<ProjectNode> resultList = new ArrayList<ProjectNode>();

        for (Map<String, Object> resultMap : queryResultList) {
            ProjectNode projectNode = new ProjectNode();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);
            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            if (newMap.get("entityType") != null) {
                newMap.put("entityType", newMap.get("entityType"));
            }
            BeanUtils.copyProperties(newMap, projectNode);
            resultList.add(projectNode);
        }

        return new PageImpl<>(resultList, taskPackageProjectNodeEntitySearchDTO.toPageable(), count.longValue());
    }


}
