package com.ose.materialspm.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.materialspm.api.SpmMaterialRequisitionAPI;
import com.ose.materialspm.domain.model.service.SpmMaterialRequisitionInterface;
import com.ose.materialspm.dto.*;
import com.ose.materialspm.entity.ViewMxjBominfoEntity;
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

@Tag(name = "领料单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/mr")
public class SpmMaterialRequisitionController extends BaseController implements SpmMaterialRequisitionAPI {

    /**
     * 查询服务
     */
    private final SpmMaterialRequisitionInterface materialRequisitionInterface;

    /**
     * 构造方法
     */
    @Autowired
    public SpmMaterialRequisitionController(SpmMaterialRequisitionInterface materialRequisitionInterface) {
        this.materialRequisitionInterface = materialRequisitionInterface;
    }

    /**
     * 查询bom信息
     */
    @Override
    @Operation(
        summary = "库存查询",
        description = "库存查询"
    )
    @RequestMapping(
        method = GET,
        value = "inventory",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<ViewMxjBominfoEntity> getInventory(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        BominfoListSimpleDTO bominfoListSimpleDTO) {
        return new JsonListResponseBody<>(
            getContext(),
            materialRequisitionInterface.getBominfoListSimple(bominfoListSimpleDTO)
        );
    }

    /**
     * 查询bom信息
     */
    @Override
    @Operation(
        summary = "根据领料单号，查询领料单信息",
        description = "根据领料单号，查询领料单信息"
    )
    @RequestMapping(
        method = GET,
        value = "fas",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<FaListResultsDTO> getFahList(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        FaListDTO faListDTO) {
        return new JsonListResponseBody<>(
            getContext(),
            materialRequisitionInterface.getFahList(faListDTO)
        );
    }

    /**
     * 查询bom信息
     */
    @Override
    @Operation(
        summary = "根据领料单ID，查询领料单详情信息（页面使用）",
        description = "根据领料单ID，查询领料单详情信息（页面使用）。"
    )
    @RequestMapping(
        method = GET,
        value = "/fads",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<FadListResultsDTO> getFadListForDisplay(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        FadListDTO fadListDTO) {
        return new JsonListResponseBody<>(
            getContext(),
            materialRequisitionInterface.getFadList(fadListDTO)
        );
    }

    /**
     * 查询bom信息
     */
    @Override
    @Operation(
        summary = "根据领料单ID，查询领料单详情信息",
        description = "根据领料单ID，查询领料单详情信息"
    )
    @RequestMapping(
        method = POST,
        value = "/fads",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<FadListResultsDTO> getFadList(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @RequestBody FadListDTO fadListDTO) {
        return new JsonListResponseBody<>(
            getContext(),
            materialRequisitionInterface.getFadListNoPage(fadListDTO)
        );
    }

    @Override
    @Operation(
        summary = "根据领料单ID，查询领料单信息",
        description = "根据领料单ID，查询领料单信息"
    )
    @RequestMapping(
        method = POST,
        value = "/fah",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<FaListResultsDTO> getFah(
        @PathVariable(value = "projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable(value = "orgId") @Parameter(description = "orgId") Long orgId,
        @RequestBody FadListDTO fadListDTO) {
        return new JsonObjectResponseBody<>(
            getContext(),
            materialRequisitionInterface.getFah(fadListDTO)
        );
    }

}
