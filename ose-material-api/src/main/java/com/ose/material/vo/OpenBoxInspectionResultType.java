package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * OSD类型。
 */
public enum OpenBoxInspectionResultType implements ValueObject {

    ACCEPTANCE("接受", "接受"),

    OPINION("有意见", "有意见"),

    REJECTION("拒收", "拒收");

    private String displayName;

    private String description;


    OpenBoxInspectionResultType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

}
