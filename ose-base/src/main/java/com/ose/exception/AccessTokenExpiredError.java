package com.ose.exception;

/**
 * 访问令牌过期错误。
 */
public class AccessTokenExpiredError extends UnauthorizedError {

    private static final long serialVersionUID = 8923970833337031897L;

    /**
     * 构造方法。
     */
    public AccessTokenExpiredError() {
        super("error.access-token-expired");
    }

}
