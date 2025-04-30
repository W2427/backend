package com.ose.tasks.entity.wbs.structureEntity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.ose.tasks.dto.numeric.LengthDTO;
import com.ose.tasks.dto.numeric.WeightDTO;
import com.ose.tasks.entity.Project;
import com.ose.tasks.entity.wbs.entity.WBSEntityBase;
import com.ose.vo.unit.LengthUnit;
import com.ose.vo.unit.WeightUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;

import java.math.BigDecimal;

/**
 * 结构Panel实体数据实体基类。 WP3分类
 */
@MappedSuperclass
public class Wp03EntityBase extends WBSEntityBase {


    private static final long serialVersionUID = 7005885566538580049L;

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

    @Schema(description = "工作项代码")
    @Column
    private String workClass;

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

    @Schema(description = "油漆代码")
    @Column
    private String paintCode;

    @Schema(description = "tag description")
    @Column
    private String tagDescription;

    // 实体业务类型
    @Schema(description = "business Type")
    @Column
    private String businessType;

    // 实体业务类型ID
    @Schema(description = "business Type Id")
    @Column
    private String businessTypeId;

    @Schema(description = "是否为新节点，用于更新 Wp01/02/03/04等节点信息")
    @Column
    private boolean isNew = false;

    @Schema(description = "二维码")
    @Column
    private String qrCode;

    @JsonCreator
    public Wp03EntityBase() {
        this(null);
    }

    public Wp03EntityBase(Project project) {
        super(project);
        setEntityType("WP03");
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
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

    @JsonSetter
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

    @JsonSetter
    public void setLength(String length) {
        this.lengthText = length;
        LengthDTO lengthDTO = new LengthDTO(this.lengthUnit, length);
        this.lengthUnit = lengthDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(lengthDTO.getMillimeters());
        this.length = mm.doubleValue();
    }

    public String getWidthText() {
        return widthText;
    }

    public void setWidthText(String widthText) {
        this.widthText = widthText;
    }

    public LengthUnit getWidthUnit() {
        return widthUnit;
    }

    @JsonSetter
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

    @JsonSetter
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

    @JsonSetter
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

    @JsonSetter
    public void setHeight(String height) {
        this.heightText = height;
        LengthDTO heightDTO = new LengthDTO(this.heightUnit, height);
        this.heightUnit = heightDTO.getUnit();
        BigDecimal mm = BigDecimal.valueOf(heightDTO.getMillimeters());
        this.height = mm.doubleValue();
    }


    @JsonSetter

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }


    public WeightUnit getWeightUnit() {
        return weightUnit;
    }

//    public String getWp01No() {
//        return wp01No;
//    }
//
//    public void setWp01No(String wp01No) {
//        this.wp01No = wp01No;
//    }
//
//    public Long getWp01Id() {
//        return wp01Id;
//    }
//
//    public void setWp01Id(Long wp01Id) {
//        this.wp01Id = wp01Id;
//    }
//
//    public String getWp02No() {
//        return wp02No;
//    }
//
//    public void setWp02No(String wp02No) {
//        this.wp02No = wp02No;
//    }
//
//    public Long getWp02Id() {
//        return wp02Id;
//    }
//
//    public void setWp02Id(Long wp02Id) {
//        this.wp02Id = wp02Id;
//    }



    @Override
    public String getEntitySubType() {
        return this.entitySubType;
    }

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    @Override
    public String getEntityBusinessType() {
        return "WP03";
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getWorkClass() {
        return workClass;
    }

    public void setWorkClass(String workClass) {
        this.workClass = workClass;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
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

    public String getWorkPackageNo() {
        return workPackageNo;
    }

    public void setWorkPackageNo(String workPackageNo) {
        this.workPackageNo = workPackageNo;
    }

    public String getTagDescription() {
        return tagDescription;
    }

    public void setTagDescription(String tagDescription) {
        this.tagDescription = tagDescription;
    }
}
