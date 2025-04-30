package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class StructurePenetrantInspectionReportDTO extends BaseListReportDTO {

    private static final long serialVersionUID = -5216650203231995498L;

    private String projectName;

    private String seriesNo;

    private Date date;

    @Schema(description = "报告号")
    private String reportNo;

    private String drawingNo;

    private String tagNo;

    private String acceptanceStandard;

    private String testingDate;

    private String materialType;

    private String weldingProcess;

    private List<StructurePenetrantInspectionReportItemDTO> items;

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
    }

    @Override
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
    public List<StructurePenetrantInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<StructurePenetrantInspectionReportItemDTO> items) {
        this.items = items;
    }

    @Override
    public String getDrawingNo() {
        return drawingNo;
    }

    @Override
    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    @Override
    public String getTagNo() {
        return tagNo;
    }

    @Override
    public void setTagNo(String tagNo) {
        this.tagNo = tagNo;
    }

    public String getAcceptanceStandard() {
        return acceptanceStandard;
    }

    public void setAcceptanceStandard(String acceptanceStandard) {
        this.acceptanceStandard = acceptanceStandard;
    }

    public String getTestingDate() {
        return testingDate;
    }

    public void setTestingDate(String testingDate) {
        this.testingDate = testingDate;
    }

    @Override
    public String getMaterialType() {
        return materialType;
    }

    @Override
    public void setMaterialType(String materialType) {
        this.materialType = materialType;
    }

    @Override
    public String getWeldingProcess() {
        return weldingProcess;
    }

    @Override
    public void setWeldingProcess(String weldingProcess) {
        this.weldingProcess = weldingProcess;
    }
}
