package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmShippingDetailEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 发货单详情库。
 */
@Transactional
public interface MmShippingDetailRepository extends PagingAndSortingWithCrudRepository<MmShippingDetailEntity, Long>, MmShippingDetailRepositoryCustom {

    List<MmShippingDetailEntity> findByOrgIdAndProjectIdAndShippingIdAndStatusOrderByMmMaterialCodeNo(Long orgId, Long projectId, Long shippingId, EntityStatus entityStatus);

    MmShippingDetailEntity findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long id,
        EntityStatus entityStatus
    );

    MmShippingDetailEntity findByOrgIdAndProjectIdAndPieceTagNoAndStatus(
        Long orgId,
        Long projectId,
        String pieceTagNo,
        EntityStatus entityStatus
    );

    MmShippingDetailEntity findByOrgIdAndProjectIdAndShippingIdAndMmMaterialCodeNoAndSpecDescriptionAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndWareHouseLocationIdAndShippingNoAndStatus(
        Long orgId,
        Long projectId,
        Long shippingId,
        String mmMaterialCodeNo,
        String specDescription,
        String pieceTagNo,
        String heatBatchNo,
        String materialCertificate,
        Long wareHouseLocationId,
        String shippingNo,
        EntityStatus status
    );

    MmShippingDetailEntity findByOrgIdAndProjectIdAndShippingIdAndMmMaterialCodeNoAndSpecDescriptionAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndWareHouseLocationIdAndShippingNoAndSpecValueAndStatus(
        Long orgId,
        Long projectId,
        Long shippingId,
        String mmMaterialCodeNo,
        String specDescription,
        String pieceTagNo,
        String heatBatchNo,
        String materialCertificate,
        Long wareHouseLocationId,
        String shippingNo,
        Double specValue,
        EntityStatus status
    );

    List<MmShippingDetailEntity>findByOrgIdAndProjectIdAndMmMaterialCodeNoAndStatus(
        Long orgId,
        Long projectId,
        String mmMaterialCodeNo,
        EntityStatus status
    );
}
