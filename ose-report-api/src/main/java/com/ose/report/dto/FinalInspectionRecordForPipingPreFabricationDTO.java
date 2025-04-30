package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class FinalInspectionRecordForPipingPreFabricationDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -2901031259378477280L;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "日期")
    private Date date;

    @Schema(description = "区域号")
    private String areaNo;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    private List<FinalInspectionItemDTO> items;

    @Override
    public String getReportNo() {
        return reportNo;
    }

    @Override
    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public List<FinalInspectionItemDTO> getItems() {
        return items;
    }

    public void setItems(List<FinalInspectionItemDTO> items) {
        this.items = items;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }

    public String getAreaNo() {
        return areaNo;
    }

    public void setAreaNo(String areaNo) {
        this.areaNo = areaNo;
    }
}
