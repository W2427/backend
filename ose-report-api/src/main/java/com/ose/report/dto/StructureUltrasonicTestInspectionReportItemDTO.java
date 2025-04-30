package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructureUltrasonicTestInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 1554144203163250812L;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "焊接长度")
    private String weldLength;

    @Schema(name = "比例值")
    private String extentOfExamination;

    @Schema(name = "壁厚")
    private String thickness;

    @Schema(name = "结果")
    private String result;

    @Schema(name = "测试结果")
    private String testResult;

    @Schema(name = "流程id")
    private Long actInstId;

    @Schema(name = "")
    private String reflectorNo;

    @Schema(name = "流程id")
    private String probe;

    @Schema(name = "流程id")
    private String point;

    @Schema(name = "流程id")
    private String defectLength;

    @Schema(name = "流程id")
    private String defectHeight;

    @Schema(name = "流程id")
    private String surfaceBelowDepth;

    @Schema(name = "流程id")
    private String echoAmpl;

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

    public String getWeldLength() {
        return weldLength;
    }

    public void setWeldLength(String weldLength) {
        this.weldLength = weldLength;
    }

    public String getExtentOfExamination() {
        return extentOfExamination;
    }

    public void setExtentOfExamination(String extentOfExamination) {
        this.extentOfExamination = extentOfExamination;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getReflectorNo() {
        return reflectorNo;
    }

    public void setReflectorNo(String reflectorNo) {
        this.reflectorNo = reflectorNo;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getDefectLength() {
        return defectLength;
    }

    public void setDefectLength(String defectLength) {
        this.defectLength = defectLength;
    }

    public String getDefectHeight() {
        return defectHeight;
    }

    public void setDefectHeight(String defectHeight) {
        this.defectHeight = defectHeight;
    }

    public String getSurfaceBelowDepth() {
        return surfaceBelowDepth;
    }

    public void setSurfaceBelowDepth(String surfaceBelowDepth) {
        this.surfaceBelowDepth = surfaceBelowDepth;
    }

    public String getEchoAmpl() {
        return echoAmpl;
    }

    public void setEchoAmpl(String echoAmpl) {
        this.echoAmpl = echoAmpl;
    }
}
