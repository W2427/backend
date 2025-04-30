package com.ose.tasks.domain.model.service.bpm.taskexec;

import com.ose.util.*;
import com.ose.auth.vo.ExecutorRole;
import com.ose.auth.vo.UserPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.response.JsonResponseBody;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.taskpackage.TaskPackageBasicRepository;
import com.ose.tasks.domain.model.repository.wbs.WBSEntryStateRepository;
import com.ose.tasks.domain.model.service.bpm.ActTaskConfigInterface;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.delegate.BaseBpmTaskInterfaceDelegate;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.BpmnGatewayDTO;
import com.ose.tasks.dto.BpmnSequenceNodeDTO;
import com.ose.tasks.dto.WBSEntryTeamWorkSiteDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.HierarchyNode;
import com.ose.tasks.entity.ProcessBpmnRelation;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.taskpackage.TaskPackageAssignNodePrivileges;
import com.ose.tasks.entity.taskpackage.TaskPackageBasic;
import com.ose.tasks.entity.wbs.entry.WBSEntryState;
import com.ose.tasks.util.SpringUtils;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateStage;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.vo.BpmTaskDefKey;
import com.ose.vo.BpmTaskType;
import com.ose.vo.EntityStatus;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.*;

import static com.ose.tasks.vo.bpm.BpmCode.REVOCATION;
import static com.ose.tasks.vo.bpm.BpmCode.SUSPEND_MARK;

/**
 * 任务执行基类。
 * execResult.getVariables().put("jsonDocuments",documents); //设置文档信息
 * execResult.getVariables().put("setAttachmentsFlag","NONE");//设置是否设置附件
 * execResult.getVariables().put("setComment", execResult.getTodoTaskDTO().getComment());//设置 comment的内容
 */
@Component
public class TaskExecService implements TaskExecInterface {
    private final static Logger logger = LoggerFactory.getLogger(TaskExecService.class);
    private String strEXCLUSIVE_GATEWAY_RESULT = BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT.getType();  // RESULT

    private static Map<String, Integer> prioritySelectForPG = new HashMap<String, Integer>(){{
       put("ACC",1);
       put("ACCEPT",1);
       put("AWC",2);
       put("NG",3);
       put("REJECT",3);
    }};
    private final TodoTaskBaseInterface todoTaskBaseService;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmExecInterface bpmExecService;

    private final UploadFeignAPI uploadFeignAPI;

    private final BpmActivityInstanceBlobRepository bpmActivityInstanceBlobRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final TaskPackageBasicRepository taskPackageBasicRepository;

    private final BpmEntitySubTypeRepository entitySubTypeRepository;

    private final ActTaskConfigInterface actTaskConfigService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final BpmActTaskRepository bpmActTaskRepository;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final ActivityTaskInterface activityTaskService;

    private final ProcessInterface processService;

    private final WBSEntryStateRepository wbsEntryStateRepository;

    // 上传文件的临时路径
    @Value("${application.files.temporary}")
    private String temporaryDir;

    // 上传文件的临时路径
    @Value("${application.files.protected}")
    private String protectedDir;


    /**
     * 构造方法
     *
     * @param
     * @param todoTaskBaseService                todoTaskBaseService
     * @param ruTaskRepository                   ruTaskRepository
     * @param bpmExecService
     * @param uploadFeignAPI
     * @param bpmActivityInstanceBlobRepository
     * @param bpmActivityInstanceStateRepository
     * @param taskPackageBasicRepository         taskPackageBasicRepository
     * @param entitySubTypeRepository            entityCategoryRepository
     * @param actTaskConfigService               actTaskConfigService
     * @param taskRuleCheckService               taskRuleCheckService
     * @param hiTaskinstRepository               hiTaskinstRepository
     * @param bpmActTaskRepository               bpmActTaskRepository
     * @param bpmActInstRepository               bpmActInstRepository
     * @param activityTaskService                activityTaskService
     * @param wbsEntryStateRepository
     * @return
     */
    @Autowired
    public TaskExecService(
        TodoTaskBaseInterface todoTaskBaseService,
        BpmRuTaskRepository ruTaskRepository,
        BpmExecInterface bpmExecService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        BpmActivityInstanceBlobRepository bpmActivityInstanceBlobRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
        TaskPackageBasicRepository taskPackageBasicRepository,
        BpmEntitySubTypeRepository entitySubTypeRepository,
        ActTaskConfigInterface actTaskConfigService,
        TaskRuleCheckService taskRuleCheckService,
        BpmHiTaskinstRepository hiTaskinstRepository,
        BpmActTaskRepository bpmActTaskRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        ActivityTaskInterface activityTaskService,
        ProcessInterface processService,
        WBSEntryStateRepository wbsEntryStateRepository) {
        this.todoTaskBaseService = todoTaskBaseService;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmExecService = bpmExecService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.entitySubTypeRepository = entitySubTypeRepository;
        this.bpmActivityInstanceBlobRepository = bpmActivityInstanceBlobRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.taskPackageBasicRepository = taskPackageBasicRepository;
        this.actTaskConfigService = actTaskConfigService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.bpmActTaskRepository = bpmActTaskRepository;
        this.bpmActInstRepository = bpmActInstRepository;
        this.activityTaskService = activityTaskService;
        this.processService = processService;
        this.wbsEntryStateRepository = wbsEntryStateRepository;
    }


    @Override
    public ExecResultDTO exec(BpmProcTaskDTO bpmProcTaskDTO) {
        /* -------------
           任务节点完成
           actRuTask 当前 任务节点 在 bpm.act_ru_task中
           ruTask 当前 任务节点 在 tasks.bpm_act_task中
           processKey 为 bpm.act_re_procdef 的 key_字段，为工序的 id DKDEJFDJ23234
           actInst 为 tasks.bpm_activity_instance_base 实例
         */

        //0.0 Initial taskExecDTO
        ExecResultDTO execResult = initTaskInitDTO(bpmProcTaskDTO);
        execResult.setExecResult(true);
        execResult.setParentTaskId(execResult.getRuTask().getParentTaskId());

        //0.1 执行前置代理任务
        //2.3 取得任务节点上的 设定信息，取得代理类的信息。执行代理类 post //todo POSTEXEC
        Map<String, List<String>> taskDelegateMap = actTaskConfigService.
            getConfigByTaskDefKey(execResult.getOrgId(),
                execResult.getProjectId(),
                execResult.getRuTask().getTaskDefKey(),
                execResult.getRuTask().getTaskType(),
                execResult.getBpmProcess().getProcessStage().getNameEn() + "-" + execResult.getBpmProcess().getNameEn(),
                ProcessType.valueOf(execResult.getBpmProcess().getProcessType().name()),
                execResult.getBpmProcess().getProcessCategory().getNameEn());

        if ((execResult.getOverrideDelegate() == null || !execResult.getOverrideDelegate()) && !CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.PRE.name()))) {
            execResult = execDelegate(execResult, BpmActTaskConfigDelegateStage.PRE, taskDelegateMap.get(BpmActTaskConfigDelegateStage.PRE.name()));
        }
        /*----
        此处应该设置 设置下个执行人的代理
         */
        if (!execResult.isExecResult()) {
            if (execResult.getErrorDesc() != null) {
                throw new BusinessError(execResult.getErrorDesc());
            }
            return execResult;//如果预执行 终止，则此处也终止

        }
        // 1.0 完成任务，
        execResult = completeTask(execResult);

        //如果执行不成功返回退出
        if (!execResult.isExecResult()) {
            execResult.setExecResult(false);
            throw new BusinessError("bpm run error");
        }

        //如果执行成功继续

        //2.0 如果完成 执行成功。更新 流程上的 变量值，包括内检时间等 exec

        try {
            execResult = updateVariables(execResult);
        } catch (Exception e) {
            execResult.setExecResult(false);
            e.printStackTrace();
            throw new BusinessError("Update Bpm Variable error");
        }

        //2.1 取得流程执行细节 从 bpm数据库中
//        execResult = getTaskExecDetail(execResult);

        //2.2 如果没有结果报错退出
//        if (!execResult.isExecResult()) {
//            throw new BusinessError("bpm run error");
//        }

        //3.1 保存 tasks.bpm_act_tasks数据
        execResult = saveBpmActTask(execResult);

        //3.2 设置历史完成信息,保存tasks.bpm_hi_taskinst中的数据 . exec
        execResult = saveBpmHiTaskInst(execResult);
//        System.out.println("临时输出 saveBpmHiTaskInst");

        //3.3  删除运行的任务 exec
        todoTaskBaseService.deleteRuTask(execResult.getRuTask().getId());

        //3.4 保存 tasks.bpm_ru_task 数据
        execResult = saveBpmRuTask(execResult);

        //2.3 取得任务节点上的 设定信息，取得代理类的信息。执行代理类 post //todo POSTEXEC
        if (!CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.POST.name()))) {
            execResult = execDelegate(execResult, BpmActTaskConfigDelegateStage.POST, taskDelegateMap.get(BpmActTaskConfigDelegateStage.POST.name()));
        }
        if (execResult.getErrorDesc() != null) {
            throw new BusinessError(execResult.getErrorDesc());
        }
