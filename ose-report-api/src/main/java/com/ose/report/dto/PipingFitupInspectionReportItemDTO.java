package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PipingFitupInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -1459184007861673410L;

    @Schema(name = "ISO号")
    private String drawingNo;

    @Schema(name = "版本")
    private String revision;

    @Schema(name = "spool号")
    private String spoolNo;

    @Schema(name = "焊口号")
    private String jointNo;

    @Schema(name = "wps号")
    private String wps;

    @Schema(name = "尺寸1")
    private String size1;

    @Schema(name = "壁厚1")
    private String thickness1;

    @Schema(name = "tag号1")
    private String group1;

    @Schema(name = "材质1")
    private String material1;

    @Schema(name = "炉批号1")
    private String heatNo1;

    @Schema(name = "尺寸2")
    private String size2;

    @Schema(name = "壁厚2")
    private String thickness2;

    @Schema(name = "tag号2")
    private String group2;

    @Schema(name = "材质2")
    private String material2;

    @Schema(name = "炉批号2")
    private String heatNo2;

    @Schema(name = "结果")
    private String result;

    @Schema(name = "流程id")
    private Long actInstId;

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getSpoolNo() {
        return spoolNo;
    }

    public void setSpoolNo(String spoolNo) {
        this.spoolNo = spoolNo;
    }

    public String getJointNo() {
        return jointNo;
    }

    public void setJointNo(String jointNo) {
        this.jointNo = jointNo;
    }

    public String getWps() {
        return wps;
    }

    public void setWps(String wps) {
        this.wps = wps;
    }

    public String getSize1() {
        return size1;
    }

    public void setSize1(String size1) {
        this.size1 = size1;
    }

    public String getThickness1() {
        return thickness1;
    }

    public void setThickness1(String thickness1) {
        this.thickness1 = thickness1;
    }

    public String getGroup1() {
        return group1;
    }

    public void setGroup1(String group1) {
        this.group1 = group1;
    }

    public String getMaterial1() {
        return material1;
    }

    public void setMaterial1(String material1) {
        this.material1 = material1;
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

    public String getThickness2() {
        return thickness2;
    }

    public void setThickness2(String thickness2) {
        this.thickness2 = thickness2;
    }

    public String getGroup2() {
        return group2;
    }

    public void setGroup2(String group2) {
        this.group2 = group2;
    }

    public String getMaterial2() {
        return material2;
    }

    public void setMaterial2(String material2) {
        this.material2 = material2;
    }

    public String getHeatNo2() {
        return heatNo2;
    }

    public void setHeatNo2(String heatNo2) {
        this.heatNo2 = heatNo2;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }
}
