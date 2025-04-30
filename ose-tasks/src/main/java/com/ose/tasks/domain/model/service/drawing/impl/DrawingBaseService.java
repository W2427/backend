package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.tasks.domain.model.repository.DrawingCoordinateRepository;
import com.ose.util.*;
import com.ose.auth.vo.UserPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.feign.RequestWrapper;
import com.ose.report.api.DrawConstructionReportFeignAPI;
import com.ose.report.dto.DrawSubPipeDTO;
import com.ose.report.dto.DrawSubPipeListDTO;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.DrawingSignatureCoordinateRepository;
import com.ose.tasks.domain.model.repository.UserSignatureRepository;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.repository.drawing.externalDrawing.ExternalDrawingHistoryRepository;
import com.ose.tasks.domain.model.repository.drawing.externalDrawing.ExternalDrawingRepository;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.bpm.taskexec.TaskRuleCheckService;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingFileHistoryInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.bpm.ExecResultDTO;
import com.ose.tasks.dto.bpm.TaskPrivilegeDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.UserSignature;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.entity.drawing.externalDrawing.DrawingAmendment;
import com.ose.tasks.entity.drawing.externalDrawing.DrawingAmendmentHistory;
import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawing;
import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawingHistory;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstDocType;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.bpm.BpmsProcessNameEnum;
import com.ose.tasks.vo.drawing.DrawingSignatureType;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.tasks.vo.setting.CategoryTypeTag;
import com.ose.vo.BpmTaskDefKey;
import com.ose.vo.DrawingFileType;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Component

public class DrawingBaseService implements DrawingBaseInterface {

    private final static Logger logger = LoggerFactory.getLogger(DrawingBaseService.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;


    @Value("${application.files.public}")
    private String publicDir;

    private final DrawingRepository drawingRepository;

    private final DrawingHistoryRepository drawingHistoryRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final SubDrawingConfigRepository subDrawingConfigRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final BpmEntityTypeRepository entityCategoryTypeRepository;

    private final BpmEntitySubTypeRepository entityCategoryRepository;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final DrawingFileRepository drawingFileRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final SubDrawingHistoryRepository subDrawingHisRepository;

    private final DrawingUploadZipFileHistoryRepository drawingUploadZipFileHistoryRepository;

    private final BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository;

    private final DrawingEntryDelegateRepository drawingEntryDelegateRepository;

    private final DrawingSignatureCoordinateRepository drawingSignatureCoordinateRepository;

    private final UserSignatureRepository userSignatureRepository;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final TaskRuleCheckService taskRuleCheckService;

    private final UploadFeignAPI uploadFeignAPI;

    private final BpmEntityDocsMaterialsRepository docsMaterialsRepository;

    private final DrawConstructionReportFeignAPI drawingReportFeignAPI;

    private final DrawingZipHistoryRepository drawingZipHistoryRepository;

    private final BatchTaskInterface batchTaskService;

    private final ProjectInterface projectService;

    private final ExternalDrawingRepository externalDrawingRepository;

    private final DrawingAmendmentRepository drawingAmendmentRepository;

    private final ExternalDrawingHistoryRepository externalDrawingHistoryRepository;

    private final DrawingAmendmentHistoryRepository drawingAmendmentHistoryRepository;

    private final EntitySubTypeInterface entitySubTypeService;

    private final DrawingFileHistoryRepository drawingFileHistoryRepository;

    private final DrawingFileHistoryInterface drawingFileHistoryService;
    private final DrawingCoordinateRepository drawingCoordinateRepository;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingBaseService(
        DrawingZipHistoryRepository drawingZipHistoryRepository,
        DrawingRepository drawingRepository,
        DrawingHistoryRepository drawingHistoryRepository,
        SubDrawingRepository subDrawingRepository,
        SubDrawingConfigRepository subDrawingConfigRepository,
        DrawingDetailRepository drawingDetailRepository,
        BpmEntityTypeRepository entityCategoryTypeRepository,
        BpmEntitySubTypeRepository entityCategoryRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        DrawingFileRepository drawingFileRepository, BpmHiTaskinstRepository hiTaskinstRepository,
        BpmRuTaskRepository ruTaskRepository,
        DrawingUploadZipFileHistoryRepository drawingUploadZipFileHistoryRepository,
        SubDrawingHistoryRepository subDrawingHisRepository,
        BpmActivityTaskNodePrivilegeRepository bpmActivityTaskNodePrivilegeRepository,
        DrawingEntryDelegateRepository drawingEntryDelegateRepository,
        DrawingSignatureCoordinateRepository drawingSignatureCoordinateRepository,
        UserSignatureRepository userSignatureRepository,
        TaskRuleCheckService taskRuleCheckService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        BpmEntityDocsMaterialsRepository docsMaterialsRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DrawConstructionReportFeignAPI drawingReportFeignAPI,
        BatchTaskInterface batchTaskService,
        ProjectInterface projectService,
        ExternalDrawingRepository externalDrawingRepository,
        DrawingAmendmentRepository drawingAmendmentRepository,
        ExternalDrawingHistoryRepository externalDrawingHistoryRepository,
        DrawingAmendmentHistoryRepository drawingAmendmentHistoryRepository,
        EntitySubTypeInterface entitySubTypeService,
        DrawingFileHistoryRepository drawingFileHistoryRepository,
        DrawingFileHistoryInterface drawingFileHistoryService,
        DrawingCoordinateRepository drawingCoordinateRepository) {
        this.drawingRepository = drawingRepository;
        this.drawingHistoryRepository = drawingHistoryRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.subDrawingConfigRepository = subDrawingConfigRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.entityCategoryTypeRepository = entityCategoryTypeRepository;
        this.entityCategoryRepository = entityCategoryRepository;
        this.bpmActInstRepository = bpmActInstRepository;
        this.drawingFileRepository = drawingFileRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.subDrawingHisRepository = subDrawingHisRepository;
        this.drawingUploadZipFileHistoryRepository = drawingUploadZipFileHistoryRepository;
        this.bpmActivityTaskNodePrivilegeRepository = bpmActivityTaskNodePrivilegeRepository;
        this.drawingEntryDelegateRepository = drawingEntryDelegateRepository;
        this.drawingSignatureCoordinateRepository = drawingSignatureCoordinateRepository;
        this.userSignatureRepository = userSignatureRepository;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.taskRuleCheckService = taskRuleCheckService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.docsMaterialsRepository = docsMaterialsRepository;
        this.drawingReportFeignAPI = drawingReportFeignAPI;
        this.drawingZipHistoryRepository = drawingZipHistoryRepository;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.externalDrawingRepository = externalDrawingRepository;
        this.drawingAmendmentRepository = drawingAmendmentRepository;
        this.externalDrawingHistoryRepository = externalDrawingHistoryRepository;
        this.drawingAmendmentHistoryRepository = drawingAmendmentHistoryRepository;
        this.entitySubTypeService = entitySubTypeService;
        this.drawingFileHistoryRepository = drawingFileHistoryRepository;
        this.drawingFileHistoryService = drawingFileHistoryService;
        this.drawingCoordinateRepository = drawingCoordinateRepository;
    }


    /**
     * 获取图纸变量
     */
    @Override
    public List<SubDrawingConfig> getVariables(Long orgId, Long projectId, Long id) {
        Optional<Drawing> drawingOp = drawingRepository.findById(id);
        if (drawingOp.isPresent()) {
            Drawing drawing = drawingOp.get();
            BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, drawing.getEntitySubType());
            return subDrawingConfigRepository.findByOrgIdAndProjectIdAndDrawingCategoryId(orgId, projectId,
                best.getId());
        }
        return null;
    }


    /**
     * 上传图纸文件
     */
    @Override
    public boolean uploadPdf(Long orgId, Long projectId, Long drawingDetailId, OperatorDTO user, DrawingUploadDTO uploadDTO) {

        Optional<DrawingDetail> drawingDetailOp = drawingDetailRepository.findById(drawingDetailId);
        if (drawingDetailOp.isPresent()) {

            DrawingDetail drawingDetail = drawingDetailOp.get();
            Drawing drawing = getDetailedDrawing(orgId, projectId, drawingDetail.getDrawingId());

            DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
            drawingUploadZipFileHistory.setCreatedAt();
            drawingUploadZipFileHistory.setDrawingId(drawing.getId());
            drawingUploadZipFileHistory.setOperator(user.getId());
            drawingUploadZipFileHistory.setOrgId(orgId);
            drawingUploadZipFileHistory.setProjectId(projectId);
            drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
            drawingUploadZipFileHistory.setFileCount(1);
            drawingUploadZipFileHistory.setZipFile(false);
            logger.info("图纸基础7 保存docs服务->开始");
            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), uploadDTO.getFileName(),
                new FilePostDTO());
            logger.info("图纸基础7 保存docs服务->结束");
            FileES fileES = responseBody.getData();
//            drawingDetail.setFileId(LongUtils.parseLong(fileES.getId()));
//            drawingDetail.setFileName(fileES.getName());
//            drawingDetail.setFilePath(fileES.getPath());
            if (StringUtils.isEmpty(drawingDetail.getQrCode()))
                drawingDetail.setQrCode(QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid());
//            drawingDetail.setUploadDate(new Date());
//            drawingDetail.setUploader(user.getName());
//            drawingDetail.setUploaderId(user.getId());
            DrawingFile drawingFile = drawingFileRepository.
                findByProjectIdAndDrawingDetailIdAndDrawingFileTypeAndStatus(projectId,
                    drawingDetailId, uploadDTO.getFileType(), EntityStatus.ACTIVE);
            if (drawingFile == null) {
                drawingFile = new DrawingFile();
                drawingFile.setStatus(EntityStatus.ACTIVE);
                drawingFile.setProjectId(projectId);
                drawingFile.setOrgId(orgId);
            }
            drawingFile.setFileId(LongUtils.parseLong(fileES.getId()));
            drawingFile.setFileName(fileES.getName());
            drawingFile.setFilePath(fileES.getPath());
            drawingFile.setUploadDate(new Date());
            drawingFile.setUploaderName(user.getName());
            drawingFile.setUploaderId(user.getId());
            drawingFile.setLastModifiedAt();
            drawingFile.setDrawingDetailId(drawingDetailId);
            drawingFile.setDrawingFileType(uploadDTO.getFileType());
            if (!StringUtils.isEmpty(drawingFile.getFileName()) &&
                (drawingFile.getFileName().endsWith("pdf") || drawingFile.getFileName().endsWith("PDF"))) {
                drawingFile.setPageCount(PdfUtils.getPdfPageCount(protectedDir + drawingFile.getFilePath()));
            }
            drawingFileRepository.save(drawingFile);
            drawingDetailRepository.save(drawingDetail);

            drawing.setFileId(LongUtils.parseLong(fileES.getId()));
            drawing.setFileName(fileES.getName());
            drawing.setFilePath(fileES.getPath());
//            if(StringUtils.isEmpty(drawing.getQrCode())) { drawing.setQrCode(QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid());
//            drawing.setQrCode(drawingDetail.getQrCode());
            drawing.setUploadDate(new Date());
            drawingRepository.save(drawing);


            DrawingHistory drawingHistory = new DrawingHistory();
            drawingHistory.setOperator(user.getId());
            drawingHistory.setQrCode(drawingDetail.getQrCode());
            drawingHistory.setLastModifiedAt();
            drawingHistory.setFileId(drawingFile.getFileId());
            drawingHistory.setFileName(drawingFile.getFileName());
            drawingHistory.setFilePath(drawingFile.getFilePath());
            drawingHistory.setMemo(uploadDTO.getComment());
            drawingHistory.setStatus(EntityStatus.ACTIVE);
            drawingHistory.setCreatedAt();
            drawingHistory.setDrawingId(drawing.getId());
            drawingHistory.setLastModifiedAt();
            drawingHistoryRepository.save(drawingHistory);

            drawingUploadZipFileHistory.setSuccessCount(1);
            drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(fileES.getId()));
            drawingUploadZipFileHistory.setFileName(fileES.getName());
            drawingUploadZipFileHistory.setFilePath(fileES.getPath());
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

            // 将同流程的history设为false
            List<DrawingFileHistory> drawingFileHistories = drawingFileHistoryRepository.findByProjectIdAndDrawingDetailIdAndDrawingFileTypeAndProcInstIdAndDeletedIsFalse(
                projectId,
                drawingDetailId,
                uploadDTO.getFileType(),
                uploadDTO.getProcInstId()
            );
            if (drawingFileHistories.size() > 0) {
                for (DrawingFileHistory history : drawingFileHistories) {
                    history.setLatest(false);
                    drawingFileHistoryRepository.save(history);
                }
            }

            // 创建Drawing File history记录
            // 查找是否存在历史记录
            DrawingFileHistory drawingFileHistory = drawingFileHistoryRepository.findByOrgIdAndProjectIdAndDrawingFileIdAndTaskIdAndStatus(
                orgId,
                projectId,
                drawingFile.getId(),
                uploadDTO.getTaskId(),
                EntityStatus.ACTIVE
            );

            if (null == drawingFileHistory) {
                // 创建图纸文件历史
                DrawingFileHistoryCreateDTO drawingFileHistoryCreateDTO = new DrawingFileHistoryCreateDTO();
                drawingFileHistoryCreateDTO.setFileId(LongUtils.parseLong(fileES.getId()));
                drawingFileHistoryCreateDTO.setFileName(fileES.getName());
                drawingFileHistoryCreateDTO.setFilePath(fileES.getPath());
                drawingFileHistoryCreateDTO.setProcInstId(uploadDTO.getProcInstId());
                drawingFileHistoryCreateDTO.setTaskId(uploadDTO.getTaskId());
                drawingFileHistoryCreateDTO.setUser(user.getName());
                drawingFileHistoryCreateDTO.setRev(drawingDetail.getRevNo());
                // 创建图纸文件历史
                drawingFileHistoryService.create(
                    orgId,
                    projectId,
                    drawingFile.getId(),
                    drawingFileHistoryCreateDTO,
                    user
                );
            } else {
                // 更新图纸文件历史
                DrawingFileHistoryCreateDTO drawingFileHistoryCreateDTO = new DrawingFileHistoryCreateDTO();
                drawingFileHistoryCreateDTO.setFileId(LongUtils.parseLong(fileES.getId()));
                drawingFileHistoryCreateDTO.setFileName(fileES.getName());
                drawingFileHistoryCreateDTO.setFilePath(fileES.getPath());
                drawingFileHistoryCreateDTO.setProcInstId(uploadDTO.getProcInstId());
                drawingFileHistoryCreateDTO.setTaskId(uploadDTO.getTaskId());
                drawingFileHistoryCreateDTO.setUser(user.getName());
                drawingFileHistoryCreateDTO.setRev(drawingDetail.getRevNo());
                drawingFileHistoryService.update(
                    orgId,
                    projectId,
                    drawingFile.getId(),
                    drawingFileHistory.getId(),
                    drawingFileHistoryCreateDTO,
                    user
                );
            }

            return true;
        }
        return false;

    }

    @Override
    public boolean uploadPdfExternal(Long orgId, Long projectId, Long id, OperatorDTO user, DrawingUploadDTO uploadDTO) throws IOException {

        List<DrawingDetail> details = drawingDetailRepository
            .findByDrawingIdAndRevNoAndStatus(id, uploadDTO.getVersion(), EntityStatus.PENDING);
        DrawingDetail detail;
        if (details.isEmpty()) {
            detail = new DrawingDetail();
            detail.setRevNo(uploadDTO.getVersion());

            detail.setDrawingId(id);
            detail.setOrgId(orgId);
            detail.setProjectId(projectId);
            detail.setCreatedAt(new Date());
            detail.setStatus(EntityStatus.PENDING);
        } else {
            detail = details.get(details.size() - 1);
        }

        Drawing drawing = getDetailedDrawing(orgId, projectId, id);

        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
        drawingUploadZipFileHistory.setCreatedAt();
        drawingUploadZipFileHistory.setDrawingId(drawing.getId());
        drawingUploadZipFileHistory.setOperator(user.getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);
        logger.error("图纸基础7 保存docs服务->开始");
        JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), uploadDTO.getFileName(),
            new FilePostDTO());
        logger.error("图纸基础7 保存docs服务->结束");
        FileES fileES = responseBody.getData();
//            drawingDetail.setFileId(LongUtils.parseLong(fileES.getId()));
//            drawingDetail.setFileName(fileES.getName());
//            drawingDetail.setFilePath(fileES.getPath());
        if (StringUtils.isEmpty(detail.getQrCode()))
            detail.setQrCode(QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid());
