package com.ose.auth.vo;

import com.ose.vo.ValueObject;

/**
 * 资源类型。
 */
public enum ResourceType implements ValueObject {

    NONE("未指定"),
    USER("用户"),
    GROUP("组织"),
    DOCUMENT("文档");

    private String displayName;

    ResourceType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
