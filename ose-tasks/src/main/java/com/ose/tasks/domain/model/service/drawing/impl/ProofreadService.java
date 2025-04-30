package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.util.*;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskInterface;
import com.ose.tasks.dto.bpm.TaskGatewayDTO;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.vo.BpmTaskDefKey;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FileMetadataDTO;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.exception.ValidationError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.BpmActTaskAssigneeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmRuTaskRepository;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.domain.model.service.drawing.SubDrawingInterface;
import com.ose.tasks.domain.model.service.drawing.ProofreadInterface;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.bpm.BpmActTaskAssignee;
import com.ose.tasks.entity.bpm.BpmRuTask;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.tasks.vo.drawing.DrawingUploadZipFileSuccessFlg;
import com.ose.tasks.vo.SuspensionState;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.bouncycastle.util.encoders.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Component
public class ProofreadService implements ProofreadInterface {

    private final static Logger logger = LoggerFactory.getLogger(ZipUtils.class);

    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    private final DrawingRepository drawingRepository;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final BpmRuTaskRepository ruTaskRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingSubReviewOpinionRepository opinionRepository;

    private final SubDrawingHistoryRepository subDrawingHisRepository;

    private final DrawingUploadZipFileHistoryRepository drawingUploadZipFileHistoryRepository;

    private final DrawingUploadZipFileHistoryDetailRepository drawingUploadZipFileHistoryDetailRepository;

    private final BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private final ActivityTaskInterface activityTaskService;

    private final SubDrawingInterface subDrawingService;

    private final UserFeignAPI userFeignAPI;

    private final UploadFeignAPI uploadFeignAPI;

    private final DrawingBaseInterface drawingBaseService;

    /**
     * 构造方法
     */
    @Autowired
    public ProofreadService(
        DrawingRepository drawingRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        BpmRuTaskRepository ruTaskRepository,
        SubDrawingRepository subDrawingRepository,
        DrawingSubReviewOpinionRepository opinionRepository,
        SubDrawingHistoryRepository subDrawingHisRepository,
        DrawingUploadZipFileHistoryRepository drawingUploadZipFileHistoryRepository,
        DrawingUploadZipFileHistoryDetailRepository drawingUploadZipFileHistoryDetailRepository,
        BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository,
        ActivityTaskInterface activityTaskService, SubDrawingInterface subDrawingService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        DrawingBaseInterface drawingBaseService) {
        this.drawingRepository = drawingRepository;
        this.bpmActInstRepository = bpmActInstRepository;
        this.ruTaskRepository = ruTaskRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.opinionRepository = opinionRepository;
        this.subDrawingHisRepository = subDrawingHisRepository;
        this.drawingUploadZipFileHistoryRepository = drawingUploadZipFileHistoryRepository;
        this.drawingUploadZipFileHistoryDetailRepository = drawingUploadZipFileHistoryDetailRepository;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
        this.activityTaskService = activityTaskService;
        this.subDrawingService = subDrawingService;
        this.userFeignAPI = userFeignAPI;
        this.uploadFeignAPI = uploadFeignAPI;
        this.drawingBaseService = drawingBaseService;
    }


    @Override
    public Page<DrawingDetail> getProofreadDrawingList(
        Long orgId,
        Long projectId,
        Long userId,
        PageDTO page,
        ProofreadDrawingListCriteriaDTO criteriaDTO
    ) {
        return drawingRepository
            .getProofreadDrawingList(
                orgId,
                projectId,
                userId,
                page,
                criteriaDTO
            );
    }

