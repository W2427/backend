package com.ose.docs.dto;

import com.ose.docs.vo.FileCategory;
import com.ose.docs.vo.FileESType;
import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Set;

/**
 * 文件查询条件数据传输对象。
 */
public class FileCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = 2400331685340702773L;

    @Schema(description = "文件的业务类型")
    private Set<FileESType> bizType;

    @Schema(description = "文件类型")
    private List<FileCategory> category;

    @Schema(description = "文件 ID")
    private String id;

    @Schema(description = "关键字")
    private List<String> keyword;

    @Schema(description = "标签")
    private List<String> tag;

    @Schema(description = "内容（全文检索）")
    private String content;

    @Schema(description = "提交者 ID")
    private String committedBy;

    public Set<FileESType> getBizType() {
        return bizType;
    }

    public void setBizType(Set<FileESType> bizType) {
        this.bizType = bizType;
    }

    public List<FileCategory> getEntitySubType() {
        return category;
    }

    public void setEntitySubType(List<FileCategory> category) {
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getKeyword() {
        return keyword;
    }

    public void setKeyword(List<String> keyword) {
        this.keyword = keyword;
    }

    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCommittedBy() {
        return committedBy;
    }

    public void setCommittedBy(String committedBy) {
        this.committedBy = committedBy;
    }

}
