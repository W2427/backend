package com.ose.tasks.domain.model.service.material;

import com.ose.dto.PageDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.entity.material.FMaterialStructureNestDrawing;
import org.springframework.data.domain.Page;

public interface FMaterialStructureNestDrawingInterface extends EntityInterface {

    /**
     * 查询结构套料零件列表。
     *
     * @param orgId                    组织id
     * @param projectId                项目id
     * @param fMaterialStructureNestId 结构套料方案id
     * @param pageDTO                  分页参数
     * @return
     */
    Page<FMaterialStructureNestDrawing> search(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        PageDTO pageDTO
    );
}
