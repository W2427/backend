package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.util.*;
import com.ose.auth.api.UserFeignAPI;
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
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.BpmActTaskAssigneeRepository;
import com.ose.tasks.domain.model.repository.bpm.BpmActivityInstanceRepository;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.service.BatchTaskInterface;
import com.ose.tasks.domain.model.service.ProjectService;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.bpm.TaskNotificationInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.domain.model.service.drawing.SubDrawingInterface;
import com.ose.tasks.dto.ActReportDTO;
import com.ose.tasks.dto.BatchResultDTO;
import com.ose.tasks.dto.QRCodeSearchResultDrawingDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.BpmActTaskAssignee;
import com.ose.tasks.entity.bpm.BpmActivityInstanceBase;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.BpmCode;
import com.ose.tasks.vo.drawing.DrawingReviewStatus;
import com.ose.tasks.vo.drawing.DrawingUploadZipFileSuccessFlg;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.vo.BpmTaskDefKey;
import com.ose.vo.DrawingFileType;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.RecordFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Component

public class SubDrawingService implements SubDrawingInterface {

    private final static Logger logger = LoggerFactory.getLogger(SubDrawingService.class);


    private SubDrawingRepository subDrawingRepository;

    private SubDrawingHistoryRepository subDrawingHisRepository;

    private DrawingRepository drawingRepository;

    private DrawingDetailRepository drawingDetailRepository;

    private DrawingFileRepository drawingFileRepository;

    private SubDrawingConfigRepository subDrawingConfigRepository;

    private DrawingUploadZipFileHistoryRepository drawingUploadZipFileHistoryRepository;

    private DrawingUploadZipFileHistoryDetailRepository drawingUploadZipFileHistoryDetailRepository;

    private BpmActivityInstanceRepository bpmActInstRepository;

    private TaskNotificationInterface taskNotificationService;

    private final EntitySubTypeInterface entitySubTypeService;

    private UploadFeignAPI uploadFeignAPI;

    private BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository;

    private DrawingEntryDelegateRepository drawingEntryDelegateRepository;

    private final DrawingBaseInterface drawingBaseService;

    private final BatchTaskInterface batchTaskService;

    private final ProjectService projectService;

    private final DrawingZipHistoryRepository drawingZipHistoryRepository;

    private UserFeignAPI userFeignAPI;


    @Value("${application.files.temporary}")
    private String temporaryDir;

    @Value("${application.files.protected}")
    private String protectedDir;

    /**
     * 构造方法
     */
    @Autowired
    public SubDrawingService(
        SubDrawingRepository subDrawingRepository,
        SubDrawingHistoryRepository subDrawingHisRepository,
        DrawingRepository drawingRepository,
        DrawingUploadZipFileHistoryRepository drawingUploadZipFileHistoryRepository,
        DrawingUploadZipFileHistoryDetailRepository drawingUploadZipFileHistoryDetailRepository,
        SubDrawingConfigRepository subDrawingConfigRepository,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        BpmActivityInstanceRepository bpmActInstRepository,
        TaskNotificationInterface taskNotificationService,
        DrawingDetailRepository drawingDetailRepository,
        EntitySubTypeInterface entitySubTypeService, BpmActTaskAssigneeRepository bpmActTaskAssigneeRepository,
        DrawingEntryDelegateRepository drawingEntryDelegateRepository,
        DrawingBaseInterface drawingBaseService, @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UserFeignAPI userFeignAPI,
        BatchTaskInterface batchTaskService,
        ProjectService projectService,
        DrawingZipHistoryRepository drawingZipHistoryRepository,
        DrawingFileRepository drawingFileRepository
    ) {
        this.subDrawingRepository = subDrawingRepository;
        this.subDrawingHisRepository = subDrawingHisRepository;
        this.drawingUploadZipFileHistoryRepository = drawingUploadZipFileHistoryRepository;
        this.drawingUploadZipFileHistoryDetailRepository = drawingUploadZipFileHistoryDetailRepository;
        this.drawingRepository = drawingRepository;
        this.subDrawingConfigRepository = subDrawingConfigRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.bpmActInstRepository = bpmActInstRepository;
        this.taskNotificationService = taskNotificationService;
        this.drawingDetailRepository = drawingDetailRepository;
        this.entitySubTypeService = entitySubTypeService;
        this.bpmActTaskAssigneeRepository = bpmActTaskAssigneeRepository;
        this.drawingEntryDelegateRepository = drawingEntryDelegateRepository;
        this.drawingBaseService = drawingBaseService;
        this.userFeignAPI = userFeignAPI;
        this.batchTaskService = batchTaskService;
        this.projectService = projectService;
        this.drawingZipHistoryRepository = drawingZipHistoryRepository;
        this.drawingFileRepository = drawingFileRepository;
    }

    /**
     * 根据图纸id及子图纸图号查询子图纸
     */
    @Override
    public SubDrawing findByDrawingIdAndSubNo(Long drawingId, String subNo) {
        return subDrawingRepository.findByDrawingIdAndSubNoAndStatus(drawingId, subNo, EntityStatus.ACTIVE);
    }

    /**
     * 创建子图纸条目
     */
    @Override
    public SubDrawing create(Long orgId, Long projectId, Long drawingId, Long userid,
                             SubDrawingDTO dto) {


        Optional<Drawing> drawingOp = drawingRepository.findById(drawingId);
        Drawing drawing = drawingOp.get();
        DrawingDetail detail;
        if (dto.getSeq() == 0) {
            int maxSeq = 0;
            if (dto.getDrawingDetailId() != null && dto.getDrawingDetailId() != 0L) {
                detail = drawingDetailRepository.findById(dto.getDrawingDetailId()).get();
                if (detail != null) {
                    List<SubDrawing> list = this.getSubDrawingByDrawingIdAndDrawingVersion(drawingId, detail.getRev());
                    if (list != null && list.size() > 0) {
                        for (SubDrawing sub : list) {
                            if (sub.getSeq() > maxSeq) {
                                maxSeq = sub.getSeq();
                                dto.setSeq(maxSeq + 1);
                            }
                            if (dto.getSubDrawingNo().equals(sub.getSubDrawingNo()) && dto.getPageNo() == sub.getPageNo()) {
                                maxSeq = sub.getSeq();
                                dto.setSeq(maxSeq);
                                break;
                            }
                        }
                    }
                }
            }
        }

        List<SubDrawing> subDrawingO = subDrawingRepository
            .findByProjectIdAndSubDrawingNoAndDrawingIdNot(projectId, dto.getSubDrawingNo(), drawingId);
        if (!subDrawingO.isEmpty()) {
            throw new BusinessError("子图纸编号存在于其他图纸中");
        }


        List<SubDrawing> subDrawingCheckList = subDrawingRepository
            .findByProjectIdAndDrawingIdAndSubDrawingNoAndPageNoAndLatestRev(projectId, drawingId, dto.getSubDrawingNo(), dto.getPageNo(), dto.getSubDrawingVersion());
        if (subDrawingCheckList.size() > 0) {
            throw new BusinessError("子图纸编号存在于当前图纸中");
        }


        SubDrawing subDrawing = BeanUtils.copyProperties(dto, new SubDrawing());
        subDrawing.setSubNo(subDrawing.getSubDrawingNo() + "_" + subDrawing.getPageNo());
        subDrawing.setDrawingId(drawingId);
        subDrawing.setDrawingVersion(drawing.getLatestRev());
        subDrawing.setOperator(userid);
        subDrawing.setProjectId(projectId);
        subDrawing.setOrgId(orgId);
        subDrawing.setShortCode(dto.getShortCode());
        subDrawing.setActInstId(dto.getActInstId());
        subDrawing.setDrawingDetailId(dto.getDrawingDetailId());
        subDrawing.setCreatedAt();
        subDrawing.setStatus(EntityStatus.PENDING);
        subDrawing.setLatestRev(dto.getSubDrawingVersion());
        subDrawing.setReviewStatus(DrawingReviewStatus.INIT);
        subDrawingRepository.save(subDrawing);


        SubDrawingHistory his = new SubDrawingHistory();
        his.setUsed(true);
        his.setSubDrawingNo(subDrawing.getSubDrawingNo());
        his.setPageNo(subDrawing.getPageNo());
        his.setQrCode(subDrawing.getQrCode());
        his.setSubDrawingVersion(subDrawing.getSubDrawingVersion());
        his.setCreatedAt();
        his.setSubDrawingId(subDrawing.getId());
        his.setOrgId(orgId);
        his.setProjectId(projectId);
        his.setStatus(EntityStatus.ACTIVE);
        subDrawingHisRepository.save(his);
        return subDrawing;
    }

