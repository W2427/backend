package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 * 挂起状态。
 */
public enum ActInstVariableType implements ValueObject {

    DATE("DATE", "日期类型"),
    STRING("STRING", "字符类型"),
    INTEGER("INTEGER", "数字类型");

    private String displayName;

    private String description;

    ActInstVariableType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }
}
