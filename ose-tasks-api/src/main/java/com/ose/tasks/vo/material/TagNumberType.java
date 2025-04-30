package com.ose.tasks.vo.material;

import com.ose.util.StringUtils;
import com.ose.vo.ValueObject;

/**
 * 材料类型。
 */
public enum TagNumberType implements ValueObject {

    // 一物一码
    GOODS("一物一码", "一物一码"),

    // 一类一码
    TYPE("一类一码", "一类一码");


    private String displayName;

    private String description;


    TagNumberType(String displayName, String description) {
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

    public static TagNumberType getByDisplayName(String displayName) {

        if (StringUtils.isEmpty(displayName)) {
            return null;
        }

        for (TagNumberType type : TagNumberType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(displayName)) {
                return type;
            }
        }
        return null;
    }

}
