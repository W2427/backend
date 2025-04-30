package com.ose.tasks.api.material;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ose.dto.PageDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.dto.material.FMaterialInspectionReportResultsDTO;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 检验报告接口
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface FMaterialInspectionReportAPI {

    @RequestMapping(
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    public JsonObjectResponseBody<FMaterialInspectionReportResultsDTO> getInspectionReports(
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "relnId") Long relnId,
        PageDTO pageDTO);

}
