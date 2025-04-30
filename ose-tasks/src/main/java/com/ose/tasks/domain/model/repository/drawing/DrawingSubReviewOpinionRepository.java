package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.SubDrawingReviewOpinion;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 子图纸校审意见 CRUD 操作接口。
 */
@Transactional
public interface DrawingSubReviewOpinionRepository extends PagingAndSortingWithCrudRepository<SubDrawingReviewOpinion, Long> {

    List<SubDrawingReviewOpinion> findByOrgIdAndProjectIdAndSubDrawingIdAndActInstIdAndParentIdIsNullOrderByCreatedAtAsc(
        Long orgId,
        Long projectId,
        Long subDrawingId,
        Long actInstId
    );

    List<SubDrawingReviewOpinion> findByOrgIdAndProjectIdAndParentIdOrderByCreatedAtAsc(
        Long orgId,
        Long projectId,
        Long parentId
    );
}
