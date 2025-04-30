package com.ose.tasks.domain.model.repository.drawing;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.DrawingWorkHourDTO;
import com.ose.tasks.entity.drawing.DrawingWorkHour;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


/**
 * 图纸 CRUD 操作接口。
 */
@Transactional
public interface DrawingWorkHourRepositoryCustom {

    Page<DrawingWorkHour> getList(Long orgId, Long projectId, DrawingWorkHourDTO dto, PageDTO page);
//
//    List<DrawingCriteriaDTO> getParamList(Long orgId, Long projectId);
//
//    Page<DrawingUploadZipFileHistory> zipFileHistory(Long orgId, Long projectId, Long drawingId, PageDTO page);
//
//    Page<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetail(Long orgId, Long projectId, Long drawingId,
//                                                                 Long id, PageDTO page);
//
//    Page<Drawing> findByOrgIdAndProjectIdAndDrawingCategoryIsNotNullAndIdIn(Long orgId, Long projectId, List<Long> entityIds, Pageable pageable, String keyword);
//
//    List<Map<String, Object>> getXlsList(Long orgId, Long projectId);
//
//    Page<Drawing> getProofreadDrawingList(
//        Long orgId,
//        Long projectId,
//        Long userId,
//        PageDTO page,
//        ProofreadDrawingListCriteriaDTO criteriaDTO
//    );
}
