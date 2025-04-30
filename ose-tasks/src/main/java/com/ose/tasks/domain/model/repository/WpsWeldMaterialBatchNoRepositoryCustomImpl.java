package com.ose.tasks.domain.model.repository;
import com.ose.repository.BaseRepository;
import com.ose.tasks.dto.WpsWeldMaterialDTO;
import com.ose.tasks.entity.WeldMaterial;
import com.ose.tasks.vo.WeldMaterialType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WpsWeldMaterialBatchNoRepositoryCustomImpl extends BaseRepository implements WpsWeldMaterialBatchNoRepositoryCustom{
    /**
     * 获取焊材分类对应的批次列表。
     *
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param wpsWeldMaterialDTO 查询条件
     * @return 批次列表
     */
    @Override
    @Transactional(readOnly = true)
    public List<WeldMaterial> search(Long orgId, Long projectId, WpsWeldMaterialDTO wpsWeldMaterialDTO) {

        EntityManager entityManager = getEntityManager();
        StringBuilder sql = new StringBuilder();


        sql.append("SELECT DISTINCT(`wm`.`batch_no`), `wm`.`weld_material_type` ");
        sql.append("FROM `weld_material` AS `wm` LEFT JOIN `wps_simplified` AS `ws` ");
        sql.append("ON `ws`.`weld_material_type` LIKE concat( '%', `wm`.`weld_material_type`, '%' ) WHERE ");

        sql.append("`ws`.`status` = 'ACTIVE' ");

        if (wpsWeldMaterialDTO.getWpsNo() != null && !"".equals(wpsWeldMaterialDTO.getWpsNo())) {
            sql.append("AND `ws`.`no` IN (:wpsNo) ");
        }
        if (wpsWeldMaterialDTO.getWeldMaterialType() != null && !"".equals(wpsWeldMaterialDTO.getWeldMaterialType())) {
            sql.append("AND `wm`.`weld_material_type` = :weldMaterialType ");
        }
        sql.append("AND `ws`.`org_id` = :orgId AND `ws`.`project_id` = :projectId ");
        sql.append("AND `wm`.`status` = 'ACTIVE' ");
        sql.append("AND `wm`.`org_id` = :orgId AND `wm`.`project_id` = :projectId ");

        Query query = entityManager.createNativeQuery(sql.toString());

        if (wpsWeldMaterialDTO.getWpsNo() != null && !"".equals(wpsWeldMaterialDTO.getWpsNo())) {
            List<String> wpsNoList = new ArrayList<>();
            for(String no : wpsWeldMaterialDTO.getWpsNo().split(",")) {
                wpsNoList.add(no);
            }
            query.setParameter("wpsNo", wpsNoList);
        }
        if (wpsWeldMaterialDTO.getWeldMaterialType() != null && !"".equals(wpsWeldMaterialDTO.getWeldMaterialType())) {
            query.setParameter("weldMaterialType", wpsWeldMaterialDTO.getWeldMaterialType());
        }
        query.setParameter("projectId", projectId);
        query.setParameter("orgId", orgId);

        query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        List<Map<String, Object>> queryResultList = query.getResultList();

        List<WeldMaterial> list = new ArrayList<>();
        WeldMaterial weldMaterial;
        for (Map<String, Object> weldMaterialMap : queryResultList) {
            weldMaterial = new WeldMaterial();
            weldMaterial.setBatchNo((String) weldMaterialMap.get("batch_no"));
            weldMaterial.setWeldMaterialType(WeldMaterialType.valueOf((String) weldMaterialMap.get("weld_material_type")));
            list.add(weldMaterial);
        }
        return list;
    }
}
