package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.FMaterialTransferItemPatchDTO;
import com.ose.tasks.dto.material.FMaterialTransfersPatchDTO;
import com.ose.tasks.dto.material.FMaterialTransfersPatchForRiceiveConfirmActivityDTO;
import com.ose.tasks.dto.material.FMaterialTransfersPostDTO;
import com.ose.tasks.entity.material.FMaterialTransferItemEntity;
import com.ose.tasks.entity.material.FMaterialTransfersEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 开箱检验接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FMaterialTransfersAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialTransfersEntity> postFMT(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody FMaterialTransfersPostDTO fMaterialTransfersPostDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialTransfersEntity> getFMTs(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialTransfersEntity> getFMT(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmtId") Long fmtId);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchFMT(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmtId") Long fmtId,
        @RequestBody FMaterialTransfersPatchDTO fMaterialTransfersPatchDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchFMTItemForActualTransfer(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmtId") Long fmtId,
        @RequestBody FMaterialTransferItemPatchDTO fMaterialTransferItemPatchDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchFMTItemForPlanTransfer(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmtId") Long fmtId,
        @RequestBody FMaterialTransferItemPatchDTO fMaterialTransferItemPatchDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialTransferItemEntity> getFMTItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmtId") Long fmtId,
        PageDTO pageDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchFMTItemForActualReceive(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmtId") Long fmtId,
        @RequestBody FMaterialTransferItemPatchDTO fMaterialTransferItemPatchDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody patchFMTForReceiveConfirmInActivity(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmtId") Long fmtId,
        @RequestBody FMaterialTransfersPatchForRiceiveConfirmActivityDTO fMaterialTransfersPatchForRiceiveConfirmActivityDTO);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteFMT(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmtId") Long fmtId);
}
