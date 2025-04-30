package com.ose.test.api;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ose.test.entity.MSiteMatlStatusEntity;
import com.ose.response.JsonListResponseBody;

import io.swagger.v3.oas.annotations.Parameter;

/**
 * 查询接口
 */
@FeignClient(name = "ose-test")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/spm-projects/{projId}")
public interface MSiteMatlStatusFeignAPI {

    @RequestMapping(
        method = GET,
        value = "m-site-matl-status",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<MSiteMatlStatusEntity> getMSiteMatlStatus(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId
    );

}
