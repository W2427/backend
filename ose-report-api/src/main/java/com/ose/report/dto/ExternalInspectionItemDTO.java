package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class ExternalInspectionItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -5908100844994129643L;

    @Schema(description = "图号")
    private String drawingNo;

    @Schema(description = "外检对象编号")
    private String inspectionItemNo;

    @Schema(description = "检验结果")
    private String inspectionResult;

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

    public String getInspectionItemNo() {
        return inspectionItemNo;
    }

    public void setInspectionItemNo(String inspectionItemNo) {
        this.inspectionItemNo = inspectionItemNo;
    }

    public String getInspectionResult() {
        return inspectionResult;
    }

    public void setInspectionResult(String inspectionResult) {
        this.inspectionResult = inspectionResult;
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
