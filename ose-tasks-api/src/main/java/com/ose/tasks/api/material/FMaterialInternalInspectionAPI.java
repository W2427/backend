package com.ose.tasks.api.material;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.ose.tasks.dto.material.FMaterialInspectionProblemListPostDTO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.FMaterialInspectionProblemPostDTO;
import com.ose.tasks.dto.material.FMaterialInspectionProblemSavePostDTO;
import com.ose.tasks.entity.material.FMaterialStocktakeItemEntity;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 开箱检验接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FMaterialInternalInspectionAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<FMaterialStocktakeItemEntity> postFIIP(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @RequestBody FMaterialInspectionProblemPostDTO fMaterialInternalInspectionProblemPostDTO);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody postFIIPs(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @RequestBody FMaterialInspectionProblemListPostDTO fMaterialInspectionProblemListPostDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<FMaterialStocktakeItemEntity> getFIIPs(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        PageDTO pageDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody deleteFIIP(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @PathVariable @Parameter(description = "fmstItemId") Long fmstItemId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonResponseBody saveFIIP(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        @RequestBody FMaterialInspectionProblemSavePostDTO fMaterialInspectionProblemSavePostDTO);
}
