package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleCriteriaDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleInsertDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleUpdateDTO;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public interface EntitySubTypeRuleAPI {

    /**
     * 查询 实体类型设置规则。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 条件DTO
     * @param pageDTO     分页用DTO
     * @return 实体类型设置规则列表
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<EntitySubTypeRule> searchEntitySubTypeRules(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        EntitySubTypeRuleCriteriaDTO criteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得实体类型设置规则。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param ruleId    实体 ID
     * @return 实体类型设置规则 详细信息
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules/{ruleId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<EntitySubTypeRule> getEntitySubTypeRule(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("ruleId") Long ruleId
    );

    /**
     * 删除实体类型设置规则。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules/{ruleId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteEntitySubTypeRule(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("ruleId") Long ruleId,
        @RequestParam @Parameter(description = "项目更新版本号") long version
    );

    /**
     * 插入实体类型设置规则。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addEntitySubTypeRule(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody EntitySubTypeRuleInsertDTO entityCategoryRuleInsertDTO
    );

    /**
     * 更新 实体类型设置规则。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     */
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules/{ruleId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody updateEntitySubTypeRule(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("ruleId") Long ruleId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody EntitySubTypeRuleUpdateDTO entityCategoryRuleUpdateDTO
    );


}
