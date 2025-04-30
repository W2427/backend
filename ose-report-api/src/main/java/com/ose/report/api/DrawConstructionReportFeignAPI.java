package com.ose.report.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.report.dto.DrawSubPipeDTO;
import com.ose.report.dto.DrawSubPipeSupportDTO;
import com.ose.report.dto.DrawSubPipeSupportOverallDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 检查单报表接口
 */
@FeignClient(name = "ose-report", contextId = "drawConstReportFeign")
public interface DrawConstructionReportFeignAPI {

    /**
     * 模拟检查单制作
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/draw-construction-report/generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportHistory> generateDrawConstructionreport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody @Parameter(description = "生产图纸制造内容") DrawSubPipeDTO drawSubPipeDTO
    );


    /**
     * 模拟检查单制作
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/draw-construction-support-overall-report/generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportHistory> generateDrawConstructionSupportOverallReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody @Parameter(description = "生产图纸制造内容") DrawSubPipeSupportOverallDTO drawSubPipeSupportOverallDTO
    );

    /**
     * 模拟检查单制作
     *
     * @param orgId        组织
     * @param projectId    项目ID
     * @param simulationId 模拟检查单ID
     * @return 制作完成的检查单
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/draw-construction-support-report/generate",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ReportHistory> generateDrawConstructionSupportReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody @Parameter(description = "生产图纸制造内容") DrawSubPipeSupportDTO drawSubPipeSupportDTO
    );
}
