package com.ose.tasks.controller.bpm;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.PageDTO;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.ProcessStageAPI;
import com.ose.tasks.domain.model.service.bpm.ProcessStageInterface;
import com.ose.tasks.dto.bpm.ProcessStageDTO;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.bpm.BpmProcessStage;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "工序阶段接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}")
public class ProcessStageController extends BaseController implements ProcessStageAPI {

    /**
     * 工序管理服务
     */
    private final ProcessStageInterface processStageService;

    /**
     * 构造方法
     *
     * @param processStageService 工序管理服务
     */
    @Autowired
    public ProcessStageController(ProcessStageInterface processStageService) {
        this.processStageService = processStageService;
    }


    @Override
    @Operation(
        summary = "创建工序阶段",
        description = "根据工序阶段信息，创建工序阶段。"
    )
    @RequestMapping(
        method = POST,
        value = "/process-stages",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<BpmProcessStage> create(
        @RequestBody @Parameter(description = "工序分类信息") ProcessStageDTO managementStepDTO,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonObjectResponseBody<>(
            getContext(),
            processStageService.create(managementStepDTO, projectId, orgId)
        );
    }


    @Override
    @Operation(
        summary = "查询工序阶段",
        description = "获取工序阶段列表。"
    )
    @RequestMapping(
        method = GET,
        value = "/process-stages"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<BpmProcessStage> getList(
        PageDTO page,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonListResponseBody<>(
            getContext(),
            processStageService.getList(page, projectId, orgId)
        );
    }


    @Override
    @Operation(
        summary = "删除工序阶段",
        description = "删除指定的工序阶段"
    )
    @RequestMapping(
        method = DELETE,
        value = "/process-stages/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "工序分类id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {

        List<BpmProcess> processes = processStageService.getProcessesByStageId(id);

        if (processes != null && processes.size() > 0) {
            throw new ValidationError("There are processes that reference this stage");
        }

        processStageService.delete(id, projectId, orgId);
        return new JsonResponseBody();
    }


    @Override
    @Operation(
        summary = "编辑工序阶段",
        description = "编辑指定的工序阶段"
    )
    @RequestMapping(
        method = POST,
        value = "/process-stages/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<BpmProcessStage> modify(
        @PathVariable @Parameter(description = "工序分类id") Long id,
        @RequestBody @Parameter(description = "工序分类信息") ProcessStageDTO managementStepDTO,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonObjectResponseBody<>(
            getContext(),
            processStageService.modify(id, managementStepDTO, projectId, orgId)
        );
    }


    @Override
    @Operation(
        summary = "获取工序阶段详细信息",
        description = "根据ID查询工序阶段详细信息"
    )
    @RequestMapping(
        method = GET,
        value = "/process-stages/{id}",
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<BpmProcessStage> getStage(
        @PathVariable @Parameter(description = "工序分类id") Long id,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "orgId") Long orgId) {
        return new JsonObjectResponseBody<>(
            getContext(),
            processStageService.getStage(id, projectId, orgId)
        );
    }


}
