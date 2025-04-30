package com.ose.tasks.controller;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.ScheduledTaskLogAPI;
import com.ose.tasks.domain.model.service.scheduled.ScheduledTaskLogInterface;
import com.ose.tasks.entity.scheduled.ScheduledTaskError;
import com.ose.tasks.entity.scheduled.ScheduledTaskLog;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.tasks.vo.scheduled.ScheduledTaskStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "定时任务执行记录管理接口")
@RestController
public class ScheduledTaskLogController extends BaseController implements ScheduledTaskLogAPI {


    private final ScheduledTaskLogInterface scheduledTaskLogService;

    /**
     * 构造方法。
     */
    @Autowired
    public ScheduledTaskLogController(
        ScheduledTaskLogInterface scheduledTaskLogService
    ) {
        this.scheduledTaskLogService = scheduledTaskLogService;
    }

    @Override
    @Operation(description = "查询定时任务执行记录")
    @WithPrivilege
    public JsonListResponseBody<ScheduledTaskLog> logs(
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestParam(required = false) @Parameter(description = "定时任务代码") ScheduledTaskCode code,
        @RequestParam(required = false) @Parameter(description = "定时任务执行状态") ScheduledTaskStatus status,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            scheduledTaskLogService.list(
                projectId,
                code,
                status,
                pageDTO.toPageable()
            )
        );
    }

    @Override
    @Operation(description = "查询定时任务执行记录错误信息")
    @WithPrivilege
    public JsonListResponseBody<ScheduledTaskError> errors(
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "定时任务执行记录 ID") Long taskId,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            scheduledTaskLogService.errors(
                projectId,
                taskId,
                pageDTO.toPageable()
            )
        );
    }

    @Override
    @Operation(description = "手动停止执行中的定时任务")
    @WithPrivilege
    public JsonResponseBody stop(
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "定时任务执行记录 ID") Long taskId
    ) {

        scheduledTaskLogService.stop(
            getContext().getOperator(),
            taskId
        );

        return new JsonResponseBody();
    }

}
