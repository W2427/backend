package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmMaterialCodeCompanyAPI;
import com.ose.material.domain.model.service.MmMaterialCodeCompanyInterface;
import com.ose.material.dto.MmMaterialCodeCreateDTO;
import com.ose.material.dto.MmMaterialCodeSearchDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
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

@Tag(name = "材料编码接口")
@RestController
@RequestMapping(value = "/companyId/{companyId}/material-code")
public class MmMaterialCodeCompanyController extends BaseController implements MmMaterialCodeCompanyAPI {

    /**
     * 公司材料编码接口服务
     */
    private final MmMaterialCodeCompanyInterface materialTagNumberCompanyService;
    /**
     * 构造方法
     *
     */
    @Autowired
    public MmMaterialCodeCompanyController(
        MmMaterialCodeCompanyInterface materialTagNumberCompanyService
    ) {
        this.materialTagNumberCompanyService = materialTagNumberCompanyService;
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
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @RequestBody @Parameter(description = "材料编码信息") MmMaterialCodeCreateDTO tagNumberCompanyCreateDTO) {
        materialTagNumberCompanyService.create(companyId, tagNumberCompanyCreateDTO, getContext());
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
        @PathVariable @Parameter(description = "公司id") Long companyId,
        MmMaterialCodeSearchDTO tagNumberCompanySearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            materialTagNumberCompanyService.search(
                companyId,
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
        value = "/{tagNumberId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "材料编码id") Long tagNumberId,
        @RequestBody @Parameter(description = "物料信息") MmMaterialCodeCreateDTO tagNumberCompanyCreateDTO

    ) {
        materialTagNumberCompanyService.update(companyId, tagNumberId, tagNumberCompanyCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除材料编码",
        description = "删除材料编码"
    )
    @RequestMapping(
        method = POST,
        value = "/{tagNumberId}/delete",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "材料编码id") Long tagNumberId,
        @RequestBody @Parameter(description = "物料信息") MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO) {
        materialTagNumberCompanyService.delete(companyId, tagNumberId, mmMaterialCodeCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取材料编码详细信息",
        description = "获取材料编码详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{tagNumberId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonObjectResponseBody<MmMaterialCodeEntity> detail(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "材料编码id") Long tagNumberId) {
        return new JsonObjectResponseBody<>(
            getContext(), materialTagNumberCompanyService.detail(companyId, tagNumberId)
        );
    }

}
