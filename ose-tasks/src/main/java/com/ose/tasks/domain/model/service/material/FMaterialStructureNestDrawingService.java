package com.ose.tasks.domain.model.service.material;

import com.ose.dto.PageDTO;
import com.ose.tasks.domain.model.repository.material.FMaterialStructureNestDrawingRepository;
import com.ose.tasks.domain.model.service.material.FMaterialStructureNestDrawingInterface;
import com.ose.tasks.entity.material.FMaterialStructureNestDrawing;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FMaterialStructureNestDrawingService implements FMaterialStructureNestDrawingInterface {

    private FMaterialStructureNestDrawingRepository fMaterialStructureNestDrawingRepository;

    /**
     * 构造方法
     */
    @Autowired
    public FMaterialStructureNestDrawingService(
        FMaterialStructureNestDrawingRepository fMaterialStructureNestDrawingRepository
    ) {
        this.fMaterialStructureNestDrawingRepository = fMaterialStructureNestDrawingRepository;
    }

    /**
     * 查询结构套料零件列表。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param pageDTO   分页参数
     * @return
     */
    @Override
    public Page<FMaterialStructureNestDrawing> search(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        PageDTO pageDTO
    ) {
        return fMaterialStructureNestDrawingRepository.findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(orgId, projectId, fMaterialStructureNestId, EntityStatus.ACTIVE, pageDTO.toPageable());
    }

}
