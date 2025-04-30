package com.ose.tasks.domain.model.repository.wbs.structure;

import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.Wp01EntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp01HierarchyInfoEntity;

/**
 * 管段实体 CRUD 操作接口。
 */
public interface Wp01HierarchyInfoEntityRepository extends WBSEntityBaseRepository<Wp01HierarchyInfoEntity>,
    WBSEntityCustomRepository<Wp01HierarchyInfoEntity, Wp01EntryCriteriaDTO> {


}
