package com.ose.tasks.controller.workinghour;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.workinghour.ProjectWorkingHourAPI;
import com.ose.tasks.domain.model.service.workinghour.ProjectWorkingHourInterface;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPatchDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPatchStatusDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPostDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourSearchDTO;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourEntity;
import com.ose.tasks.vo.workinghour.ProjectWorkingHourStatusType;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "工时接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class ProjectWorkingHourController extends BaseController implements ProjectWorkingHourAPI {

    private final ProjectWorkingHourInterface projectWorkingHourInterface;

    /**
     * 构造方法
     */
    @Autowired
    public ProjectWorkingHourController(ProjectWorkingHourInterface projectWorkingHourInterface) {
        this.projectWorkingHourInterface = projectWorkingHourInterface;
    }

    @Override
    @Operation(
        summary = "创建工时",
        description = "创建工时。"
    )
    @RequestMapping(
        method = POST,
        value = "working-hours",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ProjectWorkingHourEntity> postWorkingHour(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody ProjectWorkingHourPostDTO projectWorkingHourPostDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();

        return new JsonObjectResponseBody<>(
            getContext(),
            projectWorkingHourInterface.postWorkingHour(orgId, projectId, operatorDTO, projectWorkingHourPostDTO)
        ).setIncluded(projectWorkingHourInterface);
    }

    @Override
    @Operation(
        summary = "获取工时列表",
        description = "获取工时列表。"
    )
    @RequestMapping(
        method = GET,
        value = "working-hours",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<ProjectWorkingHourEntity> getWorkingHours(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ProjectWorkingHourSearchDTO projectWorkingHourSearchDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();

        return new JsonListResponseBody<>(
            getContext(),
            projectWorkingHourInterface.getWorkingHours(orgId, projectId, operatorDTO, projectWorkingHourSearchDTO)
        ).setIncluded(projectWorkingHourInterface);
    }

    @Override
    @Operation(
        summary = "获取工时详情",
        description = "获取工时详情。"
    )
    @RequestMapping(
        method = GET,
        value = "working-hours/{workingHourId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ProjectWorkingHourEntity> getWorkingHour(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId) {

        return new JsonObjectResponseBody<>(
            getContext(),
            projectWorkingHourInterface.getWorkingHour(orgId, projectId, workingHourId)
        ).setIncluded(projectWorkingHourInterface);
    }

    @Override
    @Operation(
        summary = "更新工时",
        description = "更新工时。"
    )
    @RequestMapping(
        method = PATCH,
        value = "working-hours/{workingHourId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonResponseBody patchWorkingHour(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchDTO projectWorkingHourPatchDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();
        projectWorkingHourInterface.patchWorkingHour(orgId, projectId, workingHourId, operatorDTO, projectWorkingHourPatchDTO);

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "提交工时审核",
        description = "提交工时审核。"
    )
    @RequestMapping(
        method = PATCH,
        value = "working-hours/{workingHourId}/submit",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonResponseBody patchWorkingHourToSubmit(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();
        projectWorkingHourInterface.patchWorkingHourStatus(
            orgId,
            projectId,
            workingHourId,
            ProjectWorkingHourStatusType.SUBMITTED,
            projectWorkingHourPatchStatusDTO,
            operatorDTO);

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "撤回工时审核",
        description = "撤回工时审核。"
    )
    @RequestMapping(
        method = PATCH,
        value = "working-hours/{workingHourId}/withdraw",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonResponseBody patchWorkingHourToWithdraw(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();
        projectWorkingHourInterface.patchWorkingHourStatus(
            orgId,
            projectId,
            workingHourId,
            ProjectWorkingHourStatusType.WITHDRAW,
            projectWorkingHourPatchStatusDTO,
            operatorDTO);

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除工时",
        description = "删除工时。"
    )
    @RequestMapping(
        method = DELETE,
        value = "working-hours/{workingHourId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonResponseBody deleteWorkingHour(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId) {

        projectWorkingHourInterface.deleteWorkingHour(orgId, projectId, workingHourId);

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取审核者需要审核的工时列表",
        description = "获取审核者需要审核的工时列表。"
    )
    @RequestMapping(
        method = GET,
        value = "approval-working-hours",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<ProjectWorkingHourEntity> getWorkingHoursForApproval(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PageDTO pageDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();
        return new JsonListResponseBody<>(
            getContext(),
            projectWorkingHourInterface.getWorkingHoursForApproval(orgId, projectId, operatorDTO, pageDTO)
        ).setIncluded(projectWorkingHourInterface);
    }

    @Override
    @Operation(
        summary = "审核者审核-批准",
        description = "审核者审核-批准。"
    )
    @RequestMapping(
        method = PATCH,
        value = "approval-working-hours/{workingHourId}/approved",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonResponseBody patchApprovalWorkingHourToApproved(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();
        projectWorkingHourInterface.patchWorkingHourStatus(
            orgId,
            projectId,
            workingHourId,
            ProjectWorkingHourStatusType.APPROVED,
            projectWorkingHourPatchStatusDTO,
            operatorDTO);

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "审核者审核-驳回",
        description = "审核者审核-驳回。"
    )
    @RequestMapping(
        method = PATCH,
        value = "approval-working-hours/{workingHourId}/rejected",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonResponseBody patchApprovalWorkingHourToRejected(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();
        projectWorkingHourInterface.patchWorkingHourStatus(
            orgId,
            projectId,
            workingHourId,
            ProjectWorkingHourStatusType.REJECTED,
            projectWorkingHourPatchStatusDTO,
            operatorDTO);

        return new JsonResponseBody();
    }
}