//        todoTaskBaseService.updateProcessStageCount(execResult.getRuTask(), execResult.getActInst());

        //4.0 如果完成任务中的任务节点为空，更新工作流为完成，更新流程信息
        if (execResult.getNextTaskNodes().isEmpty() && !execResult.isNotFinished()) {
//            System.out.println("完成当前任务");
            //完成当前任务
            //4.1 完成当前任务
            execResult = completeActInst(execResult);
            //4.2 取得任务节点上的 设定信息，取得代理类的信息。执行阶段为COMPLETE的代理类 //todo POSTEXEC
            if (!CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.COMPLETE.name()))) {
                execResult = execDelegate(execResult, BpmActTaskConfigDelegateStage.COMPLETE, taskDelegateMap.get(BpmActTaskConfigDelegateStage.COMPLETE.name()));
            }
        }
        //设定执行人 的 工作放在 代理 NEXT_ASSIGN 中去执行
        else {
//            4.3 设定执行人
//            System.out.println("assign person");
            execResult = assignPerson(execResult);//completeInfo, actInst.getId());
        }

        //4.0 保存流程实例
        execResult = saveBpmActInst(execResult);//

        System.out.println("complete task");


        return execResult;
    }


    /**
     * 初始化 流程任务执行变量
     *
     * @param bpmProcTaskDTO
     * @return 流程任务执行变量 ExecResultDTO
     */
    protected ExecResultDTO initTaskInitDTO(BpmProcTaskDTO bpmProcTaskDTO) {
        ExecResultDTO execResult = new ExecResultDTO();

        execResult.setTodoTaskDTO(bpmProcTaskDTO.getTodoTaskDTO());
        execResult.setContext(bpmProcTaskDTO.getContext());
        execResult.setOrgId(bpmProcTaskDTO.getProject().getOrgId());
        execResult.setProjectId(bpmProcTaskDTO.getProject().getId());
        execResult.setRuTask(bpmProcTaskDTO.getRuTask());
        execResult.setActInst(bpmProcTaskDTO.getActInst());

//        BpmActivityInstanceState actInstState = new BpmActivityInstanceState();
        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(bpmProcTaskDTO.getActInst().getId());

        execResult.setActInstState(actInstState);
        execResult.setProcessId(bpmProcTaskDTO.getProcessId());
        execResult.setNextTasks(new ArrayList<>());
//        execResult.setCompleteInfo(new TaskCompleteResponseDTO());
//        execResult.setComplete(new TaskCompleteResponseDTO());
        execResult.setBpmProcess(bpmProcTaskDTO.getBpmProcess());
        execResult.setOverrideDelegate(bpmProcTaskDTO.getOverrideDelegate());
        execResult.setOverridePostDelegate(bpmProcTaskDTO.getOverridePostDelegate());

        return execResult;
    }

    /**
     * 完成 流程任务
     *
     * @param execResult 流程任务执行变量 execResultDTO
     * @return 流程任务执行变量 ExecResultDTO
     */
    protected ExecResultDTO completeTask(ExecResultDTO execResult) {

        TaskCompleteDTO completeDTO = new TaskCompleteDTO(); //任务完成的DTO，完成此任务
        completeDTO.setOrgId(execResult.getOrgId());
        completeDTO.setProjectId(execResult.getProjectId());
        completeDTO.setCommand(execResult.getTodoTaskDTO().getCommand());
        completeDTO.setComment(execResult.getTodoTaskDTO().getComment());
        completeDTO.setAttachFiles(execResult.getTodoTaskDTO().getAttachFiles());
        completeDTO.setPictures(execResult.getTodoTaskDTO().getPictures());
        completeDTO.setOperator(execResult.getContext().getOperator().getName());
        completeDTO.setParentTaskId(execResult.getRuTask().getParentTaskId());
        String taskType = execResult.getRuTask().getTaskType();
        if (taskType != null) {
            BpmTaskType bpmTaskType;
            try {
                bpmTaskType = BpmTaskType.valueOf(taskType);
                completeDTO.setTaskType(taskType);
                completeDTO.setCheckNodeType(bpmTaskType.getCheckNodeType());
            } catch (Exception e) {
                //do nothing
            }
        }

        //检查节点的tasktype 是否 允许执行
        if (completeDTO.getTaskType() != null && completeDTO.isCheckNodeType() //要求检查任务类型
            && !completeDTO.getTaskType().equalsIgnoreCase(execResult.getRuTask().getTaskType())) {//并且 任务节点和bpmn上预设不相同
            execResult.setExecResult(false);
            execResult.setErrorDesc("TaskType " + execResult.getRuTask().getTaskType() + " Not match " + completeDTO.getTaskType());
            return execResult;
        }

        List<String> attachFiles = completeDTO.getAttachFiles();
        List<ActReportDTO> attachments = new ArrayList<>();
        List<ActReportDTO> pics = new ArrayList<>();

        if (attachFiles != null) {
            List<String> fileIds = new ArrayList<>();


            for (String attach : attachFiles) {
                String attachmentName = attach;
                logger.error("任务执行1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(execResult.getOrgId().toString(),
                    execResult.getProjectId().toString(), attachmentName,
                    new FilePostDTO());
                logger.error("任务执行1 保存docs服务->结束");
                FileES fileEs = fileESResBody.getData();
                if (fileEs != null && fileEs.getId() != null) {
                    String attachmentType = "file";
                    String filePath = fileEs.getPath();
                    String url = fileEs.getId();
                    fileIds.add(url);

                    ActReportDTO r = new ActReportDTO();
                    r.setFileId(LongUtils.parseLong(fileEs.getId()));
                    r.setReportNo(fileEs.getName());
                    r.setFilePath(filePath);
                    attachments.add(r);
                }
            }
//            execResult.setAttachments(attachments);

        }

        List<String> pictures = completeDTO.getPictures();
        if (pictures != null) {
            List<Map<String, String>> maps = new ArrayList<>();
            for (String attach : pictures) {
                String attachmentName = attach;
                logger.error("任务执行2 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(execResult.getOrgId().toString(),
                    execResult.getProjectId().toString(), attachmentName,
                    new FilePostDTO());
                logger.error("任务执行2 保存docs服务->结束");
                FileES fileEs = fileESResBody.getData();
                if (fileEs != null && fileEs.getId() != null) {
                    String attachmentType = "pic";
                    String filePath = fileEs.getPath();
                    String url = fileEs.getId();

                    ActReportDTO p = new ActReportDTO();
                    p.setFileId(LongUtils.parseLong(fileEs.getId()));
                    p.setReportNo(fileEs.getName());
                    p.setFilePath(fileEs.getPath());
                    pics.add(p);
                    attachments.add(p);
                }
            }

        }

        execResult.setAttachments(attachments);
        execResult.setPics(pics);
        String comment = completeDTO.getComment();
//        BpmActivityInstanceBase actInst = execResult.getActInst();
//        BpmActivityInstanceBlob actInstBlob = null;
//        if(CollectionUtils.isEmpty(attachments)) {
//            actInstBlob = bpmActivityInstanceBlobRepository.findByBaiId(actInst.getId());
//            if(actInstBlob != null) actInstBlob.set
//        }

        if (comment == null) {
            comment = "";
        }

        HashMap<String, Object> variables = new HashMap<String, Object>();
        Map<String, Object> command = completeDTO.getCommand();
        String operate = " ";
        if (command != null && !command.isEmpty()) {
//                System.out.println(command.toString());
            Iterator<String> iterator = command.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                operate += key + ":" + command.get(key) + ",";
            }
            operate = operate.substring(0, operate.length() - 1);
            variables.putAll(command);
        } else {
            operate = "";
            variables.put(strEXCLUSIVE_GATEWAY_RESULT, "");
        }

        comment = comment + "\r\n Operator " + completeDTO.getOperator() + operate;

        execResult.setComments(comment);

        variables.put("UserName", ""); // 注意对应jbpm的${UserName}
        variables.put("MasterUserName", ""); // 注意对应jbpm的${MasterUserName}
        variables.put("QcUserName", ""); // 注意对应jbpm的${QcUserName}

        ProcessBpmnRelation processBpmnRelation = processService.getBpmnRelation(execResult.getProjectId(),
            execResult.getProcessId(), execResult.getActInst().getBpmnVersion(), execResult.getRuTask().getTaskDefKey());

        //处理并行网关
        List<BpmnSequenceNodeDTO> tmpSuNodes = processBpmnRelation.getJsonSuccessorNodes();
        Set<String> runningTaskKeys = new HashSet<>();
        Set<String> relatedParallelNodes = new HashSet<>();
        BpmActivityInstanceState actInstState = execResult.getActInstState();
        Map<String, Object> newCommand = new HashMap<>();
