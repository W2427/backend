package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StructureST02InspectionReportItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = 6505076053610857598L;

    @Schema(name = "qc模板")
    private String qcfTemplate;

    @Schema(name = "qc模板名")
    private String qcfName;

    @Schema(name = "报告号")
    private String reportNo;

    @Schema(name = "报告页数")
    private String pages;

    @Schema(name = "序列数")
    private String no;

    public String getQcfTemplate() {
        return qcfTemplate;
    }

    public void setQcfTemplate(String qcfTemplate) {
        this.qcfTemplate = qcfTemplate;
    }

    public String getQcfName() {
        return qcfName;
    }

    public void setQcfName(String qcfName) {
        this.qcfName = qcfName;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getPages() {
        return pages;
    }

    public void setPages(String pages) {
        this.pages = pages;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
}
