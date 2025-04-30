package com.ose.tasks.entity.wbs.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.tasks.dto.numeric.NDERatioDTO;
import com.ose.tasks.dto.numeric.PressureDTO;
import com.ose.tasks.dto.numeric.TemperatureDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.vo.PressureExtraInfo;
import com.ose.tasks.vo.qc.NDEType;
import com.ose.vo.unit.LengthUnit;
import com.ose.vo.unit.PressureUnit;
import com.ose.vo.unit.TemperatureUnit;
import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 管线实体数据实体基类。
 */
@MappedSuperclass
public class LineEntityBase extends WBSEntityBase {

    private static final long serialVersionUID = -426367219257910579L;

    @Schema(description = "工艺系统代码")
    @Column
    private String processSystemNo;

    @Schema(description = "版本号")
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

    @Schema(description = "设计压力附加信息")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private PressureExtraInfo designPressureExtraInfo;

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

    @Schema(description = "操作压力表示值")
    @Column
    private String operatePressureText;

    @Schema(description = "操作压力单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private PressureUnit operatePressureUnit;

    @Schema(description = "操作压力")
    @Column
    private Double operatePressure;

    @Schema(description = "操作温度表示值")
    @Column
    private String operateTemperatureText;

    @Schema(description = "操作温度单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private TemperatureUnit operateTemperatureUnit;

    @Schema(description = "操作温度")
    @Column
    private Double operateTemperature;

    @Schema(description = "保温类型代码")
    @Column
    private String insulationCode;

    @Schema(description = "保温厚度表示值")
    @Column
    private String insulationThicknessText;

    @Schema(description = "保温厚度单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private LengthUnit insulationThicknessUnit;

    @Schema(description = "保温厚度")
    @Column
    private Float insulationThickness;

    @Schema(description = "试压介质")
    @Column
    private String pressureTestMedium;

    @Schema(description = "试验压力表示值")
    @Column
    private String testPressureText;

    @Schema(description = "试验压力单位")
    @Column(length = 32)
    @Enumerated(EnumType.STRING)
    private PressureUnit testPressureUnit;

    @Schema(description = "试验压力")
    @Column
    private Double testPressure;

    @Schema(description = "油漆类型")
    @Column
    private String paintingCode;

    @Schema(description = "管道级别")
    @Column
    private String pipeGrade;

    @Schema(description = "HT 代码")
    @Column
    private String heatTracingCode;

    @Schema(description = "ASME 类型")
    @Column
    private String asmeCategory;

    @Schema(description = "P&ID 图纸")
    @Column
    private String pidDrawing;

    @Schema(description = "无损探伤类型")
    @Column(length = 10)
    @Enumerated(EnumType.STRING)
    private NDEType nde;

    @Schema(description = "无损探伤抽检百分比")
    @Column
    private Integer ndeRatio;

    @Schema(description = "是否套管")
    @Column
    private Boolean jacketPipe;

    @Schema(description = "是否执行焊接后热处理")
    @Column
    private Boolean pwht;

    @Schema(description = "材质分析抽取比例")
    @Column
    private Integer pmiRatio;

    @Schema(description = "最新管道核查测试结果 ID")
    @Column
    private Long lineCheckId;

    @Schema(description = "最新盲板预制测试结果 ID")
    @Column
    private Long blindFabricateId;

    @Schema(description = "最新盲板安装测试结果 ID")
    @Column
    private Long blindInstallId;

    @Schema(description = "最新试压结果 ID")
    @Column
    private Long pressureTestId;

    @Schema(description = "最新盲板拆除结果 ID")
    @Column
    private Long blindRemoveId;

    @Schema(description = "最新吹扫结果 ID")
    @Column
    private Long blowId;

    @Schema(description = "最新串油结果 ID")
    @Column
    private Long oilFlushId;

    @Schema(description = "最新串氺结果 ID")
    @Column
    private Long waterFlushId;

    @Schema(description = "最新保温预制结果 ID")
    @Column
    private Long shieldFabricateId;

    @Schema(description = "最新保温安装结果 ID")
    @Column
    private Long fillingInstallId;

    @Schema(description = "最新保温外层安装结果 ID")
    @Column
    private Long shieldInstallId;

    @Schema(description = "批量删除标记")
    @Column
    private String remarks;

    @Schema(description = "备注")
    @Column
    private String remarks2;

    @Schema(description = "内部清洁")
    @Column
    private String internalMechanicalCleaning;

    @Schema(description = "外场管附件已领用")
    @Column
    private Boolean isUsedInFieldComponent = false;

    @Schema(description = "试压包号")
    @Column
    private String testPackageNo;

    @Schema(description = "清洁包号")
    @Column
    private String cleanPackageNo;

    @Schema(description = "父级实体id")
    @Column
    private Long parentEntityId;

    @Schema(description = "父级实体编号")
    @Column
    private String parentEntityNo;

    @Schema(description = "父级实体编号")
    @Column
    private String type;

    @Schema(description = "父级实体编号")
    @Column
    private String stage;

    // 实体子类型
    @Schema(description = "sub entity type")
    @Column
    private String entitySubType;

    @Schema(description = "子系统号")
    @Column
    private String subSystemNo;

    @Schema(description = "二维码")
    @Column
    private String qrCode;

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    @Override
    public String getEntitySubType() {
        return this.entitySubType;
    }

    public String getInternalMechanicalCleaning() {
        return internalMechanicalCleaning;
    }

    public void setInternalMechanicalCleaning(String internalMechanicalCleaning) {
        this.internalMechanicalCleaning = internalMechanicalCleaning;
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

    @JsonCreator
    public LineEntityBase() {
        this(null);
    }

    public LineEntityBase(Project project) {
        super(project);
        setEntityType("ISO");
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    @JsonGetter
    public String getNpsText() {
        return npsText;
    }

    @JsonSetter
    public void setNpsText(String npsText) {
        this.npsText = npsText;
    }

    @JsonGetter
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

    public Double getNps() {
        return nps;
    }

    public void setNps(Double nps) {
        this.nps = nps;
    }

    /**
     * 设定nps值。
     *
     * @param nps nps值
     */
    @JsonSetter
    public void setNps(String nps) {
        this.npsText = nps;
        LengthDTO lengthDTO = new LengthDTO(this.npsUnit, nps);
        this.npsUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.nps = mm.divide(
            BigDecimal.valueOf(LengthDTO.EQUIVALENCES.get("inches")), 3, RoundingMode.CEILING).doubleValue();
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
        this.designPressureExtraInfo = pressureDTO.getExtraInfo();
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

    public String getInsulationCode() {
        return insulationCode;
    }

    public void setInsulationCode(String insulationCode) {
        this.insulationCode = insulationCode;
    }

    public String getInsulationThicknessText() {
        return insulationThicknessText;
    }

    public void setInsulationThicknessText(String insulationThicknessText) {
        this.insulationThicknessText = insulationThicknessText;
    }

    public LengthUnit getInsulationThicknessUnit() {
        return insulationThicknessUnit;
    }

    @JsonSetter
    public void setInsulationThicknessUnit(LengthUnit insulationThicknessUnit) {
        this.insulationThicknessUnit = insulationThicknessUnit;
    }

    public void setInsulationThicknessUnit(String insulationThicknessUnit) {
        this.insulationThicknessUnit = LengthUnit.getByName(insulationThicknessUnit);
    }

    public Float getInsulationThickness() {
        return insulationThickness;
    }

    public void setInsulationThickness(Float insulationThickness) {
        this.insulationThickness = insulationThickness;
    }

    /**
     * 设定保温厚度。
     *
     * @param insulationThickness 保温厚度
     */
    @JsonSetter
    public void setInsulationThickness(String insulationThickness) {
        this.insulationThicknessText = insulationThickness;
        LengthDTO lengthDTO = new LengthDTO(this.insulationThicknessUnit, insulationThickness);
        this.insulationThicknessUnit = lengthDTO.getUnit();
        this.insulationThickness = lengthDTO.getMillimeters().floatValue();
    }

    public String getPressureTestMedium() {
        return pressureTestMedium;
    }

    public void setPressureTestMedium(String pressureTestMedium) {
        this.pressureTestMedium = pressureTestMedium;
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

    @Override
    public String getEntityBusinessType() {
        return "ISO";
    }

    public Long getLineCheckId() {
        return lineCheckId;
    }

    public void setLineCheckId(Long lineCheckId) {
        this.lineCheckId = lineCheckId;
    }

    public Long getBlindFabricateId() {
        return blindFabricateId;
    }

    public void setBlindFabricateId(Long blindFabricateId) {
        this.blindFabricateId = blindFabricateId;
    }

    public Long getBlindInstallId() {
        return blindInstallId;
    }

    public void setBlindInstallId(Long blindInstallId) {
        this.blindInstallId = blindInstallId;
    }

    public Long getPressureTestId() {
        return pressureTestId;
    }

    public void setPressureTestId(Long pressureTestId) {
        this.pressureTestId = pressureTestId;
    }

    public Long getBlindRemoveId() {
        return blindRemoveId;
    }

    public void setBlindRemoveId(Long blindRemoveId) {
        this.blindRemoveId = blindRemoveId;
    }

    public Long getBlowId() {
        return blowId;
    }

    public void setBlowId(Long blowId) {
        this.blowId = blowId;
    }

    public Long getOilFlushId() {
        return oilFlushId;
    }

    public void setOilFlushId(Long oilFlushId) {
        this.oilFlushId = oilFlushId;
    }

    public Long getWaterFlushId() {
        return waterFlushId;
    }

    public void setWaterFlushId(Long waterFlushId) {
        this.waterFlushId = waterFlushId;
    }

    public Long getShieldFabricateId() {
        return shieldFabricateId;
    }

    public void setShieldFabricateId(Long shieldFabricateId) {
        this.shieldFabricateId = shieldFabricateId;
    }

    public Long getFillingInstallId() {
        return fillingInstallId;
    }

    public void setFillingInstallId(Long fillingInstallId) {
        this.fillingInstallId = fillingInstallId;
    }

    public Long getShieldInstallId() {
        return shieldInstallId;
    }

    public void setShieldInstallId(Long shieldInstallId) {
        this.shieldInstallId = shieldInstallId;
    }

    public String getProcessSystemNo() {
        return processSystemNo;
    }

    public void setProcessSystemNo(String processSystemNo) {
        this.processSystemNo = processSystemNo;
    }

    public Boolean getUsedInFieldComponent() {
        return isUsedInFieldComponent;
    }

    public void setUsedInFieldComponent(Boolean usedInFieldComponent) {
        isUsedInFieldComponent = usedInFieldComponent;
    }

    public String getTestPackageNo() {
        return testPackageNo;
    }

    public void setTestPackageNo(String testPackageNo) {
        this.testPackageNo = testPackageNo;
    }

    public String getCleanPackageNo() {
        return cleanPackageNo;
    }

    public void setCleanPackageNo(String cleanPackageNo) {
        this.cleanPackageNo = cleanPackageNo;
    }

    public String getSubSystemNo() {
        return subSystemNo;
    }

    public void setSubSystemNo(String subSystemNo) {
        this.subSystemNo = subSystemNo;
    }

    public Long getParentEntityId() {
        return parentEntityId;
    }

    public void setParentEntityId(Long parentEntityId) {
        this.parentEntityId = parentEntityId;
    }

    public String getParentEntityNo() {
        return parentEntityNo;
    }

    public void setParentEntityNo(String parentEntityNo) {
        this.parentEntityNo = parentEntityNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }
}
