package com.ose.tasks.dto;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 当前项目中的模块信息。
 */
public class ModuleDTO extends BaseDTO {

    private static final long serialVersionUID = 7536956733776408299L;

    @Schema(description = "模块编号")
    private String moduleNo;

    @Schema(description = "模块批次节点ID")
    private Long moduleProjectNodeId;

    @Schema(description = "模块批次层级ID")
    private Long moduleHierarchyNodeId;

    @Schema(description = "Entity ID")
    private Long entityId;

    @Schema(description = "Entity Type")
    private String entityType;

    @Schema(description = "Sub Entity Type")
    private String entitySubType;

    @Schema(description = "数量")
    private Integer qty;

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public Long getModuleProjectNodeId() {
        return moduleProjectNodeId;
    }

    public void setModuleProjectNodeId(Long moduleProjectNodeId) {
        this.moduleProjectNodeId = moduleProjectNodeId;
    }

    public Long getModuleHierarchyNodeId() {
        return moduleHierarchyNodeId;
    }

    public void setModuleHierarchyNodeId(Long moduleHierarchyNodeId) {
        this.moduleHierarchyNodeId = moduleHierarchyNodeId;
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

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }
}

