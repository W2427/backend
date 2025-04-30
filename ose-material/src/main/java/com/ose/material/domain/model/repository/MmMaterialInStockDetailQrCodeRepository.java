package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmMaterialInStockDetailQrCodeEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 在库材料二维码库。
 */
@Transactional
public interface MmMaterialInStockDetailQrCodeRepository extends PagingAndSortingWithCrudRepository<MmMaterialInStockDetailQrCodeEntity, Long> {

    Page<MmMaterialInStockDetailQrCodeEntity> findByOrgIdAndProjectIdAndMmMaterialInStockDetailIdAndStatus(Long orgId, Long projectId, Long mmMaterialInStockDetailId, EntityStatus status, Pageable pageable);

    MmMaterialInStockDetailQrCodeEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus status);

    MmMaterialInStockDetailQrCodeEntity findByOrgIdAndProjectIdAndQrCodeAndStatus(Long orgId, Long projectId, String qrCode, EntityStatus status);

    MmMaterialInStockDetailQrCodeEntity findByOrgIdAndProjectIdAndPieceTagNoAndStatus(Long orgId, Long projectId, String pieceTagNo, EntityStatus status);

    MmMaterialInStockDetailQrCodeEntity findByOrgIdAndProjectIdAndMmMaterialCodeNoAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndSpecValueAndStatus(
        Long orgId,
        Long projectId,
        String mmMaterialCodeNo,
        String pieceTagNo,
        String heatBatchNo,
        String materialCertificate,
        Double specValue,
        EntityStatus status
    );
}
