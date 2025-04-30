package com.ose.tasks.vo.trident;

import com.ose.vo.ValueObject;


public enum ItrType implements ValueObject {

    CHECK_LIST("CHECK_LIST"),
    CHECK_RECORD("CHECK_RECORD");

    private String displayName;

    ItrType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
