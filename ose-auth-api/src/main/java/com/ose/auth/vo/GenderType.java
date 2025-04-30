package com.ose.auth.vo;

import com.ose.vo.ValueObject;

public enum GenderType implements ValueObject {

    MALE("男性"),
    FEMALE("女性");

    private String displayName;

    GenderType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
