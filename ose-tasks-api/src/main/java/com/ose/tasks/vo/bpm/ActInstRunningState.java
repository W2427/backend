package com.ose.tasks.vo.bpm;

/**
 * 运行状态状态。
 */
public enum ActInstRunningState {

    //***************************
    //* 要非常注意,有问题联系wangzhen
    //* 下面的枚举代码顺序不能改变,因为已经映射到  [bpm_activity_instance_base]
    //***************************
    NO_VALUE(0, "NO_VALUE"),
    NOT_FINISHED(1, "NOT_FINISHED"),
    FINISHED(2, "FINISHED");

    private int state;

    private String displayName;

    ActInstRunningState(int state, String displayName) {

        this.state = state;
        this.displayName = displayName;
    }

    public int getState() {
        return state;
    }

}
