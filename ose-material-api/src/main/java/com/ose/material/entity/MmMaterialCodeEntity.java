package com.ose.material.entity;

import com.ose.entity.BaseVersionedBizEntity;
import com.ose.material.vo.MaterialOrganizationType;
import com.ose.material.vo.QrCodeType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;


/**
 * 材料编码
 */
@Entity
@Table(name = "mm_material_code",
    indexes = {
        @Index(columnList = "companyId,materialOrganizationType,status"),
        @Index(columnList = "orgId,projectId,materialOrganizationType,status"),
        @Index(columnList = "orgId,projectId,no,materialOrganizationType,status"),
        @Index(columnList = "orgId,projectId,no,status")
    })
public class MmMaterialCodeEntity extends BaseVersionedBizEntity {

    private static final long serialVersionUID = -6541878432287904822L;

    @Schema(description ="组织ID")
    @Column
    public Long orgId;

    @Schema(description ="项目ID")
    @Column
    public Long projectId;

    @Schema(description ="公司ID")
    @Column
    public Long companyId;

    @Schema(description ="编号")
    @Column
    public String no;

    @Schema(description ="流水号")
    private Integer seqNumber;

    @Schema(description ="材料编码区分类型（公司、项目）")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    public MaterialOrganizationType materialOrganizationType;

    @Schema(description ="材料编码分类ID")
    @Column
    public Long mmMaterialCodeTypeId;

    @Schema(description ="材料编码分类名")
    @Column
    public String mmMaterialCodeTypeName;

    @Schema(description ="ident码")
    @Column
    public String identCode;

    @Schema(description ="材料分类")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    public QrCodeType qrCodeType = QrCodeType.EMPTY;

    @Schema(description ="推荐材料分类")
    @Column(nullable = false, length = 16)
    @Enumerated(EnumType.STRING)
    public QrCodeType recommendedQrCodeType = QrCodeType.EMPTY;

    @Schema(description ="材料描述")
    @Column
    public String description;

    @Schema(description ="材料规格")
    @Column
    public String materialSpec;

    @Schema(description ="材质")
    @Column
    public String materialQuality;

    @Schema(description ="单位")
    @Column
    public String unit;

    @Schema(description ="是否需要规格")
    @Column
    public Boolean needSpec ;

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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Integer getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(Integer seqNumber) {
        this.seqNumber = seqNumber;
    }

    public MaterialOrganizationType getMaterialOrganizationType() {
        return materialOrganizationType;
    }

    public void setMaterialOrganizationType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public Long getMmMaterialCodeTypeId() {
        return mmMaterialCodeTypeId;
    }

    public void setMmMaterialCodeTypeId(Long mmMaterialCodeTypeId) {
        this.mmMaterialCodeTypeId = mmMaterialCodeTypeId;
    }

    public String getMmMaterialCodeTypeName() {
        return mmMaterialCodeTypeName;
    }

    public void setMmMaterialCodeTypeName(String mmMaterialCodeTypeName) {
        this.mmMaterialCodeTypeName = mmMaterialCodeTypeName;
    }

    public String getIdentCode() {
        return identCode;
    }

    public void setIdentCode(String identCode) {
        this.identCode = identCode;
    }

    public QrCodeType getQrCodeType() {
        return qrCodeType;
    }

    public void setQrCodeType(QrCodeType qrCodeType) {
        this.qrCodeType = qrCodeType;
    }

    public QrCodeType getRecommendedQrCodeType() {
        return recommendedQrCodeType;
    }

    public void setRecommendedQrCodeType(QrCodeType recommendedQrCodeType) {
        this.recommendedQrCodeType = recommendedQrCodeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMaterialSpec() {
        return materialSpec;
    }

    public void setMaterialSpec(String materialSpec) {
        this.materialSpec = materialSpec;
    }

    public String getMaterialQuality() {
        return materialQuality;
    }

    public void setMaterialQuality(String materialQuality) {
        this.materialQuality = materialQuality;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Boolean getNeedSpec() {
        return needSpec;
    }

    public void setNeedSpec(Boolean needSpec) {
        this.needSpec = needSpec;
    }
}
