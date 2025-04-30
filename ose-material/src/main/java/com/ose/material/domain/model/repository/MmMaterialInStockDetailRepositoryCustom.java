package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmMaterialInStockSearchDTO;
import com.ose.material.dto.MmReleaseReceiveQrCodeResultDTO;
import com.ose.material.entity.MmMaterialInStockDetailEntity;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;

public interface MmMaterialInStockDetailRepositoryCustom {

    Page<MmMaterialInStockDetailEntity> search(
        Long orgId,
        Long projectId,
        EntityStatus status,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );

    Page<MmReleaseReceiveQrCodeResultDTO> searchQrCodes(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );

}
