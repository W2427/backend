package com.ose.tasks.domain.model.repository;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.BatchTask;
import com.ose.tasks.vo.setting.BatchTaskCode;
import com.ose.tasks.vo.setting.BatchTaskStatus;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * 批处理任务状态记录 CRUD 操作接口。
 */
public interface BatchTaskRepository extends PagingAndSortingWithCrudRepository<BatchTask, Long> {

    /**
     * 根据批处理任务 ID 及所属组织 ID 取得批处理任务。
     *
     * @param id    批处理任务 ID
     * @param orgId 所属组织 ID
     * @return 批处理任务信息
     */
    Optional<BatchTask> findByIdAndOrgId(Long id, Long orgId);

    boolean existsByProjectIdAndCodeAndStatus(Long projectId, BatchTaskCode code, BatchTaskStatus status);

    boolean existsByProjectIdAndCodeAndRunning(Long projectId, BatchTaskCode code, Boolean running);

}
