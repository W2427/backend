package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 焊口实体数据视图。
 */
// 这个是视图
@Entity
@Table(name = "weld_hierarchy_info")
public class WeldHierarchyInfoEntity extends WeldEntityBase {

    private static final long serialVersionUID = 8114098736516055931L;

    @Schema(description = "层级节点ID")
    @Column
    private Long hierarchyId;


    @Schema(description = "结构层级 路径")
    @Column(length = 2000)
    private String pathOnGeneral;

    @Schema(description = "父级层级Id")
    @Column
    private Long parentHierarchyId;


    @Schema(description = "模块项目节点名称")
    @Column
    private String moduleParentNo;


    @Schema(description = "模块项目节点Id")
    @Column
    private Long moduleParentId;

    @Schema(description = "项目节点Id")
    @Column
    private Long projectNodeId;

    @Schema(description = "实体类型")
    @Column
    private String entityType;


    @Schema(description = "实体子类型")
    @Column
    private String entitySubType;

    @JsonCreator
    public WeldHierarchyInfoEntity() {
        this(null);
    }

    public WeldHierarchyInfoEntity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }

    // 以下是层级相关信息 -----------------


    public Long getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(Long hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public String getPathOnGeneral() {
        return pathOnGeneral;
    }

    public void setPathOnGeneral(String pathOnGeneral) {
        this.pathOnGeneral = pathOnGeneral;
    }

    public Long getParentHierarchyId() {
        return parentHierarchyId;
    }

    public void setParentHierarchyId(Long parentHierarchyId) {
        this.parentHierarchyId = parentHierarchyId;
    }

    public String getModuleParentNo() {
        return moduleParentNo;
    }

    public void setModuleParentNo(String moduleParentNo) {
        this.moduleParentNo = moduleParentNo;
    }

    public Long getModuleParentId() {
        return moduleParentId;
    }

    public void setModuleParentId(Long moduleParentId) {
        this.moduleParentId = moduleParentId;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    @Override
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String getEntitySubType() {
        return entitySubType;
    }

    @Override
    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }
}
