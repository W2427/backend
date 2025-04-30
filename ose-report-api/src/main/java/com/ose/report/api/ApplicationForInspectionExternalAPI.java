package com.ose.report.api;

import com.ose.report.dto.ApplicationForInspectionExternalDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "applExInspFeign")
public interface ApplicationForInspectionExternalAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/application-for-inspection-external"
    )
    JsonObjectResponseBody<ReportHistory> generateApplicationForInspectionExternal(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ApplicationForInspectionExternalDTO reportDTO
    );

}