    @Override
    public ProofreadTaskDTO getDrawingTaskInfo(
        Long orgId,
        Long projectId,
        Long drawingId,
        DrawingProofreadSearchDTO drawingProofreadSearchDTO
    ) {
        ProofreadTaskDTO dto = new ProofreadTaskDTO();

        if (drawingProofreadSearchDTO.getActInstId() == null || drawingProofreadSearchDTO.getActInstId().equals("")) {
            throw new NotFoundError("找不到procInstId,需从待办任务中跳转");
        }
        if (drawingProofreadSearchDTO.getTaskId() == null || drawingProofreadSearchDTO.getTaskId().equals(0L)) {
            throw new NotFoundError("找不到taskId,需从待办任务中跳转");
        }

        Drawing drawing = drawingRepository
            .findById(drawingId)
            .orElse(null);
        if (drawing == null) {
            throw new NotFoundError("找不到图集");
        }

        BpmActivityInstanceBase actInst = bpmActInstRepository
            .findByProjectIdAndActInstIdAndFinishStateAndSuspensionState(
                projectId,
                drawingProofreadSearchDTO.getActInstId(),
                ActInstFinishState.NOT_FINISHED,
                SuspensionState.ACTIVE
            ).orElse(null);
        if (actInst == null) {
            throw new NotFoundError("找不到图集流程实例");
        }

        BpmRuTask ruTask = ruTaskRepository.findById(drawingProofreadSearchDTO.getTaskId()).orElse(null);
        if (ruTask == null) {
            throw new NotFoundError("找不到图集流程任务");
        }
        dto.setTaskId(ruTask.getId().toString());
        dto.setTaskDefKey(ruTask.getTaskDefKey());

        List<TaskGatewayDTO> gateWayDTOs = activityTaskService.getTaskGateway(projectId, actInst.getProcessId(),
            actInst.getBpmnVersion(), ruTask.getTaskDefKey());



        dto.setGateway(gateWayDTOs);

        String checkKey = "";
        String designKey = "";
        String reviewKey = "";
        if (drawingProofreadSearchDTO.getProcess().equals("DRAWING-REDMARK")) {
            checkKey = BpmTaskDefKey.USERTASK_REDMARK_CHECK.getType();
            designKey = BpmTaskDefKey.USERTASK_REDMARK_DESIGN.getType();
            reviewKey = BpmTaskDefKey.USERTASK_DRAWING_REVIEW.getType();
        } else {
            checkKey = BpmTaskDefKey.USERTASK_DRAWING_CHECK.getType();
            designKey = BpmTaskDefKey.USERTASK_DRAWING_DESIGN.getType();
            reviewKey = BpmTaskDefKey.USERTASK_DRAWING_REVIEW.getType();
        }
        BpmActTaskAssignee draw = bpmActTaskAssigneeRepository.findByActInstIdAndTaskDefKeyAndStatus(
            actInst.getId(),
            designKey,
            EntityStatus.ACTIVE
        );
        if (draw != null) dto.setDrawUserId(draw.getAssignee());

        BpmActTaskAssignee check = bpmActTaskAssigneeRepository.findByActInstIdAndTaskDefKeyAndStatus(
            actInst.getId(),
            checkKey,
            EntityStatus.ACTIVE
        );
        if (check != null) dto.setCheckUserId(check.getAssignee());

        BpmActTaskAssignee approved = bpmActTaskAssigneeRepository.findByActInstIdAndTaskDefKeyAndStatus(
            actInst.getId(),
            reviewKey,
            EntityStatus.ACTIVE
        );
        if (approved != null) dto.setApprovedUserId(approved.getAssignee());

        PageDTO page = new PageDTO();
        page.setFetchAll(true);
        SubDrawingCriteriaDTO criteriaDTO = new SubDrawingCriteriaDTO();
        criteriaDTO.setOrderByNo(true);
        criteriaDTO.setDrawingVersion(drawing.getLatestRev());
        criteriaDTO.setActInstId(drawingProofreadSearchDTO.getActInstId());
        Page<SubDrawing> subList = subDrawingService.getList(
            orgId,
            projectId,
            drawingId,
            page,
            criteriaDTO
        );

        Integer initCount = 0;
        Integer checkCount = 0;
        Integer reviewCount = 0;
        Integer modifyCount = 0;
        Integer checkDoneCount = 0;
        Integer reviewDoneCount = 0;
        Integer totalCount = 0;
        if (subList != null) {
            for (SubDrawing sub : subList.getContent()) {
                switch (sub.getReviewStatus()) {
                    case INIT:
                        initCount++;
                        break;
                    case CHECK:
                        checkCount++;
                        break;
                    case REVIEW:
                        reviewCount++;
                        break;
                    case MODIFY:
                        modifyCount++;
                        break;
                    case CHECK_DONE:
                        checkDoneCount++;
                        break;
                    case REVIEW_DONE:
                        reviewDoneCount++;
                        break;
                }
                totalCount++;
            }
        }
        dto.setInitCount(initCount);
        dto.setCheckCount(checkCount);
        dto.setReviewCount(reviewCount);
        dto.setModifyCount(modifyCount);
        dto.setReviewDoneCount(reviewDoneCount);
        dto.setCheckDoneCount(checkDoneCount);
        dto.setTotalCount(totalCount);
        return dto;
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
        String drawingBase = "";

        try {
            File subDrawingFile = new File(protectedDir + subDrawing.getFilePath().substring(1));
            if (subDrawingFile.length() > 1024000) {

                String subDrawingImg = pdfToImg(
                    subDrawing.getSubDrawingNo(),
                    protectedDir + subDrawing.getFilePath().substring(1)
                );















                String subDrawingPdf = convertImgToPDF(
                    subDrawingImg,
                    subDrawing.getSubDrawingNo(),
                    temporaryDir
                );
                drawingBase = getBaseFromFile(
                    new File(subDrawingPdf)
                );
            } else {
                drawingBase = getBaseFromFile(
                    new File(protectedDir + subDrawing.getFilePath().substring(1))
                );

            }
        } catch (Exception e) {

        }

        dto.setSubDrawingBase(
            drawingBase
        );

        return dto;
    }

