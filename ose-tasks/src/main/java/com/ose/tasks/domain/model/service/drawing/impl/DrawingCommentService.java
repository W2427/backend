package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.auth.api.UserFeignAPI;
import com.ose.auth.entity.UserProfile;
import com.ose.docs.api.UploadFeignAPI;
import com.ose.docs.dto.FilePostDTO;
import com.ose.docs.entity.FileES;
import com.ose.dto.BaseDTO;
import com.ose.dto.OperatorDTO;
import com.ose.exception.BusinessError;
import com.ose.exception.NotFoundError;
import com.ose.response.JsonObjectResponseBody;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.*;
import com.ose.tasks.domain.model.repository.bpm.*;
import com.ose.tasks.domain.model.repository.drawing.DrawingDetailRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingCommentInterface;
import com.ose.tasks.domain.model.service.drawing.DrawingCoordinateInterface;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.RatedTime;
import com.ose.tasks.entity.RatedTimeCriterion;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.bpm.BpmEntityTypeCoordinateRelation;
import com.ose.tasks.entity.bpm.BpmHiTaskinst;
import com.ose.tasks.entity.drawing.*;
import com.ose.tasks.vo.RelationReturnEnum;
import com.ose.util.BeanUtils;
import com.ose.util.LongUtils;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;

@Component
public class DrawingCommentService extends StringRedisService implements DrawingCommentInterface {
    private final static Logger logger = LoggerFactory.getLogger(DrawingCommentService.class);

    /**
     * 工序  操作仓库
     */
    private final BpmProcessRepository processRepository;
    /**
     * 实体类型-坐标  操作仓库
     */
    private final EntitySubTypeCoordinateRelationRepository relationRepository;
    /**
     * 实体类型 操作仓库
     */
    private final BpmEntitySubTypeRepository entitySubTypeRepository;

    private final BpmEntityTypeRepository entityTypeRepository;

    private final BpmEntitySubTypeCheckListRepository bpmEntitySubTypeCheckListRepository;

    private final DrawingSignatureCoordinateRepository drawingSignatureCoordinateRepository;

    private final DrawingCoordinateRepository drawingCoordinateRepository;

    private final DrawingCommentRepository drawingCommentRepository;

    private final DrawingCommentResponseRepository drawingCommentResponseRepository;

    private final DrawingRepository drawingRepository;

    private final DrawingDetailRepository drawingDetailRepository;

    private final ProjectRepository projectRepository;

    private final BpmHiTaskinstRepository hiTaskinstRepository;

    private final UploadFeignAPI uploadFeignAPI;

    private final UserFeignAPI userFeignAPI;


    @Value("${application.files.protected}")
    private String protectedDir;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingCommentService(
        BpmProcessRepository processRepository,
        EntitySubTypeCoordinateRelationRepository relationRepository,
        BpmEntitySubTypeRepository entitySubTypeRepository,
        BpmEntityTypeRepository entityTypeRepository,
        BpmEntitySubTypeCheckListRepository bpmEntitySubTypeCheckListRepository,
        DrawingSignatureCoordinateRepository drawingSignatureCoordinateRepository,
        DrawingCoordinateRepository drawingCoordinateRepository,
        DrawingCommentRepository drawingCommentRepository,
        DrawingRepository drawingRepository,
        DrawingDetailRepository drawingDetailRepository,
        DrawingCommentResponseRepository drawingCommentResponseRepository,
        ProjectRepository projectRepository,
        BpmHiTaskinstRepository hiTaskinstRepository,
        StringRedisTemplate stringRedisTemplate,
        UserFeignAPI userFeignAPI,
        @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") UploadFeignAPI uploadFeignAPI) {
        super(stringRedisTemplate);
        this.processRepository = processRepository;
        this.relationRepository = relationRepository;
        this.entitySubTypeRepository = entitySubTypeRepository;
        this.entityTypeRepository = entityTypeRepository;
        this.bpmEntitySubTypeCheckListRepository = bpmEntitySubTypeCheckListRepository;
        this.drawingSignatureCoordinateRepository = drawingSignatureCoordinateRepository;
        this.drawingCoordinateRepository = drawingCoordinateRepository;
        this.uploadFeignAPI = uploadFeignAPI;
        this.drawingCommentRepository = drawingCommentRepository;
        this.drawingDetailRepository = drawingDetailRepository;
        this.drawingRepository = drawingRepository;
        this.drawingCommentResponseRepository = drawingCommentResponseRepository;
        this.projectRepository = projectRepository;
        this.hiTaskinstRepository = hiTaskinstRepository;
        this.userFeignAPI = userFeignAPI;
    }

