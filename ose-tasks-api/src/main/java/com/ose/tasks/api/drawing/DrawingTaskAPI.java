package com.ose.tasks.api.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.bpm.ActInstCriteriaDTO;
import com.ose.tasks.dto.bpm.BpmActivityInstanceDTO;
import com.ose.tasks.dto.bpm.DrawingAppointDTO;
import com.ose.tasks.entity.BatchDrawingTask;
import com.ose.tasks.entity.bpm.BpmReDeployment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/drawing-task/")
public interface DrawingTaskAPI {

    /**
     * 获取管线设计流程模型列表
     */
    @RequestMapping(
        method = GET,
        value = "models",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BpmReDeployment> getModels(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    );


    /**
     * 任务列表
     *
     * @param criteria 任务列表查询数据类
     * @return 实体信息
     */
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    JsonListResponseBody<BpmActivityInstanceDTO> search(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @RequestBody ActInstCriteriaDTO criteria
    );

    @RequestMapping(
        method = GET,
        value = "drawing-package",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(OK)
    JsonListResponseBody<BatchDrawingTask> batchTaskDrawingList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PageDTO page);

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
    JsonResponseBody stopBatchTaskDrawing(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸打包批处理id") Long batchTaskDrawingId
    );

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
    JsonResponseBody startBatchTaskDrawing(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸打包批处理id") Long batchTaskDrawingId
    );

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
    JsonResponseBody startBatchTask(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸打包批处理id") Long batchTaskDrawingId
    );

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
    JsonResponseBody startSubDrawingTask(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "子图纸") Long subDrawingId
    );

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
    JsonResponseBody appoint(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸id") Long drawingId,
        @RequestBody DrawingAppointDTO appointDTO
    );

    @Operation(
        summary = "工时填报",
        description = "工时填报"
    )
    @RequestMapping(
        method = POST,
        value = "{drawingId}/work-hour/{workhour}",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    JsonResponseBody workHour(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "图纸id") Long drawingId,
        @PathVariable @Parameter(description = "图纸id") Double workhour
    );

}