    @Override
    public List<SubDrawingReviewOpinion> getSubDrawingOpinionList(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        Long taskId
    ) {
        BpmRuTask ruTask = ruTaskRepository
            .findById(taskId).orElse(null);
        if (ruTask == null) {
            throw new NotFoundError("找不到校审任务");
        }
        List<SubDrawingReviewOpinion> list = opinionRepository
            .findByOrgIdAndProjectIdAndSubDrawingIdAndActInstIdAndParentIdIsNullOrderByCreatedAtAsc(
                orgId,
                projectId,
                subDrawingId,
                ruTask.getActInstId()
            );
        Map<Long, String> operatorMap = new HashMap<>();
        for (SubDrawingReviewOpinion parent : list) {
            List<SubDrawingReviewOpinion> subList =
                opinionRepository.findByOrgIdAndProjectIdAndParentIdOrderByCreatedAtAsc(
                    orgId,
                    projectId,
                    parent.getId()
                );
            for (SubDrawingReviewOpinion sub : subList) {
                if (operatorMap.keySet().contains(sub.getOperator())) {
                    sub.setOperatorName(operatorMap.get(sub.getOperator()));
                } else {
                    UserProfile user = userFeignAPI.get(sub.getOperator()).getData();
                    if (user != null) {
                        sub.setOperatorName(user.getName());
                        operatorMap.put(sub.getOperator(), user.getName());
                    }
                }
            }
            parent.setReplyOpinions(subList);
        }
        return list;
    }

    @Override
    public SubDrawingReviewOpinion createSubDrawingOpinion(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        Long taskId,
        SubDrawingReviewOpinionDTO dto,
        Long userId
    ) {
        SubDrawing subDrawing = subDrawingRepository
            .findById(subDrawingId)
            .orElse(null);
        if (subDrawing == null) {
            throw new NotFoundError("找不到子图纸");
        }

        if (
            subDrawing.getReviewStatus() != DrawingReviewStatus.CHECK
                && subDrawing.getReviewStatus() != DrawingReviewStatus.REVIEW
        ) {
            throw new BusinessError("无法添加意见，请检查子图纸的校审状态");
        }
        BpmRuTask ruTask = ruTaskRepository
            .findById(taskId).orElse(null);
        if (ruTask == null) {
            throw new NotFoundError("找不到校审任务");
        }

        subDrawing.setReviewStatus(DrawingReviewStatus.MODIFY);
        subDrawingRepository.save(subDrawing);

        SubDrawingReviewOpinion subDrawingOpinion = new SubDrawingReviewOpinion();
        subDrawingOpinion.setContent(dto.getContent());
        subDrawingOpinion.setDrawingId(subDrawing.getDrawingId());
        subDrawingOpinion.setOperator(userId);
        subDrawingOpinion.setOrgId(orgId);
        subDrawingOpinion.setActInstId(ruTask.getActInstId());
        subDrawingOpinion.setProjectId(projectId);
        subDrawingOpinion.setSubDrawingId(subDrawingId);
        subDrawingOpinion.setCreatedAt();
        subDrawingOpinion.setLastModifiedAt();
        subDrawingOpinion.setStatus(EntityStatus.ACTIVE);
        return opinionRepository.save(subDrawingOpinion);
    }

