package com.ose.tasks.domain.model.service.bpm;

import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.WBSEntryTeamWorkSiteDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.bpm.TaskPackageDTO;
import com.ose.tasks.entity.*;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.wbs.entry.WBSEntry;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.vo.bpm.ActivityExecuteResult;
import com.ose.tasks.vo.qc.ReportStatus;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

/**
 * 代办任务的基础业务服务接口， TodoTask Base
 */
public interface TodoTaskBaseInterface {

    /**
     * 根据条件查询流程实例id
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param taskCriteria
     * @return
     */
    List<Long> findActInstIdsInActivityInstance(Long orgId, Long projectId, TodoTaskCriteriaDTO taskCriteria);


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
     * 根据流程实例id 查询流程任务信息
     *
     * @param actInstId
     * @return
     */
    BpmActivityInstanceBase findActInstByProjectIdAndActInstId(Long projectId, Long actInstId);

    /**
     * 根据id获取待办任务信息
     */
    BpmRuTask findFirstBpmRuTaskByActTaskId(Long taskId);

    /**
     * 根据任务节点信息获取任务分配记录
     *
     * @param taskDefKey
     * @param taskName
     * @param actInstId
     * @return
     */
    BpmActTaskAssignee getTaskAssigneesByTaskInfo(String taskDefKey, String taskName, Long actInstId);

    /**
     * 保存流程任务信息
     *
     * @param actInst
     * @return
     */
    BpmActivityInstanceBase saveBpmActivityInstance(BpmActivityInstanceBase actInst);


    /**
     * 保存历史人物节点
     *
     * @param hiTask
     * @return
     */
    BpmHiTaskinst saveBpmHiTaskinst(BpmHiTaskinst hiTask);

    /**
     * 查询已完成任务
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param taskCriteria
     * @param pageDTO      分页DTO
     * @param assignee
     * @return
     */
    Page<BpmActivityInstanceDTO> searchCompletedTask(Long orgId, Long projectId, TodoTaskCriteriaDTO taskCriteria,
                                                  PageDTO pageDTO, String assignee);

    /**
     * 获取待办任务层级数据-实体类型
     *
     * @return
     */
    List<HierarchyBaseDTO> getEntitiyCategoriesInRuTask(List<String> entityModuleNames, String taskNode, String taskDefKey, Long processStageId, Long processId, Long orgId, Long projectId, String assignee);

    /**
     * 获取待办任务层级数据-工序阶段
     *
     * @param entitySubTypeId
     * @param orgId            组织ID
     * @return
     */
    List<HierarchyBaseDTO> getProcessStagesInRuTask(Long orgId, Long projectId, Long assignee,
                                                    HierarchyCriteriaDTO criteriaDTO);

    /**
     * 获取待办任务层级数据-工序
     *
     * @param entitySubTypeId
     * @param processStageId
     * @param orgId            组织ID
     * @return
     */
    List<HierarchyBaseDTO> getProcessesInRuTask(Long processCategoryId, Long entitySubTypeId, Long processStageId, Long orgId,
                                                HierarchyCriteriaDTO criteriaDTO);

    /**
     * 获取待办任务层级数据-任务节点
     *
     * @param entitySubTypeId
     * @param processStageId
     * @param processId
     * @param orgId            组织ID
     * @param batchflag
     * @return
     */
    List<TaskNodeDTO> getTaskNodesInRuTask(Long processCategoryId, Long entitySubTypeId, Long processStageId, Long processId, Long orgId, Boolean batchflag);

    /**
     * 保存正在运行任务信息
     *
     * @param bpmRuTask
     * @return
     */
    BpmRuTask saveBpmRuTask(BpmRuTask bpmRuTask);

    /**
     * 获取已完成任务页面层级数据-实体类型
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param assignee
     * @return
     */
    List<Long> getEntitiyCategoriesInHiTask(Long processCategoryId, Long orgId, Long projectId, Long assignee);

    /**
     * 获取已完成任务页面层级数据-工序阶段
     *
     * @param entitySubTypeId
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param assignee
     * @return
     */
    List<Long> getProcessStagesInHiTask(Long processCategoryId, Long entitySubTypeId, Long orgId, Long projectId, Long assignee);

