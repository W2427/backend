package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmSurplusMaterialSearchDTO;
import com.ose.material.entity.MmSurplusMaterialEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 余料库
 */
@Transactional
public interface MmSurplusMaterialRepositoryCustom {

    Page<MmSurplusMaterialEntity> search(
        Long orgId,
        Long projectId,
        MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO
    );

}
