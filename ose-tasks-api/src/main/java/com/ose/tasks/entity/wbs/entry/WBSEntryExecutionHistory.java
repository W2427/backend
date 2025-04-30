package com.ose.tasks.entity.wbs.entry;

import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.wbs.WBSEntryExecutionState;

import jakarta.persistence.*;

@Entity
@Table(name = "wbs_entry_execution_history")
public class WBSEntryExecutionHistory extends BaseBizEntity {

    private static final long serialVersionUID = 3604174992460311699L;

    //项目id
    private Long projectId;

    //实体id
    private Long entityId;

    //模块号，子系统号, hierarchy ID
    @Column(nullable = true)
    private Long moduleId;

    //模块名称
    private String module;

    //层、包号， hierarchy ID
    @Column(nullable = true)
    private Long layerId;

    //层、包 名称
    private String layer;

    //操作员id
    private Long operatorId;

    //执行状态
    @Enumerated(EnumType.STRING)
    private WBSEntryExecutionState executionState;

    //实体类型

    private String entityType;

    //实体子类型
    private String entitySubType;

    @Column(nullable = true)
    private Long startTimestamp;

    @Column(nullable = true)
    private Long endTimestamp;

    private String memo;

    private String moduleType;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }


    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }


    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public Long getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Long getEndTimestamp() {
        return endTimestamp;
    }

    public void setEndTimestamp(Long endTimestamp) {
        this.endTimestamp = endTimestamp;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public WBSEntryExecutionState getExecutionState() {
        return executionState;
    }

    public void setExecutionState(WBSEntryExecutionState executionState) {
        this.executionState = executionState;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Long getLayerId() {
        return layerId;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }
}
