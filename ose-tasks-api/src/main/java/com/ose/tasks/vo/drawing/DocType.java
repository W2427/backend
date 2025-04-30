package com.ose.tasks.vo.drawing;

import com.ose.vo.ValueObject;

/**
 * 图纸类型。
 */
public enum DocType implements ValueObject {

    //"图类型，包括PDF，NATIVE，ATTACHMENT，CLASS_COMMENT，OWNER_COMMENT"
    PDF("PDF", "PDF文件"),
    ATTACHMENT("ATTACHMENT", "附件"),
    CLASS_COMMENT("CLASS_COMMENT", "船级社意见"),
    OWNER_COMMENT("OWNER_COMMENT", "船东意见"),
    CC_REPLY("CC_REPLY", "船检意见回复"),
    OC_REPLY("OC_REPLY", "船东意见回复"),
    MARKUP("MARKUP", "图纸标记文件"),
    NATIVE("NATIVE", "原始文件");

    private String displayName;

    private String description;

    DocType(String displayName, String description) {
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
