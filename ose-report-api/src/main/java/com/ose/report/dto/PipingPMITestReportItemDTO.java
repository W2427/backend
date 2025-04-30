package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PipingPMITestReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 4286044413476563415L;

    @Schema(name = "ISO号")
    private String drawingNo;

    @Schema(name = "焊口号")
    private String jointNo;

    @Schema(name = "炉批号")
    private String heatNumber;

    @Schema(name = "材料描述")
    private String materialDescription;

    @Schema(name = "流程id")
    private Long actInstId;

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getJointNo() {
        return jointNo;
    }

    public void setJointNo(String jointNo) {
        this.jointNo = jointNo;
    }

    public String getHeatNumber() {
        return heatNumber;
    }

    public void setHeatNumber(String heatNumber) {
        this.heatNumber = heatNumber;
    }

    public String getMaterialDescription() {
        return materialDescription;
    }

    public void setMaterialDescription(String materialDescription) {
        this.materialDescription = materialDescription;
    }

    public Long getActInstId() {
        return actInstId;
    }

    public void setActInstId(Long actInstId) {
        this.actInstId = actInstId;
    }
}
