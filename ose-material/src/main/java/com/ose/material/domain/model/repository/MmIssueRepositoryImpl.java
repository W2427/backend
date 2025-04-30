package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmIssueSearchDTO;
import com.ose.material.entity.MmIssueEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.repository.BaseRepository;
import com.ose.util.BeanUtils;
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

public class MmIssueRepositoryImpl extends BaseRepository implements MmIssueRepositoryCustom {

    @Override
    public Page<MmIssueEntity> search(
        Long orgId, Long projectId,
        MmIssueSearchDTO mmIssueSearchDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select mi.* ");
        sql.append(" from mm_issue mi ");
        sql.append(" where mi.org_id = :orgId ");
        sql.append(" and mi.project_id =:projectId ");
        sql.append(" and mi.status = 'ACTIVE' ");

        if (mmIssueSearchDTO.getKeyword() != null) {
            sql.append(" and (mi.no like :keyword OR mi.name like :keyword ) ");
        }
        sql.append(" order by mi.created_at desc ");
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

        if (mmIssueSearchDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + mmIssueSearchDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + mmIssueSearchDTO.getKeyword() + "%");
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = mmIssueSearchDTO.getPage().getNo();
        int pageSize = mmIssueSearchDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<MmIssueEntity> mmIssueEntities = new ArrayList<MmIssueEntity>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmIssueEntity mmIssueEntity = new MmIssueEntity();
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

            BeanUtils.copyProperties(newMap, mmIssueEntity);

            mmIssueEntities.add(mmIssueEntity);

        }
        return new PageImpl<>(mmIssueEntities, mmIssueSearchDTO.toPageable(), count.longValue());
    }

}
