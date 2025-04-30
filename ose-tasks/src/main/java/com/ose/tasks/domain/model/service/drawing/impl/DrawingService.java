package com.ose.tasks.domain.model.service.drawing.impl;

import com.alibaba.excel.EasyExcel;
import com.ose.util.*;
import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.auth.vo.UserPrivilege;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.dto.TemporaryFileDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.ContextDTO;
import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.ValidationError;
import com.ose.report.api.DrawConstructionReportFeignAPI;
import com.ose.report.dto.*;
import com.ose.report.entity.ReportHistory;
import com.ose.response.JsonObjectResponseBody;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.*;
import com.ose.tasks.domain.model.repository.plan.WBSEntityEntryDateRepository;
import com.ose.tasks.domain.model.service.ProjectInterface;
import com.ose.tasks.domain.model.service.bpm.ActivityTaskService;
import com.ose.tasks.domain.model.service.bpm.EntitySubTypeInterface;
import com.ose.tasks.domain.model.service.bpm.TodoTaskDispatchInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingBaseInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingInterface;
import com.ose.tasks.domain.model.service.drawing.SubDrawingInterface;
import com.ose.tasks.dto.WBSEntryCriteriaBaseDTO;
import com.ose.tasks.dto.bpm.ActInstCriteriaDTO;
import com.ose.tasks.dto.bpm.BpmActivityInstanceDTO;
import com.ose.tasks.dto.bpm.TaskPrivilegeDTO;
import com.ose.tasks.dto.bpm.TodoTaskExecuteDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.bpm.*;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.entity.wbs.entry.WBSEntityEntryDate;
import com.ose.tasks.util.easyExcel.EntityStatusConverter;
import com.ose.tasks.util.easyExcel.LocalDateConverter;
import com.ose.tasks.vo.SuspensionState;
import com.ose.tasks.vo.bpm.ActInstFinishState;
import com.ose.vo.EntityStatus;
import com.ose.vo.QrcodePrefixType;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static org.springframework.http.MediaType.APPLICATION_PDF_VALUE;

@Component

public class DrawingService implements DrawingInterface {

    private final static Logger logger = LoggerFactory.getLogger(DrawingService.class);
    private final BpmRuTaskRepository bpmRuTaskRepository;


    @Value("${application.files.temporary}")
    private String temporaryDir;


    @Value("${application.files.protected}")
    private String protectedDir;


    @Value("${application.files.public}")
    private String publicDir;

    @Value("${application.files.templateFilePath}")
    private String templateFilePath;

    @Value("${application.files.filePath}")
    private String filePath;

    private final DrawingRepository drawingRepository;

    private final DrawingRecordRepository drawingRecordRepository;

    private final DrawingHistoryRepository drawingHistoryRepository;

    private final SubDrawingRepository subDrawingRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final BpmEntitySubTypeRepository entityCategoryRepository;

    private final BpmActivityInstanceRepository bpmActInstRepository;

    private final DrawingFileRepository drawingFileRepository;

    private final EntitySubTypeInterface entitySubTypeService;

    private final BpmRuTaskRepository ruTaskRepository;

    private final SubDrawingInterface subDrawingService;

    private final TodoTaskDispatchInterface todoTaskDispatchService;

    private final UploadFeignAPI uploadFeignAPI;

    private final DrawConstructionReportFeignAPI drawingReportFeignAPI;

    private final DrawingBaseInterface drawingBaseService;

    private final DrawingEntryDelegateRepository drawingEntryDelegateRepository;

    private final ActivityTaskService activityTaskService;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final UserFeignAPI userFeignAPI;

    private final BpmActTaskRepository bpmActTaskRepository;

    private final ProjectInterface projectService;

    private final WBSEntityEntryDateRepository wbsEntityEntryDateRepository;

    private final BpmProcessRepository bpmProcessRepository;
    private final DrawingFileHistoryRepository drawingFileHistoryRepository;


    /**
     * 构造方法
     */
    @Autowired
    public DrawingService(
        DrawingRepository drawingRepository,
        DrawingHistoryRepository drawingHistoryRepository,
        DrawingRecordRepository drawingRecordRepository,
        SubDrawingRepository subDrawingRepository,
        DrawingDetailRepository drawingDetailRepository,
        BpmEntitySubTypeRepository entityCategoryRepository,
        BpmActivityInstanceRepository bpmActInstRepository,
        DrawingFileRepository drawingFileRepository, EntitySubTypeInterface entitySubTypeService, BpmRuTaskRepository ruTaskRepository,
        SubDrawingInterface subDrawingService,
        TodoTaskDispatchInterface todoTaskDispatchService,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") DrawConstructionReportFeignAPI drawingReportFeignAPI,
        DrawingBaseInterface drawingBaseService,
        DrawingEntryDelegateRepository drawingEntryDelegateRepository,
        ActivityTaskService activityTaskService,
        BpmHiTaskinstRepository hiTaskinstRepository,
        UserFeignAPI userFeignAPI,
        BpmActTaskRepository bpmActTaskRepository,
        ProjectInterface projectService,
        BpmProcessRepository bpmProcessRepository,
        WBSEntityEntryDateRepository wbsEntityEntryDateRepository,
        BpmRuTaskRepository bpmRuTaskRepository,
        DrawingFileHistoryRepository drawingFileHistoryRepository
    ) {
        this.drawingRepository = drawingRepository;
        this.drawingRecordRepository = drawingRecordRepository;
        this.drawingHistoryRepository = drawingHistoryRepository;
        this.subDrawingRepository = subDrawingRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.entityCategoryRepository = entityCategoryRepository;
        this.bpmActInstRepository = bpmActInstRepository;
        this.drawingFileRepository = drawingFileRepository;
        this.entitySubTypeService = entitySubTypeService;
        this.ruTaskRepository = ruTaskRepository;
        this.subDrawingService = subDrawingService;
        this.todoTaskDispatchService = todoTaskDispatchService;
        this.uploadFeignAPI = uploadFeignAPI;
        this.drawingReportFeignAPI = drawingReportFeignAPI;
        this.drawingBaseService = drawingBaseService;
        this.drawingEntryDelegateRepository = drawingEntryDelegateRepository;
        this.activityTaskService = activityTaskService;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.userFeignAPI = userFeignAPI;
        this.bpmActTaskRepository = bpmActTaskRepository;
        this.projectService = projectService;
        this.wbsEntityEntryDateRepository = wbsEntityEntryDateRepository;
        this.bpmProcessRepository = bpmProcessRepository;
        this.bpmRuTaskRepository = bpmRuTaskRepository;
        this.drawingFileHistoryRepository = drawingFileHistoryRepository;
    }

    /**
     * 创建生产设计图纸清单条目
     */
    @Override
    public Drawing create(Long orgId, Long projectId, Long userid, DrawingDTO drawingDTO) {

        BpmEntitySubType category = null;
        Optional<BpmEntitySubType> categoryOp = entityCategoryRepository.findById(drawingDTO.getDrawingCategoryId());
        if (categoryOp.isPresent()) {
            category = categoryOp.get();
        }

        Drawing drawing = BeanUtils.copyProperties(drawingDTO, new Drawing());
        drawing.setLocked(true);
//        drawing.setOperator(userid);
        drawing.setEntitySubType(category.getNameEn());
        drawing.setProjectId(projectId);
        drawing.setOrgId(orgId);
        drawing.setCreatedAt();
        drawing.setStatus(EntityStatus.ACTIVE);

        if (category != null && !category.isSubDrawingFlg()) {

            List<DrawingDetail> details = drawingDetailRepository
                .findByDrawingIdAndStatus(drawing.getId(), EntityStatus.ACTIVE);

            for (DrawingDetail detail : details) {
                DrawingHistory his = new DrawingHistory(drawing);
                his.setVersion(detail.getRev());
                drawingHistoryRepository.save(his);
            }

        }

        drawingRepository.save(drawing);

        TaskPrivilegeDTO dto = new TaskPrivilegeDTO("DESIGN_ENGINEER", null, drawingDTO.getDrawUserId());
        drawingBaseService.setProcessPrivileges(orgId, projectId, drawing.getId(), dto, userid);

        dto = new TaskPrivilegeDTO("DRAWING_CHECK_EXECUTE", null, drawingDTO.getCheckUserId());
        drawingBaseService.setProcessPrivileges(orgId, projectId, drawing.getId(), dto, userid);

        dto = new TaskPrivilegeDTO("DRAWING_REVIEW_EXECUTE", null, drawingDTO.getApprovedUserId());
        drawingBaseService.setProcessPrivileges(orgId, projectId, drawing.getId(), dto, userid);

        return drawing;
    }

