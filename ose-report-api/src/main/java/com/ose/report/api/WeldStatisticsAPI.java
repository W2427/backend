package com.ose.report.api;

import com.ose.response.JsonResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 检查单报表接口
 */
@FeignClient(name = "ose-report", contextId = "weldStatisticsFeign")
public interface WeldStatisticsAPI {

    /**
     *
     *
     * @param orgId         组织 ID
     * @param projectId     项目 ID
     * @return
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/weld-statistics",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(CREATED)
    JsonResponseBody postWeldStatistics(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );
}
