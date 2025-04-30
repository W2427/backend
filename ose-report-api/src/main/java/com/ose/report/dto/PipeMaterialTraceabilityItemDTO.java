package com.ose.report.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public class PipeMaterialTraceabilityItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 8064001319430917588L;

    @Schema(description = "管件号")
    private String pipeNo;

    @Schema(description = "图号")
    private String drawingNo;

    @Schema(description = "材质")
    private String material;

    @Schema(description = "规格")
    private String specification;

    @Schema(description = "数量")
    private String quantity;

    @Schema(description = "批号")
    private String batchNo;

    @Schema(description = "炉号")
    private String heatNo;

    @Schema(description = "材料检验报告号")
    private String reportNo;

    @Schema(description = "备注")
    private String remark;

    public String getPipeNo() {
        return pipeNo;
    }

    public void setPipeNo(String pipeNo) {
        this.pipeNo = pipeNo;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
