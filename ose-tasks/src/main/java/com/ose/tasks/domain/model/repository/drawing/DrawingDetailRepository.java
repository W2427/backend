package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.vo.EntityStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DrawingDetailRepository extends PagingAndSortingWithCrudRepository<DrawingDetail, Long> {

    Optional<DrawingDetail> findByDrawingIdAndRevAndStatus(Long id, String rev,
                                                           EntityStatus status);

    List<DrawingDetail> findByDrawingIdAndRevNoAndStatus(Long id, String revNo,
                                                           EntityStatus status);

    DrawingDetail findByDrawingIdAndRevNo(Long id, String revNo);

    DrawingDetail findByQrCode(String qrCode);

    List<DrawingDetail> findByDrawingIdAndStatus(Long drawingId, EntityStatus status);
    List<DrawingDetail> findByDrawingId(Long drawingId);

    DrawingDetail findFirstByDrawingIdAndStatusOrderByCreatedAtDesc(Long drawingId, EntityStatus status);

    List<DrawingDetail> findByDrawingIdAndStatusOrderByRevOrderDesc(Long drawingId, EntityStatus status);

    List<DrawingDetail> findByDrawingIdAndStatusInOrderByRevDesc(Long drawingId, List<EntityStatus> status);

    List<DrawingDetail> findByDrawingIdAndStatusInOrderByCreatedAt(Long drawingId, List<EntityStatus> status);

    Optional<DrawingDetail> findByDrawingIdAndRev(Long drawingId, String rev);

    DrawingDetail findByDrawingIdAndProgressStageAndRevNo(Long drawingId, String ProgressStage, String revNo);

    List<DrawingDetail> findByOrgIdAndProjectIdAndDrawingIdAndStatusIn(Long orgId, Long projectId, Long drawingId, List<EntityStatus> statusList);

    List<DrawingDetail> findByOrgIdAndProjectIdAndDrawingIdAndRevAndStatusIn(Long orgId, Long projectId, Long drawingId, String rev, List<EntityStatus> statusList);

    DrawingDetail findByOrgIdAndProjectIdAndIdAndStatus(Long orgId, Long projectId, Long id,EntityStatus status);

    List<DrawingDetail> findByDrawingIdOrderByRevDesc(Long drawingId);

    List<DrawingDetail> findByDrawingIdOrderByCreatedAt(Long drawingId);

    List<DrawingDetail> findByDrawingIdOrderByCreatedAtDesc(Long drawingId);

    List<DrawingDetail> findByDrawingIdAndActInsIdOrderByCreatedAt(Long drawingId, Long actInsId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE DrawingDetail dd SET dd.status = :newStatus WHERE dd.orgId = :orgId AND dd.projectId = :projectId AND dd.drawingId = :drawingId " +
        "AND dd.rev = :latestRev AND dd.status = :oldStatus")
    void updateStatusByDrawingIdAndDrawingVersionAndStatus(@Param("orgId") Long orgId,
                                                           @Param("projectId") Long projectId,
                                                           @Param("drawingId") Long drawingId,
                                                           @Param("latestRev") String latestRev,
                                                           @Param("newStatus") EntityStatus newStatus,
                                                           @Param("oldStatus") EntityStatus oldStatus);
    @Modifying
    @Transactional
    @Query(value = "UPDATE DrawingDetail dd SET dd.status = :newStatus WHERE dd.orgId = :orgId AND dd.projectId = :projectId AND dd.drawingId = :drawingId " +
        " AND dd.status = :oldStatus")
    void updateStatusByDrawingIdAndStatus(@Param("orgId") Long orgId,
                                                           @Param("projectId") Long projectId,
                                                           @Param("drawingId") Long drawingId,
                                                           @Param("newStatus") EntityStatus newStatus,
                                                           @Param("oldStatus") EntityStatus oldStatus);

    DrawingDetail findFirstByProjectIdAndDrawingIdAndStatusOrderByIdDesc(Long projectId, Long drawingId, EntityStatus status);
}
