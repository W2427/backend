package com.ose.tasks.Listener.handler;

import com.ose.util.*;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.UserNameCriteriaDTO;
import com.ose.auth.vo.ExecutorRole;
import com.ose.auth.vo.UserPrivilege;
import com.ose.controller.BaseController;
import com.ose.dto.ContextDTO;
import com.ose.dto.EventModel;
import com.ose.dto.OperatorDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.tasks.domain.model.repository.ProjectNodeRepository;
import com.ose.tasks.domain.model.repository.ProjectRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.ServerConfig;
import com.ose.tasks.domain.model.service.bpm.ActTaskConfigInterface;
import com.ose.tasks.domain.model.service.bpm.ProcessInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspScheduleInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.domain.model.service.delegate.BaseBpmTaskInterfaceDelegate;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.ProjectNode;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.report.ExInspActInstRelation;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.bpm.BpmActTaskConfigDelegateStage;
import com.ose.tasks.vo.bpm.ExInspApplyStatus;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.vo.EntityStatus;
import com.ose.vo.InspectParty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

import static com.ose.vo.EventType.BPM_OPT_LOCK_ERROR;

@Component
public class BpmReportHandleErrorHandler extends BaseController implements EventHandlerInterface {

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final QCReportRepository qcReportRepository;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final ProjectNodeRepository projectNodeRepository;

    private final BpmActivityInstanceRepository actInstRepository;

    private final ExInspScheduleInterface exInspScheduleService;

    private final BpmRuTaskRepository ruTaskRepository;


    private final UserFeignAPI userFeignAPI;

    private final ProjectRepository projectRepository;

    private final ActTaskConfigInterface actTaskConfigService;

    private final BpmActTaskRepository bpmActTaskRepository;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final TaskRuleCheckService taskRuleCheckService;

    private final ProjectInterface projectService;

    private final ProcessInterface processService;

    private final BpmErrorRepository bpmErrorRepository;

    private final ServerConfig serverConfig;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final BpmActivityInstanceBlobRepository bpmActivityInstanceBlobRepository;

    private final static Logger logger = LoggerFactory.getLogger(BpmReportHandleErrorHandler.class);

    /**
     * 构造方法。
     *
     * @param exInspActInstRelationRepository
     * @param qcReportRepository
     * @param bpmExInspScheduleRepository
     * @param projectNodeRepository
     * @param actInstRepository
     * @param exInspScheduleService
     * @param ruTaskRepository
     * @param userFeignAPI
     * @param projectRepository
     * @param actTaskConfigService
     * @param bpmActTaskRepository
     * @param todoTaskBaseService
     * @param taskRuleCheckService
     * @param projectService
     * @param processService
     * @param bpmErrorRepository
     * @param serverConfig
     * @param bpmActivityInstanceStateRepository
     * @param bpmActivityInstanceBlobRepository
     */
    @Autowired
    BpmReportHandleErrorHandler(
        ExInspActInstRelationRepository exInspActInstRelationRepository,
        QCReportRepository qcReportRepository,
        BpmExInspScheduleRepository bpmExInspScheduleRepository,
        ProjectNodeRepository projectNodeRepository,
        BpmActivityInstanceRepository actInstRepository,
        ExInspScheduleInterface exInspScheduleService,
        BpmRuTaskRepository ruTaskRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        ProjectRepository projectRepository, ActTaskConfigInterface actTaskConfigService,
        BpmActTaskRepository bpmActTaskRepository,
        TodoTaskBaseInterface todoTaskBaseService,
        TaskRuleCheckService taskRuleCheckService,
        ProjectInterface projectService, ProcessInterface processService, BpmErrorRepository bpmErrorRepository, ServerConfig serverConfig, BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository, BpmActivityInstanceBlobRepository bpmActivityInstanceBlobRepository) {
        this.serverConfig = serverConfig;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.qcReportRepository = qcReportRepository;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.projectNodeRepository = projectNodeRepository;
        this.actInstRepository = actInstRepository;
        this.exInspScheduleService = exInspScheduleService;
        this.ruTaskRepository = ruTaskRepository;
        this.userFeignAPI = userFeignAPI;
        this.projectRepository = projectRepository;
        this.actTaskConfigService = actTaskConfigService;
        this.bpmActTaskRepository = bpmActTaskRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.taskRuleCheckService = taskRuleCheckService;
        this.projectService = projectService;
        this.processService = processService;
        this.bpmErrorRepository = bpmErrorRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.bpmActivityInstanceBlobRepository = bpmActivityInstanceBlobRepository;
    }

