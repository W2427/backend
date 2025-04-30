package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.BatchTaskBasic;
import com.ose.tasks.vo.setting.BatchTaskCode;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 批处理任务状态记录 CRUD 操作接口。
 */
public interface BatchTaskBasicRepository extends PagingAndSortingWithCrudRepository<BatchTaskBasic, Long>, BatchTaskBasicRepositoryCustom {

    /**
     * 检查是否存在项目 ID 及代码相同且正在运行的任务。
     *
     * @param projectId     项目 ID
     * @param batchTaskCode 批处理任务代码
     * @return 是否存在项目 ID 及代码相同且正在运行的任务
     */
    boolean existsByProjectIdAndCodeAndRunningIsTrue(Long projectId, BatchTaskCode batchTaskCode);

    /**
     * 检查是否存在项目 ID 及代码相同且正在运行的任务。
     *
     * @param projectId     项目 ID
     * @param batchTaskCode 批处理任务代码
     * @return 是否存在项目 ID 及代码相同且正在运行的任务
     */
    boolean existsByProjectIdAndCodeInAndRunningIsTrue(Long projectId, BatchTaskCode[] batchTaskCode);

}
