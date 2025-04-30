package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.BatchDrawingTask;
import com.ose.tasks.vo.setting.BatchTaskCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 批处理任务状态记录 CRUD 操作接口。
 */
public interface BatchTaskDrawingRepository extends PagingAndSortingWithCrudRepository<BatchDrawingTask, Long> {
    /**
     * 检查是否存在项目 ID 及代码相同且正在运行的任务。
     *
     * @param projectId     项目 ID
     * @param batchTaskCode 批处理任务代码
     * @return 是否存在项目 ID 及代码相同且正在运行的任务
     */
    boolean existsByProjectIdAndCodeAndDrawingIdAndRunningIsTrue(Long projectId, BatchTaskCode batchTaskCode, Long drawingId);

    /**
     * 检查是否存在正在打包的图集任务。
     *
     * @param projectId 项目 ID
     * @return 是否存在项目 ID 及代码相同且正在运行的任务
     */
    boolean existsByOrgIdAndProjectIdAndRunningIsTrue(Long projectId, Long orgId);

    /**
     * 查找图纸打包历史。
     *
     * @param orgId
     * @param projectId
     * @param pageable
     * @return
     */
    Page<BatchDrawingTask> findByOrgIdAndProjectId(Long orgId, Long projectId, Pageable pageable);

    /**
     * 查找图纸打包详情。
     *
     * @param orgId
     * @param projectId
     * @param id
     * @return
     */
    BatchDrawingTask findByOrgIdAndProjectIdAndId(Long orgId, Long projectId, Long id);
}
