package com.ose.tasks.domain.model.repository.wbs.structure;


import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.structureWbs.StructureWeldEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.structureEntity.StructureWeldHierarchyInfoEntity;

/**
 * Structure Weld Entity CRUD 操作接口。
 */
public interface StructureWeldHierarchyInfoEntityRepository extends WBSEntityBaseRepository<StructureWeldHierarchyInfoEntity>,
    WBSEntityCustomRepository<StructureWeldHierarchyInfoEntity, StructureWeldEntryCriteriaDTO> {

}
