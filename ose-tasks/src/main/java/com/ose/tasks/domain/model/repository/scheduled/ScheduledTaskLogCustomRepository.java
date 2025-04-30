package com.ose.tasks.domain.model.repository.scheduled;

import com.ose.tasks.entity.scheduled.ScheduledTaskLog;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.tasks.vo.scheduled.ScheduledTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 定时任务处理状态 CRUD 操作接口。
 */
public interface ScheduledTaskLogCustomRepository {

    /**
     * 查询批处理任务。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param status    定时任务执行状态
     * @param pageable  分页参数
     * @return 定时任务执行记录分页数据
     */
    Page<ScheduledTaskLog> search(
        Long projectId,
        ScheduledTaskCode code,
        ScheduledTaskStatus status,
        Pageable pageable
    );

}
