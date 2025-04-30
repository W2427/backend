package com.ose.tasks.vo.setting;

import com.ose.vo.ValueObject;

/**
 * 批处理任务执行状态。
 */
public enum BatchTaskStatus implements ValueObject {

    RUNNING("执行中"),
    STOPPED("手动中止"),
    FINISHED("正常结束"),
    FAILED("异常结束");

    private String displayName;

    BatchTaskStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
