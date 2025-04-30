package com.ose.material.domain.model.service;

import com.ose.material.dto.MmMaterialImportHistorySearchDTO;
import com.ose.material.entity.MmImportBatchTask;
import org.springframework.data.domain.Page;

/**
 * 材料导入历史接口
 */
public interface MmMaterialImportHistoryInterface {

    /**
     * 导入记录。
     *
     * @param orgId
     * @param projectId
     * @param entityId
     * @return
     */
    Page<MmImportBatchTask> search(
        Long orgId,
        Long projectId,
        Long entityId,
        MmMaterialImportHistorySearchDTO mmMaterialImportHistorySearchDTO
    );
}
