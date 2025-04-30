package com.ose.tasks.api;

import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import com.ose.tasks.dto.BatchTaskThreadPoolDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.BatchTaskBasic;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 批处理任务管理接口。
 */
public interface BatchTaskAPI {

    /**
     * 取得批处理任务列表。
     */
    @RequestMapping(
        method = GET,
        value = "/companies/{companyId}/batch-tasks",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BatchTaskBasic> searchByCompanyId(
        @PathVariable("companyId") Long companyId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得批处理任务列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/batch-tasks",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BatchTaskBasic> searchByOrgId(
        @PathVariable("orgId") Long orgId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得批处理任务列表。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/batch-tasks",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BatchTaskBasic> searchByProjectId(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        PageDTO pageDTO
    );

    /**
     * 取得批处理任务详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/batch-tasks/{taskId}",
        produces = APPLICATION_JSON_VALUE
    )
    JsonObjectResponseBody<BatchTask> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("taskId") Long taskId
    );

    /**
     * 导出管系实体
     */
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/export-entities",
        produces = APPLICATION_JSON_VALUE
    )
    void exportEntities(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId
    ) throws IOException;

    /**
     * 手动停止任务。
     */
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/batch-tasks/{taskId}/stop",
        consumes = APPLICATION_JSON_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    JsonResponseBody stop(
        @PathVariable("orgId") Long orgId,
        @PathVariable("taskId") Long taskId
    );

    /**
     * 取得批处理任务详细信息。
     */
    @RequestMapping(
        method = GET,
        value = "/thread-pool-task",
        produces = APPLICATION_JSON_VALUE
    )
    JsonListResponseBody<BatchTaskThreadPoolDTO> threadPoolTaskSchedulerCount(
    );
}
