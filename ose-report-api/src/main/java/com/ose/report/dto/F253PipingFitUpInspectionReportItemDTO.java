package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F253PipingFitUpInspectionReportItemDTO extends BaseReportListItemDTO{

    private static final long serialVersionUID = -5681414070481005452L;

    @Schema(name = "编号")
    private String sn;

    @Schema(name = "图纸号")
    private String drawingNo;

    @Schema(name = "版本")
    private String rev;

    @Schema(name = "焊口号")
    private String jointNo;

    @Schema(name = "规格1")
    private String size1;

    @Schema(name = "组件类型1")
    private String componentType1;

    @Schema(name = "炉号1")
    private String heatNo1;

    @Schema(name = "规格2")
    private String size2;

    @Schema(name = "组件2")
    private String componentType2;

    @Schema(name = "炉号2")
    private String heatNo2;

    @Schema(name = "尺寸")
    private String dimension;

    @Schema(name = "结果")
    private String result;

    @Schema(name = "意见")
    private String remark;

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getJointNo() {
        return jointNo;
    }

    public void setJointNo(String jointNo) {
        this.jointNo = jointNo;
    }

    public String getSize1() {
        return size1;
    }

    public void setSize1(String size1) {
        this.size1 = size1;
    }

    public String getComponentType1() {
        return componentType1;
    }

    public void setComponentType1(String componentType1) {
        this.componentType1 = componentType1;
    }

    public String getHeatNo1() {
        return heatNo1;
    }

    public void setHeatNo1(String heatNo1) {
        this.heatNo1 = heatNo1;
    }

    public String getSize2() {
        return size2;
    }

    public void setSize2(String size2) {
        this.size2 = size2;
    }

    public String getComponentType2() {
        return componentType2;
    }

    public void setComponentType2(String componentType2) {
        this.componentType2 = componentType2;
    }

    public String getHeatNo2() {
        return heatNo2;
    }

    public void setHeatNo2(String heatNo2) {
        this.heatNo2 = heatNo2;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
