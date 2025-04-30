package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 材料配送明细实体类。
 */
@Entity
@Table(name = "mat_f_material_transfer_item")
public class FMaterialTransferItemEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "配送单ID")
    private Long fmtId;

    @Schema(description = "领料单ID")
    private Long fmreqId;

    @Schema(description = "领料点明细ID")
    private Long fmreqItemId;

    @Schema(description = "出库清单ID")
    private Long fmirId;

    @Schema(description = "出库清单合计ID")
    private Long fmirTotalId;

    @Schema(description = "计划配送数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal planTransferQty;

    @Schema(description = "实际配送数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal actualTransferQty;

    @Schema(description = "实际接收数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal actualReceiveQty;

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

    public Long getFmtId() {
        return fmtId;
    }

    public void setFmtId(Long fmtId) {
        this.fmtId = fmtId;
    }

    public Long getFmreqId() {
        return fmreqId;
    }

    public void setFmreqId(Long fmreqId) {
        this.fmreqId = fmreqId;
    }

    public Long getFmreqItemId() {
        return fmreqItemId;
    }

    public void setFmreqItemId(Long fmreqItemId) {
        this.fmreqItemId = fmreqItemId;
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

    public BigDecimal getPlanTransferQty() {
        return planTransferQty;
    }

    public void setPlanTransferQty(BigDecimal planTransferQty) {
        this.planTransferQty = planTransferQty;
    }

    public BigDecimal getActualTransferQty() {
        return actualTransferQty;
    }

    public void setActualTransferQty(BigDecimal actualTransferQty) {
        this.actualTransferQty = actualTransferQty;
    }

    public BigDecimal getActualReceiveQty() {
        return actualReceiveQty;
    }

    public void setActualReceiveQty(BigDecimal actualReceiveQty) {
        this.actualReceiveQty = actualReceiveQty;
    }

    @Schema(description = "领料单明细信息")
    @JsonProperty(value = "fmreqItemId", access = READ_ONLY)
    public ReferenceData getFmreqItemIdRef() {
        return this.fmreqItemId == null
            ? null
            : new ReferenceData(this.fmreqItemId);
    }

    @Schema(description = "出库单合计的明细信息")
    @JsonProperty(value = "fmirTotalId", access = READ_ONLY)
    public ReferenceData getFmirTotalIdRef() {
        return this.fmirTotalId == null
            ? null
            : new ReferenceData(this.fmirTotalId);
    }

    @Schema(description = "配送单详细信息")
    @JsonProperty(value = "fmtId", access = READ_ONLY)
    public ReferenceData getFmtIdRef() {
        return this.fmtId == null
            ? null
            : new ReferenceData(this.fmtId);
    }
}