    /**
     * 修改清单条目
     */
    @Override
    public Drawing modify(Long orgId, Long projectId, Long id, Long userid,
                          DrawingDTO drawingDTO) {

//        BpmEntitySubType category = null;
//        if (drawingDTO.getDrawingCategoryId() != null) {
//            Optional<BpmEntitySubType> categoryOp = entityCategoryRepository.findById(drawingDTO.getDrawingCategoryId());
//            if (categoryOp.isPresent()) {
//                category = categoryOp.get();
//            }
//        }


        Optional<Drawing> drawingOp = drawingRepository.findById(id);
        if (drawingOp.isPresent()) {
            Drawing drawing = drawingOp.get();
            drawing.setSdrlCode(drawingDTO.getSdrlCode());
            drawing.setPackageNo(drawingDTO.getPackageNo());
            drawing.setPackageName(drawingDTO.getPackageName());
            drawing.setOriginatorName(drawingDTO.getOriginatorName());
            drawing.setProjectNo(drawingDTO.getProjectNo());
            // 主键不能编辑
            drawing.setOrgCode(drawingDTO.getOrgCode());
            drawing.setSystemCode(drawingDTO.getSystemCode());
            drawing.setDocType(drawingDTO.getDocType());
            drawing.setShortCode(drawingDTO.getShortCode());
            drawing.setSheetNo(drawingDTO.getSheetNo());
            drawing.setDocumentTitle(drawingDTO.getDocumentTitle());
            drawing.setDocumentChain(drawingDTO.getDocumentChain());
            drawing.setChainCode(drawingDTO.getChainCode());

            if (drawingDTO.getProgressStage() != null) {
                DrawingDetail drawingDetail = drawingDetailRepository.findByDrawingIdAndProgressStageAndRevNo(drawing.getId(), drawingDTO.getProgressStage(), drawingDTO.getRevNo());
                if (drawingDetail != null) {
                    drawingDetail.setLastModifiedAt();
                    drawingDetail.setProgressStage(drawingDTO.getProgressStage());
                    drawingDetail.setRevNo(drawingDTO.getRevNo());
                    if (drawingDTO.getUploadDate() != null) {
                        drawingDetail.setUploadDate(drawingDTO.getUploadDate());
                    }
                    if (drawingDTO.getOutgoingTransmittal() != null) {
                        drawingDetail.setOutgoingTransmittal(drawingDTO.getOutgoingTransmittal());
                    }
                    if (drawingDTO.getIncomingTransmittal() != null) {
                        drawingDetail.setIncomingTransmittal(drawingDTO.getIncomingTransmittal());
                    }
                    if (drawingDTO.getReplyDate() != null) {
                        drawingDetail.setReplyDate(drawingDTO.getReplyDate());
                    }
                    if (drawingDTO.getReplyStatus() != null) {
                        drawingDetail.setReplyStatus(drawingDTO.getReplyStatus());
                    }

                    drawingDetailRepository.save(drawingDetail);
                } else if (drawingDTO.getProgressStage() != null && drawingDTO.getProgressStage().equals("VOID")) {
                    DrawingDetail detailLatest = drawingDetailRepository.findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(drawing.getId(), EntityStatus.ACTIVE);
                    DrawingDetail detail = new DrawingDetail();
                    detail.setStatus(EntityStatus.ACTIVE);
                    detail.setLastModifiedAt();
                    detail.setCreatedAt();
                    detail.setOrgId(orgId);
                    detail.setProjectId(projectId);
                    detail.setDrawingId(drawing.getId());
                    detail.setProgressStage(detailLatest.getProgressStage());
                    detail.setRevNo("V1");
                    detail.setUploadDate(drawingDTO.getUploadDate());
                    detail.setOutgoingTransmittal(drawingDTO.getOutgoingTransmittal());
                    detail.setIncomingTransmittal(drawingDTO.getIncomingTransmittal());
                    detail.setReplyDate(drawingDTO.getReplyDate());
                    detail.setReplyStatus(drawingDTO.getReplyStatus());
                    drawingDetailRepository.save(detail);
                } else {
                    DrawingDetail detail = new DrawingDetail();
                    detail.setStatus(EntityStatus.ACTIVE);
                    detail.setLastModifiedAt();
                    detail.setCreatedAt();
                    detail.setOrgId(orgId);
                    detail.setProjectId(projectId);
                    detail.setDrawingId(drawing.getId());


                    detail.setProgressStage(drawingDTO.getProgressStage());
                    detail.setRevNo(drawingDTO.getRevNo());
                    detail.setUploadDate(drawingDTO.getUploadDate());
                    detail.setOutgoingTransmittal(drawingDTO.getOutgoingTransmittal());
                    detail.setIncomingTransmittal(drawingDTO.getIncomingTransmittal());
                    detail.setReplyDate(drawingDTO.getReplyDate());
                    detail.setReplyStatus(drawingDTO.getReplyStatus());
//                    detail.setDcrNo(drawingDTO.getDcrNo());
//                    detail.setDcrOutgoingDate(drawingDTO.getDcrOutgoingDate());
//                    detail.setDcrReplyDate(drawingDTO.getDcrReplyDate());
//                    detail.setDcrRequest(drawingDTO.getDcrRequest());
//                    detail.setDcrStatus(drawingDTO.getDcrStatus());
                    drawingDetailRepository.save(detail);
                }

            }


            drawing.setProgressStage(drawingDTO.getProgressStage());
            drawing.setRevNo(drawingDTO.getRevNo());
            drawing.setUploadDate(drawingDTO.getUploadDate());
            drawing.setOutgoingTransmittal(drawingDTO.getOutgoingTransmittal());
            drawing.setIncomingTransmittal(drawingDTO.getIncomingTransmittal());
            drawing.setReplyDate(drawingDTO.getUploadDate());
            drawing.setReplyStatus(drawingDTO.getReplyStatus());


            drawing.setClientDocRev(drawingDTO.getClientDocRev());
            drawing.setClientDocNo(drawingDTO.getClientDocNo());
            drawing.setValidityStatus(drawingDTO.getValidityStatus());
            drawing.setSurveillanceType(drawingDTO.getSurveillanceType());
            drawing.setLastModifiedAt(new Date());
            // 权限暂时注释
//            if (drawingDTO.getDrawingCategoryId() != null) {
//                drawing.setDrawingCategory(category);
//                TaskPrivilegeDTO dto = new TaskPrivilegeDTO("DESIGN_ENGINEER", null, drawingDTO.getDrawUserId());
//                drawingBaseService.setProcessPrivileges(orgId, projectId, drawing.getId(), dto, userid);
//
//                dto = new TaskPrivilegeDTO("DRAWING_CHECK_EXECUTE", null, drawingDTO.getCheckUserId());
//                drawingBaseService.setProcessPrivileges(orgId, projectId, drawing.getId(), dto, userid);
//
//                dto = new TaskPrivilegeDTO("DRAWING_REVIEW_EXECUTE", null, drawingDTO.getApprovedUserId());
//                drawingBaseService.setProcessPrivileges(orgId, projectId, drawing.getId(), dto, userid);

//                if (category != null && !category.isSubDrawingFlg()) {
//                    List<DrawingDetail> details = drawingDetailRepository.findByDrawingIdAndStatus(drawing.getId(), EntityStatus.ACTIVE);
//                    for (DrawingDetail detail : details) {
//                        DrawingHistory his = new DrawingHistory(drawing);
//                        his.setVerison(detail.getRev());
//                        drawingHistoryRepository.save(his);
//                    }
//                }
//            }


            // 如果更改专业，则将之前填报的工时也更改过来
            if (drawingDTO.getDiscCode() != null && !drawingDTO.getDiscCode().equals(drawing.getDiscCode())) {
                drawing.setDiscCode(drawingDTO.getDiscCode());

                List<DrawingRecord> drawingRecords = drawingRecordRepository.findByOrgIdAndProjectIdAndDrawingNoAndDeletedIsFalse(
                    orgId,
                    projectId,
                    drawing.getDwgNo()
                );
                if (drawingRecords.size() > 0) {
                    for (DrawingRecord drawingRecord : drawingRecords) {
                        drawingRecord.setFuncPart(drawingDTO.getFuncPart());
                        drawingRecordRepository.save(drawingRecord);
                    }
                }
            }

            //关联修改task中的信息
            this.updateBpmActivity(projectId, id, drawing);

            return drawingRepository.save(drawing);
        }
        return null;
    }

    /**
     * 修改清单条目
     */
    @Override
    public Drawing modifyVersion(Long orgId, Long projectId, Long id, Long userid,
                                 DrawingDTO drawingDTO) {

        Optional<Drawing> drawingOp = drawingRepository.findById(id);

        if (drawingOp.isPresent()) {
            Drawing drawing = drawingOp.get();

            //查询版本号是否存在
            DrawingDetail drawingDetail = drawingDetailRepository.findByDrawingIdAndRevNo(drawing.getId(), drawingDTO.getNewVersion());

            if (drawingDetail != null) {
                throw new BusinessError("New version already exists");
            }

            DrawingDetail oldDrawingDetail = drawingDetailRepository.findByDrawingIdAndRevNo(drawing.getId(), drawingDTO.getLatestRev());

            oldDrawingDetail.setRevNo(drawingDTO.getNewVersion());
            oldDrawingDetail.setLastModifiedAt(new Date());
            drawingDetailRepository.save(oldDrawingDetail);

            drawing.setLatestRev(drawingDTO.getNewVersion());
            drawing.setLastModifiedBy(userid);
            drawing.setLastModifiedAt(new Date());

            return drawingRepository.save(drawing);
        }
        return null;
    }

