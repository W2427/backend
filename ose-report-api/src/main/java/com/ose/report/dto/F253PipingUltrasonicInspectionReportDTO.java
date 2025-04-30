package com.ose.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Date;
import java.util.List;

public class F253PipingUltrasonicInspectionReportDTO extends BaseListReportDTO{

    private static final long serialVersionUID = -1149231669259178668L;

    private String cnAsRegistrationNo;

    private String seriesNo;

    @Schema(description = "报告号")
    private String reportNo;

    private String projectName;

    private String structureName;

    private Date dateOfTest;

    private String drawingNo;

    private String  material;

    private String weldingProcess;

    private String technicalStandard;

    private String acceptanceStandard;

    private String inspectionCategory;

    private String procedure;

    private String couplant;

    private String testTiming;

    private String equipModel;

    private String sn;

    private String referenceBlock;

    private String testRatio;

    private List<F253PipingUltrasonicInspectionReportItemDTO> items;

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

    public String getCouplant() {
        return couplant;
    }

    public void setCouplant(String couplant) {
        this.couplant = couplant;
    }

    public String getTestTiming() {
        return testTiming;
    }

    public void setTestTiming(String testTiming) {
        this.testTiming = testTiming;
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

    public String getReferenceBlock() {
        return referenceBlock;
    }

    public void setReferenceBlock(String referenceBlock) {
        this.referenceBlock = referenceBlock;
    }

    public String getTestRatio() {
        return testRatio;
    }

    public void setTestRatio(String testRatio) {
        this.testRatio = testRatio;
    }

    @Override
    public List<F253PipingUltrasonicInspectionReportItemDTO> getItems() {
        return items;
    }

    public void setItems(List<F253PipingUltrasonicInspectionReportItemDTO> items) {
        this.items = items;
    }
}
