package com.ose.tasks.dto.worksite;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * 工作场地更新数据传输对象。
 */
public class WorkSiteMoveDTO extends WorkSiteSortDTO {

    private static final long serialVersionUID = -1612777581554036964L;

    @Schema(description = "目标上级 ID")
    @NotNull
    @NotEmpty
    private Long parentId;

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

}
