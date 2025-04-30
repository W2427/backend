package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.dto.material.*;
import com.ose.tasks.entity.material.*;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 工序管理接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FMaterialSurplusReceiptsAPI {

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialSurplusReceiptEntity> create(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody FMaterialSurplusReceiptsCreateDTO dto);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusReceiptEntity> search(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestParam FMaterialSurplusReceiptsSearchDTO criteriaDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialSurplusReceiptEntity> detail(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "msrId") Long msrId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusReceiptNodeEntity> getNodeList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "fMaterialSurplusReceiptId") Long fMaterialSurplusReceiptId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusReceiptNodeEntity> getChooseNodeList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        PageDTO pageDTO);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusReceiptNodeEntity> getNodeListByGatewayAndEntityIds(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "gateway") String gateway,
        FMaterialPrepareNodeDto fMaterialPrepareNodeDto);

    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody addNode(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "fMaterialSurplusReceiptId") Long fMaterialSurplusReceiptId,
        @RequestBody FMaterialSurplusReceiptsNodesPostDTO dto);

    @RequestMapping(
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody deleteNode(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "fMaterialSurplusReceiptId") Long fMaterialSurplusReceiptId,
        @PathVariable @Parameter(description = "fMaterialSurplusReceiptNodeId") Long fMaterialSurplusReceiptNodeId);

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<FMaterialSurplusReceiptNodeItemEntity> getNodeItemList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "fMaterialSurplusReceiptId") Long fMaterialSurplusReceiptId,
        @PathVariable @Parameter(description = "fMaterialSurplusReceiptNodeId") Long fMaterialSurplusReceiptNodeId,
        PageDTO pageDTO);

    @RequestMapping(
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody updateFMaterialSurplusReceipt(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "fMaterialSurplusReceiptId") Long fMaterialSurplusReceiptId,
        @RequestBody FMaterialSurplusReceiptsCreateDTO dto);


}