    /**
     * 保存子图纸信息
     */
    @Override
    public SubDrawing save(SubDrawing subDrawing) {
        return subDrawingRepository.save(subDrawing);
    }

    /**
     * 保存子图纸历史记录
     */
    @Override
    public SubDrawingHistory save(SubDrawingHistory his) {
        return subDrawingHisRepository.save(his);
    }

    /**
     * 修改子图纸信息
     */
    @Override
    public SubDrawing modify(Long orgId, Long projectId, Long drawingId, Long id, Long userid, SubDrawingDTO dto) {
        Optional<SubDrawing> subDrawingOp = subDrawingRepository.findById(id);


        List<SubDrawing> subDrawingO = subDrawingRepository
            .findByProjectIdAndSubDrawingNoAndDrawingIdNot(projectId, dto.getSubDrawingNo(), drawingId);
        if (!subDrawingO.isEmpty()) {
            throw new BusinessError("子图纸编号存在于其他图纸中");
        }


        List<SubDrawing> subDrawingCheckList = subDrawingRepository
            .findByProjectIdAndDrawingIdAndSubDrawingNoAndPageNoAndLatestRev(projectId, drawingId, dto.getSubDrawingNo(), dto.getPageNo(), dto.getSubDrawingVersion());
        if (subDrawingCheckList.size() > 1 || (subDrawingCheckList.size() == 1 && !subDrawingCheckList.get(0).getId().equals(subDrawingOp.get().getId()))) {
            throw new BusinessError("子图纸编号存在于当前图纸中");
        }

        if (subDrawingOp.isPresent()) {
            SubDrawing subDrawing = subDrawingOp.get();
            subDrawing.setSubNo(dto.getSubDrawingNo() + "_" + dto.getPageNo());
            subDrawing.setSubDrawingNo(dto.getSubDrawingNo());
            subDrawing.setSubDrawingVersion(dto.getSubDrawingVersion());
            subDrawing.setPageNo(dto.getPageNo());
            subDrawing.setPageCount(dto.getPageCount());
            subDrawing.setShortCode(dto.getShortCode());

            subDrawing.setActInstId(dto.getActInstId());
            subDrawing.setOperator(userid);
            subDrawing.setComment(dto.getComment());
            subDrawing.setLastModifiedAt();
            subDrawing.setDrawingDetailId(dto.getDrawingDetailId());
            subDrawingRepository.save(subDrawing);


            List<SubDrawing> subDrawingList = subDrawingRepository
                .findByOrgIdAndProjectIdAndDrawingIdAndSubDrawingNoAndDrawingDetailIdAndStatus(
                    orgId,
                    projectId,
                    subDrawing.getDrawingId(),
                    subDrawing.getSubDrawingNo(),
                    dto.getDrawingDetailId(),
                    EntityStatus.PENDING);
            for (SubDrawing sub : subDrawingList) {
                sub.setPageCount(dto.getPageCount());
                subDrawingRepository.save(sub);
            }

            return subDrawing;
        }
        return null;
    }

    /**
     * 根据子图纸id查询历史记录
     */
    @Override
    public SubDrawingHistory findDrawingSubPipingHistoryBySubDrawingId(Long id) {
        List<SubDrawingHistory> his = subDrawingHisRepository.findBySubDrawingIdAndStatusOrderByCreatedAtDesc(id,
            EntityStatus.ACTIVE);
        if (!his.isEmpty()) {
            return his.get(0);
        }
        return null;
    }

    /**
     * 查询子图纸清单
     */
    @Override
    public Page<SubDrawing> getList(Long orgId, Long projectId, Long drawingId, PageDTO page,
                                    SubDrawingCriteriaDTO criteriaDTO) {
             Page<SubDrawing> result = subDrawingRepository.searchLatestSubDrawing(orgId, projectId, drawingId, criteriaDTO, page.toPageable());
        return result;


    }

    /**
     * 查询子图纸清单
     */
    @Override
    public Page<SubDrawing> getSubList(Long orgId, Long projectId, PageDTO page,
                                       SubDrawingCriteriaDTO criteriaDTO) {
        Page<SubDrawing> result = subDrawingRepository.searchAllSubDrawing(orgId, projectId, criteriaDTO, page.toPageable());
        return result;
    }

    /**
     * 合并下载ISO子图纸。
     *
     * @param orgId
     * @param projectId
     * @param page
     * @param criteriaDTO
     * @return
     */

    @Override
    public void downSubList(
        Long orgId,
        Long projectId,
        Long drawingId,
        PageDTO page,
        SubDrawingCriteriaDTO criteriaDTO,
        OperatorDTO operatorDTO,
        ContextDTO context
    ) {
        Project project = projectService.get(orgId, projectId);

        Drawing drawing = drawingRepository.findByOrgIdAndProjectIdAndId(orgId, projectId, drawingId);
        // 创建合并历史记录

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
        drawingZipDetail.setDrawingNo(drawing.getDwgNo());
//        drawingZipDetail.setDrawer(drawing.getDrawer());

        Long drawingFileId = drawingZipDetail.getId();
        System.out.println(drawingZipDetail.getId());
        drawingZipHistoryRepository.save(drawingZipDetail);

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
                // 1.获取ISO下的所有子图纸
                Page<SubDrawing> subDrawingPage = getList(orgId, projectId, drawingId, page, criteriaDTO);
                String[] filePathArray = new String[subDrawingPage.getContent().size()];
                for (int i = 0; i < subDrawingPage.getContent().size(); i++) {
                    filePathArray[i] = protectedDir + subDrawingPage.getContent().get(i).getFilePath();
                }
                // 获取图纸打包历史
                DrawingZipDetail drawinghistory = drawingZipHistoryRepository.findById(drawingFileId).get();
                drawinghistory.setDrawingVersion(criteriaDTO.getDrawingVersion());
                drawinghistory.setIsRedMark(criteriaDTO.getIsRedMark());
                drawinghistory.setStatus(criteriaDTO.getStatus());

                // 2.将获取到的子图纸进行合并
                String fileName = criteriaDTO.getFileName() + ".pdf";
                String filePath = protectedDir + fileName;
                PdfUtils.mergePdfFiles(filePathArray,filePath);
                File file = new File(filePath);

                DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory())
                    .createItem("file", MediaType.APPLICATION_PDF_VALUE, true, file.getName());
                try {
                    IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());

