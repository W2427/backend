package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.wbs.HierarchyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * Structrue Weld 实体数据 层级视图。
 */
//这是视图
@Entity
@Table(name = "structure_weld_hierarchy_info")
public class StructureWeldHierarchyInfoEntity extends StructureWeldEntityBase {


    @Schema(description = "层级类型")
    @Column
    @Enumerated(EnumType.STRING)
    private HierarchyType hierarchyType;


    @Schema(description = "层级节点ID")
    @Column
    private Long hierarchyId;


    @Schema(description = "结构层级 路径")
    @Column(length = 2000)
    private String pathOnStructure;


    @Schema(description = "父级层级Id")
    @Column
    private Long parentHierarchyId;


    @Schema(description = "项目节点")
    @Column
    private Long projectNodeId;


    @Schema(description = "模块项目节点名称")
    @Column
    private String moduleParentNo;


    @Schema(description = "模块项目节点Id")
    @Column
    private Long moduleParentId;


    @Schema(description = "父级项目节点Id")
    @Column
    private Long parentProjectNodeId;


    @Schema(description = "父级节点类型")
    @Column
    private String parentEntityType;

    @Schema(description = "实体类型")
    @Column
    private String entityType;


    @Schema(description = "实体子类型")
    @Column
    private String entitySubType;

    @JsonCreator
    public StructureWeldHierarchyInfoEntity() {
        this(null);
    }

    public StructureWeldHierarchyInfoEntity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }

    public HierarchyType getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(HierarchyType hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public Long getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(Long hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public void setEntitySubType(String subEntityType) {
        this.entitySubType = entitySubType;
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

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    @Override
    public String getEntitySubType() {
        return entitySubType;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public Long getParentProjectNodeId() {
        return parentProjectNodeId;
    }

    public void setParentProjectNodeId(Long parentProjectNodeId) {
        this.parentProjectNodeId = parentProjectNodeId;
    }

    public String getParentEntityType() {
        return parentEntityType;
    }

    public void setParentEntityType(String parentEntityType) {
        this.parentEntityType = parentEntityType;
    }
}
