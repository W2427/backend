package com.ose.tasks.api.material;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.ReleaseNoteQualityCertificatePatchDTO;
import com.ose.tasks.dto.material.ReleaseNoteQualityCertificatePostDTO;
import com.ose.tasks.entity.material.ReleaseNoteQualityCertificateEntity;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 材料质量证明书接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface ReleaseNoteQualityCertificateAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody postRelnCert(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @RequestBody ReleaseNoteQualityCertificatePostDTO releaseNoteQualityCertificatePostDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<ReleaseNoteQualityCertificateEntity> getRelnCerts(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<ReleaseNoteQualityCertificateEntity> getRelnCert(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "certId") Long certId);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody patchRelnCert(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "certId") Long certId,
        @RequestBody ReleaseNoteQualityCertificatePatchDTO releaseNoteQualityCertificatePatchDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody deleteRelnCert(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "certId") Long certId);
}
