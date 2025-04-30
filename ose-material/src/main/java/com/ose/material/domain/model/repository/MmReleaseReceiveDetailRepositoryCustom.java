package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmReleaseReceiveDetailSearchDTO;
import com.ose.material.entity.MmReleaseReceiveDetailEntity;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


/**
 * 入库材料二维码库。
 */
@Transactional
public interface MmReleaseReceiveDetailRepositoryCustom {

    Page<MmReleaseReceiveDetailEntity> searchDetails(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        MmReleaseReceiveDetailSearchDTO mmReleaseReceiveDetailSearchDTO);

}
