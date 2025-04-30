package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.ExportFileDTO;
import com.ose.tasks.dto.material.*;
import com.ose.tasks.entity.material.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/fmir")
public interface FMaterialIssueReceiptAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialIssueReceiptsEntity> postFMIR(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody FMaterialIssueReceiptPostFromPrepareDTO fMaterialIssueReceiptPostFromPrepareDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialIssueReceiptsEntity> getFMIRs(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        FMaterialIssueReceiptSearchListDTO fMaterialIssueReceiptSearchListDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialIssueReceiptsEntity> getFMIR(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialIssueReceiptsEntity> patchFMIR(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @RequestBody FMaterialIssueReceiptPatchDTO fMaterialIssueReceiptPatchDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody checkQtyEqual(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId
    );

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialIssueReceiptsEntity> patchFMIRForSPM(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @RequestBody FMaterialIssueReceiptPatchForSPMDTO fMaterialIssueReceiptPatchForSPMDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialIssueReceiptsEntity> patchFMIRForConfirm(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @RequestBody FMaterialIssueReceiptPatchForConfirmDTO fMaterialIssueReceiptPatchForConfirmDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialIssueReceiptItemsEntity> getFMIRItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialIssueReceiptTotalEntity> getFMIRTotals(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialIssueReceiptStatisticsDTO> getFMIRStatistics(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId);


    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialIssueReceiptTotalEntity> getFMIRTotal(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @PathVariable @Parameter(description = "fmirTotalId") Long fmirTotalId);

    @RequestMapping(
        method = GET,
        value = "material-issue-receipts/{fmirId}/totals/{fmirTotalId}/fitems",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<QrCodeFItemVo> getFItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @PathVariable @Parameter(description = "fmirTotalId") Long fmirTotalId,
        PageDTO pageDTO);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody postFMIRIssue(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @RequestBody FMaterialIssueReceiptFItemPostDTO fMaterialIssueReceiptFItemPostDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteFMIRIssue(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @PathVariable @Parameter(description = "fmirTotalId") Long fmirTotalId,
        @PathVariable @Parameter(description = "fitemDetailId") Long fitemDetailId);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody postFMIRIssueForSPM(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @RequestBody FMaterialIssueReceiptFItemPostDTO fMaterialIssueReceiptFItemPostDTO);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody postFMIRIssueForSPMStructure(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @RequestBody FMaterialIssueReceiptFItemPostDTO fMaterialIssueReceiptFItemPostDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteFMIRIssueForSPM(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @PathVariable @Parameter(description = "fmirTotalId") Long fmirTotalId,
        @PathVariable @Parameter(description = "fitemDetailId") Long fitemDetailId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialTransfersEntity> getFMIRTransfers(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialTransferItemEntity> getFMIRTotalTransfers(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @PathVariable @Parameter(description = "fmirTotalId") Long fmirTotalId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ExportFileDTO> exportFMIRs(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<ExportFileDTO> exportFMIRTotals(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId);


    @RequestMapping(
        method = POST,
        value = "material-issue-receipts/structure-nest",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialIssueReceiptsEntity> createStructureNestReceipts(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody FMaterialIssueReceiptPostFromPrepareDTO fMaterialIssueReceiptPostFromPrepareDTO);

    @RequestMapping(
        method = POST,
        value = "material-issue-receipts/{fmirId}/structure-nest/ex-warehouse",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody structureExWarehouse(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId);

    @Operation(
        summary = "更新出库清单-结构套料材料出库-SPM反写使用",
        description = "更新出库清单-结构套料材料出库-SPM反写使用。"
    )
    @RequestMapping(
        method = PATCH,
        value = "material-issue-receipts-spm-str/{fmirId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<FMaterialIssueReceiptsEntity> patchSTRFMIRForSPM(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @RequestBody FMaterialIssueReceiptPatchForSPMDTO fMaterialIssueReceiptPatchForSPMDTO);

    @RequestMapping(
        method = POST,
        value = "material-issue-receipts/{fmirId}/issue-surplus",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
     JsonResponseBody issueSurplus(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmirId") Long fmirId,
        @RequestBody FMaterialIssueReceiptIssueSurplusDTO fMaterialIssueReceiptIssueSurplusDTO);
}
