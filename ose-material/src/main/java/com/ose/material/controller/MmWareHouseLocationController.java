package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmWareHouseLocationAPI;
import com.ose.material.domain.model.service.MmWareHouseLocationInterface;
import com.ose.material.dto.MmWareHouseLocationCreateDTO;
import com.ose.material.dto.MmWareHouseLocationSearchDTO;
import com.ose.material.entity.MmWareHouseLocationEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "仓库货位接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/ware-house-location")
public class MmWareHouseLocationController extends BaseController implements MmWareHouseLocationAPI {

    /**
     * 仓库货位接口服务
     */
    private MmWareHouseLocationInterface mmWareHouseLocationService;
    /**
     * 构造方法
     *
     */
    @Autowired
    public MmWareHouseLocationController(
        MmWareHouseLocationInterface mmWareHouseLocationService
    ) {
        this.mmWareHouseLocationService = mmWareHouseLocationService;
    }

    @Override
    @Operation(
        summary = "创建仓库货位",
        description = "创建仓库货位。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    @SetUserInfo
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "仓库货位信息") MmWareHouseLocationCreateDTO mmWareHouseLocationCreateDTO) {
        mmWareHouseLocationService.create(orgId, projectId, mmWareHouseLocationCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取仓库货位信息
     *
     * @return 仓库货位列表
     */
    @Override
    @Operation(
        summary = "查询仓库货位",
        description = "查询仓库货位。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmWareHouseLocationEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmWareHouseLocationSearchDTO mmWareHouseLocationSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmWareHouseLocationService.search(
                orgId,
                projectId,
                mmWareHouseLocationSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑仓库货位",
        description = "编辑仓库货位"
    )
    @RequestMapping(
        method = POST,
        value = "/{wareHouseLocationId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "仓库货位id") Long wareHouseLocationId,
        @RequestBody @Parameter(description = "仓库货位信息") MmWareHouseLocationCreateDTO mmWareHouseLocationCreateDTO

    ) {
        mmWareHouseLocationService.update(orgId, projectId, wareHouseLocationId, mmWareHouseLocationCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除仓库货位",
        description = "删除仓库货位"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{wareHouseLocationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "组织id") Long projectId,
        @PathVariable @Parameter(description = "仓库货位id") Long wareHouseLocationId) {
        mmWareHouseLocationService.delete(orgId, projectId, wareHouseLocationId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取仓库货位信息",
        description = "获取仓库货位信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{wareHouseLocationId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmWareHouseLocationEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "仓库货位id") Long wareHouseLocationId) {
        return new JsonObjectResponseBody<>(
            getContext(), mmWareHouseLocationService.detail(orgId, projectId, wareHouseLocationId)
        );
    }

    /**
     * 获取某类仓库货位信息
     *
     * @return 仓库货位列表
     */
    @Override
    @Operation(
        summary = "查询某类仓库货位",
        description = "查询某类仓库货位。"
    )
    @RequestMapping(
        method = GET,
        value = "/type/{wareHouseType}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmWareHouseLocationEntity> searchType(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "仓库货位类型") String wareHouseType) {

        return new JsonListResponseBody<>(
            getContext(),
            mmWareHouseLocationService.searchType(
                orgId,
                projectId,
                wareHouseType
            )
        );
    }

}
