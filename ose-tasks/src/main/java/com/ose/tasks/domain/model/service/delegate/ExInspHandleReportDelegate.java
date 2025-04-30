package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspScheduleInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspTaskInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.InspectResult;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.persistence.Tuple;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 外检申请 代理服务。
 */
@Component
public class ExInspHandleReportDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    /**
     * The class logger
     */
    private final static Logger logger = LoggerFactory.getLogger(ExInspHandleReportDelegate.class);

    private final QCReportRepository qcReportRepository;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final ExInspTaskInterface exInspTaskService;

    private final ExInspTaskBaseInterface exInspTaskBaseService;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final ExInspScheduleInterface exInspScheduleService;

    private final BpmRuTaskRepository ruTaskRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public ExInspHandleReportDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                      BpmRuTaskRepository ruTaskRepository,
                                      QCReportRepository qcReportRepository,
                                      BpmExInspScheduleRepository bpmExInspScheduleRepository,
                                      ExInspTaskInterface exInspTaskService,
                                      ExInspTaskBaseInterface exInspTaskBaseService,
                                      TodoTaskBaseInterface todoTaskBaseService,
                                      ExInspScheduleInterface exInspScheduleService,
                                      StringRedisTemplate stringRedisTemplate,
                                      BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                      ExInspActInstRelationRepository exInspActInstRelationRepository) {
            super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.qcReportRepository = qcReportRepository;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.exInspTaskService = exInspTaskService;
        this.exInspTaskBaseService = exInspTaskBaseService;
        this.todoTaskBaseService = todoTaskBaseService;
        this.exInspScheduleService = exInspScheduleService;
        this.ruTaskRepository = ruTaskRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
    }


    @Override

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPreExecute(ContextDTO contextDTO,
                                                                                 Map<String, Object> data,
                                                                                 P todoBatchTaskCriteriaDTO,
                                                                                 TodoBatchTaskDTO todoBatchTaskDTO) {


        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");

        ExInspReportHandleSearchDTO exInspReportHandleSearchDTO = (ExInspReportHandleSearchDTO) todoBatchTaskCriteriaDTO;

        List<ExInspReportHandleDTO> exInspReportHandleDTOS = exInspReportHandleSearchDTO.getExInspReportHandleDTOS();
        if (exInspReportHandleDTOS == null) {
            todoBatchTaskDTO.setBatchExecuteResult(false);
            return todoBatchTaskDTO;
        }



        Map<Long, BpmExInspSchedule> exInspScheduleMap = new HashMap<>();
        Map<Long, Set<Long>> procInstIdMap = new HashMap<>();
        Map<Long, Map<String, Object>> taskCommandMap = new HashMap<>();
        Map<String, Object> overallCommand = exInspReportHandleSearchDTO.getCommand();

        Map<Long, Map<String, Object>> overallCommandMap = new HashMap<>();

        Set<Long> taskIds = new HashSet<>();

        Set<QCReport> qcReports = new HashSet<>();

        for (ExInspReportHandleDTO exInspReportHandleDTO : exInspReportHandleDTOS) {
            Map<String, Object> command;

            Long exInspScheduleId = exInspReportHandleDTO.getScheduleId();
            if (LongUtils.isEmpty(exInspScheduleId)) continue;

            ActReportDTO report = exInspReportHandleDTO.getReport();
            if (report == null || report.getReportQrCode() == null) {
                logger.error("There is no report as per reportId ");
                continue;
            }

            BpmExInspSchedule exInspSchedule = exInspScheduleMap.get(exInspScheduleId);

            if (exInspSchedule == null) {
                exInspSchedule = bpmExInspScheduleRepository.findById(exInspScheduleId).orElse(null);
                exInspScheduleMap.put(exInspScheduleId, exInspSchedule);
            }
            if (exInspSchedule == null) continue;




            QCReport qcReport = qcReportRepository.findByQrcodeAndStatus(report.getReportQrCode(), EntityStatus.ACTIVE);

            if (qcReport == null || qcReport.getScheduleId() == null) {
                logger.error("There is no report as per reportQrCode " + report.getReportQrCode());
                continue;
            }

            Long scheduleId = qcReport.getScheduleId();

            List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.
                findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(orgId, projectId, scheduleId);

            Set<Long> tIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getTaskId() != null).
                map(TaskProcQLDTO::getTaskId).collect(Collectors.toSet());

            Set<Long> actInstIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getActInstId() != null).
                map(TaskProcQLDTO::getActInstId).collect(Collectors.toSet());

            procInstIdMap.put(scheduleId, actInstIds);


            ReportStatus reportStatus = ReportStatus.DONE;

            Object commandStr = null;
            String inspectResultStr = null;


            if (exInspSchedule.getSingleReport() == null || exInspSchedule.getSingleReport()) {
                if (MapUtils.isEmpty(overallCommand)) {
                    command = exInspReportHandleDTO.getCommand();
                    if (MapUtils.isNotEmpty(command)) {
                        commandStr = exInspReportHandleDTO.getCommand().values().iterator().next();
                        for (Long taskId : tIds) {
                            taskCommandMap.put(taskId, command);
                        }
                    }
                } else {
                    commandStr = overallCommand.values().iterator().next();
                }

                if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_NO_COMMENT.equals(commandStr) ||
                    BpmCode.EXCLUSIVE_GATEWAY_RESULT_ACCEPT.equals(commandStr)) {
                    reportStatus = ReportStatus.DONE;
                } else if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT.equals(commandStr)) {
                    reportStatus = ReportStatus.CANCEL;
                } else if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_COMMENT.equals(commandStr)) {
                    reportStatus = ReportStatus.COMMENTS;
                }
                overallCommandMap.put(exInspScheduleId, overallCommand);
            } else {

                if (MapUtils.isEmpty(overallCommand)) {
                    command = exInspReportHandleDTO.getCommand();
                    if (MapUtils.isNotEmpty(command)) {
                        commandStr = exInspReportHandleDTO.getCommand().values().iterator().next();
                        for (Long taskId : tIds) {
                            BpmRuTask ruTask = ruTaskRepository.findById(taskId).orElse(null);
                            if (ruTask == null) break;
                            inspectResultStr = todoTaskBaseService.getInspectResult((String) commandStr);
                            reportStatus = todoTaskBaseService.getReportStatus((String) commandStr);
                            if (ruTask.getInspectResult() == null || ruTask.getInspectResult().name().compareToIgnoreCase(inspectResultStr) < 0) {

                                ruTask.setJsonCurrentCommand(command);
                                ruTask.setInspectResult(InspectResult.valueOf(inspectResultStr));
                                ruTaskRepository.save(ruTask);
                            } else {

                                if (!CollectionUtils.isEmpty(ruTask.getJsonCurrentCommand())) {
                                    command = ruTask.getMapCurrentCommand();
                                }

                            }
                            taskCommandMap.put(taskId, command);
                        }
                    }
                } else {
                    commandStr = overallCommand.values().iterator().next();
                    if (commandStr != null && !StringUtils.isEmpty((String) commandStr)) {
                        inspectResultStr = todoTaskBaseService.getInspectResult((String) commandStr);
                        reportStatus = todoTaskBaseService.getReportStatus((String) commandStr);
                        if (exInspSchedule.getResultType() == null || exInspSchedule.getResultType().name().compareToIgnoreCase(inspectResultStr) < 0) {

                            exInspSchedule.setJsonCurrentCommand(overallCommand);
                            exInspSchedule.setResultType(InspectResult.valueOf(inspectResultStr));
                            overallCommandMap.put(exInspScheduleId, overallCommand);
                            bpmExInspScheduleRepository.save(exInspSchedule);
                        } else if (!CollectionUtils.isEmpty(exInspSchedule.getJsonCurrentCommand())) {

                            overallCommandMap.put(exInspScheduleId, exInspSchedule.getMapCurrentCommand());
                            overallCommand = exInspSchedule.getMapCurrentCommand();

                        }
                    }
                }

            }

            if(!ReportStatus.PENDING.equals(qcReport.getReportStatus()) || ReportStatus.CANCEL.equals(reportStatus)) {
                qcReport.setReportStatus(reportStatus);
            }
            qcReportRepository.save(qcReport);






            Set<String> statusSet = new HashSet<>();
            statusSet.add(ReportStatus.REHANDLE_DONE.name());
            statusSet.add(ReportStatus.SKIP_UPLOAD.name());
            statusSet.add(ReportStatus.REHANDLE_SKIP_UPLOAD.name());
            statusSet.add(ReportStatus.COMMENTS.name());
            statusSet.add(ReportStatus.PENDING.name());
            statusSet.add(ReportStatus.DONE.name());

            if(ReportStatus.CANCEL.equals(reportStatus)) {
                exInspSchedule.setState(reportStatus);
                taskIds.addAll(tIds);
            } else {
                Tuple tuple = qcReportRepository.findUploadedCount(qcReport.getScheduleId(), statusSet);



                if (tuple != null && tuple.get("totalCount") != null && ((BigDecimal) tuple.get("totalCount")).longValue() > 0L
                    && ((BigDecimal) tuple.get("unConfirmedCount")).longValue() == 0L) {

                    exInspSchedule.setState(reportStatus);
                    taskIds.addAll(tIds);
                }
            }
            qcReports.add(qcReport);
        }


        todoBatchTaskCriteriaDTO.setTaskIds(new ArrayList<>(taskIds));
        if (CollectionUtils.isEmpty(taskIds)) {
            qcReports.forEach(qcReport -> {
                qcReport.setReportBatchConfirmed(false);
                qcReportRepository.save(qcReport);
            });
            todoBatchTaskDTO.setBatchExecuteResult(false);
            return todoBatchTaskDTO;
        } else {
            qcReportRepository.saveAll(qcReports);
            Long exInspScheduleId = qcReports.iterator().next().getScheduleId();
            if(!LongUtils.isEmpty(exInspScheduleId)) {
                qcReportRepository.updateBatchConfirmed(exInspScheduleId, true);
            }
        }

        if (todoBatchTaskDTO.getMetaData() == null) {
            todoBatchTaskDTO.setMetaData(new HashMap<>());
        }

        exInspReportHandleSearchDTO.setTaskCommands(taskCommandMap);
        exInspReportHandleSearchDTO.setCommand(overallCommand);
        todoBatchTaskDTO.getMetaData().put("orgId", data.get("orgId"));
        todoBatchTaskDTO.getMetaData().put("projectId", data.get("projectId"));
        todoBatchTaskDTO.getMetaData().put("exInspScheduleMap", exInspScheduleMap);
        todoBatchTaskDTO.getMetaData().put("procInstIdMap", procInstIdMap);
        return todoBatchTaskDTO;

    }

    @Override

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPostExecute(ContextDTO contextDTO,
                                                                                  Map<String, Object> data,
                                                                                  P todoBatchTaskCriteriaDTO,
                                                                                  TodoBatchTaskDTO todoBatchTaskDTO) {


        Long projectId = (Long) todoBatchTaskDTO.getMetaData().get("projectId");
        Map<Long, BpmExInspSchedule> exInspScheduleMap = (Map) todoBatchTaskDTO.getMetaData().get("exInspScheduleMap");
        Map<Long, Set<Long>> procInstIdMap = (Map)todoBatchTaskDTO.getMetaData().get("procInstIdMap");


        ExInspReportHandleSearchDTO uploadDTO = (ExInspReportHandleSearchDTO) todoBatchTaskCriteriaDTO;

        String gateWayCommand = (String) uploadDTO.getCommand().values().iterator().next();


        for (Map.Entry<Long, BpmExInspSchedule> exInspScheduleEntry : exInspScheduleMap.entrySet()) {

            BpmExInspSchedule exInspSchedule = exInspScheduleEntry.getValue();
            Long exInspScheduleId = exInspScheduleEntry.getKey();

            List<QCReport> qcReports = qcReportRepository.findByScheduleIdAndReportStatusNot(exInspScheduleId, ReportStatus.CANCEL);
            List<ActReportDTO> actReports = exInspScheduleService.getActReports(qcReports);
            String jsonReports = StringUtils.toJSON(actReports);


            bpmExInspScheduleRepository.save(exInspSchedule);

            ruTaskRepository.updateTaskReportInfo(projectId.toString(), jsonReports, procInstIdMap.get(exInspScheduleId));

            Map<String, Object> command = todoBatchTaskCriteriaDTO.getCommand();

            List<BpmActivityInstanceState> actInstStates = bpmActivityInstanceStateRepository.findByProjectIdAndActInstIdIn(projectId, procInstIdMap.get(exInspScheduleId));
            if (command.containsKey(BpmCode.EXCLUSIVE_GATEWAY_RESULT)) {
                for (BpmActivityInstanceState actInstState : actInstStates) {
                    bpmActivityInstanceStateRepository.save(actInstState);
                }
            }




            if (exInspSchedule.getState() == ReportStatus.DONE || exInspSchedule.getState() == ReportStatus.REHANDLE_DONE) {
                exInspTaskService.moveReportsToEntityDoc(exInspSchedule, actReports,
                    exInspSchedule.getState(),
                    gateWayCommand,
                    contextDTO.getOperator());
            }

        }


        return todoBatchTaskDTO;

    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {
//        Long projectId = (Long) data.get("projectId");
//
//
//        String[] actTaskIds = (String[]) data.get("actTaskIds");


        Map<String, Object> command = (Map<String, Object>) data.get("command");

//        setFpy(projectId, execResult, command);

        if (command.containsKey(BpmCode.EXCLUSIVE_GATEWAY_RESULT)) {
            BpmActivityInstanceState actInstState = execResult.getActInstState();
        }

        return execResult;
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

        Long scheduleId = exInspSchedule.getId();
        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.
            findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(orgId, projectId, scheduleId);

        Set<Long> actInstIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getActInstId() != null).
            map(TaskProcQLDTO::getActInstId).collect(Collectors.toSet());

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
        BpmExInspSchedule exInspSchedule = (BpmExInspSchedule) actInstSuspendDTO.getMetaData().get("exInspSchedule");
        bpmExInspScheduleRepository.save(exInspSchedule);

        return new RevocationDTO();

    }


}