    @Override
    public Page<DrawingComment> getList(
        Long orgId,
        Long projectId,
        DrawingCommentCriteriaDTO criteriaDTO
    ) {
        return drawingCommentRepository.search(orgId, projectId, criteriaDTO);
    }

    @Override
    public DrawingComment create(Long orgId, Long projectId, DrawingCommentDTO commentDTO, OperatorDTO operator) {
        DrawingComment drawingComment = BeanUtils.copyProperties(commentDTO, new DrawingComment());
        drawingComment.setOrgId(orgId);
        drawingComment.setProjectId(projectId);
        drawingComment.setStatus(EntityStatus.ACTIVE);
        drawingComment.setCreatedAt();
        drawingComment.setUser(operator.getName());
        drawingComment.setUserId(operator.getId());
        drawingComment.setReplies(0);
        Optional<Project> project = projectRepository.findById(projectId);
        drawingComment.setProjectName(project.get().getName());

        Optional<Drawing> optionalDrawing = drawingRepository.findById(commentDTO.getDrawingId());
        if (optionalDrawing.isPresent()) {
            drawingComment.setDrawing(optionalDrawing.get());
        }

        List<DrawingDetail> drawingDetails = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(commentDTO.getDrawingId(), commentDTO.getProcInstId());

        if (!drawingDetails.isEmpty()){
            Optional<DrawingDetail> optionalDrawingDetail = drawingDetailRepository.findById(drawingDetails.get(0).getId());
            if (optionalDrawingDetail.isPresent()) {
                drawingComment.setDrawingDetail(optionalDrawingDetail.get());
            }

            //查询该图纸图纸设计节点人员
            if (drawingDetails.get(0).getActInsId() != null){
               BpmHiTaskinst bpmHiTaskinst = hiTaskinstRepository.findFirstByActInstIdAndCategoryOrderByEndTimeAsc(drawingDetails.get(0).getActInsId(), "DESIGN_ENGINEER_EXECUTE");
                if (bpmHiTaskinst != null){
                    drawingComment.setDesignerId(Long.valueOf(bpmHiTaskinst.getOperator()));
                    UserProfile data = userFeignAPI.get(Long.valueOf(bpmHiTaskinst.getAssignee())).getData();
                    drawingComment.setDesigner(data.getName());
                }
            }
        }

//        if (coordinateDTO.getFileName() != null && !coordinateDTO.getFileName().isEmpty()) {
//            logger.error("坐标 保存docs服务->开始");
//            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), coordinateDTO.getFileName(),
//                new FilePostDTO());
//            logger.error("坐标 保存docs服务->结束");
//            FileES fileES = fileESResBody.getData();
//            drawingCoordinate.setFileId(LongUtils.parseLong(fileES.getId()));
//            drawingCoordinate.setFilePath(fileES.getPath());
//            drawingCoordinate.setFileName(fileES.getName());
//        }

        return drawingCommentRepository.save(drawingComment);
    }

    @Override
    public DrawingComment update(
        Long orgId,
        Long projectId,
        Long id,
        DrawingCommentDTO commentDTO
    ) {
        Optional<DrawingComment> optional = drawingCommentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundError();
        }
        DrawingComment drawingComment = optional.get();
        drawingComment.setContent(commentDTO.getContent());
        drawingComment.setLastModifiedAt(new Date());

        Optional<Drawing> optionalDrawing = drawingRepository.findById(commentDTO.getDrawingId());
        if (optionalDrawing.isPresent()) {
            drawingComment.setDrawing(optionalDrawing.get());
        }

        List<DrawingDetail> drawingDetails = drawingDetailRepository.findByDrawingIdAndActInsIdOrderByCreatedAt(commentDTO.getDrawingId(), commentDTO.getProcInstId());

        if (!drawingDetails.isEmpty()){
            Optional<DrawingDetail> optionalDrawingDetail = drawingDetailRepository.findById(drawingDetails.get(0).getId());
            if (optionalDrawingDetail.isPresent()) {
                drawingComment.setDrawingDetail(optionalDrawingDetail.get());
            }
        }

//        if (coordinateDTO.getFileName() != null && !coordinateDTO.getFileName().isEmpty()) {
//            logger.error("坐标 保存docs服务->开始");
//            JsonObjectResponseBody<FileES> fileESResBody = uploadFeignAPI.save(orgId.toString(), projectId.toString(), coordinateDTO.getFileName(),
//                new FilePostDTO());
//            logger.error("坐标 保存docs服务->结束");
//            FileES fileES = fileESResBody.getData();
//            coordinate.setFileId(LongUtils.parseLong(fileES.getId()));
//            coordinate.setFilePath(fileES.getPath());
//        }

