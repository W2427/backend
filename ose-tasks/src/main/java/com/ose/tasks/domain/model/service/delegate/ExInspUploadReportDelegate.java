package com.ose.tasks.domain.model.service.delegate;

import com.ose.dto.ContextDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspScheduleInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspTaskInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.BpmExInspConfirm;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.PdfUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
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
public class ExInspUploadReportDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    private final BpmExInspConfirmRepository bpmExInspConfirmRepository;

    private final BpmExInspUploadHistoryRepository bpmExInspUploadHistoryRepository;

    private final QCReportRepository qcReportRepository;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final ExInspTaskInterface exInspTaskService;

    private final ExInspScheduleInterface exInspScheduleService;

    private final BpmRuTaskRepository ruTaskRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final static Logger logger = LoggerFactory.getLogger(ExInspUploadReportDelegate.class);

    /**
     * 构造方法。
     */
    @Autowired
    public ExInspUploadReportDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                                      BpmRuTaskRepository ruTaskRepository,
                                      BpmExInspConfirmRepository bpmExInspConfirmRepository,
                                      BpmExInspUploadHistoryRepository bpmExInspUploadHistoryRepository,
                                      QCReportRepository qcReportRepository,
                                      BpmExInspScheduleRepository bpmExInspScheduleRepository,
                                      ExInspTaskInterface exInspTaskService,
                                      StringRedisTemplate stringRedisTemplate,
                                      ExInspScheduleInterface exInspScheduleService,
                                      BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                                      ExInspActInstRelationRepository exInspActInstRelationRepository) {
            super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.bpmExInspConfirmRepository = bpmExInspConfirmRepository;
        this.bpmExInspUploadHistoryRepository = bpmExInspUploadHistoryRepository;
        this.qcReportRepository = qcReportRepository;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.exInspTaskService = exInspTaskService;
        this.exInspScheduleService = exInspScheduleService;
        this.ruTaskRepository = ruTaskRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
    }


    @Override
    @SuppressWarnings("unchecked")

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPreExecute(ContextDTO contextDTO,
                                                                                 Map<String, Object> data,
                                                                                 P todoBatchTaskCriteriaDTO,
                                                                                 TodoBatchTaskDTO todoBatchTaskDTO) {

        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");

        BpmExInspConfirmsDTO exInspConfirmDTO = (BpmExInspConfirmsDTO) todoBatchTaskCriteriaDTO;

        Set<Long> taskIds = new HashSet<>();

        List<Long> exInspConfirmIds = exInspConfirmDTO.getConfirmIdList();
        if (exInspConfirmIds == null) {
            todoBatchTaskDTO.setBatchExecuteResult(false);
            return todoBatchTaskDTO;
        }



        Map<Long, BpmExInspSchedule> exInspScheduleMap = new HashMap<>();



        Set<Long> exInspUploadHistoryIds = new HashSet<>();









        Set<QCReport> qcReports = new HashSet<>();

        for (Long exInspConfirmId : exInspConfirmIds) {

            List<EntityStatus> status = new ArrayList<>();
            status.add(EntityStatus.ACTIVE);
            status.add(EntityStatus.SKIPPED);

            BpmExInspConfirm exInspConfirm = bpmExInspConfirmRepository
                .findByIdAndStatusIn(exInspConfirmId,status)
                .orElse(null);
            if (exInspConfirm == null) {
                continue;
            }


            exInspUploadHistoryIds.add(exInspConfirm.getUploadHistoryId());

            exInspConfirm.setStatus(exInspConfirmDTO.getStatus());
            exInspConfirm.setLastModifiedAt();
            exInspConfirm.setOperator(contextDTO.getOperator().getId());
            bpmExInspConfirmRepository.save(exInspConfirm);

            bpmExInspConfirmRepository.updateRestStatus(orgId, projectId, exInspConfirm.getQrcode(), EntityStatus.DISABLED);

            bpmExInspUploadHistoryRepository.
                findUnConfirmedUpoadHistory(orgId, projectId, exInspConfirm.getQrcode(), EntityStatus.ACTIVE.name()).
                forEach(tuple -> {
                    bpmExInspUploadHistoryRepository.updateStatusById(orgId, projectId, ((BigInteger) tuple.get("historyId")).longValue(), true);
                });



            Tuple tuple = bpmExInspConfirmRepository.
                findCounts(orgId, projectId, exInspConfirm.getUploadHistoryId());
            if (tuple != null && ((BigDecimal) tuple.get("totalCount")).longValue() > 0L
                && ((BigDecimal) tuple.get("unConfirmedCount")).longValue() == 0L) {
                bpmExInspUploadHistoryRepository.
                    updateStatusById(orgId, projectId, exInspConfirm.getUploadHistoryId(), true);
            }



            if ((!exInspConfirm.getSecondUpload() && EntityStatus.REJECTED != exInspConfirmDTO.getStatus()) ||
                exInspConfirmDTO.isSkipUploadFlag()) {
                QCReport qcReport = qcReportRepository.
                    findByProjectIdAndQrcodeAndStatus(projectId, exInspConfirm.getQrcode(), EntityStatus.ACTIVE);
                if (qcReport == null || qcReport.getScheduleId() == null) continue;
                Long scheduleId = qcReport.getScheduleId();
                List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.
                    findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(orgId, projectId, scheduleId);

                Set<Long> tIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getTaskId() != null).
                    map(TaskProcQLDTO::getTaskId).collect(Collectors.toSet());

                BpmExInspSchedule exInspSchedule = exInspScheduleMap.get(qcReport.getScheduleId());

                if (exInspSchedule == null && qcReport.getScheduleId() != null) {
                    exInspSchedule = bpmExInspScheduleRepository.findById(qcReport.getScheduleId()).orElse(null);
                    exInspScheduleMap.put(qcReport.getScheduleId(), exInspSchedule);
                }
                if (exInspSchedule == null) continue;

                if (exInspConfirmDTO.isSkipUploadFlag()) {








                    qcReport.setLastModifiedAt(new Date());
                    qcReport.setReportStatus(ReportStatus.PENDING);
                } else {
                    if (qcReport.getOneOffReport() != null && qcReport.getOneOffReport()) {
                        qcReport.setReportStatus(ReportStatus.DONE);


                    } else if (qcReport.getReportStatus() != null && qcReport.getReportStatus() == ReportStatus.REHANDLE_INIT) {
                        qcReport.setReportStatus(ReportStatus.DONE);

                    } else {
                        qcReport.setReportStatus(ReportStatus.UPLOADED);

                    }
                }


                if (!exInspConfirmDTO.isSkipUploadFlag()) {
                    qcReport.setUploadFileId(exInspConfirm.getJsonReportReadOnly().getFileId());
                    qcReport.setUploadFilePath(exInspConfirm.getJsonReportReadOnly().getFilePath());
                    qcReport.setUploadReportPageCount(PdfUtils.getPdfPageCount(protectedDir + exInspConfirm.getJsonReportReadOnly().getFilePath().substring(1)));
                }
                qcReport.setUploadFileId(exInspConfirm.getJsonReportReadOnly().getFileId());
                qcReport.setUploadFilePath(exInspConfirm.getJsonReportReadOnly().getFilePath());
                qcReportRepository.save(qcReport);



                Set<String> statusSet = new HashSet<>();
                statusSet.add(ReportStatus.UPLOADED.name());
                statusSet.add(ReportStatus.REHANDLE_UPLOADED.name());
                statusSet.add(ReportStatus.SKIP_UPLOAD.name());
                statusSet.add(ReportStatus.REHANDLE_SKIP_UPLOAD.name());
                statusSet.add(ReportStatus.DONE.name());

                statusSet.add(ReportStatus.PENDING.name());
                tuple = qcReportRepository.findUploadedCount(qcReport.getScheduleId(), statusSet);

                if (tuple != null && tuple.get("totalCount") != null &&
                    ((BigDecimal) tuple.get("totalCount")).longValue() > 0L
                    && ((BigDecimal) tuple.get("unConfirmedCount")).longValue() == 0L) {
                    taskIds.addAll(tIds);

                    if (exInspSchedule.getState() == ReportStatus.REHANDLE_INIT) {
                        exInspSchedule.setState(ReportStatus.DONE);
                    } else if(qcReport.getOneOffReport() != null && qcReport.getOneOffReport()){
                        exInspSchedule.setState(ReportStatus.DONE);
                    } else {

                        exInspSchedule.setState(ReportStatus.UPLOADED);
                    }
                }
                qcReports.add(qcReport);
            }

        }

        logger.info("qc报告:" + qcReports +
                    "是否有taskId:" + taskIds);
        if (CollectionUtils.isEmpty(taskIds)) {
            qcReports.forEach(qcReport -> {
                qcReport.setReportBatchConfirmed(false);
                qcReportRepository.save(qcReport);
            });
            todoBatchTaskDTO.setBatchExecuteResult(false);
            return todoBatchTaskDTO;
        } else if(qcReports.size()>0) {
            qcReportRepository.saveAll(qcReports);
            Long exInspScheduleId = qcReports.iterator().next().getScheduleId();
            logger.info("是否有scheduleId:" + exInspScheduleId);
            if(!LongUtils.isEmpty(exInspScheduleId)) {
                qcReportRepository.updateBatchConfirmed(exInspScheduleId, true);
            }
        }

        todoBatchTaskCriteriaDTO.setTaskIds(new ArrayList<>(taskIds));
        if (todoBatchTaskDTO.getMetaData() == null) {
            todoBatchTaskDTO.setMetaData(new HashMap<>());
        }
        todoBatchTaskDTO.getMetaData().put("orgId", data.get("orgId"));
        todoBatchTaskDTO.getMetaData().put("projectId", data.get("projectId"));
        todoBatchTaskDTO.getMetaData().put("taskIds", taskIds);
        todoBatchTaskDTO.getMetaData().put("exInspScheduleMap", exInspScheduleMap);

        return todoBatchTaskDTO;

    }

    @Override
    @SuppressWarnings("unchecked")

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPostExecute(ContextDTO contextDTO,
                                                                                  Map<String, Object> data,
                                                                                  P todoBatchTaskCriteriaDTO,
                                                                                  TodoBatchTaskDTO todoBatchTaskDTO) {

        Long projectId = (Long) todoBatchTaskDTO.getMetaData().get("projectId");
        Long orgId = (Long) todoBatchTaskDTO.getMetaData().get("orgId");


        Map<Long, BpmExInspSchedule> exInspScheduleMap = (HashMap<Long, BpmExInspSchedule>) todoBatchTaskDTO.getMetaData().get("exInspScheduleMap");


        Map<String, Object> command = todoBatchTaskCriteriaDTO.getCommand();
        String gateWayCommand = null;
        if (command != null) {
            gateWayCommand = (String) command.values().iterator().next();
        }

        for (Map.Entry<Long, BpmExInspSchedule> exInspScheduleEntry : exInspScheduleMap.entrySet()) {
            BpmExInspSchedule exInspSchedule = exInspScheduleEntry.getValue();
            Long scheduleId = exInspScheduleEntry.getKey();

            if (exInspSchedule == null) continue;
            List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.
                findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(orgId, projectId, scheduleId);

            Set<Long> actInstIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getActInstId() != null).
                map(TaskProcQLDTO::getActInstId).collect(Collectors.toSet());


            List<QCReport> qcReports = qcReportRepository.findByScheduleIdAndReportStatusNot(scheduleId, ReportStatus.CANCEL);
            List<ActReportDTO> actReports = exInspScheduleService.getActReports(qcReports);
            String jsonReports = StringUtils.toJSON(actReports);

            bpmExInspScheduleRepository.save(exInspSchedule);

            if(!CollectionUtils.isEmpty(actInstIds)) {
                ruTaskRepository.updateTaskReportInfo(projectId.toString(), jsonReports, actInstIds);
            }

            if (exInspSchedule.getState() == ReportStatus.DONE || exInspSchedule.getState() == ReportStatus.REHANDLE_DONE) {
                exInspTaskService.moveReportsToEntityDoc(exInspSchedule, actReports,
                    exInspSchedule.getState(),
                    gateWayCommand,
                    contextDTO.getOperator());
            }

        }

        Map<String, Object> metaData = todoBatchTaskDTO.getMetaData();
        BpmExInspConfirmResponseDTO responseDTO = new BpmExInspConfirmResponseDTO();
        responseDTO.setSuccessful(true);
        metaData.put("responseDTO", responseDTO);

        todoBatchTaskDTO.setMetaData(metaData);

        return todoBatchTaskDTO;

    }


    @Override
    public ExecResultDTO postExecute(ContextDTO contextDTO, Map<String, Object> data, ExecResultDTO execResult) {


        Long[] taskIds = (Long[]) data.get("taskIds");
        Long projectId = execResult.getProjectId();

        Map<String, Object> command = (Map<String, Object>) data.get("command");

        setFpy(projectId, taskIds, command);

        return execResult;
    }


}