    @Override
    public void doHandler(EventModel model) {

        if (model == null) {
            throw new BusinessError("There is no EventModel");
        }



        Map<String, String> body = new HashMap<>();
        body.put("authorization", model.getAuthorization());
        body.put("userAgent", model.getUserAgent());
        body.put("operatorId", model.getOperatorId().toString());
        body.put("projectId", model.getProjectId().toString());
        body.put("taskDefKey", model.getTaskDefKey());
        body.put("entityId", model.getEntityId().toString());


        HttpUtils.postJSON(serverConfig.getUrl() + "/context", body, String.class);


    }

    public void handleTask(ContextDTO contextDTO, EventModel model) {

        Long projectId = model.getProjectId();
        Project project = projectRepository.findById(projectId).orElse(null);
        Long orgId = project.getOrgId();
        Long pId = model.getEntityId();
        String taskDefKey = model.getTaskDefKey();
        String operatorId = model.getOperatorId().toString();

        BpmError bpmError = new BpmError();
        bpmError.setCode(BPM_OPT_LOCK_ERROR.name());
        bpmError.setDate(new Date());
        bpmError.setOrgId(orgId);
        bpmError.setProjectId(projectId);
        bpmError.setActInstId(pId);
        bpmErrorRepository.save(bpmError);
        BpmRuTask ruTask = ruTaskRepository.findByActInstIdAndTaskDefKey(pId, taskDefKey);
        if (ruTask == null) {
            System.out.println("THERE IS NO RU TASK");
            return;
        }
        Long taskId = ruTask.getId();
        if (StringUtils.isEmpty(operatorId)) {
            UserNameCriteriaDTO userNameCriteriaDTO = new UserNameCriteriaDTO();
            List<String> names = new ArrayList<>();
            names.add("supper");
            userNameCriteriaDTO.setUserNames(names);
            operatorId = userFeignAPI.getUserByUsername(userNameCriteriaDTO).getData().get(0).getId().toString();
        }


        OperatorDTO operatorDTO = new OperatorDTO(LongUtils.parseLong(operatorId));
        contextDTO.setOperator(operatorDTO);
        Map<String, Object> command = new HashMap<>();

        command.put("RESULT", ruTask.getCurrentCommand());
        List<ExInspActInstRelation> exInsps = exInspActInstRelationRepository.findByActInstId(pId);
        if (CollectionUtils.isEmpty(exInsps)) return;
        Long scheduleId = exInsps.get(0).getExInspScheduleId();
        List<ExInspActInstRelation> exInspTotals = exInspActInstRelationRepository.findByExInspScheduleId(scheduleId);

        exInspTotals.forEach(exInsp -> {
            Long actInstId = exInsp.getActInstId();


            List<ExInspActInstRelation> exInspActInstRelations = exInspActInstRelationRepository.findByActInstId(actInstId);
            exInspActInstRelations.forEach(inspActInstRelation -> {
                BpmRuTask rt = ruTaskRepository.findByActInstIdAndTaskDefKey(inspActInstRelation.getActInstId(), taskDefKey);
                if (rt == null) {
                    System.out.println("THERE IS NO RU TASK");
                    return;
                }
                patchExec(contextDTO, project.getOrgId(), project.getId(), rt.getId(), command);
            });

            if ("UT-UPLOAD_EXTERNAL_INSPECTION_REPORT".equals(taskDefKey)) {

                exInspActInstRelations.forEach(exInspActInstRelation -> {
                    qcReportRepository.findById(exInspActInstRelation.getReportId()).ifPresent(
                        _report -> {
                            if (!ReportStatus.PENDING.equals(_report.getReportStatus())) {
                                _report.setReportStatus(ReportStatus.UPLOADED);
                                qcReportRepository.save(_report);
                            }
                        });

                    if (exInspActInstRelation.getExInspScheduleId() == null) return;
                    bpmExInspScheduleRepository.findById(exInspActInstRelation.getExInspScheduleId()).ifPresent(
                        _schedule -> {
                            _schedule.setState(ReportStatus.UPLOADED);
                            bpmExInspScheduleRepository.save(_schedule);
                        }
                    );
                });
            } else if ("UT-SEND_EXTERNAL_INSPECTION_EMAIL".equals(taskDefKey)) {
                exInspActInstRelations.forEach(exInspActInstRelation -> {
                    logger.info("3、正在发送外检邮件的scheduleId:" + scheduleId);
                    qcReportRepository.findById(exInspActInstRelation.getReportId()).ifPresent(
                        _report -> {
                            if (!ReportStatus.PENDING.equals(_report.getReportStatus())) {
                                _report.setReportStatus(ReportStatus.EMAIL_SENT);
                                qcReportRepository.save(_report);
                            }
                        }
                    );

                    if (exInspActInstRelation.getExInspScheduleId() == null) return;
                    bpmExInspScheduleRepository.findById(exInspActInstRelation.getExInspScheduleId()).ifPresent(
                        _schedule -> {
                            logger.info("3、正在发送外检邮件的scheduleId:" + scheduleId);
                            _schedule.setState(ReportStatus.EMAIL_SENT);
                            bpmExInspScheduleRepository.save(_schedule);
                        }
                    );
                });
            } else if ("UT-CHECK_EXTERNAL_INSPECTION_REPORT".equals(taskDefKey)) {
                exInspActInstRelations.forEach(exInspActInstRelation -> {
                    qcReportRepository.findById(exInspActInstRelation.getReportId()).ifPresent(
                        _report -> {
                            if (!ReportStatus.PENDING.equals(_report.getReportStatus())) {
                                _report.setReportStatus(ReportStatus.DONE);
                                qcReportRepository.save(_report);
                            }
                        }
                    );

                    if (exInspActInstRelation.getExInspScheduleId() == null) return;
                    bpmExInspScheduleRepository.findById(exInspActInstRelation.getExInspScheduleId()).ifPresent(
                        _schedule -> {
                            _schedule.setState(ReportStatus.DONE);
                            bpmExInspScheduleRepository.save(_schedule);
                        }
                    );
                });
            } else if ("UT-APPLY_EXTERNAL_INSPECTION".equals(taskDefKey)) {

                BpmActivityInstanceBase actInst = actInstRepository.findByProjectIdAndIdAndStatus(projectId, actInstId, EntityStatus.ACTIVE);
                if (actInst == null) return;
                ProjectNode pn = projectNodeRepository.findByProjectIdAndEntityIdAndStatus(actInst.getProjectId(), actInst.getEntityId(), EntityStatus.ACTIVE).orElse(null);
                if (pn == null) return;
                String discipline = pn.getDiscipline();
                patchExInspApplyHandle(contextDTO, taskId, discipline);
            }
        });
        bpmError.setMemo("DONE");
        bpmErrorRepository.save(bpmError);
    }

