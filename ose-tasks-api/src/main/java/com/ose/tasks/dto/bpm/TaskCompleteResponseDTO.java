package com.ose.tasks.dto.bpm;

import java.util.List;

import com.ose.dto.BaseDTO;
import com.ose.tasks.entity.bpm.BpmHiTaskinst;
import com.ose.tasks.entity.bpm.BpmRuTask;

/**
 * 实体管理 数据传输对象
 */
public class TaskCompleteResponseDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = 4517659110867097693L;

    private Long actInstId;

    private String attachments;

    private String comment;

    private String pictures;

    private List<TaskAssigneeInfo> assigneeInfos;

    private BpmHiTaskinst hiTaskinst;

    private List<BpmRuTask> ruTasks;

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPictures() {
        return pictures;
    }

    public void setPictures(String pictures) {
        this.pictures = pictures;
    }


    public static class TaskAssigneeInfo {

        private String taskDefKey;

        private String taskName;

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

    public List<TaskAssigneeInfo> getAssigneeInfos() {
        return assigneeInfos;
    }

    public void setAssigneeInfos(List<TaskAssigneeInfo> assigneeInfos) {
        this.assigneeInfos = assigneeInfos;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public BpmHiTaskinst getHiTaskinst() {
        return hiTaskinst;
    }

    public void setHiTaskinst(BpmHiTaskinst actHiTaskinst) {
        this.hiTaskinst = hiTaskinst;
    }

    public List<BpmRuTask> getRuTasks() {
        return ruTasks;
    }

    public void setActRuTasks(List<BpmRuTask> ruTasks) {
        this.ruTasks = ruTasks;
    }


}
