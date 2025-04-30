package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * 实体状态。
 */
public enum NestGateWay implements ValueObject {

    SURPLUS_NEST("余料套料"),
    NONE("无"),
    NEST("套料");

    private String displayName;

    NestGateWay(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
