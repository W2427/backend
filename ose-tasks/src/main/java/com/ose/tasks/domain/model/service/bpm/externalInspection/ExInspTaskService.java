package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.auth.api.UserFeignAPI;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.ExInspReportHandleDTO;
import com.ose.tasks.dto.bpm.ExInspReportHandleSearchDTO;
import com.ose.tasks.dto.bpm.ExInspViewCriteriaDTO;
import com.ose.tasks.dto.bpm.TaskGatewayDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.bpm.ProcessType;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.CollectionUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.InspectResult;
import com.ose.vo.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 用户服务。
 */
@Component
public class ExInspTaskService extends StringRedisService implements ExInspTaskInterface {

    private final static Logger logger = LoggerFactory.getLogger(ExInspTaskService.class);



    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final BpmExInspUploadHistoryRepository bpmExInspUploadHistoryRepository;

    private final ExInspScheduleInterface exInspScheduleService;

    private final BpmProcessRepository processRepository;

    private final ActivityTaskInterface activityTaskService;

    private final UserFeignAPI userFeignAPI;

    private final QCReportRepository qcReportRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;


    /**
     * 构造方法。
     */
    @Autowired
    public ExInspTaskService(
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmEntityDocsMaterialsRepository docsMaterialsRepository,
        BpmRuTaskRepository ruTaskRepository,
        BpmExInspUploadHistoryRepository bpmExInspUploadHistoryRepository,
        ExInspScheduleInterface exInspScheduleService,
        BpmProcessRepository processRepository,
        StringRedisTemplate stringRedisTemplate,
        ActivityTaskInterface activityTaskService, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        QCReportRepository qcReportRepository, ExInspActInstRelationRepository exInspActInstRelationRepository) {
        super(stringRedisTemplate);
        this.bpmActInstRepository = bpmActInstRepository;
        this.exInspScheduleService = exInspScheduleService;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.bpmExInspUploadHistoryRepository = bpmExInspUploadHistoryRepository;
        this.processRepository = processRepository;
        this.activityTaskService = activityTaskService;
        this.userFeignAPI = userFeignAPI;
        this.qcReportRepository = qcReportRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
    }


    /**
     * 查询需要再次处理的外检
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param pageDTO     分页DTO
     * @param criteriaDTO
     * @return  Page<QCReport>
     */
    @Override
    public Page<QCReport> externalInspectionViews(Long orgId, Long projectId, PageDTO pageDTO,
                                                  ExInspViewCriteriaDTO criteriaDTO) {
        Page<QCReport> commentReports = bpmExInspUploadHistoryRepository.externalInspectionViews(orgId, projectId, pageDTO, criteriaDTO);
        Map<Long, String> nameMap = new HashMap<>();
        for (QCReport commentReport : commentReports.getContent()) {
            Long userid = commentReport.getOperator();
            if (userid != null) {
                if (nameMap.containsKey(userid)) {
                    commentReport.setOperatorName(nameMap.get(userid));
                } else {
                    String username = getRedisKey(String.format(RedisKey.USER.getDisplayName(),userid), 60*60*8);
                    if(StringUtils.isEmpty(username)) {
                        username = userFeignAPI.get(userid).getData().getName();
                        setRedisKey(String.format(RedisKey.USER.getDisplayName(),userid), username);
                    }


                    nameMap.put(userid, username);
                    commentReport.setOperatorName(username);
                }
            }
        }
        return commentReports;
    }


