package com.ose.tasks.domain.model.repository.wbs.piping;


import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.entity.wbs.entity.PressureTestPackageEntityBase;

import java.util.List;

/**
 * 试压包实体 CRUD 操作接口。
 */
public interface PressureTestPackageEntityRepository extends
    WBSEntityBaseRepository<PressureTestPackageEntityBase>,
    WBSEntityCustomRepository<PressureTestPackageEntityBase, WBSEntryCriteriaBaseDTO> {

    List<PressureTestPackageEntityBase> findByProjectIdAndDeletedIsFalse(Long projectId);
}
