package com.ose.tasks.api.bpm;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.ExportFileDTO;
import com.ose.tasks.dto.ExportFileListDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.BpmActTaskAssignee;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 任务管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface TodoTaskAPI {

    /**
     * 待办任务列表
     */
    @RequestMapping(method = GET, value = "tasks")
    @ResponseStatus(OK)
    JsonListResponseBody<TodoTaskDTO> searchTodo(@PathVariable("orgId") Long orgId,
                                                 @PathVariable("projectId") Long projectId, TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO);

    /**
     * 导出待办任务列表
     */
    @RequestMapping(method = GET, value = "tasks/export")
    @ResponseStatus(OK)
    JsonObjectResponseBody<ExportFileDTO> exportTask(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TodoTaskCriteriaDTO taskCriteria
    );


    /**
     * 处理任务
     */
    @RequestMapping(method = POST, value = "tasks/handle")
    @ResponseStatus(OK)
    JsonResponseBody execute(@PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId, @RequestBody TodoTaskExecuteDTO toDoTaskDTO);

    /**
     * 任务处理信息
     */
    @RequestMapping(method = POST, value = "handle-tasks")
    @ResponseStatus(OK)
    JsonObjectResponseBody<TodoTaskDTO> getConfirm(@PathVariable("orgId") Long orgId,
                                                   @PathVariable("projectId") Long projectId,
                                                   @Parameter(description = "任务id") String[] actTaskId);

    /**
     * 已完成任务列表
     *
     * @param taskCriteria 任务列表查询数据类
     */
    @RequestMapping(method = GET, value = "tasks/completed")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActivityInstanceDTO> searchCompleted(@PathVariable("orgId") Long orgId,
                                                              @PathVariable("projectId") Long projectId, TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO);

    /**
     * 返回待办任务页面层级数据
     */
    @RequestMapping(method = GET, value = "tasks/hierarchy")
    @ResponseStatus(OK)
    JsonObjectResponseBody<TaskHierarchyDTO> getTaskHierarchy(@PathVariable("orgId") Long orgId,
                                                              @PathVariable("projectId") Long projectId, HierarchyCriteriaDTO criteriaDTO);

    /**
     * 返回待办任务页面层级数据
     */
    @RequestMapping(method = GET, value = "tasks/completed/hierarchy")
    @ResponseStatus(OK)
    JsonObjectResponseBody<TaskHierarchyDTO> getTaskCompletedHierarchy(@PathVariable("orgId") Long orgId,
                                                                       @PathVariable("projectId") Long projectId, HierarchyCriteriaDTO criteriaDTO);

    /*    *//**
     * 批量处理任务
     *//*
    @RequestMapping(method = POST)
    @ResponseStatus(OK)
    JsonListResponseBody<BatchHandleResponseDTO> execute(@PathVariable("orgId") Long orgId,
            @PathVariable("projectId") Long projectId, @RequestBody BatchHandleTasksDTO toDoTaskDTO);
*/

    /**
     * 委托任务
     */
    @RequestMapping(method = POST, value = "tasks/{actTaskId}/delegate/{userid}")
    @ResponseStatus(OK)
    JsonResponseBody delegate(@PathVariable("orgId") Long orgId, @PathVariable("projectId") Long projectId,
                              @PathVariable @Parameter(description = "任务id") Long taskId, @PathVariable @Parameter(description = "被委托用户id") Long userid);

    /**
     * 取得材料二维码
     */
    @RequestMapping(method = GET, value = "material-info/{qrCode}")
    @ResponseStatus(OK)
    JsonObjectResponseBody<MaterialInfoDTO> materialInfo(@PathVariable("orgId") Long orgId,
                                                         @PathVariable("projectId") Long projectId, @PathVariable("qrCode") String qrCode);

    @RequestMapping(method = GET, value = "tasks/processes/{processId}/entity-sub-types/{entitySubTypeId}")
    @Operation(summary = "查询未安排的实体任务", description = "查询未安排的实体任务")
    @ResponseStatus(OK)
    JsonListResponseBody<EntityNoBpmActivityInstanceDTO> getTodoTaskEntity(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "流程Id") Long processId,
        @PathVariable @Parameter(description = "实体类型id") Long entitySubTypeId,
        @Parameter(description = "keyWord") String keyWord,
        PageDTO pageDTO);

    @RequestMapping(method = GET, value = "tasks/mobile")
    @Operation(summary = "查询未小程序得待办任务", description = "查询未小程序得待办任务")
    @ResponseStatus(OK)
    JsonListResponseBody<TodoTaskMobileCriteriaDTO> getTodoTaskMobile(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @Parameter(description = "assignee") Long assignee);


    /**
     * 查询ndt报告生成画面层级数据
     */
    @RequestMapping(method = GET, value = "tasks/generante-ndt-report/hierarchy")
    @ResponseStatus(OK)
    JsonObjectResponseBody<TaskHierarchyDTO> getNDTReportTaskHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        HierarchyCriteriaDTO criteriaDTO
    );

    @RequestMapping(method = GET, value = "tasks/generante-ndt-report")
    @ResponseStatus(OK)
    JsonListResponseBody<TodoTaskDTO> searchNDTReportTask(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TodoTaskCriteriaDTO taskCriteria,
        PageDTO pageDTO
    );

    @RequestMapping(method = POST, value = "tasks/generante-ndt-report/pre-build")
    @ResponseStatus(OK)
    JsonObjectResponseBody<ExportFileDTO> preBuildReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody TodoTaskExecuteDTO toDoTaskDTO
    );

    /**
     * 待办任务列表-班长查询待分配任务
     */
    @RequestMapping(method = GET, value = "tasks/foreman-dispatch")
    @ResponseStatus(OK)
    JsonListResponseBody<TodoTaskForemanDispatchDTO> searchForemanDispatchTodo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TodoTaskCriteriaDTO taskCriteria,
        PageDTO pageDTO
    );

    /**
     * 待办任务列表-工人查询待认领任务
     */
    @RequestMapping(method = GET, value = "tasks/workers-claim")
    @ResponseStatus(OK)
    JsonListResponseBody<TodoTaskForemanDispatchDTO> searchWorkersClaimTodo(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TodoTaskCriteriaDTO taskCriteria,
        PageDTO pageDTO
    );

    /**
     * 工人认领任务
     */
    @RequestMapping(method = POST, value = "tasks/workers-claim")
    @ResponseStatus(OK)
    JsonResponseBody workerClaimTask(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody TodoTaskExecuteDTO toDoTaskDTO
    );

    /**
     * 批处理 任务详情
     */
    @RequestMapping(method = GET, value = "handle-batch-tasks")
    @Operation(summary = "查询批处理任务处理信息", description = "查询批处理任务处理信息")
    <P extends BaseBatchTaskCriteriaDTO> JsonObjectResponseBody<TodoBatchTaskDTO> getBatchTasksInfo(@PathVariable Long orgId,
                                                                                                    @PathVariable Long projectId,
                                                                                                    P todoBatchTaskCriteriaDTO);

    /**
     * 批 处理任务 ftjftj
     */
    @RequestMapping(method = POST, value = "tasks/batch-handle")
    @Operation(summary = "批处理任务", description = "批处理任务")
    <P extends BaseBatchTaskCriteriaDTO> JsonResponseBody batchExecute(@PathVariable Long orgId,
                                                                       @PathVariable Long projectId,
                                                                       @RequestBody P todoBatchTaskCriteriaDTO);
    /**
     * 导出待办任务报告信息
     */
    @RequestMapping(method = GET, value = "tasks/report-export")
    @ResponseStatus(OK)
    JsonObjectResponseBody<ExportFileListDTO> exportReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TodoTaskCriteriaDTO taskCriteria
    );

    /**
     * 导出NDT焊口和焊工信息
     */
    @RequestMapping(method = GET, value = "tasks/ndt-export")
    @ResponseStatus(OK)
    JsonObjectResponseBody<ExportFileDTO> exportNdt(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TodoTaskCriteriaDTO taskCriteria
    );

    @RequestMapping(method = GET, value = "tasks/ndt-export-quick")
    @ResponseStatus(OK)
    JsonObjectResponseBody<ExportFileDTO> exportNdtQuick(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TodoTaskCriteriaDTO taskCriteria
    );

    /**
     * 工作流任务节点网关
     *
     * @param orgId    组织ID
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "todo-task/{taskId}/gateway")
    @Operation(summary = "工作流任务节点网关", description = "根据条件信息，查询任务网关列表。")
    JsonListResponseBody<TaskGatewayDTO> getGateWays(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                            @PathVariable @Parameter(description = "项目Id") Long projectId,
                                                            @PathVariable @Parameter(description = "任务Id") Long  taskId);

    /**
     * 待办任务工序分类
     *
     * @param orgId    组织ID
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "todo-task/wx-processes")
    @Operation(summary = "工作流任务节点网关", description = "查询待办任务工序列表。")
    JsonListResponseBody getProcesses(@PathVariable @Parameter(description = "orgId") Long orgId,
                                      @PathVariable @Parameter(description = "项目Id") Long projectId);

    /**
     * 移交任务
     */
    @RequestMapping(method = POST, value = "/task/{taskId}/assignee/{assignee}")
    @ResponseStatus(OK)
    JsonResponseBody shiftTask(@PathVariable("orgId") Long orgId,
                               @PathVariable("projectId") Long projectId,
                               @PathVariable("taskId") Long taskId,
                               @PathVariable("assignee") Long assignee);

    /**
     * 查询线下support信息
     *
     * @param orgId    组织ID
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "/task/{actInstId}/support")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActTaskAssignee> getTaskSupport(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                            @PathVariable @Parameter(description = "项目Id") Long projectId,
                                                            @PathVariable @Parameter(description = "项目Id") Long actInstId);

    /**
     * 移交任务
     */
    @RequestMapping(method = POST, value = "/task/{actInstId}/support")
    @ResponseStatus(OK)
    JsonResponseBody addTaskSupporter(@PathVariable("orgId") Long orgId,
                                      @PathVariable("projectId") Long projectId,
                                      @PathVariable("actInstId") Long taskId,
                                      @RequestBody TaskSupportDTO dto);

}
