package com.ose.tasks.vo;

import com.ose.vo.ValueObject;

public enum RatedTimeField implements ValueObject {

    WBS_ENTITY_TYPE("WBS实体类型"),
    UNIT("单位"),
    PIPE_DIAMETER("管径"),
    MIN_PIPE_THICKNESS("最小厚度"),
    MAX_PIPE_THICKNESS("最大厚度"),
    MATERIAL("材料"),
    WELD_ENTITY_TYPE("焊接类型"),
    MIN_TEST_PRESSURE("最小测试压力"),
    MAX_TEST_PRESSURE("最大测试压力"),
    MEDIUM("介质"),
    CLEANING_MEDIUM("清洁介质");

    private String displayName;

    RatedTimeField(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }
}
