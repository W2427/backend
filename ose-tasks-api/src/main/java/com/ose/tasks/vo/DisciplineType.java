package com.ose.tasks.vo;

import com.ose.vo.ValueObject;

public enum DisciplineType implements ValueObject {

    ST("ST"),
    OU("OU"),
    EQ("EQ"),
    E_I("E&I"),
    PI("PI"),
    VT("VT");

    private String displayName;

    DisciplineType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
