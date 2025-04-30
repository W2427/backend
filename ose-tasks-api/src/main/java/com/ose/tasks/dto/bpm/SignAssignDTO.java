package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 实体管理 数据传输对象
 */
public class SignAssignDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    @Schema(description = "任务id")
    private String taskId;

    @Schema(description = "会签人员")
    private List<Long> assignees;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<Long> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<Long> assignees) {
        this.assignees = assignees;
    }
}
