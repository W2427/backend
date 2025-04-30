package com.ose.vo;

/**
 * 发货状态对象。
 */
public enum ProcurementStatus implements ValueObject {

    UN_PURCHASED("未采购"),
    PURCHASING("采购中"),
    ISSUED("已发货"),
    UN_ISSUED("未发货"),
    DELIVERED("已到货");

    private String displayName;

    ProcurementStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
