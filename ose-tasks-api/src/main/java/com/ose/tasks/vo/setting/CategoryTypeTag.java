package com.ose.tasks.vo.setting;

import com.ose.vo.ValueObject;

/**
 * 焊口类型。
 */
public enum CategoryTypeTag implements ValueObject {

    READONLY("READONLY"),
    BUSINESS("BUSINESS");

    private String displayName;

    CategoryTypeTag(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
