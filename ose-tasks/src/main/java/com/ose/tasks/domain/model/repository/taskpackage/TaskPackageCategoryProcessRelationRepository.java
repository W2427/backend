package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.tasks.entity.taskpackage.TaskPackageCategoryProcessRelation;
import com.ose.vo.EntityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * 任务包类型-实体工序数据仓库。
 */
public interface TaskPackageCategoryProcessRelationRepository extends PagingAndSortingRepository<TaskPackageCategoryProcessRelation, Long> {

    Page<TaskPackageCategoryProcessRelation> findByOrgIdAndProjectIdAndCategoryId(Long orgId, Long projectId, Long categoryId, Pageable pageable);

    List<TaskPackageCategoryProcessRelation> findByOrgIdAndProjectIdAndCategoryId(Long orgId, Long projectId, Long categoryId);

    TaskPackageCategoryProcessRelation findByOrgIdAndProjectIdAndProcessIdAndStatus(Long orgId, Long projectId, Long processId, EntityStatus status);

}
