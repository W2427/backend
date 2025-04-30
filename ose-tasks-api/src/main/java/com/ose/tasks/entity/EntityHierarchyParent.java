package com.ose.tasks.entity;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 实体层级结构上级视图。
 */
@Entity
@Table(name = "entity_hierarchy_parent")
public class EntityHierarchyParent extends BaseEntity {

    private static final long serialVersionUID = -5639879074783955068L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "上级层级节点 ID")
    @Column
    private Long parentHierarchyNodeId;

    @Schema(description = "上级层级节点类型")
    @Column
    private String parentHierarchyType;

    @Schema(description = "层级 路径 path")
    @Column
    private String path;

    @Schema(description = "父级 的 层及路径 parent_path")
    @Column(length = 2000)
    private String parentPath;

    @Schema(description = "上级项目节点 ID")
    @Column
    private Long parentProjectNodeId;

    @Schema(description = "上级实体 ID")
    @Column
    private Long parentEntityId;

    @Schema(description = "上级实体类型")
    @Column
    private String parentEntityType;

    @Schema(description = "上级实体子类型")
    @Column
    private String parentEntitySubType;

    @Schema(description = "上级编号")
    @Column
    private String parentNo;

    @Schema(description = "层级节点类型")
    @Column
    private String hierarchyType;

    @Schema(description = "项目节点 ID")
    @Column
    private Long projectNodeId;

    @Schema(description = "实体 ID")
    @Column
    private Long entityId;

    @Schema(description = "实体类型")
    @Column

    private String entityType;

    @Schema(description = "实体子类型")
    @Column
    private String entitySubType;

    @Schema(description = "编号")
    @Column
    private String no;

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

    public Long getParentHierarchyNodeId() {
        return parentHierarchyNodeId;
    }

    public void setParentHierarchyNodeId(Long parentHierarchyNodeId) {
        this.parentHierarchyNodeId = parentHierarchyNodeId;
    }

    public String getParentHierarchyType() {
        return parentHierarchyType;
    }

    public void setParentHierarchyType(String parentHierarchyType) {
        this.parentHierarchyType = parentHierarchyType;
    }

    public Long getParentProjectNodeId() {
        return parentProjectNodeId;
    }

    public void setParentProjectNodeId(Long parentProjectNodeId) {
        this.parentProjectNodeId = parentProjectNodeId;
    }

    public Long getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(Long parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public String getParentEntityType() {
        return parentEntityType;
    }

    public void setParentEntityType(String parentEntityType) {
        this.parentEntityType = parentEntityType;
    }

    public String getParentEntitySubType() {
        return parentEntitySubType;
    }

    public void setParentEntitySubType(String parentEntitySubType) {
        this.parentEntitySubType = parentEntitySubType;
    }

    public String getParentNo() {
        return parentNo;
    }

    public void setParentNo(String parentNo) {
        this.parentNo = parentNo;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }
}
