package com.ose.tasks.api.workinghour;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPatchDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPatchStatusDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourPostDTO;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourSearchDTO;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 工时接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface ProjectWorkingHourAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ProjectWorkingHourEntity> postWorkingHour(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody ProjectWorkingHourPostDTO projectWorkingHourPostDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ProjectWorkingHourEntity> getWorkingHours(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ProjectWorkingHourSearchDTO projectWorkingHourSearchDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ProjectWorkingHourEntity> getWorkingHour(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchWorkingHour(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchDTO projectWorkingHourPatchDTO);

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
    JsonResponseBody patchWorkingHourToSubmit(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO);

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
    JsonResponseBody patchWorkingHourToWithdraw(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteWorkingHour(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ProjectWorkingHourEntity> getWorkingHoursForApproval(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PageDTO pageDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchApprovalWorkingHourToApproved(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchApprovalWorkingHourToRejected(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        @RequestBody ProjectWorkingHourPatchStatusDTO projectWorkingHourPatchStatusDTO);
}
