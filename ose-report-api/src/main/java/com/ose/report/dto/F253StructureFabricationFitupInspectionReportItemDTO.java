package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F253StructureFabricationFitupInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -9186949125042052609L;

    @Schema(name = "构件号")
    private String tagNo;

    @Schema(name = "图纸")
    private String weldClass;

    @Schema(name = "图纸")
    private String quantity;

    @Schema(name = "图纸")
    private String drawingNo;

    @Schema(name = "图纸版本")
    private String revision;

    @Schema(name = "结果")
    private String result;

    public String getTagNo() {
        return tagNo;
    }

    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public String getWeldClass() {
        return weldClass;
    }

    public void setWeldClass(String weldClass) {
        this.weldClass = weldClass;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
