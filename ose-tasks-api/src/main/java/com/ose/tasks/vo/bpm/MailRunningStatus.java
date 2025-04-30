package com.ose.tasks.vo.bpm;

/**
 * 挂起状态。
 */
public enum MailRunningStatus {

    INIT("初始状态"),
    RUNNING("运行状态"),
    SENT("已发送");

    private String displayName;

    MailRunningStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
