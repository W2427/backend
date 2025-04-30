package com.ose.material.api;

import com.ose.material.dto.MmMaterialCodeTypeCreateDTO;
import com.ose.material.dto.MmMaterialCodeTypeSearchDTO;
import com.ose.material.entity.MmMaterialCodeTypeEntity;
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

@RequestMapping(value = "/companyId/{companyId}/material-code-type")

public interface MmMaterialCodeTypeCompanyAPI {

    @Operation(
        summary = "创建公司材料编码类型",
        description = "创建公司材料编码类型。"
    )
    @RequestMapping(
        method = POST
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @RequestBody @Parameter(description = "公司材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO);

    /**
     * 获取公司材料编码类型信息
     *
     * @return 公司材料编码类型列表
     */
    @Operation(
        summary = "查询公司材料编码类型",
        description = "查询公司材料编码类型。"
    )
    @RequestMapping(
        method = GET
    )
    @ResponseStatus(OK)
    JsonListResponseBody<MmMaterialCodeTypeEntity> search(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        MmMaterialCodeTypeSearchDTO mmMaterialCodeTypeSearchDTO);

    @Operation(
        summary = "编辑公司材料编码类型",
        description = "编辑公司材料编码类型"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialCodeTypeId}"
    )
    @ResponseStatus(OK)
    JsonResponseBody edit(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "公司材料编码类型id") Long materialCodeTypeId,
        @RequestBody @Parameter(description = "公司材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO

    );

    @Operation(
        summary = "删除公司材料编码类型",
        description = "删除公司材料编码类型"
    )
    @RequestMapping(
        method = POST,
        value = "/{materialCodeTypeId}/delete",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "公司材料编码类型id") Long materialCodeTypeId,
        @RequestBody @Parameter(description = "公司材料编码类型信息") MmMaterialCodeTypeCreateDTO mmMaterialCodeTypeCreateDTO);

    @Operation(
        summary = "获取公司材料编码类型信息",
        description = "获取公司材料编码类型信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{materialCodeTypeId}",
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<MmMaterialCodeTypeEntity> detail(
        @PathVariable @Parameter(description = "公司id") Long companyId,
        @PathVariable @Parameter(description = "公司材料编码类型id") Long materialCodeTypeId);

}
