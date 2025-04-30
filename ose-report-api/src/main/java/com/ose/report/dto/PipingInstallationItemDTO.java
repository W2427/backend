package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class PipingInstallationItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -8534293668042830736L;

    @Schema(description = "图号")
    private String drawingNo;

    @Schema(description = "原理图")
    private String pidNo;

    @Schema(description = "管系号")
    private String pipelineNo;

    @Schema(description = "检验日期")
    private Date inspectionDate;

    @Schema(description = "备注")
    private String remark;

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getPidNo() {
        return pidNo;
    }

    public void setPidNo(String pidNo) {
        this.pidNo = pidNo;
    }

    public String getPipelineNo() {
        return pipelineNo;
    }

    public void setPipelineNo(String pipelineNo) {
        this.pipelineNo = pipelineNo;
    }

    public Date getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(Date inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