    @Override
    public List<ExInspReportHandleDTO> externalInspectionViewsDetail(Long orgId, Long projectId,
                                                                     Long qcReportId) {
        Optional<QCReport> qcReportOptional = qcReportRepository.findById(qcReportId);
        if (qcReportOptional.isPresent()) {
            QCReport qcReport = qcReportOptional.get();
            String process = qcReport.getProcess();
            String processStage = qcReport.getProcessStage();

            BpmProcess bpmProcess = processRepository.findByOrgIdAndProjectIdAndStageNameAndName(orgId, projectId, processStage, process).orElse(null);
            if (bpmProcess == null) { return null; }

            List<ExInspReportHandleDTO> exInspReportHandleDTOS = exInspScheduleService.getScheduleDetail(orgId, projectId, qcReport);

            if (bpmProcess.getProcessType() == ProcessType.CUT_LIST || bpmProcess.getProcessType() == ProcessType.DELIVERY_LIST) {

                if (!exInspReportHandleDTOS.isEmpty()) {
                    ExInspReportHandleDTO exInspReportHandleDTO = exInspReportHandleDTOS.get(0);
                    exInspReportHandleDTOS.clear();
                    List<TaskGatewayDTO> gateWay = new ArrayList<>();
                    gateWay.add(new TaskGatewayDTO("OK", BpmCode.EXCLUSIVE_GATEWAY_RESULT, BpmCode.EXCLUSIVE_GATEWAY_RESULT_ACCEPT));
                    gateWay.add(new TaskGatewayDTO("NG", BpmCode.EXCLUSIVE_GATEWAY_RESULT, BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT));
//                    List<BpmDeliveryEntity> deliveryList = bpmDeliveryEntityRepository.findByDeliveryId(exInspReportHandleDTO.getEntityId());
//                    for (BpmDeliveryEntity entity : deliveryList) {
//                        ExInspReportHandleDTO entityDTO = BeanUtils.copyProperties(exInspReportHandleDTO, new ExInspReportHandleDTO());
//                        entityDTO.setEntityId(entity.getId());
//                        entityDTO.setEntityNo(entity.getEntityNo());
//                        entityDTO.setGateway(gateWay);
//                        exInspReportHandleDTOS.add(entityDTO);
//                    }
                }

            } else {

                for (ExInspReportHandleDTO exInspReportHandleDTO : exInspReportHandleDTOS) {

                    List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(exInspReportHandleDTO.getActInstId());
                    BpmActivityInstanceBase actInst = bpmActInstRepository.findById(exInspReportHandleDTO.getActInstId()).orElse(null);
                    if (!ruTasks.isEmpty()) {
                        Long taskId = ruTasks.get(0).getId();
                        List<TaskGatewayDTO> gateWayDTOs = activityTaskService.getTaskGateway(projectId, actInst.getProcessId(),
                            actInst.getBpmnVersion(), ruTasks.get(0).getTaskDefKey());


                        exInspReportHandleDTO.setGateway(gateWayDTOs);
                    }
                }
            }

            return exInspReportHandleDTOS;
        }
        return null;
    }



