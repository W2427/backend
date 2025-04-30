package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

public enum ITPType implements ValueObject {

    HOLD_POINT("停工等待，必须检查"),
    WITNESS("通知检查，可以不来"),
    REVIEW("可以查看报告检查"),
    NON("不用检查");

    private String displayName;

    ITPType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