        return drawingCommentRepository.save(drawingComment);
    }

    @Override
    public void deleteDrawingComment(
        Long orgId,
        Long projectId,
        Long id
    ) {
        Optional<DrawingComment> optional = drawingCommentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundError();
        }
        DrawingComment comment = optional.get();
        comment.setStatus(EntityStatus.DELETED);
        comment.setLastModifiedAt();
        drawingCommentRepository.save(comment);
    }

    @Override
    public void close(
        Long orgId,
        Long projectId,
        Long id
    ) {
        Optional<DrawingComment> optional = drawingCommentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundError();
        }
        DrawingComment comment = optional.get();
        comment.setStatus(EntityStatus.CLOSED);
        comment.setLastModifiedAt();

        //级联关闭下属回复数据
        List<DrawingCommentResponse> responses = drawingCommentResponseRepository.findByOrgIdAndProjectIdAndDrawingCommentIdAndStatus(orgId, projectId, comment.getId(), EntityStatus.ACTIVE);
        for (DrawingCommentResponse response : responses){
            this.closeResponse(orgId, projectId, response.getId());
            response.setStatus(EntityStatus.CLOSED);
            response.setLastModifiedAt();
            drawingCommentResponseRepository.save(response);
        }

        drawingCommentRepository.save(comment);
    }

    private void closeResponse(Long orgId,Long projectId ,Long responseId){
        List<DrawingCommentResponse> commentResponses = drawingCommentResponseRepository.findByOrgIdAndProjectIdAndDrawingCommentResponseId(orgId, projectId, responseId);
        for (DrawingCommentResponse response : commentResponses){
            List<DrawingCommentResponse> responses = drawingCommentResponseRepository.findByOrgIdAndProjectIdAndDrawingCommentResponseId(orgId, projectId, responseId);
            if (!responses.isEmpty()){
                for (DrawingCommentResponse data : responses){
                    this.closeResponse(orgId, projectId, data.getId());
                }
            }
            response.setStatus(EntityStatus.CLOSED);
            response.setLastModifiedAt();
            drawingCommentResponseRepository.save(response);
        }
    }

    @Override
    public void open(
        Long orgId,
        Long projectId,
        Long id
    ) {
        Optional<DrawingComment> optional = drawingCommentRepository.findById(id);
        if (!optional.isPresent()) {
            throw new NotFoundError();
        }
        DrawingComment comment = optional.get();
        comment.setStatus(EntityStatus.ACTIVE);
        comment.setLastModifiedAt();
        drawingCommentRepository.save(comment);
    }

    /**
     * 查询评论详细信息。
     *
     * @param id        坐标id
     * @param orgId     组织id
     * @param projectId 项目id
     */
    @Override
    public DrawingComment get(Long id, Long projectId, Long orgId) {
        Optional<DrawingComment> commentOptional = drawingCommentRepository.findById(id);
        if (commentOptional.isPresent()) {
            DrawingComment result = commentOptional.get();
            return result;
        }
        return null;
    }

    @Override
    public DrawingCommentResponse createResponse(Long orgId, Long projectId, Long commentId, DrawingCommentResponseDTO responseDTO, OperatorDTO operator) {
        DrawingCommentResponse drawingCommentResponse = BeanUtils.copyProperties(responseDTO, new DrawingCommentResponse());
        drawingCommentResponse.setOrgId(orgId);
        drawingCommentResponse.setProjectId(projectId);
        drawingCommentResponse.setStatus(EntityStatus.ACTIVE);
        drawingCommentResponse.setCreatedAt();
        drawingCommentResponse.setUser(operator.getName());
        drawingCommentResponse.setUserId(operator.getId());
        drawingCommentResponse.setReplies(0);
        Optional<Project> project = projectRepository.findById(projectId);
        drawingCommentResponse.setProjectName(project.get().getName());

        Optional<DrawingComment> commentOptional = drawingCommentRepository.findById(commentId);
        if (commentOptional.isPresent()) {
            DrawingComment comment = commentOptional.get();
            comment.setReplies((comment.getReplies() != null ? comment.getReplies() : 0 )+ 1);
            drawingCommentRepository.save(comment);
            drawingCommentResponse.setDrawingComment(comment);
        }

        return drawingCommentResponseRepository.save(drawingCommentResponse);
    }

    @Override
    public DrawingCommentResponse createResponseToResponse(Long orgId, Long projectId, Long responseId, DrawingCommentResponseDTO responseDTO, OperatorDTO operator) {
        DrawingCommentResponse drawingCommentResponse = BeanUtils.copyProperties(responseDTO, new DrawingCommentResponse());
        drawingCommentResponse.setOrgId(orgId);
        drawingCommentResponse.setProjectId(projectId);
        drawingCommentResponse.setStatus(EntityStatus.ACTIVE);
        drawingCommentResponse.setCreatedAt();
        drawingCommentResponse.setUser(operator.getName());
        drawingCommentResponse.setUserId(operator.getId());
        drawingCommentResponse.setReplies(0);
        Optional<Project> project = projectRepository.findById(projectId);
        drawingCommentResponse.setProjectName(project.get().getName());

        Optional<DrawingCommentResponse> optional = drawingCommentResponseRepository.findById(responseId);
        if (optional.isPresent()) {
            DrawingCommentResponse response = optional.get();
            response.setReplies(response.getReplies() + 1);
            // 设置 parentResponse 字段
            drawingCommentResponse.setParentResponse(response);

            // 添加子评论到父评论的列表中
            response.getDrawingCommentResponses().add(drawingCommentResponse);

            // 先保存子评论
            drawingCommentResponseRepository.save(drawingCommentResponse);

            // 再保存父评论
            drawingCommentResponseRepository.save(response);
        }

        return drawingCommentResponse;
    }

    @Override
    public List<DrawingCommentResponse> getResponseList(
        Long orgId,
        Long projectId,
        Long drawingCommentId
    ) {
        return drawingCommentResponseRepository.findByOrgIdAndProjectIdAndDrawingComment(orgId, projectId, drawingCommentId);
    }

    @Override
    public List<DrawingCommentResponse> getResponseListByResponse(
        Long orgId,
        Long projectId,
        Long responseId
    ) {
        return drawingCommentResponseRepository.findByOrgIdAndProjectIdAndDrawingCommentResponseId(orgId, projectId, responseId);
    }

    @Override
    public DrawingCommentResponse updateResponse(Long orgId, Long projectId, Long id, DrawingCommentResponseDTO dto) {
        Optional<DrawingCommentResponse> optional = drawingCommentResponseRepository.findById(id);

        if (!optional.isPresent()) {
            throw new NotFoundError();
        }
        DrawingCommentResponse response = BeanUtils.copyProperties(dto, optional.get());
        response.setLastModifiedAt(new Date());

        return drawingCommentResponseRepository.save(response);
    }

    @Override
    public DrawingCommentResponse deleteResponse(Long orgId, Long projectId, Long id) {
        Optional<DrawingCommentResponse> optional = drawingCommentResponseRepository.findById(id);

        if (!optional.isPresent()) {
            throw new NotFoundError();
        }
        DrawingCommentResponse response = optional.get();
        response.setStatus(EntityStatus.DELETED);
        response.setLastModifiedAt(new Date());

        return drawingCommentResponseRepository.save(response);
    }

