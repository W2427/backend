package com.ose.tasks.vo;

import com.ose.vo.ValueObject;

public enum RatedTimeUnit implements ValueObject {

    M("长度米"),
    N("个数");

    private String displayName;

    RatedTimeUnit(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
