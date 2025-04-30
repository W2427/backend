package com.ose.tasks.domain.model.service.drawing.impl;

import com.ose.dto.AnnotationHistoryResponseDTO;
import com.ose.dto.AnnotationResponseDTOV1;
import com.ose.dto.ContextDTO;
import com.ose.dto.PageDTO;
import com.ose.exception.BusinessError;
import com.ose.tasks.domain.model.repository.drawing.DrawingAnnotationRepository;
import com.ose.tasks.domain.model.service.drawing.DrawingAnnotationInterface;
import com.ose.tasks.dto.drawing.DrawingAnnotationCreateDTO;
import com.ose.tasks.entity.drawing.DrawingAnnotation;
import com.ose.tasks.util.PdfAnnotationV1Util;
import com.ose.vo.EntityStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component

public class DrawingAnnotationService implements DrawingAnnotationInterface {

    private final static Logger logger = LoggerFactory.getLogger(DrawingAnnotationService.class);

    private final DrawingAnnotationRepository drawingAnnotationRepository;

    /**
     * 构造方法
     */
    @Autowired
    public DrawingAnnotationService(
        DrawingAnnotationRepository drawingAnnotationRepository) {
        this.drawingAnnotationRepository = drawingAnnotationRepository;
    }

    /**
     * 图纸评审意见详情创建
     *
     * @param drawingAnnotationCreateDTO 图纸评审意见创建
     * @return 图纸评审意见
     */
    @Override
    public DrawingAnnotation create(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        DrawingAnnotationCreateDTO drawingAnnotationCreateDTO,
        ContextDTO contextDTO
    ) {
        DrawingAnnotation drawingAnnotation = new DrawingAnnotation();

        BeanUtils.copyProperties(drawingAnnotationCreateDTO, drawingAnnotation);
        drawingAnnotation.setOrgId(orgId);
        drawingAnnotation.setProjectId(projectId);
        drawingAnnotation.setStatus(EntityStatus.ACTIVE);
        drawingAnnotation.setCreatedAt(new Date());
        drawingAnnotation.setCreatedBy(contextDTO.getOperator().getId());
        drawingAnnotation.setLastModifiedAt(new Date());
        drawingAnnotation.setLastModifiedBy(contextDTO.getOperator().getId());

        drawingAnnotationRepository.save(drawingAnnotation);
        return drawingAnnotation;
    }


    /**
     * 图纸评审意见详情
     *
     * @param drawingAnnotationId 图纸评审意见ID
     * @return 图纸评审意见
     */
    @Override
    public DrawingAnnotation detail(Long orgId, Long projectId, Long drawingAnnotationId) {
        DrawingAnnotation drawingAnnotation = drawingAnnotationRepository.findByOrgIdAndProjectIdAndIdAndStatus(
            orgId,
            projectId,
            drawingAnnotationId,
            EntityStatus.ACTIVE
        );
        if (null == drawingAnnotation) {
            throw new BusinessError("DrawingAnnotation not find");
        }
        return drawingAnnotation;
    }

    /**
     * 图纸评审意见列表
     *
     * @param orgId           组织ID
     * @param projectId       项目ID
     * @param drawingDetailId 图纸ID
     * @return 图纸评审意见
     */
    @Override
    public Page<DrawingAnnotation> search(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        PageDTO pageDTO
    ) {
        return drawingAnnotationRepository.findByOrgIdAndProjectIdAndDrawingDetailIdAndStatusOrderByCreatedAtDesc(
            orgId,
            projectId,
            drawingDetailId,
            EntityStatus.ACTIVE,
            pageDTO.toPageable()
        );
    }

    /**
     * 图纸评审意见详情
     *
     * @param drawingAnnotationId 图纸评审意见ID
     * @return 图纸评审意见
     */
    @Override
    public DrawingAnnotation delete(Long orgId, Long projectId, Long drawingAnnotationId, ContextDTO contextDTO) {
        DrawingAnnotation drawingAnnotation =
            drawingAnnotationRepository.findByOrgIdAndProjectIdAndIdAndStatus(
                orgId,
                projectId,
                drawingAnnotationId,
                EntityStatus.ACTIVE
            );

        if (null == drawingAnnotation) {
            throw new BusinessError("DrawingAnnotation not find");
        }

        drawingAnnotation.setLastModifiedAt(new Date());
        drawingAnnotation.setLastModifiedBy(contextDTO.getOperator().getId());
        drawingAnnotation.setDeletedAt(new Date());
        drawingAnnotation.setDeletedBy(contextDTO.getOperator().getId());
        drawingAnnotation.setDeleted(true);
        drawingAnnotation.setStatus(EntityStatus.DELETED);
        drawingAnnotationRepository.save(drawingAnnotation);

        return drawingAnnotation;
    }

    @Override
    public List<AnnotationHistoryResponseDTO> getAnnotationHistory(Long projectId, Long drawingFileHistoryId, Integer pageNo){
        List<DrawingAnnotation> dwgAnnos =
            drawingAnnotationRepository.findByProjectIdAndDrawingFileHistoryIdOrderByVersion(projectId, drawingFileHistoryId);
        List<AnnotationResponseDTOV1> annoRespons = new ArrayList<>();
        List<AnnotationHistoryResponseDTO> annotationHistoryResponseDTOS = new ArrayList<>();

        Long currentVersion  = 0L;
        for(DrawingAnnotation dwgAnno : dwgAnnos) {
            Long version = dwgAnno.getVersion();
            AnnotationResponseDTOV1 annotationResponseDTOV = PdfAnnotationV1Util.convertAnnotation(dwgAnno.getJsonAnnotation(), pageNo,dwgAnno.getPw(), dwgAnno.getPh(), dwgAnno.getPageRotation());

            if(currentVersion.equals(version)) {

            } else {
                AnnotationHistoryResponseDTO annotationHistoryResponseDTO = new AnnotationHistoryResponseDTO();
                annotationHistoryResponseDTO.setAnnotations(annoRespons);
                currentVersion=version;
                annotationHistoryResponseDTO.setVersion(currentVersion);
                annotationHistoryResponseDTOS.add(annotationHistoryResponseDTO);
            }
            annoRespons.add(annotationResponseDTOV);


        }

        return annotationHistoryResponseDTOS;
    }
}