    /**
     * 获取已完成任务页面层级数据-工序
     *
     * @param entitySubTypeId
     * @param processStageId
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param assignee
     * @return
     */
    List<Long> getProcessesInHiTask(Long processCategoryId, Long entitySubTypeId, Long processStageId, Long orgId, Long projectId,
                                    Long assignee);

    /**
     * 获取已完成任务页面层级数据-任务节点
     *
     * @param entitySubTypeId
     * @param processStageId
     * @param processId
     * @param orgId            组织ID
     * @param projectId        项目ID
     * @param assignee
     * @return
     */
    List<String> getTaskNodesInHiTask(Long processCategoryId, Long entitySubTypeId, Long processStageId, Long processId, Long orgId,
                                      Long projectId, Long assignee);

    /**
     * 修改运行时任务担当人
     *
     * @param actInstId
     * @param taskDefKey
     * @param taskName
     * @param assignee
     * @return
     */
    boolean assignee(Long actInstId, String taskDefKey, String taskName, Long assignee, BpmActivityInstanceBase actInst);

    /**
     * 从ruTask中删除已完成的任务
     *
     * @param taskId
     * @return
     */
    boolean deleteRuTask(Long taskId);

    /**
     * 从运行时任务记录删除当前执行人已完成任务
     *
     * @param taskId
     * @return
     */
    boolean deleteRuTaskByAssign(Long taskId,String assign);

    /**
     * 根据id获取待办任务
     *
     * @param taskId
     * @return
     */
    BpmRuTask findBpmRuTaskByActTaskId(Long taskId);

    /**
     * 根据流程定义key获取变量列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param processId
     * @return
     */
    List<BpmActInstVariableConfig> findActInstVariables(Long orgId, Long projectId, Long processId);

    /**
     * 根据定义信息获取变量任务配置
     *
     * @param variableName
     * @return
     */
    BpmActInstVariableTaskConfig findVariableConfigByTaskDefKeyAndVariableName(Long processId, String taskDefKey,
                                                                               String variableName);

    /**
     * 根据流程定义Key，变量名获取变量信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param processId
     * @return
     */
    BpmActInstVariableConfig getVariableByVariableName(Long orgId, Long projectId, String variableName, Long processId);

    /**
     * 保存流程变量的值
     *
     * @param variableValue
     * @return
     */
    BpmActInstVariableValue saveBpmActInstVariableValue(BpmActInstVariableValue variableValue);

    /**
     * 根据流程实例id及变量名查询变量值
     *
     * @param actInstId
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param variableName
     * @return
     */
    BpmActInstVariableValue findBpmActInstVariableValue(Long orgId, Long projectId, Long actInstId, String variableName);

    /**
     * 获取待办任务列表
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param assignee
     * @param pageDTO     分页DTO
     * @return
     */
    Page<TodoTaskDTO> getRuTaskList(Long orgId, Long projectId, Long assignee, TodoTaskCriteriaDTO taskCriteria,
                                    PageDTO pageDTO);

    /**
     * 获取待办任务列表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param assignee
     * @return
     */
    List<TaskPackageDTO> getTaskPackages(Long orgId, Long projectId, Long assignee);

    /**
     * 获取代办任务
     *
     * @param bpmActTaskId
     * @return
     */
    BpmRuTask findBpmRuTaskById(Long bpmActTaskId);

    /**
     * 获取材料信息
     *
     * @param actInstId
     * @return
     */
    TaskMaterialDTO getMaterial(Long actInstId);

    /**
     * 根据二维码获取材料信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param qrCode
     * @return
     */
    MaterialInfoDTO materialInfo(Long orgId, Long projectId, String qrCode);

    /**
     * 添加流程实例材料信息
     *
     * @param ruTask
     * @param materialInfos
     * @return
     */
    BpmActInstMaterial creatBpmActInstMaterial(BpmRuTask ruTask, List<MaterialInfoDTO> materialInfos, BpmActivityInstanceBase actInst);

    /**
     * 查询工序分类
     *
     * @param id
     * @return
     */
    BpmProcessCategory findProcessCategoryById(Long id);


    /**
     * 获取工序分类 -hi
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param assignee
     * @return
     */
    List<Long> getProcessCategoriesInHiTask(Long orgId, Long projectId, Long assignee);

