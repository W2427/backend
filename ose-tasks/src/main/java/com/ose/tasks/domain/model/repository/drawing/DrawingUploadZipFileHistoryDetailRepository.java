package com.ose.tasks.domain.model.repository.drawing;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistoryDetail;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DrawingUploadZipFileHistoryDetailRepository extends PagingAndSortingWithCrudRepository<DrawingUploadZipFileHistoryDetail, Long> {

    List<DrawingUploadZipFileHistoryDetail> findByDrawingUploadZipFileHistoryId(Long drawingUploadZipFileHistoryId);

}
