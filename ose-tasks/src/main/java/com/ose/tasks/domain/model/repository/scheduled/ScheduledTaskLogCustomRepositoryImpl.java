package com.ose.tasks.domain.model.repository.scheduled;

import com.ose.repository.BaseRepository;
import com.ose.tasks.entity.scheduled.ScheduledTaskLog;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.tasks.vo.scheduled.ScheduledTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 定时任务处理状态查询。
 */
public class ScheduledTaskLogCustomRepositoryImpl extends BaseRepository implements ScheduledTaskLogCustomRepository {

    /**
     * 查询批处理任务。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param status    定时任务执行状态
     * @param pageable  分页参数
     * @return 定时任务执行记录分页数据
     */
    @Override
    public Page<ScheduledTaskLog> search(
        final Long projectId,
        final ScheduledTaskCode code,
        final ScheduledTaskStatus status,
        final Pageable pageable
    ) {
        return getSQLQueryBuilder(ScheduledTaskLog.class)
            .is("projectId", projectId)
            .is("code", code)
            .is("status", status)
            .paginate(pageable)
            .exec()
            .page();
    }

}
