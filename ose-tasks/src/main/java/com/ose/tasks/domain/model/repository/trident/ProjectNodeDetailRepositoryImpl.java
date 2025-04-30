package com.ose.tasks.domain.model.repository.trident;

import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.bpm.HierarchyBaseDTO;
import com.ose.tasks.dto.trident.ProjectNodeDetailCriteriaDTO;
import com.ose.tasks.entity.trident.ProjectNodeDetail;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
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

public class ProjectNodeDetailRepositoryImpl extends BaseRepository implements ProjectNodeDetailRepositoryCustom {

    /**
     * 获取列表。
     *
     * @param projectId   项目ID
     * @param criteriaDTO 查询条件
     * @return 列表
     */
    @Override
    public Page<ProjectNodeDetail> search(Long projectId, ProjectNodeDetailCriteriaDTO criteriaDTO) {

        EntityManager entityManager = getEntityManager();

        // 查询焊工数据SQL
        StringBuilder sql = new StringBuilder();
        // 焊工和焊工证关联
        sql.append("SELECT pn.* FROM project_node_detail pn ");

        if(criteriaDTO.getCurrentInChargeId() != null && !criteriaDTO.getCurrentInChargeId().equals(0L)) {

            sql.append(" JOIN tag_no_user_relation tn ON pn.entity_id = tn.entity_id ");
        }
        sql.append(" WHERE pn.project_id = :projectId ");

        if(criteriaDTO.getTagNo() != null) {
            sql.append(" AND pn.no = :no ");
        }

        if(criteriaDTO.getfGroup() != null && !criteriaDTO.getfGroup().equalsIgnoreCase("ALL")) {
            sql.append(" AND pn.f_group = :fGroup ");
        }

        if(criteriaDTO.getSector() != null && !criteriaDTO.getSector().equalsIgnoreCase("ALL")) {
            sql.append(" AND pn.sector = :sector ");
        }

        if(criteriaDTO.getEntityType() != null && !criteriaDTO.getEntityType().equalsIgnoreCase("ALL")) {
            sql.append(" AND pn.code = :entityType ");
        }

        if(criteriaDTO.getEntitySubType() != null) {
            sql.append(" AND pn.entity_sub_type = :entitySubType ");
        }

        if(criteriaDTO.getCco() != null) {
            sql.append(" AND pn.comm_checkok = :cco ");
        }

        if(criteriaDTO.getPackageId() != null) {
            sql.append(" AND pn.package_id = :packageId ");
        }

        if(!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            sql.append(" AND pn.sub_system_entity_id IN :subSystemIds ");
        }

        if(criteriaDTO.getPulled() != null) {
            sql.append(" AND pn.pulled_status = :pulled ");
        }

        if(criteriaDTO.getT1() != null) {
            sql.append(" AND pn.t1_status = :t1 ");
        }

        if(criteriaDTO.getT2() != null) {
            sql.append(" AND pn.t2_status = :t2 ");
        }

        if(criteriaDTO.getSubmited() != null) {
            sql.append(" AND pn.submit_status = :submited ");
        }

        if(criteriaDTO.getSigned() != null) {
            sql.append(" AND pn.signed_status = :signed ");
        }

        if(criteriaDTO.getClosed() != null) {
            sql.append(" AND pn.closed_status = :closed ");
        }

        if(criteriaDTO.getCurrentInChargeId() != null && !criteriaDTO.getCurrentInChargeId().equals(0L)) {
            sql.append(" AND tn.user_id = :userId ");
        }
        if(criteriaDTO.getIsInSkid() != null && "ALL".equalsIgnoreCase(criteriaDTO.getIsInSkid()) ) {
            sql.append(" AND (pn.is_in_skid = 0 OR pn.is_in_skid is null)");
        }

        if(criteriaDTO.getFetchAll() == null || !criteriaDTO.getFetchAll()) {
            sql.append(" LIMIT :start , :offset ");
        }

        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `w`";

        Query query = entityManager.createNativeQuery(sql.toString());
        Query countQuery = entityManager.createNativeQuery(countSQL);

        if (criteriaDTO.getTagNo() != null) {
            query.setParameter("no", criteriaDTO.getTagNo());
            countQuery.setParameter("no", criteriaDTO.getTagNo());
        }
        if(criteriaDTO.getEntityType() != null && !criteriaDTO.getEntityType().equalsIgnoreCase("ALL")) {
            query.setParameter("entityType", criteriaDTO.getEntityType());
            countQuery.setParameter("entityType", criteriaDTO.getEntityType());
        }
        if (criteriaDTO.getEntitySubType() != null) {
            query.setParameter("entitySubType", criteriaDTO.getEntitySubType());
            countQuery.setParameter("entitySubType", criteriaDTO.getEntitySubType());
        }
        if (criteriaDTO.getPackageId() != null) {
            query.setParameter("packageId", criteriaDTO.getPackageId());
            countQuery.setParameter("packageId", criteriaDTO.getPackageId());
        }
        if (!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            query.setParameter("subSystemIds", criteriaDTO.getSubSystemIds());
            countQuery.setParameter("subSystemIds", criteriaDTO.getSubSystemIds());
        }

        if(criteriaDTO.getCco() != null) {
            query.setParameter("cco", criteriaDTO.getCco());
            countQuery.setParameter("cco", criteriaDTO.getCco());
        }

        if(!StringUtils.isEmpty(criteriaDTO.getfGroup()) && !criteriaDTO.getfGroup().equalsIgnoreCase("ALL")) {
            query.setParameter("fGroup", criteriaDTO.getfGroup());
            countQuery.setParameter("fGroup", criteriaDTO.getfGroup());
        }

        if(!StringUtils.isEmpty(criteriaDTO.getSector()) && !criteriaDTO.getSector().equalsIgnoreCase("ALL")) {
            query.setParameter("sector", criteriaDTO.getSector());
            countQuery.setParameter("sector", criteriaDTO.getSector());
        }

        if(criteriaDTO.getClosed() != null) {
            query.setParameter("closed", criteriaDTO.getClosed());
            countQuery.setParameter("closed", criteriaDTO.getClosed());
        }


        if(criteriaDTO.getPulled() != null) {
            query.setParameter("pulled", criteriaDTO.getPulled());
            countQuery.setParameter("pulled", criteriaDTO.getPulled());
        }

        if(criteriaDTO.getT1() != null) {
            query.setParameter("t1", criteriaDTO.getT1());
            countQuery.setParameter("t1", criteriaDTO.getT1());
        }

        if(criteriaDTO.getT2() != null) {
            query.setParameter("t2", criteriaDTO.getT2());
            countQuery.setParameter("t2", criteriaDTO.getT2());
        }

        if(criteriaDTO.getSubmited() != null) {
            query.setParameter("submited", criteriaDTO.getSubmited());
            countQuery.setParameter("submited", criteriaDTO.getSubmited());
        }

        if(criteriaDTO.getSigned() != null) {
            query.setParameter("signed", criteriaDTO.getSigned());
            countQuery.setParameter("signed", criteriaDTO.getSigned());
        }

        if(criteriaDTO.getCurrentInChargeId() != null && !criteriaDTO.getCurrentInChargeId().equals(0L)) {
            query.setParameter("userId", criteriaDTO.getCurrentInChargeId());
            countQuery.setParameter("userId", criteriaDTO.getCurrentInChargeId());
        }

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        if(criteriaDTO.getFetchAll() == null || !criteriaDTO.getFetchAll()) {
            int pageNo = criteriaDTO.getPage().getNo();
            int pageSize = criteriaDTO.getPage().getSize();

            query.setParameter("start", (pageNo - 1) * pageSize);
            query.setParameter("offset", pageSize);
        }

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到焊工对象中
        List<ProjectNodeDetail> pnds = new ArrayList<>();
        ProjectNodeDetail pnd;
        for (Map<String, Object> pndMap : queryResultList) {
            pnd = new ProjectNodeDetail();
            BeanUtils.copyProperties(BeanUtils.toReplaceKeyLow(pndMap), pnd);
            pnds.add(pnd);
        }

        return new PageImpl<>(pnds, criteriaDTO.toPageable(), count.longValue());

    }

