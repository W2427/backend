package com.ose.tasks.vo.drawing;

import com.ose.vo.ValueObject;

/**
 * 图纸类型。
 */
public enum DrawingType implements ValueObject {

    DRAWING("DRAWING", "图纸"),
    SUB_DRAWING("SUB_DRAWING", "子图纸");

    private String displayName;

    private String description;

    DrawingType(String displayName, String description) {
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
