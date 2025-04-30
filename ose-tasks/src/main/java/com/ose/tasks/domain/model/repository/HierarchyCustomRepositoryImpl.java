package com.ose.tasks.domain.model.repository;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.ModuleDTO;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.vo.wbs.WBSEntryRunningStatus;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Transactional
public class HierarchyCustomRepositoryImpl extends BaseRepository implements HierarchyCustomRepository {

    /**
     * 启动实体工序计划实施进度设置处理。
     *
     * @param projectId 项目 ID
     */
    @Override
    public void startSettingWBSProgress(final Long projectId) {

        final EntityManager em = getEntityManager();

        (new Thread(() -> em
            .createStoredProcedureQuery("start_setting_progress_on_hierarchy_nodes")
            .execute()
        )).start();

    }

    /**
     * 查找当前实体是否存在子节点。
     *
     * @param projectId
     * @param entityId
     * @return
     */
    @Override
    public List<HierarchyNode> searchChild(
        Long projectId,
        Long entityId
    ) {
        EntityManager entityManager = getEntityManager();


        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT ch.* ");
        sql.append(" FROM project_node p ");
        sql.append(" JOIN hierarchy_node h ");
        sql.append(" ON p.id = h.node_id ");
        sql.append(" AND h.deleted = false ");
        sql.append(" Left JOIN hierarchy_node_relation hr ");
        sql.append(" ON hr.hierarchy_ancestor_id = h.id ");
        sql.append(" AND hr.hierarchy_ancestor_id != hr.hierarchy_id ");
        sql.append(" JOIN hierarchy_node ch ");
        sql.append(" ON ch.project_id = p.project_id ");
        sql.append(" AND ch.deleted = false ");
        sql.append(" AND ch.id = hr.hierarchy_id ");
        sql.append(" WHERE p.project_id = :projectId ");
        sql.append(" AND p.entity_id = :entityId ");
        sql.append(" AND p.deleted = false ");

        Query query = entityManager.createNativeQuery(sql.toString());

        query.setParameter("projectId", projectId);
        query.setParameter("entityId", entityId);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();
        List<HierarchyNode> hierarchyNodes = new ArrayList<>();
        for (Map<String, Object> resultMap : queryResultList) {

            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("hierarchyType") != null) {
                newMap.put("hierarchyType", newMap.get("hierarchyType"));
            }
            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            HierarchyNode hierarchyNode = new HierarchyNode();
            BeanUtils.copyProperties(newMap, hierarchyNode);
            hierarchyNodes.add(hierarchyNode);
        }

