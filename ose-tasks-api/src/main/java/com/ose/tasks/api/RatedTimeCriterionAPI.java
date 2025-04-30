package com.ose.tasks.api;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.RatedTimeCriterionCriteriaDTO;
import com.ose.tasks.dto.RatedTimeCriterionDTO;
import com.ose.tasks.entity.RatedTimeCriterion;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface RatedTimeCriterionAPI {

    /**
     * 创建定额工时查询条件信息。
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param ratedTimeCriterionDTO 定额工时查询条件信息
     */
    @Operation(
        summary = "创建定额工时查询条件信息"
    )
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/process-stages/{processStageId}/processes/{processId}/entity-sub-types/{entitySubTypeId}/rated-times-criteria",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "工序阶段ID") Long processStageId,
        @PathVariable @Parameter(description = "工序ID") Long processId,
        @PathVariable @Parameter(description = "实体类型ID") Long entitySubTypeId,
        @RequestBody RatedTimeCriterionDTO ratedTimeCriterionDTO
    );

    /**
     * 获取定额工时查询条件列表。
     *
     * @param orgId                         组织ID
     * @param projectId                     项目ID
     * @param ratedTimeCriterionCriteriaDTO 查询条件
     * @return 定额工时查询条件列表
     */
    @Operation(
        summary = "获取定额工时查询条件列表"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/rated-times-criteria",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<RatedTimeCriterion> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        RatedTimeCriterionCriteriaDTO ratedTimeCriterionCriteriaDTO
    );

    /**
     * 更新定额工时查询条件信息。
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param ratedTimeCriterionId  定额工时查询条件ID
     * @param ratedTimeCriterionDTO 定额工时查询条件更新信息
     */
    @Operation(
        summary = "更新定额工时查询条件信息"
    )
    @PutMapping(
        value = "/{orgId}/projects/{projectId}/rated-times-criteria/{ratedTimeCriterionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时查询条件ID") Long ratedTimeCriterionId,
        @RequestBody RatedTimeCriterionDTO ratedTimeCriterionDTO
    );

    /**
     * 删除定额工时查询条件信息。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriterionId 定额工时查询条件ID
     */
    @Operation(
        summary = "删除定额工时查询条件信息"
    )
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/rated-times-criteria/{ratedTimeCriterionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时查询条件ID") Long ratedTimeCriterionId
    );

    /**
     * 获取定额工时查询条件信息。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriterionId 定额工时查询条件ID
     */
    @Operation(
        summary = "获取定额工时查询条件信息"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/rated-times-criteria/{ratedTimeCriterionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<RatedTimeCriterion> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时查询条件ID") Long ratedTimeCriterionId
    );
}
