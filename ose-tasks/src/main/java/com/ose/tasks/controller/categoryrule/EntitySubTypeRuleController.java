package com.ose.tasks.controller.categoryrule;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.EntitySubTypeRuleAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.categoryrule.EntitySubTypeRuleService;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleCriteriaDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleInsertDTO;
import com.ose.tasks.dto.subTyperule.EntitySubTypeRuleUpdateDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.subTypeRule.EntitySubTypeRule;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "实体类型设置规则 接口")
@RestController
public class EntitySubTypeRuleController extends BaseController implements EntitySubTypeRuleAPI {


    private ProjectInterface projectService;


    private EntitySubTypeRuleService entityCategoryRuleService;

    /**
     * 构造方法。
     */
    @Autowired
    public EntitySubTypeRuleController(
        ProjectInterface projectService,
        EntitySubTypeRuleService entityCategoryRuleService
    ) {
        this.projectService = projectService;
        this.entityCategoryRuleService = entityCategoryRuleService;
    }


    /**
     * 查询 实体类型设置规则。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 条件DTO
     * @param pageDTO     分页用DTO
     * @return 实体类型设置规则列表
     */
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<EntitySubTypeRule> searchEntitySubTypeRules(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        EntitySubTypeRuleCriteriaDTO criteriaDTO,
        PageDTO pageDTO) {
        return new JsonListResponseBody<>(
            entityCategoryRuleService.search(orgId, projectId, criteriaDTO, pageDTO)
        );
    }

    /**
     * 取得实体类型设置规则。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param ruleId    实体 ID
     * @return 实体类型设置规则 详细信息
     */
    @Override
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules/{ruleId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<EntitySubTypeRule> getEntitySubTypeRule(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体类型设置ID") Long ruleId) {
        return new JsonObjectResponseBody<>(
            entityCategoryRuleService.get(ruleId, orgId, projectId)
        );
    }

    /**
     * 删除实体类型设置规则。
     *
     * @param orgId     组织 ID
     * @param projectId 项目 ID
     * @param ruleId    实体类型设置规则ID
     * @param version   工程版本号
     */
    @Override
    @RequestMapping(
        method = DELETE,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules/{ruleId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody deleteEntitySubTypeRule(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体类型设置ID") Long ruleId,
        @RequestParam @Parameter(description = "项目更新版本号") long version) {

        projectService.get(orgId, projectId, version);

        entityCategoryRuleService.delete(
            getContext().getOperator(),
            orgId,
            projectId,
            ruleId
        );

        return new JsonResponseBody();
    }

    /**
     * 插入实体类型设置规则。
     *
     * @param orgId                       组织 ID
     * @param projectId                   项目 ID
     * @param entityCategoryRuleInsertDTO 实体类型设置规则DTO
     */
    @Override
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addEntitySubTypeRule(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Parameter(description = "管件实体信息") EntitySubTypeRuleInsertDTO entityCategoryRuleInsertDTO) {
        final OperatorDTO operator = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);

        entityCategoryRuleService.insert(operator, project, entityCategoryRuleInsertDTO);
        return new JsonResponseBody(this.getContext());
    }

    /**
     * 更新 实体类型设置规则。
     *
     * @param orgId                       组织 ID
     * @param projectId                   项目 ID
     * @param ruleId                      实体类型设置规则ID
     * @param version                     项目版本号
     * @param entityCategoryRuleUpdateDTO 实体类型设置规则更新用DTO
     */
    @Override
    @RequestMapping(
        method = PATCH,
        value = "/orgs/{orgId}/projects/{projectId}/entity-category-rules/{ruleId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody updateEntitySubTypeRule(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "实体类型设定规则 ID") Long ruleId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody EntitySubTypeRuleUpdateDTO entityCategoryRuleUpdateDTO) {
        final OperatorDTO operator = getContext().getOperator();
        Project project = projectService.get(orgId, projectId, version);

        EntitySubTypeRule entityResult = entityCategoryRuleService.update(operator,
            project,
            ruleId,
            entityCategoryRuleUpdateDTO);
        return new JsonObjectResponseBody(this.getContext(), entityResult);
    }
}
