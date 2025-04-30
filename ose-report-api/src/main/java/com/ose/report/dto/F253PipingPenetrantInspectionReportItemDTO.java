package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F253PipingPenetrantInspectionReportItemDTO extends BaseReportListItemDTO{

    private static final long serialVersionUID = 5489850133794096549L;

    @Schema(name = "零件号")
    private String partNo;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊缝类型")
    private String jointType;

    @Schema(name = "测试长度")
    private String testLength;

    @Schema(name = "焊工号")
    private String welderNo;

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

    public String getJointType() {
        return jointType;
    }

    public void setJointType(String jointType) {
        this.jointType = jointType;
    }

    public String getTestLength() {
        return testLength;
    }

    public void setTestLength(String testLength) {
        this.testLength = testLength;
    }

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
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
