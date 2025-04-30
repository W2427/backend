package com.ose.tasks.api.material;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.material.StructureEntityQrCodeCriteriaDTO;
import com.ose.tasks.entity.material.StructureEntityQrCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 结构实体二维码查询接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/structure-qrcode")
public interface StructureEntityQrCodeAPI {

    @RequestMapping(
        method = GET,
        value = ""
    )
    @ResponseStatus(OK)
    JsonListResponseBody<StructureEntityQrCode> search(
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projectId") @Parameter(description = "projectId") Long projectId,
        StructureEntityQrCodeCriteriaDTO criteriaDTO
    );

    @Operation(
        summary = "更新结构两件二维码旧数据",
        description = "更新结构两件二维码旧数据。")
    @RequestMapping(
        method = POST,
        value = "generate"
    )
    @ResponseStatus(CREATED)
    JsonResponseBody create(@PathVariable @Parameter(description = "组织id") Long orgId,
                            @PathVariable @Parameter(description = "项目id") Long projectId);

}
