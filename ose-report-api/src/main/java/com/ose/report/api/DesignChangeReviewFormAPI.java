package com.ose.report.api;

import com.ose.report.dto.DesignChangeReviewFormDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-report", contextId = "designChangeReviewFeign")
public interface DesignChangeReviewFormAPI {

    /**
     * 生成设计变更单。
     *
     * @param orgId
     * @param projectId
     * @param reportDTO
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/design-change-review-forms"
    )
    JsonObjectResponseBody<ReportHistory> generateDesignChangeReviewForm(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody DesignChangeReviewFormDTO reportDTO
    );

}
