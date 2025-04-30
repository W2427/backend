package com.ose.vo;

/**
 * 发货状态对象。
 */
public enum ShippingStatus implements ValueObject {

    ISSUED("已发货"),
    UN_ISSUED("未发货"),
    DELIVERED("已到货");

    private String displayName;

    ShippingStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
