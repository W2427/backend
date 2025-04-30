package com.ose.tasks.vo.material;

import com.ose.vo.ValueObject;

/**
 * 检验结果类型。
 */
public enum InspectionType implements ValueObject {

    EXTERNAL("外检", "外部检验"),

    INTERNAL("内检", "内部检验");

    private String displayName;

    private String description;


    InspectionType(String displayName, String description) {
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
