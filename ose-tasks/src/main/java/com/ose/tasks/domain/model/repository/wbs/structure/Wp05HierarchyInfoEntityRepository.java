package com.ose.tasks.domain.model.repository.wbs.structure;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.Wp05EntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp05HierarchyInfoEntity;

/**
 * WP4 Hierarchy Infor Entity CRUD 操作接口。
 */
public interface Wp05HierarchyInfoEntityRepository extends WBSEntityBaseRepository<Wp05HierarchyInfoEntity>,
    WBSEntityCustomRepository<Wp05HierarchyInfoEntity, Wp05EntryCriteriaDTO> {

}
