package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;

public class TagCreateDTO extends BaseDTO {

    private static final long serialVersionUID = 5014991628426707283L;

    @Schema(description = "上级 ID")
    private Long parentId;

    @Schema(description = "目标 ID")
    private Long targetId;

    @Schema(description = "文本名称")
    @NotBlank
    private String text;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
