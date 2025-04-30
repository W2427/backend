package com.ose.tasks.vo.trident;

import com.ose.vo.ValueObject;


public enum CheckSheetTridentTbl implements ValueObject {

    A_CHECK_SHEET("A_CHECK_SHEET"),
    B_CHECK_SHEET("B_CHECK_SHEET");

    private String displayName;

    CheckSheetTridentTbl(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
