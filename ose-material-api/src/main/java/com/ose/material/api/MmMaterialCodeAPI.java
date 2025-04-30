package com.ose.material.api;

import com.ose.material.dto.MmMaterialCodeCreateDTO;
import com.ose.material.dto.MmMaterialCodeSearchDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-code")
@FeignClient("ose-material")
public interface MmMaterialCodeAPI {

    @Operation(
        summary = "创建材料编码",
        description = "创建材料编码。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description =  "项目id") Long projectId,
        @RequestBody @Parameter(description = "材料编码信息") MmMaterialCodeCreateDTO tagNumberCompanyCreateDTO);

    /**
     * 获取物料列表
     *
     * @return 物料列表
     */
    @Operation(
        summary = "查询材料编码",
        description = "获取材料编码。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmMaterialCodeEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmMaterialCodeSearchDTO tagNumberCompanySearchDTO);

    @Operation(
        summary = "编辑材料编码",
        description = "编辑材料编码"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialCodeId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description  = "材料编码id") Long materialCodeId,
        @RequestBody @Parameter(description = "物料信息") MmMaterialCodeCreateDTO tagNumberCompanyCreateDTO

    );

    @Operation(
        summary = "删除材料编码",
        description = "删除材料编码"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialCodeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "材料编码id") Long materialCodeId);

    @Operation(
        summary = "获取材料编码详细信息",
        description = "获取材料编码详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialCodeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmMaterialCodeEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "材料编码id") Long materialCodeId);

    @Operation(
        summary = "获取材料编码详细信息",
        description = "获取材料编码详细信息"
    )
    @RequestMapping(
        method = POST,
        value = "/detail",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmMaterialCodeEntity> searchDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody MmMaterialCodeSearchDTO tagNumberCompanySearchDTO);


}
