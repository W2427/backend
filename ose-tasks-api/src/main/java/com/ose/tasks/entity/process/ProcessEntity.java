package com.ose.tasks.entity.process;

import com.ose.entity.BaseStrEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 工序-实体关系。
 */
@Entity
@Table(name = "process_entity")
public class ProcessEntity extends BaseStrEntity {

    private static final long serialVersionUID = -419496912724584167L;

    @Id
    @Column
    @Schema(description = "concat(`s`.`id`,':',`p`.`id`,':',`hn`.`id`) AS `id")
    private String id;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "工序阶段 ID")
    @Column
    private Long stageId;

    @Schema(description = "工序阶段名称")
    @Column
    private String stage;

    @Schema(description = "工序 ID")
    @Column
    private Long processId;

    @Schema(description = "工序名称")
    @Column
    private String process;

    @Schema(description = "功能块名称")
    @Column
    private String funcPart;

    @Schema(description = "实体类型 ID")
    @Column
    private Long entityTypeId;

    @Schema(description = "实体类型名称")
    @Column

    private String entityType;

    @Schema(description = "实体子类型 ID")
    @Column
    private Long entitySubTypeId;

    @Schema(description = "实体子类型名称")
    @Column(length = 64)
    private String entitySubType;

    @Schema(description = "实体 ID")
    @Column
    private Long entityId;

    @Schema(description = "层级节点 ID")
    @Column
    private Long hierarchyNodeId;

    @Schema(description = "hierarchy Type 层级类型")
    @Column
    private String hierarchyType;

    @Schema(description = "建造视图层级节点深度")
    @Column
    private Integer hierarchyDepth;

    @Schema(description = "建造视图上级层级节点 ID")
    @Column
    private Long parentHierarchyNodeId;

    @Schema(description = "项目节点 ID")
    @Column
    private Long projectNodeId;

    @Schema(description = "项目节点编号")
    @Column
    private String projectNodeNo;

    @Schema(description = "图纸页码")
    @Column
    private Integer dwgShtNo;

    @Schema(description = "工作量 物量")
    @Column
    private Double workLoad;

    @Schema(description = "discipline 专业")
    @Column(name = "discipline")
    private String discipline;

    @Schema(description = "hierarchy_ancestor_id")
    @Column
    private Long hierarchyAncestorId;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getStageId() {
        return stageId;
    }

    public void setStageId(Long stageId) {
        this.stageId = stageId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public Long getProcessId() {
        return processId;
    }

    public void setProcessId(Long processId) {
        this.processId = processId;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public Long getEntityTypeId() {
        return entityTypeId;
    }

    public void setEntityTypeId(Long entityTypeId) {
        this.entityTypeId = entityTypeId;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Long getEntitySubTypeId() {
        return entitySubTypeId;
    }

    public void setEntitySubTypeId(Long entitySubTypeId) {
        this.entitySubTypeId = entitySubTypeId;
    }

    public String getEntitySubType() {
        return entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public Long getHierarchyNodeId() {
        return hierarchyNodeId;
    }

    public void setHierarchyNodeId(Long hierarchyNodeId) {
        this.hierarchyNodeId = hierarchyNodeId;
    }

    public Long getParentHierarchyNodeId() {
        return parentHierarchyNodeId;
    }

    public void setParentHierarchyNodeId(Long parentHierarchyNodeId) {
        this.parentHierarchyNodeId = parentHierarchyNodeId;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public String getProjectNodeNo() {
        return projectNodeNo;
    }

    public void setProjectNodeNo(String projectNodeNo) {
        this.projectNodeNo = projectNodeNo;
    }

    public Integer getHierarchyDepth() {
        return hierarchyDepth;
    }

    public void setHierarchyDepth(Integer hierarchyDepth) {
        this.hierarchyDepth = hierarchyDepth;
    }

    public Integer getDwgShtNo() {
        return dwgShtNo;
    }

    public void setDwgShtNo(Integer dwgShtNo) {
        this.dwgShtNo = dwgShtNo;
    }

    public Double getWorkLoad() {
        return workLoad;
    }

    public void setWorkLoad(Double workLoad) {
        this.workLoad = workLoad;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Long getHierarchyAncestorId() {
        return hierarchyAncestorId;
    }

    public void setHierarchyAncestorId(Long hierarchyAncestorId) {
        this.hierarchyAncestorId = hierarchyAncestorId;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }


}
