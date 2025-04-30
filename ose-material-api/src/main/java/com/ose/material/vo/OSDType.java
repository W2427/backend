package com.ose.material.vo;

import com.ose.vo.ValueObject;

/**
 * OSD类型。
 */
public enum OSDType implements ValueObject {

    NORMAL("正常", "正常"),

    OVER("超出", "超出"),

    SHORTAGE("缺失", "缺失"),

    DAMAGE("损坏", "损坏");

    private String displayName;

    private String description;


    OSDType(String displayName, String description) {
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
