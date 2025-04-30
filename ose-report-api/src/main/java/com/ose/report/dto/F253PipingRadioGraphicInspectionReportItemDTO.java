package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F253PipingRadioGraphicInspectionReportItemDTO extends BaseReportListItemDTO{

    private static final long serialVersionUID = -1169694758173706099L;

    @Schema(name = "零件号")
    private String partNo;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "片位号")
    private String filmNo;

    @Schema(name = "规格")
    private String size;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "测试长度")
    private String testLength;

    @Schema(name = "渗透剂指数")
    private String iqiVisibleNo;

    @Schema(name = "黑度")
    private String density;

    @Schema(name = "缺陷类型")
    private String defectType;

    @Schema(name = "缺陷长度")
    private String defectLength;

    @Schema(name = "接受")
    private String acc;

    @Schema(name = "拒绝")
    private String rej;

    @Schema(name = "意见")
    private String remark;

    public String getPartNo() {
        return partNo;
    }

    public void setPartNo(String partNo) {
        this.partNo = partNo;
    }

    public String getWeldNo() {
        return weldNo;
    }

    public void setWeldNo(String weldNo) {
        this.weldNo = weldNo;
    }

    public String getFilmNo() {
        return filmNo;
    }

    public void setFilmNo(String filmNo) {
        this.filmNo = filmNo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getTestLength() {
        return testLength;
    }

    public void setTestLength(String testLength) {
        this.testLength = testLength;
    }

    public String getIqiVisibleNo() {
        return iqiVisibleNo;
    }

    public void setIqiVisibleNo(String iqiVisibleNo) {
        this.iqiVisibleNo = iqiVisibleNo;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getDefectType() {
        return defectType;
    }

    public void setDefectType(String defectType) {
        this.defectType = defectType;
    }

    public String getDefectLength() {
        return defectLength;
    }

    public void setDefectLength(String defectLength) {
        this.defectLength = defectLength;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
    }

    public String getRej() {
        return rej;
    }

    public void setRej(String rej) {
        this.rej = rej;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
