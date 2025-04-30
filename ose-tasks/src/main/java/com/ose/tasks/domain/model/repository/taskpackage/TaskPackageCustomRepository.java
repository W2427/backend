package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.tasks.dto.taskpackage.TaskPackageCriteriaDTO;
import com.ose.tasks.entity.taskpackage.TaskPackagePercent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 任务包数据仓库。
 */
public interface TaskPackageCustomRepository {

    /**
     * 查询任务包。
     *
     * @param orgId       组织 ID
     * @param projectId   项目 ID
     * @param criteriaDTO 查询条件
     * @param pageable    分页参数
     * @return 任务包分页数据
     */
    Page<TaskPackagePercent> search(
        Long orgId,
        Long projectId,
        TaskPackageCriteriaDTO criteriaDTO,
        Pageable pageable
    );




/*    Page<TaskPackageBasic> search(
            Long orgId,
            Long projectId,
            TaskPackageCriteriaDTO criteriaDTO,
            Pageable pageable
    );*/

}
