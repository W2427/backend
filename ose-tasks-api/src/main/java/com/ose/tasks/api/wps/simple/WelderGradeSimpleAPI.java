package com.ose.tasks.api.wps.simple;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.wps.simple.WelderGradeSimplified;
import com.ose.tasks.entity.wps.simple.WelderGradeWpsSimplifiedRelation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface WelderGradeSimpleAPI {

    /**
     * 创建焊工等级信息
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param welderGradeSimpleCreateDTO 创建信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/welder-grade-simple",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody WelderGradeSimpleCreateDTO welderGradeSimpleCreateDTO
    );

    /**
     * 获取焊工等级列表
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param welderGradeSimpleSearchDTO 查询参数
     * @return wps列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/welder-grade-simple",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WelderGradeSimplified> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WelderGradeSimpleSearchDTO welderGradeSimpleSearchDTO
    );

    /**
     * 获取焊工等级详情详情
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param welderGradeSimplifiedId     wpsID
     * @return wps详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/welder-grade-simple/{welderGradeSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WelderGradeSimplified> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsID") Long welderGradeSimplifiedId
    );

    /**
     * 更新焊工等级信息。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param welderGradeSimplifiedId        wpsID
     * @param welderGradeSimpleCreateDTO 更新信息
     */
    @PatchMapping(
        value = "/{orgId}/projects/{projectId}/welder-grade-simple/{welderGradeSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderGradeSimplifiedId") Long welderGradeSimplifiedId,
        @RequestBody WelderGradeSimpleUpdateDTO welderGradeSimpleCreateDTO
    );

    /**
     * 删除焊工等级信息。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param welderGradeSimplifiedId        wpsID
     */
    @DeleteMapping (
        value = "/{orgId}/projects/{projectId}/welder-grade-simple/{welderGradeSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderGradeSimplifiedId") Long welderGradeSimplifiedId
    );




    /**
     * 删除WPS。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param gradeId      焊工等级id
     */
    @DeleteMapping (
        value = "/{orgId}/projects/{projectId}/welder-grade-simple/{gradeId}/wps-simplify/{wpsId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteWpsSimplify(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "gradeId") Long gradeId,
        @PathVariable @Parameter(description = "wpsId") Long wpsId
    );


    /**
     * 获取WPS列表
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param wpsSimpleRelationSearchDTO 查询参数
     * @return wps列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/welder-grade-simple/{gradeId}/wps-simplify",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WelderGradeWpsSimplifiedRelation> searchWpsSimplify(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable @Parameter(description = "gradeId") Long gradeId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    );

    /**
     * 创建WPS
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param welderGradeRelationDTO 创建信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/welder-grade-simple/{gradeId}/wps",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addWps(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long gradeId,
        @RequestBody WelderGradeRelationDTO welderGradeRelationDTO
    );


//    /**
//     * 获取焊口下所有Wps列表
//     *
//     * @param orgId          组织ID
//     * @param projectId      项目ID
//     * @param welderGradeRelationDTO 查询参数
//     * @return wps列表
//     */
//    @GetMapping(
//        value = "/{orgId}/projects/{projectId}/welder-grade/wps",
//        consumes = ALL_VALUE,
//        produces = APPLICATION_JSON_VALUE
//    )
//    JsonListResponseBody<WpsSimplified> searchWelderGrade(
//        @PathVariable("orgId") Long orgId,
//        @PathVariable("projectId") Long projectId,
//        WelderGradeRelationDTO welderGradeRelationDTO
//    );


}
