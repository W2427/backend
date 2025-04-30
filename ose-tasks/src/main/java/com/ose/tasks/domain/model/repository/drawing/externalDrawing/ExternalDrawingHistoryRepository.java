package com.ose.tasks.domain.model.repository.drawing.externalDrawing;
import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawingHistory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface ExternalDrawingHistoryRepository extends PagingAndSortingWithCrudRepository<ExternalDrawingHistory, Long> {

    List<ExternalDrawingHistory> findByDrawingIdOrderByCreatedAtDesc(Long drawingId);

    ExternalDrawingHistory findByOrgIdAndProjectIdAndQrCode(Long orgId, Long projectId, String qrCode);

    ExternalDrawingHistory findByDrawingIdAndVerison(Long drawingId, String latestRev);

    ExternalDrawingHistory findByDrawingIdAndQrCode(Long drawingId, String qrCode);

}
