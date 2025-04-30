package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.SubDrawingHistory;
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
public interface SubDrawingHistoryRepository extends PagingAndSortingWithCrudRepository<SubDrawingHistory, Long> {

    List<SubDrawingHistory> findBySubDrawingIdAndStatusOrderByCreatedAtDesc(Long subDrawingId, EntityStatus status);

    List<SubDrawingHistory> findByIdNotAndSubDrawingId(Long id, Long subDrawingId);

    List<SubDrawingHistory> findBySubDrawingIdAndSubDrawingNoAndPageNoAndSubDrawingVersion(
        Long id,
        String subDrawingNo,
        int pageNo,
        String subDrawingVersion);

    SubDrawingHistory findByOrgIdAndProjectIdAndQrCode(Long orgId, Long projectId, String qrCode);



    Optional<SubDrawingHistory> findBySubDrawingNoAndPageNoAndIssueFlagIsTrue(String subDrawingNo, int pageNo);


    Optional<SubDrawingHistory> findBySubDrawingNoAndIssueFlagIsTrue(String subDrawingNo);


    @Modifying
    @Transactional
    @Query(value = "UPDATE sub_drawing_history sdh, sub_drawing sd SET sdh.issue_flag = :issued WHERE sdh.org_id = :orgId AND sdh.project_id = :projectId " +
        "AND sdh.sub_drawing_id = sd.id AND sd.drawing_id = :drawingId AND sd.drawing_version = :latestRev AND sd.status <> 'CANCEL'", nativeQuery = true)
    void updateIssuedByDrawingIdAndDrawingVersion(@Param("orgId") Long orgId,
                                                  @Param("projectId") Long projectId,
                                                  @Param("issued") Boolean issued,
                                                  @Param("drawingId") Long drawingId,
                                                  @Param("latestRev") String latestRev);
}
