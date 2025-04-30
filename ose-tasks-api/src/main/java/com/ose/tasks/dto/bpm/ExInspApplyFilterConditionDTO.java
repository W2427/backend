package com.ose.tasks.dto.bpm;

import java.util.Set;

import com.ose.dto.BaseDTO;

/**
 * 待办任务条件 数据传输对象
 */
public class ExInspApplyFilterConditionDTO extends BaseDTO {

    /**
     *
     */
    private static final long serialVersionUID = -34948993806220141L;

    private boolean owner = false;

    private boolean third = false;

    private boolean other = false;

    private Set<String> entityModuleNames;

    private Set<String> processes;

    private Set<String> entityCategoryNameCns;

    private Set<String> oldReportNos;

    public boolean isOwner() {
        return owner;
    }

    public void setOwner(boolean owner) {
        this.owner = owner;
    }

    public boolean isThird() {
        return third;
    }

    public void setThird(boolean third) {
        this.third = third;
    }

    public boolean isOther() {
        return other;
    }

    public void setOther(boolean other) {
        this.other = other;
    }

    public Set<String> getProcesses() {
        return processes;
    }

    public void setProcesses(Set<String> processes) {
        this.processes = processes;
    }

    public Set<String> getEntitySubTypeNameCns() {
        return entityCategoryNameCns;
    }

    public void setEntitySubTypeNameCns(Set<String> entityCategoryNameCns) {
        this.entityCategoryNameCns = entityCategoryNameCns;
    }

    public Set<String> getEntityModuleNames() {
        return entityModuleNames;
    }

    public void setEntityModuleNames(Set<String> entityModuleNames) {
        this.entityModuleNames = entityModuleNames;
    }

    public Set<String> getOldReportNos() {
        return oldReportNos;
    }

    public void setOldReportNos(Set<String> oldReportNos) {
        this.oldReportNos = oldReportNos;
    }
}
