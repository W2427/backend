package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PeLiningInspectionItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -1412818207766115310L;

    @Schema(description = "管线号")
    private String lineNo;

    @Schema(description = "图纸号")
    private String drawingNo;

    @Schema(description = "备注")
    private String remark;

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
