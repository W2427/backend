package com.ose.tasks.domain.model.repository.wbs.structure;


import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.Wp02EntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.Wp02HierarchyInfoEntity;

/**
 * WP2 Hierarchy Infor Entity CRUD 操作接口。
 */
public interface Wp02HierarchyInfoEntityRepository extends WBSEntityBaseRepository<Wp02HierarchyInfoEntity>,
    WBSEntityCustomRepository<Wp02HierarchyInfoEntity, Wp02EntryCriteriaDTO> {

}
