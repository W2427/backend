package com.ose.tasks.controller.bpm;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.BpmExecuteCaseAPI;
import com.ose.tasks.domain.model.service.bpm.BpmExecuteCaseInterface;
import com.ose.tasks.dto.bpm.BpmExecuteCaseCreateDTO;
import com.ose.tasks.dto.bpm.BpmExecuteCaseDTO;
import com.ose.tasks.entity.bpm.BpmExecuteCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "任务处理case接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class BpmExecuteCaseController extends BaseController implements BpmExecuteCaseAPI {

    private final BpmExecuteCaseInterface bpmExecuteCaseService;

    /**
     * 构造方法
     */
    @Autowired
    public BpmExecuteCaseController(
        BpmExecuteCaseInterface bpmExecuteCaseService
    ) {
        this.bpmExecuteCaseService = bpmExecuteCaseService;
    }

    /**
     * 创建任务处理case。
     *
     * @param orgId                   组织id
     * @param projectId               项目id
     * @param bpmExecuteCaseCreateDTO 传输对象
     * @return
     */
    @Override
    @Operation(summary = "创建任务处理case", description = "创建任务处理case。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "bpm-execute-case")
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<BpmExecuteCase> add(@PathVariable @Parameter(description = "组织id") Long orgId,
                                                      @PathVariable @Parameter(description = "项目id") Long projectId,
                                                      @RequestBody @Parameter(description = "任务信息") BpmExecuteCaseCreateDTO bpmExecuteCaseCreateDTO) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        bpmExecuteCaseCreateDTO.setOrgId(orgId);
        bpmExecuteCaseCreateDTO.setProjectId(projectId);
        BpmExecuteCase bpmExecuteCase = bpmExecuteCaseService.add(bpmExecuteCaseCreateDTO, operatorDTO);
        return new JsonObjectResponseBody<BpmExecuteCase>(getContext(), bpmExecuteCase);

    }

    /**
     * 任务处理case列表。
     *
     * @param orgId             组织id
     * @param projectId         项目id
     * @param bpmExecuteCaseDTO 参数参数
     * @param pageDTO           分页参数
     * @return
     */
    @RequestMapping(method = GET, value = "bpm-execute-case")
    @Operation(summary = "任务处理case列表", description = "任务处理case列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmExecuteCase> list(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                     @PathVariable @Parameter(description = "项目Id") Long projectId,
                                                     BpmExecuteCaseDTO bpmExecuteCaseDTO,
                                                     PageDTO pageDTO
    ) {
        return new JsonListResponseBody<BpmExecuteCase>(getContext(),
            bpmExecuteCaseService.list(bpmExecuteCaseDTO, pageDTO));
    }

    /**
     * 查询特殊任务处理case。
     *
     * @param orgId             组织id
     * @param projectId         项目id
     * @param bpmExecuteCaseDTO 查询参数
     * @return
     */
    @RequestMapping(method = GET, value = "bpm-execute-case/item-case")
    @Operation(summary = "查询特殊任务处理case", description = "查询特殊任务处理case。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmExecuteCase> search(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                       @PathVariable @Parameter(description = "项目Id") Long projectId,
                                                       BpmExecuteCaseDTO bpmExecuteCaseDTO
    ) {
        return new JsonListResponseBody<BpmExecuteCase>(getContext(),
            bpmExecuteCaseService.search(bpmExecuteCaseDTO));
    }

    /**
     * 任务处理case详情。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param bpmExecuteCaseId 任务处理caseid
     * @return
     */
    @RequestMapping(method = GET, value = "bpm-execute-case/{bpmExecuteCaseId}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "任务处理case详情", description = "任务处理case详情")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<BpmExecuteCase> detail(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                         @PathVariable @Parameter(description = "项目Id") Long projectId,
                                                         @PathVariable @Parameter(description = "任务处理caseId") Long bpmExecuteCaseId
    ) {
        return new JsonObjectResponseBody<>(getContext(),
            bpmExecuteCaseService.detail(orgId, projectId, bpmExecuteCaseId));
    }


    /**
     * 更新任务处理case。
     *
     * @param orgId                   组织id
     * @param projectId               项目id
     * @param bpmExecuteCaseId        任务处理 caseid
     * @param bpmExecuteCaseCreateDTO 传输对象
     * @return
     */
    @Override
    @Operation(summary = "更新任务处理case", description = "更新任务处理case。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "bpm-execute-case/{bpmExecuteCaseId}")
    @ResponseStatus(OK)
    public JsonObjectResponseBody<BpmExecuteCase> edit(@PathVariable @Parameter(description = "组织id") Long orgId,
                                                       @PathVariable @Parameter(description = "项目id") Long projectId,
                                                       @PathVariable @Parameter(description = "任务处理caseId") Long bpmExecuteCaseId,
                                                       @RequestBody @Parameter(description = "任务信息") BpmExecuteCaseCreateDTO bpmExecuteCaseCreateDTO) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        bpmExecuteCaseCreateDTO.setOrgId(orgId);
        bpmExecuteCaseCreateDTO.setProjectId(projectId);
        BpmExecuteCase bpmExecuteCase = bpmExecuteCaseService.edit(bpmExecuteCaseId, bpmExecuteCaseCreateDTO, operatorDTO);
        return new JsonObjectResponseBody<>(getContext(), bpmExecuteCase);

    }

    /**
     * 删除任务处理case。
     *
     * @param orgId            组织id
     * @param projectId        项目id
     * @param bpmExecuteCaseId 任务处理caseid
     * @return
     */
    @Override
    @Operation(summary = "创建任务处理case", description = "创建任务处理case。")
    @WithPrivilege
    @RequestMapping(method = DELETE, value = "bpm-execute-case/{bpmExecuteCaseId}")
    @ResponseStatus(OK)
    public JsonResponseBody delete(
        @PathVariable("orgId") Long orgId,
        @PathVariable("projectId") Long projectId,
        @PathVariable("bpmExecuteCaseId") Long bpmExecuteCaseId
    ) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();
        bpmExecuteCaseService.delete(orgId, projectId, bpmExecuteCaseId, operatorDTO);
        return new JsonResponseBody();
    }
}