    private void updateBpmActivity(Long projectId, Long enityId, Drawing drawing) {
        List<BpmActivityInstanceBase> tasks = bpmActInstRepository.findByProjectIdAndEntityId(projectId, enityId);
        for (BpmActivityInstanceBase item : tasks) {
            item.setEntityNo(drawing.getNo());
            item.setDrawingTitle(drawing.getDocumentTitle());
            bpmActInstRepository.save(item);
        }
    }

    /**
     * 获取图纸筛选条件
     */
    @Override
    public List<DrawingCriteriaDTO> getParamList(Long orgId, Long projectId) {

        List<DrawingCriteriaDTO> result = drawingRepository.getParamList(orgId, projectId);

        return result;

    }

    String getUserName(Long userId) {
        String name = "";
        JsonObjectResponseBody<UserProfile> userResponse = userFeignAPI.get(userId);
        if (userResponse.getData() != null) {
            name = userResponse.getData().getName();
        }
        return name;
    }


    /**
     * 获取图纸清单列表
     */
    @Override
    public Page<DrawingWorkHourDTO> getList(Long orgId, Long projectId, PageDTO page,
                                            DrawingCriteriaDTO criteriaDTO) {
        Page<DrawingWorkHourDTO> result = drawingRepository.getList(orgId, projectId, page, criteriaDTO);
//        for (DrawingWorkHourDTO drawing : result.getContent()) {
//            List<BpmActivityInstanceBase> actInstList = bpmActInstRepository.findByProjectIdAndEntityIdAndFinishStateAndSuspensionState(
//                projectId, drawing.getId(), ActInstFinishState.NOT_FINISHED, SuspensionState.ACTIVE);
//            if (!actInstList.isEmpty()) {
//                drawing.setActInst(true);
//            }
//
//            if (drawing.getDrawingCategory() != null && drawing.getDrawingCategory().isSubDrawingFlg()) {
//                PageDTO subPage = new PageDTO();
//                subPage.setFetchAll(true);
//                SubDrawingCriteriaDTO criteriaSubDTO = new SubDrawingCriteriaDTO();
//                criteriaSubDTO.setOrderByNo(true);
//                criteriaSubDTO.setDrawingVersion(drawing.getLatestRev());
//                Page<SubDrawing> subList = subDrawingService.getList(
//                    orgId,
//                    projectId,
//                    drawing.getId(),
//                    subPage,
//                    criteriaSubDTO
//                );
//
//                Integer initCount = 0;
//                Integer checkCount = 0;
//                Integer reviewCount = 0;
//                Integer modifyCount = 0;
//                Integer checkDoneCount = 0;
//                Integer reviewDoneCount = 0;
//                Integer totalCount = 0;
//                if (subList != null) {
//                    for (SubDrawing sub : subList.getContent()) {
//                        switch (sub.getReviewStatus()) {
//                            case INIT:
//                                initCount++;
//                                break;
//                            case CHECK:
//                                checkCount++;
//                                break;
//                            case REVIEW:
//                                reviewCount++;
//                                break;
//                            case MODIFY:
//                                modifyCount++;
//                                break;
//                            case CHECK_DONE:
//                                checkDoneCount++;
//                                break;
//                            case REVIEW_DONE:
//                                reviewDoneCount++;
//                                break;
//                        }
//                        totalCount++;
//                    }
//                }
//                drawing.setInitCount(initCount);
//                drawing.setCheckCount(checkCount);
//                drawing.setReviewCount(reviewCount);
//                drawing.setModifyCount(modifyCount);
//                drawing.setReviewDoneCount(reviewDoneCount);
//                drawing.setCheckDoneCount(checkDoneCount);
//                drawing.setTotalCount(totalCount);
//            }
//
//        }
        return result;
    }

    @Override
    public DrawingFilterDTO getDrawingDisciplines(Long orgId, Long projectId) {
        DrawingFilterDTO result = new DrawingFilterDTO();
        List<String> disciplines = drawingRepository.findDisciplinesByOrgIdAndProjectId(orgId, projectId);
        result.setDisciplineList(disciplines);
        return result;
    }

    @Override
    public DrawingFilterDTO getDrawingDocTypes(Long orgId, Long projectId) {
        DrawingFilterDTO result = new DrawingFilterDTO();
        List<String> docTypes = drawingRepository.findDocTypesByOrgIdAndProjectId(orgId, projectId);
        result.setDocTypeList(docTypes);
        return result;
    }

    /**
     * 获取图纸清单列表(包含deleted)
     */
    @Override
    public List<Drawing> getAllList(Long orgId, Long projectId) {
        List<Drawing> result = drawingRepository.findByOrgIdAndProjectId(orgId, projectId);
        return result;
    }

    /**
     * 删除上传的图纸文件，标记删除
     */
    @Override
    public boolean deleteDrawingFile(Long orgId, Long projectId, Long drawingFileId, DrawingDeleteDTO drawingDeleteDTO, Long operatorId) {

        DrawingFile df = drawingFileRepository.findById(drawingFileId).orElse(null);
        if (null == df) {
            return false;
        }
        DrawingFileHistory drawingFileHistory = drawingFileHistoryRepository.findByOrgIdAndProjectIdAndDrawingFileIdAndTaskIdAndStatus(
            orgId,
            projectId,
            drawingFileId,
            drawingDeleteDTO.getTaskId(),
            EntityStatus.ACTIVE
        );
        if (null != drawingFileHistory) {
            drawingFileHistory.setLastModifiedAt(new Date());
            drawingFileHistory.setLastModifiedBy(operatorId);
            drawingFileHistory.setStatus(EntityStatus.DELETED);
            drawingFileHistoryRepository.save(drawingFileHistory);
        }

        df.setStatus(EntityStatus.DELETED);
        df.setLastModifiedAt();
        drawingFileRepository.save(df);
        return true;

    }


    /**
     * 删除图纸清单条目
     */
    @Override
    public boolean delete(Long orgId, Long projectId, Long id, DrawingDeleteDTO drawingDeleteDTO) {
        Optional<Drawing> drawingOp = drawingRepository.findById(id);
        if (drawingOp.isPresent()) {
            Drawing drawing = drawingOp.get();

            List<DrawingRecord> drawingRecords = drawingRecordRepository.findAllByOrgIdAndProjectIdAndDrawingIdAndDeletedIsFalse(orgId, projectId, drawing.getId());
            if (drawingRecords.size() > 0 && drawingRecords != null) {
                throw new BusinessError("There is a working hour record under this drawing. Please delete the working hour record first!");
            }

            if (drawingDeleteDTO.getDcrNo() != null) {

                // 添加detail（因为void必须在相应的某一版本上处理，所以详情不能为空）
                DrawingDetail drawingDetail = drawingDetailRepository.findByDrawingIdAndProgressStageAndRevNo(drawing.getId(), drawingDeleteDTO.getProgressStage(), drawingDeleteDTO.getRevNo());
                if (drawingDetail != null) {
//                    if (drawingDetail.getDcrNo() == null || "".equals(drawingDetail.getDcrNo())) {
//                        drawingDetail.setDcrNo(drawingDeleteDTO.getDcrNo());
//                        drawingDetail.setDcrOutgoingDate(drawingDeleteDTO.getDcrOutgoingDate());
//                        drawingDetail.setDcrReplyDate(drawingDeleteDTO.getDcrReplyDate());
//                        drawingDetail.setDcrRequest(drawingDeleteDTO.getDcrRequest());
//                        drawingDetail.setDcrStatus(drawingDeleteDTO.getDcrStatus());
//                    } else if (drawingDetail.getDcr2No() == null || "".equals(drawingDetail.getDcr2No())) {
//                        drawingDetail.setDcr2No(drawingDeleteDTO.getDcrNo());
//                        drawingDetail.setDcr2OutgoingDate(drawingDeleteDTO.getDcrOutgoingDate());
//                        drawingDetail.setDcr2ReplyDate(drawingDeleteDTO.getDcrReplyDate());
//                        drawingDetail.setDcr2Request(drawingDeleteDTO.getDcrRequest());
//                        drawingDetail.setDcr2Status(drawingDeleteDTO.getDcrStatus());
//                    } else if (drawingDetail.getDcr3No() == null || "".equals(drawingDetail.getDcr3No())) {
//                        drawingDetail.setDcr3No(drawingDeleteDTO.getDcrNo());
//                        drawingDetail.setDcr3OutgoingDate(drawingDeleteDTO.getDcrOutgoingDate());
//                        drawingDetail.setDcr3ReplyDate(drawingDeleteDTO.getDcrReplyDate());
//                        drawingDetail.setDcr3Request(drawingDeleteDTO.getDcrRequest());
//                        drawingDetail.setDcr3Status(drawingDeleteDTO.getDcrStatus());
//                    }
                    drawingDetailRepository.save(drawingDetail);

                } else {
                    throw new BusinessError("No corresponding version information was found during void!");
                }

            }

            drawing.setStatus(EntityStatus.DELETED);
            drawing.setLastModifiedAt();
            drawingRepository.save(drawing);
            return true;
        }
        return false;
    }

