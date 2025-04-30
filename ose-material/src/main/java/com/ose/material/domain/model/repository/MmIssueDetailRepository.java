package com.ose.material.domain.model.repository;


import com.ose.material.entity.MmIssueDetailEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 出库单明细表库。
 */
@Transactional
public interface MmIssueDetailRepository extends PagingAndSortingWithCrudRepository<MmIssueDetailEntity, Long> {

    Page<MmIssueDetailEntity> findByOrgIdAndProjectIdAndMmIssueIdAndStatusOrderByIdentCode(Long orgId, Long projectId, Long mmIssueId, EntityStatus status, Pageable pageable);

    List<MmIssueDetailEntity> findByOrgIdAndProjectIdAndMmIssueIdAndStatus(Long orgId, Long projectId, Long mmIssueId, EntityStatus status);

    MmIssueDetailEntity findByOrgIdAndProjectIdAndMmIssueIdAndMmMaterialCodeNoAndSpecValueAndStatus(
        Long orgId,
        Long projectId,
        Long mmIssueId,
        String mmMaterialCodeNo,
        Double specValue,
        EntityStatus status
    );

    MmIssueDetailEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

}
