package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.QRCodeAPI;
import com.ose.tasks.dto.QRCodePostDTO;
import com.ose.tasks.dto.QrCodePrintDTO;
import com.ose.tasks.dto.material.EntityQrCodeCreateDTO;
import com.ose.tasks.dto.material.EntityQrCodeCriteriaDTO;
import com.ose.tasks.dto.material.SpoolQrCodeCreateDTO;
import com.ose.tasks.entity.QRCode;
import com.ose.tasks.entity.bpm.BpmDeliveryEntity;
import com.ose.tasks.entity.material.EntityQrCodeEntity;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "二维码代码接口")
@RestController
public class QRCodeController extends BaseController implements QRCodeAPI {


    @Override
    @Operation(description = "取得二维码目标数据详细信息")
    @RequestMapping(
        method = GET,
        value = "/qrcode/{code}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonObjectResponseBody<QRCode> get(
        @PathVariable @Parameter(description = "二维码代码") String code
    ) {
        return new JsonObjectResponseBody<>();//qrCodeService.get(code));
    }

    @Override
    @Operation(description = "取得实体二维码列表信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/entity-qr-codes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<EntityQrCodeEntity> getEntityQrCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        EntityQrCodeCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>();//qrCodeService.getEntityQrCode(orgId, projectId, criteriaDTO));
    }

    @Override
    @Operation(description = "创建二维码")
    @RequestMapping(
        method = POST,
        value = "/qrcode",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonObjectResponseBody<QRCode> add(
        @RequestBody QRCodePostDTO qrCodePostDTO
    ) {
        return new JsonObjectResponseBody<>();//
//            qrCodeService.add(getContext().getOperator(), qrCodePostDTO)
//        );
    }

    @Override
    @Operation(description = "修改二维码打印flg")
    @RequestMapping(
        method = POST,
        value = "/qrcode/print-flg",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody setPrintFlgTrue(
        @RequestBody QrCodePrintDTO printDTO) {
//        qrCodeService.setPrintFlgTrue(printDTO);
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "下料单二维码出库用")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/entity-qr-codes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonObjectResponseBody<EntityQrCodeEntity> addEntityQrCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody EntityQrCodeCreateDTO entityQrCodeCreateDTO
    ) {
        return new JsonObjectResponseBody<>(
//            qrCodeService.addEntityQrCode(orgId,projectId,entityQrCodeCreateDTO,getContext().getOperator())
        );
    }

    @Override
    @Operation(description = "取得单管实体二维码列表信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-qr-codes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<BpmDeliveryEntity> getSpoolQrCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        EntityQrCodeCriteriaDTO criteriaDTO
    ) {
        return new JsonListResponseBody<>();//qrCodeService.getSpoolQrCode(orgId, projectId, criteriaDTO));
    }

    @Override
    @Operation(description = "创建单管实体二维码信息")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/spool-qr-codes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @SetUserInfo
    @WithPrivilege
    public JsonObjectResponseBody<BpmDeliveryEntity> addSpoolQrCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody SpoolQrCodeCreateDTO spoolQrCodeCreateDTO
    ) {
        return new JsonObjectResponseBody<>(
//            qrCodeService.addSpoolQrCode(orgId,projectId,spoolQrCodeCreateDTO,getContext().getOperator())
        );
    }

    @Override
    @Operation(description = "修改单管二维码打印flg")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/spool-qr-codes/print-flg",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updatePrintFlagInEntityIds(
        @RequestBody EntityQrCodeCriteriaDTO printDTO) {
//        qrCodeService.updatePrintFlagInEntityIds(printDTO);
        return new JsonResponseBody();
    }

}
