package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ose.dto.BaseDTO;
import com.ose.vo.unit.PressureUnit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


/**
 * 试压包实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PressureTestPackageEntryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 8053904013730418362L;

    @Schema(description = "版本号")
    private String revision;

    @Schema(description = "试压等级")
    private String pressureTestClass;

    @Schema(description = "试压压力")
    private Double testPressure;

    @Schema(description = "试压压力单位")
    @Enumerated(EnumType.STRING)
    private PressureUnit testPressureUnit;

    @Schema(description = "试压介质")
    private String testMedium;

    @Schema(description = "试压包文件号")
    private String ptpDrawingNo;

    @Schema(description = "最大操作压力")
    private Double maxOperatingPressure;

    @Schema(description = "最大操作压力单位")
    @Enumerated(EnumType.STRING)
    private PressureUnit maxOperatingPressureUnit;

    @Schema(description = "最大设计压力")
    private Double maxDesignPressure;

    @Schema(description = "最大设计压力单位")
    @Enumerated(EnumType.STRING)
    private PressureUnit maxDesignPressureUnit;

    @Schema(description = "是否吹扫")
    private Boolean airBlow;

    @Schema(description = "备注")
    private String remarks;

    @JsonCreator
    public PressureTestPackageEntryUpdateDTO() {
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getPressureTestClass() {
        return pressureTestClass;
    }

    public void setPressureTestClass(String pressureTestClass) {
        this.pressureTestClass = pressureTestClass;
    }

    public Double getTestPressure() {
        return testPressure;
    }

    public void setTestPressure(Double testPressure) {
        this.testPressure = testPressure;
    }

    public PressureUnit getTestPressureUnit() {
        return testPressureUnit;
    }

    public void setTestPressureUnit(PressureUnit testPressureUnit) {
        this.testPressureUnit = testPressureUnit;
    }

    public String getTestMedium() {
        return testMedium;
    }

    public void setTestMedium(String testMedium) {
        this.testMedium = testMedium;
    }

    public String getPtpDrawingNo() {
        return ptpDrawingNo;
    }

    public void setPtpDrawingNo(String ptpDrawingNo) {
        this.ptpDrawingNo = ptpDrawingNo;
    }

    public Double getMaxOperatingPressure() {
        return maxOperatingPressure;
    }

    public void setMaxOperatingPressure(Double maxOperatingPressure) {
        this.maxOperatingPressure = maxOperatingPressure;
    }

    public PressureUnit getMaxOperatingPressureUnit() {
        return maxOperatingPressureUnit;
    }

    public void setMaxOperatingPressureUnit(PressureUnit maxOperatingPressureUnit) {
        this.maxOperatingPressureUnit = maxOperatingPressureUnit;
    }

    public Double getMaxDesignPressure() {
        return maxDesignPressure;
    }

    public void setMaxDesignPressure(Double maxDesignPressure) {
        this.maxDesignPressure = maxDesignPressure;
    }

    public PressureUnit getMaxDesignPressureUnit() {
        return maxDesignPressureUnit;
    }

    public void setMaxDesignPressureUnit(PressureUnit maxDesignPressureUnit) {
        this.maxDesignPressureUnit = maxDesignPressureUnit;
    }

    public Boolean getAirBlow() {
        return airBlow;
    }

    public void setAirBlow(Boolean airBlow) {
        this.airBlow = airBlow;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
