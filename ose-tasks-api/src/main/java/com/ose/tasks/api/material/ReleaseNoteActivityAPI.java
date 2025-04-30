package com.ose.tasks.api.material;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.entity.material.ReleaseNoteEntity;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 放行单的工作流管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface ReleaseNoteActivityAPI {

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteEntity> getActivityRelns(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PageDTO pageDTO);

}
