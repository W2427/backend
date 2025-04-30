package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 * 实体类型规则分类
 */
public enum CategoryRuleType implements ValueObject {

    INCLUDE_PARENT("包含父级"),
    INCLUDE_VALUE("包含配置值"),
    INCLUDE_NON("不存在规则");

    private String displayName;

    CategoryRuleType(String displayName) {

        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
