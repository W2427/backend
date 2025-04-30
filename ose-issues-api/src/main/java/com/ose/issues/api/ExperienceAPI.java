package com.ose.issues.api;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.*;
import com.ose.issues.entity.Experience;
import com.ose.issues.entity.IssueImportTemplate;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;


public interface ExperienceAPI extends ExperienceFeignAPI {

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/experiences",
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Experience> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ExperienceCreateDTO experienceCreateDTO
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/experiences/{experienceId}",
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("experienceId") Long experienceId,
        @RequestBody ExperienceUpdateDTO experienceUpdateDTO
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/experiences/{experienceId}",
        method = DELETE,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("experienceId") Long experienceId
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/experiences",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Experience> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        IssueCriteriaDTO experienceCriteriaDTO,
        PageDTO pageDTO
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/experiences/{experienceId}",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Experience> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("experienceId") Long experienceId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/experience-import-file",
        consumes = ALL_VALUE
    )
    JsonObjectResponseBody<IssueImportTemplate> getImportFile(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-experiences",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importExperiences(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody IssueImportDTO issueImportDTO
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/experiences/{experienceId}/operate",
        method = PATCH,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody operate(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("experienceId") Long experienceId,
        @RequestBody ExperienceStatusDTO experienceStatusDTO
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/experiences/{experienceParentId}/list",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Experience> getExperienceList(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("experienceParentId") Long experienceParentId
    );

}
