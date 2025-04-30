package com.ose.tasks.vo.bpm;

import com.ose.vo.ValueObject;

/**
 * 协调员分类。
 */
public enum ExInspScheduleCoordinateCategory implements ValueObject {

    COORDINATE("有协调员"),
    NO_COORDINATE("无协调员"),
    MIX("混合报验");

    private String displayName;

    ExInspScheduleCoordinateCategory(String displayName) {
        this.displayName = displayName;
    }

    //根据key获取枚举
    public static ExInspScheduleCoordinateCategory getEnumByCategory(String category) {
        for (ExInspScheduleCoordinateCategory temp : ExInspScheduleCoordinateCategory.values()) {
            if (temp.getDisplayName().equals(category)) {
                return temp;
            }
        }
        return null;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
