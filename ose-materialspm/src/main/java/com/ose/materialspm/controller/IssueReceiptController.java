package com.ose.materialspm.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.materialspm.api.IssueReceiptAPI;
import com.ose.materialspm.domain.model.service.IssueReceiptInterface;
import com.ose.materialspm.dto.FMaterialIssueReceiptDTO;
import com.ose.materialspm.dto.IssueReceiptDTO;
import com.ose.materialspm.dto.IssueReceiptListResultsDTO;
import com.ose.materialspm.dto.ReceiveReceiptResultDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "SPM 出库单 查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class IssueReceiptController extends BaseController implements IssueReceiptAPI {

    private final IssueReceiptInterface issueReceiptInterface;

    /**
     * 构造方法
     *
     * @param issueReceiptInterface 发料查询服务
     */
    @Autowired
    public IssueReceiptController(IssueReceiptInterface issueReceiptInterface) {
        this.issueReceiptInterface = issueReceiptInterface;
    }

    @Override
    @Operation(
        summary = "获取出库单列表",
        description = "获取出库单列表。"
    )
    @RequestMapping(
        method = GET,
        value = "issue-receipts",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<IssueReceiptListResultsDTO> getIssueReceipt(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        IssueReceiptDTO issueReceiptDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            issueReceiptInterface.getIssueReceipt(issueReceiptDTO)
        );
    }

    @Override
    @Operation(
        summary = "保存出库信息",
        description = "保存出库信息"
    )
    @RequestMapping(
        method = POST,
        value = "/spm-projects/{projId}/save-mir",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ReceiveReceiptResultDTO> saveMir(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialIssueReceiptDTO issueReceiptDTO) {
        return new JsonObjectResponseBody<>(
            getContext(),
            issueReceiptInterface.saveMir(projId, issueReceiptDTO)
        );
    }

    @Override
    @Operation(
        summary = "执行出库处理",
        description = "执行出库处理"
    )
    @RequestMapping(
        method = POST,
        value = "/spm-projects/{projId}/run-mir",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ReceiveReceiptResultDTO> runMir(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId,
        @RequestBody FMaterialIssueReceiptDTO issueReceiptDTO) {
        return new JsonObjectResponseBody<>(
            getContext(),
            issueReceiptInterface.runMir(projId, issueReceiptDTO)
        );
    }
}
