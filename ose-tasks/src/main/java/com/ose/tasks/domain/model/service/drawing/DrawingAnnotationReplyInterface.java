package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.DrawingAnnotationCreateDTO;
import com.ose.tasks.entity.drawing.DrawingAnnotationReply;
import org.springframework.data.domain.Page;

/**
 * service接口
 */
public interface DrawingAnnotationReplyInterface {

    /**
     * 图纸评审意见回复详情
     *
     * @param drawingAnnotationReplyId 图纸意见回复ID
     * @return 图纸评审意见回复
     */
    DrawingAnnotationReply detail(
        Long orgId,
        Long projectId,
        Long drawingAnnotationReplyId
    );

    /**
     * 图纸评审意见回复创建
     *
     * @param drawingAnnotationId 图纸意见ID
     * @return 图纸评审意见回复
     */
    DrawingAnnotationReply create(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        Long drawingAnnotationId,
        DrawingAnnotationCreateDTO drawingAnnotationCreateDTO,
        ContextDTO contextDTO
    );

    /**
     * 图纸评审意见回复列表
     *
     * @param orgId               组织ID
     * @param projectId           项目ID
     * @param drawingAnnotationId 图纸评审意见ID
     * @return 图纸评审意见回复
     */
    Page<DrawingAnnotationReply> search(
        Long orgId,
        Long projectId,
        Long drawingAnnotationId,
        PageDTO pageDTO
    );

    DrawingAnnotationReply delete(
        Long orgId,
        Long projectId,
        Long drawingAnnotationReplyId,
        ContextDTO contextDTO
    );
}
