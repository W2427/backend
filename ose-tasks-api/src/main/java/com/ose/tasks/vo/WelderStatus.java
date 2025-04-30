package com.ose.tasks.vo;


import com.ose.vo.ValueObject;

public enum WelderStatus implements ValueObject {

    DEACTIVATE("已停用"),
    EXPIRED("已过期"),
    NORMAL("正常");

    private String displayName;

    WelderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
