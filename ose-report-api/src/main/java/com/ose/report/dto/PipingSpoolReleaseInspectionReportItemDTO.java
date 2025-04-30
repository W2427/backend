package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class PipingSpoolReleaseInspectionReportItemDTO extends BaseReportListItemDTO{

    private static final long serialVersionUID = -4316557753135797302L;

    @Schema(description = "iso号")
    private String isoMetric;

    @Schema(description = "页码")
    private String sh;

    @Schema(description = "版本")
    private String rev;

    @Schema(description = "车间位置区分")
    private String area;

    @Schema(description = "spool号")
    private String toBeShippedNr;

    @Schema(description = "包含的piece")
    private String completePieceNo;

    @Schema(description = "备注")
    private String tagNotes;

    public String getIsoMetric() {
        return isoMetric;
    }

    public void setIsoMetric(String isoMetric) {
        this.isoMetric = isoMetric;
    }

    public String getSh() {
        return sh;
    }

    public void setSh(String sh) {
        this.sh = sh;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getToBeShippedNr() {
        return toBeShippedNr;
    }

    public void setToBeShippedNr(String toBeShippedNr) {
        this.toBeShippedNr = toBeShippedNr;
    }

    public String getCompletePieceNo() {
        return completePieceNo;
    }

    public void setCompletePieceNo(String completePieceNo) {
        this.completePieceNo = completePieceNo;
    }

    public String getTagNotes() {
        return tagNotes;
    }

    public void setTagNotes(String tagNotes) {
        this.tagNotes = tagNotes;
    }
}
