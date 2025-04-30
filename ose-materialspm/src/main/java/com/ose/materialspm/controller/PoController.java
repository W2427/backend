package com.ose.materialspm.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.materialspm.api.PoAPI;
import com.ose.materialspm.domain.model.service.PoInterface;
import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.dto.PohDTO;
import com.ose.materialspm.entity.PoDetail;
import com.ose.materialspm.entity.ViewMxjValidPohEntity;
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

@Tag(name = "SPM 合同(PO) 查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class PoController extends BaseController implements PoAPI {

    /**
     * 合同查询服务
     */
    private final PoInterface poServeice;

    private UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法
     */
    @Autowired
    public PoController(
        PoInterface poServeice,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.poServeice = poServeice;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    @Override
    @Operation(
        summary = "获取合同列表",
        description = "获取合同列表。"
    )
    @RequestMapping(
        method = GET,
        value = "pos",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ViewMxjValidPohEntity> getPohs(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PohDTO pohDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            poServeice.getPohs(pohDTO)
        );
    }

    @Override
    @Operation(
        summary = "获取合同详情",
        description = "获取合同详情。"
    )
    @RequestMapping(
        method = GET,
        value = "po",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ViewMxjValidPohEntity> getPoh(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PohDTO pohDTO) {

        return new JsonObjectResponseBody<>(
            getContext(),
            poServeice.getPoh(pohDTO)
        );
    }

    @Override
    @Operation(
        summary = "导出合同",
        description = "导出合同。"
    )
    @RequestMapping(
        method = GET,
        value = "export-po",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ExportFileDTO> exportPoh(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PohDTO pohDTO) {

        return new JsonObjectResponseBody<>(
            poServeice.exportPoh(orgId, projectId, pohDTO, uploadFeignAPI));
    }


    @Override
    @Operation(
        summary = "获取合同明细",
        description = "获取合同明细。"
    )
    @RequestMapping(
        method = GET,
        value = "po-items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<PoDetail> getPoDetail(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PohDTO pohDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            poServeice.getDetail(pohDTO)
        );
    }
}
