package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.taskpackage.TaskPackageCategoryProcessRelationBasic;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * 任务包类型-实体工序数据仓库。
 */
public interface TaskPackageCategoryProcessRelationBasicRepository extends PagingAndSortingWithCrudRepository<TaskPackageCategoryProcessRelationBasic, Long> {

    Optional<TaskPackageCategoryProcessRelationBasic> findFirstByProjectIdAndProcessId(Long projectId, Long processId);

    void deleteByProjectIdAndCategoryId(Long projectId, Long categoryId);

}
