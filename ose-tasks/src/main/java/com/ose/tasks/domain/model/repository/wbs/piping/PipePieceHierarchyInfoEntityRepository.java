package com.ose.tasks.domain.model.repository.wbs.piping;


import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.wbs.PipePieceEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.PipePieceHierarchyInfoEntity;

/**
 * 管段实体 CRUD 操作接口。
 */
public interface PipePieceHierarchyInfoEntityRepository extends WBSEntityBaseRepository<PipePieceHierarchyInfoEntity>,
    WBSEntityCustomRepository<PipePieceHierarchyInfoEntity, PipePieceEntryCriteriaDTO> {

}
