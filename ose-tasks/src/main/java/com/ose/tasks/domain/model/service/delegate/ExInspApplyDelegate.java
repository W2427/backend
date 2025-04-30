package com.ose.tasks.domain.model.service.delegate;

import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.dto.BatchGetDTO;
import com.ose.auth.entity.UserBasic;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.repository.qc.ReportConfigRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspApplyInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspScheduleInterface;
import com.ose.tasks.domain.model.service.report.QCReportInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.qc.ReportSubTypeDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.report.ExInspActInstRelation;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.vo.bpm.ExInspApplyStatus;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.InspectParty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 外检申请 代理服务。
 */
@Component
public class ExInspApplyDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    private final ReportConfigRepository reportConfigRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final ExInspApplyInterface exInspApplyService;

    private final ExInspScheduleInterface exInspScheduleService;

    private final BpmProcessRepository processRepository;

    private final QCReportInterface qcReportService;

    private final BpmActivityInstanceReportRepository bpmActivityInstanceReportRepository;

    private final UserFeignAPI userFeignAPI;

    private final ProjectInterface projectService;

    private final QCReportRepository qcReportRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final static Logger logger = LoggerFactory.getLogger(ExInspApplyDelegate.class);

    /**
     * 构造方法。
     */
    @Autowired
    public ExInspApplyDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                               ExInspApplyInterface exInspApplyService,
                               BpmRuTaskRepository ruTaskRepository,
                               ExInspScheduleInterface exInspScheduleService,
                               BpmProcessRepository processRepository,
                               QCReportInterface qcReportService,
                               BpmActivityInstanceReportRepository bpmActivityInstanceReportRepository,
                               @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
                               ProjectInterface projectService, QCReportRepository qcReportRepository,
                               StringRedisTemplate stringRedisTemplate,
                               ReportConfigRepository reportConfigRepository,
                               BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                               ExInspActInstRelationRepository exInspActInstRelationRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.ruTaskRepository = ruTaskRepository;
        this.exInspApplyService = exInspApplyService;
        this.exInspScheduleService = exInspScheduleService;
        this.processRepository = processRepository;
        this.qcReportService = qcReportService;
        this.bpmActivityInstanceReportRepository = bpmActivityInstanceReportRepository;
        this.userFeignAPI = userFeignAPI;
        this.projectService = projectService;
        this.qcReportRepository = qcReportRepository;
        this.reportConfigRepository = reportConfigRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
    }


    @Override
    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPrepareExecute(ContextDTO contextDTO,
                                                                                     Map<String, Object> data,
                                                                                     P todoBatchTaskCriteriaDTO,
                                                                                     TodoBatchTaskDTO todoBatchTaskDTO) {

        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");

        ExInspApplyCriteriaDTO criteriaDTO = (ExInspApplyCriteriaDTO) todoBatchTaskCriteriaDTO;

        List<Long> assignees = criteriaDTO.getAssignees();

        todoBatchTaskDTO.setMainList(exInspApplyService.getExternalInspectionApplyList(orgId, projectId, assignees, criteriaDTO));

        return todoBatchTaskDTO;
    }


    @Override

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPreExecute(ContextDTO contextDTO,
                                                                                 Map<String, Object> data,
                                                                                 P todoBatchTaskCriteriaDTO,
                                                                                 TodoBatchTaskDTO todoBatchTaskDTO) {


        ExInspApplyDTO exInspApplyDTO = (ExInspApplyDTO) todoBatchTaskCriteriaDTO;
        Set<Long> taskIds = new HashSet<>();
        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");


        Map<Long, Long> procInstIdPnIdMap = new HashMap<>();

        Map<String, Set<String>> inspectPartyEntityNosMap = new HashMap<>();


        Set<Long> actInstIds = new HashSet<>();
        Set<String> entityNos = new HashSet<>();
        Set<InspectParty> inspectParties = new HashSet<>();
        BpmProcess bpmProcess = null;
        Boolean isSingleReport = true;
        int qcReportCnt = 0;
        List<BpmActivityInstanceReport> actInstList = new ArrayList<>();
        List<Long> assignees = exInspApplyDTO.getAssignees();
        List<String> assigneeNames = new ArrayList<>();
        if (assignees != null) {
            BatchGetDTO batchGetDTO = new BatchGetDTO();

            batchGetDTO.setEntityIDs(new HashSet<>(assignees));
            assigneeNames = userFeignAPI.batchGet(batchGetDTO).getData().stream().map(UserBasic::getName).collect(Collectors.toList());
        }

        if (exInspApplyDTO.getExternalInspectionApplyList().isEmpty()) {

            return todoBatchTaskDTO;
        }

        Set<BpmExInspApply> exInspApplySet = new HashSet<>(exInspApplyDTO.getExternalInspectionApplyList());

        for (BpmExInspApply exInspApply : exInspApplySet) {

            if (LongUtils.isEmpty(exInspApply.getActInstId())) {
                continue;
            }

            List<InspectParty> curentInspectParties = exInspApply.getJsonInspectParties();

            if (CollectionUtils.isEmpty(curentInspectParties)) {

                exInspApplyDTO.getExternalInspectionApplyList().remove(exInspApply);
                continue;
            }


            Long taskId = exInspApply.getTaskId();
            taskIds.add(taskId);
            Long actInstId = exInspApply.getActInstId();


            if (!actInstIds.contains(actInstId)) {
                BpmActivityInstanceReport actInst = bpmActivityInstanceReportRepository.findActInstByActInstId(projectId, actInstId);
                if (actInst != null) {

                    String entityNo = actInst.getEntityNo();
                    procInstIdPnIdMap.put(actInstId, actInst.getEntityProjectNodeId());
                    entityNos.add(entityNo);
                    actInstIds.add(actInstId);

                    if (bpmProcess == null) {
                        bpmProcess = processRepository.findById(actInst.getProcessId()).orElse(new BpmProcess());
                    }

                    curentInspectParties.forEach(inspectParty -> {
                        inspectPartyEntityNosMap.computeIfAbsent(inspectParty.name(), k -> new HashSet<>()).add(entityNo);

                    });
                    actInstList.add(actInst);
                }
            }

            inspectParties.addAll(curentInspectParties);

        }


        if (exInspApplyDTO.getExternalInspectionApplyList().isEmpty() || actInstIds.isEmpty()) {

            return todoBatchTaskDTO;
        }



        if (CollectionUtils.isEmpty(taskIds)) {
            todoBatchTaskDTO.setBatchExecuteResult(false);
            return todoBatchTaskDTO;
        }



        List<ReportSubTypeDTO> subReportInfos = actInstList.get(0).getJsonReportSubTypeInfo();
        List<ReportConfig> coverReportConfigs = new ArrayList<>();
        List<ReportConfig> reportConfigs = new ArrayList<>();
        if (subReportInfos.size() != 0) {

            Map<String, String> subReportInfoMap = new HashMap<>();
            for (ReportSubTypeDTO subReportInfo : subReportInfos) {
                subReportInfoMap.put(subReportInfo.getReportType(), subReportInfo.getSubReportType());
            }
            Map<String, List<ReportConfig>> reportConfigMap = qcReportService.getReportConfig(bpmProcess, subReportInfoMap);
            coverReportConfigs = reportConfigMap.get("coverReportConfigs");
            reportConfigs = reportConfigMap.get("reportConfigs");
        } else {
            List<ReportConfig> allReportConfigs = reportConfigRepository.findByOrgIdAndProjectIdAndProcessIdAndStatus(
                bpmProcess.getOrgId(),
                bpmProcess.getProjectId(),
                bpmProcess.getId(),
                EntityStatus.ACTIVE);
            String reportSubType = "";
            if (bpmProcess.getNameEn().equals("MATERIAL_RECEIVE")) {
//                List<ReleaseNoteItemEntity> releaseNoteItemEntity = releaseNoteItemRepository.findByOrgIdAndProjectIdAndRelnId(orgId, projectId, actInstList.get(0).getEntityId());
//                if (releaseNoteItemEntity.size() > 0) {
//                    reportSubType = releaseNoteItemEntity.get(0).getReportSubType().toString();
//                }
            }

            for (ReportConfig reportConfig : allReportConfigs) {
                if (bpmProcess.getNameEn().equals("MATERIAL_RECEIVE")) {
                    if (reportSubType.equals(reportConfig.getReportSubType().toString())) {
                        reportConfigs.add(reportConfig);
                        break;
                    } else {
                        continue;
                    }
                } else {
                    reportConfigs.add(reportConfig);
                }

            }
        }
        OperatorDTO operator = contextDTO.getOperator();


        /*--
        2.0 创建外检申请记录
         */




        Project project = projectService.get(orgId, projectId);
        List<QCReport> qcReports = new ArrayList<>();

        for (ReportConfig reportConfig : coverReportConfigs) {
            QCReport qcReportCover = qcReportService.generateReportCover(orgId,
                projectId,
                bpmProcess,
                assignees,
                assigneeNames,
                operator,
                exInspApplyDTO.getLocation(),
                entityNos,
                new ArrayList<>(taskIds),
                actInstIds,
                actInstList,
                exInspApplyDTO.getName(),
                new ArrayList<>(inspectParties),
                project.getName(),
                exInspApplyDTO.getExternalInspectionTime(),
                reportConfig,
                null
            );
            if (qcReportCover != null)
                qcReports.add(qcReportCover);

        }




        for (ReportConfig reportConfig : reportConfigs) {
            QCReport qcReport = qcReportService.generateReport(orgId,
                projectId,
                bpmProcess,
                operator,
                contextDTO,
                exInspApplyDTO.getLocation(),
                new ArrayList<>(taskIds),
                actInstList,
                new ArrayList<>(inspectParties),
                reportConfig,
                project.getName(),
                exInspApplyDTO.getExternalInspectionTime(),
                assignees,
                assigneeNames,
                entityNos,
                actInstIds,
                null
            );
            if(qcReport == null) {
                throw new BusinessError("Report can not be generated");
            } else if (qcReport != null && qcReport.getReportGenerateError() != null) {
                throw new BusinessError(qcReport.getReportGenerateError());
            }

            qcReports.add(qcReport);
            qcReportCnt++;

        }
        if (qcReportCnt > 1) isSingleReport = false;

        exInspApplyDTO.setTaskIds(new ArrayList<>(taskIds));
        if (todoBatchTaskDTO.getMetaData() == null) {
            todoBatchTaskDTO.setMetaData(new HashMap<>(3));
        }
        todoBatchTaskDTO.getMetaData().put("actInstIds", actInstIds);
        todoBatchTaskDTO.getMetaData().put("bpmProcess", bpmProcess);
        todoBatchTaskDTO.getMetaData().put("entityNos", entityNos);
        todoBatchTaskDTO.getMetaData().put("procInstIdPnIdMap", procInstIdPnIdMap);
        todoBatchTaskDTO.getMetaData().put("inspectParties", inspectParties);

        todoBatchTaskDTO.getMetaData().put("qcReports", qcReports);

        todoBatchTaskDTO.getMetaData().put("orgId", data.get("orgId"));
        todoBatchTaskDTO.getMetaData().put("projectId", data.get("projectId"));
        todoBatchTaskDTO.getMetaData().put("isSingleReport", isSingleReport);

        return todoBatchTaskDTO;

    }

    /**
     * 批处理 后执行任务接口
     */
    @Override
    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPostExecute(ContextDTO contextDTO,
                                                                                  Map<String, Object> data,
                                                                                  P todoBatchTaskCriteriaDTO,
                                                                                  TodoBatchTaskDTO todoBatchTaskDTO) {

        ExInspApplyDTO exInspApplyDTO = (ExInspApplyDTO) todoBatchTaskCriteriaDTO;
        Long orgId = (Long) todoBatchTaskDTO.getMetaData().get("orgId");
        Long projectId = (Long) todoBatchTaskDTO.getMetaData().get("projectId");

        Map<String, Long> procInstIdPnIdMap = (Map) todoBatchTaskDTO.getMetaData().get("procInstIdPnIdMap");
        Set<Long> actInstIds = (Set) todoBatchTaskDTO.getMetaData().get("actInstIds");
        Set<String> entityNos = (Set) todoBatchTaskDTO.getMetaData().get("entityNos");
        BpmProcess bpmProcess = (BpmProcess) todoBatchTaskDTO.getMetaData().get("bpmProcess");
        Set<InspectParty> inspectParties = (Set) todoBatchTaskDTO.getMetaData().get("inspectParties");
        List<QCReport> qcReports = (List) todoBatchTaskDTO.getMetaData().get("qcReports");
        Boolean isSingleReport = (Boolean) todoBatchTaskDTO.getMetaData().get("isSingleReport");

        todoBatchTaskDTO.setMetaData(null);

        OperatorDTO operator = contextDTO.getOperator();

        List<Long> taskIds = exInspApplyDTO.getTaskIds();


        if (bpmProcess == null) {
            todoBatchTaskDTO.setBatchExecuteResult(false);
            return todoBatchTaskDTO;
        }


        List<ActReportDTO> actReports = new ArrayList<>();
        qcReports.forEach(report -> {
            if(report == null) return;
            ActReportDTO actReport = new ActReportDTO();
            actReport.setReportNo(report.getReportNo());
            actReport.setReportType(report.getReportType().name());
            actReport.setReportQrCode(report.getQrcode());
            actReport.setFileId(report.getPdfReportFileId());
            actReport.setSeriesNo(report.getSeriesNo());
            actReports.add(actReport);
        });

        ExInspScheduleDTO exInspScheduleDTO = new ExInspScheduleDTO();
        exInspScheduleDTO.setInspectParties(new ArrayList<>(inspectParties));

        exInspScheduleDTO.setCoordinateCategory(exInspApplyDTO.getCoordinateCategory());
        exInspScheduleDTO.setComment(exInspApplyDTO.getComment());
        exInspScheduleDTO.setLocation(exInspApplyDTO.getLocation());
        exInspScheduleDTO.setName(exInspApplyDTO.getName());
        exInspScheduleDTO.setDiscipline(exInspApplyDTO.getDiscipline());
        exInspScheduleDTO.setTemporaryFileNames(exInspApplyDTO.getTemporaryFileNames());
        exInspScheduleDTO.setExternalInspectionTime(exInspApplyDTO.getExternalInspectionTime());
        exInspScheduleDTO.setEntityNos(new ArrayList<>(entityNos));

        Boolean isSendEmail = true;
        if (bpmProcess.getNameEn().equals("PMI") || bpmProcess.getNameEn().equals("PWHT")) {
            isSendEmail = false;
        }
        logger.info("ExInspApplyDelegate->" + "创建 exInspSchedule ->" + new Date());
        BpmExInspSchedule exInspSchedule =
            exInspScheduleService.
                createExternalInspectionSchedule(orgId, projectId, exInspScheduleDTO, contextDTO.getOperator(), isSingleReport, isSendEmail);

        logger.info("ExInspApplyDelegate->" + "创建 exInspSchedule 结束 " + exInspSchedule.getId() + "->" + new Date());

        Long scheduleId = exInspSchedule.getId();
        qcReports.forEach(qcReport -> {
            actInstIds.forEach(actInstId -> {
                ExInspActInstRelation exInspActInstRelation = new ExInspActInstRelation();
                exInspActInstRelation.setActInstId(actInstId);
                exInspActInstRelation.setStatus(EntityStatus.ACTIVE);
                exInspActInstRelation.setCreatedAt();
                exInspActInstRelation.setLastModifiedAt();
                exInspActInstRelation.setProjectNodeId(procInstIdPnIdMap.get(actInstId));
                exInspActInstRelation.setProjectId(projectId);
                exInspActInstRelation.setOrgId(orgId);
                exInspActInstRelation.setExInspScheduleId(scheduleId);
                exInspActInstRelation.setReportId(qcReport.getId());
                exInspActInstRelationRepository.save(exInspActInstRelation);
            });
        });


        exInspScheduleService.createExternalInspectionScheduleDetail(orgId, projectId,
            exInspSchedule, operator, inspectParties);

        /*---
            5.0 更新 外检 项目 的状态
         */
        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(
            orgId, projectId, scheduleId
        );
        Set<Long> newTaskIds = new HashSet<>();
        taskProcIds.forEach(taskProcId -> {
            if (taskProcId.getTaskId() != null) newTaskIds.add(taskProcId.getTaskId());
        });

        if (!CollectionUtils.isEmpty(newTaskIds))
            ruTaskRepository.updateStatus(ExInspApplyStatus.APPLY, new ArrayList<>(newTaskIds));

        logger.info("ExInspApplyDelegate->" + "开始更新报告中的 exInspScheduleId " + exInspSchedule.getId() + "->" + new Date());

        qcReports.forEach(report -> {
            report.setScheduleId(exInspSchedule.getId());
            qcReportRepository.save(report);
        });
        logger.info("ExInspApplyDelegate->" + "更新后的报告 exInspScheduleId " + exInspSchedule.getId() + "->" + new Date());
        return todoBatchTaskDTO;

    }

}
