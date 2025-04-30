package com.ose.vo;


/**
 * 外检的检查方。
 */
public enum InspectParty implements ValueObject {

    //业主方
    OWNER("业主"),
    //第三方船检
    THIRD_PARTY("第三方"),
    //其他方
    OTHER("其他方");

    private String displayName;

    InspectParty(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
