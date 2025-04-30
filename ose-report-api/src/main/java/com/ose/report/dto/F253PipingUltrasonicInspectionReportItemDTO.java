package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F253PipingUltrasonicInspectionReportItemDTO extends BaseReportListItemDTO{

    private static final long serialVersionUID = 5274795760788369715L;

    @Schema(name = "零件号")
    private String partNo;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊缝类型")
    private String jointType;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "厚度")
    private String thick;

    @Schema(name = "测试长度")
    private String testLength;

    @Schema(name = "长度")
    private String length;

    @Schema(name = "深度")
    private String depth;

    @Schema(name = "高度")
    private String echoHeight;

    @Schema(name = "acc")
    private String acc;

    @Schema(name = "rej")
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

    public String getWelderNo() {
        return welderNo;
    }

    public void setWelderNo(String welderNo) {
        this.welderNo = welderNo;
    }

    public String getThick() {
        return thick;
    }

    public void setThick(String thick) {
        this.thick = thick;
    }

    public String getTestLength() {
        return testLength;
    }

    public void setTestLength(String testLength) {
        this.testLength = testLength;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDepth() {
        return depth;
    }

    public void setDepth(String depth) {
        this.depth = depth;
    }

    public String getEchoHeight() {
        return echoHeight;
    }

    public void setEchoHeight(String echoHeight) {
        this.echoHeight = echoHeight;
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
