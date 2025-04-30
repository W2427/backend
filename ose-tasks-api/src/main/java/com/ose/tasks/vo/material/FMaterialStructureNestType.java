package com.ose.tasks.vo.material;

import com.ose.vo.ValueObject;

/**
 * 结构套料清单类型。
 */
public enum FMaterialStructureNestType implements ValueObject {

    // 型材
    PROFILES("型材", "型材"),

    // 板材
    PLATES("板材", "板材");

    private String displayName;

    private String description;

    FMaterialStructureNestType(String displayName, String description) {
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
