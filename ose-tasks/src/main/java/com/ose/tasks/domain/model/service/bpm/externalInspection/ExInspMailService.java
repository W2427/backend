package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.dto.ContextDTO;
import com.ose.report.dto.InspectionReportApplicationFormItemDTO;
import com.ose.report.dto.InspectionReportApplicationFormPostDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.ExInspMailApplyPreviewDTO;
import com.ose.tasks.dto.bpm.ExInspTaskEmailDTO;
import com.ose.tasks.entity.bpm.BpmExInspMailHistory;
import com.ose.tasks.entity.bpm.BpmExInspMailApplication;
import com.ose.tasks.entity.bpm.BpmExInspMailApplicationDetail;
import com.ose.tasks.entity.bpm.BpmExInspSchedule;
import com.ose.tasks.entity.bpm.BpmExInspScheduleDetail;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.vo.bpm.MailRunningStatus;
import com.ose.tasks.vo.bpm.ExInspScheduleCoordinateCategory;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.util.BeanUtils;
import com.ose.util.CollectionUtils;
import com.ose.util.MailUtils;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.InspectParty;
import com.ose.vo.QrcodePrefixType;
import org.apache.poi.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import jakarta.mail.internet.InternetAddress;
import java.util.*;

/**
 * 用户服务。
 */
@Component
public class ExInspMailService implements ExInspMailInterface {

    private final static Logger logger = LoggerFactory.getLogger(ExInspMailService.class);



    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final BpmExInspScheduleDetailRepository bpmExInspScheduleDetailRepository;

    private final BpmExInspMailApplicationDetailRepository bpmExInspMailApplicationDetailRepository;

    private final BpmExInspMailApplicationRepository bpmExInspMailApplicationRepository;

    private final BpmExInspMailHistoryRepository bpmExInspMailHistoryRepository;

    private final BpmExInspMailHistoryRepositoryCustom bpmExInspMailHistoryRepositoryCustom;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final QCReportRepository qcReportRepository;

    private final ExInspScheduleInterface exInspScheduleService;


    /**
     * 构造方法。
     */
    @Autowired
    public ExInspMailService(
        BpmExInspScheduleRepository bpmExInspScheduleRepository,
        BpmExInspMailApplicationDetailRepository bpmExInspMailApplicationDetailRepository,
        BpmExInspMailApplicationRepository bpmExInspMailApplicationRepository,
        BpmExInspScheduleDetailRepository bpmExInspScheduleDetailRepository,
        BpmExInspMailHistoryRepository bpmExInspMailHistoryRepository,
        BpmExInspMailHistoryRepositoryCustom bpmExInspMailHistoryRepositoryCustom,
        TodoTaskDispatchInterface todoTaskDispatchService,
        QCReportRepository qcReportRepository, ExInspScheduleInterface exInspScheduleService) {
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.bpmExInspMailApplicationDetailRepository = bpmExInspMailApplicationDetailRepository;
        this.bpmExInspMailApplicationRepository = bpmExInspMailApplicationRepository;
        this.bpmExInspScheduleDetailRepository = bpmExInspScheduleDetailRepository;
        this.bpmExInspMailHistoryRepository = bpmExInspMailHistoryRepository;
        this.bpmExInspMailHistoryRepositoryCustom = bpmExInspMailHistoryRepositoryCustom;
        this.qcReportRepository = qcReportRepository;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.exInspScheduleService = exInspScheduleService;
    }

