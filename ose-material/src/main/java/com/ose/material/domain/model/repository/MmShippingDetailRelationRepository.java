package com.ose.material.domain.model.repository;
import com.ose.material.entity.MmReleaseReceiveDetailQrCodeEntity;
import com.ose.material.entity.MmShippingDetailRelationEntity;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 发货单详情关联请购单详情库。
 */
@Transactional
public interface MmShippingDetailRelationRepository extends PagingAndSortingWithCrudRepository<MmShippingDetailRelationEntity, Long>, MmShippingDetailRelationRepositoryCustom{

    List<MmShippingDetailRelationEntity> findByOrgIdAndProjectIdAndShippingDetailIdAndStatus(
        Long orgId,
        Long projectId,
        Long shippingDetailId,
        EntityStatus entityStatus
    );

    MmShippingDetailRelationEntity findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long id,
        EntityStatus entityStatus
    );

    List<MmShippingDetailRelationEntity> findByOrgIdAndProjectIdAndShippingIdAndStatus(
        Long orgId,
        Long projectId,
        Long shippingId,
        EntityStatus entityStatus
    );

}
