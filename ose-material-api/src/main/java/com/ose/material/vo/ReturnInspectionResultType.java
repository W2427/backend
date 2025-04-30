package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * 退库检验结果类型。
 */
public enum ReturnInspectionResultType implements ValueObject {

    RECEIVE("接收", "接收"),

    REJECT("拒收", "拒收");

    private String displayName;

    private String description;

    ReturnInspectionResultType(String displayName, String description) {
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
