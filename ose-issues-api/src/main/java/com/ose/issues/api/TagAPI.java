package com.ose.issues.api;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.TagCreateDTO;
import com.ose.issues.dto.TagCriteriaDTO;
import com.ose.issues.entity.Tag;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface TagAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/issue_tags",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody TagCreateDTO tagCreateDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/issue_tags",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Tag> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        TagCriteriaDTO tagCriteriaDTO,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/issue_tags/{tagId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<Tag> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("tagId") Long tagId
    );

}
