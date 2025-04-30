package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.entity.ProjectNode;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 项目层级结构数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HierarchyNodeDTO extends HierarchyNodePutDTO {

    private static final long serialVersionUID = -3585726148627218039L;

    @Schema(description = "层级深度")
    private int depth;

    @Schema(description = "层级类型")
    private String hierarchyType;

    @JsonIgnore
    private ProjectNode node;

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getHierarchyType() {
        return hierarchyType;
    }

    public void setHierarchyType(String hierarchyType) {
        this.hierarchyType = hierarchyType;
    }

    public ProjectNode getNode() {
        return node;
    }

    public void setNode(ProjectNode node) {
        this.node = node;
    }

    @Schema(description = "项目节点 ID")
    public Long getNodeId() {
        return node == null ? null : node.getId();
    }

    @Schema(description = "实体ID")
    public Long getEntityId() {
        return node.getEntityId();
    }

}
