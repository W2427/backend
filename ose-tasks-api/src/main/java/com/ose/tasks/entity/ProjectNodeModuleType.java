package com.ose.tasks.entity;

import com.ose.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 节点所在模块类型视图。
 */
@Entity
@Table(name = "project_node_module_type")
public class ProjectNodeModuleType extends BaseEntity {

    private static final long serialVersionUID = 5015613526647435992L;

    @Schema(description = "组织 ID")
    @Column
    private Long orgId;

    @Schema(description = "项目 ID")
    @Column
    private Long projectId;

    @Schema(description = "模块的层级节点 ID")
    @Column
    private Long moduleHierarchyNodeId;

    @Schema(description = "模块的项目节点 ID")
    @Column
    private Long moduleProjectNodeId;

    @Schema(description = "模块类型")
    @Column
    private String moduleType;

    @Schema(description = "模块节点编号")
    @Column
    private String moduleNo;

    @Schema(description = "项目节点 ID")
    @Column
    private Long projectNodeId;

    @Schema(description = "节点编号")
    @Column
    private String no;

    @Schema(description = "节点类型")
    @Column
    private String entityType;

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

    public Long getModuleHierarchyNodeId() {
        return moduleHierarchyNodeId;
    }

    public void setModuleHierarchyNodeId(Long moduleHierarchyNodeId) {
        this.moduleHierarchyNodeId = moduleHierarchyNodeId;
    }

    public Long getModuleProjectNodeId() {
        return moduleProjectNodeId;
    }

    public void setModuleProjectNodeId(Long moduleProjectNodeId) {
        this.moduleProjectNodeId = moduleProjectNodeId;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public Long getProjectNodeId() {
        return projectNodeId;
    }

    public void setProjectNodeId(Long projectNodeId) {
        this.projectNodeId = projectNodeId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
}
