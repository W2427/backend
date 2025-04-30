package com.ose.materialspm.api;

import com.ose.dto.PageDTO;
import com.ose.materialspm.dto.ReleaseNoteListDTO;
import com.ose.materialspm.entity.ReleaseNote;
import com.ose.materialspm.entity.ReleaseNoteHead;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * SPM 放行单 接口
 */
@FeignClient(name = "ose-materialspm", contextId = "mmReleaseNoteFeign")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/spm-projects/{spmProjectId}")
public interface SpmReleaseNoteFeignAPI {

    @RequestMapping(
        method = GET,
        value = "spm-release-notes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNoteHead> getReleaseNoteHeadList(
        @PathVariable(value = "orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable(value = "projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable(value = "spmProjectId") @Parameter(description = "SPM项目id") String spmProjectId,
        ReleaseNoteListDTO releaseNoteListDTO);

    @RequestMapping(
        method = GET,
        value = "spm-release-notes/{relnNumber}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ReleaseNoteHead> getReleaseNote(
        @PathVariable(value = "orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable(value = "projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable(value = "spmProjectId") @Parameter(description = "SPM项目id") String spmProjectId,
        @PathVariable(value = "relnNumber") @Parameter(description = "relnNumber") String relnNumber);

    @RequestMapping(
        method = POST,
        value = "spm-release-notes/{relnNumber}/items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNote> getReleaseNoteItemsByPage(
        @PathVariable(value = "orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable(value = "projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable(value = "spmProjectId") @Parameter(description = "SPM项目id") String spmProjectId,
        @PathVariable(value = "relnNumber") @Parameter(description = "relnNumber") String relnNumber,
        @RequestBody PageDTO pageDTO);

    @RequestMapping(
        method = POST,
        value = "spm-release-notes/{relnNumber}/no-page-items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ReleaseNote> getReleaseNoteItems(
        @PathVariable(value = "orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable(value = "projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable(value = "spmProjectId") @Parameter(description = "SPM项目id") String spmProjectId,
        @PathVariable(value = "relnNumber") @Parameter(description = "relnNumber") String relnNumber);
}
