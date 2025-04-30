package com.ose.tasks.api.workinghour;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.workinghour.ProjectWorkingHourCategoryPostAndPatchDTO;
import com.ose.tasks.entity.workinghour.ProjectWorkingHourCategoryEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 工时接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface ProjectWorkingHourLargeCategoryAPI {


    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody postWorkingHourLargeCategory(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ProjectWorkingHourCategoryEntity> getWorkingHourLargeCategories(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ProjectWorkingHourCategoryEntity> getWorkingHourLargeCategory(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourLargeCategoryId") Long workingHourLargeCategoryId);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchWorkingHourLargeCategory(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourLargeCategoryId") Long workingHourLargeCategoryId,
        @RequestBody ProjectWorkingHourCategoryPostAndPatchDTO projectWorkingHourCategoryPostAndPatchDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteWorkingHourLargeCategory(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "workingHourLargeCategoryId") Long workingHourLargeCategoryId);
}
