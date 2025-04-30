package com.ose.tasks.domain.model.service.material;

import com.ose.dto.PageDTO;
import com.ose.tasks.domain.model.repository.material.FMaterialStructureNestProgramRepository;
import com.ose.tasks.domain.model.service.material.FMaterialStructureNestProgramInterface;
import com.ose.tasks.entity.material.FMaterialStructureNestProgram;
import com.ose.vo.EntityStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class FMaterialStructureNestProgramService implements FMaterialStructureNestProgramInterface {

    private FMaterialStructureNestProgramRepository fMaterialStructureNestProgramRepository;

    /**
     * 构造方法
     */
    @Autowired
    public FMaterialStructureNestProgramService(
        FMaterialStructureNestProgramRepository fMaterialStructureNestProgramRepository
    ) {
        this.fMaterialStructureNestProgramRepository = fMaterialStructureNestProgramRepository;
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
    public Page<FMaterialStructureNestProgram> search(
        Long orgId,
        Long projectId,
        Long fMaterialStructureNestId,
        PageDTO pageDTO
    ) {
        return fMaterialStructureNestProgramRepository.findByOrgIdAndProjectIdAndFMaterialStructureNestIdAndStatus(orgId, projectId, fMaterialStructureNestId, EntityStatus.ACTIVE, pageDTO.toPageable());
    }


}
