package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.tasks.dto.taskpackage.TaskPackageCategoryCriteriaDTO;
import com.ose.tasks.entity.taskpackage.TaskPackageCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 任务包类型数据仓库。
 */
public interface TaskPackageCategoryCustomRepository {

    /**
     * 查询任务包类型。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包类型分页数据
     */
    Page<TaskPackageCategory> search(
        Long orgId,
        Long projectId,
        TaskPackageCategoryCriteriaDTO criteriaDTO,
        Pageable pageable
    );

}
