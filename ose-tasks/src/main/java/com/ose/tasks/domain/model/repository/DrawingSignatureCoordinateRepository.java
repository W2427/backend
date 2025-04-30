package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingSignatureCoordinate;
import com.ose.tasks.vo.drawing.DrawingSignatureType;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 电子签名坐标 CRUD 操作接口。
 */
public interface DrawingSignatureCoordinateRepository extends PagingAndSortingWithCrudRepository<DrawingSignatureCoordinate, Long> {

    List<DrawingSignatureCoordinate> findByOrgIdAndProjectIdAndEntitySubTypeIdAndStatus(
        Long orgId,
        Long projectId,
        Long id,
        EntityStatus active
    );

    List<DrawingSignatureCoordinate> findByOrgIdAndProjectIdAndEntitySubTypeIdAndStatusAndDrawingSignatureType(
        Long orgId,
        Long projectId,
        Long id,
        EntityStatus active,
        DrawingSignatureType draw
    );
}
