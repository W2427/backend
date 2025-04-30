package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.WorkSiteAPI;
import com.ose.tasks.domain.model.service.worksite.WorkSiteInterface;
import com.ose.tasks.dto.worksite.WorkSiteMoveDTO;
import com.ose.tasks.dto.worksite.WorkSitePatchDTO;
import com.ose.tasks.dto.worksite.WorkSitePostDTO;
import com.ose.tasks.entity.worksite.WorkSite;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@Tag(name = "工作场地管理接口")
@RestController
public class WorkSiteController extends BaseController implements WorkSiteAPI {


    private final WorkSiteInterface workSiteService;

    /**
     * 构造方法。
     */
    @Autowired
    public WorkSiteController(
        WorkSiteInterface workSiteService
    ) {
        this.workSiteService = workSiteService;
    }

    @Operation(description = "查询工作场地")
    @Override
    @SetUserInfo
    public JsonListResponseBody<WorkSite> search(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @RequestParam(required = false) @Parameter(description = "项目 ID") Long projectId,
        PageDTO pageDTO
    ) {
        return search(companyId, null, projectId, pageDTO);
    }

    @Operation(description = "查询工作场地")
    @Override
    @SetUserInfo
    public JsonListResponseBody<WorkSite> search(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "上级 ID") Long parentId,
        @RequestParam(required = false) @Parameter(description = "项目 ID") Long projectId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            workSiteService.search(companyId, parentId, projectId, pageDTO.toPageable())
        );
    }

    @Operation(description = "取得工作场地详细信息")
    @Override
    @SetUserInfo
    public JsonObjectResponseBody<WorkSite> get(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "工作场地 ID") Long workSiteId
    ) {
        return new JsonObjectResponseBody<>(
            workSiteService.get(companyId, null, workSiteId)
        );
    }

    @Operation(description = "创建工作场地")
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<WorkSite> create(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @RequestBody @Valid WorkSitePostDTO workSiteDTO
    ) {
        return create(companyId, null, workSiteDTO);
    }

    @Operation(description = "创建工作场地（指定上级）")
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<WorkSite> create(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "上级 ID") Long parentId,
        @RequestBody @Valid WorkSitePostDTO workSiteDTO
    ) {
        return createForProject(companyId, null, parentId, workSiteDTO);
    }

    @Operation(description = "创建项目的工作场地")
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<WorkSite> createForProject(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody @Valid WorkSitePostDTO workSiteDTO
    ) {
        return createForProject(companyId, projectId, null, workSiteDTO);
    }

    @Operation(description = "创建项目的工作场地（指定上级）")
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<WorkSite> createForProject(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "上级 ID") Long parentId,
        @RequestBody @Valid WorkSitePostDTO workSiteDTO
    ) {
        return new JsonObjectResponseBody<>(
            workSiteService.create(
                getContext().getOperator(),
                companyId,
                projectId,
                parentId,
                workSiteDTO
            )
        );
    }

    @Operation(description = "更新工作场地信息")
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<WorkSite> update(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "工作场地 ID") Long workSiteId,
        @RequestParam @Parameter(description = "更新版本号") long version,
        @RequestBody WorkSitePatchDTO workSiteDTO
    ) {
        return update(companyId, null, workSiteId, version, workSiteDTO);
    }

    @Operation(description = "更新工作场地信息")
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<WorkSite> update(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "工作场地 ID") Long workSiteId,
        @RequestParam @Parameter(description = "更新版本号") long version,
        @RequestBody WorkSitePatchDTO workSiteDTO
    ) {
        return new JsonObjectResponseBody<>(
            workSiteService.update(
                getContext().getOperator(),
                companyId,
                projectId,
                workSiteId,
                version,
                workSiteDTO
            )
        );
    }

    @Operation(description = "移动工作场地")
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<WorkSite> move(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "工作场地 ID") Long workSiteId,
        @RequestParam @Parameter(description = "更新版本号") long version,
        @RequestBody @Valid WorkSiteMoveDTO workSiteDTO
    ) {
        return move(companyId, null, workSiteId, version, workSiteDTO);
    }

    @Operation(description = "移动工作场地")
    @Override
    @WithPrivilege
    public JsonObjectResponseBody<WorkSite> move(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "工作场地 ID") Long workSiteId,
        @RequestParam @Parameter(description = "更新版本号") long version,
        @RequestBody @Valid WorkSiteMoveDTO workSiteDTO
    ) {
        return new JsonObjectResponseBody<>(
            workSiteService.move(
                getContext().getOperator(),
                companyId,
                projectId,
                workSiteId,
                version,
                workSiteDTO
            )
        );
    }

    @Operation(description = "删除工作场地信息")
    @Override
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "工作场地 ID") Long workSiteId,
        @RequestParam @Parameter(description = "更新版本号") long version
    ) {
        return delete(companyId, null, workSiteId, version);
    }

    @Operation(description = "删除工作场地信息")
    @Override
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "公司 ID") Long companyId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "工作场地 ID") Long workSiteId,
        @RequestParam @Parameter(description = "更新版本号") long version
    ) {

        workSiteService.delete(
            getContext().getOperator(),
            companyId,
            projectId,
            workSiteId,
            version
        );

        return new JsonResponseBody();
    }

}
