package com.ose.tasks.domain.model.repository.process;

import com.ose.tasks.entity.ProcessBpmnRelation;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 工序 BPMN 解析 定义数据仓库。
 */
@Transactional
public interface ProcessBpmnRelationRepository extends CrudRepository<ProcessBpmnRelation, Long> {


    @Modifying
    @Transactional
    @Query("UPDATE BpmnTaskRelation btr SET btr.deleted = true WHERE btr.orgId = :orgId AND btr.projectId = :projectId " +
        "AND btr.moduleProcessDefinitionId = :moduleProcessDefinitionId")
    void deleteByModuleProcessDefinitionId(@Param("orgId") Long orgId,
                                           @Param("projectId") Long projectId,
                                           @Param("moduleProcessDefinitionId") Long moduleProcessDefinitionId);



    ProcessBpmnRelation findByProjectIdAndProcessIdAndBpmnVersionAndNodeId(Long projectId, Long processId, int bpmnVersion, String taskDefKey);

    List<ProcessBpmnRelation> findByProjectIdAndProcessIdAndBpmnVersion(Long projectId, Long processId, int version);
}
