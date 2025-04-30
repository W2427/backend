package com.ose.issues.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.issues.api.PropertyDefinitionAPI;
import com.ose.issues.domain.model.service.IssueImportInterface;
import com.ose.issues.domain.model.service.PropertyDefinitionInterface;
import com.ose.issues.dto.PropertyDefinitionCreateDTO;
import com.ose.issues.dto.PropertyDefinitionUpdateDTO;
import com.ose.issues.entity.IssuePropertyDefinition;
import com.ose.issues.vo.CustomPropertyType;
import com.ose.issues.vo.IssuePropertyCategory;
import com.ose.issues.vo.IssueType;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import static com.ose.constant.HttpResponseHeaders.DATA_REVISION;
import static org.springframework.http.HttpStatus.CREATED;

@Tag(name = "自定义属性定义")
@RestController
public class PropertyDefinitionController extends BaseController implements PropertyDefinitionAPI {

    private final PropertyDefinitionInterface propertyDefinitionService;
    private final IssueImportInterface issueImportService;

    @Autowired
    public PropertyDefinitionController(
        PropertyDefinitionInterface propertyDefinitionService,
        IssueImportInterface issueImportService
    ) {
        this.propertyDefinitionService = propertyDefinitionService;
        this.issueImportService = issueImportService;
    }

    @Override
    @Operation(description = "创建自定义属性")
    @ResponseStatus(CREATED)
    @WithPrivilege
    public JsonObjectResponseBody<IssuePropertyDefinition> create(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Valid PropertyDefinitionCreateDTO propertyDefinitionDTO
    ) {
        final Long operatorId = getContext().getOperator().getId();

        IssuePropertyDefinition propertyDefinition = propertyDefinitionService
            .create(operatorId, orgId, projectId, propertyDefinitionDTO);

        //新增遗留问题属性分类，导入模板没有变化
//        issueImportService
//            .generateImportTemplate(operatorId, orgId, projectId, propertyDefinitionDTO.getIssueType());

        return new JsonObjectResponseBody<>(propertyDefinition);
    }

    @Override
    @Operation(description = "更新自定义属性")
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "自定义属性定义 ID") Long propertyDefinitionId,
        @RequestBody @Valid PropertyDefinitionUpdateDTO propertyDefinitionDTO
    ) {
        final Long operatorId = getContext().getOperator().getId();

        IssuePropertyDefinition propertyDefinition = propertyDefinitionService
            .update(operatorId, propertyDefinitionId, propertyDefinitionDTO);

        //新增遗留问题属性分类，导入模板没有变化
        issueImportService
            .generateImportTemplate(operatorId, orgId, projectId, propertyDefinitionDTO.getIssueType());

        getResponse().setHeader(DATA_REVISION, "" + propertyDefinition.getVersion());

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "删除自定义属性定义")
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "自定义属性定义 ID") Long propertyDefinitionId
    ) {
        final Long operatorId = getContext().getOperator().getId();

        IssuePropertyDefinition propertyDefinition = propertyDefinitionService
            .delete(operatorId, propertyDefinitionId);

        issueImportService
            .generateImportTemplate(operatorId, orgId, projectId, propertyDefinition.getIssueType());

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "获取自定义属性定义列表")
    @WithPrivilege
    public JsonListResponseBody<IssuePropertyDefinition> search(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam(required = false)
        @Parameter(description = "问题类型")
            IssueType issueType,
        @RequestParam(required = false)
        @Parameter(description = "自定义属性值的类型")
            CustomPropertyType propertyType,
        @RequestParam(required = false)
        @Parameter(description = "自定义遗留问题属性值的分类")
            IssuePropertyCategory propertyCategory,
        @RequestParam(name = "fetchAll", required = false, defaultValue = "false")
        @Parameter(description = "是否取得所有数据（默认：否）")
            Boolean fetchAll,
        @RequestParam(name = "page.no", required = false)
        @Parameter(description = "页号（从 1 开始）")
            Integer pageNo,
        @RequestParam(name = "page.size", required = false)
        @Parameter(description = "分页大小（取值范围：1~100；默认：20）")
            Integer pageSize,
        @RequestParam(name = "sort", required = false)
        @Parameter(description = "排序字段，格式为字段名加升降序，如 <code>id:desc</code>")
            String[] sort
    ) {

        if (fetchAll) {

            return new JsonListResponseBody<>(
                getContext(),
                propertyDefinitionService.search(
                    projectId,
                    issueType,
                    propertyType,
                    propertyCategory
                )
            );

        } else {

            PageDTO pageDTO = new PageDTO(pageNo, pageSize);
            pageDTO.setSort(sort);

            return new JsonListResponseBody<>(
                getContext(),
                propertyDefinitionService.search(
                    projectId,
                    issueType,
                    propertyType,
                    propertyCategory,
                    pageDTO.toPageable()
                )
            );

        }

    }

    @Override
    @Operation(description = "自定义属性定义详情")
    @WithPrivilege
    public JsonObjectResponseBody<IssuePropertyDefinition> details(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "自定义属性定义 ID") Long propertyDefinitionId
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            propertyDefinitionService.details(propertyDefinitionId)
        );
    }

}
