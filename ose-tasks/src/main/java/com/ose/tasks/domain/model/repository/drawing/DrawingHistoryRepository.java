package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingHistory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DrawingHistoryRepository extends PagingAndSortingWithCrudRepository<DrawingHistory, Long> {

    DrawingHistory findByDrawingIdAndVersion(Long drawingId, String latestRev);

    List<DrawingHistory> findByDrawingIdOrderByCreatedAtDesc(Long drawingId);

    DrawingHistory findByOrgIdAndProjectIdAndQrCode(Long orgId, Long projectId, String qrCode);

    DrawingHistory findByDrawingIdAndVerison(Long drawingId, String latestRev);

    Optional<DrawingHistory> findAllByOrgIdAndProjectIdAndDrawingId(Long orgId, Long projectId, Long drawingId);

    @Query("SELECT  m FROM DrawingHistory m WHERE m.drawingId = :drawingId")
    DrawingHistory findByDrawingId(@Param("drawingId") Long drawingId);

}
