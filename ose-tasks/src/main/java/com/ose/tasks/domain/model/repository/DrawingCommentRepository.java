package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingComment;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 实体 CRUD 操作接口。
 */
public interface DrawingCommentRepository extends PagingAndSortingWithCrudRepository<DrawingComment, Long>, DrawingCommentRepositoryCustom {

//    List<DrawingCoordinate> findByOrgIdAndProjectIdAndEntitySubTypeAndStatus(
//        Long orgId,
//        Long projectId,
//        String entitySubType,
//        EntityStatus status
//    );
//
//    Page<DrawingCoordinate> findByOrgIdAndProjectIdAndEntitySubTypeIdAndStatusAndDrawingCoordinateTypeIn(
//        Long orgId,
//        Long projectId,
//        Long entitySubTypeId,
//        EntityStatus status,
//        List<DrawingCoordinateType> drawingCoordinateTypes,
//        Pageable pageable
//    );
//
//    DrawingCoordinate findByOrgIdAndProjectIdAndEntitySubTypeAndNameAndStatus(
//        Long orgId,
//        Long projectId,
//        String entitySubType,
//        String name,
//        EntityStatus status
//    );
//
//    DrawingCoordinate findByOrgIdAndProjectIdAndNameAndStatus(
//        Long orgId,
//        Long projectId,
//        String name,
//        EntityStatus status
//    );
//
//    DrawingCoordinate findByIdAndStatus(Long id, EntityStatus status);
//
//
//    @Query(
//        value = "SELECT " +
//            "  *  " +
//            "FROM " +
//            "  `drawing_coordinate` " +
//            "WHERE " +
//            "  org_id = :orgId " +
//            "  AND project_id = :projectId " +
//            "  AND `status` = 'ACTIVE' " +
//            "  AND entity_sub_type = :entitySubType " +
//            "  AND drawing_cover_width BETWEEN :coverWidth - 20 AND :coverWidth + 20 " +
//            "  AND drawing_cover_height BETWEEN :coverHeight - 20 AND :coverHeight + 20 ",
//        nativeQuery = true
//    )
//    DrawingCoordinate searchDrawingCoordinateByEntitySubTypeAndCover(
//        @Param("orgId") Long orgId,
//        @Param("projectId") Long projectId,
//        @Param("entitySubType") String entitySubType,
//        @Param("coverWidth") Float coverWidth,
//        @Param("coverHeight") Float coverHeight
//    );
//    List<DrawingSignatureCoordinate> findByOrgIdAndProjectIdAndEntitySubTypeIdAndStatusAndDrawingSignatureType(
//        Long orgId,
//        Long projectId,
//        Long id,
//        EntityStatus active,
//        DrawingSignatureType draw
//    );
}
