package com.ose.material.domain.model.repository;

import com.ose.dto.OperatorDTO;
import com.ose.material.dto.MmReleaseReceiveSearchDTO;
import com.ose.material.entity.MmReleaseReceiveEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

/**
 * 入库材料二维码库。
 */
@Transactional
public interface MmReleaseReceiveRepositoryCustom {

    Page<MmReleaseReceiveEntity> search(Long orgId,
                                        Long projectId,
                                        MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO);

    Page<MmReleaseReceiveEntity> searchByAssignee(Long orgId,
                                                  Long projectId,
                                                  MmReleaseReceiveSearchDTO mmReleaseReceiveSearchDTO,
                                                  OperatorDTO operatorDTO);

}
