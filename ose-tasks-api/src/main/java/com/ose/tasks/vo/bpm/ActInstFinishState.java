package com.ose.tasks.vo.bpm;

/**
 * 挂起状态。
 */
public enum ActInstFinishState {

    //***************************
    //* 要非常注意,有问题联系wangzhen
    //* 下面的枚举代码顺序不能改变,因为已经映射到  [bpm_activity_instance_base] 里是 index0，1，2，非 state 的0，1，2
    //***************************
    NO_VALUE(0, "NO_VALUE"),
    NOT_FINISHED(1, "NOT_FINISHED"),
    FINISHED(2, "FINISHED");

    private int state;

    private String displayName;

    ActInstFinishState(int state, String displayName) {

        this.state = state;
        this.displayName = displayName;
    }

    public int getState() {
        return state;
    }

}
