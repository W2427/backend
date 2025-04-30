package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.util.*;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.vo.BpmTaskDefKey;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingRedMarkInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.drawing.DrawingUploadDTO;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.tasks.vo.drawing.DrawingUploadZipFileSuccessFlg;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
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

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Component

public class DrawingRedMarkService implements DrawingRedMarkInterface {

    private final static Logger logger = LoggerFactory.getLogger(DrawingRedMarkService.class);


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;


    @Value("${application.files.public}")
    private String publicDir;

    private final DrawingRepository drawingRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final SubDrawingHistoryRepository subDrawingHisRepository;

    private final DrawingUploadZipFileHistoryRepository drawingUploadZipFileHistoryRepository;

    private final DrawingUploadZipFileHistoryDetailRepository drawingUploadZipFileHistoryDetailRepository;

    private final DrawingBaseInterface drawingBaseService;

    private final UploadFeignAPI uploadFeignAPI;


    /**
     * 构造方法
     */
    @Autowired
    public DrawingRedMarkService(
        DrawingRepository drawingRepository,
        SubDrawingRepository subDrawingRepository,
        DrawingDetailRepository drawingDetailRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmRuTaskRepository ruTaskRepository,
        DrawingUploadZipFileHistoryRepository drawingUploadZipFileHistoryRepository,
        SubDrawingHistoryRepository subDrawingHisRepository,
        DrawingUploadZipFileHistoryDetailRepository drawingUploadZipFileHistoryDetailRepository,
        DrawingBaseInterface drawingBaseService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI) {
        this.drawingRepository = drawingRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.subDrawingHisRepository = subDrawingHisRepository;
        this.drawingUploadZipFileHistoryRepository = drawingUploadZipFileHistoryRepository;
        this.drawingUploadZipFileHistoryDetailRepository = drawingUploadZipFileHistoryDetailRepository;
        this.drawingBaseService = drawingBaseService;
        this.uploadFeignAPI = uploadFeignAPI;
    }


    @Override
    public boolean uploadRedmarkFile(Long orgId, Long projectId, String uploadFileName, ContextDTO context,
                                     DrawingUploadDTO uploadDTO) {

        boolean result = true;

        String regEx = "_(\\d{1,3})_([\\s\\S]*)$";
        String[] str = uploadFileName.split("\\.");

        String fileName = "";
        for (int i = 0; i < str.length - 1; i++) {
            fileName += str[i] + ".";
        }
        fileName = fileName.substring(0, fileName.length() - 1);
        Long actInstId = uploadDTO.getProcInstId();


        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
        drawingUploadZipFileHistory.setCreatedAt();
        drawingUploadZipFileHistory.setOperator(context.getOperator().getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setFileName(fileName);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);

        String drawingNo = "";
        String pageNo = "";
        String drawingVersion = "";

        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(fileName);
        MatchResult ms = null;
        if (matcher.find()) {
            ms = matcher.toMatchResult();

            pageNo = ms.group(1);
            drawingVersion = ms.group(2);
            drawingNo = fileName.substring(0, fileName.indexOf(ms.group()));
        }

        if (drawingNo.equals("")
            || pageNo.equals("")
            || drawingVersion.equals("")) {
            drawingUploadZipFileHistory.setFailedCount(1);
            result = false;
        } else {
            File diskFileTemp = new File(temporaryDir, uploadDTO.getFileName());
            if (!diskFileTemp.exists()) {
                throw new NotFoundError();
            }
            Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + uploadDTO.getFileName());
            if (pageCounts != 1) {
                drawingUploadZipFileHistory.setFailedCount(1);
                drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
                result = false;
                throw new ValidationError("Upload file page count error.");
            }

            DrawingDetail detail = drawingDetailRepository.findById(uploadDTO.getDrawingDetailId()).orElse(null);
            if (detail == null) {
                throw new NotFoundError();
            }

            Drawing drawing = drawingRepository.findById(detail.getDrawingId()).orElse(null);
            if (drawing == null) {
                throw new NotFoundError();
            }
            if (drawing.getRedMarkOnGoing() == null) {
                drawing.setRedMarkOnGoing(true);
                drawingRepository.save(drawing);

            }
            BpmRuTask ruTask = null;


            ruTask = ruTaskRepository.findById(uploadDTO.getTaskId()).orElse(null);

            if (ruTask == null
                || !ruTask.getTaskDefKey().equals(BpmTaskDefKey.USERTASK_REDMARK_DESIGN.getType())) {
                throw new ValidationError("当前节点不能上传图纸");
            }

            List<SubDrawing> oldSubDrawings = subDrawingRepository.findByOrgIdAndProjectIdAndSubDrawingNoAndPageNoAndStatus(
                orgId,
                projectId,
                drawingNo,
                Integer.valueOf(pageNo),
                EntityStatus.ACTIVE);

            if (CollectionUtils.isEmpty(oldSubDrawings)) {
                throw new NotFoundError("找不到已经审批的图纸" + drawingNo + "_" + pageNo);
            } else if (oldSubDrawings.size() > 1) {
                throw new NotFoundError("发现多张" + drawingNo + "_" + pageNo);
            }

            SubDrawing oldSubDrawing = oldSubDrawings.get(0);





            if (!LongUtils.isEmpty(oldSubDrawing.getTaskId())) {
                throw new NotFoundError("有其他流程正在对图纸" + drawingNo + "_" + pageNo + "进行redmark");
            }

            if (drawingVersion.equals(oldSubDrawing.getSubDrawingVersion())) {
                throw new BusinessError("当前上传图纸版本和有效版本冲突，请重新确定要上传的图纸文件版本");
            }

            List<SubDrawing> runSubDrawings = subDrawingRepository.findByOrgIdAndProjectIdAndSubDrawingNoAndPageNoAndStatus(
                orgId,
                projectId,
                drawingNo,
                Integer.valueOf(pageNo),
                EntityStatus.PENDING);

            if (runSubDrawings != null && runSubDrawings.size() > 0) {
                throw new BusinessError("当前上传图纸正在redMark中");
            }
            SubDrawing subDrawing = BeanUtils.copyProperties(oldSubDrawing, new SubDrawing(), "id", "reviewStatus", "drawingVersion");

            logger.error("redmark3 保存docs服务->开始");
            JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                uploadDTO.getFileName(), new FilePostDTO());
            logger.error("redmark3 保存docs服务->结束");
            FileES fileES = responseBody.getData();

