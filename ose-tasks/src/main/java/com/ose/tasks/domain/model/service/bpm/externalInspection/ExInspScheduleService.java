package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.auth.api.UserFeignAPI;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.rapairData.RepairBatchRevocationNodeDTO;
import com.ose.tasks.dto.rapairData.RepairBatchRevocationNodesDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmActivityInstanceState;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.entity.bpm.BpmExInspScheduleDetail;
import com.ose.tasks.entity.report.ExInspActInstHandleHistory;
import com.ose.tasks.entity.report.ExInspActInstRelation;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.bpm.ExInspApplyStatus;
import com.ose.tasks.vo.bpm.ExInspScheduleCoordinateCategory;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.vo.BpmTaskType;
import com.ose.vo.EntityStatus;
import com.ose.vo.InspectParty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 用户服务。
 */
@Component
public class ExInspScheduleService implements ExInspScheduleInterface {

    private final static Logger logger = LoggerFactory.getLogger(ExInspScheduleService.class);



    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final BpmExInspScheduleDetailRepository bpmExInspScheduleDetailRepository;

    private final QCReportRepository qcReportRepository;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final UserFeignAPI userFeignAPI;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final ExInspActInstHandleHistoryRepository exInspActInstHandleHistoryRepository;

    private final BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository;



    /**
     * 构造方法。
     */
    @Autowired
    public ExInspScheduleService(
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmExInspScheduleRepository bpmExInspScheduleRepository,
        BpmExInspScheduleDetailRepository bpmExInspScheduleDetailRepository,
        QCReportRepository qcReportRepository,
        TodoTaskBaseInterface todoTaskBaseService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        TodoTaskDispatchInterface todoTaskDispatchService,
        ExInspActInstRelationRepository exInspActInstRelationRepository,
        ExInspActInstHandleHistoryRepository exInspActInstHandleHistoryRepository,
        BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository) {
        this.bpmActInstRepository = bpmActInstRepository;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.bpmExInspScheduleDetailRepository = bpmExInspScheduleDetailRepository;
        this.qcReportRepository = qcReportRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.userFeignAPI = userFeignAPI;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.exInspActInstHandleHistoryRepository = exInspActInstHandleHistoryRepository;
        this.bpmActivityInstanceStateRepository = bpmActivityInstanceStateRepository;
    }


    /**
     * 创建新的报检记录
     */
    @Override
    public BpmExInspSchedule createExternalInspectionSchedule(Long orgId, Long projectId,
                                                              ExInspScheduleDTO scheduleDTO, OperatorDTO operator, Boolean isSingleReport, Boolean isSendEmail) {
        BpmExInspSchedule schedule = BeanUtils.copyProperties(scheduleDTO,
            new BpmExInspSchedule());

        schedule.setProjectId(projectId);
        schedule.setOrgId(orgId);
        schedule.setCreatedAt();
        schedule.setStatus(EntityStatus.ACTIVE);
        schedule.setSendEmail(isSendEmail);

        schedule.setJsonExternalInspectionParties(scheduleDTO.getInspectParties().stream().map(Enum::name).collect(Collectors.toList()));

        if (schedule.getSendEmail()) {
            try {
                schedule.setCoordinateCategory(
                    ExInspScheduleCoordinateCategory.valueOf(scheduleDTO.getCoordinateCategory()));
            } catch (Exception e) {
                logger.error("COORDINATE STRING WRONG: " + scheduleDTO.getCoordinateCategory());

            }
        }
        if ("NOCOORDINATE".equals(scheduleDTO.getCoordinateCategory()) || StringUtils.isEmpty(scheduleDTO.getCoordinateCategory())) {
            logger.error("COORDINATE IS:" + scheduleDTO.getCoordinateCategory());
            schedule.setCoordinateCategory(ExInspScheduleCoordinateCategory.NO_COORDINATE);
        }

        List<String> temporaryFileNames = scheduleDTO.getTemporaryFileNames();
        List<ActReportDTO> attachments = null;
        if (temporaryFileNames != null && temporaryFileNames.size() > 0) {
            attachments = todoTaskBaseService.uploadTemporaryFileNames(orgId, projectId, temporaryFileNames);
        }

        schedule.setOperator(operator.getId());
        schedule.setOperatorName(operator.getName());
        String email = userFeignAPI.get(operator.getId()).getData().getEmail();
        schedule.setOperatorEmail(email);
        schedule.setState(ReportStatus.INIT);
        schedule.setExternalInspectionTime(scheduleDTO.getExternalInspectionTime());

        schedule.setJsonAttachments(attachments);

        schedule.setSingleReport(isSingleReport);
        schedule = bpmExInspScheduleRepository.save(schedule);


        return schedule;
    }