//        Set<String> gateWaySelects = new HashSet<>();
        if(!MapUtils.isEmpty(completeDTO.getCommand())) {// 处理并行网关的 拒绝情况
            for (Map.Entry<String, Object> kv : completeDTO.getCommand().entrySet()) {
                if (StringUtils.isEmpty(actInstState.getCurrentSelectForGateway()) || //选择条件为空
                    prioritySelectForPG.get(actInstState.getCurrentSelectForGateway()) == null || //未设定优先值
                    (prioritySelectForPG.get((String)kv.getValue()) != null && //当前选择的条件的优先值高
                    prioritySelectForPG.get((String)kv.getValue()) > prioritySelectForPG.get(actInstState.getCurrentSelectForGateway()))) {
                    actInstState.setCurrentSelectForGateway((String)kv.getValue());
                    execResult.setActInstState(actInstState);
//                    bpmActivityInstanceStateRepository.updateCurrentSelectForGateway(actInstState.getId(), (String)kv.getValue());
                    newCommand.put(kv.getKey(), kv.getValue());
                } else if(!StringUtils.isEmpty(actInstState.getCurrentSelectForGateway()) && prioritySelectForPG.get(actInstState.getCurrentSelectForGateway()) != null &&
                    (prioritySelectForPG.get((String)kv.getValue()) != null &&
                        prioritySelectForPG.get((String)kv.getValue()) < prioritySelectForPG.get(actInstState.getCurrentSelectForGateway()))){
                        newCommand.put(kv.getKey(), actInstState.getCurrentSelectForGateway());

                } else {
                    newCommand.put(kv.getKey(), kv.getValue());
                }
            }
        }

        boolean isParallelGw = false;
        if(!CollectionUtils.isEmpty(tmpSuNodes)) {

            for (BpmnSequenceNodeDTO sn : tmpSuNodes) {
                if (sn == null || sn.getGateways() == null) continue;
                for (BpmnGatewayDTO gw : sn.getGateways()) {
                    if (gw.getGatewayType().equalsIgnoreCase("PARALLEL") &&
                        !CollectionUtils.isEmpty(gw.getJsonIncomingNodes())) {
                        relatedParallelNodes.addAll(gw.getJsonIncomingNodes());
                        isParallelGw = true;
                    }
                }
            }

            if(!CollectionUtils.isEmpty(relatedParallelNodes)) {
                Long actInstId = execResult.getActInst().getId();
                List<BpmRuTask> relatedParallelRuTasks  = ruTaskRepository.findByActInstId(actInstId);
                if(!CollectionUtils.isEmpty(relatedParallelRuTasks)) {
                    relatedParallelRuTasks.forEach(rpt -> {
                        runningTaskKeys.add(rpt.getTaskDefKey());
                    });
                    runningTaskKeys.remove(execResult.getRuTask().getTaskDefKey());
                }
            }
        }


        BigInteger ruTaskCount = ruTaskRepository.getRunTaskCount(execResult.getActInst().getId(), execResult.getRuTask().getTaskDefKey());
        if((!CollectionUtils.isEmpty(runningTaskKeys) && relatedParallelNodes.size()>0 &&
            CollectionUtils.getSection(runningTaskKeys, relatedParallelNodes).size() == 0) ||
//            CollectionUtils.isEmpty(runningTaskKeys) ||
            (CollectionUtils.isEmpty(runningTaskKeys) && ruTaskCount.intValue() < 2)) {
            Set<NextTaskDTO> nextTaskNodes = new HashSet<>();
            if(isParallelGw) {
                nextTaskNodes = bpmExecService.evaluate(processBpmnRelation,
                    newCommand);
//                actInstState.setCurrentSelectForGateway(null);
//                execResult.setActInstState(actInstState);
            } else {
                if(execResult.getActInstState().getRejectCoSign() != null && execResult.getActInstState().getRejectCoSign() ){
                    Map<String, Object> rejectCoSignCommand = new HashMap<>();
                    rejectCoSignCommand.put("RESULT","REJECT");
                    nextTaskNodes = bpmExecService.evaluate(processBpmnRelation,
                        rejectCoSignCommand);
                }else {
                    nextTaskNodes = bpmExecService.evaluate(processBpmnRelation,
                        completeDTO.getCommand());
                }
            }
            execResult.getActInstState().setCurrentSelectForGateway(null);
            execResult.setNextTaskNodes(nextTaskNodes);
        } else {
            execResult.setNextTaskNodes(new HashSet<>());
        }
        if((!CollectionUtils.isEmpty(runningTaskKeys) && relatedParallelNodes.size()>0 &&
            CollectionUtils.getSection(runningTaskKeys, relatedParallelNodes).size() > 0) ||
            ruTaskCount.intValue() > 1) {
            execResult.setNotFinished(true);
        }

        //Cosign节点被拒
        if (completeDTO.getCommand().containsValue("REJECT") && execResult.getRuTask().getName().equals("CO-SIGN")){
            execResult.getActInstState().setRejectCoSign(true);
        }else if (!execResult.getRuTask().getName().equals("CO-SIGN")){
            execResult.getActInstState().setRejectCoSign(false);
        }

        return execResult;
    }


    /**
     * 创建工作流
     *
     * @param createResult 环境上下文
     * @return
     */

    @Override
    public CreateResultDTO preCreateActInst(CreateResultDTO createResult) {

        OperatorDTO operatorDTO = createResult.getContext().getOperator();
        ActivityInstanceDTO actInstDTO = createResult.getActInstDTO();
        Long orgId = createResult.getOrgId();
        Long projectId = createResult.getProjectId();
        Long processId = actInstDTO.getProcessId();

        //判断流程文件是否存在 todo 此处待核对
        BpmReDeployment bpmReDeployment = processService.findActivityModel(orgId, projectId, processId);
        if (bpmReDeployment == null) {
            createResult.setCreateResult(false);
            createResult.setErrorDesc("流程文件不存在");
            createResult.setActInst(new BpmActivityInstanceBase());
            createResult.setActInstState(new BpmActivityInstanceState());
            return createResult;
        }

        // 任务包人员信息
        List<TaskPackageAssignNodePrivileges> taskPackageAssignNodePrivileges = new ArrayList<>();

        if (!createResult.isCreateResult())
            return createResult;

        Long allocatee = operatorDTO.getId();

        String entityNo = actInstDTO.getEntityNo();
        Integer weldRepairCount = 0;

        Long entitySubTypeId = actInstDTO.getEntitySubTypeId();
        Long processStageId = actInstDTO.getProcessStageId();

        BpmActivityInstanceBase actInst = new BpmActivityInstanceBase();
        BpmActivityInstanceState actInstState = new BpmActivityInstanceState();

        BpmEntitySubType entitySubType = entitySubTypeRepository.findById(entitySubTypeId).orElse(null);
        if (entitySubType != null) {
            actInst.setEntitySubType(entitySubType.getNameEn());
            actInst.setEntitySubTypeId(entitySubType.getId());
            BpmEntityType entityType = entitySubType.getEntityType();
            if (entityType != null) {
                actInst.setEntityType(entityType.getNameEn());
                actInst.setEntityTypeId(entityType.getId());
            }
        }

        actInst.setOrgId(orgId);
        actInst.setProjectId(projectId);
        actInstState.setOrgId(orgId);
        actInstState.setProjectId(projectId);
        actInstState.setBaiId(actInst.getId());
        actInst.setPlanStartDate(actInstDTO.getPlanStart());
        actInst.setPlanEndDate(actInstDTO.getPlanEnd());
        actInst.setOwnerId(actInstDTO.getAssignee());
        actInst.setOwnerName(actInstDTO.getAssigneeName());
        actInst.setAllocatee(allocatee);
        actInst.setAllocateeDate(new Date());
        actInst.setStatus(EntityStatus.ACTIVE);
        actInst.setBpmnVersion(bpmReDeployment.getVersion());
        actInst.setEntityId(actInstDTO.getEntityId());
        actInstState.setEntityId(actInst.getEntityId());
        actInst.setEntityType(actInstDTO.getEntityType() == null ? actInstDTO.getEntityType() : actInstDTO.getEntityType());
        actInst.setEntityNo(entityNo);
        if (actInstDTO.getEntityNo1() != null){
            actInst.setEntityNo1(actInstDTO.getEntityNo1());
        }
        if (actInstDTO.getEntityNo2() != null){
            actInst.setEntityNo2(actInstDTO.getEntityNo2());
        }

        actInst.setDrawingTitle(actInstDTO.getDrawingTitle());
        actInst.setEntitySubType(actInstDTO.getEntitySubType());
        actInst.setEntitySubTypeId(entitySubTypeId);

        actInst.setProcessStage(actInstDTO.getProcessStage());
        actInst.setProcessStageId(processStageId);
        actInst.setProcess(actInstDTO.getProcess());
        actInst.setProcessId(processId);
        actInst.setPlanHour(actInstDTO.getPlanHour());
        actInstState.setMemo(actInstDTO.getMemo());

        //设置版本
        actInst = todoTaskBaseService.setActInstVersion(actInst, actInstDTO, orgId, projectId);

        //查找流程对应的实体
        WBSEntryState wbsEntryState = wbsEntryStateRepository.findByProjectIdAndEntityIdAndStageAndProcessAndDeletedIsFalse(
            projectId, actInstDTO.getEntityId(), actInstDTO.getProcessStage(), actInstDTO.getProcess()).orElse(null);

        if (wbsEntryState != null) {
            // TODO 设置场地及工作班组
            WBSEntryTeamWorkSiteDTO teamWorkSiteDTO = todoTaskBaseService.getWBSEntryTeamWorkSiteInfo(orgId, projectId, null, wbsEntryState, actInst.getProcessId());
            actInstState.setWorkSiteId(teamWorkSiteDTO.getWorkSiteId());
            actInstState.setWorkSiteName(teamWorkSiteDTO.getWorkSiteName());
            actInstState.setWorkSiteAddress(teamWorkSiteDTO.getWorkSiteAddress());
            actInstState.setTeamId(teamWorkSiteDTO.getTeamId());
            actInstState.setTeamName(teamWorkSiteDTO.getTeamName());
//            if (wbsEntry.getTeamId() != null) {
//                Organization team = organizationFeignAPI.details(wbsEntry.getTeamId(), null).getData();
//                if (team != null) {
//                    actInst.setTeamName(team.getName());
//                }
//            }

            actInstState.setTaskPackageId(wbsEntryState.getTaskPackageId());
            Long taskPackageId = wbsEntryState.getTaskPackageId();
            if (taskPackageId != null) {
                TaskPackageBasic taskPackageBasic = taskPackageBasicRepository.findById(taskPackageId).orElse(null);
                if (taskPackageBasic != null) {
                    actInstState.setTaskPackageName(taskPackageBasic.getName());
                }
            }

            wbsEntryState.setWorkSiteId(teamWorkSiteDTO.getWorkSiteId());
            wbsEntryState.setWorkSiteName(teamWorkSiteDTO.getWorkSiteName());
            wbsEntryState.setTeamId(teamWorkSiteDTO.getTeamId());
//            wbsEntryStateRepository.save(wbsEntryState);
        }

        Long entityId = actInstDTO.getEntityId();

        List<HierarchyNode> hierarchyNodes = todoTaskBaseService.__getParentEntitiesByEntityId(projectId, entityId); // ttoddo
        for (HierarchyNode hierarchyNode : hierarchyNodes) {
            if ("WP01".equals(hierarchyNode.getNode().getEntityType())) {
                actInst.setEntityModuleName(hierarchyNode.getNo());
                actInst.setEntityModuleProjectNodeId(hierarchyNode.getId());
            }
        }

        createResult.setActInst(actInst);
        createResult.setActInstState(actInstState);


        return createResult;


    }


    /**
     * 创建工作流
     *
     * @param createResult 创建流程DTO
     * @return BpmActivityInstance 工作流实例
     */
    @Override
    public CreateResultDTO createActInst(CreateResultDTO createResult) {

        Long entitySubTypeId = createResult.getActInstDTO().getEntitySubTypeId();//entitysubtype/type ID
        Long orgId = createResult.getOrgId();
        Long projectId = createResult.getProjectId();
        Long processId = createResult.getProcess().getId();
        BpmActivityInstanceBase actInst = createResult.getActInst();
        BpmActivityInstanceState actInstState = createResult.getActInstState();
        ActivityInstanceDTO actInstDTO = createResult.getActInstDTO();

//        if (createResult.getActInstDTO().getEntityType() != null) {
//            actInst.setEntityType(createResult.getActInstDTO().getEntityType());
//        } else if (entitySubTypeId != null) {
//            BpmEntityCategory entitiyCategory = null;
//            Optional<BpmEntityCategory> op = entityCategoryRepository.findById(entitySubTypeId);
//            if (op.isPresent()) {
//                entitiyCategory = op.get();
//            }
//            if (entitiyCategory != null) {
//                BpmEntityCategoryType entityType = entitiyCategory.getEntityType();
//                if (entityType != null) {
//                    actInst.setEntityType(entityType.getNameEn());
//                    actInst.setEntityTypeId(entityType.getId());
//                }
//            }
//        }


        ActTaskCreateDTO createDTO = new ActTaskCreateDTO();
        createDTO.setAssignee(actInstDTO.getAssignee());
        createDTO.setProcessId(processId);
        createDTO.setProjectId(projectId);
//        JsonObjectResponseBody<ActTaskCreateResponseDTO> response = actTaskFeignAPI.create(createDTO);
//            ActTaskCreateResponseDTO responseDTO = response.getData();
//        actInst.setId(CryptoUtils.uniqueDecId());
        actInst.setStartDate(new Date());
        actInstState.setStartDate(actInst.getStartDate());
        actInstState.setEndDate(null);
        actInstState.setFinishState(ActInstFinishState.NOT_FINISHED);
//            if (responseDTO.isSuspended()) {
//                actInstState.setSuspensionState(SuspensionState.SUSPEND); //ftjftj suspend
//            } else {
        actInstState.setSuspensionState(SuspensionState.ACTIVE);
//            }


        todoTaskBaseService.saveActInst(actInst);

        bpmActivityInstanceStateRepository.save(actInstState);

        //保存checklist 到 docsMaterial表
        todoTaskBaseService.saveCheckListToDocsMaterialTable(processId, actInst.getEntityId(), projectId, actInst);


        createResult.setActInst(actInst);
        createResult.setActInstState(actInstState);


        return createResult;
    }

    /**
     * todo
     */

    @Override
    public CreateResultDTO postCreateActInst(CreateResultDTO createResult) {

        // 查找当前任务流程下的所有任务节点
        Long actInstId = createResult.getActInst().getId();
        Long operatorId = createResult.getContext().getOperator().getId();
        if (actInstId == null) {
            createResult.setCreateResult(false);
            return createResult;
        }
        Long projectId = createResult.getProjectId();
//        JsonListResponseBody<BpmActRuTask> response = actTaskFeignAPI.getRuTask(actInstId);

//        List<BpmActRuTask> actRuTasks = response.getData();
        ProcessBpmnRelation processBpmnRelation = new ProcessBpmnRelation();
        processBpmnRelation.setNodeType("STARTEVENT");
        Long processId = createResult.getProcess().getId();
        int bpmnVersion = createResult.getActInst().getBpmnVersion();

        processBpmnRelation = processService.getBpmnRelation(projectId,
            processId, bpmnVersion, "startevent1");
        if (processBpmnRelation == null) {
            processBpmnRelation = processService.getBpmnRelation(projectId,
                processId, bpmnVersion, "startevent2");
        }
        if (processBpmnRelation == null) {
            throw new NotFoundError();
        }

        Set<NextTaskDTO> nextTaskNodes =
            bpmExecService.evaluate(processBpmnRelation,
                new HashMap<String, Object>());

        nextTaskNodes.forEach(nextTaskNode -> {
            BpmRuTask bpmRuTask = new BpmRuTask();
            bpmRuTask.setId(CryptoUtils.uniqueDecId());
            bpmRuTask.setAssignee(null);
            bpmRuTask.setCategory(nextTaskNode.getCategory());
            bpmRuTask.setTaskType(nextTaskNode.getTaskType());//taskType
            bpmRuTask.setCreateTime(new Timestamp(new Date().getTime()));//new Timestamp(new Date().getTime()));
            bpmRuTask.setDelegation(null);
            bpmRuTask.setDescription(nextTaskNode.getDesc());
            bpmRuTask.setName(nextTaskNode.getTaskName());
            bpmRuTask.setOwner(createResult.getContext().getOperator().getId().toString());
            bpmRuTask.setActInstId(createResult.getActInst().getId());
            bpmRuTask.setSuspensionState(1);
            bpmRuTask.setTaskDefKey(nextTaskNode.getTaskDefKey());
            bpmRuTask.setTenantId(projectId.toString());
            bpmRuTask.setParentTaskId(createResult.getActInstDTO() == null ? null : createResult.getActInstDTO().getParentTaskId());
            ruTaskRepository.save(bpmRuTask);

            BpmHiTaskinst hiTask = new BpmHiTaskinst();

            hiTask.setTaskId(bpmRuTask.getId());//(hiActinst.getTaskId());
            hiTask.setSeq(bpmRuTask.getSeq());
            hiTask.setAssignee(bpmRuTask.getAssignee());
            hiTask.setCategory(bpmRuTask.getCategory());
            hiTask.setTaskType(bpmRuTask.getTaskType()); //taskType
            hiTask.setDescription(bpmRuTask.getDescription());
            hiTask.setEndTime(null);
            hiTask.setName(bpmRuTask.getName());
            hiTask.setOwner(bpmRuTask.getOwner());
            hiTask.setParentTaskId(bpmRuTask.getParentTaskId());
            hiTask.setProcDefId(createResult.getActInst().getProcessId().toString() + ":" + createResult.getActInst().getBpmnVersion());
            hiTask.setActInstId(bpmRuTask.getActInstId());
            hiTask.setStartTime(bpmRuTask.getCreateTime());
            hiTask.setTaskDefKey(bpmRuTask.getTaskDefKey());
            hiTask.setTenantId(bpmRuTask.getTenantId());
            hiTask.setOperator(operatorId.toString());
            hiTask.setParentTaskId(bpmRuTask.getParentTaskId());

            if (todoTaskBaseService.saveBpmHiTaskinst(hiTask) == null) {
                throw new BusinessError("can't not save Bpm History");
            }
        });

        return createResult;
    }

    /**
     * 更新工作流上设置的变量值
     *
     * @param execResult 执行结果
     */
    private ExecResultDTO updateVariables(ExecResultDTO execResult) {

        Map<String, Object> variables = execResult.getTodoTaskDTO().getVariables();
        Long orgId = execResult.getOrgId();
        Long projectId = execResult.getProjectId();
        Long processId = execResult.getProcessId();
        BpmRuTask ruTask = execResult.getRuTask();

        if (variables != null) {
            Set<String> keySet = variables.keySet();
            Iterator<String> keyIterator = keySet.iterator();
            while (keyIterator.hasNext()) { //遍历传过来的变量
                String key = keyIterator.next();

                BpmActInstVariableConfig variable = todoTaskBaseService.getVariableByVariableName(orgId, projectId, key,
                    processId); //根据变量名取得变量
                if (variable != null) { //如果不为空，更新 流程变量
                    BpmActInstVariableValue variableValue =
                        todoTaskBaseService.findBpmActInstVariableValue(orgId, projectId,
                            ruTask.getActInstId(), key); //在 BpmActInstVariable中找到变量
                    if (variableValue == null) { //如果变量为空
                        variableValue = new BpmActInstVariableValue(); //新建新的变量
                        variableValue.setCreatedAt();
                        variableValue.setLastModifiedAt();
                        variableValue.setOrgId(orgId);
                        variableValue.setActInstId(ruTask.getActInstId());
                        variableValue.setProjectId(projectId);
                        variableValue.setStatus(EntityStatus.ACTIVE);
                        variableValue.setVariableDisplayName(variable.getDisplayName());
                        variableValue.setVariableName(variable.getName());
                        variableValue.setVariableType(variable.getType());
                    } else {
                        variableValue.setLastModifiedAt();
                    }
                    variableValue.setValue(variables.get(key).toString());
                    todoTaskBaseService.saveBpmActInstVariableValue(variableValue); //保存变量

                }
            }
        }
        return execResult;
    }


    /**
     * 执行流程节点上配置的 代理任务
     *
     * @param execResult
     */

    private ExecResultDTO execDelegate(ExecResultDTO execResult, BpmActTaskConfigDelegateStage delegateStage, List<String> proxyNames) {

        BpmRuTask ruTask = execResult.getRuTask();
        Long orgId = execResult.getOrgId();
        Long projectId = execResult.getProjectId();
        TodoTaskExecuteDTO todoTaskDTO = execResult.getTodoTaskDTO();
//        TaskCompleteResponseDTO completeInfo = execResult.getCompleteInfo();
//        TaskCompleteResponseDTO complete = execResult.getComplete();
        ContextDTO context = execResult.getContext();

//        List<BpmActTaskConfig> taskConfigs = todoTaskBaseService.getConfigByProcDefIdAndActTaskId(actRuTask.getProcDefId(), actRuTask,
//            delegateType); //todo 更新此方法
        if (proxyNames != null
            && !proxyNames.isEmpty()) {
            for (String proxyName : proxyNames) {
//                String proxyName = taskConfig.getProxy();
                try {
                    Class clazz = Class.forName(proxyName);
                    String className = StringUtils.lowerFirst(proxyName.substring(proxyName.lastIndexOf(".") + 1));
                    BaseBpmTaskInterfaceDelegate delegate = (BaseBpmTaskInterfaceDelegate) SpringContextUtils.getBean(className, clazz);
                    Map<String, Object> data = new HashMap<String, Object>();

                    data.put("orgId", orgId);
                    data.put("projectId", projectId);
                    data.put("taskIds", new Long[]{ruTask.getId()});
                    data.put("operator", context.getOperator());
                    data.put("variables", todoTaskDTO.getVariables());
//                    data.put("actRuTasks", completeInfo.getActRuTasks());
                    data.put("command", todoTaskDTO.getCommand());
                    data.put("attachments", execResult.getAttachments());
                    if (delegateStage == BpmActTaskConfigDelegateStage.POST)
                        execResult = delegate.postExecute(context, data, execResult);
                    else if (delegateStage == BpmActTaskConfigDelegateStage.PRE)
                        execResult = delegate.preExecute(context, data, execResult);
                    else if (delegateStage == BpmActTaskConfigDelegateStage.COMPLETE)
                        execResult = delegate.completeExecute(execResult);
//                    if(!execResult.isExecResult()) return execResult; //如果不再执行退出
                } catch (Exception e) {
                    if (execResult.getErrorDesc() == null) {
                        execResult.setErrorDesc("DELEGATE ERROR" + e.toString());
                    }
                    execResult.setExecResult(false);
                    e.printStackTrace(System.out);
                }
            }
        }
        return execResult;
    }

    /**
     * 执行流程节点上配置的 批处理 代理任务
     *
     * @param todoBatchTaskDTO
     */

    private <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchExecDelegate(ContextDTO context,
                                                                                    P todoBatchTaskCriteriaDTO,
                                                                                    TodoBatchTaskDTO todoBatchTaskDTO,
                                                                                    BpmActTaskConfigDelegateStage delegateStage,
                                                                                    List<String> proxyNames) {

        Long orgId = todoBatchTaskDTO.getOrgId();
        Long projectId = todoBatchTaskDTO.getProjectId();

//        List<BpmActTaskConfig> taskConfigs = todoTaskBaseService.getConfigByProcDefIdAndActTaskId(actRuTask.getProcDefId(), actRuTask,
//            delegateType); //todo 更新此方法
        if (proxyNames != null
            && !proxyNames.isEmpty()) {
            for (String proxyName : proxyNames) {
//                String proxyName = taskConfig.getProxy();
                try {
                    BaseBpmTaskInterfaceDelegate delegate = null;
                    try {
                        Class clazz = Class.forName(proxyName);
                        delegate = (BaseBpmTaskInterfaceDelegate) SpringContextUtils.getBean(clazz);
                    } catch (Exception e) {
                        String[] tmpArr = proxyName.split("/");
                        delegate = (BaseBpmTaskInterfaceDelegate) SpringUtils.getBean(StringUtils.lowerFirst(tmpArr[tmpArr.length - 1]));
                    }
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", orgId);
                    data.put("projectId", projectId);
                    data.put("operator", context.getOperator());
                    if (delegateStage == BpmActTaskConfigDelegateStage.BATCH_POST)
                        todoBatchTaskDTO = delegate.batchPostExecute(context, data, todoBatchTaskCriteriaDTO, todoBatchTaskDTO);
                    else if (delegateStage == BpmActTaskConfigDelegateStage.BATCH_PRE)
                        todoBatchTaskDTO = delegate.batchPreExecute(context, data, todoBatchTaskCriteriaDTO, todoBatchTaskDTO);
                    else if (delegateStage == BpmActTaskConfigDelegateStage.BATCH_COMPLETE)
                        todoBatchTaskDTO = delegate.batchCompleteExecute(context, data, todoBatchTaskCriteriaDTO, todoBatchTaskDTO);
//                    if(!execResult.isExecResult()) return execResult; //如果不再执行退出
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                    if (todoBatchTaskDTO.getErrorDesc() == null) {
                        todoBatchTaskDTO.setErrorDesc("DELEGATE ERROR" + e.toString());
                    }
                    return todoBatchTaskDTO;
                }
            }
        }
        return todoBatchTaskDTO;
    }


    /**
     * 保存 BpmActTask的工作流数据
     *
     * @param execResult
     */
    private ExecResultDTO saveBpmActTask(ExecResultDTO execResult) {

        BpmRuTask ruTask = execResult.getRuTask();
        Long projectId = execResult.getProjectId();
        BpmActivityInstanceBase actInst = execResult.getActInst();
        TodoTaskExecuteDTO todoTaskDTO = execResult.getTodoTaskDTO();
        OperatorDTO operator = execResult.getContext().getOperator();

//       处理附件字段 exec
        BpmActTask actTask = new BpmActTask();
        List<ActReportDTO> documents;

        List<ActReportDTO> attachments = execResult.getAttachments();


        if (ruTask != null) {
//                    RED-MARK 流程 并且节点为i USER-DRAWING-DESIGN， 更新图纸信息。
            if (execResult.getVariables() != null && execResult.getVariables().get("jsonDocuments") != null) { //之前保存了 jsonDocuments
                documents = (List<ActReportDTO>) execResult.getVariables().get("jsonDocuments");
            } else {
//                    其他流程存储文档。
                documents = ruTask.getJsonDocuments();
            }
            actTask.setJsonDocuments(documents);
        }

//      取得当前的运行任务，保存报告信息.exec
        List<ActReportDTO> reports = new ArrayList<>();
        if (ruTask != null) {
            reports = ruTask.getJsonReportsReadOnly();
        }

//       存储报告 post
        for (ActReportDTO r : reports) {
            actTask.addJsonDocuments(r);
        }


//      如果当前节点 不是 发图节点。设置附件。post
        actTask.setAttachments(StringUtils.toJSON(attachments));
//      如果当前流程为 签字节点。设置节点备注。post
        if (execResult.getVariables() != null && execResult.getVariables().get("setComment") != null) { //如果 设置是否保存 comments
            actTask.setComment((String) execResult.getVariables().get("setComment"));
        } else {
            //其他情况设置完成的备注。post
            actTask.setComment(execResult.getComments());
        }

//      设置任务节点完成的信息。post
        actTask.setCostHour(todoTaskDTO.getCostHour());
        actTask.setPictures(StringUtils.toJSON(execResult.getPics()));
        actTask.setOperatorId(operator.getId());
        actTask.setOperatorName(operator.getName());
        actTask.setTaskId(ruTask == null ? null : ruTask.getId());
        actTask.setAssigneeId(LongUtils.parseLong(ruTask.getAssignee()));
        actTask.setCreatedAt();
        actTask.setLastModifiedAt();
        actTask.setStatus(EntityStatus.ACTIVE);
        actTask.setActInstId(actInst.getId());
        actTask.setTaskDefKey(ruTask.getTaskDefKey());
        String taskDefKey = ruTask.getTaskDefKey();
//        BpmTaskDefKey bpmTaskDefKey = BpmTaskDefKey.valueof(taskDefKey);
        String taskType = ruTask.getTaskType();
//        }
        actTask.setTaskType(taskType); //设置 task——type


        //
        String privilege = ruTask.getCategory();
        UserPrivilege userPrivilege = UserPrivilege.valueof(privilege);
        ExecutorRole executorRole = null;

        if (userPrivilege != null) {
            executorRole = userPrivilege.getExecutorRole();
        }


        String executors;
        if (execResult.getVariables() != null && execResult.getVariables().get("executors") != null) {
            executors = (String) execResult.getVariables().get("executors");
        } else {
            executors = operator.getName();
        }

        actTask.setExecutors(executors);
        actTask.setExecutorRole(executorRole);
        actTask.setProjectId(projectId);
        //保存 tasks 中的任务节点信息. post
        bpmActTaskRepository.save(actTask);

        return execResult;

    }


    /**
     * 保存 tasks.bpm_hi_taskinst 表中的数据
     *
     * @param execResult execResult
     */
    private ExecResultDTO saveBpmHiTaskInst(ExecResultDTO execResult) {
//        TaskCompleteResponseDTO completeInfo = execResult.getCompleteInfo();

        OperatorDTO operator = execResult.getContext().getOperator();
        Long parentTaskId = execResult.getRuTask().getParentTaskId();

        BpmRuTask ruTask = execResult.getRuTask();
        if (ruTask == null) System.out.println("actHiTaskinst is null");
        BpmHiTaskinst oldHiTask = hiTaskinstRepository.findByTaskId(ruTask.getId());
        if (oldHiTask != null) {
            oldHiTask.setEndTime(new Date());
            oldHiTask.setAssignee(ruTask.getAssignee());
            hiTaskinstRepository.save(oldHiTask);
        }

        return execResult;

    }

    /**
     * 保存 流程实例
     *
     * @param execResult execResult
     */
    private ExecResultDTO saveBpmActInst(ExecResultDTO execResult) {
        BpmRuTask ruTask = execResult.getRuTask();
        BpmActivityInstanceBase actInst = execResult.getActInst();
        if (actInst == null) throw new NotFoundError();
        Long projectId = actInst.getProjectId();
        BpmActivityInstanceState actInstState = execResult.getActInstState();
        BpmActivityInstanceBlob actInstBlob = bpmActivityInstanceBlobRepository.findByBaiId(actInst.getId());

        List<ActReportDTO> reports = new ArrayList<>();
//                BpmRuTask rutask = todoTaskBaseService.findBpmRuTaskByActTaskId(actTaskId);
        if (ruTask != null) {
            reports = ruTask.getJsonReportsReadOnly();
        }
//                BpmActivityInstance actInsta = todoTaskBaseService.findActInstByActInstId(actInstId);
        if (reports != null && reports.size() > 0 && actInstBlob == null) {
            actInstBlob = new BpmActivityInstanceBlob();
            actInstBlob.setOrgId(actInst.getOrgId());
            actInstBlob.setProjectId(projectId);
            actInstBlob.setBaiId(actInst.getId());
        }
        for (ActReportDTO report : reports) {
            actInstBlob.addJsonReports(report);
        }
        List<BpmRuTask> ruTaskList = ruTaskRepository.findByActInstId(actInst.getId());
        String currentExecutor = todoTaskBaseService.setTaskAssignee(ruTaskList);

        actInstState.setCurrentExecutor(currentExecutor);
        bpmActivityInstanceStateRepository.save(actInstState);
        if (actInstBlob != null)
            bpmActivityInstanceBlobRepository.save(actInstBlob);
        if (!execResult.getNextTaskNodes().isEmpty()) {
            if (todoTaskBaseService.saveBpmActivityInstance(actInst) == null) {
                execResult.setExecResult(false);
                throw new BusinessError("can not save act inst");
            }
        }
        return execResult;
    }

    /**
     * 保存 tasks.bpm_ru_task数据
     *
     * @return execResult
     */

    private ExecResultDTO saveBpmRuTask(ExecResultDTO execResult) {
//        TaskCompleteResponseDTO completeInfo = execResult.getCompleteInfo();
        Set<NextTaskDTO> nextTaskNodes = execResult.getNextTaskNodes();
        List<ExecResultDTO> nextTasks = new ArrayList<>();
        Long operatorId = execResult.getContext().getOperator().getId();
        if(!CollectionUtils.isEmpty(nextTaskNodes)) {

            for (NextTaskDTO nextTaskNode : nextTaskNodes) {

                ExecResultDTO nextTask = new ExecResultDTO();
                TodoTaskExecuteDTO nextTodoTaskDTO = new TodoTaskExecuteDTO();


                //TODO  会签节点需要 到 代理类中处理

                BpmRuTask bpmRuTask = new BpmRuTask();
                bpmRuTask.setSeq(0);
                bpmRuTask.setAssignee(null);
                bpmRuTask.setCategory(nextTaskNode.getCategory());
                bpmRuTask.setTaskType(nextTaskNode.getTaskType());//taskType
                bpmRuTask.setCreateTime(new Timestamp(new Date().getTime()));
                bpmRuTask.setDelegation(null);
                bpmRuTask.setDescription(nextTaskNode.getDesc());
                bpmRuTask.setName(nextTaskNode.getTaskName());
                bpmRuTask.setOwner(execResult.getContext().getOperator().getId().toString());
                bpmRuTask.setParentTaskId(execResult.getRuTask().getParentTaskId());
                bpmRuTask.setActInstId(execResult.getActInst().getId());
                bpmRuTask.setSuspensionState(1);
                bpmRuTask.setTaskDefKey(nextTaskNode.getTaskDefKey());
                bpmRuTask.setTenantId(execResult.getProjectId().toString());
                nextTodoTaskDTO.setTaskId(bpmRuTask.getId());
                todoTaskBaseService.saveBpmRuTask(bpmRuTask);

                BpmHiTaskinst hiTask = new BpmHiTaskinst();

                hiTask.setTaskId(bpmRuTask.getId());//(hiActinst.getTaskId());
                hiTask.setSeq(bpmRuTask.getSeq());

                hiTask.setAssignee(bpmRuTask.getAssignee());
                hiTask.setCategory(bpmRuTask.getCategory());
                hiTask.setTaskType(bpmRuTask.getTaskType()); //taskType
                hiTask.setDescription(bpmRuTask.getDescription());
                hiTask.setEndTime(null);
                hiTask.setName(bpmRuTask.getName());
                hiTask.setOwner(bpmRuTask.getOwner());
                hiTask.setProcDefId(execResult.getProcessId().toString() + ":" + execResult.getActInst().getBpmnVersion());
                hiTask.setActInstId(bpmRuTask.getActInstId());
                hiTask.setStartTime(bpmRuTask.getCreateTime());
                hiTask.setTaskDefKey(bpmRuTask.getTaskDefKey());
                hiTask.setTenantId(bpmRuTask.getTenantId());
                hiTask.setOperator(operatorId.toString());
                hiTask.setParentTaskId(bpmRuTask.getParentTaskId());

                if (todoTaskBaseService.saveBpmHiTaskinst(hiTask) == null) {
                    throw new BusinessError("can't not save Bpm History");
                }

                nextTask.setRuTask(bpmRuTask);
                nextTodoTaskDTO.setId(bpmRuTask.getId());
                nextTodoTaskDTO.setNextAssignee(execResult.getTodoTaskDTO().getNextAssignee());
                if(taskRuleCheckService.isCounterSignTaskNode(nextTaskNode.getTaskDefKey(), nextTaskNode.getTaskType())) {
                    nextTodoTaskDTO.setAssignees(execResult.getTodoTaskDTO().getAssignees());
                }
                nextTask.setTodoTaskDTO(nextTodoTaskDTO);

                nextTasks.add(nextTask);
            }
        }
        execResult.setNextTasks(nextTasks);
        return execResult;
    }


    /**
     * 完成当前任务
     *
     * @param execResult 执行结果
     */
    private ExecResultDTO completeActInst(ExecResultDTO execResult) {
        BpmActivityInstanceState actInstState = execResult.getActInstState();
//        BpmActHiTaskinst actHiTaskinst = execResult.getCompleteInfo().getActHiTaskinst();
        actInstState.setFinishState(ActInstFinishState.FINISHED);
        actInstState.setEndDate(new Timestamp(new Date().getTime()));
        bpmActivityInstanceStateRepository.save(actInstState);

        /*----
          9.5 启动后续的四级计划
                     */
        // 启动四级计划的后置任务处理
        BpmProcess process = processService.getBpmProcess(execResult.getProcessId());
        if (process != null) {
            execResult.setExecResult(false);
            return execResult;
        }

        return execResult;
    }

    /**
     * 指定人人员
     *
     * @param execResult execResult
     */
    private ExecResultDTO assignPerson(ExecResultDTO execResult) {
//        TaskCompleteResponseDTO completeInfo = execResult.getCompleteInfo();
        Long actInstId = execResult.getActInst().getId();

        List<ExecResultDTO> nextTasks = execResult.getNextTasks();
        // System.out.println(assigneeInfos);
        for (ExecResultDTO nextTask : nextTasks) {
            if (!taskRuleCheckService.isCounterSignTaskNode(nextTask.getRuTask().getTaskDefKey(), nextTask.getRuTask().getTaskType())
            && !nextTask.getRuTask().getTaskDefKey().equals(BpmTaskDefKey.USERTASK_GOE_APPROVE.getType())) {

                BpmActTaskAssignee actAssignee = todoTaskBaseService.getTaskAssigneesByTaskInfo(nextTask.getRuTask().getTaskDefKey(),
                nextTask.getRuTask().getName(), actInstId);
                if (actAssignee != null && actAssignee.getAssignee() != null) {
//                actTaskFeignAPI.assignee(actInstId, assigneeInfo.getTaskDefKey(), assigneeInfo.getTaskName(),
//                    actAssignee.getAssignee());
                //非会签节点

                    todoTaskBaseService.assignee(actInstId, nextTask.getRuTask().getTaskDefKey(), nextTask.getRuTask().getName(),
                        actAssignee.getAssignee(), execResult.getActInst());
                }
            }
        }
        return execResult;
    }

    @Override
    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchExec(ContextDTO contextDTO,
                                                                           Map<String, Object> data,
                                                                           P todoBatchTaskCriteriaDTO) {
        logger.info("1、开始外检处理 ->  " + new Date() + " " + StringUtils.toJSON(todoBatchTaskCriteriaDTO.getTaskIds()));
        TodoBatchTaskDTO todoBatchTaskDTO = new TodoBatchTaskDTO();

        Map<Long, BpmActivityInstanceState> actInstStateMap = new HashMap<>();

        todoBatchTaskDTO.setBatchExecuteResult(true);

        List<String> taskIds = new ArrayList<>();

        List<Long> actInstIds = new ArrayList<>();
        Boolean isBatchCompleted = false;

        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        String taskDefKey = todoBatchTaskCriteriaDTO.getTaskDefKey();
        todoBatchTaskDTO.setOrgId(orgId);
        todoBatchTaskDTO.setProjectId(projectId);
        todoBatchTaskDTO.setBatchActInstStateMap(actInstStateMap);
        String taskType = todoBatchTaskCriteriaDTO.getTaskType();
        String processKey = null;
        ProcessType processType = null;
        String processCategory = null;
        BpmProcess bpmProcess = null;
        String attachFileStr = todoBatchTaskCriteriaDTO.getAttachFiles();
        String[] attachFiles;
        String[] picFiles;
        Map<Long, Set<Long>> completeTasksMap = new HashMap<>();
        double costHours = 0.0;
        String picFileStr = todoBatchTaskCriteriaDTO.getPicFiles();
        if (attachFileStr != null) {

        }
        //批处理完成代理被执行
        List<BpmActivityInstanceBase> batchActInsts = new ArrayList<>();
        Long processId = todoBatchTaskCriteriaDTO.getProcessId();
        if (processId != null && processId != 0L) {
            bpmProcess = processService.getBpmProcess(processId);
            if (bpmProcess != null) {
                processKey = bpmProcess.getProcessStage() + "-" + bpmProcess.getNameEn();
                processType = bpmProcess.getProcessType();
                processCategory = bpmProcess.getProcessCategory().getNameEn();
                todoBatchTaskDTO.setProcess(bpmProcess.getNameEn());
            }
        }


        // 取得任务节点上的 设定信息，取得代理类的信息。执行代理类 post //todo POSTEXEC
        Map<String, List<String>> taskDelegateMap = actTaskConfigService.
            getBatchTaskConfigByTaskDefKey(orgId,
                projectId,
                taskDefKey,
                taskType,
                processKey,
                processType,
                processCategory);
        if (!CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.BATCH_PRE.name()))) {
            // TODO 比较慢
            todoBatchTaskDTO = batchExecDelegate(contextDTO, todoBatchTaskCriteriaDTO, todoBatchTaskDTO, BpmActTaskConfigDelegateStage.BATCH_PRE, taskDelegateMap.get(BpmActTaskConfigDelegateStage.BATCH_PRE.name()));
            if (!todoBatchTaskDTO.isBatchExecuteResult()) {
                if (todoBatchTaskDTO.getErrorDesc() != null) {
                    throw new BusinessError(todoBatchTaskDTO.getErrorDesc());
                }
                return todoBatchTaskDTO;
            }
        }
