package com.ose.tasks.domain.model.repository.process;

import com.ose.tasks.entity.BpmnTaskRelation;
import com.ose.tasks.entity.process.ModuleProcessDefinition;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * BPMN 解析 大流程 定义数据仓库。
 */
@Transactional
public interface BpmnTaskRelationRepository extends CrudRepository<BpmnTaskRelation, Long> {


    BpmnTaskRelation findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndCategoryAndDeletedIsFalse(Long orgId,
                                                                                                     Long projectId,
                                                                                                     Long moduleProcessDefinitionId,
                                                                                                     String category);

    List<BpmnTaskRelation> findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndCategoryInAndDeletedIsFalse(
        Long orgId,
        Long projectId,
        Long moduleProcessDefinitionId,
        Set<String> categorySet
    );

    @Modifying
    @Transactional
    @Query("UPDATE BpmnTaskRelation btr SET btr.deleted = true WHERE btr.orgId = :orgId AND btr.projectId = :projectId " +
        "AND btr.moduleProcessDefinitionId = :moduleProcessDefinitionId")
    void deleteByModuleProcessDefinitionId(@Param("orgId") Long orgId,
                                           @Param("projectId") Long projectId,
                                           @Param("moduleProcessDefinitionId") Long moduleProcessDefinitionId);


    BpmnTaskRelation findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndNodeIdAndDeletedIsFalse(Long orgId,
                                                                                            Long projectId,
                                                                                            Long moduleProcessDefinitionId,
                                                                                            String predecessorId);

    Boolean existsByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndNodeIdAndDeletedIsFalse(Long orgId,
                                                                                            Long projectId,
                                                                                            Long moduleProcessDefinitionId,
                                                                                            String predecessorId);

    Set<BpmnTaskRelation> findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndNodeIdInAndDeletedIsFalse(Long orgId, Long projectId, Long moduleProcessDefinitionId, Set<String> predecessorIds);

    List<BpmnTaskRelation> findByOrgIdAndProjectIdAndModuleProcessDefinitionIdInAndDeletedIsFalse(Long orgId, Long projectId, Set<Long> moduleProcessDefinitionIds);

    BpmnTaskRelation findByOrgIdAndProjectIdAndModuleProcessDefinitionIdAndNodeTypeAndDeletedIsFalse(Long orgId, Long projectId, Long moduleProcessDefinitionId, String nodeType);

    @Query("SELECT md FROM ModuleProcessDefinition md JOIN BpmnTaskRelation btr ON md.id = btr.moduleProcessDefinitionId " +
        " WHERE btr.projectId = :projectId AND btr.category = :category AND md.funcPart = :funcPart AND md.deleted = false")
    Set<ModuleProcessDefinition> findModuleDefinitions(@Param("projectId") Long projectId,
                                                       @Param("funcPart") String funcPart,
                                                       @Param("category") String category);
}
