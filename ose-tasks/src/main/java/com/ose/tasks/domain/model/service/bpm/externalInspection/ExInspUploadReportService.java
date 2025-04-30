package com.ose.tasks.domain.model.service.bpm.externalInspection;

import com.ose.feign.RequestWrapper;
import com.ose.util.*;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.report.ExInspActInstHandleHistory;
import com.ose.tasks.vo.bpm.ExInspApplyHandleType;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.vo.BpmTaskType;
import com.ose.docs.api.FileFeignAPI;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.docs.entity.FileViewES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.bpm.*;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.report.QCReport;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.vo.qc.ReportStatus;
import com.ose.vo.EntityStatus;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.persistence.Tuple;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

/**
 * 报告上传服务。
 */
@Component
public class ExInspUploadReportService implements ExInspUploadReportInterface {

    private final static Logger logger = LoggerFactory.getLogger(ExInspUploadReportService.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private final BpmExInspUploadHistoryRepository bpmExInspUploadHistoryRepository;

    private final QCReportRepository qcReportRepository;

    private final ExInspReportInterface exInspReportService;

    private final BpmExInspConfirmRepository bpmExInspConfirmRepository;

    private final UploadFeignAPI uploadFeignAPI;

    private final FileFeignAPI fileFeignAPI;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final ExInspActInstHandleHistoryRepository exInspActInstHandleHistoryRepository;
    private final BpmExInspScheduleRepository bpmExInspScheduleRepository;

    private final BatchTaskInterface batchTaskService;
    private final ProjectInterface projectService;

    private final BpmRuTaskRepository bpmRuTaskRepository;

    /**
     * 构造方法。
     */
    @Autowired
    public ExInspUploadReportService(
        BpmExInspUploadHistoryRepository bpmExInspUploadHistoryRepository,
        QCReportRepository qcReportRepository,
        ExInspReportInterface exInspReportService,
        BpmExInspConfirmRepository bpmExInspConfirmRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") FileFeignAPI fileFeignAPI,
        TodoTaskDispatchInterface todoTaskDispatchService,
        ExInspActInstHandleHistoryRepository exInspActInstHandleHistoryRepository,
        BpmExInspScheduleRepository bpmExInspScheduleRepository,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService,
        BpmRuTaskRepository bpmRuTaskRepository) {
        this.exInspReportService = exInspReportService;
        this.bpmExInspUploadHistoryRepository = bpmExInspUploadHistoryRepository;
        this.qcReportRepository = qcReportRepository;
        this.bpmExInspConfirmRepository = bpmExInspConfirmRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.fileFeignAPI = fileFeignAPI;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.exInspActInstHandleHistoryRepository = exInspActInstHandleHistoryRepository;
        this.bpmExInspScheduleRepository = bpmExInspScheduleRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
    }


    /**
     * 外检报告上传
     */
    @Override
    public BpmExInspConfirmResponseDTO uploadReport(
        Long orgId,
        Long projectId,
        DrawingUploadDTO uploadDTO,
        OperatorDTO operatorDTO,
        boolean secondUpload,
        ContextDTO context
    ) {

        List<ExInspActInstHandleHistory> exInspActInstHandleHistories = exInspActInstHandleHistoryRepository.findByOrgIdAndProjectIdAndTypeAndRunningStatus(
            orgId,
            projectId,
            ExInspApplyHandleType.UPLOAD_REPORT,
            EntityStatus.ACTIVE
        );
        if (exInspActInstHandleHistories.size() > 9) {
            throw new BusinessError("当前项目存在正在上传的外检报告");
        }

        Optional<ExInspActInstHandleHistory> historyOptional = exInspActInstHandleHistoryRepository.findByProjectIdAndSeriesNoAndTypeAndRunningStatus(
            projectId,
            uploadDTO.getSeriesNo(),
            ExInspApplyHandleType.UPLOAD_REPORT,
            EntityStatus.ACTIVE
        );
        if (historyOptional.isPresent()) {
            throw new BusinessError("当前流水号正在回传中！！！");
        }


        ExInspActInstHandleHistory exInspActInstHandleHistory = new ExInspActInstHandleHistory();
        exInspActInstHandleHistory.setOrgId(orgId);
        exInspActInstHandleHistory.setProjectId(projectId);

        exInspActInstHandleHistory.setRunningStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setCreatedAt(new Date());
        exInspActInstHandleHistory.setCreatedBy(context.getOperator().getId());
        exInspActInstHandleHistory.setLastModifiedAt(new Date());
        exInspActInstHandleHistory.setLastModifiedBy(context.getOperator().getId());
        exInspActInstHandleHistory.setStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setRemarks("上传外检报告: " + uploadDTO.getFileName() + uploadDTO.getSeriesNo());
        exInspActInstHandleHistory.setType(ExInspApplyHandleType.UPLOAD_REPORT);
        exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);


        if (!context.isContextSet()) {
            String authorization = context.getAuthorization();
            String userAgent = context.getUserAgent();

            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                context.getResponse()
            );

            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }

        Project project = projectService.get(orgId, projectId);
        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.EXTERNAL_INSPECTION_APPLY,
            false,
            context,
            batchTask -> {

                try {
                    System.out.println("开始外检上传文件" + new Date());
                    String temporaryFileName = uploadDTO.getFileName();
                    Set<String> NDTs = new HashSet<>();
                    NDTs.add("MT");
                    NDTs.add("RT");
                    NDTs.add("PAUT");
                    NDTs.add("UT");
                    NDTs.add("PMI");
                    NDTs.add("PT");
                    NDTs.add("PWHT");
                    NDTs.add("UT_MT");

                    BpmExInspConfirmsDTO confirmDTO = new BpmExInspConfirmsDTO();


                    File diskFileTemp = new File(temporaryDir, temporaryFileName);
                    if (!diskFileTemp.exists()) {

                        throw new NotFoundError();
                    }


                    FileMetadataDTO metadata = FileUtils.readMetadata(diskFileTemp, FileMetadataDTO.class);
                    if (!metadata.getFilename().endsWith(".pdf")) {
                        throw new NotFoundError();
                    }
                    FilePostDTO filePostDTO = new FilePostDTO();
                    filePostDTO.setContext(context);


                    logger.error("外检上传 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.
                        save(orgId.toString(), projectId.toString(), temporaryFileName, filePostDTO);
                    logger.error("外检上传 保存docs服务->结束");

                    FileES fileES = fileESResBody.getData();
                    BpmExInspUploadHistory exInspUploadHistory = new BpmExInspUploadHistory();
                    exInspUploadHistory.setOrgId(orgId);
                    exInspUploadHistory.setProjectId(projectId);
                    exInspUploadHistory.setFilePath(fileES.getPath());
                    exInspUploadHistory.setOperator(operatorDTO.getId());
                    exInspUploadHistory.setOperatorName(operatorDTO.getName());
                    exInspUploadHistory.setFileId(LongUtils.parseLong(fileES.getId()));
                    exInspUploadHistory.setStatus(EntityStatus.ACTIVE);


                    if (secondUpload) {
                        exInspUploadHistory.setConfirmed(true);
                    } else {
                        exInspUploadHistory.setConfirmed(false);
                    }
                    exInspUploadHistory.setCreatedAt(new Date());
                    exInspUploadHistory.setLastModifiedAt(new Date());
                    exInspUploadHistory.setMemo(uploadDTO.getComment());
                    exInspUploadHistory.setFileName(metadata.getFilename());
                    bpmExInspUploadHistoryRepository.save(exInspUploadHistory);
                    List<Long> confirmEntityIds = new ArrayList<>();
                    List<BpmExInspConfirm> exInspUploadFileConfirms = new ArrayList<>();
                    Map<Long, QCReport> reports = new HashMap<>();
                    Map<Long, Long> reportFileIdMap = new HashMap<>();


                    if (uploadDTO.getSeriesNo() != null && !uploadDTO.getSeriesNo().equals("")) {


                        QCReport report = qcReportRepository.findByProjectIdAndSeriesNo(projectId, uploadDTO.getSeriesNo());
                        if (report == null) {
                            throw new BusinessError("CAN NOT FOUND qc REPORT");
                        }
//                        if (report.getReportStatus() == ReportStatus.INIT && !NDTs.contains(report.getProcess()) && !(projectId.equals(1595407955858982139L) || projectId.equals(1616469247194450532L))) {
//                            bpmExInspUploadHistoryRepository.delete(exInspUploadHistory);
//                            throw new BusinessError("PLEASE SEND EX INSP MAIL");
//                        }

                        if (report.getScheduleId() == null) {

                        } else {
                            BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(report.getScheduleId()).orElse(null);
                            if (bpmExInspSchedule == null) {
                                throw new BusinessError("报告不存在 schedule");
                            }
                            if (bpmExInspSchedule.getRunningStatus() != null && bpmExInspSchedule.getRunningStatus().equals(EntityStatus.ACTIVE)) {
                                throw new BusinessError("当前报告正在处理");
                            }
                            bpmExInspSchedule.setRunningStatus(EntityStatus.ACTIVE);
                            bpmExInspScheduleRepository.save(bpmExInspSchedule);
                        }


                        exInspActInstHandleHistory.setReportNo(report.getReportNo());
                        exInspActInstHandleHistory.setSeriesNo(report.getSeriesNo());
                        exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);

                        ActReportDTO reportDTO = new ActReportDTO();
                        reportDTO.setFileId(LongUtils.parseLong(fileES.getId()));
                        reportDTO.setReportQrCode(report == null ? null : report.getQrcode());
                        reportDTO.setFilePath(fileES.getPath());
                        reportDTO.setProcess(report.getProcess());


                        if (report != null && !secondUpload) {


                            logger.error("上传报告1 调用docs取得详情服务->开始");
                            FileViewES initReport = fileFeignAPI
                                .getFileInfo(orgId.toString(), LongUtils.toString(report.getPdfReportFileId()))
                                .getData();
                            logger.error("上传报告1 调用docs取得详情服务->结束");
                            BpmExInspConfirm exInspConfirm = new BpmExInspConfirm();
                            exInspConfirm.setCreatedAt();
                            exInspConfirm.setLastModifiedAt();
                            exInspConfirm.setOperator(operatorDTO.getId());
                            exInspConfirm.setOrgId(orgId);
                            exInspConfirm.setProjectId(projectId);
                            exInspConfirm.setQrcode(report.getQrcode());
                            exInspConfirm.setReportNo(report.getReportNo());
                            exInspConfirm.setSeriesNo(uploadDTO.getSeriesNo());
                            exInspConfirm.setPageCount(PdfUtils.getPdfPageCount(protectedDir + fileES.getPath().substring(1)));
                            exInspConfirm.setInitPageCount(PdfUtils.getPdfPageCount(protectedDir + initReport.getPath().substring(1)));
                            exInspConfirm.setJsonReport(reportDTO);
                            exInspConfirm.setUploadHistoryId(exInspUploadHistory.getId());
                            exInspConfirm.setStatus(getReportStatus(report));
                            exInspConfirm.setUploadFileId(fileES.getId());
                            exInspConfirm.setSecondUpload(secondUpload);
                            exInspConfirm.setUploaded(true);
                            bpmExInspConfirmRepository.save(exInspConfirm);
                            reportFileIdMap.put(exInspConfirm.getId(), LongUtils.parseLong(fileES.getId()));

                            if (exInspConfirm.getPageCount() == exInspConfirm.getInitPageCount()) {
                                confirmEntityIds.add(exInspConfirm.getId());
                                exInspUploadFileConfirms.add(exInspConfirm);
                                confirmDTO.setConfirmIdList(confirmEntityIds);
                                confirmDTO.setExInspUploadConfirms(exInspUploadFileConfirms);

                                exInspConfirm.setStatus(getReportStatus(report));

                                confirmDTO.setStatus(EntityStatus.APPROVED);
                                reports.put(exInspConfirm.getId(), report);
                                confirmDTO.setReports(reports);
                                confirmDTO.setTaskType(uploadDTO.getTaskType());
                            }
                        } else if (report != null && secondUpload) {

                            List<BpmExInspConfirm> exInspConfirms = bpmExInspConfirmRepository.findByOrgIdAndProjectIdAndQrcode(orgId, projectId, report.getQrcode());
                            BpmExInspConfirm exInspConfirm = null;
                            if (exInspConfirms != null && !exInspConfirms.isEmpty()) {
                                exInspConfirm = exInspConfirms.get(0);
                            } else {

                                if (report.getScheduleId() == null) {

                                } else {
                                    BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(report.getScheduleId()).orElse(null);
                                    bpmExInspSchedule.setRunningStatus(null);
                                    bpmExInspScheduleRepository.save(bpmExInspSchedule);
                                    throw new NotFoundError("报告尚未确认。  ");
                                }
                            }


                            if (exInspConfirm == null || report.getReportStatus() != ReportStatus.INIT
                                || report.getReportStatus() == ReportStatus.REHANDLE_INIT ||
                                report.getReportStatus() == ReportStatus.PENDING) {

                                report.setLastModifiedAt(new Date());
                                // 二次上传不替换原始报告
//                                report.setPdfReportFileId(LongUtils.parseLong(fileES.getId()));
                                report.setUploadFileId(LongUtils.parseLong(fileES.getId()));
                                report.setUploadFilePath(fileES.getPath());
                                report.setUploadReportPageCount(PdfUtils.getPdfPageCount(protectedDir + fileES.getPath().substring(1)));
                                report.setSecondUpload(true);
                                if (report.getReportStatus() == ReportStatus.PENDING || report.getReportStatus() == ReportStatus.SKIP_UPLOAD) {
                                    report.setReportStatus(ReportStatus.DONE);
                                }
                                qcReportRepository.save(report);
                            } else if (exInspConfirm.getUploaded() || report.getReportStatus() != ReportStatus.INIT
                                || report.getReportStatus() == ReportStatus.REHANDLE_INIT ||
                                report.getReportStatus() == ReportStatus.PENDING) {

                                exInspConfirm.setLastModifiedAt(new Date());
                                exInspConfirm.setPageCount(PdfUtils.getPdfPageCount(protectedDir + fileES.getPath().substring(1)));
                                exInspConfirm.setUploadHistoryId(exInspUploadHistory.getId());
                                exInspConfirm.setJsonReport(reportDTO);
                                exInspConfirm.setStatus(EntityStatus.APPROVED);
                                exInspConfirm.setUploadFileId(exInspUploadHistory.getFileId().toString());
                                exInspConfirm.setSecondUpload(true);
                                bpmExInspConfirmRepository.save(exInspConfirm);

                                report.setLastModifiedAt(new Date());
                                report.setPdfReportFileId(LongUtils.parseLong(fileES.getId()));
                                report.setUploadFileId(LongUtils.parseLong(fileES.getId()));
                                report.setUploadFilePath(fileES.getPath());
                                report.setUploadReportPageCount(PdfUtils.getPdfPageCount(protectedDir + fileES.getPath().substring(1)));
                                report.setSecondUpload(true);
                                if (report.getReportStatus() == ReportStatus.PENDING || report.getReportStatus() == ReportStatus.SKIP_UPLOAD) {
                                    report.setReportStatus(ReportStatus.DONE);
                                }
                                qcReportRepository.save(report);


                            } else {

                                if (report.getScheduleId() == null) {

                                } else {
                                    BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(report.getScheduleId()).orElse(null);
                                    bpmExInspSchedule.setRunningStatus(null);
                                    bpmExInspScheduleRepository.save(bpmExInspSchedule);
                                    throw new NotFoundError("此报告尚未完成首次存档,不能替换原始报告");
                                }
                            }

                        } else {


                            if (report.getScheduleId() == null) {

                            } else {
                                BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(report.getScheduleId()).orElse(null);
                                bpmExInspSchedule.setRunningStatus(null);
                                bpmExInspScheduleRepository.save(bpmExInspSchedule);
                                BpmExInspConfirm exInspConfirm = new BpmExInspConfirm();
                                exInspConfirm.setCreatedAt();
                                exInspConfirm.setLastModifiedAt();
                                exInspConfirm.setOperator(operatorDTO.getId());
                                exInspConfirm.setOrgId(orgId);
                                exInspConfirm.setProjectId(projectId);
                                exInspConfirm.setSeriesNo(uploadDTO.getSeriesNo());
                                exInspConfirm.setPageCount(PdfUtils.getPdfPageCount(protectedDir + fileES.getPath().substring(1)));
                                exInspConfirm.setInitPageCount(null);
                                exInspConfirm.setJsonReport(reportDTO);
                                exInspConfirm.setUploadHistoryId(exInspUploadHistory.getId());
                                exInspConfirm.setSecondUpload(secondUpload);
                                exInspConfirm.setStatus(EntityStatus.REJECTED);
                                exInspConfirm.setUploadFileId(fileES.getId());
                                bpmExInspConfirmRepository.save(exInspConfirm);
                                exInspUploadHistory.setConfirmed(true);
                                bpmExInspUploadHistoryRepository.save(exInspUploadHistory);
                                throw new NotFoundError("report " + uploadDTO.getSeriesNo() + " not found");
                            }
                        }
                        if (report.getScheduleId() == null) {

                        } else {
                            BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(report.getScheduleId()).orElse(null);
                            bpmExInspSchedule.setRunningStatus(EntityStatus.APPROVED);
                            bpmExInspScheduleRepository.save(bpmExInspSchedule);
                        }
                    } else {


                        File diskFile = new File(protectedDir, fileES.getPath());
                        if (!diskFile.exists()) {

                            throw new NotFoundError();
                        }


                        List<String> pdfPerPages = PdfUtils.partitionPdfFileToPerPage(diskFile.getPath(), temporaryDir);
                        System.out.println("cutting PDF");
                        Map<String, List<String>> qrcodePdfs = exInspReportService.handleUploadedReport(orgId, projectId, pdfPerPages);
                        System.out.println("Parse PDF");


                        Iterator<String> it = qrcodePdfs.keySet().iterator();
                        int qrcodePdfCount = 0;


                        boolean autoConfirm = true;


                        while (it.hasNext()) {
                            String qrcodeKey = it.next();
                            List<String> pdfFiles = qrcodePdfs.get(qrcodeKey);
                            if (!qrcodeKey.equals("NOT_IDENTIFIED")) {
                                qrcodePdfCount += pdfFiles.size();
                            }
                            String reportPath = temporaryDir + CryptoUtils.shortUniqueId() + ".pdf";
                            PdfUtils.mergePdfFiles(pdfFiles.toArray(new String[0]), reportPath);
                            for (String perPdfFile : pdfFiles) {
                                try {
                                    FileUtils.remove(perPdfFile);
                                } catch (Exception e) {
                                    e.printStackTrace(System.out);
                                }
                            }
                            File diskFilecCut = new File(reportPath);
                            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                                MediaType.APPLICATION_PDF_VALUE, true, diskFilecCut.getName());
                            try {
                                IOUtils.copy(new FileInputStream(diskFilecCut), fileItem.getOutputStream());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            logger.error("外检上传1 上传docs服务->开始");
                            MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                                APPLICATION_PDF_VALUE, fileItem.getInputStream());
                            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                            logger.error("外检上传1 上传docs服务->结束");
                            logger.error("外检上传1 保存docs服务->开始");
                            FileES fileESCut = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                                tempFileResBody.getData().getName(), new FilePostDTO()).getData();
                            logger.error("外检上传1 保存docs服务->结束");

                            if (qrcodeKey.equals("NOT_IDENTIFIED")) {


                                ActReportDTO reportDTO = new ActReportDTO();
                                reportDTO.setFileId(LongUtils.parseLong(fileESCut.getId()));
                                reportDTO.setFilePath(fileESCut.getPath());
                                BpmExInspConfirm exInspConfirm = new BpmExInspConfirm();
                                exInspConfirm.setCreatedAt();
                                exInspConfirm.setLastModifiedAt();
                                exInspConfirm.setOperator(operatorDTO.getId());
                                exInspConfirm.setOrgId(orgId);
                                exInspConfirm.setProjectId(projectId);
                                exInspConfirm.setQrcode("NOT_IDENTIFIED");
                                exInspConfirm.setReportNo("未识别");
                                exInspConfirm.setSeriesNo("10000");
                                exInspConfirm.setPageCount(PdfUtils.getPdfPageCount(protectedDir + fileESCut.getPath().substring(1)));
                                exInspConfirm.setInitPageCount(0);
                                exInspConfirm.setJsonReport(reportDTO);
                                exInspConfirm.setUploadHistoryId(exInspUploadHistory.getId());
                                exInspConfirm.setSecondUpload(secondUpload);
                                exInspConfirm.setStatus(EntityStatus.REJECTED);
                                exInspConfirm.setUploadFileId(fileES.getId());
                                bpmExInspConfirmRepository.save(exInspConfirm);
                                autoConfirm = false;
                            } else {


                                QCReport report = qcReportRepository.findByQrcode(qrcodeKey);
                                if (report != null) {

                                    exInspActInstHandleHistory.setReportNo(report.getReportNo());
                                    exInspActInstHandleHistory.setSeriesNo(report.getSeriesNo());
                                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);

//                                    if (report.getReportStatus() == ReportStatus.INIT && !NDTs.contains(report.getProcess()) && !(projectId.equals(1595407955858982139L) || projectId.equals(1616469247194450532L))) {
//                                        logger.error("PLEASE SEND EX INSP MAIL");
//                                        continue;
//                                    }


                                    if (report.getScheduleId() == null) {
                                        throw new BusinessError("报告不存在 scheduleId");
                                    }
                                    BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(report.getScheduleId()).orElse(null);
                                    if (bpmExInspSchedule == null) {
                                        throw new BusinessError("报告不存在 schedule");
                                    }
                                    if (bpmExInspSchedule.getRunningStatus() != null && bpmExInspSchedule.getRunningStatus().equals(EntityStatus.ACTIVE)) {
                                        throw new BusinessError("当前报告正在处理");
                                    }
                                    bpmExInspSchedule.setRunningStatus(EntityStatus.ACTIVE);
                                    bpmExInspScheduleRepository.save(bpmExInspSchedule);

                                    if (report.getReportStatus() == ReportStatus.PENDING || report.getReportStatus() == ReportStatus.SKIP_UPLOAD) {
                                        report.setReportStatus(ReportStatus.DONE);
                                        qcReportRepository.save(report);
                                    }


                                    List<ActReportDTO> reportDTOs = new ArrayList<>();
                                    ActReportDTO reportDTO = new ActReportDTO();
                                    reportDTO.setFileId(LongUtils.parseLong(fileESCut.getId()));
                                    reportDTO.setReportQrCode(report.getQrcode());
                                    reportDTO.setFilePath(fileESCut.getPath());
                                    reportDTO.setProcess(report.getProcess());
                                    reportDTOs.add(reportDTO);
                                    logger.error("上传报告2 调用docs取得详情服务->开始");
                                    FileViewES intiReport = fileFeignAPI
                                        .getFileInfo(orgId.toString(), LongUtils.toString(report.getPdfReportFileId()))
                                        .getData();
                                    logger.error("上传报告2 调用docs取得详情服务->结束");
                                    BpmExInspConfirm exInspConfirm = new BpmExInspConfirm();
                                    exInspConfirm.setCreatedAt();
                                    exInspConfirm.setLastModifiedAt();
                                    exInspConfirm.setOperator(operatorDTO.getId());
                                    exInspConfirm.setOrgId(orgId);
                                    exInspConfirm.setProjectId(projectId);
                                    exInspConfirm.setQrcode(qrcodeKey);
                                    exInspConfirm.setReportNo(report.getReportNo());
                                    exInspConfirm.setSeriesNo(report.getSeriesNo());
                                    exInspConfirm.setPageCount(PdfUtils.getPdfPageCount(protectedDir + fileESCut.getPath().substring(1)));
                                    exInspConfirm.setInitPageCount(PdfUtils.getPdfPageCount(protectedDir + intiReport.getPath().substring(1)));
                                    exInspConfirm.setJsonReport(reportDTO);
                                    exInspConfirm.setUploadHistoryId(exInspUploadHistory.getId());
                                    exInspConfirm.setSecondUpload(secondUpload);
                                    if (!secondUpload) {
                                        exInspConfirm.setStatus(getReportStatus(report));
                                    } else {


                                        exInspConfirm.setStatus(EntityStatus.ACTIVE);
                                    }
                                    exInspConfirm.setUploadFileId(fileES.getId());
                                    bpmExInspConfirmRepository.save(exInspConfirm);
                                    reportFileIdMap.put(exInspConfirm.getId(), LongUtils.parseLong(fileESCut.getId()));
                                    if (exInspConfirm.getInitPageCount() != exInspConfirm.getPageCount()) {
                                        autoConfirm = false;
                                    }
                                    confirmEntityIds.add(exInspConfirm.getId());
                                    exInspUploadFileConfirms.add(exInspConfirm);
                                    reports.put(exInspConfirm.getId(), report);


                                    report.setLastModifiedAt(new Date());
                                    report.setPdfReportFileId(LongUtils.parseLong(fileES.getId()));
                                    qcReportRepository.save(report);

                                    bpmExInspSchedule.setRunningStatus(EntityStatus.APPROVED);
                                    bpmExInspScheduleRepository.save(bpmExInspSchedule);
                                }
                            }
                        }

