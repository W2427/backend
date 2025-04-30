package com.ose.tasks.api.wps.simple;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.wps.simple.WelderGradeWpsSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WpsSimplified;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface WpsSimpleAPI {

    /**
     * 创建Wps信息
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param wpsSimpleCreateDTO 创建信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/wps-simple",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody WpsSimpleCreateDTO wpsSimpleCreateDTO
    );

    /**
     * 获取wps列表
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param wpsSimpleSearchDTO 查询参数
     * @return wps列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/wps-simple",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WpsSimplified> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WpsSimpleSearchDTO wpsSimpleSearchDTO
    );

    /**
     * 获取wps详情
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param wpsSimplifiedId     wpsID
     * @return wps详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/wps-simple/{wpsSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WpsSimplified> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId
    );

    /**
     * 更新Wps信息。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param wpsSimplifiedId        wpsID
     * @param wpsSimpleUpdateDTO 更新信息
     */
    @PatchMapping(
        value = "/{orgId}/projects/{projectId}/wps-simple/{wpsSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId,
        @RequestBody WpsSimpleUpdateDTO wpsSimpleUpdateDTO
    );

    /**
     * 更新Wps信息。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param wpsSimplifiedId        wpsID
     */
    @DeleteMapping (
        value = "/{orgId}/projects/{projectId}/wps-simple/{wpsSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId
    );

    /**
     * 创建焊工证
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param wpsSimpleRelationDTO 创建信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/wps-simple/{wpsSimplifiedId}/welderGrade",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId,
        @RequestBody WpsSimpleRelationDTO wpsSimpleRelationDTO
    );
    /**
     * 获取焊工证列表
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param wpsSimpleRelationSearchDTO 查询参数
     * @return wps列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/wps-simple/{wpsSimplifiedId}/welderGrade",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WelderGradeWpsSimplifiedRelation> searchWelderGrade(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    );

    /**
     * 删除焊工证。
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param wpsSimplifiedId        wpsID
     */
    @DeleteMapping (
        value = "/{orgId}/projects/{projectId}/wps-simple/{wpsSimplifiedId}/welderGrade/{wpsGradeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "wpsSimplifiedId") Long wpsSimplifiedId,
        @PathVariable @Parameter(description = "wpsGradeId") Long wpsGradeId
    );

    /**
     * 获取焊口下所有Wps列表
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param wpsSimpleRelationDTO 查询参数
     * @return wps列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/wps-simple/weld",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WpsSimplified> searchWeldWps(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WpsSimpleRelationDTO wpsSimpleRelationDTO
    );

}
