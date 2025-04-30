package com.ose.tasks.vo.wbs;

import com.ose.vo.ValueObject;

/**
 * 实体状态。
 */
public enum WBSEntityStatus implements ValueObject {

    NOT_DELETED("正常或已取消"),
    NORMAL("正常"),
    DELETED("已删除未取消"),
    CANCELLED("已取消"),
    DELETED_CANCELLED("已删除或已取消");

    private String displayName;

    WBSEntityStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
