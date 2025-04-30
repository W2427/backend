package com.ose.tasks.dto.bpm;


import com.ose.dto.BaseDTO;


public class ModelTaskNode extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4402058499392872999L;

    //担当者权限
    private String category;

    // 任务定义key
    private String taskDefKey;

    // 任务名称
    private String taskName;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

}
