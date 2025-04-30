package com.ose.tasks.domain.model.service.report;

import com.ose.util.*;
import com.ose.vo.*;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.dto.jpql.TaskProcQLDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.report.dto.InspectionReportPostDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.report.vo.ReportExportType;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmEntityDocsMaterialsRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.bpm.QCReportRepository;
import com.ose.tasks.domain.model.repository.qc.ExInspActInstRelationRepository;
import com.ose.tasks.domain.model.repository.qc.ReportConfigRepository;
import com.ose.tasks.domain.model.service.bpm.TodoTaskBaseInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.QCReportCriteriaDTO;
import com.ose.tasks.dto.bpm.QCReportPackageDTO;
import com.ose.tasks.dto.bpm.ReportUploadDTO;
import com.ose.tasks.dto.bpm.TodoTaskExecuteDTO;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.report.ReportConfig;
import com.ose.tasks.util.SpringUtils;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.qc.ReportTypeList;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class QCReportService extends StringRedisService implements QCReportInterface {


    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    private final static Logger logger = LoggerFactory.getLogger(QCReportService.class);



    private static Map<Long, Integer> reportSnMap = new HashMap<>();

    private static Map<ReportSubType, String> seriesSnMap = new HashMap<>();

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final QCReportNoInterface qcReportNoService;

    private final QCReportRepository qcReportRepository;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final TodoTaskBaseInterface todoTaskBaseService;

    private final UploadFeignAPI uploadFeignAPI;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final ReportConfigRepository reportConfigRepository;

    private final ExInspActInstRelationRepository exInspActInstRelationRepository;


    /**
     * 构造方法。
     */
    @Autowired

    public QCReportService(
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmRuTaskRepository ruTaskRepository,
        QCReportNoInterface qcReportNoService,
        QCReportRepository qcReportRepository, BpmEntityDocsMaterialsRepository docsMaterialsRepository,
        TodoTaskBaseInterface todoTaskBaseService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        TodoTaskDispatchInterface todoTaskDispatchService,
        ReportConfigRepository reportConfigRepository,
        StringRedisTemplate stringRedisTemplate,
        ExInspActInstRelationRepository exInspActInstRelationRepository) {
        super(stringRedisTemplate);
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.qcReportNoService = qcReportNoService;
        this.qcReportRepository = qcReportRepository;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.todoTaskBaseService = todoTaskBaseService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.reportConfigRepository = reportConfigRepository;
        this.exInspActInstRelationRepository = exInspActInstRelationRepository;
    }


    public synchronized Integer getReportSn(Long orgId, Long projectId) {

        Integer reportSn = null;

        if (reportSnMap.get(projectId) == null) {

            reportSn = qcReportRepository.getReportSnByProjectId(projectId);
            if (reportSn == null) reportSn = 0;
            reportSnMap.put(projectId, reportSn);
        }

        reportSn = reportSnMap.get(projectId) + 1;
        reportSnMap.put(projectId, reportSn);

        return reportSn;
    }

    /**
     * 生成外检报告
     *
     * @param orgId              组织ID
     * @param projectId          项目ID
     * @param bpmProcess         工序
     * @param operator           操作者
     * @param location           场地
     * @param taskIds            任务Ids
     * @param actInstList        流程列表
     * @param inspectionContents 检验内容
     */
    @Override
    public QCReport generateReportCover(Long orgId,
                                        Long projectId,
                                        BpmProcess bpmProcess,
                                        List<Long> assignees,
                                        List<String> userNames,
                                        OperatorDTO operator,
                                        String location,
                                        Set<String> entityNos,
                                        List<Long> taskIds,
                                        Set<Long> actInstIds,
                                        List<BpmActivityInstanceReport> actInstList,
                                        String inspectionContents,
                                        List<InspectParty> inspectParties,
                                        String projectName,
                                        Date inspectionTime,
                                        ReportConfig reportConfig,
                                        Long scheduleId) {

        String reportCoverGenClazzStr = reportConfig.getReportGenerateClass();

        Class clazz = null;
        try {
            clazz = Class.forName(reportCoverGenClazzStr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ReportGenerateInterface reportGenerateService = (ReportGenerateInterface) SpringUtils.getBean(clazz);

        String process = bpmProcess.getNameEn();
        String processStage = bpmProcess.getProcessStage().getNameEn();
        String ndeType = "";
        ReportHistory excelCover = null;
        ReportHistory pdfCover = null;
        if (actInstList.get(0).getNdeType() != null) {
            ndeType = actInstList.get(0).getNdeType().name();
        }

        InspectionReportPostDTO inspReportCoverDto = new InspectionReportPostDTO();

        Boolean isOneOffReport = bpmProcess.getOneOffReport();
        Integer reportNum = 0;
        try {
            inspReportCoverDto.setInspectParties(inspectParties);

            inspReportCoverDto.setSerialNo(String.valueOf(reportNum));

            String reportNo = projectName + "-REPORT_COVER";
            String moduleName = actInstList.get(0).getEntityModuleName();

            String reportQrCode = QrcodePrefixType.EXTERNAL_CONTROL_REPORT.getCode() + StringUtils.generateShortUuid();

            inspReportCoverDto.setReportName(reportNo);
            inspReportCoverDto.setReportNo(reportNo);
            inspReportCoverDto.setDate(inspectionTime);
            inspReportCoverDto.setReportQrCode(reportQrCode);

            Map<String, Object> reportMetaData = new HashMap<>();
            reportMetaData.put("date", new Date());
            reportMetaData.put("applyDate", inspectionTime);
            reportMetaData.put("location", location);
            reportMetaData.put("assignee", LongUtils.join(assignees));
            reportMetaData.put("assigneeName", String.join(",", userNames));
            reportMetaData.put("process", process);
            reportMetaData.put("ndeType", ndeType);
            reportMetaData.put("moduleName", moduleName);


            inspReportCoverDto.setExportType(ReportExportType.MS_EXCEL);
            excelCover = reportGenerateService.generateReportCover(orgId, projectId, inspectionContents, inspReportCoverDto, reportMetaData, actInstList);

            inspReportCoverDto.setExportType(ReportExportType.PDF);
            pdfCover = reportGenerateService.generateReportCover(orgId, projectId, inspectionContents, inspReportCoverDto, reportMetaData, actInstList);


        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        if (pdfCover == null || excelCover == null) {
            return null;
        }

        QCReport qcReport = new QCReport();

        qcReport.setProcess(process);
        qcReport.setProcessStage(processStage);
        qcReport.setOperator(operator.getId());
        qcReport.setOperatorName(operator.getName());
        qcReport.setJsonEntityNos(new ArrayList<>(entityNos));
        qcReport.setQrcode(pdfCover.getReportQrCode());
        qcReport.setExcelReportFileId(pdfCover.getFileId());
        qcReport.setPdfReportFileId(pdfCover.getFileId());
        qcReport.setPdfReportPageCount(PdfUtils.getPdfPageCount(protectedDir + pdfCover.getFilePath().substring(1)));
        if (bpmProcess.getNameEn().equals("FITUP") || bpmProcess.getNameEn().equals("WELD")) {
            qcReport.setNdeType("VT");
        } else if (actInstList.get(0).isPaut()) {
            qcReport.setNdeType("PAUT");
        } else if (bpmProcess.getNameEn().equals("ST02_REPORT")) {
            qcReport.setNdeType("ST02");
        } else if (bpmProcess.getNameEn().equals("CS02_REPORT")) {
            qcReport.setNdeType("CS02");
        } else {
            qcReport.setNdeType(ndeType);
        }
        qcReport.setReportNo(inspReportCoverDto.getReportNo());
        qcReport.setReportStatus(ReportStatus.INIT);
        qcReport.setCover(true);
        qcReport.setOneOffReport(isOneOffReport);
        qcReport.setJsonInspectParties(inspectParties.stream().map(Enum::name).collect(Collectors.toList()));
        qcReport.setInspectType(bpmProcess.getInspectType());
        qcReport.setReportType(reportConfig.getReportType());
        qcReport.setReportSubType(reportConfig.getReportSubType());

        qcReport.setCover(false);
        qcReport.setJsonInspectParties(inspectParties.stream().map(Enum::name).collect(Collectors.toList()));
        qcReport.setInspectType(bpmProcess.getInspectType());
        qcReport.setOneOffReport(bpmProcess.getOneOffReport());
        qcReport.setInspectType(bpmProcess.getInspectType());
        qcReport.setStatus(EntityStatus.ACTIVE);
        qcReport.setSeriesNo(String.valueOf(reportNum));
        qcReport.setSeriesNum(reportNum);
        qcReport.setProjectId(projectId);
        qcReport.setOrgId(orgId);
        qcReport.setScheduleId(scheduleId);
        qcReport.setInspectType(bpmProcess.getInspectType());



        return qcReport;
    }


    /**
     * 生成外检报告
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param bpmProcess  工序
     * @param operator    操作者
     * @param location    场地
     * @param taskIds     任务Ids
     * @param actInstList 流程列表
     */

    @Override
    public QCReport generateReport(Long orgId,
                                   Long projectId,
                                   BpmProcess bpmProcess,
                                   OperatorDTO operator,
                                   ContextDTO contextDTO,
                                   String location,
                                   List<Long> taskIds,
                                   List<BpmActivityInstanceReport> actInstList,
                                   List<InspectParty> inspectParties,
                                   ReportConfig reportConfig,
                                   String projectName,
                                   Date inspectionTime,
                                   List<Long> assignees,
                                   List<String> userNames,
                                   Set<String> entityNos,
                                   Set<Long> actInstIds,
                                   Long scheduleId) {
        return generateReport(orgId,
            projectId,
            bpmProcess,
            operator,
            contextDTO,
            location,
            taskIds,
            actInstList,
            inspectParties,
            reportConfig,
            projectName,
            inspectionTime,
            assignees,
            userNames,
            entityNos,
            actInstIds,
            scheduleId,
            null,
            null);
    }

    @Override
    public QCReport generateReport(Long orgId,
                                   Long projectId,
                                   BpmProcess bpmProcess,
                                   OperatorDTO operator,
                                   ContextDTO contextDTO,
                                   String location,
                                   List<Long> taskIds,
                                   List<BpmActivityInstanceReport> actInstList,
                                   List<InspectParty> inspectParties,
                                   ReportConfig reportConfig,
                                   String projectName,
                                   Date inspectionTime,
                                   List<Long> assignees,
                                   List<String> userNames,
                                   Set<String> entityNos,
                                   Set<Long> actInstIds,
                                   Long scheduleId,
                                   Map<String, Integer> oldReportNumMap,
                                   ReportStatus reportStatus) {


        String clazzStr = reportConfig.getReportGenerateClass();

        Class clazz = null;
        try {
            clazz = Class.forName(clazzStr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        ReportGenerateInterface reportGenerateService = (ReportGenerateInterface) SpringUtils.getBean(clazz);

        String process = bpmProcess.getNameEn();
        ReportHistory xlsReport = null;
        ReportHistory pdfReport = null;
        InspectionReportPostDTO inspReportDto = new InspectionReportPostDTO();
        NDEType ndtType = actInstList.get(0).getNdeType();
        String ndeType;
        String processType;

        if (ndtType == null) {
            ndeType = null;
        } else {
            ndeType = actInstList.get(0).getNdeType().name();
        }
        Integer reportNum = 0;
        String serialNo = "";
        String moduleName = "";
        String operatorName = "";
        String redisKey = null;

        processType = actInstList.get(0).getProcess();
        ReportSubType type = null;
        try {
            inspReportDto.setInspectParties(inspectParties);
            moduleName = actInstList.get(0).getEntityModuleName();
            type = reportConfig.getReportSubType();
            operatorName = actInstList.get(0).getCurrentExecutor();

            String reportNo = "";

            if (actInstList.get(0).getWeldRepairCount() == null || (actInstList.get(0).getWeldRepairCount() == 0)) {


                Integer n = 5;
                if (type.toString().contains("NT") || reportConfig.getProjectId().equals(1639120100344182841L)) {
                    n = 3;
                }

                String result = "";
                Boolean isRepeatNo = true;
                for(int i = 0; i < 5; i++) {
                    result = qcReportNoService.seriesNo(orgId, projectId, type, moduleName, n, projectName, i);
                    logger.info("Repeat Report NO, re-get report NO， SN is：" + result);

                    if (ReportTypeList.typeList.contains(type.toString())) {
                        redisKey = String.format(RedisKey.REPORT_SN.getDisplayName(),projectName, type, moduleName, result);

                    } else {
                        redisKey = String.format(RedisKey.REPORT_SN_GENERAL.getDisplayName(),projectName, type, result);
                    }
                    String redis = getRedisKey(redisKey);
                    if (redis == null || redis.equalsIgnoreCase("null")) {
                        isRepeatNo = false;
                        break;
                    }
                }

                if(isRepeatNo) {
                    throw new BusinessError("Repeate Report NO");
                }

                if (process.equals("ST02_REPORT")) {
                    reportNo = actInstList.get(0).getEntityNo() + "-ST02";
                } else if (process.equals("CS02_REPORT")) {
                    reportNo = actInstList.get(0).getEntityNo() + "-CS02";
                } else {
                    reportNo = qcReportNoService.reportNo(operatorName, type, moduleName, result, projectName, reportConfig);
                }

                reportNum = Integer.valueOf(result);


            } else if (actInstList.get(0).getWeldRepairCount() != 0) {

                if (actInstList.get(0).getOldReportNo() != null) {
                    String revisonNo = "R" + actInstList.get(0).getWeldRepairCount().toString();
                    String searchReportNo = actInstList.get(0).getOldReportNo() + "-" + revisonNo;
                    List<QCReport> qcReportList = qcReportRepository.findByProjectIdAndProcessAndReportNoLikeAndReportStatusNot(projectId, process, "%" + searchReportNo + "%", ReportStatus.CANCEL);
                    List<QCReport> oldQcReportList = qcReportRepository.findByProjectIdAndProcessAndReportNoLikeAndReportStatusNot(projectId, process, actInstList.get(0).getOldReportNo(), ReportStatus.CANCEL);
                    if (qcReportList.size() == 0) {
                        reportNo = searchReportNo + "-1";
                        if (oldQcReportList.size() > 0) {
                            Integer serialNum = oldQcReportList.get(0).getSeriesNum();
                            String result = "";
                            String count = "" + serialNum;
                            Integer n = 5;
                            if (type.toString().contains("-NT-")) {
                                n = 3;
                            }
                            if (count.length() <= n) {
                                for (int i = 0; i < n - count.length(); i++) {
                                    result += "0";
                                }
                            }
                            result += count;
                            reportNum = Integer.valueOf(result);
                        }
                    } else {
                        Integer serialNum = qcReportList.get(0).getSeriesNum();
                        String result = "";
                        String count = "" + serialNum;
                        Integer n = 5;
                        if (type.toString().contains("-NT-")) {
                            n = 3;
                        }
                        if (count.length() <= n) {
                            for (int i = 0; i < n - count.length(); i++) {
                                result += "0";
                            }
                        }
                        result += count;
                        reportNum = Integer.valueOf(result);
                        Integer reportNoCount = Integer.valueOf(qcReportList.size()) + 1;
                        reportNo = searchReportNo + "-" + reportNoCount;
                    }
                }

//                String entityNo = actInstList.get(0).getEntityNo().substring(0, actInstList.get(0).getEntityNo().lastIndexOf("-"));
//                entityNo = "%" + entityNo + "%";
//                List<QCReport> qcReportList = qcReportRepository.findByProcessAndStatusAndReportStatusNotAndEntityNosLike(
//                    processType,
//                    EntityStatus.ACTIVE,
//                    ReportStatus.CANCEL,
//                    entityNo
//                );
//
//                if (qcReportList != null && qcReportList.size() != 0) {
//                    QCReport qcReport = qcReportList.get(qcReportList.size() - 1);
//                    if (qcReport != null) {
//                        Integer serialNum = qcReport.getSeriesNum();
//
//                        String result = "";
//                        String count = "" + serialNum;
//                        Integer n = 5;
//                        if (type.toString().contains("-NT-")) {
//                            n = 3;
//                        }
//                        if (count.length() <= n) {
//                            for (int i = 0; i < n - count.length(); i++) {
//                                result += "0";
//                            }
//                        }
//                        result += count;
//                        reportNum = Integer.valueOf(result);
//                        String revisonNo = "R" + actInstList.get(0).getWeldRepairCount().toString();
//                        if (qcReport.getReportNo().contains("-R")) {
//                            String newReportNo = qcReport.getReportNo().substring(0, qcReport.getReportNo().lastIndexOf("-"));
//                            reportNo = newReportNo + "-" + revisonNo;
//                        } else {
//                            reportNo = qcReport.getReportNo() + "-" + revisonNo;
//                        }
//                    }
//                } else {
//                    Integer n = 5;
//                    if (type.toString().contains("-NT-")) {
//                        n = 3;
//                    }
//
//                    String result = "";
//                    Boolean isRepeatNo = true;
//                    for(int i = 0; i < 5; i++) {
//                        result = qcReportNoService.seriesNo(orgId, projectId, type, moduleName, n, projectName, i);
//                        logger.info("Repeat Report NO, re-get report NO, result: " + result);
//
//                        if (ReportTypeList.typeList.contains(type.toString())) {
//                            redisKey = String.format(RedisKey.REPORT_SN.getDisplayName(),projectName, type, moduleName, result);
//                        } else {
//                            redisKey = String.format(RedisKey.REPORT_SN_GENERAL.getDisplayName(),projectName, type, result);
//                        }
//                        String redis = getRedisKey(redisKey);
//                        if (redis == null || redis.equalsIgnoreCase("null")) {
//                            isRepeatNo = false;
//                            break;
//                        }
//                    }
//
//                    if(isRepeatNo) {
//                        throw new BusinessError("NDT Repeate Report NO");
//                    }
//
//                    reportNo = qcReportNoService.reportNo(operatorName, type, moduleName, result, projectName, reportConfig);
//                    reportNum = Integer.valueOf(result);
//                }

            }


            serialNo = qcReportNoService.getReportSn(orgId, projectId);


            String reportQrCode = QrcodePrefixType.EXTERNAL_CONTROL_REPORT.getCode() + StringUtils.generateShortUuid();

            inspReportDto.setSerialNo(serialNo);
            inspReportDto.setReportName(reportNo);
            inspReportDto.setReportNo(reportNo);
            inspReportDto.setDate(inspectionTime);
            inspReportDto.setReportQrCode(reportQrCode);

            inspReportDto.setExportType(ReportExportType.MS_EXCEL);
            Map<String, Object> reportMetaData = new HashMap<>();
            reportMetaData.put("date", new Date());
            reportMetaData.put("applyDate", inspectionTime);
            reportMetaData.put("location", location);
            reportMetaData.put("assignee", LongUtils.join(assignees));
            reportMetaData.put("assigneeName", String.join(",", userNames));
            reportMetaData.put("process", process);
            reportMetaData.put("ndeType", ndeType);
            reportMetaData.put("moduleName", moduleName);


            logger.info("已生成流水号" + serialNo);
            actInstList.sort(Comparator.comparing(BpmActivityInstanceReport::getEntityNo));

            logger.info("已重新排序");

            // 将此次的任务id放入已申请外检的集合中
            for (BpmActivityInstanceReport actInst : actInstList) {
                if (sIsMember(String.format(RedisKey.REPORT_RUNNING_LIST.getDisplayName(), projectId.toString(), type.toString()), actInst.getId().toString())) {
                    throw new BusinessError(actInst.getEntityNo() + "已经在申请！");
                } else {
                    // 针对UT_MT多份报告需要一次性加进来
                    if (process.equals("UT_MT")) {
                        sadd(String.format(RedisKey.REPORT_RUNNING_LIST.getDisplayName(), projectId.toString(), ReportSubType.STRUCTURE_Ultrasonic.toString()), actInst.getId().toString());
                        sadd(String.format(RedisKey.REPORT_RUNNING_LIST.getDisplayName(), projectId.toString(), ReportSubType.STRUCTURE_Magnetic_Particle.toString()), actInst.getId().toString());
                    } else {
                        sadd(String.format(RedisKey.REPORT_RUNNING_LIST.getDisplayName(), projectId.toString(), type.toString()), actInst.getId().toString());
                    }
                }
            }

            xlsReport = reportGenerateService.generateReport(
                orgId,
                projectId,
                taskIds.toArray(new String[taskIds.size()]),
                actInstList,
                inspReportDto,
                reportMetaData,
                operator,
                contextDTO);

            inspReportDto.setExportType(ReportExportType.PDF);

            pdfReport = reportGenerateService.generateReport(
                orgId,
                projectId,
                taskIds.toArray(new String[taskIds.size()]),
                actInstList,
                inspReportDto,
                reportMetaData,
                operator,
                contextDTO);

            // 移除放进redis中的进行中任务
            for (BpmActivityInstanceReport actInst : actInstList) {
                if (actInst.getProcess().equals("UT_MT")) {
                    // 针对第一个报告完成后将第二个报告移出redis
                    if (type.toString().equals(ReportSubType.STRUCTURE_Magnetic_Particle.toString())) {
                        srem(String.format(RedisKey.REPORT_RUNNING_LIST.getDisplayName(), projectId.toString(), ReportSubType.STRUCTURE_Ultrasonic.toString()), actInst.getId().toString());
                    } else {
                        srem(String.format(RedisKey.REPORT_RUNNING_LIST.getDisplayName(), projectId.toString(), ReportSubType.STRUCTURE_Magnetic_Particle.toString()), actInst.getId().toString());
                    }
                }

            }
        } catch (Exception e) {
            // 移除放进redis中的进行中任务
            for (BpmActivityInstanceReport actInst : actInstList) {
                srem(String.format(RedisKey.REPORT_RUNNING_LIST.getDisplayName(), projectId.toString(), type.toString()), actInst.getId().toString());
            }
            logger.error(e.toString());
            QCReport errorQcReport = new QCReport();
            errorQcReport.setReportGenerateError(e.toString());
            return errorQcReport;

        }

        if (pdfReport == null || xlsReport == null) {
            return null;
        }

        QCReport qcReport = new QCReport();

        qcReport.setProcess(bpmProcess.getNameEn());
        qcReport.setProcessStage(bpmProcess.getProcessStage().getNameEn());
        qcReport.setQrcode(inspReportDto.getReportQrCode());
        qcReport.setExcelReportFileId(xlsReport.getFileId());
        qcReport.setExcelReportFilePath(xlsReport.getFilePath());
        qcReport.setExcelReportFileName(xlsReport.getReportNo());
        qcReport.setJsonEntityNos(new ArrayList<>(entityNos));
        qcReport.setPdfReportFileId(pdfReport.getFileId());
        qcReport.setPdfReportFileName(pdfReport.getReportNo());
        qcReport.setPdfReportFilePath(pdfReport.getFilePath());
        qcReport.setPdfReportPageCount(PdfUtils.getPdfPageCount(protectedDir + pdfReport.getFilePath().substring(1)));
        if (bpmProcess.getNameEn().equals("FITUP") || bpmProcess.getNameEn().equals("WELD")) {
            qcReport.setNdeType("VT");
        } else if (actInstList.get(0).isPaut()) {
            qcReport.setNdeType("PAUT");
        } else if (bpmProcess.getNameEn().equals("ST02_REPORT")) {
            qcReport.setNdeType("ST02");
        } else if (bpmProcess.getNameEn().equals("CS02_REPORT")) {
            qcReport.setNdeType("CS02");
        } else {
            qcReport.setNdeType(ndeType);
        }
        qcReport.setModuleName(moduleName);
        qcReport.setReportNo(inspReportDto.getReportNo());
        qcReport.setOperator(operator.getId());
        qcReport.setOperatorName(operator.getName());
        if (reportStatus == null) {
            qcReport.setReportStatus(ReportStatus.INIT);
        } else {
            qcReport.setReportStatus(reportStatus);
        }
        qcReport.setCover(false);
        qcReport.setParentEntityId(actInstList.get(0).getEntityId());
        qcReport.setJsonInspectParties(inspectParties.stream().map(Enum::name).collect(Collectors.toList()));
        qcReport.setInspectType(bpmProcess.getInspectType());
        qcReport.setReportType(reportConfig.getReportType());
        qcReport.setReportSubType(reportConfig.getReportSubType());
        qcReport.setOneOffReport(bpmProcess.getOneOffReport());
        qcReport.setInspectType(bpmProcess.getInspectType());
        qcReport.setStatus(EntityStatus.ACTIVE);
        qcReport.setSeriesNo(serialNo);
        qcReport.setSeriesNum(reportNum);
        qcReport.setProjectId(projectId);
        qcReport.setOrgId(orgId);
        qcReport.setApplyDate(inspectionTime);
        logger.info("QCReportService->" + "开始更新报告 exInspScheduleId " + scheduleId + "->" + new Date());
        qcReport.setScheduleId(scheduleId);
        qcReport.setInspectType(bpmProcess.getInspectType());
        logger.info("QCReportService->" + "更新后报告 exInspScheduleId " + scheduleId + "->" + new Date());

        qcReportRepository.save(qcReport);
        return qcReport;
    }


    @Override
    public Page<QCReport> getList(Long orgId, Long projectId, PageDTO page, QCReportCriteriaDTO criteriaDTO) {
        return qcReportRepository.getList(orgId, projectId, page.toPageable(), criteriaDTO);
    }

    @Override
    public void uploadReport(ContextDTO contextDTO,
                             Long orgId,
                             Long projectId,
                             ReportUploadDTO uploadDTO,
                             OperatorDTO operatorDTO) {

        String temporaryFileName = uploadDTO.getFileName();

        File diskFileTemp = new File(temporaryDir, temporaryFileName);
        if (!diskFileTemp.exists()) {

            throw new NotFoundError();
        }

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFileTemp, FileMetadataDTO.class);
        if (!metadata.getFilename().endsWith(".pdf")) {
            throw new NotFoundError();
        }

        logger.error("QC1 保存docs服务->开始");
        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), temporaryFileName,
            new FilePostDTO());
        logger.error("QC1 保存docs服务->结束");
        FileES fileES = fileESResBody.getData();
        boolean found = false;
        try {
            List<String> imageFiles = PdfUtils.parsePdfImage(protectedDir + fileES.getPath().substring(1), temporaryDir);
            for (String imageFile : imageFiles) {
                String qrcode = QRCodeUtils.decodeQrcodeFromImage(imageFile);

                QCReport report = qcReportRepository.findByQrcodeAndReportStatus(qrcode, ReportStatus.INIT);
                if (report != null && report.getScheduleId() != null) {
                    Long scheduleId = report.getScheduleId();
                    found = true;
                    report.setReportStatus(ReportStatus.DONE);
                    report.setUploadFileId(LongUtils.parseLong(fileES.getId()));
                    report.setUploadFilePath(fileES.getPath());
                    report.setUploadReportPageCount(PdfUtils.getPdfPageCount(protectedDir + fileES.getPath().substring(1)));
                    qcReportRepository.save(report);

                    List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(
                        orgId, projectId, scheduleId
                    );

                    Set<Long> taskIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getTaskId() != null).
                        map(TaskProcQLDTO::getTaskId).collect(Collectors.toSet());

                    Set<Long> actInstIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getActInstId() != null).
                        map(TaskProcQLDTO::getActInstId).collect(Collectors.toSet());

                    for (Long taskId : taskIds) {

                        BpmRuTask ruTask = ruTaskRepository.findById(taskId).orElse(null);
                        BpmActivityInstanceBase actInst = bpmActInstRepository.findById(ruTask.getActInstId()).orElse(new BpmActivityInstanceBase());

                        List<ActReportDTO> reportDTOs = new ArrayList<>();
                        ActReportDTO reportDTO = new ActReportDTO();
                        reportDTO.setFileId(
                            LongUtils.parseLong(
                                fileES.getId()
                            ));
                        reportDTO.setReportQrCode(report.getQrcode());
                        reportDTOs.add(reportDTO);
                        ruTask.setJsonDocuments(reportDTOs);
                        ruTaskRepository.save(ruTask);

                        TodoTaskExecuteDTO toDoTaskDTO = new TodoTaskExecuteDTO();
                        todoTaskDispatchService.exec(contextDTO, orgId, projectId, taskId, toDoTaskDTO, operatorDTO);

                        BpmEntityDocsMaterials bpmDoc = new BpmEntityDocsMaterials();
                        bpmDoc.setProjectId(projectId);
                        bpmDoc.setCreatedAt();
                        bpmDoc.setProcessId(actInst.getProcessId());
                        bpmDoc.setEntityNo(actInst.getEntityNo());
                        bpmDoc.setEntityId(actInst.getEntityId());
                        bpmDoc.setActInstanceId(actInst.getId());
                        bpmDoc.setStatus(EntityStatus.ACTIVE);
                        bpmDoc.setType(ActInstDocType.EXTERNAL_INSPECTION);
                        bpmDoc.setOperator(operatorDTO.getId());
                        todoTaskBaseService.saveReportFromDocuments(bpmDoc, actInst.getId(), ruTask.getId());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        if (!found) {
            throw new NotFoundError();
        }
    }

    @Override
    public void patchUploadReport(
        ContextDTO context,
        Long orgId,
        Long projectId,
        Long reportId,
        ReportUploadDTO uploadDTO
    ) {
        String temporaryFileName = uploadDTO.getFileName();

        File diskFileTemp = new File(temporaryDir, temporaryFileName);
        if (!diskFileTemp.exists()) {
            throw new BusinessError();
        }

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFileTemp, FileMetadataDTO.class);
        if (!metadata.getFilename().endsWith(".pdf")) {
            throw new BusinessError();
        }

        logger.error("QC2 保存docs服务->开始");
        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), temporaryFileName,
            new FilePostDTO());
        logger.error("QC2 保存docs服务->结束");
        FileES fileES = fileESResBody.getData();

        QCReport report = qcReportRepository.findById(reportId).orElse(null);
        if (report == null || report.getScheduleId() == null) {
            throw new BusinessError();
        }

        Long scheduleId = report.getScheduleId();
        List<ActReportDTO> reportDtos = new ArrayList<>();
        ActReportDTO reportDTO = new ActReportDTO();
        reportDTO.setFileId(
            LongUtils.parseLong(
                fileES.getId()
            ));
        reportDTO.setReportQrCode(report.getQrcode());
        reportDtos.add(reportDTO);

        report.setReportStatus(ReportStatus.DONE);
        report.setUploadFileId(LongUtils.parseLong(fileES.getId()));
        report.setUploadFilePath(fileES.getPath());
        report.setUploadReportPageCount(PdfUtils.getPdfPageCount(protectedDir + fileES.getPath().substring(1)));
        report.setMemo(uploadDTO.getComment());
        qcReportRepository.save(report);

        List<TaskProcQLDTO> taskProcIds = exInspActInstRelationRepository.findTaskIdByOrgIdAndProjectIdAndExInspScheduleId(
            orgId, projectId, scheduleId
        );

        Set<Long> actInstIds = taskProcIds.stream().filter(taskProcId -> taskProcId.getActInstId() != null).
            map(TaskProcQLDTO::getActInstId).collect(Collectors.toSet());

        for (Long actInstId : actInstIds) {
            BpmActivityInstanceBase actInst = bpmActInstRepository.findById(actInstId).orElse(null);
            if (actInst == null) {
                continue;
            }

            Set<ActInstDocType> docTypes = new HashSet<>();
            docTypes.add(ActInstDocType.EXTERNAL_INSPECTION);
            docTypes.add(ActInstDocType.EXTERNAL_INSPECTION_RESIGN);
            BpmEntityDocsMaterials bpmDoc = docsMaterialsRepository.findByProcessIdAndEntityIdAndActInstanceIdAndTypeIn(
                actInst.getProcessId(), actInst.getEntityId(), actInst.getId(), docTypes);

            if (bpmDoc == null) {
                bpmDoc = new BpmEntityDocsMaterials();
            }
            bpmDoc.setProjectId(actInst.getProjectId());
            bpmDoc.setCreatedAt();
            bpmDoc.setProcessId(actInst.getProcessId());
            bpmDoc.setEntityNo(actInst.getEntityNo());
            bpmDoc.setEntityId(actInst.getEntityId());
            bpmDoc.setActInstanceId(actInst.getId());
            bpmDoc.setStatus(EntityStatus.ACTIVE);
            bpmDoc.setType(ActInstDocType.EXTERNAL_INSPECTION);
            bpmDoc.setOperator(context.getOperator().getId());
            bpmDoc.setJsonDocs(reportDtos);
            bpmDoc.setExternalInspectionFinal(true);
            bpmDoc.setExternalInspectionResult("A");
            if (report.getInspectResult() == InspectResult.B) {
                bpmDoc.setType(ActInstDocType.EXTERNAL_INSPECTION_RESIGN);
            } else if (report.getInspectResult() == InspectResult.A) {

                bpmDoc.setExternalInspectionResult(report.getInspectResult().name());
                bpmDoc.setType(ActInstDocType.EXTERNAL_INSPECTION);
            } else if (report.getInspectResult() == InspectResult.C) {
                bpmDoc.setType(ActInstDocType.EXTERNAL_INSPECTION);
            }
            if (
                report.getInspectResult() == InspectResult.B
                    || report.getInspectResult() == InspectResult.C
            ) {
                bpmDoc.setExternalInspectionFinal(false);
            }


            docsMaterialsRepository.save(bpmDoc);

        }


    }

    /**
     * 取得 工序上生成报告的设置
     *
     * @param bpmProcess
     * @param subReportInfoMap reportType -> subReportType
     */
    @Override
    public Map<String, List<ReportConfig>> getReportConfig(
        BpmProcess bpmProcess,
        Map<String, String> subReportInfoMap
    ) {
        List<ReportConfig> rcs = reportConfigRepository.findByOrgIdAndProjectIdAndProcessIdAndStatus(
            bpmProcess.getOrgId(),
            bpmProcess.getProjectId(),
            bpmProcess.getId(),
            EntityStatus.ACTIVE);


        List<ReportConfig> coverReportConfigs = new ArrayList<>();


        List<ReportConfig> reportConfigs = new ArrayList<>();

        if (!CollectionUtils.isEmpty(rcs)) {

            bpmProcess.setReportConfigs(rcs);
            List<ReportConfig> allReportConfigs = rcs;
            if (MapUtils.isEmpty(subReportInfoMap)) {
                reportConfigs = allReportConfigs.stream().filter(reportConfig -> !reportConfig.getCover()).distinct().collect(Collectors.toList());
                coverReportConfigs = allReportConfigs.stream().filter(ReportConfig::getCover).distinct().collect(Collectors.toList());

            } else {

                for (ReportConfig reportConfig : allReportConfigs) {
                    if (reportConfig.getCover() != null && reportConfig.getCover()) {
                        continue;
                    }
                    String subReportType = reportConfig.getReportSubType() == null ? null : reportConfig.getReportSubType().name();
                    if (subReportInfoMap.get(subReportType) != null &&
                        subReportInfoMap.get(subReportType).equalsIgnoreCase(reportConfig.getTemplateName())) {
                        reportConfigs.add(reportConfig);
                    }

                }


                for (ReportConfig reportConfig : allReportConfigs) {
                    if (reportConfig.getCover() == null || !reportConfig.getCover()) {
                        continue;
                    }
                    String subReportType = reportConfig.getReportSubType() == null ? null : reportConfig.getReportSubType().name();
                    if (subReportInfoMap.get(subReportType) != null &&
                        subReportInfoMap.get(subReportType).equalsIgnoreCase(reportConfig.getTemplateName())) {
                        coverReportConfigs.add(reportConfig);
                    }
                }
            }
        }

        Map<String, List<ReportConfig>> reportConfigMap = new HashMap<>();
        reportConfigMap.put("coverReportConfigs", coverReportConfigs);
        reportConfigMap.put("reportConfigs", reportConfigs);

        return reportConfigMap;

    }

    /**
     * 子图纸zip打包。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param operatorDTO 操作人员
     */
    @Override
    public void startZip(Long orgId,
                         Long projectId,
                         QCReportPackageDTO qcReportPackageDTO,
                         OperatorDTO operatorDTO,
                         ContextDTO context
    ) {

        Long parentId = Long.valueOf(qcReportPackageDTO.getParentEntityId());
        Optional<QCReport> qcReportOptional = qcReportRepository.findById(qcReportPackageDTO.getQcReportId());
        QCReport qcReport = new QCReport();
        if (qcReportOptional.isPresent()) {
            qcReport = qcReportOptional.get();
        }

        List<String> stProcesses = new ArrayList<>();
        stProcesses.add("FITUP");
        stProcesses.add("WELD");

        List<String> csProcesses = new ArrayList<>();
        csProcesses.add("FITUP");
        csProcesses.add("WELD");
        csProcesses.add("RT");
        csProcesses.add("UT");
        csProcesses.add("MT");
        csProcesses.add("PT");



        if (parentId != null) {
            String parentNo = "";
//            if (wp02EntityOptional.isPresent()) {
//                parentNo = wp02EntityOptional.get().getNo();
//            } else if (wp03EntityOptional.isPresent()) {
//                parentNo = wp03EntityOptional.get().getNo();
//            } else if (wp04EntityOptional.isPresent()) {
//                parentNo = wp04EntityOptional.get().getNo();
//            }
//
//
//            List<StructureWeldEntity> structureWeldEntities = structureWeldEntityRepository.findByProjectIdAndWorkPackageNoAndDeletedIsFalse(projectId, parentNo);
//            if (structureWeldEntities.size() <= 0) {
//                throw new BusinessError("该实体下未挂有焊口信息!");
//            }
//            List<QCReport> qcReports = new ArrayList<>();
//            for (StructureWeldEntity entity : structureWeldEntities) {
//                List<QCReport> qcReportList = new ArrayList<>();
//                if (qcReportPackageDTO.getProcess().equals("ST02_REPORT")) {
//                    qcReportList = qcReportRepository.findByProjectIdAndReportStatusNotAndEntityNosLike(projectId, ReportStatus.CANCEL, "%" + entity.getNo() + "%", stProcesses);
//                } else {
//                    qcReportList = qcReportRepository.findByProjectIdAndReportStatusNotAndEntityNosLike(projectId, ReportStatus.CANCEL, "%" + entity.getNo() + "%", csProcesses);
//                }
//
//                if (qcReportList.size() > 0) {
//                    qcReports.addAll(qcReportList);
//                }
//            }
//            if (qcReports.size() > 0) {
//                for (int i = 0; i < qcReports.size() - 1; i++) {
//                    for (int j = qcReports.size() - 1; j > i; j--) {
//                        if (qcReports.get(j).getId().equals(qcReports.get(i).getId())) {
//                            qcReports.remove(j);
//                        }
//                    }
//                }
//            }
//
//            if (qcReports.size() > 0) {
//
//                String savepath = temporaryDir + qcReportPackageDTO.getReportNo() + ".pdf";
//
//                int filecount = qcReports.size();
//                int listIndex = 0;
//                int index = 0;
//
//                String[] files = new String[filecount];
//
//                for (QCReport sub : qcReports) {
//
//                    if (sub.getUploadFilePath() != null) {
//                        String subFilePath = protectedDir + sub.getUploadFilePath().substring(1);
//                        files[index++] = subFilePath;
//                    } else {
//                        throw new BusinessError(sub.getReportNo() + "报告未回传！");
//                    }
//                }
//                try {
//                    PdfUtils.mergePdfFiles(files, savepath);
//
//                    File diskFile = new File(savepath);
//
//
//                    DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
//                        MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());
//
//                    IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());
//
//
//
//                    logger.error("图纸基础3 上传docs服务->开始");
//                    JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
//                        .uploadProjectDocumentFile(orgId.toString(), new CommonsMultipartFile(fileItem));
//                    logger.error("图纸基础3 上传docs服务->结束");
//                    logger.error("图纸基础3 保存docs服务->开始");
//                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
//                        tempFileResBody.getData().getName(), new FilePostDTO());
//                    logger.error("图纸基础3 保存docs服务->结束");
//
//                    if (fileESResBody != null && fileESResBody.getData() != null) {
//                        qcReport.setPackageReportFileId(LongUtils.parseLong(fileESResBody.getData().getId()));
//                        qcReport.setPackageReportFileName(fileESResBody.getData().getName());
//                        qcReport.setPackageReportFilePath(fileESResBody.getData().getPath());
//                        qcReportRepository.save(qcReport);
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//
//            }
        }
    }

}
