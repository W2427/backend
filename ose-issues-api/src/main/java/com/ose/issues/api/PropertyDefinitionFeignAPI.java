package com.ose.issues.api;

import com.ose.issues.dto.PropertyDefinitionCreateDTO;
import com.ose.issues.dto.PropertyDefinitionUpdateDTO;
import com.ose.issues.entity.IssuePropertyDefinition;
import com.ose.issues.vo.CustomPropertyType;
import com.ose.issues.vo.IssuePropertyCategory;
import com.ose.issues.vo.IssueType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@FeignClient(name = "ose-issues", contextId = "propertyDefinitionFeign")
public interface PropertyDefinitionFeignAPI {

    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/issue-properties",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<IssuePropertyDefinition> create(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody PropertyDefinitionCreateDTO propertyDefinitionDTO
    );

    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/issue-properties/{propertyDefinitionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("propertyDefinitionId") Long propertyDefinitionId,
        @RequestBody PropertyDefinitionUpdateDTO propertyKeyUpdateDTO
    );

    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/issue-properties/{propertyDefinitionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("propertyDefinitionId") Long propertyDefinitionId
    );

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/issue-properties",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<IssuePropertyDefinition> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestParam("issueType") IssueType issueType,
        @RequestParam("propertyType") CustomPropertyType propertyType,
        @RequestParam("propertyCategory") IssuePropertyCategory propertyCategory,
        @RequestParam("fetchAll") Boolean fetchAll,
        @RequestParam("page.no") Integer pageNo,
        @RequestParam("page.size") Integer pageSize,
        @RequestParam("sort") String[] sort
    );

    /**
     * 查询所有自定义属性。
     *
     * @param orgId        组织 ID
     * @param projectId    项目 ID
     * @param issueType    问题类型
     * @param propertyType 属性类型
     * @return 查询自定义属性
     */
    default JsonListResponseBody<IssuePropertyDefinition> search(
        Long orgId,
        Long projectId,
        IssueType issueType,
        CustomPropertyType propertyType
    ) {
        return search(orgId, projectId, issueType, propertyType, null, true, null, null, null);
    }

    /**
     * 查询所有自定义属性。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param issueType 问题类型
     * @return 查询自定义属性
     */
    default JsonListResponseBody<IssuePropertyDefinition> search(
        Long orgId,
        Long projectId,
        IssueType issueType
    ) {
        return search(orgId, projectId, issueType, null, null, true, null, null, null);
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/issue-properties/{propertyDefinitionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<IssuePropertyDefinition> details(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("propertyDefinitionId") Long propertyDefinitionId
    );

}
