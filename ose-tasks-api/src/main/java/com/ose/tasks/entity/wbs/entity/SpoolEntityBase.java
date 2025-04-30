package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.vo.unit.*;
import com.ose.tasks.dto.numeric.*;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.material.NestGateWay;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 单管实体数据实体。
 */
@MappedSuperclass
public class SpoolEntityBase extends WBSEntityBase {

    private static final long serialVersionUID = -6001341405920108774L;

    @Schema(description = "父级工作包号")
    @Column
    private String workPackageNo;

    @Schema(description = "所属管线实体 ID")
    @Column
    private Long isoEntityId;

    @Schema(description = "单管简写代码")
    @Column
    private String shortCode;

    @Schema(description = "页码")
    @Column(nullable = false)
    private Integer sheetNo;

    @Schema(description = "总页数")
    @Column(nullable = false)
    private Integer sheetTotal;

    @Schema(description = "版次")
    @Column(nullable = false)
    private String revision;

    @Schema(description = "NPS 表示值")
    @Column
    private String npsText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit npsUnit;

    @Schema(description = "NPS")
    @Column(nullable = false)
    private Double nps;

    @Schema(description = "length 表示值")
    @Column
    private String lengthText;

    @Schema(description = "NPS 单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit lengthUnit;

    @Schema(description = "length")
    @Column(nullable = false)
    private Double length;

    @Schema(description = "材质")
    @Column(nullable = false)
    private String material;

    @Schema(description = "重量表示值")
    @Column
    private String weightText;

    @Schema(description = "重量单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    @Schema(description = "重量")
    @Column
    private Double weight;

    @Schema(description = "油漆面积表示值")
    @Column
    private String paintingAreaText;

    @Schema(description = "油漆面积单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private AreaUnit paintingAreaUnit;

    @Schema(description = "油漆面积")
    @Column
    private Double paintingArea;

    @Schema(description = "油漆代码")
    @Column
    private String paintingCode;

    @Schema(description = "是否表面处理")
    @Column
    private Boolean surfaceTreatment = false;

    @Schema(description = "是否需要试压")
    @Column
    private Boolean pressureTestRequired = false;

    @Schema(description = "所属管线实体编号")
    @Column
    private String isoNo;

    @Schema(description = "单管二维码")
    @Column
    private String qrCode;


    @Schema(description = "工艺系统代码")
    @Column
    private String processSystemNo;

    @Schema(description = "流体")
    @Column
    private String fluid;

    @Schema(description = "管道等级")
    @Column
    private String pipeClass;

    @Schema(description = "设计压力表示值")
    @Column
    private String designPressureText;

    @Schema(description = "设计压力单位")
    @Column
    @Enumerated(EnumType.STRING)
    private PressureUnit designPressureUnit;

    @Schema(description = "设计压力")
    @Column
    private Double designPressure;

    @Schema(description = "设计温度表示值")
    @Column
    private String designTemperatureText;

    @Schema(description = "设计温度单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private TemperatureUnit designTemperatureUnit;

    @Schema(description = "设计温度")
    @Column
    private Double designTemperature;

    @Schema(description = "工作压力表示值")
    @Column
    private String operatePressureText;

    @Schema(description = "工作压力单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private PressureUnit operatePressureUnit;

    @Schema(description = "工作压力")
    @Column
    private Double operatePressure;

    @Schema(description = "工作温度表示值")
    @Column
    private String operateTemperatureText;

    @Schema(description = "工作温度单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private TemperatureUnit operateTemperatureUnit;

    @Schema(description = "工作温度")
    @Column
    private Double operateTemperature;

    @Schema(description = "保温类型代码")
    @Column
    private String insulationCode;

    @Schema(description = "试压介质")
    @Column
    private String pressureTestMedium;

    @Schema(description = "HT 代码")
    @Column
    private String heatTracingCode;

    @Schema(description = "P&ID 图纸")
    @Column
    private String pidDrawing;

    @Schema(description = "批量删除标记")
    @Column
    private String remarks2;

    @Schema(description = "内部清洁")
    @Column
    private String internalMechanicalCleaning;

    @Schema(description = "管材已领用")
    @Column
    private Boolean isUsedInPipe = false;

    @Schema(description = "内场管附件已领用")
    @Column
    private Boolean isUsedInShopComponent = false;

    @Schema(description = "套料状态")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    private NestGateWay nestGateWay = NestGateWay.NONE;

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

    @JsonCreator
    public SpoolEntityBase() {
        this(null);
    }

    public SpoolEntityBase(Project project) {
        super(project);
        setEntityType("SPOOL");
    }

    public Long getIsoEntityId() {
        return isoEntityId;
    }

    public void setIsoEntityId(Long isoEntityId) {
        this.isoEntityId = isoEntityId;
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

    /**
     * 设定nps值。
     *
     * @param nps nps值
     */
    public void setNps(String nps) {
        this.npsText = nps;
        LengthDTO lengthDTO = new LengthDTO(this.npsUnit, nps);
        this.npsUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.nps = mm.divide(
            BigDecimal.valueOf(LengthDTO.EQUIVALENCES.get("inches")), 3, RoundingMode.CEILING).doubleValue();
    }

    public void setLength(String length) {
        this.lengthText = length;
        LengthDTO lengthDTO = new LengthDTO(this.lengthUnit, length);
        this.lengthUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.length = mm.divide(
            BigDecimal.valueOf(LengthDTO.EQUIVALENCES.get("inches")), 3, RoundingMode.CEILING).doubleValue();
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

    /**
     * 设定重量。
     *
     * @param weight 重量
     */
    public void setWeight(String weight) {
        WeightDTO weightDTO = new WeightDTO(this.weightUnit, weight);
        this.weightText = weight;
        this.weight = weightDTO.getValue();
        this.weightUnit = weightDTO.getUnit();
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

    /**
     * 设定油漆面积。
     *
     * @param paintingArea 油漆面积
     */
    public void setPaintingArea(String paintingArea) {
        AreaDTO areaDTO = new AreaDTO(this.paintingAreaUnit, paintingArea);
        this.paintingAreaText = paintingArea;
        this.paintingArea = areaDTO.getValue();
        this.paintingAreaUnit = areaDTO.getUnit();
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

    @Override
    public String getEntitySubType() {
        return "SPOOL";
    }

    @Override
    public String getEntityBusinessType() {
        return "SPOOL";
    }

    public String getIsoNo() {
        return isoNo;
    }

    public void setIsoNo(String isoNo) {
        this.isoNo = isoNo;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getProcessSystemNo() {
        return processSystemNo;
    }

    public void setProcessSystemNo(String processSystemNo) {
        this.processSystemNo = processSystemNo;
    }

    public Boolean getUsedInPipe() {
        return isUsedInPipe;
    }

    public void setUsedInPipe(Boolean usedInPipe) {
        isUsedInPipe = usedInPipe;
    }

    public Boolean getUsedInShopComponent() {
        return isUsedInShopComponent;
    }

    public void setUsedInShopComponent(Boolean usedInShopComponent) {
        isUsedInShopComponent = usedInShopComponent;
    }

    public NestGateWay getNestGateWay() {
        return nestGateWay;
    }

    public void setNestGateWay(NestGateWay nestGateWay) {
        this.nestGateWay = nestGateWay;
    }

    public String getWorkPackageNo() {
        return workPackageNo;
    }

    public void setWorkPackageNo(String workPackageNo) {
        this.workPackageNo = workPackageNo;
    }
}
