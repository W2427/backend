package com.ose.tasks.domain.model.service.bpm;

import com.ose.dto.OperatorDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.SubDrawing;
import com.ose.tasks.vo.SuspensionState;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface ActivityTaskInterface {

    /**
     * 保存流程实例
     *
     * @param actInst
     * @return
     */
    BpmActivityInstanceBase saveActInst(BpmActivityInstanceBase actInst);

    /**
     * 取得实体的材料信息
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param entityId
     * @param entityType
     * @return
     */
    Map<String, Object> getEntityMaterialByEntityIdAndEntityType(Long orgId, Long projectId, Long entityId, String entityType);

    /**
     * 取得实体的图纸信息
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param entityId
     * @param entityType
     * @param actInst
     * @return
     */
    List<SubDrawing> getEntityDrawingByEntityIdAndEntityType(Long orgId, Long projectId, Long entityId, String entityType, BpmActivityInstanceBase actInst);

    /**
     * 创建任务分配记录
     *
     * @param assignee
     * @return
     */
    BpmActTaskAssignee saveActTaskAssignee(BpmActTaskAssignee assignee);

    /**
     * 查询任务流程实例列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param criteria
     * @return
     */
    Page<BpmActivityInstanceDTO> actInstList(Long orgId, Long projectId, ActInstCriteriaDTO criteria);

    /**
     * 根据id查找任务流程实例
     *
     * @param id
     * @return
     */
    BpmActivityInstanceBase findActInstById(Long id);

    /**
     * 根据actInstId查找任务流程实例
     *
     * @param actInstId
     * @return
     */
    BpmActivityInstanceBase findByProjectIdAndActInstId(Long orgId, Long projectId, Long actInstId);

    /**
     * 根据taskid查询任务完成信息
     *
     * @param taskId
     * @return
     */
    BpmActTask findActTaskByTaskId(String taskId);

    /**
     * 根据流程实例id获取任务分配信息
     *
     * @param actInstId
     * @return
     */
    List<BpmActTaskAssignee> findActTaskAssigneesByActInstId(Long actInstId);

    /**
     * 修改任务分配记录
     *
     * @param taskAssigneeId
     * @param userid
     * @param userName
     * @return
     */
    BpmActTaskAssignee modifyTaskAssignee(Long taskAssigneeId, Long userid, String userName);

    /**
     * 根据分配id获取分配信息
     *
     * @param taskAssigneeId
     * @return
     */
    BpmActTaskAssignee findActTaskAssigneesById(Long taskAssigneeId);

    /**
     * 获取任务管理页面层级数据-实体类型
     *
     * @return
     */
    List<HierarchyBaseDTO> getEntitiyCategoriesInActivity(
        Long orgId,
        Long projectId,
        Long processStageId,
        Long processId,
        Long entityTypeId
    );

    /**
     * 获取任务管理页面层级数据-工序阶段
     *
     * @return
     */
    List<HierarchyBaseDTO> getProcessStagesInActivity(Long orgId, Long projectId);

    /**
     * 获取任务管理页面层级数据-工序
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param criteriaDTO
     * @return
     */
    List<HierarchyBaseDTO> getProcessesInActivity(Long orgId, Long projectId,HierarchyCriteriaDTO criteriaDTO);

    /**
     * 保存任务材料文档
     *
     * @param bedm
     * @return
     */
    BpmEntityDocsMaterials saveDocsMaterials(BpmEntityDocsMaterials bedm);

    /**
     * 根据id获取实体类型
     *
     * @param id
     * @return
     */
    BpmEntitySubType findEntitySubTypeById(Long id);

    /**
     * 根据id获取工序阶段
     *
     * @param id
     * @return
     */
    BpmProcessStage findProcessStageById(Long id);

    /**
     * 根据id获取工序
     *
     * @param id
     * @return
     */
    BpmProcess findProcessById(Long id);

    /**
     * 保存ruTask
     *
     * @param bpmRuTask
     * @return
     */
    BpmRuTask saveBpmRuTask(BpmRuTask bpmRuTask);

    /**
     * 修改运行时任务担当人
     *
     * @param actInstId
     * @param taskDefKey
     * @param taskName
     * @param userid
     * @return
     */
    boolean assignee(Long actInstId, String taskDefKey, String taskName, Long userid, String assignee);

    /**
     * 获取任务管理页面层级数据-实体类型分类
     *
     * @return
     */
    List<HierarchyBaseDTO> getEntitiyCategoryTypesInActivity(
        Long orgId, Long projectId, Long processStageId, Long processId);

    /**
     * 根据id获取实体类型分类
     *
     * @param id
     * @return
     */
    BpmEntityType findEntityTypeById(Long id);

    /**
     * 根据id获取实体类型
     *
     * @param entitySubTypeId
     * @return
     */
    BpmEntitySubType getEntitiySubTypeById(Long entitySubTypeId);

    /**
     * 根据节点id获取实体
     *
     * @param entityId
     * @return
     */
    List<ProjectNode> getParentEntitiesByEntityId(Long projectId, Long entityId);

    /**
     * 根据processName获取模型变量
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param processId
     * @return
     */
    List<BpmActInstVariableConfig> getActInstVariablesByProcessId(Long orgId, Long projectId, Long processId);

    /**
     * 保存历史任务节点
     *
     * @param hiTask
     * @return
     */
    BpmHiTaskinst saveBpmHiTaskinst(BpmHiTaskinst hiTask);

    /**
     * 根据actInstId查询历史任务节点
     *
     * @param actInstId
     * @return
     */
    List<BpmHiTaskinst> findHiTaskinstByActInstId(Long actInstId);

    /**
     * 获取当前流程待办任务
     *
     * @param actInstId
     * @return
     */
    List<BpmRuTask> findBpmRuTaskByActInstId(Long actInstId);

    /**
     * 删除运行的任务
     *
     * @param taskId
     * @return
     */
    boolean deleteBpmRuTask(Long taskId);

    /**
     * 根据流程定义key查询流程变量
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param processKey
     * @param processKey
     * @return
     */
    List<BpmActInstVariableConfig> findActInstVariables(Long orgId, Long projectId, Long processKey);

    /**
     * 根据流程实例id及变量名查询变量值
     *
     * @param actInstId
     * @param name
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @return
     */
    BpmActInstVariableValue findBpmActInstVariableValue(Long orgId, Long projectId, Long actInstId, String name);

    /**
     * 删除流程
     *
     * @param id
     * @return
     */
    boolean deleteActInst(Long id, OperatorDTO operatorDTO);

    /**
     * 获取任务管理页面层级数据-工序分类
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    List<HierarchyBaseDTO> getProcessCategoryInActivity(Long orgId, Long projectId);

    /**
     * 获取工序分类
     *
     * @param id
     * @return
     */
    BpmProcessCategory findProcessCategoryById(Long id);

    /**
     * 获取会签信息
     *
     * @param taskId
     * @return
     */
    List<BpmDrawingSignTask> findBpmDrawingSignTask(Long taskId);

    List<BpmEntityDocsMaterials> getDocsMaterialsByProcessIdAndEntityIdAndActInstanceId(Long processId, Long entityId, Long actInstanceId);

    List<BpmProcess> getProcessByNameEN(Long orgId, Long projectId, String nameEN);

    BpmEntitySubType getEntitiySubTypeByNameEN(Long orgId, Long projectId, String productEvent);

    List<BpmActivityInstanceBase> findActInst(Long orgId, Long projectId, Long entityId);

    /**
     * 批量查询任务权限分配信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param searchDTO
     * @return
     */
    BatchTasksCategorySearchResultDTO batchTasksCategorySearch(Long orgId, Long projectId,
                                                               BatchTasksCategorySearchDTO searchDTO);

    /**
     * 批量分配任务权限
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param assigneeDTO
     */
    boolean batchTasksCategoryAssignee(Long orgId, Long projectId,
                                       BatchTasksCategoryAssigneeDTO assigneeDTO);

    void createSuspensionTaskNode(BpmActivityInstanceBase actInst, ActInstSuspendDTO dto, SuspensionState suspend, OperatorDTO operatorDTO);

    List<BpmActTask> findBpmActTaskByActInstIdOrderByCreatedAtAsc(Long actInstId);

    BpmHiTaskinst findHiTaskinstByTaskId(Long taskId);

    /**
     * 查询流程工作组工作场地
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param searchDTO
     * @return
     */
    ActInstTeamWorkSiteDTO getTeamWorkSite(Long orgId, Long projectId, BatchTasksCategorySearchDTO searchDTO);

    /**
     * 指定流程工作组工作场地
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param assigneeDTO
     */
    void setTeamWorkSite(Long orgId, Long projectId, ActInstTeamWorkSiteAssigneeDTO assigneeDTO);

    List<HierarchyBaseDTO> getTaskDefKeys(Long orgId, Long projectId, Long processStageId, Long processId);

    DiagramResourceDTO getDiagramResource(Long projectId, Long processId, int bpmnVersion);

    DiagramResourceDTO getDiagramResource(Long projectId, Long actInstId, Long processId, int bpmnVersion);

    List<TaskGatewayDTO> getTaskGateway(Long projectId, Long processId,  int bpmnVersion, String taskDefKey);

    List<ActTaskNodeDTO> getModelNodes(Long projectId, Long processId, int version);

    BpmHiTaskinst findHiTaskinstByTaskIdAndSeq(Long taskId, int seq);

    List<BpmActivityInstanceDTO>  searchFunction(Long orgId, Long projectId);

    List<BpmActivityInstanceDTO>  searchType(Long orgId, Long projectId);

    List<BpmActivityInstanceShiftLog> getShiftLogs(Long orgId, Long projectId, Long id);

    List<BpmActTaskAssignee> getTaskSupport(Long orgId, Long projectId, Long actInstId);

    void addTaskSupport(Long orgId, Long projectId, Long actInstId, TaskSupportDTO dto);
}
