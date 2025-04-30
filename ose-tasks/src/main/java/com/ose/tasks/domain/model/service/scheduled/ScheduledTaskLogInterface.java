package com.ose.tasks.domain.model.service.scheduled;

import com.ose.dto.OperatorDTO;
import com.ose.entity.BaseEntity;
import com.ose.tasks.entity.scheduled.ScheduledTaskError;
import com.ose.tasks.entity.scheduled.ScheduledTaskLog;
import com.ose.tasks.scheduler.throwable.RunningException;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.tasks.vo.scheduled.ScheduledTaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 定时任务服务接口。
 */
public interface ScheduledTaskLogInterface {

    /**
     * 开始执行定时任务。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @return 定时任务执行日志信息
     */
    ScheduledTaskLog start(Long projectId, ScheduledTaskCode code) throws RunningException;

    /**
     * 更新进度。
     *
     * @param taskId         任务日志 ID
     * @param totalCount     输入总数
     * @param processedCount 处理件数
     * @param passedCount    忽略件数
     * @param errorCount     错误件数
     */
    void updateProgress(Long taskId, int totalCount, int processedCount, int passedCount, int errorCount);

    /**
     * 删除定时任务日志。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param id        日志 ID
     */
    void remove(Long projectId, ScheduledTaskCode code, Long id);

    /**
     * 将定时任务标记为已完成。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param id        日志 ID
     */
    void finish(Long projectId, ScheduledTaskCode code, Long id);

    /**
     * 中止任务。
     *
     * @param operator 操作者信息
     * @param id       定时任务 ID
     */
    void stop(OperatorDTO operator, Long id);

    /**
     * 将定时任务标记为失败。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param id        日志 ID
     * @param errorLog  错误日志
     */
    void fail(Long projectId, ScheduledTaskCode code, Long id, String errorLog);

    /**
     * 添加错误记录。
     *
     * @param taskId    定时任务启动日志 ID
     * @param entity    数据实体信息
     * @param throwable 错误信息
     */
    void addError(Long taskId, BaseEntity entity, Throwable throwable);

    /**
     * 取得定时任务启动日志列表。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param status    定时任务执行状态
     * @param pageable  分页参数
     */
    Page<ScheduledTaskLog> list(
        Long projectId,
        ScheduledTaskCode code,
        ScheduledTaskStatus status,
        Pageable pageable
    );

    /**
     * 取得错误信息。
     *
     * @param projectId 项目 ID
     * @param taskId    任务 ID
     * @param pageable  分页参数
     * @return 错误分页数据
     */
    Page<ScheduledTaskError> errors(
        Long projectId,
        Long taskId,
        Pageable pageable
    );

}