    FileES uploadReportTodocs(Long orgId, Long projectId, String reportPath);

    List<EntityNoBpmActivityInstanceDTO> getTodoTaskEntity(Long orgId, Long projectId, Long processId,
                                                           Long entitySubTypeId, String keyWord, PageDTO pageDTO);

    /**
     * 查询ruTask
     *
     * @param actInstId
     * @return
     */
    List<BpmRuTask> findBpmRuTaskByActInstId(Long actInstId);


    List<TodoTaskMobileCriteriaDTO> getTodoTaskForMobile(Long orgId, Long projectId, Long assignee);


    /**
     * 查询任务节点privilege 权限信息
     */
    BpmActivityTaskNodePrivilege getBpmActivityTaskNodePrivilege(Long orgId, Long projectId, Long processId,
                                                                 String taskDefKey);

    /**
     * 获取待办任务中模块名
     *
     * @param taskNode
     * @param processStageId
     * @param processId
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param assignee
     * @return
     */
    List<String> getEntityModuleNameInRuTask(String taskNode, String taskDefKey, Long processStageId, Long processId, Long orgId,
                                             Long projectId, Long assignee);

    /**
     * 保存报告
     *
     * @param bpmDoc
     * @param actInstId
     * @param taskId
     */
    void saveReportFromAttachment(BpmEntityDocsMaterials bpmDoc, Long actInstId, Long taskId);


    /**
     * 根据实体id查询流程任务
     *
     * @param projectId 项目ID
     * @return
     */
    BpmActivityInstanceBase getBpmActivityInstanceByEntityId(Long orgId, Long projectId, Long entityId);


    /**
     * 根据权限查询任务分配信息
     *
     * @param actInstId
     * @param category
     * @return
     */
    List<BpmActTaskAssignee> getTaskAssigneesByTaskCategory(Long actInstId, String category);


    void saveReportFromDocuments(BpmEntityDocsMaterials bpmDoc, Long actInstId, Long taskId);



    /**
     * 修改任务指派人
     *
     * @param actInstId
     * @param taskCategory
     * @param userId
     * @param username
     * @param teamId
     */
    void modifyTaskAssigneeByCategory(Long actInstId, String taskCategory, Long userId, String username,
                                      Long teamId);


    /**
     * 保存任务文档
     *
     * @param bpmDoc
     */
    BpmEntityDocsMaterials saveBpmEntityDocsMaterials(BpmEntityDocsMaterials bpmDoc);


    /**
     * 查询任务层级筛选数据
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param assignee
     * @param taskDefKey
     * @return
     */
    List<HierarchyStageProcessDTO> getStageProcessesInRuTask(Long orgId, Long projectId, Long assignee,
                                                             String taskDefKey);

    /**
     * 匹配焊接工艺。
     *
     * @param positionA 位置1
     * @param positionB 位置2
     */
    Boolean positionMatch(String positionA, String positionB);

    /**
     * 匹配焊接位置。
     *
     * @param processA 焊接工艺1
     * @param processB 焊接工艺2
     */
    Boolean processMatch(String processA, String processB);

    /**
     * 返回运行任务的实体列表
     *
     * @param orgId     组织id
     * @param projectId 项目ID
     * @param qrcode    二维码
     * @return
     */
    List<Long> findEntityIdListForRuTask(Long orgId, Long projectId, String qrcode);

    /**
     * 保存流程上的变量
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param toDoTaskDTO 待办任务DTO
     * @param id          任务ID
     */
    void saveVariableValue(Long orgId, Long projectId, TodoTaskExecuteDTO toDoTaskDTO, Long id);

    /**
     * 取得父级实体ID
     *
     * @param projectId 项目ID
     * @param entityId
     * @return
     */
    List<HierarchyNode> __getParentEntitiesByEntityId(Long projectId, Long entityId);

    /**
     * 取得计划的施工场地信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param wbsEntryState
     * @param processId
     * @return
     */
    WBSEntryTeamWorkSiteDTO getWBSEntryTeamWorkSiteInfo(Long orgId, Long projectId, WBSEntry wbsEntry, WBSEntryState wbsEntryState, Long processId);

