package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class IncomingMaterialInspectionItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 4479305103136207555L;

    @Schema(description = "材料名称")
    private String materialName;

    @Schema(description = "材料描述")
    private String materialDescription;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "到货数量")
    private String quantity;

    @Schema(description = "抽检比例")
    private String inspectionPercentage;

    @Schema(description = "抽检数量")
    private String quantityInspected;

    @Schema(description = "样件编号")
    private String sampleNo;

    @Schema(description = "炉批号")
    private String heatNo;

    @Schema(description = "外观尺寸")
    private String appearanceOrDimension;

    @Schema(description = "其他检验项")
    private String otherInspection;

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
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

    public String getInspectionPercentage() {
        return inspectionPercentage;
    }

    public void setInspectionPercentage(String inspectionPercentage) {
        this.inspectionPercentage = inspectionPercentage;
    }

    public String getQuantityInspected() {
        return quantityInspected;
    }

    public void setQuantityInspected(String quantityInspected) {
        this.quantityInspected = quantityInspected;
    }

    public String getSampleNo() {
        return sampleNo;
    }

    public void setSampleNo(String sampleNo) {
        this.sampleNo = sampleNo;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getAppearanceOrDimension() {
        return appearanceOrDimension;
    }

    public void setAppearanceOrDimension(String appearanceOrDimension) {
        this.appearanceOrDimension = appearanceOrDimension;
    }

    public String getOtherInspection() {
        return otherInspection;
    }

    public void setOtherInspection(String otherInspection) {
        this.otherInspection = otherInspection;
    }

}
