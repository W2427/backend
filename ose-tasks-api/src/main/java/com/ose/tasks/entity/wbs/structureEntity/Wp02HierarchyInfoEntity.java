package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.entity.Project;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * WP02 Section实体数据实体基类。 WP2 分类
 */
//这是视图
@Entity
@Table(name = "wp02_hierarchy_info")
public class Wp02HierarchyInfoEntity extends Wp02EntityBase {


    private static final long serialVersionUID = -755165668335950978L;
    @Schema(description = "层级类型")
    @Column
    private String hierarchyType;


    @Schema(description = "层级节点ID")
    @Column
    private Long hierarchyId;


    @Schema(description = "结构层级 路径")
    @Column(length = 2000)
    private String pathOnStructure;


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


    @Schema(description = "项目节点类型")
    @Column
    private String nodeType;

    @Schema(description = "实体类型")
    @Column
    private String entityType;


    @Schema(description = "实体子类型")
    @Column
    private String entitySubType;


    @JsonCreator
    public Wp02HierarchyInfoEntity() {
        this(null);
    }

    public Wp02HierarchyInfoEntity(Project project) {
        super(project);
        setEntityType(getEntityType());
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public Long getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(Long hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public String getPathOnStructure() {
        return pathOnStructure;
    }

    public void setPathOnStructure(String pathOnStructure) {
        this.pathOnStructure = pathOnStructure;
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

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
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

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }
}