//        System.out.println("2、开始外检处理 ->  " + new Date());
        logger.info("2、开始外检处理 ->  " + new Date());

        // TODO 比较慢
        if (todoBatchTaskDTO.getErrorDesc() != null) {
            throw new BusinessError(todoBatchTaskDTO.getErrorDesc());
        }
        if (CollectionUtils.isEmpty(todoBatchTaskCriteriaDTO.getTaskIds())) {
            todoBatchTaskDTO.setBatchExecuteResult(false);
            return todoBatchTaskDTO;
        }
        List<Long> oldTaskIds = todoBatchTaskCriteriaDTO.getTaskIds();
        if (!CollectionUtils.isEmpty(oldTaskIds))
            costHours = todoBatchTaskCriteriaDTO.getCostHour() / oldTaskIds.size();

        Map<Long, Map<String, Object>> taskCommands = todoBatchTaskCriteriaDTO.getTaskCommands();
        Map<String, Object> command = todoBatchTaskCriteriaDTO.getCommand();

        //CHECK list at same task point
        Tuple tuple = ruTaskRepository.getStatisticData(oldTaskIds);
        if (tuple == null) {
            todoBatchTaskDTO.setBatchExecuteResult(false);
            throw new BusinessError("THE TASK IDS are EMPTY");
//            return todoBatchTaskDTO;
        }

        int totalCount = ((BigDecimal) tuple.get("totalCount")).intValue();
        String minKey = (String) tuple.get("minKey");
        String maxKey = (String) tuple.get("maxKey");
        if (oldTaskIds.size() != totalCount ||
            (minKey != null && !minKey.equalsIgnoreCase(maxKey))) {
            todoBatchTaskDTO.setBatchExecuteResult(false);
            logger.error("THERE IS SOME task NOT on the same point" + oldTaskIds.get(0));
            throw new BusinessError("THERE IS SOME task NOT on the same point" + oldTaskIds.get(0));
//            return todoBatchTaskDTO;
        }

        List<ActReportDTO> attachments = new ArrayList<>();
        List<ActReportDTO> pics = new ArrayList<>();
        //1.0 attachments
        if (!StringUtils.isEmpty(attachFileStr)) {
            attachFiles = attachFileStr.split(",");
            List<String> fileIds = new ArrayList<>();
            for (String attach : attachFiles) {
                String attachmentName = attach;
                logger.error("任务执行3 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(),
                    projectId.toString(), attachmentName,
                    new FilePostDTO());
                logger.error("任务执行3 保存docs服务->结束");
                FileES fileEs = fileESResBody.getData();
                if (fileEs != null && fileEs.getId() != null) {
                    String attachmentType = "file";
                    String filePath = fileEs.getPath();
                    String url = fileEs.getId();
                    fileIds.add(url);

                    ActReportDTO r = new ActReportDTO();
                    r.setFileId(LongUtils.parseLong(fileEs.getId()));
                    r.setReportNo(fileEs.getName());
                    r.setFilePath(filePath);
                    attachments.add(r);
                }
            }
            todoBatchTaskDTO.setAttachments(attachments);

        }

        //2.0 pics
        if (!StringUtils.isEmpty(picFileStr)) {
            picFiles = picFileStr.split(",");
            List<String> fileIds = new ArrayList<>();
            for (String picFile : picFiles) {
                String picName = picFile;
                logger.error("任务执行4 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(),
                    projectId.toString(), picName,
                    new FilePostDTO());
                logger.error("任务执行4 保存docs服务->结束");
                FileES fileEs = fileESResBody.getData();
                if (fileEs != null && fileEs.getId() != null) {
                    String fileType = "file";
                    String filePath = fileEs.getPath();
                    String url = fileEs.getId();
                    fileIds.add(url);

                    ActReportDTO r = new ActReportDTO();
                    r.setFileId(LongUtils.parseLong(fileEs.getId()));
                    r.setReportNo(fileEs.getName());
                    r.setFilePath(filePath);
                    pics.add(r);
                }
            }
            todoBatchTaskDTO.setPics(pics);
        }


        Map<String, ProcessBpmnRelation> processBpmnRelationMap = new HashMap<>();
        Map<String, Set<NextTaskDTO>> nextTaskMap = new HashMap<>();
        ProcessBpmnRelation processBpmnRelation;
        String entityCategory = null;
        String entityType = null;

        for (Long oldTaskId : oldTaskIds) {
            BpmRuTask oldRuTask = ruTaskRepository.findById(oldTaskId).orElse(null);
            BpmActivityInstanceBase actInst = bpmActInstRepository.findById(oldRuTask.getActInstId()).orElse(null);
            BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
            if (actInst == null) {
                throw new BusinessError("THERE IS NOT act instance " + oldRuTask.getActInstId());
            }

            processId = actInst.getProcessId();
            if (processId != null) {
                bpmProcess = processService.getBpmProcess(processId);
                if (bpmProcess != null) {
                    todoBatchTaskDTO.setProcess(bpmProcess.getNameEn());
                }
            }
            if (entityCategory == null) entityCategory = actInst.getEntitySubType();
            if (entityType == null) entityType = actInst.getEntityType();
            String processBpmnKey = projectId.toString() + processId.toString() + String.valueOf(actInst.getBpmnVersion()) + oldRuTask.getTaskDefKey();
            processBpmnRelation = processBpmnRelationMap.get(processBpmnKey);

            if (processBpmnRelation == null) {
                processBpmnRelation = processService.getBpmnRelation(projectId,
                    processId, actInst.getBpmnVersion(), oldRuTask.getTaskDefKey());
                if (processBpmnRelation == null) {
                    logger.error("projectId: " + projectId + " processId: " + processId + " bpmnVersion: " + actInst.getBpmnVersion() +
                        " taskDefKey : " + oldRuTask.getTaskDefKey());
                    throw new BusinessError("THERE IS NO BPMN setting");
                }
            }

            Map<String, Object> taskCommand;
            if (MapUtils.isNotEmpty(taskCommands) && MapUtils.isNotEmpty(taskCommands.get(oldTaskId))) {
                taskCommand = taskCommands.get(oldTaskId);
            } else {
                taskCommand = command;
            }
            if (taskCommand == null) {
                taskCommand = new HashMap<>();
            }
            String mapKey = "";
            mapKey += String.join("_", taskCommand.keySet());
            for (Object tv : taskCommand.values()) {
                mapKey = mapKey + "_" + tv;
            }
            String nextTaskNodeKey = processBpmnKey + mapKey;

            Set<NextTaskDTO> nextTaskNodes = nextTaskMap.get(nextTaskNodeKey);
            if (CollectionUtils.isEmpty(nextTaskNodes)) {
                nextTaskNodes = bpmExecService.evaluate(processBpmnRelation, taskCommand);
                nextTaskMap.put(nextTaskNodeKey, nextTaskNodes);
            }


            ExecResultDTO execResult = new ExecResultDTO();
            execResult.setExecResult(true);
            execResult.setBpmProcess(bpmProcess);
            execResult.setContext(contextDTO);
            execResult.setOrgId(orgId);
            execResult.setProjectId(projectId);
            execResult.setActInst(actInst);
            execResult.setActInstState(actInstState);
            execResult.setRuTask(oldRuTask);
            execResult.setProcessId(actInst.getProcessId());
            isBatchCompleted = true;
            if (execResult.getTodoTaskDTO() == null) {
                execResult.setTodoTaskDTO(new TodoTaskExecuteDTO());
            }
            execResult.getTodoTaskDTO().setCostHour(costHours);
            execResult.setNextTaskNodes(nextTaskNodes);

            //3.1 保存 tasks.bpm_act_tasks数据
            execResult = saveBpmActTask(execResult);

            //3.2 设置历史完成信息,保存tasks.bpm_hi_taskinst中的数据 . exec
            execResult = saveBpmHiTaskInst(execResult);

            //3.3  删除运行的任务 exec
            todoTaskBaseService.deleteRuTask(execResult.getRuTask().getId());

            //3.4 保存 tasks.bpm_ru_task 数据
            execResult = saveBpmRuTask(execResult);


            //4.0 如果完成任务中的任务节点为空，更新工作流为完成，更新流程信息
            if (execResult.getNextTaskNodes().isEmpty()) { // 如果没有ruTask,默认流程结束
                actInstState.setFinishState(ActInstFinishState.FINISHED);
                bpmActivityInstanceStateRepository.save(actInstState);
                todoBatchTaskDTO.getBatchActInstStateMap().put(actInst.getId(), actInstState);
                batchActInsts.add(actInst);
            } else {
//                System.out.println("3.9、开始外检处理 ->  " + new Date());
//                logger.info("3.9、开始外检处理  " + new Date());
                // 为运行时任务指定担当人
                execResult = assignPerson(execResult);//completeInfo, actInst.getId());
//                System.out.println("3.10、开始外检处理 ->  " + new Date());
//                logger.info("3.10、开始外检处理  " + new Date());
            }

            //4.0 保存流程实例
            execResult = saveBpmActInst(execResult);//
            Set<Long> newTaskIds = new HashSet<>();
            execResult.getNextTasks().forEach(execResultDTO -> {
                BpmRuTask nt = execResultDTO.getRuTask();
                if (nt != null) {
                    newTaskIds.add(nt.getId());
                }
            });
            completeTasksMap.put(oldTaskId, newTaskIds); // key:申请的taskId,value:安排的taskId

        }

