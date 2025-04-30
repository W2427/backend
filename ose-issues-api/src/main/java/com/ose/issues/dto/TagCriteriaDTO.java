package com.ose.issues.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

public class TagCriteriaDTO extends BaseDTO {

    private static final long serialVersionUID = -8669941532640083007L;

    @Schema(description = "目标ID")
    private Long targetId;

    @Schema(description = "上级ID")
    private Long parentId;

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}
