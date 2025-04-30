package com.ose.tasks.controller.bpm;

import com.ose.auth.annotation.WithPrivilege;
import com.ose.auth.api.PrivilegeFeignAPI;
import com.ose.auth.api.RoleFeignAPI;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.UserPrivilegeDTO;
import com.ose.auth.entity.Role;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.vo.UserPrivilege;
import com.ose.controller.BaseController;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonListResponseBody;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.api.bpm.TodoTaskAPI;
import com.ose.tasks.domain.model.repository.bpm.BpmActTaskRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.*;
import com.ose.tasks.dto.*;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.bpm.MaterialInfoDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.vo.SuspensionState;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.*;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.ALL_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Tag(name = "流程任务管理接口")
@RestController
@RequestMapping(value = "/orgs/{orgId}/projects/{projectId}/")
public class TodoTaskController extends BaseController implements TodoTaskAPI {

    private final static Logger logger = LoggerFactory.getLogger(TodoTaskController.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    private final BatchTaskInterface batchTaskService;

    /**
     * 任务管理服务
     */
    private final UserFeignAPI userFeignAPI;

    private final UploadFeignAPI uploadFeignAPI;

    private final RoleFeignAPI roleFeignAPI;

    private final BpmActivityInstanceRepository actInstRepository;

    private final PrivilegeFeignAPI privilegeFeignAPI;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final TodoIndividualTaskInterface todoIndividualTaskService;

    private final ActivityTaskInterface activityTaskService;

    private final ProjectInterface projectService;

    private final SearchTaskInterface searchTaskService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final BpmActTaskRepository bpmActTaskRepository;

    private final BpmRuTaskRepository ruTaskRepository;


    /**
     * 构造方法
     */
    @Autowired
    public TodoTaskController(
        BatchTaskInterface batchTaskService, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    RoleFeignAPI roleFeignAPI,
        ActivityTaskInterface activityTaskService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        UserFeignAPI userFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        UploadFeignAPI uploadFeignAPI,
        BpmActivityInstanceRepository actInstRepository, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
        PrivilegeFeignAPI privilegeFeignAPI,
        TodoTaskBaseInterface todoTaskBaseService,
        TodoIndividualTaskInterface todoIndividualTaskService,
        ProjectInterface projectService,
        SearchTaskInterface searchTaskService,
        TodoTaskDispatchInterface todoTaskDispatchService,
        BpmActTaskRepository bpmActTaskRepository, BpmRuTaskRepository ruTaskRepository) {
        this.batchTaskService = batchTaskService;
        this.userFeignAPI = userFeignAPI;
        this.uploadFeignAPI = uploadFeignAPI;
        this.roleFeignAPI = roleFeignAPI;
        this.activityTaskService = activityTaskService;
        this.actInstRepository = actInstRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.todoIndividualTaskService = todoIndividualTaskService;
        this.projectService = projectService;
        this.privilegeFeignAPI = privilegeFeignAPI;
        this.searchTaskService = searchTaskService;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.bpmActTaskRepository = bpmActTaskRepository;
        this.ruTaskRepository = ruTaskRepository;
    }

    /**
     * 获取待办任务列表
     */
    @RequestMapping(method = GET, value = "tasks")
    @Operation(summary = "查询待办任务列表", description = "根据条件查询待办任务列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<TodoTaskDTO> searchTodo(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                        @PathVariable @Parameter(description = "项目id") Long projectId, TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO) {
        Long assignee = getContext().getOperator().getId();

        Page<TodoTaskDTO> ruTasks = searchTaskService.searchTodo(orgId, projectId, assignee, taskCriteria, pageDTO);


        return new JsonListResponseBody<>(ruTasks);

    }

    /**
     * 工作流任务节点网关
     *
     * @param orgId 组织ID
     * @return 实体信息
     */
    @RequestMapping(method = GET, value = "todo-task/{taskId}/gateway")
    @Operation(summary = "工作流任务节点网关", description = "根据条件信息，查询任务网关列表。")
    @WithPrivilege
    @Override
    public JsonListResponseBody<TaskGatewayDTO> getGateWays(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                            @PathVariable @Parameter(description = "项目Id") Long projectId,
                                                            @PathVariable @Parameter(description = "任务Id") Long taskId) {

        BpmRuTask ruTask = ruTaskRepository.findById(taskId).orElse(null);
        if (ruTask == null) {
            throw new NotFoundError("NOT FOUNT TASK");
        }
        BpmActivityInstanceBase actInst = actInstRepository.findById(ruTask.getActInstId()).orElse(null);

        if (actInst == null) {
            throw new NotFoundError("NOT FOUNT TASK");
        }

        Long processId = actInst.getProcessId();

        return new JsonListResponseBody<>(getContext(),
            activityTaskService.getTaskGateway(projectId, processId, actInst.getBpmnVersion(), ruTask.getTaskDefKey()));
    }

    /**
     * 处理任务
     */
    @RequestMapping(method = POST, value = "tasks/handle")
    @Operation(summary = "处理任务", description = "处理任务")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody execute(@PathVariable @Parameter(description = "组织id") Long orgId,
                                    @PathVariable @Parameter(description = "项目id") Long projectId,
                                    @RequestBody @Parameter(description = "任务处理信息") TodoTaskExecuteDTO toDoTaskDTO) {

        ContextDTO context = getContext();

        BpmActivityInstanceBase result = todoTaskDispatchService.exec(context, orgId, projectId, toDoTaskDTO, context.getOperator()).getActInst();

        if (result != null) {
            return new JsonResponseBody();
        } else {
            return new JsonResponseBody().setError(new BusinessError());
        }
    }


    /**
     * 获取 任务详情
     */
    @RequestMapping(method = POST, value = "handle-tasks")
    @Operation(summary = "查询任务处理信息", description = "查询任务处理信息")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<TodoTaskDTO> getConfirm(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                          @PathVariable @Parameter(description = "项目id") Long projectId, @Parameter(description = "任务id") String[] actTaskIds) {
        ContextDTO contextDTO = getContext();


        if (actTaskIds == null || actTaskIds.length == 0) {
            throw new ValidationError("actTaskId cannot be null");
        }

        TodoTaskDTO todoDTO = todoTaskDispatchService.setTaskUIHandleData(getContext(), orgId, projectId, actTaskIds);


        return new JsonObjectResponseBody<>(todoDTO);

    }


    /**
     * 获取 批处理 任务详情 ftjftj
     */
    @RequestMapping(method = GET, value = "handle-batch-tasks")
    @Operation(summary = "查询批处理任务处理信息", description = "查询批处理任务处理信息")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public <P extends BaseBatchTaskCriteriaDTO> JsonObjectResponseBody<TodoBatchTaskDTO> getBatchTasksInfo(@PathVariable Long orgId,
                                                                                                           @PathVariable Long projectId,
                                                                                                           P todoBatchTaskCriteriaDTO) {
        ContextDTO contextDTO = getContext();

        if (StringUtils.isEmpty(todoBatchTaskCriteriaDTO.getTaskDefKey())) {
            throw new BusinessError("There is no Task Define Key");
        }


        TodoBatchTaskDTO todoBatchTaskDTO = todoTaskDispatchService.setBatchTaskUIHandleData(contextDTO,
            orgId,
            projectId,
            todoBatchTaskCriteriaDTO);


        return new JsonObjectResponseBody<>(todoBatchTaskDTO);

    }

    /**
     * 批 处理任务 ftjftj
     */
    @RequestMapping(method = POST, value = "tasks/batch-handle")
    @Operation(summary = "批处理任务", description = "批处理任务")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public <P extends BaseBatchTaskCriteriaDTO> JsonResponseBody batchExecute(@PathVariable Long orgId,
                                                                              @PathVariable Long projectId,
                                                                              @RequestBody P todoBatchTaskCriteriaDTO) {

        ContextDTO context = getContext();
        Map<String, Object> data = new HashMap<>();
        data.put("orgId", orgId);
        data.put("projectId", projectId);

        TodoBatchTaskDTO todoBatchTaskDTO = todoTaskDispatchService.batchExec(context, data, todoBatchTaskCriteriaDTO);


        if (todoBatchTaskDTO != null) {
            return new JsonResponseBody();
        } else {
            return new JsonResponseBody().setError(new BusinessError());
        }
    }

    /**
     * 获取已完成任务列表
     */
    @RequestMapping(method = GET, value = "tasks/completed")
    @Operation(summary = "查询已完成任务列表", description = "根据条件查询已完成任务列表。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActivityInstanceDTO> searchCompleted(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                        @PathVariable @Parameter(description = "项目id") Long projectId, TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO) {
        Long assignee = getContext().getOperator().getId();
        return new JsonListResponseBody<>(
            todoTaskBaseService.searchCompletedTask(orgId, projectId, taskCriteria, pageDTO, assignee.toString()));
    }

    /**
     * 获取待办任务页面层级数据
     */
    @RequestMapping(method = GET, value = "tasks/hierarchy")
    @Operation(summary = "获取待办页面层级数据", description = "获取待办页面层级数据")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<TaskHierarchyDTO> getTaskHierarchy(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                     @PathVariable @Parameter(description = "项目id") Long projectId,
                                                                     HierarchyCriteriaDTO criteriaDTO) {

        Long assignee = getContext().getOperator().getId();

        TaskHierarchyDTO dto = new TaskHierarchyDTO();

        if (criteriaDTO.getNodeFlag()) {
            if (criteriaDTO.getTaskDefKey() != null
                && criteriaDTO.getProcessStageId() == null
                && criteriaDTO.getProcessId() == null) {
                List<HierarchyStageProcessDTO> stageProcesses = todoTaskBaseService
                    .getStageProcessesInRuTask(orgId, projectId, assignee, criteriaDTO.getTaskDefKey());
                Set<Long> psIds = new HashSet<>();
                List<HierarchyStageProcessDTO> newStageProcesses = new ArrayList<>();
                stageProcesses.forEach(ps -> {
                    if (psIds.contains(ps.getStageId())) return;
                    psIds.add(ps.getStageId());
                    newStageProcesses.add(ps);
                });
                dto.setStageProcesses(newStageProcesses);
            }
        } else {
            if (criteriaDTO.getProcessStageId() == null) {

                List<HierarchyBaseDTO> processStages = todoTaskBaseService.getProcessStagesInRuTask(
                    orgId, projectId, assignee, new HierarchyCriteriaDTO());
                Set<Long> psIds = new HashSet<>();
                List<HierarchyBaseDTO> newProcessStages = new ArrayList<>();
                processStages.forEach(ps -> {
                    if (psIds.contains(ps.getId())) return;
                    psIds.add(ps.getId());
                    newProcessStages.add(ps);
                });
                dto.setProcessStages(newProcessStages);

            } else if (criteriaDTO.getProcessStageId() != null
                && criteriaDTO.getProcessId() == null) {

                List<HierarchyBaseDTO> processes = todoTaskBaseService.getProcessesInRuTask(
                    criteriaDTO.getProcessStageId(), orgId, projectId, assignee, new HierarchyCriteriaDTO());
                Set<Long> psIds = new HashSet<>();
                List<HierarchyBaseDTO> newProcesses = new ArrayList<>();
                processes.forEach(ps -> {
                    if (psIds.contains(ps.getId())) return;
                    psIds.add(ps.getId());
                    newProcesses.add(ps);
                });
                dto.setProcesses(newProcesses);

            } else if (criteriaDTO.getProcessStageId() != null
                && criteriaDTO.getProcessId() != null
                && criteriaDTO.getTaskNode() == null) {

                List<TaskNodeDTO> taskNodes = todoTaskBaseService.getTaskNodesInRuTask(criteriaDTO.getProcessStageId(),
                    criteriaDTO.getProcessId(), orgId, projectId, assignee, criteriaDTO.getBatchFlag());
                Set<String> psIds = new HashSet<>();
                List<TaskNodeDTO> newTaskNodes = new ArrayList<>();
//                taskNodes.forEach(ps ->{
//                    if(psIds.contains(ps.getTaskDefKey()) || !(ps.getTaskName().equals("NDT主管审核并分配UT任务") ||
//                        ps.getTaskName().equals("NDT主管审核并分配MT任务") ||
//                        ps.getTaskName().equals("NDT主管审核并分配UT&MT任务") ||
//                        ps.getTaskName().equals("生产主管准备UT&MT焊口") ||
//                        ps.getTaskName().equals("生产主管准备UT焊口") ||
//                        ps.getTaskName().equals("生产主管准备MT焊口") ||
//                        ps.getTaskName().equals("NDT主管现场审核UT&MT焊口") ||
//                        ps.getTaskName().equals("NDT主管现场审核UT焊口") ||
//                        ps.getTaskName().equals("NDT主管现场审核MT焊口"))) return;
//                    psIds.add(ps.getTaskDefKey());
//                    newTaskNodes.add(ps);
//                });
                dto.setTasks(taskNodes);

            }
        }

        if (criteriaDTO.getProcessStageId() != null
            && criteriaDTO.getProcessId() != null
            && (criteriaDTO.getTaskNode() != null || criteriaDTO.getTaskDefKey() != null)) {

            List<String> moduleName = todoTaskBaseService.getEntityModuleNameInRuTask(criteriaDTO.getTaskNode(), criteriaDTO.getTaskDefKey(),
                criteriaDTO.getProcessStageId(), criteriaDTO.getProcessId(), orgId, projectId, assignee);
            dto.setModuleNames(moduleName);

            List<String> entityModuleNamesList = null;
            if (criteriaDTO.getEntityModuleNames() != null) {
                String[] entityModuleNames = criteriaDTO.getEntityModuleNames().split(",");
                entityModuleNamesList = new ArrayList<>(Arrays.asList(entityModuleNames));
            }

            List<HierarchyBaseDTO> entitiyCategories = todoTaskBaseService.getEntitiyCategoriesInRuTask(
                entityModuleNamesList, criteriaDTO.getTaskNode(), criteriaDTO.getTaskDefKey(), criteriaDTO.getProcessStageId(),
                criteriaDTO.getProcessId(), orgId, projectId, assignee.toString());
            dto.setEntityCategories(entitiyCategories);

        }
        return new JsonObjectResponseBody<>(getContext(), dto);
    }

    /**
     * 获取待办任务页面层级数据
     */
    @RequestMapping(method = GET, value = "tasks/completed/hierarchy")
    @Operation(summary = "获取待办页面层级数据", description = "获取待办页面层级数据")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<TaskHierarchyDTO> getTaskCompletedHierarchy(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        HierarchyCriteriaDTO criteriaDTO) {

        Long assignee = getContext().getOperator().getId();

        TaskHierarchyDTO dto = new TaskHierarchyDTO();

        List<Long> processCategoryIds = todoTaskBaseService.getProcessCategoriesInHiTask(orgId, projectId, assignee);
        List<HierarchyBaseDTO> processCategories = new ArrayList<>();
        for (Long id : processCategoryIds) {
            BpmProcessCategory processCategory = todoTaskBaseService.findProcessCategoryById(id);
            if (processCategory != null) {
                processCategories.add(new HierarchyBaseDTO(id, processCategory.getNameCn(), processCategory.getNameEn()));
            } else {
                processCategories.add(new HierarchyBaseDTO(id, "", ""));
            }
        }

        List<Long> entitiyCategoryIds = todoTaskBaseService
            .getEntitiyCategoriesInHiTask(criteriaDTO.getProcessCategoryId(), orgId, projectId, assignee);
        List<HierarchyBaseDTO> entitiyCategories = new ArrayList<>();
        for (Long id : entitiyCategoryIds) {
            BpmEntitySubType entitiyCategory = todoTaskBaseService.findEntitySubTypeById(id);
            if (entitiyCategory != null) {
                entitiyCategories.add(new HierarchyBaseDTO(id, entitiyCategory.getNameCn(), entitiyCategory.getNameEn()));
            } else {
                entitiyCategories.add(new HierarchyBaseDTO(id, "", ""));
            }
        }

        List<Long> processStageIds = todoTaskBaseService.getProcessStagesInHiTask(criteriaDTO.getProcessCategoryId(),
            criteriaDTO.getEntitySubTypeId(), orgId, projectId, assignee);
        List<HierarchyBaseDTO> processStages = new ArrayList<>();
        for (Long id : processStageIds) {
            BpmProcessStage processStage = todoTaskBaseService.findProcessStageById(id);
            if (processStage != null) {
                processStages.add(new HierarchyBaseDTO(id, processStage.getNameCn(), processStage.getNameEn()));
            } else {
                processStages.add(new HierarchyBaseDTO(id, "", ""));
            }
        }

        List<Long> processIds = todoTaskBaseService.getProcessesInHiTask(criteriaDTO.getProcessCategoryId(),
            criteriaDTO.getEntitySubTypeId(), criteriaDTO.getProcessStageId(), orgId, projectId, assignee);
        List<HierarchyBaseDTO> processes = new ArrayList<>();
        for (Long id : processIds) {
            BpmProcess process = todoTaskBaseService.findProcessById(id);
            if (process != null) {
                processes.add(new HierarchyBaseDTO(id, process.getNameCn(), process.getNameEn()));
            } else {
                processes.add(new HierarchyBaseDTO(id, "", ""));
            }
        }

        List<String> taskNodes = todoTaskBaseService.getTaskNodesInHiTask(criteriaDTO.getProcessCategoryId(),
            criteriaDTO.getEntitySubTypeId(), criteriaDTO.getProcessStageId(), criteriaDTO.getProcessId(), orgId,
            projectId, assignee);

        dto.setProcessCategories(processCategories);
        dto.setEntityCategories(entitiyCategories);
        dto.setProcessStages(processStages);
        dto.setProcesses(processes);

        dto.setTaskNodes(taskNodes);

        return new JsonObjectResponseBody<>(getContext(), dto);
    }

    /**
     * 委托任务
     */
    @RequestMapping(method = POST, value = "tasks/{bpmActTaskId}/delegate/{userid}")
    @Operation(summary = "委托任务", description = "委托任务")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody delegate(@PathVariable @Parameter(description = "组织id") Long orgId,
                                     @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "任务id") Long taskId,
                                     @PathVariable @Parameter(description = "用户id") Long userid) {

        BpmRuTask bRTask = todoTaskBaseService.findBpmRuTaskById(taskId);
        BpmActivityInstanceBase actInst = actInstRepository.findById(bRTask.getActInstId()).orElse(null);
        if (actInst == null) {
            throw new NotFoundError();
        }

        Long operatorId = getContext().getOperator().getId();
        String operatorName = getContext().getOperator().getName();


        todoTaskBaseService.deleteRuTask(taskId);

        BpmRuTask bpmRuTask = new BpmRuTask();
        bpmRuTask.setAssignee(userid.toString());
        bpmRuTask.setSeq(0);
        bpmRuTask.setCategory(bRTask.getCategory());
        bpmRuTask.setTaskType(bRTask.getTaskType());
        bpmRuTask.setCreateTime(new Timestamp(new Date().getTime()));
        bpmRuTask.setDelegation(bRTask.getAssignee() + "DELEGATION");
        bpmRuTask.setDescription(bRTask.getDescription());
        bpmRuTask.setName(bRTask.getName());
        bpmRuTask.setOwner(bRTask.getOwner());
        bpmRuTask.setParentTaskId(bRTask.getParentTaskId());
        bpmRuTask.setActInstId(bRTask.getActInstId());
        bpmRuTask.setSuspensionState(bRTask.getSuspensionState());
        bpmRuTask.setTaskDefKey(bRTask.getTaskDefKey());
        bpmRuTask.setTenantId(bRTask.getTenantId());
        todoTaskBaseService.saveBpmRuTask(bpmRuTask);

        Date d = new Date();

        BpmHiTaskinst hiTask = new BpmHiTaskinst();
        hiTask.setTaskId(taskId);
        hiTask.setSeq(bRTask.getSeq());
        hiTask.setAssignee(userid.toString());
        hiTask.setCategory(bRTask.getCategory());
        hiTask.setTaskType(bRTask.getTaskType());
        hiTask.setDescription(bRTask.getDescription());
        hiTask.setEndTime(d);
        hiTask.setName(bRTask.getName());
        hiTask.setOwner(bRTask.getAssignee());
        hiTask.setParentTaskId(bRTask.getParentTaskId());
        hiTask.setProcDefId(actInst.getProcessId().toString() + String.valueOf(actInst.getBpmnVersion()));
        hiTask.setActInstId(bRTask.getActInstId());
        hiTask.setStartTime(d);
        hiTask.setTaskDefKey(bRTask.getTaskDefKey());
        hiTask.setTenantId(bRTask.getTenantId());
        hiTask.setOperator(operatorId.toString());
        todoTaskBaseService.saveBpmHiTaskinst(hiTask);

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

        BpmActTask actTask = new BpmActTask();
        actTask.setComment(operatorName + " delegate " + name);
        actTask.setOperatorId(operatorId);
        actTask.setOperatorName(operatorName);
        actTask.setTaskId(hiTask.getTaskId());
        actTask.setCreatedAt();
        actTask.setLastModifiedAt();
        actTask.setStatus(EntityStatus.ACTIVE);
        actTask.setActInstId(bpmRuTask.getActInstId());

        bpmActTaskRepository.save(actTask);

        return new JsonResponseBody();
    }

