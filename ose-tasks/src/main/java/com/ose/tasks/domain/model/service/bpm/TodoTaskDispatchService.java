package com.ose.tasks.domain.model.service.bpm;

import com.ose.auth.vo.UserPrivilege;
import com.ose.dto.BizCodeDTO;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceStateRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmReDeploymentRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.service.ProjectService;
import com.ose.tasks.domain.model.service.ServerConfig;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskExecInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.domain.model.service.delegate.BaseBpmTaskInterfaceDelegate;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.*;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.SpringContextUtils;
import com.ose.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 工作流执行调度服务。
 */
@Component
public class TodoTaskDispatchService implements TodoTaskDispatchInterface {
    private final static Logger logger = LoggerFactory.getLogger(TodoTaskDispatchService.class);

//    private final TodoTaskFeignAPI todoTaskFeignAPI;

    //流程执行 基础服务
    private final TodoTaskBaseInterface todoTaskBaseService;

    private final TaskExecInterface taskExecService;

    private final ProjectService projectService;

    private final ActTaskConfigInterface actTaskConfigService;

    private final TaskRuleCheckService taskRuleCheckService;

//    private final ActivityTaskFeignAPI actTaskFeignAPI;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final ProcessInterface processService;

    private final EntitySubTypeInterface entitySubTypeService;

    private final ServerConfig serverConfig;

    private final BpmReDeploymentRepository bpmReDeploymentRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final ActivityTaskInterface activityTaskService;

    private final DrawingDetailRepository drawingDetailRepository;

    /**
     * 构造方法。
     */
    @Autowired
//    @Lazy
    public TodoTaskDispatchService(
        TodoTaskBaseInterface todoTaskBaseService,
        ProjectService projectService,
        TaskExecInterface taskExecService, ActTaskConfigInterface actTaskConfigServic,
        TaskRuleCheckService taskRuleCheckService,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmRuTaskRepository ruTaskRepository,
        ProcessInterface processService,
        ServerConfig serverConfig, BpmReDeploymentRepository bpmReDeploymentRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository, ActivityTaskInterface activityTaskService,
        EntitySubTypeInterface entitySubTypeService,
        DrawingDetailRepository drawingDetailRepository) {
        this.todoTaskBaseService = todoTaskBaseService;
        this.projectService = projectService;
        this.taskExecService = taskExecService;
        this.actTaskConfigService = actTaskConfigServic;
        this.taskRuleCheckService = taskRuleCheckService;
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.processService = processService;
        this.serverConfig = serverConfig;
        this.bpmReDeploymentRepository = bpmReDeploymentRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.activityTaskService = activityTaskService;
        this.entitySubTypeService = entitySubTypeService;
        this.drawingDetailRepository = drawingDetailRepository;
    }


    /**
     * 取得工作流执行的背景信息
     *
     * @param taskId 任务ID
     * @return 工作流背景信息类
     */
    @Override
    public BpmProcTaskDTO getBpmInfo(Long taskId) {
        BpmProcTaskDTO bpmProcTaskDTO = new BpmProcTaskDTO();
        //取得Tasks服务中的工作流实例。在BPMActivityInstance表中
        BpmRuTask ruTask = todoTaskBaseService.findFirstBpmRuTaskByActTaskId(taskId);
        if (ruTask == null) {
            return bpmProcTaskDTO;
        }

        bpmProcTaskDTO.setRuTask(ruTask);
        BpmProcess bpmProcess = null;
        BpmActivityInstanceBase actInst = todoTaskBaseService.findActInstByProjectIdAndActInstId(LongUtils.parseLong(ruTask.getTenantId()), ruTask.getActInstId());
        bpmProcTaskDTO.setActInst(actInst);
        if (actInst != null) {
            bpmProcess = processService.getBpmProcess(actInst.getProcessId());

            bpmProcTaskDTO.setBpmProcess(bpmProcess);
        }

        if (bpmProcess == null) {
            throw new NotFoundError("BPM PROCESS NOT FOUNT");
        }
        //取得流程定义
        BpmReDeployment bpmReDeployment = bpmReDeploymentRepository.findByProjectIdAndProcessIdAndVersion(
            LongUtils.parseLong(ruTask.getTenantId()), bpmProcess.getId(), actInst.getBpmnVersion()
        );

        if (bpmReDeployment != null) {
            //取得工作流部署的ID，也就是 工序对应的 流程图ID，此ID 在bpm服务的数据库中
            bpmProcTaskDTO.setProcessId(bpmProcess.getId());
            bpmProcTaskDTO.setBpmReDeployment(bpmReDeployment);
        } else {
            return bpmProcTaskDTO;
        }


        return bpmProcTaskDTO;
    }


