package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.material.dto.*;
import com.ose.material.entity.MmImportBatchTask;
import com.ose.material.entity.MmPurchasePackageItemEntity;
import org.springframework.data.domain.Page;

/**
 * 采购包明细接口
 */
public interface MmPurchasePackageItemInterface {

    /**
     * 获取采购包明细。
     */
    Page<MmPurchasePackageItemEntity> search(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageItemSearchDTO mmPurchasePackageItemSearchDTO
    );

    /**
     * 创建采购包明细。
     *
     * @param orgId
     * @param projectId
     * @param mmPurchasePackageItemCreateDTO
     * @return
     */
    MmPurchasePackageItemEntity create(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageItemCreateDTO mmPurchasePackageItemCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新采购包明细。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @param mmPurchasePackageItemCreateDTO
     * @return
     */
    MmPurchasePackageItemEntity update(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageDetailId,
        MmPurchasePackageItemCreateDTO mmPurchasePackageItemCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 采购包明细详情。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @return
     */
    MmPurchasePackageItemEntity detail(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageDetailId
    );

    /**
     * 删除采购包详情。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageDetailId,
        ContextDTO contextDTO
    );

    /**
     * 导入采购包明细。
     *
     * @param orgId
     * @param projectId
     * @param operator
     * @param batchTask
     * @param importDTO
     * @return
     */
    MmImportBatchResultDTO importPurchasePackageItemBatch(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        OperatorDTO operator,
        MmImportBatchTask batchTask,
        MmImportBatchTaskImportDTO importDTO
    );
}
