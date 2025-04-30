package com.ose.tasks.domain.model.repository.wbs.structure;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.Wp04EntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp04HierarchyInfoEntity;

/**
 * WP4 Hierarchy Infor Entity CRUD 操作接口。
 */
public interface Wp04HierarchyInfoEntityRepository extends WBSEntityBaseRepository<Wp04HierarchyInfoEntity>,
    WBSEntityCustomRepository<Wp04HierarchyInfoEntity, Wp04EntryCriteriaDTO> {

}
