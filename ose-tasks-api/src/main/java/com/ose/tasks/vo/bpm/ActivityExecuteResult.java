package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 * 工作流实体执行结果
 */
public enum ActivityExecuteResult implements ValueObject {

    INIT("初始化"),
    OK("成功"),
    NG("失败");

    private String displayName;

    ActivityExecuteResult(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