    /**
     * 外检邮件
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param operator  操作者
     * @return ExInspMailApplyPreviewDTO
     */
    @Override
    public ExInspMailApplyPreviewDTO externalInspectionMailPreview(Long orgId, Long projectId,
                                                                   Long operator, ExInspMailApplyPreviewDTO exInspMailApplyPreviewDTO) {
        Set<String> ccMailSet = new HashSet<>();

        Map<String, BpmExInspMailApplication> exInspMailApplications = new HashMap<>();

        Map<Long, List<QCReport>> scheduleReportMap = new HashMap<>();

        List<Long> exInspScheduleIdList = exInspMailApplyPreviewDTO.getExternalInspectionApplyScheduleIds();
        if (CollectionUtils.isEmpty(exInspScheduleIdList)) {
            return exInspMailApplyPreviewDTO;
        }



        List<BpmExInspSchedule> exInspSchedules = bpmExInspScheduleRepository
            .findByIdInAndStatusAndState(exInspScheduleIdList, EntityStatus.ACTIVE,
                ReportStatus.INIT);


        Map<Long, ExInspScheduleCoordinateCategory> exInspScheduleCoordinationMap = new HashMap<>();
        Map<Long, String> exInspAttachments = new HashMap<>();
        Map<Long, BpmExInspSchedule> exInspScheduleMap = new HashMap<>();
        for (BpmExInspSchedule exInspSchedule : exInspSchedules) {
            exInspScheduleCoordinationMap.put(exInspSchedule.getId(), exInspSchedule.getCoordinateCategory());
            exInspAttachments.put(exInspSchedule.getId(), exInspSchedule.getAttachments());
            exInspScheduleMap.put(exInspSchedule.getId(), exInspSchedule);
            List<QCReport> qcReports = qcReportRepository.findByScheduleIdAndReportStatusNot(exInspSchedule.getId(), ReportStatus.CANCEL);
            scheduleReportMap.put(exInspSchedule.getId(), qcReports);
        }

        List<BpmExInspScheduleDetail> exInspScheduleDetails = bpmExInspScheduleDetailRepository
            .findByScheduleIdInAndStatus(exInspScheduleIdList, EntityStatus.ACTIVE);
        if (CollectionUtils.isEmpty(exInspSchedules)) {
            return exInspMailApplyPreviewDTO;
        }

        Map<String, List<BpmExInspScheduleDetail>> exInspScheduleDetailsMap = new HashMap<>();
        Map<String, Set<ExInspScheduleCoordinateCategory>> coordinationsMap = new HashMap<>();
        for (BpmExInspScheduleDetail exInspScheduleDetail : exInspScheduleDetails) {
            if (exInspScheduleDetail.getInspectParty() == null) {
                continue;
            }

            if (exInspScheduleDetail.getInspectParty() == null) {
                continue;
            }
            String inspParty = exInspScheduleDetail.getInspectParty().name();

            exInspScheduleDetailsMap.computeIfAbsent(inspParty, k -> new ArrayList<>()).add(exInspScheduleDetail);
            coordinationsMap.computeIfAbsent(inspParty, k -> new HashSet<>()).add(exInspScheduleCoordinationMap.get(exInspScheduleDetail.getScheduleId()));

        }


        Map<String, Integer> listSnMap = new HashMap<>();

        for (String inspectParty : exInspScheduleDetailsMap.keySet()) {

            InspectionReportApplicationFormPostDTO exInspReportApplicationForm = new InspectionReportApplicationFormPostDTO();
            String reportQr = QrcodePrefixType.EXTERNAL_CONTROL_REPORT_APPLICATION.getCode()
                + StringUtils.generateShortUuid();
            String reportNo = "M12028-QM-1000";
            exInspReportApplicationForm.setReportQrCode(reportQr);
            exInspReportApplicationForm.setReportNo(reportNo);
            exInspReportApplicationForm.setSubmitTo(inspectParty);

            BpmExInspMailApplication exInspMailApplication = new BpmExInspMailApplication();
            exInspMailApplication.setOrgId(orgId);
            exInspMailApplication.setProjectId(projectId);
            exInspMailApplication.setOperator(operator);
            exInspMailApplication.setInspectParty(InspectParty.valueOf(inspectParty));
            exInspMailApplication.setStatus(EntityStatus.ACTIVE);
            if (coordinationsMap.computeIfAbsent(inspectParty, k -> new HashSet<>()).size() > 1) {
                exInspMailApplication.setCoordinateCategory(ExInspScheduleCoordinateCategory.MIX);
            } else {
                exInspMailApplication.setCoordinateCategory(
                    coordinationsMap.computeIfAbsent(inspectParty, k -> new HashSet<>()).iterator().next());
            }

            exInspMailApplication.setMailStatus(MailRunningStatus.INIT);

            List<ActReportDTO> exInspReports = new ArrayList<>();
            List<ActReportDTO> attachments = new ArrayList<>();
            List<InspectionReportApplicationFormItemDTO> exInspReportForms = new ArrayList<>();


            for (BpmExInspScheduleDetail exInspScheduleDetail : exInspScheduleDetailsMap.get(inspectParty)) {
                BpmExInspMailApplicationDetail exInspMailApplicationDetail = new BpmExInspMailApplicationDetail();
                List<String> seriesNos = new ArrayList<>();
                List<ActReportDTO> reports = new ArrayList<>();
                BpmExInspSchedule exInspSchedule = exInspScheduleMap.get(exInspScheduleDetail.getScheduleId());
                if (exInspSchedule == null) continue;
                List<QCReport> qcReports = scheduleReportMap.get(exInspSchedule.getId());
                reports.addAll(exInspScheduleService.getActReports(qcReports));
                reports.forEach(report -> {
                    seriesNos.add(report.getSeriesNo());
                });

                exInspMailApplicationDetail.setOrgId(orgId);
                exInspMailApplicationDetail.setProjectId(projectId);
                exInspMailApplicationDetail.setOperator(operator);
                exInspMailApplicationDetail.setJsonSeriesNos(seriesNos);
                exInspMailApplicationDetail.setJsonReports(reports);
                exInspMailApplicationDetail.setAttachments(exInspAttachments.get(exInspScheduleDetail.getScheduleId()));
                exInspMailApplicationDetail.setInspectParty(InspectParty.valueOf(inspectParty));
                exInspMailApplicationDetail.setScheduleId(exInspScheduleDetail.getScheduleId());
                exInspMailApplicationDetail.setStatus(EntityStatus.ACTIVE);
                exInspMailApplicationDetail.setExternallInspectionMailApplicationId(exInspMailApplication.getId());


                attachments.addAll(exInspMailApplicationDetail.getJsonAttachments());
                exInspReports.addAll(reports);

                bpmExInspMailApplicationDetailRepository.save(exInspMailApplicationDetail);
                exInspMailApplication.addDetails(exInspMailApplicationDetail);

                InspectionReportApplicationFormItemDTO mailFormListItem = new InspectionReportApplicationFormItemDTO();

                int i = listSnMap.computeIfAbsent(inspectParty, k -> 1);
                mailFormListItem.setNo(String.valueOf(i++));
                listSnMap.put(inspectParty, i);

                String inspectionItem = exInspSchedule.getName() + " " + String.join(":", seriesNos);
                mailFormListItem.setInspectionItem(inspectionItem);
                mailFormListItem.setDateTime(exInspSchedule.getExternalInspectionTime());
                mailFormListItem.setDiscipline(exInspSchedule.getDiscipline());
                mailFormListItem.setLocation(exInspSchedule.getLocation());
                mailFormListItem.setQcName(exInspSchedule.getOperatorName());
                exInspReportForms.add(mailFormListItem);

                if (exInspSchedule.getOperatorEmail() != null) {
                    ccMailSet.add(exInspSchedule.getOperatorEmail());
                }

            }

            exInspMailApplication.setJsonAttachments(attachments);
            exInspMailApplication.setCcMail(StringUtil.join(ccMailSet.toArray(new String[0]), ","));




            exInspReportApplicationForm.setItems(exInspReportForms);
            ReportHistory reportHistory = new ReportHistory();//inspApplicationReport.generateReport(orgId, projectId, null, null, exInspReportApplicationForm, null, null, null);

            ActReportDTO actReport = new ActReportDTO();
            BeanUtils.copyProperties(reportHistory, actReport);
            exInspReports.add(actReport);
            exInspMailApplication.setJsonReports(exInspReports);


            bpmExInspMailApplicationRepository.save(exInspMailApplication);
            exInspMailApplications.put(inspectParty, exInspMailApplication);

        }


        exInspMailApplyPreviewDTO.setInspectionMailApplications(exInspMailApplications);
        return exInspMailApplyPreviewDTO;
    }

