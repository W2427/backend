package com.ose.report.api;

import com.ose.report.dto.FinalInspectionRecordForPipingPreFabricationDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "piFinalInspFeign")
public interface FinalInspectionRecordForPipingPreFabricationAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/final-inspection-record-for-piping-pre-fabrication"
    )
    JsonObjectResponseBody<ReportHistory> generateFinalInspectionRecordForPipingPreFabrication(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody FinalInspectionRecordForPipingPreFabricationDTO reportDTO
    );

}
