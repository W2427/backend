package com.ose.tasks.vo.trident;

import com.ose.vo.ValueObject;


public enum CheckSheetStage implements ValueObject {

    ACHECKSHEET("A"),//pre-comm
    MCHECKSHEET("M"),//mc
    BCHECKSHEET("B");// comm

    private String displayName;

    CheckSheetStage(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
