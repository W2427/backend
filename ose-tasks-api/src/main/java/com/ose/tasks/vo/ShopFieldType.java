package com.ose.tasks.vo;


import com.ose.vo.ValueObject;

/**
 * 实施位置类型。
 */
public enum ShopFieldType implements ValueObject {

    SHOP("SHOP"),
    FIELD("FIELD"),
    FFW("FFW");

    private String displayName;

    ShopFieldType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