    @Override
    public void externalInspectionAgainHandle(Long orgId, Long projectId,
                                              ExInspReportHandleSearchDTO uploadDTO) {
        List<ExInspReportHandleDTO> inspectionReports = uploadDTO.getExInspReportHandleDTOS();
        if (inspectionReports != null) {
            Map<Long, ExInspReportHandleDTO> map = new HashMap<>();
            for (ExInspReportHandleDTO inspectionReport : inspectionReports) {
                map.put(inspectionReport.getScheduleId(), inspectionReport);
            }
            Iterator<Long> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                Long id = iterator.next();
                ExInspReportHandleDTO inspectionReport = map.get(id);
                Long scheduleDetailId = inspectionReport.getScheduleId();
                QCReport qcReportAgain = qcReportRepository.findByScheduleIdAndReportStatus(scheduleDetailId,
                    ReportStatus.PENDING);
                if (qcReportAgain != null) {
                    qcReportRepository.save(qcReportAgain);
                }
                QCReport qcReportFirst = qcReportRepository.findByScheduleIdAndReportStatus(scheduleDetailId,
                    ReportStatus.INIT);
                if (qcReportFirst != null) {
                    qcReportRepository.save(qcReportFirst);
                }
            }

        }
    }


    @Override
    public void moveReportsToEntityDoc(BpmExInspSchedule exInspSchedule,
                                       List<ActReportDTO> reports,
                                       ReportStatus reportStatus,
                                       String gateWayCommand,
                                       OperatorDTO operatorDTO) {
        ActInstDocType actInstDocType = null;
        Boolean exInspFinal = null;
        InspectResult inspectResult = null;
        Long scheduleId = exInspSchedule.getId();
        Long orgId = exInspSchedule.getOrgId();
        Long projectId = exInspSchedule.getProjectId();





        getExInspResult(inspectResult,
            exInspFinal,
            actInstDocType,
            reportStatus,
            gateWayCommand);








        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(orgId, projectId, scheduleId);

        List<Long> actInstIds = new ArrayList<>();
        List<Long> taskIds = new ArrayList<>();
        taskProcIds.forEach(taskProcId -> {
            actInstIds.add(taskProcId.getActInstId());
            taskIds.add(taskProcId.getTaskId());
        });


        Map<Long, BpmActivityInstanceBase> bpmActivityInstanceMap = new HashMap<>();
        List<BpmActivityInstanceBase> bpmActivityInstances;

        if(actInstIds != null){
            bpmActivityInstances = bpmActInstRepository
                .findByProjectIdAndIdInAndStatus(projectId, actInstIds.toArray(new Long[0]), EntityStatus.ACTIVE);
        } else {

            if(CollectionUtils.isEmpty(taskIds)){
                return;
            }
            bpmActivityInstances = bpmActInstRepository.findByProjectIdAndTaskIdIn(exInspSchedule.getProjectId(),
                taskIds);
        }


        for (BpmActivityInstanceBase actInstance : bpmActivityInstances) {
            bpmActivityInstanceMap.put(actInstance.getId(), actInstance);
        }

        Iterator<Long> instIterator = bpmActivityInstanceMap.keySet().iterator();
        while (instIterator.hasNext()) {
            Long instanceId = instIterator.next();
            BpmActivityInstanceBase actInstance = bpmActivityInstanceMap.get(instanceId);

            BpmEntityDocsMaterials bpmDoc = new BpmEntityDocsMaterials();
            bpmDoc.setProjectId(actInstance.getProjectId());
            bpmDoc.setCreatedAt();
            bpmDoc.setProcessId(actInstance.getProcessId());
            bpmDoc.setEntityNo(actInstance.getEntityNo());
            bpmDoc.setEntityId(actInstance.getEntityId());
            bpmDoc.setStatus(EntityStatus.ACTIVE);
            bpmDoc.setOperator(operatorDTO.getId());
            bpmDoc.setJsonDocs(reports);

            bpmDoc.setType(actInstDocType);
            if (exInspFinal != null) {
                bpmDoc.setExternalInspectionFinal(exInspFinal);
            }
            if (inspectResult != null) {
                bpmDoc.setExternalInspectionResult(inspectResult.name());
            }
            docsMaterialsRepository.save(bpmDoc);
        }
    }



    @Override
    public void getExInspResult(InspectResult inspectResult,
                                Boolean exInspFinal,
                                ActInstDocType actInstDocType,
                                ReportStatus reportStatus,
                                String gateWayCommand) {
        exInspFinal = null;
        if (reportStatus == ReportStatus.INIT) {
            actInstDocType = ActInstDocType.EXTERNAL_INSPECTION;
            if (gateWayCommand.equals(BpmCode.EXCLUSIVE_GATEWAY_RESULT_NO_COMMENT)) {
                exInspFinal = true;
                inspectResult = InspectResult.A;
            } else {
                if (gateWayCommand.equals(BpmCode.EXCLUSIVE_GATEWAY_RESULT_COMMENT)) {
                    inspectResult = InspectResult.B;
                } else if (gateWayCommand.equals(BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT)) {
                    inspectResult = InspectResult.C;
                }
            }
        } else if (reportStatus == ReportStatus.REHANDLE_INIT || reportStatus == ReportStatus.REHANDLE_UPLOADED) {
            actInstDocType = ActInstDocType.EXTERNAL_INSPECTION_RESIGN;
            inspectResult = InspectResult.A;
            exInspFinal = true;
        }
    }


    @Override
    public boolean releaseDeliveryEntities(Long orgId, Long projectId,
                                           ExInspReportHandleSearchDTO exInspReportHandleSearchDTO) {
        List<ExInspReportHandleDTO> exInspReportHandleDTOS = exInspReportHandleSearchDTO.getExInspReportHandleDTOS();
        String process = "";
        String entitySubType = "";
        String processStage = "";
        if (!exInspReportHandleDTOS.isEmpty()) {
            process = exInspReportHandleDTOS.get(0).getProcess();
            entitySubType = exInspReportHandleDTOS.get(0).getEntitySubType();
            processStage = exInspReportHandleDTOS.get(0).getProcessStage();
        }

        BpmProcess bpmProcess = processRepository.findByOrgIdAndProjectIdAndStageNameAndName(orgId, projectId, processStage, process).orElse(null);
        if (bpmProcess == null || entitySubType == null) { return false; }
        if (bpmProcess.getProcessType() == ProcessType.CUT_LIST) {

            for (ExInspReportHandleDTO exInspReportHandleDTO : exInspReportHandleDTOS) {
                if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT.equals(exInspReportHandleDTO.getCommand().values().iterator().next())) {
//                    BpmDeliveryEntity deliveryEntity = bpmDeliveryEntityRepository.findById(exInspReportHandleDTO.getEntityId()).orElse(null);
//                    if (deliveryEntity == null) { continue; }
//                    deliveryEntity.setExecuteNgFlag(true);
//                    bpmDeliveryEntityRepository.save(deliveryEntity);
                }
            }
            return true;
        } else if (bpmProcess.getProcessType() == ProcessType.DELIVERY_LIST) {
            for (ExInspReportHandleDTO exInspReportHandleDTO : exInspReportHandleDTOS) {
                if (BpmCode.EXCLUSIVE_GATEWAY_RESULT_REJECT.equals(exInspReportHandleDTO.getCommand().values().iterator().next())) {
//                    BpmCuttingEntity cuttingEntity = bpmCuttingEntityRepository.findById(exInspReportHandleDTO.getEntityId()).orElse(null);
//                    if (cuttingEntity == null) { continue; }
//                    cuttingEntity.setCuttingflag(false);
//                    bpmCuttingEntityRepository.save(cuttingEntity);
                }
            }
        }
        return false;
    }

}
