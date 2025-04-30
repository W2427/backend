package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * 退库清单关联出库清单中的合计信息表
 */
@Entity
@Table(name = "mat_f_material_return_receipt_relation_issue_total")
public class FMaterialReturnReceiptRelationIssueTotalEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "退库单ID")
    private Long fmReturnId;

    @Schema(description = "退库单关联出库清单ID")
    private Long fmReturnRelationIssueId;

    @Schema(description = "OSE 出库单ID")
    private Long fmirId;

    @Schema(description = "OSE 出库单合计信息表ID")
    private Long fmirTotalId;

    @Schema(description = "领料单详情ID")
    @Column(name = "fmreq_item_id")
    private Long fmreqItemId;

    @Schema(description = "合计退库扫描数量，可退库数量通过计算取得")
    @Column(precision = 19, scale = 3)
    private BigDecimal totalQty;

    @Schema(description = "合计质量问题材料的数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal totalInspectionProblemQty;

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

    public Long getFmReturnId() {
        return fmReturnId;
    }

    public void setFmReturnId(Long fmReturnId) {
        this.fmReturnId = fmReturnId;
    }

    public Long getFmReturnRelationIssueId() {
        return fmReturnRelationIssueId;
    }

    public void setFmReturnRelationIssueId(Long fmReturnRelationIssueId) {
        this.fmReturnRelationIssueId = fmReturnRelationIssueId;
    }

    public Long getFmirId() {
        return fmirId;
    }

    public void setFmirId(Long fmirId) {
        this.fmirId = fmirId;
    }

    public Long getFmirTotalId() {
        return fmirTotalId;
    }

    public void setFmirTotalId(Long fmirTotalId) {
        this.fmirTotalId = fmirTotalId;
    }

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public Long getFmreqItemId() {
        return fmreqItemId;
    }

    public void setFmreqItemId(Long fmreqItemId) {
        this.fmreqItemId = fmreqItemId;
    }

    public BigDecimal getTotalInspectionProblemQty() {
        return totalInspectionProblemQty;
    }

    public void setTotalInspectionProblemQty(BigDecimal totalInspectionProblemQty) {
        this.totalInspectionProblemQty = totalInspectionProblemQty;
    }

    @Schema(description = "OSE 出库单合计信息")
    @JsonProperty(value = "fmirTotalId")
    public ReferenceData getFmirTotalIdRef() {
        return this.fmirTotalId == null
            ? null
            : new ReferenceData(this.fmirTotalId);
    }

    @Schema(description = "领料单详情信息")
    @JsonProperty(value = "fmreqItemId")
    public ReferenceData getFmreqItemIdRef() {
        return this.fmreqItemId == null
            ? null
            : new ReferenceData(this.fmreqItemId);
    }
}
