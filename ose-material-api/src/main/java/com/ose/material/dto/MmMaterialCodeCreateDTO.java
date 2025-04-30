package com.ose.material.dto;

import com.ose.dto.PageDTO;
import com.ose.material.entity.MmMaterialCodeEntity;
import com.ose.material.vo.QrCodeType;
import com.ose.material.vo.MaterialOrganizationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

/**
 * 材料编码创建DTO
 */
public class MmMaterialCodeCreateDTO extends PageDTO {

    private static final long serialVersionUID = -412218223930732921L;

    @Schema(description = "材料编码实体")
    public List<MmMaterialCodeEntity> entities;

    @Schema(description = "材料编码类型ID")
    public Long mmMaterialCodeTypeId;

    @Schema(description = "组织ID")
    public Long orgId;

    @Schema(description = "项目ID")
    public Long projectId;

    @Schema(description = "材料编码类型名称")
    private String mmMaterialCodeTypeName;

    @Schema(description = "编号")
    private String no;

    @Schema(description = "材料组织类型")
    private MaterialOrganizationType materialOrganizationType;

    @Schema(description = "材料描述")
    private String description;

    @Schema(description = "二维码类型")
    private QrCodeType qrCodeType = QrCodeType.EMPTY;

    @Schema(description = "推荐二维码类型")
    public QrCodeType recommendedQrCodeType = QrCodeType.EMPTY;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "材料规格")
    private String materialSpec;

    @Schema(description = "材质")
    private String materialQuality;

    @Schema(description = "是否需要规格管理")
    private Boolean needSpec;

    public List<MmMaterialCodeEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<MmMaterialCodeEntity> entities) {
        this.entities = entities;
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

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public MaterialOrganizationType getMaterialOrganizationType() {
        return materialOrganizationType;
    }

    public void setMaterialOrganizationType(MaterialOrganizationType materialOrganizationType) {
        this.materialOrganizationType = materialOrganizationType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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

    public Boolean getNeedSpec() {
        return needSpec;
    }

    public void setNeedSpec(Boolean needSpec) {
        this.needSpec = needSpec;
    }

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
}
