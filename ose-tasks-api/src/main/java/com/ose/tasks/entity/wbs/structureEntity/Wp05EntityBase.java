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
 * 结构Part实体数据实体基类。 WP5分类
 */
@MappedSuperclass
public class Wp05EntityBase extends WBSEntityBase {

    private static final long serialVersionUID = 4375611261678203304L;

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

//    @Schema(description = "零件数量")
//    @Column(nullable = false)
//    private Integer qty;

    @Schema(description = "工作项代码")
    @Column
    private String workClass;

    @Schema(description = "费用代码")
    @Column
    private String costCode;

    @Schema(description = "部件型号 Member Size")
    @Column(length = 255)
    private String memberSize;

    @Schema(description = "零件材质")
    @Column
    private String material;

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
    @Column(length = 1000)
    @Enumerated(EnumType.STRING)
    private WeightUnit weightUnit;

    @Schema(description = "weight")
    @Column(length = 1000)
    private Double weight;

    @Schema(description = "油漆类型代码")
    @Column
    private String paintCode;

//    @Schema(description = "已套料数量")
//    @Column(nullable = false)
//    private Integer totalNestedQty;
//
//    @Schema(description = "可套料最大量")
//    @Column(nullable = false)
//    private Integer allowedNestQty;
//
//    @Schema(description = "额外零件数量")
//    @Column
//    private Integer entryQty;

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

    @Schema(description = "是否套料")
    private Boolean nestedFlag;

    @Schema(description = "材质")
    @Column()
    private String materialCutting;

    @JsonCreator
    public Wp05EntityBase() {
        this(null);
    }

    public Wp05EntityBase(Project project) {
        super(project);
        setEntityType("WP05");
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

//按照规则生成图纸的编号（管理思路不可行，暂时不用）
//    public void setDwgNo(BpmEntityCategoryType bpmEntityCategoryType, Project project) {
//
//        String wp01_part = wp01No;
//        String wp02_part = "";
//        String wp03_part = "";
//        String wp04_part = "";
//        String wp05_part = "";
//
//        String dwgPattern = bpmEntityCategoryType.getDwgPattern();
//        if (StringUtils.isEmpty(dwgPattern)) {
//            return;
//        }
//
//        if (project.isStructureAssembleParent()) {
//            if (!StringUtils.isEmpty(wp02No)) {
//                wp02_part = wp02No.replaceFirst(StringUtils.escapeExprSpecialWord(wp01No) + "-", "");
//            }
//            if (!StringUtils.isEmpty(wp03No)) {
//                wp03_part = wp03No.replaceFirst(StringUtils.escapeExprSpecialWord(wp02No) + "-", "");
//            }
//            if (!StringUtils.isEmpty(wp04No)) {
//                wp04_part = wp04No.replaceFirst(StringUtils.escapeExprSpecialWord(wp03No) + "-", "");
//            }
//            if (!StringUtils.isEmpty(getNo())) {
//                wp05_part = getNo().replaceFirst(StringUtils.escapeExprSpecialWord(wp04No) + "-", "");
//            }
//
//        } else {
//            wp02_part = wp02No;
//            wp03_part = wp03No;
//            wp04_part = wp04No;
//            wp05_part = getNo();
//
//        }
//
//        //Replace project No
//        dwgPattern.replaceFirst("\\{PROJECT_NO\\}", project.getCode());
//
//        dwgPattern.replaceFirst("\\{WP01_NO\\}", wp01_part);
//        dwgPattern.replaceFirst("\\{WP02_NO\\}", wp02_part);
//        dwgPattern.replaceFirst("\\{WP03_NO\\}", wp03_part);
//        dwgPattern.replaceFirst("\\{WP04_NO\\}", wp04_part);
//        dwgPattern.replaceFirst("\\{WP05_NO\\}", wp05_part);
//        setDwgNo(dwgPattern);
//
//    }

    public String getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(String memberSize) {
        this.memberSize = memberSize;
    }

//    public Integer getQty() {
//        return qty;
//    }
//
//    public void setQty(Integer qty) {
//        this.qty = qty;
//    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

//    public Integer getEntryQty() {
//        return entryQty;
//    }
//
//    public void setEntryQty(Integer entryQty) {
//        this.entryQty = entryQty;
//    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
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

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
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

    public void setEntitySubType(String entitySubType) {
        this.entitySubType = entitySubType;
    }

    @Override
    public String getEntitySubType() {
        return this.entitySubType;
    }

    @Override
    public String getEntityBusinessType() {
        return "WP05";
    }

//    public Integer getTotalNestedQty() {
//        return totalNestedQty;
//    }
//
//    public void setTotalNestedQty(Integer totalNestedQty) {
//        this.totalNestedQty = totalNestedQty;
//    }
//
//    public Integer getAllowedNestQty() {
//        return allowedNestQty;
//    }
//
//    public void setAllowedNestQty(Integer allowedNestQty) {
//        this.allowedNestQty = allowedNestQty;
//    }

    public String getWorkClass() {
        return workClass;
    }

    public void setWorkClass(String workClass) {
        this.workClass = workClass;
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

    public void setWeightUnit(WeightUnit weightUnit) {
        this.weightUnit = weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = WeightUnit.getByName(weightUnit);
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public void setWeight(String weight) {
        this.weightText = weight;
        WeightDTO weightDTO = new WeightDTO(this.weightUnit, weight);
        this.weightUnit = weightDTO.getUnit();
        BigDecimal kg = BigDecimal.valueOf(weightDTO.getKgs());
        this.weight = kg.doubleValue();
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getNestedFlag() {
        return nestedFlag;
    }

    public void setNestedFlag(Boolean nestedFlag) {
        this.nestedFlag = nestedFlag;
    }

    public String getWorkPackageNo() {
        return workPackageNo;
    }

    public void setWorkPackageNo(String workPackageNo) {
        this.workPackageNo = workPackageNo;
    }

    public String getMaterialCutting() {
        return materialCutting;
    }

    public void setMaterialCutting(String materialCutting) {
        this.materialCutting = materialCutting;
    }
}
