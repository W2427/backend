package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.drawing.DrawingAnnotationReplyRepository;
import com.ose.tasks.domain.model.repository.drawing.DrawingAnnotationRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingAnnotationReplyInterface;
import com.ose.tasks.dto.drawing.DrawingAnnotationCreateDTO;
import com.ose.tasks.entity.drawing.DrawingAnnotation;
import com.ose.tasks.entity.drawing.DrawingAnnotationReply;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component

public class DrawingAnnotationReplyService implements DrawingAnnotationReplyInterface {

    private final static Logger logger = LoggerFactory.getLogger(DrawingAnnotationReplyService.class);

    private final DrawingAnnotationReplyRepository drawingAnnotationReplyRepository;
    private final DrawingAnnotationRepository drawingAnnotationRepository;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingAnnotationReplyService(
        DrawingAnnotationReplyRepository drawingAnnotationReplyRepository,
        DrawingAnnotationRepository drawingAnnotationRepository
    ) {
        this.drawingAnnotationReplyRepository = drawingAnnotationReplyRepository;
        this.drawingAnnotationRepository = drawingAnnotationRepository;
    }

    /**
     * 图纸评审意见回复详情
     *
     * @param drawingAnnotationReplyId 图纸意见回复ID
     * @return 图纸评审意见
     */
    @Override
    public DrawingAnnotationReply detail(Long orgId, Long projectId, Long drawingAnnotationReplyId) {
        return drawingAnnotationReplyRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            drawingAnnotationReplyId,
            EntityStatus.ACTIVE
        );
    }

    /**
     * 图纸评审意见回复创建
     *
     * @param drawingAnnotationId 图纸意见ID
     * @return 图纸评审意见
     */
    @Override
    public DrawingAnnotationReply create(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        Long drawingAnnotationId,

        DrawingAnnotationCreateDTO drawingAnnotationCreateDTO,
        ContextDTO contextDTO
    ) {
        DrawingAnnotation drawingAnnotation = drawingAnnotationRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            drawingAnnotationId,
            EntityStatus.ACTIVE
        );

        if (null == drawingAnnotation) {
            throw new BusinessError("drawingAnnotation should not be null");
        }
        DrawingAnnotationReply drawingAnnotationReply = new DrawingAnnotationReply();
        BeanUtils.copyProperties(drawingAnnotationCreateDTO, drawingAnnotationReply);
        drawingAnnotationReply.setOrgId(orgId);
        drawingAnnotationReply.setProjectId(projectId);
        drawingAnnotationReply.setDrawingAnnotationId(drawingAnnotationId);

        drawingAnnotationReply.setStatus(EntityStatus.ACTIVE);
        drawingAnnotationReply.setCreatedAt(new Date());
        drawingAnnotationReply.setCreatedBy(contextDTO.getOperator().getId());
        drawingAnnotationReply.setLastModifiedAt(new Date());
        drawingAnnotationReply.setLastModifiedBy(contextDTO.getOperator().getId());

        drawingAnnotationReplyRepository.save(drawingAnnotationReply);
        return drawingAnnotationReply;
    }

    /**
     * 图纸评审意见回复列表
     *
     * @param orgId               组织ID
     * @param projectId           项目ID
     * @param drawingAnnotationId 图纸意见ID
     * @return 图纸评审意见回复
     */
    @Override
    public Page<DrawingAnnotationReply> search(
        Long orgId,
        Long projectId,
        Long drawingAnnotationId,
        PageDTO pageDTO
    ) {
        return drawingAnnotationReplyRepository.findByOrgIdAndProjectIdAndDrawingAnnotationIdAndStatusOrderByCreatedAtDesc(
            orgId,
            projectId,
            drawingAnnotationId,
            EntityStatus.ACTIVE,
            pageDTO.toPageable()
        );
    }

    /**
     * 图纸评审意见详情
     *
     * @param drawingAnnotationReplyId 图纸评审意见ID
     * @return 图纸评审意见
     */
    @Override
    public DrawingAnnotationReply delete(
        Long orgId,
        Long projectId,
        Long drawingAnnotationReplyId,
        ContextDTO contextDTO
    ) {
        DrawingAnnotationReply drawingAnnotationReply =
            drawingAnnotationReplyRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                orgId,
                projectId,
                drawingAnnotationReplyId,
                EntityStatus.ACTIVE
            );

        drawingAnnotationReply.setLastModifiedAt(new Date());
        drawingAnnotationReply.setLastModifiedBy(contextDTO.getOperator().getId());
        drawingAnnotationReply.setDeletedAt(new Date());
        drawingAnnotationReply.setDeletedBy(contextDTO.getOperator().getId());
        drawingAnnotationReply.setDeleted(true);
        drawingAnnotationReply.setStatus(EntityStatus.DELETED);
        drawingAnnotationReplyRepository.save(drawingAnnotationReply);

        return drawingAnnotationReply;
    }
}
