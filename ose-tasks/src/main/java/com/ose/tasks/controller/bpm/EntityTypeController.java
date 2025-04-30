package com.ose.tasks.controller.bpm;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.EntityTypeAPI;
import com.ose.tasks.domain.model.service.bpm.EntityTypeInterface;
import com.ose.tasks.dto.bpm.EntityTypeDTO;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityType;
import com.ose.tasks.vo.setting.CategoryTypeTag;


import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "实体类型分类接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/entity-types/{type}")
public class EntityTypeController extends BaseController implements EntityTypeAPI {

    /**
     * 工序管理服务
     */
    private final EntityTypeInterface entityCategoryTypeService;

    /**
     * 构造方法
     *
     * @param entityCategoryTypeService 工序管理服务
     */
    @Autowired
    public EntityTypeController(EntityTypeInterface entityCategoryTypeService) {
        this.entityCategoryTypeService = entityCategoryTypeService;
    }


    @Override
    @Operation(
        summary = "创建实体类型分类",
        description = "创建实体类型分类"
    )
    @RequestMapping(
        method = POST,
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<BpmEntityType> create(
        @RequestBody @Parameter(description = "分类信息") EntityTypeDTO typeDTO,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type) {
        return new JsonObjectResponseBody<>(
            getContext(),
            entityCategoryTypeService.create(orgId, projectId, type.name(), typeDTO)
        );
    }

    @Override
    @Operation(
        summary = "查询实体类型分类",
        description = "获取实体类型分类列表。"
    )
    @RequestMapping(
        method = GET
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BpmEntityType> getList(
        PageDTO page,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type) {
        return new JsonListResponseBody<>(
            getContext(),
            entityCategoryTypeService.getList(page, projectId, orgId, type.name())
        );
    }

    @Override
    @Operation(
        summary = "查询可配置实体类型分类",
        description = "查询可配置实体类型分类。"
    )
    @RequestMapping(
        method = GET,
        value = "/fixed-level"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BpmEntityType> getFixedLevelList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type,
        EntityTypeDTO entityTypeDTO) {
        return new JsonListResponseBody<>(
            getContext(),
            entityCategoryTypeService.getFixedLevelList(orgId, projectId, type.name(), entityTypeDTO)
        );
    }

    @Override
    @Operation(
        summary = "获取实体类型分类详细信息",
        description = "根据ID查询实体类型分类详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<BpmEntityType> detail(
        @PathVariable @Parameter(description = "实体类型分类id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type) {
        return new JsonObjectResponseBody<>(
            getContext(),
            entityCategoryTypeService.detail(id, projectId, orgId, type.name())
        );
    }

    /**
     * 通过名称获取实体类型分类
     *
     * @param projectId 项目ID
     * @param orgId     组织ID
     * @param type      READONLY/BUSINESS
     * @param name      实体大类型/实体业务类型的名称
     * @return 实体类型分类对象
     */
    @Operation(
        summary = "根据名称取得实体类型分类",
        description = "根据名称取得实体类型分类"
    )
    @RequestMapping(
        method = GET,
        value = "/name/{name}"
    )
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<BpmEntityType> getCategoryTypeByName(
        @PathVariable("projectId") Long projectId,
        @PathVariable("orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type,
        @PathVariable @Parameter(description = "name") String name) {

        return new JsonObjectResponseBody<>(
            entityCategoryTypeService.getCategoryTypeByName(projectId, orgId, type.name(), name)
        );
    }

    @Override
    @Operation(
        summary = "删除实体类型分类",
        description = "删除指定的实体类型分类"
    )
    @RequestMapping(
        method = DELETE,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "实体类型分类id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type) {

        List<BpmEntitySubType> entityCategories = entityCategoryTypeService.getEntityCategoriesByTypeId(projectId, id);

        if (!entityCategories.isEmpty()) {
            throw new ValidationError("There are entity categories that reference this type");
        }

        entityCategoryTypeService.delete(id, projectId, orgId, type.name());
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "编辑实体类型分类",
        description = "编辑指定的实体类型分类"
    )
    @RequestMapping(
        method = POST,
        value = "/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody modify(
        @PathVariable @Parameter(description = "实体类型分类id") Long id,
        @RequestBody @Parameter(description = "实体类型分类信息") EntityTypeDTO typeDTO,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "type") CategoryTypeTag type) {
        return new JsonObjectResponseBody<>(
            getContext(),
            entityCategoryTypeService.modify(id, typeDTO, projectId, orgId, type.name())
        );
    }

}
