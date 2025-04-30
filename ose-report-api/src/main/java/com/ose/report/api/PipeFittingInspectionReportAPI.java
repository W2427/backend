package com.ose.report.api;

import com.ose.report.dto.PipeFittingInspectionReportDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "piFittingInspReportFeign")
public interface PipeFittingInspectionReportAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pipe-fitting-inspection-reports"
    )
    JsonObjectResponseBody<ReportHistory> generatePipeFittingInspectionReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody PipeFittingInspectionReportDTO reportDTO
    );

}
