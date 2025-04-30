package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 *
 */
public enum BpmPlanExecutionState implements ValueObject {

    UNDO("UNDO"),
    DOING("DOING"),
    DONE("DONE"),
    NOT_FOUND("NOT_FOUND");

    private String displayName;

    BpmPlanExecutionState(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
