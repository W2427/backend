package com.ose.tasks.api;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.PrintConfigDTO;
import com.ose.tasks.entity.PrintConfig;


@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public interface PrintConfigAPI {

    @RequestMapping(
        method = POST,
        value = "/print-configs"
    )
    @ResponseStatus(CREATED)
    JsonObjectResponseBody<PrintConfig> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody PrintConfigDTO configDTO
    );


    @RequestMapping(
        method = GET,
        value = "/print-configs"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<PrintConfig> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO page
    );


    @RequestMapping(
        method = GET,
        value = "/print-configs/{id}"
    )
    @ResponseStatus(OK)
    JsonObjectResponseBody<PrintConfig> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );


    @RequestMapping(
        method = DELETE,
        value = "/print-configs/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id
    );


    @RequestMapping(
        method = POST,
        value = "/print-configs/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody modify(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("id") Long id,
        @RequestBody PrintConfigDTO configDTO
    );

    @RequestMapping(
        method = GET,
        value = "/print-configs/type/{type}"
    )
    @ResponseStatus(OK)
    JsonListResponseBody<PrintConfig> searchByType(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("type") String type
    );

}
