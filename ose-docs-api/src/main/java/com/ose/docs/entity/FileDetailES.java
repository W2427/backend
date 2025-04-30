package com.ose.docs.entity;

import com.ose.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 文件 ES 数据实体。
 */
public class FileDetailES extends FileES {

    private static final long serialVersionUID = 1170417417100886203L;

    @Schema(description = "文件内容")
    @Field(type = FieldType.Text, analyzer = "ik_smart", searchAnalyzer = "ik_smart")
    private String content = "";

    @Override
    public void setName(String name) {
        super.setName(name);
        addContent(name);
    }

    @Override
    public void setRemarks(String remarks) {
        super.setRemarks(remarks);
        addContent(remarks);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 添加内容（全文检索用）。
     *
     * @param content 内容
     */
    public void addContent(String content) {

        if (StringUtils.isEmpty(content)) {
            return;
        }

        this.content += (StringUtils.isEmpty(this.content) ? "" : "\r\n\r\n") + content;
    }

}