    /**
     * 处理工作流 创建 的操作
     *
     * @param context    环境信息
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param actInstDTO 任务执行DTO
     * @param operator   操作者信息
     * @return 工作流背景信息类
     */
    @Override
    public CreateResultDTO create(ContextDTO context,
                                  Long orgId,
                                  Long projectId,
                                  OperatorDTO operator,
                                  ActivityInstanceDTO actInstDTO) {
//        String authorization = context.getAuthorization();
//        String userAgent = context.getUserAgent();
//        if(!context.isContextSet()) {
//            final RequestAttributes attributes = new ServletRequestAttributes(
//                new RequestWrapper(context.getRequest(), authorization, userAgent),
//                context.getResponse()
//            );
//            RequestContextHolder.setRequestAttributes(attributes, true);
//        }

        return createTask(
            context,
            orgId,
            projectId,
            operator,
            actInstDTO
        );

    }

    private CreateResultDTO createTask(ContextDTO context,
                                       Long orgId,
                                       Long projectId,
                                       OperatorDTO operator,
                                       ActivityInstanceDTO actInstDTO) {

        //准备流程实例的数据

        CreateResultDTO createResult = new CreateResultDTO();
        createResult.setOrgId(orgId);
        createResult.setProjectId(projectId);
        createResult.setContext(context);
        createResult.setActInstDTO(actInstDTO);
        createResult.setVariables(new HashMap<>());
        createResult.setCreateResult(true);

        Long processId = actInstDTO.getProcessId();

        BpmProcess bpmProcess = processService.getBpmProcess(processId);
        BpmEntitySubType bpmEntitySubType = entitySubTypeService.getEntity(actInstDTO.getEntitySubTypeId(), orgId, projectId);
        if (bpmProcess != null) {
            actInstDTO.setProcess(bpmProcess.getNameEn());
            actInstDTO.setProcessStageId(bpmProcess.getProcessStage().getId());
            actInstDTO.setProcessStage(bpmProcess.getProcessStage().getNameEn());
            actInstDTO.setDiscipline(bpmProcess.getDiscipline());
            createResult.setProcess(bpmProcess);
            createResult.setActInstDTO(actInstDTO);

        } else {
            return null;
        }

        if (bpmEntitySubType != null) {
            actInstDTO.setEntitySubType(bpmEntitySubType.getNameEn());
        }

        // 判断将创建的流程是否存在正在运行的重复项
        activityCheck(context, orgId, projectId, operator, actInstDTO, bpmProcess);

        // 取得全部代理类 map
        Map<String, List<String>> taskDelegateMap = actTaskConfigService.
            getConfigForCreate(orgId,
                projectId,
                createResult.getProcess().getProcessStage().getNameEn() + "-" + createResult.getProcess().getNameEn(),
                ProcessType.valueOf(createResult.getProcess().getProcessType().name()),
                createResult.getProcess().getProcessCategory().getNameEn());


        //1. 执行TaskExecService中的 预创建
        createResult = taskExecService.preCreateActInst(createResult);
        if (!createResult.isCreateResult()) {
            return createResult;
        }

        //2. 执行代理里的 预创建
        createResult = execDelegate(createResult, BpmActTaskConfigDelegateStage.PRE_CREATE, taskDelegateMap.get(BpmActTaskConfigDelegateStage.PRE_CREATE.name().toString()));
        if (!createResult.isCreateResult()) {
            if (createResult.getErrorDesc() != null) {
                throw new BusinessError(createResult.getErrorDesc());
            }
            return createResult;
        }

        //3. 执行代理里的 创建
        createResult = execDelegate(createResult, BpmActTaskConfigDelegateStage.CREATE, taskDelegateMap.get(BpmActTaskConfigDelegateStage.CREATE.name()));
        if (!createResult.isCreateResult()) {
            if (createResult.getErrorDesc() != null) {
                throw new BusinessError(createResult.getErrorDesc());
            }
            return createResult;
        }

        //4. 执行TaskExecService中的 创建
        createResult = taskExecService.createActInst(createResult);
        if (!createResult.isCreateResult()) {
            if (createResult.getErrorDesc() != null) {
                throw new BusinessError(createResult.getErrorDesc());
            }
            return createResult;
        }

        //5. 执行TaskExecService中的 后创建 创建 bpm_ru_task中的记录
        createResult = taskExecService.postCreateActInst(createResult);

        //6. 执行代理里的 后创建
        createResult = execDelegate(createResult, BpmActTaskConfigDelegateStage.POST_CREATE, taskDelegateMap.get(BpmActTaskConfigDelegateStage.POST_CREATE.name()));
        if (!createResult.isCreateResult()) {
            if (createResult.getErrorDesc() != null) {
                throw new BusinessError(createResult.getErrorDesc());
            }
            return createResult;
        }

        //7. 检查第一个任务节点是否自动启动
        if (createResult.isStartNextTask()) {

            Long taskId = createResult.getNextTaskId();
            TodoTaskExecuteDTO todoTaskDTO = createResult.getNextTodoTaskDTO();
            if (todoTaskDTO == null) todoTaskDTO = new TodoTaskExecuteDTO();
            String taskType = createResult.getTaskType();
            //执行下一个任务
            exec(context, orgId, projectId, taskId, todoTaskDTO, operator, taskType);

        }

        return createResult;
    }


