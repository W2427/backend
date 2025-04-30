package com.ose.material.api;

import com.ose.material.dto.*;
import com.ose.material.entity.MmVendorEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/vendor")
public interface MmProjectVendorAPI {

    @Operation(
        summary = "添加项目级供货商",
        description = "添加项目级供货商。"
    )
    @RequestMapping(
        method = POST,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "供货商信息") MmProjectVendorCreateDTO mmProjectVendorCreateDTO);

    /**
     * 查询项目级供货商
     *
     * @return 查询项目级供货商
     */
    @Operation(
        summary = "查询项目级供货商",
        description = "查询项目级供货商。"
    )
    @RequestMapping(
        method = GET,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmVendorEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmVendorSearchDTO mmVendorSearchDTO);

    @Operation(
        summary = "删除项目级供货商",
        description = "删除项目级供货商"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{vendorId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "供货商id") Long vendorId);

    @Operation(
        summary = "获取项目级供货商信息",
        description = "获取项目级供货商信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{vendorId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmVendorEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "供货商id") Long vendorId);


}
