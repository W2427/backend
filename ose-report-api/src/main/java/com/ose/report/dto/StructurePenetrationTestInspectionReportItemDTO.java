package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructurePenetrationTestInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 2716694100114437710L;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "焊接长度")
    private String weldLength;

    @Schema(name = "结果")
    private String judge;

    @Schema(name = "焊接日期")
    private String weldDate;

    @Schema(name = "流程id")
    private Long actInstId;

    @Schema(name = "")
    private String size;

    @Schema(name = "")
    private String dp;

    @Schema(name = "")
    private String di;

    @Schema(name = "")
    private String dl;

    @Schema(name = "")
    private String remark;

    @Schema(name = "")
    private String detectionScale;

    @Schema(name = "")
    private String no;

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

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getWeldDate() {
        return weldDate;
    }

    public void setWeldDate(String weldDate) {
        this.weldDate = weldDate;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public String getDl() {
        return dl;
    }

    public void setDl(String dl) {
        this.dl = dl;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDetectionScale() {
        return detectionScale;
    }

    public void setDetectionScale(String detectionScale) {
        this.detectionScale = detectionScale;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
