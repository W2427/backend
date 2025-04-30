package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmMaterialCodeTypeProjectAPI;
import com.ose.material.domain.model.service.MmMaterialCodeTypeProjectInterface;
import com.ose.material.dto.MmMaterialCodeTypeCreateDTO;
import com.ose.material.dto.MmMaterialCodeTypeSearchDTO;
import com.ose.material.entity.MmMaterialCodeTypeEntity;
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

@Api(description = "材料编码类型接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/material-code-type")
public class MmMaterialCodeTypeProjectController extends BaseController implements MmMaterialCodeTypeProjectAPI {

    /**
     * 材料编码类型接口服务
     */
    private MmMaterialCodeTypeProjectInterface mmMaterialCodeTypeService;
    /**
     * 构造方法
     *
     */
    @Autowired
    public MmMaterialCodeTypeProjectController(
        MmMaterialCodeTypeProjectInterface mmMaterialCodeTypeService
    ) {
        this.mmMaterialCodeTypeService = mmMaterialCodeTypeService;
    }

    @Override
    @Operation(
        summary = "创建材料编码类型",
        description = "创建材料编码类型。"
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
        @RequestBody @Parameter(description = "材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO) {
        mmMaterialCodeTypeService.create(orgId, projectId, mmMaterialCodeTypeCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取材料编码类型信息
     *
     * @return 材料编码类型列表
     */
    @Override
    @Operation(
        summary = "查询材料编码类型",
        description = "查询材料编码类型。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmMaterialCodeTypeEntity> search(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        MmMaterialCodeTypeSearchDTO mmMaterialCodeTypeSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmMaterialCodeTypeService.search(
                orgId,
                projectId,
                mmMaterialCodeTypeSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑材料编码类型",
        description = "编辑材料编码类型"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialCodeTypeId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "材料编码类型id") Long materialCodeTypeId,
        @RequestBody @Parameter(description = "材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO

    ) {
        mmMaterialCodeTypeService.update(orgId, projectId, materialCodeTypeId, mmMaterialCodeTypeCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除材料编码类型",
        description = "删除材料编码类型"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{materialCodeTypeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "组织id") Long projectId,
        @PathVariable @Parameter(description = "材料编码类型id") Long materialCodeTypeId) {
        mmMaterialCodeTypeService.delete(orgId, projectId, materialCodeTypeId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取材料编码类型信息",
        description = "获取材料编码类型信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialCodeTypeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmMaterialCodeTypeEntity> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "材料编码类型id") Long materialCodeTypeId) {
        return new JsonObjectResponseBody<>(
            getContext(), mmMaterialCodeTypeService.detail(orgId, projectId, materialCodeTypeId)
        );
    }

    /**
     * 获取某类材料编码类型信息
     *
     * @return 材料编码类型列表
     */
    @Override
    @Operation(
        summary = "查询某类材料编码类型",
        description = "查询某类材料编码类型。"
    )
    @RequestMapping(
        method = GET,
        value = "/type/{materialOrganizationType}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmMaterialCodeTypeEntity> searchType(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "材料编码类型类型") String materialOrganizationType) {

        return new JsonListResponseBody<>(
            getContext(),
            mmMaterialCodeTypeService.searchType(
                orgId,
                projectId,
                materialOrganizationType
            )
        );
    }

}
