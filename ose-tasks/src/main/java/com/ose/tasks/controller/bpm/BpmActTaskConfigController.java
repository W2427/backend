package com.ose.tasks.controller.bpm;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.BpmActTaskConfigAPI;
import com.ose.tasks.domain.model.service.bpm.ActTaskConfigInterface;
import com.ose.tasks.dto.bpm.BpmActTaskConfigCreateDTO;
import com.ose.tasks.dto.bpm.BpmActTaskConfigDTO;
import com.ose.tasks.entity.bpm.BpmActTaskConfig;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * 通过 "/biz-code-types/TASK_DELEGATE_TYPE/biz-codes" 取得任务代理的类型列表
 * 通过 "/biz-code-types/TASK_DELEGATE_STAGE/biz-codes" 取得任务代理的阶段列表
 * 通过 "/biz-code-types/PROCESS_TYPE/biz-codes" 取得工序类型列表
 * 通过 "/orgs/{orgId}/projects/{projectId}/process-categories" 取得工序类别（CONSTRUCTION，DRAWING，MATERIAL）列表
 * 通过 /delegate-classes 取得 代理类，包括 短名和全名
 * 通过
 */
@Tag(name = "任务代理设置接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class BpmActTaskConfigController extends BaseController implements BpmActTaskConfigAPI {

    private final ActTaskConfigInterface actTaskConfigService;


    /**
     * 构造方法
     */
    @Autowired
    public BpmActTaskConfigController(
        ActTaskConfigInterface actTaskConfigService) {
        this.actTaskConfigService = actTaskConfigService;
    }

    /**
     * 创建任务代理设置。
     *
     * @param orgId                     组织id
     * @param projectId                 项目id
     * @param bpmActTaskConfigCreateDTO 传输对象
     * @return
     */
    @Override
    @Operation(summary = "创建任务代理设置", description = "创建任务代理设置。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "task-delegate-config")
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<BpmActTaskConfig> add(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "任务代理设置信息") BpmActTaskConfigCreateDTO bpmActTaskConfigCreateDTO
    ) {
        OperatorDTO operator = getContext().getOperator();
        BpmActTaskConfig bpmActTaskConfig = actTaskConfigService.add(orgId, projectId, bpmActTaskConfigCreateDTO, operator);
        return new JsonObjectResponseBody<>(getContext(), bpmActTaskConfig);

    }


    /**
     * 查询任务代理设置列表。
     *
     * @param orgId               组织id
     * @param projectId           项目id
     * @param bpmActTaskConfigDTO 查询参数
     * @param pageDTO             分页参数
     * @return
     */
    @RequestMapping(method = GET, value = "task-delegate-config")
    @Operation(summary = "任务代理设置列表", description = "任务代理设置列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActTaskConfig> list(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目Id") Long projectId,
        @Parameter(description = "查询参数") BpmActTaskConfigDTO bpmActTaskConfigDTO,
        PageDTO pageDTO
    ) {

        return new JsonListResponseBody<>(getContext(),
            actTaskConfigService.search(orgId, projectId, bpmActTaskConfigDTO, pageDTO));
    }


    /**
     * 编辑任务代理设置。
     *
     * @param orgId               组织id
     * @param projectId           项目id
     * @param bpmActTaskConfigId  任务代理设置ID
     * @param bpmActTaskConfigDTO 传输对象
     * @return
     */
    @RequestMapping(
        method = PUT,
        value = "task-delegate-config/{bpmActTaskConfigId}"
    )
    @Operation(summary = "更新任务代理设置", description = "更新任务代理设置。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<BpmActTaskConfig> edit(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable("bpmActTaskConfigId") Long bpmActTaskConfigId,
        @RequestBody @Parameter(description = "传输对象参数") BpmActTaskConfigDTO bpmActTaskConfigDTO
    ) {
        OperatorDTO operator = getContext().getOperator();
        return new JsonObjectResponseBody<>(getContext(),
            actTaskConfigService.edit(orgId, projectId, bpmActTaskConfigId, bpmActTaskConfigDTO, operator));
    }


    /**
     * 任务代理设置详情。
     *
     * @param orgId              组织id
     * @param projectId          项目id
     * @param bpmActTaskConfigId 任务代理设置ID
     * @return
     */
    @RequestMapping(
        method = GET,
        value = "task-delegate-config/{bpmActTaskConfigId}"
    )
    @ResponseStatus(OK)
    @Override
    @Operation(summary = "任务代理设置详情", description = "任务代理设置详情。")
    @WithPrivilege
    public JsonObjectResponseBody<BpmActTaskConfig> detail(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "任务代理设置ID") Long bpmActTaskConfigId
    ) {
        BpmActTaskConfig bpmActTaskConfig = actTaskConfigService.detail(orgId, projectId, bpmActTaskConfigId);
        return new JsonObjectResponseBody<>(getContext(), bpmActTaskConfig);

    }


    /**
     * 删除任务代理设置。
     *
     * @param orgId              组织id
     * @param projectId          项目id
     * @param bpmActTaskConfigId 任务代理设置ID
     * @return
     */
    @RequestMapping(
        method = DELETE,
        value = "task-delegate-config/{bpmActTaskConfigId}",
        produces = APPLICATION_JSON_VALUE
    )
    @Override
    @Operation(summary = "创建任务代理设置", description = "创建任务代理设置。")
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable("bpmActTaskConfigId") @Parameter(description = "任务代理设置ID") Long bpmActTaskConfigId
    ) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        actTaskConfigService.delete(orgId, projectId, bpmActTaskConfigId, operatorDTO);
        return new JsonResponseBody();
    }
}
