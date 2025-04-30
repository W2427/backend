package com.ose.report.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.ose.report.entity.ChecklistSimulation;
import com.ose.response.JsonObjectResponseBody;

/**
 * 检查单报表接口
 */
public interface CommonReportAPI {

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
        value = "/orgs/{orgId}/projects/{projectId}/reports/{reportId}/simulations/{simulationId}/generate"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<ChecklistSimulation> simulationGenerate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("reportId") String reportId,
        @PathVariable("simulationId") String simulationId
    );

}