    private void patchExInspApplyHandle(ContextDTO context, Long taskIdOrg, String discipline) {
        Set<String> NDTS = new HashSet<>();
        NDTS.add("UT");
        NDTS.add("RT");
        NDTS.add("PT");
        NDTS.add("MT");
        NDTS.add("UT_MT");
        NDTS.add("PAUT");
        NDTS.add("PMI");

        BpmRuTask ruTask = ruTaskRepository.findById(taskIdOrg).orElse(null);
        if (ruTask == null) {
            System.out.println("ACT TASK NOT EXIST");
            return;
        }

        List<ActReportDTO> actReportDTOS = ruTask.getJsonReportsReadOnly();
        if (CollectionUtils.isEmpty(actReportDTOS)) {
            System.out.println("ACT TASK NOT EXIST");
            return;
        }
        String qrCode = actReportDTOS.get(0).getReportQrCode();

        QCReport qcReport = qcReportRepository.findByQrcodeAndStatus(qrCode, EntityStatus.ACTIVE);
        if (qcReport == null || qcReport.getReportStatus().equals(ReportStatus.CANCEL)) {
            System.out.println("NO Related QC REPORT");
            return;
        }
        OperatorDTO operator = context.getOperator();
        Long projectId = qcReport.getProjectId();

        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) return;

