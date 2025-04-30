package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmSurplusMaterialSearchDTO;
import com.ose.material.entity.MmSurplusMaterialEntity;
import com.ose.material.vo.QrCodeType;
import com.ose.repository.BaseRepository;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MmSurplusMaterialRepositoryImpl extends BaseRepository implements MmSurplusMaterialRepositoryCustom {

    @Override
    public Page<MmSurplusMaterialEntity> search(
        Long orgId,
        Long projectId,
        MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO
    ) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ms.* ");
        sql.append(" from mm_surplus_materials ms ");
        sql.append(" where ms.org_id = :orgId ");
        sql.append(" and ms.project_id =:projectId ");
        sql.append(" and ms.status = 'ACTIVE' ");

        if (mmSurplusMaterialSearchDTO.getKeyword() != null) {
            sql.append(" and ms.no like :keyword ");
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

        if (mmSurplusMaterialSearchDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + mmSurplusMaterialSearchDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + mmSurplusMaterialSearchDTO.getKeyword() + "%");
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = mmSurplusMaterialSearchDTO.getPage().getNo();
        int pageSize = mmSurplusMaterialSearchDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<MmSurplusMaterialEntity> mmSurplusMaterialEntities = new ArrayList<MmSurplusMaterialEntity>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmSurplusMaterialEntity mmSurplusMaterialEntity = new MmSurplusMaterialEntity();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }

            if (newMap.get("qrCodeType") != null) {
                newMap.put("qrCodeType", QrCodeType.valueOf((String) newMap.get("qrCodeType")));
            }

            BeanUtils.copyProperties(newMap, mmSurplusMaterialEntity);

            mmSurplusMaterialEntities.add(mmSurplusMaterialEntity);

        }
        return new PageImpl<>(mmSurplusMaterialEntities, mmSurplusMaterialSearchDTO.toPageable(), count.longValue());
    }

}
