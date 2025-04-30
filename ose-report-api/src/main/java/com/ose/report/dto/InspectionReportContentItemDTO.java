package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;

public class InspectionReportContentItemDTO extends BaseReportListItemDTO {

    private static final long serialVersionUID = -3861518962983773098L;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "报告生成日期")
    private Date reportDate;

    @Schema(description = "页码")
    private String page;

    @Schema(description = "备注")
    private String remark;

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
