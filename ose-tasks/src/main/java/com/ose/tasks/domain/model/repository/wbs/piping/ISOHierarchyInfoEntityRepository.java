package com.ose.tasks.domain.model.repository.wbs.piping;


import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.wbs.ISOEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.ISOHierarchyInfoEntity;

import java.util.Optional;

/**
 * 管线实体 CRUD 操作接口。
 */
public interface ISOHierarchyInfoEntityRepository extends WBSEntityBaseRepository<ISOHierarchyInfoEntity>,
    WBSEntityCustomRepository<ISOHierarchyInfoEntity, ISOEntryCriteriaDTO> {

    Optional<ISOHierarchyInfoEntity> findByOrgIdAndProjectIdAndNoAndDeletedIsFalse(Long orgId, Long projectId, String no);

    Optional<ISOHierarchyInfoEntity> findByOrgIdAndProjectIdAndIdAndDeletedIsFalse(Long orgId, Long projectId, Long id);

}
