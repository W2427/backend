package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class BatchTasksCategorySearchResultDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "流程id集合, BT3131")
    private List<Long> actInstIds;

    private List<TasksCategoryAssigneeDTO> assignees;


    public List<TasksCategoryAssigneeDTO> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<TasksCategoryAssigneeDTO> assignees) {
        this.assignees = assignees;
    }

    public List<Long> getActInstIds() {
        return actInstIds;
    }

    public void setActInstIds(List<Long> actInstIds) {
        this.actInstIds = actInstIds;
    }

}
