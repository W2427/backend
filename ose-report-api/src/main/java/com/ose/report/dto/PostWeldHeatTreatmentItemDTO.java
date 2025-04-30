package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PostWeldHeatTreatmentItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -2788849466298811333L;

    @Schema(description = "图纸号")
    private String drawingNo;

    @Schema(description = "管线号")
    private String lineNo;

    @Schema(description = "焊口")
    private String weld;

    @Schema(description = "母材")
    private String material;

    @Schema(description = "厚度")
    private String thickness;

    @Schema(description = "焊接方法")
    private String process;

    @Schema(description = "焊接工艺规程")
    private String wps;

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getLineNo() {
        return lineNo;
    }

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public String getWeld() {
        return weld;
    }

    public void setWeld(String weld) {
        this.weld = weld;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getThickness() {
        return thickness;
    }

    public void setThickness(String thickness) {
        this.thickness = thickness;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getWps() {
        return wps;
    }

    public void setWps(String wps) {
        this.wps = wps;
    }

}
