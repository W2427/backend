package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmPurchasePackageVendorRelationEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购包和供货商关系 库。
 */
@Transactional
public interface MmPurchasePackageVendorRelationRepository extends PagingAndSortingWithCrudRepository<MmPurchasePackageVendorRelationEntity, Long>,MmPurchasePackageVendorRelationRepositoryCustom {
    MmPurchasePackageVendorRelationEntity findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(Long orgId, Long projectId, Long purchasePackageId, Long id, EntityStatus status);
}
