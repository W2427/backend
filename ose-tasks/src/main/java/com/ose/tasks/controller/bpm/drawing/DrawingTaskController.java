package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.DrawingTaskAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingTaskInterface;
import com.ose.tasks.dto.bpm.ActInstCriteriaDTO;
import com.ose.tasks.dto.bpm.BpmActivityInstanceDTO;
import com.ose.tasks.dto.bpm.DrawingAppointDTO;
import com.ose.tasks.entity.BatchDrawingTask;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmReDeployment;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "图纸流程任务接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-task/")
public class DrawingTaskController extends BaseController implements DrawingTaskAPI {

    private static final String ACT_CATEGORY = "管线设计";

    private DrawingTaskInterface drawingTaskService;

    private ProjectInterface projectService;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingTaskController(
        DrawingTaskInterface drawingTaskService,
        ProjectInterface projectService) {
        this.drawingTaskService = drawingTaskService;
        this.projectService = projectService;
    }

    @Override
    @Operation(
        summary = "获取管线设计流程模型列表",
        description = "获取管线设计流程模型列表"
    )
    @RequestMapping(
        method = GET,
        value = "models",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonListResponseBody<BpmReDeployment> getModels(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId) {
        return new JsonListResponseBody<>(drawingTaskService.getModels(orgId, projectId));
    }


    /**
     * 图纸任务列表
     *
     * @param criteria 任务列表查询数据类
     * @return 实体信息
     */
    @RequestMapping(method = GET)
    @Operation(summary = "图纸流程任务列表", description = "根据条件信息，查询任务列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActivityInstanceDTO> search(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目Id") Long projectId,
        ActInstCriteriaDTO criteria) {
        return new JsonListResponseBody<>(getContext(),
            drawingTaskService.actInstList(orgId, projectId, criteria));
    }

    @Override
    @Operation(
        summary = "获取图纸打包历史",
        description = "获取图纸打包历史"
    )
    @RequestMapping(
        method = GET,
        value = "drawing-package",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BatchDrawingTask> batchTaskDrawingList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PageDTO page) {
        return new JsonListResponseBody<>(drawingTaskService.batchTaskDrawingList(orgId, projectId, page));
    }

    @Override
    @Operation(
        summary = "停止正在打包的图纸任务",
        description = "停止正在打包的图纸任务"
    )
    @RequestMapping(
        method = POST,
        value = "drawing-package/{batchTaskDrawingId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody stopBatchTaskDrawing(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸打包批处理id") Long batchTaskDrawingId
    ) {
        drawingTaskService.stopBatchTaskDrawing(orgId, projectId, batchTaskDrawingId);

        return new JsonResponseBody();
    }

    @Operation(
        summary = "开始打包的图纸任务",
        description = "开始打包的图纸任务"
    )
    @RequestMapping(
        method = PATCH,
        value = "drawing-package/{batchTaskDrawingId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody startBatchTaskDrawing(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸打包批处理id") Long batchTaskDrawingId
    ) {
        final Project project = projectService.get(orgId, projectId);

        drawingTaskService.startBatchTaskDrawing(getContext(), project, getContext().getOperator(), orgId, projectId, batchTaskDrawingId);

        return new JsonResponseBody();
    }

    @Operation(
        summary = "给所有有效图纸打包二维码并生成pdf",
        description = "给所有有效图纸打包二维码并生成pdf"
    )
    @RequestMapping(
        method = POST,
        value = "drawing-package/{batchTaskDrawingId}/qrcode",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody startBatchTask(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸打包批处理id") Long batchTaskDrawingId
    ) {
        final Project project = projectService.get(orgId, projectId);

        drawingTaskService.startBatchTask(getContext(), project, getContext().getOperator(), orgId, projectId, batchTaskDrawingId);

        return new JsonResponseBody();
    }


    @Operation(
        summary = "给单张子图纸打印二维码",
        description = "给单张子图纸打印二维码"
    )
    @RequestMapping(
        method = POST,
        value = "sub-drawing-repair/{subDrawingId}/qrCode",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody startSubDrawingTask(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子图纸") Long subDrawingId
    ) {
        final Project project = projectService.get(orgId, projectId);

        drawingTaskService.startSubDrawingTask(getContext(), project, getContext().getOperator(), orgId, projectId, subDrawingId);

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "图纸指派",
        description = "图纸指派"
    )
    @RequestMapping(
        method = POST,
        value = "drawing-appoint/{drawingId}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody appoint(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子图纸") Long drawingId,
        @RequestBody DrawingAppointDTO appointDTO
    ) {
        final OperatorDTO operator = getContext().getOperator();

        drawingTaskService.appoint(orgId, projectId, drawingId, appointDTO, operator);

        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "图纸指派",
        description = "图纸指派"
    )
    @RequestMapping(
        method = POST,
        value = "{drawingId}/work-hour/{workhour}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody workHour(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸id") Long drawingId,
        @PathVariable @Parameter(description = "图纸id") Double workhour
    ) {

        drawingTaskService.workHour(orgId, projectId, drawingId, workhour);

        return new JsonResponseBody();
    }
}

