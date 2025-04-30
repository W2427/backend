package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class PositiveMaterialIdentificationReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = 350275417723222954L;
    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "报告编号")
    private String reportNo;

    @Schema(description = "设备名称")
    private String instrumentType;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "报检人员1")
    private String inspectionItem1;

    private List<PositiveMaterialIdentificationItemDTO> items;

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

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

    @Override
    public List<PositiveMaterialIdentificationItemDTO> getItems() {
        return items;
    }

    public void setItems(List<PositiveMaterialIdentificationItemDTO> items) {
        this.items = items;
    }

    public String getInspectionItem1() {
        return inspectionItem1;
    }

    public void setInspectionItem1(String inspectionItem1) {
        this.inspectionItem1 = inspectionItem1;
    }
}
