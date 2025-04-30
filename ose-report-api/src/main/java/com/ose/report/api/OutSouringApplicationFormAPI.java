package com.ose.report.api;

import com.ose.report.dto.OutSouringApplicationFormDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "outSourcingApplFormFeign")
public interface OutSouringApplicationFormAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/out-souring-application-forms"
    )
    JsonObjectResponseBody<ReportHistory> generateOutSouringApplicationForm(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody OutSouringApplicationFormDTO reportDTO
    );

}
