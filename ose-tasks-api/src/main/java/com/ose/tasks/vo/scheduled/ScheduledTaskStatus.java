package com.ose.tasks.vo.scheduled;

import com.ose.vo.ValueObject;

/**
 * 定时任务执行状态。
 */
public enum ScheduledTaskStatus implements ValueObject {

    RUNNING("运行中"),
    STOPPED("已中止"),
    FAILED("失败"),
    FINISHED("已完成"),
    TIMEOUT("超时");

    private String displayName;

    ScheduledTaskStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
