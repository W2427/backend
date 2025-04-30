package com.ose.issues.api;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.IssueCommentCreateDTO;
import com.ose.issues.dto.IssueCommentUpdateDTO;
import com.ose.issues.entity.IssueComment;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface CommentAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/issues/{issueId}/comments",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("issueId") Long issueId,
        @RequestBody IssueCommentCreateDTO commentCreateDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/issues/{issueId}/comments",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<IssueComment> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("issueId") Long issueId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = PUT,
        value = "/orgs/{orgId}/projects/{projectId}/issues/{issueId}/comments/{commentId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("issueId") Long issueId,
        @PathVariable("commentId") Long commentId,
        @RequestBody IssueCommentUpdateDTO commentUpdateDTO
    );

}
