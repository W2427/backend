package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmMaterialInStockSearchDTO;
import com.ose.material.dto.MmReleaseReceiveQrCodeResultDTO;
import com.ose.material.entity.MmMaterialInStockDetailEntity;
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


public class MmMaterialInStockDetailRepositoryImpl extends BaseRepository implements MmMaterialInStockDetailRepositoryCustom {

    @Override
    public Page<MmMaterialInStockDetailEntity> search(
        Long orgId,
        Long projectId,
        EntityStatus status,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    ) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.* ");
        sql.append(" from mm_material_in_stock_detail m ");
        sql.append(" where m.org_id = :orgId ");
        sql.append(" and m.project_id =:projectId ");
        if (mmMaterialInStockSearchDTO.getMaterialCodeNo() != null) {
            sql.append(" and m.mm_material_code_no like :mmMaterialCodeNo ");
        }
        if (mmMaterialInStockSearchDTO.getSpecName() != null) {
            sql.append(" and m.spec_name like :specName ");
        }
        if (mmMaterialInStockSearchDTO.getMmMaterialCodeDescription() != null) {
            sql.append(" and m.mm_material_code_description like :mmMaterialCodeDescription ");
        }
        sql.append(" and m.status = 'ACTIVE' ");
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

        if (mmMaterialInStockSearchDTO.getMaterialCodeNo() != null) {
            query.setParameter("mmMaterialCodeNo", "%" + mmMaterialInStockSearchDTO.getMaterialCodeNo() + "%");
            countQuery.setParameter("mmMaterialCodeNo", "%" + mmMaterialInStockSearchDTO.getMaterialCodeNo() + "%");
        }
        if (mmMaterialInStockSearchDTO.getSpecName() != null) {
            query.setParameter("specName", "%" + mmMaterialInStockSearchDTO.getSpecName() + "%");
            countQuery.setParameter("specName", "%" + mmMaterialInStockSearchDTO.getSpecName() + "%");
        }

