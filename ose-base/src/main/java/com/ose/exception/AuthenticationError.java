package com.ose.exception;

/**
 * 认证失败错误。
 */
public class AuthenticationError extends UnauthorizedError {

    private static final long serialVersionUID = -3319441007439434768L;

    /**
     * 构造方法。
     */
    public AuthenticationError() {
        super("error.invalid-credentials");
    }

}
