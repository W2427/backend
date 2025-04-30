package com.ose.report.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public class MaterialReturnItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -4463700988407340682L;

    @Schema(description = "材料编码")
    private String tagNum;

    @Schema(description = "材料描述")
    private String materialDesc;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "数量")
    private String quantity;

    @Schema(description = "ident")
    private String ident;

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

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
