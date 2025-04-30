package com.ose.tasks.domain.model.service.scheduled;

import com.ose.dto.OperatorDTO;
import com.ose.entity.BaseEntity;
import com.ose.exception.ConflictError;
import com.ose.exception.NotFoundError;
import com.ose.service.StringRedisService;
import com.ose.tasks.domain.model.repository.scheduled.ScheduledTaskErrorRepository;
import com.ose.tasks.domain.model.repository.scheduled.ScheduledTaskLogRepository;
import com.ose.tasks.entity.scheduled.ScheduledTaskError;
import com.ose.tasks.entity.scheduled.ScheduledTaskLog;
import com.ose.tasks.scheduler.throwable.RunningException;
import com.ose.tasks.vo.scheduled.ScheduledTaskCode;
import com.ose.tasks.vo.scheduled.ScheduledTaskStatus;
import com.ose.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 定时任务服务。
 */
@Component
public class ScheduledTaskLogService extends StringRedisService implements ScheduledTaskLogInterface {


    private static final String START_AT_REDIS_KEY = "SCHEDULED_TASK:%s:%s:STARTED_AT";


    private final ScheduledTaskLogRepository scheduledTaskLogRepository;


    private final ScheduledTaskErrorRepository scheduledTaskErrorRepository;


    private static final long TIMEOUT_MILLISECONDS = 30 * 60 * 1000;

    /**
     * 构造方法。
     */
    @Autowired
    public ScheduledTaskLogService(
        final StringRedisTemplate stringRedisTemplate,
        final ScheduledTaskLogRepository scheduledTaskLogRepository,
        final ScheduledTaskErrorRepository scheduledTaskErrorRepository
    ) {
        super(stringRedisTemplate);
        this.scheduledTaskLogRepository = scheduledTaskLogRepository;
        this.scheduledTaskErrorRepository = scheduledTaskErrorRepository;
    }

    /**
     * 开始执行定时任务。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @return 定时任务执行日志信息
     */
    @Override
    public ScheduledTaskLog start(
        final Long projectId,
        final ScheduledTaskCode code
    ) throws RunningException {

        String redisKey = String.format(START_AT_REDIS_KEY, projectId, code.name());
        String startAt = getRedisKey(redisKey);

        if (!StringUtils.isEmpty(startAt)) {
            throw new RunningException();
        }

        startAt = "" + System.currentTimeMillis();

        setRedisKey(redisKey, startAt, 10);

        if (!startAt.equals(getRedisKey(redisKey))) {
            throw new RunningException();
        }

        ScheduledTaskLog scheduledTaskLog = scheduledTaskLogRepository
            .findFirstByProjectIdAndCodeAndStatusOrderByStartedAtDesc(
                projectId,
                code,
                ScheduledTaskStatus.RUNNING
            )
            .orElse(null);

        if (scheduledTaskLog != null) {


            if (scheduledTaskLog.getLastModifiedAt().getTime() > (System.currentTimeMillis() - TIMEOUT_MILLISECONDS)) {
                throw new RunningException();
            }


            scheduledTaskLog.setStoppedAt(new Date());
            scheduledTaskLog.setStatus(ScheduledTaskStatus.TIMEOUT);
            scheduledTaskLogRepository.save(scheduledTaskLog);
        }

        scheduledTaskLog = new ScheduledTaskLog();
        scheduledTaskLog.setProjectId(projectId);
        scheduledTaskLog.setCode(code);
        scheduledTaskLog.setStatus(ScheduledTaskStatus.RUNNING);

        return scheduledTaskLogRepository.save(scheduledTaskLog);
    }

    /**
     * 更新进度。
     *
     * @param taskId         任务日志 ID
     * @param totalCount     输入总数
     * @param processedCount 处理件数
     * @param passedCount    忽略件数
     * @param errorCount     错误件数
     */
    @Override
    @Transactional
    public void updateProgress(
        final Long taskId,
        final int totalCount,
        final int processedCount,
        final int passedCount,
        final int errorCount
    ) {
        scheduledTaskLogRepository
            .updateProgress(taskId, new Date(), totalCount, processedCount, passedCount, errorCount);
    }

    /**
     * 结束数据锁。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     */
    private void unlock(final Long projectId, final ScheduledTaskCode code) {
        deleteRedisKey(String.format(START_AT_REDIS_KEY, projectId, code.name()));
    }

