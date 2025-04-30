package com.ose.tasks.controller.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.ProcessCategoryAPI;
import com.ose.tasks.domain.model.service.bpm.ProcessCategoryInterface;
import com.ose.tasks.dto.bpm.ProcessCategoryDTO;
import com.ose.tasks.entity.bpm.BpmProcessCategory;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "工序分类管理接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class ProcessCategoryController extends BaseController implements ProcessCategoryAPI {

    /**
     * 工序管理服务
     */
    private final ProcessCategoryInterface processCategoryService;

    /**
     * 构造方法
     *
     * @param processCategoryService 工序管理服务
     */
    @Autowired
    public ProcessCategoryController(ProcessCategoryInterface processCategoryService) {
        this.processCategoryService = processCategoryService;
    }

    @Override
    @Operation(
        summary = "创建工序分类",
        description = "创建工序分类。"
    )
    @RequestMapping(
        method = POST,
        value = "/process-categories",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<BpmProcessCategory> create(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody ProcessCategoryDTO categoryDTO) {

        BpmProcessCategory category = processCategoryService.findByName(orgId, projectId, categoryDTO.getNameCn());

        if (category != null)
            throw new ValidationError("Category name conflict.");

        return new JsonObjectResponseBody<>(
            getContext(),
            processCategoryService.create(orgId, projectId, categoryDTO)
        );
    }

    @Override
    @Operation(
        summary = "查询工序分类",
        description = "获取工序分类列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/process-categories"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BpmProcessCategory> getList(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        PageDTO page) {
        return new JsonListResponseBody<>(
            getContext(),
            processCategoryService.getList(orgId, projectId, page)
        );
    }

    @Override
    @Operation(
        summary = "查询工序分类详细信息",
        description = "获取工序分类详细信息。"
    )
    @RequestMapping(
        method = GET,
        value = "/process-categories/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<BpmProcessCategory> getEntitySubType(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonObjectResponseBody<>(
            getContext(),
            processCategoryService.getEntitySubType(orgId, projectId, id)
        );
    }

    @Override
    @Operation(
        summary = "查询工序分类详细信息",
        description = "获取工序分类详细信息。"
    )
    @RequestMapping(
        method = DELETE,
        value = "/process-categories/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        processCategoryService.delete(orgId, projectId, id);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "查询工序分类详细信息",
        description = "获取工序分类详细信息。"
    )
    @RequestMapping(
        method = POST,
        value = "/process-categories/{id}"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody modify(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody ProcessCategoryDTO categoryDTO) {
        return new JsonObjectResponseBody<>(
            getContext(),
            processCategoryService.modify(orgId, projectId, id, categoryDTO)
        );
    }

}
