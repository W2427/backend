package com.ose.tasks.domain.model.service.drawing;

import com.ose.dto.AnnotationHistoryResponseDTO;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.DrawingAnnotationCreateDTO;
import com.ose.tasks.entity.drawing.DrawingAnnotation;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * service接口
 */
public interface DrawingAnnotationInterface {

    /**
     * 图纸评审意见详情
     *
     * @param drawingAnnotationCreateDTO 图纸评审意见创建
     * @return 图纸评审意见
     */
    DrawingAnnotation create(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        DrawingAnnotationCreateDTO drawingAnnotationCreateDTO,
        ContextDTO contextDTO
    );


    /**
     * 图纸评审意见详情
     *
     * @param drawingAnnotationId 图纸ID
     * @return 图纸评审意见
     */
    DrawingAnnotation detail(Long orgId, Long projectId, Long drawingAnnotationId);

    /**
     * 图纸评审意见列表
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param drawingDetailId 图纸详情ID
     * @return 图纸评审意见
     */
    Page<DrawingAnnotation> search(Long orgId, Long projectId, Long drawingDetailId, PageDTO pageDTO);


    /**
     * 图纸评审意见删除
     *
     * @param drawingAnnotationId 图纸评审意见ID
     * @return 图纸评审意见
     */
    DrawingAnnotation delete(Long orgId, Long projectId, Long drawingAnnotationId, ContextDTO contextDTO);

    List<AnnotationHistoryResponseDTO> getAnnotationHistory(Long projectId, Long drawingFileHistoryId, Integer pageNo);
}
