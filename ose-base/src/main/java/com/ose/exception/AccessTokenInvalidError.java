package com.ose.exception;

/**
 * 访问令牌无效错误。
 */
public class AccessTokenInvalidError extends UnauthorizedError {

    private static final long serialVersionUID = 2681302667596581138L;

    /**
     * 构造方法。
     */
    public AccessTokenInvalidError() {
        super("error.access-token-invalid");
    }

}
