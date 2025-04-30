package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmPurchasePackageVendorReturnDTO;
import com.ose.material.dto.MmPurchasePackageVendorSearchDTO;
import com.ose.repository.BaseRepository;
import com.ose.util.BeanUtils;
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

public class MmPurchasePackageVendorRelationRepositoryImpl extends BaseRepository implements MmPurchasePackageVendorRelationRepositoryCustom {

    @Override
    public Page<MmPurchasePackageVendorReturnDTO> search(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageVendorSearchDTO mmPurchasePackageVendorSearchDTO) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.pwps_number,r.purchase_package_id,v.supplier_code,v.supplier_name,r.vendor_id,r.id,r.confirm_status,r.remark,r.score,r.review,r.feed_back,r.bid_status ");
        sql.append(" FROM mm_purchase_package p ");
        sql.append(" INNER JOIN mm_purchase_package_vendor_relation r ");
        sql.append(" ON p.id = r.purchase_package_id ");
        sql.append(" INNER JOIN mm_vendor v ");
        sql.append(" ON v.id = r.vendor_id ");
        sql.append(" WHERE p.org_id = :orgId ");
        sql.append(" AND p.project_id =:projectId ");
        sql.append(" AND r.purchase_package_id =:purchasePackageId ");

        if (mmPurchasePackageVendorSearchDTO.getKeyword() != null) {
            sql.append(" and v.supplier_code like :keyword  ");
        }
        sql.append(" and r.status = 'ACTIVE' ");
        sql.append(" order by r.last_modified_at desc ");
        sql.append(" LIMIT :start , :offset ");

        Query query = entityManager.createNativeQuery(sql.toString());

        // 获取总数（分页用）
        String countSQL = "SELECT COUNT(*) FROM (" + sql.toString().replaceAll("LIMIT.*", "") + ") AS `W`";
        // 查询相同条件下的总数
        Query countQuery = entityManager.createNativeQuery(countSQL);

        query.setParameter("orgId", orgId);
        countQuery.setParameter("orgId", orgId);

        query.setParameter("projectId", projectId);
        countQuery.setParameter("projectId", projectId);

        query.setParameter("purchasePackageId", purchasePackageId);
        countQuery.setParameter("purchasePackageId", purchasePackageId);

        if (mmPurchasePackageVendorSearchDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + mmPurchasePackageVendorSearchDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + mmPurchasePackageVendorSearchDTO.getKeyword() + "%");
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = mmPurchasePackageVendorSearchDTO.getPage().getNo();
        int pageSize = mmPurchasePackageVendorSearchDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<MmPurchasePackageVendorReturnDTO> mmPurchasePackageVendorReturnEntities = new ArrayList<MmPurchasePackageVendorReturnDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmPurchasePackageVendorReturnDTO mmPurchasePackageVendorReturnEntity = new MmPurchasePackageVendorReturnDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            BeanUtils.copyProperties(newMap, mmPurchasePackageVendorReturnEntity);

            if (newMap.get("supplierName") != null) {
                mmPurchasePackageVendorReturnEntity.setVendorName(newMap.get("supplierName") .toString());
            }

            if (newMap.get("supplierCode") != null) {
                mmPurchasePackageVendorReturnEntity.setVendorNo(newMap.get("supplierCode").toString());
            }

            if (newMap.get("pwpsNumber") != null) {
                mmPurchasePackageVendorReturnEntity.setPwpsNumber(newMap.get("pwpsNumber").toString());
            }

            mmPurchasePackageVendorReturnEntities.add(mmPurchasePackageVendorReturnEntity);

        }
        return new PageImpl<>(mmPurchasePackageVendorReturnEntities, mmPurchasePackageVendorSearchDTO.toPageable(), count.longValue());
    }

}

