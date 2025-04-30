package com.ose.report.api;

import com.ose.report.dto.StructureMaterialReceiveReportDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "stMmReportFeign")
public interface StructureMaterialReportAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/structure-material-receive-reports"
    )
    JsonObjectResponseBody<ReportHistory> generateStructureMaterialReceiveReport(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody StructureMaterialReceiveReportDTO reportDTO
    );

}
