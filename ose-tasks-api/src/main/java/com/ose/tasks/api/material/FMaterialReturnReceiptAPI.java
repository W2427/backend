package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.FMaterialReturnPatchDTO;
import com.ose.tasks.dto.material.FMaterialReturnPostDTO;
import com.ose.tasks.dto.material.FMaterialReturnReceiptFItemPostDTO;
import com.ose.tasks.entity.material.FMaterialReturnReceiptEntity;
import com.ose.tasks.entity.material.FMaterialReturnReceiptItemEntity;
import com.ose.tasks.entity.material.FMaterialReturnReceiptRelationIssueEntity;
import com.ose.tasks.entity.material.FMaterialReturnReceiptRelationIssueTotalEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 退库清单接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FMaterialReturnReceiptAPI {


    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody postFMReturns(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody FMaterialReturnPostDTO fMaterialReturnPostDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialReturnReceiptEntity> getFMReturns(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialReturnReceiptEntity> getFMRetrun(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchFMReturn(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @RequestBody FMaterialReturnPatchDTO fMaterialReturnPatchDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchFMReturnForConfirmToReceive(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @RequestBody FMaterialReturnPatchDTO fMaterialReturnPatchDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialReturnReceiptRelationIssueEntity> getFMReturnIssues(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialReturnReceiptRelationIssueTotalEntity> getFMReturnIssueTotals(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @PathVariable @Parameter(description = "fmReturnRelationIssueId") Long fmReturnRelationIssueId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialReturnReceiptItemEntity> getFMReturnIssueTotalItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @PathVariable @Parameter(description = "fmReturnRelationIssueId") Long fmReturnRelationIssueId,
        @PathVariable @Parameter(description = "fmReturnRelationIssueTotalId") Long fmReturnRelationIssueTotalId,
        PageDTO pageDTO);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody postFMReturnItemReturn(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @RequestBody FMaterialReturnReceiptFItemPostDTO fMaterialReturnReceiptFItemPostDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteFMReturnItemReturn(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @PathVariable @Parameter(description = "fmReturnItemId") Long fmReturnItemId);
}
