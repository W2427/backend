package com.ose.tasks.vo.material;

import com.ose.vo.ValueObject;

/**
 * 材料在库状态类型。
 */
public enum FItemStatusType implements ValueObject {

    // 入库
    RECV("入库", "入库"),

    // 出库
    ISSUE("出库", "出库"),

    // 退库
    RETURN("退库", "退库");

    private String displayName;

    private String description;

    FItemStatusType(String displayName, String description) {
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
