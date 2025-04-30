package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.tasks.dto.material.SpmReleaseNoteItemResultDTO;
import com.ose.tasks.entity.material.ReleaseNoteEntity;
import com.ose.tasks.entity.material.ReleaseNoteItemEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * SPM 计划放行单 接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface SpmPlanReleaseNoteAPI {

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<SpmReleaseNoteItemResultDTO> getSpmPlanRelnNoteItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "spmPlanRelnNumer") String spmPlanRelnNumer,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteEntity> getSpmPlanRelnNoteSubReleaseNote(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "spmPlanRelnNumer") String spmPlanRelnNumer,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteItemEntity> getSpmPlanRelnNoteSubReleaseNoteItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "spmPlanRelnNumer") String spmPlanRelnNumer,
        @PathVariable @Parameter(description = "spmPlanRelnItemId") Long spmPlanRelnItemId,
        PageDTO pageDTO);
}
