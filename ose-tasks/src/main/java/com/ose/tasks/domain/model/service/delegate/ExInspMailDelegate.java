package com.ose.tasks.domain.model.service.delegate;

import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.repository.qc.ReportConfigRepository;
import com.ose.tasks.domain.model.service.AsyncExInspectionMailInterface;
import com.ose.tasks.domain.model.service.ServerConfig;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.externalInspection.ExInspScheduleInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.bpm.MailRunningStatus;
import com.ose.tasks.vo.bpm.ExInspScheduleCoordinateCategory;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.CollectionUtils;
import com.ose.util.LongUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 外检申请 代理服务。
 */
@Component
public class ExInspMailDelegate extends BaseBpmTaskDelegate implements BaseBpmTaskInterfaceDelegate {

    private final static Logger logger = LoggerFactory.getLogger(ExInspMailDelegate.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    private final ExInspScheduleInterface exInspScheduleService;


    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final BpmExInspMailApplicationDetailRepository bpmExInspMailApplicationDetailRepository;

    private final BpmExInspMailApplicationRepository bpmExInspMailApplicationRepository;

    private final BpmExInspMailHistoryRepository bpmExInspMailHistoryRepository;

    private final AsyncExInspectionMailInterface asyncExInspectionMailService;

    private final ServerConfig serverConfig;

    private final UploadFeignAPI uploadFeignAPI;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final QCReportRepository qcReportRepository;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final BpmExInspScheduleDetailRepository bpmExInspScheduleDetailRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;

    private final ReportConfigRepository reportConfigRepository;




    /**
     * 构造方法。
     */
    @Autowired
    public ExInspMailDelegate(BpmActivityInstanceRepository bpmActInstRepository,
                              BpmRuTaskRepository ruTaskRepository,
                              ExInspScheduleInterface exInspScheduleService,
                              BpmExInspMailApplicationDetailRepository bpmExInspMailApplicationDetailRepository,
                              BpmExInspMailApplicationRepository bpmExInspMailApplicationRepository,
                              BpmExInspMailHistoryRepository bpmExInspMailHistoryRepository,
                              AsyncExInspectionMailInterface asyncExInspectionMailService, ServerConfig serverConfig,
                              @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
                              TodoTaskBaseInterface todoTaskBaseService,
                              QCReportRepository qcReportRepository,
                              BpmExInspScheduleRepository bpmExInspScheduleRepository,
                              StringRedisTemplate stringRedisTemplate,
                              BpmExInspScheduleDetailRepository bpmExInspScheduleDetailRepository,
                              BpmActivityInstanceStateRepository bpmActivityInstanceStateRepository,
                              ExInspActInstRelationRepository exInspActInstRelationRepository,
                              ReportConfigRepository reportConfigRepository) {
        super(bpmActInstRepository, stringRedisTemplate, ruTaskRepository, bpmActivityInstanceStateRepository);
        this.bpmExInspMailApplicationDetailRepository = bpmExInspMailApplicationDetailRepository;
        this.bpmExInspMailApplicationRepository = bpmExInspMailApplicationRepository;
        this.bpmExInspMailHistoryRepository = bpmExInspMailHistoryRepository;
        this.asyncExInspectionMailService = asyncExInspectionMailService;
        this.serverConfig = serverConfig;
        this.uploadFeignAPI = uploadFeignAPI;
        this.exInspScheduleService = exInspScheduleService;
        this.todoTaskBaseService = todoTaskBaseService;
        this.qcReportRepository = qcReportRepository;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.bpmExInspScheduleDetailRepository = bpmExInspScheduleDetailRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
        this.reportConfigRepository = reportConfigRepository;
    }


    @Override
    @SuppressWarnings ( "unchecked" )
    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPrepareExecute(ContextDTO contextDTO,
                                                                                     Map<String, Object> data,
                                                                                     P todoBatchTaskCriteriaDTO,
                                                                                     TodoBatchTaskDTO todoBatchTaskDTO) {

        Long orgId = (Long) data.get("orgId");

        Long projectId = (Long) data.get("projectId");

        ExInspScheduleCriteriaDTO criteriaDTO = (ExInspScheduleCriteriaDTO) todoBatchTaskCriteriaDTO;
        todoBatchTaskCriteriaDTO.setFetchAll(true);

        todoBatchTaskDTO.setMainList(new PageImpl<>(exInspScheduleService.getExternalInspectionScheduleList(orgId, projectId, todoBatchTaskCriteriaDTO, criteriaDTO).getContent()));

        return todoBatchTaskDTO;
    }


    @Override
    @SuppressWarnings ( "unchecked" )

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPreExecute(ContextDTO contextDTO,
                                                                                 Map<String, Object> data,
                                                                                 P todoBatchTaskCriteriaDTO,
                                                                                 TodoBatchTaskDTO todoBatchTaskDTO) {

        ExInspMailApplyPreviewDTO exInspMailApplyDTO = (ExInspMailApplyPreviewDTO) todoBatchTaskCriteriaDTO;
        logger.info("SEND EX INSP EMAIL ---- PRE " + StringUtils.toJSON(exInspMailApplyDTO.getExternalInspectionApplyScheduleIds()));

        List<Long> exInspApplyScheduleIds = exInspMailApplyDTO.getExternalInspectionApplyScheduleIds();
        Map<String, BpmExInspMailHistory> exInspMailHistoryMap = new HashMap<>();
        Map<String, BpmExInspMailApplication> newExInspMailApplicationMap = new HashMap<>();

        Map<String, BpmExInspMailApplication> exInspMailApplicationMap = exInspMailApplyDTO
            .getInspectionMailApplications();

        Long orgId = (Long) data.get("orgId");
        Long projectId = (Long) data.get("projectId");
        OperatorDTO operator = contextDTO.getOperator();
        Map<String, Object> command = todoBatchTaskCriteriaDTO.getCommand();

        todoBatchTaskCriteriaDTO.setCommand(command);



        Map<String, Object> gateWay = todoBatchTaskCriteriaDTO.getCommand();

        boolean rejectFlag = false;
        data.put("rejectFlag", rejectFlag);
        for (String rejectStr : BpmCode.REJECT_SET) {
            if (gateWay.containsValue(rejectStr)) {
                rejectFlag = true;
                data.put("rejectFlag", rejectFlag);
                break;
            }
        }



        if (!rejectFlag) {

            Date sendTime = new Date();

            for (String applicationKey : exInspMailApplicationMap.keySet()) {
                BpmExInspMailApplication exInspMailApplication = exInspMailApplicationMap.get(applicationKey);
                Optional<BpmExInspMailApplication> option = bpmExInspMailApplicationRepository
                    .findById(exInspMailApplication.getId());
                if (!option.isPresent()) {
                    continue;
                }

                BpmExInspMailApplication savedExInspMailApplication = option.get();

                ExInspScheduleCoordinateCategory coordinateCategory = savedExInspMailApplication.getCoordinateCategory();

                savedExInspMailApplication.setCcMail(exInspMailApplication.getCcMail());
                savedExInspMailApplication.setMainContent(exInspMailApplication.getMainContent());
                savedExInspMailApplication.setFromMail(exInspMailApplication.getFromMail());
                savedExInspMailApplication.setSubject(exInspMailApplication.getSubject());
                savedExInspMailApplication.setToMail(exInspMailApplication.getToMail());


                if (exInspMailApplication.getTemporaryFileName() != null) {
                    logger.error("外检邮件1 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> fileESResponse = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        exInspMailApplication.getTemporaryFileName(), new FilePostDTO());
                    FileES fileES = fileESResponse.getData();
                    logger.error("外检邮件1 保存docs服务->结束");
                    ActReportDTO attachment = new ActReportDTO();
                    attachment.setFileId(LongUtils.parseLong(fileES.getId()));
                    attachment.setFilePath(fileES.getPath());
                    List<ActReportDTO> attachments = savedExInspMailApplication.getJsonAttachments();
                    attachments.add(attachment);
                    savedExInspMailApplication.setJsonAttachments(attachments);
                }

                List<BpmExInspMailApplicationDetail> exInspMailApplicationDetails = bpmExInspMailApplicationDetailRepository
                    .findByExternallInspectionMailApplicationId(savedExInspMailApplication.getId());
                List<ActReportDTO> attachments = new ArrayList<>();
                List<ActReportDTO> reports = new ArrayList<>();
                for (BpmExInspMailApplicationDetail exInspMailApplicationDetail : exInspMailApplicationDetails) {
                    exInspApplyScheduleIds.add(exInspMailApplicationDetail.getScheduleId());
                    attachments.addAll(exInspMailApplicationDetail.getJsonAttachments());
                    reports.addAll(exInspMailApplicationDetail.getJsonReports());
                }
                savedExInspMailApplication.setDetails(exInspMailApplicationDetails);

                BpmExInspMailHistory mailSendHis = new BpmExInspMailHistory();

                mailSendHis.setCcMail(savedExInspMailApplication.getCcMail());
                mailSendHis.setCreatedAt();
                mailSendHis.setJsonAttachments(attachments);
                mailSendHis.setJsonCatalogue(savedExInspMailApplication.getJsonReports());
                mailSendHis.setJsonReports(reports);
                mailSendHis.setMainContent(savedExInspMailApplication.getMainContent());
                mailSendHis.setOperator(operator.getId());
                mailSendHis.setOrgId(orgId);
                mailSendHis.setProjectId(projectId);
                mailSendHis.setSendTime(sendTime);
                mailSendHis.setStatus(EntityStatus.ACTIVE);
                mailSendHis.setSubject(savedExInspMailApplication.getSubject());
                mailSendHis.setToMail(savedExInspMailApplication.getToMail());
                mailSendHis.setSendStatus(MailRunningStatus.INIT);
                mailSendHis.setServerUrl(serverConfig.getUrl());
                exInspMailHistoryMap.put(applicationKey, mailSendHis);
                newExInspMailApplicationMap.put(applicationKey, savedExInspMailApplication);
            }

        }

        exInspApplyScheduleIds = exInspApplyScheduleIds.stream().distinct().collect(Collectors.toList());

        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.
            findTaskIdByOrgIdAndProjectIdAndExInspScheduleIdIn(orgId, projectId, exInspApplyScheduleIds);

        List<Long> taskIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getTaskId() != null).
            map(TaskProcQLDTO::getTaskId).collect(Collectors.toList());
        exInspMailApplyDTO.setTaskIds(taskIds);


        if (rejectFlag && CollectionUtils.isEmpty(exInspApplyScheduleIds)) {

            bpmExInspScheduleRepository.deleteByScheduleIds(exInspApplyScheduleIds, EntityStatus.DELETED);


            bpmExInspScheduleDetailRepository.deleteByScheduleIds(exInspApplyScheduleIds, EntityStatus.DELETED);
        }


        newExInspMailApplicationMap.forEach((key, exInspMailApplication) ->{
            exInspMailApplication.setMailStatus(MailRunningStatus.RUNNING);
            bpmExInspMailApplicationRepository.save(exInspMailApplication);
        });


        todoBatchTaskDTO.setMetaData(new HashMap<>());
        todoBatchTaskDTO.getMetaData().put("orgId", data.get("orgId"));
        todoBatchTaskDTO.getMetaData().put("rejectFlag", rejectFlag);
        todoBatchTaskDTO.getMetaData().put("projectId", data.get("projectId"));
        todoBatchTaskDTO.getMetaData().put("exInspApplyScheduleIds", exInspApplyScheduleIds);
        todoBatchTaskDTO.getMetaData().put("newExInspMailApplicationMap", newExInspMailApplicationMap);
        todoBatchTaskDTO.getMetaData().put("exInspMailHistoryMap", exInspMailHistoryMap);



        return todoBatchTaskDTO;

    }

    @Override
    @SuppressWarnings("unchecked" )

    public <P extends BaseBatchTaskCriteriaDTO> TodoBatchTaskDTO batchPostExecute(ContextDTO contextDTO,
                                                                                  Map<String, Object> data,
                                                                                  P todoBatchTaskCriteriaDTO,
                                                                                  TodoBatchTaskDTO todoBatchTaskDTO) {
        Long orgId = (Long) todoBatchTaskDTO.getMetaData().get("orgId");
        Long projectId = (Long) todoBatchTaskDTO.getMetaData().get("projectId");

        boolean rejectFlag = (boolean) todoBatchTaskDTO.getMetaData().get("rejectFlag");

        List<Long> exInspApplyScheduleIds = (List<Long>) todoBatchTaskDTO.getMetaData().get("exInspApplyScheduleIds");
        logger.info("SEND EX INSP EMAIL ---- POST " + StringUtils.toJSON(exInspApplyScheduleIds));

        Map<String, BpmExInspMailApplication> exInspMailApplicationMap = (Map) todoBatchTaskDTO.getMetaData().get("newExInspMailApplicationMap");
        Map<String, BpmExInspMailHistory> exInspMailHistoryMap = (Map) todoBatchTaskDTO.getMetaData().get("exInspMailHistoryMap");


        ReportStatus reportStatus = ReportStatus.INIT;


        if (rejectFlag) {

            List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.
                findTaskIdByOrgIdAndProjectIdAndExInspScheduleIdIn(orgId, projectId, exInspApplyScheduleIds);

            Set<Long> currentTaskIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getTaskId() != null).
                map(TaskProcQLDTO::getTaskId).collect(Collectors.toSet());

            Set<Long> currentProcIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getActInstId() != null).
                map(TaskProcQLDTO::getActInstId).collect(Collectors.toSet());

            for (Long taskId : currentTaskIds) {
                BpmRuTask ruTask = todoTaskBaseService.findBpmRuTaskByActTaskId(taskId);
                if (ruTask != null) {
                    ruTask.setApplyStatus(null);
                    todoTaskBaseService.saveBpmRuTask(ruTask);
                }
            }

            List<ReportConfig> allReportConfigs = reportConfigRepository.findByOrgIdAndProjectIdAndProcessIdAndStatus(
                orgId,
                projectId,
                todoBatchTaskCriteriaDTO.getProcessId(),
                EntityStatus.ACTIVE);
            for (Long procId : currentProcIds) {
                for (ReportConfig reportConfig : allReportConfigs) {
                    srem(String.format(RedisKey.REPORT_RUNNING_LIST.getDisplayName(), projectId.toString(), reportConfig.getReportSubType().toString()), procId.toString());
                }
            }


            bpmExInspScheduleRepository.deleteByScheduleIds(exInspApplyScheduleIds, EntityStatus.DELETED);


            bpmExInspScheduleDetailRepository.deleteByScheduleIds(exInspApplyScheduleIds, EntityStatus.DELETED);


            qcReportRepository.updateReportStatus(exInspApplyScheduleIds, ReportStatus.CANCEL);

        } else {


            exInspMailHistoryMap.forEach((key,exInspMailHistory)-> bpmExInspMailHistoryRepository.save(exInspMailHistory));


            for (Long scheduleId : exInspApplyScheduleIds) {
                logger.info("1、正在发送外检邮件的scheduleId:" + scheduleId);
                BpmExInspSchedule exInspSchedule = exInspScheduleService.getExternalInspectionSchedule(scheduleId);
                exInspSchedule.setState(ReportStatus.EMAIL_SENT);

                bpmExInspScheduleRepository.save(exInspSchedule);

                qcReportRepository.updateReportStatusByScheduleIdAndReportStatusNot(ReportStatus.EMAIL_SENT, scheduleId, ReportStatus.CANCEL);

            }

            exInspMailApplicationMap.forEach((key, exInspMailApplication) -> {
                exInspMailApplication.setMailStatus(MailRunningStatus.SENT);
                bpmExInspMailApplicationRepository.save(exInspMailApplication);
            });

            asyncExInspectionMailService.callAsyncExInspectionMailSend(contextDTO, orgId, projectId);

        }

        logger.info("SEND EX INSP EMAIL ---- DELEGATE FINISH " + StringUtils.toJSON(exInspApplyScheduleIds));

        return todoBatchTaskDTO;

    }
}
