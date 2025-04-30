package com.ose.tasks.controller;

import com.ose.auth.annotation.SetUserInfo;
import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.BatchTaskAPI;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.dto.BatchTaskCriteriaDTO;
import com.ose.tasks.dto.BatchTaskThreadPoolDTO;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.entity.BatchTaskBasic;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;

import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "批处理任务管理接口")
@RestController
public class BatchTaskController extends BaseController implements BatchTaskAPI {


    private BatchTaskInterface batchTaskService;

    /**
     * 构造方法。
     */
    @Autowired
    public BatchTaskController(BatchTaskInterface batchTaskService) {
        this.batchTaskService = batchTaskService;
    }

    /**
     * 查询批处理任务。
     *
     * @param companyId            所属公司 ID
     * @param orgId                所属组织 ID
     * @param projectId            项目 ID
     * @param batchTaskCriteriaDTO 批处理任务查询条件
     * @param pageDTO              查询分页参数
     * @return 批处理任务列表响应数据
     */
    private JsonListResponseBody<BatchTaskBasic> search(
        Long companyId,
        Long orgId,
        Long projectId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        PageDTO pageDTO
    ) {
        return new JsonListResponseBody<>(
            batchTaskService.search(
                companyId,
                orgId,
                projectId,
                batchTaskCriteriaDTO,
                pageDTO.toPageable()
            )
        );
    }

    @Override
    @Operation(description = "取得批处理任务列表")
    @RequestMapping(
        method = GET,
        value = "/companies/{companyId}/batch-tasks",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<BatchTaskBasic> searchByCompanyId(
        @PathVariable("companyId") Long companyId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        PageDTO pageDTO
    ) {
        return search(companyId, null, null, batchTaskCriteriaDTO, pageDTO);
    }

    @Override
    @Operation(description = "取得指定组织下的批处理任务列表")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/batch-tasks",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<BatchTaskBasic> searchByOrgId(
        @PathVariable("orgId") Long orgId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        PageDTO pageDTO
    ) {
        return search(null, orgId, null, batchTaskCriteriaDTO, pageDTO);
    }

    @Override
    @Operation(description = "取得指定项目下的批处理任务列表")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/batch-tasks",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonListResponseBody<BatchTaskBasic> searchByProjectId(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        BatchTaskCriteriaDTO batchTaskCriteriaDTO,
        PageDTO pageDTO
    ) {
        return search(null, orgId, projectId, batchTaskCriteriaDTO, pageDTO);
    }

    @Override
    @Operation(description = "取得批处理任务详细信息")
    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/batch-tasks/{taskId}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @SetUserInfo
    public JsonObjectResponseBody<BatchTask> get(
        @PathVariable("orgId") Long orgId,
        @PathVariable("taskId") Long taskId
    ) {
        return new JsonObjectResponseBody<>(
            batchTaskService.get(orgId, taskId)
        );
    }

    @RequestMapping(
        method = GET,
        value = "/orgs/{orgId}/projects/{projectId}/export-entities"
    )
    @WithPrivilege
    @Override
    public void exportEntities(@PathVariable("orgId") Long orgId,
                               @PathVariable("projectId") Long projectId) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File excel =  batchTaskService.exportEntities(orgId, projectId, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"export-project-piping-entities.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("下载文件出错");
        }

        response.flushBuffer();
    }

    @Override
    @Operation(description = "手动停止任务")
    @RequestMapping(
        method = POST,
        value = "/orgs/{orgId}/batch-tasks/{taskId}/stop",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public JsonResponseBody stop(
        @PathVariable("orgId") Long orgId,
        @PathVariable("taskId") Long taskId
    ) {
        batchTaskService.stop(getContext().getOperator(), orgId, taskId);
        return new JsonResponseBody();
    }

    @Override
    @Operation(description = "取得批处理任务详细信息")
    @RequestMapping(
        method = GET,
        value = "/thread-pool-task",
        produces = APPLICATION_JSON_VALUE
    )
    public JsonListResponseBody<BatchTaskThreadPoolDTO> threadPoolTaskSchedulerCount(
    ) {
        return new JsonListResponseBody<>(
            batchTaskService.threadPoolTaskSchedulerCount()
        );
    }
}
