package com.ose.feign;

import feign.RetryableException;
import feign.Retryer;
import org.springframework.stereotype.Component;

/**
 * Feign 请求重试逻辑。
 */
@Component
public class RequestRetryer implements Retryer {

    // 最大尝试次数
    private static final int MAX_ATTEMPTS = 5;

    // 初始重试时间间隔
    private static final long INIT_INTERVAL = 100;

    // 已尝试次数
    private int attemptTimes = 0;

    /**
     * 构造方法。
     */
    public RequestRetryer() {
    }

    /**
     * 是否重试或结束。
     *
     * @param e 异常
     */
    @Override
    public void continueOrPropagate(RetryableException e) {

        if (attemptTimes++ > MAX_ATTEMPTS) {
            throw e;
        }

        try {
            Thread.sleep((long) (INIT_INTERVAL * Math.pow(2, attemptTimes)));
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

    }

    /**
     * 克隆。
     *
     * @return 请求重试处理实例
     */
    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public Retryer clone() {
        return new RequestRetryer();
    }

}
