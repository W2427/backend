package com.ose.tasks.dto.drawing;

import com.ose.dto.BaseDTO;

/**
 * 实体管理 数据传输对象
 */
public class DrawingProofreadSearchDTO extends BaseDTO {


    private static final long serialVersionUID = 2994717167897045835L;

    //流程id
    private Long actInstId;

    // 正在运行的任务id
    private Long taskId;

    // 工序
    private String process;

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
