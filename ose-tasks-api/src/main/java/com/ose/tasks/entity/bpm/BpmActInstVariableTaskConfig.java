package com.ose.tasks.entity.bpm;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.bpm.ActInstVariableFlag;

/**
 * 流程 任务节点 实例设置变量 实体类。
 */
@Entity
@Table(name = "bpm_act_inst_variable_task_config")
public class BpmActInstVariableTaskConfig extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    // 流程名称英文
    private String processKey;

    // 任务节点名称
    private String taskName;

    // 任务节点defKey
    private String taskDefKey;

    // 变量名
    private String variableName;

    // 控制标识
    @Enumerated(EnumType.STRING)
    private ActInstVariableFlag flag;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public ActInstVariableFlag getFlag() {
        return flag;
    }

    public void setFlag(ActInstVariableFlag flag) {
        this.flag = flag;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

    public String getTaskDefKey() {
        return taskDefKey;
    }

    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey;
    }

}
