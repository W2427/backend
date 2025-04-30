package com.ose.tasks.domain.model.service.delegate;

import com.ose.vo.*;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.entity.UserBasic;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.repository.qc.ExInspDetailActInstRelationRepository;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ReportConfigRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspTaskInterface;
import com.ose.tasks.domain.model.service.report.QCReportInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.vo.bpm.*;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 外检申请 代理服务。
 */
@Component
public class ExInspReHandleReportDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(ExInspReHandleReportDelegate.class);

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final QCReportInterface qcReportService;

    private final ExInspTaskBaseInterface exInspTaskBaseService;

    private final BpmProcessRepository processRepository;

    private final ExInspTaskInterface exInspTaskService;

    private final QCReportRepository qcReportRepository;

    private final ProjectInterface projectService;

    private final BpmRuTaskRepository bpmRuTaskRepository;

    private final UserFeignAPI userFeignAPI;

    private final BpmRuTaskRepository ruTaskRepository;

    private final ReportConfigRepository reportConfigRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final ExInspDetailActInstRelationRepository exInspDetailActInstRelationRepository;

    private final BpmActivityInstanceReportRepository bpmActivityInstanceReportRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;

    private final BpmHiTaskinstRepository bpmHiTaskinstRepository;




    /**
     * 构造方法。
     */
    @Autowired
    public ExInspReHandleReportDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                        BpmExInspScheduleRepository bpmExInspScheduleRepository,
                                        QCReportInterface qcReportService,
                                        ExInspTaskBaseInterface exInspTaskBaseService,
                                        BpmProcessRepository processRepository,
                                        BpmRuTaskRepository ruTaskRepository,
                                        ExInspTaskInterface exInspTaskService,
                                        QCReportRepository qcReportRepository,
                                        ProjectInterface projectService,
                                        BpmRuTaskRepository bpmRuTaskRepository,
                                        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
                                        ReportConfigRepository reportConfigRepository,
                                        StringRedisTemplate stringRedisTemplate,
                                        ExInspActInstRelationRepository exInspActInstRelationRepository,
                                        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                        ExInspDetailActInstRelationRepository exInspDetailActInstRelationRepository,
                                        BpmActivityInstanceReportRepository bpmActivityInstanceReportRepository,
                                        BpmHiTaskinstRepository bpmHiTaskinstRepository) {
            super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.qcReportService = qcReportService;
        this.exInspTaskBaseService = exInspTaskBaseService;
        this.processRepository = processRepository;
        this.exInspTaskService = exInspTaskService;
        this.qcReportRepository = qcReportRepository;
        this.projectService = projectService;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.userFeignAPI = userFeignAPI;
        this.ruTaskRepository = ruTaskRepository;
        this.reportConfigRepository = reportConfigRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.exInspDetailActInstRelationRepository = exInspDetailActInstRelationRepository;
        this.bpmActivityInstanceReportRepository = bpmActivityInstanceReportRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
        this.bpmHiTaskinstRepository = bpmHiTaskinstRepository;
    }


    @Override
    @SuppressWarnings("unchecked")

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPreExecute(ContextDTO contextDTO,
                                                                                 Map<String, Object> data,
                                                                                 P todoBatchTaskCriteriaDTO,
                                                                                 TodoBatchTaskDTO todoBatchTaskDTO) {


        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");

        ExInspReportHandleSearchDTO exInspReportHandleSearchDTO = (ExInspReportHandleSearchDTO) todoBatchTaskCriteriaDTO;



        String reportParty = "";
        Long exInspScheduleId = null;
        BpmExInspSchedule exInspSchedule = null;
        Set<Long> taskIds = new HashSet<>();
        Set<Long> okActTaskIds = new HashSet<>();
        Set<Long> okActInstIds = new HashSet<>();
        Set<String> okEntityNos = new HashSet<>();


        String process = null;
        Long processId = null;

        List<BpmActivityInstanceReport> actInstList = new ArrayList<>();

        List<BpmActivityInstanceReport> okActInstList = new ArrayList<>();

        Map<Long, Map<String, Object>> taskCommands = new HashMap<>();
        String inspectResultStr = null;
        BpmProcess bpmProcess = null;
        ProcessType processType = null;
        String reportType = null;
        String reportSubType = null;

        /*
            1. 设定 原来的 exinspActInstRelation 和 exinspDetailActInstRelation 中的 关联 scheduleId的记录变为 CANCEL
            2. OK一条 就在 exinspActInstRelation 表中添加 一条记录， exinspDetailActInstRelation中添加 多条记录（对应多方报检）
         */


        for (ExInspReportHandleDTO exInspReportHandleDTO : exInspReportHandleSearchDTO.getExInspReportHandleDTOS()) {
            Long taskId = exInspReportHandleDTO.getTaskId();
            Map<String, Object> command = exInspReportHandleDTO.getCommand();

            Long actInstId = exInspReportHandleDTO.getActInstId();

            BpmActivityInstanceReport actInst = bpmActivityInstanceReportRepository.findActInstByActInstId(projectId, actInstId);
            actInstList.add(actInst);
            process = actInst.getProcess();
            processId = actInst.getProcessId();
            if(bpmProcess == null) {
                bpmProcess = processRepository.findById(processId).orElse(null);
                reportType = exInspReportHandleDTO.getReportType();
                reportSubType = exInspReportHandleDTO.getReportSubType();
            }
            if(bpmProcess == null) continue;
            processType = bpmProcess.getProcessType();

            if(LongUtils.isEmpty(exInspScheduleId)) {
                exInspScheduleId = exInspReportHandleDTO.getScheduleId();
                if(exInspScheduleId == null) continue;
            }

            if (MapUtils.isNotEmpty(exInspReportHandleDTO.getCommand()) &&
                !BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT.equals(exInspReportHandleDTO.getCommand().values().iterator().next())) {
                okActTaskIds.add(exInspReportHandleDTO.getTaskId());

                if(exInspSchedule == null) exInspSchedule = bpmExInspScheduleRepository.findById(exInspScheduleId).orElse(null);
                if(exInspSchedule == null) continue;

                if((exInspSchedule.getSingleReport() != null) && !exInspSchedule.getSingleReport()
                    && processType != ProcessType.CUT_LIST && processType != ProcessType.DELIVERY_LIST) {
                    inspectResultStr = (String) exInspReportHandleDTO.getCommand().values().iterator().next();




                    if(BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_ACCEPT.getType().equalsIgnoreCase(inspectResultStr)) {
                        inspectResultStr = InspectResult.A.name();
                    } else if(BpmTaskDefKey.EXCLUSIVE_GATEWAY_RESULT_COMMENT.getType().equalsIgnoreCase(inspectResultStr)) {
                        inspectResultStr = InspectResult.B.name();
                    } else {
                        inspectResultStr = InspectResult.C.name();
                    }

                    BpmRuTask ruTask = bpmRuTaskRepository.findById(exInspReportHandleDTO.getTaskId()).orElse(null);
                    if(ruTask == null) continue;

                    if (ruTask.getInspectResult() == null || ruTask.getInspectResult().name().compareToIgnoreCase(inspectResultStr) < 0) {

                        ruTask.setJsonCurrentCommand(command);
                        ruTask.setInspectResult(InspectResult.valueOf(inspectResultStr));
                        ruTaskRepository.save(ruTask);
                    } else if (!CollectionUtils.isEmpty(ruTask.getJsonCurrentCommand())) {

                        command = ruTask.getMapCurrentCommand();
                    }
                }
                okActInstList.add(actInst);
                okActInstIds.add(actInst.getId());
                okEntityNos.add(actInst.getEntityNo());
            } else if(MapUtils.isNotEmpty(exInspReportHandleDTO.getCommand()) &&
                BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT.equals(exInspReportHandleDTO.getCommand().values().iterator().next())) {
                exInspActInstRelationRepository.deleteByActInstId(actInstId);
                exInspDetailActInstRelationRepository.deleteByActInstId(actInstId);

                if (processType.equals(ProcessType.CUT_LIST)) {

//                    bpmCuttingEntityRepository.updateCuttingEntityById(exInspReportHandleDTO.getEntityId());


                } else if (processType.equals(ProcessType.DELIVERY_LIST)) {

//                    bpmDeliveryEntityRepository.updateExecuteNgFlagFalse(exInspReportHandleDTO.getEntityId());


                }
            }

            taskIds.add(exInspReportHandleDTO.getTaskId());
            taskCommands.put(exInspReportHandleDTO.getTaskId(), command);

        }

        if (actInstList.isEmpty() || processId == null
            || StringUtils.isEmpty(process) || exInspScheduleId == null) {
            todoBatchTaskDTO.setBatchExecuteResult(false);
            return todoBatchTaskDTO;
        }


        List<BpmRuTask> ruTasks = bpmRuTaskRepository.findByIdIn(okActTaskIds.toArray(new Long[0]));
        List<Long> assignees = ruTasks.stream().map(ruTask -> LongUtils.parseLong(ruTask.getAssignee())).distinct().collect(Collectors.toList());
        BatchGetDTO batchGetDTO = new BatchGetDTO();
        batchGetDTO.setEntityIDs(new HashSet<>(assignees));
        List<String> assigneeNames = userFeignAPI.batchGet(batchGetDTO).getData().stream().map(UserBasic::getName).collect(Collectors.toList());

        exInspReportHandleSearchDTO.setTaskIds(new ArrayList<>(taskIds));

        if(exInspSchedule == null) {
            exInspSchedule = bpmExInspScheduleRepository.findById(exInspScheduleId).orElse(null);
        }

        if(exInspSchedule == null) {
            todoBatchTaskDTO.setBatchExecuteResult(false);
            todoBatchTaskDTO.setErrorDesc("NO EX SCHEDULE FOUND");
            return todoBatchTaskDTO;
        }
        OperatorDTO operator = contextDTO.getOperator();

        String moduleName = "";
        if(okActInstList.size() >0) {
            moduleName = okActInstList.get(0).getEntityModuleName();
        } else {
            moduleName = actInstList.get(0).getEntityModuleName();
        }
        String location = exInspSchedule.getLocation();
        Date exInspTime = exInspSchedule.getExternalInspectionTime();
        String inspectionContents = exInspSchedule.getName();

        Map<String, Boolean> reportPartyMap = new HashMap<>();



        if(reportType == null || reportSubType == null) {
            throw new BusinessError("Report Type or Sub Type is NULL");
        }

        List<ReportConfig> coverReportConfigs = new ArrayList<>();
        List<ReportConfig> reportConfigs = new ArrayList<>();

        List<ReportConfig> allReportConfigs = reportConfigRepository.findByOrgIdAndProjectIdAndProcessIdAndStatus(
            bpmProcess.getOrgId(),
            bpmProcess.getProjectId(),
            bpmProcess.getId(),
            EntityStatus.ACTIVE);

        for (ReportConfig reportConfig : allReportConfigs) {
            if(reportType.equalsIgnoreCase(reportConfig.getReportType().name()) &&
                reportSubType.equalsIgnoreCase(reportConfig.getReportSubType().name())) {
                if (reportConfig.getCover() != null && reportConfig.getCover()) {
                    coverReportConfigs.add(reportConfig);
                } else if (reportConfig.getCover() == null || !reportConfig.getCover()) {
                    reportConfigs.add(reportConfig);
                }
            }
        }

        List<String> entityNos = okActInstList.stream().map(BpmActivityInstanceReport::getEntityNo).collect(Collectors.toList());

        /*--
        2.0 设置原来的报告 为CANCEL
         */

        for (BpmActivityInstanceReport actInst : actInstList) {
            for (ReportConfig reportConfig : reportConfigs) {
                srem(String.format(RedisKey.REPORT_RUNNING_LIST.getDisplayName(), projectId.toString(), reportConfig.getReportSubType().toString()), actInst.getId().toString());
            }
        }

        Project project = projectService.get(orgId, projectId);
        List<QCReport> qcReports = new ArrayList<>();
        List<InspectParty> inspectParties = new ArrayList<>();
        if(exInspSchedule.getJsonExternalInspectionParties() != null) {
            inspectParties.addAll(exInspSchedule.getJsonExternalInspectionParties().stream().
                map(InspectParty::valueOf).collect(Collectors.toList()));
        }




        List<Tuple> oldReportIds = qcReportRepository.findOldReportIds(projectId, exInspScheduleId, reportType, reportSubType);
        Map<String, Long> oldReportIdMap = new HashMap<>();
        Map<String, Integer> oldReportNumMap = new HashMap<>();
        Set<Long> oldQcReportIds = new HashSet<>();
        oldReportIds.forEach(oldReportId -> {
            oldReportIdMap.put((String) oldReportId.get("_key"), ((BigInteger) oldReportId.get("reportId")).longValue());
            oldReportNumMap.put((String) oldReportId.get("_key"), (Integer) oldReportId.get("reportNum"));
            oldQcReportIds.add(((BigInteger) oldReportId.get("reportId")).longValue());
        });


        if(oldQcReportIds.size() > 0)
            qcReportRepository.updateReportStatusByIds(oldQcReportIds, ReportStatus.CANCEL);

        if(okActInstIds.size() >0) {
            for (ReportConfig reportConfig : coverReportConfigs) {
                QCReport qcReportCover = qcReportService.generateReportCover(orgId,
                    projectId,
                    bpmProcess,
                    assignees,
                    assigneeNames,
                    operator,
                    location,
                    new HashSet<>(entityNos),
                    new ArrayList<>(okActTaskIds),
                    okActInstIds,
                    okActInstList,
                    inspectionContents,
                    inspectParties,
                    project.getName(),
                    exInspTime,
                    reportConfig,
                    exInspScheduleId
                );
                if (qcReportCover != null) {
                    String reportSubTypeStr = qcReportCover.getReportSubType() == null ? "" : qcReportCover.getReportSubType().name();
                    qcReportCover.setParentReportId(
                        oldReportIdMap.get(qcReportCover.getReportType().name() + "_" + reportSubTypeStr)
                    );
                    qcReportCover.setParentReportId(oldReportIdMap.get(qcReportCover.getReportType().name() + "_" + reportSubTypeStr));
                    qcReports.add(qcReportCover);
                }

            }



            for (ReportConfig reportConfig : reportConfigs) {
                QCReport qcReport = qcReportService.generateReport(orgId,
                    projectId,
                    bpmProcess,
                    operator,
                    contextDTO,
                    location,
                    new ArrayList<>(okActTaskIds),
                    okActInstList,
                    inspectParties,
                    reportConfig,
                    project.getName(),
                    exInspTime,
                    assignees,
                    assigneeNames,
                    new HashSet<>(entityNos),
                    okActInstIds,
                    exInspScheduleId,
                    oldReportNumMap,
                    ReportStatus.REHANDLE_INIT
                );

                if (qcReport != null && qcReport.getReportGenerateError() == null) {
                    String reportSubTypeStr = qcReport.getReportSubType() == null ? "" : qcReport.getReportSubType().name();
                    qcReport.setParentReportId(
                        oldReportIdMap.get(qcReport.getReportType().name() + "_" + reportSubTypeStr)
                    );
                    qcReport.setParentReportId(oldReportIdMap.get(qcReport.getReportType().name() + "_" + reportSubTypeStr));
                    qcReports.add(qcReport);
                } else if(qcReport == null) {
                    throw new BusinessError("None Report Generated");
                } else {
                    throw new BusinessError(qcReport.getReportGenerateError());
                }

            }


        }





        Set<String> statusSet = new HashSet<>();
        statusSet.add(ReportStatus.COMMENTS.name());

        Tuple tuple = qcReportRepository.findRehandleCount(exInspScheduleId, statusSet);
        InspectResult inspectResult = null;


        if (tuple != null
            && (
            (tuple.get("totalCount") == null && (processType.equals(ProcessType.DELIVERY_LIST) || processType.equals(ProcessType.CUT_LIST))) ||
                (tuple.get("totalCount") != null && ((BigDecimal) tuple.get("totalCount")).longValue() > 0L &&
                    tuple.get("unConfirmedCount") != null && ((BigDecimal) tuple.get("unConfirmedCount")).longValue() > 0L )
        )
            ) {
            qcReports.forEach(qcReport -> {
                qcReport.setReportBatchConfirmed(false);
                qcReportRepository.save(qcReport);
            });

            todoBatchTaskDTO.setBatchExecuteResult(false);
            return todoBatchTaskDTO;

        } else {
            qcReportRepository.saveAll(qcReports);
            if(!LongUtils.isEmpty(exInspScheduleId)) {
                qcReportRepository.updateBatchConfirmed(exInspScheduleId, true);
            }
        }

        exInspReportHandleSearchDTO.setTaskCommands(taskCommands);
        if (todoBatchTaskDTO.getMetaData() == null) {
            todoBatchTaskDTO.setMetaData(new HashMap<>());
        }
        todoBatchTaskDTO.getMetaData().put("orgId", data.get("orgId"));
        todoBatchTaskDTO.getMetaData().put("projectId", data.get("projectId"));
        todoBatchTaskDTO.getMetaData().put("okActInstIds", okActInstIds);
        todoBatchTaskDTO.getMetaData().put("exInspScheduleId", exInspScheduleId);
        todoBatchTaskDTO.getMetaData().put("okActInstList", okActInstList);
        todoBatchTaskDTO.getMetaData().put("okActTaskIds", okActTaskIds);

        todoBatchTaskDTO.getMetaData().put("exInspSchedule", exInspSchedule);
        todoBatchTaskDTO.getMetaData().put("qcReports", qcReports);

        return todoBatchTaskDTO;

    }

    @Override
    @SuppressWarnings("unchecked")

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPostExecute(ContextDTO contextDTO,
                                                                                  Map<String, Object> data,
                                                                                  P todoBatchTaskCriteriaDTO,
                                                                                  TodoBatchTaskDTO todoBatchTaskDTO) {

        ExInspReportHandleSearchDTO exInspReportHandleSearchDTO = (ExInspReportHandleSearchDTO) todoBatchTaskCriteriaDTO;


        Long orgId = (Long) todoBatchTaskDTO.getMetaData().get("orgId");
        Long projectId = (Long) todoBatchTaskDTO.getMetaData().get("projectId");
        Long exInspScheduleId = (Long) todoBatchTaskDTO.getMetaData().get("exInspScheduleId");

        List<QCReport> qcReports = (List<QCReport>) todoBatchTaskDTO.getMetaData().get("qcReports");
        BpmExInspSchedule exInspSchedule = (BpmExInspSchedule) todoBatchTaskDTO.getMetaData().get("exInspSchedule");


        List<ActReportDTO> actReports = new ArrayList<>();
        ReportStatus reportStatus = ReportStatus.DONE;
        qcReports.forEach(report -> {
            ActReportDTO actReport = new ActReportDTO();
            actReport.setReportNo(report.getReportNo());
            actReport.setReportType(report.getReportType().name());
            actReport.setReportQrCode(report.getQrcode());
            actReport.setFileId(report.getPdfReportFileId());
            actReport.setSeriesNo(report.getSeriesNo());
            actReports.add(actReport);
            qcReportRepository.save(report);
        });

        if(!CollectionUtils.isEmpty(qcReports)) {
            reportStatus = qcReports.get(0).getReportStatus();
        }






        exInspSchedule.setState(reportStatus);

        bpmExInspScheduleRepository.save(exInspSchedule);


        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.
            findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(orgId, projectId, exInspScheduleId);
        Set<Long> actInstIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getActInstId() != null).
            map(TaskProcQLDTO::getActInstId).collect(Collectors.toSet());
        if(!CollectionUtils.isEmpty(actInstIds)) {
            ruTaskRepository.updateTaskReportInfo(projectId.toString(), StringUtils.toJSON(actReports), actInstIds);

            // 更新对应的外检结果信息
            Map<Long, Map<String, Object>> commands = todoBatchTaskCriteriaDTO.getTaskCommands();

            for(Map.Entry<Long , Map<String, Object>> command : commands.entrySet()){
//                BpmRuTask bpmRuTask = bpmRuTaskRepository.findByTaskId(command.getKey());
                BpmHiTaskinst bpmHiTaskinst = bpmHiTaskinstRepository.findByTaskId(command.getKey());
                if (bpmHiTaskinst != null) {
                    BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByProjectIdAndBaiId(projectId, bpmHiTaskinst.getActInstId());
                    if (actInstState != null && command.getValue().containsKey(BpmCode.EXCLUSIVE_GATEWAY_RESULT)) {

                    }
                    bpmActivityInstanceStateRepository.save(actInstState);
                }
            }
        }

        exInspTaskService.releaseDeliveryEntities(orgId, projectId, exInspReportHandleSearchDTO);

        return todoBatchTaskDTO;
    }



    @Override
    public RevocationDTO batchRevocationPreExecute(ContextDTO contextDTO,
                                                   Long orgId,
                                                   Long projectId,
                                                   ActInstSuspendDTO actInstSuspendDTO) {

        BpmActivityInstanceBase actInst = actInstSuspendDTO.getActInst();
        if (actInst == null) {
            return new RevocationDTO();
        }

        BpmExInspSchedule exInspSchedule = exInspTaskBaseService.getExInspScheduleByActInst(orgId, projectId, actInst);


        if (exInspSchedule == null) {
            return new RevocationDTO();
        }
        Long exInspScheduleId = exInspSchedule.getId();
        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.
            findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(orgId, projectId, exInspScheduleId);
        List<Long> taskIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getTaskId() != null).
            map(TaskProcQLDTO::getTaskId).collect(Collectors.toList());
        actInstSuspendDTO.setTaskIds(new HashSet<>(taskIds));

        Map<String, Object> metaData = new HashMap<>();
        metaData.put("exInspSchedule", exInspSchedule);
        actInstSuspendDTO.setMetaData(metaData);

        return new RevocationDTO();

    }



    @Override
    public RevocationDTO batchRevocationPostExecute(ContextDTO contextDTO,
                                                    Long orgId,
                                                    Long projectId,
                                                    ActInstSuspendDTO actInstSuspendDTO) {
        Map<String, Object> metaData = actInstSuspendDTO.getMetaData();
        if (metaData == null) {
            return new RevocationDTO();
        }
        BpmExInspSchedule exInspSchedule = (BpmExInspSchedule) metaData.get("exInspSchedule");



        return new RevocationDTO();

    }

    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {

//        Long projectId = (Long) data.get("projectId");
//
//        String[] actTaskIds = (String[]) data.get("actTaskIds");


        Map<String, Object> command = (Map<String, Object>) data.get("command");

//        setFpy(projectId, execResult, command);

        if (command.containsKey(BpmCode.EXCLUSIVE_GATEWAY_RESULT)) {
            BpmActivityInstanceState actInstState = execResult.getActInstState();
            if (actInstState != null) {

            }
        }

        return execResult;
    }

}
