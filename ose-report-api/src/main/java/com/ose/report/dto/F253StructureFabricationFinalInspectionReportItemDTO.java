package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class F253StructureFabricationFinalInspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -2259934531856505914L;

    @Schema(name = "构件号")
    private String TagNo;

    @Schema(name = "")
    private String weldClass;

    @Schema(name = "")
    private String quantity;

    @Schema(name = "")
    private String drawingNo;

    @Schema(name = "")
    private String revision;

    @Schema(name = "结果")
    private String result;

    public String getTagNo() {
        return TagNo;
    }

    public void setTagNo(String tagNo) {
        TagNo = tagNo;
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
}
