package com.ose.material.domain.model.service.impl;

import com.ose.material.domain.model.repository.*;
import com.ose.material.domain.model.service.MmMaterialImportHistoryInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class MmMaterialImportHistoryService implements MmMaterialImportHistoryInterface {

    /**
     * 请购  操作仓库。
     */
    private final MmImportBatchTaskRepository mmImportBatchTaskRepository;

    /**
     * 构造方法。
     *
     * @param mmImportBatchTaskRepository 请购 操作仓库
     */
    @Autowired
    public MmMaterialImportHistoryService(
        MmImportBatchTaskRepository mmImportBatchTaskRepository
    ) {
        this.mmImportBatchTaskRepository = mmImportBatchTaskRepository;
    }

    /**
     * 导入记录。
     *
     * @param orgId
     * @param projectId
     * @param mmMaterialImportHistorySearchDTO
     * @return
     */
    @Override
    public Page<MmImportBatchTask> search(
        Long orgId,
        Long projectId,
        Long entityId,
        MmMaterialImportHistorySearchDTO mmMaterialImportHistorySearchDTO
    ) {
        return mmImportBatchTaskRepository.findByOrgIdAndProjectIdAndEntityId(
            orgId,
            projectId,
            entityId,
            mmMaterialImportHistorySearchDTO.toPageable()
        );
    }
}
