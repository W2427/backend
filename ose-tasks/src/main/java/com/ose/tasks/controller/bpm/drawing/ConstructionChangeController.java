package com.ose.tasks.controller.bpm.drawing;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.ConstructionChangeAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.drawing.ConstructionChangeInterface;
import com.ose.tasks.domain.model.service.drawing.DesignChangeInterface;
import com.ose.tasks.domain.model.service.drawing.impl.DesignChangeReviewFormService;
import com.ose.tasks.dto.bpm.ActivityInstanceDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.dto.drawing.ConstructionChangeRegisterDTO;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewDTO;
import com.ose.tasks.entity.ConstructionChangeRegister;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.vo.bpm.BpmsProcessNameEnum;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "建造变更申请单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class ConstructionChangeController extends BaseController implements ConstructionChangeAPI {

    private static final String PRODUCT_EVENT = "PRODUCT_EVENT";

    private ConstructionChangeInterface constructionChangeService;

    private ActivityTaskInterface activityTaskService;

    private ProjectInterface projectService;

    private DesignChangeReviewFormService designChangeReviewFormService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final DesignChangeInterface designChangeService;


    /**
     * 构造方法
     */
    @Autowired
    public ConstructionChangeController(
        ConstructionChangeInterface constructionChangeService,
        ActivityTaskInterface activityTaskService,
        ProjectInterface projectService,
        DesignChangeReviewFormService designChangeReviewFormService,
        TodoTaskDispatchInterface todoTaskDispatchService, DesignChangeInterface designChangeService) {
        this.constructionChangeService = constructionChangeService;
        this.activityTaskService = activityTaskService;
        this.designChangeService = designChangeService;
        this.projectService = projectService;
        this.designChangeReviewFormService = designChangeReviewFormService;
        this.todoTaskDispatchService = todoTaskDispatchService;
    }

    /**
     * 建造变更申请单列表。
     *
     * @param orgId       组织id
     * @param projectId   项目id
     * @param page        分页参数
     * @param criteriaDTO 查询参数
     * @return 建造变更申请单列表
     */
    @Override
    @Operation(summary = "建造变更申请单列表", description = "建造变更申请单列表")
    @RequestMapping(method = GET, value = "construction-change-register", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<ConstructionChangeRegister> searchConstructionChangeRegisterList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PageDTO page,
        DesignChangeCriteriaDTO criteriaDTO) {
        Page<ConstructionChangeRegister> result = constructionChangeService.searchConstructionChangeRegisterList(orgId, projectId, page, criteriaDTO);
        for (ConstructionChangeRegister register : result.getContent()) {
            List<BpmActivityInstanceBase> act = activityTaskService.findActInst(orgId, projectId, register.getId());
            if (!act.isEmpty()) {
                register.setHasTask(true);
            }
        }
        return new JsonListResponseBody<>(getContext(), result);
    }

    /**
     * 添加建造变更申请。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param DTO       创建建造变更DTO
     * @return 添加建造变更申请
     */
    @Override
    @Operation(summary = "添加建造变更申请", description = "添加建造变更申请")
    @RequestMapping(method = POST, value = "construction-change-register", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @WithPrivilege
    public JsonObjectResponseBody<ConstructionChangeRegister> create(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody ConstructionChangeRegisterDTO DTO) {
        OperatorDTO operatorDTO = getContext().getOperator();
        final Project project = projectService.get(orgId, projectId);
        return new JsonObjectResponseBody<>(constructionChangeService.create(orgId, projectId, DTO, operatorDTO, project));
    }

    /**
     * 删除建造变更。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        建造变更id
     * @return 删除建造变更
     */
    @Override
    @Operation(summary = "删除建造变更申请", description = "删除建造变更申请")
    @RequestMapping(method = DELETE, value = "construction-change-register/{id}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        constructionChangeService.delete(orgId, projectId, id);
        return new JsonResponseBody();
    }

    /**
     * 创建建造变更流程。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        建造变更id
     * @return 创建建造变更流程
     */
    @Override
    @Operation(summary = "创建变更流程", description = "创建变更流程")
    @RequestMapping(method = POST, value = "construction-change-register/{id}/create-task", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<BpmActivityInstanceBase> createTask(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        ContextDTO context = getContext();
        ConstructionChangeRegister ccr = constructionChangeService.getById(id);
        if (ccr != null) {
            List<BpmProcess> processes = activityTaskService.getProcessByNameEN(orgId, projectId, BpmsProcessNameEnum.CHANGE_LEAD_BY_CONSTRUCTION.getType());
            if (processes.isEmpty()) {
                throw new ValidationError("please deploy the process CHANGE_LEAD_BY_CONSTRUCTION");
            }
            BpmProcess process = processes.get(0);
            BpmEntitySubType entitySubType = activityTaskService.getEntitiySubTypeByNameEN(orgId, projectId, PRODUCT_EVENT);
            OperatorDTO operatorDTO = getContext().getOperator();
            ActivityInstanceDTO taskDTO = new ActivityInstanceDTO();
            taskDTO.setAssignee(operatorDTO.getId());
            taskDTO.setAssigneeName(operatorDTO.getName());
            taskDTO.setEntitySubType(PRODUCT_EVENT);
            taskDTO.setEntitySubTypeId(entitySubType.getId());
            taskDTO.setEntityId(ccr.getId());
            taskDTO.setEntityNo(ccr.getRegisterNo());
            taskDTO.setProcess(BpmsProcessNameEnum.CHANGE_LEAD_BY_CONSTRUCTION.getType());
            taskDTO.setProcessId(process.getId());
            taskDTO.setVersion("0");
            CreateResultDTO createResult = todoTaskDispatchService.create(context, orgId, projectId, operatorDTO, taskDTO);
            BpmActivityInstanceBase bpmActivityInstance = createResult.getActInst();

            return new JsonObjectResponseBody<>(bpmActivityInstance);

        }
        return null;
    }

    /**
     * 获取建造变更详情。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param actInstId 工作流实例id
     * @return 获取建造变更详情
     */
    @Override
    @Operation(summary = "获取建造变更申请", description = "根据流程id获取建造变更申请")
    @RequestMapping(method = GET, value = "construction-change-register/activity/{actInstId}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<ConstructionChangeRegister> getByActInstId(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "actInstId") Long actInstId) {
        BpmActivityInstanceBase actInst = activityTaskService.findActInstById(actInstId);
        if (actInst != null) {
            ConstructionChangeRegister ccr = constructionChangeService.getById(actInst.getEntityId());
            return new JsonObjectResponseBody<>(getContext(), ccr);
        }
        return null;
    }

    /**
     * 填写建造变更评审单。
     *
     * @param orgId                 组织id
     * @param projectId             项目id
     * @param id                    建造变更id
     * @param designChangeReviewDTO 设计评审DTO
     * @return 增加操作确认
     */
    @Override
    @Operation(summary = "填写设计评审单信息", description = "填写设计评审单信息")
    @RequestMapping(method = POST, value = "construction-change-register/{id}/design-change-review-form", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody addDesignChangeReviewForm(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody DesignChangeReviewDTO designChangeReviewDTO) {
        designChangeReviewFormService.checkActionItem(orgId, projectId, id, designChangeReviewDTO.getActionItem());
        ContextDTO contextDTO = getContext();
        OperatorDTO operatorDTO = contextDTO.getOperator();
        constructionChangeService.addDesignChangeReviewForm(contextDTO, orgId, projectId, id, designChangeReviewDTO,
            operatorDTO);
        return new JsonResponseBody();
    }

    /**
     * 获取设计评审信息。
     *
     * @param orgId     组织id
     * @param projectId 项目id
     * @param id        建造变更id
     * @return 设计变更表数据
     */
    @Override
    @Operation(summary = "获取设计评审单信息", description = "获取设计评审单信息")
    @RequestMapping(method = GET, value = "construction-change-register/{id}/design-change-review-form", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DesignChangeReviewDTO> getDesignChangeReviewForm(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonObjectResponseBody<>(designChangeService.getDesignChangeReviewForm(orgId, projectId, id));
    }

}
