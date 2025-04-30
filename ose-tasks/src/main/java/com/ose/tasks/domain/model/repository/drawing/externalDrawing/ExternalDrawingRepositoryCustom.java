package com.ose.tasks.domain.model.repository.drawing.externalDrawing;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.ProofreadDrawingListCriteriaDTO;
import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistory;
import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistoryDetail;
import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * 图纸 CRUD 操作接口。
 */
@Transactional
public interface ExternalDrawingRepositoryCustom {

    Page<ExternalDrawing> getList(Long orgId, Long projectId, PageDTO page, DrawingCriteriaDTO criteriaDTO);

    Page<DrawingUploadZipFileHistory> zipFileHistory(Long orgId, Long projectId, Long drawingId, PageDTO page);

    Page<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetail(Long orgId, Long projectId, Long drawingId,
                                                                 Long id, PageDTO page);

    Page<ExternalDrawing> findByOrgIdAndProjectIdAndDrawingCategoryIsNotNullAndIdIn(Long orgId, Long projectId, List<Long> entityIds, Pageable pageable, String keyword);

    List<Map<String, Object>> getXlsList(Long orgId, Long projectId);

    Page<ExternalDrawing> getProofreadDrawingList(
        Long orgId,
        Long projectId,
        Long userId,
        PageDTO page,
        ProofreadDrawingListCriteriaDTO criteriaDTO
    );
}
