package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmReleaseNotePrintDTO;
import com.ose.repository.BaseRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MmReleaseReceiveDetailQrCodeRepositoryImpl extends BaseRepository implements MmReleaseReceiveDetailQrCodeRepositoryCustom {

    @Override
    public List<MmReleaseNotePrintDTO> findByMaterialReceiveNoteIdAndMaterialReceiveNoteDetailId(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailId,
        Boolean finished
    ) {
        EntityManager entityManager = getEntityManager();
        StringBuffer sqlStringBuffer = new StringBuffer()
            .append("SELECT mrrdq.`qr_code`, mrrd.`spec_qty`, mrrd.`design_unit`, mrrd.`mm_material_code_no`, mrrd.`ident_code`, mrrd.`qr_code_type`, mrrd.`spec_name`, mrrd.`mm_material_code_description` ")
            .append(" FROM mm_release_receive mrr ")
            .append(" LEFT JOIN mm_release_receive_detail mrrd ON mrrd.`release_receive_id` = mrr.`id` ")
            .append(" LEFT JOIN mm_release_receive_detail_qr_code mrrdq ON mrrdq.`release_receive_detail_id` = mrrd.`id` ")
            .append(" WHERE mrr.`id` = :materialReceiveNoteId AND mrrd.`id` = :materialReceiveNoteDetailId ")
            .append(" AND mrr.`org_id` = :orgId AND mrr.`project_id` = :projectId ");

        if (finished) {
            sqlStringBuffer.append(" AND mrrdq.`inventory_qty` !=0 ");
        }

        String sql = sqlStringBuffer.toString();
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("orgId", orgId);
        query.setParameter("projectId", projectId);
        query.setParameter("materialReceiveNoteId", materialReceiveNoteId);
        query.setParameter("materialReceiveNoteDetailId", materialReceiveNoteDetailId);

        query.unwrap(NativeQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<Map<String, Object>> list = query.getResultList();

        List<MmReleaseNotePrintDTO> rsList = new ArrayList<>();
        for (Map<String, Object> m : list) {
            MmReleaseNotePrintDTO dto = new MmReleaseNotePrintDTO();
            if (m.get("qr_code") != null) {
                dto.setQrCode((String) m.get("qr_code"));
            }
            if (m.get("mm_material_code_no") != null) {
                dto.setMaterialCodeNo((String) m.get("mm_material_code_no"));
            }
            if (m.get("ident_code") != null) {
                dto.setIdentCode((String) m.get("ident_code"));
            }
            if (m.get("qr_code_type") != null) {
                dto.setQrCodeType((String) m.get("qr_code_type"));
            }
            if (m.get("spec_name") != null) {
                dto.setSpecName((String) m.get("spec_name"));
            }
            if (m.get("mm_material_code_description") != null) {
                dto.setDescription((String) m.get("mm_material_code_description"));
            }
            if (m.get("spec_qty") != null) {
                dto.setSpecQty(m.get("spec_qty").toString());
            }
            if (m.get("design_unit") != null) {
                dto.setDesignUnit((String) m.get("design_unit"));
            }
            rsList.add(dto);
        }

        return rsList;
    }

}
