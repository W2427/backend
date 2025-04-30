package com.ose.tasks.dto.material;

import com.ose.dto.BaseDTO;
import com.ose.tasks.vo.material.FItemStatusType;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class FItemDetailEntityDTO extends BaseDTO {
    private static final long serialVersionUID = 5998899284445078401L;



    private Long orgId;

    private Long projectId;

    @Schema(description = "item_detail_id")
    private Long itemDetailId;

    @Schema(description = "二维码")
    private String rnQrCode;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "IDENT")
    private String ident;

    @Schema(description = "材料规格")
    private String spec;

    @Schema(description = "材料单位")
    private String unitCode;

    @Schema(description = "炉号")
    private String heatNo;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "描述")
    private String shortDesc;

    @Schema(description = "数量")
    private double qty;

    @Schema(description = "材料批号")
    private String materialBatchNumber;

    private Long relationShipId;

    @Schema(description = "入库报告")
    private String fmrrCode;

    @Schema(description = "出库报告")
    private String fmirCode;

    @Schema(description = "清单描述")
    private String listShortDesc;

    @Schema(description = "状态")
    @Enumerated(EnumType.STRING)
    private FItemStatusType itemStatus;

    public String getListShortDesc() {
        return listShortDesc;
    }

    public void setListShortDesc(String listShortDesc) {
        this.listShortDesc = listShortDesc;
    }

    public String getFmirCode() {
        return fmirCode;
    }

    public void setFmirCode(String fmirCode) {
        this.fmirCode = fmirCode;
    }

    public String getFmrrCode() {
        return fmrrCode;
    }

    public void setFmrrCode(String fmrrCode) {
        this.fmrrCode = fmrrCode;
    }

    public Long getRelationShipId() {
        return relationShipId;
    }

    public void setRelationShipId(Long relationShipId) {
        this.relationShipId = relationShipId;
    }

    public String getMaterialBatchNumber() {
        return materialBatchNumber;
    }

    public void setMaterialBatchNumber(String materialBatchNumber) {
        this.materialBatchNumber = materialBatchNumber;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
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

    public Long getItemDetailId() {
        return itemDetailId;
    }

    public void setItemDetailId(Long itemDetailId) {
        this.itemDetailId = itemDetailId;
    }

    public String getRnQrCode() {
        return rnQrCode;
    }

    public void setRnQrCode(String rnQrCode) {
        this.rnQrCode = rnQrCode;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getUnitCode() {
        return unitCode;
    }

    public void setUnitCode(String unitCode) {
        this.unitCode = unitCode;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public FItemStatusType getItemStatus() {
        return itemStatus;
    }

    public void setItemStatus(FItemStatusType itemStatus) {
        this.itemStatus = itemStatus;
    }
}