//        System.out.println("4、开始外检处理 ->  " + new Date());
//        logger.info("4、开始外检处理 ->  " + new Date());
        todoBatchTaskDTO.setCompleteTasksMap(completeTasksMap);
        todoBatchTaskDTO.setProcInsts(actInstIds);
        todoBatchTaskDTO.setActTaskIds(taskIds);
        todoBatchTaskDTO.setEntitySubType(entityCategory);
        todoBatchTaskDTO.setEntityType(entityType);

        if (!todoBatchTaskDTO.isBatchExecuteResult()) {
            if (todoBatchTaskDTO.getErrorDesc() != null) {
                throw new BusinessError(todoBatchTaskDTO.getErrorDesc());
            }
            return todoBatchTaskDTO;
        }

        //Post_batch
        if (!CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.BATCH_POST.name()))) {
            todoBatchTaskDTO = batchExecDelegate(contextDTO, todoBatchTaskCriteriaDTO, todoBatchTaskDTO, BpmActTaskConfigDelegateStage.BATCH_POST, taskDelegateMap.get(BpmActTaskConfigDelegateStage.BATCH_POST.name()));
            if (!todoBatchTaskDTO.isBatchExecuteResult()) return todoBatchTaskDTO;
        }
//        System.out.println("5、开始外检处理 ->  " + new Date());
        logger.info("5、开始外检处理 ->  " + new Date());
        if (isBatchCompleted && !CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.BATCH_COMPLETE.name()))) {
            todoBatchTaskDTO.setBatchActInsts(batchActInsts);

            todoBatchTaskDTO = batchExecDelegate(contextDTO, todoBatchTaskCriteriaDTO, todoBatchTaskDTO, BpmActTaskConfigDelegateStage.BATCH_COMPLETE, taskDelegateMap.get(BpmActTaskConfigDelegateStage.BATCH_COMPLETE.name()));
            //执行 任务 完成代理 之后，设置标志 位
            if (todoBatchTaskDTO.getErrorDesc() != null) {
                throw new BusinessError(todoBatchTaskDTO.getErrorDesc());
            }
        }