    /**
     * 发送外检邮件
     */
    @Override
    public ExInspMailApplyPreviewDTO externalInspectionMailSend(Long orgId, Long projectId,
                                                                Long operator,
                                                                ExInspMailApplyPreviewDTO exInspMailApplyPreviewDTO,
                                                                ContextDTO contextDTO,
                                                                Map<String, Object> command) {

        Map<String, Object> data = new HashMap<>();
        data.put("orgId", orgId);
        data.put("projectId", projectId);
        exInspMailApplyPreviewDTO.setCommand(command);
        todoTaskDispatchService.batchExec(contextDTO, data, exInspMailApplyPreviewDTO);

        return exInspMailApplyPreviewDTO;
    }


    @Override
    public Page<BpmExInspMailHistory> getExternalInspectionEmail(Long orgId, Long projectId, ExInspTaskEmailDTO criteriaDTO) {
        return bpmExInspMailHistoryRepositoryCustom.getTaskEmailList(orgId, projectId, criteriaDTO);
    }

    @Override
    public List<ExInspTaskEmailDTO> getEmailOperatorList(Long orgId, Long projectId) {
        return bpmExInspMailHistoryRepositoryCustom.getOperatorList(orgId, projectId);
    }

    @Override
    public BpmExInspMailHistory getExternalInspectionEmail(Long orgId, Long projectId, Long id) {
        return bpmExInspMailHistoryRepository.findById(id).orElse(null);
    }



