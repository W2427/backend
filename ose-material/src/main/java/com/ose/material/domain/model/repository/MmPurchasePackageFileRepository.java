package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmPurchasePackageFileEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购包文件 库。
 */
@Transactional
public interface MmPurchasePackageFileRepository extends PagingAndSortingWithCrudRepository<MmPurchasePackageFileEntity, Long>, MmPurchasePackageFileCustom {

    MmPurchasePackageFileEntity findByOrgIdAndProjectIdAndPurchasePackageIdAndIdAndStatus(Long orgId, Long projectId, Long purchasePackageId, Long id, EntityStatus status);

}
