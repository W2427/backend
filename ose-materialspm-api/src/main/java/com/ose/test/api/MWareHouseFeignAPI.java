package com.ose.test.api;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ose.test.entity.MWareHouseEntity;
import com.ose.response.JsonListResponseBody;

import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 查询接口
 */
@FeignClient(name = "ose-test")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface MWareHouseFeignAPI {

    @RequestMapping(
        method = GET,
        value = "/spm-projects/{projId}/m-ware-houses",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<MWareHouseEntity> getMWareHouse(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @PathVariable("projId") @Parameter(description = "projId") String projId
    );

    @RequestMapping(
        method = GET,
        value = "/m-overdose-ware-houses",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<MWareHouseEntity> getOverdoseWareHouses(
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("orgId") @Parameter(description = "orgId") Long orgId,
        @RequestParam("companyId") @Parameter(description = "companyId") Integer companyId
    );

}
