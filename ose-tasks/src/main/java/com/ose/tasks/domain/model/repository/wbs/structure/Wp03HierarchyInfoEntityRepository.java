package com.ose.tasks.domain.model.repository.wbs.structure;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.Wp03EntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp03HierarchyInfoEntity;

/**
 * WP3 Hierarchy Infor Entity CRUD 操作接口。
 */
public interface Wp03HierarchyInfoEntityRepository extends WBSEntityBaseRepository<Wp03HierarchyInfoEntity>,
    WBSEntityCustomRepository<Wp03HierarchyInfoEntity, Wp03EntryCriteriaDTO> {

}
