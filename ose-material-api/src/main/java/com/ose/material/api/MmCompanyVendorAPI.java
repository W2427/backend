package com.ose.material.api;

import com.ose.material.dto.MmCompanyVendorCreateDTO;
import com.ose.material.dto.MmVendorSearchDTO;
import com.ose.material.entity.MmVendorEntity;
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

@RequestMapping(value = "/company/{companyId}/vendor")
public interface MmCompanyVendorAPI {

    @Operation(
        summary = "创建公司级供货商",
        description = "创建公司级供货商。"
    )
    @RequestMapping(
        method = POST,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        @RequestBody @Parameter(description = "供货商信息") MmCompanyVendorCreateDTO mmCompanyVendorCreateDTO);

    /**
     * 查询公司级供货商
     *
     * @return 查询公司级供货商
     */
    @Operation(
        summary = "查询公司级供货商",
        description = "查询公司级供货商。"
    )
    @RequestMapping(
        method = GET,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmVendorEntity> search(
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        MmVendorSearchDTO mmVendorSearchDTO);

    @Operation(
        summary = "编辑公司级供货商",
        description = "编辑公司级供货商"
    )
    @RequestMapping(
        method = POST,
        value = "/{vendorId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        @PathVariable @Parameter(description = "供货商ID") Long vendorId,
        @RequestBody @Parameter(description = "采购包信息") MmCompanyVendorCreateDTO mmCompanyVendorCreateDTO

    );

    @Operation(
        summary = "删除公司级供货商",
        description = "删除公司级供货商"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{vendorId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        @PathVariable @Parameter(description = "供货商id") Long vendorId);

    @Operation(
        summary = "获取公司级供货商信息",
        description = "获取公司级供货商信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{vendorId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmVendorEntity> detail(
        @PathVariable @Parameter(description = "公司ID") Long companyId,
        @PathVariable @Parameter(description = "供货商id") Long vendorId);
}
