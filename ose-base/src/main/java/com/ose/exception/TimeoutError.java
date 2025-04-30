package com.ose.exception;

import org.springframework.http.HttpStatus;

/**
 * 请求超时错误。
 */
public class TimeoutError extends BaseError {

    private static final long serialVersionUID = -5954658070774235849L;

    /**
     * 构造方法。
     */
    public TimeoutError() {
        super("error.timeout", HttpStatus.REQUEST_TIMEOUT);
    }

}
