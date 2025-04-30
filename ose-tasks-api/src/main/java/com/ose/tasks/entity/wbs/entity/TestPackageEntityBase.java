package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.tasks.dto.numeric.PressureDTO;
import com.ose.tasks.entity.Project;
import com.ose.vo.unit.PressureUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;

/**
 * 试压包实体数据实体基类。
 */
@Entity
@Table(name = "entity_test_package",
    indexes = {
        @Index(columnList = "no,projectId,deleted")
    })
public class TestPackageEntityBase extends WBSEntityBase implements WorkflowProcessVariable {

    private static final long serialVersionUID = -7087823127044774929L;

    @Schema(description = "版本号")
    @Column(nullable = false)
    private String revision;

    @Schema(description = "试压等级")
    @Column(nullable = false)
    private String pressureTestClass;

    @Schema(description = "试压压力")
    @Column(nullable = false)
    private Double testPressure;

    @Schema(description = "试压压力表示值")
    @Column(nullable = false)
    private String testPressureText;


    @Schema(description = "子类型")
    @Column(nullable = false)
    private String entitySubType;

    @Schema(description = "试压压力单位")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PressureUnit testPressureUnit;

    @Schema(description = "试压介质")
    @Column(nullable = false)
    private String testMedium;

    @Schema(description = "试压包文件号")
    @Column(nullable = false)
    private String ptpDrawingNo;

    @Schema(description = "最大操作压力")
    @Column
    private Double maxOperatingPressure;

    @Schema(description = "最大操作压力表示值")
    @Column(nullable = false)
    private String maxOperatingPressureText;

    @Schema(description = "最大操作压力单位")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PressureUnit maxOperatingPressureUnit;

    @Schema(description = "最大设计压力")
    @Column
    private Double maxDesignPressure;

    @Schema(description = "最大设计压力表示值")
    @Column(nullable = false)
    private String maxDesignPressureText;

    @Schema(description = "最大设计压力单位")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PressureUnit maxDesignPressureUnit;

    @Schema(description = "是否吹扫")
    @Column(columnDefinition = "bit default 0")
    private Boolean airBlow;

    @Schema(description = "备注")
    @Column
    private String remarks;

    @Schema(description = "批量删除标记")
    @Column
    private String remarks2;

    @Schema(description = "ITR_PACKAGE_FILE_PATH")
    @Column
    private String itrPkFilePath;

    @Schema(description = "ITR_PACKAGE_FILE_ID")
    @Column
    private Long itrPkFileId;


    @Schema(description = "ISO_FILE_ID")
    @Column
    private Long isoFileId;

    @Schema(description = "ISO_FILE_PATH")
    @Column
    private String isoFilePath;


    @Schema(description = "ISO_LIST_FILE_ID")
    @Column
    private Long isoListFileId;

    @Schema(description = "ISO_LIST_FILE_PATH")
    @Column
    private String isoListFilePath;

    @JsonCreator
    public TestPackageEntityBase() {
        this(null);
        this.setRevision("0");
        this.setMaxDesignPressureText("0bar");
        this.setMaxOperatingPressureText("0bar");
        this.setMaxDesignPressureUnit("bar");
        this.setMaxOperatingPressureUnit("bar");
        this.setPressureTestClass("a");
        this.setPtpDrawingNo("");
        this.setTestMedium("");
        this.setTestPressure("0");
        this.setTestPressureUnit("bar");
    }

    public TestPackageEntityBase(Project project) {
        super(project);
        setEntityType("TEST_PACKAGE");
    }


    @Override
    public String getName() {
        return "TestPackageEntityBase";
    }

    @Override
    public String getEntitySubType() {
        return null;
    }

