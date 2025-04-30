package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PipingVisualInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 924173211351388881L;

    @Schema(name = "ISO号")
    private String drawingNo;

    @Schema(name = "ISO版本")
    private String rev;

    @Schema(name = "SPOOL号")
    private String spoolNo;

    @Schema(name = "焊口号")
    private String jointNo;

    @Schema(name = "焊缝类型")
    private String jointType;

    @Schema(name = "WPS")
    private String wps;

    @Schema(name = "材料尺寸")
    private String size;

    @Schema(name = "壁厚")
    private String thickness;

    @Schema(name = "焊工号")
    private String welderId;

    @Schema(name = "焊接日期")
    private String weldDate;

    @Schema(name = "焊接结果")
    private String result;

    @Schema(name = "流程id")
    private Long actInstId;

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

    public String getJointType() {
        return jointType;
    }

    public void setJointType(String jointType) {
        this.jointType = jointType;
    }

    public String getWps() {
        return wps;
    }

    public void setWps(String wps) {
        this.wps = wps;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getWelderId() {
        return welderId;
    }

    public void setWelderId(String welderId) {
        this.welderId = welderId;
    }

    public String getWeldDate() {
        return weldDate;
    }

    public void setWeldDate(String weldDate) {
        this.weldDate = weldDate;
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
