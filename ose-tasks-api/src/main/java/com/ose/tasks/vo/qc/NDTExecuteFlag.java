package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

/**
 * NDT执行结果。
 */
public enum NDTExecuteFlag implements ValueObject {

    ///***************************
    //* 要非常注意,有问题联系(白东旭，或者wangzhen)
    //* 下面的枚举代码顺序不能改变,因为已经映射到  [entity_weld] 里是 index0，1，2，非 state 的0，1，2
    //*  追加只能顺序追加，不能插入，并且要顺序设定，不能跳跃设定，比如 0，1，2， 9，必须设定为0，1，2，3
    //***************************
    SKIP(0, "跳过NDT"),
    EXECUTED(1, "已经执行NDT"),
    FLARING_EXECUTED(2, "扩口执行NDT");

    private int type;
    private String displayName;

    NDTExecuteFlag(int type, String displayName) {
        this.type = type;
        this.displayName = displayName;
    }

    public int getType() {
        return type;
    }

    @Override
    public String getDisplayName() {
        // TODO Auto-generated method stub
        return displayName;
    }

}
