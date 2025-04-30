package com.ose.tasks.domain.model.repository.taskpackage;

import com.ose.repository.BaseRepository;
import com.ose.tasks.entity.taskpackage.TaskPackageBasic;

import java.util.List;
import java.util.Optional;

public class TaskPackageBasicCustomRepositoryImpl  extends BaseRepository implements TaskPackageBasicCustomRepository{
    @Override
    public List<TaskPackageBasic> findListByProjectIdAndNameAndDeletedIsFalse(Long projectId, String name) {
        return getSQLQueryBuilder(TaskPackageBasic.class)
            .is("projectId", projectId)
            .is("deleted", false)

            .like("name", name).exec().list();

    }

    @Override
    public List<TaskPackageBasic> findListByProjectIdAndName(Long projectId, String name) {
        return getSQLQueryBuilder(TaskPackageBasic.class)
            .is("projectId", projectId)
            .is("deleted", false)
            .is("name", name).exec().list();

    }

    @Override
    public Optional<TaskPackageBasic> findByProjectIdAndNameAndDeletedIsFalse(Long projectId, String name) {
        return Optional.empty();
    }
}
