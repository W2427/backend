package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.numeric.NDERatioDTO;
import com.ose.tasks.dto.numeric.PressureDTO;
import com.ose.tasks.dto.numeric.TemperatureDTO;
import com.ose.tasks.vo.PressureExtraInfo;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.util.StringUtils;
import com.ose.vo.EntityStatus;
import com.ose.vo.unit.LengthUnit;
import com.ose.vo.unit.PressureUnit;
import com.ose.vo.unit.TemperatureUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

/**
 * ISO实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ISOEntryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "管线号")
    private String processLineNo;

    @Schema(description = "系统号")
    private String systemNo;

    @Schema(description = "版本号")
    private String revision;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "流体")
    private String fluid;

    @Schema(description = "管道等级")
    private String pipeClass;

    @Schema(description = "设计压力单位")
    @Enumerated(EnumType.STRING)
    private PressureUnit designPressureUnit;

    @Schema(description = "设计压力")
    private Double designPressure;

    @Schema(description = "设计压力附加信息")
    @Enumerated(EnumType.STRING)
    private PressureExtraInfo designPressureExtraInfo;

    @Schema(description = "设计温度单位")
    @Enumerated(EnumType.STRING)
    private TemperatureUnit designTemperatureUnit;

    @Schema(description = "设计温度")
    private Double designTemperature;

    @Schema(description = "操作压力单位")
    @Enumerated(EnumType.STRING)
    private PressureUnit operatePressureUnit;

    @Schema(description = "操作压力")
    private Double operatePressure;

    @Schema(description = "操作温度单位")
    @Enumerated(EnumType.STRING)
    private TemperatureUnit operateTemperatureUnit;

    @Schema(description = "操作温度")
    private Double operateTemperature;

    @Schema(description = "保温类型代码")
    private String insulationCode;

    @Schema(description = "保温厚度表示值")
    private String insulationThicknessText;

    @Schema(description = "保温厚度单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit insulationThicknessUnit;

    @Schema(description = "保温厚度")
    private Float insulationThickness;

    @Schema(description = "试压介质")
    private String pressureTestMedium;

    @Schema(description = "试验压力单位")
    @Enumerated(EnumType.STRING)
    private PressureUnit testPressureUnit;

    @Schema(description = "试验压力")
    private Double testPressure;

    @Schema(description = "油漆类型")
    private String paintingCode;

    @Schema(description = "管道级别")
    private String pipeGrade;

    @Schema(description = "HT 代码")
    private String heatTracingCode;

    @Schema(description = "ASME 类型")
    private String asmeCategory;

    @Schema(description = "P&ID 图纸")
    private String pidDrawing;

    @Schema(description = "无损探伤类型")
    private NDEType nde;

    @Schema(description = "无损探伤抽检百分比")
    private Integer ndeRatio;

    @Schema(description = "是否夹套管")
    private Boolean jacketPipe;

    @Schema(description = "是否执行焊接后热处理")
    private Boolean pwht;

    @Schema(description = "材质分析抽取比例")
    private Integer pmiRatio;

    @Schema(description = "内部清洁")
    private String internalMechanicalCleaning;

    @Schema(description = "批量删除标记")
    @Column
    private String remarks2;

    // 数据实体状态
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    // 是否已被删除
    private boolean deleted = false;

    // 更新版本（手动乐观锁）
    private long version;

    public String getInternalMechanicalCleaning() {
        return internalMechanicalCleaning;
    }

    public void setInternalMechanicalCleaning(String internalMechanicalCleaning) {
        this.internalMechanicalCleaning = internalMechanicalCleaning;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    @JsonCreator
    public ISOEntryUpdateDTO() {
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public LengthUnit getNpsUnit() {
        return npsUnit;
    }

    @JsonSetter
    public void setNpsUnit(LengthUnit npsUnit) {
        this.npsUnit = npsUnit;
    }

    @JsonSetter
    public void setNpsUnit(String npsUnit) {
        this.npsUnit = LengthUnit.getByName(npsUnit);
    }

    public Double getNps() {
        return nps;
    }

    @JsonSetter
    public void setNps(Double nps) {
        this.nps = nps;
    }

    public String getFluid() {
        return fluid;
    }

    public void setFluid(String fluid) {
        this.fluid = fluid;
    }

    public String getPipeClass() {
        return pipeClass;
    }

    public void setPipeClass(String pipeClass) {
        this.pipeClass = pipeClass;
    }


    public PressureUnit getDesignPressureUnit() {
        return designPressureUnit;
    }

    @JsonSetter
    public void setDesignPressureUnit(PressureUnit designPressureUnit) {
        this.designPressureUnit = designPressureUnit;
    }

    @JsonSetter
    public void setDesignPressureUnit(String designPressureUnit) {
        this.designPressureUnit = PressureUnit.getByName(designPressureUnit);
    }

    public Double getDesignPressure() {
        return designPressure;
    }

    @JsonSetter
    public void setDesignPressure(Double designPressure) {
        this.designPressure = designPressure;
    }

    /**
     * 设定设计压力。
     *
     * @param designPressure 设计压力
     */
    public void setDesignPressure(String designPressure) {

        PressureDTO pressureDTO = new PressureDTO(
            this.designPressureUnit,
            designPressure
        );

        this.designPressure = pressureDTO.getValue();
        this.designPressureUnit = pressureDTO.getUnit();
        this.designPressureExtraInfo = pressureDTO.getExtraInfo();
    }

    public TemperatureUnit getDesignTemperatureUnit() {
        return designTemperatureUnit;
    }

    @JsonSetter
    public void setDesignTemperatureUnit(TemperatureUnit designTemperatureUnit) {
        this.designTemperatureUnit = designTemperatureUnit;
    }

    @JsonSetter
    public void setDesignTemperatureUnit(String designTemperatureUnit) {
        this.designTemperatureUnit = TemperatureUnit.getByName(designTemperatureUnit);
    }

    public Double getDesignTemperature() {
        return designTemperature;
    }

    @JsonSetter
    public void setDesignTemperature(Double designTemperature) {
        this.designTemperature = designTemperature;
    }

    /**
     * 设定设计温度。
     *
     * @param designTemperature 设计温度
     */
    public void setDesignTemperature(String designTemperature) {

        TemperatureDTO temperatureDTO = new TemperatureDTO(
            this.designTemperatureUnit,
            designTemperature
        );

        this.designTemperature = temperatureDTO.getValue();
        this.designTemperatureUnit = temperatureDTO.getUnit();
    }

    public PressureUnit getOperatePressureUnit() {
        return operatePressureUnit;
    }

    @JsonSetter
    public void setOperatePressureUnit(PressureUnit operatePressureUnit) {
        this.operatePressureUnit = operatePressureUnit;
    }

    @JsonSetter
    public void setOperatePressureUnit(String operatePressureUnit) {
        this.operatePressureUnit = PressureUnit.getByName(operatePressureUnit);
    }

    public Double getOperatePressure() {
        return operatePressure;
    }

    @JsonSetter
    public void setOperatePressure(Double operatePressure) {
        this.operatePressure = operatePressure;
    }

    /**
     * 设定操作压力。
     *
     * @param operatePressure 操作压力
     */
    public void setOperatePressure(String operatePressure) {

        PressureDTO pressureDTO = new PressureDTO(
            this.operatePressureUnit,
            operatePressure
        );

        this.operatePressure = pressureDTO.getValue();
        this.operatePressureUnit = pressureDTO.getUnit();
    }

    public TemperatureUnit getOperateTemperatureUnit() {
        return operateTemperatureUnit;
    }

    @JsonSetter
    public void setOperateTemperatureUnit(TemperatureUnit operateTemperatureUnit) {
        this.operateTemperatureUnit = operateTemperatureUnit;
    }

    @JsonSetter
    public void setOperateTemperatureUnit(String operateTemperatureUnit) {
        this.operateTemperatureUnit = TemperatureUnit.getByName(operateTemperatureUnit);
    }

    public Double getOperateTemperature() {
        return operateTemperature;
    }

    @JsonSetter
    public void setOperateTemperature(Double operateTemperature) {
        this.operateTemperature = operateTemperature;
    }

    /**
     * 设定操作温度。
     *
     * @param operateTemperature 操作温度
     */
    public void setOperateTemperature(String operateTemperature) {

        TemperatureDTO temperatureDTO = new TemperatureDTO(
            this.operateTemperatureUnit,
            operateTemperature
        );

        this.operateTemperature = temperatureDTO.getValue();
        this.operateTemperatureUnit = temperatureDTO.getUnit();
    }

    public String getInsulationCode() {
        return insulationCode;
    }

    public void setInsulationCode(String insulationCode) {
        this.insulationCode = insulationCode;
    }

    public LengthUnit getInsulationThicknessUnit() {
        return insulationThicknessUnit;
    }

    @JsonSetter
    public void setInsulationThicknessUnit(LengthUnit insulationThicknessUnit) {
        this.insulationThicknessUnit = insulationThicknessUnit;
    }

    @JsonSetter
    public void setInsulationThicknessUnit(String insulationThicknessUnit) {
        this.insulationThicknessUnit = LengthUnit.getByName(insulationThicknessUnit);
    }

    public Float getInsulationThickness() {
        return insulationThickness;
    }

    public void setInsulationThickness(Float insulationThickness) {
        this.insulationThickness = insulationThickness;
    }

    public String getInsulationThicknessText() {
        return insulationThicknessText;
    }

    public void setInsulationThicknessText(String insulationThicknessText) {
        this.insulationThicknessText = insulationThicknessText;
    }

    public String getPressureTestMedium() {
        return pressureTestMedium;
    }

    public void setPressureTestMedium(String pressureTestMedium) {
        this.pressureTestMedium = pressureTestMedium;
    }

    public PressureUnit getTestPressureUnit() {
        return testPressureUnit;
    }

    @JsonSetter
    public void setTestPressureUnit(PressureUnit testPressureUnit) {
        this.testPressureUnit = testPressureUnit;
    }

    @JsonSetter
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

        this.testPressure = pressureDTO.getValue();
        this.testPressureUnit = pressureDTO.getUnit();
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
    }

    public String getPipeGrade() {
        return pipeGrade;
    }

    public void setPipeGrade(String pipeGrade) {
        this.pipeGrade = pipeGrade;
    }

    public String getHeatTracingCode() {
        return heatTracingCode;
    }

    public void setHeatTracingCode(String heatTracingCode) {
        this.heatTracingCode = heatTracingCode;
    }

    public String getAsmeCategory() {
        return asmeCategory;
    }

    public void setAsmeCategory(String asmeCategory) {
        this.asmeCategory = asmeCategory;
    }

    public String getPidDrawing() {
        return pidDrawing;
    }

    public void setPidDrawing(String pidDrawing) {
        this.pidDrawing = pidDrawing;
    }

    public NDEType getNde() {
        return nde;
    }

    @JsonSetter
    public void setNde(NDEType nde) {
        this.nde = nde;
    }

    /**
     * 设定nde值。
     *
     * @param nde nde值
     */
    @JsonSetter
    public void setNde(String nde) {
        NDERatioDTO ratioDTO = new NDERatioDTO(nde);
        this.nde = ratioDTO.getType();
        this.ndeRatio = ratioDTO.getRatio();
    }

    public Integer getNdeRatio() {
        return ndeRatio;
    }

    public void setNdeRatio(Integer ndeRatio) {
        this.ndeRatio = ndeRatio;
    }

    public Boolean getJacketPipe() {
        return jacketPipe;
    }

    public void setJacketPipe(Boolean jacketPipe) {
        this.jacketPipe = jacketPipe;
    }

    public Boolean getPwht() {
        return pwht;
    }

    public void setPwht(Boolean pwht) {
        this.pwht = pwht;
    }

    public Integer getPmiRatio() {
        return pmiRatio;
    }

    public void setPmiRatio(Integer pmiRatio) {
        this.pmiRatio = pmiRatio;
    }

    public PressureExtraInfo getDesignPressureExtraInfo() {
        return designPressureExtraInfo;
    }

    public void setDesignPressureExtraInfo(PressureExtraInfo designPressureExtraInfo) {
        this.designPressureExtraInfo = designPressureExtraInfo;
    }

    /**
     * 设定设计压力附加信息。
     *
     * @param designPressureExtraInfo 设计压力附加信息
     */
    public void setDesignPressureExtraInfo(String designPressureExtraInfo) {
        if (StringUtils.isEmpty(designPressureExtraInfo)) {
            return;
        }
        this.designPressureExtraInfo = PressureExtraInfo.valueOf(designPressureExtraInfo);
    }

    /**
     * 取得是否已被删除的标记。
     *
     * @return 是否已被删除
     */
    public boolean getDeleted() {
        return deleted;
    }

    /**
     * 设置是否已被删除的标记。
     *
     * @param deleted 是否已被删除
     */
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /**
     * 取得更新版本。
     *
     * @return 更新版本
     */
    public long getVersion() {
        return version;
    }

    /**
     * 设置更新版本。
     *
     * @param version 更新版本
     */
    public void setVersion(long version) {
        this.version = version;
    }

    /**
     * 取得状态。
     *
     * @return 状态
     */
    public EntityStatus getStatus() {
        return status;
    }

    /**
     * 设置状态。
     *
     * @param status 状态
     */
    public void setStatus(EntityStatus status) {
        this.status = status;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProcessLineNo() {
        return processLineNo;
    }

    public void setProcessLineNo(String processLineNo) {
        this.processLineNo = processLineNo;
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
    }
}