    /**
     * 处理工作流 执行 的操作
     *
     * @param context     环境信息
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param todoTaskDTO 任务执行DTO
     * @param operator    操作者信息
     * @return 工作流背景信息类
     */
    @Override
    public ExecResultDTO exec(ContextDTO context,
                              Long orgId,
                              Long projectId,
                              TodoTaskExecuteDTO todoTaskDTO,
                              OperatorDTO operator) {

        Set<Long> instIds = new HashSet<>();
        Set<Long> todoIds = new HashSet<>();
        List<Long> actInstIds = new ArrayList<>();
        List<Long> taskIds = new ArrayList<>();
        String taskType = null;

        /* -----
        TODO 如果传的是单个任务，是 ose_tasks.bpm_ru_task表中的ID（BPMNDLSJLDSJLD)，如果传的是Ids，是ose_bpm.act_ru_task中的id （1212323）
         */
        if (todoTaskDTO.getId() != null) {
            Long id = todoTaskDTO.getId();
//                if (ruTaskRepository.findByTaskId(id) != null) {
            todoIds.add(id);
            todoTaskDTO.setIds(todoIds);
//                }


        }
        if (todoTaskDTO.getIds() != null) {
            for (Long taskId : todoTaskDTO.getIds()) {

//                    if (ruTaskRepository.findByTaskId(actTaskId) != null) {
                todoIds.add(taskId);
                todoTaskDTO.setIds(todoIds);
//                    }

            }
        }
        ExecResultDTO execResult = new ExecResultDTO();
        if (todoTaskDTO.getIds() != null) {
            for (Long taskId : todoTaskDTO.getIds()) {

                //TODO 注意 doDoTaskDTO 中的 id和ids不能使用了
                execResult = exec(context,
                    orgId,
                    projectId,
                    taskId,
                    todoTaskDTO,
                    operator,
                    taskType);
                //将执行的流程Id 保存到集合，供之后 聚合 操作使用 todo null for execResult
                Long instId = execResult.getActInst().getId();
                if (instId != null && !instIds.contains(instId)) {
                    instIds.add(instId);
                    actInstIds.add(instId);
                }

                //将执行的任务Id 保存到集合，供之后 聚合 操作使用
//                Long taskId = execResult.getRuTask().getId();
                if (taskId != null && !taskIds.contains(taskId)) {
                    taskIds.add(taskId);
                    taskIds.add(execResult.getRuTask().getId());
                }

                //TODO 任务执行完成的时候 要设置 检查后续任务节点自动启动
                List<ExecResultDTO> nextTasks = execResult.getNextTasks();
                for (ExecResultDTO nextTask : nextTasks) {
                    if (nextTask.isAutoStart()) {

                        exec(context,
                            orgId,
                            projectId,
                            nextTask.getTodoTaskDTO().getTaskId(),
                            nextTask.getTodoTaskDTO(),
                            context.getOperator(),
                            null
                        );
                    }
                }


            }
        }

        //TODO 任务执行完成之后，自动执行后续任务 和 批量执行后续任务 （生成报告） 只会有一个有效。当前优先检查 执行下一个任务。
//        execResult = execBatch(execResult, actInstIds, actTaskIds);


        return execResult;
    }


    /**
     * 处理工作流 执行 的操作
     *
     * @param context     环境信息
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param taskId      任务ID
     * @param todoTaskDTO 任务执行DTO
     * @param operator    操作者信息
     * @param taskType    任务类型
     * @return 工作流背景信息类
     */
    @Override
    public ExecResultDTO exec(ContextDTO context,
                              Long orgId,
                              Long projectId,
                              Long taskId,
                              TodoTaskExecuteDTO todoTaskDTO,
                              OperatorDTO operator,
                              String taskType) {

        BpmProcTaskDTO bpmProcTaskDTO = getBpmInfo(taskId);
        Project project = projectService.get(orgId, projectId);
        bpmProcTaskDTO.setContext(context);
        bpmProcTaskDTO.setTodoTaskDTO(todoTaskDTO);
        bpmProcTaskDTO.setProject(project);
        bpmProcTaskDTO.setOverrideDelegate(todoTaskDTO.getOverrideDelegate());
        bpmProcTaskDTO.setOverridePostDelegate(todoTaskDTO.getOverridePostDelegate());
//        bpmProcTaskDTO.setTaskType(taskType);
        //如果没有流程实例，则报错返回
        if (bpmProcTaskDTO.getActInst() == null
            || bpmProcTaskDTO.getBpmReDeployment() == null
            || bpmProcTaskDTO.getRuTask() == null
            || bpmProcTaskDTO.getBpmProcess() == null
            || project == null
            || bpmProcTaskDTO.getBpmProcess() == null) {
            throw new NotFoundError("Not found the bpm process instance or task");
        }

        ExecResultDTO execResult = taskExecService.exec(bpmProcTaskDTO);
        return execResult;
    }

