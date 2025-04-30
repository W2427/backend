package com.ose.material.domain.model.service;

import com.ose.material.dto.*;
import com.ose.material.entity.MmMaterialInStockDetailEntity;
import com.ose.material.entity.MmMaterialInStockDetailQrCodeEntity;
import com.ose.material.entity.MmMaterialInStockEntity;
import org.springframework.data.domain.Page;

/**
 * 查找在库材料列表
 */
public interface MmMaterialInStockInterface {

    /**
     * 查找在库材料列表。
     */
    Page<MmMaterialInStockEntity> search(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );

    /**
     * 查找在库材料列表。
     */
    MmMaterialInStockEntity detail(
        Long orgId,
        Long projectId,
        Long MaterialStockId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );

    /**
     * 查找在库材料明细。
     */
    Page<MmMaterialInStockDetailEntity> searchDetail(
        Long orgId,
        Long projectId,
        Long materialStockEntityId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );

    /**
     * 查找在库材料明细。
     */
    Page<MmMaterialInStockDetailQrCodeEntity> searchDetailQrCode(
        Long orgId,
        Long projectId,
        Long materialStockEntityId,
        Long materialStockDetailEntityId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );

    MmReleaseReceiveQrCodeResultDTO searchQrCode(
        Long orgId,
        Long projectId,
        String qrCode
    );
    MmQrCodeResultDTO qrCode(
        Long orgId,
        Long projectId,
        String qrCode
    );

    /**
     * 查找在库材料详情列表。
     */
    Page<MmMaterialInStockDetailEntity> searchMaterialDetail(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );

   Page<MmReleaseReceiveQrCodeResultDTO>  searchQrCodes(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );

    /**
     * 查找在库材料详情列表。
     */
    MmMaterialInStockDetailQrCodeEntity searchMaterialInformation(
        Long orgId,
        Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );
}
