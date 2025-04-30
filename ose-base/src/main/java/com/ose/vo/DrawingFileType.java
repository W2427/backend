package com.ose.vo;

/**
 * DRAWING_FILE_TYPE
 */
public enum DrawingFileType implements ValueObject {

    ISSUE_FILE("提交文件"),
    OWNER_COMMENT("甲方意见"),
    OWNER_COMMENT_REPLY("回复甲方意见"),
    CLASS_COMMENT("第三方意见"),
    CLASS_COMMENT_REPLY("回复第三方意见"),
    OTHER("其他");

    private String displayName;

    DrawingFileType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return displayName;
    }

}
