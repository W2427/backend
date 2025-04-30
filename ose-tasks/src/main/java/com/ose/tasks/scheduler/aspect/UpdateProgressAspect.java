package com.ose.tasks.scheduler.aspect;

import com.ose.entity.BaseEntity;
import com.ose.tasks.scheduler.annotation.UpdateProgress;
import com.ose.tasks.scheduler.base.BaseScheduler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UpdateProgressAspect {

    /**
     * 定义切入点：使用 @UpdateProgress 注解的方法。
     */
    @Pointcut("@annotation(annotation)")
    public void controller(final UpdateProgress annotation) {
    }

    /**
     * 执行数据处理，更新定时任务进度。
     *
     * @param point      切入点信息
     * @param annotation 注解设置
     */
    @Around(value = "controller(annotation)", argNames = "point,annotation")
    public Object doAfter(
        final ProceedingJoinPoint point,
        final UpdateProgress annotation
    ) {

        final BaseScheduler scheduler = (BaseScheduler) point.getTarget();
        final Object[] parameterValues = point.getArgs();

        Boolean result = null;

        try {
            scheduler.updateProgress(result = (Boolean) point.proceed());
        } catch (Throwable e) {
            final BaseEntity entity = (BaseEntity) parameterValues[1];
            scheduler.updateProgress(entity, e);
        }

        return result;
    }

}
