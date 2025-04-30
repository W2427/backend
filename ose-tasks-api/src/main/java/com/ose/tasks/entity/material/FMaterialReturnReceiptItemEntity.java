package com.ose.tasks.entity.material;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ose.entity.BaseBizEntity;
import com.ose.tasks.vo.material.ReturnInspectionResultType;
import com.ose.tasks.vo.material.TagNumberType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 退库清单的材料表
 */
@Entity
@Table(name = "mat_f_material_return_receipt_item")
public class FMaterialReturnReceiptItemEntity extends BaseBizEntity {

    private static final long serialVersionUID = -5725891260099927636L;

    private Long orgId;

    private Long projectId;

    @Schema(description = "退库单ID")
    private Long fmReturnId;

    @Schema(description = "退库单关联出库清单ID")
    private Long fmReturnRelationIssueId;

    @Schema(description = "退库单关联出库清单合计信息表ID")
    private Long fmReturnRelationIssueTotalId;

    @Schema(description = "OSE 出库单ID")
    private Long fmirId;

    @Schema(description = "OSE 出库单合计信息表ID")
    private Long fmirTotalId;

    @Schema(description = "材料主表ID")
    private Long fItemId;

    @Schema(description = "材料详情表ID")
    private Long fItemDetailId;

    @Schema(description = "二维码")
    private String rnQrCode;

    @Schema(description = "炉批号ID")
    private Long heatNoId;

    @Schema(description = "专业")
    private String dpCode;

    @Schema(description = "材料规格")
    private String spec;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "ident码")
    private String ident;

    @Schema(description = "材料类型(一物一码，一类一码)")
    @Enumerated(EnumType.STRING)
    private TagNumberType tagNumberType;

    @Schema(description = "描述")
    private String shortDesc;

    @Schema(description = "材料单位")
    private String unitCode;

    @Schema(description = "材料单位ID")
    private String unitId;

    @Schema(description = "单位量")
    @Column(name = "qty", precision = 19, scale = 3)
    private BigDecimal qty;

    @Schema(description = "扫描的退库数量")
    private int qtyCnt;

    @Schema(description = "内检质量问题数量")
    private int internalInspectionQtyCnt = 0;

    @Schema(description = "备注")
    private String memo;

    @Schema(description = "退库检验结果（接收，拒收）")
    @Enumerated(EnumType.STRING)
    private ReturnInspectionResultType returnInspectionResult;

    @Schema(description = "材料入库状态")
    private boolean receiveFlg;

    public boolean isReceiveFlg() {
        return receiveFlg;
    }

    public void setReceiveFlg(boolean receiveFlg) {
        this.receiveFlg = receiveFlg;
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

    public Long getFmReturnRelationIssueTotalId() {
        return fmReturnRelationIssueTotalId;
    }

    public void setFmReturnRelationIssueTotalId(Long fmReturnRelationIssueTotalId) {
        this.fmReturnRelationIssueTotalId = fmReturnRelationIssueTotalId;
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

    public Long getfItemId() {
        return fItemId;
    }

    public void setfItemId(Long fItemId) {
        this.fItemId = fItemId;
    }

    public Long getfItemDetailId() {
        return fItemDetailId;
    }

    public void setfItemDetailId(Long fItemDetailId) {
        this.fItemDetailId = fItemDetailId;
    }

    public String getRnQrCode() {
        return rnQrCode;
    }

    public void setRnQrCode(String rnQrCode) {
        this.rnQrCode = rnQrCode;
    }

    public Long getHeatNoId() {
        return heatNoId;
    }

    public void setHeatNoId(Long heatNoId) {
        this.heatNoId = heatNoId;
    }

    public String getDpCode() {
        return dpCode;
    }

    public void setDpCode(String dpCode) {
        this.dpCode = dpCode;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public TagNumberType getTagNumberType() {
        return tagNumberType;
    }

    public void setTagNumberType(TagNumberType tagNumberType) {
        this.tagNumberType = tagNumberType;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public int getQtyCnt() {
        return qtyCnt;
    }

    public void setQtyCnt(int qtyCnt) {
        this.qtyCnt = qtyCnt;
    }

    public int getInternalInspectionQtyCnt() {
        return internalInspectionQtyCnt;
    }

    public void setInternalInspectionQtyCnt(int internalInspectionQtyCnt) {
        this.internalInspectionQtyCnt = internalInspectionQtyCnt;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public ReturnInspectionResultType getReturnInspectionResult() {
        return returnInspectionResult;
    }

    public void setReturnInspectionResult(ReturnInspectionResultType returnInspectionResult) {
        this.returnInspectionResult = returnInspectionResult;
    }

    @Schema(description = "炉批号信息")
    @JsonProperty(value = "heatNoId")
    public ReferenceData getHeatNoIdRef() {
        return this.heatNoId == null
            ? null
            : new ReferenceData(this.heatNoId);
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
