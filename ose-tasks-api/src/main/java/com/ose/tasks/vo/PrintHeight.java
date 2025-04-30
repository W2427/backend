package com.ose.tasks.vo;

import com.ose.vo.ValueObject;


public enum PrintHeight implements ValueObject {

    GREAT("大"),
    SMALL("小");

    private String displayName;

    PrintHeight(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
