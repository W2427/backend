package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.OperatorDTO;
import com.ose.service.EntityInterface;
import com.ose.tasks.dto.drawing.*;
import com.ose.tasks.entity.bpm.BpmEntitySubType;
import com.ose.tasks.entity.drawing.DrawingComment;
import com.ose.tasks.entity.drawing.DrawingCommentResponse;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.vo.RelationReturnEnum;
import org.springframework.data.domain.Page;

import java.io.File;
import java.util.List;

/**
 * 实体管理service接口
 */
public interface DrawingCommentInterface extends EntityInterface {

    /**
     * 根据实体类型ID查询条形码评论
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param criteriaDTO
     * @return
     */
    Page<DrawingComment> getList(
        Long orgId,
        Long projectId,
        DrawingCommentCriteriaDTO criteriaDTO
    );

    /**
     * 创建评论
     *
     * @param commentDTO 评论信息
     * @param projectId  项目id
     * @param orgId      组织id
     */
    DrawingComment create(
        Long projectId,
        Long orgId,
        DrawingCommentDTO commentDTO,
        OperatorDTO operator
    );


    /**
     * 修改条形码评论
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param id
     * @param commentDTO
     * @return
     */
    DrawingComment update(
        Long orgId,
        Long projectId,
        Long id,
        DrawingCommentDTO commentDTO
    );

    /**
     * 删除评论
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     */
    void deleteDrawingComment(
        Long orgId,
        Long projectId,
        Long id
    );

    /**
     * 关闭评论
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     */
    void close(
        Long orgId,
        Long projectId,
        Long id
    );

    /**
     * 开启评论
     *
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param id
     */
    void open(
        Long orgId,
        Long projectId,
        Long id
    );

    /**
     * 查询评论详细信息
     *
     * @param id        工序id
     * @param orgId     组织id
     * @param projectId 项目id
     */
    DrawingComment get(Long id, Long projectId, Long orgId);

    /**
     * 创建评论回复
     *
     * @param responseDTO 评论信息
     * @param projectId   项目id
     * @param orgId       组织id
     */
    DrawingCommentResponse createResponse(
        Long projectId,
        Long orgId,
        Long commentId,
        DrawingCommentResponseDTO responseDTO,
        OperatorDTO operator
    );

    /**
     * 创建评论回复
     *
     * @param responseDTO 评论信息
     * @param projectId   项目id
     * @param orgId       组织id
     */
    DrawingCommentResponse createResponseToResponse(
        Long projectId,
        Long orgId,
        Long responseId,
        DrawingCommentResponseDTO responseDTO,
        OperatorDTO operator
    );


    /**
     * 根据实体类型ID查询条形码评论
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param drawingCommentId
     * @return
     */
    List<DrawingCommentResponse> getResponseList(
        Long orgId,
        Long projectId,
        Long drawingCommentId
    );


    /**
     * 根据实体类型ID查询条形码评论
     *
     * @param orgId       组织ID
     * @param projectId   项目ID
     * @param responseId
     * @return
     */
    List<DrawingCommentResponse> getResponseListByResponse(
        Long orgId,
        Long projectId,
        Long responseId
    );


    /**
     * 修改条形码评论回复
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param id
     * @param dto
     * @return
     */
    DrawingCommentResponse updateResponse(
        Long orgId,
        Long projectId,
        Long id,
        DrawingCommentResponseDTO dto
    );


    /**
     * 修改条形码评论回复
     *
     * @param orgId      组织ID
     * @param projectId  项目ID
     * @param id
     * @return
     */
    DrawingCommentResponse deleteResponse(
        Long orgId,
        Long projectId,
        Long id
    );
//    File saveDownloadFile(Long orgId, Long projectId, Long drawingCoordinateId, Long operatorId);
}
