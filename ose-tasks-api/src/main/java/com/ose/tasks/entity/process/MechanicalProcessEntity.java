package com.ose.tasks.entity.process;

import com.ose.entity.BaseStrEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 机械 工序-实体关系。
 */
@Entity
@Table(name = "mechanical_process_entity")
public class MechanicalProcessEntity extends BaseStrEntity {


    private static final long serialVersionUID = 7750635808284755514L;
    @Id
    @Column
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

    @Schema(description = "建造视图层级节点路径")
    @Column
    private String hierarchyPath;

    @Schema(description = "hierarchy Type 层级类型")
    @Column
    private String hierarchyType;

    @Schema(description = "建造视图层级节点深度")
    @Column
    private Integer hierarchyDepth;

    @Schema(description = "建造视图上级层级节点 ID")
    @Column
    private Long parentHierarchyNodeId;

    @Schema(description = "模块层级节点 ID")
    @Column
    private Long moduleHierarchyNodeId;

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

    @Schema(description = "Work Package 01 No")
    @Column(name = "wp01_no")
    private String wp01No;

    @Schema(description = "Work Package 01 Id")
    @Column(name = "wp01_id")
    private Long wp01Id;

    @Schema(description = "Work Package 02 No")
    @Column(name = "wp02_no")
    private String wp02No;

    @Schema(description = "Work Package 02 Id")
    @Column(name = "wp02_id")
    private Long wp02Id;


    @Schema(description = "Work Package 03 No")
    @Column(name = "wp03_no")
    private String wp03No;

    @Schema(description = "Work Package 03 Id")
    @Column(name = "wp03_id")
    private Long wp03Id;


    @Schema(description = "Work Package 04 No")
    @Column(name = "wp04_no")
    private String wp04No;

    @Schema(description = "Work Package 04 Id")
    @Column(name = "wp04_id")
    private Long wp04Id;

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

    public String getHierarchyPath() {
        return hierarchyPath;
    }

    public void setHierarchyPath(String hierarchyPath) {
        this.hierarchyPath = hierarchyPath;
    }

    public Long getParentHierarchyNodeId() {
        return parentHierarchyNodeId;
    }

    public void setParentHierarchyNodeId(Long parentHierarchyNodeId) {
        this.parentHierarchyNodeId = parentHierarchyNodeId;
    }

    public Long getModuleHierarchyNodeId() {
        return moduleHierarchyNodeId;
    }

    public void setModuleHierarchyNodeId(Long moduleHierarchyNodeId) {
        this.moduleHierarchyNodeId = moduleHierarchyNodeId;
    }

    public Long getHierarchyAncestorId() {
        return hierarchyAncestorId;
    }

    public void setHierarchyAncestorId(Long hierarchyAncestorId) {
        this.hierarchyAncestorId = hierarchyAncestorId;
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

    public String getWp01No() {
        return wp01No;
    }

    public void setWp01No(String wp01No) {
        this.wp01No = wp01No;
    }

    public Long getWp01Id() {
        return wp01Id;
    }

    public void setWp01Id(Long wp01Id) {
        this.wp01Id = wp01Id;
    }

    public String getWp02No() {
        return wp02No;
    }

    public void setWp02No(String wp02No) {
        this.wp02No = wp02No;
    }

    public Long getWp02Id() {
        return wp02Id;
    }

    public void setWp02Id(Long wp02Id) {
        this.wp02Id = wp02Id;
    }

    public String getWp03No() {
        return wp03No;
    }

    public void setWp03No(String wp03No) {
        this.wp03No = wp03No;
    }

    public Long getWp03Id() {
        return wp03Id;
    }

    public void setWp03Id(Long wp03Id) {
        this.wp03Id = wp03Id;
    }

    public String getWp04No() {
        return wp04No;
    }

    public void setWp04No(String wp04No) {
        this.wp04No = wp04No;
    }

    public Long getWp04Id() {
        return wp04Id;
    }

    public void setWp04Id(Long wp04Id) {
        this.wp04Id = wp04Id;
    }

    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }
}
