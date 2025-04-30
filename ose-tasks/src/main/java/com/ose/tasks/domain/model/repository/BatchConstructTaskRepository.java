package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.BatchConstructTask;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 批处理任务状态记录 CRUD 操作接口。
 */
public interface BatchConstructTaskRepository extends PagingAndSortingWithCrudRepository<BatchConstructTask, Long> {

    /**
     * 查找图纸打包历史。
     *
     * @param orgId
     * @param projectId
     * @param pageable
     * @return
     */
    Page<BatchConstructTask> findByOrgIdAndProjectId(Long orgId, Long projectId, Pageable pageable);

    /**
     * 查找图纸打包详情。
     *
     * @param orgId
     * @param projectId
     * @param id
     * @return
     */
    BatchConstructTask findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long id);
}