    @RequestMapping(method = GET, value = "material-info/{qrCode}")
    @Operation(summary = "获取材料信息", description = "根据二维码获取材料信息。")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<MaterialInfoDTO> materialInfo(@PathVariable @Parameter(description = "orgId") Long orgId,
                                                                @PathVariable @Parameter(description = "项目id") Long projectId, @PathVariable @Parameter(description = "qrCode") String qrCode) {
        return new JsonObjectResponseBody<>(todoTaskBaseService.materialInfo(orgId, projectId, qrCode));
    }


    @RequestMapping(method = GET, value = "tasks/processes/{processId}/entity-sub-types/{entitySubTypeId}")
    @Operation(summary = "查询未安排的实体任务", description = "查询未安排的实体任务")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<EntityNoBpmActivityInstanceDTO> getTodoTaskEntity(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @PathVariable @Parameter(description = "流程Id") Long processId, @PathVariable @Parameter(description = "实体类型id") Long entitySubTypeId,
        @Parameter(description = "keyWord") String keyWord, PageDTO pageDTO) {
        return new JsonListResponseBody<>(
            todoTaskBaseService.getTodoTaskEntity(orgId, projectId, processId, entitySubTypeId, keyWord, pageDTO));
    }


    @RequestMapping(method = GET, value = "tasks/mobile")
    @Operation(summary = "小程序查询未安排的实体任务", description = "小程序查询未安排的实体任务")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<TodoTaskMobileCriteriaDTO> getTodoTaskMobile(
        @PathVariable @Parameter(description = "orgId") Long orgId, @PathVariable @Parameter(description = "项目id") Long projectId,
        @Parameter(description = "keyword") Long assignee) {
        return new JsonListResponseBody<>(
            todoTaskBaseService.getTodoTaskForMobile(orgId, projectId, assignee));
    }


