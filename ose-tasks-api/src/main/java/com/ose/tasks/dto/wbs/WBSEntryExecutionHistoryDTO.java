package com.ose.tasks.dto.wbs;

import com.ose.tasks.vo.wbs.WBSEntryExecutionState;

import jakarta.persistence.*;

@SqlResultSetMapping
    (
        name = "WBSEntryExecutionHistorySqlResultMapping",
        entities = {
            @EntityResult(
                entityClass = WBSEntryExecutionHistoryDTO.class, //就是当前这个类的名字
                fields = {
//                                        @FieldResult(name = "id", column = "id"),
                    @FieldResult(name = "memo", column = "memo"),
                    @FieldResult(name = "projectId", column = "projectId"),
                    @FieldResult(name = "entityId", column = "entityId"),
                    @FieldResult(name = "moduleId", column = "moduleId"),
                    @FieldResult(name = "module", column = "module"),
                    @FieldResult(name = "layerId", column = "layerId"),
                    @FieldResult(name = "layer", column = "layer"),
                    @FieldResult(name = "operatorId", column = "operatorId"),
                    @FieldResult(name = "executionState", column = "executionState"),
                    @FieldResult(name = "entityType", column = "entityType"),
                    @FieldResult(name = "entitySubType", column = "entitySubType"),
                    @FieldResult(name = "moduleType", column = "moduleType")
                }
            )
        }
    )
@Entity
public class WBSEntryExecutionHistoryDTO {


//    @Id
//    private String id;

    //项目id
    private Long projectId;

    //实体id
    @Id
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
