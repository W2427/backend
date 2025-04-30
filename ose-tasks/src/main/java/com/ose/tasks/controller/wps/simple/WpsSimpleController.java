package com.ose.tasks.controller.wps.simple;
import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.wps.simple.WpsSimpleAPI;
import com.ose.tasks.domain.model.service.wps.simple.WpsSimpleInterface;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.wps.simple.WelderGradeWpsSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WpsSimplified;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Api(description = "简版WPS接口")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/wps-simple")
public class WpsSimpleController extends BaseController implements WpsSimpleAPI {

    private WpsSimpleInterface wpsSimpleService;

    @Autowired
    public WpsSimpleController(
        WpsSimpleInterface wpsSimpleService
    ) {
        this.wpsSimpleService = wpsSimpleService;
    }

    /**
     * 创建Wps
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param wpsSimpleCreateDTO 创建信息
     */
    @Operation(
        summary = "创建Wps",
        description = "创建Wps"
    )
    @PostMapping(
        value = "",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody WpsSimpleCreateDTO wpsSimpleCreateDTO
    ) {
        ContextDTO context = getContext();
        wpsSimpleService.create(
            orgId,
            projectId,
            context,
            wpsSimpleCreateDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 获取Wps列表
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param wpsSimpleSearchDTO 查询参数
     * @return Wps列表
     */
    @Operation(
        summary = "获取Wps列表",
        description = "获取Wps列表"
    )
    @GetMapping(
        value = "",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    @SetUserInfo
    public JsonListResponseBody<WpsSimplified> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        WpsSimpleSearchDTO wpsSimpleSearchDTO
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            wpsSimpleService.search(
                orgId,
                projectId,
                wpsSimpleSearchDTO
            )
        );
    }

    /**
     * 获取wps详情
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param wpsSimplifiedId wpsID
     * @return Wps详情
     */
    @Operation(
        summary = "获取Wps详情",
        description = "获取Wps详情"
    )
    @GetMapping(
        value = "/{wpsSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    @SetUserInfo
    public JsonObjectResponseBody<WpsSimplified> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId
    ) {

        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            wpsSimpleService.detail(
                orgId,
                projectId,
                wpsSimplifiedId
            )
        );
    }

    /**
     * 更新Wps信息。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param wpsSimplifiedId    wpsID
     * @param wpsSimpleUpdateDTO 更新信息
     */
    @Operation(
        summary = "更新Wps信息",
        description = "更新Wps信息"
    )
    @PatchMapping(
        value = "/{wpsSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId,
        @RequestBody WpsSimpleUpdateDTO wpsSimpleUpdateDTO
    ) {

        ContextDTO context = getContext();

        wpsSimpleService.update(
            orgId,
            projectId,
            wpsSimplifiedId,
            context,
            wpsSimpleUpdateDTO
        );
        return new JsonResponseBody();
    }


    /**
     * 删除Wps信息。
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param wpsSimplifiedId wpsID
     */
    @Operation(
        summary = "删除Wps信息",
        description = "删除Wps信息"
    )
    @DeleteMapping(
        value = "/{wpsSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId
    ) {

        ContextDTO context = getContext();
        wpsSimpleService.delete(
            orgId,
            projectId,
            wpsSimplifiedId,
            context
        );
        return new JsonResponseBody();
    }

    /**
     * 创建焊工证
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param wpsSimpleRelationDTO 创建信息
     */
    @Operation(
        summary = "创建焊工证",
        description = "创建焊工证"
    )
    @PostMapping(
        value = "/{wpsSimplifiedId}/welderGrade",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege

    public JsonResponseBody addWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId,
        @RequestBody WpsSimpleRelationDTO wpsSimpleRelationDTO
    ) {
        ContextDTO context = getContext();
        wpsSimpleService.addWelderGrade(
            orgId,
            projectId,
            wpsSimplifiedId,
            wpsSimpleRelationDTO,
            context
        );

        return new JsonResponseBody();
    }

    /**
     * 获取焊工证列表
     *
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param wpsSimpleRelationSearchDTO 查询参数
     * @return Wps列表
     */
    @Operation(
        summary = "获取焊工证列表",
        description = "获取焊工证列表"
    )
    @GetMapping(
        value = "/{wpsSimplifiedId}/welderGrade",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege

    public JsonListResponseBody<WelderGradeWpsSimplifiedRelation> searchWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            wpsSimpleService.searchWelderGrade(
                orgId,
                projectId,
                wpsSimplifiedId,
                wpsSimpleRelationSearchDTO
            )
        );
    }

    /**
     * 删除焊工证信息。
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param wpsSimplifiedId wpsID
     */
    @Operation(
        summary = "删除焊工证信息",
        description = "删除焊工证信息"
    )
    @DeleteMapping(
        value = "/{wpsSimplifiedId}/welderGrade/{wpsGradeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody deleteWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId,
        @PathVariable @Parameter(description = "wpsGradeId") Long wpsGradeId
    ) {

        ContextDTO context = getContext();
        wpsSimpleService.deleteWelderGrade(
            orgId,
            projectId,
            wpsSimplifiedId,
            wpsGradeId,
            context
        );
        return new JsonResponseBody();
    }

    /**
     * 获取焊口下所有Wps列表
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param wpsSimpleRelationDTO 查询参数
     * @return Wps列表
     */
    @Operation(
        summary = "获取焊口下所有Wps列表",
        description = "获取焊口下所有Wps列表"
    )
    @GetMapping(
        value = "/weld",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    @SetUserInfo
    public JsonListResponseBody<WpsSimplified> searchWeldWps(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        WpsSimpleRelationDTO wpsSimpleRelationDTO
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            wpsSimpleService.searchWeldWps(
                orgId,
                projectId,
                wpsSimpleRelationDTO
            )
        );
    }

}
