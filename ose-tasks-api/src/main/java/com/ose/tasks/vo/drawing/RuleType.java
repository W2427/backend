package com.ose.tasks.vo.drawing;

import com.ose.vo.ValueObject;

/**
 * 图纸类型。
 */
public enum RuleType implements ValueObject {

    FIXED_CHARACTER("FIXED_CHARACTER", "固定字符"),
    INCREMENTING_NUMBERS("INCREMENTING_NUMBERS", "数字递增"),
    INCREMENTING_LETTERS("INCREMENTING_LETTERS", "字母递增");

    private String displayName;

    private String description;

    RuleType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

}
