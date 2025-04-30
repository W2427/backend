package com.ose.tasks.domain.model.repository;


import com.ose.tasks.dto.WpsWeldMaterialDTO;
import com.ose.tasks.entity.WeldMaterial;

import java.util.List;

public interface WpsWeldMaterialBatchNoRepositoryCustom {

    /**
     * 获取焊材分类对应的批次列表。
     *
     * @param orgId             组织ID
     * @param projectId         项目ID
     * @param wpsWeldMaterialDTO 查询条件
     * @return 批次列表
     */
    List<WeldMaterial> search(Long orgId, Long projectId, WpsWeldMaterialDTO wpsWeldMaterialDTO);
}
