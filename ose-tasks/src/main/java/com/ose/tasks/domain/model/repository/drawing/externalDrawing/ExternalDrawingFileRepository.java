package com.ose.tasks.domain.model.repository.drawing.externalDrawing;

import com.ose.tasks.entity.drawing.externalDrawing.ExternalDrawingFile;
import com.ose.tasks.vo.drawing.DocType;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface ExternalDrawingFileRepository extends PagingAndSortingRepository<ExternalDrawingFile, Long> {

    ExternalDrawingFile findByProjectIdAndHistoryDrawingIdAndDocType(Long projectId, Long historyDrawingId, DocType docType);

    List<ExternalDrawingFile> findByOrgIdAndProjectIdAndHistoryDrawingIdAndStatus(Long orgId, Long projectId, Long id, EntityStatus entityStatus);

    List<ExternalDrawingFile> findByProjectIdAndHistoryDrawingIdAndStatus(Long projectId, Long id, EntityStatus entityStatus);

    List<ExternalDrawingFile> findByOrgIdAndProjectIdAndDrawingId(Long orgId, Long projectId, Long id);

    Page<ExternalDrawingFile> findByProjectIdAndDocTypeAndStatus(Long projectId, DocType markup, EntityStatus active, Pageable pageable);

    Page<ExternalDrawingFile> findByProjectIdAndDocTypeAndFileNameLikeAndStatus(Long projectId, DocType markup, String s, EntityStatus active, Pageable pageable);

    List<ExternalDrawingFile> findByIdOrderByCreatedAt(Long drawingId);

    List<ExternalDrawingFile> findByOrgIdAndProjectIdAndStatus(Long orgId, Long projectId, EntityStatus entityStatus);


}

