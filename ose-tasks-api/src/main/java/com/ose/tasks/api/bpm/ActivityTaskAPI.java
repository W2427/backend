package com.ose.tasks.api.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.TaskProcessDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.BpmActTaskAssigneeHistory;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceShiftLog;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.ActivityInstanceDTO;
import com.ose.tasks.entity.bpm.BpmActInstVariableConfig;

import java.io.IOException;

/**
 * 任务管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface ActivityTaskAPI {

    /**
     * 创建任务
     *
     * @param taskDTO 任务信息
     * @return 实体信息
     */
    @RequestMapping(method = POST, value = "activities")
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<BpmActivityInstanceBase> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ActivityInstanceDTO taskDTO
    );

    @RequestMapping(method = DELETE, value = "activities/{id}")
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );


    /**
     * 任务列表
     *
     * @param criteria 任务列表查询数据类
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "activities")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActivityInstanceDTO> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        ActInstCriteriaDTO criteria
    );


    /**
     * function列表
     *
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "functions")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActivityInstanceDTO> searchFunction(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );


    /**
     * type列表
     *
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "types")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActivityInstanceDTO> searchType(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    /**
     * 编辑任务
     *
     * @param instDTO 任务信息
     * @return 实体信息
     */
    @RequestMapping(
        method = POST,
        value = "activities/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmActivityInstanceBase> modify(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody ActivityInstanceDTO instDTO
    );

    /**
     * 任务详情
     *
     * @return 任务delete信息
     */
    @RequestMapping(
        method = GET,
        value = "activities/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ActInstDetailDTO> detail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 任务移交详情
     *
     * @return 任务delete信息
     */
    @RequestMapping(
        method = GET,
        value = "shift/{id}/logs"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActivityInstanceShiftLog> shiftLogs(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 导出每日汇总报表
     * @return 实体信息
     */
    @RequestMapping(
        method = GET,
        value = "activities/summary-export"
    )
    void exportDailySummary(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId
    ) throws IOException;

    /**
     * 分配任务页面
     *
     * @param id 任务id
     * @return 实体信息
     */
    @RequestMapping(
        method = GET,
        value = "activities/{id}/tasks"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ActInstAssigneeDTO> activityTasks(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );

    /**
     * 设置任务节点担当人
     *
     * @param id 任务id
     * @return 实体信息
     */
    @RequestMapping(
        method = POST,
        value = "activities/{id}/tasks/{taskNodeId}/users/{userid}"
    )
    @ResponseStatus(OK)
    JsonResponseBody activityTaskAssignee(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @PathVariable("taskNodeId") Long taskNodeId,
        @PathVariable("userid") Long userid
    );

    /**
     * 生成移交任务记录
     *
     * @param id 任务id
     * @return 实体信息
     */
    @RequestMapping(
        method = POST,
        value = "activities/{id}/tasks/{taskNodeId}/users/{userid}/shift"
    )
    @ResponseStatus(OK)
    JsonResponseBody generateActivityShiftLog(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @PathVariable("taskNodeId") Long taskNodeId,
        @PathVariable("userid") Long userid,
        @RequestBody ShiftLogDTO log
    );

    /**
     * 返回任务管理页面层级数据
     */
    @RequestMapping(
        method = GET,
        value = "activities/hierarchy"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<TaskHierarchyDTO> getActivetyHierarchy(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        HierarchyCriteriaDTO criteriaDTO
    );

    /**
     * 挂起任务流
     */
    @RequestMapping(
        method = POST,
        value = "activities/{id}/suspend"
    )
    @ResponseStatus(OK)
    JsonResponseBody suspend(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody ActInstSuspendDTO dto
    );

    /**
     * 挂起任务流
     */
    @RequestMapping(
        method = POST,
        value = "activities/{id}/active"
    )
    @ResponseStatus(OK)
    JsonResponseBody active(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody ActInstSuspendDTO dto
    );

    /**
     * 上传任务文档
     */
    @RequestMapping(
        method = POST,
        value = "activities/{id}/docs"
    )
    @ResponseStatus(OK)
    JsonResponseBody docs(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long idactivityTaskAssignee,
        @RequestBody ActInstAttachmentDTO attachDTO
    );

    /**
     * 获取工序对应的模型变量
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    @RequestMapping(method = GET, value = "activities/variables/{processId}")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActInstVariableConfig> getActInstVariables(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("processId") Long processId
    );

    @RequestMapping(method = POST, value = "activities/{actInstId}/revocation")
    @ResponseStatus(OK)
    JsonResponseBody revocation(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("actInstId") Long actInstId
    );

    @RequestMapping(method = POST, value = "activities/{actInstId}/revocation/{taskDefKey}")
    @ResponseStatus(OK)
    JsonResponseBody revocationNode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("actInstId") Long actInstId,
        @PathVariable("taskDefKey") String taskDefKey,
        @RequestBody ActInstSuspendDTO dto
    );

    /**
     * 批量分配任务
     *
     * @return 实体信息
     */
    @RequestMapping(
        method = POST,
        value = "activities/batch-tasks-category-assignee-list"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BatchTasksCategorySearchResultDTO> batchTasksCategorySearchList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody BatchTasksCategorySearchDTO searchDTO
    );

    /**
     * 批量分配任务
     *
     * @return 实体信息
     */
    @RequestMapping(
        method = GET,
        value = "activities/batch-tasks-category-assignee"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<BatchTasksCategorySearchResultDTO> batchTasksCategorySearch(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        BatchTasksCategorySearchDTO searchDTO
    );

    /**
     * 批量分配任务
     *
     * @return 实体信息
     */
    @RequestMapping(
        method = POST,
        value = "activities/batch-tasks-category-assignee"
    )
    @ResponseStatus(OK)
    JsonResponseBody batchTasksCategoryAssignee(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody BatchTasksCategoryAssigneeDTO assigneeDTO
    );

    /**
     * 查询流程工作组工作场地信息
     */
    @RequestMapping(
        method = GET,
        value = "activities/team-worksite"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ActInstTeamWorkSiteDTO> getTeamWorkSite(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        BatchTasksCategorySearchDTO searchDTO
    );
    /**
     * 查询流程工作组工作场地信息post方法
     */
    @RequestMapping(
        method = POST,
        value = "activities/team-worksite-list"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ActInstTeamWorkSiteDTO> getTeamWorkSiteList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody BatchTasksCategorySearchDTO searchDTO
    );

    /**
     * 指定流程工作组工作场地信息
     */
    @RequestMapping(
        method = POST,
        value = "activities/team-worksite"
    )
    @ResponseStatus(OK)
    JsonResponseBody setTeamWorkSite(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ActInstTeamWorkSiteAssigneeDTO assigneeDTO
    );

    /**
     * 按条件任务管理实体列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "activity-task/download"
    )
    void downloadActivtityTask(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        ActInstCriteriaDTO criteriaDTO
    ) throws IOException;

    /**
     * 任务分配记录
     */
    @RequestMapping(method = GET, value = "activity-task/history")
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActTaskAssigneeHistory> searchHistory(@PathVariable("orgId") Long orgId,
                                                                  @PathVariable("projectId") Long projectId,
                                                                  TaskAssigneHistoryDTO taskAssigneHistoryDTO,
                                                                  PageDTO pageDTO);

    /**
     * 任务列表
     *
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "activities/processes")
    @ResponseStatus(OK)
    JsonListResponseBody<TaskProcessDTO> getProcesses(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );
}
