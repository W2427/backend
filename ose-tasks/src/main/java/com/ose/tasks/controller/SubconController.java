package com.ose.tasks.controller;
import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.SubconAPI;
import com.ose.tasks.domain.model.service.SubconInterface;
import com.ose.tasks.dto.SubconDTO;
import com.ose.tasks.entity.Subcon;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Api(description = "分包商接口")
@RequestMapping(value = "/orgs")
public class SubconController extends BaseController implements SubconAPI {

    @Autowired
    SubconInterface subconService;

    /**
     * 创建分包商信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param subconDTO 分包商信息
     */
    @Operation(
        summary = "创建分包商信息",
        description = "创建分包商信息"
    )
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/subcons",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody SubconDTO subconDTO
    ) {

        ContextDTO context = getContext();

        subconService.create(
            context.getOperator().getId(),
            orgId,
            projectId,
            subconDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 获取分包商列表。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 分包商列表
     */
    @Operation(
        summary = "获取分包商列表（全部信息）",
        description = "获取分包商列表（全部信息）"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/all-subcons",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonListResponseBody<Subcon> list(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId
    ) {

        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            subconService.list(
                orgId,
                projectId
            )
        );
    }

    /**
     * 获取分包商分页列表（分页）。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageDTO   分页参数
     * @return 分包商列表
     */
    @Operation(
        summary = "获取分包商列表（分页）",
        description = "获取分包商列表（分页）"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/subcons",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<Subcon> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        PageDTO pageDTO
    ) {

        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            subconService.search(
                orgId,
                projectId,
                pageDTO
            )
        );
    }

    /**
     * 获取分包商详情。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param subconId  分包商ID
     * @return 分包商详情
     */
    @Operation(
        summary = "获取分包商详情",
        description = "获取分包商详情"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/subcons/{subconId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    @Override
    public JsonObjectResponseBody<Subcon> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "分包商ID") Long subconId
    ) {

        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            subconService.get(
                orgId,
                projectId,
                subconId
            )
        );
    }

    /**
     * 删除分包商信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param subconId  分包商ID
     */
    @Operation(
        summary = "删除分包商信息",
        description = "删除分包商信息"
    )
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/subcons/{subconId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "分包商ID") Long subconId
    ) {

        ContextDTO context = getContext();

        subconService.delete(
            context.getOperator().getId(),
            orgId,
            projectId,
            subconId
        );
        return new JsonResponseBody();
    }


    /**
     * 更新分包商信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param subconId  分包商ID
     * @param subconDTO 更新信息
     */
    @Operation(
        summary = "更新分包商信息",
        description = "更新分包商信息"
    )
    @PatchMapping(
        value = "/{orgId}/projects/{projectId}/subcons/{subconId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "分包商ID") Long subconId,
        @RequestBody SubconDTO subconDTO
    ) {

        ContextDTO context = getContext();

        subconService.update(
            context.getOperator().getId(),
            orgId,
            projectId,
            subconId,
            subconDTO
        );
        return new JsonResponseBody();
    }



    /**
     * 获取工作班组。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 工作班组列表
     */
    @Override
    @Operation(
        summary = "获取工作班组",
        description = "获取工作班组"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/teamName",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<BpmActivityInstanceState> searchTeamName(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId
    ) {
        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            subconService.teamNameList(
                orgId,
                projectId
            )
        );
    }

    /**
     * 获取工作场地。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return 工作场地列表
     */
    @Override
    @Operation(
        summary = "获取工作场地",
        description = "获取工作场地"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/workSite",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<BpmActivityInstanceState> searchWorkSiteName(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId
    ) {

        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            subconService.workSiteNameList(
                orgId,
                projectId
            )
        );
    }


}
