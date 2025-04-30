package com.ose.material.domain.model.repository;
import com.ose.material.dto.MmShippingSearchDTO;
import com.ose.material.entity.MmShippingEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.repository.BaseRepository;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.MaterialReceiveType;
import com.ose.vo.ShippingStatus;
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

public class MmShippingRepositoryImpl extends BaseRepository implements MmShippingRepositoryCustom {

    @Override
    public Page<MmShippingEntity> search(
        Long orgId, Long projectId,
        MmShippingSearchDTO mmShippingSearchDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ms.* ");
        sql.append(" from mm_shipping ms ");
        sql.append(" where ms.org_id = :orgId ");
        sql.append(" and ms.project_id =:projectId ");
        sql.append(" and ms.status = 'ACTIVE' ");

        if (mmShippingSearchDTO.getKeyword() != null) {
            sql.append(" and (ms.no like :keyword OR ms.name like :keyword ) ");
        }
        sql.append(" order by ms.created_at desc ");
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

        if (mmShippingSearchDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + mmShippingSearchDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + mmShippingSearchDTO.getKeyword() + "%");
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = mmShippingSearchDTO.getPage().getNo();
        int pageSize = mmShippingSearchDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<MmShippingEntity> mmShippingEntities = new ArrayList<MmShippingEntity>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmShippingEntity mmShippingEntity = new MmShippingEntity();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("wareHouseType") != null) {
                newMap.put("wareHouseType", MaterialOrganizationType.valueOf((String) newMap.get("wareHouseType")));
            }
            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            if (newMap.get("shippingStatus") != null) {
                newMap.put("shippingStatus", ShippingStatus.valueOf((String) newMap.get("shippingStatus")));
            }

            if (newMap.get("type") != null) {
                newMap.put("type", MaterialReceiveType.valueOf((String) newMap.get("type")));
            }

            BeanUtils.copyProperties(newMap, mmShippingEntity);

            mmShippingEntities.add(mmShippingEntity);

        }
        return new PageImpl<>(mmShippingEntities, mmShippingSearchDTO.toPageable(), count.longValue());
    }

}
