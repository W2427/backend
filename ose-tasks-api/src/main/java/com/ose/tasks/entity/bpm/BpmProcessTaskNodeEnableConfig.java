package com.ose.tasks.entity.bpm;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import com.ose.entity.BaseEntity;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_process_task_node_enable_config")
public class BpmProcessTaskNodeEnableConfig extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    private String process;

    private String processStage;

    private String taskDefKey;

    private String type;

    private Boolean enable;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getProcessStage() {
        return processStage;
    }

    public void setProcessStage(String processStage) {
        this.processStage = processStage;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

}
