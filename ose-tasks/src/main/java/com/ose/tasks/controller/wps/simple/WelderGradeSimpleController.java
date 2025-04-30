package com.ose.tasks.controller.wps.simple;
import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.wps.simple.WelderGradeSimpleAPI;
import com.ose.tasks.domain.model.service.wps.simple.WelderGradeSimpleInterface;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.wps.simple.WelderGradeSimplified;
import com.ose.tasks.entity.wps.simple.WelderGradeWpsSimplifiedRelation;
import io.swagger.annotations.Api;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Api(description = "简版焊工等级接口")
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/welder-grade-simple")
public class WelderGradeSimpleController extends BaseController implements WelderGradeSimpleAPI {

    private WelderGradeSimpleInterface welderGradeSimpleService;

    @Autowired
    public WelderGradeSimpleController(
        WelderGradeSimpleInterface welderGradeSimpleService
    ) {
        this.welderGradeSimpleService = welderGradeSimpleService;
    }

    /**
     * 创建焊工等级信息
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param welderGradeSimpleCreateDTO 创建信息
     */
    @Operation(
        summary = "创建焊工等级",
        description = "创建焊工等级"
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
        @RequestBody WelderGradeSimpleCreateDTO welderGradeSimpleCreateDTO
    ) {
        ContextDTO context = getContext();
        welderGradeSimpleService.create(
            orgId,
            projectId,
            context,
            welderGradeSimpleCreateDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 获取焊工等级列表
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param welderGradeSimpleSearchDTO 查询参数
     * @return 焊工等级列表
     */
    @Operation(
        summary = "获取焊工等级列表",
        description = "获取焊工等级列表"
    )
    @GetMapping(
        value = "",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    @SetUserInfo
    public JsonListResponseBody<WelderGradeSimplified> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        WelderGradeSimpleSearchDTO welderGradeSimpleSearchDTO
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            welderGradeSimpleService.search(
                orgId,
                projectId,
                welderGradeSimpleSearchDTO
            )
        );
    }

    /**
     * 获取焊工等级详情
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param welderGradeSimplifiedId     wpsID
     * @return 焊工等级详情
     */
    @Operation(
        summary = "获取焊工等级详情",
        description = "获取焊工等级详情"
    )
    @GetMapping(
        value = "/{welderGradeSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    @SetUserInfo
    public JsonObjectResponseBody<WelderGradeSimplified> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderGradeSimplifiedId") Long welderGradeSimplifiedId
    ) {

        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            welderGradeSimpleService.detail(
                orgId,
                projectId,
                welderGradeSimplifiedId
            )
        );
    }

    /**
     * 更新焊工等级信息。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param welderGradeSimplifiedId        wpsID
     * @param welderGradeSimpleUpdateDTO 更新信息
     */
    @Operation(
        summary = "更新焊工等级信息",
        description = "更新焊工等级信息"
    )
    @PatchMapping(
        value = "/{welderGradeSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderGradeSimplifiedId") Long welderGradeSimplifiedId,
        @RequestBody WelderGradeSimpleUpdateDTO welderGradeSimpleUpdateDTO
    ) {

        ContextDTO context = getContext();

        welderGradeSimpleService.update(
            orgId,
            projectId,
            welderGradeSimplifiedId,
            context,
            welderGradeSimpleUpdateDTO
        );
        return new JsonResponseBody();
    }


    /**
     * 删除焊工等级信息。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param welderGradeSimplifiedId     wpsID
     */
    @Operation(
        summary = "删除焊工等级信息",
        description = "删除焊工等级信息"
    )
    @DeleteMapping(
        value = "/{welderGradeSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsID") Long welderGradeSimplifiedId
    ) {

        ContextDTO context = getContext();
        welderGradeSimpleService.delete(
            orgId,
            projectId,
            welderGradeSimplifiedId,
            context
        );
        return new JsonResponseBody();
    }




    /**
     * 创建WPS
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param welderGradeRelationDTO 创建信息
     */
    @Override
    @Operation(
        summary = "创建WPS",
        description = "创建WPS"
    )
    @PostMapping(
        value = "/{gradeId}/wps",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody addWps(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "gradeId") Long gradeId,
        @RequestBody WelderGradeRelationDTO welderGradeRelationDTO
    ) {
        ContextDTO context = getContext();
        welderGradeSimpleService.addWps(
            orgId,
            projectId,
            gradeId,
            welderGradeRelationDTO,
            context
        );

        return new JsonResponseBody();
    }

    /**
     * 获取WPS列表
     *
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param wpsSimpleRelationSearchDTO 查询参数
     * @return Wps列表
     */
    @Override
    @Operation(
        summary = "获取焊工证列表",
        description = "获取焊工证列表"
    )
    @GetMapping(
        value = "/{gradeId}/wps-simple",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege

    public JsonListResponseBody<WelderGradeWpsSimplifiedRelation> searchWpsSimplify(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "gradeId") Long gradeId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    ) {

        ContextDTO context = getContext();

        return new JsonListResponseBody<>(
            context,
            welderGradeSimpleService.searchWpsSimplify(
                orgId,
                projectId,
                gradeId,
                wpsSimpleRelationSearchDTO
            )
        );
    }

    /**
     * 删除WPS信息。
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param gradeId         焊工等级id
     */
    @Operation(
        summary = "删除WPS信息",
        description = "删除WPS信息"
    )
    @DeleteMapping(
        value = "/{gradeId}/wps-simplify/{wpsId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @Override
    public JsonResponseBody deleteWpsSimplify(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "gradeId") Long gradeId,
        @PathVariable @Parameter(description = "wpsId") Long wpsId
    ) {

        ContextDTO context = getContext();
        welderGradeSimpleService.deleteWps(
            orgId,
            projectId,
            gradeId,
            wpsId,
            context
        );
        return new JsonResponseBody();
    }









































}
