package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingAnnotation;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DRAWING Annotation CRUD 操作接口。
 */
@Transactional
public interface DrawingAnnotationRepository extends PagingAndSortingWithCrudRepository<DrawingAnnotation, Long> {

    Page<DrawingAnnotation> findByOrgIdAndProjectIdAndDrawingDetailIdAndStatusOrderByCreatedAtDesc(
        Long orgId,
        Long projectId,
        Long drawingDetailId,
        EntityStatus status,
        Pageable pageable
    );

    DrawingAnnotation findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long drawingAnnotationId,
        EntityStatus status
    );

    List<DrawingAnnotation> findByProjectIdAndDrawingFileHistoryIdOrderByVersion(Long projectId, Long drawingFileHistoryId);

    DrawingAnnotation findByProjectIdAndAnnotationId(Long projectId, String annotationName);
}
