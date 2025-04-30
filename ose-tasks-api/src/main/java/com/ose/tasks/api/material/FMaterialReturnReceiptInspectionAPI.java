package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.FMaterialReturnInspectionProblemListPostDTO;
import com.ose.tasks.dto.material.FMaterialReturnInspectionProblemPostDTO;
import com.ose.tasks.entity.material.FMaterialReturnReceiptItemEntity;
import com.ose.tasks.entity.material.FMaterialReturnReceiptRelationIssueTotalEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 退库检验清单接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FMaterialReturnReceiptInspectionAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody postFMReturnInspectionProblem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @RequestBody FMaterialReturnInspectionProblemPostDTO fMaterialReturnInspectionProblemPostDTO);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody postFMReturnInspectionProblemQrCodeList(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @RequestBody FMaterialReturnInspectionProblemListPostDTO fMaterialReturnInspectionProblemListPostDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialReturnReceiptItemEntity> getFMReturnInspectionProblems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        PageDTO pageDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteFMReturnInspectionProblem(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @PathVariable @Parameter(description = "fmReturnItemId") Long fmReturnItemId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialReturnReceiptRelationIssueTotalEntity> getFMReturnIssueInspectionProblemsTotals(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @PathVariable @Parameter(description = "fmReturnRelationIssueId") Long fmReturnRelationIssueId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialReturnReceiptItemEntity> getFMReturnIssueTotalInspectionProblemsItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmReturnId") Long fmReturnId,
        @PathVariable @Parameter(description = "fmReturnRelationIssueId") Long fmReturnRelationIssueId,
        @PathVariable @Parameter(description = "fmReturnRelationIssueTotalId") Long fmReturnRelationIssueTotalId,
        PageDTO pageDTO);
}
