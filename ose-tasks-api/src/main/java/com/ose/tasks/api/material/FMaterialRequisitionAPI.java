package com.ose.tasks.api.material;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.dto.material.FMaterialRequisitionPostDTO;
import com.ose.tasks.entity.material.FMaterialRequisitionEntity;
import com.ose.tasks.entity.material.FMaterialRequisitionItemEntity;
import com.ose.tasks.entity.material.FMaterialTransferItemEntity;
import com.ose.tasks.entity.material.FMaterialTransfersEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 领料单接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FMaterialRequisitionAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialRequisitionEntity> postFMReq(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody FMaterialRequisitionPostDTO fMaterialRequisitionPostDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialRequisitionEntity> getFMReq(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmrId") Long fmrId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialRequisitionItemEntity> getFMReqItems(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmreqId") Long fmreqId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialTransfersEntity> getFMReqTransfers(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmreqId") Long fmreqId,
        PageDTO pageDTO);

    @Operation(
        summary = "获取领料单明细下所有的配送单明细",
        description = "获取领料单明细下所有的配送单明细。"
    )
    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    JsonListResponseBody<FMaterialTransferItemEntity> getFMReqItemTransfers(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "fmreqId") Long fmreqId,
        @PathVariable @Parameter(description = "fmreqItemId") Long fmreqItemId,
        PageDTO pageDTO);
}