                        if (qrcodePdfCount != pdfPerPages.size()) {
                            if (qrcodePdfCount == 0) {
                                exInspUploadHistory.setConfirmed(true);
                                bpmExInspUploadHistoryRepository.save(exInspUploadHistory);
                            }
                            throw new BusinessError("There are " + (pdfPerPages.size() - qrcodePdfCount) + " pages of PDF not identified.");
                        } else if (secondUpload) {
                            handleSecondUpload(reports, reportFileIdMap, fileES);
                        } else {


                            confirmDTO.setConfirmIdList(confirmEntityIds);
                            confirmDTO.setExInspUploadConfirms(exInspUploadFileConfirms);
                            confirmDTO.setStatus(EntityStatus.APPROVED);
                            confirmDTO.setReports(reports);
                            confirmDTO.setTaskType(uploadDTO.getTaskType());
                        }

                    }
                    System.out.println("开始上传见文件" + new Date());


                    Tuple tuple = bpmExInspConfirmRepository.
                        findCounts(orgId, projectId, exInspUploadHistory.getId());
                    if (tuple != null && tuple.get("totalCount") != null && ((BigDecimal) tuple.get("totalCount")).longValue() > 0L
                        && ((BigDecimal) tuple.get("unConfirmedCount")).longValue() == 0L) {
                        bpmExInspUploadHistoryRepository.
                            updateStatusById(orgId, projectId, exInspUploadHistory.getId(), true);
                    }
                    System.out.println("开始上传见文件" + new Date());
                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(context.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.APPROVED);
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                } catch (Exception e) {
                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(context.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.DISABLED);
                    exInspActInstHandleHistory.setErrors(e.getMessage());
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                    e.printStackTrace(System.out);
                }
                return new BatchResultDTO();
            });

        return new BpmExInspConfirmResponseDTO();
    }


    /**
     * 确认外检报告上传确认情况，接收 拒绝
     *
     * @param contextDTO                 环境变量
     * @param orgId                      组织ID
     * @param projectId                  项目ID
     * @param exInspUploadFileConfirmDTO 外检上传文件确认DTO
     * @param operatorDTO                操作者DTO
     * @return 文件上传后的反馈DTO
     */
    @Override
    public BpmExInspConfirmResponseDTO confirmUploadedReport(
        ContextDTO contextDTO,
        Long orgId,
        Long projectId,
        BpmExInspConfirmsDTO exInspUploadFileConfirmDTO,
        OperatorDTO operatorDTO
    ) {

        String reportNos = "";
        String seriesNos = "";
        boolean isAsyncStarted = false;
        if(contextDTO.isContextSet()) { isAsyncStarted = true;}
        List<Long> scheduleIds = new ArrayList<>();
        if (exInspUploadFileConfirmDTO.getConfirmIdList() != null && exInspUploadFileConfirmDTO.getConfirmIdList().size() > 0) {
            for (Long confirmId : exInspUploadFileConfirmDTO.getConfirmIdList()) {
                BpmExInspConfirm bpmExInspConfirm = bpmExInspConfirmRepository.findById(confirmId).orElse(null);
                if (bpmExInspConfirm != null) {
                    if (reportNos.equals("")) {
                        reportNos = bpmExInspConfirm.getReportNo();
                    } else {
                        if (!reportNos.contains(bpmExInspConfirm.getReportNo())) {
                            reportNos = reportNos + "," + bpmExInspConfirm.getReportNo();
                        }
                    }
                    if (seriesNos.equals("")) {
                        seriesNos = bpmExInspConfirm.getSeriesNo();
                    } else {
                        if (!seriesNos.contains(bpmExInspConfirm.getSeriesNo())) {
                            seriesNos = seriesNos + bpmExInspConfirm.getSeriesNo();
                        }
                    }

                    QCReport qCReport = qcReportRepository.findByProjectIdAndSeriesNo(
                        projectId,
                        bpmExInspConfirm.getSeriesNo()
                    );
                    if (qCReport != null) {
                        if (qCReport.getScheduleId() != null && !scheduleIds.contains(qCReport.getScheduleId())) {
                            scheduleIds.add(qCReport.getScheduleId());
                        }
                        qCReport.setReportStatus(ReportStatus.PENDING);
                        qCReport.setLastModifiedAt(new Date());
                        qcReportRepository.save(qCReport);
                    }

                }
            }
        }
        if (!contextDTO.isContextSet()) {
            String authorization = contextDTO.getAuthorization();
            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(contextDTO.getRequest(), authorization),
                contextDTO.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            contextDTO.setContextSet(true);
        }
        List<ExInspActInstHandleHistory> exInspActInstHandleHistories = exInspActInstHandleHistoryRepository.findByOrgIdAndProjectIdAndTypeAndRunningStatus(
            orgId,
            projectId,
            ExInspApplyHandleType.UPLOAD_REPORT_CONFIRM,
            EntityStatus.ACTIVE
        );
        if (exInspActInstHandleHistories.size() > 19) {
            throw new BusinessError("当前项目存在正在确认的外检报告");
        }

        ExInspActInstHandleHistory exInspActInstHandleHistory = new ExInspActInstHandleHistory();
        exInspActInstHandleHistory.setOrgId(orgId);
        exInspActInstHandleHistory.setProjectId(projectId);

        exInspActInstHandleHistory.setRunningStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setCreatedAt(new Date());
        exInspActInstHandleHistory.setReportNo(reportNos);
        exInspActInstHandleHistory.setSeriesNo(seriesNos);
        exInspActInstHandleHistory.setCreatedAt(new Date());
        exInspActInstHandleHistory.setCreatedBy(contextDTO.getOperator().getId());
        exInspActInstHandleHistory.setLastModifiedAt(new Date());
        exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
        exInspActInstHandleHistory.setStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setRemarks(exInspUploadFileConfirmDTO.getConfirmIdList().toString());
        exInspActInstHandleHistory.setType(ExInspApplyHandleType.UPLOAD_REPORT_CONFIRM);
        exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
        if (isAsyncStarted) {
            try {
                Map<String, Object> data = new HashMap<>();
                data.put("orgId", orgId);
                data.put("projectId", projectId);

                for (Long scheduleId : scheduleIds) {
                    BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);
                    if (bpmExInspSchedule != null) {
                        if (bpmExInspSchedule.getRunningStatus() != null && bpmExInspSchedule.getRunningStatus().equals(EntityStatus.ACTIVE)) {
                            throw new BusinessError("报告正在处理中");
                        } else {
                            bpmExInspSchedule.setRunningStatus(EntityStatus.ACTIVE);
                            bpmExInspScheduleRepository.save(bpmExInspSchedule);
                        }
                    }
                }

                TodoBatchTaskDTO todoBatchTaskDTO = todoTaskDispatchService.batchExec(contextDTO, data, exInspUploadFileConfirmDTO);
                Map<String, Object> metaData = todoBatchTaskDTO.getMetaData();
//                if (metaData == null) {
//                    throw new BusinessError("外检报告上传确认");
//                }
                exInspActInstHandleHistory.setLastModifiedAt(new Date());
                exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                exInspActInstHandleHistory.setRunningStatus(EntityStatus.APPROVED);
                exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                for (Long scheduleId : scheduleIds) {
                    BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);
                    if (bpmExInspSchedule != null) {
                        bpmExInspSchedule.setRunningStatus(EntityStatus.APPROVED);
                        bpmExInspScheduleRepository.save(bpmExInspSchedule);
                    }

                }
            } catch (Exception e) {
                for (Long scheduleId : scheduleIds) {
                    BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);
                    if (bpmExInspSchedule != null) {
                        bpmExInspSchedule.setRunningStatus(null);
                        bpmExInspScheduleRepository.save(bpmExInspSchedule);
                    }
                }
                exInspActInstHandleHistory.setLastModifiedAt(new Date());
                exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                exInspActInstHandleHistory.setRunningStatus(EntityStatus.DISABLED);
                exInspActInstHandleHistory.setErrors(e.getMessage());
                exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                e.printStackTrace(System.out);
            }
        }
        else {
            Project project = projectService.get(orgId, projectId);
            batchTaskService.runConstructTaskExecutor(
                null,
                project,
                BatchTaskCode.EXTERNAL_INSPECTION_APPLY,
                false,
                contextDTO,
                batchTask -> {
                    try {
                        Map<String, Object> data = new HashMap<>();
                        data.put("orgId", orgId);
                        data.put("projectId", projectId);

                        for (Long scheduleId : scheduleIds) {
                            BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);
                            if (bpmExInspSchedule != null) {
                                if (bpmExInspSchedule.getRunningStatus() != null && bpmExInspSchedule.getRunningStatus().equals(EntityStatus.ACTIVE)) {
                                    throw new BusinessError("报告正在处理中");
                                } else {
                                    bpmExInspSchedule.setRunningStatus(EntityStatus.ACTIVE);
                                    bpmExInspScheduleRepository.save(bpmExInspSchedule);
                                }
                            }
                        }

                        TodoBatchTaskDTO todoBatchTaskDTO = todoTaskDispatchService.batchExec(contextDTO, data, exInspUploadFileConfirmDTO);
                        Map<String, Object> metaData = todoBatchTaskDTO.getMetaData();
//                        if (metaData == null) {
//                            throw new BusinessError("外检报告上传确认");
//                        }
                        exInspActInstHandleHistory.setLastModifiedAt(new Date());
                        exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                        exInspActInstHandleHistory.setRunningStatus(EntityStatus.APPROVED);
                        exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                        for (Long scheduleId : scheduleIds) {
                            BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);
                            if (bpmExInspSchedule != null) {
                                bpmExInspSchedule.setRunningStatus(EntityStatus.APPROVED);
                                bpmExInspScheduleRepository.save(bpmExInspSchedule);
                            }

                        }
                    } catch (Exception e) {
                        for (Long scheduleId : scheduleIds) {
                            BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);
                            if (bpmExInspSchedule != null) {
                                bpmExInspSchedule.setRunningStatus(null);
                                bpmExInspScheduleRepository.save(bpmExInspSchedule);
                            }
                        }
                        exInspActInstHandleHistory.setLastModifiedAt(new Date());
                        exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                        exInspActInstHandleHistory.setRunningStatus(EntityStatus.DISABLED);
                        exInspActInstHandleHistory.setErrors(e.getMessage());
                        exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                        e.printStackTrace(System.out);
                    }
                    return new BatchResultDTO();
                });
        }


        return new BpmExInspConfirmResponseDTO();
    }

    /**
     * 更新report表中的SecondUploadFileId。
     *
     * @param reports
     * @param reportFileIdMap
     */
    private void handleSecondUpload(Map<Long, QCReport> reports,
                                    Map<Long, Long> reportFileIdMap,
                                    FileES fileES) {
        for (Long exInspUploadFileConfirmId : reports.keySet()) {
            QCReport report = reports.get(exInspUploadFileConfirmId);

            report.setUploadFileId(reportFileIdMap.get(exInspUploadFileConfirmId));
            report.setUploadFilePath(fileES.getPath());
            report.setUploadReportPageCount(PdfUtils.getPdfPageCount(protectedDir + fileES.getPath().substring(1)));


            report.setSecondUpload(true);
            qcReportRepository.save(report);
        }

    }


    /**
     * 跳过上传报告
     *
     * @param orgId
     * @param projectId
     * @param skipDTO
     * @param contextDTO
     */
    @Override
    public void skipUpload(
        Long orgId,
        Long projectId,
        DrawingUploadDTO skipDTO,
        ContextDTO contextDTO
    ) {

        if (!contextDTO.isContextSet()) {
            String authorization = contextDTO.getAuthorization();

            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(contextDTO.getRequest(), authorization),
                contextDTO.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            contextDTO.setContextSet(true);
        }
        List<ExInspActInstHandleHistory> exInspActInstHandleHistories = exInspActInstHandleHistoryRepository.findByOrgIdAndProjectIdAndTypeAndRunningStatus(
            orgId,
            projectId,
            ExInspApplyHandleType.UPLOAD_REPORT_SKIP,
            EntityStatus.ACTIVE
        );
        if (exInspActInstHandleHistories.size() > 9) {
            throw new BusinessError("当前项目存在正在处理跳过上传");
        }
        if (skipDTO.getSeriesNo() == null) {
            throw new BusinessError("流水号不为空");
        }

        Set<String> taskDefKeys = new HashSet<>();
        taskDefKeys.add("UT-UPLOAD_EXTERNAL_INSPECTION_REPORT");
        taskDefKeys.add("usertask-QC-REPORT-UPLOAD");
        taskDefKeys.add("usertask-PMI-REPORT-UPLOAD");
        taskDefKeys.add("usertask-PWHT-REPORT-UPLOAD");
        taskDefKeys.add("usertask-QC-REPORT-NG-UPLOAD");
        taskDefKeys.add("usertask-QC-REPORT-UPLOAD-PAUT");
        taskDefKeys.add("usertask-QC-REPORT-NG-UPLOAD-PAUT");
        taskDefKeys.add("UT-UPLOAD_RENEWED_REPORT");
        BpmRuTask bpmRuTask = bpmRuTaskRepository.findByActInstIdAndTaskDefKey(LongUtils.parseLong(skipDTO.getSeriesNo()), projectId.toString());
//        for (BpmRuTask bpmRuTask : bpmRuTasks) {
            if (!taskDefKeys.contains(bpmRuTask.getTaskDefKey())) {
                throw new BusinessError("当前报告中有流程未流转至上传报告！");
            }
//        }

        Optional<ExInspActInstHandleHistory> historyOptional = exInspActInstHandleHistoryRepository.findByProjectIdAndSeriesNoAndTypeAndRunningStatus(
            projectId,
            skipDTO.getSeriesNo(),
            ExInspApplyHandleType.UPLOAD_REPORT_SKIP,
            EntityStatus.ACTIVE
        );
        if (historyOptional.isPresent()) {
            throw new BusinessError("当前流水号正在回传中！！！");
        }

        QCReport report = qcReportRepository.findByProjectIdAndSeriesNo(projectId, skipDTO.getSeriesNo());
        if (report == null) {
            throw new BusinessError("报告不存在");
        } else {
            BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findByIdAndStatus(report.getScheduleId(), EntityStatus.ACTIVE);
            if (!report.getProcess().equals("UT_MT") && bpmExInspSchedule != null && bpmExInspSchedule.getRunningStatus() != null && bpmExInspSchedule.getRunningStatus().equals(EntityStatus.APPROVED)
                && bpmExInspSchedule.getState() != null && !bpmExInspSchedule.getState().equals(ReportStatus.REHANDLE_INIT)) {
                throw new BusinessError("当前报告已回传！");
            }
        }



        ExInspActInstHandleHistory exInspActInstHandleHistory = new ExInspActInstHandleHistory();

        exInspActInstHandleHistory.setOrgId(orgId);
        exInspActInstHandleHistory.setProjectId(projectId);
        exInspActInstHandleHistory.setExInspScheduleNo(skipDTO.getFileName());
        exInspActInstHandleHistory.setRunningStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setCreatedAt(new Date());
        exInspActInstHandleHistory.setCreatedBy(contextDTO.getOperator().getId());
        exInspActInstHandleHistory.setLastModifiedAt(new Date());
        exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
        exInspActInstHandleHistory.setStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setReportNo(report.getReportNo());
        exInspActInstHandleHistory.setSeriesNo(report.getSeriesNo());
        exInspActInstHandleHistory.setRemarks("跳过上传: " + skipDTO.getFileName());
        exInspActInstHandleHistory.setType(ExInspApplyHandleType.UPLOAD_REPORT_SKIP);
        exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);


        Project project = projectService.get(orgId, projectId);
        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.EXTERNAL_INSPECTION_APPLY,
            false,
            contextDTO,
            batchTask -> {
                try {

                    Set<String> ndtSets = new HashSet<>();
                    ndtSets.add("RT");
                    ndtSets.add("MT");
                    ndtSets.add("PAUT");
                    ndtSets.add("PT");
                    ndtSets.add("UT");
                    ndtSets.add("UT_MT");
                    OperatorDTO operatorDTO = contextDTO.getOperator();


                    logger.error("上传报告3 调用docs取得详情服务->开始");
//                    FileViewES initReport = fileFeignAPI
//                        .getFileInfo(orgId.toString(), LongUtils.toString(report.getPdfReportFileId()))
//                        .getData();
                    logger.error("上传报告3 调用docs取得详情服务->结束");
//                    if (initReport == null) {
//                        throw new BusinessError("原始报告不存在");
//                    }

                    if (ndtSets.contains(report.getProcess())) {
                        skipDTO.setTaskType(BpmTaskType.NDT_UPLOAD_REPORT.name());
                    } else if ("PMI".equalsIgnoreCase(report.getProcess())) {
                        skipDTO.setTaskType(BpmTaskType.PMI_UPLOAD_REPORT.name());
                    }
                    BpmExInspConfirmsDTO confirmDTO = new BpmExInspConfirmsDTO();

                    List<Long> confirmEntityIds = new ArrayList<>();
                    List<BpmExInspConfirm> exInspUploadFileConfirms = new ArrayList<>();
                    Map<Long, QCReport> reports = new HashMap<>();


                    BpmExInspUploadHistory exInspUploadHistory = new BpmExInspUploadHistory();

                    exInspUploadHistory.setOrgId(orgId);
                    exInspUploadHistory.setProjectId(projectId);
                    exInspUploadHistory.setOperator(operatorDTO.getId());
                    exInspUploadHistory.setOperatorName(operatorDTO.getName());

                    exInspUploadHistory.setCreatedAt(new Date());
                    exInspUploadHistory.setLastModifiedAt(new Date());
                    exInspUploadHistory.setMemo(skipDTO.getComment());
                    exInspUploadHistory.setConfirmed(true);
                    exInspUploadHistory.setStatus(EntityStatus.SKIPPED);

//                    exInspUploadHistory.setFileId(LongUtils.parseLong(initReport.getId()));
//                    exInspUploadHistory.setFilePath(initReport.getPath());
                    exInspUploadHistory.setFileName("SKIPPED.PDF");
                    bpmExInspUploadHistoryRepository.save(exInspUploadHistory);
                    ActReportDTO reportDTO = new ActReportDTO();
//                    reportDTO.setFileId(LongUtils.parseLong(initReport.getId()));
                    reportDTO.setReportQrCode(report.getQrcode());
//                    reportDTO.setFilePath(initReport.getPath());


                    BpmExInspConfirm exInspConfirm = new BpmExInspConfirm();
                    exInspConfirm.setCreatedAt();
                    exInspConfirm.setLastModifiedAt();
                    exInspConfirm.setOperator(operatorDTO.getId());
                    exInspConfirm.setOrgId(orgId);
                    exInspConfirm.setProjectId(projectId);
                    exInspConfirm.setQrcode(report.getQrcode());
                    exInspConfirm.setReportNo(report.getReportNo());
                    exInspConfirm.setSeriesNo(skipDTO.getSeriesNo());
                    exInspConfirm.setPageCount(0);

//                    exInspConfirm.setInitPageCount(PdfUtils.getPdfPageCount(protectedDir + initReport.getPath().substring(1)));
                    exInspConfirm.setJsonReport(reportDTO);
                    exInspConfirm.setUploadHistoryId(exInspUploadHistory.getId());
                    exInspConfirm.setStatus(EntityStatus.SKIPPED);
                    exInspConfirm.setSecondUpload(false);
                    bpmExInspConfirmRepository.save(exInspConfirm);

                    confirmEntityIds.add(exInspConfirm.getId());
                    exInspUploadFileConfirms.add(exInspConfirm);
                    confirmDTO.setConfirmIdList(confirmEntityIds);
                    confirmDTO.setExInspUploadConfirms(exInspUploadFileConfirms);
                    confirmDTO.setStatus(EntityStatus.APPROVED);
                    confirmDTO.setSkipUploadFlag(skipDTO.isSkipUploadFlag());
                    confirmDTO.setTaskType(skipDTO.getTaskType());
                    reports.put(exInspConfirm.getId(), report);
                    confirmDTO.setReports(reports);

                    confirmUploadedReport(
                        contextDTO, orgId, projectId, confirmDTO, operatorDTO
                    );

                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.APPROVED);
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);


                } catch (Exception e) {


                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.DISABLED);
                    exInspActInstHandleHistory.setErrors(e.getMessage());
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                    e.printStackTrace(System.out);
                }
                return new BatchResultDTO();
            });
    }

    private EntityStatus getReportStatus(QCReport report) {

        if (report.getReportStatus() == ReportStatus.INIT ||
            report.getReportStatus() == ReportStatus.PENDING) {

            return EntityStatus.ACTIVE;
        } else if (report.getReportStatus() == ReportStatus.CANCEL) {
            return EntityStatus.CLOSED;
        } else if (report.getReportStatus() == ReportStatus.DONE) {
            return EntityStatus.APPROVED;
        }

        return EntityStatus.ACTIVE;

    }

    @Override
    public void externalInspectionConfirm(
        Long orgId,
        Long projectId,
        ContextDTO contextDTO,
        ExInspReportHandleSearchDTO exInspReportUploadDTO
    ) {

        String reportNos = "";
        String seriesNos = "";
        List<Long> scheduleIds = new ArrayList<>();
        if (exInspReportUploadDTO.getExInspReportHandleDTOS() != null && exInspReportUploadDTO.getExInspReportHandleDTOS().size() > 0) {
            for (ExInspReportHandleDTO exInspReportHandleDTO : exInspReportUploadDTO.getExInspReportHandleDTOS()) {
                QCReport qcReport = qcReportRepository.findByScheduleId(exInspReportHandleDTO.getScheduleId());
                if (qcReport != null) {
                    if (reportNos.equals("")) {
                        reportNos = qcReport.getReportNo();
                    } else {
                        if (!reportNos.contains(qcReport.getReportNo())) {
                            reportNos = reportNos + "," + qcReport.getReportNo();
                        }
                    }
                    if (seriesNos.equals("")) {
                        seriesNos = qcReport.getSeriesNo();
                    } else {
                        if (!seriesNos.contains(qcReport.getSeriesNo())) {
                            seriesNos = seriesNos + qcReport.getSeriesNo();
                        }
                    }
                    if (!scheduleIds.contains(qcReport.getScheduleId())) {
                        scheduleIds.add(qcReport.getScheduleId());
                    }
                }
            }
        }


        List<ExInspActInstHandleHistory> exInspActInstHandleHistories = exInspActInstHandleHistoryRepository.findByOrgIdAndProjectIdAndTypeAndRunningStatus(
            orgId,
            projectId,
            ExInspApplyHandleType.EXTERNAL_INSPECTION_CONFIRM,
            EntityStatus.ACTIVE
        );
        if (exInspActInstHandleHistories.size() > 9) {
            throw new BusinessError("当前项目存在正在外检处理的外检报告");
        }
        ExInspActInstHandleHistory exInspActInstHandleHistory = new ExInspActInstHandleHistory();
        exInspActInstHandleHistory.setOrgId(orgId);
        exInspActInstHandleHistory.setProjectId(projectId);

        exInspActInstHandleHistory.setRunningStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setCreatedAt(new Date());
        exInspActInstHandleHistory.setCreatedBy(contextDTO.getOperator().getId());
        exInspActInstHandleHistory.setLastModifiedAt(new Date());
        exInspActInstHandleHistory.setSeriesNo(seriesNos);
        exInspActInstHandleHistory.setReportNo(reportNos);
        exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
        exInspActInstHandleHistory.setStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setRemarks("外检处理确认: " + exInspReportUploadDTO.getExInspReportHandleDTOS().toString());
        exInspActInstHandleHistory.setType(ExInspApplyHandleType.EXTERNAL_INSPECTION_CONFIRM);
        exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
        if (!contextDTO.isContextSet()) {
            String authorization = contextDTO.getAuthorization();
            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(contextDTO.getRequest(), authorization),
                contextDTO.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            contextDTO.setContextSet(true);
        }

        Project project = projectService.get(orgId, projectId);
        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.EXTERNAL_INSPECTION_APPLY,
            false,
            contextDTO,
            batchTask -> {
                try {

                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", orgId);
                    data.put("projectId", projectId);
                    BpmTaskType taskType = BpmTaskType.EX_INSP_HANDLE_REPORT;
                    exInspReportUploadDTO.setTaskType(taskType.name());
                    if (CollectionUtils.isEmpty(exInspReportUploadDTO.getExInspReportHandleDTOS())) {
                        throw new BusinessError("No reports selected");
                    }

                    for (Long scheduleId : scheduleIds) {
                        BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);

                        if (bpmExInspSchedule == null) {
                            throw new BusinessError("No bpmExInspSchedule exited");
                        }
                        if (bpmExInspSchedule.getRunningStatus() != null && bpmExInspSchedule.getRunningStatus().equals(EntityStatus.ACTIVE)) {
                            throw new BusinessError(bpmExInspSchedule.getId() + " bpmExInspSchedule is running");
                        }
                        bpmExInspSchedule.setRunningStatus(EntityStatus.ACTIVE);
                        bpmExInspScheduleRepository.save(bpmExInspSchedule);
                    }
                    todoTaskDispatchService.batchExec(contextDTO, data, exInspReportUploadDTO);
                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.APPROVED);
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                    for (Long scheduleId : scheduleIds) {
                        BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);
                        if (bpmExInspSchedule != null) {
                            bpmExInspSchedule.setRunningStatus(EntityStatus.APPROVED);
                            bpmExInspScheduleRepository.save(bpmExInspSchedule);
                        }
                    }
                } catch (Exception e) {
                    for (Long scheduleId : scheduleIds) {
                        BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(scheduleId).orElse(null);
                        if (bpmExInspSchedule != null) {
                            bpmExInspSchedule.setRunningStatus(null);
                            bpmExInspScheduleRepository.save(bpmExInspSchedule);
                        }
                    }
                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.DISABLED);
                    exInspActInstHandleHistory.setErrors(e.getMessage());
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                    e.printStackTrace(System.out);
                }
                return new BatchResultDTO();
            });
    }

    public void externalInspectionReHandle(
        Long orgId,
        Long projectId,
        ContextDTO contextDTO,
        ExInspReportHandleSearchDTO uploadDTO
    ) {

        String reportNos = "";
        String seriesNos = "";
        List<Long> schduleIds = new ArrayList<>();
        if (uploadDTO.getExInspReportHandleDTOS() != null && uploadDTO.getExInspReportHandleDTOS().size() > 0) {
            for (ExInspReportHandleDTO exInspReportHandleDTO : uploadDTO.getExInspReportHandleDTOS()) {

                List<QCReport> qcReports = qcReportRepository.findByScheduleIdAndReportStatusNot(exInspReportHandleDTO.getScheduleId(), ReportStatus.CANCEL);

                for (QCReport qcReport : qcReports) {
                    if (qcReport != null) {
                        if (reportNos.equals("")) {
                            reportNos = qcReport.getReportNo();
                        } else {
                            if (!reportNos.contains(qcReport.getReportNo())) {
                                reportNos = reportNos + "," + qcReport.getReportNo();
                            }
                        }
                        if (seriesNos.equals("")) {
                            seriesNos = qcReport.getSeriesNo();
                        } else {
                            if (!seriesNos.contains(qcReport.getSeriesNo())) {
                                seriesNos = seriesNos + qcReport.getSeriesNo();
                            }
                        }
                        if (!schduleIds.contains(qcReport.getScheduleId())) {
                            schduleIds.add(qcReport.getScheduleId());
                        }
                    }
                }
            }
        }

        if (!contextDTO.isContextSet()) {
            String authorization = contextDTO.getAuthorization();
            final RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(contextDTO.getRequest(), authorization),
                contextDTO.getResponse()
            );
            RequestContextHolder.setRequestAttributes(attributes, true);
            contextDTO.setContextSet(true);
        }
        List<ExInspActInstHandleHistory> exInspActInstHandleHistories = exInspActInstHandleHistoryRepository.findByOrgIdAndProjectIdAndTypeAndRunningStatus(
            orgId,
            projectId,
            ExInspApplyHandleType.EXTERNAL_INSPECTION_RE_HANDLE,
            EntityStatus.ACTIVE
        );
        if (exInspActInstHandleHistories.size() > 9) {
            throw new BusinessError("当前项目存在正在外检再处理的外检");
        }

        ExInspActInstHandleHistory exInspActInstHandleHistory = new ExInspActInstHandleHistory();

        exInspActInstHandleHistory.setOrgId(orgId);
        exInspActInstHandleHistory.setProjectId(projectId);
        exInspActInstHandleHistory.setExInspScheduleNo("外检再处理");
        exInspActInstHandleHistory.setRunningStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setCreatedAt(new Date());
        exInspActInstHandleHistory.setCreatedBy(contextDTO.getOperator().getId());
        exInspActInstHandleHistory.setSeriesNo(seriesNos);
        exInspActInstHandleHistory.setReportNo(reportNos);
        exInspActInstHandleHistory.setLastModifiedAt(new Date());
        exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
        exInspActInstHandleHistory.setStatus(EntityStatus.ACTIVE);
        exInspActInstHandleHistory.setRemarks("外检再处理: " + uploadDTO.getExInspReportHandleDTOS().toString());
        exInspActInstHandleHistory.setType(ExInspApplyHandleType.EXTERNAL_INSPECTION_RE_HANDLE);
        exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);

        Project project = projectService.get(orgId, projectId);
        batchTaskService.runConstructTaskExecutor(
            null,
            project,
            BatchTaskCode.EXTERNAL_INSPECTION_APPLY,
            false,
            contextDTO,
            batchTask -> {
                try {
                    BpmTaskType taskType = BpmTaskType.EX_INSP_REHANDLE_REPORT;
                    uploadDTO.setTaskType(taskType.name());
                    Map<String, Object> data = new HashMap<>();
                    data.put("orgId", orgId);
                    data.put("projectId", projectId);

                    for (Long schduleId : schduleIds) {
                        BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(schduleId).orElse(null);
                        if (bpmExInspSchedule == null) {
                            throw new BusinessError("No bpmExInspSchedule exited");
                        }
                        if (bpmExInspSchedule.getRunningStatus() != null && bpmExInspSchedule.getRunningStatus().equals(EntityStatus.ACTIVE)) {
                            throw new BusinessError(bpmExInspSchedule.getId() + " bpmExInspSchedule is running");
                        }
                        bpmExInspSchedule.setRunningStatus(EntityStatus.ACTIVE);
                        bpmExInspScheduleRepository.save(bpmExInspSchedule);
                    }

                    todoTaskDispatchService.batchExec(contextDTO, data, uploadDTO);
                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.APPROVED);
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                    for (Long schduleId : schduleIds) {
                        BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(schduleId).orElse(null);
                        if (bpmExInspSchedule != null) {
                            bpmExInspSchedule.setRunningStatus(EntityStatus.APPROVED);
                            bpmExInspScheduleRepository.save(bpmExInspSchedule);
                        }
                    }
                } catch (Exception e) {
                    for (Long schduleId : schduleIds) {
                        BpmExInspSchedule bpmExInspSchedule = bpmExInspScheduleRepository.findById(schduleId).orElse(null);
                        if (bpmExInspSchedule != null) {
                            bpmExInspSchedule.setRunningStatus(null);
                            bpmExInspScheduleRepository.save(bpmExInspSchedule);
                        }
                    }
                    exInspActInstHandleHistory.setLastModifiedAt(new Date());
                    exInspActInstHandleHistory.setLastModifiedBy(contextDTO.getOperator().getId());
                    exInspActInstHandleHistory.setRunningStatus(EntityStatus.DISABLED);
                    exInspActInstHandleHistory.setErrors(e.getMessage());
                    exInspActInstHandleHistoryRepository.save(exInspActInstHandleHistory);
                    e.printStackTrace(System.out);
                }
                return new BatchResultDTO();
            });


    }
}
