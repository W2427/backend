package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class MaterialApplicationItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -9001573416410453704L;

    @Schema(description = "材料编码")
    private String tagNum;

    @Schema(description = "材料描述")
    private String materialDesc;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "数量")
    private String quantity;

    @Schema(description = "用途")
    private String purpose;

    @Schema(description = "库存类型")
    private String warehouseType;

    @Schema(description = "备注")
    private String remark;

    public String getTagNum() {
        return tagNum;
    }

    public void setTagNum(String tagNum) {
        this.tagNum = tagNum;
    }

    public String getMaterialDesc() {
        return materialDesc;
    }

    public void setMaterialDesc(String materialDesc) {
        this.materialDesc = materialDesc;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getWarehouseType() {
        return warehouseType;
    }

    public void setWarehouseType(String warehouseType) {
        this.warehouseType = warehouseType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