    @Override
    public void modifyReviewStatus(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        SubDrawingReviewStatusDTO dto
    ) {
        SubDrawing subDrawing = subDrawingRepository
            .findById(subDrawingId)
            .orElse(null);
        if (subDrawing == null) {
            throw new NotFoundError("找不到子图纸");
        }
        subDrawing.setReviewStatus(dto.getReviewStatus());
        subDrawingRepository.save(subDrawing);
    }

    @Override
    public void batchModifyReviewStatus(
        Long orgId,
        Long projectId,
        SubDrawingReviewStatusDTO dto
    ) {
        if (dto.getSubDrawingList().size() > 0) {
            for (Long subDrawingId:dto.getSubDrawingList()) {
                SubDrawing subDrawing = subDrawingRepository
                    .findById(subDrawingId)
                    .orElse(null);
                if (subDrawing == null) {
                    throw new NotFoundError("找不到子图纸");
                }
                if (subDrawing.getReviewStatus().equals(DrawingReviewStatus.CHECK) || subDrawing.getReviewStatus().equals(DrawingReviewStatus.REVIEW)) {
                    subDrawing.setReviewStatus(dto.getReviewStatus());
                    subDrawingRepository.save(subDrawing);
                }
            }
        }
    }

    @Override
    public ProofreadUploadResponseDTO uploadSubDrawing(
        Long orgId,
        Long projectId,
        Long drawingId,
        ProofreadPipingDrawingUploadDTO uploadDTO,
        OperatorDTO operator
    ) {
        String temporaryFileName = uploadDTO.getFileName();

        File diskFile = new File(temporaryDir, temporaryFileName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);
        String uploadFileName = metadata.getFilename();
        String fileType = FileUtils.extname(uploadFileName);
        if (fileType.toLowerCase().equals(".pdf")) {
            return uploadSubDrawingByPdf(
                orgId,
                projectId,
                drawingId,
                uploadFileName,
                temporaryFileName,
                operator,
                false,
                uploadDTO
            );
        } else if (fileType.toLowerCase().equals(".zip")) {
            return uploadSubDrawingByZip(
                orgId,
                projectId,
                drawingId,
                operator,
                diskFile,
                uploadDTO
            );
        } else {
            throw new ValidationError("上传文件类型错误");
        }
    }

    @Override
    public SubDrawingReviewOpinion createOpinionReply(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        Long taskId,
        Long opinionId,
        SubDrawingReviewOpinionDTO dto,
        Long userId
    ) {
        SubDrawingReviewOpinion parent = opinionRepository
            .findById(opinionId)
            .orElse(null);
        if (parent == null) {
            throw new NotFoundError();
        }
        SubDrawingReviewOpinion subOpinion =
            BeanUtils.copyProperties(
                parent,
                new SubDrawingReviewOpinion(),
                "id"
            );
        subOpinion.setContent(dto.getContent());
        subOpinion.setOperator(userId);
        subOpinion.setCreatedAt();
        subOpinion.setLastModifiedAt();
        subOpinion.setStatus(EntityStatus.ACTIVE);
        subOpinion.setParentId(opinionId);
        return opinionRepository.save(subOpinion);
    }

