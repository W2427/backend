package com.ose.materialspm.api;

import com.ose.materialspm.dto.ExportFileDTO;
import com.ose.materialspm.dto.PohDTO;
import com.ose.materialspm.entity.PoDetail;
import com.ose.materialspm.entity.ViewMxjValidPohEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 合同查询接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface PoAPI {

    @RequestMapping(
        method = GET,
        value = "pos",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<ViewMxjValidPohEntity> getPohs(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PohDTO pohDTO
    );

    @RequestMapping(
        method = GET,
        value = "po",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ViewMxjValidPohEntity> getPoh(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PohDTO pohDTO);

    @RequestMapping(
        method = GET,
        value = "export-po",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<ExportFileDTO> exportPoh(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PohDTO pohDTO);

    @RequestMapping(
        method = GET,
        value = "po-items",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<PoDetail> getPoDetail(
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        PohDTO pohDTO
    );

}
