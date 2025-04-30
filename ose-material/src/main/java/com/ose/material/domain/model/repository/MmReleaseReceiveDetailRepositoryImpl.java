package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmReleaseReceiveDetailSearchDTO;
import com.ose.material.entity.MmReleaseReceiveDetailEntity;
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

public class MmReleaseReceiveDetailRepositoryImpl extends BaseRepository implements MmReleaseReceiveDetailRepositoryCustom {

    @Override
    public Page<MmReleaseReceiveDetailEntity> searchDetails(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveDetailSearchDTO mmReleaseReceiveDetailSearchDTO) {
        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();
        sql.append(" select m.* ");
        sql.append(" from mm_release_receive_detail m ");
        sql.append(" where m.org_id = :orgId ");
        sql.append(" and m.project_id =:projectId ");
        sql.append(" and m.release_receive_id =:releaseReceiveId ");

        if ((mmReleaseReceiveDetailSearchDTO.getWeChat() != null && mmReleaseReceiveDetailSearchDTO.getWeChat())
            || (mmReleaseReceiveDetailSearchDTO.getReleaseReceiveDetail() != null && mmReleaseReceiveDetailSearchDTO.getReleaseReceiveDetail())) {
            sql.append(" and m.receive_total_qty !=0 ");
        }
        if (mmReleaseReceiveDetailSearchDTO.getKeyword() != null) {
            sql.append(" and m.mm_material_code_no like :keyword  ");
        }
        if (mmReleaseReceiveDetailSearchDTO.getIdentCode() != null) {
            sql.append(" and m.ident_code like :identCode  ");
        }

        if (mmReleaseReceiveDetailSearchDTO.getInventory() != null && mmReleaseReceiveDetailSearchDTO.getInventory()) {
            sql.append(" and m.receive_total_qty <= 0 ");
        }

        sql.append(" and m.status = 'ACTIVE' ");

        if (mmReleaseReceiveDetailSearchDTO.getWeChat() != null && mmReleaseReceiveDetailSearchDTO.getWeChat()) {
            sql.append(" order by m.last_modified_at desc ");
        } else {
            sql.append(" order by m.ident_code asc ");
        }
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

        query.setParameter("releaseReceiveId", materialReceiveNoteId);
        countQuery.setParameter("releaseReceiveId", materialReceiveNoteId);

        if (mmReleaseReceiveDetailSearchDTO.getKeyword() != null) {
            query.setParameter("keyword", "%" + mmReleaseReceiveDetailSearchDTO.getKeyword() + "%");
            countQuery.setParameter("keyword", "%" + mmReleaseReceiveDetailSearchDTO.getKeyword() + "%");
        }
        if (mmReleaseReceiveDetailSearchDTO.getIdentCode() != null) {
            query.setParameter("identCode", "%" + mmReleaseReceiveDetailSearchDTO.getIdentCode() + "%");
            countQuery.setParameter("identCode", "%" + mmReleaseReceiveDetailSearchDTO.getIdentCode() + "%");
        }

        sql.append("LIMIT :start , :offset ");

        // 查询结果
        int pageNo = mmReleaseReceiveDetailSearchDTO.getPage().getNo();
        int pageSize = mmReleaseReceiveDetailSearchDTO.getPage().getSize();

        query.setParameter("start", (pageNo - 1) * pageSize);
        query.setParameter("offset", pageSize);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        // 获取查询结果
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> queryResultList = query.getResultList();
        // 获取数量结果
        Long count = (Long) countQuery.getSingleResult();

        // 设置结果（结构为MAP）到WPS对象中
        List<MmReleaseReceiveDetailEntity> mmReleaseReceiveDetailEntities = new ArrayList<MmReleaseReceiveDetailEntity>();
        for (Map<String, Object> resultMap : queryResultList) {
            MmReleaseReceiveDetailEntity mmReleaseReceiveDetailEntity = new MmReleaseReceiveDetailEntity();
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
            if (newMap.get("runningStatus") != null) {
                newMap.put("runningStatus", EntityStatus.valueOf((String) newMap.get("runningStatus")));
            }

            BeanUtils.copyProperties(newMap, mmReleaseReceiveDetailEntity);

            mmReleaseReceiveDetailEntities.add(mmReleaseReceiveDetailEntity);

        }
        return new PageImpl<>(mmReleaseReceiveDetailEntities, mmReleaseReceiveDetailSearchDTO.toPageable(), count.longValue());
    }

}
