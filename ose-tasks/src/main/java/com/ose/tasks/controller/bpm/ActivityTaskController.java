package com.ose.tasks.controller.bpm;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.RoleFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.Role;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.vo.UserPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.ActivityTaskAPI;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingEntryDelegateRepository;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskExecInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.domain.model.service.drawing.impl.DrawingService;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.TaskProcessDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingEntryDelegate;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.BpmTaskDefKey;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

import static com.ose.tasks.vo.bpm.BpmCode.ACTIVE_MARK;
import static com.ose.vo.BpmTaskDefKey.CO_SIGN;
import static com.ose.vo.BpmTaskType.*;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

@Tag(name = "流程实例管理接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class ActivityTaskController extends BaseController implements ActivityTaskAPI {

    private final static Logger logger = LoggerFactory.getLogger(ActivityTaskController.class);
    /**
     * 任务管理服务
     */

    private final UserFeignAPI userFeignAPI;

    private final RoleFeignAPI roleFeignAPI;

    private final UploadFeignAPI uploadFeignAPI;

    private final ActivityTaskInterface activityTaskService;

    private final DrawingService drawingService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final TaskExecInterface taskExecService;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final BpmRuTaskRepository bpmRuTaskRepository;

    private final BpmReDeploymentRepository bpmReDeploymentRepository;

    private final DrawingEntryDelegateRepository drawingEntryDelegateRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository;

    private final BpmActivityInstanceShiftRepository bpmActivityInstanceShiftRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private static final List<String> MATERIAL_REVOCATION_NODE = new ArrayList<>(
        Arrays.asList(
            BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_CONTROLLER_RECEIVE_SP_COMP.getType(),
            BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_CONTROLLER_RECEIVE_ISO_COMP.getType(),
            BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_CONTROLLER_RECEIVE.getType(),
            BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_CREATE_REQUISITION.getType()
        )
    );

    /**
     * 构造方法
     * 任务管理服务
     *
     * @param todoTaskDispatchService
     * @param taskRuleCheckService
     * @param todoTaskBaseService
     * @param bpmActivityInstanceStateRepository
     * @param taskExecService
     * @param hiTaskinstRepository
     * @param bpmReDeploymentRepository
     * @param ruTaskRepository
     */
    @Autowired
    public ActivityTaskController(ActivityTaskInterface activityTaskService,
                                  DrawingService drawingService,
                                  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                  UserFeignAPI userFeignAPI,
                                  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                  UploadFeignAPI uploadFeignAPI,
                                  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
                                  RoleFeignAPI roleFeignAPI,
                                  TodoTaskDispatchInterface todoTaskDispatchService,
                                  TaskRuleCheckService taskRuleCheckService,
                                  TodoTaskBaseInterface todoTaskBaseService,
                                  BpmRuTaskRepository bpmRuTaskRepository,
                                  BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                  TaskExecInterface taskExecService,
                                  BpmHiTaskinstRepository hiTaskinstRepository, BpmReDeploymentRepository bpmReDeploymentRepository,
                                  DrawingEntryDelegateRepository drawingEntryDelegateRepository, BpmRuTaskRepository ruTaskRepository,
                                  BpmDrawingSignTaskRepository bpmDrawingSignTaskRepository,
                                  BpmActivityInstanceShiftRepository bpmActivityInstanceShiftRepository,
                                  DrawingDetailRepository drawingDetailRepository) {
        this.activityTaskService = activityTaskService;
        this.drawingService = drawingService;
        this.userFeignAPI = userFeignAPI;
        this.uploadFeignAPI = uploadFeignAPI;
        this.roleFeignAPI = roleFeignAPI;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.todoTaskBaseService = todoTaskBaseService;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.taskExecService = taskExecService;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.bpmReDeploymentRepository = bpmReDeploymentRepository;
        this.drawingEntryDelegateRepository = drawingEntryDelegateRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmDrawingSignTaskRepository = bpmDrawingSignTaskRepository;
        this.bpmActivityInstanceShiftRepository = bpmActivityInstanceShiftRepository;
        this.drawingDetailRepository = drawingDetailRepository;
    }

    /**
     * 创建工作流任务
     *
     * @param taskDTO 任务信息信息
     * @return 实体信息
     */
    @Override
    @Operation(summary = "创建任务", description = "根据任务信息，创建工作流任务。")
    @WithPrivilege
    @RequestMapping(method = POST, value = "activities")
    @ResponseStatus(CREATED)
    public JsonObjectResponseBody<BpmActivityInstanceBase> create(@PathVariable @Parameter(description = "组织id") Long orgId,
                                                                  @PathVariable @Parameter(description = "项目id") Long projectId,
                                                                  @RequestBody @Parameter(description = "任务信息") ActivityInstanceDTO taskDTO) {
        ContextDTO context = getContext();
        OperatorDTO operatorDTO = context.getOperator();


        CreateResultDTO createResult = todoTaskDispatchService.create(context, orgId, projectId, operatorDTO, taskDTO);
        BpmActivityInstanceBase bpmActivityInstance = createResult.getActInst();


        return new JsonObjectResponseBody<>(context, bpmActivityInstance);

    }

    /**
     * 工作流管理任务列表
     *
     * @param criteria 任务列表查询数据类
     * @param orgId    组织ID
     * @return 实体信息
     */
    @GetMapping("activities")
    @Operation(summary = "工作流管理任务列表", description = "根据条件信息，查询任务列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActivityInstanceDTO> search(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                               @PathVariable @Parameter(description = "项目Id") Long projectId,
                                                               ActInstCriteriaDTO criteria) {
        return new JsonListResponseBody<>(getContext(),
            activityTaskService.actInstList(orgId, projectId, criteria));
    }

    /**
     * function列表
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @RequestMapping(method = GET, value = "functions")
    @Operation(summary = "工作流管理任务列表", description = "根据条件信息，查询任务列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActivityInstanceDTO> searchFunction(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                       @PathVariable @Parameter(description = "项目Id") Long projectId) {
        return new JsonListResponseBody<>(getContext(),
            activityTaskService.searchFunction(orgId, projectId));
    }

    /**
     * type列表
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @RequestMapping(method = GET, value = "types")
    @Operation(summary = "工作流管理任务列表", description = "根据条件信息，查询任务列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActivityInstanceDTO> searchType(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                   @PathVariable @Parameter(description = "项目Id") Long projectId) {
        return new JsonListResponseBody<>(getContext(),
            activityTaskService.searchType(orgId, projectId));
    }


    /**
     * 编辑任务
     *
     * @param taskDTO 任务信息
     * @return 实体信息
     */
    @RequestMapping(method = POST, value = "activities/{id}")
    @Operation(summary = "修改任务信息", description = "修改任务中的预计开始日期、预计结束日期、预计工时")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<BpmActivityInstanceBase> modify(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                  @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long id,
                                                                  @RequestBody @Parameter(description = "任务信息") ActivityInstanceDTO taskDTO) {
        BpmActivityInstanceBase actInst = activityTaskService.findActInstById(id);
        if (actInst != null) {
            BpmActivityInstanceState bpmActivityInstanceState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
            if (bpmActivityInstanceState == null) {
                bpmActivityInstanceState = new BpmActivityInstanceState();
                bpmActivityInstanceState.setBaiId(actInst.getId());
                bpmActivityInstanceState.setOrgId(orgId);
                bpmActivityInstanceState.setProjectId(projectId);
            }
            actInst.setOwnerId(taskDTO.getAssignee());
            actInst.setOwnerName(taskDTO.getAssigneeName());
            actInst.setPlanHour(taskDTO.getPlanHour());
            actInst.setPlanStartDate(taskDTO.getPlanStart());
            actInst.setPlanEndDate(taskDTO.getPlanEnd());
            bpmActivityInstanceState.setMemo(taskDTO.getMemo());
            actInst.setLastModifiedAt();
            bpmActivityInstanceStateRepository.save(bpmActivityInstanceState);
            return new JsonObjectResponseBody<>(getContext(),
                activityTaskService.saveActInst(actInst));
        }
        throw new NotFoundError();
    }

    @Override
    @Operation(
        summary = "导出每日汇总报告信息",
        description = "导出每日汇总报告信息。"
    )
    @RequestMapping(
        method = GET,
        value = "activities/summary-export",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    public void exportDailySummary(
        @PathVariable @Parameter(description = "组织ID") Long orgId,
        @PathVariable @Parameter(description = "项目ID") Long projectId
    ) throws IOException {
        final OperatorDTO operator = getContext().getOperator();
        File excel = null;//activityTaskService.saveSummaryFile(orgId, projectId, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"import-daily-summary.xlsx\""
            );

            IOUtils.copy(
                new FileInputStream(excel), response.getOutputStream()
            );

        } catch (FileNotFoundException e) {
            throw new NotFoundError();
        } catch (UnsupportedEncodingException e) {
            throw new BusinessError("", "文件编码不支持");
        } catch (IOException e) {
            throw new BusinessError("", "下载文件出错");
        }
        response.flushBuffer();
    }


    /**
     * 任务详情
     *
     * @param id 任务id
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "activities/{id}", consumes = ALL_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "任务详情", description = "查看任务详情")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ActInstDetailDTO> detail(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                           @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long id) {

        ActInstDetailDTO responseDTO = new ActInstDetailDTO();

        BpmActivityInstanceBase actInst = activityTaskService.findActInstById(id);

        List<DrawingDetail> drawingDetails = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(actInst.getEntityId(), actInst.getId());
        if (drawingDetails.size()>0){
            actInst.setVersion(drawingDetails.get(0).getRevNo());
        }else {
            List<DrawingDetail> detailListByEntityId = drawingDetailRepository.findByDrawingIdOrderByCreatedAtDesc(actInst.getEntityId());
            if (detailListByEntityId.size()>0){
                actInst.setVersion(detailListByEntityId.get(0).getRevNo());
            }
        }

        if (actInst != null) {

            responseDTO.setActInst(actInst);
            int bpmnVersion = actInst.getBpmnVersion();
            if (bpmnVersion == 0) {
                BpmReDeployment maxEntity = bpmReDeploymentRepository.findFirstByProjectIdAndProcessIdOrderByVersionDesc(projectId, actInst.getProcessId());
                bpmnVersion = maxEntity.getVersion();
            }


            DiagramResourceDTO diagramDTO = activityTaskService.getDiagramResource(projectId, actInst.getId(), actInst.getProcessId(), bpmnVersion);
            BpmReDeployment reDeployment = bpmReDeploymentRepository.findByProjectIdAndProcessIdAndVersion(projectId, actInst.getProcessId(), bpmnVersion);


            responseDTO.setDiagramResource(diagramDTO.getDiagramResource());
            if(null!=diagramDTO.getFileId()){
                responseDTO.setFileId(diagramDTO.getFileId());
            }

            List<ActHiTaskNodeDTO> hiTasks = new ArrayList<>();


            List<BpmActTask> bpmActTaskList = activityTaskService.findBpmActTaskByActInstIdOrderByCreatedAtAsc(actInst.getId());
            for (BpmActTask bpmActTask : bpmActTaskList) {

                ActHiTaskNodeDTO hitask = new ActHiTaskNodeDTO();

                BpmHiTaskinst hiTaskInst = activityTaskService.findHiTaskinstByTaskIdAndSeq(bpmActTask.getTaskId(), 0);

                if (hiTaskInst == null) {
                    hitask.setAssigneeName(bpmActTask.getOperatorName());
                    hitask.setStartTime(bpmActTask.getCreatedAt());
                    hitask.setEndTime(bpmActTask.getCreatedAt());
                    hitask.setTaskName(bpmActTask.getTaskId().toString());
                    hitask.setComment(bpmActTask.getComment());
                    hitask.setTaskType(bpmActTask.getTaskType());
                    hitask.setRevocation(false);
                } else {
                    hitask.setAssigneeName(hiTaskInst.getAssignee());
                    hitask.setStartTime(hiTaskInst.getStartTime());
                    hitask.setEndTime(hiTaskInst.getEndTime());
                    hitask.setTaskName(hiTaskInst.getName());
                    hitask.setTaskId(hiTaskInst.getTaskId());
                    hitask.setComment(hiTaskInst.getDescription());
                    hitask.setTaskType(hiTaskInst.getTaskType());
                    hitask.setRevocation(false);
                    hitask.setParentTaskId(hiTaskInst.getParentTaskId());
                    hitask.setTaskDefKey(hiTaskInst.getTaskDefKey());
                    hitask.setCode(hiTaskInst.getCode());
                }
                Long operator = 0L;
                if (hiTaskInst != null && hiTaskInst.getAssignee() != null) {
                    operator = LongUtils.parseLong(hiTaskInst.getAssignee());
                } else if (hiTaskInst != null && hiTaskInst.getOperator() != null) {
                    operator = LongUtils.parseLong(hiTaskInst.getOperator());
                } else if (hiTaskInst != null && hiTaskInst.getOwner() != null) {
                    operator = LongUtils.parseLong(hiTaskInst.getOwner());
                }

                if (bpmActTask.getTaskType() != null
                    && bpmActTask.getTaskType().equals("DRAWING_COSIGN")
                    && bpmActTask.getTaskDefKey() != null
                    && bpmActTask.getTaskDefKey().equals("usertask-USER-DRAWING-COSIGN")) {
                    hitask.setTaskName("CO-SIGN");
                    hitask.setTaskId(bpmActTask.getTaskId());
                    hitask.setTaskDefKey(bpmActTask.getTaskDefKey());

                    operator = bpmActTask.getOperatorId();
                }

                String name = "";

                try {
                    JsonObjectResponseBody<UserProfile> userResponse = userFeignAPI.get(operator);
                    name += ", " + userResponse.getData().getName();
                } catch (Exception e) {
                    JsonObjectResponseBody<Role> roleResponse = roleFeignAPI.get(orgId, operator);
                    if (roleResponse.getData() != null) {
                        name += ", " + roleResponse.getData().getName();
                    }
                }

                if (name.length() > 0) {
                    name = name.substring(2);
                }
                hitask.setAssigneeName(name);


                hitask.setDocuments(bpmActTask.getJsonDocumentsReadOnly());
                hitask.setHour(bpmActTask.getCostHour());
                hitask.setComment(bpmActTask.getComment());
                if (bpmActTask.getAttachments() != null) {
                    try {
                        hitask.setAttachments(StringUtils.fromJSON(bpmActTask.getAttachments(), List.class));
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                    }
                }
                if (bpmActTask.getPictures() != null) {
                    try {
                        hitask.setPictures(StringUtils.fromJSON(bpmActTask.getPictures(), List.class));
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                    }
                }

                if (hitask.getTaskName().equals("会签")) {
                    String assigneeName = "";
                    List<BpmDrawingSignTask> signTasks = activityTaskService.findBpmDrawingSignTask(hitask.getTaskId());
                    if (!signTasks.isEmpty()) {
                        for (BpmDrawingSignTask signTask : signTasks) {
                            assigneeName += "," + signTask.getAssigneeName();
                        }
                    }
                    if (!assigneeName.equals("")) {
                        hitask.setAssigneeName(assigneeName.substring(1));
                    }
                }

                if (hitask.getTaskName().equals("CO-SIGN")) {
                    String assigneeName = "";
                    List<BpmDrawingSignTask> signTasks = activityTaskService.findBpmDrawingSignTask(hitask.getTaskId());
                    if (!signTasks.isEmpty()) {
                        for (BpmDrawingSignTask signTask : signTasks) {
                            assigneeName += "," + signTask.getAssigneeName();
                        }
                    }
                    if (!assigneeName.equals("")) {
                        hitask.setAssigneeName(assigneeName.substring(1));
                    }
                }

                hiTasks.add(hitask);

            }

            if (hiTasks.size() > 0
                && hiTasks.get(hiTasks.size() - 1).getRemark() != null
                && !hiTasks.get(hiTasks.size() - 1).getRemark().contains("REVOCATION")) {
                hiTasks.get(hiTasks.size() - 1).setRevocation(true);
            }

            List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInst.getId());
            if (!CollectionUtils.isEmpty(ruTasks)) {

                String taskDefKey = "";

                for (BpmRuTask ruTask : ruTasks) {
                    ActHiTaskNodeDTO hitask = new ActHiTaskNodeDTO();
                    if (ruTask.getAssignee() != null && !ruTask.getAssignee().equals("")) {
                        Long assigneeId = LongUtils.parseLong(ruTask.getAssignee());
                        String name = "";

                        try {
                            JsonObjectResponseBody<UserProfile> userResponse = userFeignAPI.get(assigneeId);
                            name += ", " + userResponse.getData().getName();
                        } catch (Exception e) {
                            JsonObjectResponseBody<Role> roleResponse = roleFeignAPI.get(orgId, assigneeId);
                            if (roleResponse.getData() != null) {
                                name += ", " + roleResponse.getData().getName();
                            }
                        }
                        if (name.length() > 0) {
                            name = name.substring(2);
                        }
                        hitask.setAssigneeName(name);
                    }

                    if (ruTask.getName().equals(CO_SIGN)) {
                        String assigneeName = "";
                        String commond = "";
                        List<BpmDrawingSignTask> signTasks = activityTaskService.findBpmDrawingSignTask(ruTask.getId());
                        if (!signTasks.isEmpty()) {
                            for (BpmDrawingSignTask signTask : signTasks) {
                                assigneeName += "," + signTask.getAssigneeName();
                                if (signTask.isFinished()) {
                                    commond += signTask.getAssigneeName() + ":" + signTask.getCommand() + "\r\n";
                                } else {
                                    commond += signTask.getAssigneeName() + ":cosign \r\n";
                                }
                            }
                        }
                        if (!assigneeName.equals("")) {
                            hitask.setAssigneeName(assigneeName.substring(1));
                            hitask.setComment(commond);
                        }
                    }

                    taskDefKey = ruTask.getTaskDefKey();

                    hitask.setStartTime(ruTask.getCreateTime());
                    hitask.setTaskName(ruTask.getName());
                    hitask.setTaskId(ruTask.getId());
                    hitask.setTaskDefKey(ruTask.getTaskDefKey());

                    hiTasks.add(hitask);
                }


                if (taskDefKey.equals(BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_RECEIVE.getType())
                    || taskDefKey.equals(BpmTaskDefKey.USERTASK_MATERIAL_ISSUE_CREATE_REQUISITION.getType())) {
                    for (ActHiTaskNodeDTO hiDto : hiTasks) {
                        if (!StringUtils.isBlank(hiDto.getTaskDefKey())
                            && MATERIAL_REVOCATION_NODE.contains(hiDto.getTaskDefKey())
                            && !taskDefKey.equals(hiDto.getTaskDefKey())) {
                            hiDto.setRevocation(true);
                        }
                    }
                }

            }


            responseDTO.setHiTasks(hiTasks);

            List<BpmEntityDocsMaterials> dms = activityTaskService
                .getDocsMaterialsByProcessIdAndEntityIdAndActInstanceId(actInst.getProcessId(), actInst.getEntityId(), actInst.getId());
            if (!dms.isEmpty()) {
                List<BpmEntityDocsDTO> attachments = new ArrayList<>();
                for (BpmEntityDocsMaterials dm : dms) {
                    List<ActReportDTO> file = dm.getJsonDocsReadOnly();
                    if (file != null
                        && !file.isEmpty()) {
                        BpmEntityDocsDTO bpmEntityDocsDTO = new BpmEntityDocsDTO();
                        bpmEntityDocsDTO.setFileId(file.get(0).getFileId());
                        bpmEntityDocsDTO.setFilePath(file.get(0).getFilePath());
                        bpmEntityDocsDTO.setReportQrCode(file.get(0).getReportQrCode());
                        bpmEntityDocsDTO.setReportNo(file.get(0).getReportNo());
                        bpmEntityDocsDTO.setType(dm.getType());
                        bpmEntityDocsDTO.setCreateTime(dm.getCreatedAt());
                        if (dm.getOperator() != null) {
                            bpmEntityDocsDTO.setOperator(userFeignAPI.get(dm.getOperator()).getData().getName());
                        }
                        attachments.add(bpmEntityDocsDTO);
                    }
                }
                responseDTO.setActInstAttachments(attachments);
            }

            responseDTO.setMaterials(activityTaskService.getEntityMaterialByEntityIdAndEntityType(orgId, projectId,
                actInst.getEntityId(), actInst.getEntityType()));

            responseDTO.setDrawings(activityTaskService.getEntityDrawingByEntityIdAndEntityType(orgId, projectId,
                actInst.getEntityId(), actInst.getEntityType(), actInst));

            List<Map<String, Object>> variablesResult = new ArrayList<>();

            if (reDeployment != null) {
                String processKey = reDeployment.getProcessId().toString();

                List<Map<String, Object>> variables = new ArrayList<>();

                List<BpmActInstVariableConfig> variableList = activityTaskService.findActInstVariables(orgId, projectId,
                    LongUtils.parseLong(processKey.replace("_", "")));
                for (BpmActInstVariableConfig variable : variableList) {

                    BpmActInstVariableValue value = activityTaskService.findBpmActInstVariableValue(orgId, projectId,
                        actInst.getId(), variable.getName());

                    Map<String, Object> m = new HashMap<>();
                    m.put("name", variable.getName());
                    m.put("displayName", variable.getDisplayName());
                    m.put("type", variable.getType());
                    if (value != null) {
                        m.put("value", value.getValue());
                    }
                    variables.add(m);
                }

                Map<String, Object> map = new HashMap<>();
                map.put("variables", variables);
                variablesResult.add(map);
            }
            responseDTO.setVariables(variablesResult);
            return new JsonObjectResponseBody<>(getContext(), responseDTO);
        }
        throw new NotFoundError();
    }

    /**
     * 分配任务页面
     *
     * @param id 任务id
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "activities/{id}/tasks")
    @Operation(summary = "分配任务信息", description = "获取分配人物页面数据")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ActInstAssigneeDTO> activityTasks(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                    @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long id) {

        ActInstAssigneeDTO responseDTO = new ActInstAssigneeDTO();

        BpmActivityInstanceBase actInst = activityTaskService.findActInstById(id);
        if (actInst != null) {

            responseDTO.setActInst(actInst);
            int bpmnVersion = actInst.getBpmnVersion();
            if (bpmnVersion == 0) {
                BpmReDeployment maxEntity = bpmReDeploymentRepository.findFirstByProjectIdAndProcessIdOrderByVersionDesc(projectId, actInst.getProcessId());
                bpmnVersion = maxEntity.getVersion();
            }

            DiagramResourceDTO diagramDTO = activityTaskService
                .getDiagramResource(projectId, actInst.getId(), actInst.getProcessId(), bpmnVersion);
            responseDTO.setDiagramResource(diagramDTO.getDiagramResource());

            List<BpmActTaskAssignee> taskAssignees = activityTaskService
                .findActTaskAssigneesByActInstId(actInst.getId());
            List<BpmHiTaskinst> hiTasks = hiTaskinstRepository.findByActInstId(actInst.getId());

            for (BpmActTaskAssignee assignee : taskAssignees) {
                for (BpmHiTaskinst hitask : hiTasks) {
                    if (assignee.getTaskDefKey().equals(hitask.getTaskDefKey()) && hitask.getEndTime() != null) {
                        assignee.setExecuted(true);
                    }
                }
            }

            responseDTO.setTaskAssignees(taskAssignees);

            return new JsonObjectResponseBody<>(getContext(), responseDTO);
        }
        throw new NotFoundError();
    }

    /**
     * 设置任务节点担当人
     *
     * @param id 任务id
     * @return 实体信息
     */
    @RequestMapping(method = POST, value = "activities/{id}/tasks/{taskAssigneeId}/users/{userid}")
    @Operation(summary = "设置任务节点担当人", description = "设置任务节点担当人")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody activityTaskAssignee(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                 @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long id,
                                                 @PathVariable @Parameter(description = "任务节点id") Long taskAssigneeId, @PathVariable @Parameter(description = "用户id") Long userid) {

        String name = "";
        try {
            JsonObjectResponseBody<UserProfile> userResponse = userFeignAPI.get(userid);
            name += ", " + userResponse.getData().getName();
        } catch (Exception e) {
            JsonObjectResponseBody<Role> roleResponse = roleFeignAPI.get(orgId, userid);
            if (roleResponse.getData() != null) {
                name += ", " + roleResponse.getData().getName();
            }
        }

        if (name.length() > 0) {
            name = name.substring(2);
        }

        /*
         * JsonObjectResponseBody<UserProfile> userResponse = userFeignAPI.get(userid);
         *
         * String userName = userResponse.getData().getName();
         */

        BpmActTaskAssignee actAssignee = activityTaskService.findActTaskAssigneesById(taskAssigneeId);
        if (actAssignee != null) {
            // 判断"相关人员确认现场标记图"是否设置有执行人记录
            if (actAssignee.getTaskDefKey().equals("usertask-REDMARK-RECEIPT")) {
                List<BpmDrawingSignTask> byActInstIdAndFinished = bpmDrawingSignTaskRepository.findByActInstIdAndFinished(actAssignee.getActInstId(), false);
                if (byActInstIdAndFinished.size() == 1) {
                    byActInstIdAndFinished.get(0).setAssignee(userid);
                    byActInstIdAndFinished.get(0).setAssigneeName(name);
                } else if (byActInstIdAndFinished.size() > 1) {
                    throw new BusinessError("'相关人员确认现场标记图'节点有多个执行人,不可重新分配!");
                }
            }

            BpmActivityInstanceBase actInst = activityTaskService.findActInstById(id);
            if (actInst != null) {
                BpmActivityInstanceState bpmActivityInstanceState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
                if (bpmActivityInstanceState == null) {
                    bpmActivityInstanceState = new BpmActivityInstanceState();
                    bpmActivityInstanceState.setBaiId(actInst.getId());
                    bpmActivityInstanceState.setOrgId(orgId);
                    bpmActivityInstanceState.setProjectId(projectId);
                }


                OperatorDTO operatorDTO = getContext().getOperator();
                activityTaskService.modifyTaskAssignee(taskAssigneeId, userid, name);//, operatorDTO);
                activityTaskService.assignee(actInst.getId(), actAssignee.getTaskDefKey(),
                    actAssignee.getTaskName(), userid, operatorDTO.getId().toString());
                List<BpmRuTask> ruTaskList = activityTaskService.findBpmRuTaskByActInstId(actInst.getId());
                String currentExecutor = "";

                currentExecutor = todoTaskBaseService.setTaskAssignee(ruTaskList);

                bpmActivityInstanceState.setCurrentExecutor(currentExecutor);
                bpmActivityInstanceStateRepository.save(bpmActivityInstanceState);

                if ((taskRuleCheckService.isDrawingDesignTaskNode(actAssignee.getTaskDefKey()) || taskRuleCheckService.isRedMarkDesignTaskNode(actAssignee.getTaskDefKey()))
                    && taskRuleCheckService.DESIGN_PROCESSES.contains(actInst.getProcess())) {
                    Drawing dwg = drawingService.findByDwgNo(orgId, projectId, actInst.getEntityNo());
//                    dwg.setDrawUsername(name);
//                    dwg.setDrawUserId(userid);
                    drawingService.save(dwg);

                    DrawingEntryDelegate drawingEntryDelegate = drawingEntryDelegateRepository
                        .findByDrawingIdAndPrivilegeAndStatus(dwg.getId(), UserPrivilege.getByName(actAssignee.getTaskCategory()), EntityStatus.ACTIVE);
                    if (drawingEntryDelegate == null) {
                        drawingEntryDelegate = new DrawingEntryDelegate();
                        drawingEntryDelegate.setCreatedAt();
                        drawingEntryDelegate.setCreatedBy(getContext().getOperator().getId());
                        drawingEntryDelegate.setDrawingId(id);
                        drawingEntryDelegate.setPrivilege(UserPrivilege.getByName(actAssignee.getTaskCategory()));
                        drawingEntryDelegate.setStatus(EntityStatus.ACTIVE);
                    }
                    drawingEntryDelegate.setUserId(userid);
                    drawingEntryDelegate.setLastModifiedAt();
                    drawingEntryDelegate.setLastModifiedBy(getContext().getOperator().getId());
                    drawingEntryDelegateRepository.save(drawingEntryDelegate);
                }

                if ((taskRuleCheckService.isDrawingCheckTaskNode(actAssignee.getTaskDefKey()) || taskRuleCheckService.isRedMarkCheckTaskNode(actAssignee.getTaskDefKey()))
                    && taskRuleCheckService.DESIGN_PROCESSES.contains(actInst.getProcess())) {
                    Drawing dwg = drawingService.findByDwgNo(orgId, projectId, actInst.getEntityNo());
//                    dwg.setCheckUserId(userid);
//                    dwg.setCheckUsername(name);
                    drawingService.save(dwg);

                    DrawingEntryDelegate drawingEntryDelegate = drawingEntryDelegateRepository
                        .findByDrawingIdAndPrivilegeAndStatus(dwg.getId(), UserPrivilege.getByName(actAssignee.getTaskCategory()), EntityStatus.ACTIVE);
                    if (drawingEntryDelegate == null) {
                        drawingEntryDelegate = new DrawingEntryDelegate();
                        drawingEntryDelegate.setCreatedAt();
                        drawingEntryDelegate.setCreatedBy(getContext().getOperator().getId());
                        drawingEntryDelegate.setDrawingId(id);
                        drawingEntryDelegate.setPrivilege(UserPrivilege.getByName(actAssignee.getTaskCategory()));
                        drawingEntryDelegate.setStatus(EntityStatus.ACTIVE);
                    }
                    drawingEntryDelegate.setUserId(userid);
                    drawingEntryDelegate.setLastModifiedAt();
                    drawingEntryDelegate.setLastModifiedBy(getContext().getOperator().getId());
                    drawingEntryDelegateRepository.save(drawingEntryDelegate);
                }

                if ((taskRuleCheckService.isDrawingApprovedTaskNode(actAssignee.getTaskDefKey()))
                    && taskRuleCheckService.DESIGN_PROCESSES.contains(actInst.getProcess())) {
                    Drawing dwg = drawingService.findByDwgNo(orgId, projectId, actInst.getEntityNo());
//                    dwg.setApprovedUsername(name);
//                    dwg.setApprovedUserId(userid);
                    drawingService.save(dwg);

                    DrawingEntryDelegate drawingEntryDelegate = drawingEntryDelegateRepository
                        .findByDrawingIdAndPrivilegeAndStatus(dwg.getId(), UserPrivilege.getByName(actAssignee.getTaskCategory()), EntityStatus.ACTIVE);
                    if (drawingEntryDelegate == null) {
                        drawingEntryDelegate = new DrawingEntryDelegate();
                        drawingEntryDelegate.setCreatedAt();
                        drawingEntryDelegate.setCreatedBy(getContext().getOperator().getId());
                        drawingEntryDelegate.setDrawingId(id);
                        drawingEntryDelegate.setPrivilege(UserPrivilege.getByName(actAssignee.getTaskCategory()));
                        drawingEntryDelegate.setStatus(EntityStatus.ACTIVE);
                    }
                    drawingEntryDelegate.setUserId(userid);
                    drawingEntryDelegate.setLastModifiedAt();
                    drawingEntryDelegate.setLastModifiedBy(getContext().getOperator().getId());
                    drawingEntryDelegateRepository.save(drawingEntryDelegate);
                }

                return new JsonResponseBody();
            }
            throw new NotFoundError();
        }
        throw new NotFoundError();
    }


    /**
     * 生成移交任务记录
     *
     * @param id 任务id
     * @return 实体信息
     */
    @RequestMapping(method = POST, value = "activities/{id}/tasks/{taskAssigneeId}/users/{userid}/shift")
    @Operation(summary = "设置任务节点担当人", description = "设置任务节点担当人")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody generateActivityShiftLog(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                     @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long id,
                                                     @PathVariable @Parameter(description = "任务节点id") Long taskAssigneeId, @PathVariable @Parameter(description = "用户id") Long userid,
                                                     @RequestBody ShiftLogDTO log) {

        BpmActivityInstanceShiftLog item = BeanUtils.copyProperties(log,new BpmActivityInstanceShiftLog());
        item.setShiftDate(new Date());
        bpmActivityInstanceShiftRepository.save(item);
        return new JsonResponseBody();
    }


    /**
     * 生成移交任务记录
     *
     * @param id 任务id
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "shift/{id}/logs")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActivityInstanceShiftLog> shiftLogs(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                     @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long id) {


        return new JsonListResponseBody<>(getContext(),
            activityTaskService.getShiftLogs(orgId, projectId, id));
    }

    /**
     * 获取任务管理页面层级数据
     */
    @RequestMapping(method = GET, value = "activities/hierarchy")
    @Operation(summary = "获取任务管理页面层级数据", description = "获取任务管理页面层级数据")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<TaskHierarchyDTO> getActivetyHierarchy(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                         @PathVariable @Parameter(description = "项目id") Long projectId, HierarchyCriteriaDTO criteriaDTO) {

        TaskHierarchyDTO dto = new TaskHierarchyDTO();

        if (criteriaDTO.getProcessStageId() == null) {

            List<HierarchyBaseDTO> processStages = activityTaskService.getProcessStagesInActivity(
                orgId,
                projectId
            );

            dto.setProcessStages(processStages);

        } else if (criteriaDTO.getProcessStageId() != null
            && criteriaDTO.getProcessId() == null) {

            List<HierarchyBaseDTO> processes = activityTaskService.getProcessesInActivity(
                orgId,
                projectId,
                criteriaDTO
            );

            dto.setProcesses(processes);

        } else if (criteriaDTO.getProcessStageId() != null
            && criteriaDTO.getProcessId() != null
            && criteriaDTO.getTaskNode() == null) {

            List<HierarchyBaseDTO> taskDefKeys = activityTaskService.getTaskDefKeys(
                orgId,
                projectId,
                criteriaDTO.getProcessStageId(),
                criteriaDTO.getProcessId()
            );

            dto.setTaskDefKey(taskDefKeys);
        } else if (criteriaDTO.getProcessStageId() != null
            && criteriaDTO.getProcessId() != null
            && criteriaDTO.getTaskNode() != null
            && criteriaDTO.getEntityTypeId() == null) {

            List<HierarchyBaseDTO> entitiyCategoryTypes = activityTaskService.getEntitiyCategoryTypesInActivity(
                orgId,
                projectId,
                criteriaDTO.getProcessStageId(),
                criteriaDTO.getProcessId()
            );

            dto.setEntityTypes(entitiyCategoryTypes);

        } else if (criteriaDTO.getProcessStageId() != null
            && criteriaDTO.getProcessId() != null
            && criteriaDTO.getTaskNode() != null
            && criteriaDTO.getEntityTypeId() != null
            && criteriaDTO.getEntitySubTypeId() == null) {

            List<HierarchyBaseDTO> entitiyCategories = activityTaskService.getEntitiyCategoriesInActivity(
                orgId,
                projectId,
                criteriaDTO.getProcessStageId(),
                criteriaDTO.getProcessId(),
                criteriaDTO.getEntityTypeId()
            );

            dto.setEntityCategories(entitiyCategories);

        }

        return new JsonObjectResponseBody<>(getContext(), dto);
    }

    /**
     * 暂停任务流程
     */
    @RequestMapping(method = POST, value = "activities/{id}/suspend")
    @Operation(summary = "暂停任务流程", description = "暂停任务流程")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody suspend(@PathVariable @Parameter(description = "orgId") Long orgId,
                                    @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long id,
                                    @RequestBody ActInstSuspendDTO dto) {
        ContextDTO context = getContext();
        return taskExecService.suspendTask(context, orgId, projectId, id, dto);
    }

    /**
     * 激活任务流程
     */
    @RequestMapping(method = POST, value = "activities/{id}/active")
    @Operation(summary = "激活任务流程", description = "激活任务流程")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody active(@PathVariable @Parameter(description = "orgId") Long orgId,
                                   @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long id,
                                   @RequestBody ActInstSuspendDTO dto) {
        OperatorDTO operatorDTO = getContext().getOperator();
        BpmActivityInstanceBase actInst = activityTaskService.findActInstById(id);
        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(id);
        BpmRuTask ruTask = bpmRuTaskRepository.findFirstByActInstId(actInst.getId());
        if (actInstState != null || actInst != null) {

            actInstState.setSuspensionState(SuspensionState.ACTIVE);
            actInstState.setMemo(StringUtils.trim(actInstState.getMemo()) + ACTIVE_MARK + dto.getMemo());
            actInstState.setLastModifiedAt(new Date());
            ruTask.setSuspensionState(1);
            bpmRuTaskRepository.save(ruTask);
            bpmActivityInstanceStateRepository.save(actInstState);
            activityTaskService.createSuspensionTaskNode(actInst, dto, SuspensionState.ACTIVE, operatorDTO);

            return new JsonResponseBody();

        } else {
            throw new NotFoundError();
        }
    }

    /**
     * 上传任务文档
     */
    @RequestMapping(method = POST, value = "activities/{id}/docs")
    @Operation(summary = "上传任务文档", description = "上传任务文档")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody docs(@PathVariable @Parameter(description = "orgId") Long orgId,
                                 @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long id,
                                 @RequestBody @Parameter(description = "临时文件名列表") ActInstAttachmentDTO attachDTO) {

        BpmActivityInstanceBase actInst = activityTaskService.findActInstById(id);
        if (actInst != null) {
            List<String> temporaryNames = attachDTO.getTemporaryNames();
            for (String temporaryName : temporaryNames) {
                logger.error("流程1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), temporaryName,
                    new FilePostDTO());
                logger.error("流程1 保存docs服务->结束");
                FileES fileEs = fileESResBody.getData();
                if (fileEs != null && fileEs.getId() != null) {
                    BpmEntityDocsMaterials bedm = new BpmEntityDocsMaterials();
                    bedm.setProjectId(actInst.getProjectId());
                    bedm.setEntityNo(actInst.getEntityNo());
                    bedm.setProcessId(actInst.getProcessId());
                    bedm.setCreatedAt();
                    bedm.getLastModifiedAt();
                    bedm.setStatus(EntityStatus.ACTIVE);
                    bedm.setEntityId(actInst.getEntityId());
                    bedm.setType(ActInstDocType.UPLOAD_FILE);

                    List<ActReportDTO> list = new ArrayList<>();
                    ActReportDTO dto = new ActReportDTO();
                    dto.setFileId(
                        LongUtils.parseLong(fileEs.getId()
                        ));
                    list.add(dto);
                    bedm.setJsonDocs(list);

                    activityTaskService.saveDocsMaterials(bedm);
                }
            }
            return new JsonResponseBody();
        }
        throw new NotFoundError();
    }

    @RequestMapping(method = GET, value = "activities/variables/{processId}")
    @Operation(summary = "根据工序名称获取模型变量", description = "工序名称为'工序阶段-工序'格式, 例如'管道预制-组对'")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActInstVariableConfig> getActInstVariables(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "工序Id") Long processId) {
        return new JsonListResponseBody<>(getContext(),
            activityTaskService.getActInstVariablesByProcessId(orgId, projectId, processId));
    }

    @RequestMapping(method = POST, value = "/activities/{actInstId}/revocation")
    @Operation(summary = "只撤回上一步任务", description = "撤回已完成的任务节点")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody revocation(@PathVariable @Parameter(description = "组织id") Long orgId,
                                       @PathVariable @Parameter(description = "项目id") Long projectId,
                                       @PathVariable @Parameter(description = "任务流程id") Long actInstId) {

        BpmActivityInstanceBase actInst = activityTaskService.findActInstById(actInstId);

        if (actInst == null) {
            throw new ValidationError("Not found activity instance");

        }
        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInstId);
        ActInstSuspendDTO actInstSuspendDTO = new ActInstSuspendDTO();
        actInstSuspendDTO.setActInst(actInst);
        actInstSuspendDTO.setActInstState(actInstState);


        todoTaskDispatchService.revocation(getContext(), orgId, projectId, actInstSuspendDTO);

        return new JsonResponseBody();
    }

    @RequestMapping(method = POST, value = "/activities/{actInstId}/revocation/{taskDefKey}")
    @Operation(summary = "撤回到指定节点", description = "撤回已完成的任务节点")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody revocationNode(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "任务流程id") Long actInstId,
        @PathVariable @Parameter(description = "taskDefKey") String taskDefKey,
        @RequestBody ActInstSuspendDTO actInstSuspendDTO) {

        Set<String> batchTaskNode = new HashSet<String>() {{


        }};

        BpmActivityInstanceBase actInst = activityTaskService.findActInstById(actInstId);

        if (actInst == null) {
            throw new ValidationError("Not found activity instance");
        }

        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInstId);
