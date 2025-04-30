package com.ose.material.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.material.api.MmCompanyVendorAPI;
import com.ose.material.domain.model.service.MmCompanyVendorInterface;
import com.ose.material.dto.MmCompanyVendorCreateDTO;
import com.ose.material.dto.MmVendorSearchDTO;
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

@Tag(name = "公司级供货商接口")
@RestController
@RequestMapping(value = "/company/{companyId}/vendor")
public class MmCompanyVendorController extends BaseController implements MmCompanyVendorAPI {

    /**
     * 公司级供货商接口服务
     */
    private MmCompanyVendorInterface mmCompanyVendorService;

    /**
     * 构造方法
     */
    @Autowired
    public MmCompanyVendorController(
        MmCompanyVendorInterface mmCompanyVendorService
    ) {
        this.mmCompanyVendorService = mmCompanyVendorService;
    }

    @Override
    @Operation(
        summary = "创建公司级供货商",
        description = "创建公司级供货商。"
    )
    @RequestMapping(
        method = POST
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    @SetUserInfo
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        @RequestBody @Parameter(description = "供货商信息") MmCompanyVendorCreateDTO mmCompanyVendorCreateDTO) {
        mmCompanyVendorService.create(companyId, mmCompanyVendorCreateDTO, getContext());
        return new JsonResponseBody();
    }

    /**
     * 获取公司级供货商信息
     *
     * @return 公司级供货商列表
     */
    @Override
    @Operation(
        summary = "查询公司级供货商",
        description = "查询公司级供货商。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @SetUserInfo
    @ResponseStatus(OK)
    public JsonListResponseBody<MmVendorEntity> search(
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        MmVendorSearchDTO mmVendorSearchDTO) {

        return new JsonListResponseBody<>(
            getContext(),
            mmCompanyVendorService.search(
                companyId,
                mmVendorSearchDTO
            )
        );
    }

    @Override
    @Operation(
        summary = "编辑公司级供货商",
        description = "编辑公司级供货商"
    )
    @RequestMapping(
        method = POST,
        value = "/{vendorId}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody edit(
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        @PathVariable @Parameter(description = "供货商ID") Long vendorId,
        @RequestBody @Parameter(description = "供货商信息") MmCompanyVendorCreateDTO mmCompanyVendorCreateDTO

    ) {
        mmCompanyVendorService.update(companyId, vendorId, mmCompanyVendorCreateDTO, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "删除公司级供货商",
        description = "删除公司级供货商"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{vendorId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        @PathVariable @Parameter(description = "供货商id") Long vendorId) {
        mmCompanyVendorService.delete(companyId, vendorId, getContext());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "获取公司级供货商信息",
        description = "获取公司级供货商信息"
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
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        @PathVariable @Parameter(description = "供货商id") Long vendorId) {
        return new JsonObjectResponseBody<>(
            getContext(), mmCompanyVendorService.detail(companyId, vendorId)
        );
    }
}
