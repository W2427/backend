package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmPurchasePackageSearchDTO;
import com.ose.material.entity.MmPurchasePackageEntity;
import com.ose.repository.BaseRepository;
import com.ose.util.BeanUtils;
import com.ose.vo.EntityStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MmPurchasePackageRepositoryImpl extends BaseRepository implements MmPurchasePackageRepositoryCustom {

    @Override
    public Page<MmPurchasePackageEntity> search(
        Long orgId,
        Long projectId,
        MmPurchasePackageSearchDTO mmPurchasePackageSearchDTO
    ){
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT p.*");
        sql.append(" FROM mm_purchase_package p ");
        sql.append(" WHERE p.org_id = :orgId ");
        sql.append(" AND p.project_id =:projectId ");

        if (mmPurchasePackageSearchDTO.getKeyword() != null) {
            sql.append(" and p.pwps_number like :keyword  ");
        }

        if (mmPurchasePackageSearchDTO.getLocked() != null) {
            sql.append(" and p.locked =:locked  ");
        }

        sql.append(" and p.status = 'ACTIVE' ");
        sql.append(" order by p.last_modified_at desc ");
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

        if (mmPurchasePackageSearchDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + mmPurchasePackageSearchDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + mmPurchasePackageSearchDTO.getKeyword() + "%");
        }

        if (mmPurchasePackageSearchDTO.getLocked()  != null) {
            query.setParameter("locked", mmPurchasePackageSearchDTO.getLocked());
            countQuery.setParameter("locked", mmPurchasePackageSearchDTO.getLocked());
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = mmPurchasePackageSearchDTO.getPage().getNo();
        int pageSize = mmPurchasePackageSearchDTO.getPage().getSize();
        if (mmPurchasePackageSearchDTO.getFetchAll()) {
            pageSize = Integer.MAX_VALUE;
            mmPurchasePackageSearchDTO.getPage().setSize(pageSize);
        }

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<MmPurchasePackageEntity> mmPurchasePackageReturnEntities = new ArrayList<MmPurchasePackageEntity>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmPurchasePackageEntity mmPurchasePackageEntity = new MmPurchasePackageEntity();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }

            BeanUtils.copyProperties(newMap, mmPurchasePackageEntity);
            mmPurchasePackageReturnEntities.add(mmPurchasePackageEntity);

        }
        if(mmPurchasePackageSearchDTO.getFetchAll()){
            PageRequest pageRequest = PageRequest.of(
                mmPurchasePackageSearchDTO.getPage().getNo(),
                mmPurchasePackageSearchDTO.getPage().getSize(),
                Sort.by(Sort.Order.asc("createdAt"))
            );
            return new PageImpl<>(mmPurchasePackageReturnEntities,pageRequest,count.longValue());
        }else{
            return new PageImpl<>(mmPurchasePackageReturnEntities, mmPurchasePackageSearchDTO.toPageable(), count.longValue());
        }

    }

}

