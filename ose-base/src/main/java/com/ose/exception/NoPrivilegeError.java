package com.ose.exception;

import java.util.Set;

/**
 * 权限不足错误。
 */
public class NoPrivilegeError extends AccessDeniedError {

    private static final long serialVersionUID = -2110123033308023062L;

    // 所需权限
    private Set<String> privileges;

    /**
     * 构造方法。
     *
     * @param privileges 所需权限
     */
    public NoPrivilegeError(Set<String> privileges) {
        this();
        this.privileges = privileges;
    }

    /**
     * 构造方法。
     */
    public NoPrivilegeError() {
        super("error.no-privilege");
    }

    /**
     * 取得所需权限。
     *
     * @return 所需权限
     */
    public Set<String> getPrivileges() {
        return privileges;
    }

}
