package com.ose.material.domain.model.repository;

import com.ose.material.dto.HeatBatchNoSearchDTO;
import com.ose.material.entity.MmHeatBatchNoEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Transactional
public interface HeatBatchNoRepositoryCustom {

    Page<MmHeatBatchNoEntity> search(Long orgId,
                                     Long projectId,
                                     HeatBatchNoSearchDTO heatBatchNoSearchDTO);

}
