package com.ose.tasks.domain.model.repository.bpm;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.bpm.TaskPackageDTO;
import com.ose.tasks.entity.bpm.BpmExInspApply;
import com.ose.tasks.entity.bpm.BpmRuTask;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BpmRuTaskRepositoryCustom {

    Page<BpmRuTask> getRuTaskList(Long orgId, Long projectId, String assignee, TodoTaskCriteriaDTO taskCriteria,
                                  List<Long> actInstIds, PageDTO pageDTO);


    Page<TodoTaskDTO> getTodoTaskList(Long orgId, Long projectId, String assignee, List<String> taskPackageList,
                                             TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO,
                                             List<Long> entityIdList);

    List<TaskPackageDTO> getTaskPackageList(Long orgId,
                                            Long projectId,
                                            String assignee);


    Page<TodoTaskDTO> getAllTodoTaskList(Long orgId,
                                         Long projectId,
                                         TodoTaskCriteriaDTO taskCriteria,
                                         PageDTO pageDTO,
                                         List<Long> entityIdList);

    List<TodoTaskMobileCriteriaDTO> getTodoTaskForMobile(Long orgId, Long projectId, String assignee);

    Page<BpmRuTask> getExternalInspectionRuTaskList(Long orgId, Long projectId, String assignee,
                                                    List<Long> actInstIds, PageDTO pageDTO);

    Page<BpmExInspApply> getExternalInspectionApplyList(Long orgId, Long projectId, List<String> assignees, ExInspApplyCriteriaDTO criteriaDTO);

    List<HierarchyBaseDTO> getEntitiyCategoriesInRuTask(List<String> entityModuleNames, String taskNode, String taskDefKey, Long processStageId, Long processId, Long orgId, Long projectId, String assignee);

    List<HierarchyBaseCountDTO> getProcessStagesInRuTask(Long orgId, Long projectId, String assignee, HierarchyCriteriaDTO criteriaDTO);

    List<HierarchyBaseDTO> getProcessesInRuTask(Long processStageId, Long orgId, Long projectId, String assignee);

    List<HierarchyBaseCountDTO> getProcessesInRuTask(Long projectId, String assignee, HierarchyCriteriaDTO criteriaDTO);

    List<TaskNodeDTO> getTaskNodesInRuTask(Long processStageId, Long processId, Long orgId, Long projectId, String assignee);

    List<String> getEntityModuleNameInRuTask(String taskNode, String taskDefKey, Long processStageId, Long processId, Long orgId,
                                             Long projectId, String assignee);

    List<String> getProcessCategoriesInRuTask(Long orgId, Long projectId, String assignee);


    HierarchyBaseCountDTO findDTOById(@Param("processId") Long processId);


    ExInspApplyFilterConditionDTO getExternalInspectionApplyFilterCondition(Long orgId, Long projectId,
                                                                            List<Long> assignees);


    List<HierarchyStageProcessDTO> getStageProcessesInRuTask(Long orgId, Long projectId, String assignee,
                                                             String taskDefKey);

    Page<TodoTaskForemanDispatchDTO> searchForemanDispatchTodo(Long orgId, Long projectId, String assignee,
                                                               TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO, List<Long> entityIdList);
}