    @Override
    public void sendExternalInspectionMail(Long orgId, Long projectId, Long hisId) {
        BpmExInspMailHistory mailHis = bpmExInspMailHistoryRepository
            .findById(hisId)
            .orElse(null);
        try {
            Set<String> pathList = new TreeSet<>();
            for (ActReportDTO report : mailHis.getJsonCatalogue()) {
                String path = protectedDir + report.getFilePath().substring(1);
                pathList.add(path);
            }
            for (ActReportDTO report : mailHis.getJsonReports()) {
                String path = protectedDir + report.getFilePath().substring(1);
                pathList.add(path);
            }
            for (ActReportDTO report : mailHis.getJsonAttachments()) {
                String path = protectedDir + report.getFilePath().substring(1);
                pathList.add(path);
            }
            List<String> pathLists = new ArrayList<String>();
            pathLists.addAll(pathList);

            String toAddress = mailHis.getToMail().replaceAll(";", ",");
            String[] toAddressarr = toAddress.split(",");
            InternetAddress[] tos = new InternetAddress[toAddressarr.length];
            for (int i = 0; i < toAddressarr.length; i++) {
                tos[i] = new InternetAddress(toAddressarr[i]);
            }
            String ccAddress = mailHis.getToMail().replaceAll(";", ",");
            String[] ccAddressarr = ccAddress.split(",");
            InternetAddress[] ccs = new InternetAddress[ccAddressarr.length];
            for (int i = 0; i < ccAddressarr.length; i++) {
                ccs[i] = new InternetAddress(ccAddressarr[i]);
            }

            String strlog = "call MailUtils.send start " + mailHis.getId() + "**";
            System.out.println(strlog);
            logger.info(strlog);

            MailUtils.send(
                new InternetAddress(mailFromAddress),
                tos,
                ccs,
                mailHis.getSubject(),
                mailHis.getMainContent(),
                "text/html; charset=utf-8",
                pathLists
            );
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }


}