//    @Override
//    public File saveDownloadFile(Long orgId, Long projectId, Long drawingCoordinateId, Long operatorId) {
//        DrawingCoordinate drawingCoordinate = drawingCoordinateRepository.findByIdAndStatus(drawingCoordinateId, EntityStatus.ACTIVE);
//
//        if (drawingCoordinate != null){
//            File coordinateFile = new File(protectedDir, drawingCoordinate.getFilePath());
//            return coordinateFile;
//        }
//
//        return null;
//    }
//
//    /**
//     * 设置返回结果引用数据。
//     *
//     * @param included 引用数据映射表
//     * @param entities 查询结果列表
//     * @param <T>      返回结果类型
//     * @return 引用数据映射表
//     */
//    @Override
//    public <T extends BaseDTO> Map<Long, Object> setIncluded(
//        Map<Long, Object> included,
//        List<T> entities
//    ) {
//
//        if (entities == null || entities.size() == 0) {
//            return included;
//        }
//
//        List<Long> entityCategoryIDs = new ArrayList<>();
//
//        for (T entity : entities) {
//
//            if (entity instanceof RatedTime && ((RatedTime) entity).getEntitySubTypeId() != null) {
//                entityCategoryIDs.add(((RatedTime) entity).getEntitySubTypeId());
//            }
//
//            if (entity instanceof RatedTimeCriterion && ((RatedTimeCriterion) entity).getEntitySubTypeId() != null) {
//                entityCategoryIDs.add(((RatedTimeCriterion) entity).getEntitySubTypeId());
//            }
//        }
//
//        List<BpmEntitySubType> entityCategories = entitySubTypeRepository.findByIdIn(entityCategoryIDs);
//
//        for (BpmEntitySubType entitySubType : entityCategories) {
//            included.put(entitySubType.getId(), entitySubType);
//        }
//
//        return included;
//    }

}
