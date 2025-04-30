package com.ose.docs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

/**
 * 文件 ES 数据实体。
 */
public class FileES extends FileBaseES {

    private static final long serialVersionUID = -649504262090474146L;

    @Schema(description = "关键字列表")
    @Field(type = FieldType.Nested)
    private List<String> keywords;

    @Schema(description = "标签列表")
    @Field(type = FieldType.Nested)
    private List<String> tags;

    @Schema(description = "备注")
    @Field(type = FieldType.Text)
    private String remarks;

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

}
