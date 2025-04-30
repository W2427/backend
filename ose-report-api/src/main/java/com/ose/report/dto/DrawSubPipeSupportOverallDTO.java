package com.ose.report.dto;

import java.util.List;

public class DrawSubPipeSupportOverallDTO extends BaseListReportDTO {
    private static final long serialVersionUID = 19506801711784147L;
    private String projectName;
    private String title;
    private String drawNumber;
    private String reportNo;
    private List<DrawSubPipeSupportOverallListDto> items;

    public DrawSubPipeSupportOverallDTO() {
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(String drawNumber) {
        this.drawNumber = drawNumber;
    }

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public List<DrawSubPipeSupportOverallListDto> getItems() {
        return items;
    }

    public void setItems(List<DrawSubPipeSupportOverallListDto> items) {
        this.items = items;
    }

}
