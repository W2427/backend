package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.*;
import com.ose.material.entity.MmSurplusMaterialEntity;
import org.springframework.data.domain.Page;

/**
 * 余料库
 */
public interface MmSurplusMaterialInterface {

    /**
     * 余料库。
     */
    Page<MmSurplusMaterialEntity> search(
        Long orgId,
        Long projectId,
        MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO
    );

    /**
     * 创建余料库。
     */
    MmSurplusMaterialEntity create(
        Long orgId,
        Long projectId,
        MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新余料库。
     */
    MmSurplusMaterialEntity update(
        Long orgId,
        Long projectId,
        Long surplusMaterialId,
        MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO,
        ContextDTO contextDTO
    );

    // task服务调用，查找余料信息
    MmSurplusMaterialEntity searchSurplusMaterial(
        Long orgId,
        Long projectId,
        MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO);
}
