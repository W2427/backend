package com.ose.materialspm.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.materialspm.api.BomAPI;
import com.ose.materialspm.domain.model.service.BomInterface;
import com.ose.materialspm.dto.BomNodeDTO;
import com.ose.materialspm.dto.BomNodeResultsDTO;
import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.entity.ViewMxjPosEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Tag(name = "SPM BOM 查询接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class BomController extends BaseController implements BomAPI {

    /**
     * 工序服务
     */
    private final BomInterface bomServeice;

    private UploadFeignAPI uploadFeignAPI;

    /**
     * 构造方法
     *
     * @param bomServeice    BOM服务
     * @param uploadFeignAPI
     */
    @Autowired
    public BomController(
        BomInterface bomServeice,
        UploadFeignAPI uploadFeignAPI
    ) {
        this.bomServeice = bomServeice;
        this.uploadFeignAPI = uploadFeignAPI;
    }

    /**
     * 获取Bom Node
     *
     * @return node list
     */
    @Override
    @Operation(
        summary = "获取Bom Node列表",
        description = "获取Bom Node列表。"
    )
    @RequestMapping(
        method = GET,
        value = "bomnodes",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BomNodeResultsDTO> bomNodes(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestParam @Parameter(description = "SPM 项目ID") String spmProjId
    ) {

        List<BomNodeResultsDTO> voDto = bomServeice.getByProjId(spmProjId);

        JsonListResponseBody<BomNodeResultsDTO> rs = new JsonListResponseBody<BomNodeResultsDTO>(
            getContext(),
            voDto
        );

        return rs;
    }

    /**
     * 获取Bom List
     *
     * @return bom list
     */
    @Override
    @Operation(
        summary = "获取BOM List",
        description = "获取BOM List。"
    )
    @RequestMapping(
        method = GET,
        value = "boms",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ViewMxjPosEntity> boms(
        HttpServletRequest request,
        HttpServletResponse response,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        BomNodeDTO bomNodeDTO
    ) {

        JsonListResponseBody<ViewMxjPosEntity> rs = new JsonListResponseBody<ViewMxjPosEntity>(
            getContext(),
            bomServeice.getByProjIdAndBompath(bomNodeDTO)
        );

        return rs;
    }

    @Override
    @Operation(
        summary = "导出BOM List",
        description = "导出BOM List。"
    )
    @RequestMapping(
        method = GET,
        value = "export-boms",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ExportFileDTO> exportBoms(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        BomNodeDTO bomNodeDTO
    ) {
        return new JsonObjectResponseBody<>(
            bomServeice.exportBoms(orgId, projectId, bomNodeDTO, uploadFeignAPI));
    }

}
