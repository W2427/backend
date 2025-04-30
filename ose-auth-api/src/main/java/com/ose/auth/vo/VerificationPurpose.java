package com.ose.auth.vo;

import com.ose.vo.ValueObject;

/**
 * 电子邮件/短信验证目的值对象。
 */
public enum VerificationPurpose implements ValueObject {

    SIGN_UP("用户注册"),
    SIGN_IN("用户登录"),
    RESET_PASSWORD("重置登录密码");

    private String displayName;

    VerificationPurpose(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
