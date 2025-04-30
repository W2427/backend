package com.ose.vo;

/**
 * 入库单类型对象。
 */
public enum MaterialReceiveType implements ValueObject {

    MM_BFE("船厂采购"),
    MM_OFE("总包方采购"),
    MM_ODE("甲方采购");

    private String displayName;

    MaterialReceiveType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
