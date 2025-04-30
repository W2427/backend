package com.ose.tasks.domain.model.repository.scheduled;

import com.ose.repository.PagingAndSortingWithCrudRepository;
import com.ose.tasks.entity.scheduled.ScheduledTaskLog;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.tasks.vo.scheduled.ScheduledTaskStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

/**
 * 定时任务处理状态 CRUD 操作接口。
 */
@Transactional
public interface ScheduledTaskLogRepository extends PagingAndSortingWithCrudRepository<ScheduledTaskLog, Long>, ScheduledTaskLogCustomRepository {

    /**
     * 取得最新的定时任务日志。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param status    定时任务执行状态
     * @return 定时任务执行日志
     */
    Optional<ScheduledTaskLog> findFirstByProjectIdAndCodeAndStatusOrderByStartedAtDesc(
        Long projectId,
        ScheduledTaskCode code,
        ScheduledTaskStatus status
    );

    /**
     * 取得定时任务日志。
     *
     * @param id   日志 ID
     * @param code 定时任务代码
     * @return 定时任务执行日志
     */
    Optional<ScheduledTaskLog> findByIdAndCode(Long id, ScheduledTaskCode code);

    /**
     * 更新进度。
     *
     * @param taskId         任务日志 ID
     * @param lastModifiedAt 最后更新时间
     * @param totalCount     输入总数
     * @param processedCount 处理件数
     * @param passedCount    忽略件数
     * @param errorCount     错误件数
     */
    @Transactional
    @Modifying
    @Query("UPDATE ScheduledTaskLog t SET t.lastModifiedAt = :lastModifiedAt, t.totalCount = :totalCount, t.processedCount = :processedCount, t.passedCount = :passedCount, t.errorCount = :errorCount WHERE t.id = :taskId")
    void updateProgress(
        @Param("taskId") Long taskId,
        @Param("lastModifiedAt") Date lastModifiedAt,
        @Param("totalCount") int totalCount,
        @Param("processedCount") int processedCount,
        @Param("passedCount") int passedCount,
        @Param("errorCount") int errorCount
    );

}
