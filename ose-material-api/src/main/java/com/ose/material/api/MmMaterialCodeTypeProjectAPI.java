package com.ose.material.api;

import com.ose.material.dto.MmMaterialCodeTypeCreateDTO;
import com.ose.material.dto.MmMaterialCodeTypeSearchDTO;
import com.ose.material.entity.MmMaterialCodeTypeEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-code-type")

public interface MmMaterialCodeTypeProjectAPI {

    @Operation(
        summary = "创建材料编码类型",
        description = "创建材料编码类型。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Schema(description ="组织id") Long orgId,
        @PathVariable @Schema(description ="项目id") Long projectId,
        @RequestBody @Schema(description ="材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO);

    /**
     * 获取材料编码类型信息
     *
     * @return 材料编码类型列表
     */
    @Operation(
        summary = "查询材料编码类型",
        description = "查询材料编码类型。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmMaterialCodeTypeEntity> search(
        @PathVariable @Schema(description ="组织id") Long orgId,
        @PathVariable @Schema(description ="项目id") Long projectId,
        MmMaterialCodeTypeSearchDTO mmMaterialCodeTypeSearchDTO);

    @Operation(
        summary = "编辑材料编码类型",
        description = "编辑材料编码类型"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialCodeTypeId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Schema(description ="组织id") Long orgId,
        @PathVariable @Schema(description ="项目id") Long projectId,
        @PathVariable @Schema(description ="材料编码类型id") Long materialCodeTypeId,
        @RequestBody @Schema(description ="材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO

    );

    @Operation(
        summary = "删除材料编码类型",
        description = "删除材料编码类型"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialCodeTypeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Schema(description ="组织id") Long orgId,
        @PathVariable @Schema(description ="项目id") Long projectId,
        @PathVariable @Schema(description ="材料编码类型id") Long materialCodeTypeId);

    @Operation(
        summary = "获取材料编码类型信息",
        description = "获取材料编码类型信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialCodeTypeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmMaterialCodeTypeEntity> detail(
        @PathVariable @Schema(description ="组织id") Long orgId,
        @PathVariable @Schema(description ="项目id") Long projectId,
        @PathVariable @Schema(description ="材料编码类型id") Long materialCodeTypeId);


    /**
     * 获取某类材料编码类型信息
     *
     * @return 材料编码类型列表
     */
    @Operation(
        summary = "查询某类材料编码类型",
        description = "查询某类材料编码类型。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmMaterialCodeTypeEntity> searchType(
        @PathVariable @Schema(description ="组织id") Long orgId,
        @PathVariable @Schema(description ="项目id") Long projectId,
        @PathVariable @Schema(description ="材料编码类型类型") String materialOrganizationType);


}
