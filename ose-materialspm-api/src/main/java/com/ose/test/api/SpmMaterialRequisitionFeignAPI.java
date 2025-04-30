package com.ose.test.api;

import com.ose.test.dto.*;
import com.ose.test.entity.ViewMxjBominfoEntity;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 合同查询接口
 */
@FeignClient(name = "ose-test")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/mr")
public interface SpmMaterialRequisitionFeignAPI {

    @RequestMapping(
        method = GET,
        value = "inventory",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<ViewMxjBominfoEntity> getInventory(
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        BominfoListSimpleDTO bominfoListSimpleDTO
    );

    @RequestMapping(
        method = GET,
        value = "fas",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<FaListResultsDTO> getFahList(
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        FaListDTO faListDTO
    );

    @RequestMapping(
        method = POST,
        value = "fads",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<FadListResultsDTO> getFadList(
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @RequestBody FadListDTO fadListDTO
    );

    @RequestMapping(
        method = POST,
        value = "/fah",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<FaListResultsDTO> getFah(
        @PathVariable(value = "projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable(value = "orgId") @Parameter(description = "orgId") Long orgId,
        @RequestBody FadListDTO fadListDTO
    );
}
