package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class BatchTasksCategoryAssigneeDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "流程id集合, BT123323")
    private List<Long> actInstIds;

    @Schema(description = "权限")
    private String category;

    @Schema(description = "分配人员Id")
    private Long assignee;

    @Schema(description = "是否覆盖")
    private boolean covered;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(boolean covered) {
        this.covered = covered;
    }

    public List<Long> getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(List<Long> actInstIds) {
        this.actInstIds = actInstIds;
    }
}
