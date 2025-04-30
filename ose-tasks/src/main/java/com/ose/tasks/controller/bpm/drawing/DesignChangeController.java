package com.ose.tasks.controller.bpm.drawing;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.util.List;

import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.drawing.DesignChangeInterface;
import com.ose.tasks.dto.bpm.ActivityInstanceDTO;
import com.ose.tasks.dto.bpm.CreateResultDTO;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.vo.bpm.BpmsProcessNameEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.drawing.DesignChangeAPI;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.domain.model.service.drawing.impl.DesignChangeReviewFormService;
import com.ose.tasks.dto.drawing.DesignChangeReviewDTO;
import com.ose.tasks.dto.drawing.DesignChangeCriteriaDTO;
import com.ose.tasks.dto.drawing.DesignChangeReviewRegisterDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.dto.drawing.UploadDrawingFileResultDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmProcess;
import com.ose.tasks.entity.DesignChangeReviewRegister;
import com.ose.tasks.entity.Project;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

@Tag(name = "图纸修改评审单接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class DesignChangeController extends BaseController implements DesignChangeAPI {

    private static final String PRODUCT_EVENT = "PRODUCT_EVENT";
    private static final String CHANGE_LEAD_BY_DRAWING = BpmsProcessNameEnum.CHANGE_LEAD_BY_DRAWING.getType();

    private DesignChangeInterface designChangeService;

    private ActivityTaskInterface activityTaskService;

    private ProjectInterface projectService;

    private DesignChangeReviewFormService designChangeReviewFormService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;


    /**
     * 构造方法
     */
    @Autowired
    public DesignChangeController(
        DesignChangeInterface designChangeService,
        ActivityTaskInterface activityTaskService,
        ProjectInterface projectService,
        DesignChangeReviewFormService designChangeReviewFormService,
        TodoTaskDispatchInterface todoTaskDispatchService) {
        this.designChangeService = designChangeService;
        this.activityTaskService = activityTaskService;
        this.projectService = projectService;
        this.designChangeReviewFormService = designChangeReviewFormService;
        this.todoTaskDispatchService = todoTaskDispatchService;
    }

    @Override
    @Operation(summary = "图纸修改评审单登记列表", description = "图纸修改评审单登记列表")
    @RequestMapping(method = GET, value = "modification-review-register", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<DesignChangeReviewRegister> searchModificationReviewRegisterList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        PageDTO page,
        DesignChangeCriteriaDTO criteriaDTO) {
        Page<DesignChangeReviewRegister> result = designChangeService.searchDesignChangeReviewRegisterList(orgId, projectId, page, criteriaDTO);
        for (DesignChangeReviewRegister register : result.getContent()) {
            List<BpmActivityInstanceBase> act = activityTaskService.findActInst(orgId, projectId, register.getId());
            if (!act.isEmpty()) {
                register.setHasTask(true);
            }
        }
        return new JsonListResponseBody<>(getContext(), result);
    }

    @Override
    @Operation(summary = "上传图纸修改评审单登记表", description = "上传图纸修改评审单登记表")
    @RequestMapping(method = POST, value = "modification-review-register/upload", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<UploadDrawingFileResultDTO> upload(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DrawingUploadDTO uploadDTO) {
        return new JsonObjectResponseBody<>(designChangeService.upload(orgId, projectId, uploadDTO));
    }

    @Override
    @Operation(summary = "新建图纸修改评审单登记表", description = "新建图纸修改评审单登记信息")
    @RequestMapping(method = POST, value = "modification-review-register", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    @WithPrivilege
    public JsonObjectResponseBody<DesignChangeReviewRegister> create(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @RequestBody DesignChangeReviewRegisterDTO DTO) {
        ContextDTO context = getContext();
        final Project project = projectService.get(orgId, projectId);

        String vorNo = designChangeService.getNewVorNo(orgId, projectId, project);
        DTO.setVorNo(vorNo);
        DesignChangeReviewRegister mrr = designChangeService.create(orgId, projectId, DTO);

        List<BpmProcess> processes = activityTaskService.getProcessByNameEN(orgId, projectId, CHANGE_LEAD_BY_DRAWING);
        if (processes.isEmpty()) {
            throw new ValidationError("please deploy the process CHANGE_LEAD_BY_DRAWING");
        }
        BpmProcess process = processes.get(0);
        BpmEntitySubType entitySubType = activityTaskService.getEntitiySubTypeByNameEN(orgId, projectId, PRODUCT_EVENT);
        OperatorDTO operatorDTO = getContext().getOperator();
        ActivityInstanceDTO taskDTO = new ActivityInstanceDTO();
        taskDTO.setAssignee(operatorDTO.getId());
        taskDTO.setAssigneeName(operatorDTO.getName());
        taskDTO.setEntitySubType(PRODUCT_EVENT);
        taskDTO.setEntitySubTypeId(entitySubType.getId());
        taskDTO.setEntityId(mrr.getId());
        taskDTO.setEntityNo(mrr.getVorNo());
        taskDTO.setProcess(CHANGE_LEAD_BY_DRAWING);
        taskDTO.setProcessId(process.getId());
        taskDTO.setVersion("0");
        taskDTO.setDrawingTitle(DTO.getTitle());
        todoTaskDispatchService.create(context, orgId, projectId, operatorDTO, taskDTO);

        return new JsonObjectResponseBody<>(mrr);
    }

    @Override
    @Operation(summary = "删除图纸修改评审单登记表", description = "删除图纸修改评审单登记表")
    @RequestMapping(method = DELETE, value = "modification-review-register/{id}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody delete(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        designChangeService.delete(orgId, projectId, id);
        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "获取设计评审单信息", description = "获取设计评审单信息")
    @RequestMapping(method = GET, value = "modification-review-register/{id}/design-change-review-form", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<DesignChangeReviewDTO> getDesignChangeReviewForm(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        return new JsonObjectResponseBody<>(designChangeService.getDesignChangeReviewForm(orgId, projectId, id));
    }

    @Override
    @Operation(summary = "填写设计评审单信息", description = "填写设计评审单信息")
    @RequestMapping(method = POST, value = "modification-review-register/{id}/design-change-review-form", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonResponseBody addDesignChangeReviewForm(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id,
        @RequestBody DesignChangeReviewDTO designChangeReviewDTO) {
        ContextDTO contextDTO = getContext();
        OperatorDTO operatorDTO = contextDTO.getOperator();
        designChangeReviewFormService.checkActionItem(orgId, projectId, id, designChangeReviewDTO.getActionItem());
        designChangeService.addDesignChangeReviewForm(contextDTO, orgId, projectId, id, designChangeReviewDTO, operatorDTO);
        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "创建变更流程", description = "创建变更流程")
    @RequestMapping(method = POST, value = "modification-review-register/{id}/create-task", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonObjectResponseBody<BpmActivityInstanceBase> createTask(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        @PathVariable @Parameter(description = "id") Long id) {
        ContextDTO context = getContext();
        DesignChangeReviewRegister mrr = designChangeService.getById(id);
        if (mrr != null) {
            List<BpmProcess> processes = activityTaskService.getProcessByNameEN(orgId, projectId, CHANGE_LEAD_BY_DRAWING);
            if (processes.isEmpty()) {
                throw new ValidationError("please deploy the process CHANGE_LEAD_BY_DRAWING");
            }
            BpmProcess process = processes.get(0);
            BpmEntitySubType entitySubType = activityTaskService.getEntitiySubTypeByNameEN(orgId, projectId, PRODUCT_EVENT);
            OperatorDTO operatorDTO = getContext().getOperator();
            ActivityInstanceDTO taskDTO = new ActivityInstanceDTO();
            taskDTO.setAssignee(operatorDTO.getId());
            taskDTO.setAssigneeName(operatorDTO.getName());
            taskDTO.setEntitySubType(PRODUCT_EVENT);
            taskDTO.setEntitySubTypeId(entitySubType.getId());
            taskDTO.setEntityId(mrr.getId());
            taskDTO.setEntityNo(mrr.getVorNo());
            taskDTO.setProcess(CHANGE_LEAD_BY_DRAWING);
            taskDTO.setProcessId(process.getId());
            taskDTO.setVersion("0");
            CreateResultDTO createResult = todoTaskDispatchService.create(context, orgId, projectId, operatorDTO, taskDTO);

            BpmActivityInstanceBase bpmActivityInstance = createResult.getActInst();

            return new JsonObjectResponseBody<>(bpmActivityInstance);

        }
        return null;
    }

    @Override
    @Operation(summary = "查询图纸清单", description = "查询图纸清单")
    @RequestMapping(method = GET, value = "modification-review-register/drawing-list", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @WithPrivilege
    public JsonListResponseBody<Drawing> searchDrawingList(
        @PathVariable @Parameter(description = "所属组织 ID") Long orgId,
        @PathVariable @Parameter(description = "项目 ID") Long projectId,
        String keyword,
        PageDTO page) {
        return new JsonListResponseBody<>(getContext(), designChangeService.searchDrawingList(orgId, projectId, page, keyword));
    }

}
