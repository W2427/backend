package com.ose.tasks.vo.drawing;

import com.ose.vo.ValueObject;

/**
 * 图纸类型。
 */
public enum DocScope implements ValueObject {

    //"文档分类，详设文件，项目文件，加设文件，调试程序，厂家资料，完工文档包"
    DD_DOC("DD_DOC", "详设文件"),
    SD_DOC("SD_DOC", "加设文件"),
    PM_DOC("PM_DOC", "项目文件"),
    TCP_DOC("TCP_DOC", "调试程序"),
    MARKUP_DOC("MARKUP_DOC", "标记图纸"),
    VCD_DOC("VCD_DOC", "厂家资料"),
    CP_DOC("CP_DOC", "完工文档包");

    private String displayName;

    private String description;

    DocScope(String displayName, String description) {
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
