package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmReleaseReceiveDetailEntity;
import com.ose.material.entity.MmReleaseReceiveDetailQrCodeEntity;
import com.ose.material.vo.QrCodeType;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 入库材料二维码库。
 */
@Transactional
public interface MmReleaseReceiveDetailQrCodeRepository extends PagingAndSortingWithCrudRepository<MmReleaseReceiveDetailQrCodeEntity, Long>, MmReleaseReceiveDetailQrCodeRepositoryCustom {

    Page<MmReleaseReceiveDetailQrCodeEntity> findByOrgIdAndProjectIdAndReleaseReceiveDetailIdAndStatus(
        Long orgId,
        Long projectId,
        Long releaseReceiveDetailId,
        EntityStatus status,
        Pageable pageable
    );

//    Page<MmReleaseReceiveDetailQrCodeEntity> findByOrgIdAndProjectIdAndReleaseReceiveDetailIdAndInventoryQtyNotAndStatus(
//        Long orgId,
//        Long projectId,
//        Long releaseReceiveDetailId,
//        Integer inventoryQty,
//        EntityStatus status,
//        Pageable pageable
//    );

    List<MmReleaseReceiveDetailQrCodeEntity> findByOrgIdAndProjectIdAndReleaseReceiveDetailIdInAndQrCodeType(
        Long orgId,
        Long projectId,
        List<Long> releaseReceiveDetailIds,
        QrCodeType qrCodeType
    );

    MmReleaseReceiveDetailQrCodeEntity findByOrgIdAndProjectIdAndReleaseReceiveIdAndQrCodeAndStatus(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        String qrCode,
        EntityStatus status
    );

    List<MmReleaseReceiveDetailQrCodeEntity> findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatus(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        EntityStatus status
    );

    MmReleaseReceiveDetailQrCodeEntity findByOrgIdAndProjectIdAndReleaseReceiveDetailIdAndQrCode(
        Long orgId,
        Long projectId,
        Long releaseReceiveDetailId,
        String qrCode
    );

    List<MmReleaseReceiveDetailQrCodeEntity> findByOrgIdAndProjectIdAndQrCodeAndStatus(
        Long orgId,
        Long projectId,
        String qrCode,
        EntityStatus status
    );

    MmReleaseReceiveDetailQrCodeEntity findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteDetailQrCodeId,
        EntityStatus status
    );
}
