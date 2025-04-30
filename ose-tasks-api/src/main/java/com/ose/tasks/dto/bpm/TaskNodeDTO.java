package com.ose.tasks.dto.bpm;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 实体数据传输对象
 */
public class TaskNodeDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 7615182766136985152L;

    @Schema(description = "defKey")
    private String taskDefKey;

    @Schema(description = "节点名称")
    private String taskName;

    public TaskNodeDTO(String taskDefKey, String taskName) {
        super();
        this.taskDefKey = taskDefKey;
        this.taskName = taskName;
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
