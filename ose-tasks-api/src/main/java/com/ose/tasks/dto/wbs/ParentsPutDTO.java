package com.ose.tasks.dto.wbs;

import com.ose.dto.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 各维度上级节点更新数据传输对象。
 */
public class ParentsPutDTO extends BaseDTO {

    private static final long serialVersionUID = -5913465784481704800L;

    @Schema(description = "PIPING维度上级节点 ID")
    private Long parentHierarchyIdOnPiping;

    @Schema(description = "试压包维度上级节点 ID")
    private Long parentHierarchyIdOnPtp;

    @Schema(description = "子系统维度上级节点 ID")
    private Long parentHierarchyIdOnSs;

    @Schema(description = "清洁包维度上级节点 ID")
    private Long parentHierarchyIdOnCp;

    @Schema(description = "上级ISO节点 ID")
    private Long parentIsoHierarchyId;

    @Schema(description = "上级SPOOL节点 ID")
    private Long parentSpoolHierarchyId;

    @Schema(description = "Structure 层维度上级节点 ID")
    private Long parentHierarchyIdOnStructure;

    public Long getParentHierarchyIdOnPiping() {
        return parentHierarchyIdOnPiping;
    }

    public void setParentHierarchyIdOnPiping(Long parentHierarchyIdOnPiping) {
        this.parentHierarchyIdOnPiping = parentHierarchyIdOnPiping;
    }

    public Long getParentHierarchyIdOnPtp() {
        return parentHierarchyIdOnPtp;
    }

    public void setParentHierarchyIdOnPtp(Long parentHierarchyIdOnPtp) {
        this.parentHierarchyIdOnPtp = parentHierarchyIdOnPtp;
    }

    public Long getParentHierarchyIdOnSs() {
        return parentHierarchyIdOnSs;
    }

    public void setParentHierarchyIdOnSs(Long parentHierarchyIdOnSs) {
        this.parentHierarchyIdOnSs = parentHierarchyIdOnSs;
    }

    public Long getParentHierarchyIdOnCp() {
        return parentHierarchyIdOnCp;
    }

    public void setParentHierarchyIdOnCp(Long parentHierarchyIdOnCp) {
        this.parentHierarchyIdOnCp = parentHierarchyIdOnCp;
    }

    public Long getParentIsoHierarchyId() {
        return parentIsoHierarchyId;
    }

    public void setParentIsoHierarchyId(Long parentIsoHierarchyId) {
        this.parentIsoHierarchyId = parentIsoHierarchyId;
    }

    public Long getParentSpoolHierarchyId() {
        return parentSpoolHierarchyId;
    }

    public void setParentSpoolHierarchyId(Long parentSpoolHierarchyId) {
        this.parentSpoolHierarchyId = parentSpoolHierarchyId;
    }

    public Long getParentHierarchyIdOnStructure() {
        return parentHierarchyIdOnStructure;
    }

    public void setParentHierarchyIdOnStructure(Long parentHierarchyIdOnStructure) {
        this.parentHierarchyIdOnStructure = parentHierarchyIdOnStructure;
    }

}
