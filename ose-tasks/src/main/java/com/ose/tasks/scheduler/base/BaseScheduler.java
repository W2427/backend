package com.ose.tasks.scheduler.base;

import com.ose.entity.BaseEntity;
import com.ose.tasks.domain.model.service.scheduled.ScheduledTaskLogInterface;
import com.ose.tasks.entity.Project;
import com.ose.tasks.scheduler.throwable.RunningException;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.util.SpringContextUtils;

/**
 * 定时任务基类。
 */
public abstract class BaseScheduler<ST extends BaseScheduler, IT extends BaseEntity> {


    private final ScheduledTaskCode code;


    private final ScheduledTaskLogInterface scheduledTaskLogService;


    private final Class<ST> schedulerType;

    private ST schedulerBean = null;


    private Long taskId;


    private int totalCount = 0;
    private int itemIndex = 0;
    private int progressUpdateBufferSize = 1;
    private int processedCount = 0;
    private int passedCount = 0;
    private int errorCount = 0;

    /**
     * 构造方法。
     */
    public BaseScheduler(
        final ScheduledTaskCode code,
        final ScheduledTaskLogInterface scheduledTaskLogService,
        final Class<ST> schedulerType
    ) {
        this.code = code;
        this.scheduledTaskLogService = scheduledTaskLogService;
        this.schedulerType = schedulerType;
    }

    /**
     * 取得定时任务代码。
     *
     * @return 定时任务代码
     */
    public final ScheduledTaskCode getCode() {
        return code;
    }

    /**
     * 取得当前定时任务类的 Bean。
     *
     * @return 定时任务类的 Bean
     */
    protected final ST self() {

        if (schedulerBean == null) {
            schedulerBean = SpringContextUtils.getBean(schedulerType);
        }

        return schedulerBean;
    }

    /**
     * 设置当前处理中的日志 ID。
     *
     * @param taskId 日志 ID
     */
    public final void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    /**
     * 取得当前时间的 Unix 时间戳（秒）。
     *
     * @return Unix 时间戳（秒）
     */
    protected double timestamp() {
        return Math.round(System.currentTimeMillis() / 1000.0);
    }

    /**
     * 开始定时任务。
     */
    public abstract void start();

    /**
     * 取得待处理数据的总件数。
     *
     * @return 总件数
     */
    public Integer count() {
        return null;
    }

    /**
     * 取得待处理数据的总件数。
     *
     * @param project 项目信息
     * @return 总件数
     */
    public Integer count(final Project project) {
        return null;
    }

    /**
     * 数据处理。
     *
     * @param project 项目信息
     */
    public void process(final Project project) {
    }

    /**
     * 数据处理。
     *
     * @param project 项目 ID
     * @param item    数据
     * @return 是否被处理（或被忽略/跳过）
     */
    public Boolean process(final Project project, final IT item) {
        return false;
    }

    /**
     * （当输入数据件数为 0 时）删除当前定时任务日志。
     *
     * @param projectId 项目 ID
     * @param taskId    任务 ID
     */
    public final void remove(final Long projectId, final Long taskId) {
        scheduledTaskLogService.remove(projectId, code, taskId);
    }

    /**
     * 将当前定时任务标记为已完成。
     *
     * @param projectId 项目 ID
     * @param taskId    定时任务日志 ID
     */
    public final void finish(final Long projectId, final Long taskId) {
        updateProgress();
        scheduledTaskLogService.finish(projectId, code, taskId);
    }

    /**
     * 将当前定时任务标记为失败。
     *
     * @param projectId 项目 ID
     * @param taskId    定时任务日志 ID
     * @param throwable 错误信息
     */
    public final void fail(
        final Long projectId,
        final Long taskId,
        final Throwable throwable
    ) {

        if (throwable instanceof RunningException) {
            return;
        }

        updateProgress();
        scheduledTaskLogService.fail(projectId, code, taskId, throwable.getMessage());
    }

    /**
     * 重置计数器。
     */
    public final void resetCounters() {
        totalCount = 0;
        progressUpdateBufferSize = 1;
        itemIndex = 0;
        processedCount = 0;
        passedCount = 0;
        errorCount = 0;
    }

    /**
     * 更新进度。
     *
     * @param totalCount 输入总数
     */
    public final void setTotalCount(final int totalCount) {
        this.totalCount = totalCount;
        progressUpdateBufferSize = (int) Math.ceil(totalCount / 100.0);
        scheduledTaskLogService.updateProgress(taskId, totalCount, 0, 0, 0);
    }

    /**
     * 更新进度。
     *
     * @param processed 数据是否被处理（或忽略）
     */
    public final void updateProgress(Boolean processed) {

        itemIndex++;

        if (processed) {
            processedCount++;
        } else {
            passedCount++;
        }

        if (itemIndex % progressUpdateBufferSize == 0) {
            updateProgress();
        }

    }

    /**
     * 更新进度。
     *
     * @param entity    数据实体
     * @param throwable 错误信息
     */
    public final void updateProgress(BaseEntity entity, Throwable throwable) {

        itemIndex++;
        errorCount++;

        if (itemIndex % progressUpdateBufferSize == 0) {
            updateProgress();
        }

        scheduledTaskLogService.addError(taskId, entity, throwable);
    }

    /**
     * 更新进度。
     */
    private void updateProgress() {
        scheduledTaskLogService.updateProgress(taskId, totalCount, processedCount, passedCount, errorCount);
    }

}
