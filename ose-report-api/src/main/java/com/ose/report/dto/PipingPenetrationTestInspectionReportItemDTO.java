package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PipingPenetrationTestInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 4886571045239569979L;

    @Schema(name = "焊口号")
    private String weldNo;

    @Schema(name = "焊工号")
    private String welderNo;

    @Schema(name = "尺寸")
    private String size;

    @Schema(name = "焊接长度")
    private String weldLength;

    @Schema(name = "结果")
    private String result;

    @Schema(name = "焊接日期")
    private String weldDate;

    @Schema(name = "流程id")
    private Long actInstId;

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

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getWeldLength() {
        return weldLength;
    }

    public void setWeldLength(String weldLength) {
        this.weldLength = weldLength;
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
