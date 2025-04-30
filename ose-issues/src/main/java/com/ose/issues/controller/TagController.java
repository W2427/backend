package com.ose.issues.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.issues.api.TagAPI;
import com.ose.issues.domain.model.service.TagInterface;
import com.ose.issues.dto.TagCreateDTO;
import com.ose.issues.dto.TagCriteriaDTO;
import com.ose.issues.entity.Tag;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@io.swagger.v3.oas.annotations.tags.Tag(name = "遗留问题标签")
@RestController
public class TagController extends BaseController implements TagAPI {

    private TagInterface tagService;

    @Autowired
    public TagController(
        TagInterface tagService
    ) {
        this.tagService = tagService;
    }

    @Override
    @Operation(description = "创建项目遗留问题标签")
    @WithPrivilege
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Valid TagCreateDTO tagCreateDTO
    ) {

        tagService.create(
            getContext().getOperator().getId(),
            projectId,
            tagCreateDTO
        );

        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "获取项目遗留问题标签列表")
    @WithPrivilege
    public JsonListResponseBody<Tag> search(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        TagCriteriaDTO tagCriteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            getContext(),
            tagService.search(projectId, pageDTO, tagCriteriaDTO)
        );
    }

    @Override
    @Operation(description = "获取项目遗留问题标签详情")
    @WithPrivilege
    public JsonObjectResponseBody<Tag> get(
        @PathVariable @Parameter(description = "组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "标签 ID") Long tagId
    ) {
        return new JsonObjectResponseBody<>(
            getContext(),
            tagService.get(projectId, tagId)
        );
    }

}
