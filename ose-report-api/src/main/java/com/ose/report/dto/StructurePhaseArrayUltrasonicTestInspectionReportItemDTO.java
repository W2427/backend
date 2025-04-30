package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructurePhaseArrayUltrasonicTestInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 4796547705747198479L;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "材质")
    private String defectNo;

    @Schema(name = "长度")
    private String weldLength;

    @Schema(name = "壁厚")
    private String inspectionLength;

    @Schema(name = "结果")
    private String x;

    @Schema(name = "流程id")
    private String y;

    @Schema(name = "")
    private String z;

    @Schema(name = "")
    private String length;

    @Schema(name = "")
    private String height;

    @Schema(name = "")
    private String amplitude;

    @Schema(name = "")
    private String defectType;

    @Schema(name = "")
    private String result;

    @Schema(name = "焊口号")
    private String no;

    @Schema(name = "焊口号")
    private String remark;

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getDefectNo() {
        return defectNo;
    }

    public void setDefectNo(String defectNo) {
        this.defectNo = defectNo;
    }

    public String getWeldLength() {
        return weldLength;
    }

    public void setWeldLength(String weldLength) {
        this.weldLength = weldLength;
    }

    public String getInspectionLength() {
        return inspectionLength;
    }

    public void setInspectionLength(String inspectionLength) {
        this.inspectionLength = inspectionLength;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(String amplitude) {
        this.amplitude = amplitude;
    }

    public String getDefectType() {
        return defectType;
    }

    public void setDefectType(String defectType) {
        this.defectType = defectType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
