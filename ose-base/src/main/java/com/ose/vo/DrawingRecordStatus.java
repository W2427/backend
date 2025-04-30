package com.ose.vo;

/**
 * 数据实体状态值对象。
 */
public enum DrawingRecordStatus implements ValueObject {

    APPOINT("待指派"),
    FILL("待填写"),
    EXAMINE("待审核"),
    FINISH("已完成"),
    CANCEL("已退回");

    private String displayName;

    DrawingRecordStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
