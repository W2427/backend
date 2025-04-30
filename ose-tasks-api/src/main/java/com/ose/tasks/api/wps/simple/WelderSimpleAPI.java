package com.ose.tasks.api.wps.simple;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.HierarchyNodeImportDTO;
import com.ose.tasks.dto.wps.simple.*;
import com.ose.tasks.entity.wps.simple.WelderGradeWelderSimplifiedRelation;
import com.ose.tasks.entity.wps.simple.WelderSimplified;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface WelderSimpleAPI {

    /**
     * 创建焊工
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param welderimpleCreateDTO 创建信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/welder-simple",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestBody WelderSimpleCreateDTO welderimpleCreateDTO
    );

    /**
     * 获取焊工列表
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param welderSimpleSearchDTO 查询参数
     * @return wps列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/welder-simple",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WelderSimplified> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WelderSimpleSearchDTO welderSimpleSearchDTO
    );

    /**
     * 获取焊工详情
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param welderSimplifiedId wpsID
     * @return wps详情
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/welder-simple/{welderSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<WelderSimplified> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId
    );

    /**
     * 更新焊工信息。
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param welderSimplifiedId    wpsID
     * @param welderSimpleUpdateDTO 更新信息
     */
    @PatchMapping(
        value = "/{orgId}/projects/{projectId}/welder-simple/{welderSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId,
        @RequestBody WelderSimpleUpdateDTO welderSimpleUpdateDTO
    );

    /**
     * 打开焊工账户
     *
     * @param orgId 组织ID
     * @param projectId 项目ID
     * @param welderId 焊工ID
     * @return
     */
    @PatchMapping(
        value = "{orgId}/projects/{projectId}/welder-simple/{welderId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody open(
        @PathVariable @Parameter(description = "组织Id") Long orgId,
        @PathVariable @Parameter(description = "项目Id") Long projectId,
        @PathVariable @Parameter(description = "焊工Id") Long welderId
    );

    @PatchMapping(
        value = "{orgId}/projects/{projectId}/welder-simple/{welderId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deactivate(
        @PathVariable @Parameter(description = "组织Id") Long orgId,
        @PathVariable @Parameter(description = "项目Id") Long projectId,
        @PathVariable @Parameter(description = "焊工Id") Long welderId
    );

    /**
     * 删除焊工信息。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param welderSimplifiedId wpsID
     */
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/welder-simple/{welderSimplifiedId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId
    );

    /**
     * 创建焊工等级关联信息
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param wpsSimpleRelationDTO 创建信息
     */
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/welder-simple/{welderSimplifiedId}/welderGrade",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody addWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId,
        @RequestBody WpsSimpleRelationDTO wpsSimpleRelationDTO
    );

    /**
     * 获取焊工等级关联信息表
     *
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param wpsSimpleRelationSearchDTO 查询参数
     * @return wps列表
     */
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/welder-simple/{welderSimplifiedId}/welderGrade",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<WelderGradeWelderSimplifiedRelation> searchWelderGrade(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId,
        WpsSimpleRelationSearchDTO wpsSimpleRelationSearchDTO
    );

    /**
     * 删除焊工等级关联信息。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param welderSimplifiedId wpsID
     */
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/welder-simple/{welderSimplifiedId}/welderGrade/{welderGradeRelationSimpleId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody deleteWelderGrade(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "welderSimplifiedId") Long welderSimplifiedId,
        @PathVariable @Parameter(description = "welderGradeRelationSimpleId") Long welderGradeRelationSimpleId
    );

    /**
     * 导入焊工(简单模式）。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/welder-simple/import",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importWelder(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody HierarchyNodeImportDTO welderImportDTO
    );

    /**
     * 按条件下载焊工管理 weld simple 实体列表。
     *
     * @param orgId                 组织 ID
     * @param projectId             项目 ID
     * @param welderSimpleSearchDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/welder-simple/download"
    )
    void downloadWeldSimpleEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        WelderSimpleSearchDTO welderSimpleSearchDTO
    ) throws IOException;
}
