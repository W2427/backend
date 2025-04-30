package com.ose.material.api;

import com.ose.material.dto.*;
import com.ose.material.entity.MmHeatBatchNoEntity;
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

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/heat-batch-no")
public interface HeatBatchNoAPI {

    @Operation(
        summary = "创建炉批号",
        description = "创建炉批号。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "炉批号信息") HeatBatchNoCreateDTO heatBatchNoCreateDTO);

    /**
     * 获取炉批号列表
     *
     * @return 炉批号列表
     */
    @Operation(
        summary = "炉批号列表",
        description = "炉批号列表。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmHeatBatchNoEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        HeatBatchNoSearchDTO heatBatchNoSearchDTO);

    @Operation(
        summary = "编辑炉批号",
        description = "编辑炉批号"
    )
    @RequestMapping(
        method = POST,
        value = "/{heatBatchId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "炉批号id") Long heatBatchId,
        @RequestBody @Parameter(description = "物料信息") HeatBatchNoCreateDTO heatBatchNoCreateDTO

    );

    @Operation(
        summary = "删除物料",
        description = "删除物料"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{heatBatchId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "炉批号id") Long heatBatchId);

    @Operation(
        summary = "获取炉批号详细信息",
        description = "获取炉批号详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{heatBatchId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmHeatBatchNoEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "炉批号id") Long heatBatchId);

    /**
     * 炉批号导入
     *
     * @return 炉批号导入
     */
    @Operation(
        summary = "炉批号导入",
        description = "炉批号导入。"
    )
    @RequestMapping(
        method = POST,
        value = "/import"
    )
    @ResponseStatus(OK)
    JsonResponseBody importHeatBatch(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody MmImportBatchTaskImportDTO importDTO);

}
