package com.ose.exception;

import org.springframework.http.HttpStatus;

/**
 * 访问拒绝。
 */
public class AccessDeniedError extends BaseError {

    private static final long serialVersionUID = 5012253355903728775L;

    /**
     * 构造方法。
     */
    public AccessDeniedError() {
        this("error.access-denied");
    }

    /**
     * 构造方法。
     */
    public AccessDeniedError(String errorCode) {
        super(errorCode, HttpStatus.FORBIDDEN);
    }

}
