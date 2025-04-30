package com.ose.material.api;

import com.ose.material.dto.MmWareHouseLocationCreateDTO;
import com.ose.material.dto.MmWareHouseLocationSearchDTO;
import com.ose.material.entity.MmWareHouseLocationEntity;
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

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/ware-house-location")
public interface MmWareHouseLocationAPI {

    @Operation(
        summary = "创建仓库货位",
        description = "创建仓库货位。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "仓库信息") MmWareHouseLocationCreateDTO mmWareHouseLocationCreateDTO);

    /**
     * 获取仓库信息
     *
     * @return 仓库列表
     */
    @Operation(
        summary = "查询仓库货位",
        description = "查询仓库货位。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmWareHouseLocationEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmWareHouseLocationSearchDTO mmWareHouseLocationSearchDTO);

    @Operation(
        summary = "编辑仓库货位",
        description = "编辑仓库货位"
    )
    @RequestMapping(
        method = POST,
        value = "/{wareHouseLocationId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "仓库id") Long wareHouseLocationId,
        @RequestBody @Parameter(description = "仓库信息") MmWareHouseLocationCreateDTO mmWareHouseLocationCreateDTO

    );

    @Operation(
        summary = "删除仓库货位",
        description = "删除仓库货位"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{wareHouseLocationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "仓库id") Long wareHouseLocationId);

    @Operation(
        summary = "获取仓库货位信息",
        description = "获取仓库货位信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{wareHouseLocationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmWareHouseLocationEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "仓库货位id") Long wareHouseLocationId);


    /**
     * 获取某类仓库信息
     *
     * @return 仓库列表
     */
    @Operation(
        summary = "查询某类仓库货位",
        description = "查询某类仓库货位。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmWareHouseLocationEntity> searchType(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "仓库货位类型") String wareHouseType);


}
