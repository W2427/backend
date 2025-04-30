package com.ose.tasks.domain.model.repository.drawing;

import java.util.List;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ose.tasks.entity.drawing.DrawingUploadZipFileHistory;


/**
 * 工序 CRUD 操作接口。
 */
@Transactional
public interface DrawingUploadZipFileHistoryRepository extends PagingAndSortingWithCrudRepository<DrawingUploadZipFileHistory, Long> {

    List<DrawingUploadZipFileHistory> findByDrawingId(Long drawingId);

}
