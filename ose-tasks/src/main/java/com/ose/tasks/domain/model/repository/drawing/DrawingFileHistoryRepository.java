package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingFileHistory;
import com.ose.vo.DrawingFileType;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * DRAWING FILE HISTORY CRUD 操作接口。
 */
@Transactional
public interface DrawingFileHistoryRepository extends PagingAndSortingWithCrudRepository<DrawingFileHistory, Long>, DrawingFileHistoryRepositoryCustom {

    List<DrawingFileHistory> findByOrgIdAndProjectIdAndDrawingFileIdAndStatusOrderByCreatedAtDesc(Long orgId, Long projectId, Long drawingFileId, EntityStatus status);

    Page<DrawingFileHistory> findByOrgIdAndProjectIdAndDrawingFileIdAndStatusOrderByCreatedAtDesc(Long orgId, Long projectId, Long drawingFileId, EntityStatus status, Pageable pageable);

    Page<DrawingFileHistory> findByOrgIdAndProjectIdAndDrawingDetailIdAndStatusOrderByCreatedAtDesc(Long orgId, Long projectId, Long drawingDetailId, EntityStatus status, Pageable pageable);


    DrawingFileHistory findByOrgIdAndProjectIdAndIdAndStatus(Long orgId,
                                                             Long projectId,
                                                             Long drawingFileHistoryId,
                                                             EntityStatus status);

    DrawingFileHistory findByOrgIdAndProjectIdAndDrawingFileIdAndTaskIdAndStatus(
        Long orgId,
        Long projectId,
        Long drawingFileId,
        Long taskId,
        EntityStatus status
    );

    List<DrawingFileHistory> findByProjectIdAndDrawingFileIdAndProcInstIdAndStatusAndDeletedIsFalse(Long projectId, Long drawingFileId, Long procInstId, EntityStatus status);

    List<DrawingFileHistory> findByProjectIdAndDrawingDetailIdAndDrawingFileTypeAndProcInstIdAndDeletedIsFalse(
        Long projectId,
        Long drawingDetailId,
        DrawingFileType drawingFileType,
        Long procInstId
    );
}
