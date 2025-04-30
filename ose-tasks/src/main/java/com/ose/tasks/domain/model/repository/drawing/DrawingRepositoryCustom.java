package com.ose.tasks.domain.model.repository.drawing;

import com.ose.dto.PageDTO;
import com.ose.tasks.dto.drawing.DrawingCriteriaDTO;
import com.ose.tasks.dto.drawing.DrawingWorkHourDTO;
import com.ose.tasks.dto.drawing.ProofreadDrawingListCriteriaDTO;
import com.ose.tasks.dto.timesheet.SelectDataDTO;
import com.ose.tasks.entity.drawing.Drawing;
import com.ose.tasks.entity.drawing.DrawingDetail;
import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistory;
import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistoryDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;


/**
 * 图纸 CRUD 操作接口。
 */
@Transactional
public interface DrawingRepositoryCustom {

    Page<DrawingWorkHourDTO> getList(Long orgId, Long projectId, PageDTO page, DrawingCriteriaDTO criteriaDTO);

    List<DrawingCriteriaDTO> getParamList(Long orgId, Long projectId);

    Page<DrawingUploadZipFileHistory> zipFileHistory(Long orgId, Long projectId, Long drawingId, PageDTO page);

    Page<DrawingUploadZipFileHistoryDetail> zipFileHistoryDetail(Long orgId, Long projectId, Long drawingId,
                                                                 Long id, PageDTO page);

    Page<Drawing> findByOrgIdAndProjectIdAndDrawingCategoryIsNotNullAndIdIn(Long orgId, Long projectId, List<Long> entityIds, Pageable pageable, String keyword);

    List<Map<String, Object>> getXlsList(Long orgId, Long projectId);

    Page<DrawingDetail> getProofreadDrawingList(
        Long orgId,
        Long projectId,
        Long userId,
        PageDTO page,
        ProofreadDrawingListCriteriaDTO criteriaDTO
    );

    List<SelectDataDTO> getVersionList(Long drawingId, Long processId, Long userId);

    List<SelectDataDTO> getTaskNodes(Long drawingId, String version, Long userId);
}
