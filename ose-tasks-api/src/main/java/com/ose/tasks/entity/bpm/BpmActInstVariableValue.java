package com.ose.tasks.entity.bpm;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.bpm.ActInstVariableType;

/**
 * 实体管理 实体类。
 */
@Entity
@Table(name = "bpm_act_inst_variable_value")
public class BpmActInstVariableValue extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    //项目id
    private Long projectId;

    //组织id
    private Long orgId;

    //流程实例id
    private Long actInstId;

    //变量显示名称
    private String variableDisplayName;

    //变量名称
    private String variableName;

    //变量类型
    @Enumerated(EnumType.STRING)
    private ActInstVariableType variableType;

    //变量值
    private String value;

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getVariableDisplayName() {
        return variableDisplayName;
    }

    public void setVariableDisplayName(String variableDisplayName) {
        this.variableDisplayName = variableDisplayName;
    }

    public String getVariableName() {
        return variableName;
    }

    public void setVariableName(String variableName) {
        this.variableName = variableName;
    }

    public ActInstVariableType getVariableType() {
        return variableType;
    }

    public void setVariableType(ActInstVariableType variableType) {
        this.variableType = variableType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

}
