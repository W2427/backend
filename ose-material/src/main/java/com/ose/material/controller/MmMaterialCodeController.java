package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmMaterialCodeAPI;
import com.ose.material.domain.model.service.MmMaterialCodeInterface;
import com.ose.material.dto.MmMaterialCodeCreateDTO;
import com.ose.material.dto.MmMaterialCodeSearchDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Api(description = "材料编码接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-code")
public class MmMaterialCodeController extends BaseController implements MmMaterialCodeAPI {

    /**
     * 公司材料编码接口服务
     */
    private final MmMaterialCodeInterface materialTagNumberService;
    /**
     * 构造方法
     *
     */
    @Autowired
    public MmMaterialCodeController(
        MmMaterialCodeInterface materialTagNumberService
    ) {
        this.materialTagNumberService = materialTagNumberService;
    }

    @Override
    @Operation(
        summary = "创建材料编码",
        description = "创建材料编码。"
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
        @RequestBody @Parameter(description = "材料编码信息") MmMaterialCodeCreateDTO tagNumberCompanyCreateDTO) {
        materialTagNumberService.create(orgId, projectId, tagNumberCompanyCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取材料编码列表
     *
     * @return 材料编码列表
     */
    @Override
    @Operation(
        summary = "查询材料编码",
        description = "查询材料编码。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmMaterialCodeEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmMaterialCodeSearchDTO tagNumberCompanySearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialTagNumberService.search(
                orgId,
                projectId,
                tagNumberCompanySearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑材料编码",
        description = "编辑材料编码"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialCodeId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "材料编码id") Long materialCodeId,
        @RequestBody @Parameter(description = "物料信息") MmMaterialCodeCreateDTO tagNumberCompanyCreateDTO

    ) {
        materialTagNumberService.update(orgId, projectId, materialCodeId, tagNumberCompanyCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除材料编码",
        description = "删除材料编码"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialCodeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "材料编码id") Long materialCodeId) {
        materialTagNumberService.delete(orgId, projectId, materialCodeId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取材料编码详细信息",
        description = "获取材料编码详细信息"
    )
    @RequestMapping(
        method = GET,
        value= "/{materialCodeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmMaterialCodeEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "材料编码id") Long materialCodeId) {
        return new JsonObjectResponseBody<>(
            getContext(), materialTagNumberService.detail(orgId, projectId, materialCodeId)
        );
    }

    @Override
    @Operation(
        summary = "获取材料编码详细信息",
        description = "获取材料编码详细信息"
    )
    @RequestMapping(
        method = POST,
        value = "/detail",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmMaterialCodeEntity> searchDetail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody MmMaterialCodeSearchDTO tagNumberCompanySearchDTO) {
        return new JsonObjectResponseBody<>(
            getContext(), materialTagNumberService.searchDetail(orgId, projectId, tagNumberCompanySearchDTO)
        );
    }

}
