package com.ose.tasks.entity.material;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * 领料单明细实体类。
 */
@Entity
@Table(name = "mat_f_material_requisition_item")
public class FMaterialRequisitionItemEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    private String fmreqId;

    @Schema(description = "合同详情ID")
    private String spmFadId;

    @Schema(description = "大类")
    private String spmCgGroupCode;

    @Schema(description = "小类")
    private String spmPartCode;

    @Schema(description = "材料编码")
    private String spmTagNumber;

    @Schema(description = "ident码")
    private String ident;

    @Schema(description = "描述")
    private String spmShortDesc;

    @Schema(description = "数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal spmQuantity;

    @Schema(description = "预留数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal spmResvQty;

    @Schema(description = "已出库数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal spmIssueQty;

    @Schema(description = "临时单预留数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal spmQty;

    @Schema(description = "BOM结构")
    private String spmBomPath;

    @Schema(description = "计量单位")
    private String spmUnitCode;

    @Schema(description = "专业")
    private String spmDpCode;

    @Schema(description = "")
    private String spmLpId;

    @Schema(description = "计划配送数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal planTransferQty;

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

    public String getFmreqId() {
        return fmreqId;
    }

    public void setFmreqId(String fmreqId) {
        this.fmreqId = fmreqId;
    }

    public String getSpmFadId() {
        return spmFadId;
    }

    public void setSpmFadId(String spmFadId) {
        this.spmFadId = spmFadId;
    }

    public String getSpmCgGroupCode() {
        return spmCgGroupCode;
    }

    public void setSpmCgGroupCode(String spmCgGroupCode) {
        this.spmCgGroupCode = spmCgGroupCode;
    }

    public String getSpmPartCode() {
        return spmPartCode;
    }

    public void setSpmPartCode(String spmPartCode) {
        this.spmPartCode = spmPartCode;
    }

    public String getSpmTagNumber() {
        return spmTagNumber;
    }

    public void setSpmTagNumber(String spmTagNumber) {
        this.spmTagNumber = spmTagNumber;
    }

    public String getSpmShortDesc() {
        return spmShortDesc;
    }

    public void setSpmShortDesc(String spmShortDesc) {
        this.spmShortDesc = spmShortDesc;
    }

    public BigDecimal getSpmQuantity() {
        return spmQuantity;
    }

    public void setSpmQuantity(BigDecimal spmQuantity) {
        this.spmQuantity = spmQuantity;
    }

    public BigDecimal getSpmIssueQty() {
        return spmIssueQty;
    }

    public void setSpmIssueQty(BigDecimal spmIssueQty) {
        this.spmIssueQty = spmIssueQty;
    }

    public BigDecimal getSpmQty() {
        return spmQty;
    }

    public void setSpmQty(BigDecimal spmQty) {
        this.spmQty = spmQty;
    }

    public String getSpmBomPath() {
        return spmBomPath;
    }

    public void setSpmBomPath(String spmBomPath) {
        this.spmBomPath = spmBomPath;
    }

    public String getSpmUnitCode() {
        return spmUnitCode;
    }

    public void setSpmUnitCode(String spmUnitCode) {
        this.spmUnitCode = spmUnitCode;
    }

    public BigDecimal getSpmResvQty() {
        return spmResvQty;
    }

    public void setSpmResvQty(BigDecimal spmResvQty) {
        this.spmResvQty = spmResvQty;
    }

    public String getSpmDpCode() {
        return spmDpCode;
    }

    public void setSpmDpCode(String spmDpCode) {
        this.spmDpCode = spmDpCode;
    }

    public String getSpmLpId() {
        return spmLpId;
    }

    public void setSpmLpId(String spmLpId) {
        this.spmLpId = spmLpId;
    }

    public BigDecimal getPlanTransferQty() {
        return planTransferQty;
    }

    public void setPlanTransferQty(BigDecimal planTransferQty) {
        this.planTransferQty = planTransferQty;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
}
