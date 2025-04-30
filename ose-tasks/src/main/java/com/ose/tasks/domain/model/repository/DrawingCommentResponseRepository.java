package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingComment;
import com.ose.tasks.entity.drawing.DrawingCommentResponse;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 实体 CRUD 操作接口。
 */
public interface DrawingCommentResponseRepository extends PagingAndSortingWithCrudRepository<DrawingCommentResponse, Long> {


        @Query(
        value = "SELECT " +
            "  *  " +
            "FROM " +
            "  `drawing_comment_response` " +
            "WHERE " +
            "  org_id = :orgId " +
            "  AND project_id = :projectId " +
            "  AND `status` != 'DELETED' " +
            "  AND drawing_comment_id = :drawingCommentId ",
        nativeQuery = true
    )
    List<DrawingCommentResponse> findByOrgIdAndProjectIdAndDrawingComment(
        Long orgId,
        Long projectId,
        Long drawingCommentId
    );

    @Query(
        value = "SELECT " +
            "  *  " +
            "FROM " +
            "  `drawing_comment_response` " +
            "WHERE " +
            "  org_id = :orgId " +
            "  AND project_id = :projectId " +
            "  AND `status` != 'DELETED' " +
            "  AND parent_response_id = :drawingCommentId ",
        nativeQuery = true
    )
    List<DrawingCommentResponse> findByOrgIdAndProjectIdAndDrawingCommentResponseId(
        Long orgId,
        Long projectId,
        Long drawingCommentId
    );

    List<DrawingCommentResponse> findByOrgIdAndProjectIdAndDrawingCommentIdAndStatus(
        Long orgId,
        Long projectId,
        Long drawingCommentId,
        EntityStatus status
    );
}
