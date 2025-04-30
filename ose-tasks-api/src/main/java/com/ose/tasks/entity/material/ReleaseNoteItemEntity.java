package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.qc.ReportSubType;
import com.ose.tasks.vo.material.TagNumberType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

/**
 * 放行单详情实体类。
 */
@Entity
@Table(name = "mat_release_note_item",
    indexes = {
        @Index(columnList = "orgId,projectId,relnId")
    })

public class ReleaseNoteItemEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "放行单ID")
    private Long relnId;

    @Schema(description = "计划放行单ID, BT1234")
    private Long spmPlanRelnId;

    @Schema(description = "单个项目ID")
    private String spmItemShipId;

    @Schema(description = "计划放行单明细ID, BT1234")
    private Long spmPlanRelnItemId;

    @Schema(description = "合同单件物品ID")
    private int poliId;

    @Schema(description = "专业ID")
    private int dpId;

    @Schema(description = "专业")
    private String dpCode;

    @Schema(description = "材料单位ID")
    private int qtyUnitId;

    @Schema(description = "材料单位")
    private String qtyUnitCode;

    @Schema(description = "订单数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal poliQty;

    @Schema(description = "放行数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal relnQty;

    @Schema(description = "合计数量")
    @Column(precision = 19, scale = 3)
    private BigDecimal totalQty;

    @Schema(description = "放行重量")
    @Column(precision = 19, scale = 3)
    private BigDecimal relnWeight;

    @Schema(description = "材料编码ID")
    private int tagNumberId;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "ident码")
    private String ident;

    @Schema(description = "材料类型(一物一码，一类一码)")
    @Enumerated(EnumType.STRING)
    private TagNumberType tagNumberType;

    @Schema(description = "材料描述")
    @Column(length = 500)
    private String shortDesc;

    @Schema(description = "验收报告的类型(管材料, 管附件, 阀件)")
    @Enumerated(EnumType.STRING)
    private ReportSubType reportSubType;

    public Long getRelnId() {
        return relnId;
    }

    public void setRelnId(Long relnId) {
        this.relnId = relnId;
    }

    public int getQtyUnitId() {
        return qtyUnitId;
    }

    public void setQtyUnitId(int qtyUnitId) {
        this.qtyUnitId = qtyUnitId;
    }

    public String getQtyUnitCode() {
        return qtyUnitCode;
    }

    public void setQtyUnitCode(String qtyUnitCode) {
        this.qtyUnitCode = qtyUnitCode;
    }

    public BigDecimal getPoliQty() {
        return poliQty;
    }

    public void setPoliQty(BigDecimal poliQty) {
        this.poliQty = poliQty;
    }

    public BigDecimal getRelnQty() {
        return relnQty;
    }

    public void setRelnQty(BigDecimal relnQty) {
        this.relnQty = relnQty;
    }

    public BigDecimal getRelnWeight() {
        return relnWeight;
    }

    public void setRelnWeight(BigDecimal relnWeight) {
        this.relnWeight = relnWeight;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public int getDpId() {
        return dpId;
    }

    public void setDpId(int dpId) {
        this.dpId = dpId;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public int getPoliId() {
        return poliId;
    }

    public void setPoliId(int poliId) {
        this.poliId = poliId;
    }

    public int getTagNumberId() {
        return tagNumberId;
    }

    public void setTagNumberId(int tagNumberId) {
        this.tagNumberId = tagNumberId;
    }

    public TagNumberType getTagNumberType() {
        return tagNumberType;
    }

    public void setTagNumberType(TagNumberType tagNumberType) {
        this.tagNumberType = tagNumberType;
    }

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

    public BigDecimal getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(BigDecimal totalQty) {
        this.totalQty = totalQty;
    }

    public ReportSubType getReportSubType() {
        return reportSubType;
    }

    public void setReportSubType(ReportSubType reportSubType) {
        this.reportSubType = reportSubType;
    }

    public Long getSpmPlanRelnId() {
        return spmPlanRelnId;
    }

    public void setSpmPlanRelnId(Long spmPlanRelnId) {
        this.spmPlanRelnId = spmPlanRelnId;
    }

    public Long getSpmPlanRelnItemId() {
        return spmPlanRelnItemId;
    }

    public void setSpmPlanRelnItemId(Long spmPlanRelnItemId) {
        this.spmPlanRelnItemId = spmPlanRelnItemId;
    }

    @Schema(description = "放行单信息")
    @JsonProperty(value = "relnId", access = READ_ONLY)
    public ReferenceData getRelnIdRef() {
        return this.relnId == null
            ? null
            : new ReferenceData(this.relnId);
    }

    @Schema(description = "SPM 计划放行单明细信息")
    @JsonProperty(value = "spmPlanRelnItemId", access = READ_ONLY)
    public ReferenceData getSpmPlanRelnItemIdRef() {
        return this.spmPlanRelnItemId == null
            ? null
            : new ReferenceData(this.spmPlanRelnItemId);
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getSpmItemShipId() {
        return spmItemShipId;
    }

    public void setSpmItemShipId(String spmItemShipId) {
        this.spmItemShipId = spmItemShipId;
    }
}
