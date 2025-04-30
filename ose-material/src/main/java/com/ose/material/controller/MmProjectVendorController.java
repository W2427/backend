package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmProjectVendorAPI;
import com.ose.material.domain.model.service.MmProjectVendorInterface;
import com.ose.material.dto.*;
import com.ose.material.entity.MmVendorEntity;
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

@Tag(name = "供货商接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/vendor")
public class MmProjectVendorController extends BaseController implements MmProjectVendorAPI {

    /**
     * 供货商接口服务
     */
    private MmProjectVendorInterface mmProjectVendorService;

    /**
     * 构造方法
     */
    @Autowired
    public MmProjectVendorController(
        MmProjectVendorInterface mmProjectVendorService
    ) {
        this.mmProjectVendorService = mmProjectVendorService;
    }

    @Override
    @Operation(
        summary = "创建供货商",
        description = "创建供货商。"
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
        @RequestBody @Parameter(description = "供货商信息") MmProjectVendorCreateDTO mmProjectVendorCreateDTO) {
        mmProjectVendorService.create(orgId, projectId, mmProjectVendorCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取供货商信息
     *
     * @return 供货商列表
     */
    @Override
    @Operation(
        summary = "查询供货商",
        description = "查询供货商。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmVendorEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmVendorSearchDTO mmVendorSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmProjectVendorService.search(
                orgId,
                projectId,
                mmVendorSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "删除供货商",
        description = "删除供货商"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{vendorId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "组织id") Long projectId,
        @PathVariable @Parameter(description = "供货商id") Long vendorId) {
        mmProjectVendorService.delete(orgId, projectId, vendorId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取供货商信息",
        description = "获取供货商信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{vendorId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmVendorEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "供货商id") Long vendorId) {
        return new JsonObjectResponseBody<>(
            getContext(), mmProjectVendorService.detail(orgId, projectId, vendorId)
        );
    }
}
