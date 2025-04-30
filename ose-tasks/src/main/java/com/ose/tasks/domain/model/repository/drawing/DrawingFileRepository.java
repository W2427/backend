package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingFile;
import com.ose.vo.DrawingFileType;
import com.ose.vo.EntityStatus;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * DRAWING FILE CRUD 操作接口。
 */
@Transactional
public interface DrawingFileRepository extends PagingAndSortingWithCrudRepository<DrawingFile, Long> {

    List<DrawingFile> findByProjectIdAndDrawingDetailIdAndStatus(Long projectId, Long drawingDetailId, EntityStatus status);

    DrawingFile findByProjectIdAndDrawingDetailIdAndDrawingFileTypeAndStatus(Long projectId,
                                                                             Long drawingDetailId,
                                                                             DrawingFileType fileType,
                                                                             EntityStatus status);

    DrawingFile findByOrgIdAndProjectIdAndIdAndStatus(
        Long orgId,
        Long projectId,
        Long drawingFileId,
        EntityStatus status);

    List<DrawingFile> findByProjectIdAndStatus(Long projectId, EntityStatus status);


}