    /**
     * 保存流程实例
     */
    BpmActivityInstanceBase saveActInst(BpmActivityInstanceBase actInst);

    /**
     * 更改遗留问题 解决人清单
     *
     * @param actInst
     * @param nextAssignee
     */
    void modifyPunchlistMembers(BpmActivityInstanceBase actInst, Long nextAssignee);

    /**
     * 取得流程花费的时间
     *
     * @param id 流程ID
     * @return
     */
    double getProcessCostHour(Long id);

    /**
     * 船检计划执行异步任务
     *
     * @param projectId             项目ID
     * @param operatorDTO
     * @param entityId
     * @param processId
     * @param bpmActivityInstanceId
     * @param approved
     * @param hours
     * @return
     */
    BpmPlanExecutionHistory createBpmPlanExecutionHistory(Long projectId, OperatorDTO operatorDTO, Long entityId,
                                                          Long processId, Long bpmActivityInstanceId,
                                                          Boolean approved, double hours, Long version);

    /**
     * 获取版本图纸
     *
     * @param actInsta
     * @return
     */
    ActReportDTO getDrawing(BpmActivityInstanceBase actInsta, String taskDefKey);


    /**
     * 设置流程实例的版本,设置流程的版本 0，1，2
     *
     * @param actInst
     * @param actInstDTO
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @return
     */
    BpmActivityInstanceBase setActInstVersion(BpmActivityInstanceBase actInst, ActivityInstanceDTO actInstDTO, Long orgId, Long projectId);


    /**
     * 材料类流程信息
     *
     * @param createResult 流程创建 DTO
     * @return
     */
    CreateResultDTO getMaterialActInstInfo(CreateResultDTO createResult);

    /**
     * 设计类流程信息
     *
     * @param createResult 流程创建 DTO
     * @return
     */
    CreateResultDTO getDrawingActInstInfo(CreateResultDTO createResult);

    /**
     * 保存Checklist 信息到 工作流 文档表
     *
     * @param processId
     * @return
     */
    boolean saveCheckListToDocsMaterialTable(Long processId, Long entityId, Long projectId, BpmActivityInstanceBase actInst);

    /**
     * 指定bpmActTask的执行人
     *
     * @param bpmActTaskAssigneeMap
     * @param ruTask
     * @param currentExecutor
     * @param actInstId
     * @return
     */
    String setAssigneeForBpmRuTask(Map<String, BpmActTaskAssignee> bpmActTaskAssigneeMap,
                                   BpmRuTask ruTask,
                                   String currentExecutor,
                                   Long actInstId);

    /**
     * 更新焊口实体的NDT执行结果
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param entityId
     */
    void updateActivityExecuteResultForWeldEntity(Long orgId, Long projectId, Long entityId,
                                                  ActivityExecuteResult ndtResult, ActivityExecuteResult pmiResult);


    /**
     * 上传附件
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param temporaryFileNames 临时文件名
     * @return List 返回列表
     */
    List<ActReportDTO> uploadTemporaryFileNames(Long orgId, Long projectId, List<String> temporaryFileNames);


    /**
     * 为任务执行执行人
     *
     * @param ruTaskList
     * @return
     */
    String setTaskAssignee(List<BpmRuTask> ruTaskList);


    /**
     * 根据网关字符串判断 执行结果
     */
    String getInspectResult(String commandStr);

    /**
     * 根据网关字符串判断 报告状态
     */
    ReportStatus getReportStatus(String commandStr);

    boolean updateProcessStageCount(BpmRuTask ruTask, BpmActivityInstanceBase actInst);

    /**
     * 船检计划执行异步任务
     *
     * @param projectId             项目ID
     * @param operatorDTO           操作者DTO
     * @param entityId              实体ID
     * @param processId             工序ID
     * @param bpmActivityInstanceId 流程实例ID
     * @param approved              完成
     * @param hours                 消耗的时间
     * @return
     */
    BpmPlanExecutionHistory createBpmPlanExecutionHistory(Long projectId, OperatorDTO operatorDTO, Long entityId,
                                                                 Long processId, Long bpmActivityInstanceId,
                                                                 Boolean approved, double hours, Long version,
                                                                 Boolean isHalt, Boolean forceStart);

}

