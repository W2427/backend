package com.ose.docs.dto;

import com.ose.dto.BaseDTO;
import com.ose.dto.ContextDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 上传文件的附加信息。
 */
public class FilePostDTO extends BaseDTO {

    private static final long serialVersionUID = 8194574818310660466L;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "关键字列表")
    private List<String> keywords;

    @Schema(description = "标签列表")
    private List<String> tags;

    @Schema(description = "可见性")
    private List<String> visibility;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "上下文")
    private ContextDTO context;

    /**
     * 构造方法。
     */
    public FilePostDTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public List<String> getVisibility() {
        return visibility;
    }

    public void setVisibility(List<String> visibility) {
        this.visibility = visibility;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public ContextDTO getContext() {
        return context;
    }

    public void setContext(ContextDTO context) {
        this.context = context;
    }
}