    @Override
    @Operation(
        summary = "导出待办任务列表",
        description = "导出待办任务列表。"
    )
    @RequestMapping(
        method = GET,
        value = "tasks/export",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ExportFileDTO> exportTask(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        TodoTaskCriteriaDTO taskCriteria
    ) {
        Long assignee = getContext().getOperator().getId();

        taskCriteria.setPageable(false);
        Page<TodoTaskDTO> ruTasks = todoTaskBaseService.getRuTaskList(orgId, projectId, assignee, taskCriteria,
            new PageDTO());


        List<TodoTaskDTO> activelyResults = new ArrayList<>();

        List<TodoTaskDTO> suspendedResults = new ArrayList<>();


        List<Long> taskIdList = new ArrayList<>();
        for (TodoTaskDTO ruTask : ruTasks.getContent()) {
            taskIdList.add(ruTask.getTaskId());
        }


        for (TodoTaskDTO ruTask : ruTasks.getContent()) {


            ruTask.setSuspensionState(SuspensionState.ACTIVE);
            activelyResults.add(ruTask);


        }

        List<TodoTaskDTO> resultList = new ArrayList<>();

        if (taskCriteria.isActiveSearch()) {
            resultList = activelyResults;
        } else if (taskCriteria.isSuspendSearch()) {
            resultList = suspendedResults;
        }

        return new JsonObjectResponseBody<>();

    }

    @Operation(summary = "查询ndt报告生成画面层级数据", description = "查询ndt报告生成画面层级数据")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<TaskHierarchyDTO> getNDTReportTaskHierarchy(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        HierarchyCriteriaDTO criteriaDTO
    ) {
        Long assignee = getContext().getOperator().getId();

        TaskHierarchyDTO dto = new TaskHierarchyDTO();

        if (criteriaDTO.getProcessStageId() == null) {


        } else if (criteriaDTO.getProcessStageId() != null
            && criteriaDTO.getTaskNode() == null) {


        } else if (criteriaDTO.getProcessStageId() != null
            && criteriaDTO.getTaskNode() != null) {


            List<String> entityModuleNamesList = null;
            if (criteriaDTO.getEntityModuleNames() != null) {
                String[] entityModuleNames = criteriaDTO.getEntityModuleNames().split(",");
                entityModuleNamesList = new ArrayList<>(Arrays.asList(entityModuleNames));
            }


        }
        return new JsonObjectResponseBody<>(getContext(), dto);
    }

    @Override
    public JsonListResponseBody<TodoTaskDTO> searchNDTReportTask(Long orgId, Long projectId, TodoTaskCriteriaDTO taskCriteria, PageDTO pageDTO) {
        return null;
    }


    @Operation(summary = "预生成NDT报告", description = "预生成NDT报告")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonObjectResponseBody<ExportFileDTO> preBuildReport(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "projectId") Long projectId,
        @RequestBody TodoTaskExecuteDTO toDoTaskDTO
    ) {
        return new JsonObjectResponseBody<>(
            todoIndividualTaskService.preBuildReport(orgId, projectId, toDoTaskDTO)
        );
    }

