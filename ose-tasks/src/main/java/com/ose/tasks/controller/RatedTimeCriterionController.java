package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.RatedTimeCriterionAPI;
import com.ose.tasks.domain.model.service.RatedTimeCriterionInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.dto.RatedTimeCriterionCriteriaDTO;
import com.ose.tasks.dto.RatedTimeCriterionDTO;
import com.ose.tasks.entity.RatedTimeCriterion;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Tag(name = "定额工时查询条件查询条件接口")
@RequestMapping("/orgs")
public class RatedTimeCriterionController extends BaseController implements RatedTimeCriterionAPI {

    private RatedTimeCriterionInterface ratedTimeCriterionService;
    private ProcessInterface processService;
    private EntitySubTypeInterface entityCategoryService;

    @Autowired
    public RatedTimeCriterionController(
        RatedTimeCriterionInterface ratedTimeCriterionService,
        ProcessInterface processService,
        EntitySubTypeInterface entityCategoryService
    ) {
        this.ratedTimeCriterionService = ratedTimeCriterionService;
        this.processService = processService;
        this.entityCategoryService = entityCategoryService;
    }

    /**
     * 创建定额工时查询条件信息。
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param ratedTimeCriterionDTO 定额工时查询条件信息
     */
    @Override
    @Operation(
        summary = "创建定额工时查询条件信息"
    )
    @PostMapping(
        value = "/{orgId}/projects/{projectId}/process-stages/{processStageId}/processes/{processId}/entity-sub-types/{entitySubTypeId}/rated-times-criteria",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody create(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "工序阶段ID") Long processStageId,
        @PathVariable @Parameter(description = "工序ID") Long processId,
        @PathVariable @Parameter(description = "实体类型ID") Long entitySubTypeId,
        @RequestBody RatedTimeCriterionDTO ratedTimeCriterionDTO
    ) {

        ContextDTO context = getContext();

        ratedTimeCriterionService.create(
            context.getOperator().getId(),
            orgId,
            projectId,
            processStageId,
            processId,
            entitySubTypeId,
            ratedTimeCriterionDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 获取定额工时查询条件列表。
     *
     * @param orgId                         组织ID
     * @param projectId                     项目ID
     * @param ratedTimeCriterionCriteriaDTO 查询条件
     * @return 定额工时查询条件列表
     */
    @Override
    @Operation(
        summary = "获取定额工时查询条件列表"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/rated-times-criteria",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<RatedTimeCriterion> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        RatedTimeCriterionCriteriaDTO ratedTimeCriterionCriteriaDTO
    ) {

        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            ratedTimeCriterionService.search(
                orgId,
                projectId,
                ratedTimeCriterionCriteriaDTO
            )
        )
            .setIncluded(processService)
            .setIncluded(entityCategoryService);
    }

    /**
     * 更新定额工时查询条件信息。
     *
     * @param orgId                 组织ID
     * @param projectId             项目ID
     * @param ratedTimeCriterionId  定额工时查询条件ID
     * @param ratedTimeCriterionDTO 定额工时查询条件更新信息
     */
    @Override
    @Operation(
        summary = "更新定额工时查询条件信息"
    )
    @PutMapping(
        value = "/{orgId}/projects/{projectId}/rated-times-criteria/{ratedTimeCriterionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时查询条件ID") Long ratedTimeCriterionId,
        @RequestBody RatedTimeCriterionDTO ratedTimeCriterionDTO
    ) {

        ContextDTO context = getContext();

        ratedTimeCriterionService.update(context.getOperator().getId(), orgId, projectId, ratedTimeCriterionId, ratedTimeCriterionDTO);

        return new JsonResponseBody();
    }

    /**
     * 删除定额工时查询条件信息。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriterionId 定额工时查询条件ID
     */
    @Override
    @Operation(
        summary = "删除定额工时查询条件信息"
    )
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/rated-times-criteria/{ratedTimeCriterionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时查询条件ID") Long ratedTimeCriterionId
    ) {

        ContextDTO context = getContext();

        ratedTimeCriterionService.delete(context.getOperator().getId(), orgId, projectId, ratedTimeCriterionId);

        return new JsonResponseBody();
    }

    /**
     * 获取定额工时查询条件信息。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriterionId 定额工时查询条件ID
     */
    @Override
    @Operation(
        summary = "获取定额工时查询条件信息"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/rated-times-criteria/{ratedTimeCriterionId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<RatedTimeCriterion> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时查询条件ID") Long ratedTimeCriterionId
    ) {

        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            ratedTimeCriterionService.get(orgId, projectId, ratedTimeCriterionId)
        );
    }
}
