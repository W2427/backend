package com.ose.material.domain.model.service;

import com.ose.dto.ContextDTO;
import com.ose.material.dto.*;
import com.ose.material.entity.MmPurchasePackageEntity;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 采购包接口
 */
public interface MmPurchasePackageInterface {

    /**
     * 获取采购包。
     */
    Page<MmPurchasePackageEntity> search(
        Long orgId,
        Long projectId,
        MmPurchasePackageSearchDTO mmPurchasePackageSearchDTO
    );

    /**
     * 创建采购包。
     *
     * @param orgId
     * @param projectId
     * @param mmPurchasePackageCreateDTO
     * @return
     */
    MmPurchasePackageEntity create(
        Long orgId,
        Long projectId,
        MmPurchasePackageCreateDTO mmPurchasePackageCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 更新采购包。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @param mmPurchasePackageCreateDTO
     * @return
     */
    MmPurchasePackageEntity update(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageCreateDTO mmPurchasePackageCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 采购包详情。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     * @return
     */
    MmPurchasePackageEntity detail(
        Long orgId,
        Long projectId,
        Long purchasePackageId
    );

    /**
     * 删除采购包。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     */
    void delete(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        ContextDTO contextDTO
    );

    /**
     * 添加供货商。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId,
     * @param mmPurchasePackageVendorAddDTO
     * @return
     */
    void addVendor(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageVendorAddDTO mmPurchasePackageVendorAddDTO,
        ContextDTO contextDTO
    );

    /**
     * 查找采购包已添加供货商。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId,
     * @param mmPurchasePackageVendorSearchDTO
     * @return
     */
    Page<MmPurchasePackageVendorReturnDTO> searchVendor(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        MmPurchasePackageVendorSearchDTO mmPurchasePackageVendorSearchDTO
    );

    /**
     * 修改供货商。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId,
     * @param mmPurchasePackageVendorAddDTO
     * @return
     */
    void updateVendor(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageVendorRelationId,
        MmPurchasePackageVendorAddDTO mmPurchasePackageVendorAddDTO,
        ContextDTO contextDTO
    );

    /**
     * 删除采购包中的供货商。
     *
     * @param orgId
     * @param projectId
     * @param purchasePackageId
     */
    void deleteVendor(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        Long purchasePackageVendorRelationId,
        ContextDTO contextDTO
    );

    /**
     * 获取采购包相关实体子类型。
     */
    List<MmPurchasePackageEntitySubTypeDTO> searchEntitySubType(
        Long orgId,
        Long projectId,
        String entityType
    );

}
