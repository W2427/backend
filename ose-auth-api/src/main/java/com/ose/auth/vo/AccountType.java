package com.ose.auth.vo;

import com.ose.vo.ValueObject;

/**
 * 账号类型值对象。
 */
public enum AccountType implements ValueObject {

    EMAIL("电子邮箱注册账户"),
    MOBILE("手机号码注册用户");

    private String displayName;

    AccountType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
