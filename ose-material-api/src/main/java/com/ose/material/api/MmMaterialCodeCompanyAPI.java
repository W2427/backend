package com.ose.material.api;

import com.ose.material.dto.MmMaterialCodeCreateDTO;
import com.ose.material.dto.MmMaterialCodeSearchDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
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

@RequestMapping(value = "/companyId/{companyId}/material-code")

public interface MmMaterialCodeCompanyAPI {

    @Operation(
        summary = "创建材料编码",
        description = "创建材料编码。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @RequestBody @Parameter(description = "材料编码信息") MmMaterialCodeCreateDTO tagNumberCompanyCreateDTO);

    /**
     * 获取物料列表
     *
     * @return 物料列表
     */
    @Operation(
        summary = "查询材料编码",
        description = "获取材料编码。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmMaterialCodeEntity> search(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        MmMaterialCodeSearchDTO tagNumberCompanySearchDTO);

    @Operation(
        summary = "编辑材料编码",
        description = "编辑材料编码"
    )
    @RequestMapping(
        method = POST,
        value = "/{tagNumberId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "材料编码id") Long tagNumberId,
        @RequestBody @Parameter(description = "物料信息") MmMaterialCodeCreateDTO tagNumberCompanyCreateDTO

    );

    @Operation(
        summary = "删除材料编码",
        description = "删除材料编码"
    )
    @RequestMapping(
        method = POST,
        value = "/{tagNumberId}/delete",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "材料编码id") Long tagNumberId,
        @RequestBody @Parameter(description = "物料信息") MmMaterialCodeCreateDTO mmMaterialCodeCreateDTO);

    @Operation(
        summary = "获取材料编码详细信息",
        description = "获取材料编码详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{tagNumberId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmMaterialCodeEntity> detail(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "材料编码id") Long tagNumberId);



}
