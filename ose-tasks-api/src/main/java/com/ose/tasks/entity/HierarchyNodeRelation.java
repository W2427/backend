package com.ose.tasks.entity;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

@Entity
@Table(name = "hierarchy_node_relation"
    ,
    indexes = {
        @Index(columnList = "hierarchyId,hierarchyAncestorId"),
        @Index(columnList = "hierarchyAncestorId,hierarchyId"),
        @Index(columnList = "orgId,projectId,nodeId,ancestorEntityType"),
        @Index(columnList = "projectId,nodeAncestorId,depth"),
        @Index(columnList = "entityId,ancestorEntityType"),
        @Index(columnList = "ancestorEntityId,entityType"),
        @Index(columnList = "projectId,nodeAncestorId,depth"),
        @Index(columnList = "projectId,hierarchyAncestorId"),
        @Index(columnList = "projectId,entityId"),
        @Index(columnList = "projectId,nodeId,depth")
    }
    )
public class HierarchyNodeRelation extends BaseEntity {


    private static final long serialVersionUID = -8379953275640225168L;
    @Schema(description = "组织ID")
    @Column
    private Long orgId;

    @Schema(description = "项目ID")
    @Column
    private Long projectId;

    @Schema(description = "层级ID hierarchyId")
    @Column
    private Long hierarchyId;

    @Schema(description = "层级 祖先ID hierarchy ancestor Id")
    @Column
    private Long hierarchyAncestorId;

    @Schema(description = "Project Node Id")
    @Column
    private Long nodeId;

    @Schema(description = " Entity Id")
    @Column
    private Long entityId;

    @Schema(description = "Ancestor Project Node Id")
    @Column
    private Long nodeAncestorId;

    @Schema(description = "Ancestor Entity Id")
    @Column
    private Long ancestorEntityId;

    @Schema(description = "Ancestor Entity Type")
    @Column
    private String ancestorEntityType;

    @Schema(description = "Entity Type")
    @Column
    private String entityType;

    @Schema(description = "Sub Entity Type")
    @Column
    private String entitySubType;

    @Schema(description = "深度 层次 depth")
    @Column
    private int depth;

    @Schema(description = "module project node id")
    @Column
    private Long moduleHierarchyNodeId;

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

    public Long getHierarchyId() {
        return hierarchyId;
    }

    public void setHierarchyId(Long hierarchyId) {
        this.hierarchyId = hierarchyId;
    }

    public Long getHierarchyAncestorId() {
        return hierarchyAncestorId;
    }

    public void setHierarchyAncestorId(Long hierarchyAncestorId) {
        this.hierarchyAncestorId = hierarchyAncestorId;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public Long getModuleHierarchyNodeId() {
        return moduleHierarchyNodeId;
    }

    public void setModuleHierarchyNodeId(Long moduleHierarchyNodeId) {
        this.moduleHierarchyNodeId = moduleHierarchyNodeId;
    }

    public Long getNodeId() {
        return nodeId;
    }

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public Long getNodeAncestorId() {
        return nodeAncestorId;
    }

    public void setNodeAncestorId(Long nodeAncestorId) {
        this.nodeAncestorId = nodeAncestorId;
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

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }


    public String getAncestorEntityType() {
        return ancestorEntityType;
    }

    public void setAncestorEntityType(String ancestorEntityType) {
        this.ancestorEntityType = ancestorEntityType;
    }

    public Long getAncestorEntityId() {
        return ancestorEntityId;
    }

    public void setAncestorEntityId(Long ancestorEntityId) {
        this.ancestorEntityId = ancestorEntityId;
    }
}