    @Override
    public String getEntityBusinessType() {
        return null;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getTestPressureText() {
        return testPressureText;
    }

    public void setTestPressureText(String testPressureText) {
        this.testPressureText = testPressureText;
    }

    @JsonGetter
    public PressureUnit getTestPressureUnit() {
        return testPressureUnit;
    }

    @JsonSetter
    public void setTestPressureUnit(PressureUnit testPressureUnit) {
        this.testPressureUnit = testPressureUnit;
    }

    public void setTestPressureUnit(String testPressureUnit) {
        this.testPressureUnit = PressureUnit.getByName(testPressureUnit);
    }

    public Double getTestPressure() {
        return testPressure;
    }

    @JsonSetter
    public void setTestPressure(Double testPressure) {
        this.testPressure = testPressure;
    }

    /**
     * 设定测试压力。
     *
     * @param testPressure 测试压力
     */
    public void setTestPressure(String testPressure) {

        PressureDTO pressureDTO = new PressureDTO(
            this.testPressureUnit,
            testPressure
        );

        this.testPressureText = testPressure;
        this.testPressure = pressureDTO.getValue();
        this.testPressureUnit = pressureDTO.getUnit();
    }

    public String getPressureTestClass() {
        return pressureTestClass;
    }

    public void setPressureTestClass(String pressureTestClass) {
        this.pressureTestClass = pressureTestClass;
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

    public String getMaxOperatingPressureText() {
        return maxOperatingPressureText;
    }

    /**
     * 设定最大操作压力。
     *
     * @param maxOperatingPressure 最大操作压力
     */
    public void setMaxOperatingPressure(String maxOperatingPressure) {

        PressureDTO pressureDTO = new PressureDTO(
            this.maxDesignPressureUnit,
            maxOperatingPressure
        );

        this.maxOperatingPressureText = maxOperatingPressure;
        this.maxOperatingPressure = pressureDTO.getValue();
        this.maxOperatingPressureUnit = pressureDTO.getUnit();
    }

    public void setMaxOperatingPressureText(String maxOperatingPressureText) {
        this.maxOperatingPressureText = maxOperatingPressureText;
    }

    public PressureUnit getMaxOperatingPressureUnit() {
        return maxOperatingPressureUnit;
    }

    public void setMaxOperatingPressureUnit(PressureUnit maxOperatingPressureUnit) {
        this.maxOperatingPressureUnit = maxOperatingPressureUnit;
    }

    public void setMaxOperatingPressureUnit(String maxOperatingPressureUnit) {
        this.maxOperatingPressureUnit = PressureUnit.getByName(maxOperatingPressureUnit);
    }

    public Double getMaxDesignPressure() {
        return maxDesignPressure;
    }

    public void setMaxDesignPressure(Double maxDesignPressure) {
        this.maxDesignPressure = maxDesignPressure;
    }

    /**
     * 设定最大设计压力。
     *
     * @param maxDesignPressure 最大设计压力
     */
    public void setMaxDesignPressure(String maxDesignPressure) {

        PressureDTO pressureDTO = new PressureDTO(
            this.maxDesignPressureUnit,
            maxDesignPressure
        );

        this.maxDesignPressureText = maxDesignPressure;
        this.maxDesignPressure = pressureDTO.getValue();
        this.maxDesignPressureUnit = pressureDTO.getUnit();
    }

    public String getMaxDesignPressureText() {
        return maxDesignPressureText;
    }

    public void setMaxDesignPressureText(String maxDesignPressureText) {
        this.maxDesignPressureText = maxDesignPressureText;
    }

    public PressureUnit getMaxDesignPressureUnit() {
        return maxDesignPressureUnit;
    }

    public void setMaxDesignPressureUnit(PressureUnit maxDesignPressureUnit) {
        this.maxDesignPressureUnit = maxDesignPressureUnit;
    }

    public void setMaxDesignPressureUnit(String maxDesignPressureUnit) {
        this.maxDesignPressureUnit = PressureUnit.getByName(maxDesignPressureUnit);
    }

    public Boolean getAirBlow() {
        return airBlow;
    }

    public void setAirBlow(Boolean airBlow) {
        this.airBlow = airBlow;
    }

    @Override
    public String getVariableName() {
        return "TEST_PACKAGE";
    }

    @Override
    public String getRemarks() {
        return remarks;
    }

    @Override
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getItrPkFilePath() {
        return itrPkFilePath;
    }

    public void setItrPkFilePath(String itrPkFilePath) {
        this.itrPkFilePath = itrPkFilePath;
    }

    public Long getItrPkFileId() {
        return itrPkFileId;
    }

    public void setItrPkFileId(Long itrPkFileId) {
        this.itrPkFileId = itrPkFileId;
    }

    public Long getIsoFileId() {
        return isoFileId;
    }

    public void setIsoFileId(Long isoFileId) {
        this.isoFileId = isoFileId;
    }

    public String getIsoFilePath() {
        return isoFilePath;
    }

    public void setIsoFilePath(String isoFilePath) {
        this.isoFilePath = isoFilePath;
    }

    public Long getIsoListFileId() {
        return isoListFileId;
    }

    public void setIsoListFileId(Long isoListFileId) {
        this.isoListFileId = isoListFileId;
    }

    public String getIsoListFilePath() {
        return isoListFilePath;
    }

    public void setIsoListFilePath(String isoListFilePath) {
        this.isoListFilePath = isoListFilePath;
    }
}
