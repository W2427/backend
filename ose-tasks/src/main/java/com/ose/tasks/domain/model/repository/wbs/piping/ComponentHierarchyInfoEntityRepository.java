package com.ose.tasks.domain.model.repository.wbs.piping;


import com.ose.tasks.domain.model.repository.wbs.WBSEntityBaseRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntityCustomRepository;
import com.ose.tasks.dto.ComponentEntryCriteriaDTO;
import com.ose.tasks.dto.wbs.WeldEntryCriteriaDTO;
import com.ose.tasks.entity.wbs.entity.ComponentHierarchyInfoEntity;
import com.ose.tasks.entity.wbs.entity.WeldHierarchyInfoEntity;

/**
 * 管件实体视图 CRUD 操作接口。
 */
public interface ComponentHierarchyInfoEntityRepository extends WBSEntityBaseRepository<ComponentHierarchyInfoEntity>,
    WBSEntityCustomRepository<ComponentHierarchyInfoEntity, ComponentEntryCriteriaDTO> {

}