    /**
     * 删除定时任务日志。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param id        日志 ID
     */
    @Override
    public void remove(
        final Long projectId,
        final ScheduledTaskCode code,
        final Long id
    ) {
        unlock(projectId, code);
        scheduledTaskLogRepository.deleteById(id);
    }

    /**
     * 将定时任务标记为已完成。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param id        定时任务 ID
     * @param errorLog  错误日志
     * @param stoppedBy 执行中止操作的用户的 ID
     * @param status    执行状态
     */
    private void finish(
        final Long projectId,
        final ScheduledTaskCode code,
        final Long id,
        final String errorLog,
        final Long stoppedBy,
        final ScheduledTaskStatus status
    ) {

        unlock(projectId, code);

        ScheduledTaskLog scheduledTaskLog = scheduledTaskLogRepository
            .findByIdAndCode(id, code).orElse(null);

        if (scheduledTaskLog == null) {
            return;
        }

        if (stoppedBy == null) {
            scheduledTaskLog.setFinishedAt(new Date());
        } else {
            scheduledTaskLog.setStoppedAt(new Date());
            scheduledTaskLog.setStoppedBy(stoppedBy);
        }

        scheduledTaskLog.setErrorLog(errorLog);
        scheduledTaskLog.setStatus(status);

        scheduledTaskLogRepository.save(scheduledTaskLog);
    }

    /**
     * 将定时任务标记为已完成。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param id        日志 ID
     */
    @Override
    @Transactional
    public void finish(
        final Long projectId,
        final ScheduledTaskCode code,
        final Long id
    ) {
        finish(projectId, code, id, null, null, ScheduledTaskStatus.FINISHED);
    }

    /**
     * 中止任务。
     *
     * @param operator 操作者信息
     * @param id       定时任务 ID
     */
    @Override
    @Transactional
    public void stop(
        final OperatorDTO operator,
        final Long id
    ) {

        ScheduledTaskLog taskLog = scheduledTaskLogRepository.findById(id).orElse(null);

        if (taskLog == null) {
            throw new NotFoundError();
        }

        if (taskLog.getStatus() == ScheduledTaskStatus.STOPPED) {
            return;
        }

        if (taskLog.getStatus() != ScheduledTaskStatus.RUNNING) {
            throw new ConflictError("error.scheduled-task.not-running");
        }

        finish(taskLog.getProjectId(), taskLog.getCode(), id, null, operator.getId(), ScheduledTaskStatus.STOPPED);
    }

    /**
     * 将定时任务标记为失败。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param id        日志 ID
     * @param errorLog  错误日志
     */
    @Override
    @Transactional
    public void fail(
        final Long projectId,
        final ScheduledTaskCode code,
        final Long id,
        final String errorLog
    ) {
        finish(projectId, code, id, errorLog, null, ScheduledTaskStatus.FAILED);
    }

    /**
     * 添加错误记录。
     *
     * @param taskId    定时任务启动日志 ID
     * @param entity    数据实体信息
     * @param throwable 错误信息
     */
    @Override
    public void addError(
        final Long taskId,
        final BaseEntity entity,
        final Throwable throwable
    ) {

        ScheduledTaskError taskError = new ScheduledTaskError();
        taskError.setLogId(taskId);
        taskError.setItemId(entity.getId());
        taskError.setErrorType(throwable.getClass().toString());
        taskError.setError(throwable.getMessage());

        scheduledTaskErrorRepository.save(taskError);
    }

    /**
     * 取得定时任务启动日志列表。
     *
     * @param projectId 项目 ID
     * @param code      定时任务代码
     * @param status    定时任务执行状态
     * @param pageable  分页参数
     */
    @Override
    public Page<ScheduledTaskLog> list(
        final Long projectId,
        final ScheduledTaskCode code,
        final ScheduledTaskStatus status,
        final Pageable pageable
    ) {
        return scheduledTaskLogRepository
            .search(projectId, code, status, pageable);
    }

    /**
     * 取得错误信息。
     *
     * @param projectId 项目 ID
     * @param taskId    任务 ID
     * @param pageable  分页参数
     * @return 错误分页数据
     */
    @Override
    public Page<ScheduledTaskError> errors(
        final Long projectId,
        final Long taskId,
        final Pageable pageable
    ) {
        return scheduledTaskErrorRepository
            .findByLogIdOrderByIdAsc(taskId, pageable);
    }

}
