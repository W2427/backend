package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * 材料锁定状态
 */
public enum MaterialLockedStatus implements ValueObject {
    LOCKED("已锁定", "已锁定"),
    UNLOCKED("未锁定", "未锁定");

    private String displayName;

    private String description;

    MaterialLockedStatus(String displayName, String description) {
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
