package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.entity.scheduled.ScheduledTaskError;
import com.ose.tasks.entity.scheduled.ScheduledTaskLog;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.tasks.vo.scheduled.ScheduledTaskStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 定时任务执行记录管理接口。
 */
public interface ScheduledTaskLogAPI {

    /**
     * 取得定时任务执行记录列表。
     */
    @RequestMapping(
        method = GET,
        value = "/projects/{projectId}/scheduled-task-logs",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ScheduledTaskLog> logs(
        @PathVariable("projectId") Long projectId,
        @RequestParam(required = false) ScheduledTaskCode code,
        @RequestParam(required = false) ScheduledTaskStatus status,
        PageDTO pageDTO
    );

    /**
     * 取得定时任务执行记录的错误列表。
     */
    @RequestMapping(
        method = GET,
        value = "/projects/{projectId}/scheduled-task-logs/{taskId}/errors",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<ScheduledTaskError> errors(
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskId") Long taskId,
        PageDTO pageDTO
    );

    /**
     * 手动停止执行中的定时任务。
     */
    @RequestMapping(
        method = POST,
        value = "/projects/{projectId}/scheduled-task-logs/{taskId}/stop",
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody stop(
        @PathVariable("projectId") Long projectId,
        @PathVariable("taskId") Long taskId
    );

}
