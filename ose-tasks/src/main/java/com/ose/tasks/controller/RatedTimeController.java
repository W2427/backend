package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.RatedTimeAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.RatedTimeInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.dto.RatedTimeCreateDTO;
import com.ose.tasks.dto.RatedTimeCriteriaDTO;
import com.ose.tasks.dto.RatedTimeImportDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.RatedTime;
import com.ose.tasks.entity.RatedTimeImportRecord;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@Tag(name = "定额工时接口")
@RequestMapping("/orgs")
public class RatedTimeController extends BaseController implements RatedTimeAPI {

    private RatedTimeInterface ratedTimeService;
    private ProcessInterface processService;
    private EntitySubTypeInterface entityCategoryService;


    private ProjectInterface projectService;

    @Autowired
    public RatedTimeController(
        RatedTimeInterface ratedTimeService,
        ProjectInterface projectService,
        ProcessInterface processService,
        EntitySubTypeInterface entityCategoryService
    ) {
        this.ratedTimeService = ratedTimeService;
        this.processService = processService;
        this.entityCategoryService = entityCategoryService;
        this.projectService = projectService;
    }

    /**
     * 创建定额工时信息。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param ratedTimeCreateDTO 定额工时信息
     */
    @Override
    @Operation(
        summary = "创建定额工时信息"
    )
    @PostMapping(
        value =
            "/{orgId}/projects/{projectId}/process-stages/{processStageId}/processes/{processId}/entity-sub-types/{entitySubTypeId}/rated-times",
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
        @RequestBody RatedTimeCreateDTO ratedTimeCreateDTO
    ) {

        ContextDTO context = getContext();

        ratedTimeService.create(
            context.getOperator().getId(),
            orgId,
            projectId,
            processStageId,
            processId,
            entitySubTypeId,
            ratedTimeCreateDTO
        );

        return new JsonResponseBody();
    }

    /**
     * 获取定额工时列表。
     *
     * @param orgId                组织ID
     * @param projectId            项目ID
     * @param ratedTimeCriteriaDTO 查询条件
     * @return 定额工时列表
     */
    @Override
    @Operation(
        summary = "获取定额工时列表"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/rated-times",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<RatedTime> search(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        RatedTimeCriteriaDTO ratedTimeCriteriaDTO
    ) {

        ContextDTO context = getContext();
        return new JsonListResponseBody<>(
            context,
            ratedTimeService.search(
                orgId,
                projectId,
                ratedTimeCriteriaDTO
            )
        )
            .setIncluded(processService)
            .setIncluded(entityCategoryService);
    }

    /**
     * 更新定额工时信息。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param ratedTimeId        定额工时ID
     * @param ratedTimeCreateDTO 定额工时更新信息
     */
    @Override
    @Operation(
        summary = "更新定额工时信息"
    )
    @PutMapping(
        value = "/{orgId}/projects/{projectId}/rated-times/{ratedTimeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody update(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时ID") Long ratedTimeId,
        @RequestBody RatedTimeCreateDTO ratedTimeCreateDTO
    ) {

        ContextDTO context = getContext();

        ratedTimeService.update(context.getOperator().getId(), orgId, projectId, ratedTimeId, ratedTimeCreateDTO);

        return new JsonResponseBody();
    }

    /**
     * 删除定额工时信息。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     */
    @Override
    @Operation(
        summary = "删除定额工时信息"
    )
    @DeleteMapping(
        value = "/{orgId}/projects/{projectId}/rated-times/{ratedTimeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时ID") Long ratedTimeId
    ) {

        ContextDTO context = getContext();

        ratedTimeService.delete(context.getOperator().getId(), orgId, projectId, ratedTimeId);

        return new JsonResponseBody();
    }

    /**
     * 获取定额工时信息。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param ratedTimeId 定额工时ID
     */
    @Override
    @Operation(
        summary = "获取定额工时信息"
    )
    @GetMapping(
        value = "/{orgId}/projects/{projectId}/rated-times/{ratedTimeId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonObjectResponseBody<RatedTime> get(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId,
        @PathVariable @Parameter(description = "定额工时ID") Long ratedTimeId
    ) {

        ContextDTO context = getContext();

        return new JsonObjectResponseBody<>(
            context,
            ratedTimeService.get(orgId, projectId, ratedTimeId)
        );
    }

    /**
     * 导入excel。
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param version            项目版本号
     * @param ratedTimeImportDTO 导入文件DTO
     */
    @RequestMapping(
        method = POST,
        value = "/{orgId}/projects/{projectId}/import-rated-time",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @WithPrivilege
    @SetUserInfo
    @Operation(
        summary = "额定工时",
        description = "请先通过文档管理服务的 <code>POST /orgs/{orgId}/rated-time-import-files</code> 接口将导入文件上传到服务器，"
            + "导入文件请参照 <a href=\"/template/import-rated-time-template.xlsx\">import-rated-time-template.xlsx</a>。"
    )
    public JsonObjectResponseBody<RatedTimeImportRecord> importRatedTime(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam @Parameter(description = "项目更新版本号") long version,
        @RequestBody RatedTimeImportDTO ratedTimeImportDTO) {

        ContextDTO context = getContext();

        final OperatorDTO operator = getContext().getOperator();
        Project project = projectService.get(orgId, projectId, version);
        return new JsonObjectResponseBody<>(
            context,
            ratedTimeService.importRatedTimeFile(
                operator.getId(),
                orgId,
                project,
                ratedTimeImportDTO)
        );
    }

}
