package com.ose.tasks.domain.model.repository.scheduled;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.scheduled.ScheduledTaskError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 定时任务错误信息 CRUD 操作接口。
 */
public interface ScheduledTaskErrorRepository extends PagingAndSortingWithCrudRepository<ScheduledTaskError, Long> {

    /**
     * 查询错误列表。
     *
     * @param logId    定时任务日志 ID
     * @param pageable 分页参数
     * @return 定时任务错误列表
     */
    Page<ScheduledTaskError> findByLogIdOrderByIdAsc(Long logId, Pageable pageable);

}
