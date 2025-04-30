package com.ose.tasks.dto.wbs;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.dto.BaseDTO;
import com.ose.tasks.dto.numeric.PressureDTO;
import com.ose.tasks.dto.numeric.TemperatureDTO;
import com.ose.vo.EntityStatus;
import com.ose.vo.unit.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;


/**
 * Spool实体数据传输对象。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpoolEntryUpdateDTO extends BaseDTO {

    private static final long serialVersionUID = 3753812239306672043L;

    @Schema(description = "版本号")
    private String revision;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "单管简写代码")
    private String shortCode;

    @Schema(description = "页码")
    private Integer sheetNo;

    @Schema(description = "总页数")
    private Integer sheetTotal;

    @Schema(description = "NPS 表示值")
    private String npsText;

    @Schema(description = "NPS 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    private Double nps;

    @Schema(description = "length 表示值")
    private String lengthText;

    @Schema(description = "length 单位")
    @Enumerated(EnumType.STRING)
    private LengthUnit lengthUnit;

    @Schema(description = "length")
    private Double length;

    @Schema(description = "材质")
    private String material;

    @Schema(description = "重量表示值")
    private String weightText;

    @Schema(description = "重量单位")
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    @Schema(description = "重量")
    private Double weight;

    @Schema(description = "油漆面积表示值")
    private String paintingAreaText;

    @Schema(description = "油漆面积单位")
    @Enumerated(EnumType.STRING)
    private AreaUnit paintingAreaUnit;

    @Schema(description = "油漆面积")
    private Double paintingArea;

    @Schema(description = "油漆代码")
    private String paintingCode;

    @Schema(description = "是否表面处理")
    private Boolean surfaceTreatment;

    @Schema(description = "是否需要试压")
    private Boolean pressureTestRequired;

    @Schema(description = "系统号")
    private String systemNo;

    @Schema(description = "流体")
    private String fluid;

    @Schema(description = "管道等级")
    private String pipeClass;

    @Schema(description = "设计压力表示值")
    private String designPressureText;

    @Schema(description = "设计压力单位")
    @Enumerated(EnumType.STRING)
    private PressureUnit designPressureUnit;

    @Schema(description = "设计压力")
    private Double designPressure;

    @Schema(description = "设计温度表示值")
    private String designTemperatureText;

    @Schema(description = "设计温度单位")
    @Enumerated(EnumType.STRING)
    private TemperatureUnit designTemperatureUnit;

    @Schema(description = "设计温度")
    private Double designTemperature;

    @Schema(description = "操作压力表示值")
    private String operatePressureText;

    @Schema(description = "操作压力单位")
    @Enumerated(EnumType.STRING)
    private PressureUnit operatePressureUnit;

    @Schema(description = "操作压力")
    private Double operatePressure;

    @Schema(description = "操作温度表示值")
    private String operateTemperatureText;

    @Schema(description = "操作温度单位")
    @Enumerated(EnumType.STRING)
    private TemperatureUnit operateTemperatureUnit;

    @Schema(description = "操作温度")
    private Double operateTemperature;

    @Schema(description = "保温类型代码")
    private String insulationCode;

    @Schema(description = "试压介质")
    private String pressureTestMedium;

    @Schema(description = "HT 代码")
    private String heatTracingCode;

    @Schema(description = "P&ID 图纸")
    private String pidDrawing;

    @Schema(description = "批量删除标记")
    private String remarks2;

    @Schema(description = "内部清洁")
    private String internalMechanicalCleaning;

    // 数据实体状态
    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    // 是否已被删除
    private boolean deleted = false;

    // 更新版本（手动乐观锁）
    private long version;

    @JsonCreator
    public SpoolEntryUpdateDTO() {
    }

    public String getDesignPressureText() {
        return designPressureText;
    }

    public void setDesignPressureText(String designPressureText) {
        this.designPressureText = designPressureText;
    }

    @JsonGetter
    public PressureUnit getDesignPressureUnit() {
        return designPressureUnit;
    }

    @JsonSetter
    public void setDesignPressureUnit(PressureUnit designPressureUnit) {
        this.designPressureUnit = designPressureUnit;
    }

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

        this.designPressureText = designPressure;
        this.designPressure = pressureDTO.getValue();
        this.designPressureUnit = pressureDTO.getUnit();
    }

    public String getDesignTemperatureText() {
        return designTemperatureText;
    }

    public void setDesignTemperatureText(String designTemperatureText) {
        this.designTemperatureText = designTemperatureText;
    }

    public TemperatureUnit getDesignTemperatureUnit() {
        return designTemperatureUnit;
    }

    @JsonSetter
    public void setDesignTemperatureUnit(TemperatureUnit designTemperatureUnit) {
        this.designTemperatureUnit = designTemperatureUnit;
    }

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

        this.designTemperatureText = designTemperature;
        this.designTemperature = temperatureDTO.getValue();
        this.designTemperatureUnit = temperatureDTO.getUnit();
    }

    public String getOperatePressureText() {
        return operatePressureText;
    }

    public void setOperatePressureText(String operatePressureText) {
        this.operatePressureText = operatePressureText;
    }

    @JsonGetter
    public PressureUnit getOperatePressureUnit() {
        return operatePressureUnit;
    }

    @JsonSetter
    public void setOperatePressureUnit(PressureUnit operatePressureUnit) {
        this.operatePressureUnit = operatePressureUnit;
    }

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

        this.operatePressureText = operatePressure;
        this.operatePressure = pressureDTO.getValue();
        this.operatePressureUnit = pressureDTO.getUnit();
    }

    public String getOperateTemperatureText() {
        return operateTemperatureText;
    }

    public void setOperateTemperatureText(String operateTemperatureText) {
        this.operateTemperatureText = operateTemperatureText;
    }

    public TemperatureUnit getOperateTemperatureUnit() {
        return operateTemperatureUnit;
    }

    @JsonSetter
    public void setOperateTemperatureUnit(TemperatureUnit operateTemperatureUnit) {
        this.operateTemperatureUnit = operateTemperatureUnit;
    }

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

        this.operateTemperatureText = operateTemperature;
        this.operateTemperature = temperatureDTO.getValue();
        this.operateTemperatureUnit = temperatureDTO.getUnit();
    }

    public String getSystemNo() {
        return systemNo;
    }

    public void setSystemNo(String systemNo) {
        this.systemNo = systemNo;
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

    public String getInsulationCode() {
        return insulationCode;
    }

    public void setInsulationCode(String insulationCode) {
        this.insulationCode = insulationCode;
    }

    public String getPressureTestMedium() {
        return pressureTestMedium;
    }

    public void setPressureTestMedium(String pressureTestMedium) {
        this.pressureTestMedium = pressureTestMedium;
    }

    public String getHeatTracingCode() {
        return heatTracingCode;
    }

    public void setHeatTracingCode(String heatTracingCode) {
        this.heatTracingCode = heatTracingCode;
    }

    public String getPidDrawing() {
        return pidDrawing;
    }

    public void setPidDrawing(String pidDrawing) {
        this.pidDrawing = pidDrawing;
    }

    public String getRemarks2() {
        return remarks2;
    }

    public void setRemarks2(String remarks2) {
        this.remarks2 = remarks2;
    }

    public String getInternalMechanicalCleaning() {
        return internalMechanicalCleaning;
    }

    public void setInternalMechanicalCleaning(String internalMechanicalCleaning) {
        this.internalMechanicalCleaning = internalMechanicalCleaning;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getShortCode() {
        return shortCode;
    }

    public void setShortCode(String shortCode) {
        this.shortCode = shortCode;
    }

    public Integer getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(Integer sheetNo) {
        this.sheetNo = sheetNo;
    }

    public Integer getSheetTotal() {
        return sheetTotal;
    }

    public void setSheetTotal(Integer sheetTotal) {
        this.sheetTotal = sheetTotal;
    }

    public String getNpsText() {
        return npsText;
    }

    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
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

    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    @JsonSetter
    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    @JsonSetter
    public void setLengthUnit(String lengthUnit) {
        this.lengthUnit = LengthUnit.getByName(lengthUnit);
    }

    public Double getNps() {
        return nps;
    }

    @JsonSetter
    public void setNps(Double nps) {
        this.nps = nps;
    }

    public Double getLength() {
        return length;
    }

    @JsonSetter
    public void setLength(Double length) {
        this.length = length;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getWeightText() {
        return weightText;
    }

    public void setWeightText(String weightText) {
        this.weightText = weightText;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

    @JsonSetter
    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    @JsonSetter
    public void setWeightUnit(String weightUnit) {
        this.weightUnit = WeightUnit.getByName(weightUnit);
    }

    public Double getWeight() {
        return weight;
    }

    @JsonSetter
    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getPaintingAreaText() {
        return paintingAreaText;
    }

    public void setPaintingAreaText(String paintingAreaText) {
        this.paintingAreaText = paintingAreaText;
    }

    public AreaUnit getPaintingAreaUnit() {
        return paintingAreaUnit;
    }

    @JsonSetter
    public void setPaintingAreaUnit(AreaUnit paintingAreaUnit) {
        this.paintingAreaUnit = paintingAreaUnit;
    }

    @JsonSetter
    public void setPaintingAreaUnit(String paintingAreaUnit) {
        this.paintingAreaUnit = AreaUnit.getByName(paintingAreaUnit);
    }

    public Double getPaintingArea() {
        return paintingArea;
    }

    @JsonSetter
    public void setPaintingArea(Double paintingArea) {
        this.paintingArea = paintingArea;
    }

    public String getPaintingCode() {
        return paintingCode;
    }

    public void setPaintingCode(String paintingCode) {
        this.paintingCode = paintingCode;
    }

    public Boolean getSurfaceTreatment() {
        return surfaceTreatment;
    }

    public void setSurfaceTreatment(Boolean surfaceTreatment) {
        this.surfaceTreatment = surfaceTreatment;
    }

    public Boolean getPressureTestRequired() {
        return pressureTestRequired;
    }

    public void setPressureTestRequired(Boolean pressureTestRequired) {
        this.pressureTestRequired = pressureTestRequired;
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

}
