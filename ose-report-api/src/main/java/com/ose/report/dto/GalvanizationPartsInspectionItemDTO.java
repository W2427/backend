package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class GalvanizationPartsInspectionItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -992266248212271878L;

    @Schema(description = "构件名称/件号")
    private String partName;

    @Schema(description = "图号")
    private String drawingNo;

    @Schema(description = "外观检查")
    private String visualInspection;

    @Schema(description = "镀锌层厚度测试1")
    private String thickness1;

    @Schema(description = "镀锌层厚度测试2")
    private String thickness2;

    @Schema(description = "镀锌层厚度测试3")
    private String thickness3;

    @Schema(description = "镀锌层厚度测试4")
    private String thickness4;

    @Schema(description = "镀锌层厚度测试5")
    private String thickness5;

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getVisualInspection() {
        return visualInspection;
    }

    public void setVisualInspection(String visualInspection) {
        this.visualInspection = visualInspection;
    }

    public String getThickness1() {
        return thickness1;
    }

    public void setThickness1(String thickness1) {
        this.thickness1 = thickness1;
    }

    public String getThickness2() {
        return thickness2;
    }

    public void setThickness2(String thickness2) {
        this.thickness2 = thickness2;
    }

    public String getThickness3() {
        return thickness3;
    }

    public void setThickness3(String thickness3) {
        this.thickness3 = thickness3;
    }

    public String getThickness4() {
        return thickness4;
    }

    public void setThickness4(String thickness4) {
        this.thickness4 = thickness4;
    }

    public String getThickness5() {
        return thickness5;
    }

    public void setThickness5(String thickness5) {
        this.thickness5 = thickness5;
    }

}
