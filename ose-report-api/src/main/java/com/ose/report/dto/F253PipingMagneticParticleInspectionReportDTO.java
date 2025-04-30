package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class F253PipingMagneticParticleInspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = 5470403330736486150L;

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

    private String magnetizationType;

    private String surfaceCondition;

    private String liftingPower;

    private String magnetizationTime;

    private String temperature;

    private String particleModel;

    private String whiteModel;

    private String testTiming;

    private String particleLotNo;

    private String equipType;

    private String sn;

    private String testRatio;

    private String particleType;

    private String sensitivityStrip;

    private String Illumination;

    private List<F253PipingMagneticParticleInspectionReportItemDTO> items;

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

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public Date getDateOfTest() {
        return dateOfTest;
    }

    public void setDateOfTest(Date dateOfTest) {
        this.dateOfTest = dateOfTest;
    }

    public String getDrawingNo() {
        return drawingNo;
    }

    public void setDrawingNo(String drawingNo) {
        this.drawingNo = drawingNo;
    }

    public String getMaterial() {
        return material;
    }

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

    public String getMagnetizationType() {
        return magnetizationType;
    }

    public void setMagnetizationType(String magnetizationType) {
        this.magnetizationType = magnetizationType;
    }

    public String getSurfaceCondition() {
        return surfaceCondition;
    }

    public void setSurfaceCondition(String surfaceCondition) {
        this.surfaceCondition = surfaceCondition;
    }

    public String getLiftingPower() {
        return liftingPower;
    }

    public void setLiftingPower(String liftingPower) {
        this.liftingPower = liftingPower;
    }

    public String getMagnetizationTime() {
        return magnetizationTime;
    }

    public void setMagnetizationTime(String magnetizationTime) {
        this.magnetizationTime = magnetizationTime;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getParticleModel() {
        return particleModel;
    }

    public void setParticleModel(String particleModel) {
        this.particleModel = particleModel;
    }

    public String getWhiteModel() {
        return whiteModel;
    }

    public void setWhiteModel(String whiteModel) {
        this.whiteModel = whiteModel;
    }

    public String getTestTiming() {
        return testTiming;
    }

    public void setTestTiming(String testTiming) {
        this.testTiming = testTiming;
    }

    public String getParticleLotNo() {
        return particleLotNo;
    }

    public void setParticleLotNo(String particleLotNo) {
        this.particleLotNo = particleLotNo;
    }

    public String getEquipType() {
        return equipType;
    }

    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getTestRatio() {
        return testRatio;
    }

    public void setTestRatio(String testRatio) {
        this.testRatio = testRatio;
    }

    public String getParticleType() {
        return particleType;
    }

    public void setParticleType(String particleType) {
        this.particleType = particleType;
    }

    public String getSensitivityStrip() {
        return sensitivityStrip;
    }

    public void setSensitivityStrip(String sensitivityStrip) {
        this.sensitivityStrip = sensitivityStrip;
    }

    public String getIllumination() {
        return Illumination;
    }

    public void setIllumination(String illumination) {
        Illumination = illumination;
    }

    public List<F253PipingMagneticParticleInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<F253PipingMagneticParticleInspectionReportItemDTO> items) {
        this.items = items;
    }
}