    public boolean physicalDelete(Long orgId, Long projectId, Long id) {
        Optional<Drawing> drawingOp = drawingRepository.findById(id);
        if (drawingOp.isPresent()) {
            Drawing drawing = drawingOp.get();

            List<DrawingRecord> drawingRecords = drawingRecordRepository.findAllByOrgIdAndProjectIdAndDrawingIdAndDeletedIsFalse(orgId, projectId, drawing.getId());
            if (drawingRecords.size() > 0 && drawingRecords != null) {
                throw new BusinessError("There is a working hour record under this drawing. Please delete the working hour record first!");
            }

            drawing.setStatus(EntityStatus.CLOSED);
            drawing.setLastModifiedAt();
            drawingRepository.save(drawing);
            return true;
        }
        return false;
    }


    @Override
    public Drawing findByDwgNo(Long orgId, Long projectId, String dwgNo) {
        Optional<Drawing> op = drawingRepository.findByOrgIdAndProjectIdAndDwgNoAndStatus(orgId, projectId, dwgNo, EntityStatus.ACTIVE);
        if (op.isPresent()) {
            return op.get();
        }
        return null;
    }

    /**
     * 保存图纸条目
     */
    @Override
    public Drawing save(Drawing d) {
        return drawingRepository.save(d);
    }

    /**
     * 保存图纸文件历史记录
     */
    @Override
    public DrawingHistory save(DrawingHistory his) {
        return drawingHistoryRepository.save(his);
    }


    @Override
    public boolean check(ContextDTO contextDTO, Long orgId, Long projectId, Long id, OperatorDTO operatorDTO,
                         DrawingProofreadDTO proofreadDTO) {

        if (LongUtils.isEmpty(proofreadDTO.getTaskId())) {
            throw new BusinessError("there is no task id");
        }

        BpmRuTask ruTask = ruTaskRepository.findById(proofreadDTO.getTaskId()).orElse(null);
        if (ruTask == null) {
            throw new BusinessError("There is no running task in DB with ID" + proofreadDTO.getTaskId());
        }


        Optional<Drawing> opDrawing = drawingRepository.findById(id);
        if (!opDrawing.isPresent()) return false;

        String latestRev = null;

        Drawing drawing = opDrawing.get();

        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, drawing.getEntitySubType());

        if (best != null && best.isSubDrawingFlg()) {
            latestRev = drawing.getLatestRev();

            List<SubDrawing> subDrawingsCheck = subDrawingRepository.findByDrawingVersionAndDrawingIdAndStatusActive(orgId, projectId, latestRev,
                drawing.getId(), EntityStatus.PENDING);

            if (subDrawingsCheck.size() == 0) {
                throw new BusinessError("无审核图纸");
            }

            List<SubDrawing> subDrawingsChk = subDrawingRepository.findByDrawingVersionAndDrawingIdAndFileIdIsNull(orgId, projectId,
                latestRev, drawing.getId(), EntityStatus.PENDING);

            if (subDrawingsChk.size() > 0) {
                throw new BusinessError("请上传图纸 ");
            }


        } else {

            if (drawing.getFileId() == null) {
                throw new ValidationError("请上传图纸");
            }
        }


        TodoTaskExecuteDTO todoDTO = new TodoTaskExecuteDTO();
        Map<String, Object> variables = new HashMap<>();

        variables.put("drawing", drawing);
        variables.put("latestRev", latestRev);
        todoDTO.setVariables(variables);
        boolean result = todoTaskDispatchService.exec(contextDTO, orgId, projectId, ruTask.getId(), todoDTO, operatorDTO).isExecResult();

