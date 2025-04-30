package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmShippingDetailsDTO;
import com.ose.material.dto.MmShippingSearchDTO;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 发货单详情库
 */
@Transactional
public interface MmShippingDetailRepositoryCustom {

    Page<MmShippingDetailsDTO> searchRequisitionDetail(
        Long orgId,
        Long projectId,
        Long shippingId,
        MmShippingSearchDTO mmShippingSearchDTO
    );

}
