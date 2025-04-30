package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.SplitPDFHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * 。
 *
 * @auth DengMing
 * @date 2021/8/3 13:49
 */
@Transactional
public interface SplitPDFHistoryRepository extends PagingAndSortingWithCrudRepository<SplitPDFHistory,Long> {

    Page<SplitPDFHistory> findByOrgIdAndProjectId(
        Long orgId,
        Long projectId,
        Pageable pageable
    );
    Page<SplitPDFHistory> findByOrgIdAndProjectIdAndDrawingNoLikeOrFileNameLike(
        Long orgId,
        Long projectId,
        String drawingNo,
        String fileName,
        Pageable pageable
    );
}