    /**
     * 创建新的报检详情记录
     */
    @Override
    public List<BpmExInspScheduleDetail> createExternalInspectionScheduleDetail(Long orgId, Long projectId,
                                                                                BpmExInspSchedule schedule, OperatorDTO operator,
                                                                                Set<InspectParty> inspectParties) {
        List<BpmExInspScheduleDetail> exInspScheduleDetails = new ArrayList<>();
        inspectParties.forEach(inspectParty -> {
            BpmExInspScheduleDetail exInspScheduleDetail = new BpmExInspScheduleDetail();
            exInspScheduleDetail.setOrgId(orgId);
            exInspScheduleDetail.setProjectId(projectId);
            exInspScheduleDetail.setScheduleId(schedule.getId());
            exInspScheduleDetail.setInspectParty(inspectParty);
            exInspScheduleDetail.setStatus(EntityStatus.ACTIVE);
            exInspScheduleDetail.setCreatedBy(operator.getId());
            exInspScheduleDetail.setLastModifiedBy(operator.getId());
            exInspScheduleDetail.setApplyStatus(ExInspApplyStatus.APPLY);
            bpmExInspScheduleDetailRepository.save(exInspScheduleDetail);
            exInspScheduleDetails.add(exInspScheduleDetail);
        });


        return exInspScheduleDetails;
    }


    /**
     * 删除报检记录
     */
    @Override
    public boolean deleteExternalInspectionSchedule(ContextDTO context, Long orgId, Long projectId, Long id, Map<String, Object> command) {
        // 获取所有proc_inst_id
        List<ExInspActInstRelation> exInspActInstRelationList = exInspActInstRelationRepository.findByExInspScheduleId(id);
        Object[] collect = exInspActInstRelationList.stream().map(ExInspActInstRelation::getActInstId).toArray();
        Long[] actInstIdArray = new Long[collect.length];
        for (int i = 0; i < collect.length; i++) {
            actInstIdArray[i] =(Long)collect[i];

        }
        // 取消scheduleId下的所有报告
        QCReport qcReport = qcReportRepository.findByScheduleId(id);
        if (qcReport != null){
            qcReport.setReportStatus(ReportStatus.CANCEL);
            qcReport.setLastModifiedAt(new Date());
            qcReportRepository.save(qcReport);
        }
        // 获取所有流程实例
        List<BpmActivityInstanceBase> activityInstanceBases = bpmActInstRepository.findByProjectIdAndIdInAndStatus(projectId, actInstIdArray, EntityStatus.ACTIVE);
        if (activityInstanceBases!= null && activityInstanceBases.size() > 0 ) {
            RepairBatchRevocationNodesDTO repairBatchRevocationNodesDTO = new RepairBatchRevocationNodesDTO();
            List<RepairBatchRevocationNodeDTO> repairBatchRevocationNodeDTOs = new ArrayList<>();
            // 将找到的流程实例进行撤回到"申请外检"
            activityInstanceBases.forEach(item->{
                RepairBatchRevocationNodeDTO repairBatchRevocationNodeDTO = new RepairBatchRevocationNodeDTO();
                repairBatchRevocationNodeDTO.setActInstId(item.getId());
                repairBatchRevocationNodeDTO.setTaskDefKey("UT-APPLY_EXTERNAL_INSPECTION");
                repairBatchRevocationNodeDTOs.add(repairBatchRevocationNodeDTO);
            });
            repairBatchRevocationNodesDTO.setRepairBatchRevocationNodeDTOs(repairBatchRevocationNodeDTOs);
            // 调用数据修复接口,批量撤回节点
//            repairDataService.batchRevocationNode(orgId, projectId, repairBatchRevocationNodesDTO, context);

            // 删除外检申请的待办任务中的记录
            BpmExInspSchedule op = bpmExInspScheduleRepository.findByIdAndStatus(id,EntityStatus.ACTIVE);
            if (op != null) {
                op.setStatus(EntityStatus.DELETED);
                op.setLastModifiedAt(new Date());
                bpmExInspScheduleRepository.save(op);
                return true;
            }
        }
        return false;
    }

