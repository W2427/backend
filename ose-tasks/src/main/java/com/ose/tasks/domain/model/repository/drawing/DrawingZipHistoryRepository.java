package com.ose.tasks.domain.model.repository.drawing;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.drawing.DrawingZipDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface DrawingZipHistoryRepository extends PagingAndSortingWithCrudRepository<DrawingZipDetail,Long> {
    /**
     * 根据项目ID,组织ID获取表
     * @param orgId     组织ID
     * @param projectId 项目ID
     * @param pageable  分页
     * @return
     */
    Page<DrawingZipDetail> findByOrgIdAndProjectId(Long orgId,Long projectId,Pageable pageable);
}
