package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.OperatorDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.SubDrawingReviewOpinion;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * service接口
 */
public interface ProofreadInterface {

    /**
     * 查询登陆用户相关校审图集
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param userId
     * @param page
     * @param criteriaDTO
     * @return
     */
    Page<DrawingDetail> getProofreadDrawingList(
        Long orgId,
        Long projectId,
        Long userId,
        PageDTO page,
        ProofreadDrawingListCriteriaDTO criteriaDTO
    );

    /**
     * 查询图集任务信息
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId
     * @return
     */
    ProofreadTaskDTO getDrawingTaskInfo(
        Long orgId,
        Long projectId,
        Long drawingId,
        DrawingProofreadSearchDTO drawingProofreadSearchDTO
    );

    /**
     * 子图纸预览
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param subDrawingId
     * @return
     */
    ProofreadSubDrawingPreviewDTO getSubDrawingPreview(
        Long orgId,
        Long projectId,
        Long subDrawingId
    );

    /**
     * 查询子图纸校审意见列表
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param subDrawingId
     * @param taskId
     * @return
     */
    List<SubDrawingReviewOpinion> getSubDrawingOpinionList(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        Long taskId
    );

    /**
     * 创建子图纸校审意见
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param subDrawingId
     * @param taskId
     * @param dto
     * @param userId
     * @return
     */
    SubDrawingReviewOpinion createSubDrawingOpinion(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        Long taskId,
        SubDrawingReviewOpinionDTO dto,
        Long userId
    );

    /**
     * 修改子图纸校审状态
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param subDrawingId
     * @param dto
     */
    void modifyReviewStatus(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        SubDrawingReviewStatusDTO dto
    );

    /**
     * 修改子图纸校审状态
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param dto
     */
    void batchModifyReviewStatus(
        Long orgId,
        Long projectId,
        SubDrawingReviewStatusDTO dto
    );

    /**
     * 上传子图纸
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param drawingId
     * @param uploadDTO
     * @param operator
     * @return
     */
    ProofreadUploadResponseDTO uploadSubDrawing(
        Long orgId,
        Long projectId,
        Long drawingId,
        ProofreadPipingDrawingUploadDTO uploadDTO,
        OperatorDTO operator
    );

    /**
     * 创建校审回复
     *
     * @param orgId        组织ID
     * @param projectId    项目ID
     * @param subDrawingId
     * @param taskId
     * @param opinionId
     * @param dto
     * @param userId
     * @return
     */
    SubDrawingReviewOpinion createOpinionReply(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        Long taskId,
        Long opinionId,
        SubDrawingReviewOpinionDTO dto,
        Long userId
    );
}
