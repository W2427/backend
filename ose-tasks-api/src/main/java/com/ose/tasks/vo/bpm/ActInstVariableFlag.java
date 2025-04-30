package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 * 挂起状态。
 */
public enum ActInstVariableFlag implements ValueObject {

    EDIT("EDIT", "可编辑"),
    DISPLAY("DISPLAY", "可显示");

    private String displayName;

    private String description;

    ActInstVariableFlag(String displayName, String description) {
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
