package com.ose.materialspm.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.materialspm.api.ReturnReceiptAPI;
import com.ose.materialspm.domain.model.service.ReturnReceiptInterface;
import com.ose.materialspm.dto.FMaterialReturnReceiptDTO;
import com.ose.materialspm.dto.ReceiveReceiptResultDTO;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "SPM 退库单 查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class ReturnReceiptController extends BaseController implements ReturnReceiptAPI {

    private final ReturnReceiptInterface returnReceiptInterface;

    /**
     * 构造方法
     *
     * @param returnReceiptInterface 退库查询
     */
    @Autowired
    public ReturnReceiptController(
        ReturnReceiptInterface returnReceiptInterface
    ) {
        this.returnReceiptInterface = returnReceiptInterface;
    }

    @Override
    @Operation(
        summary = "保存退库信息",
        description = "保存退库信息"
    )
    @RequestMapping(
        method = POST,
        value = "/spm-projects/{projId}/save-rti",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ReceiveReceiptResultDTO> saveRti(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialReturnReceiptDTO returnReceiptDTO
    ) {

        return new JsonObjectResponseBody<>(
            getContext(),
            returnReceiptInterface.saveRti(projId, returnReceiptDTO)
        );
    }

    @Override
    @Operation(
        summary = "执行退库处理",
        description = "执行退库处理"
    )
    @RequestMapping(
        method = POST,
        value = "/spm-projects/{projId}/run-rti",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ReceiveReceiptResultDTO> runRti(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialReturnReceiptDTO returnReceiptDTO
    ) {

        return new JsonObjectResponseBody<>(
            getContext(),
            returnReceiptInterface.runRti(projId, returnReceiptDTO)
        );
    }
}
