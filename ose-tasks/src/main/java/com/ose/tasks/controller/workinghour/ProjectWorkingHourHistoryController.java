package com.ose.tasks.controller.workinghour;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.api.workinghour.ProjectWorkingHourHistoryAPI;
import com.ose.tasks.domain.model.service.workinghour.ProjectWorkingHourHistoryInterface;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourHistoryEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "工时履历接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class ProjectWorkingHourHistoryController extends BaseController implements ProjectWorkingHourHistoryAPI {

    private final ProjectWorkingHourHistoryInterface projectWorkingHourHistoryInterface;

    /**
     * 构造方法
     */
    @Autowired
    public ProjectWorkingHourHistoryController(ProjectWorkingHourHistoryInterface projectWorkingHourHistoryInterface) {
        this.projectWorkingHourHistoryInterface = projectWorkingHourHistoryInterface;
    }

    @Override
    @Operation(
        summary = "获取工时履历的列表",
        description = "获取工时履历的列表。"
    )
    @RequestMapping(
        method = GET,
        value = "working-hour-histories/{workingHourId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<ProjectWorkingHourHistoryEntity> getWorkingHourHistories(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourId") Long workingHourId,
        PageDTO pageDTO) {

        return new JsonListResponseBody<ProjectWorkingHourHistoryEntity>(
            getContext(),
            projectWorkingHourHistoryInterface.getWorkingHourHistories(orgId, projectId, workingHourId, pageDTO)
        );
    }
}
