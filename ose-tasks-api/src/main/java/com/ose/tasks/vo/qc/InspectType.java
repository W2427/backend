package com.ose.tasks.vo.qc;

import com.ose.vo.ValueObject;

public enum InspectType implements ValueObject {

    EXTERNAL("外检"),
    INTERNAL("内检"),
    NONE("非检验");


    private String displayName;

    InspectType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
