package com.ose.tasks.api;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.ose.tasks.dto.material.EntityQrCodeCreateDTO;
import com.ose.tasks.dto.material.SpoolQrCodeCreateDTO;
import com.ose.tasks.entity.bpm.BpmDeliveryEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.QRCodePostDTO;
import com.ose.tasks.dto.QrCodePrintDTO;
import com.ose.tasks.dto.material.EntityQrCodeCriteriaDTO;
import com.ose.tasks.entity.QRCode;
import com.ose.tasks.entity.material.EntityQrCodeEntity;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 二维码代码接口。
 */
public interface QRCodeAPI {

    @RequestMapping(
        method = GET,
        value = "/qrcode/{code}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<QRCode> get(@PathVariable("code") String code);

    @RequestMapping(
        method = POST,
        value = "/qrcode",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<QRCode> add(@RequestBody QRCodePostDTO qrCodePostDTO);

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/entity-qr-codes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<EntityQrCodeEntity> getEntityQrCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        EntityQrCodeCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/entity-qr-codes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<EntityQrCodeEntity> addEntityQrCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody EntityQrCodeCreateDTO entityQrCodeCreateDTO
    );


    @RequestMapping(
        method = POST,
        value = "/qrcode/print-flg",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody setPrintFlgTrue(@RequestBody QrCodePrintDTO printDTO);

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/spool-qr-codes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BpmDeliveryEntity> getSpoolQrCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        EntityQrCodeCriteriaDTO criteriaDTO
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/spool-qr-codes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<BpmDeliveryEntity> addSpoolQrCode(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody SpoolQrCodeCreateDTO spoolQrCodeCreateDTO);

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/spool-qr-codes/print-flg",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updatePrintFlagInEntityIds(@RequestBody EntityQrCodeCriteriaDTO printDTO);
}
