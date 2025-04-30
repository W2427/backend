package com.ose.tasks.entity.material;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.vo.unit.LengthUnit;
import com.ose.vo.unit.WeightUnit;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

/**
 * 材料二维码管理表。
 */
@Entity
@Table(name = "structure_entity_qr_code",
indexes = {
    @Index(columnList = "orgId,projectId,entityId"),
    @Index(columnList = "orgId,projectId,entityNo,deleted")
})
public class StructureEntityQrCode extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -7567223336165872862L;

    @Schema(description = "组织ID")
    private Long orgId;

    @Schema(description = "项目Id")
    private Long projectId;

    @Schema(description = "实体ID")
    private Long entityId;

    @Schema(description = "实体编号")
    private String entityNo;

    @Schema(description = "实体类型")
    @Column(length = 32)

    private String entityType;

    @Schema(description = "版本号")
    private String revision;

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

    @Schema(description = "材料二维码")
    @Column
    private String materialQrCode;

    @Schema(description = "HeatNoId")
    @Column
    private Long heatNoId;

    @Schema(description = "是否套料")
    private Boolean nestedFlag;

    @Schema(description = "材料编码ID")
    private int tagNumberId;

    @Schema(description = "材料编码")
    private String tagNumber;

    @Schema(description = "炉批号")
    private String heatNo;

    @Schema(description = "IDENT编码")
    private String ident;

    @Schema(description = "材料描述")
    @Column(name = "short_desc", length = 500)
    private String shortDesc;

    @Schema(description = "下料程序id")
    private Long programId;

    @Schema(description = "下料程序编号")
    private String programNo;

    @Schema(description = "结构下料单ID")
    private Long cuttingId;

    @Schema(description = "下料单编号")
    private String cuttingNo;

    @Schema(description = "打印状态")
    @Column(nullable = true)
    private Boolean printFlg;

    @Schema(description = "手动创建标识")
    private Boolean isManuallyCreated = false;

    @Schema(description = "下料单编号")
    private String materialCutting;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public Long getEntityId() {
        return entityId;
    }

    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }

    public String getEntityNo() {
        return entityNo;
    }

    public void setEntityNo(String entityNo) {
        this.entityNo = entityNo;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getWorkClass() {
        return workClass;
    }

    public void setWorkClass(String workClass) {
        this.workClass = workClass;
    }

    public String getCostCode() {
        return costCode;
    }

    public void setCostCode(String costCode) {
        this.costCode = costCode;
    }

    public String getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(String memberSize) {
        this.memberSize = memberSize;
    }

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

    public void setLengthUnit(LengthUnit lengthUnit) {
        this.lengthUnit = lengthUnit;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
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

    public void setWidthUnit(LengthUnit widthUnit) {
        this.widthUnit = widthUnit;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
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

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
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

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getPaintCode() {
        return paintCode;
    }

    public void setPaintCode(String paintCode) {
        this.paintCode = paintCode;
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

    public Boolean getNestedFlag() {
        return nestedFlag;
    }

    public void setNestedFlag(Boolean nestedFlag) {
        this.nestedFlag = nestedFlag;
    }

    public int getTagNumberId() {
        return tagNumberId;
    }

    public void setTagNumberId(int tagNumberId) {
        this.tagNumberId = tagNumberId;
    }

    public String getTagNumber() {
        return tagNumber;
    }

    public void setTagNumber(String tagNumber) {
        this.tagNumber = tagNumber;
    }

    public String getHeatNo() {
        return heatNo;
    }

    public void setHeatNo(String heatNo) {
        this.heatNo = heatNo;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getProgramNo() {
        return programNo;
    }

    public void setProgramNo(String programNo) {
        this.programNo = programNo;
    }

    public Long getCuttingId() {
        return cuttingId;
    }

    public void setCuttingId(Long cuttingId) {
        this.cuttingId = cuttingId;
    }

    public String getCuttingNo() {
        return cuttingNo;
    }

    public void setCuttingNo(String cuttingNo) {
        this.cuttingNo = cuttingNo;
    }

    public Boolean getPrintFlg() {
        return printFlg;
    }

    public void setPrintFlg(Boolean printFlg) {
        this.printFlg = printFlg;
    }

    public Boolean getManuallyCreated() {
        return isManuallyCreated;
    }

    public void setManuallyCreated(Boolean manuallyCreated) {
        isManuallyCreated = manuallyCreated;
    }

    public String getMaterialQrCode() {
        return materialQrCode;
    }

    public void setMaterialQrCode(String materialQrCode) {
        this.materialQrCode = materialQrCode;
    }

    public Long getHeatNoId() {
        return heatNoId;
    }

    public void setHeatNoId(Long heatNoId) {
        this.heatNoId = heatNoId;
    }

    public String getMaterialCutting() {
        return materialCutting;
    }

    public void setMaterialCutting(String materialCutting) {
        this.materialCutting = materialCutting;
    }
}
