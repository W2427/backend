package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmPurchasePackageItemEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 采购包明细 库。
 */
@Transactional
public interface MmPurchasePackageItemRepository extends PagingAndSortingWithCrudRepository<MmPurchasePackageItemEntity, Long> {

    Page<MmPurchasePackageItemEntity> findByOrgIdAndProjectIdAndPurchasePackageIdAndStatus(Long orgId, Long projectId, Long purchasePackageId, EntityStatus status, Pageable pageable);

    MmPurchasePackageItemEntity findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(Long orgId, Long projectId, Long purchasePackageId, Long id, EntityStatus status);

    MmPurchasePackageItemEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId,Long id, EntityStatus status);

    MmPurchasePackageItemEntity findByOrgIdAndProjectIdAndPurchasePackageIdAndMmMaterialCodeNoAndSpecDescriptionAndSpecValueAndStatus(
        Long orgId,
        Long projectId,
        Long purchasePackageId,
        String mmMaterialCodeNo,
        String specDescription,
        Double specValue,
        EntityStatus status
    );

    List<MmPurchasePackageItemEntity>findByOrgIdAndProjectIdAndMmMaterialCodeNoAndStatus(
        Long orgId,
        Long projectId,
        String mmMaterialCodeNo,
        EntityStatus status
    );

}
