package com.ose.tasks.controller.bpm;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.issues.entity.Issue;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.InspectionIssueAPI;
import com.ose.tasks.domain.model.service.bpm.InspectionIssueInterface;
import com.ose.tasks.dto.bpm.BpmActivityInstanceDTO;
import com.ose.tasks.dto.bpm.InspectionIssueCriteriaDTO;
import com.ose.tasks.dto.bpm.InternalInspectionIssueDTO;
import com.ose.tasks.dto.bpm.IssueCreateTaskDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "内检意见接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class InspectionIssueController extends BaseController implements InspectionIssueAPI {

    private InspectionIssueInterface inspectionIssueService;

    /**
     * 构造方法。
     */
    @Autowired
    public InspectionIssueController(InspectionIssueInterface inspectionIssueService) {
        this.inspectionIssueService = inspectionIssueService;
    }

    @RequestMapping(method = GET, value = "internal-inspection-issues")
    @Operation(summary = "查询内检意见", description = "查询内检意见")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<Issue> internalInspectionIssueList(
        @PathVariable @Parameter(description = "组织id") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        InspectionIssueCriteriaDTO criteriaDTO) {
        return new JsonListResponseBody<>(inspectionIssueService.internalInspectionIssueList(orgId, projectId, criteriaDTO));
    }

    @RequestMapping(method = POST, value = "internal-inspection-issues")
    @Operation(summary = "添加内检意见", description = "添加内检意见")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody addInternalInspectionIssue(
        @PathVariable @Parameter(description = "组织id") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "内检意见DTO") InternalInspectionIssueDTO dto) {
        inspectionIssueService.addInternalInspectionIssue(orgId, projectId, dto, getContext().getOperator());
        return new JsonResponseBody();
    }

    @RequestMapping(method = POST, value = "punchlist-task")
    @Operation(summary = "创建遗留问题流程实例", description = "创建遗留问题流程实例")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody createPunchlistTask(
        @PathVariable @Parameter(description = "组织id") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "遗留问题DTO") IssueCreateTaskDTO dto) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        inspectionIssueService.createPunchlistTask(context,
            orgId, projectId, operatorDTO, dto);
        return new JsonResponseBody();
    }

    @RequestMapping(method = GET, value = "punchlist-task/{id}")
    @Operation(summary = "查询遗留问题流程实例", description = "查询遗留问题流程实例")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<BpmActivityInstanceDTO> getIssueActivityTask(
        @PathVariable @Parameter(description = "组织id") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "issueid") Long id) {
        BpmActivityInstanceDTO bpmActivityInstance = inspectionIssueService.searchPunchlistTask(orgId, projectId, id);

        return new JsonObjectResponseBody<>(bpmActivityInstance);
    }

}
