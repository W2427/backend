package com.ose.tasks.vo.bpm;

/**
 * 挂起状态。
 */
public enum ExInspApplyStatus {

    APPLY("申请中"),
    DONE("确认完了");

    private String displayName;

    ExInspApplyStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
