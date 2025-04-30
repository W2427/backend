package com.ose.tasks.api;

import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.RatedTimeCreateDTO;
import com.ose.tasks.dto.RatedTimeCriteriaDTO;
import com.ose.tasks.dto.RatedTimeImportDTO;
import com.ose.tasks.entity.RatedTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

public interface RatedTimeAPI {

    /**
     * 导入excel。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/projects/{projectId}/import-rated-time",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody importRatedTime(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody RatedTimeImportDTO ratedTimeImportDTO
    );

    /**
     * 创建定额工时信息。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param ratedTimeCreateDTO 定额工时信息
     */
    @Operation(
        summary = "创建定额工时信息"
    )
    @PostMapping(
        value =
            "/{orgId}/projects/{projectId}/process-stages/{processStageId}/processes/{processId}/entity-sub-types/{entitySubTypeId}/rated-times",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "工序阶段ID") Long processStageId,
        @PathVariable @Parameter(description = "工序ID") Long processId,
        @PathVariable @Parameter(description = "实体类型ID") Long entitySubTypeId,
        @RequestBody RatedTimeCreateDTO ratedTimeCreateDTO
    );

    /**
     * 获取定额工时列表。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriteriaDTO 查询条件
     * @return 定额工时列表
     */
    @Operation(
        summary = "获取定额工时列表"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/rated-times",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<RatedTime> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        RatedTimeCriteriaDTO ratedTimeCriteriaDTO
    );

    /**
     * 更新定额工时信息。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param ratedTimeId        定额工时ID
     * @param ratedTimeCreateDTO 定额工时更新信息
     */
    @Operation(
        summary = "更新定额工时信息"
    )
    @PutMapping(
        value = "/{orgId}/projects/{projectId}/rated-times/{ratedTimeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时ID") Long ratedTimeId,
        @RequestBody RatedTimeCreateDTO ratedTimeCreateDTO
    );

    /**
     * 删除定额工时信息。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     */
    @Operation(
        summary = "删除定额工时信息"
    )
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/rated-times/{ratedTimeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时ID") Long ratedTimeId
    );

    /**
     * 获取定额工时信息。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     */
    @Operation(
        summary = "获取定额工时信息"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/rated-times/{ratedTimeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<RatedTime> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时ID") Long ratedTimeId
    );
}
