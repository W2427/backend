package com.ose.tasks.vo.wbs;

import com.ose.vo.ValueObject;

/**
 * 实体添加或者删除后，实体对应更新计划的状态
 */
public enum WBSEntryExecutionState implements ValueObject {

    UNDO("UNDO"),
    DOING("DOING"),
    DONE("DONE");

    private String displayName;

    WBSEntryExecutionState(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
