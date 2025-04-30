package com.ose.docs.entity;

/**
 * 文件查询视图。
 */
public interface FileView {

    /**
     * 设置文件业务类型。
     */
    FileView setBizType(String bizType);

    /**
     * 设置查询匹配文本。
     */
    FileView setMatchedText(String matchedText);

}