//            drawingDetail.setUploadDate(new Date());
//            drawingDetail.setUploader(user.getName());
//            drawingDetail.setUploaderId(user.getId());
        DrawingFile drawingFile = drawingFileRepository.
            findByProjectIdAndDrawingDetailIdAndDrawingFileTypeAndStatus(projectId,
                detail.getId(), uploadDTO.getFileType(), EntityStatus.ACTIVE);
        if (drawingFile == null) {
            drawingFile = new DrawingFile();
            drawingFile.setStatus(EntityStatus.ACTIVE);
            drawingFile.setProjectId(projectId);
            drawingFile.setOrgId(orgId);
        }
        drawingFile.setFileId(LongUtils.parseLong(fileES.getId()));
        drawingFile.setFileName(fileES.getName());
        drawingFile.setFilePath(fileES.getPath());
        drawingFile.setUploadDate(new Date());
        drawingFile.setUploaderName(user.getName());
        drawingFile.setUploaderId(user.getId());
        drawingFile.setLastModifiedAt();
        drawingFile.setDrawingDetailId(detail.getId());
        drawingFile.setDrawingFileType(uploadDTO.getFileType());
        if (!StringUtils.isEmpty(drawingFile.getFileName()) &&
            (drawingFile.getFileName().endsWith("pdf") || drawingFile.getFileName().endsWith("PDF"))) {
            drawingFile.setPageCount(PdfUtils.getPdfPageCount(protectedDir + drawingFile.getFilePath()));
        }
        drawingFileRepository.save(drawingFile);
        drawingDetailRepository.save(detail);

        drawing.setFileId(LongUtils.parseLong(fileES.getId()));
        drawing.setFileName(fileES.getName());
        drawing.setFilePath(fileES.getPath());
        if (StringUtils.isEmpty(drawing.getQrCode()))
            drawing.setQrCode(QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid());
        drawing.setUploadDate(new Date());
        drawingRepository.save(drawing);


        DrawingHistory drawingHistory = new DrawingHistory();
        drawingHistory.setOperator(user.getId());
        drawingHistory.setQrCode(detail.getQrCode());
        drawingHistory.setLastModifiedAt();
        drawingHistory.setFileId(drawingFile.getFileId());
        drawingHistory.setFileName(drawingFile.getFileName());
        drawingHistory.setFilePath(drawingFile.getFilePath());
        drawingHistory.setMemo(uploadDTO.getComment());
        drawingHistory.setStatus(EntityStatus.ACTIVE);
        drawingHistory.setCreatedAt();
        drawingHistory.setDrawingId(drawing.getId());
        drawingHistory.setLastModifiedAt();
        drawingHistoryRepository.save(drawingHistory);

        drawingUploadZipFileHistory.setSuccessCount(1);
        drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(fileES.getId()));
        drawingUploadZipFileHistory.setFileName(fileES.getName());
        drawingUploadZipFileHistory.setFilePath(fileES.getPath());
        drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

        return true;
    }

    /**
     * 上传图纸文件
     */
    @Override
    public boolean uploadExternalPdf(Long orgId, Long projectId, Long id, Long userid, DrawingUploadDTO uploadDTO) throws IOException {

        ExternalDrawingHistory his = externalDrawingHistoryRepository
            .findByDrawingIdAndVerison(id, uploadDTO.getVersion());
        if (his == null) {
            his = new ExternalDrawingHistory();
            his.setCreatedAt();
            his.setDrawingId(id);
            his.setOrgId(orgId);
            his.setProjectId(projectId);
            his.setStatus(EntityStatus.ACTIVE);
            his.setVerison(uploadDTO.getVersion());

            DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
            drawingUploadZipFileHistory.setCreatedAt();
            drawingUploadZipFileHistory.setDrawingId(id);
            drawingUploadZipFileHistory.setOperator(userid);
            drawingUploadZipFileHistory.setOrgId(orgId);
            drawingUploadZipFileHistory.setProjectId(projectId);
            drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
            drawingUploadZipFileHistory.setFileCount(1);
            drawingUploadZipFileHistory.setZipFile(false);

            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), uploadDTO.getFileName(),
                new FilePostDTO());
            FileES fileES = responseBody.getData();

            ExternalDrawing drawing = getDetailedExternalDrawing(orgId, projectId, id);
            drawing.setFileId(LongUtils.parseLong(fileES.getId()));
            drawing.setFileName(fileES.getName());
            drawing.setFilePath(fileES.getPath());
            drawing.setLatestRev(uploadDTO.getVersion());
            drawing.setMemo(uploadDTO.getComment());
            drawing.setQrCode(QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid());
            externalDrawingRepository.save(drawing);

            // 添加二维码
            String resultFile = temporaryDir + fileES.getName();
            String initFile = protectedDir + drawing.getFilePath().substring(1);
            String qrFileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
            QRCodeUtils.generateQRCodeNoBlank(drawing.getQrCode(), 60, "png", qrFileName);
            PdfUtils.setSingleImageToPdf(resultFile, initFile, qrFileName, 250,
                250, 60, 60);

            File diskFile = new File(resultFile);

            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

            IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());

            // 再次将文件上传到文档服务器
            MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);

            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                tempFileResBody.getData().getName(), new FilePostDTO());
            FileES newFileES = fileESResBody.getData();

            drawing.setFileId(LongUtils.parseLong(newFileES.getId()));
            drawing.setFileName(newFileES.getName());
            drawing.setFilePath(newFileES.getPath());
            externalDrawingRepository.save(drawing);


            // 上传历史记录、图纸记录关于文件信息的更新
            drawingUploadZipFileHistory.setSuccessCount(1);
            drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(newFileES.getId()));
            drawingUploadZipFileHistory.setFileName(newFileES.getName());
            drawingUploadZipFileHistory.setFilePath(newFileES.getPath());
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

            his.setOperator(userid);
            his.setQrCode(drawing.getQrCode());
            his.setLastModifiedAt();
            his.setFileId(drawing.getFileId());
            his.setFileName(drawing.getFileName());
            his.setFilePath(drawing.getFilePath());
            his.setMemo(uploadDTO.getComment());
            externalDrawingHistoryRepository.save(his);

            return true;
        } else {
            throw new BusinessError("此图集版本已存在,请确认版本！");
        }

    }

    /**
     * 上传图纸文件
     */
    @Override
    public boolean uploadAmendmentPdf(Long orgId, Long projectId, Long id, Long userid, DrawingUploadDTO uploadDTO) throws IOException {

        DrawingAmendmentHistory his = drawingAmendmentHistoryRepository
            .findByDrawingIdAndVerison(id, uploadDTO.getVersion());
        if (his == null) {
            his = new DrawingAmendmentHistory();
            his.setCreatedAt();
            his.setDrawingId(id);
            his.setOrgId(orgId);
            his.setProjectId(projectId);
            his.setStatus(EntityStatus.ACTIVE);
            his.setVerison(uploadDTO.getVersion());

            DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
            drawingUploadZipFileHistory.setCreatedAt();
            drawingUploadZipFileHistory.setDrawingId(id);
            drawingUploadZipFileHistory.setOperator(userid);
            drawingUploadZipFileHistory.setOrgId(orgId);
            drawingUploadZipFileHistory.setProjectId(projectId);
            drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
            drawingUploadZipFileHistory.setFileCount(1);
            drawingUploadZipFileHistory.setZipFile(false);

            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), uploadDTO.getFileName(),
                new FilePostDTO());
            FileES fileES = responseBody.getData();

            DrawingAmendment drawing = getDetailedAmendmentDrawing(orgId, projectId, id);
            drawing.setFileId(LongUtils.parseLong(fileES.getId()));
            drawing.setFileName(fileES.getName());
            drawing.setFilePath(fileES.getPath());
            drawing.setLatestRev(uploadDTO.getVersion());
            drawing.setMemo(uploadDTO.getComment());
            drawing.setQrCode(QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid());
            drawingAmendmentRepository.save(drawing);

            // 添加二维码
            String resultFile = temporaryDir + fileES.getName();
            String initFile = protectedDir + drawing.getFilePath().substring(1);
            String qrFileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
            QRCodeUtils.generateQRCodeNoBlank(drawing.getQrCode(), 60, "png", qrFileName);
            PdfUtils.setSingleImageToPdf(resultFile, initFile, qrFileName, 250,
                250, 60, 60);

            File diskFile = new File(resultFile);

            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

            IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());

            // 再次将文件上传到文档服务器
            MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);

            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                tempFileResBody.getData().getName(), new FilePostDTO());
            FileES newFileES = fileESResBody.getData();

            drawing.setFileId(LongUtils.parseLong(newFileES.getId()));
            drawing.setFileName(newFileES.getName());
            drawing.setFilePath(newFileES.getPath());
            drawingAmendmentRepository.save(drawing);


            // 上传历史记录、图纸记录关于文件信息的更新
            drawingUploadZipFileHistory.setSuccessCount(1);
            drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(newFileES.getId()));
            drawingUploadZipFileHistory.setFileName(newFileES.getName());
            drawingUploadZipFileHistory.setFilePath(newFileES.getPath());
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

            his.setOperator(userid);
            his.setQrCode(drawing.getQrCode());
            his.setLastModifiedAt();
            his.setFileId(drawing.getFileId());
            his.setFileName(drawing.getFileName());
            his.setFilePath(drawing.getFilePath());
            his.setMemo(uploadDTO.getComment());
            drawingAmendmentHistoryRepository.save(his);

            return true;
        } else {
            throw new BusinessError("此图集版本已存在,请确认版本！");
        }

    }

    @Override
    public boolean uploadPdfAmendment(Long orgId, Long projectId, Long id, Long userid, DrawingUploadDTO uploadDTO) throws IOException {

        DrawingAmendmentHistory his = drawingAmendmentHistoryRepository
            .findByDrawingIdAndVerison(id, uploadDTO.getVersion());
        if (his == null) {
            his = new DrawingAmendmentHistory();
            his.setCreatedAt();
            his.setDrawingId(id);
            his.setOrgId(orgId);
            his.setProjectId(projectId);
            his.setStatus(EntityStatus.ACTIVE);
            his.setVerison(uploadDTO.getVersion());

            DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
            drawingUploadZipFileHistory.setCreatedAt();
            drawingUploadZipFileHistory.setDrawingId(id);
            drawingUploadZipFileHistory.setOperator(userid);
            drawingUploadZipFileHistory.setOrgId(orgId);
            drawingUploadZipFileHistory.setProjectId(projectId);
            drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
            drawingUploadZipFileHistory.setFileCount(1);
            drawingUploadZipFileHistory.setZipFile(false);

            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), uploadDTO.getFileName(),
                new FilePostDTO());
            FileES fileES = responseBody.getData();

            DrawingAmendment drawing = getDetailedAmendmentDrawing(orgId, projectId, id);
            drawing.setFileId(LongUtils.parseLong(fileES.getId()));
            drawing.setFileName(fileES.getName());
            drawing.setFilePath(fileES.getPath());
            drawing.setLatestRev(uploadDTO.getVersion());
            drawing.setMemo(uploadDTO.getComment());
            drawing.setQrCode(QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid());
            drawingAmendmentRepository.save(drawing);

            // 添加二维码
            String resultFile = temporaryDir + fileES.getName();
            String initFile = protectedDir + drawing.getFilePath().substring(1);
            String qrFileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
            QRCodeUtils.generateQRCodeNoBlank(drawing.getQrCode(), 60, "png", qrFileName);
            PdfUtils.setSingleImageToPdf(resultFile, initFile, qrFileName, 250,
                250, 60, 60);

            File diskFile = new File(resultFile);

            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

            IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());

            // 再次将文件上传到文档服务器
            MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);

            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                tempFileResBody.getData().getName(), new FilePostDTO());
            FileES newFileES = fileESResBody.getData();

            drawing.setFileId(LongUtils.parseLong(newFileES.getId()));
            drawing.setFileName(newFileES.getName());
            drawing.setFilePath(newFileES.getPath());
            drawingAmendmentRepository.save(drawing);


            // 上传历史记录、图纸记录关于文件信息的更新
            drawingUploadZipFileHistory.setSuccessCount(1);
            drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(newFileES.getId()));
            drawingUploadZipFileHistory.setFileName(newFileES.getName());
            drawingUploadZipFileHistory.setFilePath(newFileES.getPath());
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

            his.setOperator(userid);
            his.setQrCode(drawing.getQrCode());
            his.setLastModifiedAt();
            his.setFileId(drawing.getFileId());
            his.setFileName(drawing.getFileName());
            his.setFilePath(drawing.getFilePath());
            his.setMemo(uploadDTO.getComment());
            drawingAmendmentHistoryRepository.save(his);

            return true;
        } else {
            throw new BusinessError("此图集版本已存在,请确认版本！");
        }
    }

    /**
     * 查看图纸清单条目详细信息
     */
    @Override
    public Drawing getDetailedDrawing(Long orgId, Long projectId, Long id) {
        Optional<Drawing> drawingOp = drawingRepository.findById(id);
        if (drawingOp.isPresent()) {
            Drawing drawing = drawingOp.get();

            DrawingEntryDelegate approvedD = drawingEntryDelegateRepository
                .findByDrawingIdAndPrivilegeAndStatus(
                    drawing.getId(),
                    UserPrivilege.DRAWING_REVIEW_EXECUTE,
                    EntityStatus.ACTIVE
                );
            if (approvedD != null) {
//                drawing.setApprovedUserId(approvedD.getUserId());
            }

            DrawingEntryDelegate checkD = drawingEntryDelegateRepository
                .findByDrawingIdAndPrivilegeAndStatus(
                    drawing.getId(),
                    UserPrivilege.DRAWING_CHECK_EXECUTE,
                    EntityStatus.ACTIVE
                );
            if (checkD != null) {
//                drawing.setCheckUserId(checkD.getUserId());
            }

            DrawingEntryDelegate drawD = drawingEntryDelegateRepository
                .findByDrawingIdAndPrivilegeAndStatus(
                    drawing.getId(),
                    UserPrivilege.DESIGN_ENGINEER_EXECUTE,
                    EntityStatus.ACTIVE
                );
            if (drawD != null) {
//                drawing.setDrawUserId(drawD.getUserId());
            }
            return drawing;
        }
        return null;
    }

    /**
     * 查看图纸清单条目详细信息
     */
    @Override
    public Drawing getDetailedDrawingUncheck(Long orgId, Long projectId, Long id) {
        Optional<Drawing> drawingOp = drawingRepository.findById(id);
        return drawingOp.orElse(null);
    }

    /**
     * 查看图纸清单条目详细信息
     */
    @Override
    public ExternalDrawing getDetailedExternalDrawing(Long orgId, Long projectId, Long id) {
        Optional<ExternalDrawing> drawingOp = externalDrawingRepository.findById(id);
        if (drawingOp.isPresent()) {
            ExternalDrawing drawing = drawingOp.get();

            DrawingEntryDelegate approvedD = drawingEntryDelegateRepository
                .findByDrawingIdAndPrivilegeAndStatus(
                    drawing.getId(),
                    UserPrivilege.DRAWING_REVIEW_EXECUTE,
                    EntityStatus.ACTIVE
                );
            if (approvedD != null) {
                drawing.setApprovedUserId(approvedD.getUserId());
            }

            DrawingEntryDelegate checkD = drawingEntryDelegateRepository
                .findByDrawingIdAndPrivilegeAndStatus(
                    drawing.getId(),
                    UserPrivilege.DRAWING_CHECK_EXECUTE,
                    EntityStatus.ACTIVE
                );
            if (checkD != null) {
                drawing.setCheckUserId(checkD.getUserId());
            }

            DrawingEntryDelegate drawD = drawingEntryDelegateRepository
                .findByDrawingIdAndPrivilegeAndStatus(
                    drawing.getId(),
                    UserPrivilege.DESIGN_ENGINEER_EXECUTE,
                    EntityStatus.ACTIVE
                );
            if (drawD != null) {
                drawing.setDrawUserId(drawD.getUserId());
            }
            return drawing;
        }
        return null;
    }

    /**
     * 查看图纸清单条目详细信息
     */
    @Override
    public DrawingAmendment getDetailedAmendmentDrawing(Long orgId, Long projectId, Long id) {
        Optional<DrawingAmendment> drawingOp = drawingAmendmentRepository.findById(id);
        if (drawingOp.isPresent()) {
            DrawingAmendment drawing = drawingOp.get();

//            DrawingEntryDelegate approvedD = drawingEntryDelegateRepository
//                .findByDrawingIdAndPrivilegeAndStatus(
//                    drawing.getId(),
//                    UserPrivilege.DRAWING_REVIEW_EXECUTE,
//                    EntityStatus.ACTIVE
//                );
//            if (approvedD != null) {
//                drawing.setApprovedUserId(approvedD.getUserId());
//            }
//
//            DrawingEntryDelegate checkD = drawingEntryDelegateRepository
//                .findByDrawingIdAndPrivilegeAndStatus(
//                    drawing.getId(),
//                    UserPrivilege.DRAWING_CHECK_EXECUTE,
//                    EntityStatus.ACTIVE
//                );
//            if (checkD != null) {
//                drawing.setCheckUserId(checkD.getUserId());
//            }
//
//            DrawingEntryDelegate drawD = drawingEntryDelegateRepository
//                .findByDrawingIdAndPrivilegeAndStatus(
//                    drawing.getId(),
//                    UserPrivilege.DESIGN_ENGINEER,
//                    EntityStatus.ACTIVE
//                );
//            if (drawD != null) {
//                drawing.setDrawUserId(drawD.getUserId());
//            }
            return drawing;
        }
        return null;
    }

    /**
     * 获取图纸类型列表
     */
    @Override
    public List<BpmEntitySubType> getList(Long orgId, Long projectId) {
        Optional<BpmEntityType> opType = entityCategoryTypeRepository.findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(
            "SHOP_DRAWING", projectId, orgId, CategoryTypeTag.READONLY.name(), EntityStatus.ACTIVE);
        if (opType.isPresent()) {
            BpmEntityType type = opType.get();
            return entityCategoryRepository.findEntityCategoriesByTypeId(projectId, type.getId());
        }
        return null;
    }

    /**
     * 获取图纸类型列表
     */
    @Override
    public List<BpmEntitySubType> getExternalList(Long orgId, Long projectId) {
        Optional<BpmEntityType> opType = entityCategoryTypeRepository.findByNameEnAndProjectIdAndOrgIdAndTypeAndStatus(
            "SHOP_DRAWING", projectId, orgId, CategoryTypeTag.READONLY.name(), EntityStatus.ACTIVE);
        if (opType.isPresent()) {
            BpmEntityType type = opType.get();
            return entityCategoryRepository.findEntityCategoriesByTypeId(projectId, type.getId());
        }
        return null;
    }


    /**
     * 查询zip文件上传历史记录
     */
    @Override
    public Page<DrawingUploadZipFileHistory> zipFileHistory(Long orgId, Long projectId, Long drawingId, PageDTO page) {
        return drawingRepository.zipFileHistory(orgId, projectId, drawingId, page);
    }

    /**
     * 查询zip文件上传历史记录
     */
    @Override
    public Page<DrawingUploadZipFileHistory> zipFileHistoryExternal(Long orgId, Long projectId, Long drawingId, PageDTO page) {
        return drawingRepository.zipFileHistory(orgId, projectId, drawingId, page);
    }

    /**
     * 查询zip文件上传历史记录明细
     */
    @Override
    public Page<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetail(Long orgId, Long projectId,
                                                                        Long drawingId, Long id, PageDTO page) {
        return drawingRepository.zipFileHistoryDetail(orgId, projectId, drawingId, id, page);
    }

    /**
     * 查询zip文件上传历史记录明细
     */
    @Override
    public Page<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetailExternal(Long orgId, Long projectId,
                                                                                Long drawingId, Long id, PageDTO page) {
        return drawingRepository.zipFileHistoryDetail(orgId, projectId, drawingId, id, page);
    }


    @Override
    public boolean setDrawingFile(Drawing dl, DrawingFileDTO dto) {
        dl.setFileId(dto.getFileId());
        dl.setFileName(dto.getFileName());
        dl.setFilePath(dto.getFilePath());
        dl.setQrCode(dto.getQrCode());
        drawingRepository.save(dl);
        return true;
    }

    @Override
    public Object checkIssue(Long orgId, Long projectId, String qrCode) {
        DrawingHistory drawing = drawingHistoryRepository.findByOrgIdAndProjectIdAndQrCode(orgId, projectId, qrCode);
        if (drawing != null) {
            return drawing;
        }
        SubDrawingHistory subDrawing = subDrawingHisRepository.findByOrgIdAndProjectIdAndQrCode(orgId, projectId, qrCode);
        if (subDrawing != null) {
            return subDrawing;
        }
        return null;
    }

    @Override
    public Object checkIssueExternal(Long orgId, Long projectId, String qrCode) {
        DrawingHistory drawing = drawingHistoryRepository.findByOrgIdAndProjectIdAndQrCode(orgId, projectId, qrCode);
        if (drawing != null) {
            return drawing;
        }
        SubDrawingHistory subDrawing = subDrawingHisRepository.findByOrgIdAndProjectIdAndQrCode(orgId, projectId, qrCode);
        if (subDrawing != null) {
            return subDrawing;
        }
        return null;
    }

    @Override
    public boolean checkActivity(Long orgId, Long projectId, Long drawingHisId) {
        Optional<DrawingHistory> opHis = drawingHistoryRepository.findById(drawingHisId);
        if (opHis.isPresent()) {
            Long drawingId = opHis.get().getDrawingId();
            Optional<Drawing> drawingOp = drawingRepository.findById(drawingId);
            if (drawingOp.isPresent()) {
                String entityNo = drawingOp.get().getDwgNo();
                String version = drawingOp.get().getLatestRev();
                BpmActivityInstanceBase actInst = bpmActInstRepository.findByProjectIdAndEntityIdAndVersion(projectId, drawingId, version);
                if (actInst != null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取创建图纸工作流程需要的信息
     */
    @Override
    public DrawingCreateTaskInfoDTO getCreateTaskInfo(Long orgId, Long projectId, Long id) {
        DrawingCreateTaskInfoDTO drawingCreateTaskInfoDTO = new DrawingCreateTaskInfoDTO();
        Optional<Drawing> drawingOp = drawingRepository.findById(id);
        if (drawingOp.isPresent()) {
            Drawing drawing = drawingOp.get();
            BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, drawing.getEntitySubType());

            BpmEntitySubType bpmEntitySubType = best;
            if (bpmEntitySubType == null) {
                throw new ValidationError();
            }
            String currentVersion = drawing.getLatestRev();
            String latestApprovedRev = drawing.getLatestApprovedRev();
            List<BpmProcess> processes = new ArrayList<>();
            BpmEntitySubType category = best;
            Set<BpmEntityTypeProcessRelation> setRelation = category.getEntitySubTypeProcessList();


            Iterator<BpmEntityTypeProcessRelation> iterator = setRelation.iterator();

            while (iterator.hasNext()) {
                BpmEntityTypeProcessRelation relation = iterator.next();
                if (relation.getStatus() == EntityStatus.ACTIVE) {
                    BpmProcess relationProcess = relation.getProcess();
                    processes.add(relationProcess);
                }
            }
            drawingCreateTaskInfoDTO.setProcesses(processes);
            drawingCreateTaskInfoDTO.setEntitySubType(best.getNameCn());
            drawingCreateTaskInfoDTO.setEntitySubTypeId(best.getId());
            drawingCreateTaskInfoDTO.setEntityId(id);
            drawingCreateTaskInfoDTO.setEntityNo(drawing.getDwgNo());
            drawingCreateTaskInfoDTO.setDocumentTitle(drawing.getDocumentTitle());
            drawingCreateTaskInfoDTO.setDrawingTitle(drawing.getDocumentTitle());
            drawingCreateTaskInfoDTO.setVersion(currentVersion);
            drawingCreateTaskInfoDTO.setLatestApprovedRev(latestApprovedRev);
        } else {
            throw new ValidationError();
        }
        return drawingCreateTaskInfoDTO;
    }


    @Override
    public UploadDrawingFileResultDTO uploadDrawingSubPipingZip(Long orgId, Long projectId, String file,
                                                                ContextDTO context, DrawingUploadDTO uploadDTO) {

        int errorCount = 0;
        int successCount = 0;

        List<String> noMatchFile = new ArrayList<>();

        String temporaryFileName = uploadDTO.getFileName();


        File diskFile = new File(temporaryDir, temporaryFileName);


        String fileFolder = temporaryDir + File.separator + CryptoUtils.uniqueId().toUpperCase();
        File folder = new File(fileFolder);
        folder.mkdirs();

        List<File> uploadFiles = new ArrayList<>();

        errorCount = handleUploadedZipFiles(fileFolder, diskFile, uploadFiles);


        for (File uFile : uploadFiles) {
            System.out.println(uFile.getName());
            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, uFile.getName());
            MockMultipartFile fileItem1 = null;
            try {
                IOUtils.copy(new FileInputStream(uFile), fileItem.getOutputStream());
                fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
            } catch (Exception e) {
                logger.error(uFile.getName() + "Can't be found");
                continue;
            }


            logger.error("图纸基础1 上传docs服务->开始");

            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
            logger.error("图纸基础1 上传docs服务->结束");
            TemporaryFileDTO temporaryFileDTO = tempFileResBody.getData();

            File diskFilePDF = new File(temporaryDir, tempFileResBody.getData().getName());

            FileMetadataDTO metadataPDF = FileUtils.readMetadata(diskFilePDF, FileMetadataDTO.class);
            String uploadFileNamePDF = metadataPDF.getFilename();
            String fileTypePDF = FileUtils.extname(uploadFileNamePDF);

            if (fileTypePDF.equals("." + BpmCode.FILE_TYPE_PDF)) {
                uploadDTO.setFileName(temporaryFileDTO.getName());
                boolean result = this.uploadDrawingSubPipingPdf(orgId, projectId,
                    uploadFileNamePDF, context, uploadDTO, uploadFeignAPI);
                if (result) {
                    successCount++;
                } else {
                    errorCount++;
                    noMatchFile.add(uploadFileNamePDF);
                }
            } else {
                errorCount++;
                noMatchFile.add(uploadFileNamePDF);
            }
        }

        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();
        dto.setErrorCount(errorCount);
        dto.setErrorList(noMatchFile);
        dto.setSuccessCount(successCount);
        return dto;
    }

    @Override
    public UploadDrawingFileResultDTO uploadDrawingSubPipingZipExternal(Long orgId, Long projectId, String file,
                                                                        Long drawingId, Long subdrawingId, ContextDTO context, DrawingUploadDTO uploadDTO) throws IOException {

        int errorCount = 0;
        int successCount = 0;
        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
        List<String> noMatchFile = new ArrayList<>();

        String temporaryFileName = uploadDTO.getFileName();

        // 取得已上传的临时文件
        File diskFile = new File(temporaryDir, temporaryFileName);
        // 读取上传文件的元数据
        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);

        String uploadFileName = metadata.getFilename();
        String fileFolder = temporaryDir + File.separator + CryptoUtils.uniqueId().toUpperCase();
        File folder = new File(fileFolder);
        folder.mkdirs();

        List<File> uploadFiles = new ArrayList<>();

        errorCount = handleUploadedZipFiles(fileFolder, diskFile, uploadFiles);

        for (File uFile : uploadFiles) {
            System.out.println(uFile.getName());
            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, uFile.getName());
            MockMultipartFile fileItem1 = null;
            try {
                IOUtils.copy(new FileInputStream(uFile), fileItem.getOutputStream());
                fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
            } catch (Exception e) {
                logger.error(uFile.getName() + "Can't be found");
                continue;
            }

            // 将文件上传到文档服务器
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
            TemporaryFileDTO temporaryFileDTO = tempFileResBody.getData();
            // 取得已上传的临时文件
            File diskFilePDF = new File(temporaryDir, tempFileResBody.getData().getName());
            // 读取上传文件的元数据
            FileMetadataDTO metadataPDF = FileUtils.readMetadata(diskFilePDF, FileMetadataDTO.class);
            String uploadFileNamePDF = metadataPDF.getFilename();
            String fileTypePDF = FileUtils.extname(uploadFileNamePDF);

            String drawingNo = uploadFileNamePDF.substring(0, uploadFileNamePDF.lastIndexOf("_"));
            String version = uploadFileNamePDF.substring(uploadFileNamePDF.lastIndexOf("_") + 1, uploadFileNamePDF.lastIndexOf("."));

            if (fileTypePDF.toLowerCase().equals("." + BpmCode.FILE_TYPE_PDF)) {
                boolean result = true;
                Optional<Drawing> drawing = drawingRepository.findByOrgIdAndProjectIdAndDwgNoAndStatus(orgId, projectId, drawingNo, EntityStatus.ACTIVE);
                if (drawing.isPresent()) {
                    DrawingUploadDTO drawingUploadDTO = new DrawingUploadDTO();
                    drawingUploadDTO.setVersion(version);
                    drawingUploadDTO.setFileName(tempFileResBody.getData().getName());

                    result = uploadPdfExternal(orgId, projectId, drawing.get().getId(), context.getOperator(), drawingUploadDTO);
                } else {
                    result = false;
                }

                if (result) {
                    successCount++;
                } else {
                    errorCount++;
                    noMatchFile.add(uploadFileNamePDF);
                }
            } else {
                errorCount++;
                noMatchFile.add(uploadFileNamePDF);
            }
        }

        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();
        dto.setErrorCount(errorCount);
        dto.setErrorList(noMatchFile);
        dto.setSuccessCount(successCount);
        return dto;
    }

    @Override
    public UploadDrawingFileResultDTO uploadDrawingSubPipingZipAmendment(Long orgId, Long projectId, String file,
                                                                         Long drawingId, Long subdrawingId, ContextDTO context, DrawingUploadDTO uploadDTO) throws IOException {

        int errorCount = 0;
        int successCount = 0;
        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
        List<String> noMatchFile = new ArrayList<>();

        String temporaryFileName = uploadDTO.getFileName();

        // 取得已上传的临时文件
        File diskFile = new File(temporaryDir, temporaryFileName);
        // 读取上传文件的元数据
        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);

        String uploadFileName = metadata.getFilename();
        String fileFolder = temporaryDir + File.separator + CryptoUtils.uniqueId().toUpperCase();
        File folder = new File(fileFolder);
        folder.mkdirs();

        List<File> uploadFiles = new ArrayList<>();

        errorCount = handleUploadedZipFiles(fileFolder, diskFile, uploadFiles);

        for (File uFile : uploadFiles) {
            System.out.println(uFile.getName());
            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, uFile.getName());
            MockMultipartFile fileItem1 = null;
            try {
                IOUtils.copy(new FileInputStream(uFile), fileItem.getOutputStream());
                fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
            } catch (Exception e) {
                logger.error(uFile.getName() + "Can't be found");
                continue;
            }

            // 将文件上传到文档服务器
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
            TemporaryFileDTO temporaryFileDTO = tempFileResBody.getData();
            // 取得已上传的临时文件
            File diskFilePDF = new File(temporaryDir, tempFileResBody.getData().getName());
            // 读取上传文件的元数据
            FileMetadataDTO metadataPDF = FileUtils.readMetadata(diskFilePDF, FileMetadataDTO.class);
            String uploadFileNamePDF = metadataPDF.getFilename();
            String fileTypePDF = FileUtils.extname(uploadFileNamePDF);

            String drawingNo = uploadFileNamePDF.substring(0, uploadFileNamePDF.lastIndexOf("_"));
            String version = uploadFileNamePDF.substring(uploadFileNamePDF.lastIndexOf("_") + 1, uploadFileNamePDF.lastIndexOf("."));

            if (fileTypePDF.toLowerCase().equals("." + BpmCode.FILE_TYPE_PDF)) {
                boolean result = true;
                Optional<DrawingAmendment> drawing = drawingAmendmentRepository.findByOrgIdAndProjectIdAndNoAndStatus(orgId, projectId, drawingNo, EntityStatus.ACTIVE);
                if (drawing.isPresent()) {
                    DrawingUploadDTO drawingUploadDTO = new DrawingUploadDTO();
                    drawingUploadDTO.setVersion(version);
                    drawingUploadDTO.setFileName(tempFileResBody.getData().getName());
                    result = uploadPdfAmendment(orgId, projectId, drawing.get().getId(), context.getOperator().getId(), drawingUploadDTO);
                } else {
                    result = false;
                }

                if (result) {
                    successCount++;
                } else {
                    errorCount++;
                    noMatchFile.add(uploadFileNamePDF);
                }
            } else {
                errorCount++;
                noMatchFile.add(uploadFileNamePDF);
            }
        }

        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();
        dto.setErrorCount(errorCount);
        dto.setErrorList(noMatchFile);
        dto.setSuccessCount(successCount);
        return dto;
    }

    @Override
    public Integer handleUploadedZipFiles(String fileFolder, File diskFile, List<File> uploadFiles) {
        ZipEntry entry = null;
        ZipFile zf = null;
        int errorCount = 0;
        try {
            zf = new ZipFile(diskFile);
            Enumeration entries = zf.entries();

            while (entries.hasMoreElements()) {
                entry = (ZipEntry) entries.nextElement();
                System.out.println("解压" + entry.getName());
                if (entry.isDirectory()) {
                    String dirPath = fileFolder + File.separator + entry.getName();
                    File dir = new File(dirPath);
                    dir.mkdirs();
                } else {

                    File f = new File(fileFolder + File.separator + entry.getName());
                    if (!f.exists()) {
                        String dirs = f.getParent();
                        File parentDir = new File(dirs);
                        parentDir.mkdirs();
                    }
                    f.createNewFile();

                    uploadFiles.add(f);


                    InputStream is = zf.getInputStream(entry);
                    FileOutputStream fos = new FileOutputStream(f);
                    int count;
                    byte[] buf = new byte[8192];
                    while ((count = is.read(buf)) != -1) {
                        fos.write(buf, 0, count);
                    }
                    is.close();
                    fos.close();
                }
            }
        } catch (Exception e) {
            errorCount++;
            e.printStackTrace(System.out);
        } finally {
            try {
                if (zf != null)
                    zf.close();
            } catch (IOException e) {
                logger.error("fileOutputStream closed exception", e);
            }
        }

        return errorCount;
    }


    @Override
    public List<TaskPrivilegeDTO> getProcessPrivileges(Long orgId, Long projectId, Long id) {

        Drawing drawing = drawingRepository.findById(id).get();
        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, drawing.getEntitySubType());

        BpmEntitySubType bpmEntitySubType = best;
        if (bpmEntitySubType == null) {
            throw new ValidationError("请设置图纸类型");
        }

        List<BpmProcess> processes = new ArrayList<BpmProcess>();

        Set<BpmEntityTypeProcessRelation> setRelation = bpmEntitySubType.getEntitySubTypeProcessList();
        Iterator<BpmEntityTypeProcessRelation> iteratorR = setRelation.iterator();
        while (iteratorR.hasNext()) {
            BpmEntityTypeProcessRelation relation = iteratorR.next();
            if (relation.getStatus() == EntityStatus.ACTIVE) {
                BpmProcess relationProcess = relation.getProcess();
                processes.add(relationProcess);
            }
        }

        List<Long> processIDs = new ArrayList<>();

        for (BpmProcess process : processes) {
            processIDs.add(process.getId());
        }

        List<BpmActivityTaskNodePrivilege> list = bpmActivityTaskNodePrivilegeRepository
            .findByOrgIdAndProjectIdAndProcessIdInAndStatus(orgId, projectId, processIDs, EntityStatus.ACTIVE);
        Map<String, Long> categoryMap = new HashMap<>();
        for (BpmActivityTaskNodePrivilege p : list) {
            if (p.getCategory() != null) {
                try {
                    DrawingEntryDelegate d = drawingEntryDelegateRepository
                        .findByDrawingIdAndPrivilegeAndStatus(id, UserPrivilege.getByName(p.getCategory()), EntityStatus.ACTIVE);
                    if (d != null) {
                        categoryMap.put(p.getCategory(), d.getUserId());
                    } else {
                        categoryMap.put(p.getCategory(), null);
                    }
                } catch (Error e) {
                    e.printStackTrace(System.out);
                }
            }
        }
        List<TaskPrivilegeDTO> result = new ArrayList<>();
        Iterator<String> iterator = categoryMap.keySet().iterator();
        while (iterator.hasNext()) {
            String category = iterator.next();
            String displayName = category;
            try {
                displayName = UserPrivilege.getByName(category).getDisplayName();
                if (displayName.endsWith("执行")) {
                    displayName = displayName.substring(0, displayName.length() - 2);
                }
            } catch (Error e) {
                e.printStackTrace(System.out);
            }
            result.add(new TaskPrivilegeDTO(category, displayName, categoryMap.get(category)));
        }
        return result;
    }

    @Override
    public boolean setProcessPrivileges(Long orgId, Long projectId, Long id, TaskPrivilegeDTO dTO, Long operatorId) {
        DrawingEntryDelegate drawingEntryDelegate = drawingEntryDelegateRepository
            .findByDrawingIdAndPrivilegeAndStatus(id, UserPrivilege.getByName(dTO.getEntitySubType()), EntityStatus.ACTIVE);
        if (drawingEntryDelegate == null) {
            drawingEntryDelegate = new DrawingEntryDelegate();
            drawingEntryDelegate.setCreatedAt();
            drawingEntryDelegate.setCreatedBy(operatorId);
            drawingEntryDelegate.setDrawingId(id);
            drawingEntryDelegate.setPrivilege(UserPrivilege.getByName(dTO.getEntitySubType()));
            drawingEntryDelegate.setStatus(EntityStatus.ACTIVE);
        }
        drawingEntryDelegate.setUserId(dTO.getAssignee());
        drawingEntryDelegate.setLastModifiedAt();
        drawingEntryDelegate.setLastModifiedBy(operatorId);
        System.out.println("图纸分配人员： " + "用户id：" + id);
        System.out.println("图纸分配人员： " + "权限：" + UserPrivilege.getByName(dTO.getEntitySubType()));
        drawingEntryDelegateRepository.save(drawingEntryDelegate);
        return true;
    }

    @Override
    public List<BpmEntitySubType> getDrawingCategoryList(Long orgId, Long projectId) {
        return drawingRepository.findDrawingCategoryList(orgId, projectId);
    }

    @Override
    public List<BpmEntitySubType> getExternalDrawingCategoryList(Long orgId, Long projectId) {
        return drawingRepository.findDrawingCategoryList(orgId, projectId);
    }

    @Override
    public boolean checkUpVersion(Long orgId, Long projectId, Long id, String version) {
        Optional<Drawing> op = drawingRepository.findById(id);
        if (op.isPresent()) {
            Drawing dwg = op.get();
            Optional<DrawingDetail> opD = drawingDetailRepository.findByDrawingIdAndRevAndStatus(id, dwg.getLatestRev(), EntityStatus.ACTIVE);
            if (opD.isPresent()) {
                DrawingDetail detail = opD.get();
                int current = detail.getRevOrder();
                int input = convertDrawVersionToOrder(version);
                if (input == -1) {
                    throw new ValidationError("请输入正确的版本.");
                } else if (current == input) {
                    throw new ValidationError("变更版本与当前版本一致.");
                } else if (current > input) {
                    throw new ValidationError("变更版本小于当前版本.");
                }
            }
        }
        return true;
    }

    @Override
    public DrawingFileDTO generateDWGFileWithCover(Long orgId, Drawing dl,
                                                   Project project, Long userid, boolean printQRCode) {
        BpmEntitySubType best = entitySubTypeService.getEntitySubType(project.getId(), dl.getEntitySubType());

        int coverPositionX = best == null ? 0 : best.getCoverPositionX();
        int coverPositionY = best == null ? 0 : best.getCoverPositionY();
        int coverScaleToFit = best == null ? 0 : best.getCoverScaleToFit();

        try {

            String savepath = temporaryDir + dl.getDwgNo() + "_" + "R" + dl.getLatestRev() + ".pdf";

            String[] files = new String[2];
            if (dl.getCoverId() != null) {
                String coverFilePath = protectedDir + dl.getCoverPath().substring(1);
                if (printQRCode) {

                    String coverPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";


                    String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                    String qrCode = dl.getQrCode();
                    QRCodeUtils.generateQRCodeNoBlank(qrCode, coverScaleToFit, "png", fileName);

                    PdfUtils.setImageToPdf(coverPDF, coverFilePath, fileName, coverPositionX, coverPositionY, coverScaleToFit, coverScaleToFit);


                    files[0] = generateSignatureCover(
                        orgId,
                        project.getId(),
                        dl,
                        coverPDF
                    );
                } else {
                    files[0] = coverFilePath;
                }

                File diskFile = null;
                DrawingHistory his = drawingHistoryRepository.findByDrawingIdAndVerison(dl.getId(), dl.getLatestRev());
                if (FileUtils.extname(his.getFileName()).toLowerCase().equals("." + BpmCode.FILE_TYPE_PDF)) {
                    files[1] = protectedDir + his.getFilePath().substring(1);
                    PdfUtils.mergePdfFiles(files, savepath);
                    diskFile = new File(savepath);
                } else {
                    diskFile = new File(files[0]);
                }

                System.out.println(diskFile.getName());
                DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                    MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

                IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());


                logger.error("图纸基础2 上传docs服务->开始");
                MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
                JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                    .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                logger.error("图纸基础2 上传docs服务->结束");
                logger.error("图纸基础2 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), project.getId().toString(),
                    tempFileResBody.getData().getName(), new FilePostDTO());
                logger.error("图纸基础2 保存docs服务->结束");
                DrawingFileDTO fdto = new DrawingFileDTO();
                fdto.setFileId(LongUtils.parseLong(fileESResBody.getData().getId()));
                fdto.setFileName(fileESResBody.getData().getName());
                fdto.setFilePath(fileESResBody.getData().getPath());
                fdto.setQrCode(dl.getQrCode());
                return fdto;
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        return null;
    }


    @Override
    public boolean checkLock(Long orgId, Long projectId, Long id) {
        List<BpmActivityInstanceBase> actInstList = bpmActInstRepository.findByProjectIdAndEntityIdAndFinishStateAndSuspensionState(
            projectId, id, ActInstFinishState.NOT_FINISHED, SuspensionState.ACTIVE);
        if (actInstList.isEmpty()) {
            return false;
        }
        Drawing dwg = drawingRepository.findById(id).get();
        boolean locked = true;
        for (BpmActivityInstanceBase actInst : actInstList) {
            List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInst.getId());
            if (!ruTasks.isEmpty()) {
                BpmRuTask ruTask = ruTasks.get(0);
                if (
                    taskRuleCheckService.isDrawingDesignTaskNode(ruTask.getTaskDefKey())
                        || taskRuleCheckService.isDrawingModifyTaskNode(ruTask.getTaskDefKey())
                        || taskRuleCheckService.isRedMarkDesignTaskNode(ruTask.getTaskDefKey())
                ) {
                    locked = false;
                }
            }
        }
        dwg.setLocked(locked);
        drawingRepository.save(dwg);
        return true;
    }


    /**
     * 保存下载的DrawingList文件
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @param operatorId
     * @return
     */

    @Override
    public File saveDownloadFile(Long orgId, Long projectId, DrawingCriteriaDTO criteriaDTO, Long operatorId) {

        String temporaryFileName = FileUtils.copy(
            this.getClass()
                .getClassLoader()
                .getResourceAsStream("templates/export-piping-drawing-list.xlsx"),
            temporaryDir,
            operatorId.toString()
        );


        File excel;
        Workbook workbook;

        try {
            excel = new File(temporaryDir, temporaryFileName);
            workbook = WorkbookFactory.create(excel);
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }

        Sheet sheet = workbook.getSheet("PIPING");

        int rowNum = BpmCode.SD_PP_DATA_START_ROW;
        int seq = 1;

        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);

        List<Map<String, Object>> drawingListPipings = drawingRepository.getXlsList(orgId, projectId);

        /*序号  S/N
        图号 Doc & DWG. NO.
        文件名称 Document & Drawing Title	"
        设计开始时间 Engineering Start Date"
        "设计结束时间 Engineering Finish Date"
        最新版本    LAST REV.
        计划提交建造时间    Delivery Date(IFC)	IFC
		实际发图日期 Actural Drawing Issue Date
		版本号 Rev.
		生产接收日期
		发放记录卡编号 Issue Card No.
		回收记录 Return Record
		"设计变更评审单号 Design Change Review form"
		"变更通知单编号 Change Notice No.
		"	配布份数
		打印份数
		用纸量（A3）
		纸张数量
		图纸审核表编号*/


        String str = "";
        for (Map<String, Object> dlp : drawingListPipings) {
            Row row = WorkbookUtils.getRow(sheet, rowNum++);

            if (rowNum >= BpmCode.SD_PP_TEMPLATE_ROW_COUNT + BpmCode.SD_PP_DATA_START_ROW) {
                WorkbookUtils.copyRow(sheet.getRow(BpmCode.SD_PP_DATA_START_ROW), row);
            }
            if (dlp == null) {
                continue;
            }

            String sn = Integer.toString(seq++);
            str = String.valueOf(dlp.get("dwgNo"));
            if (str.equals("null")) str = "";
            String dwgNo = str;
            str = String.valueOf(dlp.get("drawingTitle"));
            if (str.equals("null")) str = "";
            String dwgTitle = str;
            str = String.valueOf(dlp.get("engineeringStartDate"));
            if (str.equals("null")) str = "";
            String engStartDate = str;
            str = String.valueOf(dlp.get("engineeringFinishDate"));
            if (str.equals("null")) str = "";
            String engEndDate = str;
            str = String.valueOf(dlp.get("latestRev"));
            if (str.equals("null")) str = "";
            String latestRev = str;
            str = String.valueOf(dlp.get("deliveryDate"));
            if (str.equals("null")) str = "";
            String afcDate = str;
            str = String.valueOf(dlp.get("acturalDrawingIssuDat"));
            if (str.equals("null")) str = "";
            String actualAfcDate = str;
            str = String.valueOf(dlp.get("rev"));
            if (str.equals("null")) str = "";
            String rev = str;
            str = String.valueOf(dlp.get("productionReceivingDate"));
            if (str.equals("null")) str = "";
            String receiveDate = str;
            str = String.valueOf(dlp.get("issueCardNo"));
            if (str.equals("null")) str = "";
            String issueCardNo = str;
            str = String.valueOf(dlp.get("returnRecord"));
            if (str.equals("null")) str = "";
            String returnRecord = str;
            str = String.valueOf(dlp.get("designChangeReviewForm"));
            if (str.equals("null")) str = "";
            String designChangeReviewForm = str;
            str = String.valueOf(dlp.get("changeNoticeNo"));
            if (str.equals("null")) str = "";
            String changeNoticeNo = str;
            str = String.valueOf(dlp.get("quantity"));
            if (str.equals("null")) str = "";
            String issueQty = str;
            str = String.valueOf(dlp.get("printing"));
            if (str.equals("null")) str = "";
            String printQty = str;
            str = String.valueOf(dlp.get("getPaperUse"));
            if (str.equals("null")) str = "";
            String paperQty = str;
            str = String.valueOf(dlp.get("paperAmount"));
            if (str.equals("null")) str = "";
            String usePaperQty = str;
            str = String.valueOf(dlp.get("designChangeReviewForm"));
            if (str.equals("null")) str = "";
            String dwgReviewNo = str;


            WorkbookUtils.getCell(row, 0).setCellValue(sn);
            WorkbookUtils.getCell(row, 1).setCellValue(dwgNo);
            WorkbookUtils.getCell(row, 2).setCellValue(dwgTitle);
            WorkbookUtils.getCell(row, 3).setCellValue(engStartDate);
            WorkbookUtils.getCell(row, 4).setCellValue(engEndDate);
            WorkbookUtils.getCell(row, 5).setCellValue(latestRev);
            WorkbookUtils.getCell(row, 6).setCellValue(afcDate);
            WorkbookUtils.getCell(row, 7).setCellValue(actualAfcDate);
            WorkbookUtils.getCell(row, 8).setCellValue(rev);
            WorkbookUtils.getCell(row, 9).setCellValue(receiveDate);
            WorkbookUtils.getCell(row, 10).setCellValue(issueCardNo);
            WorkbookUtils.getCell(row, 11).setCellValue(returnRecord);
            WorkbookUtils.getCell(row, 12).setCellValue(designChangeReviewForm);
            WorkbookUtils.getCell(row, 13).setCellValue(changeNoticeNo);
            WorkbookUtils.getCell(row, 14).setCellValue(issueQty);
            WorkbookUtils.getCell(row, 15).setCellValue(printQty);
            WorkbookUtils.getCell(row, 16).setCellValue(paperQty);
            WorkbookUtils.getCell(row, 17).setCellValue(usePaperQty);
            WorkbookUtils.getCell(row, 18).setCellValue(dwgReviewNo);

        }

        try {
            WorkbookUtils.save(workbook, excel.getAbsolutePath());
            return excel;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            throw new BusinessError();
        }
    }


    /**
     * 查找图纸最新版本下的所有子图纸。
     *
     * @param drawingId
     * @return
     */
    @Override
    public List<SubDrawing> findLastSubByDrawingId(Long drawingId) {
        Optional<Drawing> op = drawingRepository.findById(drawingId);
        if (op.isPresent()) {
            Drawing dwg = op.get();
            Optional<DrawingDetail> opD = drawingDetailRepository.findByDrawingIdAndRevAndStatus(drawingId, dwg.getLatestRev(), EntityStatus.ACTIVE);
            if (opD.isPresent()) {
                DrawingDetail detail = opD.get();
                List<SubDrawing> result = new ArrayList<>();
                List<SubDrawing> subList = subDrawingRepository.findByDrawingIdAndRevOrderLE(dwg.getId(), detail.getRevOrder());
                Map<String, Long> subResult = new HashMap<>();
                for (SubDrawing sub : subList) {
                    subResult.put(sub.getSubDrawingNo() + "_" + sub.getPageNo(), sub.getId());
                }
                for (SubDrawing sub : subList) {
                    if (subResult.containsValue(sub.getId())) {
                        result.add(sub);
                    }
                }
                return result;
            }
        }
        return null;
    }


    /**
     * 查找包含图纸文件的子图纸。
     *
     * @param id
     * @return
     */
    @Override
    public List<SubDrawing> findByDrawingIdAndStatusAndFilePathNotNull(Long id) {
        List<SubDrawing> result = new ArrayList<>();
        List<SubDrawing> list = this.findSubByDrawingId(id);
        for (SubDrawing sub : list) {
            if (sub.getFilePath() != null
                && !sub.getFilePath().equals("")) {
                result.add(sub);
            }
        }
        return result;
    }


    private boolean uploadDrawingSubPipingPdf(Long orgId, Long projectId, String file,
                                              ContextDTO context, DrawingUploadDTO uploadDTO, UploadFeignAPI uploadFeignAPI) {

        boolean result = true;

        String regEx = "_(\\d{1,3})_([\\s\\S]*)$";
        String[] str = file.split("\\.");
        String fileType = str[str.length - 1];
        String fileName = "";
        for (int i = 0; i < str.length - 1; i++) {
            fileName += str[i] + ".";
        }
        fileName = fileName.substring(0, fileName.length() - 1);


        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
        drawingUploadZipFileHistory.setCreatedAt();
        drawingUploadZipFileHistory.setOperator(context.getOperator().getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setFileName(fileName);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);

        if (fileType.equals(BpmCode.FILE_TYPE_PDF)) {

            String drawingNo = "";
            String pageNo = "";
            String drawingVersion = "";

            Pattern pattern = Pattern.compile(regEx);
            Matcher matcher = pattern.matcher(fileName);
            MatchResult ms = null;
            while (matcher.find()) {
                ms = matcher.toMatchResult();


                pageNo = ms.group(1);
                drawingVersion = ms.group(2);
                drawingNo = fileName.substring(0, fileName.indexOf(ms.group()));
            }

            SubDrawing subDrawing = null;

            if (drawingNo.equals("")
                || pageNo.equals("")
                || drawingVersion.equals("")) {
                drawingUploadZipFileHistory.setFailedCount(1);
                result = false;
            } else {
                subDrawing = subDrawingRepository.findByProjectIdAndSubDrawingNoAndPageNoAndSubDrawingVersionAndStatus(
                    projectId, drawingNo, Integer.valueOf(pageNo), drawingVersion, EntityStatus.ACTIVE);
            }

            if (subDrawing != null && drawingNo.equals(subDrawing.getSubDrawingNo())
                && pageNo.equals("" + subDrawing.getPageNo())) {
                logger.error("图纸基础6 保存docs服务->开始");
                JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                    uploadDTO.getFileName(), new FilePostDTO());
                FileES f = responseBody.getData();
                logger.error("图纸基础5 保存docs服务->结束");
                String qrCode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();

                List<SubDrawingHistory> hisl = subDrawingHisRepository.findBySubDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersion(
                    subDrawing.getId(), drawingNo, Integer.valueOf(pageNo), drawingVersion);
                if (!hisl.isEmpty()) {

                    boolean newup = true;
                    for (SubDrawingHistory his : hisl) {
                        if (his.getFileId() == null) {
                            newup = false;
                            his.setOperator(context.getOperator().getId());
                            his.setQrCode(qrCode);
                            his.setFileId(LongUtils.parseLong(f.getId()));
                            his.setFileName(f.getName());
                            his.setMemo(uploadDTO.getComment());
                            his.setFilePath(f.getPath());
                            subDrawingHisRepository.save(his);
                        }
                    }

                    if (newup) {
                        SubDrawingHistory his = new SubDrawingHistory();
                        his.setDrawingDetailId(subDrawing.getDrawingDetailId());
                        his.setSubDrawingId(subDrawing.getId());
                        his.setSubDrawingVersion(subDrawing.getSubDrawingVersion());
                        his.setSubDrawingNo(subDrawing.getSubDrawingNo());
                        his.setPageNo(subDrawing.getPageNo());
                        his.setSubDrawingVersion(subDrawing.getSubDrawingVersion());
                        his.setCreatedAt();
                        his.setOrgId(orgId);
                        his.setProjectId(projectId);
                        his.setStatus(EntityStatus.ACTIVE);
                        his.setOperator(context.getOperator().getId());
                        his.setQrCode(qrCode);
                        his.setFileId(LongUtils.parseLong(f.getId()));
                        his.setFileName(f.getName());
                        his.setMemo(uploadDTO.getComment());
                        his.setFilePath(f.getPath());
                        subDrawingHisRepository.save(his);
                    }
                    subDrawing.setFileName(f.getName());
                    subDrawing.setFilePath(f.getPath());
                    subDrawing.setFileId(LongUtils.parseLong(f.getId()));
                    subDrawing.setQrCode(qrCode);
                    subDrawingRepository.save(subDrawing);

                    drawingUploadZipFileHistory.setSuccessCount(1);
                    drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(f.getId()));
                    drawingUploadZipFileHistory.setFileName(f.getName());
                    drawingUploadZipFileHistory.setFilePath(f.getPath());
                } else {
                    drawingUploadZipFileHistory.setFailedCount(1);
                    result = false;
                }
            } else {
                drawingUploadZipFileHistory.setFailedCount(1);
                result = false;
            }
        }

        drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

        return result;
    }

    @Override
    public boolean clearParentQrCode(Long subDrawingId) {
        Optional<SubDrawing> subDrawingOp = subDrawingRepository.findById(subDrawingId);
        if (subDrawingOp.isPresent()) {
            SubDrawing sub = subDrawingOp.get();
            Optional<Drawing> drawingOp = drawingRepository.findById(sub.getDrawingId());
            if (drawingOp.isPresent()) {
                Drawing parent = drawingOp.get();
                parent.setQrCode(null);
                parent.setFileId(null);
                drawingRepository.save(parent);
            }
        }
        return false;
    }

    @Override
    public int convertDrawVersionToOrder(String str) {
        if (str == null) {
            str = "0";
        }
        if (StringUtils.isNumeric(str)) {
            if (str.length() < 3) {
                str = removeBeforZeroAdd(str);
                str = str + "00";
            } else if (str.length() > 4) {
                return -1;
            }
            return Integer.parseInt(str) + 20000;
        } else {

            if (str.length() == 1) {
                str = str + "00";
            }
            if (str.length() == 3) {
                String mainVer = str.substring(0, 1);
                String subVer = str.substring(1, 3);
                int asc = StringUtils.getAsc(mainVer);
                if (asc > 90 || asc < 65) {
                    return -1;
                } else {

                }
                if (StringUtils.isNumeric(subVer)) {
                    return 10000 + (asc - 64) * 100 + Integer.parseInt(subVer);
                } else {
                    return -1;
                }
            } else {
                return -1;
            }
        }
    }

    @Override
    public String removeBeforZeroAdd(String str) {
        int len = str.length();
        int index = -1;

        char strs[] = str.toCharArray();
        for (int i = 0; i < len; i++) {
            if ('0' != strs[i]) {
                index = i;
                break;
            }
        }
        String strLast = "0";
        if (index >= 0) {
            strLast = str.substring(index, len);
        }
        return strLast;
    }

    @Override
    public String generateSignatureCover(
        Long orgId,
        Long projectId,
        Drawing dl,
        String coverPDF
    ) throws IOException {

        String coverPath = coverPDF;

        BpmActivityInstanceBase actInst = bpmActInstRepository
            .findByProjectIdAndEntityIdAndProcessAndVersionAndSuspensionStateAndStatus(
                projectId,
                dl.getId(),
                BpmsProcessNameEnum.DRAWING_PARTIAL_UPDATE.getType(),
                dl.getLatestRev(),
                SuspensionState.ACTIVE,
                EntityStatus.ACTIVE
            );
        if (actInst == null) {
            actInst = bpmActInstRepository
                .findByProjectIdAndEntityIdAndProcessAndVersionAndSuspensionStateAndStatus(
                    projectId,
                    dl.getId(),
                    BpmsProcessNameEnum.DRAWING_INTEGRAL_UPDATE.getType(),
                    dl.getLatestRev(),
                    SuspensionState.ACTIVE,
                    EntityStatus.ACTIVE
                );
        }
        if (actInst == null) {
            actInst = bpmActInstRepository
                .findByProjectIdAndEntityIdAndProcessAndVersionAndSuspensionStateAndStatus(
                    projectId,
                    dl.getId(),
                    BpmsProcessNameEnum.ENGINEERING.getType(),
                    dl.getLatestRev(),
                    SuspensionState.ACTIVE,
                    EntityStatus.ACTIVE
                );
        }
        if (actInst == null) {
            return coverPath;
        }

        BpmHiTaskinst drawTask = hiTaskinstRepository.findFirstByActInstIdAndTaskDefKeyOrderByEndTimeDesc(
            actInst.getId(),
            BpmTaskDefKey.USERTASK_DRAWING_DESIGN.getType()
        );
        if (drawTask != null && drawTask.getAssignee() != null) {
            coverPath = setUserSignatureToCover(
                orgId,
                projectId,
                dl,
                drawTask,
                DrawingSignatureType.DRAW,
                coverPath
            );
        }

        BpmHiTaskinst checkTask = hiTaskinstRepository.findFirstByActInstIdAndTaskDefKeyOrderByEndTimeDesc(
            actInst.getId(),
            BpmTaskDefKey.USERTASK_DRAWING_CHECK.getType()
        );
        if (checkTask != null && checkTask.getAssignee() != null) {
            coverPath = setUserSignatureToCover(
                orgId,
                projectId,
                dl,
                checkTask,
                DrawingSignatureType.CHECK,
                coverPath
            );
        }

        BpmHiTaskinst signTask = hiTaskinstRepository.findFirstByActInstIdAndTaskDefKeyOrderByEndTimeDesc(
            actInst.getId(),
            BpmTaskDefKey.USERTASK_DRAWING_SIGN.getType()
        );
        if (signTask != null && signTask.getAssignee() != null) {
            coverPath = setUserSignatureToCover(
                orgId,
                projectId,
                dl,
                signTask,
                DrawingSignatureType.SIGN,
                coverPath
            );
        }

        BpmHiTaskinst reviewTask = hiTaskinstRepository.findFirstByActInstIdAndTaskDefKeyOrderByEndTimeDesc(
            actInst.getId(),
            BpmTaskDefKey.USERTASK_DRAWING_REVIEW.getType()
        );
        if (reviewTask != null && reviewTask.getAssignee() != null) {
            coverPath = setUserSignatureToCover(
                orgId,
                projectId,
                dl,
                reviewTask,
                DrawingSignatureType.REVIEW,
                coverPath
            );
        }

        BpmHiTaskinst approveTask = hiTaskinstRepository.findFirstByActInstIdAndTaskDefKeyOrderByEndTimeDesc(
            actInst.getId(),
            BpmTaskDefKey.USERTASK_DRAWING_APPROVE.getType()
        );
        if (approveTask != null && approveTask.getAssignee() != null) {
            coverPath = setUserSignatureToCover(
                orgId,
                projectId,
                dl,
                approveTask,
                DrawingSignatureType.APPROVE,
                coverPath
            );
        }

        return coverPath;
    }

    private String setUserSignatureToCover(
        Long orgId,
        Long projectId,
        Drawing dl,
        BpmHiTaskinst assignee,
        DrawingSignatureType signatureType,
        String coverPath
    ) throws IOException {

        String newPath = coverPath;
        System.out.println("****" + assignee.getAssignee() + "******" + signatureType.name() + "********");
        String[] assignees = assignee.getAssignee().split(",");
        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dl.getEntitySubType());
        List<DrawingSignatureCoordinate> corrdinate = drawingSignatureCoordinateRepository
            .findByOrgIdAndProjectIdAndEntitySubTypeIdAndStatusAndDrawingSignatureType(
                orgId,
                projectId,
                best.getId(),
                EntityStatus.ACTIVE,
                signatureType
            );
        int count = assignees.length - corrdinate.size() > 0 ? corrdinate.size() : assignees.length;
        for (int i = 0; i < count; i++) {
            UserSignature signature = userSignatureRepository.findByUserId(LongUtils.parseLong(assignees[i]));
            if (signature != null) {

                String signaturePath = protectedDir + signature.getFilePath().substring(1);
                String signatureGrayPath = temporaryDir + CryptoUtils.uniqueId() + ".PNG";
                String signatureCover = temporaryDir + CryptoUtils.uniqueId() + ".pdf";

                ImageUtils.toGray(
                    signaturePath,
                    signatureGrayPath,
                    null
                );


                PdfUtils.setImageToPdf(
                    signatureCover,
                    newPath,
                    signatureGrayPath,
                    corrdinate.get(i).getDrawingPositionX(),
                    corrdinate.get(i).getDrawingPositionY(),
                    corrdinate.get(i).getDrawingScaleToFit(),
                    corrdinate.get(i).getDrawingScaleToFit()
                );
                newPath = signatureCover;
            }
        }
        return newPath;
    }

    /**
     * 查找当前图纸最新版本下的正在运行的子图纸。
     *
     * @param id
     * @return
     */
    @Override
    public List<SubDrawing> findSubByDrawingId(Long id) {
        Optional<Drawing> op = drawingRepository.findById(id);
        if (!op.isPresent()) return null;
        Drawing dwg = op.get();
        Optional<DrawingDetail> opD = drawingDetailRepository.findByDrawingIdAndRevAndStatus(id, dwg.getLatestRev(), EntityStatus.ACTIVE);
        if (!opD.isPresent()) return null;

        DrawingDetail detail = opD.get();
        List<SubDrawing> result = new ArrayList<>();
        List<SubDrawing> subList = subDrawingRepository.findByDrawingIdAndStatusAndRevOrderLE(dwg.getId(), EntityStatus.ACTIVE, detail.getRevOrder());
        Map<String, Long> subResult = new HashMap<>();
        for (SubDrawing sub : subList) {
            subResult.put(sub.getSubDrawingNo() + "_" + sub.getPageNo(), sub.getId());
        }
        for (SubDrawing sub : subList) {

            if (subResult.containsValue(sub.getId())) {
                result.add(sub);
            }
        }
        return result;
    }

    @Override
    public SubDrawing getLastVersionSubDrawing(List<SubDrawing> drawingList, String subNo) {
        int maxInt = 0;
        SubDrawing subDrawing = null;
        for (SubDrawing sub : drawingList) {
            if (sub.getSubNo().equals(subNo)
                && sub.getStatus() == EntityStatus.ACTIVE) {
                int i = convertDrawVersionToOrder(sub.getSubDrawingVersion());
                if (i > maxInt) {
                    maxInt = i;
                    subDrawing = sub;
                }
            }
        }
        return subDrawing;
    }


    @Override
    public ExecResultDTO checkRuTask(ExecResultDTO execResult) {
        BpmRuTask ruTask = null;
        if (execResult.getRuTask() == null) {
            ruTask = ruTaskRepository.findById(execResult.getTodoTaskDTO().getId()).orElse(null);
            if (ruTask == null) {
                execResult.setExecResult(false);
                return execResult;
            }
            execResult.setRuTask(ruTask);
        }

        return execResult;
    }


    /**
     * 更新或修改图纸文件。
     *
     * @param orgId       组织id
     * @param project     项目
     * @param dwg         图纸信息
     * @param processId   工序信息
     * @param operatorDTO 操作人
     * @param lockFlag    锁定状态
     */


    @Override
    public DrawingPackageReturnDTO packSubFiles(Long orgId, Project project,
                                                Long actInstId,
                                                OperatorDTO operatorDTO,
                                                Boolean lockFlag,
                                                Drawing dwg,
                                                Long processId,
                                                DrawingDetail drawingDetail) {

        DrawingPackageReturnDTO drawingPackageReturnDTO = new DrawingPackageReturnDTO();


        DrawingFileDTO drawingFileDto = null;
        drawingFileDto = generateDrawingReport(orgId, actInstId, dwg, project, operatorDTO.getId(), lockFlag, lockFlag, drawingDetail);

        if (drawingFileDto == null) return null;


//        if (dwg.getCurrentProcessNameEn() != null && !dwg.getCurrentProcessNameEn().equals("DRAWING-REDMARK")) {
//
//            if (lockFlag) {
//                drawingDetail.setIssueFileId(drawingFileDto.getFileId());
//                drawingDetail.setIssueFileName(dwg.getDwgNo() + "_" + "R" + dwg.getLatestRev());
//                drawingDetail.setIssueFilePath(drawingFileDto.getFilePath());
//            } else {
//
//
//                drawingDetail.setFileId(drawingFileDto.getFileId());
//                drawingDetail.setFileName(dwg.getDwgNo() + "_" + "R" + dwg.getLatestRev());
//                drawingDetail.setFilePath(drawingFileDto.getFilePath());
//            }
//
//            drawingDetail.setQrCode(drawingFileDto.getQrCode());
//
//            drawingPackageReturnDTO.setDrawingDetail(drawingDetail);
//
//        }


        BpmEntityDocsMaterials bpmDoc = docsMaterialsRepository.findByEntityNoAndActInstanceId(dwg.getDwgNo(), actInstId);
        if (bpmDoc == null) {
            bpmDoc = new BpmEntityDocsMaterials();
            bpmDoc.setProjectId(project.getId());
            bpmDoc.setCreatedAt();
            bpmDoc.setStatus(EntityStatus.ACTIVE);
        }
        bpmDoc.setEntityNo(dwg.getDwgNo());
        bpmDoc.setLastModifiedAt(new Date());
        bpmDoc.setProcessId(processId);
        bpmDoc.setEntityId(drawingDetail.getDrawingId());
        bpmDoc.setActInstanceId(actInstId);
        bpmDoc.setType(ActInstDocType.DESIGN_DRAWING);

        List<ActReportDTO> list = new ArrayList<>();
        ActReportDTO reportDto = new ActReportDTO();
        reportDto.setFileId(drawingFileDto.getFileId());
        reportDto.setReportQrCode(drawingFileDto.getQrCode());
        reportDto.setReportNo(drawingFileDto.getFileName());
        list.add(reportDto);
        bpmDoc.setJsonDocs(list);
        BeanUtils.copyProperties(drawingFileDto, drawingPackageReturnDTO);
        drawingPackageReturnDTO.setBpmDoc(bpmDoc);
        return drawingPackageReturnDTO;
    }

    @Override
    public DrawingPackageReturnDTO packMonoFiles(
        Long orgId,
        Project project,
        OperatorDTO operatorDTO,
        Boolean lockFlag,
        Drawing dwg,
        Long processId,
        Long actInstId,
        DrawingDetail drawingDetail
    ) {
        DrawingPackageReturnDTO drawingPackageReturnDTO = new DrawingPackageReturnDTO();

        List<DrawingHistory> drawingHisList = drawingHistoryRepository.findByDrawingIdOrderByCreatedAtDesc(dwg.getId());
        for (DrawingHistory drawHis : drawingHisList) {

            if (dwg.getLatestRev() == null || dwg.getLatestRev().equals(drawHis.getVersion())) {
                if (lockFlag) {
//                    drawHis.setIssueFlag(true);
                }
                DrawingFileDTO dto = null;


                dto = generateDWGFileWithCover(orgId, dwg, project, operatorDTO.getId(), lockFlag);
                if (dto != null) {
                    if (lockFlag) {
                        drawingDetail.setIssueFileId(dto.getFileId());
                        drawingDetail.setIssueFileName(dwg.getDwgNo() + "_" + "R" + dwg.getLatestRev());
                        drawingDetail.setIssueFilePath(dto.getFilePath());
                    } else {
//                        drawingDetail.setFileId(dto.getFileId());
//                        drawingDetail.setFileName(dwg.getDwgNo() + "_" + "R" + dwg.getLatestRev());
//                        drawingDetail.setFilePath(dto.getFilePath());
                    }
                    drawingDetail.setQrCode(dto.getQrCode());
                    drawingPackageReturnDTO.setDrawingDetail(drawingDetail);


                    dwg.setFileId(dto.getFileId());
                    dwg.setFileName(dwg.getDwgNo() + "_" + "R" + dwg.getLatestRev());
                    dwg.setFilePath(dto.getFilePath());
                    drawingPackageReturnDTO.setDrawing(dwg);


                    BpmEntityDocsMaterials bpmDoc = docsMaterialsRepository.findByEntityNoAndActInstanceId(dwg.getDwgNo(), actInstId);
                    if (bpmDoc == null) {
                        bpmDoc = new BpmEntityDocsMaterials();
                        bpmDoc.setProjectId(project.getId());
                        bpmDoc.setCreatedAt();
                        bpmDoc.setStatus(EntityStatus.ACTIVE);
                    }
                    bpmDoc.setEntityNo(dwg.getDwgNo());
                    bpmDoc.setProcessId(processId);
                    bpmDoc.setEntityId(drawingDetail.getDrawingId());
                    bpmDoc.setActInstanceId(actInstId);
                    bpmDoc.setType(ActInstDocType.DESIGN_DRAWING);

                    List<ActReportDTO> list = new ArrayList<>();
                    ActReportDTO reportDto = new ActReportDTO();
                    reportDto.setFileId(dto.getFileId());
                    reportDto.setReportQrCode(dto.getQrCode());
                    reportDto.setReportNo(dto.getFileName());
                    list.add(reportDto);
                    bpmDoc.setJsonDocs(list);
                    drawingPackageReturnDTO.setBpmDoc(bpmDoc);
                } else {
                    if (lockFlag) {
                        drawingDetail.setIssueFileId(drawHis.getFileId());
                        drawingDetail.setIssueFileName(dwg.getDwgNo() + "_" + "R" + dwg.getLatestRev());
                        drawingDetail.setIssueFilePath(drawHis.getFilePath());
                    } else {
//                        drawingDetail.setFileId(drawHis.getFileId());
//                        drawingDetail.setFileName(dwg.getDwgNo() + "_" + "R" + dwg.getLatestRev());
//                        drawingDetail.setFilePath(drawHis.getFilePath());
                    }
                    drawingDetail.setQrCode(drawHis.getQrCode());
                    drawingPackageReturnDTO.setDrawingDetail(drawingDetail);


                    BpmEntityDocsMaterials bpmDoc = docsMaterialsRepository.findByEntityNoAndActInstanceId(dwg.getDwgNo(), actInstId);
                    if (bpmDoc == null) {
                        bpmDoc = new BpmEntityDocsMaterials();
                        bpmDoc.setProjectId(project.getId());
                        bpmDoc.setCreatedAt();
                        bpmDoc.setStatus(EntityStatus.ACTIVE);
                    }
                    bpmDoc.setEntityNo(dwg.getDwgNo());
                    bpmDoc.setProcessId(processId);
                    bpmDoc.setEntityId(drawHis.getDrawingId());
                    bpmDoc.setActInstanceId(actInstId);
                    bpmDoc.setType(ActInstDocType.DESIGN_DRAWING);
                    List<ActReportDTO> list = new ArrayList<>();
                    ActReportDTO reportDto = new ActReportDTO();
                    reportDto.setFileId(drawHis.getFileId());
                    reportDto.setReportQrCode(drawHis.getQrCode());
                    reportDto.setReportNo(drawHis.getFileName());
                    list.add(reportDto);
                    bpmDoc.setJsonDocs(list);
                    drawingPackageReturnDTO.setBpmDoc(bpmDoc);
                }
            } else {
//                drawHis.setIssueFlag(false);
            }

        }
        drawingPackageReturnDTO.setDrawingHisList(drawingHisList);
        return drawingPackageReturnDTO;
    }

    /**
     * 生成图纸报表
     *
     * @param orgId   组织ID
     * @param project 项目ID
     * @param dl
     * @param project
     * @param userid
     */
    @Override
    public DrawingFileDTO generateDrawingReport(Long orgId,
                                                Long actInstId,
                                                Drawing dl,
                                                Project project,
                                                Long userid,
                                                boolean printCoverQRCode,
                                                boolean printDwgQRCode,
                                                DrawingDetail drawingDetail) {
        DrawingFileDTO drawingFileDTO = new DrawingFileDTO();
        BpmEntitySubType bpmEntitySubType = entitySubTypeService.getEntitySubType(project.getId(), dl.getEntitySubType());

        if (bpmEntitySubType == null) throw new BusinessError("Drawing CATEGORY NOT DEFINED");

        int positionX = 0;
        if (!"".equals(bpmEntitySubType.getDrawingPositionX() + "")) {
            positionX = bpmEntitySubType.getDrawingPositionX();
        }

        int positionY = 0;
        if (!"".equals(bpmEntitySubType.getDrawingPositionY() + "")) {
            positionY = bpmEntitySubType.getDrawingPositionY();
        }
        int scaleToFit = 0;
        if (!"".equals(bpmEntitySubType.getDrawingScaleToFit() + "")) {
            scaleToFit = bpmEntitySubType.getDrawingScaleToFit();
        }

        int shortX = 0;
        if (!"".equals(bpmEntitySubType.getDrawingShortX() + "")) {
            shortX = bpmEntitySubType.getDrawingShortX();
        }
        int shortY = 0;
        if (!"".equals(bpmEntitySubType.getDrawingShortY() + "")) {
            shortY = bpmEntitySubType.getDrawingShortY();
        }


        int coverPositionX = 0;
        if (!"".equals(bpmEntitySubType.getCoverPositionX() + "")) {
            coverPositionX = bpmEntitySubType.getCoverPositionX();
        }
        int coverPositionY = 0;
        if (!"".equals(bpmEntitySubType.getCoverPositionY() + "")) {
            coverPositionY = bpmEntitySubType.getCoverPositionY();
        }
        int coverScaleToFit = 0;
        if (!"".equals(bpmEntitySubType.getCoverScaleToFit() + "")) {
            coverScaleToFit = bpmEntitySubType.getCoverScaleToFit();
        }


        String drawingQrCode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();
        dl.setQrCode(drawingQrCode);
        drawingRepository.save(dl);

        try {

            String savepath = temporaryDir + dl.getDwgNo() + "_" + "R" + dl.getLatestRev() + ".pdf";

            PageDTO page = new PageDTO();
            page.setFetchAll(true);
            SubDrawingCriteriaDTO criteriaDTO = new SubDrawingCriteriaDTO();
            criteriaDTO.setDrawingVersion(dl.getLatestRev());
            criteriaDTO.setStatus(EntityStatus.PENDING);
            criteriaDTO.setActInstId(actInstId);
            Page<SubDrawing> result = subDrawingRepository.searchLatestSubDrawing(orgId, project.getId(), dl.getId(), criteriaDTO, page.toPageable());
            List<SubDrawing> subFiles = result.getContent();
            List<SubDrawing> newSubFiles = new ArrayList<>();
            if (subFiles.size() > 0) {
                int filecount = 0;
                int listIndex = 0;
                int index = 1;
                if (dl.getCoverId() != null) {
                    filecount = subFiles.size() + 2;
                    listIndex++;
                    index++;
                } else {
                    filecount = subFiles.size() + 1;
                }
                String[] files = new String[filecount];

                if (dl.getCoverId() != null) {
                    String coverFilePath = protectedDir + dl.getCoverPath().substring(1);

                    if (printCoverQRCode) {
                        String coverPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";
                        String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                        drawingQrCode = dl.getQrCode();


                        QRCodeUtils.createBackgroundImg(
                            scaleToFit,
                            scaleToFit,
                            "rgba(255,182,193)",
                            temporaryDir + dl.getDwgNo() + "-qrCode-blank.PNG"
                        );

                        QRCodeUtils.generateQRCodeNoBlank(drawingQrCode, coverScaleToFit, "png", fileName);

                        PdfUtils.setCoverImageAndFontWithBlankToPdf(
                            coverPDF,
                            coverFilePath,
                            temporaryDir + dl.getDwgNo() + "-qrCode-blank.PNG",
                            fileName,
                            coverPositionX,
                            coverPositionY,
                            coverScaleToFit,
                            coverScaleToFit,
                            "",
                            "",
                            0,
                            0
                        );

                        files[0] = generateSignatureCover(
                            orgId,
                            project.getId(),
                            dl,
                            coverPDF
                        );
                    } else {
                        files[0] = coverFilePath;
                    }
                }
                DrawSubPipeDTO dto = new DrawSubPipeDTO();
                dto.setProjectName(project.getName());
                dto.setCreatedBy(userid);
                dto.setDrawNumber(dl.getDwgNo());
                if (dl.getLatestRev() != null) {
                    dto.setRev(dl.getLatestRev());
                }
                dto.setTitle(dl.getDocumentTitle());
                dto.setOrgId(orgId);
                dto.setProjectId(project.getId());
                List<DrawSubPipeListDTO> items = new ArrayList<DrawSubPipeListDTO>();
                int seq = 1;

                for (SubDrawing sub : subFiles) {
                    String subPDF = null;
                    SubDrawing newSubDrawing = new SubDrawing();

                    if (sub.getFilePath() != null) {
                        DrawSubPipeDTO drawSubPipeDTO = new DrawSubPipeDTO();
                        drawSubPipeDTO.setProjectName(project.getName());
                        drawSubPipeDTO.setCreatedBy(userid);
                        drawSubPipeDTO.setDrawNumber(dl.getDwgNo());
                        drawSubPipeDTO.setTitle(dl.getDocumentTitle());
                        drawSubPipeDTO.setOrgId(orgId);
                        drawSubPipeDTO.setProjectId(project.getId());
                        List<DrawSubPipeListDTO> subItems = new ArrayList<DrawSubPipeListDTO>();

                        String subFilePath = protectedDir + sub.getFilePath().substring(1);
                        String shortCode = "";
                        if (sub.getShortCode() != null && !"".equals(sub.getShortCode())) {
                            shortCode = sub.getShortCode();
                        }


                        if (printDwgQRCode) {

                            subPDF = temporaryDir + sub.getFileName();
                            String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                            String subDrawingQrCode = sub.getQrCode();


                            QRCodeUtils.createBackgroundImg(
                                scaleToFit,
                                scaleToFit,
                                "rgba(255,255,255)",
                                temporaryDir + sub.getSubDrawingNo() + "-qrCode-blank.PNG"
                            );

                            if (!shortCode.equals("")) {
                                QRCodeUtils.createBackgroundImg(
                                    8 * shortCode.length(),
                                    8,
                                    "rgba(255,255,255)",
                                    temporaryDir + sub.getSubDrawingNo() + "-shortCode-blank.PNG"
                                );
                            }


                            QRCodeUtils.generateQRCodeNoBlank(subDrawingQrCode, scaleToFit, "png", fileName);

                            if (sub.getIsRedMark() != null) {
                                PdfUtils.setImageAndFontWithBlankToPdf(
                                    subPDF,
                                    subFilePath,
                                    temporaryDir + sub.getSubDrawingNo() + "-qrCode-blank.PNG",
                                    fileName,
                                    positionX,
                                    positionY,
                                    scaleToFit,
                                    scaleToFit,
                                    "",
                                    "",
                                    0,
                                    0
                                );
                            } else {
                                if (!shortCode.equals("")) {
                                    PdfUtils.setImageAndFontWithBlankToPdf(
                                        subPDF,
                                        subFilePath,
                                        temporaryDir + sub.getSubDrawingNo() + "-qrCode-blank.PNG",
                                        fileName,
                                        positionX,
                                        positionY,
                                        scaleToFit,
                                        scaleToFit,
                                        temporaryDir + sub.getSubDrawingNo() + "-shortCode-blank.PNG",
                                        shortCode,
                                        shortX,
                                        shortY
                                    );
                                } else {
                                    PdfUtils.setImageAndFontWithBlankToPdf(
                                        subPDF,
                                        subFilePath,
                                        temporaryDir + sub.getSubDrawingNo() + "-qrCode-blank.PNG",
                                        fileName,
                                        positionX,
                                        positionY,
                                        scaleToFit,
                                        scaleToFit,
                                        "",
                                        "",
                                        0,
                                        0
                                    );
                                }

                            }
                            files[index++] = subPDF;
                        } else {

                            subPDF = temporaryDir + sub.getFileName();

                            if (!shortCode.equals("")) {
                                QRCodeUtils.createBackgroundImg(
                                    8 * shortCode.length(),
                                    8,
                                    "rgba(255,255,255)",
                                    temporaryDir + sub.getSubDrawingNo() + "-shortCode-blank.PNG"
                                );
                            }

                            if (sub.getIsRedMark() == null) {
                                if (!shortCode.equals("")) {
                                    PdfUtils.setImageAndFontWithBlankToPdf(
                                        subPDF,
                                        subFilePath,
                                        "",
                                        "",
                                        0,
                                        0,
                                        0,
                                        0,
                                        temporaryDir + sub.getSubDrawingNo() + "-shortCode-blank.PNG",
                                        shortCode,
                                        shortX,
                                        shortY
                                    );
                                } else {
                                    PdfUtils.setImageAndFontWithBlankToPdf(
                                        subPDF,
                                        subFilePath,
                                        "",
                                        "",
                                        0,
                                        0,
                                        0,
                                        0,
                                        "",
                                        "",
                                        0,
                                        0
                                    );
                                }

                            }
                            files[index++] = subPDF;
                        }
                        DrawSubPipeListDTO subDTO = new DrawSubPipeListDTO();
                        String pageInfo = sub.getPageNo() + " OF " + sub.getPageCount();
                        subDTO.setIsoVersion(String.valueOf(sub.getSubDrawingVersion()));
                        subDTO.setSeq(String.valueOf(seq++));
                        subDTO.setSubDrawNo(sub.getSubDrawingNo());
                        subDTO.setPageInfo(pageInfo);
                        items.add(subDTO);
                        subItems.add(subDTO);
                        drawSubPipeDTO.setItems(subItems);
                        if (subPDF != null) {
                            JsonObjectResponseBody<FileES> subDrawingItemFileESResBody = uploadFile(orgId, project.getId(), drawSubPipeDTO, files, subPDF, listIndex, false);
                            sub.setFileId(LongUtils.parseLong(subDrawingItemFileESResBody.getData().getId()));
                            sub.setFileName(sub.getFileName());
                            sub.setFilePath(subDrawingItemFileESResBody.getData().getPath());
                        }
                    }
                    BeanUtils.copyProperties(sub, newSubDrawing);
                    newSubFiles.add(newSubDrawing);
                }
                dto.setItems(items);
                JsonObjectResponseBody<FileES> fileESResBody = uploadFile(orgId, project.getId(), dto, files, savepath, listIndex, true);

                drawingFileDTO.setFileId(LongUtils.parseLong(fileESResBody.getData().getId()));
                drawingFileDTO.setFileName(fileESResBody.getData().getName());
                drawingFileDTO.setFilePath(fileESResBody.getData().getPath());
                drawingFileDTO.setQrCode(dl.getQrCode());
                drawingFileDTO.setSubDrawingList(newSubFiles);

            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return drawingFileDTO;
    }

    /**
     * 取得当前校审的图纸和图纸的最新版本
     *
     * @param execResult
     * @return
     */
    @Override
    public ExecResultDTO getDrawingAndLatestRev(ExecResultDTO execResult) {


        String latestRev = null;

        Drawing dwg = drawingRepository.findById(execResult.getActInst().getEntityId()).orElse(null);
        if (dwg == null) {
            execResult.setExecResult(false);
            execResult.setErrorDesc("图纸不存在");
            return execResult;
        }

        latestRev = dwg.getLatestRev();
        if (execResult.getVariables() == null) {
            execResult.setVariables(new HashMap<>());
        }
        execResult.getVariables().put("drawing", dwg);
        execResult.getVariables().put("latestRev", latestRev);

        return execResult;
    }

    /**
     * 上传文件至服务器，并返回文件属性。
     */
    private JsonObjectResponseBody<FileES> uploadFile(
        Long orgId,
        Long projectId,
        DrawSubPipeDTO dto,
        String[] files,
        String savepath,
        int listIndex,
        Boolean isCatalog
    ) {
        try {
            if (isCatalog) {
                JsonObjectResponseBody<ReportHistory> reportResponse = drawingReportFeignAPI
                    .generateDrawConstructionreport(orgId, projectId, dto);
                ReportHistory rs = reportResponse.getData();
                files[listIndex] = protectedDir + rs.getFilePath().substring(1);
                PdfUtils.mergePdfFiles(files, savepath);
            }

            File diskFile = new File(savepath);


            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

            IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());


            logger.error("图纸基础3 上传docs服务->开始");
            MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
//            .uploadIssueAttachment(orgId.toString(), new CommonsMultipartFile(fileItem));
            logger.error("图纸基础3 上传docs服务->结束");
            logger.error("图纸基础3 保存docs服务->开始");
            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                tempFileResBody.getData().getName(), new FilePostDTO());
            logger.error("图纸基础3 保存docs服务->结束");
            return fileESResBody;
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObjectResponseBody<>();
        }

    }

    /**
     * 打包有效图纸文件。
     *
     * @param orgId       组织id
     * @param project     项目
     * @param dwg         图纸信息
     * @param operatorDTO 操作人
     */
    @Override
    public DrawingPackageReturnDTO packEffectiveSubFiles(
        Long orgId,
        Project project,
        OperatorDTO operatorDTO,
        Drawing dwg) {

        DrawingPackageReturnDTO drawingPackageReturnDTO = new DrawingPackageReturnDTO();
        BpmEntitySubType bpmEntitySubType = entitySubTypeService.getEntitySubType(project.getId(), dwg.getEntitySubType());
        if (bpmEntitySubType == null) throw new BusinessError("Drawing CATEGORY NOT DEFINED");

        int positionX = bpmEntitySubType.getDrawingPositionX();
        int positionY = bpmEntitySubType.getDrawingPositionY();
        int scaleToFit = bpmEntitySubType.getDrawingScaleToFit();


        int coverPositionX = bpmEntitySubType.getCoverPositionX();
        int coverPositionY = bpmEntitySubType.getCoverPositionY();
        int coverScaleToFit = bpmEntitySubType.getCoverScaleToFit();
        try {
            String savepath = temporaryDir + dwg.getDwgNo() + "_" + "R" + dwg.getLatestRev() + ".pdf";


            List<SubDrawing> subFiles = subDrawingRepository.findByOrgIdAndProjectIdAndDrawingIdAndStatus(orgId, project.getId(), dwg.getId(), EntityStatus.ACTIVE);
            if (subFiles.size() > 0) {
                int filecount = 0;
                int listIndex = 0;
                int index = 1;
                if (dwg.getCoverId() != null) {
                    filecount = subFiles.size() + 2;
                    listIndex++;
                    index++;
                } else {
                    filecount = subFiles.size() + 1;
                }
                String[] files = new String[filecount];


                if (dwg.getCoverId() != null) {
                    String coverFilePath = protectedDir + dwg.getCoverPath().substring(1);

                    String coverPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";
                    String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                    String drawingQrCode = dwg.getQrCode();


                    QRCodeUtils.createBackgroundImg(
                        scaleToFit,
                        scaleToFit,
                        "rgba(255,182,193)",
                        temporaryDir + dwg.getDwgNo() + "-qrCode-blank.PNG"
                    );

                    QRCodeUtils.generateQRCodeNoBlank(drawingQrCode, coverScaleToFit, "png", fileName);

                    PdfUtils.setCoverImageAndFontWithBlankToPdf(
                        coverPDF,
                        coverFilePath,
                        temporaryDir + dwg.getDwgNo() + "-qrCode-blank.PNG",
                        fileName,
                        coverPositionX,
                        coverPositionY,
                        coverScaleToFit,
                        coverScaleToFit,
                        "",
                        "",
                        0,
                        0
                    );


                    files[0] = generateSignatureCover(
                        orgId,
                        project.getId(),
                        dwg,
                        coverPDF
                    );
                }
                DrawSubPipeDTO dto = new DrawSubPipeDTO();
                dto.setProjectName(project.getName());
                dto.setCreatedBy(operatorDTO.getId());
                dto.setDrawNumber(dwg.getDwgNo());
                dto.setTitle(dwg.getDocumentTitle());
                dto.setOrgId(orgId);
                dto.setProjectId(project.getId());
                dto.setRev(dwg.getLatestRev());
                List<DrawSubPipeListDTO> items = new ArrayList<DrawSubPipeListDTO>();
                int seq = 1;
                for (SubDrawing sub : subFiles) {

                    if (sub.getFilePath() != null) {
                        String subFilePath = protectedDir + sub.getFilePath().substring(1);
                        files[index++] = subFilePath;
                        DrawSubPipeListDTO subDTO = new DrawSubPipeListDTO();
                        String pageInfo = sub.getPageNo() + " OF " + sub.getPageCount();
                        subDTO.setIsoVersion(String.valueOf(sub.getSubDrawingVersion()));
                        subDTO.setSeq(String.valueOf(seq++));
                        subDTO.setSubDrawNo(sub.getSubDrawingNo());
                        subDTO.setPageInfo(pageInfo);
                        items.add(subDTO);
                    }
                }
                dto.setItems(items);
                JsonObjectResponseBody<FileES> fileESResBody = uploadFile(orgId, project.getId(), dto, files, savepath, listIndex, true);
                if (fileESResBody != null && fileESResBody.getData() != null) {
                    dwg.setFileId(LongUtils.parseLong(fileESResBody.getData().getId()));
                    dwg.setFileName(fileESResBody.getData().getName());
                    dwg.setFilePath(fileESResBody.getData().getPath());
                    drawingPackageReturnDTO.setDrawing(dwg);

                }

            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return drawingPackageReturnDTO;
    }

    /**
     * 打包带有二维码的封面。
     */
    @Override
    public DrawingFileDTO packageCover(
        Long orgId,
        Project project,
        Drawing dwg) {

        DrawingFileDTO fdto = new DrawingFileDTO();
        BpmEntitySubType bpmEntitySubType = entitySubTypeService.getEntitySubType(project.getId(), dwg.getEntitySubType());
        if (bpmEntitySubType == null) throw new BusinessError("NO DWG ENTITY CATEGORY ");
        int coverPositionX = bpmEntitySubType.getCoverPositionX();
        int coverPositionY = bpmEntitySubType.getCoverPositionY();
        int coverScaleToFit = bpmEntitySubType.getCoverScaleToFit();

        try {
            String[] files = new String[2];
            if (dwg.getCoverId() != null) {
                String coverFilePath = protectedDir + dwg.getCoverPath().substring(1);

                String coverPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";


                String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                String qrCode = dwg.getQrCode();
                QRCodeUtils.generateQRCodeNoBlank(qrCode, coverScaleToFit, "png", fileName);

                PdfUtils.setImageToCoverPdf(coverPDF, coverFilePath, fileName, coverPositionX, coverPositionY, coverScaleToFit, coverScaleToFit);


                files[0] = generateSignatureCover(
                    orgId,
                    project.getId(),
                    dwg,
                    coverPDF
                );

                File diskFile = new File(files[0]);

                DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                    MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

                IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());


                logger.error("图纸基础4 上传docs服务->开始");
                MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
                JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                    .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                logger.error("图纸基础4 上传docs服务->结束");
                logger.error("图纸基础4 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), project.getId().toString(),
                    tempFileResBody.getData().getName(), new FilePostDTO());
                logger.error("图纸基础4 保存docs服务->结束");
                fdto.setFileId(LongUtils.parseLong(fileESResBody.getData().getId()));
                fdto.setFileName(fileESResBody.getData().getName());
                fdto.setFilePath(fileESResBody.getData().getPath());
                fdto.setQrCode(dwg.getQrCode());
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return fdto;
    }

    /**
     * 给所有有效图纸生成二维码并更新子图纸，然后进行有效图纸打包。
     *
     * @param orgId       组织ID
     * @param project     项目ID
     * @param operatorDTO
     * @param dl
     */
    @Override
    public DrawingFileDTO startBatchTask(Long orgId,
                                         Project project,
                                         OperatorDTO operatorDTO,
                                         Drawing dl
    ) {
        DrawingFileDTO drawingFileDTO = new DrawingFileDTO();

        BpmEntitySubType bpmEntitySubType = entitySubTypeService.getEntitySubType(project.getId(), dl.getEntitySubType());
        if (bpmEntitySubType == null) throw new BusinessError("NO DWG ENTITY CATEGORY ");

        int positionX = bpmEntitySubType.getDrawingPositionX();
        int positionY = bpmEntitySubType.getDrawingPositionY();
        int scaleToFit = bpmEntitySubType.getDrawingScaleToFit();

        int coverPositionX = bpmEntitySubType.getCoverPositionX();
        int coverPositionY = bpmEntitySubType.getCoverPositionY();
        int coverScaleToFit = bpmEntitySubType.getCoverScaleToFit();
        try {
            String savepath = temporaryDir + dl.getDwgNo() + "_" + "R" + dl.getLatestRev() + ".pdf";

            List<SubDrawing> subFiles = subDrawingRepository.findByOrgIdAndProjectIdAndDrawingIdAndStatus(orgId, project.getId(), dl.getId(), EntityStatus.ACTIVE);

            if (subFiles.size() > 0) {
                int filecount = 0;
                int listIndex = 0;
                int index = 1;
                if (dl.getCoverId() != null) {
                    filecount = subFiles.size() + 2;
                    listIndex++;
                    index++;
                } else {
                    filecount = subFiles.size() + 1;
                }
                String[] files = new String[filecount];

                if (dl.getCoverId() != null) {
                    String coverFilePath = protectedDir + dl.getCoverPath().substring(1);
                    String coverPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";
                    String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                    String drawingQrCode = dl.getQrCode();

                    QRCodeUtils.createBackgroundImg(
                        scaleToFit,
                        scaleToFit,
                        "rgba(255,182,193)",
                        temporaryDir + dl.getDwgNo() + "-qrCode-blank.PNG"
                    );

                    QRCodeUtils.generateQRCodeNoBlank(drawingQrCode, coverScaleToFit, "png", fileName);

                    PdfUtils.setImageAndFontWithBlankToPdf(
                        coverPDF,
                        coverFilePath,
                        temporaryDir + dl.getDwgNo() + "-qrCode-blank.PNG",
                        fileName,
                        coverPositionX,
                        coverPositionY,
                        coverScaleToFit,
                        coverScaleToFit,
                        "",
                        "",
                        0,
                        0
                    );


                    files[0] = generateSignatureCover(
                        orgId,
                        project.getId(),
                        dl,
                        coverPDF
                    );
                }
                DrawSubPipeDTO dto = new DrawSubPipeDTO();
                dto.setProjectName(project.getName());
                dto.setCreatedBy(operatorDTO.getId());
                dto.setDrawNumber(dl.getDwgNo());
                dto.setRev(dl.getLatestRev());
                dto.setTitle(dl.getDocumentTitle());
                dto.setOrgId(orgId);
                dto.setProjectId(project.getId());
                List<DrawSubPipeListDTO> items = new ArrayList<DrawSubPipeListDTO>();
                int seq = 1;
                for (SubDrawing sub : subFiles) {
                    DrawSubPipeDTO drawSubPipeDTO = new DrawSubPipeDTO();
                    drawSubPipeDTO.setProjectName(project.getName());
                    drawSubPipeDTO.setCreatedBy(operatorDTO.getId());
                    drawSubPipeDTO.setDrawNumber(dl.getDwgNo());
                    drawSubPipeDTO.setTitle(dl.getDocumentTitle());
                    drawSubPipeDTO.setOrgId(orgId);
                    drawSubPipeDTO.setProjectId(project.getId());
                    String shortCode = "";
                    if (sub.getShortCode() != null && !"".equals(sub.getShortCode())) {
                        shortCode = sub.getShortCode();
                    }
                    List<DrawSubPipeListDTO> subItems = new ArrayList<DrawSubPipeListDTO>();

                    String subFilePath = protectedDir + sub.getFilePath().substring(1);
                    String subPDF = temporaryDir + sub.getFileName();
                    String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                    String subDrawingQrCode = sub.getQrCode();

                    QRCodeUtils.createBackgroundImg(
                        scaleToFit,
                        scaleToFit,
                        "rgba(255,255,255)",
                        temporaryDir + sub.getSubDrawingNo() + "-qrCode-blank.PNG"
                    );

                    if (!shortCode.equals("")) {
                        QRCodeUtils.createBackgroundImg(
                            8 * shortCode.length(),
                            8,
                            "rgba(255,255,255)",
                            temporaryDir + sub.getSubDrawingNo() + "-shortCode-blank.PNG"
                        );
                    }


                    QRCodeUtils.generateQRCodeNoBlank(subDrawingQrCode, scaleToFit, "png", fileName);

                    if (!shortCode.equals("")) {
                        PdfUtils.setImageAndFontWithBlankToPdf(
                            subPDF,
                            subFilePath,
                            temporaryDir + sub.getSubDrawingNo() + "-qrCode-blank.PNG",
                            fileName,
                            positionX,
                            positionY,
                            scaleToFit,
                            scaleToFit,
                            temporaryDir + sub.getSubDrawingNo() + "-shortCode-blank.PNG",
                            shortCode,
                            640,
                            807
                        );
                    } else {
                        PdfUtils.setImageAndFontWithBlankToPdf(
                            subPDF,
                            subFilePath,
                            temporaryDir + sub.getSubDrawingNo() + "-qrCode-blank.PNG",
                            fileName,
                            positionX,
                            positionY,
                            scaleToFit,
                            scaleToFit,
                            "",
                            "",
                            0,
                            0
                        );
                    }


                    files[index++] = subPDF;
                    DrawSubPipeListDTO subDTO = new DrawSubPipeListDTO();
                    String pageInfo = sub.getPageNo() + " OF " + sub.getPageCount();
                    subDTO.setIsoVersion(String.valueOf(sub.getSubDrawingVersion()));
                    subDTO.setSeq(String.valueOf(seq++));
                    subDTO.setSubDrawNo(sub.getSubDrawingNo());
                    subDTO.setPageInfo(pageInfo);
                    items.add(subDTO);
                    subItems.add(subDTO);
                    drawSubPipeDTO.setItems(subItems);
                    JsonObjectResponseBody<FileES> subDrawingItemFileESResBody = uploadFile(orgId, project.getId(), drawSubPipeDTO, files, subPDF, listIndex, false);
                    sub.setFileId(LongUtils.parseLong(subDrawingItemFileESResBody.getData().getId()));
                    sub.setFileName(sub.getFileName());
                    sub.setFilePath(subDrawingItemFileESResBody.getData().getPath());
                    sub.setLastModifiedAt(new Date());
                    subDrawingRepository.save(sub);
                    System.out.println(sub.getSubDrawingNo() + " -> 子图纸上传成功");
                }
                dto.setItems(items);
                JsonObjectResponseBody<FileES> fileESResBody = uploadFile(orgId, project.getId(), dto, files, savepath, listIndex, true);
                dl.setFileId(LongUtils.parseLong(fileESResBody.getData().getId()));
                dl.setFileName(fileESResBody.getData().getName());
                dl.setFilePath(fileESResBody.getData().getPath());
                drawingRepository.save(dl);
                System.out.println(dl.getDwgNo() + " -> 图集保存成功");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return drawingFileDTO;
    }

    @Override
    public ProofreadSubDrawingPreviewDTO getSubDrawingPreview(
        Long orgId,
        Long projectId,
        Long subDrawingId
    ) {
        SubDrawing subDrawing = subDrawingRepository
            .findById(subDrawingId)
            .orElse(null);
        if (subDrawing == null) {
            throw new NotFoundError("找不到子图纸");
        }
        if (subDrawing.getFilePath() == null) {
            throw new NotFoundError("请上传子图纸文件");
        }

        ProofreadSubDrawingPreviewDTO dto = new ProofreadSubDrawingPreviewDTO();
        dto.setSubDrawingBase(
            getBaseFromFile(
                new File(protectedDir + subDrawing.getFilePath().substring(1))
            )
        );

        return dto;
    }

    /**
     * 文件转base64字符串
     *
     * @param file 文件对象
     * @return
     */
    private String getBaseFromFile(File file) {
        FileInputStream fin = null;
        BufferedInputStream bin = null;
        BufferedOutputStream bout = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            fin = new FileInputStream(file);
            bin = new BufferedInputStream(fin);
            bout = new BufferedOutputStream(baos);
            byte[] buffer = new byte[1024];
            int len = bin.read(buffer);
            while (len != -1) {
                bout.write(buffer, 0, len);
                len = bin.read(buffer);
            }

            bout.flush();
            byte[] bytes = baos.toByteArray();
            return Base64.toBase64String(bytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                fin.close();
                bin.close();
                bout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 子图纸zip打包。
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param operatorDTO 操作人员
     * @param criteriaDTO 查询条件
     */
    @Override
    public void startZip(Long orgId,
                         Long projectId,
                         Long drawingId,
                         OperatorDTO operatorDTO,
                         SubDrawingCriteriaDTO criteriaDTO,
                         ContextDTO context
    ) {


        if (!context.isContextSet()) {
            String authorization = context.getAuthorization();
            String userAgent = context.getUserAgent();
            RequestAttributes attributes = new ServletRequestAttributes(
                new RequestWrapper(context.getRequest(), authorization, userAgent),
                null
            );

            RequestContextHolder.setRequestAttributes(attributes, true);
            context.setContextSet(true);
        }

        Drawing drawing = drawingRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, drawingId);
        if (drawing == null) {
            throw new BusinessError("图集不存在");
        }
        DrawingZipDetail drawingZipDetail = new DrawingZipDetail();
        drawingZipDetail.setOrgId(orgId);
        drawingZipDetail.setProjectId(projectId);
        drawingZipDetail.setOperateName(operatorDTO.getName());
        drawingZipDetail.setOperateBy(operatorDTO.getId());
        drawingZipDetail.setPackageAt(new Date());
        drawingZipDetail.setCreatedAt(new Date());
        drawingZipDetail.setLastModifiedAt(new Date());
        drawingZipDetail.setStatus(EntityStatus.ACTIVE);
        drawingZipDetail.setPackageStatus(EntityStatus.ACTIVE);

        Long drawingFileId = drawingZipDetail.getId();
        System.out.println(drawingZipDetail.getId());
        drawingZipHistoryRepository.save(drawingZipDetail);

        Project project = projectService.get(orgId, projectId);

        batchTaskService.runDrawingPackage(
            context,
            project,
            BatchTaskCode.DRAWING,
            null,
            null,
            null,
            drawing,
            null,
            null,
            null,
            null,
            batchTask -> {
                String drawingNo = "drawing.zip";

                drawingNo = drawing.getDwgNo() + ".zip";
                PageDTO pageDTO = new PageDTO();
                pageDTO.setFetchAll(true);
                Page<SubDrawing> result = subDrawingRepository.searchLatestSubDrawing(orgId, projectId, drawingId, criteriaDTO, pageDTO.toPageable());

                List<SubDrawing> subFiles = result.getContent();


                if (subFiles == null || subFiles.size() == 0) {
                    return new BatchResultDTO();
                }


                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                String zipFileName = formatter.format(new Date()) + ".zip";
                String zipPath = temporaryDir + zipFileName;


                ZipOutputStream zipOutputStream = null;
                FileInputStream zipSource = null;
                BufferedInputStream bufferStream = null;
                File zipFile = new File(zipPath);

                try {
                    zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile));
                    for (SubDrawing subFile : subFiles) {
                        String subDrawingPath = subFile.getFilePath();
                        File subDrawingFile = new File(protectedDir, subDrawingPath);

                        if (subDrawingFile.exists()) {
                            zipSource = new FileInputStream(subDrawingFile);
                            ZipEntry zipEntry = new ZipEntry(subFile.getFileName());
                            zipOutputStream.putNextEntry(zipEntry);
                            bufferStream = new BufferedInputStream(zipSource, 1024 * 10);
                            int read = 0;
                            byte[] buf = new byte[1024 * 10];
                            while ((read = bufferStream.read(buf, 0, 1024 * 10)) != -1) {
                                zipOutputStream.write(buf, 0, read);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                } finally {

                    try {
                        if (null != bufferStream) {
                            bufferStream.close();
                        }
                        if (null != zipOutputStream) {
                            zipOutputStream.flush();
                            zipOutputStream.close();
                        }
                        if (null != zipSource) {
                            zipSource.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace(System.out);
                    }
                }
                DrawingZipDetail drawinghistory = drawingZipHistoryRepository.findById(drawingFileId).get();

                drawinghistory.setDrawingNo(drawing.getDwgNo());
                drawinghistory.setDrawingVersion(criteriaDTO.getDrawingVersion());
                drawinghistory.setIsRedMark(criteriaDTO.getIsRedMark());
                drawinghistory.setStatus(criteriaDTO.getStatus());
                try {
                    DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                        MediaType.APPLICATION_PDF_VALUE, true, zipFile.getName());

                    IOUtils.copy(new FileInputStream(zipFile), fileItem.getOutputStream());


                    logger.error("图纸基础5 上传docs服务->开始");
                    MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                        APPLICATION_PDF_VALUE, fileItem.getInputStream());
                    JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                        .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                    logger.error("图纸基础5 上传docs服务->结束");
                    logger.error("图纸基础5 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        tempFileResBody.getData().getName(), new FilePostDTO());
                    logger.error("图纸基础5 保存docs服务->结束");

                    if (fileESResBody != null) {
                        drawinghistory.setFileName(drawingNo);
                        drawinghistory.setFileId(fileESResBody.getData().getId());
                        drawinghistory.setFilePath(fileESResBody.getData().getPath());

//                        drawinghistory.setDrawer(drawing.getDrawer());
                        drawinghistory.setPackageStatus(EntityStatus.FINISHED);

                    }
                } catch (Exception e) {
                    drawinghistory.setErrorMsg(e.getMessage());
                } finally {
                    drawingZipHistoryRepository.save(drawinghistory);
                }
                return new BatchResultDTO();
            });
    }

    /**
     * 查询打包记录。
     *
     * @param pageDTO   分页信息
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @return
     */
    @Override
    public Page<DrawingZipDetail> search(PageDTO pageDTO,
                                         Long orgId,
                                         Long projectId) {

        return drawingZipHistoryRepository.findByOrgIdAndProjectId(orgId, projectId, pageDTO.toPageable());
    }

    /**
     * 获取子图纸base64文件。
     *
     * @param orgId          组织ID
     * @param projectId      项目ID
     * @param subDrawingList 操作人员
     */
    @Override
    public List<String> getSubDrawingImg(Long orgId,
                                         Long projectId,
                                         List<SubDrawing> subDrawingList
    ) {

        List<String> tempFiles = new ArrayList<>();
        List<String> targetFiles = new ArrayList<>();
        List<String> base64Files = new ArrayList<>();
        for (SubDrawing subDrawing : subDrawingList) {
            tempFiles.add(subDrawing.getFilePath());
        }
        try {
            for (String tempFile : tempFiles) {
                targetFiles.add(protectedDir + tempFile.substring(1));
            }
            List<String> files = PdfUtils.convertPdfToImage(targetFiles, protectedDir);
            for (String file : files) {
                base64Files.add(
                    getBaseFromFile(
                        new File(file)
                    )
                );
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return base64Files;
    }

    @Override
    public void startSubDrawingTask(Long orgId,
                                    Project project,
                                    OperatorDTO operatorDTO,
                                    Drawing dl,
                                    SubDrawing subDrawing
    ) {
        BpmEntitySubType best = entitySubTypeService.getEntitySubType(project.getId(), dl.getEntitySubType());
        int positionX = best.getDrawingPositionX();
        int positionY = best.getDrawingPositionY();
        int scaleToFit = best.getDrawingScaleToFit();
        try {

            if (subDrawing != null) {
                int filecount = 1;
                int listIndex = 0;
                int index = 0;
                String[] files = new String[filecount];
                DrawSubPipeDTO dto = new DrawSubPipeDTO();
                dto.setProjectName(project.getName());
                dto.setCreatedBy(operatorDTO.getId());
                dto.setDrawNumber(dl.getDwgNo());
                dto.setRev(dl.getLatestRev());
                dto.setTitle(dl.getDocumentTitle());
                dto.setOrgId(orgId);
                dto.setProjectId(project.getId());
                List<DrawSubPipeListDTO> items = new ArrayList<DrawSubPipeListDTO>();
                int seq = 1;
                DrawSubPipeDTO drawSubPipeDTO = new DrawSubPipeDTO();
                drawSubPipeDTO.setProjectName(project.getName());
                drawSubPipeDTO.setCreatedBy(operatorDTO.getId());
                drawSubPipeDTO.setDrawNumber(dl.getDwgNo());
                drawSubPipeDTO.setTitle(dl.getDocumentTitle());
                drawSubPipeDTO.setOrgId(orgId);
                drawSubPipeDTO.setProjectId(project.getId());
                List<DrawSubPipeListDTO> subItems = new ArrayList<DrawSubPipeListDTO>();

                String subFilePath = protectedDir + subDrawing.getFilePath().substring(1);
                String subPDF = temporaryDir + subDrawing.getFileName();
                String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                String subDrawingQrCode = subDrawing.getQrCode();
                String shortCode = "";
                if (subDrawing.getShortCode() != null && !"".equals(subDrawing.getShortCode())) {
                    shortCode = subDrawing.getShortCode();
                }

                QRCodeUtils.createBackgroundImg(
                    scaleToFit,
                    scaleToFit,
                    "rgba(255,255,255)",
                    temporaryDir + subDrawing.getSubDrawingNo() + "-qrCode-blank.PNG"
                );


                if (!shortCode.equals("")) {
                    QRCodeUtils.createBackgroundImg(
                        8 * shortCode.length(),
                        8,
                        "rgba(255,255,255)",
                        temporaryDir + subDrawing.getSubDrawingNo() + "-shortCode-blank.PNG"
                    );
                }


                QRCodeUtils.generateQRCodeNoBlank(subDrawingQrCode + "dwwww", scaleToFit, "png", fileName);

                if (!shortCode.equals("")) {
                    PdfUtils.setImageAndFontWithBlankToPdf(
                        subPDF,
                        subFilePath,
                        temporaryDir + subDrawing.getSubDrawingNo() + "-qrCode-blank.PNG",
                        fileName,
                        positionX,
                        positionY,
                        scaleToFit,
                        scaleToFit,
                        temporaryDir + subDrawing.getSubDrawingNo() + "-shortCode-blank.PNG",
                        shortCode,
                        640,
                        807
                    );
                } else {
                    PdfUtils.setImageAndFontWithBlankToPdf(
                        subPDF,
                        subFilePath,
                        temporaryDir + subDrawing.getSubDrawingNo() + "-qrCode-blank.PNG",
                        fileName,
                        positionX,
                        positionY,
                        scaleToFit,
                        scaleToFit,
                        "",
                        "",
                        0,
                        0
                    );
                }
                files[index] = subPDF;
                DrawSubPipeListDTO subDTO = new DrawSubPipeListDTO();
                String pageInfo = subDrawing.getPageNo() + " OF " + subDrawing.getPageCount();
                subDTO.setIsoVersion(String.valueOf(subDrawing.getSubDrawingVersion()));
                subDTO.setSeq(String.valueOf(seq));
                subDTO.setSubDrawNo(subDrawing.getSubDrawingNo());
                subDTO.setPageInfo(pageInfo);
                items.add(subDTO);
                subItems.add(subDTO);
                drawSubPipeDTO.setItems(subItems);
                JsonObjectResponseBody<FileES> subDrawingItemFileESResBody = uploadFile(orgId, project.getId(), drawSubPipeDTO, files, subPDF, listIndex, false);
                subDrawing.setFileId(LongUtils.parseLong(subDrawingItemFileESResBody.getData().getId()));
                subDrawing.setFileName(subDrawing.getFileName());
                subDrawing.setFilePath(subDrawingItemFileESResBody.getData().getPath());
                subDrawing.setLastModifiedAt(new Date());
                subDrawingRepository.save(subDrawing);
                System.out.println(subDrawing.getSubDrawingNo() + " -> 子图纸上传成功");
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    @Override
    public void checkSubDrawing(Long orgId, Long projectId) {

        List<SubDrawing> subDrawings = subDrawingRepository.findByProjectIdAndStatusAndQrCodeStatusIsNull(projectId, EntityStatus.ACTIVE);

        if (subDrawings != null && subDrawings.size() > 0) {
            System.out.println("Sub Drawing count " + subDrawings.size());
        }

        subDrawings.forEach(subDrawing -> {
            int subNumber = 1;
            String fileStr = subDrawing.getFilePath();
            fileStr = protectedDir + fileStr;
            Drawing drawing = drawingRepository.findById(subDrawing.getDrawingId()).orElse(null);
            BpmEntitySubType bpmEntitySubType = entitySubTypeService.getEntitySubType(projectId, drawing.getEntitySubType());
            if (bpmEntitySubType == null) throw new BusinessError("NO DWG ENTITY CATEGORY");

            int positionX = bpmEntitySubType.getDrawingPositionX() - 20;
            int positionY = bpmEntitySubType.getDrawingPositionY() - 20;
            int scaleToFit = bpmEntitySubType.getDrawingScaleToFit();


            System.out.println("Blur parse");
            boolean isQrCodeOk = false;
            boolean isQrCodeRec = false;

            try {
                String qrcode = null;

                String imageBlur = "";
                for (int i = 0; i < 3 && !isQrCodeOk; i++) {
                    imageBlur = temporaryDir + CryptoUtils.uniqueId() + ".png";
                    boolean convertFlag;
                    if (i == 0) {

                        convertFlag = PdfUtils.convertFileByTerminal(fileStr, imageBlur,
                            " -gravity southwest -crop 120x120+" + String.valueOf(positionX - 20) + "+" + String.valueOf(positionY - 20));
                    } else {
                        convertFlag = PdfUtils.convertFileByTerminal(fileStr, imageBlur,
                            " -blur " + i + "x" + i + " -gravity southwest -crop 120x120+" + String.valueOf(positionX - 20) + "+" + String.valueOf(positionY - 20));
                    }
                    if (convertFlag && qrcode == null) {
                        qrcode = QRCodeUtils.decodeQrcodeFromImage(imageBlur);

                        if (qrcode != null) {
                            isQrCodeRec = true;
                            if (qrcode.equals(subDrawing.getQrCode())) {
                                isQrCodeOk = true;
                                System.out.println("qrcode ok " + qrcode);
                                break;
                            } else {
                                qrcode = null;
                            }
                        } else {
                            qrcode = null;

                        }
                    }
                    try {
                        FileUtils.remove(imageBlur);
                    } catch (Exception e) {

                    }

                }
            } catch (Exception e) {
                System.out.println(e.toString());

            }

            if (isQrCodeOk) {
                System.out.println("qrcode OK " + subDrawing.getSubDrawingNo());
                subDrawing.setQrCodeStatus("OK");
                subDrawingRepository.save(subDrawing);
            } else if (isQrCodeRec) {
                System.out.println("qrcode NOT OK " + subDrawing.getSubDrawingNo());

                subDrawing.setQrCodeStatus("NG");
                subDrawingRepository.save(subDrawing);
            } else {

                System.out.println("qrcode NOT REC " + subDrawing.getSubDrawingNo());

                subDrawing.setQrCodeStatus("NOT REC");
                subDrawingRepository.save(subDrawing);
            }
            subNumber = subNumber + 1;
            System.out.println("Sub Drawing" + subNumber);

        });
    }

    /**
     * 批量 替换 失效的子图纸二维码。
     *
     * @param projectId 项目
     */
    @Override
    public void patchSubDrawingQrCode(
        Long projectId, String once) {


        List<SubDrawing> subDrawings = subDrawingRepository.findByProjectIdAndQrCodeStatus(projectId, "NG");

        for (SubDrawing sub : subDrawings) {
            Long dwgId = sub.getDrawingId();
            Drawing dwg = drawingRepository.findById(dwgId).orElse(null);
            if (dwg == null) continue;
            BpmEntitySubType bpmEntitySubType = entitySubTypeService.getEntitySubType(projectId, dwg.getEntitySubType());
            if (bpmEntitySubType == null) throw new BusinessError("NO DWG ENTITY CATEGORY");

            int positionX = bpmEntitySubType.getDrawingPositionX();
            int positionY = bpmEntitySubType.getDrawingPositionY();
            int scaleToFit = bpmEntitySubType.getDrawingScaleToFit();


            String subPDF = null;
            if (sub.getFilePath() == null) continue;

            String subFilePath = protectedDir + sub.getFilePath().substring(1);


            try {
                Float height = PdfUtils.getPdfHeight(subFilePath);


                PdfUtils.removeOldQrImages(subFilePath, positionX, height - positionY - scaleToFit);

                subPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";
                String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                String subDrawingQrCode = sub.getQrCode();


                QRCodeUtils.createBackgroundImg(
                    scaleToFit,
                    scaleToFit,
                    "rgba(255,255,255)",
                    temporaryDir + sub.getSubDrawingNo() + "-qrCode-blank.PNG"
                );


                QRCodeUtils.generateQRCodeNoBlank(subDrawingQrCode, scaleToFit, "png", fileName);


                Float width = PdfUtils.getPdfWidth(subFilePath);

                PdfUtils.setImageAndFontWithBlankToPdf(
                    subPDF,
                    subFilePath,
                    temporaryDir + sub.getSubDrawingNo() + "-qrCode-blank.PNG",
                    fileName,
                    positionX,
                    positionY,
                    scaleToFit,
                    scaleToFit,
                    "",
                    "",
                    0,
                    0
                );
            } catch (Exception e) {
                System.out.println(e.toString());
                continue;
            }

            File tempFile = new File(subFilePath);
            String path = subFilePath.substring(0, subFilePath.lastIndexOf("/"));
            String fname = tempFile.getName();
            FileUtils.move(tempFile, path, fname + ".bk");


            File tmpFile = new File(subPDF);
            FileUtils.move(tmpFile, path, fname);

            sub.setQrCodeStatus("FIXED");
            subDrawingRepository.save(sub);
            if (once != null && once.equals("once")) break;
        }

    }

    @Override
    public void generateBarCode(Long orgId, Long projectId, Drawing drawing, DrawingDetail drawingDetail, Long barCodeId) throws IOException {

        // 获取图纸详情下的所有有效文件列表
        List<DrawingFile> drawingFiles = drawingFileRepository.
            findByProjectIdAndDrawingDetailIdAndStatus(projectId,
                drawingDetail.getId(), EntityStatus.ACTIVE);
        if (!drawingFiles.isEmpty()) {
            for (DrawingFile drawingFile : drawingFiles) {
                if (null != drawingFile.getFileName() && FileUtils.extname(drawingFile.getFileName()).toLowerCase().equals("." + BpmCode.FILE_TYPE_PDF)) {

                    String drawingFilePath = protectedDir + drawingFile.getFilePath().substring(1);
                    String resultFile = temporaryDir + CryptoUtils.uniqueId() + ".pdf";

                    // 根据实体类型&封面长宽获取坐标
                    File file = new File(drawingFilePath);
                    PDDocument doc = PDDocument.load(file);
                    int total = doc.getNumberOfPages();
                    PDPage page = doc.getPage(0);
//                    DrawingCoordinate drawingCoordinate = drawingCoordinateRepository.searchDrawingCoordinateByEntitySubTypeAndCover(
//                        orgId,projectId,drawing.getEntitySubType(),page.getMediaBox().getWidth(),page.getMediaBox().getHeight());

                    DrawingCoordinate drawingCoordinate = drawingCoordinateRepository.findByIdAndStatus(barCodeId, EntityStatus.ACTIVE);

                    int coverPositionX = drawingCoordinate == null ? 100 : drawingCoordinate.getDrawingPositionX();
                    //前端获取的y坐标是反的,用pdf的高度减去前端获取的y坐标得到后台所需的y坐标
                    int coverPositionY = drawingCoordinate == null ? 50 : (int) (page.getMediaBox().getHeight() - drawingCoordinate.getDrawingPositionY());
                    int coverScaleToFitX = drawingCoordinate == null ? 100 : drawingCoordinate.getGraphWidth();
                    int coverScaleToFitY = drawingCoordinate == null ? 50 : drawingCoordinate.getGraphHeight();
                    String coordinateType = drawingCoordinate == null ? "BARCODE_HORIZONTAL" : drawingCoordinate.getDrawingCoordinateType().toString();

                    // 加载PDF并添加二维码
                    BarCodeUtils.generateBarCodeToPdf(
//                        drawing.getQrCode(),
                        drawingDetail.getQrCode(),
                        coverPositionX,
                        coverPositionY,
                        coverScaleToFitX,
                        coverScaleToFitY,
                        temporaryDir,
                        resultFile,
                        drawingFilePath,
                        coordinateType);

                    File diskFile = new File(resultFile);
                    DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                        MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());
                    IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());
                    // 再次将文件上传到文档服务器
                    MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                        APPLICATION_PDF_VALUE, fileItem.getInputStream());
                    JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                        .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                    // 保存文件
                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        tempFileResBody.getData().getName(), new FilePostDTO());
                    FileES newFileES = fileESResBody.getData();
                    // 更新图纸详情文件数据
//                    drawingFile.setFileName(newFileES.getName());
                    drawingFile.setFileId(LongUtils.parseLong(newFileES.getId()));
                    drawingFile.setFilePath(newFileES.getPath());
                    drawingFileRepository.save(drawingFile);
                }
            }
        }
    }

    @Override
    public UploadDrawingFileResultDTO repairDrawing(
        Long orgId,
        Long projectId,
        String file,
        ContextDTO context,
        DrawingUploadDTO uploadDTO
    ) throws IOException {
        int errorCount = 0;
        int successCount = 0;
        List<UploadDrawingFileResultDTO.ErrorObject> noMatchFile = new ArrayList<>();
        String temporaryFileName = uploadDTO.getFileName();
        // 取得已上传的临时文件
        File diskFile = new File(temporaryDir, temporaryFileName);
        // 读取上传文件的元数据
//        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);
        String fileFolder = temporaryDir + File.separator + CryptoUtils.uniqueId().toUpperCase();
        File folder = new File(fileFolder);
        folder.mkdirs();

        List<File> uploadFiles = new ArrayList<>();

        errorCount = handleUploadedZipFiles(fileFolder, diskFile, uploadFiles);

        for (File uFile : uploadFiles) {
            System.out.println(uFile.getName());
            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, uFile.getName());
            MockMultipartFile fileItem1 = null;
            try {
                IOUtils.copy(new FileInputStream(uFile), fileItem.getOutputStream());
                fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
            } catch (Exception e) {
                logger.error(uFile.getName() + "Can't be found");
                continue;
            }

            // 将文件上传到文档服务器
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
            // 取得已上传的临时文件
            File diskFilePDF = new File(temporaryDir, tempFileResBody.getData().getName());
            // 读取上传文件的元数据
            FileMetadataDTO metadataPDF = FileUtils.readMetadata(diskFilePDF, FileMetadataDTO.class);
            String uploadFileNamePDF = metadataPDF.getFilename();
            String fileTypePDF = FileUtils.extname(uploadFileNamePDF);
            // 或缺图纸名和图纸版本
            String[] fileNames = uploadFileNamePDF.split(",");
            if (fileNames.length != 5 && fileNames.length != 3) {
                errorCount++;
                UploadDrawingFileResultDTO.ErrorObject errorObject = new UploadDrawingFileResultDTO.ErrorObject();
                errorObject.setName(uploadFileNamePDF);
                errorObject.setContent("文件名命名不规范： " + uploadFileNamePDF);
                noMatchFile.add(errorObject);
                continue;
            }
            String drawingNo = fileNames[0];
            String version = fileNames[1];
            String fileType = fileNames[2];
            String drawingCoordinateName = "";
            String isLatestRev = "";
            if (fileNames.length == 5) {
                drawingCoordinateName = fileNames[3];
                isLatestRev = fileNames[4].substring(0, fileNames[4].lastIndexOf('.'));
            }
            if (fileNames.length == 3) {
                fileType = fileNames[2].substring(0, fileNames[2].lastIndexOf('.'));
            }

            try {
                DrawingFileType.valueOf(fileType);
            } catch (Exception e) {
                errorCount++;
                UploadDrawingFileResultDTO.ErrorObject errorObject = new UploadDrawingFileResultDTO.ErrorObject();
                errorObject.setName(uploadFileNamePDF);
                errorObject.setContent("文件类型不规范： " + uploadFileNamePDF + " " + fileType);
                noMatchFile.add(errorObject);
                continue;
            }

            // 获取图纸信息
            Drawing drawing = drawingRepository.findByOrgIdAndProjectIdAndDisplayNameAndStatus(
                orgId,
                projectId,
                drawingNo,
                EntityStatus.ACTIVE
            );
            // 跳过并保留错误信息
            if (null == drawing) {
                errorCount++;
                UploadDrawingFileResultDTO.ErrorObject errorObject = new UploadDrawingFileResultDTO.ErrorObject();
                errorObject.setName(uploadFileNamePDF);
                errorObject.setContent("图纸名不存在于当前项目： " + uploadFileNamePDF + " " + drawingNo);
                noMatchFile.add(errorObject);
                continue;
            }

            DrawingCoordinate drawingCoordinate = null;
            if (!drawingCoordinateName.isEmpty() && DrawingFileType.ISSUE_FILE.name().equals(fileType)) {
                drawingCoordinate = drawingCoordinateRepository.findByOrgIdAndProjectIdAndNameAndStatus(
                    orgId,
                    projectId,
                    drawingCoordinateName,
                    EntityStatus.ACTIVE
                );
                if (null == drawingCoordinate) {
                    errorCount++;
                    UploadDrawingFileResultDTO.ErrorObject errorObject = new UploadDrawingFileResultDTO.ErrorObject();
                    errorObject.setName(uploadFileNamePDF);
                    errorObject.setContent("图纸名不存在于当前项目： " + drawingCoordinateName + " " + drawingCoordinateName);
                    noMatchFile.add(errorObject);
                    continue;
                }
            }

            // 特殊文件PDF处理
            DrawingUploadDTO drawingUploadDTO = new DrawingUploadDTO();
            drawingUploadDTO.setVersion(version);
            drawingUploadDTO.setFileName(tempFileResBody.getData().getName());
            drawingUploadDTO.setFileExtensionType(fileTypePDF);
            drawingUploadDTO.setFileType(DrawingFileType.valueOf(fileType));
            if (null != drawingCoordinate) {
                drawingUploadDTO.setDrawingCoordinateId(drawingCoordinate.getId());
            }
            drawingUploadDTO.setIsLatestRev(isLatestRev);

            boolean result = uploadFileForDrawing(
                orgId,
                projectId,
                drawing,
                context.getOperator(),
                drawingUploadDTO,
                uploadFileNamePDF
            );
            if (result) {
                successCount++;
            } else {
                errorCount++;
                UploadDrawingFileResultDTO.ErrorObject errorObject = new UploadDrawingFileResultDTO.ErrorObject();
                errorObject.setName(uploadFileNamePDF);
                errorObject.setContent("上传图纸失败： " + drawingCoordinateName);
                noMatchFile.add(errorObject);
            }
        }

        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();
        dto.setErrorCount(errorCount);
        dto.setErrorListForRepairing(noMatchFile);
        dto.setSuccessCount(successCount);
        return dto;
    }

    private boolean uploadFileForDrawing(
        Long orgId,
        Long projectId,
        Drawing drawing,
        OperatorDTO user,
        DrawingUploadDTO uploadDTO,
        String fileNAme
    ) throws IOException {

        // 查找图纸详情，按照版本去查找
        DrawingDetail detail = drawingDetailRepository
            .findByDrawingIdAndRevNo(drawing.getId(), uploadDTO.getVersion());
        if (null == detail) {
            detail = new DrawingDetail();
            detail.setRevNo(uploadDTO.getVersion());
            detail.setDrawingId(drawing.getId());
            detail.setOrgId(orgId);
            detail.setProjectId(projectId);
            detail.setCreatedAt(new Date());
            detail.setStatus(EntityStatus.ACTIVE);
        }

        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
        drawingUploadZipFileHistory.setCreatedAt();
        drawingUploadZipFileHistory.setDrawingId(drawing.getId());
        drawingUploadZipFileHistory.setOperator(user.getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);
        drawingUploadZipFileHistory.setDrawingId(drawing.getId());

        logger.error("图纸基础7 保存docs服务->开始");
        JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), uploadDTO.getFileName(),
            new FilePostDTO());
        logger.error("图纸基础7 保存docs服务->结束");
        FileES fileES = responseBody.getData();
        if (StringUtils.isEmpty(detail.getQrCode()))
            detail.setQrCode(QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid());
        DrawingFile drawingFile = drawingFileRepository.
            findByProjectIdAndDrawingDetailIdAndDrawingFileTypeAndStatus(projectId,
                detail.getId(), uploadDTO.getFileType(), EntityStatus.ACTIVE);
        if (drawingFile == null) {
            drawingFile = new DrawingFile();
            drawingFile.setStatus(EntityStatus.ACTIVE);
            drawingFile.setProjectId(projectId);
            drawingFile.setOrgId(orgId);
        }
        drawingFile.setFileId(LongUtils.parseLong(fileES.getId()));
        drawingFile.setFileName(fileNAme);
        drawingFile.setFilePath(fileES.getPath());
        drawingFile.setUploadDate(new Date());
        drawingFile.setUploaderName(user.getName());
        drawingFile.setUploaderId(user.getId());
        drawingFile.setLastModifiedAt();
        drawingFile.setDrawingDetailId(detail.getId());
        drawingFile.setStatus(EntityStatus.ACTIVE);
        drawingFile.setDrawingFileType(uploadDTO.getFileType());
        if (!StringUtils.isEmpty(drawingFile.getFileName()) &&
            (drawingFile.getFileName().endsWith("pdf") || drawingFile.getFileName().endsWith("PDF"))) {
            drawingFile.setPageCount(PdfUtils.getPdfPageCount(protectedDir + drawingFile.getFilePath()));
        }
        drawingFileRepository.save(drawingFile);
        drawingDetailRepository.save(detail);

        DrawingHistory drawingHistory = new DrawingHistory();
        drawingHistory.setOperator(user.getId());
        drawingHistory.setQrCode(detail.getQrCode());
        drawingHistory.setLastModifiedAt();
        drawingHistory.setFileId(drawingFile.getFileId());
        drawingHistory.setFileName(fileNAme);
        drawingHistory.setFilePath(drawingFile.getFilePath());
        drawingHistory.setMemo(uploadDTO.getComment());
        drawingHistory.setStatus(EntityStatus.ACTIVE);
        drawingHistory.setCreatedAt();
        drawingHistory.setDrawingId(drawing.getId());

        drawingHistory.setOrgId(orgId);
        drawingHistory.setProjectId(projectId);
        drawingHistory.setLastModifiedAt();
        drawingHistory.setDrawingId(drawing.getId());
        drawingHistoryRepository.save(drawingHistory);

        drawingUploadZipFileHistory.setSuccessCount(1);
        drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(fileES.getId()));
        drawingUploadZipFileHistory.setFileName(fileNAme);
        drawingUploadZipFileHistory.setFilePath(fileES.getPath());
        drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

        if (uploadDTO.getFileExtensionType().toLowerCase().equals("." + BpmCode.FILE_TYPE_PDF) && null != uploadDTO.getDrawingCoordinateId()) {
            generateBarCode(orgId, projectId, drawing, detail, uploadDTO.getDrawingCoordinateId());
        }

        if (null != uploadDTO.getIsLatestRev() && uploadDTO.getIsLatestRev().equals("Y")) {
            drawing.setFileId(drawingFile.getFileId());
            drawing.setFileName(drawingFile.getFileName());
            drawing.setFilePath(drawingFile.getFilePath());
            drawing.setLatestRev(detail.getRevNo());
            drawing.setLatestApprovedRev(detail.getRevNo());
            drawing.setUploadDate(new Date());
            drawing.setQrCode(detail.getQrCode());
            drawingRepository.save(drawing);
        }
        return true;
    }
}
