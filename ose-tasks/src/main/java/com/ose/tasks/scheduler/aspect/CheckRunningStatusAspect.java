package com.ose.tasks.scheduler.aspect;

import com.ose.tasks.domain.model.service.scheduled.ScheduledTaskLogInterface;
import com.ose.tasks.entity.Project;
import com.ose.tasks.scheduler.annotation.CheckRunningStatus;
import com.ose.tasks.scheduler.base.BaseScheduler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CheckRunningStatusAspect {


    private final ScheduledTaskLogInterface scheduledTaskLogService;

    /**
     * 构造方法。
     */
    @Autowired
    public CheckRunningStatusAspect(ScheduledTaskLogInterface scheduledTaskLogService) {
        this.scheduledTaskLogService = scheduledTaskLogService;
    }

    /**
     * 定义切入点：使用 @CheckRunningStatus 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(final CheckRunningStatus annotation) {
    }

    /**
     * 在定时任务执行前检查定时任务的执行状态，并在执行前后更新定时任务执行日志。
     *
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Around(value = "controller(annotation)", argNames = "point,annotation")
    public Object doAfter(
        final ProceedingJoinPoint point,
        final CheckRunningStatus annotation
    ) {
        final BaseScheduler scheduler = (BaseScheduler) point.getTarget();
        final Object[] parameterValues = point.getArgs();
        final Class returnType = ((MethodSignature) point.getSignature()).getReturnType();
        final Project project;
        final Long projectId;

        if (parameterValues.length == 1
            && parameterValues[0] != null
            && parameterValues[0] instanceof Project) {
            project = (Project) parameterValues[0];
            projectId = project.getId();
        } else {
            project = null;
            projectId = null;
        }

        Long taskId = null;
        Object result = returnType == Integer.class ? 0 : null;

        try {

            final Integer totalCount;

            if (project != null) {
                totalCount = scheduler.count(project);
            } else {
                totalCount = scheduler.count();
            }


            if (totalCount != null && totalCount == 0) {
                return result;
            }


            scheduler.setTaskId(taskId = scheduledTaskLogService.start(
                projectId,
                scheduler.getCode()).getId()
            );


            scheduler.resetCounters();


            if (totalCount != null && totalCount > 0) {
                scheduler.setTotalCount(totalCount);
            }


            result = point.proceed();


            scheduler.finish(projectId, taskId);

        } catch (Throwable e) {

            scheduler.fail(projectId, taskId, e);
        }

        return result;
    }

}
