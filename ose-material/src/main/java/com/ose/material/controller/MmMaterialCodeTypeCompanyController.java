package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmMaterialCodeTypeCompanyAPI;
import com.ose.material.domain.model.service.MmMaterialCodeTypeCompanyInterface;
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

@Api(description = "公司材料编码类型接口")
@RestController
@RequestMapping(value = "/companyId/{companyId}/material-code-type")
public class MmMaterialCodeTypeCompanyController extends BaseController implements MmMaterialCodeTypeCompanyAPI {

    /**
     * 公司材料编码类型接口服务
     */
    private MmMaterialCodeTypeCompanyInterface mmMaterialCodeTypeCompanyService;
    /**
     * 构造方法
     *
     */
    @Autowired
    public MmMaterialCodeTypeCompanyController(
        MmMaterialCodeTypeCompanyInterface mmMaterialCodeTypeCompanyService
    ) {
        this.mmMaterialCodeTypeCompanyService = mmMaterialCodeTypeCompanyService;
    }

    @Override
    @Operation(
        summary = "创建公司材料编码类型",
        description = "创建公司材料编码类型。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    @SetUserInfo
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @RequestBody @Parameter(description = "公司材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO) {
        mmMaterialCodeTypeCompanyService.create(companyId, mmMaterialCodeTypeCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取公司材料编码类型信息
     *
     * @return 公司材料编码类型列表
     */
    @Override
    @Operation(
        summary = "查询公司材料编码类型",
        description = "查询公司材料编码类型。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmMaterialCodeTypeEntity> search(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        MmMaterialCodeTypeSearchDTO mmMaterialCodeTypeSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmMaterialCodeTypeCompanyService.search(
                companyId,
                mmMaterialCodeTypeSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑公司材料编码类型",
        description = "编辑公司材料编码类型"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialCodeTypeId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "公司材料编码类型id") Long materialCodeTypeId,
        @RequestBody @Parameter(description = "公司材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO

    ) {
        mmMaterialCodeTypeCompanyService.update(companyId, materialCodeTypeId, mmMaterialCodeTypeCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除公司材料编码类型",
        description = "删除公司材料编码类型"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialCodeTypeId}/delete",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "公司材料编码类型id") Long materialCodeTypeId,
        @RequestBody @Parameter(description = "公司材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO) {
        mmMaterialCodeTypeCompanyService.delete(companyId, materialCodeTypeId, mmMaterialCodeTypeCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取公司材料编码类型信息",
        description = "获取公司材料编码类型信息"
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
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "公司材料编码类型id") Long materialCodeTypeId) {
        return new JsonObjectResponseBody<>(
            getContext(), mmMaterialCodeTypeCompanyService.detail(companyId, materialCodeTypeId)
        );
    }

}
