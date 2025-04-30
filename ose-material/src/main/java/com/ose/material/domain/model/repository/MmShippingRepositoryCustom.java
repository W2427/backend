package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmShippingSearchDTO;
import com.ose.material.entity.MmShippingEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 发货单库
 */
@Transactional
public interface MmShippingRepositoryCustom {

    Page<MmShippingEntity> search(Long orgId,
                                  Long projectId,
                                  MmShippingSearchDTO mmShippingSearchDTO);

}