//        Long actInstId = actInst.getId();

        List<BpmRuTask> currentRuTasks = activityTaskService.findBpmRuTaskByActInstId(actInstId);
        if (currentRuTasks.size() > 1) {
            throw new BusinessError("正在执行任务数量大于1，不能进行撤回操作");
        }

        actInstSuspendDTO.setActInst(actInst);
        actInstSuspendDTO.setActInstState(actInstState);
        actInstSuspendDTO.setTaskDefKey(taskDefKey);
        if (taskDefKey.equalsIgnoreCase("UT-COORDINATE_EXTERNAL_INSPECTION_APPLICATION")) {
            actInstSuspendDTO.setTaskType(EX_INSP_APPLY_MAIL.name());
        }

        actInstSuspendDTO.setTaskDefKey(taskDefKey);
        if (taskDefKey.equalsIgnoreCase("UT-CHECK_EXTERNAL_INSPECTION_REPORT")) {
            actInstSuspendDTO.setTaskType(EX_INSP_HANDLE_REPORT.name());
        }

        actInstSuspendDTO.setTaskDefKey(taskDefKey);
        if (taskDefKey.equalsIgnoreCase("UT-RECHECK_CLASS_B_REPORT")) {
            actInstSuspendDTO.setTaskType(EX_INSP_REHANDLE_REPORT.name());
        }

        if (batchTaskNode.contains(taskDefKey)) {
            todoTaskDispatchService.batchRevocation(getContext(), orgId, projectId, actInstSuspendDTO);
        } else {

            RevocationDTO revocationDTO = todoTaskDispatchService.revocation(getContext(), orgId, projectId, actInstSuspendDTO);
        }
        return new JsonResponseBody();
    }

    @Override
    @Operation(summary = "删除任务", description = "删除工作流任务。")
    @WithPrivilege
    @RequestMapping(method = DELETE, value = "activities/{id}")
    public JsonResponseBody delete(
        @PathVariable("orgId") @Parameter(description = "组织id") Long orgId,
        @PathVariable("projectId") @Parameter(description = "项目id") Long projectId,
        @PathVariable("id") @Parameter(description = "id") Long id) {
        activityTaskService.deleteActInst(id,getContext().getOperator());
        return new JsonResponseBody();
    }


    @RequestMapping(method = POST, value = "activities/batch-tasks-category-assignee-list")
    @Operation(summary = "批量查询任务权限分配", description = "批量查询任务权限分配")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<BatchTasksCategorySearchResultDTO> batchTasksCategorySearchList(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody BatchTasksCategorySearchDTO searchDTO) {
        return new JsonObjectResponseBody<>(
            activityTaskService.batchTasksCategorySearch(
                orgId,
                projectId,
                searchDTO
            )
        );
    }

    @RequestMapping(method = GET, value = "activities/batch-tasks-category-assignee")
    @Operation(summary = "批量查询任务权限分配", description = "批量查询任务权限分配")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<BatchTasksCategorySearchResultDTO> batchTasksCategorySearch(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        BatchTasksCategorySearchDTO searchDTO) {
        return new JsonObjectResponseBody<>(
            activityTaskService.batchTasksCategorySearch(
                orgId,
                projectId,
                searchDTO
            )
        );
    }

    @RequestMapping(method = POST, value = "activities/batch-tasks-category-assignee")
    @Operation(summary = "批量任务权限分配", description = "批量任务权限分配")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody batchTasksCategoryAssignee(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody BatchTasksCategoryAssigneeDTO assigneeDTO) {
        ContextDTO contextDTO = getContext();
        activityTaskService.batchTasksCategoryAssignee(orgId, projectId, assigneeDTO);//, contextDTO);
        return new JsonResponseBody();
    }

    @RequestMapping(method = GET, value = "activities/team-worksite")
    @Operation(summary = "查询流程工作组工作场地信息", description = "查询流程工作组工作场地信息")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ActInstTeamWorkSiteDTO> getTeamWorkSite(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        BatchTasksCategorySearchDTO searchDTO) {
        return new JsonObjectResponseBody<>(
            activityTaskService.getTeamWorkSite(
                orgId,
                projectId,
                searchDTO
            )
        );
    }

    @RequestMapping(method = POST, value = "activities/team-worksite-list")
    @Operation(summary = "查询流程工作组工作场地信息post方法", description = "查询流程工作组工作场地信息post方法")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ActInstTeamWorkSiteDTO> getTeamWorkSiteList(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody BatchTasksCategorySearchDTO searchDTO) {
        return new JsonObjectResponseBody<>(
            activityTaskService.getTeamWorkSite(
                orgId,
                projectId,
                searchDTO
            )
        );
    }

    @RequestMapping(method = POST, value = "activities/team-worksite")
    @Operation(summary = "指定流程工作组工作场地信息", description = "指定流程工作组攻错场地信息")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody setTeamWorkSite(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody ActInstTeamWorkSiteAssigneeDTO assigneeDTO) {
        activityTaskService.setTeamWorkSite(orgId, projectId, assigneeDTO);
        return new JsonResponseBody();
    }

    /**
     * 按条件下导出任务管理列表。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     */
    @RequestMapping(
        method = GET,
        value = "activity-task/download"
    )
    @Operation(description = "按条件下导出任务管理列表")
    @WithPrivilege
    @Override
    public synchronized void downloadActivtityTask(@PathVariable("orgId") Long orgId,
                                                   @PathVariable("projectId") Long projectId,
                                                   ActInstCriteriaDTO criteriaDTO) throws IOException {


        final OperatorDTO operator = getContext().getOperator();
        File excel = null;//activityTaskService.saveDownloadFile(orgId, projectId, criteriaDTO, operator.getId());

        HttpServletResponse response = getContext().getResponse();
        try {
            response.setContentType(APPLICATION_OCTET_STREAM_VALUE);

            response.setHeader(
                CONTENT_DISPOSITION,
                "attachment; filename=\"activity-tasks-export.xlsx\""
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

    /**
     * 获取待办任务列表
     */
    @RequestMapping(method = GET, value = "activity-task/history")
    @Operation(summary = "查询分配记录", description = "查询分配记录。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActTaskAssigneeHistory> searchHistory(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                         @PathVariable @Parameter(description = "项目id") Long projectId,
                                                                         TaskAssigneHistoryDTO taskAssigneHistoryDTO,
                                                                         PageDTO pageDTO) {

        Page<BpmActTaskAssigneeHistory> ruTasks = null;//  activityTaskService.searchTaskHistory(orgId, projectId, taskAssigneHistoryDTO, pageDTO);


        return new JsonListResponseBody<>(ruTasks);

    }

    /**
     * 获取工序
     *
     * @param orgId 组织ID
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "activities/processes")
    @Operation(summary = "工作流管理任务列表", description = "查询工序列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<TaskProcessDTO> getProcesses(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                             @PathVariable @Parameter(description = "项目Id") Long projectId) {
        return new JsonListResponseBody();
//            activityTaskService.getProcess(orgId, projectId));
    }

}
