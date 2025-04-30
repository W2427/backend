package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.tasks.dto.numeric.WeightDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.vo.unit.LengthUnit;
import com.ose.vo.unit.WeightUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.math.BigDecimal;

/**
 * WP01 Module实体数据实体基类。 WP1 分类
 */
@MappedSuperclass
public class Wp01EntityBase extends WBSEntityBase {

    private static final long serialVersionUID = -211574428142419512L;

    @Schema(description = "父级工作包号")
    @Column
    private String workPackageNo;

    // 实体子类型
    @Schema(description = "sub entity type")
    @Column
    private String entitySubType;

    @Schema(description = "版本号")
    @Column(nullable = false)
    private String revision;

    @Schema(description = "长度 文本")
    @Column
    private String lengthText;

    @Schema(description = "长度 单位")
    @Column(length = 200)
    @Enumerated(EnumType.STRING)
    private LengthUnit lengthUnit;

    @Schema(description = "length")
    @Column(nullable = false)
    private Double length;

    @Schema(description = "宽度 文本")
    @Column
    private String widthText;

    @Schema(description = "宽度 单位")
    @Column(length = 200)
    @Enumerated(EnumType.STRING)
    private LengthUnit widthUnit;

    @Schema(description = "width")
    @Column(nullable = false)
    private Double width;

    @Schema(description = "高度 文本")
    @Column
    private String heightText;

    @Schema(description = "高度 单位")
    @Column(length = 200)
    @Enumerated(EnumType.STRING)
    private LengthUnit heightUnit;

    @Schema(description = "height")
    @Column(nullable = false)
    private Double height;

    @Schema(description = "重量 文本")
    @Column
    private String weightText;

    @Schema(description = "重量 单位")
    @Column(length = 200)
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    @Schema(description = "weight")
    @Column(nullable = false)
    private Double weight;

    // 实体业务类型
    @Schema(description = "business Type")
    @Column
    private String businessType;

//    // 实体业务类型ID
    @Schema(description = "business Type Id")
    @Column
    private Long businessTypeId;

    @Schema(description = "是否已指定任务包")
    @Transient
    private Boolean tpAssigned;

    @Schema(description = "是否为新节点，用于更新 Wp01/02/03/04等节点信息")
    @Column
    private boolean isNew = false;

    @Schema(description = "二维码")
    @Column
    private String qrCode;

    @Schema(description = "发运批次号")
    @Column
    private String shipmentNo;

    @JsonCreator
    public Wp01EntityBase() {
        this(null);
    }

    public Wp01EntityBase(Project project) {
        super(project);
        setEntityType("WP01");
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getWeightText() {
        return weightText;
    }

    public void setWeightText(String weightText) {
        this.weightText = weightText;
    }

    public WeightUnit getUnitWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = WeightUnit.getByName(weightUnit);
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weightText = weight;
        WeightDTO weightDTO = new WeightDTO(this.weightUnit, weight);
        this.weightUnit = weightDTO.getUnit();
        BigDecimal kg = BigDecimal.valueOf(weightDTO.getKgs());
        this.weight = kg.doubleValue();
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getLengthText() {
        return lengthText;
    }

    public void setLengthText(String lengthText) {
        this.lengthText = lengthText;
    }


    public LengthUnit getLengthUnit() {
        return lengthUnit;
    }

    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public void setLengthUnit(String lengthUnit) {
        this.lengthUnit = LengthUnit.getByName(lengthUnit);
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public void setLength(String length) {
        this.lengthText = length;
        LengthDTO lengthDTO = new LengthDTO(this.lengthUnit, length);
        this.lengthUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.length = mm.doubleValue();
    }

    //------------------------------------------------
    public String getWidthText() {
        return widthText;
    }

    public void setWidthText(String widthText) {
        this.widthText = widthText;
    }

    public LengthUnit getWidthUnit() {
        return widthUnit;
    }

    public void setWidthUnit(LengthUnit widthUnit) {
        this.widthUnit = widthUnit;
    }

    public void setWidthUnit(String widthUnit) {
        this.widthUnit = LengthUnit.getByName(widthUnit);
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public void setWidth(String width) {
        this.widthText = width;
        LengthDTO widthDTO = new LengthDTO(this.widthUnit, width);
        this.widthUnit = widthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(widthDTO.getMillimeters());
        this.width = mm.doubleValue();
    }

    public String getHeightText() {
        return heightText;
    }

    public void setHeightText(String heightText) {
        this.heightText = heightText;
    }

    public LengthUnit getHeightUnit() {
        return heightUnit;
    }

    public void setHeightUnit(LengthUnit heightUnit) {
        this.heightUnit = heightUnit;
    }

    public void setHeightUnit(String heightUnit) {
        this.heightUnit = LengthUnit.getByName(heightUnit);
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public void setHeight(String height) {
        this.heightText = height;
        LengthDTO heightDTO = new LengthDTO(this.heightUnit, height);
        this.heightUnit = heightDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(heightDTO.getMillimeters());
        this.height = mm.doubleValue();
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public WeightUnit getWeightUnit() {
        return weightUnit;
    }


    public String getEntitySubType() {
        return this.entitySubType;
    }

    public String getEntityBusinessType() {
        return "WP01";
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public Long getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(Long businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public Boolean getTpAssigned() {
        return tpAssigned;
    }

    public void setTpAssigned(Boolean tpAssigned) {
        this.tpAssigned = tpAssigned;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getShipmentNo() {
        return shipmentNo;
    }

    public void setShipmentNo(String shipmentNo) {
        this.shipmentNo = shipmentNo;
    }

    public String getWorkPackageNo() {
        return workPackageNo;
    }

    public void setWorkPackageNo(String workPackageNo) {
        this.workPackageNo = workPackageNo;
    }
}