//        System.out.println("开始外检处理结束 -> " + new Date());
        logger.info("开始外检处理结束 -> " + new Date());
        return todoBatchTaskDTO;

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


        BpmActivityInstanceBase actInst = actInstSuspendDTO.getActInst();

        String taskDefKey = actInstSuspendDTO.getTaskDefKey();

        Long actInstId = actInst.getId();

        Long userId = context.getOperator().getId();
        RevocationDTO revocationDTO = new RevocationDTO();//revocationResponseDTO.getData();
//        if (revocationResponseDTO.getSuccess()) {
        List<BpmHiTaskinst> hiTaskinsts = hiTaskinstRepository.findByActInstIdOrderByStartTimeAsc(actInstId);
        if (hiTaskinsts == null || hiTaskinsts.size() < 2 || hiTaskinsts.get(hiTaskinsts.size() - 1).getEndTime() != null) {
            throw new NotFoundError("CAN NOT FOUND Tasks OR MORE THAN ONE Running Task");
        }

        taskDefKey = hiTaskinsts.get(hiTaskinsts.size() - 2).getTaskDefKey();

        BpmHiTaskinst hiTaskinst = hiTaskinsts.get(hiTaskinsts.size() - 1);
        hiTaskinst.setDescription(REVOCATION);
        hiTaskinst.setEndTime(new Date());
        hiTaskinst.setDescription(actInstSuspendDTO.getMemo());
        activityTaskService.saveBpmHiTaskinst(hiTaskinst);

        BpmActTask task = new BpmActTask();
        task.setComment(actInstSuspendDTO.getMemo());
        task.setOperatorId(userId);
        task.setOperatorName(context.getOperator().getName());
        task.setActInstId(actInst.getId());
        task.setTaskId(hiTaskinst.getTaskId());
        task.setRemark(REVOCATION + "-" + hiTaskinst.getName());
        task.setCreatedAt();
        task.setStatus(EntityStatus.ACTIVE);
        task.setTaskType(hiTaskinst.getTaskType()); //task type

        task.setProjectId(projectId);
        task.setTaskDefKey(hiTaskinst.getTaskDefKey());

        String privilege = hiTaskinst.getCategory();
        UserPrivilege userPrivilege = UserPrivilege.valueof(privilege);
        ExecutorRole executorRole = null;
        if (userPrivilege != null) {
            executorRole = userPrivilege.getExecutorRole();
        }


        String executors = context.getOperator() == null ? null : context.getOperator().getName();

        task.setExecutors(executors);
        task.setExecutorRole(executorRole);

        bpmActTaskRepository.save(task);

        activityTaskService.deleteBpmRuTask(hiTaskinst.getId());//(actTaskInt.getTaskId());

        BpmHiTaskinst hiTask = hiTaskinstRepository.findFirstByActInstIdAndTaskDefKeyOrderByEndTimeDesc(actInstId, taskDefKey);

        BpmRuTask bpmRuTask = new BpmRuTask();
        bpmRuTask.setSeq(0);
        bpmRuTask.setAssignee(hiTask.getAssignee());
        bpmRuTask.setParentTaskId(hiTask.getParentTaskId());
        bpmRuTask.setHandling(false);
        bpmRuTask.setSuspensionState(1);
        bpmRuTask.setCategory(hiTask.getCategory());
        bpmRuTask.setId(CryptoUtils.uniqueDecId());
        bpmRuTask.setCreateTime(new Timestamp(new Date().getTime()));
        bpmRuTask.setName(hiTask.getName());
        bpmRuTask.setActInstId(actInstId);
        bpmRuTask.setDescription(actInstSuspendDTO.getMemo());
        bpmRuTask.setOwner(context.getOperator().getId().toString());
        bpmRuTask.setTaskDefKey(taskDefKey);
        bpmRuTask.setTaskType(hiTask.getTaskType());
        bpmRuTask.setTenantId(hiTask.getTenantId());

        ruTaskRepository.save(bpmRuTask);


        BpmHiTaskinst curHiTask = new BpmHiTaskinst();
        curHiTask.setAssignee(hiTask.getAssignee());
        curHiTask.setSeq(bpmRuTask.getSeq());

        curHiTask.setParentTaskId(hiTask.getParentTaskId());
        curHiTask.setEndTime(null);
        curHiTask.setCategory(hiTask.getCategory());
        curHiTask.setTaskId(bpmRuTask.getId());
        curHiTask.setStartTime(new Timestamp(new Date().getTime()));
        curHiTask.setName(hiTask.getName());
        curHiTask.setActInstId(actInstId);
        curHiTask.setDescription(actInstSuspendDTO.getMemo());
        curHiTask.setOwner(context.getOperator().getId().toString());
        curHiTask.setTaskDefKey(taskDefKey);
        curHiTask.setTaskType(hiTask.getTaskType());
        curHiTask.setTenantId(hiTask.getTenantId());
        curHiTask.setOperator(userId.toString());
        curHiTask.setProcDefId(hiTask.getProcDefId());
        hiTaskinstRepository.save(curHiTask);

        // 取得任务节点上的 设定信息，取得代理类的信息。执行代理类 post //todo POSTEXEC
        Map<String, List<String>> taskDelegateMap = actTaskConfigService.
            getBatchTaskConfigByTaskDefKey(orgId,
                projectId,
                taskDefKey,
                bpmRuTask.getTaskType(),
                actInstSuspendDTO.getBpmProcess().getNameEn(),
                actInstSuspendDTO.getBpmProcess().getProcessType(),
                actInstSuspendDTO.getBpmProcess().getProcessCategory().getNameEn());

        if (!CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.REVOCATION.name()))) {
            revocationExecDelegate(context, orgId, projectId, actInstSuspendDTO, taskDelegateMap.get(BpmActTaskConfigDelegateStage.REVOCATION.name()));
        }


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

        // 取得任务节点上的 设定信息，取得代理类的信息。执行代理类 post //todo POSTEXEC

        String taskDefKey = actInstSuspendDTO.getTaskDefKey();
        Map<String, List<String>> taskDelegateMap = actTaskConfigService.
            getBatchTaskConfigByTaskDefKey(orgId,
                projectId,
                actInstSuspendDTO.getTaskDefKey(),
                actInstSuspendDTO.getTaskType(),
                actInstSuspendDTO.getBpmProcess().getNameEn(),
                actInstSuspendDTO.getBpmProcess().getProcessType(),
                actInstSuspendDTO.getBpmProcess().getProcessCategory().getNameEn());
        RevocationDTO revocationDTO = null;
        if (!CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.PRE_BATCH_REVOCATION.name()))) {
            revocationDTO = batchRevocationExecDelegate(context, orgId, projectId, actInstSuspendDTO, taskDelegateMap.get(BpmActTaskConfigDelegateStage.PRE_BATCH_REVOCATION.name()), BpmActTaskConfigDelegateStage.PRE_BATCH_REVOCATION);
        }


        Set<Long> taskIds = actInstSuspendDTO.getTaskIds();
        if (CollectionUtils.isEmpty(taskIds)) {
            return revocationDTO;
        }

        Map<Long, Long> taskIdActInstIdMap = new HashMap<>();
        for (Long taskId : taskIds) {
            BpmHiTaskinst hiTaskinst = hiTaskinstRepository.findByTaskId(taskId);
            taskIdActInstIdMap.put(taskId, hiTaskinst.getActInstId());

            Long userId = context.getOperator().getId();
            JsonObjectResponseBody<RevocationDTO> revocationResponseDTO;
//            if (taskDefKey != null) {
//                revocationResponseDTO = actTaskFeignAPI.revocationNode(hiTaskinst.getActInstId(), userId, taskDefKey);
//            } else {
//                revocationResponseDTO = actTaskFeignAPI.revocation(hiTaskinst.getActInstId(), userId);
//            }

//            revocationDTO = revocationResponseDTO.getData();
//            if (revocationResponseDTO.getSuccess()) {

            hiTaskinst.setOperator(userId.toString());
            activityTaskService.saveBpmHiTaskinst(hiTaskinst);

            BpmActTask task = new BpmActTask();
            task.setComment(actInstSuspendDTO.getMemo());
            task.setOperatorId(userId);
            task.setOperatorName(context.getOperator().getName());
            task.setActInstId(hiTaskinst.getActInstId());
            task.setTaskId(hiTaskinst.getTaskId());
            task.setRemark(REVOCATION + "-" + hiTaskinst.getName());
            task.setCreatedAt();
            task.setStatus(EntityStatus.ACTIVE);
            task.setTaskType(hiTaskinst.getTaskType());
            String privilege = hiTaskinst.getCategory();
            UserPrivilege userPrivilege = UserPrivilege.valueof(privilege);
            ExecutorRole executorRole = null;
            if (userPrivilege != null) {
                executorRole = userPrivilege.getExecutorRole();
            }


            String executors = context.getOperator() == null ? null : context.getOperator().getName();

            task.setExecutors(executors);
            task.setExecutorRole(executorRole);
            task.setProjectId(projectId);
            bpmActTaskRepository.save(task);

            activityTaskService.deleteBpmRuTask(hiTaskinst.getId());//actTaskInt.getTaskId());

            List<BpmRuTask> ruTasks = revocationDTO.getActRuTasks();
            for (BpmRuTask ruTask : ruTasks) {
//                BpmRuTask bpmRuTask = saveBpmActTask(actRuTask);
                activityTaskService.saveBpmRuTask(ruTask);
            }
//            }
        }

        if (!CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.POST_BATCH_REVOCATION.name()))) {
            revocationDTO = batchRevocationExecDelegate(context, orgId, projectId, actInstSuspendDTO, taskDelegateMap.get(BpmActTaskConfigDelegateStage.POST_BATCH_REVOCATION.name()), BpmActTaskConfigDelegateStage.POST_BATCH_REVOCATION);
        }

        return revocationDTO;
    }

    @Override
    public JsonResponseBody suspendTask(ContextDTO context, Long orgId, Long projectId, Long taskId, ActInstSuspendDTO actInstSuspendDTO) {

        BpmActivityInstanceBase actInst = activityTaskService.findActInstById(taskId);
        if (actInst == null) return null;

        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInst.getId());
        if (actInstState == null) return null;
        actInstSuspendDTO.setActInst(actInst);
        actInstSuspendDTO.setActInstState(actInstState);
        List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInst.getId());
        if (ruTasks == null) {
            throw new NotFoundError("NOT FOUND TASSK " + taskId.toString());

        }

        JsonResponseBody suspendResponse = new JsonResponseBody();// actTaskFeignAPI.updateStateSuspend(actInst.getId());
