package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class InspectionReportPostDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -7409326073513273783L;

    private String projectName;

    private String partName;

    private Date date;

    @Schema(description = "报告号")
    private String reportNo;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    private List<InspectionReportItemDTO> items;

    public InspectionReportPostDTO() {
        super();
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    @Override
    public List<InspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<InspectionReportItemDTO> items) {
        this.items = items;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }
}
