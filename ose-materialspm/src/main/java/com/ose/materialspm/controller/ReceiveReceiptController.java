package com.ose.materialspm.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.materialspm.api.ReceiveReceiptAPI;
import com.ose.materialspm.domain.model.service.ReceiveReceiptInterface;
import com.ose.materialspm.dto.FMaterialReceiveReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptListResultsDTO;
import com.ose.materialspm.dto.ReceiveReceiptResultDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "SPM 入库单 查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class ReceiveReceiptController extends BaseController implements ReceiveReceiptAPI {

    private final ReceiveReceiptInterface receiveReceiptInterface;

    /**
     * 构造方法
     *
     * @param receiveReceiptInterface 收货查询服务
     */
    @Autowired
    public ReceiveReceiptController(ReceiveReceiptInterface receiveReceiptInterface) {
        this.receiveReceiptInterface = receiveReceiptInterface;
    }

    @Override
    @Operation(
        summary = "获取入库单列表",
        description = "获取入库单列表。"
    )
    @RequestMapping(
        method = GET,
        value = "receive-receipts",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ReceiveReceiptListResultsDTO> getReceiveReceipt(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        ReceiveReceiptDTO receiveReceiptDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            receiveReceiptInterface.getReceiveReceipt(receiveReceiptDTO)
        );
    }

    @Override
    @Operation(
        summary = "保存入库信息",
        description = "保存入库信息"
    )
    @RequestMapping(
        method = POST,
        value = "/spm-projects/{projId}/save-mrr",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ReceiveReceiptResultDTO> saveMrr(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialReceiveReceiptDTO receiveReceiptDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            receiveReceiptInterface.saveMrr(projId, receiveReceiptDTO)
        );
    }

    @Override
    @Operation(
        summary = "执行入库处理",
        description = "执行入库处理"
    )
    @RequestMapping(
        method = POST,
        value = "/spm-projects/{projId}/run-mrr",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ReceiveReceiptResultDTO> runMrr(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialReceiveReceiptDTO receiveReceiptDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            receiveReceiptInterface.runProcedure(projId, receiveReceiptDTO)
        );
    }
}
