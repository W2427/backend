package com.ose.report.dto;

import java.util.Date;
import java.util.List;

public class InspectionReportApplicationFormPostDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -7409326073513273783L;

    private String projectName;

    private String partName;

    private String submitTo;

    private Date date;

    private List<InspectionReportApplicationFormItemDTO> items;

    public InspectionReportApplicationFormPostDTO() {
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
    public List<InspectionReportApplicationFormItemDTO> getItems() {
        return items;
    }

    public void setItems(List<InspectionReportApplicationFormItemDTO> items) {
        this.items = items;
    }

    public String getSubmitTo() {
        return submitTo;
    }

    public void setSubmitTo(String submitTo) {
        this.submitTo = submitTo;
    }

}