    @Override
    @Operation(
        summary = "查询待办任务-班长查询待分配任务列表",
        description = "查询待办任务-班长查询待分配任务列表"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<TodoTaskForemanDispatchDTO> searchForemanDispatchTodo(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        TodoTaskCriteriaDTO taskCriteria,
        PageDTO pageDTO
    ) {

        Long assignee;

        UserPrivilegeDTO userPrivilegeDTO = privilegeFeignAPI.getUserAvailablePrivileges(orgId).getData();
        Set<String> privileges = userPrivilegeDTO.getPrivileges();

        if (privileges.contains(UserPrivilege.FOREMAN_EXECUTE.toString())
            || privileges.contains(UserPrivilege.ALL.toString())) {
            assignee = getContext().getOperator().getId();
        } else {
            return new JsonListResponseBody<>(new ArrayList<>());
        }

        Page<TodoTaskForemanDispatchDTO> dtos = todoIndividualTaskService.searchForemanDispatchTodo(orgId, projectId, assignee, taskCriteria, pageDTO);

        if (dtos == null) {
            return new JsonListResponseBody<>();
        }
        for (TodoTaskForemanDispatchDTO dto : dtos) {
            int weldCount = 0;
            for (TodoTaskDTO ruTask : dto.getWelds()) {


                ruTask.setSuspensionState(SuspensionState.ACTIVE);
                weldCount++;


            }
            if (weldCount == 0) {
                dto.setSuspensionState(SuspensionState.SUSPEND);
            }
        }

        return new JsonListResponseBody<>(dtos);
    }

    @Override
    @Operation(
        summary = "查询待办任务-工人查询待认领任务列表",
        description = "查询待办任务-工人查询待认领任务列表"
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonListResponseBody<TodoTaskForemanDispatchDTO> searchWorkersClaimTodo(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        TodoTaskCriteriaDTO taskCriteria,
        PageDTO pageDTO
    ) {

        UserPrivilegeDTO userPrivilegeDTO = privilegeFeignAPI.getUserAvailablePrivileges(orgId).getData();
        Set<String> privileges = userPrivilegeDTO.getPrivileges();
        if (
            !privileges.contains(UserPrivilege.WELD_EXECUTE.toString())
                && !privileges.contains(UserPrivilege.FITUP_EXECUTE.toString())
                && !privileges.contains(UserPrivilege.ALL.toString())
        ) {
            return new JsonListResponseBody<>(new ArrayList<>());
        }

        Page<TodoTaskForemanDispatchDTO> dtos = todoIndividualTaskService.searchForemanDispatchTodo(orgId, projectId, null, taskCriteria, pageDTO);

        for (TodoTaskForemanDispatchDTO dto : dtos) {
            int weldCount = 0;
            for (TodoTaskDTO ruTask : dto.getWelds()) {
                if (dto.getPrivilege() == null) {
                    BpmActivityInstanceBase actInst = todoTaskBaseService.findActInstByProjectIdAndActInstId(projectId, ruTask.getActInstId());
                    BpmActivityTaskNodePrivilege nodePrivilege = todoTaskBaseService.getBpmActivityTaskNodePrivilege(orgId,
                        projectId, actInst.getProcessId(), ruTask.getTaskDefKey());
                    String category = null;
                    if (nodePrivilege != null) {
                        category = nodePrivilege.getSubCategory();
                    }
                    dto.setPrivilege(UserPrivilege.getByName(category).toString());
                }


                ruTask.setSuspensionState(SuspensionState.ACTIVE);
                weldCount++;


            }
            if (weldCount == 0) {
                dto.setSuspensionState(SuspensionState.SUSPEND);
            }
        }

        return new JsonListResponseBody<>(
            dtos
        );
    }

    @Operation(summary = "处理任务-工人认领任务", description = "处理任务-工人认领任务")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody workerClaimTask(
        @PathVariable @Parameter(description = "组织id") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        @RequestBody @Parameter(description = "任务处理信息") TodoTaskExecuteDTO toDoTaskDTO
    ) {
        todoTaskDispatchService.workerClaimTask(getContext(), orgId, projectId, toDoTaskDTO);
        return new JsonResponseBody();
    }

    @Override
    @Operation(
        summary = "导出待办任务报告信息",
        description = "导出待办任务报告信息。"
    )
    @RequestMapping(
        method = GET,
        value = "tasks/report-export",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ExportFileListDTO> exportReport(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        TodoTaskCriteriaDTO taskCriteria
    ) {
        return new JsonObjectResponseBody<>();
    }

    @Override
    @Operation(
        summary = "导出NDT焊口和焊工信息",
        description = "导出NDT焊口和焊工信息。"
    )
    @RequestMapping(
        method = GET,
        value = "tasks/ndt-export",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ExportFileDTO> exportNdt(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        TodoTaskCriteriaDTO taskCriteria
    ) {
        return new JsonObjectResponseBody<>();
    }

    @Override
    @Operation(
        summary = "导出NDT焊口和焊工信息",
        description = "导出NDT焊口和焊工信息。"
    )
    @RequestMapping(
        method = GET,
        value = "tasks/ndt-export-quick",
        consumes = ALL_VALUE,
        produces = APPLICATION_JSON_VALUE
    )
    @WithPrivilege
    @ResponseStatus(OK)
    public JsonObjectResponseBody<ExportFileDTO> exportNdtQuick(
        @PathVariable @Parameter(description = "orgId") Long orgId,
        @PathVariable @Parameter(description = "项目id") Long projectId,
        TodoTaskCriteriaDTO taskCriteria
    ) {
        return new JsonObjectResponseBody<>();
    }

    /**
     * 获取待办工序列表
     *
     * @param orgId     组织ID
     * @param projectId
     * @return
     */
    @RequestMapping(method = GET, value = "todo-task/wx-processes")
    @Operation(summary = "处理任务", description = "处理任务")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<TaskProcessDTO> getProcesses(@PathVariable @Parameter(description = "组织id") Long orgId,
                                                             @PathVariable @Parameter(description = "组织id") Long projectId) {
        Long assignee = getContext().getOperator().getId();

        return new JsonListResponseBody();
    }


    @RequestMapping(method = POST, value = "/task/{taskId}/assignee/{assignee}")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody shiftTask(@PathVariable @Parameter(description = "组织id") Long orgId,
                                      @PathVariable @Parameter(description = "组织id") Long projectId,
                                      @PathVariable @Parameter(description = "组织id") Long taskId,
                                      @PathVariable @Parameter(description = "组织id") Long assignee) {

        BpmRuTask ruTask = ruTaskRepository.findById(taskId).orElse(null);
        assert ruTask != null;
        ruTask.setAssignee(String.valueOf(assignee));
        ruTaskRepository.save(ruTask);
        return new JsonObjectResponseBody<>();
    }

    @RequestMapping(method = GET, value = "/task/{actInstId}/support")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonListResponseBody<BpmActTaskAssignee> getTaskSupport(
        @PathVariable Long orgId,
        @PathVariable Long projectId,
        @PathVariable Long actInstId) {

        return new JsonListResponseBody<>(getContext(),
            activityTaskService.getTaskSupport(orgId, projectId, actInstId));
    }


    @RequestMapping(method = POST, value = "/task/{actInstId}/support")
    @WithPrivilege
    @ResponseStatus(OK)
    @Override
    public JsonResponseBody addTaskSupporter(@PathVariable @Parameter(description = "组织id") Long orgId,
                                             @PathVariable @Parameter(description = "组织id") Long projectId,
                                             @PathVariable @Parameter(description = "组织id") Long actInstId,
                                             @RequestBody TaskSupportDTO dto) {
        activityTaskService.addTaskSupport(orgId, projectId, actInstId, dto);
        return new JsonObjectResponseBody<>();
    }


}
