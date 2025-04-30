package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmMaterialInStockDetailEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 在库材料明细表库。
 */
@Transactional
public interface MmMaterialInStockDetailRepository extends PagingAndSortingWithCrudRepository<MmMaterialInStockDetailEntity, Long>, MmMaterialInStockDetailRepositoryCustom {

    Page<MmMaterialInStockDetailEntity> findByOrgIdAndProjectIdAndMmMaterialInStockIdAndStatus(Long orgId, Long projectId, Long mmMaterialInStockId, EntityStatus status, Pageable pageable);

    MmMaterialInStockDetailEntity findByOrgIdAndProjectIdAndMmMaterialInStockIdAndMmMaterialCodeNoAndSpecValueAndStatus(
        Long orgId,
        Long projectId,
        Long mmMaterialInStockId,
        String mmMaterialCodeNo,
        Double specValue,
        EntityStatus status
    );

    MmMaterialInStockDetailEntity findByOrgIdAndProjectIdAndMmMaterialCodeNoAndSpecValueAndStatus(
        Long orgId,
        Long projectId,
        String mmMaterialCodeNo,
        Double specValue,
        EntityStatus status
    );

    MmMaterialInStockDetailEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

}
