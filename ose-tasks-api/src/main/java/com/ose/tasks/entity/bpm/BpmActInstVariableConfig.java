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
@Table(name = "bpm_act_inst_variable_config")
public class BpmActInstVariableConfig extends BaseBizEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4387445149304502246L;

    //变量名称
    private String name;

    //显示名称
    private String displayName;

    //变量类型
    @Enumerated(EnumType.STRING)
    private ActInstVariableType type;

    private String processKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public ActInstVariableType getType() {
        return type;
    }

    public void setType(ActInstVariableType type) {
        this.type = type;
    }

    public String getProcessKey() {
        return processKey;
    }

    public void setProcessKey(String processKey) {
        this.processKey = processKey;
    }

}
