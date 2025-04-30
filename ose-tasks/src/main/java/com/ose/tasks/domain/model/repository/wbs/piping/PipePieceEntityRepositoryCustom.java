package com.ose.tasks.domain.model.repository.wbs.piping;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.wbs.PipePieceEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.PipePieceEntity;
import org.springframework.data.domain.Page;

/**
 * 管件实体 CRUD 操作接口。
 */
public interface PipePieceEntityRepositoryCustom {

    Page<PipePieceEntity> search(
        Long orgId,
        Long projectId,
        PipePieceEntryCriteriaDTO criteriaDTO,
        PageDTO pageDTO);
}
