package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F217xPipingx14xPipingPWHTandHDTestReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 7842409569985764509L;

    @Schema(name = "图纸号")
    private String drawingNo;

    @Schema(name = "材料")
    private String material;

    @Schema(name = "焊口号")
    private String jointNo;

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getJointNo() {
        return jointNo;
    }

    public void setJointNo(String jointNo) {
        this.jointNo = jointNo;
    }
}