    private ProofreadUploadResponseDTO uploadSubDrawingByPdf(
        Long orgId,
        Long projectId,
        Long drawingId,
        String uploadFileName,
        String tempFileName,
        OperatorDTO operator,
        boolean zip,
        ProofreadPipingDrawingUploadDTO uploadDTO
    ) {
        boolean result = true;
        String msg = null;

        String regEx = "_(\\d{1,3})_([\\s\\S]*)$";
        String[] str = uploadFileName.split("\\.");
        String fileName = "";
        for (int i = 0; i < str.length - 1; i++) {
            fileName += str[i] + ".";
        }
        fileName = fileName.substring(0, fileName.length() - 1);

        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
        drawingUploadZipFileHistory.setCreatedAt();
        drawingUploadZipFileHistory.setDrawingId(drawingId);
        drawingUploadZipFileHistory.setFileName(fileName);
        drawingUploadZipFileHistory.setOperator(operator.getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);


        Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + tempFileName);
        if (pageCounts != 1) {
            drawingUploadZipFileHistory.setFailedCount(1);
            result = false;
            msg = "PDF页数错误";
        } else {

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
                String matchRegEx = ms.group();
                drawingNo = fileName.substring(0, fileName.length() - matchRegEx.length());
            }
            SubDrawing subDrawing = null;
            if (drawingNo.equals("") || pageNo.equals("") || drawingVersion.equals("")) {
                drawingUploadZipFileHistory.setFailedCount(1);
                result = false;
                msg = "文件名解析错误";
            } else {
                List<SubDrawing> subDrawingList = subDrawingRepository
                    .findByDrawingIdAndSubDrawingNoAndPageNoAndStatus(drawingId, drawingNo, Integer.valueOf(pageNo), EntityStatus.PENDING);
                if (!subDrawingList.isEmpty()) {
                    for (SubDrawing sub : subDrawingList) {
                        if (sub.getSubDrawingVersion().equals(drawingVersion)) {
                            subDrawing = sub;
                        }
                    }
                }
                if (
                    uploadDTO.getSubDrawingId() != null
                        && subDrawing != null
                        && !uploadDTO.getSubDrawingId().equals(subDrawing.getId())
                ) {
                    drawingUploadZipFileHistory.setFailedCount(1);
                    result = false;
                    msg = "文件名不匹配.";
                }
            }
            if (subDrawing != null
                && drawingNo.equals(subDrawing.getSubDrawingNo())
                && pageNo.equals("" + subDrawing.getPageNo())
            ) {

                if (subDrawing.getReviewStatus() == DrawingReviewStatus.REVIEW_DONE) {
                    drawingUploadZipFileHistory.setFailedCount(1);
                    result = false;
                    msg = "无法替换图纸.";
                }
                logger.error("图纸预览1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(
                    orgId.toString(), projectId.toString(),
                    tempFileName,
                    new FilePostDTO()
                );
                logger.error("图纸预览1 保存docs服务->结束");
                FileES f = responseBody.getData();

                String qrCode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();
                List<SubDrawingHistory> hisl = subDrawingHisRepository
                    .findBySubDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersion(
                        subDrawing.getId(),
                        drawingNo,
                        Integer.parseInt(pageNo),
                        drawingVersion
                    );
                if (!hisl.isEmpty()) {

                    if (result) {
                        for (SubDrawingHistory his : hisl) {
                            subDrawingHisRepository.delete(his);
                        }

                        SubDrawingHistory hisH = hisl.get(0);
                        SubDrawingHistory his = new SubDrawingHistory();
                        his.setDrawingDetailId(subDrawing.getDrawingDetailId());
                        his.setSubDrawingId(subDrawing.getId());
                        his.setSubDrawingVersion(hisH.getSubDrawingVersion());
                        his.setSubDrawingNo(subDrawing.getSubDrawingNo());
                        his.setPageNo(subDrawing.getPageNo());
                        his.setCreatedAt();
                        his.setOrgId(orgId);
                        his.setProjectId(projectId);
                        his.setStatus(EntityStatus.ACTIVE);
                        his.setOperator(operator.getId());
                        his.setQrCode(qrCode);
                        his.setFileId(LongUtils.parseLong(f.getId()));
                        his.setFileName(f.getName());
                        his.setFilePath(f.getPath());
                        subDrawingHisRepository.save(his);

                        subDrawing.setFileName(f.getName());
                        subDrawing.setFilePath(f.getPath());
                        subDrawing.setFileId(LongUtils.parseLong(f.getId()));
                        subDrawing.setQrCode(qrCode);
                        subDrawing.setReviewStatus(DrawingReviewStatus.INIT);
                        subDrawingRepository.save(subDrawing);

                    }
                }
            } else {
                drawingUploadZipFileHistory.setFailedCount(1);
                result = false;
                msg = "找不到文件名对应的子图纸";
            }
        }
        if (!zip) {
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
        }
        ProofreadUploadResponseDTO resultDTO = new ProofreadUploadResponseDTO();
        resultDTO.setSuccessful(result);
        if (!result) {
            List<ProofreadUploadResponseDTO.ConfirmResponseDTO> list = new ArrayList<>();
            list.add(
                resultDTO.new ConfirmResponseDTO(
                    uploadFileName,
                    msg
                )
            );
            resultDTO.setResponseDetails(list);
        }
        return resultDTO;
    }

    private ProofreadUploadResponseDTO uploadSubDrawingByZip(
        Long orgId,
        Long projectId,
        Long drawingId,
        OperatorDTO operator,
        File diskFile,
        ProofreadPipingDrawingUploadDTO uploadDTO
    ) {
        ProofreadUploadResponseDTO result = new ProofreadUploadResponseDTO();
        result.setSuccessful(true);
        List<ProofreadUploadResponseDTO.ConfirmResponseDTO> responseDetails = new ArrayList<>();

        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();

        String fileFolder = temporaryDir + File.separator + CryptoUtils.uniqueId().toUpperCase();
        File folder = new File(fileFolder);
        folder.mkdirs();


        List<File> uploadFiles = new ArrayList<>();

        drawingBaseService.handleUploadedZipFiles(fileFolder, diskFile, uploadFiles);



        drawingUploadZipFileHistory.setFileName(diskFile.getName());
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setCreatedAt();
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setFileCount(uploadFiles.size());
        drawingUploadZipFileHistory.setOperator(operator.getId());
        drawingUploadZipFileHistory.setDrawingId(drawingId);
        drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
        Long drawingUploadZipFileHistoryId = drawingUploadZipFileHistory.getId();

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

            logger.error("图纸预览1 上传docs服务->开始");
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
            logger.error("图纸预览1 上传docs服务->结束");
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

            if (fileTypePDF.toLowerCase().equals(".pdf")) {
                ProofreadUploadResponseDTO pdfResponse = uploadSubDrawingByPdf(
                    orgId,
                    projectId,
                    drawingId,
                    uploadFileNamePDF,
                    temporaryFileDTO.getName(),
                    operator,
                    true,
                    uploadDTO
                );
                if (!pdfResponse.getSuccessful()) {
                    result.setSuccessful(false);
                    responseDetails.addAll(pdfResponse.getResponseDetails());
                    drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                } else {
                    drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.SUCCESS);
                }

            } else {
                drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                responseDetails.add(
                    result.new ConfirmResponseDTO(uFile.getName(), "file type")
                );
            }
            drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
            drawingUploadZipFileHistoryDetail.setCreatedAt();
            drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
        }

        result.setResponseDetails(responseDetails);
        return result;
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
     * pdf 转图片
     */
    public String pdfToImg(
        String drawingNo,
        String pdfPath
    ) {

        float DEFAULT_DPI = 105;

        String DEFAULT_FORMAT = "jpg";
        try {


            int width = 0;

            int[] singleImgRGB;
            int shiftHeight = 0;

            BufferedImage imageResult = null;

            PDDocument pdDocument = PDDocument.load(new File(pdfPath));
            PDFRenderer renderer = new PDFRenderer(pdDocument);

            BufferedImage image = renderer.renderImageWithDPI(0, DEFAULT_DPI, ImageType.RGB);
            int imageHeight = image.getHeight();
            int imageWidth = image.getWidth();


            width = imageWidth;

            imageResult = new BufferedImage(width, imageHeight, BufferedImage.TYPE_INT_RGB);

            singleImgRGB = image.getRGB(0, 0, width, imageHeight, null, 0, width);

            imageResult.setRGB(0, shiftHeight, width, imageHeight, singleImgRGB, 0, width);
            pdDocument.close();

            String imgPath = temporaryDir + drawingNo + ".jpg";
            ImageIO.write(imageResult, DEFAULT_FORMAT, new File(imgPath));
            return imgPath;
        } catch (Exception e) {
            e.printStackTrace();
            return pdfPath;
        }
    }

    /**
     * 图片转pdf
     */
    public String convertImgToPDF(
        String imagePath,
        String fileName,
        String destDir) throws IOException {
        PDDocument document = new PDDocument();
        InputStream in = new FileInputStream(imagePath);
        BufferedImage bimg = ImageIO.read(in);
        float width = bimg.getWidth();
        float height = bimg.getHeight();
        PDPage page = new PDPage(new PDRectangle(width, height));
        document.addPage(page);
        PDImageXObject img = PDImageXObject.createFromFile(imagePath, document);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(img, 0, 0);
        contentStream.close();
        in.close();
        document.save(destDir + "/" + fileName + ".pdf");
        document.close();
        return destDir + "/" + fileName + ".pdf";
    }

}