            String qrCode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();

            subDrawing.setSubDrawingVersion(drawingVersion);
            subDrawing.setDrawingVersion(drawing.getLatestRev());
            subDrawing.setDrawingDetailId(uploadDTO.getDrawingDetailId());
            subDrawing.setIssued(false);
            subDrawing.setQrCode(qrCode);
            subDrawing.setStatus(EntityStatus.PENDING);
            subDrawing.setComment("REDMARK");
            subDrawing.setIsRedMark(EntityStatus.PENDING);
            subDrawing.setActInstId(ruTask.getActInstId());
            subDrawing.setTaskId(ruTask.getId());
            subDrawing.setCreatedAt(new Date());










            List<SubDrawingHistory> subDrawingHistories = subDrawingHisRepository
                .findBySubDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersion(subDrawing.getId(), drawingNo,
                    Integer.parseInt(pageNo), drawingVersion);

            SubDrawingHistory subDrawingHistory = new SubDrawingHistory();
            if (!subDrawingHistories.isEmpty()) {
                subDrawingHistory = subDrawingHistories.get(0);
            }
            subDrawingHistory.setDrawingDetailId(subDrawing.getDrawingDetailId());
            subDrawingHistory.setSubDrawingId(subDrawing.getId());
            subDrawingHistory.setSubDrawingVersion(subDrawing.getSubDrawingVersion());
            subDrawingHistory.setSubDrawingNo(subDrawing.getSubDrawingNo());
            subDrawingHistory.setPageNo(subDrawing.getPageNo());
            subDrawingHistory.setSubDrawingVersion(subDrawing.getSubDrawingVersion());
            subDrawingHistory.setCreatedAt();
            subDrawingHistory.setOrgId(orgId);
            subDrawingHistory.setProjectId(projectId);
            subDrawingHistory.setStatus(EntityStatus.ACTIVE);
            subDrawingHistory.setOperator(context.getOperator().getId());
            subDrawingHistory.setQrCode(subDrawing.getQrCode());
            subDrawingHistory.setFileId(LongUtils.parseLong(fileES.getId()));
            subDrawingHistory.setFileName(fileES.getName());
            subDrawingHistory.setMemo(uploadDTO.getComment());
            subDrawingHistory.setFilePath(fileES.getPath());
            subDrawingHisRepository.save(subDrawingHistory);