        if (mmMaterialInStockSearchDTO.getMmMaterialCodeDescription() != null) {
            query.setParameter("mmMaterialCodeDescription", "%" + mmMaterialInStockSearchDTO.getMmMaterialCodeDescription() + "%");
            countQuery.setParameter("mmMaterialCodeDescription", "%" + mmMaterialInStockSearchDTO.getMmMaterialCodeDescription() + "%");
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
        List<MmMaterialInStockDetailEntity> mmMaterialInStockDetailEntities = new ArrayList<MmMaterialInStockDetailEntity>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmMaterialInStockDetailEntity mmMaterialInStockDetailEntity = new MmMaterialInStockDetailEntity();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("qrCodeType") != null) {
                newMap.put("qrCodeType", QrCodeType.valueOf((String) newMap.get("qrCodeType")));
            }
            if (newMap.get("wareHouseType") != null) {
                newMap.put("wareHouseType", MaterialOrganizationType.valueOf((String) newMap.get("wareHouseType")));
            }
            if (newMap.get("status") != null) {
                newMap.put("status", EntityStatus.valueOf((String) newMap.get("status")));
            }
            BeanUtils.copyProperties(newMap, mmMaterialInStockDetailEntity);
            mmMaterialInStockDetailEntities.add(mmMaterialInStockDetailEntity);

        }
        return new PageImpl<>(mmMaterialInStockDetailEntities, mmMaterialInStockSearchDTO.toPageable(), count.longValue());
    }

    @Override
    public Page<MmReleaseReceiveQrCodeResultDTO> searchQrCodes(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    ) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT mq.qr_code AS qrCode, ");
        sql.append(" md.qr_code_type AS qrCodeType, ");
        sql.append(" mq.in_stock_qty AS qty, ");
        sql.append(" md.mm_material_code_no AS mmMaterialCodeNo, ");
        sql.append(" md.ident_code AS identCode, ");
        sql.append(" md.spec_qty AS specQty, ");
        sql.append(" md.spec_name AS specName, ");
        sql.append(" md.design_unit AS designUnit, ");
        sql.append(" md.ware_house_type AS wareHouseType, ");
        sql.append(" md.mm_material_code_description AS mmMaterialCodeDescription, ");
        sql.append(" mq.running_status AS runningStatus ");

        sql.append(" FROM mm_material_in_stock_detail_qr_code mq ");
        sql.append(" LEFT JOIN mm_material_in_stock_detail md ");
        sql.append(" ON mq.mm_material_in_stock_detail_id = md.id ");
        sql.append(" WHERE mq.org_id = :orgId ");
        sql.append(" AND mq.project_id =:projectId ");
        if (mmMaterialInStockSearchDTO.getQrCode() != null) {
            sql.append(" and mq.qr_code like :qrCode ");
        }
        if (mmMaterialInStockSearchDTO.getIdentCode() != null) {
            sql.append(" and md.ident_code like :identCode ");
        }
        if (mmMaterialInStockSearchDTO.getMaterialCodeNo() != null) {
            sql.append(" and md.mm_material_code_no like :mmMaterialCodeNo ");
        }
        if (mmMaterialInStockSearchDTO.getSpecName() != null) {
            sql.append(" and md.spec_name like :specName ");
        }
        if (mmMaterialInStockSearchDTO.getMmMaterialCodeDescription() != null) {
            sql.append(" and m.mm_material_code_description like :mmMaterialCodeDescription ");
        }
        sql.append(" and mq.status = 'ACTIVE' ");
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
        if (mmMaterialInStockSearchDTO.getQrCode() != null) {
            query.setParameter("qrCode", "%" + mmMaterialInStockSearchDTO.getQrCode() + "%");
            countQuery.setParameter("qrCode", "%" + mmMaterialInStockSearchDTO.getQrCode() + "%");
        }
        if (mmMaterialInStockSearchDTO.getIdentCode() != null) {
            query.setParameter("identCode", "%" + mmMaterialInStockSearchDTO.getIdentCode() + "%");
            countQuery.setParameter("identCode", "%" + mmMaterialInStockSearchDTO.getIdentCode() + "%");
        }
        if (mmMaterialInStockSearchDTO.getMaterialCodeNo() != null) {
            query.setParameter("mmMaterialCodeNo", "%" + mmMaterialInStockSearchDTO.getMaterialCodeNo() + "%");
            countQuery.setParameter("mmMaterialCodeNo", "%" + mmMaterialInStockSearchDTO.getMaterialCodeNo() + "%");
        }
        if (mmMaterialInStockSearchDTO.getSpecName() != null) {
            query.setParameter("specName", "%" + mmMaterialInStockSearchDTO.getSpecName() + "%");
            countQuery.setParameter("specName", "%" + mmMaterialInStockSearchDTO.getSpecName() + "%");
        }
        if (mmMaterialInStockSearchDTO.getMmMaterialCodeDescription() != null) {
            query.setParameter("mmMaterialCodeDescription", "%" + mmMaterialInStockSearchDTO.getMmMaterialCodeDescription() + "%");
            countQuery.setParameter("mmMaterialCodeDescription", "%" + mmMaterialInStockSearchDTO.getMmMaterialCodeDescription() + "%");
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
        List<MmReleaseReceiveQrCodeResultDTO> mmMaterialInStockDetailEntities = new ArrayList<MmReleaseReceiveQrCodeResultDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmReleaseReceiveQrCodeResultDTO mmMaterialInStockDetailEntity = new MmReleaseReceiveQrCodeResultDTO();
            Map<String, Object> newMap = BeanUtils.toReplaceKeyLow(resultMap);

            if (newMap.get("qrCodeType") != null) {
                newMap.put("qrCodeType", QrCodeType.valueOf((String) newMap.get("qrCodeType")));
            }
            if (newMap.get("wareHouseType") != null) {
                newMap.put("wareHouseType", MaterialOrganizationType.valueOf((String) newMap.get("wareHouseType")));
            }
            if (newMap.get("runningStatus") != null) {
                newMap.put("runningStatus", EntityStatus.valueOf((String) newMap.get("runningStatus")));
            }
            BeanUtils.copyProperties(newMap, mmMaterialInStockDetailEntity);
            mmMaterialInStockDetailEntities.add(mmMaterialInStockDetailEntity);

        }
        return new PageImpl<>(mmMaterialInStockDetailEntities, mmMaterialInStockSearchDTO.toPageable(), count.longValue());
    }
}
