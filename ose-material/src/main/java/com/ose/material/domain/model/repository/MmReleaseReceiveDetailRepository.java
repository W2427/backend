package com.ose.material.domain.model.repository;

import com.ose.material.entity.MmReleaseReceiveDetailEntity;
import com.ose.material.entity.MmShippingDetailEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 材料入库单库。
 */
@Transactional
public interface MmReleaseReceiveDetailRepository extends PagingAndSortingWithCrudRepository<MmReleaseReceiveDetailEntity, Long>, MmReleaseReceiveDetailRepositoryCustom {

    List<MmReleaseReceiveDetailEntity> findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatusOrderByIdentCode(Long orgId, Long projectId, Long releaseReceiveId, EntityStatus status);

//    Page<MmReleaseReceiveDetailEntity> findByOrgIdAndProjectIdAndReleaseReceiveIdAndReleasedQtyNotAndStatusOrderByLastModifiedAtDesc(Long orgId, Long projectId, Long releaseReceiveId, Integer releasedQty, EntityStatus status, Pageable pageable);
//

//    Page<MmReleaseReceiveDetailEntity> findByOrgIdAndProjectIdAndReleaseReceiveIdAndReleasedQtyNotAndStatusOrderByIdentCode(Long orgId, Long projectId, Long releaseReceiveId, Integer releasedQty, EntityStatus status, Pageable pageable);

    MmReleaseReceiveDetailEntity findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long id);

    MmReleaseReceiveDetailEntity findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long mmReleaseReceiveDetailEntityId, EntityStatus status);

    MmReleaseReceiveDetailEntity findByOrgIdAndProjectIdAndReleaseReceiveIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        Long releaseReceiveDetailId,
        EntityStatus status);

    List<MmReleaseReceiveDetailEntity> findByOrgIdAndProjectIdAndReleaseReceiveIdAndStatus(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        EntityStatus status
    );

    MmReleaseReceiveDetailEntity findByOrgIdAndProjectIdAndPieceTagNoAndStatus(
        Long orgId,
        Long projectId,
        String pieceTagNo,
        EntityStatus status);

//    List<MmReleaseReceiveDetailEntity> findByOrgIdAndProjectIdAndMmMaterialCodeNoAndSpecNameAndStatus(
//        Long orgId,
//        Long projectId,
//        String mmMaterialCodeNo,
//        String specName,
//        EntityStatus status);

//    MmReleaseReceiveDetailEntity findByOrgIdAndProjectIdAndReleaseReceiveIdAndMmMaterialCodeNoAndStatus(
//        Long orgId,
//        Long projectId,
//        Long releaseReceiveId,
//        String mmMaterialCodeNo,
//        EntityStatus status);

    MmReleaseReceiveDetailEntity findByOrgIdAndProjectIdAndReleaseReceiveIdAndMmMaterialCodeNoAndHeatBatchNoAndWareHouseLocationIdAndMaterialCertificateAndStatus(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
        String mmMaterialCodeNo,
        String heatBatchNo,
        Long wareHouseLocationId,
        String materialCertificate,
        EntityStatus status
    );

    MmReleaseReceiveDetailEntity findByOrgIdAndProjectIdAndReleaseReceiveIdAndMmMaterialCodeNoAndSpecDescriptionAndPieceTagNoAndHeatBatchNoAndMaterialCertificateAndWareHouseLocationIdAndShippingNoAndSpecValueAndStatus(
        Long orgId,
        Long projectId,
        Long releaseReceiveId,
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

    List<MmReleaseReceiveDetailEntity>findByOrgIdAndProjectIdAndMmMaterialCodeNoAndStatus(
        Long orgId,
        Long projectId,
        String mmMaterialCodeNo,
        EntityStatus status
    );

}
