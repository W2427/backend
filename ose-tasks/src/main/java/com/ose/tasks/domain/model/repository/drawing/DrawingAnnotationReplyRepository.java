package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingAnnotationReply;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * DRAWING Annotation Reply CRUD 操作接口。
 */
@Transactional
public interface DrawingAnnotationReplyRepository extends PagingAndSortingWithCrudRepository<DrawingAnnotationReply, Long> {

    Page<DrawingAnnotationReply> findByOrgIdAndProjectIdAndDrawingAnnotationIdAndStatusOrderByCreatedAtDesc(
        Long orgId,
        Long projectId,
        Long drawingAnnotationId,
        EntityStatus status,
        Pageable pageable
    );

    DrawingAnnotationReply findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long drawingAnnotationReplyId,
        EntityStatus status
    );

}
