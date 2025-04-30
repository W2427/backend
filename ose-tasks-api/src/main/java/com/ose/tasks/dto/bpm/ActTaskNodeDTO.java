package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

public class ActTaskNodeDTO extends BaseDTO {

    private static final long serialVersionUID = 8194574818310660466L;

    private String taskCategory;

    private String taskDefKey;

    private String taskNodeName;

    private int orderNo;

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getTaskNodeName() {
        return taskNodeName;
    }

    public void setTaskNodeName(String taskNodeName) {
        this.taskNodeName = taskNodeName;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public String getTaskCategory() {
        return taskCategory;
    }

    public void setTaskCategory(String taskCategory) {
        this.taskCategory = taskCategory;
    }

}
