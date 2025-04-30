package com.ose.tasks.entity.wps;
import com.ose.entity.BaseVersionedBizEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "welder_certificate")
public class WelderCertificate extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -3101076135278003891L;

    @Column
    private Long welderId;

    @Column
    private String welderType;

    @Column
    private String wpsNo;

    @Column
    private String joint;

    @Column
    private Date testDate;

    @Column
    private String actualProcess;

    @Column
    private String actualElectrode;

    @Column
    private String actualPolarity;

    @Column
    private String actualPosition;

    @Column
    private Boolean actualBacking;

    @Column(length = 1600)
    private String actualBaseMetal;

    @Column
    private String actualBaseMetalAlias;

    @Column
    private Boolean thicknessPlateChecked;

    @Column
    private Boolean thicknessPipeChecked;

    @Column
    private Boolean thicknessGrooveChecked;

    @Column
    private Boolean thicknessFilletChecked;

    @Column
    private Double actualThickness;

    @Column
    private Boolean diaFilletChecked;

    @Column
    private Boolean diaGrooveChecked;

    @Column
    private Double actualDia;

    @Column
    private String actualFillerMetalSpecNo;

    @Column
    private String actualFillerMetalClass;

    @Column
    private String actualFillerMetalFNo;

    @Column
    private String actualGasOrFluxType;

    @Column
    private String planProcess;

    @Column
    private String planElectrode;

    @Column
    private String planPolarity;

    @Column
    private String planPosition;

    @Column
    private Boolean planBacking;

    @Column
    private String planBaseMetal;

    @Column
    private String planThicknessMin;

    @Column
    private Boolean planThicknessMinIncluded;

    @Column
    private String planThicknessMax;

    @Column
    private Boolean planThicknessMaxIncluded;

    @Column
    private String planThickness;

    @Column
    private String planDia;

    @Column
    private String planDiaMin;

    @Column
    private String planDiaMax;

    @Column
    private Boolean planDiaMinIncluded;

    @Column
    private Boolean planDiaMaxIncluded;

    @Column
    private String planFillerMetalSpecNo;

    @Column
    private String planFillerMetalClass;

    @Column
    private String planFillerMetalFNo;

    @Column
    private String planGasOrFluxType;

    @Column
    private Boolean isVisualAcceptable;

    @Column
    private String radioReportNo;

    @Column
    private String radioReportResult;

    @Column
    private String appearance;

    @Column
    private String filletSize;

    @Column
    private String fractureTest;

    @Column
    private String macroetch;

    @Column
    private String inspector;

    @Column
    private String filletTestNo;

    @Column
    private String organization;

    @Column
    private Date filletTestDate;

    @Column
    private String prepareBy;

    @Column
    private Date prepareDate;

    @Column
    private String reviewBy;

    @Column
    private Date reviewDate;

    @Column
    private String approvedBy;

    @Column
    private Date approvedDate;

    @Column
    private Date expDate;

    @Column
    private Boolean certIssued;

    @Column
    private Boolean stickIssued;

    @Column
    private String standard;

    @Column
    private String baseMetalGroup;

    @Column
    private String memo;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getBaseMetalGroup() {
        return baseMetalGroup;
    }

    public void setBaseMetalGroup(String baseMetalGroup) {
        this.baseMetalGroup = baseMetalGroup;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public Long getWelderId() {
        return welderId;
    }

    public void setWelderId(Long welderId) {
        this.welderId = welderId;
    }

    public String getWelderType() {
        return welderType;
    }

    public void setWelderType(String welderType) {
        this.welderType = welderType;
    }

    public String getWpsNo() {
        return wpsNo;
    }

    public void setWpsNo(String wpsNo) {
        this.wpsNo = wpsNo;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public String getActualProcess() {
        return actualProcess;
    }

    public void setActualProcess(String actualProcess) {
        this.actualProcess = actualProcess;
    }

    public String getActualElectrode() {
        return actualElectrode;
    }

    public void setActualElectrode(String actualElectrode) {
        this.actualElectrode = actualElectrode;
    }

    public String getActualPolarity() {
        return actualPolarity;
    }

    public void setActualPolarity(String actualPolarity) {
        this.actualPolarity = actualPolarity;
    }

    public String getActualPosition() {
        return actualPosition;
    }

    public void setActualPosition(String actualPosition) {
        this.actualPosition = actualPosition;
    }

    public Boolean getActualBacking() {
        return actualBacking;
    }

    public void setActualBacking(Boolean actualBacking) {
        this.actualBacking = actualBacking;
    }

    public String getActualBaseMetal() {
        return actualBaseMetal;
    }

    public void setActualBaseMetal(String actualBaseMetal) {
        this.actualBaseMetal = actualBaseMetal;
    }

    public String getActualBaseMetalAlias() {
        return actualBaseMetalAlias;
    }

    public void setActualBaseMetalAlias(String actualBaseMetalAlias) {
        this.actualBaseMetalAlias = actualBaseMetalAlias;
    }

    public Boolean getThicknessPlateChecked() {
        return thicknessPlateChecked;
    }

    public void setThicknessPlateChecked(Boolean thicknessPlateChecked) {
        this.thicknessPlateChecked = thicknessPlateChecked;
    }

    public Boolean getThicknessPipeChecked() {
        return thicknessPipeChecked;
    }

    public void setThicknessPipeChecked(Boolean thicknessPipeChecked) {
        this.thicknessPipeChecked = thicknessPipeChecked;
    }

    public Boolean getThicknessGrooveChecked() {
        return thicknessGrooveChecked;
    }

    public void setThicknessGrooveChecked(Boolean thicknessGrooveChecked) {
        this.thicknessGrooveChecked = thicknessGrooveChecked;
    }

    public Boolean getThicknessFilletChecked() {
        return thicknessFilletChecked;
    }

    public void setThicknessFilletChecked(Boolean thicknessFilletChecked) {
        this.thicknessFilletChecked = thicknessFilletChecked;
    }

    public Double getActualThickness() {
        return actualThickness;
    }

    public void setActualThickness(Double actualThickness) {
        this.actualThickness = actualThickness;
    }

    public void setDiaFilletChecked(Boolean diaFilletChecked) {
        this.diaFilletChecked = diaFilletChecked;
    }

    public void setDiaGrooveChecked(Boolean diaGrooveChecked) {
        this.diaGrooveChecked = diaGrooveChecked;
    }

    public Double getActualDia() {
        return actualDia;
    }

    public void setActualDia(Double actualDia) {
        this.actualDia = actualDia;
    }

    public String getPlanThickness() {
        return planThickness;
    }

    public void setPlanThickness(String planThickness) {
        this.planThickness = planThickness;
    }

    public String getPlanDia() {
        return planDia;
    }

    public void setPlanDia(String planDia) {
        this.planDia = planDia;
    }

    public String getActualFillerMetalSpecNo() {
        return actualFillerMetalSpecNo;
    }

    public void setActualFillerMetalSpecNo(String actualFillerMetalSpecNo) {
        this.actualFillerMetalSpecNo = actualFillerMetalSpecNo;
    }

    public String getActualFillerMetalClass() {
        return actualFillerMetalClass;
    }

    public void setActualFillerMetalClass(String actualFillerMetalClass) {
        this.actualFillerMetalClass = actualFillerMetalClass;
    }

    public String getActualFillerMetalFNo() {
        return actualFillerMetalFNo;
    }

    public void setActualFillerMetalFNo(String actualFillerMetalFNo) {
        this.actualFillerMetalFNo = actualFillerMetalFNo;
    }

    public String getActualGasOrFluxType() {
        return actualGasOrFluxType;
    }

    public void setActualGasOrFluxType(String actualGasOrFluxType) {
        this.actualGasOrFluxType = actualGasOrFluxType;
    }

    public String getPlanGasOrFluxType() {
        return planGasOrFluxType;
    }

    public void setPlanGasOrFluxType(String planGasOrFluxType) {
        this.planGasOrFluxType = planGasOrFluxType;
    }

    public String getPlanProcess() {
        return planProcess;
    }

    public void setPlanProcess(String planProcess) {
        this.planProcess = planProcess;
    }

    public String getPlanElectrode() {
        return planElectrode;
    }

    public void setPlanElectrode(String planElectrode) {
        this.planElectrode = planElectrode;
    }

    public String getPlanPolarity() {
        return planPolarity;
    }

    public void setPlanPolarity(String planPolarity) {
        this.planPolarity = planPolarity;
    }

    public String getPlanPosition() {
        return planPosition;
    }

    public void setPlanPosition(String planPosition) {
        this.planPosition = planPosition;
    }

    public Boolean getPlanBacking() {
        return planBacking;
    }

    public void setPlanBacking(Boolean planBacking) {
        this.planBacking = planBacking;
    }

    public String getPlanBaseMetal() {
        return planBaseMetal;
    }

    public void setPlanBaseMetal(String planBaseMetal) {
        this.planBaseMetal = planBaseMetal;
    }

    public String getPlanThicknessMin() {
        return planThicknessMin;
    }

    public void setPlanThicknessMin(String planThicknessMin) {
        this.planThicknessMin = planThicknessMin;
    }

    public Boolean getPlanThicknessMinIncluded() {
        return planThicknessMinIncluded;
    }

    public void setPlanThicknessMinIncluded(Boolean planThicknessMinIncluded) {
        this.planThicknessMinIncluded = planThicknessMinIncluded;
    }

    public String getPlanThicknessMax() {
        return planThicknessMax;
    }

    public Boolean getDiaFilletChecked() {
        return diaFilletChecked;
    }

    public Boolean getDiaGrooveChecked() {
        return diaGrooveChecked;
    }

    public void setPlanThicknessMax(String planThicknessMax) {
        this.planThicknessMax = planThicknessMax;
    }

    public Boolean getPlanThicknessMaxIncluded() {
        return planThicknessMaxIncluded;
    }

    public void setPlanThicknessMaxIncluded(Boolean planThicknessMaxIncluded) {
        this.planThicknessMaxIncluded = planThicknessMaxIncluded;
    }

    public String getPlanDiaMin() {
        return planDiaMin;
    }

    public void setPlanDiaMin(String planDiaMin) {
        this.planDiaMin = planDiaMin;
    }

    public String getPlanDiaMax() {
        return planDiaMax;
    }

    public void setPlanDiaMax(String planDiaMax) {
        this.planDiaMax = planDiaMax;
    }

    public Boolean getPlanDiaMinIncluded() {
        return planDiaMinIncluded;
    }

    public void setPlanDiaMinIncluded(Boolean planDiaMinIncluded) {
        this.planDiaMinIncluded = planDiaMinIncluded;
    }

    public Boolean getPlanDiaMaxIncluded() {
        return planDiaMaxIncluded;
    }

    public void setPlanDiaMaxIncluded(Boolean planDiaMaxIncluded) {
        this.planDiaMaxIncluded = planDiaMaxIncluded;
    }

    public String getPlanFillerMetalSpecNo() {
        return planFillerMetalSpecNo;
    }

    public void setPlanFillerMetalSpecNo(String planFillerMetalSpecNo) {
        this.planFillerMetalSpecNo = planFillerMetalSpecNo;
    }

    public String getPlanFillerMetalClass() {
        return planFillerMetalClass;
    }

    public void setPlanFillerMetalClass(String planFillerMetalClass) {
        this.planFillerMetalClass = planFillerMetalClass;
    }

    public String getPlanFillerMetalFNo() {
        return planFillerMetalFNo;
    }

    public void setPlanFillerMetalFNo(String planFillerMetalFNo) {
        this.planFillerMetalFNo = planFillerMetalFNo;
    }

    public Boolean getVisualAcceptable() {
        return isVisualAcceptable;
    }

    public void setVisualAcceptable(Boolean visualAcceptable) {
        isVisualAcceptable = visualAcceptable;
    }

    public String getAppearance() {
        return appearance;
    }

    public void setAppearance(String appearance) {
        this.appearance = appearance;
    }

    public String getFilletSize() {
        return filletSize;
    }

    public void setFilletSize(String filletSize) {
        this.filletSize = filletSize;
    }

    public String getFractureTest() {
        return fractureTest;
    }

    public void setFractureTest(String fractureTest) {
        this.fractureTest = fractureTest;
    }

    public String getMacroetch() {
        return macroetch;
    }

    public void setMacroetch(String macroetch) {
        this.macroetch = macroetch;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getFilletTestNo() {
        return filletTestNo;
    }

    public void setFilletTestNo(String filletTestNo) {
        this.filletTestNo = filletTestNo;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Date getFilletTestDate() {
        return filletTestDate;
    }

    public void setFilletTestDate(Date filletTestDate) {
        this.filletTestDate = filletTestDate;
    }

    public String getPrepareBy() {
        return prepareBy;
    }

    public void setPrepareBy(String prepareBy) {
        this.prepareBy = prepareBy;
    }

    public String getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(String reviewBy) {
        this.reviewBy = reviewBy;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }


    public Date getExpDate() {
        return expDate;
    }

    public void setExpDate(Date expDate) {
        this.expDate = expDate;
    }

    public Boolean getCertIssued() {
        return certIssued;
    }

    public void setCertIssued(Boolean certIssued) {
        this.certIssued = certIssued;
    }

    public Boolean getStickIssued() {
        return stickIssued;
    }

    public void setStickIssued(Boolean stickIssued) {
        this.stickIssued = stickIssued;
    }

    public String getRadioReportNo() {
        return radioReportNo;
    }

    public void setRadioReportNo(String radioReportNo) {
        this.radioReportNo = radioReportNo;
    }

    public String getRadioReportResult() {
        return radioReportResult;
    }

    public void setRadioReportResult(String radioReportResult) {
        this.radioReportResult = radioReportResult;
    }

    public Date getPrepareDate() {
        return prepareDate;
    }

    public void setPrepareDate(Date prepareDate) {
        this.prepareDate = prepareDate;
    }

    public Date getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(Date reviewDate) {
        this.reviewDate = reviewDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getJoint() {
        return joint;
    }

    public void setJoint(String joint) {
        this.joint = joint;
    }
}
