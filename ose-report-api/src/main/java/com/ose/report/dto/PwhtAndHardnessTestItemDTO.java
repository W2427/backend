package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PwhtAndHardnessTestItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -5592623564996347247L;

    @Schema(description = "母材")
    private String material;

    @Schema(description = "图纸号")
    private String drawingNo;

    @Schema(description = "焊口")
    private String weldNo;

    @Schema(description = "尺寸")
    private String size;

    private String thk;

    private String baseTestValue;

    private String hazTestValue;

    private String weldTestValue;

    private String required;

    @Schema(description = "结果")
    private String result;

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThk() {
        return thk;
    }

    public void setThk(String thk) {
        this.thk = thk;
    }

    public String getBaseTestValue() {
        return baseTestValue;
    }

    public void setBaseTestValue(String baseTestValue) {
        this.baseTestValue = baseTestValue;
    }

    public String getHazTestValue() {
        return hazTestValue;
    }

    public void setHazTestValue(String hazTestValue) {
        this.hazTestValue = hazTestValue;
    }

    public String getWeldTestValue() {
        return weldTestValue;
    }

    public void setWeldTestValue(String weldTestValue) {
        this.weldTestValue = weldTestValue;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
