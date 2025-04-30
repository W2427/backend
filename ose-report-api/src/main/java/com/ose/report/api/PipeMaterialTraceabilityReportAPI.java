package com.ose.report.api;

import com.ose.report.dto.PipeMaterialTraceabilityReportDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "piMmTraceReportFeign")
public interface PipeMaterialTraceabilityReportAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-material-traceability-reports"
    )
    JsonObjectResponseBody<ReportHistory> generatePipeMaterialTraceabilityReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody PipeMaterialTraceabilityReportDTO reportDTO
    );

}