//        if (suspendResponse.getSuccess()) {
        actInstState.setSuspensionState(SuspensionState.SUSPEND);
        actInstState.setMemo(StringUtils.trim(actInstState.getMemo()) + SUSPEND_MARK + actInstSuspendDTO.getMemo());
        actInst.setLastModifiedAt();
        activityTaskService.saveActInst(actInst);
        ruTasks.forEach(ruTask -> {
            ruTask.setSuspensionState(0);
            ruTaskRepository.save(ruTask);
        });

        activityTaskService.createSuspensionTaskNode(actInst, actInstSuspendDTO, SuspensionState.SUSPEND, context.getOperator());
//        }
        suspendResponse.setSuccess(true);

        Long processId = actInst.getProcessId();
        if (processId == null) return suspendResponse;

        BpmProcess bpmProcess = processService.getBpmProcess(processId);
        if (bpmProcess == null) return suspendResponse;

        Map<String, List<String>> taskDelegateMap = actTaskConfigService.
            getSuspendConfigByTaskDefKey(orgId,
                projectId,
                actInstSuspendDTO.getTaskDefKey(),
                actInstSuspendDTO.getTaskType(),
                bpmProcess.getNameEn(),
                bpmProcess.getProcessType(),
                bpmProcess.getProcessCategory().getNameEn());
        suspendExecDelegate(context, orgId, projectId, actInstSuspendDTO, taskDelegateMap.get(BpmActTaskConfigDelegateStage.SUSPEND.name()));

        return suspendResponse;

    }


    /**
     * 执行流程节点上配置的 批处理 代理任务
     */

    private RevocationDTO revocationExecDelegate(ContextDTO context, Long orgId, Long projectId,
                                                 ActInstSuspendDTO actInstSuspendDTO,
                                                 List<String> proxyNames) {
        RevocationDTO revocationDTO = null;
        if (proxyNames != null
            && !proxyNames.isEmpty()) {
            for (String proxyName : proxyNames) {
                try {
                    Class clazz = Class.forName(proxyName);
                    BaseBpmTaskInterfaceDelegate delegate = (BaseBpmTaskInterfaceDelegate) SpringContextUtils.getBean(clazz);
                    revocationDTO = delegate.revocationExecute(context, orgId, projectId, actInstSuspendDTO);
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        return revocationDTO;
    }


    /**
     * 执行流程节点上配置的 批处理 代理任务
     */

    private RevocationDTO batchRevocationExecDelegate(ContextDTO context, Long orgId, Long projectId,
                                                      ActInstSuspendDTO actInstSuspendDTO,
                                                      List<String> proxyNames,
                                                      BpmActTaskConfigDelegateStage delegateStage) {
        RevocationDTO revocationDTO = new RevocationDTO();
        for (String proxyName : proxyNames) {
            try {
                Class clazz = Class.forName(proxyName);
                BaseBpmTaskInterfaceDelegate delegate = (BaseBpmTaskInterfaceDelegate) SpringContextUtils.getBean(clazz);
                if (delegateStage == BpmActTaskConfigDelegateStage.PRE_BATCH_REVOCATION)
                    revocationDTO = delegate.batchRevocationPreExecute(context, orgId, projectId, actInstSuspendDTO);
                else if (delegateStage == BpmActTaskConfigDelegateStage.POST_BATCH_REVOCATION)
                    revocationDTO = delegate.batchRevocationPostExecute(context, orgId, projectId, actInstSuspendDTO);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        return revocationDTO;
    }


    /**
     * 执行流程节点上配置的 SUSPEND 代理任务
     */

    private void suspendExecDelegate(ContextDTO context, Long orgId, Long projectId,
                                     ActInstSuspendDTO actInstSuspendDTO,
                                     List<String> proxyNames) {
        if (proxyNames == null || proxyNames.isEmpty()) return;

        for (String proxyName : proxyNames) {
            try {
                Class clazz = Class.forName(proxyName);
                BaseBpmTaskInterfaceDelegate delegate = (BaseBpmTaskInterfaceDelegate) SpringContextUtils.getBean(clazz);
                delegate.suspendExecute(context, orgId, projectId, actInstSuspendDTO);
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
    }

    /**
     * 任务节点撤回操作（任意节点）
     *
     * @param orgId
     * @param projectId
     * @param actInstSuspendDTO
     * @return
     */
    @Override
    public Boolean revocationAny(ContextDTO context, Long orgId, Long projectId, ActInstSuspendDTO actInstSuspendDTO) {


        BpmActivityInstanceBase actInst = actInstSuspendDTO.getActInst();

        String taskDefKey = actInstSuspendDTO.getTaskDefKey();

        Long actInstId = actInst.getId();

        Long userId = context.getOperator().getId();
        RevocationDTO revocationDTO = new RevocationDTO();

        // TODO 找到要撤回的任务历史 BpmHiTaskinst
        BpmHiTaskinst hiTask = hiTaskinstRepository.findFirstByActInstIdAndTaskDefKeyOrderByEndTimeDesc(actInstId, taskDefKey);

        if (hiTask == null) {
            return false;
        }
        // TODO 查找要撤回的节点后面的所有任务节点（含自己）List<BpmHiTaskinst>
        List<BpmHiTaskinst> hiTaskinsts = hiTaskinstRepository.getAfterHiTask(hiTask.getActInstId(), hiTask.getEndTime());

        hiTaskinstRepository.deleteAll(hiTaskinsts);
        // TODO 删除正在运行的任务 BpmRuTask
        List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(hiTask.getActInstId());
        if (ruTasks != null && ruTasks.size() > 0) {
            ruTaskRepository.deleteAll(ruTasks);
        }
        // TODO 删除BpmActTask List<BpmActTask>

        List<BpmActTask> bpmActTasks = bpmActTaskRepository.getAfterBpmActTask(
            hiTask.getActInstId(),
            hiTask.getEndTime()
        );
        bpmActTaskRepository.deleteAll(bpmActTasks);
        // TODO 创建新的BpmHiTaskinst
        BpmHiTaskinst curHiTask = new BpmHiTaskinst();
        curHiTask.setAssignee(hiTask.getAssignee());
        curHiTask.setSeq(hiTask.getSeq());
        curHiTask.setParentTaskId(hiTask.getParentTaskId());
        curHiTask.setEndTime(null);
        curHiTask.setCategory(hiTask.getCategory());
        curHiTask.setTaskId(CryptoUtils.uniqueDecId());
        curHiTask.setStartTime(new Timestamp(new Date().getTime()));
        curHiTask.setName(hiTask.getName());
        curHiTask.setActInstId(actInstId);
        curHiTask.setDescription(actInstSuspendDTO.getMemo());
        curHiTask.setOwner(context.getOperator().getId().toString());
        curHiTask.setTaskDefKey(taskDefKey);
        curHiTask.setTaskType(hiTask.getTaskType());
        curHiTask.setTenantId(hiTask.getTenantId());
        curHiTask.setOperator(userId.toString());
        curHiTask.setProcDefId(hiTask.getProcDefId());
        hiTaskinstRepository.save(curHiTask);
//        // TODO 创建新的BpmActTask
//        BpmActTask task = new BpmActTask();
//        task.setComment(actInstSuspendDTO.getMemo());
//        task.setOperatorId(userId);
//        task.setOperatorName(context.getOperator().getName());
//        task.setActInstId(actInst.getId());
//        task.setTaskId(curHiTask.getTaskId());
//        task.setCreatedAt();
//        task.setStatus(EntityStatus.ACTIVE);
//        task.setTaskType(hiTask.getTaskType()); //task type
//
//        task.setProjectId(projectId);
//        task.setTaskDefKey(hiTask.getTaskDefKey());
//
//        String privilege = hiTask.getCategory();
//        UserPrivilege userPrivilege = UserPrivilege.valueof(privilege);
//        ExecutorRole executorRole = null;
//        if (userPrivilege != null) {
//            executorRole = userPrivilege.getExecutorRole();
//        }
//
//        String executors = context.getOperator() == null ? null : context.getOperator().getName();
//
//        task.setExecutors(executors);
//        task.setExecutorRole(executorRole);
//
//        bpmActTaskRepository.save(task);
        // TODO 创建新的BpmRuTask
        BpmRuTask bpmRuTask = new BpmRuTask();
        bpmRuTask.setAssignee(hiTask.getAssignee());
        bpmRuTask.setSeq(0);
        bpmRuTask.setParentTaskId(hiTask.getParentTaskId());
        bpmRuTask.setHandling(false);
        bpmRuTask.setSuspensionState(1);
        bpmRuTask.setCategory(hiTask.getCategory());
        bpmRuTask.setId(curHiTask.getTaskId());
        bpmRuTask.setCreateTime(new Timestamp(new Date().getTime()));
        bpmRuTask.setName(hiTask.getName());
        bpmRuTask.setActInstId(actInstId);
        bpmRuTask.setDescription(actInstSuspendDTO.getMemo());
        bpmRuTask.setOwner(context.getOperator().getId().toString());
        bpmRuTask.setTaskDefKey(taskDefKey);
        bpmRuTask.setTaskType(hiTask.getTaskType());
        bpmRuTask.setTenantId(hiTask.getTenantId());

        ruTaskRepository.save(bpmRuTask);

        return true;
    }

}
