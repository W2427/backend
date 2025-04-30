package com.ose.material.api;

import com.ose.material.dto.*;
import com.ose.material.entity.MmSurplusMaterialEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient(name = "ose-material", contextId = "mmSurplusMaterialFeign")
public interface MmSurplusMaterialFeignAPI {
    /**
     * 查询余料库
     *
     * @return 查询余料库
     */
    @Operation(
        summary = "查询余料库",
        description = "查询余料库。"
    )
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/surplus-material",
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmSurplusMaterialEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO);

    @Operation(
        summary = "创建余料",
        description = "创建余料。"
    )
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/surplus-material",
        method = POST,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "创建余料信息") MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO);

    @Operation(
        summary = "更新余料",
        description = "更新余料"
    )
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/surplus-material/{surplusMaterialId}",
        method = POST,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody update(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "余料单ID") Long surplusMaterialId,
        @RequestBody @Parameter(description = "创建余料信息") MmSurplusMaterialCreateDTO mmSurplusMaterialCreateDTO);

    @Operation(
        summary = "task服务调用，查找余料信息",
        description = "task服务调用，查找余料信息。"
    )
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/surplus-material/information",
        method = POST,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<MmSurplusMaterialEntity> searchSurplusMaterial(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "查找余料信息查询参数") MmSurplusMaterialSearchDTO mmSurplusMaterialSearchDTO);

}
