package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.ExperienceMailDTO;
import com.ose.issues.dto.ExperienceMailListDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * 经验教训。
 */
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public interface ExperienceMailAPI {

    /**
     * 经验教训发送邮件。
     */
    @RequestMapping(
        value = "experiences/{experienceId}/experience-mail-send",
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

    /**
     * 经验教训邮件列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/experiences/mail",
        consumes = ALL_VALUE
    )
    JsonListResponseBody<ExperienceMailListDTO> getList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO
    );

}