        Long orgId = project.getOrgId();

        List<ActReportDTO> reportDTOS = new ArrayList<>();
        ActReportDTO actReportDTO = new ActReportDTO();
        actReportDTO.setReportNo(qcReport.getReportNo());
        actReportDTO.setReportType(qcReport.getReportType() == null ? null : qcReport.getReportType().name());
        actReportDTO.setReportQrCode(qcReport.getQrcode());
        actReportDTO.setFileId(qcReport.getUploadFileId());
        actReportDTO.setSeriesNo(qcReport.getSeriesNo());
        actReportDTO.setProcess(qcReport.getProcess());
        reportDTOS.add(actReportDTO);
        String processStr = qcReport.getProcess();

        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.findTaskIdByOrgIdAndProjectIdAndReportId(orgId, projectId, qcReport.getId());

        if (CollectionUtils.isEmpty(taskProcIds)) return;

        List<Long> taskIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getTaskId() != null).
            map(TaskProcQLDTO::getTaskId).collect(Collectors.toList());


        if (CollectionUtils.isEmpty(taskIds)) {
            System.out.println("There is no task ids for qc report");
            return;
        }

        Set<Long> currentTaskIds = new HashSet<>();
        currentTaskIds.addAll(taskIds);

        for (Long taskId : taskIds) {
            Set<Long> tIds = patchExec(context, orgId, projectId, taskId, null);
            if (!CollectionUtils.isEmpty(tIds)) {
                currentTaskIds.remove(taskId);
                currentTaskIds.addAll(tIds);
            }
        }

        List<String> inParties = qcReport.getJsonInspectParties();
        Set<InspectParty> inspectParties = new HashSet<>();
        String coordinate = "NO_COORDINATE";

        inParties.forEach(inParty -> {
            inspectParties.add(InspectParty.valueOf(inParty));
        });

        ExInspScheduleDTO exInspScheduleDTO = new ExInspScheduleDTO();
        exInspScheduleDTO.setInspectParties(new ArrayList<>(inspectParties));

        exInspScheduleDTO.setCoordinateCategory(coordinate);
        exInspScheduleDTO.setComment("PATCH DATA");
        exInspScheduleDTO.setLocation("");
        exInspScheduleDTO.setName("");
        exInspScheduleDTO.setDiscipline(discipline);

        exInspScheduleDTO.setExternalInspectionTime(new Date());
        exInspScheduleDTO.setTaskIds(new ArrayList<>(currentTaskIds));
        Boolean isSendEmail = true;
        if (NDTS.contains(processStr)) isSendEmail = false;

        logger.info("BpmReportHandleErrorHandler->" + "开始创建 exInspSchedule ->" + new Date());
        BpmExInspSchedule exInspSchedule =
            exInspScheduleService.
                createExternalInspectionSchedule(orgId, projectId, exInspScheduleDTO, operator, false, isSendEmail);
        logger.info("BpmReportHandleErrorHandler->" + "结束创建 exInspScheduleId " + exInspSchedule.getId() + "->" + new Date());


        Map<String, Set<Long>> inspectPartyTaskIdsMap = new HashMap<>();
        inspectPartyTaskIdsMap.put("OWNER", currentTaskIds);
        exInspScheduleService.createExternalInspectionScheduleDetail(orgId, projectId,
            exInspSchedule, operator, inspectParties);

        /*---
        5.0 更新 外检 项目 的状态
         */
        if (!CollectionUtils.isEmpty(currentTaskIds))
            ruTaskRepository.updateStatus(ExInspApplyStatus.APPLY, new ArrayList<>(currentTaskIds));


        logger.info("BpmReportHandleErrorHandler->" + "开始更新报告 exInspScheduleId " + exInspSchedule.getId() + "->" + new Date());
        qcReport.setScheduleId(exInspSchedule.getId());
        qcReportRepository.save(qcReport);
        logger.info("BpmReportHandleErrorHandler->" + "更新后的报告 exInspScheduleId " + exInspSchedule.getId() + "->" + new Date());
    }

    private Set<Long> patchExec(ContextDTO context, Long orgId, Long projectId, Long taskId, Map<String, Object> command) {
        BpmProcTaskDTO bpmProcTaskDTO = new BpmProcTaskDTO();

        BpmRuTask ruTask = todoTaskBaseService.findFirstBpmRuTaskByActTaskId(taskId);

        if (ruTask == null) {
            return new HashSet<>();
        }
        String taskType = ruTask.getTaskType();


            TaskCompleteDTO completeDTO = new TaskCompleteDTO();
            completeDTO.setTaskType(taskType);
            completeDTO.setCommand(command);

        BpmActivityInstanceBase actInst = actInstRepository.findByProjectIdAndIdAndStatus(projectId, ruTask.getActInstId(), EntityStatus.ACTIVE);
        if(actInst == null) {
            return new HashSet<>();
        }
        BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(
            actInst.getId()
        );
        if(actInstState == null) {
            return new HashSet<>();
        }

        Long processId = actInst.getProcessId();

        bpmProcTaskDTO.setProcessId(processId);





        Set<Long> currentTaskIds = new HashSet<>();

        bpmProcTaskDTO.setRuTask(ruTask);




        bpmProcTaskDTO.setActInst(actInst);
        if (actInst != null) {
            BpmProcess bpmProcess = processService.getBpmProcess(actInst.getProcessId());
            bpmProcTaskDTO.setBpmProcess(bpmProcess);
        }


        TodoTaskExecuteDTO todoTaskDTO = new TodoTaskExecuteDTO();
        Project project = projectService.get(orgId, projectId);
        bpmProcTaskDTO.setContext(context);
        bpmProcTaskDTO.setTodoTaskDTO(todoTaskDTO);
        bpmProcTaskDTO.setProject(project);
        bpmProcTaskDTO.setOverrideDelegate(todoTaskDTO.getOverrideDelegate());
        bpmProcTaskDTO.setOverridePostDelegate(todoTaskDTO.getOverridePostDelegate());


        if (bpmProcTaskDTO.getActInst() == null

            || bpmProcTaskDTO.getRuTask() == null
            || bpmProcTaskDTO.getBpmProcess() == null
            || project == null
            || bpmProcTaskDTO.getBpmProcess() == null) {
            throw new NotFoundError("Not found the bpm process instance or task");
        }


        ExecResultDTO execResult = initTaskInitDTO(bpmProcTaskDTO);
        execResult.setExecResult(true);
        execResult.setActInstState(actInstState);
        execResult.setParentTaskId(execResult.getRuTask().getParentTaskId());



        Map<String, List<String>> taskDelegateMap = actTaskConfigService.
            getConfigByTaskDefKey(execResult.getOrgId(),
                execResult.getProjectId(),
                execResult.getRuTask().getTaskDefKey(),
                execResult.getRuTask().getTaskType(),
                execResult.getBpmProcess().getProcessStage().getNameEn() + "-" + execResult.getBpmProcess().getNameEn(),
                ProcessType.valueOf(execResult.getBpmProcess().getProcessType().name()),
                execResult.getBpmProcess().getProcessCategory().getNameEn());


        execResult = getTaskExecDetail(execResult);




        if (!execResult.isExecResult()) {
            throw new BusinessError("bpm run error");
        }


        execResult = saveBpmActTask(execResult);



        execResult = saveBpmHiTaskInst(execResult);




        todoTaskBaseService.deleteRuTask(execResult.getRuTask().getId());


        execResult = saveBpmRuTask(execResult);

        List<ExecResultDTO> nextExecResultDTOs = execResult.getNextTasks();
        if (!CollectionUtils.isEmpty(nextExecResultDTOs)) {
            nextExecResultDTOs.forEach(nextExecResultDTO -> {
                currentTaskIds.add(nextExecResultDTO.getRuTask().getId());
            });
        }


        if (!CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.POST.name()))) {
            execResult = execDelegate(execResult, BpmActTaskConfigDelegateStage.POST, taskDelegateMap.get(BpmActTaskConfigDelegateStage.POST.name()));
        }
        if (execResult.getErrorDesc() != null) {
            throw new BusinessError(execResult.getErrorDesc());
        }


        if (execResult.getNextTaskNodes().isEmpty()) {
            System.out.println("完成当前任务");


            execResult = completeActInst(execResult);

            if (!CollectionUtils.isEmpty(taskDelegateMap.get(BpmActTaskConfigDelegateStage.COMPLETE.name()))) {
                execResult = execDelegate(execResult, BpmActTaskConfigDelegateStage.COMPLETE, taskDelegateMap.get(BpmActTaskConfigDelegateStage.COMPLETE.name()));
            }
        }

        else {

            System.out.println("assign person");
            execResult = assignPerson(execResult);
        }


        saveBpmActInst(execResult);

        System.out.println("临时输出 saveBpmActInst");


        return currentTaskIds;
    }

    /**
     * 初始化 流程任务执行变量
     *
     * @param bpmProcTaskDTO 启动 DTO
     * @return 流程任务执行变量 ExecResultDTO
     */
    private ExecResultDTO initTaskInitDTO(BpmProcTaskDTO bpmProcTaskDTO) {
        ExecResultDTO execResult = new ExecResultDTO();

        execResult.setTodoTaskDTO(bpmProcTaskDTO.getTodoTaskDTO());
        execResult.setContext(bpmProcTaskDTO.getContext());
        execResult.setOrgId(bpmProcTaskDTO.getProject().getOrgId());
        execResult.setProjectId(bpmProcTaskDTO.getProject().getId());
        execResult.setRuTask(bpmProcTaskDTO.getRuTask());
        execResult.setActInst(bpmProcTaskDTO.getActInst());
        execResult.setProcessId(bpmProcTaskDTO.getProcessId());
        execResult.setNextTasks(new ArrayList<>());


        execResult.setBpmProcess(bpmProcTaskDTO.getBpmProcess());
        execResult.setOverrideDelegate(bpmProcTaskDTO.getOverrideDelegate());
        execResult.setOverridePostDelegate(bpmProcTaskDTO.getOverridePostDelegate());

        return execResult;
    }

    /**
     * 保存 tasks.bpm_hi_taskinst 表中的数据
     *
     * @param execResult execResult
     */
    private ExecResultDTO saveBpmHiTaskInst(ExecResultDTO execResult) {


        OperatorDTO operator = execResult.getContext().getOperator();
        Long parentTaskId = execResult.getRuTask().getParentTaskId();

        BpmRuTask ruTask = execResult.getRuTask();
        if (ruTask == null) System.out.println("actHiTaskinst is null");
        BpmHiTaskinst hiTask = new BpmHiTaskinst();


        if (ruTask != null) {
            hiTask.setTaskId(ruTask.getId());
            hiTask.setSeq(ruTask.getSeq());
            hiTask.setAssignee(ruTask.getAssignee());
            hiTask.setCategory(ruTask.getCategory());
            hiTask.setTaskType(ruTask.getTaskType());
            hiTask.setDescription(ruTask.getDescription());
            hiTask.setEndTime(new Date());
            hiTask.setName(ruTask.getName());
            hiTask.setOwner(ruTask.getOwner());
            hiTask.setParentTaskId(ruTask.getParentTaskId());
            hiTask.setProcDefId(execResult.getProcessId().toString() + ":" + execResult.getActInst().getBpmnVersion());
            hiTask.setActInstId(ruTask.getActInstId());
            hiTask.setStartTime(ruTask.getCreateTime());
            hiTask.setTaskDefKey(ruTask.getTaskDefKey());
            hiTask.setTenantId(ruTask.getTenantId());
            hiTask.setOperator(operator.getId().toString());
            hiTask.setParentTaskId(parentTaskId);
        }
        if (todoTaskBaseService.saveBpmHiTaskinst(hiTask) == null) {
            throw new BusinessError("can't not save Bpm History");
        }

        return execResult;

    }


    protected ExecResultDTO getTaskExecDetail(ExecResultDTO execResult) {

        BpmRuTask ruTask = execResult.getRuTask();











        return execResult;
    }

    /**
     * 保存 BpmActTask的工作流数据
     *
     * @param execResult
     */
    @SuppressWarnings("unchecked")
    private ExecResultDTO saveBpmActTask(ExecResultDTO execResult) {


        BpmRuTask ruTask = execResult.getRuTask();

        Long projectId = execResult.getProjectId();
        BpmActivityInstanceBase actInst = execResult.getActInst();
        TodoTaskExecuteDTO todoTaskDTO = execResult.getTodoTaskDTO();
        OperatorDTO operator = execResult.getContext().getOperator();


        BpmActTask actTask = new BpmActTask();
        List<ActReportDTO> documents;

        List<ActReportDTO> attachments = execResult.getAttachments();


        if (ruTask != null) {

            if (execResult.getVariables() != null && execResult.getVariables().get("jsonDocuments") != null) {
                documents = (List<ActReportDTO>) execResult.getVariables().get("jsonDocuments");
            } else {

                documents = ruTask.getJsonDocuments();
            }
            actTask.setJsonDocuments(documents);
        }


        List<ActReportDTO> reports = new ArrayList<>();
        if (ruTask != null) {
            reports = ruTask.getJsonReportsReadOnly();
        }


        for (ActReportDTO r : reports) {
            actTask.addJsonDocuments(r);
        }



        actTask.setAttachments(StringUtils.toJSON(attachments));

        if (execResult.getVariables() != null && execResult.getVariables().get("setComment") != null) {
            actTask.setComment((String) execResult.getVariables().get("setComment"));
        } else {

            actTask.setComment(execResult.getComments());
        }


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

        String taskType = ruTask.getTaskType();

        actTask.setTaskType(taskType);



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

        bpmActTaskRepository.save(actTask);

        return execResult;

    }

    /**
     * 保存 tasks.bpm_ru_task数据
     *
     * @return execResult
     */
    private ExecResultDTO saveBpmRuTask(ExecResultDTO execResult) {

        Set<NextTaskDTO> nextTaskNodes = execResult.getNextTaskNodes();
        List<ExecResultDTO> nextTasks = new ArrayList<>();
        for (NextTaskDTO nextTaskNode : nextTaskNodes) {

            ExecResultDTO nextTask = new ExecResultDTO();
            TodoTaskExecuteDTO nextTodoTaskDTO = new TodoTaskExecuteDTO();




            BpmRuTask bpmRuTask = new BpmRuTask();
            bpmRuTask.setAssignee(null);
            bpmRuTask.setSeq(0);
            bpmRuTask.setCategory(nextTaskNode.getCategory());
            bpmRuTask.setTaskType(nextTaskNode.getTaskType());
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
            bpmRuTask.setParentTaskId(execResult.getParentTaskId());
            todoTaskBaseService.saveBpmRuTask(bpmRuTask);

            nextTask.setRuTask(bpmRuTask);
            nextTodoTaskDTO.setId(bpmRuTask.getId());
            nextTodoTaskDTO.setNextAssignee(execResult.getTodoTaskDTO().getNextAssignee());
            nextTask.setTodoTaskDTO(nextTodoTaskDTO);
            nextTasks.add(nextTask);
        }
        execResult.setNextTasks(nextTasks);
        return execResult;
    }


    /**
     * 执行流程节点上配置的 代理任务
     *
     * @param execResult execResult
     */
    private ExecResultDTO execDelegate(ExecResultDTO execResult, BpmActTaskConfigDelegateStage delegateStage, List<String> proxyNames) {

        BpmRuTask ruTask = execResult.getRuTask();
        Long orgId = execResult.getOrgId();
        Long projectId = execResult.getProjectId();
        TodoTaskExecuteDTO todoTaskDTO = execResult.getTodoTaskDTO();


        ContextDTO context = execResult.getContext();

        if (proxyNames != null
            && !proxyNames.isEmpty()) {
            for (String proxyName : proxyNames) {
                try {
                    Class clazz = Class.forName(proxyName);
                    BaseBpmTaskInterfaceDelegate delegate = (BaseBpmTaskInterfaceDelegate) SpringContextUtils.getBean(clazz);
                    Map<String, Object> data = new HashMap<>();

                    data.put("orgId", orgId);
                    data.put("projectId", projectId);
                    data.put("taskIds", new Long[]{ruTask.getId()});
                    data.put("operator", context.getOperator());
                    data.put("variables", todoTaskDTO.getVariables());

                    data.put("command", todoTaskDTO.getCommand());
                    data.put("attachments", execResult.getAttachments());
                    if (delegateStage == BpmActTaskConfigDelegateStage.POST)
                        execResult = delegate.postExecute(context, data, execResult);
                    else if (delegateStage == BpmActTaskConfigDelegateStage.PRE)
                        execResult = delegate.preExecute(context, data, execResult);
                    else if (delegateStage == BpmActTaskConfigDelegateStage.COMPLETE)
                        execResult = delegate.completeExecute(execResult);
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
     * 完成当前任务
     *
     * @param execResult 执行结果
     */
    private ExecResultDTO completeActInst(ExecResultDTO execResult) {
        BpmActivityInstanceState actInstState = execResult.getActInstState();

        actInstState.setFinishState(ActInstFinishState.FINISHED);
        actInstState.setEndDate(new Date());
        bpmActivityInstanceStateRepository.save(actInstState);


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

        Long actInstId = execResult.getActInst().getId();

        List<ExecResultDTO> nextTasks = execResult.getNextTasks();

        for (ExecResultDTO nextTask : nextTasks) {
            BpmActTaskAssignee actAssignee = todoTaskBaseService.getTaskAssigneesByTaskInfo(nextTask.getRuTask().getTaskDefKey(),
                nextTask.getRuTask().getName(), actInstId);
            if (actAssignee != null && actAssignee.getAssignee() != null) {



                if (!taskRuleCheckService.isCounterSignTaskNode(nextTask.getRuTask().getTaskDefKey(), nextTask.getRuTask().getTaskType())) {

                    todoTaskBaseService.assignee(actInstId, nextTask.getRuTask().getTaskDefKey(), nextTask.getRuTask().getName(),
                        actAssignee.getAssignee(), execResult.getActInst());
                }
            }
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

        if (ruTask != null) {
            reports = ruTask.getJsonReportsReadOnly();
        }

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

    @Override
    public String getSupportEventType() {
        return BPM_OPT_LOCK_ERROR.name();
    }


}
