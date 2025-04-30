package com.ose.issues.api;

import com.ose.issues.dto.ExperienceMailDTO;
import com.ose.issues.dto.ExperienceMailListDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@FeignClient(name = "ose-issues", contextId = "experienceFeign")
public interface ExperienceFeignAPI {
    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/experiences/{experienceId}/experience-mail-send",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody mail(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("experienceId") Long experienceId,
        @RequestBody ExperienceMailDTO experienceMailDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/experiences/mail",
        consumes = ALL_VALUE
    )
    JsonListResponseBody<ExperienceMailListDTO> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("size") Integer size,
        @RequestParam("no") Integer no
    );

}
