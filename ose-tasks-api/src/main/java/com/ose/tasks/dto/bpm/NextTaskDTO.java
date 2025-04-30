package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;

/**
 * 下一个任务节点 数据传输对象
 */
public class NextTaskDTO extends BaseDTO {

    private static final long serialVersionUID = 9084841473406112264L;
    private String taskDefKey;

    private String taskName;

    private String taskType;

    private String nodeType;

    private String category;

    private String desc;

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

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
}
