package com.ose.tasks.entity.material;

import com.ose.entity.BaseBizEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 * SPM 计划放行单明细 实体类。
 */
@Entity
@Table(name = "mat_spm_plan_release_note_item")
public class SpmPlanReleaseNoteItemEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "SPM 计划放行单ID,OSE 编号")
    private Long spmPlanRelnId;

    @Schema(description = "SPM 放行单编号")
    private String spmRelnNumber;

    @Schema(description = "合计放行数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal totalRelnQty;

    @Schema(description = "单个项目ID")
    private String spmItemShipId;

    @Schema(description = "材料编码")
    private String spmTagNumber;

    @Schema(description = "ident码")
    private String ident;

//    @Schema(description = "合同单件物品ID")
//    private int spmPoliId;
//
//    @Schema(description = "专业ID")
//    private int spmDpId;
//
//    @Schema(description = "专业")
//    private String spmDpCode;
//
//    @Schema(description = "材料单位ID")
//    private int spmQtyUnitId;
//
//    @Schema(description = "材料单位")
//    private String spmQtyUnitCode;
//
//    @Schema(description = "订单数量")
//    @Column(precision = 19, scale = 3)
//    private BigDecimal spmPoliQty;
//
//    @Schema(description = "放行数量")
//    @Column(precision = 19, scale = 3)
//    private BigDecimal spmRelnQty;
//
//    @Schema(description = "放行重量")
//    @Column(precision = 19, scale = 3)
//    private BigDecimal spmRelnWeight;
//
//    @Schema(description = "材料编码ID")
//    private int spmTagNumberId;
//
//    @Schema(description = "材料描述")
//    private String spmShortDesc;

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

    public String getSpmRelnNumber() {
        return spmRelnNumber;
    }

    public void setSpmRelnNumber(String spmRelnNumber) {
        this.spmRelnNumber = spmRelnNumber;
    }

    public Long getSpmPlanRelnId() {
        return spmPlanRelnId;
    }

    public void setSpmPlanRelnId(Long spmPlanRelnId) {
        this.spmPlanRelnId = spmPlanRelnId;
    }

    public BigDecimal getTotalRelnQty() {
        return totalRelnQty;
    }

    public void setTotalRelnQty(BigDecimal totalRelnQty) {
        this.totalRelnQty = totalRelnQty;
    }

    public String getSpmItemShipId() {
        return spmItemShipId;
    }

    public void setSpmItemShipId(String spmItemShipId) {
        this.spmItemShipId = spmItemShipId;
    }

    public String getSpmTagNumber() {
        return spmTagNumber;
    }

    public void setSpmTagNumber(String spmTagNumber) {
        this.spmTagNumber = spmTagNumber;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }
}
