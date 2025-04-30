package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.MmPurchasePackageFileCreateDTO;
import com.ose.material.dto.MmPurchasePackageFileSearchDTO;
import com.ose.material.entity.MmPurchasePackageFileEntity;
import org.springframework.data.domain.Page;

/**
 * 采购包文件接口
 */
public interface MmPurchasePackageFileInterface {

    /**
     * 获取采购包文件。
     */
    Page<MmPurchasePackageFileEntity> search(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageFileSearchDTO mmPurchasePackageFileSearchDTO
    );

    /**
     * 创建采购包文件。
     *
     * @param orgId
     * @param projectId
     * @param mmPurchasePackageFileCreateDTO
     * @return
     */
    MmPurchasePackageFileEntity create(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageFileCreateDTO mmPurchasePackageFileCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 采购包文件详情。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @return
     */
    MmPurchasePackageFileEntity detail(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageFileId
    );

    /**
     * 删除采购包文件。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageFileId,
        ContextDTO contextDTO
    );
}
