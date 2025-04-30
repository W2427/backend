package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Date;

public class PrefabricatePipingReleaseReportBeforeCoatingDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 9005811916919678813L;

    @Schema(description = "放行日期")
    private Date releaseDate;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "系统填写")
    private String memo;

    @Schema(description = "模块号")
    private String moduleNo;

    @Schema(description = "施工方")
    private String subconstruction;

    @Schema(description = "放行单号")
    private String releaseReportNo;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    private List<PrefabricatePipingReleaseReportBeforeCoatingItemDTO> items;

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
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
    public List<PrefabricatePipingReleaseReportBeforeCoatingItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PrefabricatePipingReleaseReportBeforeCoatingItemDTO> items) {
        this.items = items;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getReleaseReportNo() {
        return releaseReportNo;
    }

    public void setReleaseReportNo(String releaseReportNo) {
        this.releaseReportNo = releaseReportNo;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }

    public String getModuleNo() {
        return moduleNo;
    }

    public void setModuleNo(String moduleNo) {
        this.moduleNo = moduleNo;
    }

    public String getSubconstruction() {
        return subconstruction;
    }

    public void setSubconstruction(String subconstruction) {
        this.subconstruction = subconstruction;
    }
}
