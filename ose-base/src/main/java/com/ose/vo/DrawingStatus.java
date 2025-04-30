package com.ose.vo;

/**
 * @author: DaiZeFeng
 * @date: 2023/2/20
 */
public enum DrawingStatus implements ValueObject {

    PENDING("未指派"),
    UNFINISHED("未完成"),
    FINISHED("已完成");

    private final String displayName;

    DrawingStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
