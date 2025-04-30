package com.ose.tasks.entity.bpm;

import com.ose.entity.BaseBizEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_drawing_sign_task")
public class BpmDrawingSignTask extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    //流程实例id
    private Long actInstId;

    //任务id,bpm服务中的ID 1234561234566667
    private Long taskId;

    private Long assignee;

    private String assigneeName;

    private String command;

    private String comment;

    private boolean finished = false;

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

    public Long getAssignee() {
        return assignee;
    }

    public void setAssignee(Long assignee) {
        this.assignee = assignee;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public String getAssigneeName() {
        return assigneeName;
    }

    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