            subDrawing.setFileName(fileES.getName());
            subDrawing.setFilePath(fileES.getPath());
            subDrawing.setFileId(LongUtils.parseLong(fileES.getId()));
            subDrawing.setReviewStatus(DrawingReviewStatus.INIT);
            subDrawingRepository.save(subDrawing);

            drawingUploadZipFileHistory.setDrawingId(drawing.getId());
            drawingUploadZipFileHistory.setSuccessCount(1);
            drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(fileES.getId()));
            drawingUploadZipFileHistory.setFileName(fileES.getName());
            drawingUploadZipFileHistory.setFilePath(fileES.getPath());

            List<ActReportDTO> documents = new ArrayList<>();
            ActReportDTO report = new ActReportDTO();
            report.setFileId(LongUtils.parseLong(fileES.getId()));
            report.setFilePath(fileES.getPath());
            documents.add(report);
            ruTask.setJsonDocuments(documents);

            ruTaskRepository.save(ruTask);

            /*String actTaskId = ruTask.getTaskId();

               boolean r = todoTaskService.execute(orgId, projectId, actTaskId, new TodoTaskExecuteDTO(), context.getOperator());
                if(r) {
                    drawing.setLocked(true);
                    drawingRepository.save(drawing);
                }*/
        }

        drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

        return result;
    }

    @Override
    public boolean uploadRedmarkFileZip(Long orgId, Long projectId, String uploadFileName, ContextDTO context,
                                        DrawingUploadDTO uploadDTO) {

        int errorCount = 0;
        int successCount = 0;

        List<String> noMatchFile = new ArrayList<>();
        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();

        DrawingDetail detail = drawingDetailRepository.findById(uploadDTO.getDrawingDetailId()).orElse(null);
        if (detail == null) {
            throw new NotFoundError();
        }

        Drawing drawing = drawingRepository.findById(detail.getDrawingId()).orElse(null);
        if (drawing == null) {
            throw new NotFoundError();
        }

        BpmRuTask ruTask = null;
        Long actInstId = uploadDTO.getProcInstId();

        ruTask = ruTaskRepository.findById(uploadDTO.getTaskId()).orElse(null);

        if (ruTask == null
            || !ruTask.getTaskDefKey().equals(BpmTaskDefKey.USERTASK_REDMARK_DESIGN.getType())) {
            throw new ValidationError("当前节点不能上传图纸");
        }

        Long drawingId = detail.getDrawingId();


        File diskFile = new File(temporaryDir, uploadDTO.getFileName());



        String fileFolder = temporaryDir + File.separator + CryptoUtils.uniqueId().toUpperCase();
        File folder = new File(fileFolder);
        folder.mkdirs();
        String[] files = null;
        List<File> uploadFiles = new ArrayList<>();
        errorCount = drawingBaseService.handleUploadedZipFiles(fileFolder, diskFile, uploadFiles);


        drawingUploadZipFileHistory.setFileName(uploadFileName);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setCreatedAt();
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setFileCount(uploadFiles.size());
        drawingUploadZipFileHistory.setOperator(context.getOperator().getId());
        drawingUploadZipFileHistory.setDrawingId(drawingId);
        drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
        Long drawingUploadZipFileHistoryId = drawingUploadZipFileHistory.getId();

        files = new String[uploadFiles.size()];
        int index = 0;
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

            logger.error("redmark1 上传docs服务->开始");
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
            logger.error("redmark1 上传docs服务->结束");
            TemporaryFileDTO temporaryFileDTO = tempFileResBody.getData();

            File diskFilePDF = new File(temporaryDir, tempFileResBody.getData().getName());

            FileMetadataDTO metadataPDF = FileUtils.readMetadata(diskFilePDF, FileMetadataDTO.class);
            String uploadFileNamePDF = metadataPDF.getFilename();
            String fileTypePDF = FileUtils.extname(uploadFileNamePDF);
            DrawingUploadZipFileHistoryDetail drawingUploadZipFileHistoryDetail = new DrawingUploadZipFileHistoryDetail();
            drawingUploadZipFileHistoryDetail.setOrgId(orgId);
            drawingUploadZipFileHistoryDetail.setProjectId(projectId);
            drawingUploadZipFileHistoryDetail.setFileName(uploadFileNamePDF);
            drawingUploadZipFileHistoryDetail.setDrawingUploadZipFileHistoryId(drawingUploadZipFileHistoryId);
            if (fileTypePDF.equals("." + BpmCode.FILE_TYPE_PDF)) {
                String regEx = "_(\\d{1,3})_([\\s\\S]*)$";
                String[] str = uploadFileNamePDF.split("\\.");
                String fileType = str[str.length - 1];
                String fileName = "";
                for (int i = 0; i < str.length - 1; i++) {
                    fileName += str[i] + ".";
                }
                fileName = fileName.substring(0, fileName.length() - 1);

                String drawingNo = "";
                String pageNo = "";
                String drawingVersion = "";

                Pattern pattern = Pattern.compile(regEx);
                Matcher matcher = pattern.matcher(fileName);
                MatchResult ms = null;
                if (matcher.find()) {
                    ms = matcher.toMatchResult();
                    pageNo = ms.group(1);
                    drawingVersion = ms.group(2);
                    drawingNo = fileName.substring(0, fileName.indexOf(ms.group()));
                }
                if (drawingNo.equals("")
                    || pageNo.equals("")
                    || drawingVersion.equals("")) {
                    noMatchFile.add(uploadFileNamePDF);
                    errorCount++;
                    drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                    drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
                    drawingUploadZipFileHistoryDetail.setCreatedAt();
                    drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
                    continue;
                } else {
                    File diskFileTemp = new File(temporaryDir, tempFileResBody.getData().getName());
                    if (!diskFileTemp.exists()) {
                        throw new NotFoundError();
                    }
                    Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + tempFileResBody.getData().getName());
                    if (pageCounts != 1) {
                        noMatchFile.add(uploadFileNamePDF);
                        errorCount++;
                        drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                        drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
                        drawingUploadZipFileHistoryDetail.setCreatedAt();
                        drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
                        continue;
                    }
                    logger.error("redmark4 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        temporaryFileDTO.getName(), new FilePostDTO());
                    logger.error("redmark4 保存docs服务->结束");
                    FileES f = responseBody.getData();

                    List<SubDrawing> oldSubDrawings = subDrawingRepository.findByOrgIdAndProjectIdAndSubDrawingNoAndPageNoAndStatus(
                        orgId,
                        projectId,
                        drawingNo,
                        Integer.valueOf(pageNo),
                        EntityStatus.ACTIVE);

                    if (CollectionUtils.isEmpty(oldSubDrawings)) {
                        noMatchFile.add(uploadFileNamePDF);
                        errorCount++;
                        drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                        drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
                        drawingUploadZipFileHistoryDetail.setCreatedAt();
                        drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
                        continue;
                    } else if (oldSubDrawings.size() > 1) {
                        noMatchFile.add(uploadFileNamePDF);
                        errorCount++;
                        drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                        drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
                        drawingUploadZipFileHistoryDetail.setCreatedAt();
                        drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
                        continue;
                    }

                    SubDrawing oldSubDrawing = oldSubDrawings.get(0);

                    if (!LongUtils.isEmpty(oldSubDrawing.getTaskId())) {
                        noMatchFile.add(uploadFileNamePDF);
                        errorCount++;
                        drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                        drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
                        drawingUploadZipFileHistoryDetail.setCreatedAt();
                        drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
                        continue;
                    }

                    if (drawingVersion.equals(oldSubDrawing.getSubDrawingVersion())) {
                        noMatchFile.add(uploadFileNamePDF);
                        errorCount++;
                        drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                        drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
                        drawingUploadZipFileHistoryDetail.setCreatedAt();
                        drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
                        continue;
                    }

                    List<SubDrawing> runSubDrawings = subDrawingRepository.findByOrgIdAndProjectIdAndSubDrawingNoAndPageNoAndStatus(
                        orgId,
                        projectId,
                        drawingNo,
                        Integer.valueOf(pageNo),
                        EntityStatus.PENDING);

                    if (runSubDrawings != null && runSubDrawings.size() > 0) {
                        noMatchFile.add(uploadFileNamePDF);
                        errorCount++;
                        drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                        drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
                        drawingUploadZipFileHistoryDetail.setCreatedAt();
                        drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
                        continue;
                    }
                    SubDrawing subDrawing = BeanUtils.copyProperties(oldSubDrawing, new SubDrawing(), "id", "reviewStatus", "drawingVersion");

                    if (subDrawing == null) {
                        subDrawing = BeanUtils.copyProperties(oldSubDrawing, new SubDrawing(), "id", "reviewStatus");
                    }

                    String qrCode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();

                    subDrawing.setSubDrawingVersion(drawingVersion);
                    subDrawing.setDrawingDetailId(uploadDTO.getDrawingDetailId());
                    subDrawing.setDrawingVersion(drawing.getLatestRev());
                    subDrawing.setActInstId(ruTask.getActInstId());
                    subDrawing.setTaskId(ruTask.getId());
                    subDrawing.setIssued(false);
                    subDrawing.setQrCode(qrCode);
                    subDrawing.setIsRedMark(EntityStatus.PENDING);
                    subDrawing.setCreatedAt(new Date());
                    subDrawing.setStatus(EntityStatus.PENDING);
                    subDrawing.setComment("REDMARK");

                    List<SubDrawingHistory> hisl = subDrawingHisRepository
                        .findBySubDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersion(subDrawing.getId(), drawingNo,
                            Integer.parseInt(pageNo), drawingVersion);
                    SubDrawingHistory his = new SubDrawingHistory();
                    if (!hisl.isEmpty()) {
                        his = hisl.get(0);
                    }
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
                    his.setQrCode(subDrawing.getQrCode());
                    his.setFileId(LongUtils.parseLong(f.getId()));
                    his.setFileName(f.getName());
                    his.setMemo(uploadDTO.getComment());
                    his.setFilePath(f.getPath());
                    subDrawingHisRepository.save(his);

                    subDrawing.setFileName(f.getName());
                    subDrawing.setFilePath(f.getPath());
                    subDrawing.setFileId(LongUtils.parseLong(f.getId()));
                    subDrawing.setReviewStatus(DrawingReviewStatus.INIT);
                    subDrawingRepository.save(subDrawing);

                    successCount++;
                    drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.SUCCESS);

                    files[index++] = protectedDir + f.getPath().substring(1);
                }

            } else {
                noMatchFile.add(uploadFileNamePDF);
                errorCount++;
                drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
            }
            drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
            drawingUploadZipFileHistoryDetail.setCreatedAt();
            drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
        }


        drawingUploadZipFileHistory.setFailedCount(errorCount);
        drawingUploadZipFileHistory.setSuccessCount(successCount);
        drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

        if (errorCount == 0) {
            String savepath = temporaryDir + CryptoUtils.uniqueId() + ".pdf";

            try {

                PdfUtils.mergePdfFiles(files, savepath);

                File diskMergeFile = new File(savepath);
                System.out.println(diskMergeFile.getName());
                DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                    MediaType.APPLICATION_PDF_VALUE, true, diskMergeFile.getName());

                IOUtils.copy(new FileInputStream(diskMergeFile), fileItem.getOutputStream());


                logger.error("redmark2 上传docs服务->开始");
                MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
                JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                    .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                logger.error("redmark2 上传docs服务->结束");
                logger.error("redmark2 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                    tempFileResBody.getData().getName(), new FilePostDTO());
                logger.error("redmark2 保存docs服务->结束");
                FileES mergeFile = fileESResBody.getData();

                List<ActReportDTO> documents = new ArrayList<>();
                ActReportDTO report = new ActReportDTO();
                report.setFileId(LongUtils.parseLong(mergeFile.getId()));
                report.setFilePath(mergeFile.getPath());
                documents.add(report);
                ruTask.setJsonDocuments(documents);
                ruTaskRepository.save(ruTask);

            } catch (Exception e) {
                e.printStackTrace(System.out);
            }

            /*String actTaskId = ruTask.getTaskId();

            boolean r = todoTaskService.execute(orgId, projectId, actTaskId, new TodoTaskExecuteDTO(), context.getOperator());
            if(r) {
                drawing.setLocked(true);
                drawingRepository.save(drawing);
            }*/

            if (drawing.getRedMarkOnGoing() == null) {
                drawing.setRedMarkOnGoing(true);
                drawingRepository.save(drawing);
            }

        } else {
            String errfiles = String.join(",", noMatchFile);
            throw new ValidationError("请检查下列文件: " + errfiles);
        }
        return false;
    }


    @Override
    public void uploadRedmarkFileQrcode(Long orgId, Long projectId, String uploadFileName, ContextDTO context,
                                        DrawingUploadDTO uploadDTO) {

        if (uploadDTO.getVersion() == null || uploadDTO.getVersion().equals("")) {
            throw new ValidationError("Upload drawing version is not found");
        }
        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
        drawingUploadZipFileHistory.setCreatedAt();
        drawingUploadZipFileHistory.setOperator(context.getOperator().getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setFileName(uploadFileName);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);

        Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + uploadDTO.getFileName());
        if (pageCounts != 1) {
            drawingUploadZipFileHistory.setFailedCount(1);
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
            throw new ValidationError("Upload file page count error.");
        }

        String qrcode = null;

        try {
            List<String> imageFiles = PdfUtils.parsePdfImage(temporaryDir + uploadDTO.getFileName(), temporaryDir);

            String imageBlur = "";
            for (int i = 1; i < 4; i++) {
                for (String imageFile : imageFiles) {
                    imageBlur = temporaryDir + CryptoUtils.uniqueId() + ".png";
                    boolean convertFlag = PdfUtils.convertFileByTerminal(imageFile, imageBlur, " -blur " + i + "x" + i + " -density 300 ");
                    if (convertFlag && qrcode == null) {
                        qrcode = QRCodeUtils.decodeQrcodeFromImage(imageBlur);
                    }
                    try {
                        FileUtils.remove(imageBlur);
                    } catch (Exception e) {
                    }
                }
                try {
                    FileUtils.remove(imageBlur);
                } catch (Exception e) {
                }
                if (qrcode != null) {
                    break;
                }
            }

            for (String imageFile : imageFiles) {
                if (qrcode == null) {
                    qrcode = QRCodeUtils.decodeQrcodeFromImage(imageFile);
                }
            }

            for (String imageFile : imageFiles) {
                try {
                    FileUtils.remove(imageFile);
                } catch (Exception e) {
                }
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        if (qrcode == null) {
            drawingUploadZipFileHistory.setFailedCount(1);
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
            throw new BusinessError("Cannot decode Qrcode.");
        }

        SubDrawing oldDrawing = subDrawingRepository.findByProjectIdAndQrCode(projectId, qrcode);

        if (oldDrawing == null) {
            drawingUploadZipFileHistory.setFailedCount(1);
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
            throw new NotFoundError("找不到图纸。");
        }

        DrawingDetail detail = drawingDetailRepository.findById(uploadDTO.getDrawingDetailId()).orElse(null);
        if (detail == null) {
            throw new NotFoundError("图纸详情不存在");
        }

        SubDrawing subDrawing = subDrawingRepository.findByProjectIdAndSubDrawingNoAndPageNoAndSubDrawingVersionAndStatus(
            projectId, oldDrawing.getSubDrawingNo(), oldDrawing.getPageNo(), uploadDTO.getVersion(), EntityStatus.ACTIVE);

        List<BpmActivityInstanceBase> actInst = bpmActInstRepository.findByProjectIdAndEntityIdAndProcessAndFinishStateAndSuspensionState(
            projectId, oldDrawing.getDrawingId(), BpmCode.DRAWING_REDMARK, ActInstFinishState.NOT_FINISHED, SuspensionState.ACTIVE);
        if (actInst.isEmpty()) {
            drawingUploadZipFileHistory.setFailedCount(1);
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
            throw new NotFoundError("图纸没有启动工序" + BpmCode.DRAWING_REDMARK);
        }

        BpmRuTask ruTask = null;
        Long actInstId = null;
        if (actInst.size() == 1) {
            List<BpmRuTask> ruTasks = ruTaskRepository.findByActInstId(actInst.get(0).getId());
            actInstId = ruTasks.get(0).getActInstId();
            ruTask = ruTasks.get(0);
        } else {
            if (uploadDTO.getTaskId() == null
                || uploadDTO.getTaskId().equals(0L)) {
                drawingUploadZipFileHistory.setFailedCount(1);
                drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
                throw new ValidationError("Please link to the current page from the todo-task page.");
            }
            ruTask = ruTaskRepository.findById(uploadDTO.getTaskId()).orElse(null);
            actInstId = ruTask == null ? null : ruTask.getActInstId();
        }

        if (oldDrawing.getActInstId() != null && actInstId != null
            && !actInstId.equals(oldDrawing.getActInstId())) {
            drawingUploadZipFileHistory.setFailedCount(1);
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
            throw new NotFoundError("有其他流程正在对图纸" + oldDrawing.getSubDrawingNo() + "_" + oldDrawing.getPageNo() + "进行redmark");
        }

        if (ruTask == null
            || !ruTask.getTaskDefKey().equals(BpmTaskDefKey.USERTASK_REDMARK_DESIGN.getType())) {
            drawingUploadZipFileHistory.setFailedCount(1);
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
            throw new ValidationError("当前节点不能上传图纸");
        }

        if (uploadDTO.getVersion().equals(oldDrawing.getSubDrawingVersion())) {
            throw new BusinessError("当前上传图纸版本和有效版本冲突，请重新确定要上传的图纸文件版本");
        }

        List<SubDrawing> runSubDrawings = subDrawingRepository.findByOrgIdAndProjectIdAndSubDrawingNoAndPageNoAndStatus(
            orgId,
            projectId,
            oldDrawing.getSubDrawingNo(),
            oldDrawing.getPageNo(),
            EntityStatus.ACTIVE);
        if (runSubDrawings != null && runSubDrawings.size() > 0) {
            throw new BusinessError("当前上传图纸正在redMark中");
        }

        if (subDrawing == null) {
            subDrawing = BeanUtils.copyProperties(oldDrawing, new SubDrawing(), "id", "reviewStatus", "status");
        }
        logger.error("redmark5 保存docs服务->开始");
        JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
            uploadDTO.getFileName(), new FilePostDTO());
        logger.error("redmark5 保存docs服务->结束");
        FileES f = responseBody.getData();

        String qrCode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();

        String version = uploadDTO.getVersion();
        if (version != null || !"".equals(version)) {

            subDrawing.setSubDrawingVersion(version);
        }
        subDrawing.setActInstId(actInstId);
        subDrawing.setIssued(false);
        subDrawing.setQrCode(qrCode);
        subDrawing.setStatus(EntityStatus.ACTIVE);
        subDrawing.setComment("REDMARK");

        List<SubDrawingHistory> hisl = subDrawingHisRepository
            .findBySubDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersion(subDrawing.getId(), oldDrawing.getSubDrawingNo(),
                oldDrawing.getPageNo(), uploadDTO.getVersion());
        SubDrawingHistory his = new SubDrawingHistory();
        if (!hisl.isEmpty()) {
            his = hisl.get(0);
        }

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
        his.setQrCode(subDrawing.getQrCode());
        his.setFileId(LongUtils.parseLong(f.getId()));
        his.setFileName(f.getName());
        his.setMemo(uploadDTO.getComment());
        his.setFilePath(f.getPath());
        subDrawingHisRepository.save(his);

        subDrawing.setFileName(f.getName());
        subDrawing.setFilePath(f.getPath());
        subDrawing.setFileId(LongUtils.parseLong(f.getId()));
        subDrawingRepository.save(subDrawing);







        drawingUploadZipFileHistory.setSuccessCount(1);
        drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(f.getId()));
        drawingUploadZipFileHistory.setFileName(f.getName());
        drawingUploadZipFileHistory.setFilePath(f.getPath());

        List<ActReportDTO> documents = new ArrayList<>();
        ActReportDTO report = new ActReportDTO();
        report.setFileId(LongUtils.parseLong(f.getId()));
        report.setFilePath(f.getPath());
        documents.add(report);
        ruTask.setJsonDocuments(documents);
        ruTaskRepository.save(ruTask);

        drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);

    }

    private String getNewRedMarkVersion(String version) {
        boolean containR = false;
        String oldVersion = version;
        String newVersion = version + "1";
        if (version.contains("R")) {
            containR = true;
            oldVersion = oldVersion.replace("R", "");
        }

        if (oldVersion.length() <= 2) {
            newVersion = oldVersion + "01";
        } else {
            try {
                String temp = "" + (Integer.parseInt(oldVersion) + 1);
                for (int i = temp.length(); i < oldVersion.length(); i++) {
                    temp = "0" + temp;
                }
                newVersion = temp;
            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }

        if (containR) {
            newVersion = "R" + newVersion;
        }
        return newVersion;
    }


}