    /**
     * 处理工作流 执行 的操作
     *
     * @param context     环境信息
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param taskId      任务ID
     * @param todoTaskDTO 任务执行DTO
     * @param operator    操作者信息
     * @return 工作流背景信息类
     */
    @Override
    public ExecResultDTO exec(ContextDTO context,
                              Long orgId,
                              Long projectId,
                              Long taskId,
                              TodoTaskExecuteDTO todoTaskDTO,
                              OperatorDTO operator) {
        return exec(context, orgId, projectId, taskId, todoTaskDTO, operator, null);

    }

    /**
     * 返回 任务处理画面需要的数据
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param actTaskIds 任务IDs
     * @return TodoTaskDTO
     */
    @Override
    public TodoTaskDTO setTaskUIHandleData(ContextDTO contextDTO, Long orgId, Long projectId, String[] actTaskIds) {
        TodoTaskDTO todoDTO = new TodoTaskDTO();

        String[] taskIds = actTaskIds;//new String[actTaskIds.length];

        //第一个任务的ID
        String ruTaskId = taskIds[0];
        /*
        TODO taskID FTJ
         */
        String eEntityNoStr = "";
        BpmRuTask ruTask = ruTaskRepository.findById(LongUtils.parseLong(ruTaskId)).orElse(null);
        if (ruTask == null) return todoDTO;
        long taskid = ruTask.getId();

        //第一个任务对应的流程实例
        BpmActivityInstanceBase actInst = todoTaskBaseService.findActInstByProjectIdAndActInstId(projectId, ruTask.getActInstId());

        if (actInst == null)
            throw new NotFoundError();

        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
        if (actInstState == null)
            throw new NotFoundError();

        //取得流程定义，工作流定义
        BpmReDeployment bpmReDeployment = bpmReDeploymentRepository.findByProjectIdAndProcessIdAndVersion(projectId,
            actInst.getProcessId(), actInst.getBpmnVersion());

        //设置 流程变量，设置 可编辑变量 和显示变量
        setActInstTaskVariables(orgId, projectId, bpmReDeployment, todoDTO, ruTask, actInst, taskid);

        List<ActReportDTO> documents = null;


        // 获取流程图
        DiagramResourceDTO diagramDTO = activityTaskService
            .getDiagramResource(projectId, actInst.getId(), actInst.getProcessId(), actInst.getBpmnVersion());
        todoDTO.setDiagramResource(diagramDTO.getDiagramResource());

        todoDTO.setActInstId(actInst.getId());
        todoDTO.setTaskId(ruTask.getId());
        todoDTO.setTaskDefKey(ruTask.getTaskDefKey());

        List<DrawingDetail> drawingDetails = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(actInst.getEntityId(), actInst.getId());
        if (drawingDetails.size() > 0) {
            todoDTO.setVersion(drawingDetails.get(0).getRevNo());
        }

        todoDTO.setProcessCategory(actInst.getActCategory());
        todoDTO.setEntitySubType(actInst.getEntitySubType());
        todoDTO.setEntityId(actInst.getEntityId());
        todoDTO.setDrawingTitle(actInst.getDrawingTitle());
        todoDTO.setUnAcceptCount(actInstState.getUnAcceptCount());
        todoDTO.setTaskType(ruTask.getTaskType()); //taskType
        todoDTO.setEntityNo(actInst.getEntityNo());
        todoDTO.setProcess(actInst.getProcess());
        todoDTO.setProcessStage(actInst.getProcessStage());

        if (actInst.getProcessId() != null) {
            BpmProcess process = processService.getBpmProcess(actInst.getProcessId());
            if (process != null) {
                todoDTO.setDiscipline(process.getDiscipline());
            }
        }

//        if (actRuTask.getSuspensionState() == SuspensionState.SUSPEND.getState()) {
//            todoDTO.setSuspensionState(SuspensionState.SUSPEND);
//        } else {
        todoDTO.setSuspensionState(SuspensionState.ACTIVE);
//        }
        todoDTO.setTaskCreatedTime(ruTask.getCreateTime());
        todoDTO.setTaskNode(ruTask.getName());

        List<TaskGatewayDTO> gateWays = activityTaskService.getTaskGateway(projectId, actInst.getProcessId(), actInst.getBpmnVersion(), ruTask.getTaskDefKey());
        todoDTO.setGateway(gateWays);
        if (gateWays != null && !gateWays.isEmpty()) {
            todoDTO.setMutiSelectFlag(gateWays.get(0).isMutiSelectFlag());
        }


        // 设置外检状态枚举列表
        todoDTO.setExternalInspectionStatusList(BizCodeDTO.list(ExInspStatus.values()));

        //当前节点需要分配权限信息
        BpmActivityTaskNodePrivilege nodePrivilege = todoTaskBaseService.getBpmActivityTaskNodePrivilege(orgId,
            projectId, actInst.getProcessId(), ruTask.getTaskDefKey());
        String category = null;
        if (nodePrivilege != null) {
            category = nodePrivilege.getSubCategory();
        }

        List<String> subCategories = new ArrayList<>();
        if (category != null && !"".equals(category)) {
            subCategories = Arrays.asList(category.split(","));
        }

        if (subCategories.size() > 0) {
            List<TaskNodePrivilegeDTO> categories = new ArrayList<>();
            for (String subCategory : subCategories) {
                TaskNodePrivilegeDTO categoryDTO = new TaskNodePrivilegeDTO();
                setPrivilegeCategory(subCategory, categoryDTO, actInst);
                categories.add(categoryDTO);
            }
            if (categories.size() > 0) {
                todoDTO.setCategories(categories);
            }
        }





/*                String teamId = null;
                BpmActTaskAssignee actAssignee = todoTaskService.getTaskAssigneesByTaskInfo(actInst.getId(),
                        ruTask.getTaskDefKey());
                if (actAssignee != null) {
                    teamId = actAssignee.getTeamId();
                    if(teamId == null) {
                        teamId = "";
                    }
                }*/
        Long teamId = actInstState.getTeamId();
        if (teamId == null) {
            todoDTO.setTeamId(0L);
        } else {
            todoDTO.setTeamId(teamId);
        }
        todoDTO.setTeamName(actInstState.getTeamName());
        todoDTO.setWorkSiteId(actInstState.getWorkSiteId());
        todoDTO.setWorkSiteName(actInstState.getWorkSiteName());
        todoDTO.setWorkSiteAddress(actInstState.getWorkSiteAddress());

        todoDTO.setMemo(actInstState.getMemo());

        //执行 数据准备类型的 代理 PREPARE 类型的任务。TODO 需要修改 有 projectId 等参数
        BpmProcess bpmProcess = processService.getBpmProcess(actInst.getProcessId());
        List<BpmActTaskConfig> taskConfigs = actTaskConfigService.getConfigByProcDefIdAndActTaskId(bpmProcess,
            ruTask, BpmActTaskConfigDelegateStage.PREPARE);
        if (taskConfigs != null
            && !taskConfigs.isEmpty()) {
            for (BpmActTaskConfig taskConfig : taskConfigs) {
                String proxyName = taskConfig.getProxy();
                try {
                    Class clazz = Class.forName(proxyName);
                    BaseBpmTaskInterfaceDelegate delegate = (BaseBpmTaskInterfaceDelegate) SpringContextUtils.getBean(clazz);
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", orgId);
                    data.put("projectId", projectId);
                    data.put("taskIds", taskIds);
                    data.put("operator", contextDTO.getOperator());
                    delegate.prepareExecute(contextDTO, data, actInst, ruTask, todoDTO);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
            BpmRuTask bpmRuTask = todoTaskBaseService.findBpmRuTaskByActTaskId(taskid);
            if (bpmRuTask != null) {
                documents = bpmRuTask.getJsonDocuments();
            }
        }
        todoDTO.setDocuments(documents);

        if (taskRuleCheckService.isWeldExecuteTaskNode(ruTask.getTaskType())
            || taskRuleCheckService.isFormanAssignWorkerTaskNode(ruTask.getTaskType())) {
            TaskMaterialDTO materialDTO = todoTaskBaseService.getMaterial(ruTask.getActInstId());
            todoDTO.setMaterialDTO(materialDTO);
        }

        return todoDTO;
    }

    @Override
    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchExec(ContextDTO contextDTO,
                                                                           Map<String, Object> data,
                                                                           P todoBatchTaskCriteriaDTO) {


        return taskExecService.batchExec(contextDTO, data, todoBatchTaskCriteriaDTO);

    }


    /**
     * 执行流程节点上配置的 代理任务
     *
     * @param createResult 创建结果
     */

    private CreateResultDTO execDelegate(CreateResultDTO createResult, BpmActTaskConfigDelegateStage delegateStage, List<String> proxyNames) {

        if (proxyNames != null
            && !proxyNames.isEmpty()) {
            for (String proxyName : proxyNames) {
                try {
                    Class clazz = Class.forName(proxyName);
                    BaseBpmTaskInterfaceDelegate delegate = (BaseBpmTaskInterfaceDelegate) SpringContextUtils.getBean(clazz);
                    if (delegateStage == BpmActTaskConfigDelegateStage.PRE_CREATE)
                        createResult = delegate.preCreateActInst(createResult);
                    else if (delegateStage == BpmActTaskConfigDelegateStage.CREATE)
                        createResult = delegate.createActInst(createResult);
                    else if (delegateStage == BpmActTaskConfigDelegateStage.POST_CREATE)
                        createResult = delegate.postCreateActInst(createResult);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return createResult;
    }

    @Override
    public void workerClaimTask(ContextDTO context, Long orgId, Long projectId, TodoTaskExecuteDTO toDoTaskDTO) {
        List<String> attach = toDoTaskDTO.getAttachFiles();
        Set<Long> todoIds = new HashSet<>();
        toDoTaskDTO.setAttachFiles(null);
        List<Long> actInstIds = new ArrayList<>();
        if (toDoTaskDTO.getIds() != null) {
            for (Long taskId : toDoTaskDTO.getIds()) {

//                    if (ruTaskRepository.findByTaskId(actTaskId) != null) {
                todoIds.add(taskId);
                toDoTaskDTO.setIds(todoIds);
//                    }

            }
        }
        for (Long id : toDoTaskDTO.getIds()) { //任务节点：班长分配任务
            BpmRuTask ruTask = todoTaskBaseService.findBpmRuTaskByActTaskId(id);
            if (ruTask == null) {
                throw new NotFoundError();
            }
            BpmActivityInstanceState bpmInstance = bpmActivityInstanceStateRepository.findByProjectIdAndBaiId(projectId, ruTask.getActInstId());
            exec(context, orgId, projectId, ruTask.getId(), toDoTaskDTO, context.getOperator());
            actInstIds.add(ruTask.getActInstId());
        }
        toDoTaskDTO.setAttachFiles(attach);
        toDoTaskDTO.setNextAssignee(null);
        toDoTaskDTO.setTeamId(null);
        for (Long actInstId : actInstIds) { //任务节点：组对执行/焊接执行
            List<BpmRuTask> bpmRuTasks = todoTaskBaseService.findBpmRuTaskByActInstId(actInstId);
            if (bpmRuTasks.isEmpty())
                continue;
            exec(context, orgId, projectId, bpmRuTasks.get(0).getId(), toDoTaskDTO, context.getOperator());
        }
    }


    /**
     * 设定流程上的流程变量，设置 可编辑流程变量 可显示变量
     */
    private void setActInstTaskVariables(Long orgId,
                                         Long projectId,
                                         BpmReDeployment bpmReDeployment,
                                         TodoTaskDTO todoDTO,
                                         BpmRuTask ruTask,
                                         BpmActivityInstanceBase actInst,
                                         Long taskid
    ) {
        List<Map<String, Object>> variablesResult = new ArrayList<>();
        List<Map<String, Object>> variablesDisplayResult = new ArrayList<>();

        if (bpmReDeployment != null) {
            Long processId = actInst.getProcessId();


            List<Map<String, Object>> editVariables = new ArrayList<>();
            List<Map<String, Object>> displayVariables = new ArrayList<>();

            //取得流程实例上的变量清单，从 bpm_act_inst_variable_config 中
            List<BpmActInstVariableConfig> actInstVariableConfigList = todoTaskBaseService.findActInstVariables(orgId, projectId,
                processId);
            //对于每一个流程变量的设置
            for (BpmActInstVariableConfig actInstVariableConfig : actInstVariableConfigList) {

                //取得流程变量在任务节点上的设置 bpm_act_inst_variable_task_config
                BpmActInstVariableTaskConfig config = todoTaskBaseService
                    .findVariableConfigByTaskDefKeyAndVariableName(processId, ruTask.getTaskDefKey(), actInstVariableConfig.getName());

                //取得流程变量 在任务节点上设置的对应的值 bpm_act_inst_variable_value
                BpmActInstVariableValue value = todoTaskBaseService.findBpmActInstVariableValue(orgId, projectId,
                    ruTask.getActInstId(), actInstVariableConfig.getName());

                Map<String, Object> variableMap = new HashMap<>();
                variableMap.put("name", actInstVariableConfig.getName());
                variableMap.put("displayName", actInstVariableConfig.getDisplayName());
                variableMap.put("type", actInstVariableConfig.getType());
                if (value != null) {
                    variableMap.put("value", value.getValue());
                } else {
                    variableMap.put("value", "");
                }
                if (config != null) {
                    if (config.getFlag() == ActInstVariableFlag.EDIT) {
                        editVariables.add(variableMap);
                    } else if (config.getFlag() == ActInstVariableFlag.DISPLAY) {
                        displayVariables.add(variableMap);
                    }
                }
            }

            Map<String, Object> editMap = new HashMap<>();
            editMap.put("actInstId", ruTask.getActInstId());
            editMap.put("actTaskId", taskid);
            editMap.put("variables", editVariables);
            variablesResult.add(editMap);

            Map<String, Object> displayMap = new HashMap<>();
            displayMap.put("actInstId", ruTask.getActInstId());
            displayMap.put("actTaskId", taskid);
            displayMap.put("variables", displayVariables);
            variablesDisplayResult.add(displayMap);
        }

        todoDTO.setVariables(variablesResult);
        todoDTO.setVariablesDisplay(variablesDisplayResult);
    }

    /**
     * 返回 批处理任务 处理画面需要的数据
     *
     * @param orgId                    组织ID
     * @param projectId                项目ID
     * @param todoBatchTaskCriteriaDTO 批处理DTO
     * @return TodoBatchTaskDTO
     */
    @Override
    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO setBatchTaskUIHandleData(ContextDTO contextDTO,
                                                                                          Long orgId,
                                                                                          Long projectId,
                                                                                          P todoBatchTaskCriteriaDTO) {

        TodoBatchTaskDTO todoBatchTaskDTO = new TodoBatchTaskDTO();

        Long processId = todoBatchTaskCriteriaDTO.getProcessId();
        BpmProcess bpmProcess;
        String processKey = null;
        String taskDefKey = todoBatchTaskCriteriaDTO.getTaskDefKey();
        String taskType = todoBatchTaskCriteriaDTO.getTaskType();
        if (StringUtils.isEmpty(taskDefKey)) return todoBatchTaskDTO;
        ProcessType processType = null;
        String processCategory = null;

        if (processId != null) {
            bpmProcess = processService.getBpmProcess(processId);
            processCategory = bpmProcess.getProcessCategory().getNameEn();
            processKey = bpmProcess.getProcessStage().getNameEn() + "-" + bpmProcess.getNameEn();
            processType = bpmProcess.getProcessType();
        }


        List<String> proxies = actTaskConfigService.
            getBatchTaskConfigByTaskDefKey(orgId, projectId, taskDefKey, taskType, processKey, processType, processCategory).
            get(BpmActTaskConfigDelegateStage.BATCH_PREPARE.name());

        if (!CollectionUtils.isEmpty(proxies)) {
            for (String proxyName : proxies) {
                try {
                    Class clazz = Class.forName(proxyName);
                    BaseBpmTaskInterfaceDelegate delegate = (BaseBpmTaskInterfaceDelegate) SpringContextUtils.getBean(clazz);
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", orgId);
                    data.put("projectId", projectId);
                    delegate.batchPrepareExecute(contextDTO, data, todoBatchTaskCriteriaDTO, todoBatchTaskDTO);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        }

        //如果不需要bpm的图形等信息
        if (!todoBatchTaskCriteriaDTO.isBpmInfoRequired())
            return todoBatchTaskDTO;

        //第一个任务的ID
        List<String> taskIds = todoBatchTaskDTO.getActTaskIds();
        if (!CollectionUtils.isEmpty(taskIds)) {
            String taskid = taskIds.get(0);

            BpmRuTask ruTask = ruTaskRepository.findById(LongUtils.parseLong(taskid)).orElse(null);
            if (ruTask == null) throw new NotFoundError();

            todoBatchTaskCriteriaDTO.setTaskType(ruTask.getTaskType());//taskType
            //第一个任务对应的流程实例
            BpmActivityInstanceBase actInst = todoTaskBaseService.findActInstByProjectIdAndActInstId(projectId, ruTask.getActInstId());

            if (actInst == null) throw new NotFoundError();

            BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
            if (actInstState == null) throw new NotFoundError();


            // 获取流程图
            DiagramResourceDTO diagramDTO = activityTaskService
                .getDiagramResource(projectId, actInst.getId(), actInst.getProcessId(), actInst.getBpmnVersion());
            todoBatchTaskDTO.setDiagramResource(diagramDTO.getDiagramResource());

            todoBatchTaskDTO.setTaskDefKey(ruTask.getTaskDefKey());
            todoBatchTaskDTO.setProcessCategory(actInst.getActCategory());
            todoBatchTaskDTO.setEntitySubType(actInst.getEntitySubType());

            todoBatchTaskDTO.setProcess(actInst.getProcess());
            todoBatchTaskDTO.setProcessStage(actInst.getProcessStage());

            List<TaskGatewayDTO> gateWays = activityTaskService.getTaskGateway(projectId, actInst.getProcessId(), actInst.getBpmnVersion(), ruTask.getTaskDefKey());
            todoBatchTaskDTO.setGateway(gateWays);
            if (gateWays != null && !gateWays.isEmpty()) {
                todoBatchTaskDTO.setMutiSelectFlag(gateWays.get(0).isMutiSelectFlag());
            }


            //当前节点需要分配权限信息
            BpmActivityTaskNodePrivilege nodePrivilege = todoTaskBaseService.getBpmActivityTaskNodePrivilege(orgId,
                projectId, actInst.getProcessId(), ruTask.getTaskDefKey());
            String category = null;
            if (nodePrivilege != null) {
                category = nodePrivilege.getSubCategory();
            }
            TaskNodePrivilegeDTO categoryDTO = new TaskNodePrivilegeDTO();
            setPrivilegeCategory(category, categoryDTO, actInst);

            todoBatchTaskDTO.setCategory(categoryDTO);

            Long teamId = actInstState.getTeamId();
            if (teamId == null) {
                todoBatchTaskDTO.setTeamId(0L);
            } else {
                todoBatchTaskDTO.setTeamId(teamId);
            }
            todoBatchTaskDTO.setTeamName(actInstState.getTeamName());
            todoBatchTaskDTO.setWorkSiteId(actInstState.getWorkSiteId());
            todoBatchTaskDTO.setWorkSiteName(actInstState.getWorkSiteName());

            todoBatchTaskDTO.setMemo(actInstState.getMemo());


        }

        return todoBatchTaskDTO;


    }

    private void setPrivilegeCategory(String category, TaskNodePrivilegeDTO categoryDTO, BpmActivityInstanceBase actInst) {

        if (category != null) {
            categoryDTO.setCategory(category);
            List<BpmActTaskAssignee> taskAssignees = todoTaskBaseService.getTaskAssigneesByTaskCategory(actInst.getId(),
                category);
            if (!taskAssignees.isEmpty()) {
                categoryDTO.setUserId(taskAssignees.get(0).getAssignee());
                categoryDTO.setUserName(taskAssignees.get(0).getAssigneeName());
            }
            try {
                categoryDTO.setCategoryName(UserPrivilege.getByName(category).getDisplayName());
            } catch (Error e) {
                e.printStackTrace(System.out);
            }
        }
    }


    /**
     * 任务节点撤回操作
     *
     * @param orgId
     * @param projectId
     * @param actInstSuspendDTO
     * @return
     */
    @Override
    public RevocationDTO revocation(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO) {

        BpmProcess bpmProcess = processService.getBpmProcess(actInstSuspendDTO.getActInst().getProcessId());
        actInstSuspendDTO.setBpmProcess(bpmProcess);

        RevocationDTO revocationDTO = taskExecService.revocation(context, orgId, projectId, actInstSuspendDTO);


        return revocationDTO;
    }

    /**
     * 任务节点批量撤回操作
     *
     * @param orgId
     * @param projectId
     * @param actInstSuspendDTO
     * @return
     */
    @Override
    public RevocationDTO batchRevocation(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO) {

        BpmProcess bpmProcess = processService.getBpmProcess(actInstSuspendDTO.getActInst().getProcessId());
        actInstSuspendDTO.setBpmProcess(bpmProcess);

        RevocationDTO revocationDTO = taskExecService.batchRevocation(context, orgId, projectId, actInstSuspendDTO);


        return revocationDTO;
    }

    /**
     * 任务流程创建重复性check。
     *
     * @param context
     * @param orgId
     * @param projectId
     * @param operator
     * @param actInstDTO
     */
    private void activityCheck(
        ContextDTO context,
        Long orgId,
        Long projectId,
        OperatorDTO operator,
        ActivityInstanceDTO actInstDTO,
        BpmProcess bpmProcess
    ) {

        if (bpmProcess == null) {
            throw new BusinessError("工序不存在");
        }
        // 通用流程check
        List<BpmActivityInstanceBase> runningTasks = bpmActInstRepository.findByProjectIdAndEntityIdAndProcessIdAndFinishStateAndSuspensionState(
            projectId,
            actInstDTO.getEntityId(),
            bpmProcess.getId(),
            ActInstFinishState.NOT_FINISHED,
            SuspensionState.ACTIVE);
        if (!bpmProcess.getNameEn().equals("DRAWING-REDMARK") && runningTasks != null && runningTasks.size() > 0) {
            throw new BusinessError("当前对象已存在正在运行的任务");
        }

//        if (bpmProcess.getNameEn().equals("ENGINEERING") || bpmProcess.getNameEn().equals("DRAWING_PARTIAL_UPDATE") || bpmProcess.getNameEn().equals("DRAWING_INTEGRAL_UPDATE")) {
//            // 图纸出图流程创建check
//            List<BpmActivityInstance> runningDrawingTasks = bpmActInstRepository.findByOrgIdAndProjectIdAndEntityIdAndFinishStateAndSuspensionState(
//                orgId,
//                projectId,
//                actInstDTO.getEntityId(),
//                ActInstFinishState.NOT_FINISHED,
//                SuspensionState.ACTIVE);
//            if (runningDrawingTasks != null && runningDrawingTasks.size() > 0) {
//                throw new BusinessError("本图纸尚有未结束的校审流程");
//            }
//
//            if (bpmProcess.getNameEn().equals("ENGINEERING")) {
//                List<BpmActivityInstance> runningDrawingEngineerTasks = bpmActInstRepository.findByOrgIdAndProjectIdAndEntityIdAndProcessIdAndSuspensionState(
//                    orgId,
//                    projectId,
//                    bpmProcess.getId(),
//                    actInstDTO.getEntityId(),
//                    ActInstFinishState.FINISHED);
//                if (runningDrawingEngineerTasks != null && runningDrawingEngineerTasks.size() > 0) {
//                    throw new BusinessError("本图纸已出图");
//                }
//            }
//        }
    }

    /**
     * 任务节点撤回操作
     *
     * @param orgId
     * @param projectId
     * @param actInstSuspendDTO
     * @return
     */
    @Override
    public Boolean revocationAny(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO) {

        BpmProcess bpmProcess = processService.getBpmProcess(actInstSuspendDTO.getActInst().getProcessId());
        actInstSuspendDTO.setBpmProcess(bpmProcess);

        return taskExecService.revocationAny(context, orgId, projectId, actInstSuspendDTO);

    }

}

