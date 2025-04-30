package com.ose.tasks.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.tasks.dto.wbs.WBSEntryPatchDTO;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 项目 WBS 实体条目更新数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WBSEntityPatchDTO extends WBSEntryPatchDTO {

    private static final long serialVersionUID = -8654502858758535209L;

    @Schema(description = "前置任务列表")
    private List<WBSRelationDTO> predecessors;

    @Schema(description = "后置任务列表")
    private List<WBSRelationDTO> successors;

    @JsonIgnore
    private Long hierarchyNodeId;

    @JsonIgnore
    private Long parentHierarchyNodeId;

    @JsonIgnore
    private Long moduleHierarchyNodeId;

    @JsonIgnore
    private Integer proportion;

    private String funcPart;

    public List<WBSRelationDTO> getPredecessors() {
        return predecessors;
    }

    public void setPredecessors(List<WBSRelationDTO> predecessors) {
        this.predecessors = predecessors;
    }

    public List<WBSRelationDTO> getSuccessors() {
        return successors;
    }

    public void setSuccessors(List<WBSRelationDTO> successors) {
        this.successors = successors;
    }

    public Long getHierarchyNodeId() {
        return hierarchyNodeId;
    }

    public void setHierarchyNodeId(Long hierarchyNodeId) {
        this.hierarchyNodeId = hierarchyNodeId;
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

    public Integer getProportion() {
        return proportion;
    }

    public void setProportion(Integer proportion) {
        this.proportion = proportion;
    }

    public String getFuncPart() {
        return funcPart;
    }

    public void setFuncPart(String funcPart) {
        this.funcPart = funcPart;
    }
}
