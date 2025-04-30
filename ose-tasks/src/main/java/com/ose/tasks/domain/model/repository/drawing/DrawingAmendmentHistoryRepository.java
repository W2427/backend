package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.externalDrawing.DrawingAmendmentHistory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DrawingAmendmentHistoryRepository extends PagingAndSortingWithCrudRepository<DrawingAmendmentHistory, Long> {

    List<DrawingAmendmentHistory> findByDrawingIdOrderByCreatedAtDesc(Long drawingId);

    DrawingAmendmentHistory findByOrgIdAndProjectIdAndQrCode(Long orgId, Long projectId, String qrCode);

    DrawingAmendmentHistory findByDrawingIdAndVerison(Long drawingId, String latestRev);

    DrawingAmendmentHistory findByDrawingIdAndQrCode(Long drawingId, String qrCode);

}
