package com.ose.tasks.api.bpm;

import com.ose.issues.entity.Issue;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.BpmActivityInstanceDTO;
import com.ose.tasks.dto.bpm.InspectionIssueCriteriaDTO;
import com.ose.tasks.dto.bpm.InternalInspectionIssueDTO;
import com.ose.tasks.dto.bpm.IssueCreateTaskDTO;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 任务管理接口。
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface InspectionIssueAPI {

    /**
     * 内检意见列表。
     */
    @RequestMapping(method = GET, value = "internal-inspection-issues")
    @ResponseStatus(OK)
    JsonListResponseBody<Issue> internalInspectionIssueList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        InspectionIssueCriteriaDTO criteriaDTO);

    /**
     * 创建新的内检意见。
     */
    @RequestMapping(method = POST, value = "internal-inspection-issues")
    @ResponseStatus(CREATED)
    JsonResponseBody addInternalInspectionIssue(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody InternalInspectionIssueDTO dto);

    /**
     * 创建遗留问题流程实例
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param dto
     * @return
     */
    @RequestMapping(method = POST, value = "punchlist-task")
    @ResponseStatus(CREATED)
    JsonResponseBody createPunchlistTask(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody IssueCreateTaskDTO dto);

    /**
     * 查看遗留问题流程任务
     */
    @RequestMapping(method = GET, value = "punchlist-task/{id}")
    @ResponseStatus(OK)
    JsonObjectResponseBody<BpmActivityInstanceDTO> getIssueActivityTask(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id);

}
