package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 * 工序类型 清单类，切割清单类，个体类
 */
public enum ProcessType implements ValueObject {

    DELIVERY_LIST("清单类"),
    CUT_LIST("下料单类"),
    MONO_CONSTRUCT("建造单实体"),
    MONO_OTHERS("其他单实体");

    private String displayName;

    ProcessType(String displayName) {

        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
