package com.ose.materialspm.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.materialspm.api.ReqAPI;
import com.ose.materialspm.domain.model.service.ReqInterface;
import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.dto.ReqDetailDTO;
import com.ose.materialspm.dto.ReqListCriteriaDTO;
import com.ose.materialspm.entity.ReqDetail;
import com.ose.materialspm.entity.ViewMxjReqs;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "SPM 请购单(REQ) 查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class ReqController extends BaseController implements ReqAPI {

    /**
     * 请购单列表查询服务
     */
    private final ReqInterface reqServeice;

    private UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法
     *
     * @param reqServeice    请购单列表查询服务
     * @param uploadFeignAPI
     */
    @Autowired
    public ReqController(
        ReqInterface reqServeice,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.reqServeice = reqServeice;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 获取请购单列表
     *
     * @return 请购单列表
     */
    @Override
    @Operation(
        summary = "获取请购单列表",
        description = "获取请购单列表。"
    )
    @RequestMapping(
        method = GET,
        value = "reqs",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ViewMxjReqs> getList(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ReqListCriteriaDTO reqListDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            reqServeice.getList(reqListDTO)
        );
    }

    @Override
    @Operation(
        summary = "获取请购单详情",
        description = "获取请购单详情。"
    )
    @RequestMapping(
        method = GET,
        value = "req",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ViewMxjReqs> getReq(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ReqDetailDTO reqDetailDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            reqServeice.getReq(reqDetailDTO)
        );
    }

    @Override
    @Operation(
        summary = "导出请购单",
        description = "导出请购单。"
    )
    @RequestMapping(
        method = GET,
        value = "export-req",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ExportFileDTO> exportReq(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ReqDetailDTO reqDetailDTO) {

        return new JsonObjectResponseBody<>(
            reqServeice.exportReq(orgId, projectId, reqDetailDTO, uploadFeignAPI));
    }

    /**
     * 获取请购单详情
     *
     * @return 请购单详情
     */
    @Override
    @Operation(
        summary = "获取请购单明细",
        description = "获取请购单明细。"
    )
    @RequestMapping(
        method = GET,
        value = "req-items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ReqDetail> getDetail(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        ReqDetailDTO reqDetailDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            reqServeice.getDetail(reqDetailDTO)
        );
    }
}
