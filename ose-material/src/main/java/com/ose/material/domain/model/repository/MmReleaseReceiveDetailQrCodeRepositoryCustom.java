package com.ose.material.domain.model.repository;

import com.ose.material.dto.MmReleaseNotePrintDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 入库材料二维码库。
 */
@Transactional
public interface MmReleaseReceiveDetailQrCodeRepositoryCustom {

    List<MmReleaseNotePrintDTO> findByMaterialReceiveNoteIdAndMaterialReceiveNoteDetailId(
        Long orgId,
        Long projectId,
        Long materialReceiveNoteId,
        Long materialReceiveNoteDetailId,
        Boolean finished
    );

}
