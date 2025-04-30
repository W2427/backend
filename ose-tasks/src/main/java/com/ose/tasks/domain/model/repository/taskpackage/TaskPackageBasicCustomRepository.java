package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.tasks.entity.taskpackage.TaskPackageBasic;

import java.util.List;
import java.util.Optional;

public interface TaskPackageBasicCustomRepository {

    /**
     * 根据项目 ID 和名称取得任务包。
     *
     * @param projectId 项目 ID
     * @param name      类型名称
     * @return 任务包信息
     */
    List<TaskPackageBasic> findListByProjectIdAndNameAndDeletedIsFalse(Long projectId, String name);

    List<TaskPackageBasic> findListByProjectIdAndName(Long projectId, String name);


    Optional<TaskPackageBasic> findByProjectIdAndNameAndDeletedIsFalse(Long projectId, String name);
}
