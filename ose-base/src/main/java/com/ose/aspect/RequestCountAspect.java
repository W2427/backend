package com.ose.aspect;

import com.ose.exception.ServiceUnavailableError;
import com.ose.util.ConsoleUtils;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * 处理中的请求计数切面。
 */
@Aspect
@Component
public class RequestCountAspect extends BaseAspect {

    private static boolean suspended = false;
    private static boolean closing = false;
    private static long requestCount = 0;
    private static Runnable shutdownRunnable = null;

    /**
     * 服务是否处于【暂停】的状态。
     */
    public static boolean isSuspended() {
        return suspended;
    }

    /**
     * 将服务的状态更新为【暂停】。
     */
    public static void suspend() {
        suspended = true;
    }

    /**
     * 服务是否处于【准备关闭】的状态。
     */
    public static boolean isClosing() {
        return closing;
    }

    /**
     * 将服务的状态更新为【准备关闭】。
     *
     * @param runnable 关闭逻辑
     */
    public static void close(final Runnable runnable) {
        suspended = true;
        closing = true;
        shutdownRunnable = runnable;
        shutdown();
    }

    /**
     * 恢复服务。
     * 将服务从【暂停】或【准备关闭】状态恢复到【正常】状态。
     */
    public static void restore() {
        suspended = false;
        closing = false;
    }

    /**
     * 取得处理中的请求数量。
     *
     * @return 处理中的请求数量
     */
    public static long getRequestCount() {
        return requestCount;
    }

    /**
     * 关闭服务。
     */
    private static void shutdown() {
        if (closing && requestCount == 0 && shutdownRunnable != null) {
            (new Thread(() -> {
                ConsoleUtils.warn(" SPRING APPLICATION WILL BE CLOSED IN 10 SECONDS ");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // nothing to do
                }
                if (closing) {
                    ConsoleUtils.warn(" SPRING APPLICATION IS SHUTTING DOWN ");
                    shutdownRunnable.run();
                }
            })).start();
        }
    }

    /**
     * 定义切入点：RestController 客户端请求处理方法。
     */
    @Pointcut(
        "(execution(public com.ose.response.* com.ose.*.controller..*.*(..))"
            + " || execution(public com.ose.response.* com.ose.controller..*.*(..)))"
            + " && !execution(public * com.ose.controller.ServerController.*(..))"
            + " && !@annotation(org.springframework.web.bind.annotation.ExceptionHandler)"
    )
    public void controller() {
    }

    /**
     * 处理开始。
     * 若服务处于【暂停】状态则返回【服务不可用】错误。
     */
    @Before(value = "controller()")
    public void doBefore() {

        if (suspended) {
            throw new ServiceUnavailableError();
        }

        requestCount++;
    }

    /**
     * 处理结束。
     */
    @After(value = "controller()")
    public void doAfter() {
        requestCount = Math.max(0L, requestCount - 1);
        shutdown();
    }

    /**
     * 处理结束（正常）。
     */
    @AfterReturning(value = "controller()")
    public void doAfterReturning() {
    }

    /**
     * 处理结束（错误）。
     */
    @AfterThrowing(value = "controller()")
    public void doAfterThrowing() {
    }

}
