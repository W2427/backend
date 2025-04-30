package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmMaterialInStockEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 在库材料主表库。
 */
@Transactional
public interface MmMaterialInStockRepository extends PagingAndSortingWithCrudRepository<MmMaterialInStockEntity, Long>, MmMaterialInStockRepositoryCustom {

    Page<MmMaterialInStockEntity> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus status, Pageable pageable);

    MmMaterialInStockEntity findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long id);

    MmMaterialInStockEntity findByOrgIdAndProjectIdAndMmMaterialCodeNoAndStatus(Long orgId, Long projectId, String mmMaterialCodeNo, EntityStatus status);

    MmMaterialInStockEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);
}
