package com.ose.exception;

import org.springframework.http.HttpStatus;

/**
 * 未授权错误。
 */
public class UnauthorizedError extends BaseError {

    private static final long serialVersionUID = 52593004496429873L;

    /**
     * 构造方法。
     */
    public UnauthorizedError() {
        this("error.unauthorized");
    }

    /**
     * 构造方法。
     */
    public UnauthorizedError(String errorCode) {
        super(errorCode, HttpStatus.UNAUTHORIZED);
    }

}