    /**
     * 撤回外检申请
     *
     * @param schedule 外检安排
     * @return
     */
    private boolean revocationExternalInspectionSchedule(ContextDTO context, Long orgId, Long projectId,
                                                         BpmExInspSchedule schedule, Map<String, Object> command) {

        ExInspMailApplyPreviewDTO applyDTO = new ExInspMailApplyPreviewDTO();

        Map<String, Object> data = new HashMap<>();
        data.put("orgId", orgId);
        data.put("projectId", projectId);
        List<Long> scheduleIds = new ArrayList<>();
        scheduleIds.add(schedule.getId());
        applyDTO.setExternalInspectionApplyScheduleIds(scheduleIds);
        applyDTO.setCommand(command);
        applyDTO.setTaskType(BpmTaskType.EX_INSP_APPLY_MAIL.name());

        TodoBatchTaskDTO todoBatchTaskDTO = todoTaskDispatchService.batchExec(context, data, applyDTO);

        return todoBatchTaskDTO.isBatchExecuteResult();
    }

    /**
     * 修改报检记录
     */
    @Override
    public BpmExInspSchedule modifyExternalInspectionSchedule(Long orgId, Long projectId, Long id,
                                                              ExInspScheduleDTO scheduleDTO) {

        BpmExInspSchedule schedule = bpmExInspScheduleRepository.findById(id).orElse(null);
        if (schedule != null) {

            List<InspectParty> inspectParties = scheduleDTO.getInspectParties();





            List<String> temporaryFileNames = scheduleDTO.getTemporaryFileNames();

            List<ActReportDTO> attachments = new ArrayList<>();
            if (!CollectionUtils.isEmpty(temporaryFileNames)) {
                attachments = todoTaskBaseService.uploadTemporaryFileNames(orgId, projectId, temporaryFileNames);
            }

            schedule = BeanUtils.copyProperties(scheduleDTO, schedule);
            schedule.setJsonAttachments(attachments);


            schedule.setJsonExternalInspectionParties(inspectParties.stream().map(inspectParty -> inspectParty.name()).collect(Collectors.toList()));
            schedule.setExternalInspectionTime(scheduleDTO.getExternalInspectionTime());
            schedule.setLastModifiedAt();

            schedule = bpmExInspScheduleRepository.save(schedule);

            return schedule;
        }
        return null;

    }

    /**
     * 查询外检任务明细
     */
    @Override
    public List<ExInspReportHandleDTO> getScheduleDetail(Long orgId, Long projectId,
                                                         QCReport qcReport) {
        List<ExInspReportHandleDTO> exInspReportHandleDTOS = new ArrayList<>();
        QCReport report = null;

        if (qcReport.getQrcode() == null) {
            return exInspReportHandleDTOS;
        }
        report = qcReportRepository.findByQrcode(qcReport.getQrcode());

        if (report == null || report.getScheduleId() == null) {
            return exInspReportHandleDTOS;
        }

        Long scheduleId = report.getScheduleId();

        BpmExInspSchedule exInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);

        if (exInspSchedule == null) return exInspReportHandleDTOS;

        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(orgId, projectId, scheduleId);

        for (TaskProcQLDTO taskProcId : taskProcIds) {


            BpmActivityInstanceBase actInstance = bpmActInstRepository.findById(taskProcId.getActInstId()).orElse(null);
            BpmActivityInstanceState actInstState = bpmActivityInstanceStateRepository.findByBaiId(actInstance.getId());
            ExInspReportHandleDTO exInspReportHandleDTO = new ExInspReportHandleDTO();
            if (actInstance == null || actInstState == null) continue;

            exInspReportHandleDTO.setTaskId(taskProcId.getTaskId());
            exInspReportHandleDTO.setEntityNo(actInstance.getEntityNo());
            exInspReportHandleDTO.setEntityId(actInstance.getEntityId());
            exInspReportHandleDTO.setEntityType(actInstance.getEntityType());
            exInspReportHandleDTO.setEntitySubType(actInstance.getEntitySubType());
            exInspReportHandleDTO.setProcess(actInstance.getProcess());
            exInspReportHandleDTO.setProcessStage(actInstance.getProcessStage());
            exInspReportHandleDTO.setSuspensionState(actInstState.getSuspensionState());
            exInspReportHandleDTO.setFinishState(actInstState.getFinishState());
            exInspReportHandleDTO.setBpmActivityInstanceId(actInstance.getId());
            exInspReportHandleDTO.setReportNo(qcReport.getReportNo());
            exInspReportHandleDTO.setActInstId(actInstance.getId());
            exInspReportHandleDTO.setScheduleId(qcReport.getScheduleId());
            exInspReportHandleDTO.setReportName(qcReport.getReportNo());
            exInspReportHandleDTO.setReportSeriesNo(String.valueOf(qcReport.getSeriesNo()));
            exInspReportHandleDTO.setInspectResult(taskProcId.getInspectResult());
            exInspReportHandleDTO.setReportType(report.getReportType() == null ? null : report.getReportType().name());
            exInspReportHandleDTO.setReportSubType(report.getReportSubType() == null ? null : report.getReportSubType().name());
            exInspReportHandleDTOS.add(exInspReportHandleDTO);
        }