                    // 3.将合并后的文件进行上传
                    MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                        APPLICATION_PDF_VALUE, fileItem.getInputStream());
                    JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                        .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                    JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        tempFileResBody.getData().getName(), new FilePostDTO());

                    // 4.将合并后的图纸保存路径进行更新
                    if (fileESResBody != null) {
                        drawinghistory.setFileName(fileName);
                        drawinghistory.setFileId(fileESResBody.getData().getId());
                        drawinghistory.setFilePath(fileESResBody.getData().getPath());
                        drawinghistory.setPackageStatus(EntityStatus.FINISHED);

                    }else {
                        throw new BusinessError("子图纸文件合并下载失败");
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
     * 查找主图纸下的子图纸（根据主图纸版本）。
     *
     * @param drawingId      图纸id
     * @param drawingVersion 图纸版本
     * @return
     */
    private List<SubDrawing> getSubDrawingByDrawingIdAndDrawingVersion(Long drawingId, String drawingVersion) {


        Optional<Drawing> op = drawingRepository.findById(drawingId);


        if (op.isPresent()) {


            Drawing dwg = op.get();
            Optional<DrawingDetail> opD = drawingDetailRepository
                .findByDrawingIdAndRevAndStatus(drawingId, drawingVersion, EntityStatus.ACTIVE);


            if (opD.isPresent()) {
                DrawingDetail detail = opD.get();
                List<SubDrawing> result = new ArrayList<>();


                List<SubDrawing> subList = subDrawingRepository.findByDrawingIdAndStatusAndRevOrderLE(dwg.getId(),
                    null, detail.getRevOrder());


                Map<String, Long> subResult = new HashMap<>();
                for (SubDrawing sub : subList) {
                    subResult.put(sub.getSubDrawingNo() + "_" + sub.getPageNo(), sub.getId());
                }
                for (SubDrawing sub : subList) {

                    result.add(sub);

                }
                return result;
            }
        }
        return null;
    }

    /**
     * 获取子图纸清单条目详细信息
     */
    @Override
    public SubDrawing get(Long orgId, Long projectId, Long id) {
        Optional<SubDrawing> subDrawingOp = subDrawingRepository.findById(id);
        if (subDrawingOp.isPresent()) {
            return subDrawingOp.get();
        }
        return null;
    }

    /**
     * 删除ISO图纸条目
     */
    @Override
    public boolean delete(Long orgId, Long projectId, Long id) {
        Optional<SubDrawing> subDrawingOp = subDrawingRepository.findById(id);
        if (subDrawingOp.isPresent()) {
            SubDrawing sub = subDrawingOp.get();
            if (sub.getIssued()) {
                sub.setStatus(EntityStatus.DELETED);
                subDrawingRepository.save(sub);
            } else {
                subDrawingRepository.delete(sub);
                List<SubDrawingHistory> hiss = subDrawingHisRepository.findBySubDrawingIdAndStatusOrderByCreatedAtDesc(id, EntityStatus.ACTIVE);
                for (SubDrawingHistory his : hiss) {
                    subDrawingHisRepository.delete(his);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 获取子图纸变量列表
     */
    @Override
    public List<SubDrawingConfig> getVariables(Long orgId, Long projectId, Long drawingId) {
        Optional<Drawing> drawingOp = drawingRepository.findById(drawingId);
        if (drawingOp.isPresent()) {
            Drawing drawing = drawingOp.get();
            BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, drawing.getEntitySubType());
            return subDrawingConfigRepository.findByOrgIdAndProjectIdAndDrawingCategoryId(orgId, projectId,
                best.getId());
        }
        return null;
    }

    /**
     * 获取子图纸历史记录
     */
    @Override
    public List<SubDrawingHistory> getHistory(Long orgId, Long projectId, Long id) {
        return subDrawingHisRepository.findBySubDrawingIdAndStatusOrderByCreatedAtDesc(id, EntityStatus.ACTIVE);
    }


    /**
     * 根据子图纸id获取子图纸
     */
    @Override
    public SubDrawing findDrawingSubPipingBySubId(Long subId) {
        Optional<SubDrawing> subOp = subDrawingRepository.findById(subId);
        if (subOp.isPresent()) {
            return subOp.get();
        }
        return null;
    }

    /**
     * 根据历史记录id获取历史详情
     */
    @Override
    public SubDrawingHistory getHistoryById(Long id) {
        Optional<SubDrawingHistory> hisOp = subDrawingHisRepository.findById(id);
        if (hisOp.isPresent()) {
            return hisOp.get();
        }
        return null;
    }

    /**
     * 修改其他历史记录use
     */
    @Override
    public boolean updateHistoryUsedFalseExcept(Long id, Long subDrawingId) {
        List<SubDrawingHistory> histories = subDrawingHisRepository.findByIdNotAndSubDrawingId(id, subDrawingId);
        if (!histories.isEmpty()) {
            for (SubDrawingHistory his : histories) {
                his.setUsed(false);
                subDrawingHisRepository.save(his);
            }
        }
        return true;
    }

    /**
     * 图纸出图上传pdf文件（单独上传pdf、批量上传pdf）。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param file         文件
     * @param drawingId    图集id
     * @param subdrawingId 子图纸id
     * @param context      后台内容
     * @param uploadDTO    上传数据
     * @param zip          是否是zip文件
     * @return
     */
    @Override
    public boolean uploadDrawingSubPipingPdf(Long orgId, Long projectId, String file, Long drawingId,
                                             Long subdrawingId, ContextDTO context, DrawingUploadDTO uploadDTO, boolean zip) {

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
        drawingUploadZipFileHistory.setDrawingId(drawingId);
        drawingUploadZipFileHistory.setFileName(fileName);
        drawingUploadZipFileHistory.setOperator(context.getOperator().getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);


        if (fileType.toLowerCase().equals(BpmCode.FILE_TYPE_PDF)) {

            File diskFileTemp = new File(temporaryDir, uploadDTO.getFileName());
            if (!diskFileTemp.exists()) {
                throw new NotFoundError();
            }


            Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + uploadDTO.getFileName());


            if (pageCounts != 1) {
                drawingUploadZipFileHistory.setFailedCount(1);
                result = false;
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
                } else if (subdrawingId == null) {

                    List<SubDrawing> subDrawingList = subDrawingRepository
                        .findByDrawingIdAndSubDrawingNoAndPageNoAndStatus(drawingId, drawingNo, Integer.valueOf(pageNo), EntityStatus.PENDING);

                    if (subDrawingList.isEmpty()) {
                        drawingUploadZipFileHistory.setFailedCount(1);
                        result = false;
                    }

                    for (SubDrawing sub : subDrawingList) {
                        if (sub.getSubDrawingVersion().equals(drawingVersion)) {
                            subDrawing = sub;
                        }
                    }

                    if (subDrawing == null && !subDrawingList.isEmpty()) {
                        subDrawing = subDrawingList.get(0);
                    }

                } else {

                    subDrawing = this.get(orgId, projectId, subdrawingId);
                }


                if (subDrawing != null && drawingNo.equals(subDrawing.getSubDrawingNo())
                    && pageNo.equals("" + subDrawing.getPageNo())) {


                    logger.error("子图纸4 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        uploadDTO.getFileName(), new FilePostDTO());
                    logger.error("子图纸4 保存docs服务->结束");
                    FileES f = responseBody.getData();


                    String qrCode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();


                    List<SubDrawingHistory> hisl = subDrawingHisRepository
                        .findBySubDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersion(subDrawing.getId(), drawingNo,

                            Integer.parseInt(pageNo), drawingVersion);
                    if (!hisl.isEmpty()) {

                        for (SubDrawingHistory his : hisl) {
                            if (his.isIssueFlag()) {
                                drawingUploadZipFileHistory.setFailedCount(1);
                                result = false;
                                break;
                            }
                        }
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
                            his.setOperator(context.getOperator().getId());
                            his.setQrCode(qrCode);
                            his.setFileId(LongUtils.parseLong(f.getId()));
                            his.setFileName(f.getName());
                            his.setMemo(uploadDTO.getComment());
                            his.setFilePath(f.getPath());
                            subDrawingHisRepository.save(his);

                            subDrawing.setFileName(f.getName());
                            subDrawing.setFilePath(f.getPath());
                            subDrawing.setFileId(LongUtils.parseLong(f.getId()));
                            subDrawing.setQrCode(qrCode);
                            subDrawingRepository.save(subDrawing);

                            drawingUploadZipFileHistory.setSuccessCount(1);
                            drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(f.getId()));
                            drawingUploadZipFileHistory.setFileName(f.getName());
                            drawingUploadZipFileHistory.setFilePath(f.getPath());
                        }
                    } else {

                        drawingUploadZipFileHistory.setFailedCount(1);
                        result = false;
                    }
                } else {
                    drawingUploadZipFileHistory.setFailedCount(1);
                    result = false;
                }
            }
        } else {

            drawingUploadZipFileHistory.setFailedCount(1);
            result = false;
        }

        if (!zip) {
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
        }
        return result;
    }

    /**
     * 上传图纸局部升版pdf文件（批量上传、单独上传）。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param file         文件
     * @param drawingId    图纸id
     * @param subdrawingId
     * @param context
     * @param uploadDTO
     * @param zip
     * @return
     */
    @Override
    public boolean uploadUpdateDrawingSubPipingPdf(
        Long orgId,
        Long projectId,
        String file,
        Long drawingId,
        Long subdrawingId,
        ContextDTO context,
        DrawingUploadDTO uploadDTO,
        boolean zip) {

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
        drawingUploadZipFileHistory.setDrawingId(drawingId);
        drawingUploadZipFileHistory.setFileName(fileName);
        drawingUploadZipFileHistory.setOperator(context.getOperator().getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);


        if (fileType.toLowerCase().equals(BpmCode.FILE_TYPE_PDF)) {

            File diskFileTemp = new File(temporaryDir, uploadDTO.getFileName());
            if (!diskFileTemp.exists()) {
                throw new NotFoundError();
            }


            Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + uploadDTO.getFileName());


            if (pageCounts != 1) {
                drawingUploadZipFileHistory.setFailedCount(1);
                result = false;
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
                } else if (subdrawingId == null) {

                    List<SubDrawing> subDrawingList = subDrawingRepository
                        .findByDrawingIdAndSubDrawingNoAndPageNoAndStatus(drawingId, drawingNo, Integer.valueOf(pageNo), EntityStatus.ACTIVE);

                    if (subDrawingList.isEmpty()) {
                        drawingUploadZipFileHistory.setFailedCount(1);
                        result = false;
                    }

                    for (SubDrawing sub : subDrawingList) {
                        if (sub.getSubDrawingVersion().equals(drawingVersion)) {
                            subDrawing = sub;
                        }
                    }

                    if (subDrawing == null && !subDrawingList.isEmpty()) {
                        subDrawing = subDrawingList.get(0);
                    }

                } else {

                    subDrawing = this.get(orgId, projectId, subdrawingId);
                }


                if (subDrawing != null && drawingNo.equals(subDrawing.getSubDrawingNo())
                    && pageNo.equals("" + subDrawing.getPageNo())) {


                    logger.error("子图纸5 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        uploadDTO.getFileName(), new FilePostDTO());
                    logger.error("子图纸5 保存docs服务->结束");
                    FileES f = responseBody.getData();


                    String qrCode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();


                    List<SubDrawingHistory> hisl = subDrawingHisRepository
                        .findBySubDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersion(subDrawing.getId(), drawingNo,
                            Integer.valueOf(pageNo), drawingVersion);


                    if (!hisl.isEmpty()) {

                        for (SubDrawingHistory his : hisl) {
                            if (his.isIssueFlag()) {
                                drawingUploadZipFileHistory.setFailedCount(1);
                                result = false;
                                break;
                            }
                        }
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
                            his.setOperator(context.getOperator().getId());
                            his.setQrCode(qrCode);
                            his.setFileId(LongUtils.parseLong(f.getId()));
                            his.setFileName(f.getName());
                            his.setMemo(uploadDTO.getComment());
                            his.setFilePath(f.getPath());
                            subDrawingHisRepository.save(his);

                            subDrawing.setFileName(f.getName());
                            subDrawing.setFilePath(f.getPath());
                            subDrawing.setFileId(LongUtils.parseLong(f.getId()));
                            subDrawing.setQrCode(qrCode);
                            subDrawingRepository.save(subDrawing);

                            drawingUploadZipFileHistory.setSuccessCount(1);
                            drawingUploadZipFileHistory.setFileId(LongUtils.parseLong(f.getId()));
                            drawingUploadZipFileHistory.setFileName(f.getName());
                            drawingUploadZipFileHistory.setFilePath(f.getPath());
                        }
                    } else {

                        drawingUploadZipFileHistory.setFailedCount(1);
                        result = false;
                    }
                } else {
                    drawingUploadZipFileHistory.setFailedCount(1);
                    result = false;
                }
            }
        } else {

            drawingUploadZipFileHistory.setFailedCount(1);
            result = false;
        }

        if (!zip) {
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
        }
        return result;
    }

    /**
     * 上传子图纸（zip包）。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param file         文件
     * @param drawingId    图纸id
     * @param subdrawingId 子图纸id
     * @param context
     * @param uploadDTO
     * @return
     */
    @Override
    public UploadDrawingFileResultDTO uploadDrawingSubPipingZip(Long orgId, Long projectId, String file,
                                                                Long drawingId, Long subdrawingId, ContextDTO context, DrawingUploadDTO uploadDTO) {

        int errorCount = 0;
        int successCount = 0;
        DrawingUploadZipFileHistory drawingUploadZipFileHistory = new DrawingUploadZipFileHistory();
        List<String> noMatchFile = new ArrayList<>();

        String temporaryFileName = uploadDTO.getFileName();


        File diskFile = new File(temporaryDir, temporaryFileName);

        FileMetadataDTO metadata = FileUtils.readMetadata(diskFile, FileMetadataDTO.class);

        String uploadFileName = metadata.getFilename();










        String fileFolder = temporaryDir + File.separator + CryptoUtils.uniqueId().toUpperCase();
        File folder = new File(fileFolder);
        folder.mkdirs();





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


            logger.error("子图纸1 上传docs服务->开始");
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
            logger.error("子图纸1 上传docs服务->结束");
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
            if (fileTypePDF.toLowerCase().equals("." + BpmCode.FILE_TYPE_PDF)) {
                uploadDTO.setFileName(temporaryFileDTO.getName());
                boolean result = this.uploadDrawingSubPipingPdf(orgId, projectId, uploadFileNamePDF, drawingId,
                    null, context, uploadDTO, true);
                if (result) {
                    successCount++;
                    drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.SUCCESS);
                } else {
                    errorCount++;
                    drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                    noMatchFile.add(uploadFileNamePDF);
                }
            } else {
                errorCount++;
                drawingUploadZipFileHistoryDetail.setSuccessFlg(DrawingUploadZipFileSuccessFlg.FAILED);
                noMatchFile.add(uploadFileNamePDF);
            }
            drawingUploadZipFileHistoryDetail.setStatus(EntityStatus.ACTIVE);
            drawingUploadZipFileHistoryDetail.setCreatedAt();
            drawingUploadZipFileHistoryDetailRepository.save(drawingUploadZipFileHistoryDetail);
        }

        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();
        dto.setErrorCount(errorCount);
        dto.setErrorList(noMatchFile);
        dto.setSuccessCount(successCount);
        drawingUploadZipFileHistory.setFailedCount(errorCount);
        drawingUploadZipFileHistory.setSuccessCount(successCount);
        drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
        return dto;
    }

    /**
     * 上传子图纸目录
     */
    @Override
    public UploadDrawingFileResultDTO uploadDrawingSubPipingCatalog(Long orgId, Long projectId, Long drawingId,
                                                                    Long userid, DrawingUploadDTO uploadDTO) {

        Workbook workbook = null;
        File excel;


        try {
            excel = new File(temporaryDir, uploadDTO.getFileName());
            workbook = WorkbookFactory.create(excel);
        } catch (RecordFormatException e) {
            throw new ValidationError("WRONG XLS FORMAT");
        } catch (IOException e) {

            throw new NotFoundError();
        }

        boolean foundSheet = false;
        int errorCount = 0;
        int successCount = 0;
        List<String> errLine = new ArrayList<>();
        int sheetNum = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetNum; i++) {
            if (!workbook.getSheetAt(i).getSheetName().equals(BpmCode.SUB_DRAWING_LIST)) {
                continue;
            } else {
                foundSheet = true;
                Row row;
                Iterator<Row> rows = workbook.getSheetAt(i).rowIterator();
                while (rows.hasNext()) {
                    row = rows.next();
                    if (row.getRowNum() < BpmCode.SUB_DRAWING_LIST_HEADER) {

                        if (row.getRowNum() == 2) {
                            Optional<Drawing> opDrawing = drawingRepository.findById(drawingId);
                            if (opDrawing.isPresent()) {
                                String dwgNo = WorkbookUtils.readAsString(row, 1);
                                String rev = WorkbookUtils.readAsString(row, 5);
                                Drawing drawing = opDrawing.get();
                                if (!dwgNo.trim().equals(drawing.getDwgNo())) {
                                    throw new ValidationError("drawing no not match.");
                                }

                                if (uploadDTO.getDrawingDetailId() == null) {
                                    throw new ValidationError("drawing detail id not be null.");
                                }
                                DrawingDetail detail = drawingDetailRepository.findById(uploadDTO.getDrawingDetailId()).orElse(null);
                                if (detail.getRev() == null || !detail.getRev().equals(rev.trim())) {
                                    throw new ValidationError("drawing rev not match.");
                                }
                            } else {
                                throw new ValidationError("not found the drawing.");
                            }
                        }
                        continue;
                    }
                    try {
                        int colIndex = 0;


                        int seq = WorkbookUtils.readAsInteger(row, colIndex++);


                        String subDrawingNo = WorkbookUtils.readAsString(row, colIndex++);


                        if (StringUtils.isEmpty(subDrawingNo, true))
                            continue;


                        String page = WorkbookUtils.readAsString(row, colIndex++);
                        if (StringUtils.isEmpty(page, true)) {
                            errorCount++;
                            errLine.add("" + (row.getRowNum() + 1) + " page info is empty");
                        }
                        if (!page.contains(BpmCode.SUB_DRAWING_LIST_PAGE_SPLIT)) {
                            errorCount++;
                            errLine.add("" + (row.getRowNum() + 1) + " page format is wrong");
                            continue;
                        }
                        String[] pageInfo = page.split(BpmCode.SUB_DRAWING_LIST_PAGE_SPLIT);
                        String pageNo = pageInfo[0];
                        String pageCount = pageInfo[1];


                        String subDrawingVersion = WorkbookUtils.readAsString(row, colIndex++);
                        if (StringUtils.isEmpty(subDrawingVersion, true)) {
                            errorCount++;
                            errLine.add("" + (row.getRowNum() + 1) + " drawing version is empty");
                        }


                        String shortCode = WorkbookUtils.readAsString(row, colIndex++);


                        String comment = WorkbookUtils.readAsString(row, colIndex++);
                        if (comment == null)
                            comment = "";
                        boolean markDeleted = false;
                        if (comment.contains(BpmCode.DELETE)) {
                            markDeleted = true;
                        }


                        List<SubDrawing> subDrawingO = subDrawingRepository
                            .findByProjectIdAndSubDrawingNoAndDrawingIdNot(projectId, subDrawingNo, drawingId);
                        if (!subDrawingO.isEmpty()) {
                            errorCount++;
                            errLine.add("" + (row.getRowNum() + 1) + "The sub-dwg is in Other main-dwg");
                            continue;
                        }


                        List<SubDrawing> subDrawingCheckList = subDrawingRepository
                            .findByProjectIdAndDrawingIdAndSubDrawingNoAndPageNoAndLatestRev(projectId, drawingId, subDrawingNo, Integer.valueOf(pageNo), subDrawingVersion);
                        if (subDrawingCheckList.size() > 0) {
                            errorCount++;
                            errLine.add("" + (row.getRowNum() + 1) + "The sub-dwg is in sub-dwg-list");
                            continue;
                        }


                        SubDrawing subDrawingE = subDrawingRepository
                            .findByDrawingIdAndSubDrawingNoAndPageNoAndDrawingDetailIdAndStatus(drawingId,
                                subDrawingNo, Integer.valueOf(pageNo), uploadDTO.getDrawingDetailId(),
                                EntityStatus.PENDING);
                        SubDrawing subDrawing = null;
                        SubDrawingDTO dto = new SubDrawingDTO();
                        dto.setDrawingDetailId(uploadDTO.getDrawingDetailId());
                        dto.setSubDrawingNo(subDrawingNo);
                        dto.setShortCode(shortCode);
                        dto.setComment(comment);
                        dto.setSeq(seq);
                        dto.setSubDrawingVersion(subDrawingVersion);
                        dto.setPageNo(Integer.valueOf(pageNo));
                        dto.setActInstId(uploadDTO.getProcInstId());
                        dto.setPageCount(Integer.valueOf(pageCount));
                        if (subDrawingE != null) {
                            errorCount++;
                            errLine.add("" + (row.getRowNum() + 1) + " The sub-dwg is exists");
                            continue;
//                            subDrawing = this.modify(orgId, projectId, drawingId, subDrawingE.getId(), userid, dto);
                        } else {
                            SubDrawing subDrawingC = subDrawingRepository
                                .findByDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersionAndDrawingDetailIdNotAndStatus(
                                    drawingId, subDrawingNo, Integer.valueOf(pageNo), subDrawingVersion,
                                    uploadDTO.getDrawingDetailId(), EntityStatus.ACTIVE);
                            if (subDrawingC != null) {
                                errorCount++;
                                errLine.add("" + (row.getRowNum() + 1) + " The sub-dwg is in other rev main-dwg");
                                continue;
                            }

                            subDrawing = this.create(orgId, projectId, drawingId, userid, dto);
                        }


                        if (comment != null) {
                            if (comment.contains(BpmCode.DELETE)) {
                                subDrawing.setStatus(EntityStatus.DELETED);
                                subDrawingRepository.save(subDrawing);
                            }
                        }
                        successCount++;
                    } catch (Exception e) {
                        e.printStackTrace(System.out);
                        errorCount++;
                        errLine.add("" + (row.getRowNum() + 1));
                    }
                }
            }
        }

        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }


        if (!foundSheet) {
            throw new ValidationError("THERE IS NO '" + BpmCode.SUB_DRAWING_LIST + "' SHEET");
        }

        UploadDrawingFileResultDTO dto = new UploadDrawingFileResultDTO();
        dto.setErrorCount(errorCount);
        dto.setErrorList(errLine);
        dto.setSuccessCount(successCount);
        return dto;
    }

    /**
     * 当前制图的当前版本是否存在已运行的任务。
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId
     * @return
     */
    @Override
    public boolean checkActivity(Long orgId, Long projectId, Long drawingId) {
        Optional<Drawing> drawingOp = drawingRepository.findById(drawingId);
        if (drawingOp.isPresent()) {
            String entityNo = drawingOp.get().getDwgNo();
            String version = drawingOp.get().getLatestRev();
            BpmActivityInstanceBase actInst = bpmActInstRepository.findByProjectIdAndEntityIdAndVersion(
                projectId, drawingId, version);
            if (actInst != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 导出图纸写入excel方法。
     *
     * @param orgId
     * @param projectId
     * @return
     */
    @Override
    public Long writeExcel(Long orgId, Long projectId) {

        Iterator<Drawing> iterator = drawingRepository.findAll().iterator();

        Workbook workbook;
        File excel;

        try {
            excel = new File(temporaryDir, "aaaaaaa.xlsx");
            excel.createNewFile();
            workbook = new XSSFWorkbook();
        } catch (IOException e) {
            throw new NotFoundError();
        }
        Sheet sheet = workbook.createSheet();

        Row row = sheet.createRow(0);
        row.createCell(0).setCellValue("DWG_No");
        row.createCell(1).setCellValue("DWG_Title");

        int i = 1;
        while (iterator.hasNext()) {
            row = sheet.createRow(i++);
            Drawing drawing = iterator.next();
            row.createCell(0).setCellValue(drawing.getDwgNo());
            row.createCell(1).setCellValue(drawing.getDocumentTitle());
        }

        FileOutputStream stream;
        try {
            stream = new FileOutputStream(excel);
            workbook.write(stream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        System.out.println(excel.getName());
        DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
            MediaType.APPLICATION_PDF_VALUE, true, excel.getName());
        MockMultipartFile fileItem1 = null;
        try {
            IOUtils.copy(new FileInputStream(excel), fileItem.getOutputStream());
            fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }


        logger.error("子图纸2 上传docs服务->开始");
        JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI.uploadProjectDocumentFile(orgId.toString(),
            fileItem1);
        logger.error("子图纸2 上传docs服务->结束");
        logger.error("子图纸2 保存docs服务->开始");
        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
            tempFileResBody.getData().getName(), new FilePostDTO());
        logger.error("子图纸2 保存docs服务->结束");
        return LongUtils.parseLong(fileESResBody.getData().getId());
    }

    /**
     * 通过子图纸编号查找子图纸信息。
     *
     * @param orgId
     * @param projectId
     * @param subDrawingNo
     * @return
     */
    @Override
    public List<SubDrawing> findBySubDrawingNo(Long orgId, Long projectId, String subDrawingNo) {
        return subDrawingRepository.findByProjectIdAndSubDrawingNoAndStatus(projectId, subDrawingNo,
            EntityStatus.ACTIVE);
    }

    /**
     * 通过图纸id和图纸详情id判断当前子图纸是否存在。
     *
     * @param orgId
     * @param projectId
     * @param subDrawingNo
     * @param pageNo
     * @param drawingDetailId
     * @param drawingId
     */
    @Override
    public void checkSubDrawingNo(Long orgId, Long projectId, String subDrawingNo, Integer pageNo,
                                  Long drawingDetailId, Long drawingId) {
        List<SubDrawing> subDrawingO = subDrawingRepository
            .findByProjectIdAndSubDrawingNoAndDrawingIdNot(projectId, subDrawingNo, drawingId);
        if (!subDrawingO.isEmpty()) {
            throw new ValidationError("The sub-dwg is in Other main-dwg");
        }

        SubDrawing subDrawing = subDrawingRepository
            .findByDrawingIdAndSubDrawingNoAndPageNoAndDrawingDetailIdAndStatus(drawingId, subDrawingNo, pageNo,
                drawingDetailId, EntityStatus.ACTIVE);
        if (subDrawing != null) {
            throw new ValidationError("The sub-dwg is already exists");
        }
    }

    /**
     * 下载子图纸
     *
     * @param id
     * @return
     */
    @Override
    public DrawingFileDTO downloadSubDrawing(Long id) {
        SubDrawing sub = subDrawingRepository.findById(id).orElse(null);
        DrawingFileDTO dto = new DrawingFileDTO();
        if (sub != null) {
            if (sub.getIssued()) {
                ActReportDTO newdto = null;
                try {
                    if (!LongUtils.isEmpty(sub.getActInstId())) {
                        newdto = this.setQrCodeIntoPdfFile(
                            sub.getOrgId(), sub.getProjectId(), sub.getQrCode(), sub.getFilePath(), sub.getFileName(), 50, 50, 75);
                    } else {
                        Drawing dl = drawingRepository.findById(sub.getDrawingId()).orElse(null);
                        BpmEntitySubType best = entitySubTypeService.getEntitySubType(dl.getProjectId(), dl.getEntitySubType());
                        if (dl != null) {
                            int positionX = best.getDrawingPositionX();
                            int positionY = best.getDrawingPositionY();
                            int scaleToFit = best.getDrawingScaleToFit();

                            newdto = this.setQrCodeIntoPdfFile(
                                sub.getOrgId(), sub.getProjectId(), sub.getQrCode(), sub.getFilePath(), sub.getFileName(), positionX, positionY, scaleToFit);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace(System.out);
                }
                if (newdto != null) {
                    dto.setFileId(newdto.getFileId());
                    dto.setFilePath(newdto.getFilePath());
                    dto.setQrCode(sub.getQrCode());
                    return dto;
                }
            }
            dto.setFileId(sub.getFileId());
            dto.setFileName(sub.getFileName());
            dto.setFilePath(sub.getFilePath());
            dto.setQrCode(sub.getQrCode());
        }
        return dto;
    }

    /**
     * 设置二维码。
     *
     * @param orgId
     * @param projectId
     * @param qrCode
     * @param filePath
     * @param fileName
     * @param positionX
     * @param positionY
     * @param scaleToFit
     * @return
     * @throws Exception
     */
    @Override
    public ActReportDTO setQrCodeIntoPdfFile(
        Long orgId,
        Long projectId,
        String qrCode,
        String filePath,
        String fileName,
        int positionX,
        int positionY,
        int scaleToFit
    ) throws Exception {

        String initFile = protectedDir + filePath.substring(1);

        String resultFile = temporaryDir + fileName;



        String qrFileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
        QRCodeUtils.generateQRCodeNoBlank(qrCode, scaleToFit, "png", qrFileName);

        PdfUtils.setImageToPdf(resultFile, initFile, qrFileName, positionX, positionY, scaleToFit, scaleToFit);

        File diskFile = new File(resultFile);

        DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
            MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

        IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());


        logger.error("子图纸3 上传docs服务->开始");
        MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
            APPLICATION_PDF_VALUE, fileItem.getInputStream());
        JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
            .uploadProjectDocumentFile(orgId.toString(), fileItem1);
        logger.error("子图纸3 上传docs服务->结束");
        logger.error("子图纸3 保存docs服务->开始");
        JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
            tempFileResBody.getData().getName(), new FilePostDTO());
        FileES fileES = fileESResBody.getData();
        logger.error("子图纸3 保存docs服务->结束");
        ActReportDTO result = new ActReportDTO();
        result.setFileId(LongUtils.parseLong(fileES.getId()));
        result.setFilePath(fileES.getPath());

        return result;
    }

    /**
     * 通过二维吗查找图纸信息。
     *
     * @param orgId
     * @param projectId
     * @param qrcode
     * @return
     */
    @Override
    public QRCodeSearchResultDrawingDTO getDrawingByQrcode(Long orgId, Long projectId, String qrcode) {

        QRCodeSearchResultDrawingDTO dto = new QRCodeSearchResultDrawingDTO();
        Drawing dl = null;
        DrawingDetail detail = null;
        DrawingFile drawingFile = null;

        // 查询detail信息，获取
        detail = drawingDetailRepository.findByQrCode(qrcode);
        if (detail != null) {
            dto.setQrCodeType("DRAWING");
            drawingFile = drawingFileRepository.findByProjectIdAndDrawingDetailIdAndDrawingFileTypeAndStatus(
                projectId,
                detail.getId(),
                DrawingFileType.ISSUE_FILE,
                EntityStatus.ACTIVE
            );
            if (drawingFile != null) {
                dto.setFileId(drawingFile.getFileId());
                dto.setFileName(drawingFile.getFileName());
                dto.setFilePath(drawingFile.getFilePath());
            }
            Optional<Drawing> dlOption = drawingRepository.findById(detail.getDrawingId());
            if (dlOption.isPresent()) {
                dl = dlOption.get();
                dto.setDrawingNo(dl.getDwgNo());
                dto.setDrawingTitle(dl.getDocumentTitle());
                dto.setLatestRev(dl.getLatestApprovedRev());
            }

            dto.setOrgId(orgId);
            dto.setProjectId(projectId);
            dto.setRev(detail.getRevNo());
            dto.setCreatedAt(detail.getCreatedAt());

        } else {
            dto.setQrCodeType("NOT_FOUND");
        }


        if (
            dl != null
                && detail != null
        ) {
            Long drawUserId = null;
            Long checkUserId = null;
            Long approvedUserId = null;

            List<BpmActivityInstanceBase> actInst = bpmActInstRepository
                .findByProjectIdAndEntityIdAndVersionAndSuspensionStateOrderByCreatedAtDesc(
                    projectId,
                    dl.getId(),
                    detail.getRevNo(),
                    SuspensionState.ACTIVE
                );
            if (actInst.isEmpty()) {
                DrawingEntryDelegate approvedD = drawingEntryDelegateRepository
                    .findByDrawingIdAndPrivilegeAndStatus(
                        dl.getId(),
                        UserPrivilege.DRAWING_APPROVE_EXECUTE,
                        EntityStatus.ACTIVE
                    );
                if (approvedD != null) {
                    approvedUserId = approvedD.getUserId();
                }
                DrawingEntryDelegate checkD = drawingEntryDelegateRepository
                    .findByDrawingIdAndPrivilegeAndStatus(
                        dl.getId(),
                        UserPrivilege.DRAWING_CHECK_EXECUTE,
                        EntityStatus.ACTIVE
                    );
                if (checkD != null) {
                    checkUserId = checkD.getUserId();
                }
                DrawingEntryDelegate drawD = drawingEntryDelegateRepository
                    .findByDrawingIdAndPrivilegeAndStatus(
                        dl.getId(),
                        UserPrivilege.DESIGN_ENGINEER_EXECUTE,
                        EntityStatus.ACTIVE
                    );
                if (drawD != null) {
                    drawUserId = drawD.getUserId();
                }
            } else {
                Long actInstId = actInst.get(0).getId();
                BpmActTaskAssignee draw = bpmActTaskAssigneeRepository.findByActInstIdAndTaskDefKeyAndStatus(
                    actInstId,
                    BpmTaskDefKey.USERTASK_DRAWING_DESIGN.getType(),
                    EntityStatus.ACTIVE
                );
                if (draw != null) {
                    drawUserId = draw.getAssignee();
                }

                BpmActTaskAssignee check = bpmActTaskAssigneeRepository.findByActInstIdAndTaskDefKeyAndStatus(
                    actInstId,
                    BpmTaskDefKey.USERTASK_DRAWING_CHECK.getType(),
                    EntityStatus.ACTIVE
                );
                if (check != null) {
                    checkUserId = check.getAssignee();
                }

                BpmActTaskAssignee approved = bpmActTaskAssigneeRepository.findByActInstIdAndTaskDefKeyAndStatus(
                    actInstId,
                    BpmTaskDefKey.USERTASK_DRAWING_APPROVE.getType(),
                    EntityStatus.ACTIVE
                );
                if (approved != null) {
                    approvedUserId = approved.getAssignee();
                }
            }

            if (drawUserId != null) {
                dto.setOperator(
                    userFeignAPI.get(drawUserId)
                        .getData()
                        .getName()
                );
            }
            if (checkUserId != null) {
                dto.setCheckUser(
                    userFeignAPI.get(checkUserId)
                        .getData()
                        .getName()
                );
            }
            if (approvedUserId != null) {
                dto.setApproveUser(
                    userFeignAPI.get(approvedUserId)
                        .getData()
                        .getName()
                );
            }

        }
        return dto;
    }

    /**
     * 导出子图纸。
     *
     * @param orgId
     * @param projectId
     * @param drawingId
     * @param subDrawingDownLoadDTO
     * @param operatorDTO
     * @param project
     * @return
     */
    @Override
    public File exportSubDrawing(
        Long orgId,
        Long projectId,
        Long drawingId,
        SubDrawingDownLoadDTO subDrawingDownLoadDTO,
        OperatorDTO operatorDTO,
        Project project) {

        Drawing drawing = drawingRepository.findById(drawingId).orElse(null);

        if (drawing != null) {

            List<SubDrawing> subList = subDrawingRepository.findByDrawingIdAndDrawingVersionAndStatus(
                orgId,
                projectId,
                drawingId,
                subDrawingDownLoadDTO.getDrawingVersion(),
                subDrawingDownLoadDTO.getStatus()
            );





            String temporaryFileName = FileUtils.copy(
                this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("samples/export-drawing-catalog.xlsx"),
                temporaryDir,
                operatorDTO.getId().toString()
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

            Sheet sheet = workbook.getSheetAt(0);

            int rowNum = 4;

            Row firstRow = WorkbookUtils.getRow(sheet, 0);
            WorkbookUtils.getCell(firstRow, 0).setCellValue("ISO图纸清单");

            Row secondRow = WorkbookUtils.getRow(sheet, 1);
            WorkbookUtils.getCell(secondRow, 1).setCellValue(project.getName());

            Row thirdRow = WorkbookUtils.getRow(sheet, 2);
            WorkbookUtils.getCell(thirdRow, 1).setCellValue(drawing.getDwgNo());
            WorkbookUtils.getCell(thirdRow, 4).setCellValue(drawing.getLatestRev());

            Short height = null;
            CellStyle style0 = null;
            CellStyle style1 = null;
            CellStyle style2 = null;
            CellStyle style3 = null;
            CellStyle style4 = null;
            for (SubDrawing sub : subList) {
                Row row = WorkbookUtils.getRow(sheet, rowNum++);

                if (height == null) {
                    height = row.getHeight();
                    style0 = row.getCell(0).getCellStyle();
                    style1 = row.getCell(1).getCellStyle();
                    style2 = row.getCell(2).getCellStyle();
                    style3 = row.getCell(3).getCellStyle();
                    style4 = row.getCell(4).getCellStyle();
                } else {
                    row.setHeight(height);
                }

                WorkbookUtils.getCell(row, 0).setCellValue(subList.indexOf(sub) + 1);
                WorkbookUtils.getCell(row, 1).setCellValue(sub.getSubDrawingNo());
                WorkbookUtils.getCell(row, 2).setCellValue("" + sub.getPageNo() + " OF " + sub.getPageCount());
                WorkbookUtils.getCell(row, 3).setCellValue(sub.getSubDrawingVersion());
                WorkbookUtils.getCell(row, 4).setCellValue(sub.getComment());

                row.getCell(0).setCellStyle(style0);
                row.getCell(1).setCellStyle(style1);
                row.getCell(2).setCellStyle(style2);
                row.getCell(3).setCellStyle(style3);
                row.getCell(4).setCellStyle(style4);
            }


            try {
                WorkbookUtils.save(workbook, excel.getAbsolutePath());
                return excel;
            } catch (IOException e) {
                e.printStackTrace(System.out);
                throw new BusinessError();
            }
        }
        return null;
    }

    /**
     * 上传替换最新有效子图纸。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param file         文件
     * @param drawingId    图纸id
     * @param subdrawingId
     * @param context
     * @param uploadDTO
     * @param zip
     * @return
     */
    @Override
    public boolean uploadActivePDF(
        Long orgId,
        Long projectId,
        String file,
        Long drawingId,
        Long subdrawingId,
        ContextDTO context,
        DrawingUploadDTO uploadDTO,
        boolean zip) {

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
        drawingUploadZipFileHistory.setDrawingId(drawingId);
        drawingUploadZipFileHistory.setFileName(fileName);
        drawingUploadZipFileHistory.setOperator(context.getOperator().getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);


        if (fileType.toLowerCase().equals(BpmCode.FILE_TYPE_PDF)) {

            File diskFileTemp = new File(temporaryDir, uploadDTO.getFileName());
            if (!diskFileTemp.exists()) {
                throw new NotFoundError();
            }


            Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + uploadDTO.getFileName());


            if (pageCounts != 1) {
                drawingUploadZipFileHistory.setFailedCount(1);
                result = false;
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

                SubDrawing subDrawing = this.get(orgId, projectId, subdrawingId);


                if (subDrawing != null && drawingNo.equals(subDrawing.getSubDrawingNo())
                    && pageNo.equals("" + subDrawing.getPageNo())) {

                    logger.error("子图纸6 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        uploadDTO.getFileName(), new FilePostDTO());
                    logger.error("子图纸6 保存docs服务->结束");
                    FileES f = responseBody.getData();

                    if (f != null) {
                        subDrawing.setFileName(subDrawing.getFileName());
                        subDrawing.setFilePath(f.getPath());
                        subDrawing.setFileId(LongUtils.parseLong(f.getId()));
                        subDrawingRepository.save(subDrawing);
                        drawingUploadZipFileHistory.setSuccessCount(1);
                    } else {
                        drawingUploadZipFileHistory.setFailedCount(1);
                        result = false;
                    }

                } else {
                    drawingUploadZipFileHistory.setFailedCount(1);
                    result = false;
                }
            }
        } else {

            drawingUploadZipFileHistory.setFailedCount(1);
            result = false;
        }

        if (!zip) {
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
        }
        return result;
    }

    /**
     * 上传文件至服务器，并返回文件属性。
     */
    private JsonObjectResponseBody<FileES> uploadFile(
        Long orgId,
        Long projectId,
        String savepath
    ) {
        try {
            File diskFile = new File(savepath);
            logger.info("创建文件：" + diskFile.getName());
            DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

            IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());
            logger.info("复制文件：" + diskFile.getName());

            logger.error("子图纸4 上传docs服务->开始");
            MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                APPLICATION_PDF_VALUE, fileItem.getInputStream());
            JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                .uploadProjectDocumentFile(orgId.toString(), fileItem1);
            logger.error("子图纸4 上传docs服务->结束");
            logger.error("子图纸4 保存docs服务->开始");
            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                tempFileResBody.getData().getName(), new FilePostDTO());
            logger.error("子图纸4 保存docs服务->结束");
            return fileESResBody;
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonObjectResponseBody<>();
        }

    }


    /**
     * 上传替换最新有效子图纸。
     *
     * @param orgId        组织id
     * @param projectId    项目id
     * @param file         文件
     * @param drawingId    图纸id
     * @param subdrawingId
     * @param context
     * @param uploadDTO
     * @param zip
     * @return
     */
    @Override
    public boolean uploadSecondActivePDF(
        Long orgId,
        Long projectId,
        String file,
        Long drawingId,
        Long subdrawingId,
        ContextDTO context,
        DrawingUploadDTO uploadDTO,
        boolean zip) {

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
        drawingUploadZipFileHistory.setDrawingId(drawingId);
        drawingUploadZipFileHistory.setFileName(fileName);
        drawingUploadZipFileHistory.setOperator(context.getOperator().getId());
        drawingUploadZipFileHistory.setOrgId(orgId);
        drawingUploadZipFileHistory.setProjectId(projectId);
        drawingUploadZipFileHistory.setStatus(EntityStatus.ACTIVE);
        drawingUploadZipFileHistory.setFileCount(1);
        drawingUploadZipFileHistory.setZipFile(false);


        if (fileType.toLowerCase().equals(BpmCode.FILE_TYPE_PDF)) {

            File diskFileTemp = new File(temporaryDir, uploadDTO.getFileName());
            if (!diskFileTemp.exists()) {
                throw new NotFoundError();
            }


            Integer pageCounts = PdfUtils.getPdfPageCount(temporaryDir + uploadDTO.getFileName());


            if (pageCounts != 1) {
                drawingUploadZipFileHistory.setFailedCount(1);
                result = false;
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

                SubDrawing subDrawing = this.get(orgId, projectId, subdrawingId);


                if (subDrawing != null && drawingNo.equals(subDrawing.getSubDrawingNo())
                    && pageNo.equals("" + subDrawing.getPageNo())) {

                    logger.info("子图纸7 保存docs服务->开始");
                    JsonObjectResponseBody<FileES> responseBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                        uploadDTO.getFileName(), new FilePostDTO());
                    logger.info("子图纸7 保存docs服务->结束");
                    FileES f = responseBody.getData();

                    if (f != null) {
                        subDrawing.setFileName(subDrawing.getFileName());
                        subDrawing.setFilePath(f.getPath());
                        subDrawing.setFileId(LongUtils.parseLong(f.getId()));
                        subDrawingRepository.save(subDrawing);
                        drawingUploadZipFileHistory.setSuccessCount(1);
                    } else {
                        drawingUploadZipFileHistory.setFailedCount(1);
                        result = false;
                    }

                } else {
                    drawingUploadZipFileHistory.setFailedCount(1);
                    result = false;
                }
            }
        } else {

            drawingUploadZipFileHistory.setFailedCount(1);
            result = false;
        }

        if (!zip) {
            drawingUploadZipFileHistoryRepository.save(drawingUploadZipFileHistory);
        }
        return result;
    }

}
