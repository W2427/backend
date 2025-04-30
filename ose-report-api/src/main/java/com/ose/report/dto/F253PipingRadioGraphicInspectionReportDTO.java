package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class F253PipingRadioGraphicInspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = 8380748080863952663L;

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

    private String focalSpotSize;

    private String testTiming;

    private String voltage;

    private String current;

    private String testRatio;

    private String radioGraphicTechnique;

    private String sfd;

    private String exposureTime;

    private String developmentMethod;

    private String equipModel;

    private String sn;

    private String iQIType;

    private String filmType;

    private String screenType;

    private String iQILocation;

    private String developingTemp;

    private String time;

    private String screenThick;

    private String requirementIqiWireNo;

    private List<F253PipingRadioGraphicInspectionReportItemDTO> items;

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

    public String getFocalSpotSize() {
        return focalSpotSize;
    }

    public void setFocalSpotSize(String focalSpotSize) {
        this.focalSpotSize = focalSpotSize;
    }

    public String getTestTiming() {
        return testTiming;
    }

    public void setTestTiming(String testTiming) {
        this.testTiming = testTiming;
    }

    public String getVoltage() {
        return voltage;
    }

    public void setVoltage(String voltage) {
        this.voltage = voltage;
    }

    public String getCurrent() {
        return current;
    }

    public void setCurrent(String current) {
        this.current = current;
    }

    public String getTestRatio() {
        return testRatio;
    }

    public void setTestRatio(String testRatio) {
        this.testRatio = testRatio;
    }

    public String getRadioGraphicTechnique() {
        return radioGraphicTechnique;
    }

    public void setRadioGraphicTechnique(String radioGraphicTechnique) {
        this.radioGraphicTechnique = radioGraphicTechnique;
    }

    public String getSfd() {
        return sfd;
    }

    public void setSfd(String sfd) {
        this.sfd = sfd;
    }

    public String getExposureTime() {
        return exposureTime;
    }

    public void setExposureTime(String exposureTime) {
        this.exposureTime = exposureTime;
    }

    public String getDevelopmentMethod() {
        return developmentMethod;
    }

    public void setDevelopmentMethod(String developmentMethod) {
        this.developmentMethod = developmentMethod;
    }

    public String getEquipModel() {
        return equipModel;
    }

    public void setEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getiQIType() {
        return iQIType;
    }

    public void setiQIType(String iQIType) {
        this.iQIType = iQIType;
    }

    public String getFilmType() {
        return filmType;
    }

    public void setFilmType(String filmType) {
        this.filmType = filmType;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getiQILocation() {
        return iQILocation;
    }

    public void setiQILocation(String iQILocation) {
        this.iQILocation = iQILocation;
    }

    public String getDevelopingTemp() {
        return developingTemp;
    }

    public void setDevelopingTemp(String developingTemp) {
        this.developingTemp = developingTemp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getScreenThick() {
        return screenThick;
    }

    public void setScreenThick(String screenThick) {
        this.screenThick = screenThick;
    }

    public String getRequirementIqiWireNo() {
        return requirementIqiWireNo;
    }

    public void setRequirementIqiWireNo(String requirementIqiWireNo) {
        this.requirementIqiWireNo = requirementIqiWireNo;
    }

    @Override
    public List<F253PipingRadioGraphicInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<F253PipingRadioGraphicInspectionReportItemDTO> items) {
        this.items = items;
    }
}
