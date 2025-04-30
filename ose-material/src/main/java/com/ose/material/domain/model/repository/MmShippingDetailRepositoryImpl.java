package com.ose.material.domain.model.repository;
import com.ose.material.dto.MmShippingDetailsDTO;
import com.ose.material.dto.MmShippingSearchDTO;
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

public class MmShippingDetailRepositoryImpl extends BaseRepository implements MmShippingDetailRepositoryCustom {

    /**
     * 查询请购单详情。
     *
     * @return 请购单详情分页数据
     */
    @Override
    public Page<MmShippingDetailsDTO> searchRequisitionDetail(
        Long orgId,
        Long projectId,
        Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO
    ) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select mrd.*, ");
        sql.append(" mrd.design_qty AS dQty, ");
        sql.append(" mr.no AS mmRequisitionNo ");
//        sql.append(" from mm_requisition_detail mrd ");
//        sql.append(" join mm_requisition mr ");
        sql.append(" on mr.id = mrd.mm_requisition_id ");
        sql.append(" where mrd.org_id = :orgId ");
        sql.append(" and mrd.project_id =:projectId ");
        sql.append(" and mrd.deleted is false ");
        sql.append(" and mrd.un_inventory_qty > 0 ");
        sql.append(" and mr.running_status = 'APPROVED' ");
        sql.append(" and mrd.id not in ( select msdr.requisition_detail_id ");
        sql.append(" from mm_shipping_detail_relation msdr where msdr.project_id =:projectId ");
        sql.append(" and msdr.deleted IS FALSE and msdr.shipping_id =:shippingId ) ");

        if (mmShippingSearchDTO.getKeywordA() != null && !"".equals(mmShippingSearchDTO.getKeywordA())) {
            sql.append(" and mr.no like :mmRequisitionNo ");
        }
        if (mmShippingSearchDTO.getKeywordB() != null && !"".equals(mmShippingSearchDTO.getKeywordB())) {
            sql.append(" and mrd.mm_material_code_description like :mmMaterialCodeDescription ");
        }


        sql.append(" order by mrd.mm_material_code_no ");
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

        query.setParameter("shippingId", shippingId);
        countQuery.setParameter("shippingId", shippingId);

        if (mmShippingSearchDTO.getKeywordA() != null && !"".equals(mmShippingSearchDTO.getKeywordA())) {
            query.setParameter("mmRequisitionNo", "%" + mmShippingSearchDTO.getKeywordA() + "%");
            countQuery.setParameter("mmRequisitionNo", "%" + mmShippingSearchDTO.getKeywordA() + "%");

        }

        if (mmShippingSearchDTO.getKeywordB() != null && !"".equals(mmShippingSearchDTO.getKeywordB())) {
            query.setParameter("mmMaterialCodeDescription", "%" + mmShippingSearchDTO.getKeywordB() + "%");
            countQuery.setParameter("mmMaterialCodeDescription", "%" + mmShippingSearchDTO.getKeywordB() + "%");

        }

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
        List<MmShippingDetailsDTO> mmShippingDetailsDTOS = new ArrayList<MmShippingDetailsDTO>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmShippingDetailsDTO mmShippingDetailsDTO = new MmShippingDetailsDTO();
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
            BeanUtils.copyProperties(newMap, mmShippingDetailsDTO);

            if (newMap.get("dQty") != null) {
                mmShippingDetailsDTO.setDesignQty((Integer) newMap.get("dQty"));
            }
            if (newMap.get("mmRequisitionNo") != null) {
                mmShippingDetailsDTO.setMmRequisitionNo((String) newMap.get("mmRequisitionNo"));
            }
            if (newMap.get("mmRequisitionNo") != null) {
                mmShippingDetailsDTO.setMmRequisitionNo((String) newMap.get("mmRequisitionNo"));
            }

            if (newMap.get("id") != null) {
                mmShippingDetailsDTO.setMmRequisitionDetailId(((BigInteger) newMap.get("id")).longValue());
            }

            mmShippingDetailsDTOS.add(mmShippingDetailsDTO);

        }
        return new PageImpl<>(mmShippingDetailsDTOS, mmShippingSearchDTO.toPageable(), count.longValue());

    }

}
