package com.ose.material.api;


import com.ose.material.dto.MmQrCodeResultDTO;
import com.ose.material.dto.MmReleaseReceiveQrCodeResultDTO;
import com.ose.material.dto.MmMaterialInStockSearchDTO;
import com.ose.material.entity.MmMaterialInStockDetailEntity;
import com.ose.material.entity.MmMaterialInStockDetailQrCodeEntity;
import com.ose.material.entity.MmMaterialInStockEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient(name = "ose-material", contextId = "mmMaterialInStockFeign")
public interface MmMaterialInStockFeignAPI {

    /**
     * 查找在库材料列表
     *
     * @return 查找在库材料列表
     */
    @Operation(
        summary = "查找在库材料列表",
        description = "查找在库材料列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-stock"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmMaterialInStockEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO);

    /**
     * 查找在库材料详情
     *
     * @return 查找在库材料详情
     */
    @Operation(
        summary = "查找在库材料详情",
        description = "查找在库材料详情。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-stock/{MaterialStockId}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmMaterialInStockEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") Long MaterialStockId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO);

    /**
     * 查找在库材料明细
     *
     * @return 查找在库材料明细
     */
    @Operation(
        summary = "查找在库材料明细",
        description = "查找在库材料明细。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-stock/{materialStockEntityId}/details"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmMaterialInStockDetailEntity> searchDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") Long materialStockEntityId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO);

    /**
     * 查找在库材料明细二维码
     *
     * @return 查找在库材料明细二维码
     */
    @Operation(
        summary = "查找在库材料明细二维码",
        description = "查找在库材料明细二维码。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-stock/{materialStockEntityId}/details/{materialStockDetailEntityId}/qrcode"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmMaterialInStockDetailQrCodeEntity> searchDetailQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") Long materialStockEntityId,
        @PathVariable @Parameter(description = "在库材料详情id") Long materialStockDetailEntityId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO);

    /**
     * 查找在库材料二维码信息
     *
     * @return 查找在库材料二维码信息
     */
    @Operation(
        summary = "通过件号或二维码查找在库材料信息",
        description = "通过件号或二维码查找在库材料信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-stock/qrcode/{qrCode}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmReleaseReceiveQrCodeResultDTO> searchQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") String qrCode);

    /**
     * 查找在库材料详情列表
     *
     * @return 查找在库材料详情列表
     */
    @Operation(
        summary = "查找在库材料详情列表",
        description = "查找在库材料详情列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-stock/material-detail"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmMaterialInStockDetailEntity> searchMaterialDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO);

    /**
     * 查找二维码信息
     *
     * @return 查找二维码信息
     */
    @Operation(
        summary = "查找二维码信息",
        description = "查找二维码信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-stock/material/{qrCode}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmQrCodeResultDTO> qrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") String qrCode);

    /**
     * 查找在库材料二维码信息
     *
     * @return 查找在库材料二维码信息
     */
    @Operation(
        summary = "查找在库材料二维码信息",
        description = "查找在库材料二维码信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/material-stock/qrcode"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmReleaseReceiveQrCodeResultDTO> searchQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO
    );

    /**
     * 查找材料信息
     *
     * @return 查找材料信息
     */
    @Operation(
        summary = "查找材料信息",
        description = "查找材料信息。"
    )
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/material-stock/material-information"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmMaterialInStockDetailQrCodeEntity> searchMaterialInformation(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO);
}
