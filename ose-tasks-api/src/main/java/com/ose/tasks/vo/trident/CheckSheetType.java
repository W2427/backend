package com.ose.tasks.vo.trident;

import com.ose.vo.ValueObject;


public enum CheckSheetType implements ValueObject {

    CHECK_LIST("CHECK_LIST"),
    COVER("COVER"),
    TAG_LIST("TAG_LIST"),
    CHECK_RECORD("CHECK_RECORD");

    private String displayName;

    CheckSheetType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
