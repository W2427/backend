package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmMaterialInStockSearchDTO;
import com.ose.material.entity.MmMaterialInStockEntity;
import com.ose.material.vo.QrCodeType;
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

public class MmMaterialInStockRepositoryImpl extends BaseRepository implements MmMaterialInStockRepositoryCustom {

    @Override
    public Page<MmMaterialInStockEntity> search(
        Long orgId, Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.* ");
        sql.append(" from mm_material_in_stock m ");
        sql.append(" where m.org_id = :orgId ");
        sql.append(" and m.project_id =:projectId ");
        sql.append(" and m.status = 'ACTIVE' ");

        if (mmMaterialInStockSearchDTO.getKeyword() != null) {
            sql.append(" and (m.ident_code like :keyword OR m.mm_material_code_no like :keyword ) ");
        }
        sql.append(" order by m.ident_code asc ");
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

        if (mmMaterialInStockSearchDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + mmMaterialInStockSearchDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + mmMaterialInStockSearchDTO.getKeyword() + "%");
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = mmMaterialInStockSearchDTO.getPage().getNo();
        int pageSize = mmMaterialInStockSearchDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<MmMaterialInStockEntity> mmMaterialInStockEntities = new ArrayList<MmMaterialInStockEntity>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmMaterialInStockEntity mmMaterialInStockEntity = new MmMaterialInStockEntity();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("wareHouseType") != null) {
                newMap.put("wareHouseType", MaterialOrganizationType.valueOf((String) newMap.get("wareHouseType")));
            }
            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            if (newMap.get("qrCodeType") != null) {
                newMap.put("qrCodeType", QrCodeType.valueOf((String) newMap.get("qrCodeType")));
            }

            BeanUtils.copyProperties(newMap, mmMaterialInStockEntity);

            mmMaterialInStockEntities.add(mmMaterialInStockEntity);

        }
        return new PageImpl<>(mmMaterialInStockEntities, mmMaterialInStockSearchDTO.toPageable(), count.longValue());
    }

}
