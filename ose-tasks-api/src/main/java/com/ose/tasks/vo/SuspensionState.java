package com.ose.tasks.vo;

import com.ose.vo.ValueObject;

/**
 * 挂起状态。
 */
public enum SuspensionState implements ValueObject {

    //***************************
    //* 要非常注意,有问题联系wangzhen
    //* 下面的枚举代码顺序不能改变,因为已经映射到  [bpm_activity_instance_base] 里是 index0，1，2，非 state 的0，1，2
    //***************************
    NO_VALUE(0,"NO_VALUE"),
    ACTIVE(1,"ACTIVE"),
    SUSPEND(2,"SUSPEND"),
    SUB_RUNNING(3, "SUB_RUNNING");//子流程运行中

    private int state;

    private String displayName;

    SuspensionState(int state, String displayName) {

        this.state = state;
        this.displayName = displayName;
    }

    public int getState() {
        return state;
    }


    @Override
    public String getDisplayName() {
        return name();
    }
}
