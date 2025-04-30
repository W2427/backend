package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * 采购状态。
 */
public enum PurchasedStatus implements ValueObject {


    PURCHASED("PURCHASED", "已采购"),
    PURCHASED_NOT("PURCHASED_NOT", "未采购"),
    PURCHASED_PART("PURCHASED_PART", "部分采购");

    private String code;
    String displayName;

    PurchasedStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    public String getCode() {
        return code;
    }

}
