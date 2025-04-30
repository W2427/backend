package com.ose.tasks.vo;

import com.ose.vo.ValueObject;

/**
 * 压强附加信息。
 */
public enum PressureExtraInfo implements ValueObject {

    // Full Vacuum
    FV("真空"),

    // Air Traffic Management
    ATM("常压");

    private String displayName;

    PressureExtraInfo(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