    @Override
    public List<HierarchyBaseDTO> findDistinctFGroups(Long projectId, ProjectNodeDetailCriteriaDTO criteriaDTO) {
        EntityManager entityManager = getEntityManager();

        // 查询焊工数据SQL
        StringBuilder sql = new StringBuilder();
        // 焊工和焊工证关联
        sql.append("SELECT f_group, count(0) AS cnt FROM project_node_detail WHERE project_id = :projectId ");

        if(criteriaDTO.getSubSystem() != null && !criteriaDTO.getSubSystem().equalsIgnoreCase("ALL")) {
            sql.append(" AND sub_system = :subSystem ");
        }

        if(criteriaDTO.getEntityType() != null && !criteriaDTO.getEntityType().equalsIgnoreCase("ALL")) {
            sql.append(" AND code = :entityType ");
        }

        if(!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            sql.append(" AND sub_system_entity_id IN :subSystemIds ");
        }

        if(criteriaDTO.getSubmited() != null) {
            sql.append(" AND submit_status = :submited ");
        }

        if(criteriaDTO.getSigned() != null) {
            sql.append(" AND signed_status = :signed ");
        }

        if(criteriaDTO.getClosed() != null) {
            sql.append(" AND closed_status = :closed ");
        }

        sql.append(" GROUP BY project_id, f_group ");

        Query query = entityManager.createNativeQuery(sql.toString());

        if(criteriaDTO.getEntityType() != null && !criteriaDTO.getEntityType().equalsIgnoreCase("ALL")) {
            query.setParameter("entityType", criteriaDTO.getEntityType());
        }

        if (!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            query.setParameter("subSystemIds", criteriaDTO.getSubSystemIds());
        }


        if(!StringUtils.isEmpty(criteriaDTO.getSubSystem()) && !criteriaDTO.getSubSystem().equalsIgnoreCase("ALL")) {
            query.setParameter("subSystem", criteriaDTO.getSubSystem());
        }

        if(criteriaDTO.getClosed() != null) {
            query.setParameter("closed", criteriaDTO.getClosed());
        }

        if(criteriaDTO.getSubmited() != null) {
            query.setParameter("submited", criteriaDTO.getSubmited());
        }

        if(criteriaDTO.getSigned() != null) {
            query.setParameter("signed", criteriaDTO.getSigned());
        }

        query.setParameter("projectId", projectId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果

        // 设置结果（结构为MAP）到焊工对象中
        List<HierarchyBaseDTO> hbds = new ArrayList<>();
        HierarchyBaseDTO hbd;
        for (Map<String, Object> hbdMap : queryResultList) {
            hbd = new HierarchyBaseDTO();
//            hbd.setCnt(((BigInteger)hbdMap.get("cnt")).intValue());
            hbd.setNameEn((String) hbdMap.get("f_group"));
//            BeanUtils.copyProperties(BeanUtils.toReplaceKeyLow(hbdMap), hbd);
            hbds.add(hbd);
        }

        return hbds;
    }

    @Override
    public List<HierarchyBaseDTO> findDistinctSubSystems(Long projectId, ProjectNodeDetailCriteriaDTO criteriaDTO) {
        EntityManager entityManager = getEntityManager();

        // 查询焊工数据SQL
        StringBuilder sql = new StringBuilder();
        // 焊工和焊工证关联
        sql.append("SELECT sub_system, count(0) AS cnt FROM project_node_detail WHERE project_id = :projectId ");

        if(criteriaDTO.getfGroup() != null && !criteriaDTO.getfGroup().equalsIgnoreCase("ALL")) {
            sql.append(" AND f_group = :fGroup ");
        }

        if(criteriaDTO.getCco() != null) {
            sql.append(" AND comm_checkok = :cco ");
        }

        if(criteriaDTO.getEntityType() != null && !criteriaDTO.getEntityType().equalsIgnoreCase("ALL")) {
            sql.append(" AND code = :entityType ");
        }

        if(!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            sql.append(" AND sub_system_entity_id IN :subSystemIds ");
        }

        if(criteriaDTO.getSubmited() != null) {
            sql.append(" AND submit_status = :submited ");
        }

        if(criteriaDTO.getSigned() != null) {
            sql.append(" AND signed_status = :signed ");
        }

        if(criteriaDTO.getClosed() != null) {
            sql.append(" AND closed_status = :closed ");
        }

        sql.append(" GROUP BY project_id, sub_system ");

        Query query = entityManager.createNativeQuery(sql.toString());

        if(criteriaDTO.getEntityType() != null && !criteriaDTO.getEntityType().equalsIgnoreCase("ALL")) {
            query.setParameter("entityType", criteriaDTO.getEntityType());
        }

        if (!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            query.setParameter("subSystemIds", criteriaDTO.getSubSystemIds());
        }


        if(criteriaDTO.getfGroup() != null && !criteriaDTO.getfGroup().equalsIgnoreCase("ALL")) {
            query.setParameter("fGroup", criteriaDTO.getfGroup());
        }

        if(criteriaDTO.getClosed() != null) {
            query.setParameter("closed", criteriaDTO.getClosed());
        }

        if(criteriaDTO.getSubmited() != null) {
            query.setParameter("submited", criteriaDTO.getSubmited());
        }

        if(criteriaDTO.getSigned() != null) {
            query.setParameter("signed", criteriaDTO.getSigned());
        }
//
        if(criteriaDTO.getCco() != null) {
            query.setParameter("cco", criteriaDTO.getCco());
        }

            query.setParameter("projectId", projectId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果

        // 设置结果（结构为MAP）到焊工对象中
        List<HierarchyBaseDTO> hbds = new ArrayList<>();
        HierarchyBaseDTO hbd;
        for (Map<String, Object> hbdMap : queryResultList) {
            hbd = new HierarchyBaseDTO();
//            hbd.setCnt(((BigInteger)hbdMap.get("cnt")).intValue());
            hbd.setNameEn((String) hbdMap.get("sub_system"));
//            BeanUtils.copyProperties(BeanUtils.toReplaceKeyLow(hbdMap), hbd);
            hbds.add(hbd);
        }

        return hbds;
    }

    @Override
    public List<HierarchyBaseDTO> findDistinctEntityTypes(Long projectId, ProjectNodeDetailCriteriaDTO criteriaDTO) {
        EntityManager entityManager = getEntityManager();

        // 查询焊工数据SQL
        StringBuilder sql = new StringBuilder();
        // 焊工和焊工证关联
        sql.append("SELECT code, count(0) AS cnt FROM project_node_detail WHERE project_id = :projectId ");

        if(criteriaDTO.getfGroup() != null && !criteriaDTO.getfGroup().equalsIgnoreCase("ALL")) {
            sql.append(" AND f_group = :fGroup ");
        }

        if(criteriaDTO.getSubSystem() != null && !criteriaDTO.getSubSystem().equalsIgnoreCase("ALL")) {
            sql.append(" AND sub_system = :subSystem ");
        }

        if(!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            sql.append(" AND sub_system_entity_id IN :subSystemIds ");
        }

        if(criteriaDTO.getCco() != null) {
            sql.append(" AND comm_checkok = :cco ");
        }

        if(criteriaDTO.getSubmited() != null) {
            sql.append(" AND submit_status = :submited ");
        }

        if(criteriaDTO.getSigned() != null) {
            sql.append(" AND signed_status = :signed ");
        }

        if(criteriaDTO.getClosed() != null) {
            sql.append(" AND closed_status = :closed ");
        }

        sql.append(" GROUP BY project_id, code ");

        Query query = entityManager.createNativeQuery(sql.toString());

        if(criteriaDTO.getSubSystem() != null && !criteriaDTO.getSubSystem().equalsIgnoreCase("ALL")) {
            query.setParameter("subSystem", criteriaDTO.getSubSystem());
        }

        if (!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            query.setParameter("subSystemIds", criteriaDTO.getSubSystemIds());
        }


        if(criteriaDTO.getfGroup() != null && !criteriaDTO.getfGroup().equalsIgnoreCase("ALL")) {
            query.setParameter("fGroup", criteriaDTO.getfGroup());
        }

        if(criteriaDTO.getClosed() != null) {
            query.setParameter("closed", criteriaDTO.getClosed());
        }

        if(criteriaDTO.getSubmited() != null) {
            query.setParameter("submited", criteriaDTO.getSubmited());
        }

        if(criteriaDTO.getSigned() != null) {
            query.setParameter("signed", criteriaDTO.getSigned());
        }

        if(criteriaDTO.getCco() != null) {
            query.setParameter("cco", criteriaDTO.getCco());
        }

        query.setParameter("projectId", projectId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果

        // 设置结果（结构为MAP）到焊工对象中
        List<HierarchyBaseDTO> hbds = new ArrayList<>();
        HierarchyBaseDTO hbd;
        for (Map<String, Object> hbdMap : queryResultList) {
            hbd = new HierarchyBaseDTO();
//            hbd.setCnt(((BigInteger)hbdMap.get("cnt")).intValue());
            hbd.setNameEn((String) hbdMap.get("code"));
            hbds.add(hbd);
        }

        return hbds;
    }

    @Override
    public List<HierarchyBaseDTO> searchInCharge(Long projectId, ProjectNodeDetailCriteriaDTO criteriaDTO) {
        EntityManager entityManager = getEntityManager();

        // 查询焊工数据SQL
        StringBuilder sql = new StringBuilder();
        // 焊工和焊工证关联
        sql.append("SELECT distinct tn.user_id, tn.user_name FROM project_node_detail pn JOIN tag_no_user_relation tn ON pn.project_id = tn.project_id AND pn.entity_id = tn.entity_id ");

//        if(criteriaDTO.getInChargeId() != null) {

//            sql.append(" JOIN tag_no_user_relation tn ON pnd.project_id = tn.project_id AND pnd.entity_id = tn.entity_id ");
//        }
        sql.append(" WHERE pn.project_id = :projectId ");

        if(criteriaDTO.getTagNo() != null) {
            sql.append(" AND pn.no = :no ");
        }

        if(criteriaDTO.getfGroup() != null && !criteriaDTO.getfGroup().equalsIgnoreCase("ALL")) {
            sql.append(" AND pn.f_group = :fGroup ");
        }

        if(criteriaDTO.getSector() != null && !criteriaDTO.getSector().equalsIgnoreCase("ALL")) {
            sql.append(" AND pn.sector = :sector ");
        }

        if(criteriaDTO.getEntityType() != null && !criteriaDTO.getEntityType().equalsIgnoreCase("ALL")) {
            sql.append(" AND pn.code = :entityType ");
        }

        if(criteriaDTO.getEntitySubType() != null) {
            sql.append(" AND pn.entity_sub_type = :entitySubType ");
        }

        if(criteriaDTO.getPackageId() != null) {
            sql.append(" AND pn.package_id = :packageId ");
        }

        if(!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            sql.append(" AND pn.sub_system_entity_id IN :subSystemIds ");
        }

        if(criteriaDTO.getPulled() != null) {
            sql.append(" AND pn.pulled_status = :pulled ");
        }

        if(criteriaDTO.getSubmited() != null) {
            sql.append(" AND pn.submit_status = :submited ");
        }

        if(criteriaDTO.getSigned() != null) {
            sql.append(" AND pn.signed_status = :signed ");
        }

        if(criteriaDTO.getClosed() != null) {
            sql.append(" AND pn.closed_status = :closed ");
        }

        if(criteriaDTO.getCurrentInChargeId() != null && !criteriaDTO.getCurrentInChargeId().equals(0L)) {
            sql.append(" AND tn.user_id = :userId ");
        }

        Query query = entityManager.createNativeQuery(sql.toString());

        if (criteriaDTO.getTagNo() != null) {
            query.setParameter("no", criteriaDTO.getTagNo());
        }
        if(criteriaDTO.getEntityType() != null && !criteriaDTO.getEntityType().equalsIgnoreCase("ALL")) {
            query.setParameter("entityType", criteriaDTO.getEntityType());
        }
        if (criteriaDTO.getEntitySubType() != null) {
            query.setParameter("entitySubType", criteriaDTO.getEntitySubType());
        }
        if (criteriaDTO.getPackageId() != null) {
            query.setParameter("packageId", criteriaDTO.getPackageId());
        }
        if (!CollectionUtils.isEmpty(criteriaDTO.getSubSystemIds())) {
            query.setParameter("subSystemIds", criteriaDTO.getSubSystemIds());
        }


        if(!StringUtils.isEmpty(criteriaDTO.getfGroup()) && !criteriaDTO.getfGroup().equalsIgnoreCase("ALL")) {
            query.setParameter("fGroup", criteriaDTO.getfGroup());
        }

        if(!StringUtils.isEmpty(criteriaDTO.getSector()) && !criteriaDTO.getSector().equalsIgnoreCase("ALL")) {
            query.setParameter("sector", criteriaDTO.getSector());
        }

        if(criteriaDTO.getClosed() != null) {
            query.setParameter("closed", criteriaDTO.getClosed());
        }


        if(criteriaDTO.getPulled() != null) {
            query.setParameter("pulled", criteriaDTO.getPulled());
        }

        if(criteriaDTO.getSubmited() != null) {
            query.setParameter("submited", criteriaDTO.getSubmited());
        }

        if(criteriaDTO.getSigned() != null) {
            query.setParameter("signed", criteriaDTO.getSigned());
        }

        if(criteriaDTO.getCurrentInChargeId() != null && !criteriaDTO.getCurrentInChargeId().equals(0L)) {
            query.setParameter("userId", criteriaDTO.getCurrentInChargeId());
        }

        query.setParameter("projectId", projectId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();

        // 设置结果（结构为MAP）到焊工对象中
        List<HierarchyBaseDTO> pnds = new ArrayList<>();
        HierarchyBaseDTO pnd;
        for (Map<String, Object> pndMap : queryResultList) {
            pnd = new HierarchyBaseDTO();
            BigInteger idB = (BigInteger) pndMap.get("user_id");
            if(idB != null) {
                Long id = idB.longValue();
                String nameEn = (String) pndMap.get("user_name");
                pnd.setNameEn(nameEn);
                pnd.setId(id);

                pnds.add(pnd);
            }
        }

        return pnds;
    }

}
