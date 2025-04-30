package com.ose.issues.api;

import com.ose.dto.PageDTO;
import com.ose.issues.dto.*;
import com.ose.issues.entity.Issue;
import com.ose.issues.entity.IssueImportRecord;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

public interface IssueAPI extends IssueFeignAPI {


    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/issues",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<Issue> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO,
        PageDTO pageDTO
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/issues/departments",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<IssueDepartmentDTO> departments(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO
    );


    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/issues/sources",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<IssueSourceDTO> sources(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/issues/all-sources",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<IssueSourceDTO> getAllSources(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/issues/disciplines",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<IssueDisciplineDTO> disciplines(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO
    );

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/issues/systems",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<IssueSystemDTO> systems(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO
    );


    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/issues/modules",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<IssueModuleDTO> modules(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        IssueCriteriaDTO issueCriteriaDTO
    );


    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/issues/export-xls",
        method = GET,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    void export_xls(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        String issueType
    ) throws IOException;

    @RequestMapping(
        value = "/orgs/{orgId}/projects/{projectId}/punchlist/import-record",
        method = GET,
        consumes = ALL_VALUE
    )
    JsonListResponseBody<IssueImportRecord> importHistory(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        PageDTO pageDTO
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/issues/{issueId}/open",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody open(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("issueId") Long issueId
    );
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/issues/{issueId}/close",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody close(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("issueId") Long issueId
    );
}