        return result;
    }

    @Override
    public boolean redMarkCheck(ContextDTO contextDTO, Long orgId, Long projectId, Long id, OperatorDTO operatorDTO,
                                DrawingProofreadDTO proofreadDTO) {

        if (LongUtils.isEmpty(proofreadDTO.getTaskId())) {
            throw new BusinessError("there is no task id");
        }

        BpmRuTask ruTask = ruTaskRepository.findById(proofreadDTO.getTaskId()).orElse(null);
        if (ruTask == null) {
            throw new BusinessError("There is no running task in DB with ID" + proofreadDTO.getTaskId());
        }


        Optional<Drawing> opDrawing = drawingRepository.findById(id);
        if (!opDrawing.isPresent()) return false;

        String latestRev = null;

        Drawing drawing = opDrawing.get();

        drawing.setFileName(drawing.getDwgNo() + "_" + "R" + drawing.getLatestRev() + ".pdf");


        List<SubDrawing> subDrawingsChk = subDrawingRepository.findByDrawingVersionAndDrawingIdAndAndProcIdAndFileIdIsNull(orgId, projectId, ruTask.getActInstId(), latestRev, drawing.getId());

        if (!CollectionUtils.isEmpty(subDrawingsChk)) {
            throw new ValidationError("请上传图纸 " + subDrawingsChk.get(0).getSubDrawingNo());
        }


        if (proofreadDTO.getActInstId() == null || proofreadDTO.getActInstId().equals("")) {
            throw new ValidationError("请从待办任务中重新处理。");
        }
        List<SubDrawing> redMarkSubDrawingList = subDrawingRepository.findByOrgIdAndProjectIdAndDrawingIdAndActInstId(orgId, projectId, id, proofreadDTO.getActInstId());
        if (redMarkSubDrawingList == null || redMarkSubDrawingList.size() == 0) {
            throw new ValidationError("请上传 RedMark 图纸");
        }


        TodoTaskExecuteDTO todoDTO = new TodoTaskExecuteDTO();
        Map<String, Object> variables = new HashMap<>();

        variables.put("drawing", drawing);
        variables.put("latestRev", latestRev);
        todoDTO.setNextAssignee(proofreadDTO.getRedMarkAssignId());
        todoDTO.setVariables(variables);
        boolean result = todoTaskDispatchService.exec(contextDTO, orgId, projectId, ruTask.getId(), todoDTO, operatorDTO).isExecResult();

        return result;
    }

    /**
     * 生产支架制作图报表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param dl
     * @param project
     * @param userid
     */
    @Override
    public DrawingFileDTO generateReportPipeSupport(Long orgId, Long projectId, Drawing dl, Project project,
                                                    Long userid, boolean printCoverQRCode, boolean printDwgQRCode) {

        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dl.getEntitySubType());
        int positionX = best.getDrawingPositionX();
        int positionY = best.getDrawingPositionY();
        int scaleToFit = best.getDrawingScaleToFit();

        int coverPositionX = best.getCoverPositionX();
        int coverPositionY = best.getCoverPositionY();
        int coverScaleToFit = best.getCoverScaleToFit();

        String qrcode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();
        dl.setQrCode(qrcode);
        drawingRepository.save(dl);

        try {

            String savepath = temporaryDir + dl.getDwgNo() + "_" + "R" + dl.getLatestRev() + ".pdf";

            List<SubDrawing> subs = drawingBaseService.findSubByDrawingId(dl.getId());
            if (subs.size() > 0) {
                int filecount = 0;
                int listIndex = 0;
                int index = 2;
                if (dl.getCoverId() != null) {
                    filecount = subs.size() + 3;
                    listIndex++;
                    index++;
                } else {
                    filecount = subs.size() + 2;
                }
                String[] files = new String[filecount];
                if (dl.getCoverId() != null) {
                    String coverFilePath = protectedDir + dl.getCoverPath().substring(1);
                    if (printCoverQRCode) {

                        String coverPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";

                        Optional<DrawingDetail> opDetail = drawingDetailRepository.findByDrawingIdAndRevAndStatus(dl.getId(), dl.getLatestRev(), EntityStatus.ACTIVE);
                        if (opDetail.isPresent()) {
                            DrawingDetail detail = opDetail.get();


                            String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                            String qrCode = dl.getQrCode();
                            QRCodeUtils.generateQRCodeNoBlank(qrCode, coverScaleToFit, "png", fileName);

                            PdfUtils.setImageToPdf(coverPDF, coverFilePath, fileName, coverPositionX, coverPositionY, coverScaleToFit, coverScaleToFit);
                        }
                        files[0] = drawingBaseService.generateSignatureCover(
                            orgId,
                            projectId,
                            dl,
                            coverPDF
                        );
                        ;
                    } else {
                        files[0] = coverFilePath;
                    }
                }
                List<SubDrawingConfig> variables = drawingBaseService.getVariables(orgId, projectId,
                    dl.getId());
                Map<String, Integer> countMap = new HashMap<>();
                List<DrawSubPipeSupportListDTO> items = new ArrayList<>();

                int seq = 1;
                for (SubDrawing sub : subs) {
                    String subFilePath = protectedDir + sub.getFilePath().substring(1);

                    if (printDwgQRCode) {

                        String subPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";


                        String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                        String qrCode = sub.getQrCode();
                        QRCodeUtils.generateQRCodeNoBlank(qrCode, scaleToFit, "png", fileName);


                        if (!LongUtils.isEmpty(sub.getActInstId())) {
                            PdfUtils.setImageAndFontToPdf(subPDF, subFilePath, fileName, 50, 50, 75, "No: " + seq + " OF " + subs.size(), 20, 15, 160, 15);
                        } else {
                            PdfUtils.setImageAndFontToPdf(subPDF, subFilePath, fileName, positionX, positionY, scaleToFit, "No: " + seq + " OF " + subs.size(), 20, 15, 160, 15);
                        }

                        files[index++] = subPDF;

                    } else {
                        String subPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";


                        PdfUtils.setFontToPdf(subPDF, subFilePath, "No: " + seq + " OF " + subs.size(), 20, 15, 160, 15);
                        files[index++] = subPDF;
                    }

                    if (sub.getConfigData() != null) {
                        try {
                            List<Map<String, String>> le = StringUtils.fromJSON(sub.getConfigData(), List.class);
                            for (Map<String, String> map : le) {

                                DrawSubPipeSupportListDTO subDTO2 = new DrawSubPipeSupportListDTO();
                                subDTO2.setSeq(String.valueOf(seq++));
                                subDTO2.setSupportNo(sub.getSubNo());
                                subDTO2.setMaterial(map.get("texture"));
                                subDTO2.setSpecifications(map.get("specification"));
                                subDTO2.setUnit(map.get("unit"));
                                subDTO2.setCount(map.get("num"));
                                items.add(subDTO2);

                                String countKey = "";
                                for (SubDrawingConfig v : variables) {
                                    String key = v.getVariableName();
                                    if (key.equals("num"))
                                        continue;
                                    String value = map.get(key);
                                    countKey += "#" + key + "," + value;
                                }
                                countKey = countKey.substring(1);
                                if (countMap.containsKey(countKey)) {
                                    int i = countMap.get(countKey);
                                    countMap.remove(countKey);
                                    countMap.put(countKey, i + Integer.parseInt(map.get("num")));
                                } else {
                                    countMap.put(countKey, Integer.parseInt(map.get("num")));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace(System.out);
                        }
                    }
                }

                DrawSubPipeSupportDTO drawSubPipeSupportDTO = new DrawSubPipeSupportDTO();
                drawSubPipeSupportDTO.setOrgId(orgId);
                drawSubPipeSupportDTO.setProjectId(projectId);
                drawSubPipeSupportDTO.setProjectName(project.getName());
                drawSubPipeSupportDTO.setDrawNumber(dl.getDwgNo());
                drawSubPipeSupportDTO.setTitle(dl.getDocumentTitle() + "(详细)");
                drawSubPipeSupportDTO.setItems(items);

                JsonObjectResponseBody<ReportHistory> reportResponse = drawingReportFeignAPI
                    .generateDrawConstructionSupportReport(orgId, projectId, drawSubPipeSupportDTO);
                ReportHistory rs = reportResponse.getData();
                files[listIndex + 1] = protectedDir + rs.getFilePath().substring(1);

                List<DrawSubPipeSupportOverallListDto> itemsOverAll = new ArrayList<>();
                DrawSubPipeSupportOverallDTO drawSubPipeSupportOverallDTO = new DrawSubPipeSupportOverallDTO();
                drawSubPipeSupportOverallDTO.setDrawNumber(dl.getDwgNo());
                drawSubPipeSupportOverallDTO.setTitle(dl.getDocumentTitle() + "(合计)");
                drawSubPipeSupportOverallDTO.setProjectName(project.getName());

                Set<String> countMapSet = countMap.keySet();
                Iterator<String> countMapIterator = countMapSet.iterator();
                int seqCount = 1;
                while (countMapIterator.hasNext()) {
                    DrawSubPipeSupportOverallListDto dto = new DrawSubPipeSupportOverallListDto();
                    String key = countMapIterator.next();
                    String[] mapStr = key.split("#");
                    for (int i = 0; i < mapStr.length; i++) {
                        String[] kv = mapStr[i].split(",");
                        if (kv[0].equals("texture"))
                            dto.setMaterial(kv[1]);
                        if (kv[0].equals("specification"))
                            dto.setSpecifications(kv[1]);
                        if (kv[0].equals("unit"))
                            dto.setUnit(kv[1]);
                        if (kv[0].equals("num"))
                            dto.setCount(kv[1]);
                    }
                    dto.setSeq(String.valueOf(seqCount++));
                    dto.setCount(countMap.get(key).toString());
                    itemsOverAll.add(dto);
                }
                drawSubPipeSupportOverallDTO.setItems(itemsOverAll);

                JsonObjectResponseBody<ReportHistory> reportOverAllResponse = drawingReportFeignAPI
                    .generateDrawConstructionSupportOverallReport(orgId, projectId, drawSubPipeSupportOverallDTO);
                ReportHistory rsOverAll = reportOverAllResponse.getData();
                files[listIndex] = protectedDir + rsOverAll.getFilePath().substring(1);

                PdfUtils.mergePdfFiles(files, savepath);

                File diskFile = new File(savepath);
                System.out.println(diskFile.getName());
                DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                    MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

                IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());


                logger.error("图纸1 上传docs服务->开始");
                MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
                JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                    .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                logger.error("图纸1 上传docs服务->结束");
                logger.error("图纸1 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                    tempFileResBody.getData().getName(), new FilePostDTO());
                logger.error("图纸1 保存docs服务->结束");
                DrawingFileDTO dto = new DrawingFileDTO();
                dto.setFileId(LongUtils.parseLong(fileESResBody.getData().getId()));
                dto.setFileName(fileESResBody.getData().getName());
                dto.setFilePath(fileESResBody.getData().getPath());
                dto.setQrCode(dl.getQrCode());
                return dto;
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
        return null;
    }


    /**
     * 生成上报设计图报表
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param dl
     * @param project
     * @param userid
     */
    @Override
    public DrawingFileDTO generateReportPipe(Long orgId, Long projectId, Drawing dl, Project project,
                                             Long userid,
                                             boolean printCoverQRCode, boolean printDwgQRCode) {
        BpmEntitySubType best = entitySubTypeService.getEntitySubType(projectId, dl.getEntitySubType());


        int positionX = best.getDrawingPositionX();
        int positionY = best.getDrawingPositionY();
        int scaleToFit = best.getDrawingScaleToFit();


        int coverPositionX = best.getCoverPositionX();
        int coverPositionY = best.getCoverPositionY();
        int coverScaleToFit = best.getCoverScaleToFit();


        String qrcode = QrcodePrefixType.DRAWING.getCode() + StringUtils.generateShortUuid();
        dl.setQrCode(qrcode);
        drawingRepository.save(dl);

        try {

            String savepath = temporaryDir + dl.getDwgNo() + "_" + "R" + dl.getLatestRev() + ".pdf";


            List<SubDrawing> subs = drawingBaseService.findSubByDrawingId(dl.getId());


            List<SubDrawing> subFiles = drawingBaseService.findByDrawingIdAndStatusAndFilePathNotNull(dl.getId());
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


                        Optional<DrawingDetail> opDetail = drawingDetailRepository.findByDrawingIdAndRevAndStatus(dl.getId(), dl.getLatestRev(), EntityStatus.ACTIVE);

                        if (opDetail.isPresent()) {
                            DrawingDetail detail = opDetail.get();


                            String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                            String qrCode = dl.getQrCode();


                            QRCodeUtils.generateQRCodeNoBlank(qrCode, coverScaleToFit, "png", fileName);


                            PdfUtils.setImageToPdf(coverPDF, coverFilePath, fileName, coverPositionX, coverPositionY, coverScaleToFit, coverScaleToFit);
                        }


                        files[0] = drawingBaseService.generateSignatureCover(
                            orgId,
                            projectId,
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
                dto.setTitle(dl.getDocumentTitle());
                dto.setOrgId(orgId);
                dto.setProjectId(projectId);

                List<DrawSubPipeListDTO> items = new ArrayList<DrawSubPipeListDTO>();

                int seq = 1;
                for (SubDrawing sub : subs) {

                    if (sub.getFilePath() != null) {
                        String subFilePath = protectedDir + sub.getFilePath().substring(1);

                        if (printDwgQRCode) {

                            String subPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";


                            String fileName = temporaryDir + CryptoUtils.uniqueId() + ".png";
                            String qrCode = sub.getQrCode();
                            QRCodeUtils.generateQRCodeNoBlank(qrCode, scaleToFit, "png", fileName);


                            if (!LongUtils.isEmpty(sub.getActInstId())) {
                                PdfUtils.setImageAndFontToPdf(subPDF, subFilePath, fileName, 50, 50, 75, "No: " + seq + " OF " + subs.size(), 20, 15, 160, 15);
                            } else {
                                PdfUtils.setImageAndFontToPdf(subPDF, subFilePath, fileName, positionX, positionY, scaleToFit, "No: " + seq + " OF " + subs.size(), 20, 15, 160, 15);
                            }

                            files[index++] = subPDF;

                        } else {
                            String subPDF = temporaryDir + CryptoUtils.uniqueId() + ".pdf";


                            PdfUtils.setFontToPdf(subPDF, subFilePath, "No: " + seq + " OF " + subs.size(), 20, 15, 160, 15);
                            files[index++] = subPDF;
                        }

                        DrawSubPipeListDTO subDTO = new DrawSubPipeListDTO();
                        String pageInfo = sub.getPageNo() + " OF " + sub.getPageCount();
                        subDTO.setIsoVersion(String.valueOf(sub.getSubDrawingVersion()));
                        subDTO.setSeq(String.valueOf(seq++));
                        subDTO.setSubDrawNo(sub.getSubDrawingNo());
                        subDTO.setPageInfo(pageInfo);
                        items.add(subDTO);

                    } else {
                        DrawSubPipeListDTO subDTO = new DrawSubPipeListDTO();
                        subDTO.setIsoVersion("");
                        subDTO.setSeq(String.valueOf(seq++));
                        subDTO.setSubDrawNo(sub.getSubDrawingNo());
                        subDTO.setPageInfo("");
                        items.add(subDTO);
                    }
                }

                dto.setItems(items);
                JsonObjectResponseBody<ReportHistory> reportResponse = drawingReportFeignAPI
                    .generateDrawConstructionreport(orgId, projectId, dto);
                ReportHistory rs = reportResponse.getData();
                files[listIndex] = protectedDir + rs.getFilePath().substring(1);
                PdfUtils.mergePdfFiles(files, savepath);

                File diskFile = new File(savepath);
                System.out.println(diskFile.getName());
                DiskFileItem fileItem = (DiskFileItem) (new DiskFileItemFactory()).createItem("file",
                    MediaType.APPLICATION_PDF_VALUE, true, diskFile.getName());

                IOUtils.copy(new FileInputStream(diskFile), fileItem.getOutputStream());


                logger.error("图纸2 上传docs服务->开始");
                MockMultipartFile fileItem1 = new MockMultipartFile("file", fileItem.getName(),
                    APPLICATION_PDF_VALUE, fileItem.getInputStream());
                JsonObjectResponseBody<TemporaryFileDTO> tempFileResBody = uploadFeignAPI
                    .uploadProjectDocumentFile(orgId.toString(), fileItem1);
                logger.error("图纸2 上传docs服务->结束");
                logger.error("图纸2 保存docs服务->开始");
                JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(),
                    tempFileResBody.getData().getName(), new FilePostDTO());
                logger.error("图纸2 保存docs服务->结束");
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


    /**
     * 获取图纸详细信息列表
     */
    @Override
    public List<DrawingDetail> getList(Long orgId, Long projectId, Long drawingId, DrawingDetailQueryDTO drawingDetailQueryDTO) {
        if (drawingDetailQueryDTO == null || drawingDetailQueryDTO.getStatus() == null) {
            return drawingDetailRepository.findByDrawingIdOrderByCreatedAt(drawingId);
        } else {
            return drawingDetailRepository.findByDrawingIdAndStatusInOrderByCreatedAt(drawingId, drawingDetailQueryDTO.getStatus());
        }

    }

    @Override
    public List<DrawingDetail> getListFiles(Long orgId, Long projectId, Long drawingId, DrawingDetailQueryDTO drawingDetailQueryDTO) {
        List<DrawingDetail> drawingDetails = new ArrayList<>();
        List<BpmActivityInstanceBase> bais = bpmActInstRepository.findByProjectIdAndEntityIdAndFinishStateAndSuspensionState(
            projectId,
            drawingId,
            ActInstFinishState.NOT_FINISHED,
            SuspensionState.ACTIVE
        );
        if (drawingDetailQueryDTO == null || drawingDetailQueryDTO.getStatus() == null) {
            if (bais.isEmpty()) {
                return null;
            } else {
                drawingDetails = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(drawingId, bais.get(0).getId());
            }
        } else {
            drawingDetails = drawingDetailRepository.findByDrawingIdAndStatusInOrderByCreatedAt(drawingId, drawingDetailQueryDTO.getStatus());
        }
        drawingDetails.forEach(dd -> {
            List<DrawingFile> drawingFiles = drawingFileRepository.findByProjectIdAndDrawingDetailIdAndStatus(
                dd.getProjectId(), dd.getId(), EntityStatus.ACTIVE
            );
            dd.setDrawingFiles(drawingFiles);
        });

        return drawingDetails;
    }

    @Override
    public List<DrawingDetail> getAllListFiles(Long orgId, Long projectId, Long drawingId, DrawingDetailQueryDTO drawingDetailQueryDTO) {
        List<DrawingDetail> drawingDetails = drawingDetailRepository.findByDrawingId(drawingId);
        drawingDetails.forEach(dd -> {
            List<DrawingFile> drawingFiles = drawingFileRepository.findByProjectIdAndDrawingDetailIdAndStatus(
                dd.getProjectId(), dd.getId(), EntityStatus.ACTIVE
            );
            dd.setDrawingFiles(drawingFiles);
        });

        return drawingDetails;
    }


    @Override
    public File saveDownloadFile(Long orgId, Long projectId, DrawingCriteriaDTO criteriaDTO, Long operatorId) {


        String templateFilePathDown = templateFilePath + "export-drawing-list.xlsx";
        String templateFileName = System.currentTimeMillis() + ".xlsx";
        String filePathDown = filePath + templateFileName;
        File excel = new File(temporaryDir, templateFileName);
        PageDTO pageDTO = new PageDTO();
        pageDTO.setFetchAll(true);
        List<DrawingWorkHourDTO> drawingListPipings = getList(
            orgId,
            projectId,
            pageDTO,
            criteriaDTO).getContent();

        //导出新增字段
        for (DrawingWorkHourDTO item : drawingListPipings) {
            ActInstCriteriaDTO dto = new ActInstCriteriaDTO();
            dto.setEntityNo(item.getDocumentTitle());
            List<BpmActivityInstanceDTO> bpmActivityInstanceDTOS = activityTaskService.actInstList(orgId, projectId, dto).getContent();

            if (bpmActivityInstanceDTOS.size() > 0) {
                BpmActivityInstanceDTO data = bpmActivityInstanceDTOS.get(bpmActivityInstanceDTOS.size() - 1);
                //判断任务是否已完成
                if (data.getFinishState().equals(ActInstFinishState.FINISHED)) {
                    //任务已完成的情况，只需要issued rev & designer & 图纸任务阶段
                    List<DrawingDetail> drawingDetails = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(data.getEntityId(), data.getId());
                    if (drawingDetails.size() > 0) item.setIssuedRev(drawingDetails.get(0).getRevNo());
                    item.setCurrentProcess(data.getProcessStage() + "-" + data.getProcess());
                    //判断是不是升版
                    if (!data.getProcess().contains("_U")) {
                        List<BpmActTask> drawingDesign = bpmActTaskRepository.findByActInstIdAndTaskDefKey(data.getId(), "usertask-USER-DRAWING-DESIGN");
                        if (!drawingDesign.isEmpty()) item.setIssuedDesigner(drawingDesign.get(0).getOperatorName());
                    } else {
                        //是升版则去查原版的designer
                        String process = data.getProcess().substring(0, 3);
                        dto.setProcessName(process);
                        List<BpmActivityInstanceDTO> activityInstanceDTOs = activityTaskService.actInstList(orgId, projectId, dto).getContent();
                        if (!activityInstanceDTOs.isEmpty()) {
                            List<BpmActTask> drawingDesign = bpmActTaskRepository.findByActInstIdAndTaskDefKey(activityInstanceDTOs.get(0).getId(), "usertask-USER-DRAWING-DESIGN");
                            if (!drawingDesign.isEmpty())
                                item.setIssuedDesigner(drawingDesign.get(0).getOperatorName());
                        }
                    }
                } else {
                    //任务未完成的情况
                    //判断是否是在RECORD RESULT节点
                    if (data.getCurrentTaskNode().equals("RECORD RESULT")) {
                        List<DrawingDetail> drawingDetails = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(data.getEntityId(), data.getId());
                        if (!drawingDetails.isEmpty()) item.setIssuedRev(drawingDetails.get(0).getRevNo());

                        //判断是不是升版
                        if (!data.getProcess().contains("_U")) {
                            List<BpmActTask> drawingDesign = bpmActTaskRepository.findByActInstIdAndTaskDefKey(data.getId(), "usertask-USER-DRAWING-DESIGN");
                            if (!drawingDesign.isEmpty())
                                item.setIssuedDesigner(drawingDesign.get(0).getOperatorName());
                        } else {
                            //是升版则去查原版的designer
                            String process = data.getProcess().substring(0, 3);
                            dto.setProcessName(process);
                            List<BpmActivityInstanceDTO> activityInstanceDTOs = activityTaskService.actInstList(orgId, projectId, dto).getContent();
                            if (!activityInstanceDTOs.isEmpty()) {
                                List<BpmActTask> drawingDesign = bpmActTaskRepository.findByActInstIdAndTaskDefKey(activityInstanceDTOs.get(0).getId(), "usertask-USER-DRAWING-DESIGN");
                                if (!drawingDesign.isEmpty())
                                    item.setIssuedDesigner(drawingDesign.get(0).getOperatorName());
                            }
                        }

                        item.setCurrentProcess(data.getProcessStage() + "-" + data.getProcess() + "-" + data.getCurrentTaskNode());
                        item.setCurrentExecutor(data.getCurrentExecutor());
                    } else {
                        //不在RECORD RESULT节点,判断是否存在多个任务
                        if (bpmActivityInstanceDTOS.size() > 1) {
                            //存在多个任务，则需要去查上一个任务用上一个任务为issued，当前任务为unissued
                            List<DrawingDetail> drawingDetails1 = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(data.getEntityId(), data.getId());
                            if (!drawingDetails1.isEmpty()) item.setUnIssuedRev(drawingDetails1.get(0).getRevNo());

                            //判断是不是升版
                            if (!data.getProcess().contains("_U")) {
                                List<BpmActTask> drawingDesign = bpmActTaskRepository.findByActInstIdAndTaskDefKey(data.getId(), "usertask-USER-DRAWING-DESIGN");
                                if (!drawingDesign.isEmpty())
                                    item.setUnIssuedDesigner(drawingDesign.get(0).getOperatorName());
                            } else {
                                //是升版则去查原版的designer
                                String process = data.getProcess().substring(0, 3);
                                dto.setProcessName(process);
                                List<BpmActivityInstanceDTO> activityInstanceDTOs = activityTaskService.actInstList(orgId, projectId, dto).getContent();
                                if (!activityInstanceDTOs.isEmpty()) {
                                    List<BpmActTask> drawingDesign = bpmActTaskRepository.findByActInstIdAndTaskDefKey(activityInstanceDTOs.get(0).getId(), "usertask-USER-DRAWING-DESIGN");
                                    if (!drawingDesign.isEmpty())
                                        item.setIssuedDesigner(drawingDesign.get(0).getOperatorName());
                                }
                            }
                            //上一个任务
                            BpmActivityInstanceDTO data2 = bpmActivityInstanceDTOS.get(bpmActivityInstanceDTOS.size() - 2);
                            List<DrawingDetail> drawingDetails2 = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(data2.getEntityId(), data2.getId());
                            if (drawingDetails2.size() > 0) item.setIssuedRev(drawingDetails2.get(0).getRevNo());

                            //判断是不是升版
                            if (!data2.getProcess().contains("_U")) {
                                List<BpmActTask> drawingDesign = bpmActTaskRepository.findByActInstIdAndTaskDefKey(data2.getId(), "usertask-USER-DRAWING-DESIGN");
                                if (!drawingDesign.isEmpty())
                                    item.setIssuedDesigner(drawingDesign.get(0).getOperatorName());
                            } else {
                                //是升版则去查原版的designer
                                String process = data2.getProcess().substring(0, 3);
                                dto.setProcessName(process);
                                List<BpmActivityInstanceDTO> activityInstanceDTOs = activityTaskService.actInstList(orgId, projectId, dto).getContent();
                                if (!activityInstanceDTOs.isEmpty()) {
                                    List<BpmActTask> drawingDesign = bpmActTaskRepository.findByActInstIdAndTaskDefKey(activityInstanceDTOs.get(0).getId(), "usertask-USER-DRAWING-DESIGN");
                                    if (!drawingDesign.isEmpty())
                                        item.setIssuedDesigner(drawingDesign.get(0).getOperatorName());
                                }
                            }

                        }
                        item.setCurrentProcess(data.getProcessStage() + "-" + data.getProcess() + "-" + data.getCurrentTaskNode());
                        item.setCurrentExecutor(data.getCurrentExecutor());

                    }
                }
            }

            //新增delay相关字段
            //1.Plan Date, Forecast Date
            List<WBSEntityEntryDate> wbsEntityEntryDates = wbsEntityEntryDateRepository.findByProjectIdAndEntityId(projectId, item.getId());
            for (WBSEntityEntryDate wbsEntityEntryDate : wbsEntityEntryDates) {
                Optional<BpmProcess> optional = bpmProcessRepository.findById(wbsEntityEntryDate.getProcessId());
                if (optional.isPresent()) {
                    switch (optional.get().getNameEn()) {
                        case "IDC":
                            item.setPlanStartDate(wbsEntityEntryDate.getPlanStartDate());
                            item.setPlanIDCDate(wbsEntityEntryDate.getPlanEndDate());
                            item.setForecastStartDate(wbsEntityEntryDate.getForecastStartDate());
                            item.setForecastIDCDate(wbsEntityEntryDate.getForecastEndDate());
                            break;
                        case "IFR":
                            item.setPlanIFRDate(wbsEntityEntryDate.getPlanEndDate());
                            item.setForecastIFRDate(wbsEntityEntryDate.getForecastEndDate());
                            break;
                        case "IFU":
                            item.setPlanIFUDate(wbsEntityEntryDate.getPlanEndDate());
                            item.setForecastIFUDate(wbsEntityEntryDate.getForecastEndDate());
                            break;
                        case "IFA":
                            item.setPlanIFADate(wbsEntityEntryDate.getPlanEndDate());
                            item.setForecastIFADate(wbsEntityEntryDate.getForecastEndDate());
                            break;
                        case "AFC":
                            item.setPlanAFCDate(wbsEntityEntryDate.getPlanEndDate());
                            item.setForecastAFCDate(wbsEntityEntryDate.getForecastEndDate());
                            break;
                        case "IFI":
                            item.setPlanIFIDate(wbsEntityEntryDate.getPlanEndDate());
                            item.setForecastIFIDate(wbsEntityEntryDate.getForecastEndDate());
                            break;
                    }
                }
            }
            //2.Actual Date
            bpmActivityInstanceDTOS.forEach(
                data -> {
                    String process = data.getProcess();
                    // 统一处理实际开始时间
                    if (item.getActualStartDate() == null) {
                        item.setActualStartDate(data.getStartDate());
                    }

                    // 根据流程类型设置实际完成时间
                    if (data.getCurrentTaskNode().equals("RECORD RESULT")) {
                        setDateByProcess(item, process, data.getCurrentTaskNodeDate());
                    } else if (data.getFinishState() == ActInstFinishState.FINISHED) {
                        setDateByProcess(item, process, data.getEndDate());
                    }
                }
            );

            //3.Delay Date

            item.setDelayPlanStart(
                item.getPlanStartDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getPlanStartDate(), item.getActualStartDate() != null ? item.getActualStartDate() : new Date()))
                    : (item.getActualStartDate() != null ? formatDateToString(item.getActualStartDate()) : "- -")
            );

            item.setDelayForecastStart(
                item.getForecastStartDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getForecastStartDate(), item.getActualStartDate() != null ? item.getActualStartDate() : new Date()))
                    : (item.getActualStartDate() != null ? formatDateToString(item.getActualStartDate()) : "- -")
            );

            item.setDelayPlanIDC(
                item.getPlanIDCDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getPlanIDCDate(), item.getActualIDCDate() != null ? item.getActualIDCDate() : new Date()))
                    : (item.getActualIDCDate() != null ? formatDateToString(item.getActualIDCDate()) : "- -")
            );

            item.setDelayForecastIDC(
                item.getForecastIDCDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getForecastIDCDate(), item.getActualIDCDate() != null ? item.getActualIDCDate() : new Date()))
                    : (item.getActualIDCDate() != null ? formatDateToString(item.getActualIDCDate()) : "- -")
            );

            item.setDelayPlanIFR(
                item.getPlanIFRDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getPlanIFRDate(), item.getActualIFRDate() != null ? item.getActualIFRDate() : new Date()))
                    : (item.getActualIFRDate() != null ? formatDateToString(item.getActualIFRDate()) : "- -")
            );

            item.setDelayForecastIFR(
                item.getForecastIFRDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getForecastIFRDate(), item.getActualIFRDate() != null ? item.getActualIFRDate() : new Date()))
                    : (item.getActualIFRDate() != null ? formatDateToString(item.getActualIFRDate()) : "- -")
            );

            item.setDelayPlanIFU(
                item.getPlanIFUDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getPlanIFUDate(), item.getActualIFUDate() != null ? item.getActualIFUDate() : new Date()))
                    : (item.getActualIFUDate() != null ? formatDateToString(item.getActualIFUDate()) : "- -")
            );

            item.setDelayForecastIFU(
                item.getForecastIFUDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getForecastIFUDate(), item.getActualIFUDate() != null ? item.getActualIFUDate() : new Date()))
                    : (item.getActualIFUDate() != null ? formatDateToString(item.getActualIFUDate()) : "- -")
            );

            item.setDelayPlanIFA(
                item.getPlanIFADate() != null ?
                    String.valueOf(calculateDaysBetween(item.getPlanIFADate(), item.getActualIFADate() != null ? item.getActualIFADate() : new Date()))
                    : (item.getActualIFADate() != null ? formatDateToString(item.getActualIFADate()) : "- -")
            );

            item.setDelayForecastIFA(
                item.getForecastIFADate() != null ?
                    String.valueOf(calculateDaysBetween(item.getForecastIFADate(), item.getActualIFADate() != null ? item.getActualIFADate() : new Date()))
                    : (item.getActualIFADate() != null ? formatDateToString(item.getActualIFADate()) : "- -")
            );

            item.setDelayPlanAFC(
                item.getPlanAFCDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getPlanAFCDate(), item.getActualAFCDate() != null ? item.getActualAFCDate() : new Date()))
                    : (item.getActualAFCDate() != null ? formatDateToString(item.getActualAFCDate()) : "- -")
            );

            item.setDelayForecastAFC(
                item.getForecastAFCDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getForecastAFCDate(), item.getActualAFCDate() != null ? item.getActualAFCDate() : new Date()))
                    : (item.getActualAFCDate() != null ? formatDateToString(item.getActualAFCDate()) : "- -")
            );

            item.setDelayPlanIFI(
                item.getPlanIFIDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getPlanIFIDate(), item.getActualIFIDate() != null ? item.getActualIFIDate() : new Date()))
                    : (item.getActualIFIDate() != null ? formatDateToString(item.getActualIFIDate()) : "- -")
            );

            item.setDelayForecastIFI(
                item.getForecastIFIDate() != null ?
                    String.valueOf(calculateDaysBetween(item.getForecastIFIDate(), item.getActualIFIDate() != null ? item.getActualIFIDate() : new Date()))
                    : (item.getActualIFIDate() != null ? formatDateToString(item.getActualIFIDate()) : "- -")
            );


        }
        EasyExcel.write(filePathDown).registerConverter(new LocalDateConverter()).registerConverter(new EntityStatusConverter()).withTemplate(templateFilePathDown).sheet().doFill(drawingListPipings);
        return excel;

    }

    // 新增的辅助方法
    private void setDateByProcess(DrawingWorkHourDTO item, String process, Date date) {
        if (process.startsWith("IDC")) {
            item.setActualIDCDate(date);
        } else if (process.startsWith("IFR")) {
            item.setActualIFRDate(date);
        } else if (process.startsWith("IFU")) {
            item.setActualIFUDate(date);
        } else if (process.startsWith("IFA")) {
            item.setActualIFADate(date);
        } else if (process.startsWith("AFC")) {
            item.setActualAFCDate(date);
        } else if (process.startsWith("IFI")) {
            item.setActualIFIDate(date);
        }
    }

    public static long calculateDaysBetween(Date date1, Date date2) {
        // 将 Date 类型转换为 LocalDate 类型
        LocalDate localDate1 = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 计算天数差
        return ChronoUnit.DAYS.between(localDate1, localDate2);
    }

    public static String formatDateToString(Date date) {
        // 定义日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 将 Date 转为指定格式的字符串
        return sdf.format(date);
    }

    @Override
    public Page<? extends Drawing> search(Long orgId, Long projectId, WBSEntryCriteriaBaseDTO criteriaDTO, PageDTO pageDTO) {
        DrawingCriteriaDTO crtDTO = (DrawingCriteriaDTO) criteriaDTO;
        Page<DrawingWorkHourDTO> result = drawingRepository.getList(orgId, projectId, pageDTO, crtDTO);
        return result;
    }

    @Override
    public Drawing get(Long orgId, Long projectId, Long entityId) {
        return drawingRepository.findById(entityId).orElse(null);
    }

    @Override
    public void delete(OperatorDTO operator, Long orgId, Project project, Long entityId) {

    }

    @Override
    public void insert(OperatorDTO operator, Long orgId, Long projectId, Drawing entity) {

    }

    @Override
    public Drawing update(OperatorDTO operator, Long orgId, Long projectId, Drawing entity) {
        return null;
    }

    @Override
    public boolean existsByEntityNo(String entityNO, Long projectId) {
        return false;
    }

    @Override
    public File saveDownloadFile(Long orgId, Long projectId, WBSEntryCriteriaBaseDTO criteriaDTO, Long operatorId) {
        return null;
    }

    @Override
    public DrawingDetail getLatestDetail(Long projectId, Long id) {
        return drawingDetailRepository.findFirstByProjectIdAndDrawingIdAndStatusOrderByIdDesc(projectId, id, EntityStatus.PENDING);
    }

    @Override
    public DrawingDelegateDTO getDrawingDelegate(Long orgId, Long projectId, Long drawingId) {
        DrawingDelegateDTO drawingDelegateDTO = new DrawingDelegateDTO();
        String design = drawingEntryDelegateRepository.findNameByDrawingIdAndPrivilegeAndStatus(
            drawingId,
            UserPrivilege.DESIGN_ENGINEER_EXECUTE.name(),
            EntityStatus.ACTIVE.toString());
        if (design != null && !"".equals(design)) {
            drawingDelegateDTO.setDesign(design);
        }
        String review = drawingEntryDelegateRepository.findNameByDrawingIdAndPrivilegeAndStatus(
            drawingId,
            UserPrivilege.DRAWING_REVIEW_EXECUTE.name(),
            EntityStatus.ACTIVE.toString());
        if (review != null && !"".equals(review)) {
            drawingDelegateDTO.setReview(review);
        }
        String approve = drawingEntryDelegateRepository.findNameByDrawingIdAndPrivilegeAndStatus(drawingId,
            UserPrivilege.DRAWING_APPROVE_EXECUTE.name(),
            EntityStatus.ACTIVE.toString());
        if (approve != null && !"".equals(approve)) {
            drawingDelegateDTO.setApprove(approve);
        }
        return drawingDelegateDTO;
    }

    /**
     * @param projectId
     * @return
     */
    public List<Map<String, Object>> getDisciplineTreeData(Long orgId, Long projectId) {

        final Project project = projectService.get(orgId, projectId);
        List<String> disciplineList = drawingRepository.findByProjectIdGroupByDiscipline(projectId);

        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> root = new HashMap<>();
        root.put("id", projectId);
        root.put("projectId", "0");
        root.put("name", project.getName());
        root.put("isParent", true);
        root.put("level", 0);
        mapList.add(root);
        for (String discipline : disciplineList) {
            Map<String, Object> secondNode = new HashMap<>();
            secondNode.put("id", discipline);
            secondNode.put("projectId", projectId);
            secondNode.put("name", discipline);
            secondNode.put("isParent", false);
            secondNode.put("level", 1);
            mapList.add(secondNode);

        }
        return mapList;
    }

    public List<Map<String, Object>> getTreeXData(Long orgId, Long projectId) {

        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> orgList = drawingRepository.findOrgInfoByOrgId("%" + orgId.toString() + "%");
        if (orgList.size() > 0) {
            for (Map<String, Object> org : orgList) {
                // 创建一个新的 HashMap，将 TupleBackedMap 内容复制进去
                Map<String, Object> modifiableMap = new HashMap<>(org);
                modifiableMap.put("departmentId", org.get("departmentId"));
                modifiableMap.put("departmentName", org.get("departmentName"));
                Map<String, Object> userMap = drawingRepository.findUserInfoByOrgId(org.get("departmentId").toString());
                if (userMap != null) {
                    modifiableMap.put("userId", userMap.get("id"));
                    modifiableMap.put("userName", userMap.get("name"));
                }
                list.add(modifiableMap);

            }
        }

        return list;
    }

    public List<Map<String, Object>> getTreeYData(Long orgId, Long projectId, String discipline, String level) {

        List<Map<String, Object>> mapList = new ArrayList<>();
        List<Drawing> drawings;
        if (level.equals("0")) {
            // 整个项目的数据
            drawings = drawingRepository.findByOrgIdAndProjectIdAndStatus(orgId, projectId, EntityStatus.ACTIVE);
        } else {
            // 查询专业下的图纸信息
            drawings = drawingRepository.findByProjectIdAndDisciplineAndStatus(projectId, discipline, EntityStatus.ACTIVE);
        }
        for (Drawing drawing : drawings) {
            Map<String, Object> drawingInfo = new HashMap<>();
            drawingInfo.put("drawingId", drawing.getId());
            drawingInfo.put("drawingName", drawing.getDwgNo());
            drawingInfo.put("displayName", drawing.getDocumentTitle());
            drawingInfo.put("discipline", drawing.getDiscipline());
            mapList.add(drawingInfo);
        }
        return mapList;
    }

}
