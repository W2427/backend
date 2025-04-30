package com.ose.report.api;

import com.ose.report.dto.PeLiningInspectionChecklistDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "peLiningInspChklistFeign")
public interface PeLiningInspectionChecklistAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/pe-lining-inspection-checklists"
    )
    JsonObjectResponseBody<ReportHistory> generatePeLiningInspectionChecklist(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody PeLiningInspectionChecklistDTO reportDTO
    );

}
