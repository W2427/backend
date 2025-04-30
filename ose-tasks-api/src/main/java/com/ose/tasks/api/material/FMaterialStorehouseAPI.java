package com.ose.tasks.api.material;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.FMaterialStorehouseDTO;
import com.ose.tasks.dto.material.FMaterialStorehouseIdDTO;
import com.ose.tasks.entity.material.FMaterialStorehouseEntity;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 库位管理
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/warehouse-location")
public interface FMaterialStorehouseAPI {

    /**
     * 创建库位
     *
     * @param orgId                  组织 ID
     * @param projectId              项目 ID
     * @param fMaterialStorehouseDTO 库位信息
     * @return 创建的库位
     */
    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialStorehouseEntity> create(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @RequestBody FMaterialStorehouseDTO fMaterialStorehouseDTO);

    /**
     * 获取列表
     *
     * @param pageDTO                分页信息
     * @param fMaterialStorehouseDTO 筛选信息
     * @return 库位管理列表
     */
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    JsonListResponseBody<FMaterialStorehouseEntity> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO,
        FMaterialStorehouseDTO fMaterialStorehouseDTO
    );

    /**
     * 获取库位详情
     *
     * @param orgId               组织ID
     * @param projectId           项目ID
     * @param warehouseLocationId 库位ID
     * @return 库位详情
     */
    @GetMapping(
        value = "/{warehouseLocationId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialStorehouseEntity> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("warehouseLocationId") Long warehouseLocationId
    );

    /**
     * 编辑库位详情
     *
     * @param orgId               组织ID
     * @param projectId           项目ID
     * @param warehouseLocationId 库位ID
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/{warehouseLocationId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialStorehouseEntity> edit(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "warehouseLocationId") Long warehouseLocationId,
        @RequestBody FMaterialStorehouseDTO fMaterialStorehouseDTO);

    /**
     * 删除库位详情
     *
     * @param orgId               组织ID
     * @param projectId           项目ID
     * @param warehouseLocationId 库位ID
     * @return
     */
    @RequestMapping(
        method = DELETE,
        value = "/{warehouseLocationId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "warehouseLocationId") Long warehouseLocationId);

    /**
     * 库位打印状态变更
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 库位详情
     */
    @RequestMapping(
        method = POST,
        value = "/print",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody print(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @RequestBody FMaterialStorehouseIdDTO fMaterialStorehouseIdDTO);


    /**
     * 获取库位二维码详情
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param qrCode    二维码
     * @return 库位详情
     */
    @GetMapping(
        value = "/qrCode/{qrCode}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<FMaterialStorehouseEntity> getQrCode(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("qrCode") String qrCode
    );

}
