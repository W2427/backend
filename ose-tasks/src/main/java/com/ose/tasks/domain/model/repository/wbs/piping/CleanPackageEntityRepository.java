package com.ose.tasks.domain.model.repository.wbs.piping;


import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.entity.wbs.entity.CleanPackageEntityBase;

import java.util.List;

/**
 * 清洁包实体 CRUD 操作接口。
 */
public interface CleanPackageEntityRepository extends
    WBSEntityBaseRepository<CleanPackageEntityBase>,
    WBSEntityCustomRepository<CleanPackageEntityBase, WBSEntryCriteriaBaseDTO> {

    List<CleanPackageEntityBase> findByProjectIdAndDeletedIsFalse(Long projectId);
}
