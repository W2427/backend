package com.ose.report.api;

import com.ose.report.dto.DocumentTransmittalRecordDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "docTransRecordFeign")
public interface DocumentTransmittalRecordAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/document-transmittal-records"
    )
    JsonObjectResponseBody<ReportHistory> generateDocumentTransmittalRecord(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DocumentTransmittalRecordDTO reportDTO
    );

}
