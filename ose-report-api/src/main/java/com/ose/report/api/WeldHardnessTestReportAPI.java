package com.ose.report.api;

import com.ose.report.dto.WeldHardnessTestReportDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "weldHdReportFeign")
public interface WeldHardnessTestReportAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/weld-hardness-test-reports"
    )
    JsonObjectResponseBody<ReportHistory> generateWeldHardnessTestReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody WeldHardnessTestReportDTO reportDTO
    );

}
