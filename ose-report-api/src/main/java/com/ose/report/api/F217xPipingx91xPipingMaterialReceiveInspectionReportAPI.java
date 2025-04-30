package com.ose.report.api;

import com.ose.report.dto.PipingMaterialReceiveReportDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "f217PiMmReceiveFeign")
public interface F217xPipingx91xPipingMaterialReceiveInspectionReportAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/piping-material-receive-reports"
    )
    JsonObjectResponseBody<ReportHistory> generatePipingMaterialReceiveReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody PipingMaterialReceiveReportDTO reportDTO
    );
}
