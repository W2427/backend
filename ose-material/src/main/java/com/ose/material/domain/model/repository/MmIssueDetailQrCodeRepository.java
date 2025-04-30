package com.ose.material.domain.model.repository;


import com.ose.material.entity.MmIssueDetailQrCodeEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 出库单明细二维码表库。
 */
@Transactional
public interface MmIssueDetailQrCodeRepository extends PagingAndSortingWithCrudRepository<MmIssueDetailQrCodeEntity, Long> {

    Page<MmIssueDetailQrCodeEntity> findByOrgIdAndProjectIdAndMmIssueDetailIdAndStatus(Long orgId, Long projectId, Long mmIssueDetailId, EntityStatus status, Pageable pageable);


    MmIssueDetailQrCodeEntity findByOrgIdAndProjectIdAndMmIssueDetailIdAndQrCodeAndStatus(
        Long orgId,
        Long projectId,
        Long mmIssueDetailId,
        String qrCode,
        EntityStatus status
    );

    List<MmIssueDetailQrCodeEntity> findByOrgIdAndProjectIdAndQrCodeAndStatus(
        Long orgId,
        Long projectId,
        String qrCode,
        EntityStatus status
    );

    MmIssueDetailQrCodeEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

}
