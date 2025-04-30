package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class F253PipingPenetrantInspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = -2926355728375823079L;

    private String cnAsRegistrationNo;

    private String seriesNo;

    @Schema(description = "报告号")
    private String reportNo;

    private String projectName;

    private String structureName;

    private Date dateOfTest;

    private String drawingNo;

    private String material;

    private String weldingProcess;

    private String technicalStandard;

    private String acceptanceStandard;

    private String inspectionCategory;

    private String procedure;

    private String sensitivityIndicator;

    private String surfaceCondition;

    private String penetrateModel;

    private String penetrateLotNo;

    private String temperature;

    private String removerModel;

    private String removerLotNo;

    private String testTiming;

    private String developerModel;

    private String developerLotNo;

    private String testRatio;

    private String dwellTime;

    private String developerTime;

    private String Illumination;

    private List<F253PipingPenetrantInspectionReportItemDTO> items;

    public String getCnAsRegistrationNo() {
        return cnAsRegistrationNo;
    }

    public void setCnAsRegistrationNo(String cnAsRegistrationNo) {
        this.cnAsRegistrationNo = cnAsRegistrationNo;
    }

    public String getSeriesNo() {
        return seriesNo;
    }

    public void setSeriesNo(String seriesNo) {
        this.seriesNo = seriesNo;
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
    public String getStructureName() {
        return structureName;
    }

    @Override
    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public Date getDateOfTest() {
        return dateOfTest;
    }

    public void setDateOfTest(Date dateOfTest) {
        this.dateOfTest = dateOfTest;
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
    public String getMaterial() {
        return material;
    }

    @Override
    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String getWeldingProcess() {
        return weldingProcess;
    }

    @Override
    public void setWeldingProcess(String weldingProcess) {
        this.weldingProcess = weldingProcess;
    }

    public String getTechnicalStandard() {
        return technicalStandard;
    }

    public void setTechnicalStandard(String technicalStandard) {
        this.technicalStandard = technicalStandard;
    }

    public String getAcceptanceStandard() {
        return acceptanceStandard;
    }

    public void setAcceptanceStandard(String acceptanceStandard) {
        this.acceptanceStandard = acceptanceStandard;
    }

    public String getInspectionCategory() {
        return inspectionCategory;
    }

    public void setInspectionCategory(String inspectionCategory) {
        this.inspectionCategory = inspectionCategory;
    }

    public String getProcedure() {
        return procedure;
    }

    public void setProcedure(String procedure) {
        this.procedure = procedure;
    }

    public String getSensitivityIndicator() {
        return sensitivityIndicator;
    }

    public void setSensitivityIndicator(String sensitivityIndicator) {
        this.sensitivityIndicator = sensitivityIndicator;
    }

    public String getSurfaceCondition() {
        return surfaceCondition;
    }

    public void setSurfaceCondition(String surfaceCondition) {
        this.surfaceCondition = surfaceCondition;
    }

    public String getPenetrateModel() {
        return penetrateModel;
    }

    public void setPenetrateModel(String penetrateModel) {
        this.penetrateModel = penetrateModel;
    }

    public String getPenetrateLotNo() {
        return penetrateLotNo;
    }

    public void setPenetrateLotNo(String penetrateLotNo) {
        this.penetrateLotNo = penetrateLotNo;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getRemoverModel() {
        return removerModel;
    }

    public void setRemoverModel(String removerModel) {
        this.removerModel = removerModel;
    }

    public String getRemoverLotNo() {
        return removerLotNo;
    }

    public void setRemoverLotNo(String removerLotNo) {
        this.removerLotNo = removerLotNo;
    }

    public String getTestTiming() {
        return testTiming;
    }

    public void setTestTiming(String testTiming) {
        this.testTiming = testTiming;
    }

    public String getDeveloperModel() {
        return developerModel;
    }

    public void setDeveloperModel(String developerModel) {
        this.developerModel = developerModel;
    }

    public String getDeveloperLotNo() {
        return developerLotNo;
    }

    public void setDeveloperLotNo(String developerLotNo) {
        this.developerLotNo = developerLotNo;
    }

    public String getTestRatio() {
        return testRatio;
    }

    public void setTestRatio(String testRatio) {
        this.testRatio = testRatio;
    }

    public String getDwellTime() {
        return dwellTime;
    }

    public void setDwellTime(String dwellTime) {
        this.dwellTime = dwellTime;
    }

    public String getDeveloperTime() {
        return developerTime;
    }

    public void setDeveloperTime(String developerTime) {
        this.developerTime = developerTime;
    }

    public String getIllumination() {
        return Illumination;
    }

    public void setIllumination(String illumination) {
        Illumination = illumination;
    }

    @Override
    public List<F253PipingPenetrantInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<F253PipingPenetrantInspectionReportItemDTO> items) {
        this.items = items;
    }
}
