package com.ose.material.domain.model.repository;

import com.ose.dto.OperatorDTO;
import com.ose.material.dto.MmReleaseReceiveSearchDTO;
import com.ose.material.entity.MmReleaseReceiveEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.repository.BaseRepository;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.MaterialReceiveType;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MmReleaseReceiveRepositoryImpl extends BaseRepository implements MmReleaseReceiveRepositoryCustom {

    @Override
    public Page<MmReleaseReceiveEntity> search(
        Long orgId, Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select mrr.* ");
        sql.append(" from mm_release_receive mrr ");
        sql.append(" where mrr.org_id = :orgId ");
        sql.append(" and mrr.project_id =:projectId ");
        sql.append(" and mrr.status = 'ACTIVE' ");

        if (mmReleaseReceiveSearchDTO.getKeyword() != null) {
            sql.append(" and (mrr.no like :keyword OR mrr.name like :keyword ) ");
        }
        sql.append(" order by mrr.created_at desc ");
        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        query.setParameter("orgId", orgId);
        countQuery.setParameter("orgId", orgId);

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        if (mmReleaseReceiveSearchDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + mmReleaseReceiveSearchDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + mmReleaseReceiveSearchDTO.getKeyword() + "%");
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = mmReleaseReceiveSearchDTO.getPage().getNo();
        int pageSize = mmReleaseReceiveSearchDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<MmReleaseReceiveEntity> mmReleaseReceiveEntities = new ArrayList<MmReleaseReceiveEntity>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmReleaseReceiveEntity mmReleaseReceiveEntity = new MmReleaseReceiveEntity();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

//            if (newMap.get("wareHouseType") != null) {
//                newMap.put("wareHouseType", WareHouseType.valueOf((String) newMap.get("wareHouseType")));
//            }
            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            if (newMap.get("runningStatus") != null) {
                newMap.put("runningStatus", EntityStatus.valueOf((String) newMap.get("runningStatus")));
            }

            if (newMap.get("type") != null) {
                newMap.put("type", MaterialReceiveType.valueOf((String) newMap.get("type")));
            }

            BeanUtils.copyProperties(newMap, mmReleaseReceiveEntity);

            mmReleaseReceiveEntities.add(mmReleaseReceiveEntity);

        }
        return new PageImpl<>(mmReleaseReceiveEntities, mmReleaseReceiveSearchDTO.toPageable(), count.longValue());
    }

    @Override
    public Page<MmReleaseReceiveEntity> searchByAssignee(
        Long orgId,
        Long projectId,
        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO,
        OperatorDTO operatorDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT mrr.* ");

        sql.append("FROM mm_release_receive mrr ");

        sql.append("LEFT JOIN saint_whale_tasks.bpm_activity_instance_base AS bai ");

        sql.append("ON mrr.org_id = bai.org_id ");

        sql.append("AND mrr.project_id = bai.project_id ");

        sql.append("AND mrr.id = bai.entity_id ");

        sql.append("LEFT JOIN saint_whale_tasks.bpm_activity_instance_state AS bais ");

        sql.append("ON bai.project_id = bais.project_id ");

        sql.append("AND bai.id = bais.bai_id ");

        sql.append("LEFT JOIN saint_whale_tasks.bpm_ru_task AS brt ");

        sql.append("ON brt.act_inst_id = bai.id ");

        sql.append("WHERE mrr.org_id = :orgId ");

        sql.append("AND mrr.project_id = :projectId ");

        sql.append("AND mrr.status = 'ACTIVE' ");

        sql.append("AND mrr.running_status <> 'APPROVED' ");

        if (mmReleaseReceiveSearchDTO.getKeyword() != null) {
            sql.append(" and (mrr.no like :keyword OR mrr.name like :keyword ) ");
        }
        sql.append("AND bais.suspension_state = 'ACTIVE' ");
        sql.append("AND bais.finish_state = 'NOT_FINISHED' ");
        sql.append("AND brt.assignee_ = :userId ");
        sql.append("ORDER BY mrr.created_at desc ");
        sql.append("LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取WPS总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        query.setParameter("orgId", orgId);
        countQuery.setParameter("orgId", orgId);

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        query.setParameter("userId", operatorDTO.getId());
        countQuery.setParameter("userId", operatorDTO.getId());

        if (mmReleaseReceiveSearchDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + mmReleaseReceiveSearchDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + mmReleaseReceiveSearchDTO.getKeyword() + "%");
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = mmReleaseReceiveSearchDTO.getPage().getNo();
        int pageSize = mmReleaseReceiveSearchDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<MmReleaseReceiveEntity> mmReleaseReceiveEntities = new ArrayList<MmReleaseReceiveEntity>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmReleaseReceiveEntity mmReleaseReceiveEntity = new MmReleaseReceiveEntity();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("wareHouseType") != null) {
                newMap.put("wareHouseType", MaterialOrganizationType.valueOf((String) newMap.get("wareHouseType")));
            }
            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            if (newMap.get("runningStatus") != null) {
                newMap.put("runningStatus", EntityStatus.valueOf((String) newMap.get("runningStatus")));
            }

            if (newMap.get("type") != null) {
                newMap.put("type", MaterialReceiveType.valueOf((String) newMap.get("type")));
            }

            BeanUtils.copyProperties(newMap, mmReleaseReceiveEntity);

            mmReleaseReceiveEntities.add(mmReleaseReceiveEntity);

        }
        return new PageImpl<>(mmReleaseReceiveEntities, mmReleaseReceiveSearchDTO.toPageable(), count.longValue());
    }

}