        return exInspReportHandleDTOS;
    }


    /**
     * 获取报检记录列表
     */
    @Override
    public Page<BpmExInspSchedule> getExternalInspectionScheduleList(Long orgId, Long projectId,
                                                                     PageDTO page, ExInspScheduleCriteriaDTO criteriaDTO) {
        ExInspScheduleCriteriaDTO dto = new ExInspScheduleCriteriaDTO();
        dto.setState(criteriaDTO.getState());
        dto.setCoordinateCategory(criteriaDTO.getCoordinateCategory());
        dto.setKeyword("");
        dto.setOperator(criteriaDTO.getOperator());
        Page<BpmExInspSchedule> list = bpmExInspScheduleRepository.findByOrgIdAndProjectIdOrderByCreatedAtDesc(orgId, projectId, page,
            dto);
        for (BpmExInspSchedule schedule : list) {
            if (schedule.getSeriesNos() == null) {
                List<String> seriesNo = bpmExInspScheduleRepository.findSeriesNosByProjectIdAndScheduleId(projectId, schedule.getId());
                schedule.setSeriesNos(String.join(",", seriesNo));
                bpmExInspScheduleRepository.save(schedule);
            }
        }
        Page<BpmExInspSchedule> newList = bpmExInspScheduleRepository.findByOrgIdAndProjectIdOrderByCreatedAtDesc(orgId, projectId, page,
            criteriaDTO);
        return newList;
    }


    /**
     * 获取报检记录详细信息
     */
    @Override
    public BpmExInspSchedule getExternalInspectionSchedule(Long id) {
        Optional<BpmExInspSchedule> op = bpmExInspScheduleRepository.findById(id);
        if (op.isPresent()) {
            BpmExInspSchedule schedule = op.get();
            return schedule;
        }
        return null;
    }


    @Override
    public List<BpmExInspScheduleDetail> getExternalInspectionScheduleDetails(Long id) {

        return bpmExInspScheduleDetailRepository.findByScheduleId(id);
    }


    @Override
    public BpmExInspSchedule saveBpmExternalInspectionSchedule(BpmExInspSchedule schedule) {
        return bpmExInspScheduleRepository.save(schedule);
    }

    @Override
    public List<ActReportDTO> getActReports(List<QCReport> qcReports) {
        List<ActReportDTO> actReports = new ArrayList<>();
        qcReports.forEach(report -> {
            ActReportDTO actReport = new ActReportDTO();
            actReport.setReportNo(report.getReportNo());
            actReport.setReportType(report.getReportType().name());
            actReport.setReportQrCode(report.getQrcode());
            actReport.setFileId(report.getPdfReportFileId());
            actReport.setSeriesNo(report.getSeriesNo());
            actReports.add(actReport);
        });
        return actReports;
    }

    @Override
    public List<QCReport> getExternalInspectionScheduleReport(Long orgId, Long projectId, Long scheduleId) {
        return qcReportRepository.findByProjectIdAndScheduleId(projectId, scheduleId);
    }

    @Override
    public Page<ExInspActInstHandleHistory> getExInspActInstHandleHistory(
        Long orgId,
        Long projectId,
        ExInspActInstHandleHistoryDTO exInspActInstHandleHistoryDTO
    ) {
        return exInspActInstHandleHistoryRepository.search(
            orgId,
            projectId,
            exInspActInstHandleHistoryDTO
        );
    }
}
