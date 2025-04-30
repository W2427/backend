package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 管段实体数据实体。
 */
@Entity
@Table(name = "pipe_piece_hierarchy_info")
public class PipePieceHierarchyInfoEntity extends PipePieceEntityBase {

    private static final long serialVersionUID = -6605438918295989624L;

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
    public PipePieceHierarchyInfoEntity() {
        this(null);
    }

    public PipePieceHierarchyInfoEntity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }

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
