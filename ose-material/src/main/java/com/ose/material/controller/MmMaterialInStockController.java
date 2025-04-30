package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmMaterialInStockAPI;
import com.ose.material.domain.model.service.MmMaterialInStockInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmMaterialInStockDetailEntity;
import com.ose.material.entity.MmMaterialInStockDetailQrCodeEntity;
import com.ose.material.entity.MmMaterialInStockEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "查找在库材料")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-stock")
public class MmMaterialInStockController extends BaseController implements MmMaterialInStockAPI {

    /**
     * 炉批号接口服务
     */
    private final MmMaterialInStockInterface materialStockService;


    /**
     * 构造方法
     */
    @Autowired
    public MmMaterialInStockController(MmMaterialInStockInterface materialStockService) {
        this.materialStockService = materialStockService;
    }

    /**
     * 查找在库材料列表
     *
     * @return 查找在库材料列表
     */
    @Override
    @Operation(
        summary = "查找在库材料列表",
        description = "查找在库材料列表。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmMaterialInStockEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialStockService.search(
                orgId,
                projectId,
                mmMaterialInStockSearchDTO
            )
        );
    }

    /**
     * 查找在库材料列表
     *
     * @return 查找在库材料列表
     */
    @Override
    @Operation(
        summary = "查找在库材料列表",
        description = "查找在库材料列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/{MaterialStockId}"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmMaterialInStockEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") Long MaterialStockId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO) {

        return new JsonObjectResponseBody<>(
            materialStockService.detail(
                orgId,
                projectId,
                MaterialStockId,
                mmMaterialInStockSearchDTO
            )
        );
    }

    /**
     * 查找在库材料明细
     *
     * @return 查找在库材料明细
     */
    @Override
    @Operation(
        summary = "查找在库材料明细",
        description = "查找在库材料明细。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialStockEntityId}/details"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmMaterialInStockDetailEntity> searchDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") Long materialStockEntityId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialStockService.searchDetail(
                orgId,
                projectId,
                materialStockEntityId,
                mmMaterialInStockSearchDTO
            )
        );
    }

    /**
     * 查找在库材料明细
     *
     * @return 查找在库材料明细
     */
    @Override
    @Operation(
        summary = "查找在库材料明细",
        description = "查找在库材料明细。"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialStockEntityId}/details/{materialStockDetailEntityId}/qrcode"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmMaterialInStockDetailQrCodeEntity> searchDetailQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") Long materialStockEntityId,
        @PathVariable @Parameter(description = "在库材料详情id") Long materialStockDetailEntityId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialStockService.searchDetailQrCode(
                orgId,
                projectId,
                materialStockEntityId,
                materialStockDetailEntityId,
                mmMaterialInStockSearchDTO
            )
        );
    }


    /**
     * 查找在库材料二维码信息
     *
     * @return 查找在库材料二维码信息
     */
    @Override
    @Operation(
        summary = "查找在库材料二维码信息",
        description = "查找在库材料二维码信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/qrcode/{qrCode}"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmReleaseReceiveQrCodeResultDTO> searchQrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") String qrCode) {

        return new JsonObjectResponseBody<>(
            materialStockService.searchQrCode(
                orgId,
                projectId,
                qrCode
            )
        );
    }

    /**
     * 查找在库材料详情列表
     *
     * @return 查找在库材料详情列表
     */
    @Override
    @Operation(
        summary = "查找在库材料详情列表",
        description = "查找在库材料详情列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/material-detail"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmMaterialInStockDetailEntity> searchMaterialDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialStockService.searchMaterialDetail(
                orgId,
                projectId,
                mmMaterialInStockSearchDTO
            )
        );
    }

    /**
     * 查找二维码信息
     *
     * @return 查找二维码信息
     */
    @Override
    @Operation(
        summary = "查找二维码信息",
        description = "查找二维码信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/material/{qrCode}"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmQrCodeResultDTO> qrCode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "在库材料id") String qrCode) {

        return new JsonObjectResponseBody<>(
            materialStockService.qrCode(
                orgId,
                projectId,
                qrCode
            )
        );
    }

    /**
     * 查找在库材料二维码信息
     *
     * @return 查找在库材料二维码信息
     */
    @Override
    @Operation(
        summary = "查找在库材料二维码信息",
        description = "查找在库材料二维码信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/qrcode"
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmReleaseReceiveQrCodeResultDTO> searchQrCodes(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO) {

        return new JsonListResponseBody<>(
            materialStockService.searchQrCodes(
                orgId,
                projectId,
                mmMaterialInStockSearchDTO
            )
        );
    }

    /**
     * 查找材料信息
     *
     * @return 查找材料信息
     */
    @Override
    @Operation(
        summary = "查找材料信息",
        description = "查找材料信息。"
    )
    @RequestMapping(
        method = POST,
        value = "/material-information"
    )
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmMaterialInStockDetailQrCodeEntity> searchMaterialInformation(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody MmMaterialInStockSearchDTO mmMaterialInStockSearchDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            materialStockService.searchMaterialInformation(
                orgId,
                projectId,
                mmMaterialInStockSearchDTO
            )
        );
    }
}

