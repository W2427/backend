package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class PostWeldHeatTreatmentRecordDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -909975085964963738L;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "日期")
    private Date date;
    private List<PostWeldHeatTreatmentItemDTO> items;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Date getDate() {

        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public List<PostWeldHeatTreatmentItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PostWeldHeatTreatmentItemDTO> items) {
        this.items = items;
    }
}
