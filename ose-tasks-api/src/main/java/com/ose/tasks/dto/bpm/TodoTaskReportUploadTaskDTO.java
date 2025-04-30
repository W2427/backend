package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;

/**
 * 文档报告上传后任务执行
 */
public class TodoTaskReportUploadTaskDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    // 任务流程id
    private String command;

    // 当前任务节点id
    private List<Long> bpmTaskIDs;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<Long> getBpmTaskIDs() {
        return bpmTaskIDs;
    }

    public void setBpmTaskIDs(List<Long> bpmTaskIDs) {
        this.bpmTaskIDs = bpmTaskIDs;
    }

}