        return hierarchyNodes;
    }

    /**
     * 查找当前项目指定专业下所有实体信息含概区域和模块信息。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Override
    public List<ModuleDTO> searchEntity(
        Long orgId,
        Long projectId,
        Long entityId,
        String hierarchyType
    ) {
        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT mp.no AS moduleNo, ");
        sql.append(" mp.id AS moduleProjectNodeId, ");
        sql.append(" mh.id AS moduleHierarchyNodeId, ");
        sql.append(" ehr.entity_id AS entityId, ");
        sql.append(" ehr.entity_type AS entityType, ");
        sql.append(" ehr.entity_sub_type AS entitySubType ");
        sql.append(" FROM project_node mp  ");
        sql.append(" JOIN hierarchy_node mh  ");
        sql.append(" ON mp.id = mh.node_id   ");
        sql.append(" INNER JOIN hierarchy_node_relation ehr  ");
        sql.append(" ON ehr.hierarchy_ancestor_id = mh.id   ");
        sql.append(" WHERE  mp.project_id =:projectId ");
        sql.append(" AND mp.deleted = false ");
        sql.append(" AND mp.entity_id =:entityId ");
        sql.append(" AND mh.hierarchy_type =:hierarchyType");
        sql.append(" AND mh.deleted = false  ");
        sql.append(" AND ehr.project_id =:projectId ");
        sql.append(" AND ehr.entity_id is not null ");
        sql.append(" ORDER BY ehr.entity_type");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);
        query.setParameter("hierarchyType", hierarchyType);
        query.setParameter("entityId", entityId);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();
        List<ModuleDTO> moduleDTOs = new ArrayList<>();
        for (Map<String, Object> resultMap : queryResultList) {

            ModuleDTO moduleDTO = new ModuleDTO();
            BeanUtils.copyProperties(resultMap, moduleDTO);
            moduleDTOs.add(moduleDTO);
        }
        return moduleDTOs;
    }

    /**
     * 查找当前项目指定专业下所有实体信息含概区域和模块信息。
     * 测试用
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Override
    public List<ModuleDTO> searchEntityTest(
        Long orgId,
        Long projectId,
        Long processId,
        String moduleNo,
        WBSEntryRunningStatus runningStatus
    ) {
        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT w.sector AS moduleNo, ");
        sql.append(" count(w.id) AS qty ");
        sql.append(" FROM wbs_entry w ");
        sql.append(" JOIN wbs_entry_state ws ");
        sql.append(" ON w.id = ws.wbs_entry_id ");
        sql.append(" WHERE w.sector =:moduleNo ");
        sql.append(" AND w.project_id =:projectId ");
        sql.append(" AND w.process_id =:processId");
        sql.append(" AND w.deleted = false ");
        if (runningStatus != null) {
            sql.append(" AND ws.project_id =:projectId ");
            sql.append(" AND ws.running_status =:runningStatus ");
        }
        sql.append(" GROUP BY w.sector ");

        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);
        query.setParameter("processId", processId);
        query.setParameter("moduleNo", moduleNo);
        if (runningStatus != null) {
            query.setParameter("runningStatus", runningStatus.toString());
        }
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();
        List<ModuleDTO> moduleDTOs = new ArrayList<>();
        for (Map<String, Object> resultMap : queryResultList) {

            ModuleDTO moduleDTO = new ModuleDTO();
            BeanUtils.copyProperties(resultMap, moduleDTO);
            moduleDTOs.add(moduleDTO);
        }
        return moduleDTOs;
    }

    /**
     * 查找模块下所有管段长度总和。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Override
    public Double searchPip(
        Long orgId,
        Long projectId,
        String entityNo
    ) {
        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(e.length) AS length");
        sql.append(" FROM project_node p ");
        sql.append(" JOIN hierarchy_node h ");
        sql.append(" ON p.id = h.node_id ");
        sql.append(" AND h.deleted = false ");
        sql.append(" Left JOIN hierarchy_node_relation hr ");
        sql.append(" ON hr.hierarchy_ancestor_id = h.id ");
        sql.append(" AND hr.hierarchy_ancestor_id != hr.hierarchy_id ");

        sql.append(" JOIN entity_pipe_piece e ");
        sql.append(" ON e.id = hr.entity_id ");

        sql.append(" WHERE p.project_id = :projectId ");
        sql.append(" AND p.no = :entityNo ");
        sql.append(" AND p.deleted = false ");
        sql.append(" AND h.project_id = :projectId  ");
        sql.append(" AND hr.project_id = :projectId  ");
        sql.append(" AND hr.entity_type = 'PIPE_PIECE'  ");
        sql.append(" AND e.project_id = :projectId  ");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);
        query.setParameter("entityNo", entityNo);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();

        Double length = 0.0;
        for (Map<String, Object> resultMap : queryResultList) {
            if (resultMap.get("length") != null) {
                length = (Double) resultMap.get("length");
            }
        }
        return length;
    }

    /**
     * 查找模块下所有管附件数量。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Override
    public Integer searchComponent(
        Long orgId,
        Long projectId,
        String entityNo
    ) {
        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT SUM(e.qty) AS qty");
        sql.append(" FROM project_node p ");
        sql.append(" JOIN hierarchy_node h ");
        sql.append(" ON p.id = h.node_id ");
        sql.append(" AND h.deleted = false ");
        sql.append(" Left JOIN hierarchy_node_relation hr ");
        sql.append(" ON hr.hierarchy_ancestor_id = h.id ");
        sql.append(" AND hr.hierarchy_ancestor_id != hr.hierarchy_id ");

        sql.append(" JOIN entity_components e ");
        sql.append(" ON e.id = hr.entity_id ");

        sql.append(" WHERE p.project_id = :projectId ");
        sql.append(" AND p.no = :entityNo ");
        sql.append(" AND p.deleted = false ");
        sql.append(" AND h.project_id = :projectId  ");
        sql.append(" AND hr.project_id = :projectId  ");
        sql.append(" AND hr.entity_type = 'COMPONENT'  ");
        sql.append(" AND e.project_id = :projectId  ");
        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("projectId", projectId);
        query.setParameter("entityNo", entityNo);
        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();

        Integer qty = 0;
        for (Map<String, Object> resultMap : queryResultList) {
            if (resultMap.get("qty") != null) {
                qty = Integer.valueOf(resultMap.get("qty").toString());
            }
        }
        return qty;
    }

    /**
     * 查找模块下所有零件的重量。
     *
     * @param orgId
     * @param projectId
     * @param wp01EntityId
     * @return
     */
    @Override
    public Double sumTotalWeight(Long orgId, Long projectId, Long wp01EntityId) {
        EntityManager entityManager = getEntityManager();

        StringBuilder sql = new StringBuilder();

        sql.append(" SELECT	SUM(`e`.weight) AS total_weight ");
        sql.append(" FROM	entity_wp05 `e` ");
        sql.append(" WHERE id IN ( ");
        sql.append(" SELECT entity_id FROM `hierarchy_node_relation` `hr` ");
        sql.append(" WHERE org_id = :orgId ");
        sql.append("   AND project_id = :projectId ");
        sql.append("   AND ancestor_entity_id = :ancestorEntityId ");
        sql.append("   AND ancestor_entity_type = 'WP01') ");


        Query query = entityManager.createNativeQuery(sql.toString());
        query.setParameter("orgId", orgId);
        query.setParameter("projectId", projectId);
        query.setParameter("ancestorEntityId", wp01EntityId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();

        Double weight = 0.0;
        for (Map<String, Object> resultMap : queryResultList) {
            if (resultMap.get("total_weight") != null) {
                weight = Double.valueOf(resultMap.get("total_weight").toString());
            }
        }
        return weight;
    }
}
