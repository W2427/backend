package com.ose.tasks.controller.workinghour;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.workinghour.ProjectWorkingHourLargeCategoryAPI;
import com.ose.tasks.domain.model.service.workinghour.ProjectWorkingHourCategoryInterface;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourCategoryPostAndPatchDTO;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourCategoryEntity;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "工时类型(小分类)接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class ProjectWorkingHourLargeCategoryController extends BaseController implements ProjectWorkingHourLargeCategoryAPI {

    private final ProjectWorkingHourCategoryInterface projectWorkingHourCategoryInterface;

    /**
     * 构造方法
     */
    @Autowired
    public ProjectWorkingHourLargeCategoryController(ProjectWorkingHourCategoryInterface projectWorkingHourCategoryInterface) {
        this.projectWorkingHourCategoryInterface = projectWorkingHourCategoryInterface;
    }

    @Override
    @Operation(
        summary = "创建工时大分类",
        description = "创建工时大分类。"
    )
    @RequestMapping(
        method = POST,
        value = "working-hour-categories/large-categories",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonResponseBody postWorkingHourLargeCategory(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();
        projectWorkingHourCategoryInterface.postWorkingHourLargeCategory(
            orgId,
            projectId,
            operatorDTO,
            projectWorkingHourCategoryPostAndPatchDTO);

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取工时大分类列表",
        description = "获取工时大分类列表。"
    )
    @RequestMapping(
        method = GET,
        value = "working-hour-categories/large-categories",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<ProjectWorkingHourCategoryEntity> getWorkingHourLargeCategories(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PageDTO pageDTO) {

        return new JsonListResponseBody<ProjectWorkingHourCategoryEntity>(
            getContext(),
            projectWorkingHourCategoryInterface.getWorkingHourLargeCategories(orgId, projectId, pageDTO)
        );
    }

    @Override
    @Operation(
        summary = "获取工时大分类详情",
        description = "获取工时大分类详情。"
    )
    @RequestMapping(
        method = GET,
        value = "working-hour-categories/large-categories/{workingHourLargeCategoryId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ProjectWorkingHourCategoryEntity> getWorkingHourLargeCategory(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourLargeCategoryId") Long workingHourLargeCategoryId) {

        return new JsonObjectResponseBody<ProjectWorkingHourCategoryEntity>(
            getContext(),
            projectWorkingHourCategoryInterface.getWorkingHourLargeCategory(orgId, projectId, workingHourLargeCategoryId)
        );
    }

    @Override
    @Operation(
        summary = "更新工时大分类",
        description = "更新工时大分类。"
    )
    @RequestMapping(
        method = PATCH,
        value = "working-hour-categories/large-categories/{workingHourLargeCategoryId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonResponseBody patchWorkingHourLargeCategory(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourLargeCategoryId") Long workingHourLargeCategoryId,
        @RequestBody ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO) {

        OperatorDTO operatorDTO = getContext().getOperator();
        projectWorkingHourCategoryInterface.patchWorkingHourLargeCategory(
            orgId,
            projectId,
            workingHourLargeCategoryId,
            operatorDTO,
            projectWorkingHourCategoryPostAndPatchDTO);

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除工时大分类",
        description = "删除工时大分类。"
    )
    @RequestMapping(
        method = DELETE,
        value = "working-hour-categories/large-categories/{workingHourLargeCategoryId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonResponseBody deleteWorkingHourLargeCategory(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourLargeCategoryId") Long workingHourLargeCategoryId) {

        projectWorkingHourCategoryInterface.deleteWorkingHourLargeCategory(orgId, projectId, workingHourLargeCategoryId);

        return new JsonResponseBody();
    }
}
