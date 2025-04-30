package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingCoordinate;
import com.ose.tasks.vo.drawing.DrawingCoordinateType;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 条形码坐标 CRUD 操作接口。
 */
public interface DrawingCoordinateRepository extends PagingAndSortingWithCrudRepository<DrawingCoordinate, Long>, DrawingCoordinateRepositoryCustom {


    Page<DrawingCoordinate> findByOrgIdAndProjectIdAndStatusAndDrawingCoordinateTypeInAndIdIn(
        Long orgId,
        Long projectId,
        EntityStatus status,
        List<DrawingCoordinateType> drawingCoordinateTypes,
        List<Long> ids,
        Pageable pageable
    );

    DrawingCoordinate findByOrgIdAndProjectIdAndNameAndStatus(
        Long orgId,
        Long projectId,
        String name,
        EntityStatus status
    );

    DrawingCoordinate findByIdAndStatus(Long id, EntityStatus status);


    @Query(
        value = "SELECT " +
            "  *  " +
            "FROM " +
            "  `drawing_coordinate` " +
            "WHERE " +
            "  org_id = :orgId " +
            "  AND project_id = :projectId " +
            "  AND `status` = 'ACTIVE' " +
            "  AND entity_sub_type = :entitySubType " +
            "  AND drawing_cover_width BETWEEN :coverWidth - 20 AND :coverWidth + 20 " +
            "  AND drawing_cover_height BETWEEN :coverHeight - 20 AND :coverHeight + 20 ",
        nativeQuery = true
    )
    DrawingCoordinate searchDrawingCoordinateByEntitySubTypeAndCover(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entitySubType") String entitySubType,
        @Param("coverWidth") Float coverWidth,
        @Param("coverHeight") Float coverHeight
    );

    @Query(
        value = "SELECT " +
            "  dc.*  " +
            "FROM " +
            "  `drawing_coordinate` dc  " +
            "  LEFT JOIN bpm_entity_type_coordinate_relation r ON r.drawing_coordinate_id = dc.id " +
            "  LEFT JOIN bpm_entity_sub_type t ON t.id = r.entity_sub_type_id " +
            "WHERE " +
            "  dc.`status` = 'ACTIVE' " +
            "  and dc.org_id = :orgId " +
            "  AND dc.project_id = :projectId " +
            "  AND t.name_en = :entitySubType " +
            "GROUP BY " +
            "  dc.id ",
        nativeQuery = true
    )
    List<DrawingCoordinate> searchDrawingCoordinateByEntitySubType(
        @Param("orgId") Long orgId,
        @Param("projectId") Long projectId,
        @Param("entitySubType") String entitySubType
    );

//    List<DrawingSignatureCoordinate> findByOrgIdAndProjectIdAndEntitySubTypeIdAndStatusAndDrawingSignatureType(
//        Long orgId,
//        Long projectId,
//        Long id,
//        EntityStatus active,
//        DrawingSignatureType draw
//    );
}
