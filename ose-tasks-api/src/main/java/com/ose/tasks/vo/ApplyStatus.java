package com.ose.tasks.vo;

import com.ose.vo.ValueObject;


public enum ApplyStatus implements ValueObject {


    ACCEPT("ACCEPT"),
    UNAPPROVED("UNAPPROVED"),
    REJECT("REJECT");

    private String displayName;

    ApplyStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
