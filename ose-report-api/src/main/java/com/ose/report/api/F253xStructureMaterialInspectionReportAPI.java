package com.ose.report.api;

import com.ose.report.dto.F253StructureMaterialInspectionReportDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "f253StMmInspFeign")
public interface F253xStructureMaterialInspectionReportAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/structure-material-inspection-reports"
    )
    JsonObjectResponseBody<ReportHistory> generateStructureMaterialInspectionReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody F253StructureMaterialInspectionReportDTO reportDTO
    );
}
